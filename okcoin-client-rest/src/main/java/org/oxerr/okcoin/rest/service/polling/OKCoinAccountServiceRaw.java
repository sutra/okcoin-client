package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;
import java.math.BigDecimal;

import org.knowm.xchange.Exchange;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.AccountRecords;
import org.oxerr.okcoin.rest.dto.UserInfo;
import org.oxerr.okcoin.rest.dto.Withdrawal;

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

	/**
	 * Returns the user deposits or withdraw records.
	 *
	 * @param symbol the symbol: btc_cny, ltc_cny, cny.
	 * @param type 0: deposits, 1: withdraw.
	 * @param currentPage the current page number, 1 based.
	 * @param pageLength the data entries number per page, maximum 50.
	 * @return the user deposits or withdraw records.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	public AccountRecords getAccountRecords(String symbol, int type,
			int currentPage, int pageLength) throws OKCoinException,
			IOException {
		return okCoin.getAccountRecords(apiKey, symbol, type, currentPage,
				pageLength, sign);
	}

}
