package org.oxerr.okcoin.rest.service.polling;

import static org.oxerr.okcoin.rest.OKCoinAdapters.adaptOrderBook;
import static org.oxerr.okcoin.rest.OKCoinAdapters.adaptSymbol;
import static org.oxerr.okcoin.rest.OKCoinAdapters.adaptTicker;

import java.io.IOException;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.polling.marketdata.PollingMarketDataService;
import org.oxerr.okcoin.rest.OKCoinAdapters;
import org.oxerr.okcoin.rest.dto.Trade;

/**
 * {@link PollingMarketDataService} implementation.
 */
public class OKCoinMarketDataService extends OKCoinMarketDataServiceRaw
		implements PollingMarketDataService {

	public OKCoinMarketDataService(Exchange exchange) {
		super(exchange);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ticker getTicker(CurrencyPair currencyPair, Object... args)
			throws IOException {
		return adaptTicker(getTicker(adaptSymbol(currencyPair)), currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args)
			throws IOException {
		return adaptOrderBook(
			getDepth(
				adaptSymbol(currencyPair),
				args.length > 0 ? ((Number) args[0]).intValue() : null,
				args.length > 1 ? ((Number) args[1]).intValue() : null),
			currencyPair);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trades getTrades(CurrencyPair currencyPair, Object... args)
			throws IOException {
		final String symbol = OKCoinAdapters.adaptSymbol(currencyPair);
		final Trade[] trades;
		if (args.length == 0) {
			trades = getTrades(symbol, (Long) null);
		} else {
			trades = getTrades(symbol, ((Number) args[0]).longValue());
		}
		return OKCoinAdapters.adaptTrades(trades, currencyPair);
	}

}
