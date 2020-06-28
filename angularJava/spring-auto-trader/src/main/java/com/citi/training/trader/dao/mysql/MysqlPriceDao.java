package com.citi.training.trader.dao.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.citi.training.trader.dao.PriceDao;
import com.citi.training.trader.model.Price;
import com.citi.training.trader.model.Stock;


/**
 * JDBC MySQL DAO implementation for price table.
 *
 */
@Component
public class MysqlPriceDao implements PriceDao {

    private static final Logger logger =
                            LoggerFactory.getLogger(MysqlPriceDao.class);

    private static String FIND_ALL_SQL = "select price.id as price_id, stock.id as stock_id, stock.ticker, price, " +
                                         "recorded_at from price left join stock on stock.id = price.stock_id";

    private static String FIND_BY_TICKER_SQL = FIND_ALL_SQL + " where stock.ticker = ?";

    private static String FIND_LATEST_SQL = FIND_ALL_SQL + " where stock.ticker = ?" +
                                            " order by -recorded_at limit ?";

    private static String INSERT_SQL = "INSERT INTO price (stock_id, price, recorded_at) " +
                                       "values (:stock_id, :price, :recorded_at)";

    private static String DELETE_OLDER_THAN_SQL = "delete from price where recorded_at > ?";

    @Autowired
    private JdbcTemplate tpl;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Price> findAll(Stock stock){
        logger.debug("findAll SQL: [" + FIND_BY_TICKER_SQL + "]");
        return tpl.query(FIND_ALL_SQL,
                         new Object[] {stock.getTicker()},
                         new PriceMapper());
    }

    @Override
    public List<Price> findLatest(Stock stock, int count) {
        logger.debug("findLatest(" + stock.getTicker() + ", " + count +
                     ") SQL: [" + FIND_LATEST_SQL + "]");
        List<Price> Prices = this.tpl.query(FIND_LATEST_SQL,
                new Object[]{stock.getTicker(), count},
                new PriceMapper()
        );
        return Prices;
    }

    public int create(Price price) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        namedParameters.addValue("stock_id", price.getStock().getId());
        namedParameters.addValue("price", price.getPrice());
        namedParameters.addValue("recorded_at", price.getRecordedAt());

            logger.debug("Inserting price: " + price);
            namedParameters.addValue("created", LocalDateTime.now());

            KeyHolder keyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(INSERT_SQL, namedParameters, keyHolder);
            price.setId(keyHolder.getKey().intValue());


        logger.debug("Saved price: " + price);
        return price.getId();
    }

    @Override
    public int deleteOlderThan(Date cutOffTime) {
        logger.debug("deleteOlderThan(" + cutOffTime + ") SQL: [" + DELETE_OLDER_THAN_SQL +"]");
        
        int numDeleted = this.tpl.update(DELETE_OLDER_THAN_SQL, cutOffTime);
        if(numDeleted <= 0) {
            logger.warn("No price records to delete older than: " + cutOffTime);
        }
        else {
            logger.info("Deleted " + numDeleted +
                        " price records older than: " + cutOffTime);
        }
        return numDeleted;
    }

    /**
     * private internal class to map database rows to Price objects.
     *
     */
    private static final class PriceMapper implements RowMapper<Price> {
        public Price mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.debug("Mapping price result set row num [" + rowNum + "], id : [" +
                         rs.getInt("price_id") + "]");

            return new Price(rs.getInt("price_id"),
                             new Stock(rs.getInt("stock_id"),
                                       rs.getString("stock.ticker")),
                             rs.getDouble("price"),
                             rs.getDate("recorded_at"));
        }
    }
}
