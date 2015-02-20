package org.oxerr.okcoin.rest;

import java.io.IOException;
import java.math.BigDecimal;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.oxerr.okcoin.rest.dto.BatchTradeResult;
import org.oxerr.okcoin.rest.dto.BorrowOrderInfo;
import org.oxerr.okcoin.rest.dto.BorrowResult;
import org.oxerr.okcoin.rest.dto.BorrowsInfo;
import org.oxerr.okcoin.rest.dto.CancelOrderResult;
import org.oxerr.okcoin.rest.dto.CandlestickChart;
import org.oxerr.okcoin.rest.dto.Depth;
import org.oxerr.okcoin.rest.dto.LendDepth;
import org.oxerr.okcoin.rest.dto.OrderHistory;
import org.oxerr.okcoin.rest.dto.OrderResult;
import org.oxerr.okcoin.rest.dto.TickerResponse;
import org.oxerr.okcoin.rest.dto.Trade;
import org.oxerr.okcoin.rest.dto.TradeResult;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.rest.dto.UnrepaymentsInfo;
import org.oxerr.okcoin.rest.dto.UserInfo;
import org.oxerr.okcoin.rest.dto.Withdrawal;

import si.mazi.rescu.ParamsDigest;

/**
 * RESTful API.
 */
@Path("/api/v1/")
@Produces(MediaType.APPLICATION_JSON)
public interface OKCoin {

	/**
	 * Get price ticker.
	 *
	 * @param symbol the symbol.
	 * @return price ticker.
	 * @throws IOException indicates I/O exception.
	 */
	@GET
	@Path("ticker.do")
	TickerResponse getTicker(@QueryParam("symbol") String symbol)
		throws IOException;

	/**
	 * Get market depth.
	 *
	 * @param symbol the symbol.
	 * @param size must be between 5 - 200
	 * @param merge 1 (merge depth)
	 * @return market depth.
	 * @throws IOException indicates I/O exception.
	 */
	@GET
	@Path("depth.do")
	Depth getDepth(
		@QueryParam("symbol") String symbol,
		@QueryParam("size") Integer size,
		@QueryParam("merge") Integer merge)
			throws IOException;

	/**
	 * Get trade history.
	 *
	 * @param symbol the symbol.
	 * @param since if 'since' parameter is not supplied, the most recent 60
	 * transactions are returned.
	 * @return the trade history.
	 * @throws IOException indicates I/O exception.
	 */
	@GET
	@Path("trades.do")
	Trade[] getTrades(
		@QueryParam("symbol") String symbol,
		@QueryParam("since") Long since)
			throws IOException;

	/**
	 * Get BTC/LTC candlestick data.
	 *
	 * @param symbol the symbol.
	 * @param type
	 * <ul>
	 * <li>1min : 1 minute candlestick data</li>
	 * <li>3min : 3 minutes candlestick data</li>
	 * <li>5min : 5 minutes candlestick data</li>
	 * <li>15min : 15 minutes candlestick data</li>
	 * <li>30min : 30 minutes candlestick data</li>
	 * <li>1day : 1 day candlestick data</li>
	 * <li>3day : 3 days candlestick data</li>
	 * <li>1week : 1 week candlestick data</li>
	 * <li>1hour : 1 hour candlestick data</li>
	 * <li>2hour : 2 hours candlestick data</li>
	 * <li>4hour : 4 hours candlestick data</li>
	 * <li>6hour : 6 hours candlestick data</li>
	 * <li>12hour : 12 hours candlestick data</li>
	 * </ul>
	 * @param size specify data size to be acquired
	 * @param since timestamp(eg:1417536000000).
	 * data after the timestamp will be returned
	 * @return candlestick.
	 * @throws IOException indicates I/O exception.
	 */
	@GET
	@Path("kline.do")
	CandlestickChart getCandlestickChart(
		@QueryParam("symbol") String symbol,
		@QueryParam("type") String type,
		@QueryParam("size") Integer size,
		@QueryParam("since") Long since)
			throws IOException;

