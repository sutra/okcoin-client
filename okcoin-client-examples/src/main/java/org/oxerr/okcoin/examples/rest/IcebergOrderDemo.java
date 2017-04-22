package org.oxerr.okcoin.examples.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.service.trade.TradeService;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.IcebergOrder;
import org.oxerr.okcoin.rest.dto.IcebergOrderHistory;
import org.oxerr.okcoin.rest.service.OKCoinTradeServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstration of iceberg orders management.
 */
public class IcebergOrderDemo {

	private final Logger log = LoggerFactory.getLogger(IcebergOrderDemo.class);

	private final TradeService tradeService;
	private final OKCoinTradeServiceRaw rawTradeService;
	private final CurrencyPair currencyPair;

	public IcebergOrderDemo(Exchange exchange, CurrencyPair currencyPair) {
		tradeService = exchange.getTradeService();
		rawTradeService = (OKCoinTradeServiceRaw) tradeService;
		this.currencyPair = currencyPair;
	}

	public void demoGetIcebergOrderHistory() throws IOException {
		int page = 1;
		IcebergOrderHistory icebergOrderHistory;
		do {
			log.info("page: {}", page);
			icebergOrderHistory = rawTradeService.getIcebergOrderHistory(currencyPair, page);
			logOrders(icebergOrderHistory.getOrders());
			page++;
		} while(icebergOrderHistory.hasNextPage() && page < 10);
	}

	public void demoGetOpenIcebergOrders() throws IOException {
		IcebergOrder[] icebergOrders = rawTradeService.getOpenIcebergOrders(currencyPair).getOrders();
		logOrders(icebergOrders);
	}

	private void logOrders(IcebergOrder[] icebergOrders) {
		Arrays.stream(icebergOrders).forEach(
			o -> {
				log.info("ID: {}, Date: {}, Side: {}, Total Order Amount: {}, Average Order Amount: {}, Price Variance: {},  Highest/Lowest Price: {}, Filled: {}, Status: {}.",
					o.getId(),
					o.getDate(),
					o.getSide(),
					o.getTradeValue(),
					o.getSingleAvg(),
					o.getDepthRange(),
					o.getProtectedPrice(),
					o.getFilled(),
					o.getStatus());
			}
		);
	}

	public void demoBuyIcebergOrder() throws IOException {
		BigDecimal totalOrderAmount = new BigDecimal("100"); // CNY
		BigDecimal averageOrderAmount = new BigDecimal("1"); // CNY
		BigDecimal priceVariance = new BigDecimal("0.1");
		BigDecimal highestBuyPrice = new BigDecimal("1"); // CNY

		long id = rawTradeService.placeIcebergOrder(currencyPair, OrderType.BID,
			totalOrderAmount, averageOrderAmount, priceVariance, highestBuyPrice);
		log.info("buy iceberg order ID: {}", id);
	}

	public void demoSellIcebergOrder() throws IOException {
		BigDecimal totalOrderAmount = new BigDecimal("0.01"); // BTC
		BigDecimal averageOrderAmount = new BigDecimal("0.01"); // BTC
		BigDecimal priceVariance = new BigDecimal("0.1");
		BigDecimal lowestSellPrice = new BigDecimal(9999_9999); // CNY

		long id = rawTradeService.placeIcebergOrder(currencyPair, OrderType.ASK,
				totalOrderAmount, averageOrderAmount, priceVariance, lowestSellPrice);
		log.info("buy iceberg order ID: {}", id);
	}

	public void demoCancelIcebergOrder(long id) throws IOException {
		boolean cancelled = rawTradeService.cancelIcebergeOrder(currencyPair, id);
		log.info("cancel iceberg order. ID: {}, result: {}.", id, cancelled);
	}

	public static void main(String[] args) throws IOException {
		String username = args[0], password = args[1],
			tradePassword = args.length > 2 ? args[2] : null;

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setUserName(username);
		spec.setPassword(password);
		spec.setExchangeSpecificParametersItem(OKCoinExchange.TRADE_PASSWORD_PARAMETER, tradePassword);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		IcebergOrderDemo icebergOrderDemo = new IcebergOrderDemo(domesticExchange, CurrencyPair.BTC_CNY);

		// icebergOrderDemo.demoBuyIcebergOrder();
		// icebergOrderDemo.demoSellIcebergOrder();

		icebergOrderDemo.demoGetOpenIcebergOrders();
		icebergOrderDemo.demoGetIcebergOrderHistory();

		// icebergOrderDemo.demoCancelIcebergOrder(12813L);
	}

}
