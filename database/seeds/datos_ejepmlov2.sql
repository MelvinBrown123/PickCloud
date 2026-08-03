-- =====================================================================
-- DATOS DE EJEMPLO PARA PICKCLOUD - TIENDA DE ABARROTES "Doña lupe"
-- Congruente con reglas de negocio (RN-01 a RN-26) y modelo E-R (5.6)
-- =====================================================================
-- Recordatorio del negocio:
--   * VENTA  -> compra física en mostrador. id_usuario = EMPLEADO o
--               ADMIN que la registró (RN-11, RF-08, ACT-02).
--   * PEDIDO -> compra para RECOLECCIÓN (PickUp). id_usuario = CLIENTE
--               que lo generó (RN-13, RN-14, ACT-03).
-- =====================================================================
-- Recordatorio del negocio:
--   * VENTA  -> compra física en mostrador. id_usuario = EMPLEADO o
--               ADMIN que la registró (RN-11, RF-08, ACT-02).
--   * PEDIDO -> compra para RECOLECCIÓN (PickUp). id_usuario = CLIENTE
--               que lo generó (RN-13, RN-14, ACT-03).
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------------
-- 0. LIMPIEZA: vacía todas las tablas y reinicia los contadores IDENTITY
-- ---------------------------------------------------------------------
TRUNCATE TABLE
    rol, usuarios, configuracion_negocio, token_recuperacion,
    categoria, producto, metodo_pago, venta, venta_producto,
    pedido, pedido_producto, comprobante, notificaciones,
    reportes, pedido_reporte, venta_reporte
RESTART IDENTITY CASCADE;

-- ---------------------------------------------------------------------
-- 1. ROL  (RN-01: administrador, empleado, cliente)
-- ---------------------------------------------------------------------
INSERT INTO rol (descripcion) VALUES
('Administrador'),   -- id_rol 1
('Empleado'),        -- id_rol 2
('Cliente');         -- id_rol 3

-- ---------------------------------------------------------------------
-- 2. USUARIOS
-- ---------------------------------------------------------------------
INSERT INTO usuarios (id_rol, nombre, correo, contrasena) VALUES
(1, 'Marisol Hernández', 'marisol.admin@laesquina.com', '$2b$10$examplehash.admin.0001'),   -- id_usuario 1: Admin
(2, 'Jorge Ramírez', 'jorge.empleado@laesquina.com', '$2b$10$examplehash.emple.0002'),      -- id_usuario 2: Empleado
(2, 'Ana Cortés', 'ana.empleado@laesquina.com', '$2b$10$examplehash.emple.0003'),           -- id_usuario 3: Empleado
(3, 'Roberto Sánchez', 'roberto.cliente@gmail.com', '$2b$10$examplehash.cliente.0004'),     -- id_usuario 4: Cliente
(3, 'Daniela Torres', 'daniela.cliente@gmail.com', '$2b$10$examplehash.cliente.0005'),      -- id_usuario 5: Cliente
(3, 'Emilio Vargas', 'emilio.cliente@gmail.com', '$2b$10$examplehash.cliente.0006');        -- id_usuario 6: Cliente

-- ---------------------------------------------------------------------
-- 3. CONFIGURACION_NEGOCIO
-- ---------------------------------------------------------------------
INSERT INTO configuracion_negocio (id_usuario, nombre_comercial, direccion, logo_url, stock_minimo_defecto) VALUES
(1, 'Abarrotes La Esquina', 'Av. Insurgentes Sur 1234, CDMX', 'https://cdn.laesquina.com/logo.png', 5);

-- ---------------------------------------------------------------------
-- 4. TOKEN_RECUPERACION
-- ---------------------------------------------------------------------
INSERT INTO token_recuperacion (id_usuario, token, expiracion, usado) VALUES
(4, 'a1b2c3d4-e5f6-7890-recuperacion-token', CURRENT_TIMESTAMP + INTERVAL '1 day', FALSE);

-- ---------------------------------------------------------------------
-- 5. CATEGORIA
-- ---------------------------------------------------------------------
INSERT INTO categoria (descripcion) VALUES
('Abarrotes básicos'),   -- 1
('Bebidas'),             -- 2
('Lácteos'),             -- 3
('Panadería'),           -- 4
('Limpieza del hogar'),  -- 5
('Botanas y dulces'),    -- 6
('Higiene personal');    -- 7

