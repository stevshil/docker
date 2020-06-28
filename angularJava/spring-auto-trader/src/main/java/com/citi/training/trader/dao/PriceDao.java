package com.citi.training.trader.dao;

import java.util.Date;
import java.util.List;

import com.citi.training.trader.model.Price;
import com.citi.training.trader.model.Stock;

public interface PriceDao {

    int create(Price price);

    List<Price> findAll(Stock stock);

    List<Price> findLatest(Stock stock, int count);

    int deleteOlderThan(Date cutOffTime);
}
