package org.oxerr.okcoin.examples.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.oxerr.okcoin.rest.OKCoinDigest;
import org.oxerr.okcoin.websocket.OKCoinClientEndpoint;
import org.oxerr.okcoin.websocket.event.OKCoinDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

	private static final Logger log = LoggerFactory.getLogger(Client.class);

	public static void main(String[] args) throws DeploymentException,
			IOException, URISyntaxException, InterruptedException {
		String apiKey = args.length > 0 ? args[0] : null, secretKey = args.length > 1 ? args[1] : null;

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

		endpoint.addChannelListener("ok_cny_realtrades", printLogListener);
		endpoint.addChannelListener("ok_spotcny_trade", printLogListener);
		endpoint.addChannelListener("ok_spotcny_cancel_order", printLogListener);
		endpoint.addChannelListener("ok_spotcny_userinfo", printLogListener);
		endpoint.addChannelListener("ok_spotcny_order_info", printLogListener);

		WebSocketContainer container = ContainerProvider
				.getWebSocketContainer();
		Session session = container.connectToServer(endpoint, new URI(
				"wss://real.okcoin.cn:10440/websocket/okcoinapi"));

		endpoint.addChannel(session, "ok_btccny_ticker");
		endpoint.addChannel(session, "ok_btccny_depth");
		endpoint.addChannel(session, "ok_btccny_depth60");
		endpoint.addChannel(session, "ok_btccny_trades_v1");
		endpoint.addChannel(session, "ok_btccny_kline_1min");

		if (apiKey != null && secretKey != null) {
			Map<String, String> parameters = new HashMap<>();
			parameters.put("api_key", apiKey);
			parameters.put("sign", new OKCoinDigest(secretKey).digestParams(parameters));
			endpoint.addChannel(session, "ok_cny_realtrades", parameters);

			parameters = new HashMap<>();
			parameters.put("api_key", apiKey);
			parameters.put("symbol", "btc_cny");
			parameters.put("type", "buy");
			parameters.put("price", "50");
			parameters.put("amount", "0.02");
			parameters.put("sign", new OKCoinDigest(secretKey).digestParams(parameters));
			// endpoint.addChannel(session, "ok_spotcny_trade", parameters);

			parameters = new HashMap<>();
			parameters.put("api_key", apiKey);
			parameters.put("symbol", "btc_cny");
			parameters.put("order_id", "1");
			parameters.put("sign", new OKCoinDigest(secretKey).digestParams(parameters));
			// endpoint.addChannel(session, "ok_spotcny_cancel_order", parameters);;

			parameters = new HashMap<>();
			parameters.put("api_key", apiKey);
			parameters.put("sign", new OKCoinDigest(secretKey).digestParams(parameters));
			endpoint.addChannel(session, "ok_spotcny_userinfo", parameters);;

			parameters = new HashMap<>();
			parameters.put("api_key", apiKey);
			parameters.put("symbol", "btc_cny");
			parameters.put("order_id", "1");
			parameters.put("sign", new OKCoinDigest(secretKey).digestParams(parameters));
			// endpoint.addChannel(session, "ok_spotcny_order_info", parameters);;
		}

		TimeUnit.MINUTES.sleep(5);
	}

}
