package com.citi.training.trader.exceptions;

/**
 * An exception to be thrown by {@link com.citi.trading.dao.TradeDao} implementations
 * when a requested stock is not found.
 *
 */
@SuppressWarnings("serial")
public class TradeNotFoundException extends RuntimeException {
    public TradeNotFoundException(String msg) {
        super(msg);
    }
}
