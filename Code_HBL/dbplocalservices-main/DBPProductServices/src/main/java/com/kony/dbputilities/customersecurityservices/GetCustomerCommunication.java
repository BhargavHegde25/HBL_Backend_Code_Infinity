package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerCommunication implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERCOMMUNICATIONVIEW_GET);
		}
		return postProcess(dcRequest, result);
	}

	private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		Result retResult = new Result();
		Record usrAttr = new Record();
		if (HelperMethods.hasRecords(result)) {
			usrAttr = result.getAllDatasets().get(0).getRecord(0);
			usrAttr.setId("customer");
			HelperMethods.setSuccessMsg(DBPUtilitiesConstants.RECORD_FOUND_IN_DBX, retResult);
		} else if (HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_10020.setErrorCode(retResult, HelperMethods.getError(result));

		} else {
			usrAttr.setId("customer");
		}
		retResult.addRecord(usrAttr);
		return retResult;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
		String userName = inputParams.get("UserName");
		StringBuilder sb = new StringBuilder();
		sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(userName);
		if (StringUtils.isBlank(userName)) {
			return false;
		}
		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		return true;
	}
}