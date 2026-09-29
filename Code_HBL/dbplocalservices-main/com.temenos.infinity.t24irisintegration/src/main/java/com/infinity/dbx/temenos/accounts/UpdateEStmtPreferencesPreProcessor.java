package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateEStmtPreferencesPreProcessor extends TemenosBasePreProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		super.execute(params, request, response, result);
		CommonUtils.setCompanyIdToRequest(request);

		String eStatementEnable = CommonUtils.getParamValue(params, AccountsConstants.PARAM_ESTATEMENT_TOGGLE);
		if (StringUtils.isEmpty(eStatementEnable)) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return false;
		}

		if (!StringUtils.equalsIgnoreCase(eStatementEnable, "1")
				&& !StringUtils.equalsIgnoreCase(eStatementEnable, "0")) {
			result.addStringParam("errmsg", "Invalid EStatementEnable Value");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return false;
		}
		//Load Properties from resources file into map, and get values
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		temenosUtils.loadUserProperties();
		String eStmtName = temenosUtils.userMap.get("ESTATEMENT_PRINT_ATTRIBUTE_NAME");
		String eStmtValue = temenosUtils.userMap.get("ESTATEMENT_PRINT_ATTRIBUTE_VALUE");
		
		//Load Accounts from Cache
		HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
		String accountId = CommonUtils.getParamValue(params, AccountsConstants.ACCOUNTID);

		if (accounts != null && StringUtils.isNotEmpty(eStatementEnable)) {
			Account account = accounts.get(accountId);
			
			//If account not found in cache
			if (account == null) {
				result.addStringParam("errmsg", "Invalid Account Number");
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				return false;
			}
			
			String arrangementId = account.getArrangementId();
			String productId = account.getProductId();
			String productLine = account.getAccountType();

			if (StringUtils.isEmpty(productLine)) {
				result.addStringParam("errmsg", "Empty Product Line Value");
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				return false;
			}

			params.put(ARRANGEMENT_ID, arrangementId);
			params.put(PRODUCT_ID, productId);
			String validateRequest = CommonUtils.getParamValue(params, AccountsConstants.PARAM_VALIDATE);

			if (StringUtils.isNotBlank(validateRequest) && validateRequest.equalsIgnoreCase("true")) {
				request.addRequestParam_(AccountsConstants.PARAM_VALIDATE_ONLY, validateRequest);
			}
			if (StringUtils.equalsIgnoreCase(eStatementEnable, "1")) {
				params.put(PARAM_ESTATEMENT_PRINT_ATTRIBUTE_NAME, eStmtName);
				params.put(PARAM_ESTATEMENT_PRINT_ATTRIBUTE_VALUE, eStmtValue);
			} else {
				params.put(PARAM_ESTATEMENT_PRINT_ATTRIBUTE_NAME, eStmtName);
				params.put(PARAM_ESTATEMENT_PRINT_ATTRIBUTE_VALUE, ESTATEMENT_NULL);
			}
			if (productLine.equalsIgnoreCase(Constants.ACCOUNT_TYPE_LOAN)
					|| productLine.equalsIgnoreCase(Constants.ACCOUNT_TYPE_MORTGAGE))
			{
			params.put(ACTIVITY_ID, LENDING_UPDATE_STATEMENT);
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
			}
			else if (productLine.equalsIgnoreCase(Constants.ACCOUNT_TYPE_DEPOSIT))
			{
			params.put(ACTIVITY_ID, DEPOSITS_UPDATE_STATEMENT);
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
			}
		    
			if (productLine.equalsIgnoreCase(Constants.ACCOUNT_TYPE_SAVINGS)
					|| productLine.equalsIgnoreCase(Constants.ACCOUNT_TYPE_CHECKING) || productLine.equalsIgnoreCase("CURRENT.ACCOUNT"))
				params.put(ACTIVITY_ID, ACCOUNTS_UPDATE_STATEMENT);

			alert.prepareError("Params " + params.toString()).log();

			
			return Boolean.TRUE;
		}

		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return Boolean.FALSE;
	}

}