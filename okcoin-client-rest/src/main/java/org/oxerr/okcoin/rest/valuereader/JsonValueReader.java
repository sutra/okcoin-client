package org.oxerr.okcoin.rest.valuereader;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonValueReader<T> implements ValueReader<T> {

	private final ObjectMapper objectMapper;

	private final Class<T> valueType;

	public JsonValueReader(ObjectMapper objectMapper, Class<T> valueType) {
		this.objectMapper = objectMapper;
		this.valueType = valueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T read(InputStream inputStream) throws IOException {
		return objectMapper.readValue(inputStream, valueType);
	}

}