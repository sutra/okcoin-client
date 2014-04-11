package com.redv.okcoin;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.redv.okcoin.domain.Balance;
import com.redv.okcoin.domain.Depth;
import com.redv.okcoin.domain.Result;
import com.redv.okcoin.domain.Ticker;
import com.redv.okcoin.domain.TickerResponse;
import com.redv.okcoin.domain.Trade;
import com.redv.okcoin.domain.TradeParam;
import com.redv.okcoin.valuereader.IndexHtmlPageReader;
import com.redv.okcoin.valuereader.ResultValueReader;
import com.redv.okcoin.valuereader.VoidValueReader;

public class OKCoinClient implements AutoCloseable {

	public static final String ENCODING = "UTF-8";

	public static final ContentType APPLICATION_FORM_URLENCODED = ContentType.create(
			"application/x-www-form-urlencoded", Consts.UTF_8);

	private static final URI HTTPS_BASE = URI.create("https://www.okcoin.com/");
	private static final URI API_BASE = URIUtils.resolve(HTTPS_BASE, "api/");

	private static final URI TICKER_URI = URIUtils.resolve(API_BASE, "ticker.do");
	private static final URI DEPTH_URI = URIUtils.resolve(API_BASE, "depth.do");
	private static final URI TRADES_URI = URIUtils.resolve(API_BASE, "trades.do");

	private static final URI LOGIN_URI = URIUtils.resolve(HTTPS_BASE, "login/index.do");
	private static final URI LOGOUT_URI = URIUtils.resolve(HTTPS_BASE, "user/logout.do");

	private static final URI BUY_BTC_SUBMIT_URI = URIUtils.resolve(HTTPS_BASE, "trade/buyBtcSubmit.do");
	private static final URI SELL_BTC_SUBMIT_URI = URIUtils.resolve(HTTPS_BASE, "trade/sellBtcSubmit.do");

	private static final String TRADE_BTC_REFERER_PREFIX = HTTPS_BASE.toString() + "trade/btc.do?tradeType=";

	private static final BigDecimal MIN_TRADE_AMOUNT = new BigDecimal("0.01");

	private final Logger log = LoggerFactory.getLogger(OKCoinClient.class);

	private final HttpClient httpClient;

	private String loginName;

	private String password;

	private String tradePwd;

	public OKCoinClient(
			int socketTimeout,
			int connectTimeout,
			int connectionRequestTimeout) {
		this(null, null, null,
				socketTimeout, connectTimeout, connectionRequestTimeout);
	}

	public OKCoinClient(String loginName, String password,
			int socketTimeout,
			int connectTimeout,
			int connectionRequestTimeout) {
		this(loginName, password, null,
				socketTimeout, connectTimeout, connectionRequestTimeout);
	}

	public OKCoinClient(String loginName, String password, String tradePwd,
			int socketTimeout,
			int connectTimeout,
			int connectionRequestTimeout) {
		httpClient = new HttpClient(
				socketTimeout, connectTimeout, connectionRequestTimeout);

		this.loginName = loginName;
		this.password = password;
		this.tradePwd = tradePwd;
	}

	public Ticker getTicker() throws IOException {
		return httpClient.get(TICKER_URI, TickerResponse.class).getTicker();
	}

	public Depth getDepth() throws IOException {
		return httpClient.get(DEPTH_URI, Depth.class);
	}

	/**
	 * Returns the recent 80 trades.
	 *
	 * @return the recent 80 trades.
	 * @throws IOException indicates I/O exception.
	 */
	public List<Trade> getTrades() throws IOException {
		return httpClient.get(TRADES_URI, new TypeReference<List<Trade>>() {
		});
	}

	public List<Trade> getTrades(int since) throws IOException {
		URIBuilder builder = new URIBuilder(TRADES_URI);
		builder.addParameter("since", Integer.toString(since));

		final URI uri;
		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}

