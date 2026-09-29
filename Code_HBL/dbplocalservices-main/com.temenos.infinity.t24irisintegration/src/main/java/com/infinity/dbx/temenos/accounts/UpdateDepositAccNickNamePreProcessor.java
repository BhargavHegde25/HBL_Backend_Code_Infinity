package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.objects.Account;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UpdateDepositAccNickNamePreProcessor extends TemenosBasePreProcessor implements AccountsConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		super.execute(params, request, response, result);
		CommonUtils.setCompanyIdToRequest(request);

		String updateNickNameCall = request.getParameter(UPDATENICKNAME_CALL);
		String nickName = CommonUtils.getParamValue(params, AccountsConstants.NICKNAME);
		if ("false".equalsIgnoreCase(updateNickNameCall) || "".equalsIgnoreCase(nickName)) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return false;
		}

		if(nickName.length() > 16) {
			result.addStringParam("errmsg", "Length should be less than or equal to 16");
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return false;
		}
		TemenosUtils temenosUtils = TemenosUtils.getInstance();

		HashMap<String, Account> accounts = temenosUtils.getAccountsMapFromCache(request);
		String accountId = CommonUtils.getParamValue(params, AccountsConstants.ACCOUNTID);
		
		if (accounts != null && !"".equalsIgnoreCase(nickName) && Constants.ACCOUNT_TYPE_DEPOSIT.equalsIgnoreCase(accounts.get(accountId).getAccountType())) {
			Account account = accounts.get(accountId);

			String arrangementId = account.getArrangementId();
			String productId = account.getProductId();

			params.put(ARRANGEMENT_ID, arrangementId);
			params.put(PRODUCT_ID, productId);
			
			alert.prepareError("Params " + params.toString()).log();

			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
			//return Boolean.TRUE;
		}
		
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return Boolean.FALSE;
	}

}
