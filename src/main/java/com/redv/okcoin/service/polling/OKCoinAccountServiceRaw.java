package com.redv.okcoin.service.polling;

import com.redv.okcoin.domain.UserInfo;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinAccountServiceRaw extends OKCoinBaseTradePollingService {

	/**
	 * @param exchangeSpecification
	 */
	protected OKCoinAccountServiceRaw(
			ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	public UserInfo getUserInfo() {
		UserInfo userInfo = okCoin.getUserInfo(partner, signatureCreator.sign());
		return returnOrThrow(userInfo);
	}

}
