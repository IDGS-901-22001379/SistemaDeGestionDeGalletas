USE DonGalleto;
-- Proveedores 
insert into proveedores (nombreProveedor) VALUES ('Proveedor 1'), ('Proveedor 2');
select*from proveedores;

-- Insumos
INSERT INTO insumos (nombreInsumo, unidad, cantidad, total, fecha, id_proveedor)
VALUES ('Harina', '5', 5, 25, '2024-11-10', 1),
	   ('Mantequilla', '2', 10, 20, '2024-11-20', 1),
       ('Azúcar', '5', 3, 15, '2024-11-02', 1),
       ('Nueces', '3', 3, 9, '2024-10-20', 1),
       ('Leche', '1', 10, 10, '2024-10-29', 2),
       ('Sal', '2', 3, 5, '2024-10-02', 2),
       ('Polvo para hornear', '1', 5, 5, '2024-10-10', 2),
       ('Escencia de vainilla', '1', 5, 5, '2024-10-30', 2);
select*from insumos;
DELIMITER $$

CREATE PROCEDURE RegistrarCompras(
    IN p_proveedor_id INT,
    IN p_fecha DATE,
    IN p_insumos TEXT           -- Insumos seleccionados para la compra con sus cantidades, precios y pesos (formato JSON)
)
BEGIN
    DECLARE v_compra_id INT;
    DECLARE v_total_cantidad INT;
    DECLARE v_total_peso FLOAT DEFAULT 0;  -- Peso total de la compra
    DECLARE v_total_precio FLOAT DEFAULT 0; -- Precio total de la compra
    DECLARE v_numeroOrden VARCHAR(50);
    DECLARE v_ultimoNumero INT;
    DECLARE v_descripcion JSON;  -- Cambié a tipo JSON
    
    IF p_insumos IS NULL OR p_insumos = '' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El JSON proporcionado está vacío o es nulo';
    END IF;

    IF JSON_VALID(p_insumos) = 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'El JSON proporcionado no es válido';
    END IF;

    -- Calcular la suma total de las cantidades, pesos y precios de los insumos
    -- Sumar las cantidades de los insumos
    SELECT SUM(jt.cantidad) INTO v_total_cantidad
    FROM JSON_TABLE(p_insumos, '$[*]' COLUMNS (
        insumo VARCHAR(100) PATH '$.insumo',
        cantidad INT PATH '$.cantidad',
        precio FLOAT PATH '$.precio',
        peso FLOAT PATH '$.peso' -- Recibimos el peso de cada insumo
    )) AS jt;

    -- Calcular el peso total de la compra (cantidad * peso)
    SELECT SUM(jt.cantidad * jt.peso) INTO v_total_peso
    FROM JSON_TABLE(p_insumos, '$[*]' COLUMNS (
        insumo VARCHAR(100) PATH '$.insumo',
        cantidad INT PATH '$.cantidad',
        precio FLOAT PATH '$.precio',
        peso FLOAT PATH '$.peso' -- Recibimos el peso de cada insumo
    )) AS jt;

    -- Calcular el precio total de la compra (cantidad * precio)
    SELECT SUM(jt.cantidad * jt.precio) INTO v_total_precio
    FROM JSON_TABLE(p_insumos, '$[*]' COLUMNS (
        insumo VARCHAR(100) PATH '$.insumo',
        cantidad INT PATH '$.cantidad',
        precio FLOAT PATH '$.precio',
        peso FLOAT PATH '$.peso' -- Recibimos el peso de cada insumo
    )) AS jt;

    -- Obtener el último número de orden insertado
    SELECT MAX(CAST(SUBSTRING(numeroOrden, 4) AS UNSIGNED)) INTO v_ultimoNumero
    FROM comprasRealizadas;

    -- Si no hay compras previas, empezar desde 1
    IF v_ultimoNumero IS NULL THEN
        SET v_ultimoNumero = 0;
    END IF;

    -- Incrementar el último número de orden y formatear el nuevo número de orden
    SET v_numeroOrden = CONCAT('ORD', LPAD(v_ultimoNumero + 1, 5, '0'));

    -- Insertar la compra realizada con los totales calculados
    INSERT INTO comprasRealizadas (proveedor_id, cantidad, precio, fecha, numeroOrden, estatus, peso)
    VALUES (p_proveedor_id, v_total_cantidad, v_total_precio, p_fecha, v_numeroOrden, 0, v_total_peso);

    -- Obtener el ID de la compra recién insertada
    SET v_compra_id = LAST_INSERT_ID();

    -- Generar la descripción como un JSON
    SET v_descripcion = (
        SELECT JSON_ARRAYAGG(
                    JSON_OBJECT(
                        'insumo', jt.insumo, 
                        'cantidad', jt.cantidad, 
                        'precio', jt.precio, 
                        'peso', jt.peso, 
                        'total_precio', jt.cantidad * jt.precio
                    )
                    )
        FROM JSON_TABLE(p_insumos, '$[*]' COLUMNS (
            insumo VARCHAR(100) PATH '$.insumo',
            cantidad INT PATH '$.cantidad',
            precio FLOAT PATH '$.precio',
            peso FLOAT PATH '$.peso'
        )) AS jt
    );

    -- Insertar la descripción en la tabla detalleCompra
    INSERT INTO detalleCompra (descripcion, compra_id)
    VALUES (v_descripcion, v_compra_id);

