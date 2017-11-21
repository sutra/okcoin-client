package org.oxerr.okcoin.rest.dto.valuereader;

import static org.apache.http.HttpStatus.SC_OK;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

public interface ValueReader<T> {

	default T read(@Nonnull HttpResponse response) throws IOException {
		final StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == SC_OK) {
			HttpEntity entity = response.getEntity();
			return read(entity);
		} else {
			throw new IOException(statusLine.getReasonPhrase());
		}
	}

	default T read(@Nullable HttpEntity entity) throws IOException {
		if (entity == null) {
			return null;
		}

		final ContentType contentType = ContentType.get(entity);
		try (InputStream content = entity.getContent()) {
			return read(content, contentType);
		} finally {
			EntityUtils.consume(entity);
		}
	}

	default T read(@Nonnull InputStream inputStream,
		@Nullable ContentType contentType) throws IOException {
		String mimeType = null;
		Charset charset = null;
		if (contentType != null) {
			mimeType = contentType.getMimeType();
			charset = contentType.getCharset();
		}
		return read(inputStream, mimeType, charset);
	}

	default T read(@Nonnull InputStream inputStream,
		@Nullable String mimeType, @Nullable Charset charset) throws IOException {
		return read(inputStream);
	}

	default T read(@Nonnull InputStream inputStream) throws IOException {
		return null;
	}

}
