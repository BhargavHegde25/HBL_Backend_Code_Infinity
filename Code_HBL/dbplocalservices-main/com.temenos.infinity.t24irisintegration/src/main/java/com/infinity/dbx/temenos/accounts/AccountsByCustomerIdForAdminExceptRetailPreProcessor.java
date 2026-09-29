package com.infinity.dbx.temenos.accounts;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AccountsByCustomerIdForAdminExceptRetailPreProcessor  extends TemenosBasePreProcessor implements AccountsConstants{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request);
		
		String customerId = params.get(Constants.CUSTOMER_ID) != null ? params.get(Constants.CUSTOMER_ID).toString() : "";
		if(customerId.equals("")) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			result.addErrMsgParam("Misssing input param username");
			return Boolean.FALSE;
		}
		
		Map<String, String> typeUserNameMap = getCustomerTypeAndUserName( request, customerId);
		String customerType_id =  typeUserNameMap.get(AccountsConstants.CUSTOMER_TYPE_ID);
		if(!"".equalsIgnoreCase(customerType_id) && RETAIL_TYPE.equalsIgnoreCase(customerType_id)) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
		
		/* In case of Business and Combined User
		 * Read accounts from DB
		 * Append accountId to new string with space separated
		 */
		
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("$filter", "Customer_id eq " + customerId);
		request.addRequestParam_("$filter", "Customer_id eq " + customerId);
		Result userAccounts = CommonUtils.callIntegrationService(request, inputParams, null, SERVICE_BACKEND_CERTIFICATE, OPERATION_CUSTOMER_ACCOUNTS_GET, true);
		String accounts = "";
		for(Record rec : userAccounts.getDatasetById(AccountsConstants.DS_CUSTOMERACCOUNTS).getAllRecords()) {
			accounts += rec.getParamValueByName(AccountsConstants.DB_ACCOUNTID);
			accounts += " ";
		}
		params.put(AccountsConstants.DB_ACCOUNTID, URLEncoder.encode(accounts.substring(0, accounts.length()-1),"UTF-8"));
		alert.prepareError("Final Params for getAccounts for Admin " + params.toString()).log();
		request.addRequestParam_(PARAM_LOGINUSERID, customerId);
		request.addRequestParam_(TemenosConstants.PARAM_USERNAME, typeUserNameMap.get(TemenosConstants.PARAM_USERNAME));

		
		return Boolean.TRUE;
	}
	
public Map<String, String> getCustomerTypeAndUserName(DataControllerRequest request, String customerId) {
		
		String customerType_id = "";
		String userName = "";
		Map<String, String> map = new HashMap<String, String>(); 
		try {
			
			String filter = CommonUtils.buildOdataCondition(Constants.PARAM_ID, Constants.EQUAL, customerId);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			
			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if ( null !=customerDataset ) {
				
				customerType_id = customerDataset.getRecord(0).getParamValueByName(AccountsConstants.CUSTOMER_TYPE_ID);
				userName = customerDataset.getRecord(0).getParamValueByName(TemenosConstants.PARAM_USERNAME);
			}
			
		} catch(Exception e) {
			
			alert.prepareError("Error while retrieving CustomerType_id for Customer "+ customerId ).log();
		}

		map.put(AccountsConstants.CUSTOMER_TYPE_ID, customerType_id );
		map.put(TemenosConstants.PARAM_USERNAME, userName );
		
		return map;
	}
	
}