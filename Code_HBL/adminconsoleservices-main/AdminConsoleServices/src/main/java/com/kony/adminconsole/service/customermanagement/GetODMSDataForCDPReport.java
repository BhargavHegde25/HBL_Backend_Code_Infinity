package com.kony.adminconsole.service.customermanagement;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetODMSDataForCDPReport implements JavaService2 {
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		String entityItems = request.getParameter("entityItems").toString();
		JSONArray applicationDetails = new JSONArray();
		if ("failed".equalsIgnoreCase(entityItems)) {
			ErrorCodeEnum.ERR_22224.setErrorCode(result);
			return result;
		} else {
			applicationDetails = new JSONArray(entityItems.toString());
		}
		result.addParam("entityItems", applicationDetails.toString());
		result =  CommonUtilities.withSuccessParams(result);
		return result;
	}
}
