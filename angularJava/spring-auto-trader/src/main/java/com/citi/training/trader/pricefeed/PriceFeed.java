package com.citi.training.trader.pricefeed;

import com.citi.training.trader.model.Stock;

public interface PriceFeed {

    double getLatestPrice(Stock stock);
}
