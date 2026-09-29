package com.infinity.dbx.temenos.user;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetFullUserDetailsPreProcessor extends TemenosBasePreProcessor {

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		if (StringUtils.isNotEmpty(request.getParameter("partyID"))) {
			super.execute(params, request);
			return Boolean.TRUE;
		} else {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
	}
}
