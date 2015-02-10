package org.oxerr.okcoin.fix;

import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MDUpdateType;
import quickfix.field.MarketDepth;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.fix44.MarketDataRequest;
import quickfix.fix44.MarketDataRequest.NoMDEntryTypes;
import quickfix.fix44.MarketDataRequest.NoRelatedSym;

/**
 * Utilities for creating market data request message.
 */
public class MarketDataRequestCreator {

	public MarketDataRequestCreator() {
	}

	public MarketDataRequest createMarketDataRequest(
			String mdReqId,
			String symbol,
			char subscriptionRequestType,
			int marketDepth,
			int mdUpdateType,
			char... mdEntryTypes) {
		MarketDataRequest message = new MarketDataRequest();

		NoRelatedSym symGroup = new NoRelatedSym();
		symGroup.set(new Symbol(symbol));
		message.addGroup(symGroup);

		message.set(new MDReqID(mdReqId));
		message.set(new SubscriptionRequestType(subscriptionRequestType));
		message.set(new MarketDepth(marketDepth));
		message.set(new MDUpdateType(mdUpdateType));

		for (char mdEntryType : mdEntryTypes) {
			NoMDEntryTypes entryTypesGroup = new NoMDEntryTypes();
			entryTypesGroup.set(new MDEntryType(mdEntryType));
			message.addGroup(entryTypesGroup);
		}

		return message;
	}

	public MarketDataRequest createOrderBookRequest(
			String mdReqId,
			String symbol,
			char subscriptionRequestType,
			int marketDepth,
			int mdUpdateType) {
		return createMarketDataRequest(
				mdReqId,
				symbol,
				subscriptionRequestType,
				marketDepth,
				mdUpdateType,
				new char[] { MDEntryType.BID, MDEntryType.OFFER, });
	}

	public MarketDataRequest createLiveTradesRequest(
			String mdReqId,
			String symbol) {
		return createMarketDataRequest(
				mdReqId,
				symbol,
				SubscriptionRequestType.SNAPSHOT,
				0,
				MDUpdateType.INCREMENTAL_REFRESH,
				new char[] { MDEntryType.TRADE, });
	}

	public MarketDataRequest create24HTickerRequest(
			String mdReqId,
			String symbol) {
		return createMarketDataRequest(
				mdReqId,
				symbol,
				SubscriptionRequestType.SNAPSHOT,
				0,
				MDUpdateType.FULL_REFRESH,
				new char[] {
						MDEntryType.OPENING_PRICE,
						MDEntryType.CLOSING_PRICE,
						MDEntryType.TRADING_SESSION_HIGH_PRICE,
						MDEntryType.TRADING_SESSION_LOW_PRICE,
						MDEntryType.TRADING_SESSION_VWAP_PRICE,
						MDEntryType.TRADE_VOLUME,
					});

	}

}
