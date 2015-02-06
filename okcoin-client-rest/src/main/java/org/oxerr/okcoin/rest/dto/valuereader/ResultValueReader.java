package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;

import org.oxerr.okcoin.rest.dto.Result;

import com.fasterxml.jackson.databind.ObjectMapper;

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
