package com.citi.training.trader.model;

import java.util.Date;

public class Price {

    private int id;
    private Stock stock;
    private double price;
    private Date recordedAt;

    public Price(Stock stock, double price) {
        this(-1, stock, price, new Date());
    }

    public Price(Stock stock, double price, Date recordedAt) {
        this(-1, stock, price, recordedAt);
    }

    public Price(int id, Stock stock, double price, Date recordedAt) {
        this.id = id;
        this.stock = stock;
        this.price = price;
        this.recordedAt = recordedAt;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(Date recordedAt) {
        this.recordedAt = recordedAt;
    }

    @Override
    public String toString() {
        return "Price [id=" + id + ", stock=" + stock + ", price=" + price +
               ", recordedAt=" + recordedAt + "]";
    }
}
