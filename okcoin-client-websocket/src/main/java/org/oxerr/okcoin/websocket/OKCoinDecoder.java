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
import org.oxerr.okcoin.websocket.dto.Ticker;
import org.oxerr.okcoin.websocket.dto.TradesV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder to decode the WebSocket message to array of {@link OKCoinData}.
 */
public class OKCoinDecoder implements Decoder.TextStream<OKCoinData[]> {

	private final Logger log = LoggerFactory.getLogger(OKCoinDecoder.class);

	private final Map<String, Class<?>> types;

	public OKCoinDecoder() {
		types = new HashMap<String, Class<?>>();
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
			JsonObject o = (JsonObject) v;
			String channel = o.getString("channel");
			Object data = decodeData(channel, o.get("data"));
			return new OKCoinData(channel, data);
		}).collect(Collectors.toList()).toArray(new OKCoinData[]{});
	}

	private Object decodeData(String channel, JsonValue data) {
		Object ret = null;
		Class<?> type = types.get(channel);
		try {
			ret = type.getConstructor(JsonValue.class).newInstance(data);
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		}
		return ret;
	}

}
