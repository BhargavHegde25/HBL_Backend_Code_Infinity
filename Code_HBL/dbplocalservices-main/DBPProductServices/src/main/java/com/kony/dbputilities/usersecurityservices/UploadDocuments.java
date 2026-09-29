package com.kony.dbputilities.usersecurityservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UploadDocuments implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.PERSONAL_INFO_UPDATE);
		}
		if (!HelperMethods.hasError(result)) {
			Param p = new Param("success", "You have successfully added User personal info",
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(p);
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = false;
		String docType = (String) inputParams.get(DBPInputConstants.DOC_TYPE);
		String doc = (String) inputParams.get(DBPInputConstants.DOC);
		if (StringUtils.isNotBlank(docType) && StringUtils.isNotBlank(doc)) {
			status = true;
			if ("Signature".equalsIgnoreCase(docType)) {
				inputParams.put(DBPUtilitiesConstants.SIG_IMG, doc);
			} else if ("Address".equalsIgnoreCase(docType)) {
				inputParams.put(DBPUtilitiesConstants.ADDRESS_DOC, doc);
			} else if ("Income".equalsIgnoreCase(docType)) {
				inputParams.put(DBPUtilitiesConstants.INCOME_DOC, doc);
			} else if ("Employment".equalsIgnoreCase(docType)) {
				inputParams.put(DBPUtilitiesConstants.EMP_DOC, doc);
			}
			inputParams.put(DBPUtilitiesConstants.P_ID, getPersonalInfoId(dcRequest));
		}

		return status;
	}

	private String getPersonalInfoId(DataControllerRequest dcRequest) throws HttpCallException {
		String userId = HelperMethods.getUserIdFromNUOSession(dcRequest);
		Result tempResult = HelperMethods.callGetApi(dcRequest, "newuser_id" + DBPUtilitiesConstants.EQUAL + userId,
				HelperMethods.getHeaders(dcRequest), URLConstants.PERSONAL_INFO_GET);
		List<Record> pInfo = tempResult.getAllDatasets().get(0).getAllRecords();
		if (!pInfo.isEmpty()) {
			Record exitInfo = pInfo.get(0);
			return HelperMethods.getFieldValue(exitInfo, DBPUtilitiesConstants.P_ID);
		}
		return null;
	}
}