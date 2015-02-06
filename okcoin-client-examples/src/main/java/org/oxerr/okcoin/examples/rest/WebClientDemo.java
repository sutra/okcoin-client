package org.oxerr.okcoin.examples.rest;

import java.io.IOException;
import java.util.List;

import org.oxerr.okcoin.rest.dto.Funds;
import org.oxerr.okcoin.rest.dto.Ticker;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;
import org.oxerr.okcoin.rest.service.web.OKCoinClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebClientDemo {

	private static final Logger log = LoggerFactory.getLogger(WebClientDemo.class);

	public static void main(String[] args) throws IOException, InterruptedException {
		String loginName = args[0];
		String password = args[1];
		String tradePwd = args.length > 2 ? args[2] : null;

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
				log.info("login required.");
				// Login
				client.login();
			}

			// Balance
			Funds funds = client.getBalance();
			log.info("Balance: {}", funds);
		}
	}

}
