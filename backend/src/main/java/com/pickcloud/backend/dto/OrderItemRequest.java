package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents one product requested as part of an order.
 * The client only provides the product identifier and desired quantity.
 * Prices and subtotals are calculated by the backend using database values.
 */
@Getter
@Setter
public class OrderItemRequest {

    private Integer idProducto;
    private Integer cantidad;
}