-- Agregar orden de compra a insumos 
DELIMITER //

CREATE PROCEDURE ActualizarEstatusCompra (
    IN p_numeroOrden VARCHAR(50) -- Parámetro de entrada para el número de orden
)
BEGIN
    DECLARE v_descripcion JSON;
    DECLARE v_insumo VARCHAR(100);
    DECLARE v_cantidad INT;
    DECLARE v_peso FLOAT;
    DECLARE v_total FLOAT;
    DECLARE v_proveedor_id INT;
    DECLARE v_iter INT DEFAULT 0;
    DECLARE v_insumo_count INT;
    DECLARE v_estatus INT;

    -- Obtener el proveedor_id de la compra y el estatus
    SELECT proveedor_id, estatus INTO v_proveedor_id, v_estatus
    FROM comprasRealizadas
    WHERE numeroOrden = p_numeroOrden;

    -- Verificar si se encontró la compra
    IF v_proveedor_id IS NULL THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No se encontró ninguna compra con el número de orden proporcionado.';
    END IF;

    -- Verificar si el estatus es 1, si es así, marcar como ya registrada y salir
    IF v_estatus = 1 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'La compra ya está registrada.';
    END IF;

    -- Obtener la descripción (en formato JSON) de la compra
    SELECT descripcion INTO v_descripcion
    FROM detalleCompra
    WHERE compra_id = (SELECT id_comprasRealizadas FROM comprasRealizadas WHERE numeroOrden = p_numeroOrden);

    -- Obtener la cantidad de elementos en la descripción JSON
    SET v_insumo_count = JSON_LENGTH(v_descripcion);

    -- Iterar sobre los insumos en la descripción JSON
    WHILE v_iter < v_insumo_count DO
        -- Obtener el insumo, cantidad y peso del JSON
        SET v_insumo = JSON_UNQUOTE(JSON_EXTRACT(v_descripcion, CONCAT('$[', v_iter, '].insumo')));
        SET v_cantidad = JSON_UNQUOTE(JSON_EXTRACT(v_descripcion, CONCAT('$[', v_iter, '].cantidad')));
        SET v_peso = JSON_UNQUOTE(JSON_EXTRACT(v_descripcion, CONCAT('$[', v_iter, '].peso')));

        -- Calcular el total (cantidad * peso)
        SET v_total = v_cantidad * v_peso;

        -- Actualizar los registros correspondientes en la tabla insumos
        UPDATE insumos
        SET cantidad = cantidad + v_cantidad, 
            total = total + v_total
        WHERE nombreInsumo = v_insumo 
          AND id_proveedor = v_proveedor_id;

        -- Incrementar el iterador para el siguiente insumo
        SET v_iter = v_iter + 1;
    END WHILE;

    -- Actualizar el estatus de la compra a 1
    UPDATE comprasRealizadas
    SET estatus = 1
    WHERE numeroOrden = p_numeroOrden;

    -- Opcional: Verificar si se realizó alguna actualización
    IF ROW_COUNT() = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'No se encontró ninguna compra con el número de orden proporcionado.';
    END IF;

END //

DELIMITER ;
CREATE VIEW vista_insumos AS
SELECT 
    i.id_insumo,
    i.nombreInsumo,
    i.unidad,
    i.cantidad,
    i.total,
    i.fecha,
    p.id_proveedor,
    p.nombreProveedor
FROM 
    insumos i
JOIN 
    proveedores p
ON 
    i.id_proveedor = p.id_proveedor;


SELECT * FROM insumos;
SELECT * FROM vista_insumos;