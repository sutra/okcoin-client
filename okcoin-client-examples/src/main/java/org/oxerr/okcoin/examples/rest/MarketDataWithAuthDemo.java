package org.oxerr.okcoin.examples.rest;

import java.io.IOException;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.service.OKCoinTradeServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarketDataWithAuthDemo {

	private final Logger log = LoggerFactory.getLogger(MarketDataWithAuthDemo.class);

	private final OKCoinTradeServiceRaw rawTradeService;

	public MarketDataWithAuthDemo(Exchange exchange) {
		rawTradeService = (OKCoinTradeServiceRaw) exchange.getTradeService();
	}

	public void demoTradeHistory() throws IOException {
		Trade[] trades = rawTradeService.getTradeHistory("btc_cny", 2024856248L);
		for (Trade trade : trades) {
			log.info("{}", trade);
		}
	}

	public static void main(String[] args) throws IOException {
		// www.okcoin.cn
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);

		MarketDataWithAuthDemo domesticDemo = new MarketDataWithAuthDemo(domesticExchange);
		domesticDemo.demoTradeHistory();
	}

}
