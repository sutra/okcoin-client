package org.oxerr.okcoin.websocket.dto;

public class Event extends BaseObject {

	private static final long serialVersionUID = 2015030501L;

	private final String event;
	private final String channel;

	public Event(String event, String channel) {
		this.event = event;
		this.channel = channel;
	}

	public String getEvent() {
		return event;
	}

	public String getChannel() {
		return channel;
	}

}
