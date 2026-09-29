/**
 * 
 */
package com.temenos.infinity.api.wealthservices.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPTableEvent;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.api.wealthservices.javaservice.GeneratePDF;
import com.kony.dbputilities.util.HelperMethods;

/**
 * @author himaja.sridhar
 *
 */

@SuppressWarnings("unused")
public class PDFGenerator {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private String imgFileName = "RetailBankingBanner.png";
	Font redFont = new Font(Font.TIMES_ROMAN, 10f, Font.NORMAL, Color.RED);
	Font font = new Font(Font.TIMES_ROMAN, 10f, Font.NORMAL, Color.BLACK);
	Font boldFont = new Font(Font.TIMES_ROMAN, 10f, Font.BOLD, Color.black);

	public static class customerDetails {
		String customerName;
		String portfolioName;

		public static String[] getKeys() {
			return new String[] { "Name: ", "Portfolio: " };
		}

		public String[] getValues() {
			return new String[] { this.customerName, this.portfolioName };
		}

		public customerDetails(String customerName, String portfolioName) {
			super();
			this.customerName = customerName;
			this.portfolioName = portfolioName;
		}
	}

	public static class accountDetails {
		String accountName;
		String accountCurrency;

		public static String[] getKeys() {
			return new String[] { "Account: ", "Account Currency: " };
		}

		public String[] getValues() {
			return new String[] { this.accountName, this.accountCurrency };
		}

		public accountDetails(String accountName, String accountCurrency) {
			super();
			this.accountName = accountName;
			this.accountCurrency = accountCurrency;
		}
	}

	public byte[] generateFile(customerDetails details, accountDetails acdet, JSONArray resultArr, int columnCount,
			LinkedHashMap<String, String> fields, String navPage, String isEur, String isIncludeOrders) throws IOException {
		InputStream is = GeneratePDF.class.getClassLoader().getResourceAsStream("log4j.properties");
		PropertyConfigurator.configure(is);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		Boolean isLandscape = false;
		try {
			Document document = new Document();
			if ((navPage.equals("Open Order") || navPage.equals("History Order") || navPage.equals("Watchlist"))) {
				document.setPageSize(PageSize.A4.rotate());
				isLandscape = true;
			} else {
				//document.setPageSize(PageSize.A4.rotate()); navPage.equals("InstrumentTransactions")
				//isLandscape = true;
			}
			PdfWriter writer = PdfWriter.getInstance(document, os);
			document.open();
			/** Adding Image **/
			setImageDetails(document, isLandscape);

			/** Customer Details **/
			PdfPTable customerTable = new PdfPTable(2);
			customerTable.setTotalWidth(new float[] { 65f, 90f });
			customerTable.setLockedWidth(true);
			customerTable.setSpacingBefore(7);
			customerTable.setSpacingAfter(5);
			customerTable.setHorizontalAlignment(0);

			customerTable.setTableEvent(new BorderEvent());
			getCustomerDetailsDiv(customerTable, details, acdet, navPage);
			Paragraph customerPara = new Paragraph();
			//customerPara.setIndentationLeft(15f);
			customerPara.add(customerTable);
			document.add(customerPara);

			/** Report Details **/
			PdfPTable genTable = new PdfPTable(1);
			genTable.setTotalWidth(new float[] { 50 });
			genTable.setSpacingAfter(5);
			getHoldingsGenDateDiv(genTable, navPage, isEur);
			document.add(genTable);

			PdfPTable noRecTable = new PdfPTable(2);
			noRecTable.setTotalWidth(new float[] { 50, 50 });
			noRecTable.setHorizontalAlignment(0);
			noRecTable.getDefaultCell().setBorder(0);
			if (resultArr.length() == 0) {
				getNoDataTable(noRecTable);
				document.add(noRecTable);
				/** Portfolio Performance **/
			} else if (navPage.equals("Performance")) {
				JSONArray overviewArray = resultArr.getJSONArray(0);
				JSONObject performanceObj = resultArr.getJSONObject(1);
				Paragraph performancePara = new Paragraph();
				performancePara.setIndentationLeft(15f);
				performancePara.add(this.getPerformnaceDetailsDiv(performanceObj));
				document.add(performancePara);
				document.add(this.getTableHeaderDetails(overviewArray, columnCount, fields, navPage, null));
			} else {
				document.add(this.getTableHeaderDetails(resultArr, columnCount, fields, navPage, isIncludeOrders));
			}
			document.close();
			writer.close();
		} catch (Exception e) {
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
		}
		return os.toByteArray();
	}

