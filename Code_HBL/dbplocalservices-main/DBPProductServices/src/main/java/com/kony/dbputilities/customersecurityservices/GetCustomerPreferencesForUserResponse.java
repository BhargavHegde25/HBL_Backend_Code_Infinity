package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerPreferencesForUserResponse implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String Customer_id = inputParams.get("Customer_id");
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + Customer_id;

		Map<String, String> requestParams = new HashMap<>();
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);

		Result result = new Result();
		Record record = new Record();
		record.setId("CustomerPreferences");
		Result preferencesResult = HelperMethods.callApi(dcRequest, requestParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERPREFERENCE_GET);

		Map<String, String> responseParams = repsonseMap();
		Set<String> responseParamSet = responseParams.keySet();
		for (String responseParam : responseParamSet) {
			Param p = new Param(responseParams.get(responseParam),
					HelperMethods.getFieldValue(preferencesResult, responseParam),
					DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			record.addParam(p);
		}
		result.addRecord(record);
		return result;
	}

	private Map<String, String> repsonseMap() {
		Map<String, String> responseParser = new HashMap<>();
		responseParser.put("DefaultAccountDeposit", "default_account_deposit");
		responseParser.put("DefaultAccountBillPay", "default_account_billPay");
		responseParser.put("DefaultAccountPayments", "default_account_payments");
		responseParser.put("DefaultAccountCardless", "default_account_cardless");
		responseParser.put("DefaultAccountTransfers", "default_account_transfers");
		responseParser.put("DefaultModule_id", "DefaultModule_id");
		responseParser.put("DefaultAccountWire", "default_account_wire");
		responseParser.put("DefaultFromAccountP2P", "default_from_account_p2p");
		responseParser.put("DefaultToAccountP2P", "default_to_account_p2p");
		responseParser.put("ShowBillPayFromAccPopup", "showBillPayFromAccPopup");
		
		responseParser.put("DefaultAccountLoanPayment", "default_account_loanpayment");
		responseParser.put("DefaultAccountCardPayment", "default_account_cardpayment");
		responseParser.put("DefaultAccountCheckManagement", "default_account_checkmanagement");
		
		responseParser.put("DefaultAccountDashboard", "default_account_dashboard");
		responseParser.put("DefaultAccountQRPayment", "default_account_qrpayment");
		responseParser.put("DefaultAccountCheckDeposit", "default_account_checkdeposit");
		responseParser.put("DefaultAccountCashWithdrawl", "default_account_cashwithdrawl");
		responseParser.put("DefaultAccounteSewa", "default_account_esewa");
		
		return responseParser;
	}

}
