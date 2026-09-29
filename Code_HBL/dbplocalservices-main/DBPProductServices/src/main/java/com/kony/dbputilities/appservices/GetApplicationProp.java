package com.kony.dbputilities.appservices;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetApplicationProp implements JavaService2 {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Result appInfo = new Result();
		Result branchInfo = new Result();
		Map inputParams = HelperMethods.getInputParamMap(inputArray);
		appInfo = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.APPLICATION_GET);
		inputParams.put(DBPUtilitiesConstants.FILTER,
				DBPUtilitiesConstants.BRANCH_TYPE + DBPUtilitiesConstants.EQUAL + "MainBranch");
		branchInfo = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.BANK_BRANCH_GET);
		postProcess(result, appInfo, branchInfo);
		return result;
	}

	private void postProcess(Result result, Result appInfo, Result branchInfo) {
		Dataset ds = appInfo.getAllDatasets().get(0);
		Record reqRec = new Record();
		if (!ds.getAllRecords().isEmpty()) {
			reqRec = ds.getAllRecords().get(0);
		}
		postProcessBranchInfo(branchInfo, reqRec);
		if (ds.getAllRecords().isEmpty()) {
			ds.addRecord(reqRec);
		}
		result.addDataset(ds);
	}

	private void postProcessBranchInfo(Result branchInfo, Record reqRec) {
		if (HelperMethods.hasRecords(branchInfo)) {
			Dataset ds = branchInfo.getAllDatasets().get(0);
			Record temp = ds.getAllRecords().get(0);
			Param p = null;
			p = temp.getParam("Type");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_TYPE, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("address1");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_ADDRESS1, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("address2");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_ADDRESS2, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("city");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_CITY, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("latitude");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_LATITUDE, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("longitude");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_LONGTUDE, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("phone");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_PHONE, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("services");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_SERVICES, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("state");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_STATE, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("workingHours");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_WORKING_HOURS, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			p = temp.getParam("zipCode");
			reqRec.addParam(new Param(DBPUtilitiesConstants.MB_ZIPCODE, HelperMethods.getParamValue(p),
					DBPUtilitiesConstants.STRING_TYPE));
			reqRec.addParam(new Param(DBPUtilitiesConstants.IS_UPDATE, "false", "String"));
			reqRec.addParam(new Param(DBPUtilitiesConstants.IS_UPDATE_MANDT, "false", "String"));
		}
	}
}
