package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;

import org.oxerr.okcoin.rest.OKCoin;
import org.oxerr.okcoin.rest.OKCoinAdapters;
import org.oxerr.okcoin.rest.domain.Depth;
import org.oxerr.okcoin.rest.domain.TickerResponse;
import org.oxerr.okcoin.rest.domain.Trade;

import si.mazi.rescu.RestProxyFactory;

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
