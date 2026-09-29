package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetUserDetailsToAdminPreProcessor extends TemenosBasePreProcessor {

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		String accountId = params.get(AccountsConstants.DB_ACCOUNTID) != null ? params.get(AccountsConstants.DB_ACCOUNTID).toString() : "";
		if(accountId.equals("")) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			result.addErrMsgParam("Misssing input param Account_id");
			return Boolean.FALSE;
		}
		super.execute(params, request);
		
		return Boolean.TRUE;
	}
}