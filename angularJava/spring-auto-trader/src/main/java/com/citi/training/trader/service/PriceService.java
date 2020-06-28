package com.citi.training.trader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.citi.training.trader.dao.PriceDao;
import com.citi.training.trader.dao.StockDao;
import com.citi.training.trader.model.Price;
import com.citi.training.trader.model.Stock;
import com.citi.training.trader.pricefeed.PriceFeed;

/**
 * Gathers price data from price feed for all stocks in current strategies.
 *
 */
@Profile("!no-scheduled")
@Component
public class PriceService {

    private static final Logger logger =
                            LoggerFactory.getLogger(PriceService.class);

    @Autowired
    private PriceFeed priceFeed;

    @Autowired
    private PriceDao priceDao;

    @Autowired
    private StockDao stockDao;

    @Value("${price.service.feed.url}")
    private String priceFeedUrl;
 
    @Scheduled(fixedRateString = "${price.service.feed_poll_ms:5000}")
    public void readPrices() {
        for(Stock stock: stockDao.findAll()) {
            double price = priceFeed.getLatestPrice(stock);

            logger.debug("Received price: [" + price + "] for stock: " + stock);
            priceDao.create(new Price(stock, price));
        }
    }
}
