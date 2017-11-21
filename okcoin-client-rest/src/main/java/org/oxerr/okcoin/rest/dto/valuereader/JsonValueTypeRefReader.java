package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonValueTypeRefReader<T> implements ValueReader<T> {

	private final Logger log = Logger.getLogger(getClass().getName());

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
	public T read(InputStream inputStream,
		@Nullable String mimeType, @Nullable Charset charset) throws IOException {
		if (log.isLoggable(Level.FINEST)) {
			inputStream = IOUtils.buffer(inputStream);
			inputStream.mark(Integer.MAX_VALUE);
			String s = IOUtils.toString(inputStream, charset != null ? charset : StandardCharsets.UTF_8);
			log.log(Level.FINEST, "JSON: {0}", s);
			inputStream.reset();
		}
		try {
			return objectMapper.readValue(inputStream, valueTypeRef);
		} catch (JsonMappingException jme) {
			if (jme.getCause() instanceof RuntimeException) {
				throw (RuntimeException) jme.getCause();
			} else {
				throw jme;
			}
		}
	}

}
