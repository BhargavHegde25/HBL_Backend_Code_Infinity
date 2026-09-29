package com.temenos.dbx.eum.product.usermanagement.resource.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerSecurityQuestionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.CustomerSecurityQuestionsResource;
import com.temenos.dbx.product.dto.DBXResult;

public class CustomerSecurityQuestionsResourceImpl implements CustomerSecurityQuestionsResource {

	@Override
	public Result get(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		return null;
	}

	@Override
	public Object getAreSecurityQuestionsConfigured(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) {
		DBXResult dbxResult = new DBXResult();

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result returnResult = new Result();

		String customerId = inputParams.get("Customer_id");

		CustomerSecurityQuestionsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerSecurityQuestionsBusinessDelegate.class);

		dbxResult = businessDelegate.getAreSecurityQuestionsConfigured(customerId, dcRequest.getHeaderMap());

		if(dbxResult.getResponse() != null) {
			returnResult.addParam(new Param("isSecurityQuestionConfigured", Boolean.toString((boolean)dbxResult.getResponse()),
					"string"));
		}
		
		return returnResult;
	}

}
