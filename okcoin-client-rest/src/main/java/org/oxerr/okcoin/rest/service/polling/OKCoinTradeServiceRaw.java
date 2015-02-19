package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.BatchTradeResult;
import org.oxerr.okcoin.rest.dto.CancelOrderResult;
import org.oxerr.okcoin.rest.dto.IcebergOrder;
import org.oxerr.okcoin.rest.dto.OrderData;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.dto.Result;
import org.oxerr.okcoin.rest.dto.TradeResult;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;
import org.oxerr.okcoin.rest.service.web.OKCoinClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;

/**
 * Raw trade service.
 */
public class OKCoinTradeServiceRaw extends OKCoinBaseTradePollingService {

	private static final String METHOD_TRADE = "trade";
	private static final String METHOD_CANCEL_ORDER = "cancel_order";
	private static final String METHOD_GET_ORDER = "order_info";

	private final Logger log = LoggerFactory.getLogger(OKCoinTradeServiceRaw.class);

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

	public IcebergOrder[] getOpenIcebergOrders(CurrencyPair currencyPair)
			throws IOException {
		int symbol = toSymbol(currencyPair);
		return getOpenIcebergOrders(symbol);
	}

	private IcebergOrder[] getOpenIcebergOrders(int symbol) throws IOException {
		int sign = 1; // open orders;
		return getIcebergOrders(symbol, sign);
	}

	public IcebergOrder[] getIcebergOrderHistory(CurrencyPair currencyPair)
			throws IOException {
		int symbol = toSymbol(currencyPair);
		int sign = 2; // order history
		return getIcebergOrders(symbol, sign);
	}

	private IcebergOrder[] getIcebergOrders(int symbol, int sign)
			throws IOException {
		int retry = 0;

		while (true) {
			try {
				log.debug("Trying to get iceberge open orders.");
				return okCoinClient.getIcebergOrders(symbol, 5, sign, 2);
			} catch (LoginRequiredException e) {
				if (++retry <= this.loginMaxRetryTimes) {
					log.debug("Not logged in. Try to login. Retry: {}.", retry);
					okCoinClient.login();
				} else {
					throw e;
				}
			}
		}
	}

	public long placeIcebergOrder(
		CurrencyPair currencyPair,
		OrderType type,
		BigDecimal tradeValue,
		BigDecimal singleAvg,
		BigDecimal depthRange,
		BigDecimal protectedPrice)
			throws OKCoinClientException, IOException {
		int symbol = toSymbol(currencyPair);
		Result result = submitContinuousEntrust(
				symbol,
				type == OrderType.BID ? 1 : 2,
				tradeValue, singleAvg, depthRange, protectedPrice);
		log.debug("result: {}", result);
		if (result.getResultCode() != 0) {
			throw new OKCoinClientException(result);
		}
		IcebergOrder[] icebergOrders = getOpenIcebergOrders(symbol);
		return icebergOrders[0].getId();
	}

	private Result submitContinuousEntrust(int symbol, int type,
			BigDecimal tradeValue, BigDecimal singleAvg, BigDecimal depthRange,
			BigDecimal protePrice) throws IOException {
		int retry = 0;

		while (true) {
			try {
				return okCoinClient.submitContinuousEntrust(symbol, type, tradeValue, singleAvg, depthRange, protePrice);
			} catch (LoginRequiredException e) {
				if (++retry <= this.loginMaxRetryTimes) {
					okCoinClient.login();
				} else {
					throw e;
				}
			}
		}
	}

	public boolean cancelIcebergeOrder(CurrencyPair currencyPair, long id)
			throws IOException {
		int symbol = toSymbol(currencyPair);
		return cancelContinuousEntrust(symbol, id);
	}

	private boolean cancelContinuousEntrust(int symbol, long id) throws IOException {
		int retry = 0;

		while (true) {
			try {
				return okCoinClient.cancelContinuousEntrust(symbol, id);
			} catch (LoginRequiredException e) {
				if (++retry <= this.loginMaxRetryTimes) {
					okCoinClient.login();
				} else {
					throw e;
				}
			}
		}
	}

	private int toSymbol(CurrencyPair currencyPair) {
		return currencyPair.baseSymbol.equals("BTC") ? 0 : 1;
	}

}
