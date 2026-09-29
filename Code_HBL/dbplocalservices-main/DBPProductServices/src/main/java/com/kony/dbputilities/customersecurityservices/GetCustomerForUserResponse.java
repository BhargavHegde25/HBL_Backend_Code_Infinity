package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerForUserResponse implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> requestParams = new HashMap<>();

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String Customer_id = inputParams.get("Customer_id");

		String filter = "id" + DBPUtilitiesConstants.EQUAL + Customer_id;
		String select = "areUserAlertsTurnedOn,areAccountStatementTermsAccepted,areDepositTermsAccepted,"
				+ "isP2PActivated, isP2PSupported,isBillPaySupported,isBillPayActivated,isWireTransferActivated,"
				+ "isWireTransferEligible,FirstName,LastName,Gender,IsPinSet,DateOfBirth,NoOfDependents,"
				+ "SpouseName,Ssn,CountryCode,UserImage,UserImageURL,isEagreementSigned,id,UserName,MaritalStatus_id,"
				+ "Lastlogintime,Bank_id,isVIPCustomer,isCombinedUser";

		requestParams.put(DBPUtilitiesConstants.SELECT, select);
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);

		result = HelperMethods.callApi(dcRequest, requestParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_GET);

		if (HelperMethods.hasRecords(result)) {
			return result;
		} else {
			ErrorCodeEnum.ERR_10003.setErrorCode(result);
			return result;
		}
	}

}
