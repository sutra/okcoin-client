package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.oxerr.okcoin.rest.dto.Depth;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class DepthTest extends UnmarshalTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {
		Depth depth = readValue("depth.json", Depth.class);

		assertEquals(5, depth.getAsks().length);

		assertEquals(new BigDecimal("792"), depth.getAsks()[0][0]);
		assertEquals(new BigDecimal("5"), depth.getAsks()[0][1]);

		assertEquals(new BigDecimal("789.68"), depth.getAsks()[1][0]);
		assertEquals(new BigDecimal("0.018"), depth.getAsks()[1][1]);

		assertEquals(new BigDecimal("788.99"), depth.getAsks()[2][0]);
		assertEquals(new BigDecimal("0.042"), depth.getAsks()[2][1]);

		assertEquals(new BigDecimal("788.43"), depth.getAsks()[3][0]);
		assertEquals(new BigDecimal("0.036"), depth.getAsks()[3][1]);

		assertEquals(new BigDecimal("787.27"), depth.getAsks()[4][0]);
		assertEquals(new BigDecimal("0.02"), depth.getAsks()[4][1]);

		assertEquals(7, depth.getBids().length);

		assertEquals(new BigDecimal("787.1"), depth.getBids()[0][0]);
		assertEquals(new BigDecimal("0.35"), depth.getBids()[0][1]);

		assertEquals(new BigDecimal("787"), depth.getBids()[1][0]);
		assertEquals(new BigDecimal("12.071"), depth.getBids()[1][1]);

		assertEquals(new BigDecimal("786.5"), depth.getBids()[2][0]);
		assertEquals(new BigDecimal("0.014"), depth.getBids()[2][1]);

		assertEquals(new BigDecimal("786.2"), depth.getBids()[3][0]);
		assertEquals(new BigDecimal("0.38"), depth.getBids()[3][1]);

		assertEquals(new BigDecimal("786"), depth.getBids()[4][0]);
		assertEquals(new BigDecimal("3.217"), depth.getBids()[4][1]);

		assertEquals(new BigDecimal("785.3"), depth.getBids()[5][0]);
		assertEquals(new BigDecimal("5.322"), depth.getBids()[5][1]);

		assertEquals(new BigDecimal("785.04"), depth.getBids()[6][0]);
		assertEquals(new BigDecimal("5.04"), depth.getBids()[6][1]);
	}

}
