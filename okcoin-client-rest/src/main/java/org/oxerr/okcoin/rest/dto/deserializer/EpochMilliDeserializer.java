package org.oxerr.okcoin.rest.dto.deserializer;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class EpochMilliDeserializer extends JsonDeserializer<Instant> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Instant deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		return Instant.ofEpochMilli(jp.getLongValue());
	}

}
