package org.oxerr.okcoin.websocket;

import java.io.IOException;
import java.io.Writer;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.oxerr.okcoin.websocket.dto.Event;

public class OKCoinEncoder implements Encoder.TextStream<Event> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(EndpointConfig config) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void encode(Event message, Writer writer) throws EncodeException,
			IOException {
		JsonGenerator g = Json.createGenerator(writer);
		g.writeStartObject()
			.write("event", message.getEvent())
			.write("channel", message.getChannel());

		if (!message.getParameters().isEmpty()) {
			g.writeStartObject("parameters");
			message.getParameters().forEach((k, v) -> g.write(k, v));
			g.writeEnd();
		}

		g.writeEnd();
		g.flush();
	}

}
