package com.redv.okcoin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;

public class OKCoinBasePollingService extends BasePollingExchangeService {

	private static final List<CurrencyPair> CURRENCY_PAIRS = Arrays.asList(
			CurrencyPair.BTC_CNY,
			CurrencyPair.LTC_CNY);

	/**
	 * @param exchangeSpecification
	 */
	protected OKCoinBasePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CurrencyPair> getExchangeSymbols() throws IOException {
		return CURRENCY_PAIRS;
	}

}
