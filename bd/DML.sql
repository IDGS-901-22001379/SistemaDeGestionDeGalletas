INSERT INTO galletas (tipo, galleta, costo, existencia, fecha, hora)
VALUES
('Unidad', 'Chocolate', 10.00, 3, '2024-01-01', '11:11:11'),
('Unidad', 'Arándano', 10.00, 12, '2024-01-01', '11:11:11'),
('Unidad', 'Avena', 10.00, 56, '2024-01-01', '11:11:11'),
('Unidad', 'Vainilla', 10.00, 13, '2024-01-01', '11:11:11'),
('Unidad', 'Chispas de Chocolate', 10.00, 200, '2024-01-01', '11:11:11'),
('Unidad', 'Limón', 10.00, 100, '2024-01-01', '11:11:11'),
('Unidad', 'Almendras', 10.00, 70, '2024-01-01', '11:11:11'),
('Unidad', 'Coco', 10.00, 95, '2024-01-01', '11:11:11'),
('Unidad', 'Jengibre', 10.00, 17, '2024-01-01', '11:11:11'),
('Unidad', 'Sorpresa Nuez', 8.00, 80, '2024-01-01', '11:11:11'),

('Caja De Kilo', 'Chocolate', 200.00, 25, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Arándano', 200.00, 18, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Avena', 200.00, 27, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Vainilla', 200.00, 7, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Chispas de Chocolate', 10.00, 30, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Limón', 200.00, 25, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Almendras', 200.00, 20, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Coco', 200.00, 19, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Jengibre', 200.00, 15, '2024-01-01', '11:11:11'),
('Caja De Kilo', 'Sorpresa Nuez', 180.00, 10, '2024-01-01', '11:11:11'),

('Caja De Medio Kilo', 'Chocolate', 100.00, 23, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Arándano', 100.00, 36, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Avena', 100.00, 10, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Vainilla', 100.00, 15, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Chispas de Chocolate', 10.00, 20, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Limón', 100.00, 17, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Almendras', 100.00, 40, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Coco', 100.00, 19, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Jengibre', 100.00, 19, '2024-01-01', '11:11:11'),
('Caja De Medio Kilo', 'Sorpresa Nuez', 90.00, 25, '2024-01-01', '11:11:11');

INSERT INTO ventas (descripcion, total, fecha, hora, ticket, tipoVenta)
VALUES
('Venta de tres tipos de galletas', 93.99, '2024-11-22', '11:00:00', 'Ticket123', 'Variado'),
('Venta de dos tipos de galletas', 54.60, '2024-11-22', '11:30:00', 'Ticket124', 'Variado');

-- Detalles de la primera venta (Venta ID: 1)
INSERT INTO detalleVentaGalletas (venta_id, galleta_id, cantidad, subtotal)
VALUES
(1, 1, 2, 31.75),  -- 2 Galletas de Chocolate
(1, 2, 3, 36.45),  -- 3 Galletas de Vainilla
(1, 3, 1, 18.30);  -- 1 Galleta de Avena

-- Detalles de la segunda venta (Venta ID: 2)
INSERT INTO detalleVentaGalletas (venta_id, galleta_id, cantidad, subtotal)
VALUES
(2, 1, 2, 31.66),  -- 2 Galletas de Chocolate
(2, 2, 2, 24.92);  -- 2 Galletas de Vainilla
