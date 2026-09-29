package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosPreLoginBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAllAccountsPreProcessor extends TemenosPreLoginBasePreProcessor{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request, response, result);
		alert.prepareError("GetAllAccountsPreProcessor params " + params.toString()).log();
		String membership_id = params.get(AccountsConstants.MEMBERSHIP_ID) != null ? params.get(AccountsConstants.MEMBERSHIP_ID).toString() : "";
		if(!"".equalsIgnoreCase(membership_id)) {
			params.put(USER_ID, membership_id);
		}
		
		String customer_id = request.getParameter(AccountsConstants.CUSTOMER_ID) != null ? request.getParameter(AccountsConstants.CUSTOMER_ID).toString() : "";
		if(!"".equalsIgnoreCase(customer_id)) {
			params.put(USER_ID, customer_id);
		}
		
		String account_id = params.get(AccountsConstants.ACCOUNT_ID) != null ? params.get(AccountsConstants.ACCOUNT_ID).toString() : "";
		if(!"".equalsIgnoreCase(account_id)) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
		alert.prepareError("Params true " + params.toString()).log();
		return Boolean.TRUE;
	}
}
