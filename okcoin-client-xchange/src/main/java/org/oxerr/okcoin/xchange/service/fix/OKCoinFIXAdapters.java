package org.oxerr.okcoin.xchange.service.fix;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.oxerr.okcoin.fix.fix44.AccountInfoResponse;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.trade.Wallet;

import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * Various adapters for converting from {@link Message} to XChange DTOs.
 */
public final class OKCoinFIXAdapters {

	private OKCoinFIXAdapters() {
	}

	public static String adaptSymbol(CurrencyPair currencyPair) {
		return String.format("%s/%s", currencyPair.baseSymbol, currencyPair.counterSymbol);
	}

	public static AccountInfo adaptAccountInfo(AccountInfoResponse message)
			throws FieldNotFound {
		String[] currencies = message.getCurrency().getValue().split("/");
		String[] balances = message.getBalance().getValue().split("/");

		int walletCount = currencies.length;
		Map<String, Wallet> wallets = new HashMap<>(walletCount);

		for (int i = 0; i < walletCount; i++) {
			Wallet wallet = new Wallet(currencies[i],
					new BigDecimal(balances[i]));
			wallets.put(wallet.getCurrency(), wallet);
		}

		return new AccountInfo(null, wallets);
	}

}
