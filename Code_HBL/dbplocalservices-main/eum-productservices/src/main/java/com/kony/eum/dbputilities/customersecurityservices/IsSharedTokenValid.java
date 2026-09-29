package com.kony.eum.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class IsSharedTokenValid implements JavaService2 {
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String sessionToken = inputParams.get("session_token");
		if(AdminUtil.verifyCSRAssistToken(dcRequest, sessionToken)) {
			result.addParam("Result", "true");
		}
		else {
			result.addParam("Result", "false");
			ErrorCodeEnum.ERR_10221.setErrorCode(result);
			result.addParam(DBPConstants.DBP_ERROR_MESSAGE_KEY, dcRequest.getAttribute("CSR_Response"));
			if(StringUtils.isNotBlank(dcRequest.getAttribute("CSR_ResponseCode"))) {
				result.addParam(DBPConstants.DBP_ERROR_CODE_KEY, dcRequest.getAttribute("CSR_ResponseCode"));
			}
		}
		return result;
	}

}