	public void getCustomerDetailsDiv(PdfPTable customerDivTable, customerDetails cust, accountDetails acdet,
			String navPage) {
		String[] keys = customerDetails.getKeys();
		String[] values = cust.getValues();
		for (int i = 0; i < keys.length; i++) {
			if (values[i] != null) {
				customerDivTable.addCell(CustomNoBorderCell(keys[i], font));
				customerDivTable.addCell(CustomNoBorderCell(values[i], font));

			}
		}
		if (navPage.equals("Account Activity")) {
			String[] keysA = accountDetails.getKeys();
			String[] valuesA = acdet.getValues();
			for (int i = 0; i < keysA.length; i++) {
				customerDivTable.addCell(CustomNoBorderCell(keysA[i], font));
				customerDivTable.addCell(CustomNoBorderCell(valuesA[i], font));
			}
		}
	}

	public void setImageDetails(Document mainDoc, Boolean isLandscape) throws IOException {
		InputStream is = PDFGenerator.class.getClassLoader().getResourceAsStream(this.imgFileName);
		Image image;
		int width = 530;
		if (isLandscape == true) {
			width = 770;
		}
		try {
			image = Image.getInstance(IOUtils.toByteArray(is));
			image.scaleAbsoluteHeight(50);
			image.scaleAbsoluteWidth(width);
			mainDoc.add(image);
		} catch (BadElementException e) {
			alert.prepareError("Error while invoking Image in PDF Generation: "+e).log();	
		} catch (IOException e) {
			alert.prepareError("Error while invoking Image in PDF Generation: "+e).log();
		}
		is.close();
	}

	public void getHoldingsGenDateDiv(PdfPTable genDiv, String navPage, String isEur) {
		SimpleDateFormat sdf = (isEur.equals("") ? new SimpleDateFormat("MM/dd/yyyy")
				: new SimpleDateFormat("yyyy/MM/dd"));
		Calendar cal = Calendar.getInstance();
		String header = navPage.concat(" Report");
		String content = navPage.concat(" Report as on ").concat(sdf.format(cal.getTime()));
		genDiv.addCell(getNoBorderCell(header, boldFont));
		genDiv.addCell(getNoBorderCell(content, font));
		genDiv.setHorizontalAlignment(0);

	}

	public void getNoDataTable(PdfPTable noTable) {
		String noRec = "No records found";
		noTable.addCell(getNoBorderCell(noRec, font));
		noTable.setHorizontalAlignment(1);

	}

	public PdfPTable getTableHeaderDetails(JSONArray resultArr, int columnCount, LinkedHashMap<String, String> fields,
			String navPage, String isIncludeOrders) {
		PdfPTable tableHeadDiv = new PdfPTable(resultArr.length() + 2);
		PdfPTable detailsTable = getTransactionsTable(fields.size(), navPage, isIncludeOrders);
		if ((navPage.equals("Open Order") || navPage.equals("History Order") || navPage.equals("Watchlist"))) {
			detailsTable.setTotalWidth(770);
		} else {
			detailsTable.setTotalWidth(520);
		}
		Object[] keys = fields.keySet().toArray();
		addHeaders(detailsTable, keys);
		for (int j = 0; j < resultArr.length(); j++) {
			JSONObject accountObject = resultArr.getJSONObject(j);
			addRow(detailsTable, accountObject, fields);
		}
		return detailsTable;

	}

