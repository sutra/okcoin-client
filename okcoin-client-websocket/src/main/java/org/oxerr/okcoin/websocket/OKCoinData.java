package org.oxerr.okcoin.websocket;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * The data that OKCoin WebSocket API responded.
 */
public class OKCoinData implements Serializable {

	private static final long serialVersionUID = 2015030301L;

	private final String channel;

	private final Object data;

	public OKCoinData(String channel, Object data) {
		this.channel = channel;
		this.data = data;
	}

	public String getChannel() {
		return channel;
	}

	public Object getData() {
		return data;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
