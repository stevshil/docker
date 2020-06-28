package com.citi.training.trader.dao;

import java.util.List;

import com.citi.training.trader.model.Trade;

public interface TradeDao {

    int save(Trade trade);

    List<Trade> findAll();

    Trade findById(int id);

    List<Trade> findAllByState(Trade.TradeState state);

    void deleteById(int id);
}
