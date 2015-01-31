package org.oxerr.okcoin.rest;

import java.io.IOException;
import java.util.List;

import org.oxerr.okcoin.rest.domain.Balance;
import org.oxerr.okcoin.rest.domain.Ticker;
import org.oxerr.okcoin.rest.domain.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.account.PollingAccountService;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException, InterruptedException {
		String loginName = args[0];
		String password = args[1];
		String tradePwd = args[2];

		String partner = args[3];
		String secretKey = args[4];

		// Market data service
		Exchange publicExchange = ExchangeFactory.INSTANCE.createExchange(OKCoinExchange.class.getName());
		PollingMarketDataService marketDataService = publicExchange.getPollingMarketDataService();

		// Ticker
		com.xeiam.xchange.dto.marketdata.Ticker ticker0 = marketDataService.getTicker(CurrencyPair.BTC_CNY);
		log.info("Ticker: {}", ticker0);

		// Depth
		OrderBook orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_CNY);
		log.info("Depth time stamp: {}", orderBook.getTimeStamp());

		// Trades
		Trades trades0 = marketDataService.getTrades(CurrencyPair.BTC_CNY);
		log.info("Trades count: {}, lastID: {}", trades0.getTrades().size(), trades0.getlastID());
		for (com.xeiam.xchange.dto.marketdata.Trade trade : trades0.getTrades()) {
			log.info("{} {} {} {}@{}",
					trade.getId(), trade.getTimestamp(), trade.getType(),
					trade.getTradableAmount(), trade.getPrice());
		}

		trades0 = marketDataService.getTrades(CurrencyPair.BTC_CNY, trades0.getlastID());
		log.info("Trades count: {}, lastID: {}", trades0.getTrades().size(), trades0.getlastID());
		for (com.xeiam.xchange.dto.marketdata.Trade trade : trades0.getTrades()) {
			log.info("{} {} {} {}@{}",
					trade.getId(), trade.getTimestamp(), trade.getType(),
					trade.getTradableAmount(), trade.getPrice());
		}

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(partner);
		spec.setSecretKey(secretKey);

		// Account service.
		Exchange tradeExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		PollingAccountService accountService = tradeExchange.getPollingAccountService();

		// User info
		AccountInfo accountInfo = accountService.getAccountInfo();
		log.info("Account info: {}", accountInfo);

		// Trade service
		PollingTradeService tradeService = tradeExchange.getPollingTradeService();

		// Open orders
		OpenOrders openOrders = tradeService.getOpenOrders();
		log.info("Open orders count: {}", openOrders.getOpenOrders().size());

		try (OKCoinClient client = new OKCoinClient(loginName, password,
				tradePwd, 5000, 5000, 5000)) {
			// Ticker
			Ticker ticker = client.getTicker();
			log.info("Ticker: {}", ticker);

			// Trades.
			List<Trade> trades = client.getTrades();
			log.info("Trades: {}", trades);

			trades = client.getTrades(200);
			log.info("Trades since 200: {}", trades);

			// Get balance before login
			try {
				client.getBalance();
			} catch (LoginRequiredException e) {
				// Login
				client.login();
			}

			// Balance
			Balance balance = client.getBalance();
			log.info("Balance: {}", balance);
		}
	}

}
