package com.kony.dbputilities.customerentitlement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

public class EntitlementsGetAddress implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final String INPUT_CUSTOMER_ID = "customerId";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		try {

			// Validate Service Inputs
			String customerId = requestInstance.getParameter(INPUT_CUSTOMER_ID);
			if (StringUtils.isBlank(customerId)) {
				ErrorCodeEnum.ERR_10688.setErrorCode(processedResult);
				return processedResult;
			}

			// Fetch address details
			Map<String, String> queryMap = new HashMap<>();
			queryMap.put(DBPUtilitiesConstants.FILTER, "CustomerId eq '" + customerId + "'");
			queryMap.put(DBPUtilitiesConstants.SELECT,
					"Address_id,AddressType,AddressLine1,AddressLine2,ZipCode,CityName,City_id,RegionName,Region_id,RegionCode,CountryName,Country_id,CountryCode,isPrimary");
			String serviceResponse = HelperMethods.callApiAndGetString(requestInstance, queryMap, null,
					URLConstants.CUSTOMERADDRESS_VIEW_READ);

			JSONObject serviceResponseJSON = HelperMethods.getStringAsJSONObject(serviceResponse);
			if (serviceResponseJSON == null || !serviceResponseJSON.has(DBPConstants.FABRIC_OPSTATUS_KEY)
					|| serviceResponseJSON.getInt(DBPConstants.FABRIC_OPSTATUS_KEY) != 0
					|| !serviceResponseJSON.has("customeraddress_view")) {
				alert.prepareError("Failed read operation on view customeraddress_view").log();
				ErrorCodeEnum.ERR_10689.setErrorCode(processedResult);
				return processedResult;
			}
			JSONArray addressDetails = serviceResponseJSON.optJSONArray("customeraddress_view");
			Dataset AddressDetailsArray = HelperMethods.constructDatasetFromJSONArray(addressDetails);
			AddressDetailsArray.setId("Addresses");
			processedResult.addDataset(AddressDetailsArray);

		} catch (Exception e) {
			alert.prepareError("Exception occurred while processing customer entitlements service ", e).log();
			ErrorCodeEnum.ERR_10700.setErrorCode(processedResult);
		}

		// Return Service Result
		return processedResult;
	}

}
