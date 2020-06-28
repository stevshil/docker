package com.citi.training.trader.model;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@XmlRootElement(name = "trade")
@XmlAccessorType(XmlAccessType.FIELD)

// Ignore properties we created for Xml marshalling
@JsonIgnoreProperties({ "tradeTypeXml", "stateXml", "lastStateChangeXml" })
public class Trade {
    private static final Logger log = LoggerFactory.getLogger(Trade.class);

    // Static objects for marshalling to/from XML
    private static JAXBContext jaxbContext = null;
    private static Unmarshaller unmarshaller = null;
    private static Marshaller marshaller = null;

    static {
        try {
            jaxbContext = JAXBContext.newInstance(Trade.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (JAXBException e) {
            log.error("Unable to create Trade JAXB elements");
        }
    }

    public enum TradeType {
        BUY("true"), SELL("false");

        private final String xmlString;

        private TradeType(String xmlString) {
            this.xmlString = xmlString;
        }

        public String getXmlString() {
            return this.xmlString;
        }

        public static TradeType fromXmlString(String xmlValue) {
            switch (xmlValue.toLowerCase()) {
            case "true":
                return TradeType.BUY;
            case "false":
                return TradeType.SELL;
            default:
                throw new IllegalArgumentException(xmlValue);
            }
        }
    }

    public enum TradeState {
        INIT, WAITING_FOR_REPLY, FILLED, PARTIALLY_FILLED,
        CANCELED, DONE_FOR_DAY, REJECTED;
    }

    private int id = -1;
    private double price;
    private int size;

    // ignore these fields in xml, we will use the getters/setters for xml
    @XmlTransient
    private Stock stock;

    @XmlTransient
    private String tempStockTicker;

    @XmlTransient
    private SimpleStrategy strategy;

    @XmlTransient
    private LocalDateTime lastStateChange = LocalDateTime.now();

    @XmlTransient
    private TradeType tradeType;

    @XmlTransient
    private TradeState state = TradeState.INIT;

    public Trade() {}

    public Trade(Stock stock, double price, int size,
            TradeType tradeType, SimpleStrategy strategy) {
        this(-1, stock, price, size, tradeType,
             TradeState.INIT, LocalDateTime.now(), strategy);
    }
    public Trade(int id, Stock stock, double price, int size,
                 String tradeType, String tradeState, LocalDateTime lastStateChange,
                 SimpleStrategy strategy) {
        this(id, stock, price, size, TradeType.valueOf(tradeType),
             TradeState.valueOf(tradeState), lastStateChange, strategy);
    }

    public Trade(int id, Stock stock, double price, int size,
                 TradeType tradeType, TradeState tradeState,
                 LocalDateTime lastStateChange, SimpleStrategy strategy) {
        this.id = id;
        this.stock = stock;
        this.price = price;
        this.size = size;
        this.strategy = strategy;
        this.setState(tradeState);
        this.setLastStateChange(lastStateChange);
        this.setTradeType(tradeType);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @XmlElement(name = "stock")
    public String getStockTicker() {
        return stock.getTicker();
    }

    public void setStockTicker(String ticker) {
        this.tempStockTicker = ticker;
    }

    public String getTempStockTicker() {
        return tempStockTicker;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    // methods for mashalling/unmarshalling tradeType
    @XmlElement(name = "buy")
    public String getTradeTypeXml() {
        return this.tradeType.getXmlString();
    }

    public void setTradeTypeXml(String tradeTypeStr) {
        this.tradeType = TradeType.fromXmlString(tradeTypeStr);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public SimpleStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(SimpleStrategy strategy) {
        this.strategy = strategy;
    }

    public LocalDateTime getLastStateChange() {
        return lastStateChange;
    }

    public void setLastStateChange(LocalDateTime lastStateChange) {
        this.lastStateChange = lastStateChange;
    }

    // methods for mashalling/unmarshalling lastStateChange
    @XmlElement(name = "whenAsDate")
    public String getLastStateChangeXml() {
        return lastStateChange.toString();
    }

    public void setLastStateChangeXml(String lastStateChange) {
        this.lastStateChange = LocalDateTime.parse(lastStateChange);
    }

    public TradeState getState() {
        return state;
    }

    public void setState(TradeState state) {
        this.state = state;
    }

    // to be called when state is changing (setState used for obj creation etc..)
    public void stateChange(TradeState newState) {
        this.state = newState;
        this.lastStateChange = LocalDateTime.now();
    }

    // methods for mashalling/unmarshalling state
    @XmlElement(name = "result")
    public TradeState getStateXml() {
        // don't include state when marshalling obj=>xml
        return null;
    }

    public void setStateXml(TradeState state) {
        stateChange(state);
    }

    @Override
    public String toString() {
        String returnVal =
               "Trade [id=" + id + ", stock=" + stock + ", tradeType=" + tradeType +
               ", price=" + price + ", size=" + size +
               ", state=" + state + ", lastStateChange=" + lastStateChange +
               ", strategyId=";

        if(strategy != null) {
            returnVal += (strategy.getId());
        } else {
            returnVal += ("null");
        }
        return returnVal;
    }

    @Override
    public boolean equals(Object trade) {
        if (trade == this) {
            return true;
        }

        // This implementation allows ids to be different
        return trade != null && (trade instanceof Trade) && getStock().equals(((Trade) trade).getStock())
                && getTradeType() == ((Trade) trade).getTradeType() && getPrice() == ((Trade) trade).getPrice()
                && getSize() == ((Trade) trade).getSize() && getState() == ((Trade) trade).getState()
                && getLastStateChange().equals(((Trade) trade).getLastStateChange())
                && getStrategy() == ((Trade) trade).getStrategy();
    }

    /**
     * Converts from Trade object to XML representation.
     * @param trade the Trade object to convert.
     * @return the XML String representation of this trade.
     */
    public static String toXml(Trade trade) {
        StringWriter stringWriter = new StringWriter();

        try {
            marshaller.marshal(trade, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            log.error("Unabled to marshall trade: " + trade);
            log.error("JAXBException: " + e);
        }
        return null;
    }

    /**
     * Converts XML representation of trade to Trade object.
     * @param xmlString representation of a trade
     * @return Trade object representation of the trade
     */
    public static Trade fromXml(String xmlString) {
        try {
            return (Trade) unmarshaller.unmarshal(new StringReader(xmlString));
        } catch (JAXBException e) {
            log.error("Unable to unmarshal trade: " + xmlString);
            log.error("JAXBException: " + e);
        }
        return null;
    }
}