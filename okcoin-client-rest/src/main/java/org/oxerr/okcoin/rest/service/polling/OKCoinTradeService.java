package org.oxerr.okcoin.rest.service.polling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.UserTrades;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.polling.trade.PollingTradeService;
import org.knowm.xchange.service.polling.trade.params.DefaultTradeHistoryParamPaging;
import org.knowm.xchange.service.polling.trade.params.TradeHistoryParamCurrencyPair;
import org.knowm.xchange.service.polling.trade.params.TradeHistoryParamPaging;
import org.knowm.xchange.service.polling.trade.params.TradeHistoryParams;
import org.oxerr.okcoin.rest.OKCoinAdapters;
import org.oxerr.okcoin.rest.OKCoinException;
import org.oxerr.okcoin.rest.dto.CancelOrderResult;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.dto.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link PollingTradeService} implementation.
 */
public class OKCoinTradeService extends OKCoinTradeServiceRaw implements
		PollingTradeService {

	private final Logger log = LoggerFactory.getLogger(OKCoinTradeService.class);

	public OKCoinTradeService(Exchange exchange) {
		super(exchange);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpenOrders getOpenOrders() throws OKCoinException, IOException {
		Collection<CurrencyPair> symbols = getExchangeSymbols();
		List<OrderResult> orderResults = new ArrayList<>(symbols.size());

		for (CurrencyPair symbol : symbols) {
			log.debug("Getting order: {}", symbol);
			OrderResult orderResult = getOrder(
				OKCoinAdapters.adaptSymbol(symbol), -1);
			orderResults.add(orderResult);
		}

		return OKCoinAdapters.adaptOpenOrders(orderResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String placeMarketOrder(MarketOrder marketOrder)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotYetImplementedForExchangeException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String placeLimitOrder(LimitOrder limitOrder)
			throws OKCoinException, IOException {
		long orderId = trade(OKCoinAdapters.adaptSymbol(limitOrder.getCurrencyPair()),
				limitOrder.getType() == OrderType.BID ? Type.BUY : Type.SELL,
				limitOrder.getLimitPrice(),
				limitOrder.getTradableAmount())
				.getOrderId();
		return String.valueOf(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancelOrder(String orderId) throws OKCoinException,
			IOException {
		boolean ret = false;
		long id = Long.parseLong(orderId);

		for (CurrencyPair symbol : getExchangeSymbols()) {
			try {
				CancelOrderResult cancelResult = cancelOrder(
						OKCoinAdapters.adaptSymbol(symbol), id);

				if (id == cancelResult.getOrderId()) {
					ret = true;
				}
				break;
			} catch (OKCoinException e) {
				if (e.getErrorCode() == 10009) {
					// order not found.
					continue;
				}
			}
		}
		return ret;
	}

	@Deprecated
	public UserTrades getTradeHistory(Object... arguments)
			throws OKCoinException, IOException {
		int argc = arguments.length;

		CurrencyPair currencyPair = argc > 0 ? (CurrencyPair) arguments[0] : null;
		if (currencyPair == null) {
			throw new IllegalArgumentException();
		}

		int currentPage = argc > 1 ? ((Number) arguments[1]).intValue() : 0;
		int pageLength = argc > 2 ? ((Number) arguments[2]).intValue() : 200;

		String symbol = OKCoinAdapters.adaptSymbol(currencyPair);
		int status = 1; // 1 for filled orders
		OrderHistory history = getOrderHistory(symbol, status, currentPage, pageLength);
		return OKCoinAdapters.adaptUserTrades(history);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserTrades getTradeHistory(TradeHistoryParams params)
			throws OKCoinException, IOException {
		OKCoinTradeHistoryParams p = (OKCoinTradeHistoryParams) params;

		String symbol = OKCoinAdapters.adaptSymbol(p.getCurrencyPair());
		int status = 1; //  1 for filled orders
		int currentPage = p.getPageNumber();
		int pageLength = p.getPageLength();

		OrderHistory history = getOrderHistory(symbol, status, currentPage, pageLength);
		return OKCoinAdapters.adaptUserTrades(history);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TradeHistoryParams createTradeHistoryParams() {
		return new OKCoinTradeHistoryParams();
	}

	public static class OKCoinTradeHistoryParams extends
			DefaultTradeHistoryParamPaging implements
			TradeHistoryParamCurrencyPair, TradeHistoryParamPaging {

		private CurrencyPair pair;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setCurrencyPair(CurrencyPair pair) {
			this.pair = pair;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public CurrencyPair getCurrencyPair() {
			return pair;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Order> getOrder(String... orderIds)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		throw new NotYetImplementedForExchangeException();
	}

}
