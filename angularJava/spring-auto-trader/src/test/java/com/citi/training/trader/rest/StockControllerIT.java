package com.citi.training.trader.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.citi.training.trader.model.Stock;

/**
 * Integration test for Stock REST Interface.
 *
 * Makes HTTP requests to {@link com.citi.training.stockr.rest.StockController}.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2test")
@Transactional
public class StockControllerIT {

    private static final Logger logger =
                        LoggerFactory.getLogger(StockControllerIT.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${com.citi.trading.trader.rest.stock-base-path:/stock}")
    private String stockBasePath;

    @Test
    public void findAll_returnsList() {
        Stock testStock = new Stock(-1, "NFLX");
        restTemplate.postForEntity(stockBasePath,
                                   testStock, Stock.class);

        ResponseEntity<List<Stock>> findAllResponse = restTemplate.exchange(
                                stockBasePath,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<List<Stock>>(){});

        assertEquals(findAllResponse.getStatusCode(), HttpStatus.OK);
        assertEquals(findAllResponse.getBody().get(0).getTicker(), testStock.getTicker());
    }

    @Test
    public void findById_returnsCorrectId() {
        Stock testStock = new Stock(-1, "NFLX");
        ResponseEntity<Stock> createdResponse =
                restTemplate.postForEntity(stockBasePath,
                                           testStock, Stock.class);

        assertEquals(createdResponse.getStatusCode(), HttpStatus.CREATED);

        Stock foundStock = restTemplate.getForObject(
                                stockBasePath + "/" + createdResponse.getBody().getId(),
                                Stock.class);

        assertEquals(foundStock.getId(), createdResponse.getBody().getId());
        assertEquals(foundStock.getTicker(), testStock.getTicker());
    }

    @Test
    public void deleteById_deletes() {
        Stock testStock = new Stock(-1, "NFLX");
        ResponseEntity<Stock> createdResponse =
                restTemplate.postForEntity(stockBasePath,
                                           testStock, Stock.class);

        assertEquals(createdResponse.getStatusCode(), HttpStatus.CREATED);

        Stock foundStock = restTemplate.getForObject(
                                stockBasePath + "/" + createdResponse.getBody().getId(),
                                Stock.class);

        logger.debug("Before delete, findById gives: " + foundStock);
        assertNotNull(foundStock);

        restTemplate.delete(stockBasePath + "/" + createdResponse.getBody().getId());

        ResponseEntity<Stock> response = restTemplate.exchange(
                                stockBasePath + "/" + createdResponse.getBody().getId(),
                                HttpMethod.GET,
                                null,
                                Stock.class);

        logger.debug("After delete, findById response code is: " +
                     response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
