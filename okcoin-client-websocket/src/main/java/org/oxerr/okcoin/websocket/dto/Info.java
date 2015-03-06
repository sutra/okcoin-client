package org.oxerr.okcoin.websocket.dto;

import javax.json.JsonObject;
import javax.json.JsonValue;

public class Info extends org.oxerr.okcoin.rest.dto.Info {

	private static final long serialVersionUID = 2015030701L;

	public Info(JsonValue jsonValue) {
		this((JsonObject) jsonValue);
	}

	public Info(JsonObject jsonObject) {
		super(new Funds(jsonObject.getJsonObject("info").getJsonObject("funds")));
	}

}
