package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;


import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AddLoanAccountBannerPreProcessor extends TemenosBasePreProcessor implements AccountsConstants{

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		
		super.execute(params, request, response, result);
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		String productType = CommonUtils.getParamValue(params, PRODUCT_TYPE);
		if (Constants.ACCOUNT_TYPE_LOAN.equalsIgnoreCase(productType)) {
			temenosUtils.loadBannerAccountTypeProperties();
			String accountType = temenosUtils.bannerAccountTypesMap.get(productType);
			String[] propety = accountType.split(" ");
			params.put(PRODUCT_ID, propety[0]);
			params.put(ACTIVITY_ID, propety[1]);
			request.addRequestParam_(PRODUCT_TYPE, productType);
			return Boolean.TRUE;
		}
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return Boolean.FALSE;
	}

}
