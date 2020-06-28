package com.citi.training.trader.pricefeed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.citi.training.trader.model.Stock;

/**
 * Gathers price data from yahoo feed.
 *
 */
@Component
public class YahooPriceFeed implements PriceFeed {

    private static final Logger logger =
                            LoggerFactory.getLogger(YahooPriceFeed.class);

    @Value("${yahoo.price.feed.url}")
    private String priceFeedUrl;
 

    public double getLatestPrice(Stock stock) {
         RestTemplate restTemplate = new RestTemplate();

        // Note this is only asking for 1 price per request - inefficient
        String url  = priceFeedUrl + "?s=" + stock.getTicker() + "&f=p0";

        logger.debug("Requesting price: [" + url + "]");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        logger.debug("Received price data: [" + response.getBody() + "]");
        return Double.parseDouble(response.getBody());
    }
}
