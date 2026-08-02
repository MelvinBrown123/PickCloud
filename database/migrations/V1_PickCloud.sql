CREATE DATABASE pickcloud;

-- Conéctate a la base antes de ejecutar el resto:
-- \c pickcloud

CREATE TABLE rol (
    id_rol INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE usuarios (
    id_usuario INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_rol INT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    CONSTRAINT fk_usuarios_rol
        FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE configuracion_negocio (
    id_configuracion INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario INT NOT NULL,
    nombre_comercial VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    logo_url VARCHAR(500),
    stock_minimo_defecto INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_configuracion_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE token_recuperacion (
    id_token INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_token_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE categoria (
    id_categoria INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    descripcion VARCHAR(150) NOT NULL
);

CREATE TABLE producto (
    id_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_categoria INT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio_costo DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    precio_venta DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    stock_actual INT NOT NULL DEFAULT 0,
    sku VARCHAR(80) NOT NULL UNIQUE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    url_imagen VARCHAR(500),
    descuento DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_producto_stock CHECK (stock_actual >= 0),
    CONSTRAINT chk_producto_precios CHECK (precio_costo >= 0 AND precio_venta >= 0),
    CONSTRAINT chk_producto_descuento CHECK (descuento >= 0 AND descuento <= 100)
);

CREATE TABLE metodo_pago (
 id_metodo_pago INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE venta (
 id_venta INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_usuario INT NOT NULL,
 id_metodo_pago INT NOT NULL,
 fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 subtotal DECIMAL(10,2) NOT NULL DEFAULT 0,
 descuento DECIMAL(10,2) NOT NULL DEFAULT 0,
 total DECIMAL(10,2) NOT NULL DEFAULT 0,
 FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
 FOREIGN KEY(id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE venta_producto(
 id_venta_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_venta INT NOT NULL,
 id_producto INT NOT NULL,
 cantidad INT NOT NULL CHECK(cantidad>0),
 UNIQUE(id_venta,id_producto),
 FOREIGN KEY(id_venta) REFERENCES venta(id_venta) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY(id_producto) REFERENCES producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE pedido(
 id_pedido INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_usuario INT NOT NULL,
 id_metodo_pago INT NOT NULL,
 estado VARCHAR(50) NOT NULL DEFAULT 'pendiente',
 fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 fecha_recoleccion TIMESTAMP,
 total DECIMAL(10,2) NOT NULL DEFAULT 0,
 FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT,
 FOREIGN KEY(id_metodo_pago) REFERENCES metodo_pago(id_metodo_pago) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE pedido_producto(
 id_pedido_producto INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_pedido INT NOT NULL,
 id_producto INT NOT NULL,
 cantidad INT NOT NULL CHECK(cantidad>0),
 UNIQUE(id_pedido,id_producto),
 FOREIGN KEY(id_pedido) REFERENCES pedido(id_pedido) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY(id_producto) REFERENCES producto(id_producto) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE comprobante(
 id_comprobante INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_venta INT NOT NULL UNIQUE,
 url_pdf VARCHAR(500),
 fecha_emision TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY(id_venta) REFERENCES venta(id_venta) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE notificaciones(
 id_notificacion INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_pedido INT NOT NULL,
 id_usuario INT NOT NULL,
 tipo VARCHAR(100) NOT NULL,
 intentos INT NOT NULL DEFAULT 0,
 FOREIGN KEY(id_pedido) REFERENCES pedido(id_pedido) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE reportes(
 id_reporte INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_usuario INT NOT NULL,
 tipo VARCHAR(100) NOT NULL,
 fecha_generacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 fecha_inicio TIMESTAMP,
 fecha_fin TIMESTAMP,
 url_pdf VARCHAR(500),
 metodo_pago VARCHAR(100),
 FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE pedido_reporte(
 id_pedido_reporte INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_pedido INT NOT NULL,
 id_reporte INT NOT NULL,
 UNIQUE(id_pedido,id_reporte),
 FOREIGN KEY(id_pedido) REFERENCES pedido(id_pedido) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY(id_reporte) REFERENCES reportes(id_reporte) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE venta_reporte(
 id_venta_reporte INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 id_venta INT NOT NULL,
 id_reporte INT NOT NULL,
 UNIQUE(id_venta,id_reporte),
 FOREIGN KEY(id_venta) REFERENCES venta(id_venta) ON UPDATE CASCADE ON DELETE CASCADE,
 FOREIGN KEY(id_reporte) REFERENCES reportes(id_reporte) ON UPDATE CASCADE ON DELETE CASCADE
);
