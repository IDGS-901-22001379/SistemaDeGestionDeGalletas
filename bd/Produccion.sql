 DELIMITER $$

CREATE PROCEDURE agregarGalletasYDescontarInsumos(
    IN p_galleta_id INT,              -- ID de la galleta
    IN p_cantidad_total INT,          -- Cantidad total de galletas
    IN p_fecha_ingreso DATE,          -- Fecha de ingreso
    IN p_fecha_vencimiento DATE,      -- Fecha de vencimiento
    IN p_insumos JSON,                -- JSON con los id_insumo y cantidad_descontar
    IN p_vendido INT                  -- Cantidad vendida
)
BEGIN
    DECLARE v_total_insumo DOUBLE;
    DECLARE v_vendido INT;
    DECLARE v_galletas_existentes INT;
    DECLARE v_index INT DEFAULT 0;
    DECLARE v_insumo JSON;
    DECLARE v_insumo_id INT;
    DECLARE v_cantidad_descontar DOUBLE;
    DECLARE v_error_msg VARCHAR(255);

    -- Si p_vendido es NULL, asignar 0 como valor predeterminado
    SET v_vendido = IFNULL(p_vendido, 0);

    -- Iterar sobre los insumos recibidos en el parámetro JSON
    WHILE v_index < JSON_LENGTH(p_insumos) DO
        -- Obtener el insumo actual y la cantidad a descontar
        SET v_insumo = JSON_EXTRACT(p_insumos, CONCAT('$[', v_index, ']'));
        SET v_insumo_id = JSON_UNQUOTE(JSON_EXTRACT(v_insumo, '$.id_insumo'));
        SET v_cantidad_descontar = JSON_UNQUOTE(JSON_EXTRACT(v_insumo, '$.cantidad_descontar'));

        -- Verificar la existencia del insumo
        SELECT total INTO v_total_insumo
        FROM insumos
        WHERE id_insumo = v_insumo_id;

        -- Validar si hay suficiente insumo en total para descontar
        IF v_total_insumo >= v_cantidad_descontar THEN
            -- Descontar la cantidad especificada del total del insumo
            UPDATE insumos
            SET total = total - v_cantidad_descontar
            WHERE id_insumo = v_insumo_id;
        ELSE
            -- Si no hay suficiente insumo, generar un mensaje de error
            SET v_error_msg = CONCAT('No hay suficiente insumo para el id_insumo ', v_insumo_id);
            -- Usar SIGNAL para generar el error
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_error_msg;
        END IF;

        -- Incrementar el índice para la siguiente iteración
        SET v_index = v_index + 1;
    END WHILE;

    -- Insertar el registro en la tabla inventario_galletas
    INSERT INTO inventario_galletas (galleta_id, cantidad, fecha_ingreso, fecha_vencimiento, vendido)
    VALUES (p_galleta_id, p_cantidad_total, p_fecha_ingreso, p_fecha_vencimiento, v_vendido);

    -- Verificar si la galleta ya existe en la tabla galletas
    SELECT COUNT(*) INTO v_galletas_existentes
    FROM galletas
    WHERE id_galleta = p_galleta_id;

    IF v_galletas_existentes > 0 THEN
        -- Si la galleta ya existe, actualizar su existencia
        UPDATE galletas
        SET existencia = existencia + p_cantidad_total
        WHERE id_galleta = p_galleta_id;
    ELSE
        -- Si no existe, generar un mensaje de error
        SET v_error_msg = CONCAT('La galleta con ID ', p_galleta_id, ' no existe.');
        -- Usar SIGNAL para generar el error
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = v_error_msg;
    END IF;

    -- Confirmación de la operación
    SELECT 'Registro y descuento realizados correctamente.' AS mensaje;

END$$

DELIMITER ;
CALL agregarGalletasYDescontarInsumos(
    1,                          -- p_galleta_id
    100,                        -- p_cantidad_total
    '2024-12-01',               -- p_fecha_ingreso
    '2025-12-01',               -- p_fecha_vencimiento
    '[{"id_insumo": 1, "cantidad_descontar": 5}, {"id_insumo": 2, "cantidad_descontar": 3}]', -- p_insumos (JSON)
    null                         -- p_vendido
);

DELIMITER $$

-- Procedimiento para agregar galletas al stock
CREATE PROCEDURE agregarGalletasStock(
    IN p_galleta_id INT,
    IN p_cantidad INT
)
BEGIN
    UPDATE galletas
    SET existencia = existencia + p_cantidad
    WHERE id_galleta = p_galleta_id;

    SELECT 'Stock actualizado correctamente.' AS mensaje;
END$$

-- Procedimiento para registrar merma de galletas
CREATE PROCEDURE registrarMermaGalletas(
    IN p_galleta_id INT,
    IN p_cantidad INT,
    IN p_descripcion TEXT
)
BEGIN
    IF (SELECT existencia FROM galletas WHERE id_galleta = p_galleta_id) >= p_cantidad THEN
        UPDATE galletas
        SET existencia = existencia - p_cantidad
        WHERE id_galleta = p_galleta_id;

        INSERT INTO alertas_inventario (galleta_id, lote_id, tipo_alerta, descripcion, fecha_alerta, atendida)
        VALUES (p_galleta_id, 0, 'MERMA', p_descripcion, NOW(), FALSE);

        SELECT 'Merma registrada correctamente.' AS mensaje;
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cantidad insuficiente para registrar merma.';
    END IF;
END$$

DELIMITER ;
