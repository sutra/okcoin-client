package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import org.apache.http.entity.ContentType;
import org.oxerr.okcoin.rest.dto.Result;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;

import com.fasterxml.jackson.databind.JsonMappingException;
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
	public Result read(InputStream inputStream,
		@Nullable ContentType contentType) throws IOException {
		try {
			return jsonValueReader.read(inputStream, contentType);
		} catch (JsonMappingException e) {
			throw new LoginRequiredException(e.getMessage(), e);
		}
	}

}
