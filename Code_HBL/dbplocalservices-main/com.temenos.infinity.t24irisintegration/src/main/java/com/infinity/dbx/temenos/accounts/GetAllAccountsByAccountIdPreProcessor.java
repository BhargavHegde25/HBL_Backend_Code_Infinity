package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosPreLoginBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetAllAccountsByAccountIdPreProcessor extends TemenosPreLoginBasePreProcessor{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request, response, result);
		alert.prepareError("GetAllAccountsByAccountIdPreProcessor").log();
		String account_id = params.get("Account_id") != null ? params.get("Account_id").toString() : "";
		if(!"".equalsIgnoreCase(account_id)) {
			alert.prepareError("GetAllAccountsByAccountIdPreProcessor return true").log();
			return Boolean.TRUE;
		}
		
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return Boolean.FALSE;
	}

}