	/**
	 * Get user account info.
	 *
	 * @param apiKey API key of the user.
	 * @param sign signature of request parameters
	 * @return user account info.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("userinfo.do")
	UserInfo getUserInfo(
		@FormParam("api_key") String apiKey,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Place order.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol.
	 * @param type order type: limit order(buy/sell) market order(buy_market/sell_market).
	 * @param price order price. For limit orders, the price must be
	 * between 0~1,000,000. IMPORTANT: for market buy orders, the price is to
	 * total amount you want to buy, and it must be higher than the current
	 * price of 0.01 BTC (minimum buying unit) or 0.1 LTC.
	 * @param amount order quantity. Must be higher than 0.01 for BTC, or 0.1 for LTC.
	 * @param sign signature of request parameters
	 * @return
	 * result: true means order placed successfully
	 * order_id: order ID of the newly placed order
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("trade.do")
	TradeResult trade(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("type") Type type,
		@FormParam("price") BigDecimal price,
		@FormParam("amount") BigDecimal amount,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol.
	 * @param type optional, order type for limit orders (buy/sell).
	 * @param ordersData JSON string Example: [{price:3,amount:5,type:
	 * 'sell'},{price:3,amount:3,type:'buy'},{price:3,amount:3}] max
	 * order number is 5，for 'price' and 'amount' parameter, refer to
	 * trade/API. Final order type is decided primarily by 'type'
	 * field within 'orders_data' and subsequently by 'type' field
	 * (if no 'type' is provided within 'orders_data' field)
	 * @param sign signature of request parameters.
	 * @return
	 * result: true indicates order successfully placed
	 * order_id: order ID
	 * Note: return true if any one order is placed successfully
	 * returned orders info and 'orders_data' are in same sequence
	 * if fail to place order: order_id is -1
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("batch_trade.do")
	BatchTradeResult batchTrade(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("type") Type type,
		@FormParam("orders_data") String ordersData,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Cancel orders.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol.
	 * @param orderId order ID (multiple orders are separated by a comma ',',
	 * Max of 3 orders are allowed per request)
	 * @param sign signature of request parameters.
	 * @return
	 * result: request result (applicable to single order)
	 * order_id: order ID (applicable to single order)
	 * success: success order IDs (applicable to batch orders)
	 * error: failed order IDs (applicable to batch orders)
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("cancel_order.do")
	CancelOrderResult cancelOrder(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("order_id") String orderId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Get order info.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol.
	 * @param orderId if order_id is -1, then return all unfilled orders,
	 * otherwise return the order specified
	 * @param sign signature of request parameters.
	 * @return the order with the specified order ID or all unfilled orders.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("order_info.do")
	OrderResult getOrder(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("order_id") long orderId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Get order information in batch.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol.
	 * @param type query type: 0 for unfilled (open) orders, 1 for filled orders
	 * @param orderId order ID (multiple orders are separated by ',',
	 * 50 orders at most are allowed per request).
	 * @param sign signature of request parameters
	 * @return order information.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("orders_info.do")
	OrderResult getOrders(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("type") int type,
		@FormParam("order_id") String orderId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Returns the most recent 7 days orders.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol.
	 * @param status query status: 0 for unfilled orders, 1 for filled orders.
	 * @param currentPage current page number.
	 * @param pageLength number of orders returned per page, maximum 200.
	 * @param sign signature of request parameters
	 * @return recent orders with specified parameters,
	 * only the most recent 7 days are returned.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("order_history.do")
	OrderHistory getOrderHistory(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("status") int status,
		@FormParam("current_page") int currentPage,
		@FormParam("page_length") int pageLength,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * BTC/LTC Withdraw.
	 *
	 * @param apiKey the API key of the suer.
	 * @param symbol the symbol: BTC, LTC.
	 * @param chargeFee network transaction fee. By default, fee is between
	 * 0.0001 - 0.01 for BTC, and 0.001 - 0.2 for LTC, transaction gets
	 * confirmed faster with higher fees. For withdraws to another OKCoin
	 * address, chargefee can be 0 and the withdraw will be 0 confirmation
	 * as well.
	 * @param tradePassword trade/admin password.
	 * @param withdrawAddress withdraw address.
	 * @param withdrawAmount withdraw amount in BTC or LTC.
	 * @param sign signature of request parameters.
	 * @return
	 * withdraw_id: withdrawal request ID
	 * result: true means request successful
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("withdraw.do")
	Withdrawal withdraw(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("chargefee") BigDecimal chargeFee,
		@FormParam("trade_pwd") String tradePassword,
		@FormParam("withdraw_address") String withdrawAddress,
		@FormParam("withdraw_amount") BigDecimal withdrawAmount,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Withdrawal Cancellation Request.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol: BTC, LTC.
	 * @param withdrawId withdrawal request ID.
	 * @param sign signature of request parameters.
	 * @return
	 * result: true for success, false for error
	 * withdraw_id: withdrawal request ID
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("cancel_withdraw.do")
	Withdrawal cancelWithdraw(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("withdraw_id") long withdrawId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Get user borrow information.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol, such as btc_cny, ltc_cny, cny.
	 * @param sign signature of request parameters.
	 * @return the borrow information of the user.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("borrows_info.do")
	BorrowsInfo getBorrowsInfo(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Request borrow.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol, such as btc_cny, ltc_cny, cny.
	 * @param days days of borrow: three, seven, fifteen, thirty, sixty, ninety.
	 * @param amount borrow amount.
	 * @param rate borrow rate [0.0001, 0.01].
	 * @param sign signature of request parameters.
	 * @return the borrow.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("borrow_money.do")
	BorrowResult borrowMoney(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("days") String days,
		@FormParam("amount") BigDecimal amount,
		@FormParam("rate") BigDecimal rate,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Cancel borrow order.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol, such as btc_cny, ltc_cny, cny.
	 * @param borrowId the borrow order ID.
	 * @param sign signature of request parameters.
	 * @return the cancelled borrow order.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("cancel_borrow.do")
	BorrowResult cancelBorrow(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("borrow_id") long borrowId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Get top 10 lending entries.
	 *
	 * @param symbol the symbol, such as btc_cny, ltc_cny, cny.
	 * @return the top 10 lending entries.
	 * @throws IOException indicates I/O exception.
	 */
	@GET
	@Path("lend_depth.do")
	LendDepth getLendDepth(
		@QueryParam("symbol") String symbol)
			throws OKCoinException, IOException;

