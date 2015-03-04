package org.oxerr.okcoin.examples.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.oxerr.okcoin.websocket.OKCoinClientEndpoint;
import org.oxerr.okcoin.websocket.event.OKCoinDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	public static void main(String[] args) throws DeploymentException,
			IOException, URISyntaxException, InterruptedException {
		OKCoinClientEndpoint endpoint = new OKCoinClientEndpoint();

		OKCoinDataListener printLogListener = new OKCoinDataListener() {

			@Override
			public void onMessage(Session session, Object data) {
				log.info("data: {}", data);
			}
		};

		endpoint.addChannelListener("ok_btccny_ticker", printLogListener);
		endpoint.addChannelListener("ok_btccny_depth", printLogListener);
		endpoint.addChannelListener("ok_btccny_depth60", printLogListener);
		endpoint.addChannelListener("ok_btccny_trades_v1", printLogListener);
		endpoint.addChannelListener("ok_btccny_kline_1min", printLogListener);

		WebSocketContainer container = ContainerProvider
				.getWebSocketContainer();
		Session session = container.connectToServer(endpoint, new URI(
				"wss://real.okcoin.cn:10440/websocket/okcoinapi"));

		endpoint.addChannel(session, "ok_btccny_ticker");
		endpoint.addChannel(session, "ok_btccny_depth");
		endpoint.addChannel(session, "ok_btccny_depth60");
		endpoint.addChannel(session, "ok_btccny_trades_v1");
		endpoint.addChannel(session, "ok_btccny_kline_1min");

		TimeUnit.MINUTES.sleep(5);
	}

}
