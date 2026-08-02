-- =====================================================================
-- DATOS DE EJEMPLO PARA PICKCLOUD - TIENDA DE ABARROTES "LA ESQUINA"
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

-- ---------------------------------------------------------------------
-- 1. ROL  (RN-01: administrador, empleado, cliente)
-- ---------------------------------------------------------------------
INSERT INTO rol (descripcion) VALUES
('Administrador'),
('Empleado'),
('Cliente');

-- ---------------------------------------------------------------------
-- 2. USUARIOS
-- ---------------------------------------------------------------------
-- id_rol: 1 = Administrador, 2 = Empleado, 3 = Cliente
-- 'contrasena' representa un hash (ej. bcrypt), aquí solo es un valor de ejemplo.
-- =====================================================================
-- DATOS DE EJEMPLO PARA PICKCLOUD - TIENDA DE ABARROTES "LA ESQUINA"
-- Congruente con reglas de negocio (RN-01 a RN-26) y modelo E-R (5.6)
--
-- USO: copia y pega TODO este archivo de una sola vez en tu cliente de
-- PostgreSQL (psql, pgAdmin, DBeaver...). Está envuelto en una sola
-- transacción (BEGIN/COMMIT) y arranca con TRUNCATE ... RESTART IDENTITY
-- para garantizar que los IDs siempre empiecen en 1, sin importar qué
-- datos tenías antes.
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------------
-- 0. LIMPIEZA: vacía todas las tablas y reinicia los contadores IDENTITY
--    a 1. CASCADE se encarga de las dependencias sin importar el orden.
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
-- 'contrasena' representa un hash (ej. bcrypt), aquí solo es un valor de ejemplo.
INSERT INTO usuarios (id_rol, nombre, correo, contrasena) VALUES
(1, 'Marisol Hernández', 'marisol.admin@laesquina.com', '$2b$10$examplehash.admin.0001'),   -- id_usuario 1: Admin
(2, 'Jorge Ramírez', 'jorge.empleado@laesquina.com', '$2b$10$examplehash.emple.0002'),      -- id_usuario 2: Empleado
(2, 'Ana Cortés', 'ana.empleado@laesquina.com', '$2b$10$examplehash.emple.0003'),           -- id_usuario 3: Empleado
(3, 'Roberto Sánchez', 'roberto.cliente@gmail.com', '$2b$10$examplehash.cliente.0004'),     -- id_usuario 4: Cliente
(3, 'Daniela Torres', 'daniela.cliente@gmail.com', '$2b$10$examplehash.cliente.0005'),      -- id_usuario 5: Cliente
(3, 'Emilio Vargas', 'emilio.cliente@gmail.com', '$2b$10$examplehash.cliente.0006');        -- id_usuario 6: Cliente

-- ---------------------------------------------------------------------
-- 3. CONFIGURACION_NEGOCIO (siempre asociada a un administrador)
-- ---------------------------------------------------------------------
INSERT INTO configuracion_negocio (id_usuario, nombre_comercial, direccion, logo_url, stock_minimo_defecto) VALUES
(1, 'Abarrotes La Esquina', 'Av. Insurgentes Sur 1234, CDMX', 'https://cdn.laesquina.com/logo.png', 5);

-- ---------------------------------------------------------------------
-- 4. TOKEN_RECUPERACION (ej. cliente solicitando restablecer contraseña)
-- ---------------------------------------------------------------------
INSERT INTO token_recuperacion (id_usuario, token, expiracion, usado) VALUES
(4, 'a1b2c3d4-e5f6-7890-recuperacion-token', CURRENT_TIMESTAMP + INTERVAL '1 day', FALSE);

-- ---------------------------------------------------------------------
-- 5. CATEGORIA
-- ---------------------------------------------------------------------
INSERT INTO categoria (descripcion) VALUES
('Abarrotes básicos'),
('Bebidas'),
('Lácteos'),
('Panadería'),
('Limpieza del hogar'),
('Botanas y dulces'),
('Higiene personal');

-- ---------------------------------------------------------------------
-- 6. PRODUCTO
-- ---------------------------------------------------------------------
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
('Efectivo'),
('Transferencia'),
('Tarjeta');

