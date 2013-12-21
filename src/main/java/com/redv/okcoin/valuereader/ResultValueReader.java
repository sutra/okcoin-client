package com.redv.okcoin.valuereader;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redv.okcoin.domain.Result;

public class ResultValueReader implements ValueReader<Result> {

	private static final ResultValueReader INSTANCE = new ResultValueReader();

	private JsonValueReader<Result> jsonValueReader;

	public static ResultValueReader getInstance() {
		return INSTANCE;
	}

	public ResultValueReader() {
		ObjectMapper objectMapper = new ObjectMapper();
		jsonValueReader = new JsonValueReader<>(objectMapper, Result.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Result read(InputStream inputStream) throws IOException {
		return jsonValueReader.read(inputStream);
	}

}
