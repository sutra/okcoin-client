package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;
import java.math.BigDecimal;

import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.UserInfo;
import org.oxerr.okcoin.rest.dto.Withdrawal;

import com.xeiam.xchange.Exchange;

/**
 * Raw account service.
 */
public class OKCoinAccountServiceRaw extends OKCoinBaseTradePollingService {

	private static final String METHOD_GET_USER_INFO = "userinfo";

	protected OKCoinAccountServiceRaw(Exchange exchange) {
		super(exchange);
	}

	public UserInfo getUserInfo() throws OKCoinException, IOException {
		sleep(METHOD_GET_USER_INFO);
		UserInfo userInfo = okCoin.getUserInfo(apiKey, sign);
		updateLast(METHOD_GET_USER_INFO);
		return userInfo;
	}

	public Withdrawal withdraw(String symbol, BigDecimal chargeFee,
			String tradePassword, String withdrawAddress,
			BigDecimal withdrawAmount) throws OKCoinException, IOException {
		return okCoin.withdraw(apiKey, symbol, chargeFee, tradePassword,
				withdrawAddress, withdrawAmount, sign);
	}

	public Withdrawal cancelWithdraw(String symbol, long withdrawId)
			throws OKCoinException, IOException {
		return okCoin.cancelWithdraw(apiKey, symbol, withdrawId, sign);
	}

}