-- =====================================================================
-- VENTAS (mostrador físico) — id_usuario = EMPLEADO o ADMIN que registró
-- (RN-11, RF-08, RN-25)
-- =====================================================================
INSERT INTO venta (id_usuario, id_metodo_pago, fecha, subtotal, descuento, total) VALUES
(2, 1, '2026-07-28 09:15:00', 58.00, 0.00, 58.00),
(3, 3, '2026-07-28 11:40:00', 104.00, 5.20, 98.80),
(2, 1, '2026-07-29 16:20:00', 45.00, 0.00, 45.00),
(1, 2, '2026-07-30 10:05:00', 130.00, 13.00, 117.00);

INSERT INTO venta_producto (id_venta, id_producto, cantidad) VALUES
(1, 1, 1),
(1, 6, 2),
(2, 8, 2),
(2, 9, 1),
(2, 12, 1),
(3, 5, 1),
(3, 15, 1),
(3, 16, 1),
(4, 14, 2),
(4, 13, 1),
(4, 17, 2);

-- ---------------------------------------------------------------------
-- COMPROBANTE (uno por venta)
-- ---------------------------------------------------------------------
INSERT INTO comprobante (id_venta, url_pdf, fecha_emision) VALUES
(1, 'https://cdn.laesquina.com/comprobantes/venta_1.pdf', '2026-07-28 09:15:05'),
(2, 'https://cdn.laesquina.com/comprobantes/venta_2.pdf', '2026-07-28 11:40:05'),
(3, 'https://cdn.laesquina.com/comprobantes/venta_3.pdf', '2026-07-29 16:20:05'),
(4, 'https://cdn.laesquina.com/comprobantes/venta_4.pdf', '2026-07-30 10:05:05');

-- =====================================================================
-- PEDIDOS (PickUp) — id_usuario = CLIENTE que lo generó (RN-13, RN-14)
-- estado: pendiente, pagado, listo, entregado o cancelado (RN-15)
-- =====================================================================
INSERT INTO pedido (id_usuario, id_metodo_pago, estado, fecha_creacion, fecha_recoleccion, total) VALUES
(4, 3, 'listo', '2026-07-29 08:00:00', NULL, 76.00),
(5, 1, 'entregado', '2026-07-29 12:30:00', '2026-07-29 13:15:00', 90.00),
(6, 2, 'pendiente', '2026-07-31 09:20:00', NULL, 54.00);

INSERT INTO pedido_producto (id_pedido, id_producto, cantidad) VALUES
(1, 7, 2),
(1, 10, 1),
(2, 1, 1),
(2, 2, 1),
(2, 4, 1),
(3, 11, 6),
(3, 12, 1),
(3, 18, 2);

-- ---------------------------------------------------------------------
-- NOTIFICACIONES (avisos al cliente por cambio de estado de pedido)
-- ---------------------------------------------------------------------
INSERT INTO notificaciones (id_pedido, id_usuario, tipo, intentos) VALUES
(1, 4, 'pedido_listo_para_recoger', 1),
(2, 5, 'pedido_entregado', 1),
(3, 6, 'pedido_recibido', 1);

-- ---------------------------------------------------------------------
-- REPORTES (solo administradores generan/consultan reportes)
-- ---------------------------------------------------------------------
INSERT INTO reportes (id_usuario, tipo, fecha_generacion, fecha_inicio, fecha_fin, url_pdf, metodo_pago) VALUES
(1, 'ventas_semanal', '2026-07-30 20:00:00', '2026-07-27 00:00:00', '2026-08-02 23:59:59', 'https://cdn.laesquina.com/reportes/ventas_semanal_1.pdf', NULL),
(1, 'pedidos_pickup_semanal', '2026-07-30 20:05:00', '2026-07-27 00:00:00', '2026-08-02 23:59:59', 'https://cdn.laesquina.com/reportes/pedidos_semanal_1.pdf', NULL),
(1, 'ventas_por_metodo_pago', '2026-07-30 20:10:00', '2026-07-27 00:00:00', '2026-08-02 23:59:59', 'https://cdn.laesquina.com/reportes/ventas_efectivo_1.pdf', 'Efectivo');

-- ---------------------------------------------------------------------
-- PEDIDO_REPORTE / VENTA_REPORTE
-- ---------------------------------------------------------------------
INSERT INTO pedido_reporte (id_pedido, id_reporte) VALUES
(1, 2),
(2, 2),
(3, 2);

INSERT INTO venta_reporte (id_venta, id_reporte) VALUES
(1, 1),
(2, 1),
(3, 1),
(4, 1),
(1, 3);

COMMIT;