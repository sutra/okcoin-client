package org.oxerr.okcoin.websocket;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.websocket.EncodeException;

import org.junit.Test;
import org.oxerr.okcoin.websocket.dto.Event;

public class OKCoinEncoderTest {

	private OKCoinEncoder encoder = new OKCoinEncoder();

	@Test
	public void testEncode() throws EncodeException, IOException {
		StringWriter writer = new StringWriter();
		Event event = new Event("addChannel", "ok_btccny_ticker");
		encoder.encode(event, writer);
		writer.close();
		assertEquals("{\"event\":\"addChannel\",\"channel\":\"ok_btccny_ticker\"}",
			writer.toString());
	}

	@Test
	public void testEncodeParameters() throws EncodeException, IOException {
		StringWriter writer = new StringWriter();

		Map<String, String> parameters = new LinkedHashMap<>();
		parameters.put("api_key", "value1");
		parameters.put("sign", "value2");

		Event event = new Event("addChannel", "ok_btccny_ticker", parameters);
		encoder.encode(event, writer);
		writer.close();
		assertEquals("{\"event\":\"addChannel\",\"channel\":\"ok_btccny_ticker\",\"parameters\":{\"api_key\":\"value1\",\"sign\":\"value2\"}}",
				writer.toString());
	}

}
