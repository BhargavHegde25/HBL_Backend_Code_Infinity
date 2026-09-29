/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DownloadAttachmentsPDFBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.CSVGenerator;
import com.temenos.infinity.api.wealthservices.util.ExcelGenerator;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.accountDetails;
import com.temenos.infinity.api.wealthservices.util.PDFGenerator.customerDetails;
import com.temenos.infinity.wealth.common.util.UserAttributesUtil;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

/**
 * @author himaja.sridhar
 *
 */
public class DownloadAttachmentsPDFBackendDelegateImpl implements DownloadAttachmentsPDFBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	String coreValue = "";
	int columnCount;
	LinkedHashMap<String, String> fields = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> currency = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> checkCurr = new LinkedHashMap<String, String>();

	@SuppressWarnings("unchecked")
	@Override
	public byte[] generatePDF(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object accountIdObj = inputParams.get(TemenosConstants.ACCID);
		Object currencyIdObj = inputParams.get(TemenosConstants.CURRENCYID);
		Object isEurObj = inputParams.get(TemenosConstants.ISEURO);
		Object formatObj = inputParams.get(TemenosConstants.DOWNLOADFORMAT);
		String portfolioId = null;
		String navPage = null;
		String accountId = null;
		String currencyId = null;
		String isEur = null;
		String downloadFormat = null;
		if (inputParams.get(TemenosConstants.PORTFOLIOID) != null
				&& inputParams.get(TemenosConstants.PORTFOLIOID).toString().trim().length() > 0 || formatObj == null || formatObj.equals("")) {
			portfolioId = inputParams.get(TemenosConstants.PORTFOLIOID).toString();
			inputMap.put(TemenosConstants.PORTFOLIOID, portfolioId);
		} else {
			String str = "Invalid Input! portfolioId is mandatory.";
			byte[] b = str.getBytes();
			String s = new String(b);
			return s.getBytes();
		}

		if (inputParams.get(TemenosConstants.NAVPAGE) != null
				&& inputParams.get(TemenosConstants.NAVPAGE).toString().trim().length() > 0) {
			navPage = inputParams.get(TemenosConstants.NAVPAGE).toString();
			inputMap.put(TemenosConstants.NAVPAGE, navPage);
		} else {
			PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.NAVPAGE);
		}

		if (accountIdObj != null) {
			accountId = inputParams.get(TemenosConstants.ACCID).toString();
			inputMap.put(TemenosConstants.ACCID, accountId);
		} else {
			accountId = "";
			inputMap.put(TemenosConstants.ACCID, accountId);
		}
		if (currencyIdObj != null) {
			currencyId = inputParams.get(TemenosConstants.CURRENCYID).toString();
			inputMap.put(TemenosConstants.CURRENCYID, currencyId);
		} else {
			currencyId = "";
			inputMap.put(TemenosConstants.CURRENCYID, currencyId);
		}
		if (isEurObj != null) {
			isEur = inputParams.get(TemenosConstants.ISEURO).toString();
			if (isEur.equals("true")) {
				inputMap.put(TemenosConstants.ISEURO, isEur);
			} else {
				isEur = "";
				inputMap.put(TemenosConstants.ISEURO, isEur);
			}
		} else {
			isEur = "";
			inputMap.put(TemenosConstants.ISEURO, isEur);
		}
		if (formatObj != null) {
			downloadFormat = inputParams.get(TemenosConstants.DOWNLOADFORMAT).toString();
			inputMap.put(TemenosConstants.DOWNLOADFORMAT, downloadFormat);
		}

		PDFGenerator pdfGenerator = new PDFGenerator();
		ExcelGenerator excelGenerator = new ExcelGenerator();
		CSVGenerator csvGenerator = new CSVGenerator();
		byte[] bytes = null;
		try {
			String fName=null;
			try {
				fName = UserAttributesUtil.getCustomerFirstName(request);
			} catch (Exception e) {
				alert.prepareError("Error while fetching firstName - " + e.getMessage()).log();
			}
			String FirstName = fName;
			//String FirstName = PortfolioWealthUtils.getUserAttributeFromIdentity(request, "FirstName").concat(" ");
			//String lastName = PortfolioWealthUtils.getUserAttributeFromIdentity(request, "LastName");
			String lName=null;
			try {
				lName = UserAttributesUtil.getCustomerLastName(request);
			} catch (Exception e) {
				alert.prepareError("Error while fetching LastName - " + e.getMessage()).log();
			}
			String lastName = lName;
			List<String> allportfoliosList = PortfolioWealthUtils.getAllPortfoliosFromCache(request);
			if (allportfoliosList.contains(portfolioId)) {
				JSONArray jsonResult = getResultObject(methodID, inputArray, request, response, headersMap, navPage,
						portfolioId, isEur);
				JSONArray resultArr = jsonResult;
				if(downloadFormat.equalsIgnoreCase("pdf")) {
				bytes = pdfGenerator.generateFile(new customerDetails(FirstName.concat(lastName), portfolioId),
						new accountDetails(accountId, currencyId), resultArr, columnCount, fields, navPage, isEur, null);
				}
				else if (downloadFormat.equalsIgnoreCase("xlsx")) {
					bytes = excelGenerator.generateFile(new customerDetails(FirstName.concat(lastName), portfolioId),
							new accountDetails(accountId, currencyId), resultArr, columnCount, fields, navPage, isEur);
				}
				else if (downloadFormat.equalsIgnoreCase("csv")) {
					bytes = csvGenerator.generateFile(new customerDetails(FirstName.concat(lastName), portfolioId),
							new accountDetails(accountId, currencyId), resultArr, columnCount, fields, navPage, isEur);
				}
				return bytes;
			} else {
				String errStr = "Portfolio ID  does not exist for the Customer";
				alert.prepareError(errStr).log();
				return errStr.getBytes();

			}

		} catch (IOException e) {
			alert.prepareError("Error while invoking Transact - " + WealthAPIServices.WEALTH_GENERATEPDF.getOperationName()
					+ "  : " + e).log();
			return null;
		}

	}

	@SuppressWarnings("unused")
	private JSONArray getResultObject(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap, String navPage, String portfolioId,
			String isEur) {
		Result serviceResult = new Result();
		JSONArray accListArr = new JSONArray();
		JSONArray jsonArrayList = new JSONArray();
		SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy");
		String replaceVal = ",", replaceWith = ".";
		if (isEur.equalsIgnoreCase("true")) {
			replaceVal = ".";
			replaceWith = ",";
			sdformat = new SimpleDateFormat("dd/MM/yyyy");
		}
		//NumberFormat formatter = new DecimalFormat("#0.00");

		if (navPage.equalsIgnoreCase("InstrumentTransactions")) {
			columnCount = 8;
			fields.clear();
			checkCurr = getCurrency();

			String[] fieldName = new String[] { "tradeDate", "description", "orderType", "quantity", "limitPrice",
					"instrumentAmount", "fees", "total" };
			String[] columnName = new String[] { "Trade Date", "Instrument", "OrderType", "Quantity", "Price", "Amount",
					"Fees", "Total Amount" };
			DecimalFormat df = new DecimalFormat("#.##");
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				TransactionsListBackendDelegateImpl transactionsListBackendDelegateImpl = new TransactionsListBackendDelegateImpl();
				serviceResult = transactionsListBackendDelegateImpl.viewInstrumentTransactions(methodID, inputArray,
						request, response, headersMap);

				List<Dataset> dataset = serviceResult.getAllDatasets();
				List<Record> drecords = dataset.get(0).getAllRecords();
				for (int j = 0; j < drecords.size(); j++) {
					JSONObject actListObj = new JSONObject();
					Record drecord = drecords.get(j);
					actListObj = CommonUtils.convertRecordToJSONObject(drecord); // secCCy
					String secCurr = actListObj.get("instrumentCurrency").toString();
					String refCurr = actListObj.get("referenceCurrency").toString();
					String feesCurr = actListObj.get("feesCurrency").toString();

					String curr = checkCurr.get(secCurr) == null ? secCurr.toUpperCase() :checkCurr.get(secCurr).toString();
					String refcurr = checkCurr.get(refCurr) == null ? refCurr.toUpperCase() :checkCurr.get(refCurr).toString();
					String feescurr = checkCurr.get(feesCurr) == null ? feesCurr.toUpperCase()
							: checkCurr.get(feesCurr).toString();
					
					actListObj.put("limitPrice",
							curr.concat(correctDecimaPlaces(actListObj.get("limitPrice").toString(),isEur)));
					actListObj.put("instrumentAmount", refcurr.concat(correctDecimaPlaces(actListObj.get("instrumentAmount").toString(),isEur)));
					actListObj.put("total", refcurr.concat(correctDecimaPlaces(actListObj.get("total").toString(),isEur)));
					actListObj.put("fees",feescurr.concat(correctDecimaPlaces(actListObj.get("fees").toString(),isEur)));
					actListObj.put("tradeDate", sdformat
							.format(new SimpleDateFormat("yyyy-MM-dd").parse(actListObj.get("tradeDate").toString())));
					actListObj.remove("valueDate");
					actListObj.remove("ISIN");
					actListObj.remove("transactionId");
					actListObj.remove("exchangeRate");
					actListObj.remove("RICCode");
					actListObj.remove("holdingsType");
					actListObj.remove("netAmount");
					accListArr.put(actListObj);
				}

				return accListArr;
			}
			catch (Exception e) {
				alert.prepareError("Error while invoking Transact - "
						+ WealthAPIServices.WEALTH_VIEWINSTRUMENTTRANSACTIONS.getOperationName() + "  : " + e).log();
				return null;
			}
		}
		if (navPage.equalsIgnoreCase("Watchlist")) {
			columnCount = 10;
			fields.clear();
			checkCurr = getCurrency();
			String[] fieldName = new String[] { "instrumentName", "ISINCode", "exchange", "referenceCurrency", "lastRate", "percentageChange",
					"formatDateTime", "bidRate", "askRate", "volume" };
			String[] columnName = new String[] { "InstrumentName", "ISIN", "Exchange", "Currency", "Latest", "Change",
					"DateTime", "Bid", "Ask", "Volume" };
			for (int k = 0; k < fieldName.length; k++) {
				fields.put(columnName[k], fieldName[k]);
			}
			try {
				FavoriteInstrumentsBackendDelegateImpl watchlistBackendDelegate = new FavoriteInstrumentsBackendDelegateImpl();
				serviceResult = watchlistBackendDelegate.getFavoriteInstruments(methodID, inputArray, request, response, headersMap);
					List<Dataset> dataset = serviceResult.getAllDatasets();
					List<Record> drecords = dataset.get(0).getAllRecords();
					for (int j = 0; j < drecords.size(); j++) {
						JSONObject watchlistObj = new JSONObject();
						Record drecord = drecords.get(j);
						watchlistObj = CommonUtils.convertRecordToJSONObject(drecord);
						String refCurr = watchlistObj.get("referenceCurrency").toString();
						String refcurr = checkCurr.get(refCurr) == null ? refCurr.toUpperCase() :checkCurr.get(refCurr).toString();
						watchlistObj.put("lastRate",
								watchlistObj.get("lastRate").toString().isEmpty() ? "" :refcurr.concat(correctDecimaPlaces(watchlistObj.get("lastRate").toString(),isEur)));
						watchlistObj.put("bidRate",
								watchlistObj.get("bidRate").toString().isEmpty() ? "" :refcurr.concat(correctDecimaPlaces(watchlistObj.get("bidRate").toString(),isEur)));
						watchlistObj.put("askRate",
								watchlistObj.get("askRate").toString().isEmpty() ? "" :refcurr.concat(correctDecimaPlaces(watchlistObj.get("askRate").toString(),isEur)));
						accListArr.put(watchlistObj);
					}
				
				return accListArr;
			} catch (Exception e) {
				alert.prepareError("Error while invoking - "
						+ WealthAPIServices.GET_FAVOURITE_INSTRUMENT.getOperationName() + "  : " + e).log();
				return null;
			}
			
		}
		return null;
	}

	private LinkedHashMap<String, String> getCurrency() {
		currency.clear();
		String[] currencyAbb = new String[] { "USD", "EUR", "CRC", "GBP", "ILS", "INR", "JPY", "KRW", "NGN", "PHP",
				"PLN", "PYG", "THB", "UAH", "VND", "AUD", "CAD", "CHF", "SGD" };
		String[] currencySymb = new String[] { "$", "€", "₡", "£", "₪", "INR ", "¥", "₩", "₦", "₱", "zł", "₲", "฿", "₴",
				"₫", "$", "$", "Fr.", "SGD" };
		for (int k = 0; k < currencyAbb.length; k++) {
			currency.put(currencyAbb[k], currencySymb[k]);
		}
		return currency;

	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String correctDecimaPlaces(String field, String isEur) throws NumberFormatException, ParseException {
		field = field.replace(",", "");
		String value = "";
		String value1 = "";
		if (field.equalsIgnoreCase("0.0")) {
			return field;
		} else if (field.equalsIgnoreCase("")) {
			return field;
		}
			else {
			if (isEur.equalsIgnoreCase("true")) {
				Locale ukLocale = new Locale("en", "DE");
				NumberFormat numberFormat = NumberFormat.getInstance(ukLocale);
				Double ukVal = Double.parseDouble(field);
				String val = String.format("%.2f", ukVal);
				Double ukkVal = Double.parseDouble(val);
				value = numberFormat.format(ukkVal);
		        int iP = value.indexOf(",");
		        int dP =  value.length() -  iP -1;
		        if (value.contains(",")){
		        	if (dP==1) {
		        	value1 = value.concat("0");
		        } else if (dP > 1) {
		        	value1 = value;
		        }
		      } else {
		        	value1 = value.concat(",00");
		        }
				return value1;

			} else {
				Locale usLocale = new Locale("en", "US");
				NumberFormat numberFormat = NumberFormat.getInstance(usLocale);
				Double usVal = Double.parseDouble(field);
//				NumberFormat formatter = new DecimalFormat("#0.00");
//			    value = formatter.format(usVal);
				value = numberFormat.format(Double.parseDouble(String.format("%.2f", usVal)));	
//				Double uVal = Double.parseDouble(value);
//				value1 = formatter.format(uVal);
				int iP = value.indexOf(".");
		        int dP =  value.length() -  iP -1;
		        if (value.contains(".")){
		        	if (dP==1) {
		        	value1 = value.concat("0");
		        } else if (dP > 1) {
		        	value1 = value;
		        }
		      } else {
		        	value1 = value.concat(".00");
		        }
				return value1;
			}
		}
	}

}
