
CREATE VIEW vista_detalle_venta AS
SELECT 
    g.id_galleta,
    g.tipo AS tipo_galleta,
    g.galleta,
    g.costo,
    g.existencia,
    g.fecha AS fecha_galleta,
    g.hora AS hora_galleta,
    
    v.id_venta,
    v.descripcion,
    v.total,
    v.fecha AS fecha_venta,
    v.hora AS hora_venta,
    v.ticket,
    v.tipoVenta,
    
    d.id_detalleVentaGalletas,
    d.venta_id,
    d.galleta_id,
    d.cantidad,
    d.subtotal
    
FROM 
    galletas g
JOIN 
    detalleVentaGalletas d ON g.id_galleta = d.galleta_id
JOIN 
    ventas v ON v.id_venta = d.venta_id;
    
select * from vista_detalle_venta;
select * from galletas;


DELIMITER $$

CREATE PROCEDURE insertarVenta(
    IN p_descripcion LONGTEXT,
    IN p_total FLOAT,
    IN p_ticket LONGTEXT,
    IN p_tipoVenta VARCHAR(50),
    IN p_detalles JSON,
    OUT p_id_venta INT
)
BEGIN
    DECLARE v_detalle JSON;
    DECLARE v_galleta_id INT;
    DECLARE v_cantidad INT;
    DECLARE v_subtotal FLOAT;
    DECLARE i INT DEFAULT 0;
    DECLARE detalles_count INT;
    DECLARE current_existencia INT;

    -- Insertar la venta en la tabla 'ventas'
    INSERT INTO ventas (descripcion, total, fecha, hora, ticket, tipoVenta)
    VALUES (p_descripcion, p_total, CURDATE(), CURTIME(), p_ticket, p_tipoVenta);

    -- Obtener el id_venta generado
    SET p_id_venta = LAST_INSERT_ID();

    -- Obtener la cantidad de elementos en el JSON de detalles
    SET detalles_count = JSON_LENGTH(p_detalles);

    -- Iterar sobre el arreglo de detalles de venta (JSON)
    WHILE i < detalles_count DO
        -- Obtener el detalle actual
        SET v_detalle = JSON_UNQUOTE(JSON_EXTRACT(p_detalles, CONCAT('$[', i, ']')));

        -- Extraer los valores de cada detalle
        SET v_galleta_id = JSON_UNQUOTE(JSON_EXTRACT(v_detalle, '$.id_galleta'));
        SET v_cantidad = JSON_UNQUOTE(JSON_EXTRACT(v_detalle, '$.cantidad'));
        SET v_subtotal = JSON_UNQUOTE(JSON_EXTRACT(v_detalle, '$.subtotal'));

        -- Insertar cada detalle de venta en la tabla 'detalleVentaGalletas'
        INSERT INTO detalleVentaGalletas (venta_id, galleta_id, cantidad, subtotal)
        VALUES (p_id_venta, v_galleta_id, v_cantidad, v_subtotal);

        -- Obtener la existencia actual de la galleta
        SELECT existencia INTO current_existencia FROM galletas WHERE id_galleta = v_galleta_id;

        -- Verificar si hay suficiente existencia
        IF current_existencia >= v_cantidad THEN
            -- Descontar la cantidad vendida de la existencia
            UPDATE galletas
            SET existencia = existencia - v_cantidad
            WHERE id_galleta = v_galleta_id;
        END IF;

        -- Incrementar el índice del detalle
        SET i = i + 1;
    END WHILE;

    -- Retornar el id_venta insertado
    SELECT p_id_venta AS id_venta;

END$$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE disminuirCantidadPorGalleta(
    IN p_nombre_galleta VARCHAR(100),
    IN p_cantidad INT
)
BEGIN
    DECLARE current_existencia INT;
    DECLARE galleta_id INT;

    -- Obtener la ID y existencia actual de la galleta por su nombre
    SELECT id_galleta, existencia
    INTO galleta_id, current_existencia
    FROM galletas 
    WHERE galleta = p_nombre_galleta AND tipo = 'Unidad';

    -- Verificar si la galleta existe
    IF galleta_id IS NOT NULL THEN
        -- Verificar si hay suficiente existencia
        IF current_existencia >= p_cantidad THEN
            -- Actualizar la existencia restando la cantidad especificada
            UPDATE galletas
            SET existencia = existencia - p_cantidad
            WHERE id_galleta = galleta_id;
        ELSE
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'No hay suficiente existencia para realizar la operación';
        END IF;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La galleta especificada no existe';
    END IF;

END$$

DELIMITER ;
