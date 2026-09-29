package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateCustomerProfile implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		inputParams.put("doNotSendOTP", "true");

		String personalInformation = dcRequest.getParameter("personalInformation");
		if (StringUtils.isNotBlank(personalInformation)) {
			HashMap<String, String> hashMap = HelperMethods.getRecordMap(personalInformation);
			inputParams.put("UserName", hashMap.get("userName"));
		}

		Result result1 = (Result) new VerifyDBXUserName().invoke(methodID, inputArray, dcRequest, dcResponse);
		Record record = result1.getRecordById(DBPUtilitiesConstants.USR_ATTR);

		if ((record != null)
				&& (HelperMethods.getFieldValue(record, DBPUtilitiesConstants.IS_USERNAME_EXISTS).equals("true"))) {
			ErrorCodeEnum.ERR_10181.setErrorCode(result);
			return result;
		}

		result = (Result) new com.kony.dbputilities.customersecurityservices.CreateApplicant().invoke(methodID,
				inputArray, dcRequest, dcResponse);
		if (result.getParamByName(DBPUtilitiesConstants.ERROR_CODE) != null && result
				.getParamByName(DBPUtilitiesConstants.ERROR_CODE).getValue().equals(ErrorCodes.RECORD_CREATED)) {
			String customerId = result.getParamByName("applicantID").getValue();
			result.removeParamByName("applicantID");
			result.removeParamByName(DBPUtilitiesConstants.ERROR_CODE);
			result.addParam(new Param("Customer_id", customerId, "String"));
		} else {
			ErrorCodeEnum.ERR_10182.setErrorCode(result);
		}
		return result;
	}
}
