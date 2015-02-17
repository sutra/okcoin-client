package org.oxerr.okcoin.rest.dto.valuereader;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

import org.oxerr.okcoin.rest.dto.IcebergOrder;
import org.oxerr.okcoin.rest.dto.Type;
import org.oxerr.okcoin.rest.service.web.LoginRequiredException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLTableCellElement;
import org.w3c.dom.html.HTMLTableElement;
import org.w3c.dom.html.HTMLTableRowElement;

public class IcebergOrdersReader extends HtmlPageReader<IcebergOrder[]> {

	private static final IcebergOrdersReader INSTANCE = new IcebergOrdersReader();

	public static IcebergOrdersReader getInstance() {
		return INSTANCE;
	}

	private final SimpleDateFormat dateFormat;

	public IcebergOrdersReader() {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IcebergOrder[] read(HTMLDocument doc) {
		NodeList tableNodeList = doc.getElementsByTagName("table");
		if (tableNodeList.getLength() == 0) {
			throw new LoginRequiredException("No HTML table found.");
		}

		Node tableNode = tableNodeList.item(0);
		HTMLTableElement table = (HTMLTableElement) tableNode;
		HTMLCollection rows = table.getRows();
		IcebergOrder[] orders = new IcebergOrder[rows.getLength() - 1];
		for (int i = 1; i < rows.getLength(); i++) {
			HTMLTableRowElement row = (HTMLTableRowElement) rows.item(i);
			HTMLCollection cells = row.getCells();

			if (cells.getLength() == 1) {
				// no open orders
				orders = new IcebergOrder[0];
				break;
			}

			HTMLTableCellElement dateCell = (HTMLTableCellElement) cells.item(0);
			HTMLTableCellElement sideCell = (HTMLTableCellElement) cells.item(1);
			HTMLTableCellElement tradeValueCell = (HTMLTableCellElement) cells.item(2);
			HTMLTableCellElement singleAvgCell = (HTMLTableCellElement) cells.item(3);
			HTMLTableCellElement depthRangeCell = (HTMLTableCellElement) cells.item(4);
			HTMLTableCellElement protectedPriceCell = (HTMLTableCellElement) cells.item(5);
			HTMLTableCellElement filledCell = (HTMLTableCellElement) cells.item(6);
			HTMLTableCellElement statusCell = (HTMLTableCellElement) cells.item(7);

			String dateContent = dateCell.getTextContent();
			String sideContent = sideCell.getTextContent();
			String tradeValueContent = tradeValueCell.getTextContent();
			String singleAvgContent = singleAvgCell.getTextContent();
			String depthRangeContent = depthRangeCell.getTextContent();
			String protetedPriceContent = protectedPriceCell.getTextContent();
			String filledContent = filledCell.getTextContent();
			String idContent = statusCell.getId();

			Instant date;
			synchronized (dateFormat) {
				try {
					date = dateFormat.parse(dateContent).toInstant();
				} catch (ParseException e) {
					throw new IllegalArgumentException(e);
				}
			}

			Type side = sideContent.equals("Bid") ? Type.BUY : Type.SELL;
			BigDecimal tradeValue = new BigDecimal(tradeValueContent.substring(1));
			BigDecimal singleAvg = new BigDecimal(singleAvgContent.substring(1));
			BigDecimal depthRange = new BigDecimal(depthRangeContent.substring(0, depthRangeContent.length() - 1));
			BigDecimal protectedPrice = new BigDecimal(protetedPriceContent.substring(1).replace(",", ""));
			BigDecimal filled = new BigDecimal(filledContent.substring(1));
			long id = Long.valueOf(idContent.substring("continuous_".length()));
			orders[i - 1] = new IcebergOrder(id, date, side, tradeValue, singleAvg, depthRange, protectedPrice, filled);
		}
		return orders;
	}

}
