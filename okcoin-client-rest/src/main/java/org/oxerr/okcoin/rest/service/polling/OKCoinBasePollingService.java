package org.oxerr.okcoin.rest.service.polling;

import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.service.polling.BasePollingExchangeService;
import org.knowm.xchange.service.polling.BasePollingService;
import org.oxerr.okcoin.rest.OKCoinExchange;

/**
 * {@link BasePollingService} implementation.
 */
public class OKCoinBasePollingService extends BasePollingExchangeService
		implements BasePollingService {

	private final List<CurrencyPair> symbols;

	@SuppressWarnings("unchecked")
	protected OKCoinBasePollingService(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		symbols = (List<CurrencyPair>) spec
				.getExchangeSpecificParametersItem(
						OKCoinExchange.SYMBOLS_PARAMETER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<CurrencyPair> getExchangeSymbols() {
		return symbols;
	}

}
