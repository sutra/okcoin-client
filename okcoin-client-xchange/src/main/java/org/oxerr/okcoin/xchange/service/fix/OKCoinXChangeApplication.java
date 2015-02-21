package org.oxerr.okcoin.xchange.service.fix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import quickfix.field.NoMDEntries;
import quickfix.field.OrigTime;
import quickfix.field.Side;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.trade.LimitOrder;

/**
 * {@link Application} implementation using XChange DTOs as callback parameters.
 */
public class OKCoinXChangeApplication extends OKCoinApplication {

	private final Logger log = LoggerFactory.getLogger(OKCoinXChangeApplication.class);

	public OKCoinXChangeApplication(String apiKey, String secretKey) {
		super(apiKey, secretKey);
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

		List<LimitOrder> asks = new ArrayList<LimitOrder>();
		List<LimitOrder> bids = new ArrayList<LimitOrder>();
		List<Trade> trades = new ArrayList<Trade>();
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
				bids.add(new LimitOrder.Builder(OrderType.BID, currencyPair).limitPrice(px).tradableAmount(size).build());
				break;
			case MDEntryType.OFFER:
				asks.add(new LimitOrder.Builder(OrderType.ASK, currencyPair).limitPrice(px).tradableAmount(size).build());
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

		if (asks.size() > 0 && bids.size() > 0) {
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

			OrderBook orderBook = new OrderBook(origTime, asks, bids);
			onOrderBook(orderBook, sessionId);
		}

		if (trades.size() > 0) {
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

	public void onOrderBook(OrderBook orderBook, SessionID sessionId) {
	}

	public void onTrades(List<Trade> trade, SessionID sessionId) {
	}

	public void onTicker(Ticker ticker) {
	}

	public void onAccountInfo(AccountInfo accountInfo, SessionID sessionId) {
	}

}
