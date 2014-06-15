package com.redv.okcoin.service.polling;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.okcoin.domain.OrderResult;
import com.redv.okcoin.domain.TradeResult;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinTradeServiceRaw extends OKCoinBaseTradePollingService {

	private static final long INTERVAL = 2_000;

	private static final String METHOD_TRADE = "trade";
	private static final String METHOD_CANCEL_ORDER = "cancelorder";
	private static final String METHOD_GET_ORDER = "getorder";

	private final Logger log = LoggerFactory.getLogger(OKCoinTradeServiceRaw.class);

	private Map<String, Long> lasts = new HashMap<String, Long>();

	/**
	 * @param exchangeSpecification
	 */
	protected OKCoinTradeServiceRaw(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	public TradeResult trade(String symbol, String type, String rate, String amount) {
		Map<String, Object> params = new HashMap<>();
		params.put("symbol", symbol);
		params.put("type", type);
		params.put("rate", rate);
		params.put("amount", amount);

		String sign = signatureCreator.sign(params);

		sleep(METHOD_TRADE);
		TradeResult tradeResult = okCoin.trade(partner, symbol, type, rate, amount, sign);
		updateLast(METHOD_TRADE);

		return returnOrThrow(tradeResult);
	}

	public TradeResult cancelOrder(long orderId, String symbol) {
		Map<String, Object> params = new HashMap<>();
		params.put("order_id", orderId);
		params.put("symbol", symbol);

		String sign = signatureCreator.sign(params);

		sleep(METHOD_CANCEL_ORDER);
		TradeResult tradeResult = okCoin.cancelOrder(partner, orderId, symbol, sign);
		updateLast(METHOD_CANCEL_ORDER);

		return returnOrThrow(tradeResult);
	}

	public OrderResult getOrder(long orderId, String symbol) {
		Map<String, Object> params = new HashMap<>();
		params.put("order_id", orderId);
		params.put("symbol", symbol);

		String sign = signatureCreator.sign(params);

		sleep(METHOD_GET_ORDER);
		OrderResult orderResult = okCoin.getOrder(partner, orderId, symbol, sign);
		updateLast(METHOD_GET_ORDER);

		return returnOrThrow(orderResult);
	}

	private long getLast(String method) {
		Long last = lasts.get(method);
		if (last == null) {
			return 0;
		} else {
			return last.longValue();
		}
	}

	private void updateLast(String method) {
		lasts.put(method, System.currentTimeMillis());
	}

	private void sleep(String method) {
		if (System.currentTimeMillis() - getLast(method) < INTERVAL) {
			sleep();
		}
	}

	private void sleep() {
		try {
			log.debug("Sleeping for {} ms.", INTERVAL);
			Thread.sleep(INTERVAL);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
