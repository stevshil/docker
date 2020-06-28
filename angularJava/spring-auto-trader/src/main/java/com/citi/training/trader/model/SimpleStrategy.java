package com.citi.training.trader.model;

import java.util.Date;

/**
 * A model object to hold the data for an instance of the SimpleStrategy algorithm.
 *
 * This strategy will trade for maxTrades and then exit.
 * 
 * See {@link com.citi.training.trader.strategy.SimpleStrategyAlgorithm}.
 *
 */
public class SimpleStrategy {

    private static final int DEFAULT_MAX_TRADES = 20;

    private int id;
    private Stock stock;
    private int size;
    private double exitProfitLoss;
    private int currentPosition;
    private double lastTradePrice;
    private double profit;
    private Date stopped;

    public SimpleStrategy(Stock stock, int size) {
        this(-1, stock, size, DEFAULT_MAX_TRADES, 0, 0, 0, null);
    }

    public SimpleStrategy(int id, Stock stock, int size,
                          double exitProfitLoss, int currentPosition,
                          double lastTradePrice, double profit,
                          Date stopped) {
        this.id = id;
        this.stock = stock;
        this.size = size;
        this.exitProfitLoss = exitProfitLoss;
        this.currentPosition = currentPosition;
        this.lastTradePrice = lastTradePrice;
        this.profit = profit;
        this.stopped = stopped;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getExitProfitLoss() {
        return exitProfitLoss;
    }

    public void setExitProfitLoss(double exitProfitLoss) {
        this.exitProfitLoss = exitProfitLoss;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public Date getStopped() {
        return stopped;
    }

    public void setStopped(Date stopped) {
        this.stopped = stopped;
    }

    public void addProfitLoss(double profitLoss) {
        this.profit += profitLoss;
        if(Math.abs(this.profit) >= exitProfitLoss) {
            this.setStopped(new Date());
        }
    }

    public void stop() {
        this.stopped = new Date();
    }

    public boolean hasPosition() {
        return this.currentPosition != 0;
    }

    public boolean hasShortPosition() {
        return this.currentPosition < 0;
    }

    public boolean hasLongPosition() {
        return this.currentPosition > 0;
    }

    public void takeShortPosition() {
        this.currentPosition = -1;
    }

    public void takeLongPosition() {
        this.currentPosition = 1;
    }

    public void closePosition() {
        this.currentPosition = 0;
    }

    @Override
    public String toString() {
        return "Strategy [id=" + id + ", stock=" + stock +
                ", size=" + size + ", maxTrades=" + exitProfitLoss +
                ", currentPosition=" + currentPosition +
                ", stopped=" + stopped + "]";
    }
}
