package com.infinity.dbx.temenos.externalAccounts;

import java.util.HashMap;


import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetExternalAccountPreProcessor extends TemenosBasePreProcessor implements ExternalAccountsConstants{

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request, response, result);
		
		String offset = CommonUtils.getParamValue(params, OFFSET);

		String limit = CommonUtils.getParamValue(params, LIMIT);
		
		if((offset == null || offset == "") && (limit == null || limit == "")) {
			params.put(OFFSET, OFFSET_VALUE);
			params.put(LIMIT, LIMIT_VALUE);
		}
		
		return true;
	}

}
