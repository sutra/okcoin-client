package org.oxerr.okcoin.xchange.service.fix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.oxerr.okcoin.fix.fix44.AccountInfoResponse;

import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * Various adapters for converting from {@link Message} to XChange DTOs.
 */
public final class OKCoinFIXAdapters {

	private OKCoinFIXAdapters() {
	}

	public static String adaptSymbol(CurrencyPair currencyPair) {
		return String.format("%s/%s", currencyPair.base.getCurrencyCode(), currencyPair.counter.getCurrencyCode());
	}

	public static CurrencyPair adaptCurrencyPair(String symbol) {
		String[] symbols = symbol.split("/");
		CurrencyPair currencyPair = new CurrencyPair(symbols[0], symbols[1]);
		return currencyPair;
	}

	public static AccountInfo adaptAccountInfo(AccountInfoResponse message)
			throws FieldNotFound {
		final String[] currencies = message.getCurrency().getValue().split("/");
		final String[] balances = message.getBalance().getValue().split("/");

		final int walletCount = currencies.length;
		final List<Balance> balanceList = new ArrayList<>(walletCount);

		for (int i = 0; i < walletCount; i++) {
			final String currency = currencies[i];
			final BigDecimal available = new BigDecimal(balances[i]);
			final Balance balance = new Balance(Currency.getInstance(currency), available, available);
			balanceList.add(balance);
		}

		final Wallet wallet = new Wallet(balanceList);
		return new AccountInfo(wallet);
	}

}