END$$

DELIMITER ;


-- ------------------------------------------------------------------------------------------------------------------------------

-- ------------------------------------------------------------------------------------------------------------------------------
-- Procedimiento almacenado para agregar a un nuevo proveedor (temporales)
DELIMITER $$

CREATE PROCEDURE InsertarProveedor(
    IN p_nombreProveedor VARCHAR(100)
)
BEGIN
    INSERT INTO proveedores (nombreProveedor)
    VALUES (p_nombreProveedor);
END$$

DELIMITER ;
-- ------------------------------------------------------------------------------------------------------------------------------

-- -------------------------------------------------------     VISTAS     -------------------------------------------------------

-- ------------------------------------------------------------------------------------------------------------------------------
-- Vista de las compras realizadas para el historial para el apartado de de compras
CREATE OR REPLACE VIEW VistaComprasHistorial AS
SELECT 
	cr.id_comprasRealizadas,
    cr.fecha, 
    cr.precio, 
    cr.numeroOrden, 
    cr.estatus,
    cr.cantidad,
    cr.peso,
    p.id_proveedor,
    p.nombreProveedor
FROM 
    comprasRealizadas cr
JOIN 
    proveedores p ON cr.proveedor_id = p.id_proveedor;


CREATE OR REPLACE VIEW VistaDetalleCompras AS
SELECT 
	cr.id_comprasRealizadas,
    cr.fecha, 
    cr.precio, 
    cr.numeroOrden, 
    cr.estatus, 
    cr.cantidad, 
    cr.peso, 
    p.id_proveedor,
    p.nombreProveedor,
    dc.id_detalleCompra,
    dc.descripcion
FROM 
    detalleCompra dc
JOIN 
    comprasRealizadas cr ON dc.compra_id = cr.id_comprasRealizadas
JOIN 
    proveedores p ON cr.proveedor_id = p.id_proveedor;
    
    SELECT * FROM VistaDetalleCompras;

-- -------------------------------------------------------------------------------------------------------------------------------

-- ------------------------------------------------------------------------------------------------------------------------------
-- Vista de las compras realizadas para el historial para el apartado de de compras
CREATE VIEW VistaProveedor AS
SELECT 
    id_proveedor,
    nombreProveedor
FROM 
    proveedores;
-- -------------------------------------------------------------------------------------------------------------------------------

-- ----------------------------------------------     SELECTS A TABLAS Y VISTAS    -----------------------------------------------

-- -------------------------------------------------------------------------------------------------------------------------------
-- select*from comprasRealizadas;
-- select*from detalleCompra;
-- SELECT * FROM VistaComprasHistorial;
-- SELECT * FROM VistaProveedor;
-- SELECT*from proveedores;
-- ------------------------------------------------------------------------------------------------------------------------------

-- ------------------------------------------------------     PRUEBAS     -------------------------------------------------------

-- ------------------------------------------------------------------------------------------------------------------------------
-- proveedor 1 que tiene 2 insumos
-- proveedor 1
CALL RegistrarCompras(
    1,  -- proveedor_id (Ejemplo: Proveedor con ID 1)
    '2024-12-05',  -- Fecha de la compra
      -- Estatus de la compra (0 = Inactivo, puedes usar otro valor si es necesario)
    '[{"insumo": "Harina", "cantidad": 5, "precio": 350, "peso": 25}, 
      {"insumo": "Mantequilla", "cantidad": 2, "precio": 42, "peso": 1}]'  -- JSON con los insumos, cantidades, precios y pesos
);

-- Prueba para agregar un proveedor nuevo-temporal
CALL InsertarProveedor('Proveedor XYZ');