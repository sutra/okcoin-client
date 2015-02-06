package org.oxerr.okcoin.rest.service.polling;

import java.util.HashMap;
import java.util.Map;

import org.oxerr.okcoin.rest.OKCoin;
import org.oxerr.okcoin.rest.service.OKCoinDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinBaseTradePollingService extends OKCoinBasePollingService {

	private static final long INTERVAL = 2_000;

	private final Logger log = LoggerFactory.getLogger(OKCoinBaseTradePollingService.class);

	protected final OKCoin okCoin;

	protected final OKCoinDigest sign;

	protected final String apiKey;

	private Map<String, Long> lasts = new HashMap<String, Long>();

	protected OKCoinBaseTradePollingService(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();
		okCoin = RestProxyFactory.createProxy(OKCoin.class, spec.getSslUri());
		this.apiKey = spec.getApiKey();
		sign = new OKCoinDigest(spec.getSecretKey());
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
			log.trace("Sleeping for {} ms.", INTERVAL);
			Thread.sleep(INTERVAL);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
