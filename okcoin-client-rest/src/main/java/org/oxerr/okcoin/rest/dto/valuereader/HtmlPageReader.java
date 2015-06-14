package org.oxerr.okcoin.rest.dto.valuereader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.oxerr.okcoin.rest.service.web.OKCoinClient;
import org.oxerr.okcoin.rest.service.web.OKCoinClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class HtmlPageReader<T> implements ValueReader<T> {

	private static final String ENCODING = OKCoinClient.ENCODING;

	private final Logger log = LoggerFactory.getLogger(IndexHtmlPageReader.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T read(InputStream inputStream) throws IOException {
		HTMLDocument document;
		try {
			document = toDocument(inputStream);
		} catch (SAXException e) {
			throw new IOException(e);
		}
		return read(document);
	}

	protected abstract T read(HTMLDocument document) throws OKCoinClientException;

	private HTMLDocument toDocument(InputStream inputStream)
			throws IOException, SAXException {
		final InputSource inputSource;
		if (log.isTraceEnabled()) {
			String html = IOUtils.toString(inputStream, ENCODING);
			log.trace("Parsing HTML:\n{}", html);
			inputSource = new InputSource(new InputStreamReader(
					IOUtils.toInputStream(html, ENCODING), ENCODING));
		} else {
			inputSource = new InputSource(new InputStreamReader(inputStream,
					ENCODING));
		}
		DOMParser parser = new DOMParser();
		parser.parse(inputSource);
		HTMLDocument document = (HTMLDocument) parser.getDocument();
		return document;
	}

}
