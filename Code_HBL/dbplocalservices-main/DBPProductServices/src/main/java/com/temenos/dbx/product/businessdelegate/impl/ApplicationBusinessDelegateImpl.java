package com.temenos.dbx.product.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class ApplicationBusinessDelegateImpl implements ApplicationBusinessDelegate {

	@Override
	public ApplicationDTO getApplicationProperties(Map<String, Object> headersMap) throws ApplicationException {
		ApplicationDTO responseDTO = new ApplicationDTO();
		JsonObject response = new JsonObject();
		Map<String, Object> inputParams = new HashMap<>();
		try {
			response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap, URLConstants.APPLICATION_GET);
			for (JsonElement dto : response.get("application").getAsJsonArray()) {
				responseDTO = ((ApplicationDTO) DTOUtils.loadJsonObjectIntoObject(dto.getAsJsonObject(),
						ApplicationDTO.class, true));
			}
		} catch (Exception e) {
			return responseDTO;
		}
		return responseDTO;
	}

}
