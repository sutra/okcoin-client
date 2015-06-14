package org.oxerr.okcoin.websocket.dto;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonValue;

import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.dto.Type;

public class TradesV1 extends BaseObject {

	private static final long serialVersionUID = 2015030501L;

	private static final Trade[] EMPTY_TRADES = new Trade[0];

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static {
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
		dateFormat.setTimeZone(timeZone);
		timeFormat.setTimeZone(timeZone);
	}

	private final Trade[] trades;

	public TradesV1(JsonValue jsonValue) {
		this((JsonArray) jsonValue);
	}

	public TradesV1(JsonArray jsonArray) {
		String today;
		synchronized (dateFormat) {
			today = dateFormat.format(new Date());
		}

		this.trades = jsonArray.stream().map(v -> {
			JsonArray a = (JsonArray) v;
			long tid = Long.parseLong(a.getString(0));
			BigDecimal px = new BigDecimal(a.getString(1));
			BigDecimal qty = new BigDecimal(a.getString(2));
			Date time;
			synchronized (timeFormat) {
				try {
					time = timeFormat.parse(String.format("%s %s", today, a.getString(3)));
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
			String side = a.getString(4);
			Type type = side.equals("ask") ? Type.SELL : Type.BUY;
			return new Trade(time.toInstant(), px, qty, tid, type);
		}).collect(Collectors.toList()).toArray(EMPTY_TRADES);
	}

	public Trade[] getTrades() {
		return trades;
	}

}
