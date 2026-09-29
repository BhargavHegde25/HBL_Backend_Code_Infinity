package com.kony.dbputilities.productservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetProductList implements JavaService2 {

	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);

		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.PRODUCT_GET);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		boolean status = false;

		String stateId = (String) inputParams.get(DBPUtilitiesConstants.STATE_ID);
		String accntType = (String) inputParams.get(DBPUtilitiesConstants.ACNT_TYPE);

		if (StringUtils.isNotBlank(stateId) && StringUtils.isNotBlank(accntType)) {
			String filter = DBPUtilitiesConstants.STATE_ID + DBPUtilitiesConstants.EQUAL + stateId
					+ DBPUtilitiesConstants.AND + DBPUtilitiesConstants.ACNT_TYPE + DBPUtilitiesConstants.EQUAL
					+ accntType;
			status = true;
			inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		}

		return status;
	}
}
