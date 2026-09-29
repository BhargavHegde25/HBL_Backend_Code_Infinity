package com.temenos.infinity.api.chequemanagement.resource.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.businessdelegate.api.GetStopPaymentBusinessDelegate;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.chequemanagement.resource.api.GetStopPaymentResource;
import com.temenos.infinity.api.chequemanagement.utils.AccountUtilities;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementUtilities;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetStopPaymentsResourceImpl implements GetStopPaymentResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getStopPaymentRequests(StopPayment stopPaymentInput, DataControllerRequest request) {
        List<StopPayment> stopPaymentsList = null;
        Result result = new Result();
        
        AccountUtilities ac = new AccountUtilities(); 
        String customerId = "";
        try {
            customerId = (String) request.getServicesManager().getIdentityHandler().getUserAttributes()
                    .get(ChequeManagementConstants.PARAM_CUSTOMER_ID);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch the customer id from session" + e).log();
        }

        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        if (!ac.validateInternalAccount(customerId, stopPaymentInput.getAccountID())) { 
            return ErrorCodeEnum.ERR_26008.setErrorCode(new Result());
        }
        
        String accountID = stopPaymentInput.getAccountID();
        if (accountID.contains("-")) {
            accountID = ChequeManagementUtilities.RemoveCompanyId(accountID);
            stopPaymentInput.setAccountID(accountID);
        }

        try {
        	String CHEQUE_BACKEND = EnvironmentConfigurationsHandler.getServerAppProperty("CHEQUE_BACKEND");
			if ("STUB".equalsIgnoreCase(CHEQUE_BACKEND)) {
				stopPaymentsList = getStubbedStopPaymentsResponse(accountID);
			} else if ("T24".equalsIgnoreCase(CHEQUE_BACKEND)) {
				GetStopPaymentBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(GetStopPaymentBusinessDelegate.class);
				stopPaymentsList = orderBusinessDelegate.getStopPaymentRequestsT24(stopPaymentInput, request);
			} else {
				GetStopPaymentBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(GetStopPaymentBusinessDelegate.class);
				stopPaymentsList = orderBusinessDelegate.getStopPaymentRequests(stopPaymentInput, request);
			}

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch get stop payment requests " + e).log();
            return ErrorCodeEnum.ERR_26013.setErrorCode(new Result());
        }
        
        JSONArray stopPaymentsArray = new JSONArray(stopPaymentsList);
        for (int i = 0; i < stopPaymentsArray.length(); i++) {
            JSONObject order = stopPaymentsArray.getJSONObject(i);
            if (order.has("id")) {
                String id = order.getString("id");
                order.put("Id", id);
                order.remove("id");
            }
        }

        JSONObject responseObj = new JSONObject();
        responseObj.put("accountransactionview", stopPaymentsArray);
        result = JSONToResult.convert(responseObj.toString());
        
        return result;
    }

	private List<StopPayment> getStubbedStopPaymentsResponse(String accountID) {
		List<StopPayment> stopPaymentsList;
		stopPaymentsList = new ArrayList<>();
		StopPayment order = new StopPayment();
		order.setId("SPC23209IIS2F");
		order.setTransactionDate("2023-07-28");
		order.setStatusDesc("SUCCESS");
		order.setAmount("10.00");
		order.setFromAccountNickName("Avinash");
		order.setNote("Cheque Stolen");
		order.setCheckDateOfIssue("20220113");
		order.setCheckReason("Cheque Stolen");
		order.setCheckNumber1("140221");
		order.setRequestType("single");
		order.setFee("45.48");
		order.setPayeeName("kumar");
		order.setFromAccountNumber(accountID);
		stopPaymentsList.add(order);
		
		order = new StopPayment();
		order.setId("SPC33505IIS4F");
		order.setTransactionDate("2023-04-28");
		order.setStatusDesc("SUCCESS");
		order.setAmount("20.00");
		order.setFromAccountNickName("Avinash");
		order.setNote("Cheque Stolen");
		order.setCheckDateOfIssue("20210113");
		order.setCheckReason("Cheque Stolen");
		order.setCheckNumber1("153221");
		order.setRequestType("single");
		order.setFee("65.48");
		order.setPayeeName("Tushar");
		order.setFromAccountNumber(accountID);
		stopPaymentsList.add(order);
		
		return stopPaymentsList;
	}
}
