package org.oxerr.okcoin.websocket.dto;

import java.util.Collections;
import java.util.Map;

public class Event extends BaseObject {

	private static final long serialVersionUID = 2015030501L;

	private final String event;
	private final String channel;
	private final Map<String, String> parameters;

	public Event(String event, String channel) {
		this.event = event;
		this.channel = channel;
		this.parameters = Collections.emptyMap();
	}

	public Event(String event, String channel, Map<String, String> parameters) {
		this.event = event;
		this.channel = channel;
		this.parameters = parameters;
	}

	public String getEvent() {
		return event;
	}

	public String getChannel() {
		return channel;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

}
