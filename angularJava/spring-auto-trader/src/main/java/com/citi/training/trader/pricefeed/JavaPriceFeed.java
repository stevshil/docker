package com.citi.training.trader.pricefeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.citi.training.trader.model.Stock;

/**
 * Gathers price data from mock java feed.
 *
 */
@Profile("alt-feed")
@Component
public class JavaPriceFeed implements PriceFeed {

    private static final Logger logger =
                            LoggerFactory.getLogger(JavaPriceFeed.class);

    @Value("${java.price.feed.url}")
    private String priceFeedUrl;
 

    public double getLatestPrice(Stock stock) {
         RestTemplate restTemplate = new RestTemplate();

        // Note this is only asking for 1 price per request - inefficient
        String url  = priceFeedUrl + "/prices/" + stock.getTicker() + "?periods=1";

        logger.debug("Requesting price: [" + url + "]");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        logger.debug("Received price data: [" + response.getBody() + "]");
        return parsePricePoint(response.getBody().split("\n")[1]);
    }

    public static double parsePricePoint(String CSV) {
        String[] fields = CSV.split(",");
        if (fields.length < 6) {
            throw new IllegalArgumentException
                ("There must be at least 6 comma-separated fields: '" + CSV + "'");
        }

        try {
            double close = Double.parseDouble(fields[4]);
            return close;
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't parse timestamp.", ex);
        }
    }
}
