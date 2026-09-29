package com.kony.dbputilities.kms;

import java.util.HashMap;

import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class KMSLoginPreProcessor implements DataPreProcessor2 {
	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(HashMap paramMap, DataControllerRequest dcRequest, DataControllerResponse dcResponse,
			Result result) throws Exception {
		paramMap.put("userid", URLFinder.getPathUrl(URLConstants.KMS_USERID, dcRequest));
		paramMap.put("password", URLFinder.getPathUrl(URLConstants.KMS_PWD, dcRequest));
		return true;
	}
}