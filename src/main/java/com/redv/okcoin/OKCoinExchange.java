package com.redv.okcoin;

import java.util.Arrays;
import java.util.List;

import com.redv.okcoin.service.polling.OKCoinAccountService;
import com.redv.okcoin.service.polling.OKCoinMarketDataService;
import com.redv.okcoin.service.polling.OKCoinTradeService;
import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.currency.CurrencyPair;

public class OKCoinExchange extends BaseExchange {

	private static final List<CurrencyPair> SYMBOLS = Arrays.asList(
			CurrencyPair.BTC_CNY,
			CurrencyPair.LTC_CNY);

	@Override
	public void applySpecification(ExchangeSpecification exchangeSpecification) {
		super.applySpecification(exchangeSpecification);
		this.pollingMarketDataService = new OKCoinMarketDataService(exchangeSpecification);
		if (exchangeSpecification.getApiKey() != null) {
			this.pollingAccountService = new OKCoinAccountService(exchangeSpecification);
			this.pollingTradeService = new OKCoinTradeService(exchangeSpecification);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExchangeSpecification getDefaultExchangeSpecification() {
		ExchangeSpecification exchangeSpecification = new ExchangeSpecification(
				this.getClass().getCanonicalName());
		exchangeSpecification.setSslUri("https://www.okcoin.cn/api/");
		exchangeSpecification.setHost("www.okcoin.cn");
		exchangeSpecification.setExchangeName("OKCoin");
		exchangeSpecification
				.setExchangeDescription("OKCoin is a globally oriented crypto-currency trading platform.");
		exchangeSpecification.setExchangeSpecificParametersItem("symbols", SYMBOLS);
		return exchangeSpecification;
	}

}
