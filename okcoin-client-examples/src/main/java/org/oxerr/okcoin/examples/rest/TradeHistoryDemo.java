package org.oxerr.okcoin.examples.rest;

import java.io.IOException;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.service.trade.TradeService;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.service.OKCoinTradeService.OKCoinTradeHistoryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstration of getting order history.
 */
public class TradeHistoryDemo {

	private final Logger log = LoggerFactory.getLogger(TradeHistoryDemo.class);

	private final TradeService tradeService;

	public TradeHistoryDemo(Exchange exchange) {
		tradeService = exchange.getTradeService();
	}

	public void demoGetTradeHistory() throws IOException {
		OKCoinTradeHistoryParams params = (OKCoinTradeHistoryParams) tradeService.createTradeHistoryParams();
		params.setCurrencyPair(CurrencyPair.BTC_CNY);
		params.setPageNumber(1);
		params.setPageLength(10);

		UserTrades userTrades = tradeService.getTradeHistory(params);
		userTrades.getUserTrades().forEach(
			userTrade -> log.info("ID: {}, OrderID: {}, {} {} {}@{}",
				userTrade.getId(),
				userTrade.getOrderId(),
				userTrade.getTimestamp(),
				userTrade.getType(),
				userTrade.getTradableAmount(),
				userTrade.getPrice()));
	}

	public static void main(String[] args) throws IOException {
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		TradeHistoryDemo tradeHistoryDemo = new TradeHistoryDemo(domesticExchange);

		tradeHistoryDemo.demoGetTradeHistory();
	}

}
