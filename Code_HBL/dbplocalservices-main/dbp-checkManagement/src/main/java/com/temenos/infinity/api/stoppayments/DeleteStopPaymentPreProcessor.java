package com.temenos.infinity.api.stoppayments;

import java.util.HashMap;


import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class DeleteStopPaymentPreProcessor extends TemenosBasePreProcessor implements StopPaymentConstants {

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		super.execute(params, request, response, result);
		String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
		if (STOPCHECKPAYMENT_REQ.equalsIgnoreCase(transactionType)) {
			return Boolean.TRUE;
		}
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return Boolean.FALSE;
		
	}

}
