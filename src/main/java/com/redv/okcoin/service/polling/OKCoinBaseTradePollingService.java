package com.redv.okcoin.service.polling;

import si.mazi.rescu.RestProxyFactory;

import com.redv.okcoin.Messages;
import com.redv.okcoin.OKCoin;
import com.redv.okcoin.OKCoinException;
import com.redv.okcoin.SignatureCreator;
import com.redv.okcoin.domain.ErrorResult;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinBaseTradePollingService extends OKCoinBasePollingService {

	protected final OKCoin okCoin;

	protected final SignatureCreator signatureCreator;

	protected final long partner;

	/**
	 * @param exchangeSpecification
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

}
