-- Migración V2: agrega lo que le falta a `venta` y `pedido_producto` para
-- los módulos de Ventas (CU-05.4) y Pedidos Pickup.
-- El script original V1_PickCloud.sql no tiene columna de estado en `venta`,
-- así que no hay forma de distinguir una venta activa de una cancelada.
-- Pídanle a Lucas que corra esto sobre la base ya creada:

ALTER TABLE venta
    ADD COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'completada';

-- (Opcional pero recomendado) restringir a los valores válidos:
ALTER TABLE venta
    ADD CONSTRAINT chk_venta_estado CHECK (estado IN ('completada', 'cancelada'));

-- venta_producto solo guarda `cantidad`, sin precio. Esto significa que si
-- el precio de un producto cambia despues, el total historico de una venta
-- vieja ya no se puede recalcular correctamente (y los Reportes, cuando los
-- hagan, saldrian mal). El diagrama de clases YA contempla esto con
-- DetalleVenta (precioUnitario, subtotal) — solo falta reflejarlo en el SQL:

ALTER TABLE venta_producto
    ADD COLUMN precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    ADD COLUMN subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00;

-- Mismo problema en pedido_producto (modulo Pedidos Pickup): tampoco guarda
-- el precio del producto al momento de crear el pedido.

ALTER TABLE pedido_producto
    ADD COLUMN precio_unitario DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    ADD COLUMN subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00;
