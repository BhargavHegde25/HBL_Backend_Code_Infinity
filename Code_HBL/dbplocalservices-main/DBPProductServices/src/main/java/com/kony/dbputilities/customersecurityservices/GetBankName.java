package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetBankName implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		// Require Input Bank_id
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String bankId = inputParams.get("Bank_id");
		String bankName = getBankName(dcRequest, bankId);
		result.addParam(new Param("bankName", bankName, "String"));
		return result;
	}

	private String getBankName(DataControllerRequest dcRequest, String bankId) throws HttpCallException {
		String filter = "";
		if (StringUtils.isNotBlank(bankId)) {
			filter = "id" + DBPUtilitiesConstants.EQUAL + bankId;
		}
		Result bank = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BANK_GET);
		return HelperMethods.getFieldValue(bank, "Description");
	}

}
