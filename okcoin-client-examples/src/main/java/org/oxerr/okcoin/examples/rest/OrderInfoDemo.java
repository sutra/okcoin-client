package org.oxerr.okcoin.examples.rest;

import java.io.IOException;
import java.util.Arrays;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.TradeService;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.service.OKCoinTradeServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstration of getting order info.
 */
public class OrderInfoDemo {

	private final Logger log = LoggerFactory.getLogger(TradeDemo.class);

	private final TradeService tradeService;
	private final OKCoinTradeServiceRaw rawTradeService;

	public OrderInfoDemo(Exchange exchange) {
		tradeService = exchange.getTradeService();
		rawTradeService = (OKCoinTradeServiceRaw) tradeService;
	}

	public void getOpenOrders() throws IOException {
		OpenOrders openOrders = tradeService.getOpenOrders();
		log.info("open orders: {}", openOrders);
		openOrders.getOpenOrders().stream().forEach(
				o -> log.info("{} {} {} {} {}@{}",
						o.getCurrencyPair(),
						o.getId(), o.getTimestamp(),
						o.getType(), o.getTradableAmount(), o.getLimitPrice()));

	}

	public void getUnfilledOrders() throws OKCoinException, IOException {
		String symbol = "btc_cny";
		OrderResult result = rawTradeService.getOrder(symbol, -1);
		log.info("unfilled orders: {}", result);
		Arrays.stream(result.getOrders()).forEach(
			o -> log.info("{} {} {} {}@{}, filled: {}, avg px: {}",
				o.getOrderId(), o.getCreateDate(),
				o.getType(), o.getAmount(), o.getPrice(),
				o.getDealAmount(), o.getAvgPrice()));
	}

	public void getOrders() throws OKCoinException, IOException {
		OrderResult result = rawTradeService.getOrders("btc_cny", 0,
				new long[] { 295510436L });
		log.info("order result: {}", result);
	}

	public void getOrderHistory() throws OKCoinException, IOException {
		OrderHistory history = rawTradeService.getOrderHistory("btc_cny", 0, 1, 2);
		log.info("order history: {}", history);

		Arrays.stream(history.getOrders()).forEach(
				o -> log.info("{} {} {} {}@{}, filled: {}, avg px: {}",
					o.getOrderId(), o.getCreateDate(),
					o.getType(), o.getAmount(), o.getPrice(),
					o.getDealAmount(), o.getAvgPrice()));
	}

	public static void main(String[] args) throws OKCoinException, IOException {
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		OrderInfoDemo orderInfoDemo = new OrderInfoDemo(domesticExchange);

		orderInfoDemo.getOpenOrders();
		orderInfoDemo.getUnfilledOrders();
		orderInfoDemo.getOrders();
		orderInfoDemo.getOrderHistory();
	}

}
