package com.infinity.dbx.temenos.externalAccounts;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EditExternalAccountPostProcessor extends TemenosBasePostProcessor implements ExternalAccountsConstants {
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		String errmsg = CommonUtils.getParamValue(result, PARAM_ERR_MSG);
		if(!"".equalsIgnoreCase(errmsg)) {
			return result;
		}
		if (TRANSACTION_TYPE_ACIB.equalsIgnoreCase(result.getParamValueByName(TRANSACTION_TYPE))) {
			result.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
			result.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
		}
		else if (TRANSACTION_TYPE_BCIB.equalsIgnoreCase(result.getParamValueByName(TRANSACTION_TYPE))) {
			result.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
			result.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
		}
		else {
			result.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
			result.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
		}
		return result;

	}
}
