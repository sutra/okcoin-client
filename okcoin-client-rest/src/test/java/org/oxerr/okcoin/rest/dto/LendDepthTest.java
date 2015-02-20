package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

public class LendDepthTest extends UnmarshalTest {

	@Test
	public void test() throws IOException {
		LendDepth lendDepth = readValue("lend_depth.json", LendDepth.class);
		Lend[] lendArray = lendDepth.getLendDepth();
		Lend lend = lendArray[0];
		assertEquals(new BigDecimal("26272982.5427"), lend.getAmount());
		assertEquals("15 - 90", lend.getDays());
		assertEquals(14, lend.getNum());
		assertEquals(new BigDecimal("0.001"), lend.getRate());

		lend = lendArray[1];
		assertEquals(new BigDecimal("47403.1424"), lend.getAmount());
		assertEquals("15", lend.getDays());
		assertEquals(1, lend.getNum());
		assertEquals(new BigDecimal("0.0013"), lend.getRate());
	}

}
