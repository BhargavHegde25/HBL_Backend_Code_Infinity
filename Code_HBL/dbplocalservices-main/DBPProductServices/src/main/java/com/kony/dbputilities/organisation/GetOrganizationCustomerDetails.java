package com.kony.dbputilities.organisation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganizationCustomerDetails implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMER_GET);
		}

		result = postProcess(inputParams, result, dcRequest);

		return result;
	}

	private Result postProcess(Map<String, String> inputParams, Result result, DataControllerRequest dcRequest)
			throws HttpCallException {

		Result retResult = new Result();
		if (HelperMethods.hasRecords(result)) {
			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.USER_EXISTS_IN_DBX, ErrorCodes.RECORD_FOUND,
					retResult);
			Map<String, String> map = HelperMethods.getCommunicationInfo(inputParams.get("id"), dcRequest);
			Record record = HelperMethods.getRecord(result);
			record.setId("DbxUser");
			for (String key : map.keySet()) {
				record.addParam(new Param(key, map.get(key), "String"));
			}
			record.addParam(new Param("Status",
					HelperMethods.getStatusMap().get(HelperMethods.getFieldValue(record, "Status_id")), "String"));
			retResult.addRecord(record);
		} else if (HelperMethods.hasError(result)) {
			HelperMethods.setValidationMsgwithCode(HelperMethods.getError(result), ErrorCodes.ERROR_SEARCHING_RECORD,
					retResult);
		} else {
			HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.USER_NOT_EXISTS_IN_DBX,
					ErrorCodes.RECORD_NOT_FOUND, retResult);
		}

		return retResult;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest)
			throws HttpCallException {
		Result result = new Result();
		String id = HelperMethods.getCustomerIdFromSession(dcRequest);
		String orgId = HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest);
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(orgId)) {
			sb.append("id").append(DBPUtilitiesConstants.EQUAL).append(orgId);
			result = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATION_GET);
			if (HelperMethods.hasRecords(result)) {
				id = inputParams.get("id");
			}
		} else {
			id = inputParams.get("id");
		}

		if (StringUtils.isNotBlank(id)) {
			sb = new StringBuilder();
			sb.append("id").append(DBPUtilitiesConstants.EQUAL).append(id);
			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
			return true;
		}
		return false;
	}
}
