package com.citi.training.trader.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.citi.training.trader.dao.StockDao;
import com.citi.training.trader.dao.TradeDao;
import com.citi.training.trader.model.Trade;

@Component
public class TradeService {

    private static final Logger logger =
                    LoggerFactory.getLogger(TradeService.class);
    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private StockDao stockService;

    public int save(Trade trade) {
        // make sure stock id is filled in the trade object
        if(trade.getStock() == null) {
            logger.error("Attempt to save trade with no stock id: " + trade);
            throw new IllegalArgumentException(
                            "Unable to save trade with no stock reference");
        }
        if(trade.getStock().getId() <= 0) {
            trade.setStock(stockService.findByTicker(trade.getStockTicker()));
        }
        return tradeDao.save(trade);
    }

    public List<Trade> findAll() {
        return tradeDao.findAll();
    }

    public Trade findById(int id) {
        return tradeDao.findById(id);
    }

    public List<Trade> findAllByState(Trade.TradeState state) {
        return tradeDao.findAllByState(state);
    }
    public void deleteById(int id) {
        tradeDao.deleteById(id);
    }
}
