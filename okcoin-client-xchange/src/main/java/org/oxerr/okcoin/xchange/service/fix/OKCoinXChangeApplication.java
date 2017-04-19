package org.oxerr.okcoin.xchange.service.fix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.oxerr.okcoin.fix.OKCoinApplication;
import org.oxerr.okcoin.fix.fix44.AccountInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quickfix.Application;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryType;
import quickfix.field.MDUpdateType;
import quickfix.field.NoMDEntries;
import quickfix.field.OrigTime;
import quickfix.field.Side;
import quickfix.field.SubscriptionRequestType;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

/**
 * {@link Application} implementation using XChange DTOs as callback parameters.
 */
public class OKCoinXChangeApplication extends OKCoinApplication {

	private final Logger log = LoggerFactory.getLogger(OKCoinXChangeApplication.class);

	private final Map<String, String> mdReqIds = new HashMap<>();

	public OKCoinXChangeApplication(String apiKey, String secretKey) {
		super(apiKey, secretKey);
	}

	/**
	 * Subscribes the order book of the specified symbol.
	 * When the order book refreshed,
	 * the {@link #onOrderBook(Date, List, List, SessionID)}
	 * and {@link #onOrderBook(OrderBook, SessionID)} will be invoked.
	 *
	 * @param symbol the symbol, such as "BTC/CNY", "LTC/CNY".
	 * @param sessionId the FIX session ID.
	 */
	public synchronized void subscribeOrderBook(
			String symbol,
			SessionID sessionId) {
		String mdReqId = UUID.randomUUID().toString();
		log.trace("Subscribing {}...", symbol);
		requestOrderBook(
			mdReqId,
			symbol,
			SubscriptionRequestType.SNAPSHOT_PLUS_UPDATES,
			0,
			MDUpdateType.FULL_REFRESH,
			sessionId);
		mdReqIds.put(symbol, mdReqId);
	}

	public void subscribeOrderBook(CurrencyPair currencyPair, SessionID sessionId) {
		subscribeOrderBook(OKCoinFIXAdapters.adaptSymbol(currencyPair), sessionId);
	}

	/**
	 * Unsubscribes the order book of the specified symbol.
	 *
	 * @param symbol the symbol, such as "BTC/CNY", "LTC/CNY".
	 * @param sessionId the FIX session ID.
	 */
	public synchronized void unsubscribeOrderBook(
			String symbol,
			SessionID sessionId) {
		String mdReqId = mdReqIds.get(symbol);
		if (mdReqId == null) {
			log.trace("{} is not subscribed, skip unsubscribing.", symbol);
			return;
		}

		log.trace("Unsubscribing {}...", symbol);
		requestOrderBook(
			mdReqId,
			symbol,
			SubscriptionRequestType.DISABLE_PREVIOUS_SNAPSHOT_PLUS_UPDATE_REQUEST,
			0,
			MDUpdateType.FULL_REFRESH,
			sessionId);
		mdReqIds.remove(symbol);
	}

