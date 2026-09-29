package com.kony.dbputilities.customerentitlement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class EntitlementsGetContactInfomation implements JavaService2 {

	private static final String CONTACT_VALUE_SEPERATOR = "-";
	private static final String COMM_TYPE_EMAIL = "COMM_TYPE_EMAIL";
	private static final String COMM_TYPE_PHONE = "COMM_TYPE_PHONE";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String INPUT_CUSTOMER_ID = "customerId";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();
		String customerId = requestInstance.getParameter(INPUT_CUSTOMER_ID);

		if (StringUtils.isBlank(customerId)) {
			ErrorCodeEnum.ERR_10688.setErrorCode(processedResult);
			return processedResult;
		}

		try {
			// Fetch contact information
			return readCustomerMobileAndEmailInfo(customerId, processedResult, requestInstance);

		} catch (Exception e) {
			alert.prepareError("Exception occurred while processing customer entitlements service ", e).log();
			ErrorCodeEnum.ERR_10700.setErrorCode(processedResult);
			return processedResult;
		}
	}

	private Result readCustomerMobileAndEmailInfo(String customerID, Result communicationResult,
			DataControllerRequest requestInstance) throws HttpCallException {

		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq '" + customerID + "' and (Type_id eq '"
				+ COMM_TYPE_EMAIL + "' or Type_id eq '" + COMM_TYPE_PHONE + "')");
		queryMap.put(DBPUtilitiesConstants.SELECT, "id,Type_id,isPrimary,Value,Extension,Description");

		String readEndpointResponse = HelperMethods.callApiAndGetString(requestInstance, queryMap, null,
				URLConstants.CUSTOMERCOMMUNICATION_READ);
		JSONObject readEndpointResponseJSON = new JSONObject(readEndpointResponse);
		if (readEndpointResponseJSON != null && readEndpointResponseJSON.has(DBPConstants.FABRIC_OPSTATUS_KEY)
				&& readEndpointResponseJSON.optInt(DBPConstants.FABRIC_OPSTATUS_KEY) == 0
				&& readEndpointResponseJSON.has("customercommunication")) {
			JSONArray emailArray = new JSONArray();
			JSONArray phoneArray = new JSONArray();
			JSONArray communicationInfoArray = readEndpointResponseJSON.getJSONArray("customercommunication");
			for (Object communicationJSONObject : communicationInfoArray) {
				JSONObject communicationInfoJSONObject = (JSONObject) communicationJSONObject;
				if (communicationInfoJSONObject.getString("Type_id").equalsIgnoreCase(COMM_TYPE_EMAIL)) {
					emailArray.put(communicationInfoJSONObject);
				} else {
					phoneArray.put(communicationInfoJSONObject);
				}
			}
			// E-mail addresses
			Dataset emailIdsArrayDataset = HelperMethods.constructDatasetFromJSONArray(emailArray);
			emailIdsArrayDataset.setId("EmailIds");
			communicationResult.addDataset(emailIdsArrayDataset);

			// Phone numbers
			processMobileNumbers(phoneArray);
			Dataset phoneArrayDataset = HelperMethods.constructDatasetFromJSONArray(phoneArray);
			phoneArrayDataset.setId("ContactNumbers");
			communicationResult.addDataset(phoneArrayDataset);
		} else {
			ErrorCodeEnum.ERR_10690.setErrorCode(communicationResult);
		}
		return communicationResult;
	}

	public static void processMobileNumbers(JSONArray customerCommunicationArray) {
		String currValue;
		String currValueArray[];
		String phoneNumber, extension, countryCode;
		for (int indexVar = 0; indexVar < customerCommunicationArray.length(); indexVar++) {
			JSONObject currRecord = customerCommunicationArray.optJSONObject(indexVar);
			currValue = currRecord.optString("Value");
			if (StringUtils.isNotBlank(currValue)) {
				currValueArray = StringUtils.split(currValue, CONTACT_VALUE_SEPERATOR);
				if (currValueArray != null && currValueArray.length >= 2) {
					countryCode = currValueArray[0];
					currRecord.put("phoneCountryCode", countryCode);
					phoneNumber = currValueArray[1];
					currRecord.put("phoneNumber", phoneNumber);
					if (currValueArray.length == 3) {
						extension = currValueArray[2];
						currRecord.put("phoneExtension", extension);
					}
				} else {
					currRecord.put("phoneNumber", currValue);
					currRecord.put("phoneCountryCode", "");
				}

			}
		}
	}
}
