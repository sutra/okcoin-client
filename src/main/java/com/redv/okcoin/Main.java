package com.redv.okcoin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.okcoin.domain.Balance;
import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.Depth.Data;
import com.redv.okcoin.domain.Ticker;
import com.redv.okcoin.domain.Trade;

public class Main {

	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {
		String loginName = args[0];
		String password = args[1];
		String tradePwd = args[2];

		try (OKCoinClient client = new OKCoinClient(loginName, password,
				tradePwd)) {
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

			// Bid
			client.bid(new BigDecimal("0.01"), BigDecimal.ONE);

			// Ask
			client.ask(new BigDecimal("0.01"), BigDecimal.ONE);
		}
	}

}
