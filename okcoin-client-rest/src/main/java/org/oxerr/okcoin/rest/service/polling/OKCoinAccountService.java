package org.oxerr.okcoin.rest.service.polling;

import static org.oxerr.okcoin.rest.OKCoinExchange.TRADE_PASSWORD_PARAMETER;

import java.io.IOException;
import java.math.BigDecimal;

import org.oxerr.okcoin.rest.OKCoinAdapters;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.Withdrawal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.exceptions.NotYetImplementedForExchangeException;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

public class OKCoinAccountService extends OKCoinAccountServiceRaw implements
		PollingAccountService {

	private final Exchange exchange;

	public OKCoinAccountService(Exchange exchange) {
		super(exchange);
		this.exchange = exchange;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountInfo getAccountInfo() throws OKCoinException,
			OKCoinException, IOException {
		return OKCoinAdapters.adaptAccountInfo(getUserInfo());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String withdrawFunds(String currency, BigDecimal amount,
			String address) throws OKCoinException, IOException {
		BigDecimal chargeFee = currency.equals("BTC") ? new BigDecimal("0.0001") : new BigDecimal("0.001");
		String tradePassword = (String) exchange.getExchangeSpecification().getParameter(TRADE_PASSWORD_PARAMETER);
		Withdrawal withdrawal = withdraw(currency, chargeFee, tradePassword, address, amount);
		return String.valueOf(withdrawal.getWithdrawId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String requestDepositAddress(String currency, String... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotAvailableFromExchangeException();
	}

}
