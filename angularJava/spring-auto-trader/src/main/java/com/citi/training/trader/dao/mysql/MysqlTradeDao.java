package com.citi.training.trader.dao.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

import com.citi.training.trader.dao.TradeDao;
import com.citi.training.trader.exceptions.TradeNotFoundException;
import com.citi.training.trader.model.Stock;
import com.citi.training.trader.model.Trade;

/**
 * JDBC MySQL DAO implementation for trade table.
 *
 */
@Component
public class MysqlTradeDao implements TradeDao {

    private static final Logger logger =
                            LoggerFactory.getLogger(MysqlTradeDao.class);

    private static String FIND_ALL_SQL = "select trade.id as trade_id, stock.id as stock_id, stock.ticker, price, " +
                                         "size, buy, state, last_state_change, created from trade left join stock on stock.id = trade.stock_id";

    private static String FIND_SQL = FIND_ALL_SQL + " where trade.id = ?";

    private static String FIND_BY_STATE_SQL = FIND_ALL_SQL + " where state = ?";

    private static String INSERT_SQL = "INSERT INTO trade (stock_id, price, size, buy, state, last_state_change, created) " +
                                       "values (:stock_id, :price, :size, :buy, :state, :last_state_change, :created)";

    private static String UPDATE_SQL = "UPDATE trade SET stock_id=:stock_id, price=:price, size=:size, " +
                                       "buy=:buy, state=:state, last_state_change=:last_state_change WHERE id=:id";

    private static String DELETE_SQL = "delete from trade where id=?";

    @Autowired
    private JdbcTemplate tpl;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Trade> findAll(){
        logger.debug("findAll SQL: [" + FIND_ALL_SQL + "]");
        return tpl.query(FIND_ALL_SQL,
                         new TradeMapper());
    }

    @Override
    public Trade findById(int id) {
        logger.debug("findBydId(" + id + ") SQL: [" + FIND_SQL + "]");
        List<Trade> Trades = this.tpl.query(FIND_SQL,
                new Object[]{id},
                new TradeMapper()
        );
        if(Trades.size() <= 0) {
            String warnMsg = "Requested Trade not found, id: " + id;
            logger.warn(warnMsg);
            throw new TradeNotFoundException(warnMsg);
        }
        if(Trades.size() > 1) {
            logger.warn("Found more than one Trade with id: " + id);
        }
        return Trades.get(0);
    }


    @Override
    public List<Trade> findAllByState(Trade.TradeState state) {
        logger.debug("findAllByState(" + state + ") SQL: [" + FIND_BY_STATE_SQL + "]");
        List<Trade> Trades = this.tpl.query(FIND_BY_STATE_SQL,
                new Object[]{state.toString()},
                new TradeMapper()
        );
        return Trades;
    }

    public int save(Trade trade) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        namedParameters.addValue("stock_id", trade.getStock().getId());
        namedParameters.addValue("price", trade.getPrice());
        namedParameters.addValue("size", trade.getSize());
        namedParameters.addValue("buy", trade.getTradeType().equals(Trade.TradeType.BUY) ? 1 : 0);
        namedParameters.addValue("state", trade.getState().toString());
        namedParameters.addValue("last_state_change", trade.getLastStateChange());

        if (trade.getId() < 0) {
            logger.debug("Inserting trade: " + trade);
            namedParameters.addValue("created", LocalDateTime.now());

            KeyHolder keyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(INSERT_SQL, namedParameters, keyHolder);
            trade.setId(keyHolder.getKey().intValue());
        } else {
            logger.debug("Updating trade: " + trade);
            namedParameters.addValue("id", trade.getId());
            namedParameterJdbcTemplate.update(UPDATE_SQL, namedParameters);
        }

        logger.debug("Saved trade: " + trade);
        return trade.getId();
    }

    @Override
    public void deleteById(int id) {
        logger.debug("deleteById(" + id + ") SQL: [" + DELETE_SQL +"]");
        if(this.tpl.update(DELETE_SQL, id) <= 0) {
            String warnMsg = "Failed to delete, trade not found: " + id;
            logger.warn(warnMsg);
            throw new TradeNotFoundException(warnMsg);
        }
        else {
            for(Trade trade: findAll()) {
                logger.debug(trade.toString());
            }
        }
    }

    /**
     * private internal class to map database rows to Trade objects.
     *
     */
    private static final class TradeMapper implements RowMapper<Trade> {
        public Trade mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.debug("Mapping trade result set row num [" + rowNum + "], id : [" +
                         rs.getInt("trade_id") + "]");

            return new Trade(rs.getInt("trade_id"),
                             new Stock(rs.getInt("stock_id"),
                                       rs.getString("stock.ticker")),
                             rs.getDouble("price"),
                             rs.getInt("size"),
                             rs.getInt("buy") == 1 ? "BUY" : "SELL",
                             rs.getString("state"),
                             (LocalDateTime)rs.getObject("last_state_change", LocalDateTime.class),
                             null);
        }
    }
}
