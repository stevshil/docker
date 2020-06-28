package com.citi.training.trader.exceptions;

/**
 * An exception to be thrown by {@link com.citi.trading.dao.StockDao} implementations
 * when a requested stock is not found.
 */
@SuppressWarnings("serial")
public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String msg) {
        super(msg);
    }
}
