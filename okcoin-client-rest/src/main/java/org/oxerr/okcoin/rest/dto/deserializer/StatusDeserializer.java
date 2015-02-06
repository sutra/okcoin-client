package org.oxerr.okcoin.rest.dto.deserializer;

import java.io.IOException;

import org.oxerr.okcoin.rest.dto.Status;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StatusDeserializer extends JsonDeserializer<Status> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		int code = jp.getIntValue();
		return Status.of(code);
	}

}
