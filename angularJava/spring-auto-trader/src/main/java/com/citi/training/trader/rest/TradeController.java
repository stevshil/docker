package com.citi.training.trader.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.citi.training.trader.service.TradeService;
import com.citi.training.trader.model.Trade;

/**
 * REST Controller for {@link com.citi.training.trader.model.Trade} resource.
 *
 */
@CrossOrigin(origins = "${com.citi.training.trader.cross-origin-host:http://localhost:4200}")
@RestController
@RequestMapping("${com.citi.training.trader.rest.trade-base-path:/trade}")
public class TradeController {

    private static final Logger logger =
                    LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeService TradeService;

    @RequestMapping(method=RequestMethod.GET,
                    produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Trade> findAll() {
        logger.debug("findAll()");
        return TradeService.findAll();
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET,
                    produces=MediaType.APPLICATION_JSON_VALUE)
    public Trade findById(@PathVariable int id) {
        logger.debug("findById(" + id + ")");
        return TradeService.findById(id);
    }

    @RequestMapping(method=RequestMethod.POST,
                    consumes=MediaType.APPLICATION_JSON_VALUE,
                    produces=MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Trade> create(@RequestBody Trade trade) {
        logger.debug("create(" + trade + ")");

        trade.setId(TradeService.save(trade));
        logger.debug("created trade: " + trade);

        return new ResponseEntity<Trade>(trade, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        logger.debug("deleteById(" + id + ")");
        TradeService.deleteById(id);
    }
}
