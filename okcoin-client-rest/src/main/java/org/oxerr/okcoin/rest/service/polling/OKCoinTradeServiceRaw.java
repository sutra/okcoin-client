package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.oxerr.okcoin.rest.domain.OrderResult;
import org.oxerr.okcoin.rest.domain.TradeResult;

import com.xeiam.xchange.Exchange;

public class OKCoinTradeServiceRaw extends OKCoinBaseTradePollingService {

	private static final String METHOD_TRADE = "trade";
	private static final String METHOD_CANCEL_ORDER = "cancelorder";
	private static final String METHOD_GET_ORDER = "getorder";

	protected OKCoinTradeServiceRaw(Exchange exchange) {
		super(exchange);
	}

	public TradeResult trade(String symbol, String type, String rate,
			String amount) throws IOException {
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

	public TradeResult cancelOrder(long orderId, String symbol)
			throws IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("order_id", orderId);
		params.put("symbol", symbol);

		String sign = signatureCreator.sign(params);

		sleep(METHOD_CANCEL_ORDER);
		TradeResult tradeResult = okCoin.cancelOrder(partner, orderId, symbol, sign);
		updateLast(METHOD_CANCEL_ORDER);

		return returnOrThrow(tradeResult);
	}

	/**
	 * Returns order details.
	 *
	 * @param orderId the order ID.
	 * @param symbol could be "btc_cny" or "ltc_cny".
	 * @return the order detail.
	 * @throws IOException indicates I/O exception.
	 */
	public OrderResult getOrder(long orderId, String symbol) throws IOException {
		Map<String, Object> params = new HashMap<>();
		params.put("order_id", orderId);
		params.put("symbol", symbol);

		String sign = signatureCreator.sign(params);

		sleep(METHOD_GET_ORDER);
		OrderResult orderResult = okCoin.getOrder(partner, orderId, symbol, sign);
		updateLast(METHOD_GET_ORDER);

		return returnOrThrow(orderResult);
	}

}
