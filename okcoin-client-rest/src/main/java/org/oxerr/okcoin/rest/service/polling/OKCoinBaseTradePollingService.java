package org.oxerr.okcoin.rest.service.polling;

import static org.oxerr.okcoin.rest.OKCoinExchange.CONNECTION_REQUEST_TIMEOUT_PARAMETER;
import static org.oxerr.okcoin.rest.OKCoinExchange.CONNECT_TIMEOUT_PARAMETER;
import static org.oxerr.okcoin.rest.OKCoinExchange.SOCKET_TIMEOUT_PARAMETER;
import static org.oxerr.okcoin.rest.OKCoinExchange.TRADE_PASSWORD_PARAMETER;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.oxerr.okcoin.rest.OKCoin;
import org.oxerr.okcoin.rest.service.OKCoinDigest;
import org.oxerr.okcoin.rest.service.web.OKCoinClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import si.mazi.rescu.RestProxyFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;

/**
 * Base trade service.
 */
public class OKCoinBaseTradePollingService extends OKCoinBasePollingService {

	private static final long INTERVAL = 2_000;

	private final Logger log = LoggerFactory.getLogger(OKCoinBaseTradePollingService.class);

	protected final OKCoin okCoin;

	protected final OKCoinDigest sign;

	protected final String apiKey;

	private Map<String, Long> lasts = new HashMap<String, Long>();

	protected final OKCoinClient okCoinClient;

	protected OKCoinBaseTradePollingService(Exchange exchange) {
		super(exchange);
		ExchangeSpecification spec = exchange.getExchangeSpecification();

		if (spec.getApiKey() != null && spec.getSecretKey() != null) {
			okCoin = RestProxyFactory.createProxy(OKCoin.class, spec.getSslUri());
			this.apiKey = spec.getApiKey();
			sign = new OKCoinDigest(spec.getSecretKey());
		} else {
			okCoin = null;
			this.apiKey = null;
			sign = null;
		}

		if (spec.getUserName() != null && spec.getPassword() != null) {
			String tradePassword = (String) spec.getExchangeSpecificParametersItem(TRADE_PASSWORD_PARAMETER);
			Number socketTimeout = (Number) spec.getExchangeSpecificParametersItem(SOCKET_TIMEOUT_PARAMETER);
			Number connectTimeout = (Number) spec.getExchangeSpecificParametersItem(CONNECT_TIMEOUT_PARAMETER);
			Number connectionRequestTimeout = (Number) spec.getExchangeSpecificParametersItem(CONNECTION_REQUEST_TIMEOUT_PARAMETER);

			okCoinClient = new OKCoinClient(
				spec.getUserName(), spec.getPassword(), tradePassword,
				socketTimeout == null ? 0 : socketTimeout.intValue(),
				connectTimeout == null ? 0 : connectTimeout.intValue(),
				connectionRequestTimeout == null ? 0 : connectionRequestTimeout.intValue());
			try {
				okCoinClient.login();
			} catch (IOException e) {
				log.warn(e.getMessage(), e);
			}
		} else {
			okCoinClient = null;
		}
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
