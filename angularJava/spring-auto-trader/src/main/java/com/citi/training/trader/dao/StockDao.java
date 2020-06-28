package com.citi.training.trader.dao;

import java.util.List;

import com.citi.training.trader.model.Stock;

public interface StockDao {

    int create(Stock stock);

    List<Stock> findAll();

    Stock findById(int id);

    Stock findByTicker(String ticker);

    void deleteById(int id);
}
