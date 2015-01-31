package org.oxerr.okcoin.rest.domain;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UnmarshalTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	protected <T> T readValue(String resource, Class<T> valueType)
			throws IOException {
		URL url = getClass().getResource(resource);
		return objectMapper.readValue(url, valueType);
	}

	protected <T> T readValue(String resource, TypeReference<T> valueTypeRef)
			throws IOException {
	URL url = getClass().getResource(resource);
		return objectMapper.readValue(url, valueTypeRef);
	}

}
