package com.pickcloud.backend.dto;

import lombok.Getter;
import lombok.Setter;
/**
 * Represents a stock movement request.
 * The quantity indicates how many units should be added to
 * or removed from the current product stock.
 */
@Getter
@Setter

public class StockRequest {

    private Integer cantidad;
}
