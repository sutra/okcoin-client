package com.redv.okcoin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.Depth.Data;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.redv.okcoin.domain.Type;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Ticker.TickerBuilder;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;

/**
 * Various adapters for converting from OKCoin DTOs to XChange DTOs.
 */
public final class OKCoinAdapters {

	/**
	 * Private constructor.
	 */
	private OKCoinAdapters() {
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
		return new Trades(tradeList, TradeSortType.SortByTimestamp);
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
}
