package org.oxerr.okcoin.websocket;

import static org.junit.Assert.assertEquals;

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
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.websocket.dto.CandlestickChart;
import org.oxerr.okcoin.websocket.dto.Depth;
import org.oxerr.okcoin.websocket.dto.Ticker;
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