-- ---------------------------------------------------------------------
-- 6. PRODUCTO
-- ---------------------------------------------------------------------
-- id 1  Arroz Morelos 1kg          precio_venta 22.00  descuento 0%
-- id 2  Frijol Negro 1kg           precio_venta 27.50  descuento 0%
-- id 3  Aceite Vegetal 1L          precio_venta 32.00  descuento 5%
-- id 4  Azúcar Estándar 1kg        precio_venta 24.00  descuento 0%
-- id 5  Refresco de Cola 600ml     precio_venta 16.00  descuento 0%
-- id 6  Agua Natural 1L            precio_venta  9.00  descuento 0%
-- id 7  Jugo de Naranja 1L         precio_venta 28.00  descuento 0%
-- id 8  Leche Entera 1L            precio_venta 26.00  descuento 0%
-- id 9  Queso Panela 400g          precio_venta 52.00  descuento 0%
-- id 10 Yogur Natural 1L           precio_venta 40.00  descuento 10%
-- id 11 Bolillo (pieza)            precio_venta  3.00  descuento 0%
-- id 12 Pan de Caja Integral       precio_venta 45.00  descuento 0%
-- id 13 Jabón para Trastes 750ml   precio_venta 24.00  descuento 0%
-- id 14 Cloro 1L                   precio_venta 19.00  descuento 0%
-- id 15 Papas Fritas 150g          precio_venta 22.00  descuento 0%
-- id 16 Chocolate en Barra         precio_venta 18.00  descuento 0%
-- id 17 Pasta Dental 100ml         precio_venta 32.00  descuento 0%
-- id 18 Jabón de Baño (pieza)      precio_venta 13.00  descuento 0%
INSERT INTO producto (id_categoria, nombre, descripcion, precio_costo, precio_venta, stock_actual, sku, activo, url_imagen, descuento) VALUES
(1, 'Arroz Morelos 1kg', 'Arroz de grano largo, bolsa de 1kg', 14.50, 22.00, 120, 'ABR-ARR-001', TRUE, NULL, 0.00),
(1, 'Frijol Negro 1kg', 'Frijol negro seleccionado, bolsa de 1kg', 18.00, 27.50, 90, 'ABR-FRI-002', TRUE, NULL, 0.00),
(1, 'Aceite Vegetal 1L', 'Aceite vegetal comestible, botella 1 litro', 22.00, 32.00, 60, 'ABR-ACE-003', TRUE, NULL, 5.00),
(1, 'Azúcar Estándar 1kg', 'Azúcar refinada, bolsa de 1kg', 16.00, 24.00, 100, 'ABR-AZU-004', TRUE, NULL, 0.00),
(2, 'Refresco de Cola 600ml', 'Bebida gaseosa sabor cola', 10.00, 16.00, 150, 'BEB-COL-005', TRUE, NULL, 0.00),
(2, 'Agua Natural 1L', 'Agua purificada embotellada', 5.00, 9.00, 200, 'BEB-AGU-006', TRUE, NULL, 0.00),
(2, 'Jugo de Naranja 1L', 'Jugo de naranja 100% natural', 18.00, 28.00, 45, 'BEB-JUG-007', TRUE, NULL, 0.00),
(3, 'Leche Entera 1L', 'Leche entera pasteurizada', 19.00, 26.00, 80, 'LAC-LEC-008', TRUE, NULL, 0.00),
(3, 'Queso Panela 400g', 'Queso panela fresco', 35.00, 52.00, 30, 'LAC-QUE-009', TRUE, NULL, 0.00),
(3, 'Yogur Natural 1L', 'Yogur natural sin azúcar añadida', 28.00, 40.00, 25, 'LAC-YOG-010', TRUE, NULL, 10.00),
(4, 'Bolillo (pieza)', 'Pan bolillo tradicional', 1.50, 3.00, 300, 'PAN-BOL-011', TRUE, NULL, 0.00),
(4, 'Pan de Caja Integral', 'Pan de caja integral 680g', 32.00, 45.00, 40, 'PAN-CAJ-012', TRUE, NULL, 0.00),
(5, 'Jabón para Trastes 750ml', 'Detergente líquido para trastes', 15.00, 24.00, 70, 'LIM-JAB-013', TRUE, NULL, 0.00),
(5, 'Cloro 1L', 'Cloro desinfectante multiusos', 12.00, 19.00, 85, 'LIM-CLO-014', TRUE, NULL, 0.00),
(6, 'Papas Fritas 150g', 'Botana de papas fritas saladas', 14.00, 22.00, 110, 'BOT-PAP-015', TRUE, NULL, 0.00),
(6, 'Chocolate en Barra', 'Chocolate con leche 90g', 12.00, 18.00, 95, 'BOT-CHO-016', TRUE, NULL, 0.00),
(7, 'Pasta Dental 100ml', 'Pasta dental blanqueadora', 20.00, 32.00, 65, 'HIG-PAS-017', TRUE, NULL, 0.00),
(7, 'Jabón de Baño (pieza)', 'Jabón de tocador hidratante', 8.00, 13.00, 140, 'HIG-JAB-018', TRUE, NULL, 0.00);

