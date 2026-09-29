package com.kony.dbputilities.campaignservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class IgnoreCampaignForCustomer implements JavaService2 {

	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		try {
			Map inputParams = HelperMethods.getInputParamMap(inputArray);
			
			String customerId = (String) inputParams.get("customerId");
			String campaignId = (String) inputParams.get("campaignId");
			
			if(preProcess(inputParams, dcRequest, result) == null && !StringUtils.equalsAnyIgnoreCase(campaignId, DBPUtilitiesConstants.DEFAULT_CAMPAIGN)) {

				Map<String, String> input = new HashMap<>();
			
				input.put("customer_id", customerId);
				input.put("campaign_id", campaignId);
				input.put("createdby", customerId);
				JsonObject procResponse = HelperMethods.callApiJson(dcRequest, input, HelperMethods.getHeaders(dcRequest),
						URLConstants.CAMPAIGN_CUSTCOMPLETEDCAMPAIGN_CREATE);
				
				if (HelperMethods.hasErrorOpstatus(procResponse)) {
					return ErrorCodeEnum.ERR_12460.setErrorCode(result);
				}
			}
		} 
		catch (Exception e) {
			alert.prepareError("Exception occured in IgnoreCampaignForCustomer JAVA service. Error: ", e).log();
			ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Result preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		
		String customerId = (String) inputParams.get("customerId");
		String campaignId = (String) inputParams.get("campaignId");
		
		if (!StringUtils.isNotBlank(customerId)) {
			return ErrorCodeEnum.ERR_12458.setErrorCode(result);
		}
		
		if (!StringUtils.isNotBlank(campaignId)) {
			return ErrorCodeEnum.ERR_12459.setErrorCode(result);
		}
		return null;
	}
	

}