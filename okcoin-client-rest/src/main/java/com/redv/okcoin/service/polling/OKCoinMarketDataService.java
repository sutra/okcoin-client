package com.redv.okcoin.service.polling;

import java.io.IOException;

import com.redv.okcoin.OKCoinAdapters;
import com.redv.okcoin.domain.Trade;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.exceptions.NotYetImplementedForExchangeException;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

public class OKCoinMarketDataService extends OKCoinMarketDataServiceRaw
		implements PollingMarketDataService {

	/**
	 * Constructor.
	 *
	 * @param exchangeSpecification The {@link ExchangeSpecification}.
	 */
	public OKCoinMarketDataService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticker getTicker(CurrencyPair currencyPair, Object... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		return OKCoinAdapters.adaptTicker(getTicker(currencyPair), currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		return OKCoinAdapters.adaptOrderBook(getDepth(currencyPair), currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trades getTrades(CurrencyPair currencyPair, Object... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		final Trade[] trades;
		if (args.length == 0) {
			trades = getTrades(currencyPair);
		} else {
			trades = getTrades(currencyPair, (Long) args[0]);
		}
		return OKCoinAdapters.adaptTrades(trades, currencyPair);
	}

}
