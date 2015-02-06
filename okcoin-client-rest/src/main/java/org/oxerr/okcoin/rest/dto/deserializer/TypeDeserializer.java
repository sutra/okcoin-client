package org.oxerr.okcoin.rest.dto.deserializer;

import java.io.IOException;

import org.oxerr.okcoin.rest.dto.Type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TypeDeserializer extends JsonDeserializer<Type> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String code = jp.getText();
		return Type.of(code);
	}

}
