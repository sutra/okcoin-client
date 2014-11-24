package com.redv.okcoin.service.polling;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.mazi.rescu.RestProxyFactory;

import com.redv.okcoin.Messages;
import com.redv.okcoin.OKCoin;
import com.redv.okcoin.OKCoinException;
import com.redv.okcoin.SignatureCreator;
import com.redv.okcoin.domain.ErrorResult;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinBaseTradePollingService extends OKCoinBasePollingService {

	private static final long INTERVAL = 2_000;

	private final Logger log = LoggerFactory.getLogger(OKCoinBaseTradePollingService.class);

	protected final OKCoin okCoin;

	protected final SignatureCreator signatureCreator;

	protected final long partner;

	private Map<String, Long> lasts = new HashMap<String, Long>();

	/**
	 * @param exchangeSpecification the exchange specification.
	 */
	protected OKCoinBaseTradePollingService(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
		okCoin = RestProxyFactory.createProxy(OKCoin.class,
				exchangeSpecification.getSslUri());
		final String apiKey = exchangeSpecification.getApiKey();
		signatureCreator = new SignatureCreator(
				apiKey,
				exchangeSpecification.getSecretKey());
		partner = Long.parseLong(apiKey);
	}

	protected <T extends ErrorResult> T returnOrThrow(T t) {
		if (t.isResult()) {
			return t;
		} else {
			throw createException(t.getErrorCode());
		}
	}

	private OKCoinException createException(int errorCode) {
		String message = Messages.getString(String.valueOf(errorCode));
		return new OKCoinException(errorCode, message);
	}

	private long getLast(String method) {
		Long last = lasts.get(method);
		if (last == null) {
			return 0;
		} else {
			return last.longValue();
		}
	}

	protected void updateLast(String method) {
		lasts.put(method, System.currentTimeMillis());
	}

	protected void sleep(String method) {
		if (System.currentTimeMillis() - getLast(method) < INTERVAL) {
			sleep();
		}
	}

	private void sleep() {
		try {
			log.debug("Sleeping for {} ms.", INTERVAL);
			Thread.sleep(INTERVAL);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
