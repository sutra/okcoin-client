package org.oxerr.okcoin.examples.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.BatchTradeResult;
import org.oxerr.okcoin.rest.dto.CancelOrderResult;
import org.oxerr.okcoin.rest.dto.OrderData;
import org.oxerr.okcoin.rest.dto.TradeResult;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.rest.service.polling.OKCoinTradeServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;

/**
 * Demonstration of placing order.
 */
public class TradeDemo {

	private final Logger log = LoggerFactory.getLogger(TradeDemo.class);

	private final PollingTradeService tradeService;
	private final OKCoinTradeServiceRaw rawTradeService;

	public TradeDemo(Exchange exchange) {
		tradeService = exchange.getPollingTradeService();
		rawTradeService = (OKCoinTradeServiceRaw) tradeService;
	}

	public void demoLimitOrder() throws OKCoinException, IOException {
		LimitOrder order = new LimitOrder
			.Builder(OrderType.BID, CurrencyPair.BTC_CNY)
			.tradableAmount(new BigDecimal("1"))
			.limitPrice(new BigDecimal("0.01"))
			.build();
		// place a limit order 1@0.01
		String orderId = tradeService.placeLimitOrder(order);
		log.info("placed a limit order: {}", orderId);

		// cancel the above order
		boolean cancelled = tradeService.cancelOrder(orderId);
		log.info("order {} cancellation result: {}", orderId, cancelled);
	}

	public void demoLimitOrderRaw() throws OKCoinException, IOException {
		String symbol = "btc_cny";

		// place a limit order 1@0.01
		TradeResult ret = rawTradeService.trade("btc_cny", Type.BUY, new BigDecimal("0.01"), new BigDecimal("1"));
		long orderId = ret.getOrderId();
		log.info("placed a limit order: {}", orderId);

		// cancel the above order
		CancelOrderResult cxlRet = rawTradeService.cancelOrder(symbol, orderId);
		log.info("order {} cancellation result: {}", orderId, cxlRet);
	}

	public void demoBatchTrade() throws OKCoinException, IOException {
		OrderData[] orders = new OrderData[] {
			new OrderData(new BigDecimal("0.01"), new BigDecimal("1"), Type.BUY),
			new OrderData(new BigDecimal("0.02"), new BigDecimal("2"), Type.BUY),
		};
		BatchTradeResult result = rawTradeService.batchTrade("btc_cny", Type.BUY, orders);

		Arrays.stream(result.getOrderInfo()).forEach(info -> {
			try {
				cancelOrder(info.getOrderId());
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}
		});
	}

	public void cancelOrder(String orderId) throws OKCoinException, IOException {
		boolean cancelled = tradeService.cancelOrder(orderId);
		log.info("order {} cancellation result: {}", orderId, cancelled);
	}

	public void cancelOrder(long orderId) throws OKCoinException, IOException {
		CancelOrderResult cxlRet = rawTradeService.cancelOrder("btc_cny", orderId);
		log.info("order {} cancellation result: {}", orderId, cxlRet);
	}

	public static void main(String[] args) throws OKCoinException, IOException {
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		TradeDemo tradeDemo = new TradeDemo(domesticExchange);

		tradeDemo.demoLimitOrder();
		// tradeDemo.demoLimitOrderRaw();

		// tradeDemo.demoBatchTrade();
	}

}
