package org.oxerr.okcoin.examples.rest;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.knowm.xchange.currency.CurrencyPair.BTC_CNY;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;
import static org.knowm.xchange.currency.CurrencyPair.LTC_CNY;
import static org.knowm.xchange.currency.CurrencyPair.LTC_USD;

import java.io.IOException;
import java.util.Arrays;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.service.polling.marketdata.PollingMarketDataService;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.CandlestickChart;
import org.oxerr.okcoin.rest.dto.Depth;
import org.oxerr.okcoin.rest.dto.TickerResponse;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.service.polling.OKCoinMarketDataServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstration of getting market data.
 */
public class MarketDataDemo {

	private final Logger log = LoggerFactory.getLogger(MarketDataDemo.class);

	private final PollingMarketDataService mdService;
	private final OKCoinMarketDataServiceRaw rawMdService;

	public MarketDataDemo(Exchange exchange) {
		mdService = exchange.getPollingMarketDataService();
		rawMdService = (OKCoinMarketDataServiceRaw) mdService;
	}

	public void demoTicker(CurrencyPair currencyPair) throws IOException {
		Ticker ticker = mdService.getTicker(currencyPair);
		log.info("{} ticker: {}", currencyPair, reflectionToString(ticker, MULTI_LINE_STYLE));
	}

	public void demoTicker(String symbol) throws IOException {
		TickerResponse response = rawMdService.getTicker(symbol);
		log.info("ticker date: {}", response.getDate());
		log.info("{} ticker: {}", symbol, reflectionToString(response.getTicker(), MULTI_LINE_STYLE));
	}

	public void demoDepth(CurrencyPair currencyPair) throws ExchangeException, IOException {
		OrderBook orderBook = mdService.getOrderBook(currencyPair);
		log.info("{} best bid: {}", currencyPair, orderBook.getBids().get(0));
		log.info("{} best ask: {}", currencyPair, orderBook.getAsks().get(0));
	}

	public void demoDepth(String symbol) throws IOException {
		Depth depth = rawMdService.getDepth(symbol, null, null);
		log.info("{} best bid: {}@{}", symbol, depth.getBids()[0][1], depth.getBids()[0][0]);
		log.info("{} best ask: {}@{}", symbol,
				depth.getAsks()[depth.getAsks().length - 1][1],
				depth.getAsks()[depth.getAsks().length - 1][0]);
	}

	public void demoTrades(CurrencyPair currencyPair) throws IOException {
		Trades trades = mdService.getTrades(currencyPair);
		log.info("trades: {}", trades.getTrades());
		log.info("last ID: {}", trades.getlastID());
	}

	public void demoTrades(String symbol) throws IOException {
		Trade[] trades = rawMdService.getTrades(symbol, null);
		log.info("trades: {}", Arrays.toString(trades));
	}

	public void demoCandlestickChart(String symbol, String type, Integer size,
			Long since) throws IOException {
		CandlestickChart chart = rawMdService.getCandlestickChart(symbol, type, size, since);
		log.info("candlestick chart: {}", chart);
	}

	public static void main(String[] args) throws IOException {
		// www.okcoin.cn
		Exchange domesticExchange = ExchangeFactory.INSTANCE
				.createExchange(OKCoinExchange.class.getName());
		MarketDataDemo domesticDemo = new MarketDataDemo(domesticExchange);

		domesticDemo.demoTicker(BTC_CNY);
		domesticDemo.demoTicker("btc_cny");

		domesticDemo.demoTicker(LTC_CNY);
		domesticDemo.demoTicker("ltc_cny");

		domesticDemo.demoDepth(BTC_CNY);
		domesticDemo.demoDepth("btc_cny");

		domesticDemo.demoDepth(LTC_CNY);
		domesticDemo.demoDepth("ltc_cny");

		domesticDemo.demoTrades(BTC_CNY);
		domesticDemo.demoTrades("btc_cny");

		domesticDemo.demoTrades(LTC_CNY);
		domesticDemo.demoTrades("ltc_cny");

		domesticDemo.demoCandlestickChart("btc_cny", "1min", 1, null);
		domesticDemo.demoCandlestickChart("ltc_cny", "1min", 1, null);

		// www.okcoin.com
		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setSslUri("https://www.okcoin.com");
		Exchange intlExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		MarketDataDemo intlDemo = new MarketDataDemo(intlExchange);

		intlDemo.demoTicker(BTC_USD);
		intlDemo.demoTicker("btc_usd");

		intlDemo.demoTicker(LTC_USD);
		intlDemo.demoTicker("ltc_usd");

		intlDemo.demoDepth(BTC_USD);
		intlDemo.demoDepth("btc_usd");

		intlDemo.demoDepth(LTC_USD);
		intlDemo.demoDepth("ltc_usd");

		intlDemo.demoTrades(BTC_USD);
		intlDemo.demoTrades("btc_usd");

		intlDemo.demoTrades(LTC_USD);
		intlDemo.demoTrades("ltc_usd");

		intlDemo.demoCandlestickChart("btc_usd", "1min", 1, null);
		intlDemo.demoCandlestickChart("ltc_usd", "1min", 1, null);
	}

}