-- ---------------------------------------------------------------------
-- 7. METODO_PAGO  (Efectivo, Transferencia, Tarjeta)
-- ---------------------------------------------------------------------
INSERT INTO metodo_pago (descripcion) VALUES
('Efectivo'),       -- 1
('Transferencia'),  -- 2
('Tarjeta');        -- 3

-- =====================================================================
-- VENTAS (mostrador físico) — id_usuario = EMPLEADO o ADMIN que registró
-- Totales verificados: subtotal = suma de líneas (precio con descuento
-- de producto aplicado x cantidad); total = subtotal - descuento venta.
-- =====================================================================

-- venta 1: Jorge (empleado), Efectivo, 03-mayo
--   Arroz x1 (22.00) + Agua Natural x2 (9.00 c/u = 18.00) = 40.00
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(2, 1, '2026-05-03 09:15:00', 40.00, 0.00, 40.00);

-- venta 2: Ana (empleado), Tarjeta, 07-mayo
--   Leche x2 (26.00 c/u = 52.00) + Queso Panela x1 (52.00) + Pan de Caja x1 (45.00) = 149.00
--   Descuento de venta 5% = 7.45  -> total 141.55
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(3, 3, '2026-05-07 11:40:00', 149.00, 7.45, 141.55);

-- venta 3: Jorge (empleado), Efectivo, 12-mayo
--   Refresco Cola x2 (16.00 c/u = 32.00) + Papas Fritas x1 (22.00) + Chocolate x1 (18.00) = 72.00
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(2, 1, '2026-05-12 16:20:00', 72.00, 0.00, 72.00);

-- venta 4: Marisol (admin), Transferencia, 20-mayo
--   Cloro x2 (19.00 c/u = 38.00) + Jabón para Trastes x1 (24.00) + Pasta Dental x2 (32.00 c/u = 64.00) = 126.00
--   Descuento de venta 10% = 12.60 -> total 113.40
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(1, 2, '2026-05-20 10:05:00', 126.00, 12.60, 113.40);

-- venta 5: Ana (empleado), Efectivo, 02-junio
--   Bolillo x10 (3.00 c/u = 30.00) + Pan de Caja x1 (45.00) = 75.00
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(3, 1, '2026-06-02 08:50:00', 75.00, 0.00, 75.00);

-- venta 6: Jorge (empleado), Tarjeta, 15-junio
--   Agua Natural x3 (9.00 c/u = 27.00) + Refresco Cola x1 (16.00) + Chocolate x2 (18.00 c/u = 36.00) = 79.00
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(2, 3, '2026-06-15 17:05:00', 79.00, 0.00, 79.00);

-- venta 7: Marisol (admin), Transferencia, 25-junio
--   Arroz x2 (22.00 c/u = 44.00) + Frijol x2 (27.50 c/u = 55.00) + Azúcar x1 (24.00) = 123.00
--   Descuento de venta (promoción fija) 3.00 -> total 120.00
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(1, 2, '2026-06-25 12:30:00', 123.00, 3.00, 120.00);

-- Detalle de líneas por venta
INSERT INTO venta_producto (id_venta, id_producto, cantidad) VALUES
(1, 1, 1), (1, 6, 2),
(2, 8, 2), (2, 9, 1), (2, 12, 1),
(3, 5, 2), (3, 15, 1), (3, 16, 1),
(4, 14, 2), (4, 13, 1), (4, 17, 2),
(5, 11, 10), (5, 12, 1),
(6, 6, 3), (6, 5, 1), (6, 16, 2),
(7, 1, 2), (7, 2, 2), (7, 4, 1);

-- ---------------------------------------------------------------------
-- COMPROBANTE (uno por venta)
-- ---------------------------------------------------------------------
INSERT INTO comprobante (id_venta, url_pdf, fecha_emision) VALUES
(1, 'https://cdn.laesquina.com/comprobantes/venta_1.pdf', '2026-05-03 09:15:05'),
(2, 'https://cdn.laesquina.com/comprobantes/venta_2.pdf', '2026-05-07 11:40:05'),
(3, 'https://cdn.laesquina.com/comprobantes/venta_3.pdf', '2026-05-12 16:20:05'),
(4, 'https://cdn.laesquina.com/comprobantes/venta_4.pdf', '2026-05-20 10:05:05'),
(5, 'https://cdn.laesquina.com/comprobantes/venta_5.pdf', '2026-06-02 08:50:05'),
(6, 'https://cdn.laesquina.com/comprobantes/venta_6.pdf', '2026-06-15 17:05:05'),
(7, 'https://cdn.laesquina.com/comprobantes/venta_7.pdf', '2026-06-25 12:30:05');

