package org.oxerr.okcoin.rest;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;
import org.oxerr.okcoin.rest.OKCoinAdapters;
import org.oxerr.okcoin.rest.domain.Depth;
import org.oxerr.okcoin.rest.domain.OrderResult;
import org.oxerr.okcoin.rest.domain.TickerResponse;
import org.oxerr.okcoin.rest.domain.Trade;
import org.oxerr.okcoin.rest.domain.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.Wallet;

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
				.getResource("domain/tickerResponse.json"),
				TickerResponse.class);
		Ticker ticker = OKCoinAdapters.adaptTicker(tickerResponse, CurrencyPair.BTC_CNY);
		assertEquals(new BigDecimal("809.00"), ticker.getHigh());
		assertEquals(new BigDecimal("770.01"), ticker.getLow());
		assertEquals(new BigDecimal("787.10"), ticker.getBid());
		assertEquals(new BigDecimal("787.27"), ticker.getAsk());
		assertEquals(new BigDecimal("787.00"), ticker.getLast());
		assertEquals(new BigDecimal("2625.38100000"), ticker.getVolume());
	}

	@Test
	public void testAdaptOrderBook() throws IOException {
		Depth depth = mapper.readValue(
				getClass().getResource("domain/depth.json"), Depth.class);
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
				getClass().getResource("domain/trades.json"), Trade[].class);
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
				getClass().getResource("domain/userinfo.json"), UserInfo.class);
		AccountInfo accountInfo = OKCoinAdapters.adaptAccountInfo(userInfo);
		for (Wallet wallet : accountInfo.getWallets()) {
			log.debug("{}: {}", wallet.getCurrency(), wallet.getBalance());
		}
		assertEquals(new BigDecimal("2000"), accountInfo.getBalance("CNY"));
		assertEquals(new BigDecimal("20"), accountInfo.getBalance("BTC"));
		assertEquals(new BigDecimal("0"), accountInfo.getBalance("LTC"));
	}

	@Test
	public void testAdaptOpenOrders() throws IOException {
		OrderResult orderResult = mapper.readValue(
				getClass().getResource("domain/getorder-limit-orders.json"),
				OrderResult.class);
		OpenOrders openOrders = OKCoinAdapters.adaptOpenOrders(Arrays.asList(orderResult));
		assertEquals(2, openOrders.getOpenOrders().size());

		assertEquals("15088", openOrders.getOpenOrders().get(0).getId());
		assertEquals(CurrencyPair.BTC_CNY, openOrders.getOpenOrders().get(0).getCurrencyPair());
		assertEquals(OrderType.ASK, openOrders.getOpenOrders().get(0).getType());
		assertEquals(new BigDecimal("811"), openOrders.getOpenOrders().get(0).getLimitPrice());
		assertEquals(new BigDecimal("0.39901357"), openOrders.getOpenOrders().get(0).getTradableAmount());

		assertEquals("15088", openOrders.getOpenOrders().get(1).getId());
		assertEquals(CurrencyPair.BTC_CNY, openOrders.getOpenOrders().get(1).getCurrencyPair());
		assertEquals(OrderType.ASK, openOrders.getOpenOrders().get(1).getType());
		assertEquals(new BigDecimal("811"), openOrders.getOpenOrders().get(1).getLimitPrice());
		assertEquals(new BigDecimal("0.39901357"), openOrders.getOpenOrders().get(1).getTradableAmount());
	}

}
