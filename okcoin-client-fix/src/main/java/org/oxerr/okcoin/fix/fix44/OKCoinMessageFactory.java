package org.oxerr.okcoin.fix.fix44;

import quickfix.Message;
import quickfix.fix44.MessageFactory;

/**
 * {@link MessageFactory} that added OKCoin customized message support.
 */
public class OKCoinMessageFactory extends MessageFactory {

	@Override
	public Message create(String beginString, String msgType) {
		if (AccountInfoRequest.MSGTYPE.equals(msgType)) {
			return new AccountInfoRequest();
		}

		if (AccountInfoResponse.MSGTYPE.equals(msgType)) {
			return new AccountInfoResponse();
		}

		return super.create(beginString, msgType);
	}

}
