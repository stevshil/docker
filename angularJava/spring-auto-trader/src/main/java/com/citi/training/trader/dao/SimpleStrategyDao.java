package com.citi.training.trader.dao;

import java.util.List;

import com.citi.training.trader.model.SimpleStrategy;

public interface SimpleStrategyDao {

    List<SimpleStrategy> findAll();

    int save(SimpleStrategy strategy);
}
