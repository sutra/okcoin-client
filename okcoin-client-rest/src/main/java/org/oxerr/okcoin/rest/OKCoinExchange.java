package org.oxerr.okcoin.rest;

import java.util.Arrays;
import java.util.List;

import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.oxerr.okcoin.rest.service.polling.OKCoinAccountService;
import org.oxerr.okcoin.rest.service.polling.OKCoinMarketDataService;
import org.oxerr.okcoin.rest.service.polling.OKCoinTradeService;

import si.mazi.rescu.SynchronizedValueFactory;

/**
 * {@link Exchange} implementation for OKCoin.
 */
public class OKCoinExchange extends BaseExchange {

	/**
	 * The parameter name of the symbols that will focus on.
	 */
	public static final String SYMBOLS_PARAMETER = "symbols";

	/**
	 * Max count of retry in logging into via web form.
	 */
	public static final String LOGIN_MAX_RETRY_TIMES_PARAMETER = "login.max.retry.times";

	public static final String SOCKET_TIMEOUT_PARAMETER = "socketTimeout";
	public static final String CONNECT_TIMEOUT_PARAMETER = "connectTimeout";
	public static final String CONNECTION_REQUEST_TIMEOUT_PARAMETER = "connectionRequestTimeout";

	/**
	 * The parameter key of the trade password.
	 */
	public static final String TRADE_PASSWORD_PARAMETER = "trade_pwd";

	private static final List<CurrencyPair> SYMBOLS = Arrays.asList(
			CurrencyPair.BTC_CNY,
			CurrencyPair.LTC_CNY);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void initServices() {
		this.pollingMarketDataService = new OKCoinMarketDataService(this);

		if (exchangeSpecification.getApiKey() != null
			&& exchangeSpecification.getSecretKey() != null) {
			this.pollingAccountService = new OKCoinAccountService(this);
		}

		if ((exchangeSpecification.getApiKey() != null
				&& exchangeSpecification.getSecretKey() != null)
			|| (exchangeSpecification.getUserName() != null
				&& exchangeSpecification.getPassword() != null)) {
			this.pollingTradeService = new OKCoinTradeService(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExchangeSpecification getDefaultExchangeSpecification() {
		ExchangeSpecification exchangeSpecification = new ExchangeSpecification(
				this.getClass().getCanonicalName());
		exchangeSpecification.setSslUri("https://www.okcoin.cn");
		exchangeSpecification.setHost("www.okcoin.cn");
		exchangeSpecification.setExchangeName("OKCoin");
		exchangeSpecification
				.setExchangeDescription("OKCoin is a globally oriented crypto-currency trading platform.");
		exchangeSpecification.setExchangeSpecificParametersItem(
				SYMBOLS_PARAMETER, SYMBOLS);
		return exchangeSpecification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SynchronizedValueFactory<Long> getNonceFactory() {
		return null;
	}

}
