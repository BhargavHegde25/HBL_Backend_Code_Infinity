package com.infinity.dbx.temenos.transactions;

import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_VALUE_TO_ACCOUNT_NAME;
import static com.infinity.dbx.temenos.transactions.TransactionConstants.TRANSFER_TO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.infinity.dbx.temenos.dto.TransactDate;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

/**
 * <p>
 * Post-Processor class for User Standing Orders
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class UserStandingOrderPostProcessor extends BasePostProcessor {

	private static final String TRANSACTIONS_ARRAY_KEY = "Transactions";

	private static Map<String, String> STANDING_ORDER_PRODUCT_MAP = new HashMap<>();
	static {
		STANDING_ORDER_PRODUCT_MAP.put(TransactionConstants.INATIONAL_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		STANDING_ORDER_PRODUCT_MAP.put(TransactionConstants.DOMESTIC_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		STANDING_ORDER_PRODUCT_MAP.put(TransactionConstants.INSTA_PAY_PRODUCT_ID,
				TransactionConstants.EXTERNAL_TRANSFER_PRODUCT_ID);
		STANDING_ORDER_PRODUCT_MAP.put(TransactionConstants.ACTRF_PRODUCT_ID,
				TransactionConstants.INTERNAL_TRANSFER_PRODUCT_ID);
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			TransactionConstants.TRANSACTION_DATE_FORMAT);

	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {

		try {
			diagnostic.prepareDebug("In " + UserStandingOrderPostProcessor.class.getName()).log();

			// Convert Result to JSON
			String serviceResponse = ResultToJSON.convert(result);
			JSONObject serviceResponseJSON = new JSONObject(serviceResponse);

			// Get Transact Date
			TransactDate transactDate = TransactionUtils.getTransactDate(request);

			// Compute Future Date
			Calendar calender = Calendar.getInstance();
			calender.setTime(transactDate.getCurrentWorkingDate());
			calender.add(Calendar.DAY_OF_MONTH, TransactionConstants.SCHEDULED_TRANSACTIONS_FETCH_WINDOW);
			Date standingOrderLimitDate = calender.getTime();

			Date currentTransactionDate;
			String currentTransactionDateStr, currentStandingOrderTransactionType, currentDescription, currentFrequency,
					currentFromAccountNumber;

			JSONArray resultArray = new JSONArray();
			JSONArray standingOrdersArray = serviceResponseJSON.optJSONArray(TRANSACTIONS_ARRAY_KEY);
			if(standingOrdersArray == null)
			{
				return TemenosUtils.getEmptyResult(TRANSACTIONS_ARRAY_KEY);
			}

			// Traverse Payment Orders
			for (Object currObject : standingOrdersArray) {
				if (currObject instanceof JSONObject) {
					JSONObject currentStandingOrderJSON = (JSONObject) currObject;
					currentTransactionDateStr = currentStandingOrderJSON
							.optString(TransactionConstants.FREQUENCY_START_DATE_KEY);
					if (StringUtils.isNotBlank(currentTransactionDateStr)) {
						try {
							currentTransactionDate = DATE_FORMATTER.parse(currentTransactionDateStr);
							if (currentTransactionDate.before(standingOrderLimitDate)) {
								// Add current JSON to the result JSON array
								resultArray.put(currentStandingOrderJSON);

								// Setting transaction Type
								currentStandingOrderTransactionType = currentStandingOrderJSON
										.optString(TransactionConstants.STANDING_ORDER_PRODUCT_NAME_KEY);
								if (StringUtils.isNotBlank(currentStandingOrderTransactionType)) {
									if (STANDING_ORDER_PRODUCT_MAP.containsKey(currentStandingOrderTransactionType)) {
										currentStandingOrderJSON.put(TransactionConstants.TRANSACTION_TYPE_KEY,
												STANDING_ORDER_PRODUCT_MAP.get(currentStandingOrderTransactionType));
										currentStandingOrderJSON
												.remove(TransactionConstants.STANDING_ORDER_PRODUCT_NAME_KEY);
									}
								}

								// Setting from Account Number
								currentFromAccountNumber = currentStandingOrderJSON
										.optString(Constants.PARAM_TRANSACTION_ID);
								if (StringUtils.isNotBlank(currentFromAccountNumber)) {
									try {
										currentFromAccountNumber = currentFromAccountNumber.substring(0,
												currentFromAccountNumber.indexOf("."));
									} catch (Exception e) {
										// Suppressed Exception
									}
								}
								currentStandingOrderJSON.put(Constants.PARAM_FROM_ACCOUNT_NUMBER,
										currentFromAccountNumber);

								// Seting currencyId
								if (currentStandingOrderJSON.has(TransactionConstants.CURRENCY_ID_KEY)) {
									currentStandingOrderJSON.put(TransactionConstants.TRANSACTION_CURRECNY,
											currentStandingOrderJSON.optString(TransactionConstants.CURRENCY_ID_KEY));
								}

								// Setting description based on toAccountName
								currentDescription = currentStandingOrderJSON.optString(PARAM_VALUE_TO_ACCOUNT_NAME);
								currentDescription = TRANSFER_TO + currentDescription;
								currentStandingOrderJSON.put(TransactionConstants.PARAM_DESCRIPTION,
										currentDescription);

								// Converting t24 frequency to dbx frequency
								currentFrequency = currentStandingOrderJSON.optString(Constants.PARAM_FREQUENCY_TYPE);
								currentStandingOrderJSON.put(Constants.PARAM_FREQUENCY_TYPE,
										TransactionUtils.convertT24FrequencyToDbxFrequencyType(currentFrequency));

							}
						} catch (ParseException e) {
							alert.prepareError("Parse Exception. Ignoring current scheduled transaction", e).log();
						}
					}
				}
			}
			serviceResponseJSON.remove(TRANSACTIONS_ARRAY_KEY);
			serviceResponseJSON.put(TRANSACTIONS_ARRAY_KEY, resultArray);

			// Convert JSON to Result
			result = JSONToResult.convert(serviceResponseJSON.toString());
			diagnostic.prepareDebug("Processed User Standing Order Response").log();

		} catch (Exception e) {
			alert.prepareError("Exception in " + UserStandingOrderPostProcessor.class.getName(), e).log();
			return  TemenosUtils.getEmptyResult(TRANSACTIONS_ARRAY_KEY);
		}
		return result;
	}
}