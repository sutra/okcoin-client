package org.oxerr.okcoin.websocket;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.oxerr.okcoin.websocket.dto.Event;
import org.oxerr.okcoin.websocket.event.OKCoinDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OKCoin WebSocket client endpoint.
 */
@ClientEndpoint(encoders = OKCoinEncoder.class, decoders = OKCoinDecoder.class)
public final class OKCoinClientEndpoint {

	private final Logger log = LoggerFactory
			.getLogger(OKCoinClientEndpoint.class);

	private final Map<String, Set<OKCoinDataListener>> listeners;

	public OKCoinClientEndpoint() {
		this.listeners = new HashMap<String, Set<OKCoinDataListener>>();
	}

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		log.trace("open: {}, config: {}", session, config);
	}

	@OnMessage
	public void onMessage(Session session, OKCoinData[] data)
		throws IOException, EncodeException {

		if (log.isTraceEnabled()) {
			log.trace("data: {}", Arrays.toString(data));
		}

		Arrays.stream(data).forEach(e -> {
			this.listeners.get(e.getChannel()).forEach(listener -> {
				listener.onMessage(session, e.getData());
			});
		});
	}

	@OnClose
	public void onClose(Session session, CloseReason reason) {
		log.trace("close: {}, reason: {}", session, reason);
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		log.trace("error: {}", session, throwable);
	}

	public synchronized void addChannelListener(String channel, OKCoinDataListener listener) {
		Set<OKCoinDataListener> channelListeners = this.listeners.get(channel);
		if (channelListeners == null) {
			channelListeners = new HashSet<>();
			this.listeners.put(channel, channelListeners);
		}
		channelListeners.add(listener);
	}

	public void addChannel(Session session, String channel) {
		Event event = new Event("addChannel", channel);
		session.getAsyncRemote().sendObject(event);
	}

	public void removeChannel(Session session, String channel) {
		Event event = new Event("removeChannel", channel);
		session.getAsyncRemote().sendObject(event);
	}

}
