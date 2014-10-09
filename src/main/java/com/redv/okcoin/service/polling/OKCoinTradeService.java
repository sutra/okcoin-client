package com.redv.okcoin.service.polling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redv.okcoin.OKCoinAdapters;
import com.redv.okcoin.OKCoinException;
import com.redv.okcoin.domain.OrderResult;
import com.redv.okcoin.domain.TradeResult;
import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.NotAvailableFromExchangeException;
import com.xeiam.xchange.NotYetImplementedForExchangeException;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.service.polling.PollingTradeService;

public class OKCoinTradeService extends OKCoinTradeServiceRaw implements
		PollingTradeService {

	private final Logger log = LoggerFactory.getLogger(OKCoinTradeService.class);

	/**
	 * @param exchangeSpecification the exchange specification.
	 */
	public OKCoinTradeService(ExchangeSpecification exchangeSpecification) {
		super(exchangeSpecification);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OpenOrders getOpenOrders() throws ExchangeException,
			NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		Collection<CurrencyPair> symbols = getExchangeSymbols();
		List<OrderResult> orderResults = new ArrayList<>(symbols.size());

		for (CurrencyPair symbol : symbols) {
			log.debug("Getting order: {}", symbol);
			OrderResult orderResult = getOrder(-1,
					OKCoinAdapters.adaptSymbol(symbol));
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
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		long orderId = trade(OKCoinAdapters.adaptSymbol(limitOrder.getCurrencyPair()),
				limitOrder.getType() == OrderType.BID ? "buy" : "sell",
				limitOrder.getLimitPrice().toPlainString(),
				limitOrder.getTradableAmount().toPlainString())
				.getOrderId();
		return String.valueOf(orderId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean cancelOrder(String orderId) throws ExchangeException,
			NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		boolean ret = false;
		long id = Long.parseLong(orderId);

		for (CurrencyPair symbol : getExchangeSymbols()) {
			try {
				TradeResult cancelResult = cancelOrder(id,
						OKCoinAdapters.adaptSymbol(symbol));

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Trades getTradeHistory(Object... arguments)
			throws ExchangeException, NotAvailableFromExchangeException,
			NotYetImplementedForExchangeException, IOException {
		int argc = arguments.length;
		CurrencyPair currencyPair = argc > 0 ? (CurrencyPair) arguments[0] : null;
		Long orderId = argc > 0 ? (Long) arguments[1] : null;

		if (currencyPair != null && orderId != null) {
			return OKCoinAdapters.adaptTrades(
					getOrder(orderId, OKCoinAdapters.adaptSymbol(currencyPair)));
		} else {
			throw new IllegalArgumentException();
		}
	}

}
