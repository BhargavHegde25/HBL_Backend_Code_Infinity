package com.infinity.dbx.temenos.transactions;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AccountPaymentOrdersForAdminPreProcessor extends TemenosBasePreProcessor {


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		try {
			
			if (StringUtils.isBlank(CommonUtils.getParamValue(params, TransactionConstants.ACCOUNT_NUMBER))) {
				
				result.addOpstatusParam(0);
				result.addHttpStatusCodeParam(200);
				result.addErrMsgParam("Misssing input param "+TransactionConstants.ACCOUNT_NUMBER);
				return Boolean.FALSE;
			}
			super.execute(params, request);
			
			if (StringUtils.isEmpty(request.getParameter(TransactionConstants.PARAM_LIMIT))) {
				params.put(TransactionConstants.PARAM_LIMIT, TransactionConstants.PARAM_TRANSACTION_COUNT_DEF_VALUE);
			}
			
			if (StringUtils.isEmpty(request.getParameter(TransactionConstants.PARAM_PAGE_START))) {
				params.put(TransactionConstants.PARAM_PAGE_START, TransactionConstants.PARAM_PAGE_START_DEF_VALUE);
			}
			
			return Boolean.TRUE;
			
		} catch (Exception e) {
			return Boolean.FALSE;
		}

	}
}