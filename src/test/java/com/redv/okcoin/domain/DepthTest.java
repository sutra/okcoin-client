package com.redv.okcoin.domain;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DepthTest {

	@Test
	public void test() throws JsonParseException, JsonMappingException,
			IOException {
		ObjectMapper mapper = new ObjectMapper();
		Depth depth = mapper.readValue(getClass().getResource("depth.json"),
				Depth.class);

		assertEquals(5, depth.getAsks().size());

		assertEquals(new BigDecimal("787.27"), depth.getAsks().get(0).getRate());
		assertEquals(new BigDecimal("0.02"), depth.getAsks().get(0).getAmount());
		assertEquals(Type.SELL, depth.getAsks().get(0).getType());

		assertEquals(new BigDecimal("788.43"), depth.getAsks().get(1).getRate());
		assertEquals(new BigDecimal("0.036"), depth.getAsks().get(1).getAmount());
		assertEquals(Type.SELL, depth.getAsks().get(1).getType());

		assertEquals(new BigDecimal("788.99"), depth.getAsks().get(2).getRate());
		assertEquals(new BigDecimal("0.042"), depth.getAsks().get(2).getAmount());
		assertEquals(Type.SELL, depth.getAsks().get(2).getType());

		assertEquals(new BigDecimal("789.68"), depth.getAsks().get(3).getRate());
		assertEquals(new BigDecimal("0.018"), depth.getAsks().get(3).getAmount());
		assertEquals(Type.SELL, depth.getAsks().get(3).getType());

		assertEquals(new BigDecimal("792"), depth.getAsks().get(4).getRate());
		assertEquals(new BigDecimal("5"), depth.getAsks().get(4).getAmount());
		assertEquals(Type.SELL, depth.getAsks().get(4).getType());

		assertEquals(7, depth.getBids().size());

		assertEquals(new BigDecimal("787.1"), depth.getBids().get(0).getRate());
		assertEquals(new BigDecimal("0.35"), depth.getBids().get(0).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(0).getType());

		assertEquals(new BigDecimal("787"), depth.getBids().get(1).getRate());
		assertEquals(new BigDecimal("12.071"), depth.getBids().get(1).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(1).getType());

		assertEquals(new BigDecimal("786.5"), depth.getBids().get(2).getRate());
		assertEquals(new BigDecimal("0.014"), depth.getBids().get(2).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(2).getType());

		assertEquals(new BigDecimal("786.2"), depth.getBids().get(3).getRate());
		assertEquals(new BigDecimal("0.38"), depth.getBids().get(3).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(3).getType());

		assertEquals(new BigDecimal("786"), depth.getBids().get(4).getRate());
		assertEquals(new BigDecimal("3.217"), depth.getBids().get(4).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(4).getType());

		assertEquals(new BigDecimal("785.3"), depth.getBids().get(5).getRate());
		assertEquals(new BigDecimal("5.322"), depth.getBids().get(5).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(5).getType());

		assertEquals(new BigDecimal("785.04"), depth.getBids().get(6).getRate());
		assertEquals(new BigDecimal("5.04"), depth.getBids().get(6).getAmount());
		assertEquals(Type.BUY, depth.getBids().get(6).getType());
	}

}
