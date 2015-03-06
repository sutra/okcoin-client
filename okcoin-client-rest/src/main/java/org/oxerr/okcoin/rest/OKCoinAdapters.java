package org.oxerr.okcoin.rest;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.oxerr.okcoin.rest.dto.Depth;
import org.oxerr.okcoin.rest.dto.Funds;
import org.oxerr.okcoin.rest.dto.Order;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.dto.TickerResponse;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.rest.dto.UserInfo;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.UserTrade;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.dto.trade.Wallet;

/**
 * Various adapters for converting from OKCoin DTOs to XChange DTOs.
 */
public final class OKCoinAdapters {

	/**
	 * Private constructor.
	 */
	private OKCoinAdapters() {
	}

	public static String adaptSymbol(CurrencyPair currencyPair) {
		return String.format("%1$s_%2$s",
				currencyPair.baseSymbol,
				currencyPair.counterSymbol)
				.toLowerCase();
	}

	public static CurrencyPair adaptSymbol(String symbol) {
		String[] currencies = symbol.toUpperCase().split("_");
		return new CurrencyPair(currencies[0], currencies[1]);
	}

	public static Ticker adaptTicker(TickerResponse tickerResponse,
			CurrencyPair currencyPair) {
		return new Ticker.Builder()
				.currencyPair(currencyPair)
				.timestamp(Date.from(tickerResponse.getDate()))
				.high(tickerResponse.getTicker().getHigh())
				.low(tickerResponse.getTicker().getLow())
				.bid(tickerResponse.getTicker().getBuy())
				.ask(tickerResponse.getTicker().getSell())
				.last(tickerResponse.getTicker().getLast())
				.volume(tickerResponse.getTicker().getVol())
				.build();
	}

	public static OrderBook adaptOrderBook(Depth depth, CurrencyPair currencyPair) {
		List<LimitOrder> asks = adaptLimitOrders(OrderType.ASK, depth.getAsks(), currencyPair);
		Collections.reverse(asks);

		List<LimitOrder> bids = adaptLimitOrders(OrderType.BID, depth.getBids(), currencyPair);

		return new OrderBook(null, asks, bids);
	}

	public static Trades adaptTrades(Trade[] trades, CurrencyPair currencyPair) {
		List<com.xeiam.xchange.dto.marketdata.Trade> tradeList = new ArrayList<>(
				trades.length);
		for (Trade trade : trades) {
			tradeList.add(adaptTrade(trade, currencyPair));
		}
		long lastTid = trades.length > 0
				? trades[trades.length - 1].getTid()
				: 0L;
		return new Trades(tradeList, lastTid, TradeSortType.SortByTimestamp);
	}

	public static AccountInfo adaptAccountInfo(UserInfo userInfo) {
		Funds funds = userInfo.getInfo().getFunds();

		Map<String, BigDecimal> balances = new LinkedHashMap<>(
				Math.max(funds.getFree().size(), funds.getFrozen().size()));

		for (Map.Entry<String, BigDecimal> entry : userInfo.getInfo()
				.getFunds().getFree().entrySet()) {
			String currency = entry.getKey().toUpperCase();
			BigDecimal balance = balances.get(currency);
			if (balance == null) {
				balance = entry.getValue();
			} else {
				balance = balance.add(entry.getValue());
			}
			balances.put(currency, balance);
		}

		for (Map.Entry<String, BigDecimal> entry : userInfo.getInfo().getFunds().getFrozen().entrySet()) {
			String currency = entry.getKey().toUpperCase();
			BigDecimal balance = balances.get(currency);
			if (balance == null) {
				balance = entry.getValue();
			} else {
				balance = balance.add(entry.getValue());
			}
			balances.put(currency, balance);
		}

		List<Wallet> wallets = new ArrayList<>(balances.size());
		for (Map.Entry<String, BigDecimal> balance : balances.entrySet()) {
			wallets.add(new Wallet(balance.getKey(), balance.getValue()));
		}

		return new AccountInfo(null, wallets);
	}

	public static OpenOrders adaptOpenOrders(Collection<OrderResult> orderResults) {
		List<LimitOrder> openOrders = new ArrayList<>();
		for (OrderResult orderResult : orderResults) {
			openOrders.addAll(adaptOpenOrders(orderResult));
		}
		return new OpenOrders(openOrders);
	}

	public static UserTrades adaptUserTrades(OrderHistory orderHistory) {
		List<UserTrade> userTrades = Arrays
			.stream(orderHistory.getOrders())
			.map(order -> adaptUserTrade(order))
			.collect(toList());
		return new UserTrades(userTrades, TradeSortType.SortByTimestamp);
	}

	private static List<LimitOrder> adaptLimitOrders(OrderType type,
			BigDecimal[][] list, CurrencyPair currencyPair) {
		List<LimitOrder> limitOrders = new ArrayList<>(list.length);
		for (BigDecimal[] data : list) {
			limitOrders.add(adaptLimitOrder(type, data, currencyPair, null,
					null));
		}
		return limitOrders;
	}

	private static LimitOrder adaptLimitOrder(OrderType type, BigDecimal[] data,
			CurrencyPair currencyPair, String id, Date timestamp) {
		return new LimitOrder(type, data[1], currencyPair, id,
				timestamp, data[0]);
	}

	private static com.xeiam.xchange.dto.marketdata.Trade adaptTrade(
			Trade trade, CurrencyPair currencyPair) {
		return new com.xeiam.xchange.dto.marketdata.Trade.Builder()
			.id(String.valueOf(trade.getTid()))
			.timestamp(Date.from(trade.getDate()))
			.currencyPair(currencyPair)
			.type(trade.getType() == Type.BUY ? OrderType.BID : OrderType.ASK)
			.tradableAmount(trade.getAmount())
			.price(trade.getPrice())
			.build();
	}

	private static List<LimitOrder> adaptOpenOrders(OrderResult orderResult) {
		List<LimitOrder> openOrders = new ArrayList<>(orderResult.getOrders().length);
		for (Order order : orderResult.getOrders()) {
			LimitOrder openOrder = adaptOpenOrder(order);
			if (openOrder != null) {
				openOrders.add(openOrder);
			}
		}
		return openOrders;
	}

	private static LimitOrder adaptOpenOrder(Order order) {
		return new LimitOrder(
				adaptOrderType(order.getType()),
				order.getAmount().subtract(order.getDealAmount()),
				adaptSymbol(order.getSymbol()),
				String.valueOf(order.getOrderId()),
				Date.from(order.getCreateDate()),
				order.getPrice());
	}

	public static OrderType adaptOrderType(Type type) {
		return type == Type.BUY || type == Type.BUY_MARKET
				? OrderType.BID : OrderType.ASK;
	}

	private static com.xeiam.xchange.dto.trade.UserTrade adaptUserTrade(
			Order order) {
		return new UserTrade.Builder()
			.type(adaptOrderType(order.getType()))
			.tradableAmount(order.getDealAmount())
			.currencyPair(adaptSymbol(order.getSymbol()))
			.price(order.getAvgPrice())
			.timestamp(Date.from(order.getCreateDate()))
			.orderId(String.valueOf(order.getOrderId()))
			.build();
	}

}
