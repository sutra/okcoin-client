package org.oxerr.okcoin.websocket.event;

import java.util.EventListener;

import javax.websocket.Session;

public interface OKCoinDataListener extends EventListener {

	void onMessage(Session session, Object data);

}
