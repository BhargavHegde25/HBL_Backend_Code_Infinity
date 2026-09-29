package com.infinity.dbx.temenos.externalAccounts;

import java.util.HashMap;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AddExternalAccountPreProcessor extends TemenosBasePreProcessor implements ExternalAccountsConstants {

	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		// run TemenosBasePreProcessor
		super.execute(params, request, response, result);
		
		String internationalAccount = CommonUtils.getParamValue(params, IS_INTERNATIONAL_ACCOUNT);

		String sameBankAccount = CommonUtils.getParamValue(params, IS_SAMEBANK_ACCOUNT);
		
		if (Constants.FALSE.equalsIgnoreCase(internationalAccount) && Constants.FALSE.equalsIgnoreCase(sameBankAccount)) {
			params.put(PAYMENT_PRODUCT, ACCOUNT_TYPE_SEPA);
			params.put(TRANSACTION_TYPE, TRANSACTION_TYPE_BCIB);
		}
		else if(Constants.FALSE.equalsIgnoreCase(internationalAccount) && Constants.TRUE.equalsIgnoreCase(sameBankAccount)) {
			params.put(PAYMENT_PRODUCT, ACCOUNT_TYPE_DOMESTIC);
			params.put(TRANSACTION_TYPE, TRANSACTION_TYPE_ACIB);
		}
		else {
			params.put(PAYMENT_PRODUCT, ACCOUNT_TYPE_INTERNATIONAL);
			params.put(TRANSACTION_TYPE, TRANSACTION_TYPE_OTIB);
		}

		return Boolean.TRUE;
	}

}
