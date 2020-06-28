CREATE TABLE IF NOT EXISTS stock
(
  `id` INT NOT NULL AUTO_INCREMENT,
  `ticker` VARCHAR(8) NOT NULL,
  UNIQUE(`ticker`),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `trade`
(
  `id` INT NOT NULL AUTO_INCREMENT,
  `stock_id` INT NOT NULL,
  `price` DOUBLE NOT NULL,
  `size` INT NOT NULL,
  `buy` BOOLEAN NOT NULL,
  `state` VARCHAR(20),
  `last_state_change` DATETIME NOT NULL,
  `created` DATETIME NOT NULL,
  CONSTRAINT `trade_stock_foreign_key`
    FOREIGN KEY (`stock_id`) REFERENCES stock (`id`),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `simple_strategy`
(
  `id` INT NOT NULL AUTO_INCREMENT,
  `stock_id` INT NOT NULL,
  `size` INT NOT NULL,
  `exit_profit_loss` DOUBLE NOT NULL,
  `current_position` INT DEFAULT 0,
  `last_trade_price` DOUBLE,
  `profit` DOUBLE,
  `stopped` DATETIME,
  CONSTRAINT `strategy_stock_foreign_key`
    FOREIGN KEY (`stock_id`) REFERENCES stock (`id`),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS price
(
  `id` INT NOT NULL AUTO_INCREMENT,
  `stock_id` INT NOT NULL,
  `price` DOUBLE,
  `recorded_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `price_stock`
    FOREIGN KEY (`stock_id`) REFERENCES stock (`id`)
);
