package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;

import org.oxerr.okcoin.rest.OKCoin;
import org.oxerr.okcoin.rest.dto.CandlestickChart;
import org.oxerr.okcoin.rest.dto.Depth;
import org.oxerr.okcoin.rest.dto.TickerResponse;
import org.oxerr.okcoin.rest.dto.Trade;

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinMarketDataServiceRaw extends OKCoinBasePollingService {

	private final OKCoin okCoin;

	protected OKCoinMarketDataServiceRaw(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		final String baseUrl = spec.getSslUri();
		okCoin = RestProxyFactory.createProxy(OKCoin.class, baseUrl);
	}

	public TickerResponse getTicker(String symbol) throws IOException {
		return okCoin.getTicker(symbol);
	}

	public Depth getDepth(String symbol, Integer size, Integer merge)
			throws IOException {
		return okCoin.getDepth(symbol, size, merge);
	}

	public Trade[] getTrades(String symbol, Long since) throws IOException {
		return okCoin.getTrades(symbol, since);
	}

	/**
	 * Get BTC/LTC Candlestick Data.
	 *
	 * @param symbol the symbol.
	 * @param type type of candlestick.
	 * @param size 1 based.
	 * @param since since timestamp.
	 * @return Candlestick data.
	 * @throws IOException indicates I/O exception.
	 */
	public CandlestickChart getCandlestickChart(String symbol, String type,
			Integer size, Long since) throws IOException {
		return okCoin.getCandlestickChart(symbol, type, size, since);
	}

}
