package com.bct.custom.backenddeligate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.ManualMerchantsBackendDelagate;
import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;

public class ManualMerchantsBackendDelagateImpl implements ManualMerchantsBackendDelagate{

	private static final Logger logger = LogManager.getLogger(GetAllMerchantOperationsBackendDeligateImpl.class);
	@Override
	public JSONArray getAvailableMerchantsForCreate(String input, DataControllerRequest dcRequest) throws ApplicationException {
		
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "type eq 'APP'";
		if(StringUtils.isNotBlank(input)) {
			filter = "type eq '" + input + "'";
		}
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::ManualMerchantsBackendDelagateImpl:getAvailableMerchantsForCreate: inputmap:"+
				inputmap.toString());
		JSONArray dbCategories = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					//.withOperationId(HBLURLConstants.GET_ALL_MERCHANT_CATEGORIES_OPERATION)
					.withOperationId(HBLURLConstants.GET_MASTER_MERCHANTS)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(dcRequest.getHeaderMap()).build().getResponse();
			logger.debug("BCT::ManualMerchantsBackendDelagateImpl: getAvailableMerchantsForCreate response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			dbCategories = responseJSON.getJSONArray("mastermerchants");
			
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant categories:" +e.toString());
			
		}
		return dbCategories;
	}

}
