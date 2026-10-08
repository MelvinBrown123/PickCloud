package com.pickcloud.backend.repository;

import com.pickcloud.backend.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Provides persistence operations for product associated with orders.
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {

    List<OrderProduct> findByPedido_IdPedido(Integer idPedido);

    @Query("""
    SELECT COALESCE(SUM(op.cantidad), 0)
    FROM OrderProduct op
    WHERE op.producto.idProducto = :idProducto
      AND (
          op.pedido.estado = 'pendiente'
          OR (
              op.pedido.estado = 'listo'
              AND UPPER(op.pedido.metodoPago.descripcion) <> 'PAGO_EN_LINEA'
          )
      )
    """)
    Long getReservedStockByProduct(
            @Param("idProducto") Integer idProducto
    );
}
