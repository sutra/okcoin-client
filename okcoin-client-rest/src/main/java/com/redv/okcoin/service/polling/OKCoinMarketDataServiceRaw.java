package com.redv.okcoin.service.polling;

import java.io.IOException;

import si.mazi.rescu.RestProxyFactory;

import com.redv.okcoin.OKCoin;
import com.redv.okcoin.OKCoinAdapters;
import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;

public class OKCoinMarketDataServiceRaw extends OKCoinBasePollingService {

	private final OKCoin okCoin;

	protected OKCoinMarketDataServiceRaw(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		final String baseUrl = spec.getSslUri();
		okCoin = RestProxyFactory.createProxy(OKCoin.class, baseUrl);
	}

	public TickerResponse getTicker(CurrencyPair currencyPair)
			throws IOException {
		return okCoin.getTicker(OKCoinAdapters.adaptSymbol(currencyPair));
	}

	public Depth getDepth(CurrencyPair currencyPair) throws IOException {
		return okCoin.getDepth(OKCoinAdapters.adaptSymbol(currencyPair));
	}

	public Trade[] getTrades(CurrencyPair currencyPair) throws IOException {
		return okCoin.getTrades(OKCoinAdapters.adaptSymbol(currencyPair));
	}

	public Trade[] getTrades(CurrencyPair currencyPair, long since)
			throws IOException {
		return okCoin.getTrades(OKCoinAdapters.adaptSymbol(currencyPair), since);
	}

}
