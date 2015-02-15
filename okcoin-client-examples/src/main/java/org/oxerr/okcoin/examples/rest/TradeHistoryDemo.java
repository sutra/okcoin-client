package org.oxerr.okcoin.examples.rest;

import java.io.IOException;

import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.service.polling.OKCoinTradeService.OKCoinTradeHistoryParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;

/**
 * Demonstration of getting order history.
 */
public class TradeHistoryDemo {

	private final Logger log = LoggerFactory.getLogger(TradeHistoryDemo.class);

	private final PollingTradeService tradeService;

	public TradeHistoryDemo(Exchange exchange) {
		tradeService = exchange.getPollingTradeService();
	}

	public void demoGetTradeHistory() throws IOException {
		OKCoinTradeHistoryParams params = (OKCoinTradeHistoryParams) tradeService.createTradeHistoryParams();
		params.setCurrencyPair(CurrencyPair.BTC_CNY);
		params.setPageNumber(0);
		params.setPageLength(10);

		UserTrades userTrades = tradeService.getTradeHistory(params);
		userTrades.getUserTrades().forEach(
			userTrade -> log.info("ID: {}, OrderID: {}, {} {}@{}",
				userTrade.getId(),
				userTrade.getOrderId(),
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
