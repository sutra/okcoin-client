package org.oxerr.okcoin.examples.rest;

import static org.oxerr.okcoin.rest.OKCoinExchange.TRADE_PASSWORD_PARAMETER;

import java.io.IOException;
import java.math.BigDecimal;

import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.Withdrawal;
import org.oxerr.okcoin.rest.service.polling.OKCoinAccountServiceRaw;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

/**
 * Demonstration of withdrawing and canceling withdrawal.
 */
public class WithdrawDemo {

	private final PollingAccountService accountService;
	private final OKCoinAccountServiceRaw rawAccountService;
	private final String tradePwd;

	public WithdrawDemo(Exchange exchange, String tradePwd) {
		this.tradePwd = tradePwd;
		accountService = exchange.getPollingAccountService();
		rawAccountService = (OKCoinAccountServiceRaw) accountService;
	}

	public void demoWithdraw() throws IOException {
		String symbol = "BTC";

		// request to withdraw
		String withdrawId = accountService.withdrawFunds(Currency.getInstance(symbol), new BigDecimal("1"), "yourAddress");
		// cancel the above withdrawal
		rawAccountService.cancelWithdraw(symbol, Long.valueOf(withdrawId));

		// request to withdraw via raw service
		Withdrawal withdrawal = rawAccountService.withdraw(symbol, new BigDecimal("0.0001"), tradePwd, "yourAddress", new BigDecimal("1"));
		// cancel the above withdrawl
		rawAccountService.cancelWithdraw(symbol, withdrawal.getWithdrawId());
	}

	public static void main(String[] args) throws IOException {
		String apiKey = args[0], secretKey = args[1], tradePwd = args[2];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinException.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);
		spec.setExchangeSpecificParametersItem(TRADE_PASSWORD_PARAMETER, tradePwd);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		WithdrawDemo withdrawDemo = new WithdrawDemo(domesticExchange, tradePwd);
		withdrawDemo.demoWithdraw();
	}

}
