package org.oxerr.okcoin.websocket;

import java.io.IOException;
import java.io.Writer;

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
		writer.write(String.format("{'event':'%s','channel':'%s'}",
				message.getEvent(), message.getChannel()));
	}

}