	/**
	 * Get borrowing order info.
	 *
	 * @param apiKey the API key of the user.
	 * @param borrowId the borrow order ID.
	 * @param sign signature of request parameters.
	 * @return the borrowing order info.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("borrow_order_info.do")
	BorrowOrderInfo getBorrowOrderInfo(
		@FormParam("api_key") String apiKey,
		@FormParam("borrow_id") long borrowId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Pay off debt.
	 *
	 * @param apiKey the API key of the user.
	 * @param borrowId the borrow order ID.
	 * @param sign signature of request parameters.
	 * @return the borrowing order info.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("repayment.do")
	BorrowResult repay(
		@FormParam("api_key") String apiKey,
		@FormParam("borrow_id") long borrowId,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

	/**
	 * Get debt list.
	 *
	 * @param apiKey the API key of the user.
	 * @param symbol the symbol, such as btc_cny, ltc_cny, cny.
	 * @param currentPage the current page number.
	 * @param pageLength data entries number per page, maximum 50.
	 * @param sign signature of request parameters
	 * @return the debt list.
	 * @throws OKCoinException indicates request failed.
	 * @throws IOException indicates I/O exception.
	 */
	@POST
	@Path("unrepayments_info.do")
	UnrepaymentsInfo getUnrepaymentsInfo(
		@FormParam("api_key") String apiKey,
		@FormParam("symbol") String symbol,
		@FormParam("current_page") int currentPage,
		@FormParam("page_length") int pageLength,
		@FormParam("sign") ParamsDigest sign)
			throws OKCoinException, IOException;

}
