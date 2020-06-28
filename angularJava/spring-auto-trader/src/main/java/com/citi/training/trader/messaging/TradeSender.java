package com.citi.training.trader.messaging;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.citi.training.trader.model.Trade;
import com.citi.training.trader.service.TradeService;

@Component
public class TradeSender {

    private final static Logger logger =
                    LoggerFactory.getLogger(TradeSender.class);

    @Autowired
    private TradeService tradeService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${jms.sender.responseQueueName:OrderBroker_Reply}")
    private String responseQueue;

    public void sendTrade(Trade tradeToSend) {
        logger.debug("Sending Trade " + tradeToSend);
        logger.debug("Trade XML:");
        logger.debug(Trade.toXml(tradeToSend));

        tradeToSend.stateChange(Trade.TradeState.WAITING_FOR_REPLY);
        tradeService.save(tradeToSend);

        jmsTemplate.convertAndSend("OrderBroker", Trade.toXml(tradeToSend), message -> {
            message.setStringProperty("Operation", "update");
            message.setJMSCorrelationID(String.valueOf(tradeToSend.getId()));
            message.setJMSReplyTo(buildReplyTo());
            return message;
        });
    }

    private Destination buildReplyTo() throws JMSException {
        final Session session = jmsTemplate.getConnectionFactory().createConnection()
            .createSession(false, Session.AUTO_ACKNOWLEDGE);
        final Destination queue =
            jmsTemplate.getDestinationResolver().resolveDestinationName(session, responseQueue, false);
        return queue;
      }
}
