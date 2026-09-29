package com.temenos.dbx.product.resource.impl;

import java.util.Map;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.BankBusinessDelegate;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.resource.api.BankResource;

public class BankResourceImpl implements BankResource {


	@Override
	public Result getBankName(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String bankId = inputParams.get("Bank_id");

		BankBusinessDelegate  bankDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BankBusinessDelegate.class);
		DBXResult dbxResult = bankDelegate.getBankName(bankId, dcRequest.getHeaderMap());
		Result result = new Result();
		if(dbxResult.getResponse() != null) {
			result.addParam(new Param("bankName", (String) dbxResult.getResponse(), "String"));
		}
		return result;
	}
}
