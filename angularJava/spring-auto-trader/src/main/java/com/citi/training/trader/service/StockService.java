package com.citi.training.trader.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citi.training.trader.dao.StockDao;
import com.citi.training.trader.model.Stock;

@Component
public class StockService {

    @Autowired
    private StockDao stockDao;

    public int create(Stock stock) {
        return stockDao.create(stock);
    }

    public List<Stock> findAll() {
        return stockDao.findAll();
    }

    public Stock findById(int id) {
        return stockDao.findById(id);
    }

    public Stock findByTicker(String ticker) {
        return stockDao.findByTicker(ticker);
    }

    public void deleteById(int id) {
        stockDao.deleteById(id);
    }
}
