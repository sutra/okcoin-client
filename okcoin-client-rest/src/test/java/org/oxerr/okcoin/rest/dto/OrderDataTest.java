package org.oxerr.okcoin.rest.dto;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class OrderDataTest {

	private static final ObjectMapper mapper = new ObjectMapper();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
	}

	@Test
	public void test() throws JsonProcessingException {
		OrderData[] orders = new OrderData[] {
			new OrderData(new BigDecimal("0.01"), new BigDecimal("1"), Type.BUY),
			new OrderData(new BigDecimal("0.02"), new BigDecimal("2"), Type.BUY),
		};

		assertEquals("[{\"price\":0.01,\"amount\":1,\"type\":\"buy\"},{\"price\":0.02,\"amount\":2,\"type\":\"buy\"}]",
			mapper.writeValueAsString(orders));
	}

}
