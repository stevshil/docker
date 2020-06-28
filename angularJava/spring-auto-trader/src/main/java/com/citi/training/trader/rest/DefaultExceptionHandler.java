package com.citi.training.trader.rest;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.citi.training.trader.exceptions.StockNotFoundException;

/**
 * Exception handler applied to all REST Controllers.
 *
 */
@ControllerAdvice
@Priority(20)
public class DefaultExceptionHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(value = {StockNotFoundException.class})
    public ResponseEntity<Object> stockNotFoundExceptionHandler(
            HttpServletRequest request, StockNotFoundException ex) {
        logger.warn(ex.toString());
        return new ResponseEntity<>("{\"message\": \"" + ex.getMessage() + "\"}",
                                    HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> illegalArgumentExceptionHandler(
            HttpServletRequest request, IllegalArgumentException ex) {
        logger.warn(ex.toString());
        return new ResponseEntity<>("{\"message\": \"" + ex.getMessage() + "\"}",
                                    HttpStatus.BAD_REQUEST);
    }
}