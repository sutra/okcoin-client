package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;

import org.oxerr.okcoin.rest.domain.UserInfo;

import com.xeiam.xchange.Exchange;

public class OKCoinAccountServiceRaw extends OKCoinBaseTradePollingService {

	private static final String METHOD_GET_USER_INFO = "userinfo";

	/**
	 * @param exchangeSpecification the exchange specification.
	 */
	protected OKCoinAccountServiceRaw(Exchange exchange) {
		super(exchange);
	}

	public UserInfo getUserInfo() throws IOException {
		sleep(METHOD_GET_USER_INFO);
		UserInfo userInfo = okCoin.getUserInfo(partner, signatureCreator.sign());
		updateLast(METHOD_GET_USER_INFO);
		return returnOrThrow(userInfo);
	}

}