		return httpClient.get(uri, new TypeReference<List<Trade>>() {
		});
	}

	public void login() throws IOException {
		initLoginPage();

		URIBuilder builder = new URIBuilder(LOGIN_URI);
		builder.setParameter("random", randomInString());
		URI uri;
		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}

		Result result = httpClient.post(
				uri,
				ResultValueReader.getInstance(),
				new BasicNameValuePair("loginName", loginName),
				new BasicNameValuePair("password", password));

		log.debug("Login result: {}", result);

		switch(result.getResultCode()) {
		case -1:
			throw new OKCoinClientException("用户名或密码错误");
		case -2:
			throw new OKCoinClientException("此ip登录频繁，请2小时后再试");
		case -3:
			if (result.getErrorNum() == 0) {
				throw new OKCoinClientException("此ip登录频繁，请2小时后再试");
			} else {
				throw new OKCoinClientException("用户名或密码错误，您还有" + result.getErrorNum() + "次机会");
			}
		case -4:
			throw new OKCoinClientException("请设置启用COOKIE功能");
		case 1:
			break;
		case 2:
			throw new OKCoinClientException("账户出现安全隐患被冻结，请尽快联系客服。");
		}
	}

	public void logout() throws IOException {
		httpClient.get(LOGOUT_URI, VoidValueReader.getInstance());
	}

	public Balance getBalance() throws IOException {
		return httpClient.get(HTTPS_BASE, new IndexHtmlPageReader());
	}

	public BigDecimal getMinTradeAmount() {
		return MIN_TRADE_AMOUNT;
	}

	public void bid(BigDecimal amount, BigDecimal cnyPrice) throws IOException {
		final int tradeType = 0;
		final int symbol = 0;
		final int isopen = 0;

		trade(amount, cnyPrice, tradeType, symbol, isopen);
	}

	public void ask(BigDecimal amount, BigDecimal cnyPrice) throws IOException {
		final int tradeType = 1;
		final int symbol = 0;
		final int isopen = 0;

		trade(amount, cnyPrice, tradeType, symbol, isopen);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		logout();
		httpClient.close();
	}

	/**
	 * Calls this method before doing login post is required, otherwise the
	 * server will complain "请设置启用COOKIE功能" as
	 * {@link Result#getResultCode()} = -4.
	 */
	private void initLoginPage() throws IOException {
		httpClient.get(HTTPS_BASE, VoidValueReader.getInstance());
	}

	/**
	 *
	 * @param tradeAmount
	 * @param tradeCnyPrice
	 * @param tradeType 0 means buy, otherwise sell.
	 * @param symbol 0 means BTC, 1 means LTC.
	 * @param isopen
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	private void trade(BigDecimal tradeAmount, BigDecimal tradeCnyPrice,
			int tradeType, int symbol, int isopen) throws IOException {
		if (tradeAmount.compareTo(MIN_TRADE_AMOUNT) < 0) {
			final String error;
			if (symbol == 1) {
				error = "最小交易数量为：0.1LTC！";
			} else {
				error = "最小交易数量为：0.01BTC！";
			}
			throw new IllegalArgumentException(error);
		}

		final URI submitUri;
		if (tradeType == 0) {
			submitUri = BUY_BTC_SUBMIT_URI;
		} else {
			submitUri = SELL_BTC_SUBMIT_URI;
		}

		final URI uri;
		try {
			uri = new URIBuilder(submitUri).setParameter("random",
					randomInString()).build();
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}

		TradeParam tradeParam = new TradeParam(
				tradeAmount, tradeCnyPrice, tradePwd, symbol);

		final HttpPost post = new HttpPost(uri);
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		final String referer =  TRADE_BTC_REFERER_PREFIX + tradeType;
		log.debug("Add referer header: {}", referer);
		post.setHeader("Referer", referer);
		post.setEntity(new UrlEncodedFormEntity(tradeParam.toNameValurPairs()));

		Result result = httpClient.execute(ResultValueReader.getInstance(), post);

		final String error;
		if (result != null) {
			log.debug("Result: {}", result);

			if (result.getResultCode() == -1) {
				if (tradeType == 0) {
					if (symbol == 1) {
						error = "最小购买数量为：0.1LTC！";
					} else {
						error = "最小购买数量为：0.01BTC！";
					}
				} else {
					if (symbol == 1) {
						error = "最小卖出数量为：0.1LTC！";
					} else {
						error = "最小卖出数量为：0.01BTC！";
					}
				}
			} else if (result.getResultCode() == -2) {
				if (result.getErrorNum() == 0) {
					error = "交易密码错误五次，请2小时后再试！";
				} else {
					error = "交易密码不正确！您还有" + result.getErrorNum() + "次机会";
				}
			} else if (result.getResultCode() == -3) {
				error = "出价不能为0！";
			} else if (result.getResultCode() == -4) {
				error = "余额不足！";
			} else if (result.getResultCode() == -5) {
				error = "您未设置交易密码，请设置交易密码。";
			} else if (result.getResultCode() == -6) {
				error = "您输入的价格与最新成交价相差太大，请检查是否输错";
			} else if (result.getResultCode() == -7) {
				error = "交易密码免输超时，请刷新页面输入交易密码后重新激活。";
			} else if (result.getResultCode() == -8) {
				error = "请输入交易密码";
			} else if (result.getResultCode() == 0) {
				// success
				error = null;
			} else if (result.getResultCode() == 2) {
				error = "账户出现安全隐患已被冻结，请尽快联系客服。";
			} else {
				error = null;
			}
		} else {
			error = null;
		}

		if (error != null) {
			throw new OKCoinClientException(error);
		}
	}

	private String randomInString() {
		return Long.toString(random());
	}

	private long random() {
		long random = Math.round(Math.random() * 100);
		return random;
	}

}
