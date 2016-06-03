package org.oxerr.okcoin.websocket;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.oxerr.okcoin.websocket.dto.CandlestickChart;
import org.oxerr.okcoin.websocket.dto.Depth;
import org.oxerr.okcoin.websocket.dto.Info;
import org.oxerr.okcoin.websocket.dto.OrderResult;
import org.oxerr.okcoin.websocket.dto.Ticker;
import org.oxerr.okcoin.websocket.dto.Trade;
import org.oxerr.okcoin.websocket.dto.TradeResult;
import org.oxerr.okcoin.websocket.dto.TradesV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder to decode the WebSocket message to array of {@link OKCoinData}.
 */
public class OKCoinDecoder implements Decoder.TextStream<OKCoinData[]> {

	private static final OKCoinData[] EMPTY_DATA = new OKCoinData[0];

	private final Logger log = LoggerFactory.getLogger(OKCoinDecoder.class);

	private final Map<String, Class<?>> types;

	public OKCoinDecoder() {
		types = new HashMap<>();
		for (String symbol : new String[] {"btccny", "ltccny"}) {
			types.put(String.format("ok_%s_ticker", symbol), Ticker.class);
			types.put(String.format("ok_%s_depth", symbol), Depth.class);
			types.put(String.format("ok_%s_depth60", symbol), Depth.class);
			types.put(String.format("ok_%s_trades_v1", symbol), TradesV1.class);
			for (String x : new String[] {
				"1min", "3min", "5min", "15min", "30min",
				"1hour", "2hour", "4hour", "6hour", "12hour",
				"day", "3day", "week", }) {
				types.put(String.format("ok_%s_kline_%s", symbol, x),
						CandlestickChart.class);
			}
		}

		types.put("ok_cny_realtrades", Trade.class);
		types.put("ok_spotcny_trade", TradeResult.class);
		types.put("ok_spotcny_cancel_order", TradeResult.class);
		types.put("ok_spotcny_userinfo", Info.class);
		types.put("ok_spotcny_order_info", OrderResult.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(EndpointConfig config) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void destroy() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OKCoinData[] decode(Reader reader) throws DecodeException, IOException {
		JsonReader jsonReader = Json.createReader(reader);
		JsonArray jsonArray = jsonReader.readArray();
		return jsonArray.stream().map(v -> {

			if (log.isTraceEnabled()) {
				log.trace("Decoding: {}", v);
			}

			JsonObject o = (JsonObject) v;
			String channel = o.getString("channel");
			Object data = decodeData(channel, o.get("data"));
			return new OKCoinData(channel, data);
		}).collect(Collectors.toList()).toArray(EMPTY_DATA);
	}

	private Object decodeData(String channel, JsonValue data) {
		if (data == null) {
			return null;
		}

		Object ret = null;
		Class<?> type = types.get(channel);

		if (type == null) {
			throw new IllegalArgumentException("Unknown channel: " + channel);
		}

		try {
			ret = type.getConstructor(JsonValue.class).newInstance(data);
		} catch (Exception e) {
			log.warn(String.format("Decode failed. Channel: %s, data: %s.", channel, data), e);
		}
		return ret;
	}

}
