package com.citi.training.trader.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.citi.training.trader.dao.StockDao;
import com.citi.training.trader.exceptions.StockNotFoundException;
import com.citi.training.trader.model.Stock;

/**
 * JDBC MySQL DAO implementation for stock table.
 *
 */
@Component
public class MysqlStockDao implements StockDao {

    private static final Logger logger =
                            LoggerFactory.getLogger(MysqlStockDao.class);

    private static String FIND_ALL_SQL = "select id, ticker from stock";
    private static String FIND_BY_ID_SQL = FIND_ALL_SQL + " where id = ?";
    private static String FIND_BY_TICKER_SQL = FIND_ALL_SQL + " where ticker = ?";
    private static String INSERT_SQL = "insert into stock (ticker) values (?)";
    private static String DELETE_SQL = "delete from stock where id=?";

    @Autowired
    private JdbcTemplate tpl;

    public List<Stock> findAll(){
        logger.debug("findAll SQL: [" + FIND_ALL_SQL + "]");
        return tpl.query(FIND_ALL_SQL,
                         new StockMapper());
    }

    @Override
    public Stock findById(int id) {
        logger.debug("findBydId(" + id + ") SQL: [" + FIND_BY_ID_SQL + "]");
        List<Stock> Stocks = this.tpl.query(FIND_BY_ID_SQL,
                new Object[]{id},
                new StockMapper()
        );
        if(Stocks.size() <= 0) {
            String warnMsg = "Requested Stock not found, id: " + id;
            logger.warn(warnMsg);
            throw new StockNotFoundException(warnMsg);
        }
        if(Stocks.size() > 1) {
            logger.warn("Found more than one Stock with id: " + id);
        }
        return Stocks.get(0);
    }

    @Override
    public Stock findByTicker(String ticker) {
        logger.debug("findByTicker(" + ticker + ") SQL: [" + FIND_BY_TICKER_SQL + "]");
        List<Stock> Stocks = this.tpl.query(FIND_BY_TICKER_SQL,
                new Object[]{ticker},
                new StockMapper()
        );
        if(Stocks.size() <= 0) {
            String warnMsg = "Requested Stock not found, ticker: " + ticker;
            logger.warn(warnMsg);
            throw new StockNotFoundException(warnMsg);
        }
        if(Stocks.size() > 1) {
            logger.warn("Found more than one Stock with ticker: " + ticker);
        }
        return Stocks.get(0);
    }

    @Override
    public int create(Stock stock) {
        logger.debug("create( " + stock + ") SQL: [" + INSERT_SQL + "]");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.tpl.update(
            new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps =
                            connection.prepareStatement(INSERT_SQL,
                            Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, stock.getTicker());
                    return ps;
                }
            },
            keyHolder);
        logger.debug("Created Stock with id: " + keyHolder.getKey().intValue());
        return keyHolder.getKey().intValue();
    }

    @Override
    public void deleteById(int id) {
        logger.debug("deleteById(" + id + ") SQL: [" + DELETE_SQL +"]");
        if(this.tpl.update(DELETE_SQL, id) <= 0) {
            String warnMsg = "Failed to delete, stock not found: " + id;
            logger.warn(warnMsg);
            throw new StockNotFoundException(warnMsg);
        }
        else {
            for(Stock stock: findAll()) {
                logger.debug(stock.toString());
            }
        }
    }

    /**
     * private internal class to map database rows to Stock objects.
     *
     */
    private static final class StockMapper implements RowMapper<Stock> {
        public Stock mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.debug("Mapping stock result set row num [" + rowNum + "], id : [" +
                         rs.getInt("id") + "]");
        
            return new Stock(rs.getInt("id"),
                             rs.getString("ticker"));
        }
    }
}
