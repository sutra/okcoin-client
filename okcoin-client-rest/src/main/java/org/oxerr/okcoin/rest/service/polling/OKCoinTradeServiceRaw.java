package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.BatchTradeResult;
import org.oxerr.okcoin.rest.dto.CancelOrderResult;
import org.oxerr.okcoin.rest.dto.OrderData;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.dto.TradeResult;
import org.oxerr.okcoin.rest.dto.Type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xeiam.xchange.Exchange;

public class OKCoinTradeServiceRaw extends OKCoinBaseTradePollingService {

	private static final String METHOD_TRADE = "trade";
	private static final String METHOD_CANCEL_ORDER = "cancel_order";
	private static final String METHOD_GET_ORDER = "order_info";

	private final ObjectMapper mapper;

	protected OKCoinTradeServiceRaw(Exchange exchange) {
		super(exchange);

		mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
	}

	public TradeResult trade(String symbol, Type type, BigDecimal price,
			BigDecimal amount) throws OKCoinException, IOException {
		sleep(METHOD_TRADE);
		TradeResult tradeResult = okCoin.trade(apiKey, symbol, type, price, amount, sign);
		updateLast(METHOD_TRADE);

		return tradeResult;
	}

	public BatchTradeResult batchTrade(String symbol, Type type,
			OrderData[] orders) throws OKCoinException, IOException {
		String ordersData = mapper.writeValueAsString(orders);
		return okCoin.batchTrade(apiKey, symbol, type, ordersData, sign);
	}

	public CancelOrderResult cancelOrder(String symbol, long... orderIds)
			throws OKCoinException, IOException {
		sleep(METHOD_CANCEL_ORDER);
		String orderId = Arrays.stream(orderIds)
			.mapToObj(id -> String.valueOf(id))
			.collect(Collectors.joining(","));
		CancelOrderResult result = okCoin.cancelOrder(apiKey, symbol, orderId, sign);
		updateLast(METHOD_CANCEL_ORDER);

		return result;
	}

	public OrderResult getOrder(String symbol, long orderId)
			throws OKCoinException, IOException {
		sleep(METHOD_GET_ORDER);
		OrderResult orderResult = okCoin.getOrder(apiKey, symbol, orderId, sign);
		updateLast(METHOD_GET_ORDER);

		return orderResult;
	}

	public OrderResult getOrders(String symbol, int type, long[] orderIds)
			throws OKCoinException, IOException {
		String orderId = Arrays.stream(orderIds)
				.mapToObj(id -> String.valueOf(id))
				.collect(Collectors.joining(","));
		return okCoin.getOrders(apiKey, symbol, type, orderId, sign);
	}

	public OrderHistory getOrderHistory(String symbol, int status,
			int currentPage, int pageLength) throws OKCoinException,
			IOException {
		return okCoin.getOrderHistory(apiKey, symbol, status, currentPage,
				pageLength, sign);
	}

}
