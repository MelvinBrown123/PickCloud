package com.pickcloud.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Represents one product included in an order.
 * The unit price is stored at order creation time so future changes
 * to the product price do not modify the historical order amount.
 */
@Entity
@Table(
        name = "pedido_producto",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"id_pedido", "id_producto"})
        }
)
@Getter
@Setter
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido_producto")
    private Integer idPedidoProducto;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Order pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Product producto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}