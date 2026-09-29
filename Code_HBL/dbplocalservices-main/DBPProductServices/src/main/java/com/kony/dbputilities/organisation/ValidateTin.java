package com.kony.dbputilities.organisation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ValidateTin implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		if (preProcess(inputParams, dcRequest, result)) {
			result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
					URLConstants.MEMBERSHIP_ACCOUNTS_GET);
			Result retResult = new Result();
			retResult = postProcess(result);
			return retResult;
		}
		return result;
	}

	private Result postProcess(Result result) {
		Dataset dataset = HelperMethods.getDataSet(result);
		dataset.setId(DBPUtilitiesConstants.ORGMEM_ATTR);
		Result retResult = new Result();
		if (HelperMethods.hasRecords(result)) {
			HelperMethods.setSuccessMsg("Tin id exists", retResult);
			Param param = new Param("isExists", "true", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retResult.addParam(param);
		} else if (HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_11015.setErrorCode(retResult, HelperMethods.getError(result));
		} else {
			Param param = new Param("isExists", "false", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			retResult.addParam(param);
		}
		retResult.addDataset(dataset);
		return retResult;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		boolean status = true;
		String tin = inputParams.get("Tin");
		String filterKey = "";
		String filtervalue = "";
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(tin)) {
			filterKey = "Taxid";
			filtervalue = tin;
		} else {
			Record record = new Record();
			record.setId(DBPUtilitiesConstants.ORGMEM_ATTR);
			ErrorCodeEnum.ERR_11014.setErrorCode(record);
			result.addRecord(record);
			status = false;
		}
		if (status) {
			sb.append(filterKey).append(DBPUtilitiesConstants.EQUAL).append(filtervalue);
			inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		}
		return status;
	}
}