	public void unsubscribeOrderBook(CurrencyPair currencyPair, SessionID sessionId) {
		unsubscribeOrderBook(OKCoinFIXAdapters.adaptSymbol(currencyPair), sessionId);
	}

	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message,
			SessionID sessionId) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		Date origTime = message.getField(new OrigTime()).getValue();
		String symbol = message.getSymbol().getValue();
		String mdReqId = message.isSetMDReqID() ? message.getMDReqID().getValue() : null;
		String[] symbols = symbol.split("/");
		CurrencyPair currencyPair = new CurrencyPair(symbols[0], symbols[1]);

		log.debug("OrigTime: {}", origTime);
		log.debug("Symbol: {}, currency pair: {}", symbol, currencyPair);
		log.debug("MDReqID: {}", mdReqId);

		List<LimitOrder> asks = new ArrayList<>();
		List<LimitOrder> bids = new ArrayList<>();
		List<Trade> trades = new ArrayList<>();
		BigDecimal openingPrice = null, closingPrice = null,
			highPrice = null, lowPrice = null,
			vwapPrice = null, lastPrice = null,
			volume = null;

		for (int i = 1, l = message.getNoMDEntries().getValue(); i <= l; i++) {
			Group group = message.getGroup(i, NoMDEntries.FIELD);
			char type = group.getChar(MDEntryType.FIELD);
			BigDecimal px = group.getField(new MDEntryPx()).getValue();
			BigDecimal size = group.isSetField(MDEntrySize.FIELD) ? group.getField(new MDEntrySize()).getValue() : null;
			log.debug("type: {}, px: {}, size: {}", type, px, size);

			switch (type) {
			case MDEntryType.BID:
				bids.add(new LimitOrder.Builder(OrderType.BID, currencyPair).limitPrice(px).tradableAmount(size).timestamp(origTime).build());
				break;
			case MDEntryType.OFFER:
				asks.add(new LimitOrder.Builder(OrderType.ASK, currencyPair).limitPrice(px).tradableAmount(size).timestamp(origTime).build());
				break;
			case MDEntryType.TRADE:
				OrderType orderType = group.getField(new Side()).getValue() == Side.BUY ? OrderType.BID : OrderType.ASK;
				Trade trade = new Trade.Builder().currencyPair(currencyPair).type(orderType).price(px).tradableAmount(size).build();
				trades.add(trade);
				break;
			case MDEntryType.OPENING_PRICE:
				openingPrice = px;
				break;
			case MDEntryType.CLOSING_PRICE:
				closingPrice = px;
				break;
			case MDEntryType.TRADING_SESSION_HIGH_PRICE:
				highPrice = px;
				break;
			case MDEntryType.TRADING_SESSION_LOW_PRICE:
				lowPrice = px;
				break;
			case MDEntryType.TRADING_SESSION_VWAP_PRICE:
				vwapPrice = px;
				break;
			case MDEntryType.TRADE_VOLUME:
				lastPrice = px;
				volume = size;
				break;
			default:
				break;
			}
		}

		if (!asks.isEmpty() && !bids.isEmpty()) {
			onOrderBook(origTime, asks, bids, sessionId);
		}

		if (!trades.isEmpty()) {
			onTrades(trades, sessionId);
		}

		if (openingPrice != null && closingPrice != null
			&& highPrice != null && lowPrice != null
			&& vwapPrice != null && lastPrice != null
			&& volume != null) {
			Ticker ticker = new Ticker.Builder()
				.currencyPair(currencyPair)
				.timestamp(origTime)
				.high(highPrice)
				.low(lowPrice)
				.last(lastPrice)
				.volume(volume)
				.build();
			onTicker(ticker);
		}
	}

	@Override
	public void onMessage(AccountInfoResponse message, SessionID sessionId)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		onAccountInfo(OKCoinFIXAdapters.adaptAccountInfo(message), sessionId);
	}

	/**
	 * Invoked when the order book updated.
	 *
	 * @param origTime time of message origination.
	 * @param asks ask orders.
	 * @param bids bid orders.
	 * @param sessionId the FIX session ID.
	 */
	public void onOrderBook(Date origTime, List<LimitOrder> asks, List<LimitOrder> bids, SessionID sessionId) {
		LimitOrder lowestAsk = asks.get(0);
		LimitOrder highestBid = bids.get(0);

		if (lowestAsk.getLimitPrice().compareTo(highestBid.getLimitPrice()) <= 0) {
			// OKCoin's bid/ask of SNAPSHOT are reversed?
			// Swap the bid/ask orders
			List<LimitOrder> tmpAsks = new ArrayList<>(asks);

			asks.clear();
			for (LimitOrder limitOrder : bids) {
				asks.add(LimitOrder.Builder.from(limitOrder).orderType(OrderType.ASK).build());
			}

			bids.clear();
			for (LimitOrder limitOrder : tmpAsks) {
				bids.add(LimitOrder.Builder.from(limitOrder).orderType(OrderType.BID).build());
			}
		}

		// bids should be sorted by limit price descending
		Collections.sort(bids);

		// asks should be sorted by limit price ascending
		Collections.sort(asks);

		OrderBook orderBook = new OrderBook(origTime, asks, bids);;
		onOrderBook(orderBook, sessionId);
	}

	/**
	 * Invoked when the order book updated.
	 *
	 * @param orderBook the full order book.
	 * @param sessionId the FIX session ID.
	 */
	public void onOrderBook(OrderBook orderBook, SessionID sessionId) {
	}

	public void onTrades(List<Trade> trade, SessionID sessionId) {
	}

	public void onTicker(Ticker ticker) {
	}

	public void onAccountInfo(AccountInfo accountInfo, SessionID sessionId) {
	}

}
