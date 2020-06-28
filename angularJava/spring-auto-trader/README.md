# Algorithmic Trading Application

For your final application-development project, you will work in teams of three to complete and then to improve an application that supports high-frequency stock traders in managing sets of automated/algorithmic trading agents.

There is a list of mandatory tasks, including fixes to known issues with the application and creation of a CI/CD infrastructure that lets us deploy the application to an OpenShift server; and then you can either choose from among suggested application enhancements or propose your own. You will also work with a delegate from production support to complete deployment of the application to UAT and production servers.

You will demonstrate your completed and deployed application at the Tech Fair on the final day of the training program, and will give a presentation showing your command of the application's architecture and supporting technology and methodology.

Instructors will provide support and will also act as your client or customer. You will meet at least once with an instructor for advice on development strategies and technique, and once in order to review feature choices and UI design.

## Background

In a traditional market-analyst workstation, the user monitors stock prices and other data, and manually triggers trades: some number of shares of some stock, taking either a "long" position by buying the stock in hopes of selling it at a higher price or "shorting" the stock by selling it and planning to buy it back at a lower price. In our algorithmic trading workstation, the user instead creates automated traders that have certain marching orders to buy and sell on their own, and then monitors their activity, often stopping unprofitable traders, creating new ones, and tuning operating parameters.

Once activated, an algorithmic trader will trade more frequently than a human trader who makes each buy/sell decision and triggers each trade manually. For purposes of developing, testing, and especially demonstrating our application, we speed things up even further: stock pricing is available at 15-second intervals, and a trader might make a trade as frequently as once every minute or so.

There are many well-known trading algorithms, and others can easily be invented. Reliably profitable algorithms, of course, are fewer and farther between. The starter skeleton has an extremely simple "dumb" algorithm that you will need to replace with something more sophisticated.

## Model and Terms

There can be some confusion between an algorithm and the specific application of that algorithm with specific parameters. We use these terms in the application code and the remaining discussion:

* An **algorithm** is some specific logic for trading, based on a specific theory. For example, 2MA posits that, if we can detect that a stock's price is departing from the norm to some significant degree, it is likely to continue diverging in that direction, at least long enough for us to make some money by playing it that way, and the logic is that we take the crossing of the line of a shorter-term rolling average over the line of a longer-term one as the signal of that departure -- and that we then wait for a fixed-percentage profit or loss before closing the resulting position.

* A **strategy** is the application of an algorithm to a specific stock, for a specific number of shares, with specific parameters: for example, let's try trading up to 1000 shares of Honeywell at a time, opening a position when a 2-hour average crosses over a 4-hour average, and taking profit or loss at 3.00%.

The strategy's state includes a history of trades made (by the associated trader). These in turn are organized into **positions**: the trader always trades with a purpose, and all of our algorithms follow the pattern that they look for some trading signal in order to open a position, and then they look to close that position before going back on the hunt. A trader applies one set of criteria to incoming price data, and opens a long position by buying, and a short position by selling; then, it applies other criteria to decide when to close the position, by making the opposite trade to its opening trade -- that is, selling out of a long position, or buying out of a short one.

A position is "open" if it doesn't yet have a closing trade, and once "closed" it's over. Only a closed position has profit or loss, and a strategy's profit or loss is the sum of the profit/loss of all of its closed positions to date.

## Architecture

The application manages a relational database in which strategies, positions, and trades are stored and updated. Pricing history for stocks is also stored as it is used by running strategies, and to support auditing and analytics after the fact.

### MySQL
By default the application.properties file is looking for mysql at localhost:3306 and uses a schema named traderdb.

To get the application to run on your windows machine you should create a schema called traderdb in mysql and import the schema from src/main/resources/schema.sql

As there will be no data in the database by default the application will not make any trades i.e. it has no strategies to implement.

An example strategy can be created by inserting a stock and a strategy:

```
INSERT INTO `stock` VALUES (1,'AAPL');
INSERT INTO `simple_strategy` VALUES (2,1,199,1000,0,0,0,NULL);
```

Note: If you run the application when there is a valid strategy in the simple_strategy table, then you could use this property in application.properties to print debug logs from the strategy:

```logging.level.com.citi.training.trader.strategy=DEBUG```

## External Services

The application relies on two external services, which are considered to be out of our control: a **PriceService** that provides a stream of stock pricing data on demand, and an **OrderBroker** that acts as our intermediary to the stock market itself (though it is actually not an intermediary but rather a simulation of market responses to our trade requests). The price service is a synchronous, HTTP request/response service; and the order broker is available over an asynchronous-messaging broker.

### The Mock OrderBroker
The mock order broker is a java application that listens on a JMS queue for requests to buy or sell stock. The broker will the "mock" a response to the request.

```xml
  <trade>
      <id>1</id>
      <price>99.99</price>
      <size>600</size>
      <whenAsDate>2019-07-25T02:31:07</whenAsDate>
      <stock>AAPL</stock>
      <buy>true</buy>
    </trade>
```

All fields are required.

The mock broker will also look for a JMS correlation ID, and bases response messaging on that:

If there is a correlation ID, the broker will set the same ID in a response message. This allows your application to know which request the response corresponds to.
If there is no correlation ID, the broker logs a warning and doesn't send a response message. This allows for one-way trade requests as an option, perhaps for manual traders -- but isn't appropriate for your application.
The response from the broker will be of the same XML format as above, except that there will be one additional property called "result" with one of the following values:

* FILLED -- trade executed as requested
* PARTIALLY_FILLED -- trade executed with some number of shares less than requested -- and you can check the size property for the number
* REJECTED -- the trade was not executed

The mock order broker responses may be practically immediate, or may take up to 10 seconds, and actual time will vary, message-by-message, simulating real-world slowdowns.

