package com.redv.okcoin.service.polling;

import java.util.Collection;

import com.redv.okcoin.OKCoinExchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;

public class OKCoinBasePollingService extends BasePollingExchangeService {

	private final Collection<CurrencyPair> symbols;

	/**
	 * @param exchangeSpecification the exchange specification.
	 */
	@SuppressWarnings("unchecked")
	protected OKCoinBasePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
		symbols = (Collection<CurrencyPair>) exchangeSpecification
				.getExchangeSpecificParametersItem(
						OKCoinExchange.SYMBOLS_PARAMETER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<CurrencyPair> getExchangeSymbols() {
		return symbols;
	}

}
