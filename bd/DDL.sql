DROP DATABASE IF EXISTS DonGalleto;
CREATE DATABASE DonGalleto;
USE DonGalleto;

-- Crear la tabla proveedores 
CREATE TABLE proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombreProveedor VARCHAR(100) NOT NULL
);

-- Crear la tabla insumos
CREATE TABLE insumos (
    id_insumo INT AUTO_INCREMENT PRIMARY KEY,
    nombreInsumo VARCHAR(100) NOT NULL,
    unidad VARCHAR(50) NOT NULL,
    cantidad INT NOT NULL,
    total DOUBLE NOT NULL,
    fecha DATE NOT NULL,
	id_proveedor INT NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
);

-- Crear la tabla comprasRealizadas 
CREATE TABLE comprasRealizadas (
    id_comprasRealizadas INT AUTO_INCREMENT PRIMARY KEY,
    proveedor_id INT NOT NULL, -- Relación con proveedores
    cantidad INT NOT NULL,
    precio float NOT NULL,
    fecha DATE NOT NULL,
    peso FLOAT NOT NULL,
    numeroOrden VARCHAR(50) NOT NULL,
    estatus INT NOT NULL DEFAULT 0, -- 0 EN PROCESO 1 LLEGO
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id_proveedor)
);

-- Crear la tabla galletas
CREATE TABLE galletas (
    id_galleta INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    galleta VARCHAR(100) NOT NULL,
    costo float NOT NULL,
    existencia INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL
);

-- Crear la tabla ventas 
CREATE TABLE ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY, 
    descripcion LONGTEXT NOT NULL, 
    total FLOAT NOT NULL, -- Total general de la venta
    fecha DATE NOT NULL, 
    hora TIME NOT NULL, 
    ticket LONGTEXT, 
    tipoVenta VARCHAR(50) NOT NULL
);

-- Crear la tabla detalleVentaGalletas
CREATE TABLE detalleVentaGalletas (
    id_detalleVentaGalletas INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL, -- Relación con ventas
    galleta_id INT NOT NULL, -- Relación con galletas
    cantidad INT NOT NULL, -- Cantidad de galletas de este tipo
    subtotal FLOAT NOT NULL, -- Subtotal = cantidad * precio_unitario
    FOREIGN KEY (venta_id) REFERENCES ventas(id_venta),
    FOREIGN KEY (galleta_id) REFERENCES galletas(id_galleta)
);

-- Crear la tabla ganancias 
CREATE TABLE ganancias (
    id_ganancia INT AUTO_INCREMENT PRIMARY KEY,
    totalVenta float NOT NULL,
    fecha DATE NOT NULL,
    salidaDinero float NOT NULL,
    entradaDinero float NOT NULL,
    gananciasTotales float NOT NULL,
    observaciones float, -- Permitir valores nulos
    venta_id INT,
    compra_id INT,
    FOREIGN KEY (venta_id) REFERENCES ventas(id_venta),
    FOREIGN KEY (compra_id) REFERENCES comprasRealizadas(id_comprasRealizadas)
);

-- Crear la tabla detalleCompra
CREATE TABLE detalleCompra (
    id_detalleCompra INT AUTO_INCREMENT PRIMARY KEY,
    descripcion JSON,
    compra_id INT NOT NULL,
    FOREIGN KEY (compra_id) REFERENCES comprasRealizadas(id_comprasRealizadas)
);

CREATE TABLE inventario_galletas (
    id_lote INT AUTO_INCREMENT PRIMARY KEY,              -- Identificador único del lote
    galleta_id INT NOT NULL,                             -- Relación con la tabla de galletas
    cantidad INT NOT NULL,                               -- Cantidad inicial en este lote
    fecha_ingreso DATE NOT NULL,                         -- Fecha en que se ingresó el lote
    fecha_vencimiento DATE NOT NULL,                     -- Fecha de vencimiento del lote
    vendido INT DEFAULT 0,                               -- Cantidad vendida de este lote
    restante INT GENERATED ALWAYS AS (cantidad - vendido) STORED, -- Cantidad restante (calculada)
    FOREIGN KEY (galleta_id) REFERENCES galletas(id_galleta) -- Relación con la tabla de galletas
);

CREATE TABLE alertas_inventario (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,            -- Identificador único de la alerta
    galleta_id INT NOT NULL,                             -- Relación con la tabla de galletas
    lote_id INT NOT NULL,                                -- Relación con la tabla de inventario
    tipo_alerta ENUM('VENCIMIENTO', 'MINIMO') NOT NULL,  -- Tipo de alerta: Vencimiento o Mínimo
    descripcion VARCHAR(255) NOT NULL,                  -- Descripción de la alerta
    fecha_alerta DATE NOT NULL,                         -- Fecha de generación de la alerta
    atendida BOOLEAN DEFAULT FALSE,                     -- Si la alerta ha sido atendida o no
    FOREIGN KEY (galleta_id) REFERENCES galletas(id_galleta), -- Relación con la tabla de galletas
    FOREIGN KEY (lote_id) REFERENCES inventario_galletas(id_lote) -- Relación con la tabla de inventario
);


CREATE TABLE mermas_galletas (
    id_merma_galleta INT AUTO_INCREMENT PRIMARY KEY,
    id_galleta INT NOT NULL,
    cantidad INT NOT NULL,
    descripcion VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_galleta) REFERENCES galletas(id_galleta)
);

CREATE TABLE mermas_insumos (
    id_merma_insumo INT AUTO_INCREMENT PRIMARY KEY,
    id_insumo INT NOT NULL,
    cantidad INT NOT NULL,
    descripcion VARCHAR(255),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_insumo) REFERENCES insumos(id_insumo)
);
