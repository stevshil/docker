package com.citi.training.trader.strategy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.citi.training.trader.dao.PriceDao;
import com.citi.training.trader.dao.SimpleStrategyDao;
import com.citi.training.trader.messaging.TradeSender;
import com.citi.training.trader.model.Price;
import com.citi.training.trader.model.SimpleStrategy;
import com.citi.training.trader.model.Trade;

@Profile("!no-scheduled")
@Component
public class SimpleStrategyAlgorithm implements StrategyAlgorithm {

    private static final Logger logger =
                LoggerFactory.getLogger(SimpleStrategyAlgorithm.class);

    @Autowired
    private PriceDao priceDao;

    @Autowired
    private TradeSender tradeSender;

    @Autowired
    private SimpleStrategyDao strategyDao;

    @Scheduled(fixedRateString = "${simple.strategy.refresh.rate_ms:5000}")
    public void run() {

        for(SimpleStrategy strategy: strategyDao.findAll()) {
            if(strategy.getStopped() != null) {
                continue;
            }

            if(!strategy.hasPosition()) {
                // we have no open position

                // get latest two prices for this stock
                List<Price> prices = priceDao.findLatest(strategy.getStock(), 2);

                if(prices.size() < 2) {
                    logger.warn("Unable to execute strategy, not enough price data: " +
                                strategy); 
                    continue;
                }

                logger.debug("Taking position based on prices:");
                for(Price price: prices) {
                    logger.debug(price.toString());
                }

                // if price going down => take short => sell
                double currentPriceChange = prices.get(0).getPrice() - prices.get(1).getPrice();
                logger.debug("Current Price change: " + currentPriceChange);

                if(currentPriceChange < 0.001 && currentPriceChange > -0.001) {
                    logger.debug("Insufficient price change, taking no action");
                    continue;
                }
                if(currentPriceChange < 0) {
                    logger.debug("Taking short position for strategy: " + strategy);
                    strategy.takeShortPosition();
                    strategy.setLastTradePrice(makeTrade(strategy, Trade.TradeType.SELL));
                } else {
                    // if price going up => take long => buy
                    logger.debug("Taking long position for strategy: " + strategy);
                    strategy.takeLongPosition();
                    strategy.setLastTradePrice(makeTrade(strategy, Trade.TradeType.BUY));
                }

            } else if(strategy.hasLongPosition()) {
                // we have a long position on this stock
                // close the position by selling
                logger.debug("Closing long position for strategy: " + strategy);

                double thisTradePrice = makeTrade(strategy, Trade.TradeType.SELL);
                logger.debug("Bought at: " + strategy.getLastTradePrice() + ", sold at: " +
                             thisTradePrice);
                closePosition(thisTradePrice - strategy.getLastTradePrice(), strategy);

            } else if(strategy.hasShortPosition()) {
                // we have a short position on this stock
                // close the position by buying
                logger.debug("Closing short position for strategy: " + strategy);

                double thisTradePrice = makeTrade(strategy, Trade.TradeType.BUY);
                logger.debug("Sold at: " + strategy.getLastTradePrice() + ", bought at: " +
                             thisTradePrice);

                closePosition(strategy.getLastTradePrice() - thisTradePrice, strategy);
            }
            strategyDao.save(strategy);
        }
    }

    private void closePosition(double profitLoss, SimpleStrategy strategy) {
        logger.debug("Recording profit/loss of: " + profitLoss +
                     " for strategy: " + strategy);
        strategy.addProfitLoss(profitLoss);
        strategy.closePosition();
    }


    private double makeTrade(SimpleStrategy strategy, Trade.TradeType tradeType) {
        Price currentPrice = priceDao.findLatest(strategy.getStock(), 1).get(0);
        tradeSender.sendTrade(new Trade(strategy.getStock(), currentPrice.getPrice(),
                                        strategy.getSize(), tradeType,
                                        strategy));
        return currentPrice.getPrice();
    }

}
