package org.oxerr.okcoin.examples.rest;

import java.io.IOException;
import java.util.Arrays;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.service.polling.account.PollingAccountService;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.OKCoinExchange;
import org.oxerr.okcoin.rest.dto.AccountRecords;
import org.oxerr.okcoin.rest.dto.UserInfo;
import org.oxerr.okcoin.rest.service.polling.OKCoinAccountServiceRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public void demoGetAccountRecords() throws OKCoinException, IOException {
		AccountRecords accountRecords = rawAccountService.getAccountRecords(
				"btc_cny", 0, 1, 50);
		Arrays.stream(accountRecords.getRecords()).forEach(
				record -> log.info("{}", record));

		accountRecords = rawAccountService.getAccountRecords(
				"ltc_cny", 0, 1, 50);
		Arrays.stream(accountRecords.getRecords()).forEach(
				record -> log.info("{}", record));

		accountRecords = rawAccountService.getAccountRecords(
				"cny", 0, 1, 50);
		Arrays.stream(accountRecords.getRecords()).forEach(
				record -> log.info("{}", record));
	}

	public static void main(String[] args) throws IOException {
		String apiKey = args[0], secretKey = args[1];

		ExchangeSpecification spec = new ExchangeSpecification(OKCoinExchange.class);
		spec.setApiKey(apiKey);
		spec.setSecretKey(secretKey);

		Exchange domesticExchange = ExchangeFactory.INSTANCE.createExchange(spec);
		AccountServiceDemo domesticDemo = new AccountServiceDemo(domesticExchange);
		domesticDemo.demoUserInfo();
		domesticDemo.demoGetAccountRecords();
	}

}
