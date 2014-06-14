package com.redv.okcoin;

import java.util.HashMap;
import java.util.Map;

import com.redv.okcoin.domain.OrderResult;
import com.redv.okcoin.domain.TradeResult;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinTradeServiceRaw extends OKCoinBaseTradePollingService {

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
		return returnOrThrow(okCoin.trade(partner, symbol, type, rate, amount, sign));
	}

	public TradeResult cancelOrder(long orderId, String symbol) {
		Map<String, Object> params = new HashMap<>();
		params.put("order_id", orderId);
		params.put("symbol", symbol);

		String sign = signatureCreator.sign(params);
		return returnOrThrow(okCoin.cancelOrder(partner, orderId, symbol, sign));
	}

	public OrderResult getOrder(long orderId, String symbol) {
		Map<String, Object> params = new HashMap<>();
		params.put("order_id", orderId);
		params.put("symbol", symbol);

		String sign = signatureCreator.sign(params);
		return returnOrThrow(okCoin.getOrder(partner, orderId, symbol, sign));
	}

}
