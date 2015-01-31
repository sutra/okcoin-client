package org.oxerr.okcoin.rest.service.polling;

import java.util.List;

import org.oxerr.okcoin.rest.OKCoinExchange;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.service.polling.BasePollingExchangeService;
import com.xeiam.xchange.service.polling.BasePollingService;

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
