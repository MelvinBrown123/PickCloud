package com.pickcloud.backend.entity;

/**
 * Represents the possible states of a Pickup order.
 */
public enum OrderStatus {

    PENDIENTE("pendiente"),
    PAGADO("pagado"),
    LISTO("listo"),
    ENTREGADO("entregado"),
    CANCELADO("cancelado");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}