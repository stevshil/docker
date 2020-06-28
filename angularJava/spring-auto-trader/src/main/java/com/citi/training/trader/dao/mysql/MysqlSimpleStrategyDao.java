package com.citi.training.trader.dao.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
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

import com.citi.training.trader.dao.SimpleStrategyDao;
import com.citi.training.trader.model.SimpleStrategy;
import com.citi.training.trader.model.Stock;

/**
 * JDBC MySQL DAO implementation for simple_strategy table.
 *
 */
@Component
public class MysqlSimpleStrategyDao implements SimpleStrategyDao {

    private static final Logger logger =
                            LoggerFactory.getLogger(MysqlSimpleStrategyDao.class);

    private static String FIND_ALL_SQL = "select simple_strategy.id as strategy_id, stock.id as stock_id, stock.ticker, " +
                                         "size, exit_profit_loss, current_position, last_trade_price, profit, stopped " +
                                         "from simple_strategy left join stock on stock.id = simple_strategy.stock_id";

    private static String INSERT_SQL = "INSERT INTO simple_strategy (stock_id, size, exit_profit_loss, current_position, " +
                                       "last_trade_price, profit, stopped) " +
                                       "values (:stock_id, :size, :exit_profit_loss, :current_position, "+ 
                                       ":last_trade_price, :profit, :stopped)";

    private static String UPDATE_SQL = "UPDATE simple_strategy set stock_id=:stock_id, size=:size, " +
                                       "exit_profit_loss=:exit_profit_loss, current_position=:current_position, " +
                                       "last_trade_price=:last_trade_price, profit=:profit, stopped=:stopped where id=:id";

    @Autowired
    private JdbcTemplate tpl;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<SimpleStrategy> findAll(){
        logger.debug("findAll SQL: [" + FIND_ALL_SQL + "]");
        return tpl.query(FIND_ALL_SQL,
                         new SimpleStrategyMapper());
    }

    public int save(SimpleStrategy strategy) {
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();

        namedParameters.addValue("stock_id", strategy.getStock().getId());
        namedParameters.addValue("size", strategy.getSize());
        namedParameters.addValue("exit_profit_loss", strategy.getExitProfitLoss());
        namedParameters.addValue("current_position", strategy.getCurrentPosition());
        namedParameters.addValue("last_trade_price", strategy.getLastTradePrice());
        namedParameters.addValue("profit", strategy.getProfit());
        namedParameters.addValue("stopped", strategy.getStopped());

        if(strategy.getId() < 0) {
            logger.debug("Inserting simpleStrateg: " + strategy);

            KeyHolder keyHolder = new GeneratedKeyHolder();

            namedParameterJdbcTemplate.update(INSERT_SQL, namedParameters, keyHolder);
            strategy.setId(keyHolder.getKey().intValue());
        } else {
            logger.debug("Updating simpleStrategy: " + strategy);
            namedParameters.addValue("id", strategy.getId());
            namedParameterJdbcTemplate.update(UPDATE_SQL, namedParameters);
        }

        logger.debug("Saved trade: " + strategy);
        return strategy.getId();
    }

    /**
     * private internal class to map database rows to SimpleStrategy objects.
     *
     */
    private static final class SimpleStrategyMapper implements RowMapper<SimpleStrategy> {
        public SimpleStrategy mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.debug("Mapping simple_strategy result set row num [" + rowNum + "], id : [" +
                         rs.getInt("strategy_id") + "]");

            return new SimpleStrategy(rs.getInt("strategy_id"),
                             new Stock(rs.getInt("stock_id"),
                                       rs.getString("stock.ticker")),
                             rs.getInt("size"),
                             rs.getDouble("exit_profit_loss"),
                             rs.getInt("current_position"),
                             rs.getDouble("last_trade_price"),
                             rs.getDouble("profit"),
                             rs.getDate("stopped"));
        }
    }
}
