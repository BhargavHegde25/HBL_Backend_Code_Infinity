package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;
import java.util.Map;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AddAccountPostProcessor extends BasePostProcessor implements AccountsConstants, Constants{

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		String productType = request.getParameter(PRODUCT_TYPE);
		String toAccountId = CommonUtils.getParamValue(result, ACCOUNTID); 
		if(!"".equalsIgnoreCase(toAccountId) && ACCOUNT_TYPE_SAVINGS.equalsIgnoreCase(productType)) {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			Map<String, String> serverProperties = paramHelper.getAllServerProperties();
			String fromAccountId = CommonUtils.getPropertyValue(serverProperties, T24_FROMACCOUNT);
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			HashMap<String, Object> headerParams = new HashMap<String, Object>();

			inputParams.put(PARAM_TO_ACCOUNT_NUMBER, toAccountId);
			request.addRequestParam_(PARAM_TO_ACCOUNT_NUMBER, toAccountId);
			inputParams.put(PARAM_FROM_ACCOUNT_NUMBER, fromAccountId);
			request.addRequestParam_(PARAM_FROM_ACCOUNT_NUMBER, fromAccountId);
			inputParams.put(PARAM_AMOUNT, "100");
			request.addRequestParam_(PARAM_AMOUNT, "100");
			inputParams.put(TOACCOUNT_CURRENCY, "EUR");
			request.addRequestParam_(TOACCOUNT_CURRENCY, "EUR");
			inputParams.put(PARAM_TRANSACTION_TYPE, T24_TXN_TYPE_ACTRF);
			request.addRequestParam_(PARAM_TRANSACTION_TYPE, T24_TXN_TYPE_ACTRF);
			
			CommonUtils.callIntegrationService(request, inputParams, headerParams, TemenosConstants.SERVICE_T24IS_TRANSFERS, TemenosConstants.OP_CREATE_TRANSFER, true);
		}
		return result;
	}
}
