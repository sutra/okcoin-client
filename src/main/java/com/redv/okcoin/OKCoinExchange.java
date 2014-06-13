package com.redv.okcoin;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.ExchangeSpecification;

public class OKCoinExchange extends BaseExchange {

	@Override
	public void applySpecification(ExchangeSpecification exchangeSpecification) {
		super.applySpecification(exchangeSpecification);
		this.pollingMarketDataService = new OKCoinMarketDataService(exchangeSpecification);
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
		return exchangeSpecification;
	}

}
