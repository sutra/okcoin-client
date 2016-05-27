package org.oxerr.okcoin.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.marketdata.Trades.TradeSortType;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrade;
import org.knowm.xchange.dto.trade.UserTrades;
import org.oxerr.okcoin.rest.dto.Depth;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.dto.TickerResponse;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.dto.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OKCoinAdaptersTest {

	private final Logger log = LoggerFactory.getLogger(OKCoinAdaptersTest.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void testAdaptSymbolCurrencyPair() {
		assertEquals("btc_cny", OKCoinAdapters.adaptSymbol(CurrencyPair.BTC_CNY));
	}

	@Test
	public void testAdaptSymbolString() {
		assertEquals(CurrencyPair.BTC_CNY, OKCoinAdapters.adaptSymbol("btc_cny"));
	}

	@Test
	public void testAdaptTicker() throws IOException {
		TickerResponse tickerResponse = mapper.readValue(getClass()
				.getResource("dto/ticker.json"),
				TickerResponse.class);
		Ticker ticker = OKCoinAdapters.adaptTicker(tickerResponse, CurrencyPair.BTC_CNY);
		assertEquals(1410431279_000L, ticker.getTimestamp().getTime());
		assertEquals(new BigDecimal("34.15"), ticker.getHigh());
		assertEquals(new BigDecimal("32.05"), ticker.getLow());
		assertEquals(new BigDecimal("33.15"), ticker.getBid());
		assertEquals(new BigDecimal("33.16"), ticker.getAsk());
		assertEquals(new BigDecimal("33.15"), ticker.getLast());
		assertEquals(new BigDecimal("10532696.39199642"), ticker.getVolume());
	}

	@Test
	public void testAdaptOrderBook() throws IOException {
		Depth depth = mapper.readValue(
				getClass().getResource("dto/depth.json"), Depth.class);
		OrderBook orderBook = OKCoinAdapters.adaptOrderBook(depth, CurrencyPair.BTC_CNY);
		assertEquals(5, orderBook.getAsks().size());

		// asks should be sorted ascending
		// ask 0.02@787.27
		assertEquals(new BigDecimal("787.27"), orderBook.getAsks().get(0).getLimitPrice());
		assertEquals(new BigDecimal("0.02"), orderBook.getAsks().get(0).getTradableAmount());
		assertEquals(Order.OrderType.ASK, orderBook.getAsks().get(0).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getAsks().get(0).getCurrencyPair());

		// ask 0.036@788.43
		assertEquals(new BigDecimal("788.43"), orderBook.getAsks().get(1).getLimitPrice());
		assertEquals(new BigDecimal("0.036"), orderBook.getAsks().get(1).getTradableAmount());
		assertEquals(OrderType.ASK, orderBook.getAsks().get(1).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getAsks().get(1).getCurrencyPair());

		// ask 0.042@788.99
		assertEquals(new BigDecimal("788.99"), orderBook.getAsks().get(2).getLimitPrice());
		assertEquals(new BigDecimal("0.042"), orderBook.getAsks().get(2).getTradableAmount());
		assertEquals(OrderType.ASK, orderBook.getAsks().get(2).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getAsks().get(2).getCurrencyPair());

		// ask 0.018@789.68
		assertEquals(new BigDecimal("789.68"), orderBook.getAsks().get(3).getLimitPrice());
		assertEquals(new BigDecimal("0.018"), orderBook.getAsks().get(3).getTradableAmount());
		assertEquals(OrderType.ASK, orderBook.getAsks().get(3).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getAsks().get(3).getCurrencyPair());

		// ask 5@792
		assertEquals(new BigDecimal("792"), orderBook.getAsks().get(4).getLimitPrice());
		assertEquals(new BigDecimal("5"), orderBook.getAsks().get(4).getTradableAmount());
		assertEquals(OrderType.ASK, orderBook.getAsks().get(4).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getAsks().get(4).getCurrencyPair());

		assertEquals(7, orderBook.getBids().size());

		// bids should be sorted deascending
		// bid 0.35@787.1
		assertEquals(new BigDecimal("787.1"), orderBook.getBids().get(0).getLimitPrice());
		assertEquals(new BigDecimal("0.35"), orderBook.getBids().get(0).getTradableAmount());
		assertEquals(Order.OrderType.BID, orderBook.getBids().get(0).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(0).getCurrencyPair());

		// bid 12.071@787
		assertEquals(new BigDecimal("787"), orderBook.getBids().get(1).getLimitPrice());
		assertEquals(new BigDecimal("12.071"), orderBook.getBids().get(1).getTradableAmount());
		assertEquals(OrderType.BID, orderBook.getBids().get(1).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(1).getCurrencyPair());

		// bid 0.014@786.5
		assertEquals(new BigDecimal("786.5"), orderBook.getBids().get(2).getLimitPrice());
		assertEquals(new BigDecimal("0.014"), orderBook.getBids().get(2).getTradableAmount());
		assertEquals(OrderType.BID, orderBook.getBids().get(2).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(2).getCurrencyPair());

		// bid 0.38@786.2
		assertEquals(new BigDecimal("786.2"), orderBook.getBids().get(3).getLimitPrice());
		assertEquals(new BigDecimal("0.38"), orderBook.getBids().get(3).getTradableAmount());
		assertEquals(OrderType.BID, orderBook.getBids().get(3).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(3).getCurrencyPair());

		// bid 3.217@786
		assertEquals(new BigDecimal("786"), orderBook.getBids().get(4).getLimitPrice());
		assertEquals(new BigDecimal("3.217"), orderBook.getBids().get(4).getTradableAmount());
		assertEquals(OrderType.BID, orderBook.getBids().get(4).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(4).getCurrencyPair());

		// bid 5.322@785.3
		assertEquals(new BigDecimal("785.3"), orderBook.getBids().get(5).getLimitPrice());
		assertEquals(new BigDecimal("5.322"), orderBook.getBids().get(5).getTradableAmount());
		assertEquals(OrderType.BID, orderBook.getBids().get(5).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(5).getCurrencyPair());

		// bid 5.04@785.04
		assertEquals(new BigDecimal("785.04"), orderBook.getBids().get(6).getLimitPrice());
		assertEquals(new BigDecimal("5.04"), orderBook.getBids().get(6).getTradableAmount());
		assertEquals(OrderType.BID, orderBook.getBids().get(6).getType());
		assertEquals(CurrencyPair.BTC_CNY, orderBook.getBids().get(6).getCurrencyPair());
	}

	@Test
	public void testAdaptTrades() throws IOException {
		Trade[] tradeArray = mapper.readValue(
				getClass().getResource("dto/trades.json"), Trade[].class);
		Trades trades = OKCoinAdapters.adaptTrades(tradeArray, CurrencyPair.BTC_CNY);
		assertEquals(230512L, trades.getlastID());
		assertEquals(TradeSortType.SortByTimestamp, trades.getTradeSortType());
		assertEquals(4, trades.getTrades().size());

		assertEquals(1367130137000L, trades.getTrades().get(0).getTimestamp().getTime());
		assertEquals(new BigDecimal("787.71"), trades.getTrades().get(0).getPrice());
		assertEquals(new BigDecimal("0.003"), trades.getTrades().get(0).getTradableAmount());
		assertEquals("230433", trades.getTrades().get(0).getId());
		assertEquals(OrderType.ASK, trades.getTrades().get(0).getType());

		assertEquals(1367130137000L, trades.getTrades().get(1).getTimestamp().getTime());
		assertEquals(new BigDecimal("787.65"), trades.getTrades().get(1).getPrice());
		assertEquals(new BigDecimal("0.001"), trades.getTrades().get(1).getTradableAmount());
		assertEquals("230434", trades.getTrades().get(1).getId());
		assertEquals(OrderType.ASK, trades.getTrades().get(1).getType());

		assertEquals(1367130137000L, trades.getTrades().get(2).getTimestamp().getTime());
		assertEquals(new BigDecimal("787.5"), trades.getTrades().get(2).getPrice());
		assertEquals(new BigDecimal("0.091"), trades.getTrades().get(2).getTradableAmount());
		assertEquals("230435", trades.getTrades().get(2).getId());
		assertEquals(OrderType.ASK, trades.getTrades().get(2).getType());

		assertEquals(1367131526000L, trades.getTrades().get(3).getTimestamp().getTime());
		assertEquals(new BigDecimal("787.27"), trades.getTrades().get(3).getPrice());
		assertEquals(new BigDecimal("0.03"), trades.getTrades().get(3).getTradableAmount());
		assertEquals("230512", trades.getTrades().get(3).getId());
		assertEquals(OrderType.BID, trades.getTrades().get(3).getType());
	}

	@Test
	public void testAdaptAccountInfo() throws IOException {
		UserInfo userInfo = mapper.readValue(
				getClass().getResource("dto/userinfo.json"), UserInfo.class);
		AccountInfo accountInfo = OKCoinAdapters.adaptAccountInfo(userInfo);
		for (Map.Entry<Currency, Balance> entry : accountInfo.getWallet().getBalances().entrySet()) {
			log.debug("{}: {}", entry.getKey(), entry.getValue());
		}
		assertEquals(new BigDecimal("0"), accountInfo.getWallet().getBalance(Currency.CNY).getBorrowed());
		assertEquals(new BigDecimal("1000"), accountInfo.getWallet().getBalance(Currency.CNY).getAvailable());
		assertEquals(new BigDecimal("1000"), accountInfo.getWallet().getBalance(Currency.CNY).getFrozen());

		assertEquals(new BigDecimal("0"), accountInfo.getWallet().getBalance(Currency.BTC).getBorrowed());
		assertEquals(new BigDecimal("10"), accountInfo.getWallet().getBalance(Currency.BTC).getAvailable());
		assertEquals(new BigDecimal("10"), accountInfo.getWallet().getBalance(Currency.BTC).getFrozen());

		assertEquals(new BigDecimal("0"), accountInfo.getWallet().getBalance(Currency.LTC).getBorrowed());
		assertEquals(new BigDecimal("0"), accountInfo.getWallet().getBalance(Currency.LTC).getAvailable());
		assertEquals(new BigDecimal("0"), accountInfo.getWallet().getBalance(Currency.LTC).getFrozen());
	}

	@Test
	public void testAdaptOpenOrders() throws IOException {
		OrderResult orderResult = mapper.readValue(
				getClass().getResource("dto/order_info.json"),
				OrderResult.class);
		OpenOrders openOrders = OKCoinAdapters.adaptOpenOrders(Arrays.asList(orderResult));
		assertEquals(2, openOrders.getOpenOrders().size());

		assertEquals("10000591", openOrders.getOpenOrders().get(0).getId());
		assertEquals(CurrencyPair.BTC_CNY, openOrders.getOpenOrders().get(0).getCurrencyPair());
		assertEquals(OrderType.ASK, openOrders.getOpenOrders().get(0).getType());
		assertEquals(new BigDecimal("500"), openOrders.getOpenOrders().get(0).getLimitPrice());
		assertEquals(new BigDecimal("0.1"), openOrders.getOpenOrders().get(0).getTradableAmount());

		assertEquals("10000724", openOrders.getOpenOrders().get(1).getId());
		assertEquals(CurrencyPair.BTC_CNY, openOrders.getOpenOrders().get(1).getCurrencyPair());
		assertEquals(OrderType.BID, openOrders.getOpenOrders().get(1).getType());
		assertEquals(new BigDecimal("0.1"), openOrders.getOpenOrders().get(1).getLimitPrice());
		assertEquals(new BigDecimal("0.2"), openOrders.getOpenOrders().get(1).getTradableAmount());
	}

	@Test
	public void testAdaptUserTrades() throws IOException {
		OrderHistory orderHistory = mapper.readValue(getClass().getResource("dto/order_history_with_market_orders.json"), OrderHistory.class);
		UserTrades userTrades = OKCoinAdapters.adaptUserTrades(orderHistory);

		UserTrade trade = userTrades.getUserTrades().get(0);
		assertEquals(new BigDecimal("0.1"), trade.getTradableAmount());
		assertEquals(new BigDecimal("1467.94"), trade.getPrice());
		assertEquals(1424313017000L, trade.getTimestamp().getTime());
		assertEquals("342062526", trade.getOrderId());
		assertNull(trade.getId());
		assertEquals(CurrencyPair.BTC_CNY, trade.getCurrencyPair());
		assertEquals(OrderType.ASK, trade.getType());

		trade = userTrades.getUserTrades().get(7);
		assertEquals(new BigDecimal("0.01"), trade.getTradableAmount());
		assertEquals(new BigDecimal("1467.98"), trade.getPrice());
		assertEquals(1424315429000L, trade.getTimestamp().getTime());
		assertEquals("342110762", trade.getOrderId());
		assertNull(trade.getId());
		assertEquals(CurrencyPair.BTC_CNY, trade.getCurrencyPair());
		assertEquals(OrderType.ASK, trade.getType());

		trade = userTrades.getUserTrades().get(8);
		assertEquals(new BigDecimal("0.01"), trade.getTradableAmount());
		assertEquals(new BigDecimal("1467.99"), trade.getPrice());
		assertEquals(1424315585000L, trade.getTimestamp().getTime());
		assertEquals("342113073", trade.getOrderId());
		assertNull(trade.getId());
		assertEquals(CurrencyPair.BTC_CNY, trade.getCurrencyPair());
		assertEquals(OrderType.BID, trade.getType());
	}

}
