package com.citi.training.trader.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.citi.training.trader.service.StockService;
import com.citi.training.trader.service.TradeService;
import com.citi.training.trader.model.Trade;

/**
 * Service class that will listen on JMS queue for Trades and put
 * returned trades into the database.
 *
 */
@Component
public class TradeReceiver {

    private static final Logger logger =
                    LoggerFactory.getLogger(TradeReceiver.class);

    @Autowired
    private TradeService tradeService;

    @Autowired
    private StockService stockService;

    @JmsListener(destination = "${jms.sender.responseQueueName:OrderBroker_Reply}")
    public void receiveTrade(String xmlReply) {

        logger.debug("Processing OrderBroker Replies");

        Trade tradeReply = Trade.fromXml(xmlReply);

        logger.debug("Parsed returned trade: " + tradeReply);
        tradeReply.setStock(stockService.findByTicker(tradeReply.getTempStockTicker()));

        if (xmlReply.contains("<result>REJECTED</result>")) {
            tradeReply.stateChange(Trade.TradeState.REJECTED);
            tradeService.save(tradeReply);
        } else if (xmlReply.contains("<result>Partially_Filled</result>")) {
            tradeReply.stateChange(Trade.TradeState.INIT);
            tradeService.save(tradeReply);
        } else {
            tradeReply.stateChange(Trade.TradeState.FILLED);
            tradeService.save(tradeReply);
        }
    }
}
