package com.redv.okcoin.valuereader;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.redv.okcoin.domain.Balance;

public class IndexHtmlPageReaderTest {

	private IndexHtmlPageReader reader;

	@Before
	public void setUp() throws Exception {
		reader = new IndexHtmlPageReader();
	}

	@Test
	public void testRead() throws IOException {
		try (InputStream inputStream = getClass().getResourceAsStream("index.html")) {
			Balance balance = reader.read(inputStream);
			assertEquals(BigDecimal.ZERO, balance.getCny());
			assertEquals(BigDecimal.ZERO, balance.getCnyFreez());
			assertEquals(BigDecimal.ZERO, balance.getBtc());
			assertEquals(BigDecimal.ZERO, balance.getBtcFreez());
			assertEquals(BigDecimal.ZERO, balance.getLtc());
			assertEquals(BigDecimal.ZERO, balance.getLtcFreez());
		}
	}

}
