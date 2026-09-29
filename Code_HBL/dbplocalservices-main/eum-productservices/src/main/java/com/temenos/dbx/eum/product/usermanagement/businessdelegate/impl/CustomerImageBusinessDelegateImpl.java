package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.dto.CustomerImageDTO;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerImageBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.CustomerImageBusinessDelegateImpl;

public class CustomerImageBusinessDelegateImpl implements CustomerImageBusinessDelegate {
	private static LoggerUtil logger;
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public JsonObject getCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap) {

		logger = new LoggerUtil(CustomerImageBusinessDelegateImpl.class);
		if (customerImageDTO == null)
			return null;
		JsonObject customerImage = new JsonObject();
		Map<String, Object> inputParams = new HashMap<>();
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerImageDTO.getCustomer_id() + DBPUtilitiesConstants.AND + "legalEntityId"
                + DBPUtilitiesConstants.EQUAL + customerImageDTO.getLegalEntityId();
		inputParams.put("$filter", filter);
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams,
				HelperMethods.convertToObjectMap(headersMap), URLConstants.CUSTOMERIMAGE_GET,
				headersMap.get("X-Kony-Authorization"));

		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("customerimage")) {
			return null;
		}

		if (response.get("customerimage").getAsJsonArray().size() == 0) {
			return customerImage;
		}
		List<CustomerImageDTO> customerImageDTOList = new ArrayList<>();
		try {
			customerImageDTOList = JSONUtils.parseAsList(response.get("customerimage").getAsJsonArray().toString(),
					CustomerImageDTO.class);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		customerImage=response.get("customerimage").getAsJsonArray().get(0).getAsJsonObject();
		return customerImage;
	}

	@Override
	public boolean deleteCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap) {
		logger = new LoggerUtil(CustomerImageBusinessDelegateImpl.class);
		if (customerImageDTO == null)
			return false;

		Map<String, Object> inputParams = new HashMap<>();
		
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerImageDTO.getCustomer_id() + DBPUtilitiesConstants.AND + "legalEntityId"
                + DBPUtilitiesConstants.EQUAL + customerImageDTO.getLegalEntityId();
		inputParams.put("$filter", filter);
		
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams,
				HelperMethods.convertToObjectMap(headersMap), URLConstants.CUSTOMERIMAGE_DELETE,
				headersMap.get("X-Kony-Authorization"));
		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("deletedRecords")) {
			return false;
		}
		return true;
	}

	@Override
	public boolean createCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap) {
		logger = new LoggerUtil(CustomerImageBusinessDelegateImpl.class);
		if (customerImageDTO == null)
			return false;

		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("Customer_id", customerImageDTO.getCustomer_id());
		inputParams.put("legalEntityId", customerImageDTO.getLegalEntityId());
		inputParams.put("UserImage", customerImageDTO.getUserImage());
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams,
				HelperMethods.convertToObjectMap(headersMap), URLConstants.CUSTOMERIMAGE_CREATE,
				headersMap.get("X-Kony-Authorization"));

		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("customerimage")) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateCustomerImage(CustomerImageDTO customerImageDTO, Map<String, String> headersMap) {
		logger = new LoggerUtil(CustomerImageBusinessDelegateImpl.class);
		if (customerImageDTO == null)
			return false;
        
		Map<String, Object> inputParams = new HashMap<>();
		inputParams.put("id", customerImageDTO.getid());
		inputParams.put("UserImage", customerImageDTO.getUserImage());
		JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams,
				HelperMethods.convertToObjectMap(headersMap), URLConstants.CUSTOMERIMAGE_UPDATE,
				headersMap.get("X-Kony-Authorization"));
		if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("updatedRecords")) {
			return false;
		}
		return true;
	}

}
