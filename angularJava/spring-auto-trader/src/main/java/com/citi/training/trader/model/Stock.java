package com.citi.training.trader.model;

public class Stock {

    private int id;
    private String ticker;

    public Stock(int id, String ticker) {
        this.id = id;
        this.ticker = ticker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @Override
    public String toString() {
        return "Stock [id=" + id + ", ticker=" + ticker + "]";
    }
}