-- =====================================================================
-- PEDIDOS (PickUp) — id_usuario = CLIENTE que lo generó (RN-13, RN-14)
-- estado: pendiente, pagado, listo, entregado o cancelado (RN-15)
-- =====================================================================

-- pedido 1: Roberto (cliente), Tarjeta, 'listo', 05-mayo
--   Jugo de Naranja x2 (28.00 c/u = 56.00) + Yogur Natural x1 (40.00 con 10% desc. = 36.00) = 92.00
INSERT INTO pedido (id_usuario, id_metodo_pago, estado, fecha_creacion, fecha_recoleccion, total) VALUES
(4, 3, 'listo', '2026-05-05 08:00:00', NULL, 92.00);

-- pedido 2: Daniela (cliente), Efectivo, 'entregado', 18-mayo (recolectado 40 min después, dentro de la 1 hr de RN-26)
--   Arroz x1 (22.00) + Frijol x1 (27.50) + Azúcar x1 (24.00) = 73.50
INSERT INTO pedido (id_usuario, id_metodo_pago, estado, fecha_creacion, fecha_recoleccion, total) VALUES
(5, 1, 'entregado', '2026-05-18 12:30:00', '2026-05-18 13:10:00', 73.50);

-- pedido 3: Emilio (cliente), Transferencia, 'pendiente', 10-junio
--   Bolillo x6 (3.00 c/u = 18.00) + Pan de Caja x1 (45.00) + Jabón de Baño x2 (13.00 c/u = 26.00) = 89.00
INSERT INTO pedido (id_usuario, id_metodo_pago, estado, fecha_creacion, fecha_recoleccion, total) VALUES
(6, 2, 'pendiente', '2026-06-10 09:20:00', NULL, 89.00);

-- pedido 4: Roberto (cliente), Efectivo, 'cancelado', 20-junio (stock reservado se liberó al cancelarse, RN-16)
--   Aceite Vegetal x2 (32.00 con 5% desc. = 30.40 c/u = 60.80) + Agua Natural x2 (9.00 c/u = 18.00) = 78.80
INSERT INTO pedido (id_usuario, id_metodo_pago, estado, fecha_creacion, fecha_recoleccion, total) VALUES
(4, 1, 'cancelado', '2026-06-20 15:40:00', NULL, 78.80);

-- Detalle de líneas por pedido
INSERT INTO pedido_producto (id_pedido, id_producto, cantidad) VALUES
(1, 7, 2), (1, 10, 1),
(2, 1, 1), (2, 2, 1), (2, 4, 1),
(3, 11, 6), (3, 12, 1), (3, 18, 2),
(4, 3, 2), (4, 6, 2);

-- ---------------------------------------------------------------------
-- NOTIFICACIONES (avisos al cliente por cambio de estado de pedido)
-- ---------------------------------------------------------------------
INSERT INTO notificaciones (id_pedido, id_usuario, tipo, intentos) VALUES
(1, 4, 'pedido_listo_para_recoger', 1),
(2, 5, 'pedido_entregado', 1),
(3, 6, 'pedido_recibido', 1),
(4, 4, 'pedido_cancelado', 1);

-- ---------------------------------------------------------------------
-- REPORTES (solo administradores, RN-18/RN-20), rango mayo-junio 2026
-- ---------------------------------------------------------------------
INSERT INTO reportes (id_usuario, tipo, fecha_generacion, fecha_inicio, fecha_fin, url_pdf, metodo_pago) VALUES
(1, 'ventas_mayo_junio', '2026-06-30 20:00:00', '2026-05-01 00:00:00', '2026-06-30 23:59:59', 'https://cdn.laesquina.com/reportes/ventas_may_jun.pdf', NULL),
(1, 'pedidos_pickup_mayo_junio', '2026-06-30 20:05:00', '2026-05-01 00:00:00', '2026-06-30 23:59:59', 'https://cdn.laesquina.com/reportes/pedidos_may_jun.pdf', NULL),
(1, 'ventas_por_metodo_pago', '2026-06-30 20:10:00', '2026-05-01 00:00:00', '2026-06-30 23:59:59', 'https://cdn.laesquina.com/reportes/ventas_efectivo.pdf', 'Efectivo');

-- ---------------------------------------------------------------------
-- PEDIDO_REPORTE / VENTA_REPORTE
-- ---------------------------------------------------------------------
INSERT INTO pedido_reporte (id_pedido, id_reporte) VALUES
(1, 2), (2, 2), (3, 2), (4, 2);

INSERT INTO venta_reporte (id_venta, id_reporte) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1),
(1, 3), (3, 3), (5, 3); -- ventas en Efectivo también entran al reporte por método de pago

COMMIT;