	private void addRow(PdfPTable detailsTable, JSONObject accountObject, LinkedHashMap<String, String> fields) {
		try {
			for (String key : fields.values()) {
				if (accountObject.has(key)) {
					String data = accountObject.get(key).toString();
					if (("unrealPLMkt".equals(key) && data.contains("+"))
							|| ("amount".equals(key) && data.contains("+"))
							|| ("percentageChange".equals(key) && data.contains("+"))
							|| ("benchMarkIndex".equals(key) && data.contains("+"))
							|| ("balance".equals(key) && data.contains("+"))
							|| ("marketValue".equals(key) && data.contains("+"))
							|| ("fees".equals(key) && data.contains("+"))
							|| ("portfolioReturn".equals(key) && data.contains("+"))
							|| ("total".equals(key) && data.contains("+")))
							{
						data = data.replace("+", "");
						detailsTable.addCell(getNoBorderCell(data, font));
					} else if (("unrealPLMkt".equals(key) && data.contains("-"))
							|| ("amount".equals(key) && data.contains("-"))
							|| ("percentageChange".equals(key) && data.contains("-"))
							|| ("benchMarkIndex".equals(key) && data.contains("-"))
							|| ("balance".equals(key) && data.contains("-"))
							|| ("marketValue".equals(key) && data.contains("-"))
							|| ("fees".equals(key) && data.contains("-"))
							|| ("portfolioReturn".equals(key) && data.contains("-"))
							|| ("total".equals(key) && data.contains("-")))
							{
						String bracs = "(";
						data = bracs.concat(data.replace("-", "").concat(")"));
						detailsTable.addCell(getNoBorderCell(data, redFont));
					} else {
						detailsTable.addCell(getNoBorderCell(data, font));
					}
				} else {
					detailsTable.addCell(getNoBorderCell("", font));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in generation of pdf", e).log();
		}

	}

	public void addHeaders(PdfPTable table, Object[] headers) {
		for (int i = 0; i < headers.length; i++) {
			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(new Color(240, 255, 255));
			cell.setPhrase(new Phrase(headers[i].toString(), font));
			cell.setPaddingBottom(7f);
			cell.disableBorderSide(PdfPCell.LEFT);
			cell.disableBorderSide(PdfPCell.RIGHT);
			if (i == 0) {
				cell.enableBorderSide(PdfPCell.LEFT);
			}
			if (i == headers.length - 1) {
				cell.enableBorderSide(PdfPCell.RIGHT);
			}
			table.addCell(cell);
		}
	}

	private PdfPTable getTransactionsTable(int colCount, String navPage, String isIncludeOrders) {
		PdfPTable table = new PdfPTable(colCount);
		table.setSpacingBefore(10);
		table.setWidthPercentage(100);

		if (navPage.equalsIgnoreCase("Holdings")) {
			if (isIncludeOrders != null && isIncludeOrders.equalsIgnoreCase("true")) {
				table.setWidths(new float[] { 2.7f, 2.2f, 1.5f, 1.7f, 1.7f, 1.7f, 1.7f });
			} else {
				table.setWidths(new float[] { 2.7f, 2.2f, 1.5f, 1.7f, 1.7f, 1.7f });
			}
		}
		if (navPage.equalsIgnoreCase("Transactions")) {
			table.setWidths(new float[] { 1.7f, 2.2f, 1.9f, 1.8f, 1.4f, 1.8f, 1.6f, 1.9f });
		}
		if (navPage.equalsIgnoreCase("Accounts Activity")) {
			table.setWidths(new float[] { 1.8f, 3.6f, 1.6f, 2.5f, 1.7f, 1.7f });
		}
		if (navPage.equalsIgnoreCase("Open Order")) {
			table.setWidths(new float[] { 2.3f, 1.8f, 1.1f, 2.4f, 1.4f, 1.7f, 1.4f, 1.5f, 1.3f, 1.3f });
		}
		if (navPage.equalsIgnoreCase("History Order")) {
			table.setWidths(new float[] { 2.3f, 1.8f, 1.2f, 2.5f, 1.4f, 1.7f, 1.3f, 1.2f, 1.5f });
		}
		if (navPage.equalsIgnoreCase("InstrumentTransactions")) {
			table.setWidths(new float[] { 1.4f, 1.8f, 1.5f, 1.8f, 1.3f, 2.4f, 1.1f, 2.6f });
		}
		if (navPage.equalsIgnoreCase("Watchlist")) {
			table.setWidths(new float[] { 3f, 1.7f, 1.5f, 1.2f, 1.5f, 1.3f, 1.7f, 1.3f, 1.3f, 1.3f });
		}
		return table;
	}

	public PdfPCell getNoBorderCell(String data, Font red) {
		PdfPCell newCell = new PdfPCell();
		newCell.setBorder(0);
		newCell.setPaddingTop(5f);
		newCell.setPaddingBottom(3f);
		Phrase dataPhrase = new Phrase(data, red);
		newCell.setPhrase(dataPhrase);
		return newCell;
	}
	public PdfPCell CustomNoBorderCell(String data, Font red) {
		PdfPCell newCell = new PdfPCell();
		newCell.setBorder(0);
		newCell.setPadding(6f);
		Phrase dataPhrase = new Phrase(data, red);
		newCell.setPhrase(dataPhrase);
		return newCell;
	}


	public PdfPTable getPerformnaceDetailsDiv(JSONObject performanceObj) {
		PdfPTable userTable = new PdfPTable(2);
		userTable.setTableEvent(new BorderEvent());
		userTable.setSpacingBefore(5f);
		userTable.setSpacingAfter(5f);
		userTable.setHorizontalAlignment(0);
		PdfPCell key = null;
		PdfPCell value = null;
		Set<String> keys = performanceObj.keySet();
		for (String s : keys) {
			key = CustomNoBorderCell(s.concat(": "), font);
			String data = performanceObj.get(s).toString();
			if (s.equals("Fees & Taxes") && data.contains("+")||
					s.equals("Current Value") && data.contains("+") ||
					s.equals("Initial Value") && data.contains("+") ||
					s.equals("Net Deposit") && data.contains("+") ||
					s.equals("P&L") && data.contains("+") ||
					s.equals("Time-Weighted Return") && data.contains("+") ||
					s.equals("Money-Weighted Return") && data.contains("+")) {
				data = data.replace("+", "");
				value = CustomNoBorderCell(data, font);
			} else if (s.equals("Fees & Taxes") && data.contains("-")|| 
					s.equals("Current Value") && data.contains("-") ||
					s.equals("Initial Value") && data.contains("-") ||
					s.equals("Net Deposit") && data.contains("-") ||
					s.equals("P&L") && data.contains("-") ||
					s.equals("Time-Weighted Return") && data.contains("-") ||
					s.equals("Money-Weighted Return") && data.contains("-")) {
				String bracs = "(";
				data = bracs.concat(data.replace("-", "").concat(")"));
				value = CustomNoBorderCell(data, redFont);
			} else {
				value = CustomNoBorderCell(performanceObj.get(s).toString(), font);
			}
			userTable.addCell(key);
			userTable.addCell(value);
		}
		return userTable;
	}

	public class BorderEvent implements PdfPTableEvent {
		public void tableLayout(PdfPTable table, float[][] widths, float[] heights, int headerRows, int rowStart,
				PdfContentByte[] canvases) {
			float width[] = widths[0];
			float x1 = width[0];
			float x2 = 350; // width[width.length - 1];
			float y1 = heights[0];
			float y2 = heights[heights.length - 1];
			PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
			cb.rectangle(x1, y1, x2 - x1, y2 - y1);
			cb.stroke();
			cb.resetRGBColorStroke();
		}
	}

}
