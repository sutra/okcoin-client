package org.oxerr.okcoin.examples.rest;

import java.io.IOException;

import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.UserInfo;
import org.oxerr.okcoin.rest.service.polling.OKCoinAccountServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

/**
 * Demonstration of getting account balance.
 */
public class AccountServiceDemo {

	private final Logger log = LoggerFactory.getLogger(AccountServiceDemo.class);

	private final PollingAccountService accountService;
	private final OKCoinAccountServiceRaw rawAccountService;

	public AccountServiceDemo(Exchange exchange) {
		accountService = exchange.getPollingAccountService();
		rawAccountService = (OKCoinAccountServiceRaw) accountService;
	}

	public void demoUserInfo() throws IOException {
		UserInfo userInfo = rawAccountService.getUserInfo();
		log.info("user info: {}", userInfo);
		log.info("asset: {}", userInfo.getInfo().getFunds().getAsset());
		log.info("free: {}", userInfo.getInfo().getFunds().getFree());
		log.info("frozen: {}", userInfo.getInfo().getFunds().getFrozen());

		AccountInfo accountInfo = accountService.getAccountInfo();
		log.info("account info: {}", accountInfo);
	}

	public static void main(String[] args) throws IOException {
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		AccountServiceDemo domesticDemo = new AccountServiceDemo(domesticExchange);
		domesticDemo.demoUserInfo();
	}

}
