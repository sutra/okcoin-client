package com.redv.okcoin;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.okcoin.domain.Balance;
import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.Depth.Data;
import com.redv.okcoin.domain.Ticker;
import com.redv.okcoin.domain.Trade;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.PollingAccountService;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.xeiam.xchange.service.polling.PollingTradeService;

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
		log.debug("Ticker: {}", ticker0);

		// Depth
		OrderBook orderBook = marketDataService.getOrderBook(CurrencyPair.BTC_CNY);
		log.debug("Depth: {}", orderBook);

		// Trades
		Trades trades0 = marketDataService.getTrades(CurrencyPair.BTC_CNY);
		log.debug("Trades: {}", trades0);

		trades0 = marketDataService.getTrades(CurrencyPair.BTC_CNY, 0);
		log.debug("Trades: {}", trades0);

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(partner);
		spec.setSecretKey(secretKey);

		// Account service.
		Exchange tradeExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		PollingAccountService accountService = tradeExchange.getPollingAccountService();

		// User info
		AccountInfo accountInfo = accountService.getAccountInfo();
		log.debug("Account info: {}", accountInfo);

		// Trade service
		PollingTradeService tradeService = tradeExchange.getPollingTradeService();

		// Open orders
		OpenOrders openOrders = tradeService.getOpenOrders();
		log.debug("Open orders count: {}", openOrders.getOpenOrders().size());

		try (OKCoinClient client = new OKCoinClient(loginName, password,
				tradePwd, 5000, 5000, 5000)) {
			// Ticker
			Ticker ticker = client.getTicker();
			log.debug("Ticker: {}", ticker);

			// Depth
			Depth depth = client.getDepth();
			log.debug("Depth: {}", depth);

			for (Data data : depth.getBids()) {
				log.debug("{}", data);
			}

			for (Data data : depth.getAsks()) {
				log.debug("{}", data);
			}

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
			log.debug("Balance: {}", balance);
		}
	}

}
