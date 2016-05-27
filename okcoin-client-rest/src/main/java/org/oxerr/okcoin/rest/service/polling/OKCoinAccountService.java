package org.oxerr.okcoin.rest.service.polling;

import static org.oxerr.okcoin.rest.OKCoinExchange.TRADE_PASSWORD_PARAMETER;

import java.io.IOException;
import java.math.BigDecimal;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.polling.account.PollingAccountService;
import org.oxerr.okcoin.rest.OKCoinAdapters;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.Withdrawal;

/**
 * {@link PollingAccountService} implementation.
 */
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
	public String withdrawFunds(Currency currency, BigDecimal amount,
			String address) throws OKCoinException, IOException {
		BigDecimal chargeFee = currency.equals("BTC") ? new BigDecimal("0.0001") : new BigDecimal("0.001");
		String tradePassword = (String) exchange.getExchangeSpecification().getParameter(TRADE_PASSWORD_PARAMETER);
		Withdrawal withdrawal = withdraw(currency.getCurrencyCode(), chargeFee, tradePassword, address, amount);
		return String.valueOf(withdrawal.getWithdrawId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String requestDepositAddress(Currency currency, String... args)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotAvailableFromExchangeException();
	}

}
