package com.redv.okcoin.service.polling;

import si.mazi.rescu.RestProxyFactory;

import com.redv.okcoin.OKCoin;
import com.redv.okcoin.OKCoinAdapters;
import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;

public class OKCoinMarketDataServiceRaw extends OKCoinBasePollingService {

	private final OKCoin okCoin;

	/**
	 * @param exchangeSpecification
	 */
	protected OKCoinMarketDataServiceRaw(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
		final String baseUrl = exchangeSpecification.getSslUri();
		okCoin = RestProxyFactory.createProxy(OKCoin.class, baseUrl);
	}

	public TickerResponse getTicker(CurrencyPair currencyPair) {
		return okCoin.getTicker(OKCoinAdapters.adaptSymbol(currencyPair));
	}

	public Depth getDepth(CurrencyPair currencyPair) {
		return okCoin.getDepth(OKCoinAdapters.adaptSymbol(currencyPair));
	}

	public Trade[] getTrades(CurrencyPair currencyPair) {
		return okCoin.getTrades(OKCoinAdapters.adaptSymbol(currencyPair));
	}

	public Trade[] getTrades(CurrencyPair currencyPair, long since) {
		return okCoin.getTrades(OKCoinAdapters.adaptSymbol(currencyPair), since);
	}

}
