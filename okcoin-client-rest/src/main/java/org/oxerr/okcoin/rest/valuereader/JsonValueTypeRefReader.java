package org.oxerr.okcoin.rest.valuereader;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonValueTypeRefReader<T> implements ValueReader<T> {

	private final ObjectMapper objectMapper;

	private final TypeReference<T> valueTypeRef;

	public JsonValueTypeRefReader(ObjectMapper objectMapper,
			TypeReference<T> valueTypeRef) {
		this.objectMapper = objectMapper;
		this.valueTypeRef = valueTypeRef;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T read(InputStream inputStream) throws IOException {
		return objectMapper.readValue(inputStream, valueTypeRef);
	}

}