### Price Feed
A mock price feed service is running in the development environment at http://feed.conygre.com:8080/MockYahoo/quotes.csv

This service will return mock stock prices in response to HTTP GET requests.

An example of a request that retrieves the price of a single stock (goog):

```http://feed.conygre.com:8080/MockYahoo/quotes.csv?s=goog&f=p0```

An example of a request that retrieves the ticker, price and volume of two stocks (apl,msft):

```http://feed.conygre.com:8080/MockYahoo/quotes.csv?s=apl,msft&f=s0p0v0```

The s parameter should contain a comma separated list of stock tickers.

The f parameter should contain a combination of:

* price as p0
* ask/bid as a0/b0
* ticker as s0
* volume as v0

If you repeat requests to these URLs you should see that the price varies -- in fact it oscillates over a sine wave based on a hard-coded base price for each of a handful of securities (AAPL GOOG BRK-A NSC MSFT). If you request a ticker not known to the service, the base price will be 100.

All prices vary over a roughly two-minute sine wave, with a magnitude of 5% of the base share price; this is modulated with a shorter and shallower wave just to make the numbers look a bit more real. So, a two-moving-averages strategy set with long and short periods in the vicinity of one minute and 15 seconds, for example, should trigger regular trading.

It should not be necessary, but if you wish to deploy the Fake feed locally, you can ask the instructors for a WAR file (note that it requires Tomcat NOT JBoss/WildFly).

The source code for the fake feed is also available, so you could change it to work for different algorothms and stocks, and then incorporate it into your own project.

Bear in mind that the mock feed service is a simulation of price variations, when preparing a demo you should take into account that the prices may not vary realistically. As the project progresses you may choose to use a live price feed from the internet.


## Frequently Asked Questions

1. "How does the user log in?" There is no login process, and there is only one user. In order to focus on the trading algorithms and other, more interesting matters, we are making the (perhaps dramatic) simplifying assumption that there is only one user. There is no login.

1. "Where is the user's portfolio?" a/k/a "How do we know how many shares we have to buy or sell?" For purposes of this application, you can assume unlimited cash and a portfolio sufficient to support any and all strategies put in play by the user. Remember that each strategy is given only N shares, maximum, for all time: this amount will always be sold out or bought back before opening a new position.

1. "What stocks will the user trade?" You may choose in your UI explicitly to support symbols from specific markets, or not. The application will function fine on any stock symbol -- even a hypothetical one. Ideally a user should be able to start a strategy for any stock that the application can get price information for.

## Necessary Enhancements

The following actions should be considered priority for a useable application:

1\. The application requires a CI/CD infrastructure, and must be deployed via Jenkins pipeline builds to OpenShift development, UAT, and production servers.

- The first step is to create your teams bitbucket repository and put the application into it. Don't fork this repository, instead clone it and delete the .git directory. The treat it like unversioned code. When creating your own repository give it a name beginning with your team name. You should also pre-pend your openshift projects, containers etc.. with fl-TEAMNAME where TEAMNAME is your teams name.

* The first step is to get the application running in docker containers on your development Linux machine. From there the instructors will guide you in creating a Jenkins pipeline.

* When your application is running on the  development openshift server you can then work with a liaison from production support to promote it through testing and on to production.

* You must develop meaningful UA tests and work with your PS liaison to see them running successfully in UAT.

* There must be a way to vary settings per deployment: for example, you will set secure passwords on the database, and these must vary: at a minimum the credentials for production must be different from those for dev and UAT. The reply queue name sent to the order broker must vary as well, and backing URLs for ActiveMQ and the price service should be easily configurable.

2\. The application has no user interface. You need to design and implement a user interface to allow users to create strategies and get metrics and feedback about current and previous strategies e.g. trades placed, profit/loss made, perhaps current price fluctuations etc.

3\. The simple algorithm used in the skeleton is insufficient. It simply reads the last two prices for the given stock. If the stock seems to be increasing in value it takes a long position. If the stock is decreasing in value it takes a short position. It then closes it's position the next time around.

You need to implement at least one of the strategies described in the Trading Strategies pdf document.

4\. There is almost no unit or integration testing. This is unacceptable and so unit tests need to be added to give a reasonable level of code coverage.

5\. There is currently no real consideration given to error conditions e.g. what if the price feed doesn't respond, the activemq server is not available etc.

6\. The stored price data will grow over time. Some type of scheduled process is needed that will trim this table to a configurable max size.

7\. The result of a trade request is not taken into account. There are four possible outcomes from a trade request: filled, partially filled, rejected ... and it's possible, though uncommon, to get no response from the order broker at all. The algorithmic traders should deal with those first three possibilities: filled, partial, rejected.

8\. You'll see that the user can create a strategy, and it will trade with those parameters over time. But, what happens if the user wants to stop a strategy that has an open position? Or is in the midst of a trade, having requested it and now waiting for a response from the order broker? Those replies are usually pretty quick but can take up to 10 seconds.

Another similar scenario is if the application is shutdown or exits with an open position.

The current application doesn't consider these possibilities at all.


## Further Enhancements

You can also implement some enhancements of your own. What do you think the application could use / where would you take it next? Imagine new capabilities for the application, do some cursory design, and consult with an instructor before moving into implementation.

And/or, here are some ideas, roughly from smallest to most ambitious:

1\. A second or even a third algorithm option.

2\. Filtering, sorting, and grouping of trade data -- by stock, by profitability, started/stopped state, etc.

3\. Strong input validation, in the UI as well as the REST service.

4\. Create a "trial-run" option, so the user could create a strategy and watch it trade __hypothetically__, as a way of exploring what works and what doesn't without risking actual money.


