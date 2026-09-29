package com.temenos.dbx.product.usermanagement.resource.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerAddressDTO;
import com.temenos.dbx.product.dto.CustomerAddressViewDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerSecurityQuestionsBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerAddressResource;
import com.temenos.dbx.product.usermanagement.resource.api.CustomerSecurityQuestionsResource;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerSecurityQuestionsResourceImpl implements CustomerSecurityQuestionsResource {

	@Override
	public Result get(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		// TODO Auto-generated method stub
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
