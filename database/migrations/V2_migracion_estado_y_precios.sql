-- Migración V2: agrega lo que le falta a `venta` y `pedido_producto` para
-- los módulos de Ventas (CU-05.4) y Pedidos Pickup.
-- El script original V1_PickCloud.sql no tiene columna de estado en `venta`,
-- así que no hay forma de distinguir una venta activa de una cancelada.
-- Pídanle a Lucas que corra esto sobre la base ya creada:

--  ALTER TABLE venta
--    ADD COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'completada';

-- (Opcional pero recomendado) restringir a los valores válidos:
--  ALTER TABLE venta
--    ADD CONSTRAINT chk_venta_estado CHECK (estado IN ('completada', 'cancelada'));

-- venta_producto solo guarda `cantidad`, sin precio. Esto significa que si
-- el precio de un producto cambia despues, el total historico de una venta
-- vieja ya no se puede recalcular correctamente (y los Reportes, cuando los
-- hagan, saldrian mal). El diagrama de clases YA contempla esto con
-- DetalleVenta (precioUnitario, subtotal) — solo falta reflejarlo en el SQL:
-- RESUMEN DE CAMBIOS V2
-- ============================================================
--
-- - Se agregó "precio_unitario" a pedido_producto.
-- - Se agregó "subtotal" a pedido_producto.
-- - Los precios de los pedidos existentes se obtienen de
--   producto.precio_venta.
-- - Se calcularon los subtotales existentes como:
--   cantidad * precio_unitario.
-- - Ambos campos quedaron como NOT NULL.
-- - Se agregó "estado" a venta.
-- - Las ventas existentes se marcaron como "completada".
-- - Se agregó una restricción para permitir únicamente:
--   "completada" o "cancelada".
-- - Se agregó "precio_unitario" a venta_producto.
-- - Se agregó "subtotal" a venta_producto.
-- - Los precios de las ventas existentes se obtienen de
--   producto.precio_venta.
-- - Se calcularon los subtotales existentes como:
--   cantidad * precio_unitario.
-- - Ambos campos quedaron como NOT NULL.
--
-- ============================================================


-- ============================================================
-- 1. PEDIDO_PRODUCTO
-- ============================================================

ALTER TABLE pedido_producto
ADD COLUMN precio_unitario NUMERIC(10,2);

ALTER TABLE pedido_producto
ADD COLUMN subtotal NUMERIC(10,2);


-- Obtener el precio actual del producto para los pedidos
-- existentes y conservarlo como precio histórico.

UPDATE pedido_producto pp
SET precio_unitario = p.precio_venta
FROM producto p
WHERE pp.id_producto = p.id_producto;


-- Calcular subtotal del producto dentro del pedido.

UPDATE pedido_producto
SET subtotal = cantidad * precio_unitario;


-- Los precios y subtotales son obligatorios.

ALTER TABLE pedido_producto
ALTER COLUMN precio_unitario SET NOT NULL;

ALTER TABLE pedido_producto
ALTER COLUMN subtotal SET NOT NULL;


-- ============================================================
-- 2. VENTA
-- ============================================================

ALTER TABLE venta
ADD COLUMN estado VARCHAR(50) NOT NULL DEFAULT 'completada';


-- Restringir los posibles estados de una venta.

ALTER TABLE venta
ADD CONSTRAINT chk_venta_estado
CHECK (estado IN ('completada', 'cancelada'));


-- ============================================================
-- 3. VENTA_PRODUCTO
-- ============================================================

ALTER TABLE venta_producto
ADD COLUMN precio_unitario NUMERIC(10,2);

ALTER TABLE venta_producto
ADD COLUMN subtotal NUMERIC(10,2);


-- Obtener el precio actual del producto para las ventas
-- existentes y conservarlo como precio histórico.

UPDATE venta_producto vp
SET precio_unitario = p.precio_venta
FROM producto p
WHERE vp.id_producto = p.id_producto;


-- Calcular subtotal del producto dentro de la venta.

UPDATE venta_producto
SET subtotal = cantidad * precio_unitario;


-- Los precios y subtotales son obligatorios.

ALTER TABLE venta_producto
ALTER COLUMN precio_unitario SET NOT NULL;

ALTER TABLE venta_producto
ALTER COLUMN subtotal SET NOT NULL;


-- ============================================================
-- 4. VALIDACIONES FINALES
-- ============================================================

-- Verificar pedido_producto

SELECT
    id_pedido_producto,
    id_pedido,
    id_producto,
    cantidad,
    precio_unitario,
    subtotal
FROM pedido_producto
ORDER BY id_pedido_producto;


-- Verificar ventas

SELECT
    id_venta,
    id_usuario,
    id_metodo_pago,
    fecha,
    subtotal,
    descuento,
    total,
    estado
FROM venta
ORDER BY id_venta;


-- Verificar venta_producto

SELECT
    id_venta_producto,
    id_venta,
    id_producto,
    cantidad,
    precio_unitario,
    subtotal
FROM venta_producto
ORDER BY id_venta_producto;


-- Verificar que todas las ventas existentes estén completadas.

SELECT
    estado,
    COUNT(*) AS total
FROM venta
GROUP BY estado
ORDER BY estado;


COMMIT;
