package org.oxerr.okcoin.websocket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.websocket.DecodeException;

import org.junit.Test;
import org.oxerr.okcoin.rest.dto.Candlestick;
import org.oxerr.okcoin.rest.dto.Funds;
import org.oxerr.okcoin.rest.dto.Order;
import org.oxerr.okcoin.rest.dto.Status;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.websocket.dto.CandlestickChart;
import org.oxerr.okcoin.websocket.dto.Depth;
import org.oxerr.okcoin.websocket.dto.Info;
import org.oxerr.okcoin.websocket.dto.OrderResult;
import org.oxerr.okcoin.websocket.dto.Ticker;
import org.oxerr.okcoin.websocket.dto.TradeResult;
import org.oxerr.okcoin.websocket.dto.TradesV1;

public class OKCoinDecoderTest {

	private OKCoinDecoder decoder = new OKCoinDecoder();

	@Test
	public void testDecodeTicker() throws IOException, DecodeException {
		OKCoinData[] data = decode("dto/ok_btccny_ticker.json");
		assertEquals(1, data.length);
		assertEquals("ok_btccny_ticker", data[0].getChannel());
		assertEquals(Ticker.class, data[0].getData().getClass());
		Ticker ticker = (Ticker) data[0].getData();
		assertEquals(new BigDecimal("2478.3"), ticker.getBuy());
		assertEquals(new BigDecimal("2555"), ticker.getHigh());
		assertEquals(new BigDecimal("2478.51"), ticker.getLast());
		assertEquals(new BigDecimal("2466"), ticker.getLow());
		assertEquals(new BigDecimal("2478.5"), ticker.getSell());
		assertEquals(1411718074965L, ticker.getTimestamp().toEpochMilli());
		assertEquals(new BigDecimal("49020.3"), ticker.getVol());
	}

	@Test
	public void testDecodeDepth() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_btccny_depth.json");
		assertEquals(1, data.length);
		assertEquals("ok_btccny_depth", data[0].getChannel());

		Depth depth = (Depth) data[0].getData();

		assertEquals(new BigDecimal("2473.88"), depth.getBids()[0][0]);
		assertEquals(new BigDecimal("2.025"), depth.getBids()[0][1]);

		assertEquals(new BigDecimal("2473.5"), depth.getBids()[1][0]);
		assertEquals(new BigDecimal("2.4"), depth.getBids()[1][1]);

		assertEquals(new BigDecimal("2470"), depth.getBids()[2][0]);
		assertEquals(new BigDecimal("12.203"), depth.getBids()[2][1]);

		assertEquals(new BigDecimal("2484"), depth.getAsks()[0][0]);
		assertEquals(new BigDecimal("17.234"), depth.getAsks()[0][1]);

		assertEquals(new BigDecimal("2483.01"), depth.getAsks()[1][0]);
		assertEquals(new BigDecimal("6"), depth.getAsks()[1][1]);

		assertEquals(new BigDecimal("2482.88"), depth.getAsks()[2][0]);
		assertEquals(new BigDecimal("3"), depth.getAsks()[2][1]);

