package com.redv.okcoin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import si.mazi.rescu.RestProxyFactory;

import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;

public class OKCoinMarketDataServiceRaw extends BasePollingExchangeService {

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

	private static final List<CurrencyPair> CURRENCY_PAIRS = Arrays.asList(
			CurrencyPair.BTC_CNY,
			CurrencyPair.LTC_CNY);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CurrencyPair> getExchangeSymbols() throws IOException {
		return CURRENCY_PAIRS;
	}

	public TickerResponse getTicker(CurrencyPair currencyPair) {
		return okCoin.getTicker(toSymbol(currencyPair));
	}

	public Depth getDepth(CurrencyPair currencyPair) {
		return okCoin.getDepth(toSymbol(currencyPair));
	}

	public Trade[] getTrades(CurrencyPair currencyPair) {
		return okCoin.getTrades(toSymbol(currencyPair));
	}

	public Trade[] getTrades(CurrencyPair currencyPair, int since) {
		return okCoin.getTrades(toSymbol(currencyPair), since);
	}

	private String toSymbol(CurrencyPair currencyPair) {
		return String.format("%1$s_%2$s",
				currencyPair.baseSymbol,
				currencyPair.counterSymbol)
				.toLowerCase();
	}

}
