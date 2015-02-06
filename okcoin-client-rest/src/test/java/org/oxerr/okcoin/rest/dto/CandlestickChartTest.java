package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class CandlestickChartTest extends UnmarshalTest {

	@Test
	public void testCandlestickChart() throws IOException {
		CandlestickChart chart = readValue("kline.json", CandlestickChart.class);
		assertEquals(2, chart.getCandlesticks().length);

		assertEquals(1417478400000L, chart.getCandlesticks()[0].getTimestamp().toEpochMilli());
		assertEquals(new BigDecimal("380.94"), chart.getCandlesticks()[0].getOpen());
		assertEquals(new BigDecimal("387.7"), chart.getCandlesticks()[0].getHigh());
		assertEquals(new BigDecimal("378.75"), chart.getCandlesticks()[0].getLow());
		assertEquals(new BigDecimal("384.61"), chart.getCandlesticks()[0].getClose());
		assertEquals(new BigDecimal("6857.31"), chart.getCandlesticks()[0].getVolume());

		assertEquals(1417564800000L, chart.getCandlesticks()[1].getTimestamp().toEpochMilli());
		assertEquals(new BigDecimal("384.47"), chart.getCandlesticks()[1].getOpen());
		assertEquals(new BigDecimal("387.13"), chart.getCandlesticks()[1].getHigh());
		assertEquals(new BigDecimal("383.5"), chart.getCandlesticks()[1].getLow());
		assertEquals(new BigDecimal("387.13"), chart.getCandlesticks()[1].getClose());
		assertEquals(new BigDecimal("1062.04"), chart.getCandlesticks()[1].getVolume());
	}

}