		assertEquals(1411718972024L, depth.getTimestamp().toEpochMilli());
	}

	@Test
	public void testDecodeTradesV1() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_btccny_trades_v1.json");
		assertEquals(1, data.length);
		assertEquals("ok_btccny_trades_v1", data[0].getChannel());

		TradesV1 tradesV1 = (TradesV1) data[0].getData();
		assertEquals(1, tradesV1.getTrades().length);

		Trade[] trades = tradesV1.getTrades();
		assertEquals(1001, trades[0].getTid());
		assertEquals(new BigDecimal("2463.86"), trades[0].getPrice());
		assertEquals(new BigDecimal("0.052"), trades[0].getAmount());
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		assertEquals("16:34:07", df.format(Date.from(trades[0].getDate())));
		assertEquals(Type.SELL, trades[0].getType());
	}

	@Test
	public void testDecodeKlineFirst() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_btccny_kline_1min_first.json");
		assertEquals(1, data.length);
		assertEquals("ok_btccny_kline_1min", data[0].getChannel());

		CandlestickChart cc = (CandlestickChart) data[0].getData();
		Candlestick[] sticks = cc.getCandlesticks();
		assertEquals(2, sticks.length);

		Candlestick stick = sticks[0];
		assertEquals(1411720800000L, stick.getTimestamp().toEpochMilli());
		assertEquals(new BigDecimal("2466.98"), stick.getOpen());
		assertEquals(new BigDecimal("2467.98"), stick.getHigh());
		assertEquals(new BigDecimal("2466.21"), stick.getLow());
		assertEquals(new BigDecimal("2467.97"), stick.getClose());
		assertEquals(new BigDecimal("101.3"), stick.getVolume());

		stick = sticks[1];
		assertEquals(1411720860000L, stick.getTimestamp().toEpochMilli());
		assertEquals(new BigDecimal("2467.96"), stick.getOpen());
		assertEquals(new BigDecimal("2467.98"), stick.getHigh());
		assertEquals(new BigDecimal("2467.94"), stick.getLow());
		assertEquals(new BigDecimal("2467.98"), stick.getClose());
		assertEquals(new BigDecimal("5.89"), stick.getVolume());
	}

	@Test
	public void testDecodeKlineFollowUp() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_btccny_kline_1min_follow.json");
		assertEquals(1, data.length);
		assertEquals("ok_btccny_kline_1min", data[0].getChannel());

		CandlestickChart cc = (CandlestickChart) data[0].getData();
		Candlestick[] sticks = cc.getCandlesticks();
		assertEquals(1, sticks.length);

		Candlestick stick = sticks[0];
		assertEquals(1425528420000L, stick.getTimestamp().toEpochMilli());
		assertEquals(new BigDecimal("1694.44"), stick.getOpen());
		assertEquals(new BigDecimal("1695.2"), stick.getHigh());
		assertEquals(new BigDecimal("1694.44"), stick.getLow());
		assertEquals(new BigDecimal("1695.2"), stick.getClose());
		assertEquals(new BigDecimal("34.24"), stick.getVolume());
	}

	@Test
	public void testDecodeRealTrades() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_cny_realtrades.json");
		assertEquals(1, data.length);
		assertEquals("ok_cny_realtrades", data[0].getChannel());

		org.oxerr.okcoin.websocket.dto.Trade realTrade = (org.oxerr.okcoin.websocket.dto.Trade) data[0].getData();
		assertEquals(new BigDecimal("0"), realTrade.getAveragePrice());
		assertEquals(new BigDecimal("0"), realTrade.getCompletedTradeAmount());
		assertEquals(1422258604000L, realTrade.getCreatedDate().toEpochMilli());
		assertEquals(268013884L, realTrade.getId());
		assertEquals(268013884L, realTrade.getOrderId());
		assertEquals(new BigDecimal("0"), realTrade.getSigTradeAmount());
		assertEquals(new BigDecimal("0"), realTrade.getSigTradePrice());
		assertEquals(Status.CANCELLED, realTrade.getStatus());
		assertEquals("btc_cny", realTrade.getSymbol());
		assertEquals(new BigDecimal("1.105"), realTrade.getTradeAmount());
		assertEquals(new BigDecimal("0"), realTrade.getTradePrice());
		assertEquals(Type.BUY, realTrade.getTradeType());
		assertEquals(new BigDecimal("1853.74"), realTrade.getTradeUnitPrice());
		assertEquals(new BigDecimal("0"), realTrade.getUnTrade());
	}

	@Test
	public void testDecodeTradeResult() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_spotcny_trade.json");
		assertEquals(1, data.length);
		assertEquals("ok_spotcny_trade", data[0].getChannel());

		TradeResult tradeResult = (TradeResult) data[0].getData();
		assertEquals(125433029L, tradeResult.getOrderId());
		assertTrue(tradeResult.getResult());
	}

	@Test
	public void testDecodeCancelOrderResult() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_spotcny_cancel_order.json");
		assertEquals(1, data.length);
		assertEquals("ok_spotcny_cancel_order", data[0].getChannel());

		TradeResult tradeResult = (TradeResult) data[0].getData();
		assertEquals(125433027L, tradeResult.getOrderId());
		assertTrue(tradeResult.getResult());
	}

	@Test
	public void testDecodeUserInfo() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_spotcny_userinfo.json");
		assertEquals(1, data.length);
		assertEquals("ok_spotcny_userinfo", data[0].getChannel());

		Info info = (Info) data[0].getData();
		Funds funds = info.getFunds();
		assertEquals(new BigDecimal("0"), funds.getAsset().get("net"));
		assertEquals(new BigDecimal("0"), funds.getAsset().get("total"));

		assertEquals(new BigDecimal("0"), funds.getBorrow().get("btc"));
		assertEquals(new BigDecimal("0"), funds.getBorrow().get("cny"));
		assertEquals(new BigDecimal("0"), funds.getBorrow().get("ltc"));

		assertEquals(new BigDecimal("0"), funds.getFree().get("btc"));
		assertEquals(new BigDecimal("0"), funds.getFree().get("cny"));
		assertEquals(new BigDecimal("0"), funds.getFree().get("ltc"));

		assertEquals(new BigDecimal("0"), funds.getFrozen().get("btc"));
		assertEquals(new BigDecimal("0"), funds.getFrozen().get("cny"));
		assertEquals(new BigDecimal("0"), funds.getFrozen().get("ltc"));

		assertEquals(new BigDecimal("0"), funds.getUnionFund().get("btc"));
		assertEquals(new BigDecimal("0"), funds.getUnionFund().get("ltc"));
	}

	@Test
	public void testDecodeOrderResult() throws DecodeException, IOException {
		OKCoinData[] data = decode("dto/ok_spotcny_order_info.json");
		assertEquals(1, data.length);
		assertEquals("ok_spotcny_order_info", data[0].getChannel());

		OrderResult result = (OrderResult) data[0].getData();
		Order order = result.getOrders()[0];
		assertEquals(new BigDecimal("0.1"), order.getAmount());
		assertEquals(new BigDecimal("1.961"), order.getAvgPrice());
		assertEquals(1422502117000L, order.getCreateDate().toEpochMilli());
		assertEquals(new BigDecimal("0.1"), order.getDealAmount());
		assertEquals(20914907L, order.getOrderId());
		assertEquals(new BigDecimal("0"), order.getPrice());
		assertEquals(Status.FULLY_FILLED, order.getStatus());
		assertEquals("ltc_cny", order.getSymbol());
		assertEquals(Type.SELL_MARKET, order.getType());
	}

	private OKCoinData[] decode(String resource) throws DecodeException, IOException {
		try (
			InputStream input = getClass().getResourceAsStream(resource);
			Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
		) {
			OKCoinData[] data = decoder.decode(reader);
			return data;
		}
	}

}
