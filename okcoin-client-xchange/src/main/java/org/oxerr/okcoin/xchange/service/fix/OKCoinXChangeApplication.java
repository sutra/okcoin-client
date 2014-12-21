package org.oxerr.okcoin.xchange.service.fix;

import org.oxerr.okcoin.fix.OKCoinApplication;
import org.oxerr.okcoin.fix.fix44.AccountInfoResponse;

import quickfix.Application;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

import com.xeiam.xchange.dto.account.AccountInfo;

/**
 * {@link Application} implementation uses XChange DTOs as callback parameters.
 */
public class OKCoinXChangeApplication extends OKCoinApplication {

	public OKCoinXChangeApplication(String partner, String secretKey) {
		super(partner, secretKey);
	}

	@Override
	public void onMessage(AccountInfoResponse message, SessionID sessionId)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		onAccountInfo(OKCoinFIXAdapters.adaptAccountInfo(message), sessionId);
	}

	public void onAccountInfo(AccountInfo accountInfo, SessionID sessionId) {
	}

}
