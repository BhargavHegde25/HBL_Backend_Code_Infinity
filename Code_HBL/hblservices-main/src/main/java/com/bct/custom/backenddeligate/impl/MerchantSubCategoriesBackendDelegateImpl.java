package com.bct.custom.backenddeligate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.MerchantSubCategoriesBackendDelegate;
import com.bct.custom.constants.HBLURLConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MerchantSubCategoriesBackendDelegateImpl implements MerchantSubCategoriesBackendDelegate{
	private static final Logger logger = LogManager.getLogger(MerchantSubCategoriesBackendDelegateImpl.class);
	@Override
	public JSONArray getMerchantSubCategoriesForAdmin(String category, String subCategory,
			Map<String, Object> headerMap) throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "subcategoryof ne '" + subCategory + "' and type eq '"+category+"'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantSubCategoriesForAdminBackendDelegateImpl: getMerchantSubCategoriesForAdmin inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MASTER_MERCHANTS)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantSubCategoriesForAdminBackendDelegateImpl: getMerchantSubCategoriesForAdmin response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("mastermerchants");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant subcategories:" +e.toString());
			
		}
		return types;
	}

}
