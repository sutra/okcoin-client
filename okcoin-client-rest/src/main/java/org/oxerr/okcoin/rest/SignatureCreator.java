package org.oxerr.okcoin.rest;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

public class SignatureCreator {

	private final String partner;

	private final String secretKey;

	public SignatureCreator(String partner, String secretKey) {
		this.partner = partner;
		this.secretKey = secretKey;
	}

	public String sign() {
		return sign(Collections.<String, Object>emptyMap());
	}

	public String sign(Map<String, Object> params) {
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("partner", partner));

		for (Map.Entry<String, Object> param : params.entrySet()) {
			nvps.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
		}

		Collections.sort(nvps, new Comparator<NameValuePair>() {

			@Override
			public int compare(NameValuePair o1, NameValuePair o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		return sign(nvps);
	}

	public String sign(List<NameValuePair> nvps) {
		final String parameters;
		try {
			parameters = new URIBuilder().addParameters(nvps).build().toString().substring(1);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		String sign = DigestUtils.md5Hex(parameters + secretKey).toUpperCase();
		return sign;
	}

}
