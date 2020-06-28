package com.citi.training.trader.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.citi.training.trader.service.StockService;
import com.citi.training.trader.model.Stock;

/**
 * REST Controller for {@link com.citi.training.trader.model.Stock} resource.
 *
 */
@RestController
@RequestMapping("${com.citi.training.trader.rest.stock-base-path:/stock}")
public class StockController {

    private static final Logger logger =
                    LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService StockService;

    @RequestMapping(method=RequestMethod.GET,
                    produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Stock> findAll() {
        logger.debug("findAll()");
        return StockService.findAll();
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET,
                    produces=MediaType.APPLICATION_JSON_VALUE)
    public Stock findById(@PathVariable int id) {
        logger.debug("findById(" + id + ")");
        return StockService.findById(id);
    }

    @RequestMapping(method=RequestMethod.POST,
                    consumes=MediaType.APPLICATION_JSON_VALUE,
                    produces=MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Stock> create(@RequestBody Stock stock) {
        logger.debug("create(" + stock + ")");

        stock.setId(StockService.create(stock));
        logger.debug("created stock: " + stock);

        return new ResponseEntity<Stock>(stock, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        logger.debug("deleteById(" + id + ")");
        StockService.deleteById(id);
    }
}
