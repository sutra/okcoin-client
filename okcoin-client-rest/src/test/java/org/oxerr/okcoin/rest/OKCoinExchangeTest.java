package org.oxerr.okcoin.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.dto.meta.RateLimit;

public class OKCoinExchangeTest {

	@Test
	public void test() {
		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		Exchange exchange = ExchangeFactory.INSTANCE.createExchange(spec);
		RateLimit[] rateLimits = exchange.getExchangeMetaData().getPrivateRateLimits();
		assertEquals(1, rateLimits.length);
	}

}
