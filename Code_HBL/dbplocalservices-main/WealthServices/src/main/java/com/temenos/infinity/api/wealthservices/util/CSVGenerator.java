/**
 * 
 */
package com.temenos.infinity.api.wealthservices.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.infinity.api.wealthservices.util.PDFGenerator.accountDetails;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.customerDetails;

/**
 * @author himaja.sridhar
 *
 */
public class CSVGenerator {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private String imgFileName = "RetailBankingBanner.png";

	private String FIELD_SEPARATOR = ",";
	private String HEADER_SEPARATOR = "\t,";

	public String getFieldSeparator() {
		return FIELD_SEPARATOR;
	}

	public void setFieldSeparator(String fIELD_SEPARATOR) {
		FIELD_SEPARATOR = fIELD_SEPARATOR;
	}

	public byte[] generateFile(customerDetails details, accountDetails acdet, JSONArray resultArr, int columnCount,
			LinkedHashMap<String, String> fields, String navPage, String isEur) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();) {

			/** Customer Details **/
			getCustomerDetailsDiv(bos, details, acdet, navPage);

			/** Report Details **/
			getReportGenerateDiv(bos, navPage, isEur);

			/** No records **/
			if (resultArr.length() == 0) {
				getNoData(bos);
			} else if (navPage.equals("Performance")) {
				JSONArray overviewArray = resultArr.getJSONArray(0);
				JSONObject performanceObj = resultArr.getJSONObject(1);
				getPerformanceDetailsDiv(bos, performanceObj);
				getTableHeaderDetails(bos, overviewArray, columnCount, fields, navPage);
			} else {
				getTableHeaderDetails(bos, resultArr, columnCount, fields, navPage);
			}
			return bos.toByteArray();
		} catch (IOException ioe) {
			throw ioe;
		}

	}

	private void getTableHeaderDetails(ByteArrayOutputStream bos, JSONArray resultArr, int columnCount,
			LinkedHashMap<String, String> fields, String navPage) throws IOException {
		Object[] keys = fields.keySet().toArray();
		bos.write(getBytes(System.lineSeparator()));
		for (int i = 0; i < keys.length; i++) {
			bos.write(getBytes(keys[i].toString()));
			bos.write(getBytes(getFieldSeparator()));
		}
		for (int j = 0; j < resultArr.length(); j++) {
			JSONObject accountObject = resultArr.getJSONObject(j);
			addRow(bos, accountObject, fields);
		}

	}

	private void addRow(ByteArrayOutputStream bos, JSONObject accountObject, LinkedHashMap<String, String> fields) {
		try {
			bos.write(getBytes(System.lineSeparator()));
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
						    || ("portfolioReturn".equals(key) && data.contains("+")))
					{
						data = data.replace("+", "");
						data = data.replace(data, "\"" + data + "\"");
						bos.write(getBytes(data));
						bos.write(getBytes(getFieldSeparator()));
					} else if (("unrealPLMkt".equals(key) && data.contains("-"))
							|| ("amount".equals(key) && data.contains("-"))
							|| ("percentageChange".equals(key) && data.contains("-"))
							|| ("benchMarkIndex".equals(key) && data.contains("-"))
							|| ("balance".equals(key) && data.contains("-"))
							|| ("marketValue".equals(key) && data.contains("-"))
							|| ("fees".equals(key) && data.contains("-"))
							|| ("portfolioReturn".equals(key) && data.contains("-")))
					{
						String bracs = "(";
						data = bracs.concat(data.replace("-", "").concat(")"));
						data = data.replace(data, "\"" + data + "\"");
						bos.write(getBytes(data));
						bos.write(getBytes(getFieldSeparator()));
					} else {
						data = data.replace(data, "\"" + data + "\"") ;
						bos.write(getBytes(data));
						bos.write(getBytes(getFieldSeparator()));
					}
				} else {
					bos.write(getBytes(""));
					bos.write(getBytes(getFieldSeparator()));
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error in generation of pdf", e).log();
		}

	}

	private void getPerformanceDetailsDiv(ByteArrayOutputStream bos, JSONObject performanceObj) throws IOException {
		Set<String> keys = performanceObj.keySet();
		for (String s : keys) {
			bos.write(getBytes(System.lineSeparator()));
			bos.write(getBytes(s.concat(": ")));
			bos.write(getBytes(getFieldSeparator()));
			String data = performanceObj.get(s).toString();
			if (s.equals("Fees & Taxes") && data.contains("+") || s.equals("Current Value") && data.contains("+")
					|| s.equals("Initial Value") && data.contains("+") || s.equals("Net Deposit") && data.contains("+")
					|| s.equals("P&L") && data.contains("+") || s.equals("Time-Weighted Return") && data.contains("+")
					|| s.equals("Money-Weighted Return") && data.contains("+")) {
				data = data.replace("+", "");
				data = data.contains(",") ? data.replace(data, "\"" + data + "\"") : data;
				bos.write(getBytes(data));
			} else if (s.equals("Fees & Taxes") && data.contains("-") || s.equals("Current Value") && data.contains("-")
					|| s.equals("Initial Value") && data.contains("-") || s.equals("Net Deposit") && data.contains("-")
					|| s.equals("P&L") && data.contains("-") || s.equals("Time-Weighted Return") && data.contains("-")
					|| s.equals("Money-Weighted Return") && data.contains("-")) {
				String bracs = "(";
				data = bracs.concat(data.replace("-", "").concat(")"));
				data = data.contains(",") ? data.replace(data, "\"" + data + "\"") : data;
				bos.write(getBytes(data));
			} else {
				data = data.contains(",") ? data.replace(data, "\"" + data + "\"") : data;
				bos.write(getBytes(data));
			}
		}
		bos.write(getBytes(System.lineSeparator()));
	}

	private void getNoData(ByteArrayOutputStream bos) throws IOException {
		String noRec = "No records found";
		bos.write(getBytes(noRec));
		bos.write(getBytes(System.lineSeparator()));

	}

	private void getCustomerDetailsDiv(ByteArrayOutputStream bos, customerDetails details, accountDetails acdet,
			String navPage) throws IOException {
		String[] keys = customerDetails.getKeys();
		String[] values = details.getValues();
		for (int i = 0; i < keys.length; i++) {
			if (values[i] != null) {
				bos.write(getBytes(keys[i]));
				bos.write(getBytes(getFieldSeparator()));
				bos.write(getBytes(values[i]));
				bos.write(getBytes(System.lineSeparator()));
			}
		}
		bos.write(getBytes(System.lineSeparator()));
		if (navPage.equals("Account Activity")) {
			String[] keysA = accountDetails.getKeys();
			String[] valuesA = acdet.getValues();
			for (int i = 0; i < keysA.length; i++) {
				bos.write(getBytes(keysA[i] + ":" + valuesA[i]));
				bos.write(getBytes(System.lineSeparator()));
			}
		}
	}

	private void getReportGenerateDiv(ByteArrayOutputStream bos, String navPage, String isEur) throws IOException {
		SimpleDateFormat sdf = (isEur.equals("") ? new SimpleDateFormat("MM/dd/yyyy")
				: new SimpleDateFormat("yyyy/MM/dd"));
		Calendar cal = Calendar.getInstance();
		String header = navPage.concat(" Report");
		String content = navPage.concat(" Report as on ").concat(sdf.format(cal.getTime()));
		bos.write(getBytes(header));
		bos.write(getBytes(System.lineSeparator()));
		bos.write(getBytes(content));
		bos.write(getBytes(System.lineSeparator()));
		bos.write(getBytes(System.lineSeparator()));
	}

	private byte[] getBytes(String str) {
		if (null == str) {
			str = "";
		}

		return str.getBytes();
	}

}
