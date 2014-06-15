package com.redv.okcoin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.Depth.Data;
import com.redv.okcoin.domain.Order;
import com.redv.okcoin.domain.OrderResult;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.redv.okcoin.domain.Type;
import com.redv.okcoin.domain.UserInfo;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
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
		return TickerBuilder
				.newInstance()
				.withCurrencyPair(currencyPair)
				.withHigh(tickerResponse.getTicker().getHigh())
				.withLow(tickerResponse.getTicker().getLow())
				.withBid(tickerResponse.getTicker().getBuy())
				.withAsk(tickerResponse.getTicker().getSell())
				.withLast(tickerResponse.getTicker().getLast())
				.withVolume(tickerResponse.getTicker().getVol())
				.build();
	}

	public static OrderBook adaptOrderBook(Depth depth, CurrencyPair currencyPair) {
		List<LimitOrder> asks = adaptLimitOrders(OrderType.ASK, depth.getAsks(), currencyPair);
		List<LimitOrder> bids = adaptLimitOrders(OrderType.BID, depth.getBids(), currencyPair);
		return new OrderBook(null, asks, bids);
	}

	public static Trades adaptTrades(Trade[] trades, CurrencyPair currencyPair) {
		List<com.xeiam.xchange.dto.marketdata.Trade> tradeList = new ArrayList<>(
				trades.length);
		for (Trade trade : trades) {
			tradeList.add(adaptTrade(trade, currencyPair));
		}
		long lastTid = trades.length > 1 ? NumberUtils.toLong(trades[trades.length - 1].getTid()) : 0L;
		return new Trades(tradeList, lastTid, TradeSortType.SortByTimestamp);
	}

	public static AccountInfo adaptAccountInfo(UserInfo userInfo) {
		List<Wallet> wallets = new ArrayList<>(
				userInfo.getInfo().getFunds().getFree().size()
				+ userInfo.getInfo().getFunds().getFreezed().size());

		for (Map.Entry<String, BigDecimal> entry : userInfo.getInfo()
				.getFunds().getFree().entrySet()) {
			String currency = entry.getKey().toUpperCase();
			BigDecimal balance = entry.getValue();
			Wallet wallet = new Wallet(currency, balance, "Free " + currency);
			wallets.add(wallet);
		}
		for (Map.Entry<String, BigDecimal> entry : userInfo.getInfo().getFunds().getFreezed().entrySet()) {
			String currency = entry.getKey().toUpperCase();
			BigDecimal balance = entry.getValue();
			Wallet wallet = new Wallet(currency, balance, "Frozen " + currency);
			wallets.add(wallet);
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

	private static List<LimitOrder> adaptLimitOrders(OrderType type,
			List<Data> list, CurrencyPair currencyPair) {
		List<LimitOrder> limitOrders = new ArrayList<>(list.size());
		for (Data data : list) {
			limitOrders.add(adaptLimitOrder(type, data, currencyPair, null,
					null));
		}
		return limitOrders;
	}

	private static LimitOrder adaptLimitOrder(OrderType type, Data data,
			CurrencyPair currencyPair, String id, Date timestamp) {
		return new LimitOrder(type, data.getAmount(), currencyPair, id,
				timestamp, data.getRate());
	}

	private static com.xeiam.xchange.dto.marketdata.Trade adaptTrade(
			Trade trade, CurrencyPair currencyPair) {
		return new com.xeiam.xchange.dto.marketdata.Trade(
				trade.getType() == Type.BUY ? OrderType.BID : OrderType.ASK,
				trade.getAmount(), currencyPair, trade.getPrice(),
				trade.getDate(), trade.getTid());
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
				adaptType(order.getType()),
				order.getAmount().subtract(order.getDealAmount()),
				adaptSymbol(order.getSymbol()),
				String.valueOf(order.getOrderId()),
				order.getCreateDate(),
				order.getRate());
	}

	private static OrderType adaptType(String type) {
		return type.equals("buy") || type.equals("buy_market") ? OrderType.BID : OrderType.ASK;
	}

}
