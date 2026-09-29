package com.kony.dbputilities.adsservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetDirectMarketingAds implements JavaService2 {
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.DM_AD_GET);
		}
		if (!HelperMethods.hasError(result)) {
			result = postProcess(dcRequest, result, inputParams);
		}

		return result;
	}

	@SuppressWarnings("rawtypes")
	private Result postProcess(DataControllerRequest dcRequest, Result result, Map inputParams)
			throws HttpCallException {
		List<Record> dmAdList = result.getAllDatasets().get(0).getAllRecords();
		if (!dmAdList.isEmpty()) {
			String desc = (String) inputParams.get("flowposition");
			for (Record dm : dmAdList) {
				if ("mobile_postlogin_ad_new".equalsIgnoreCase(desc)) {
					String id = HelperMethods.getParamValue(dm.getParam("id"));
					String filterQuery = DBPUtilitiesConstants.DM_ADD_ID + DBPUtilitiesConstants.EQUAL + id;
					Result temp = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
							URLConstants.DM_INTERACTION_GET);
					Dataset dmInteractionList = temp.getAllDatasets().get(0);
					dm.addDataset(dmInteractionList);
				}
			}
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
		String filter = "flowPosition" + DBPUtilitiesConstants.EQUAL + (String) inputParams.get("flowposition");
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
		String sortBy = (String) inputParams.get("sortBy");

		if (StringUtils.isBlank(sortBy)) {
			sortBy = "id";
		}

		inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy);
		return true;
	}
}
