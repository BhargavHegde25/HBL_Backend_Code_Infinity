package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.infinity.api.arrangements.backenddelegate.api.SubmitChangeRepaymentBackendDelegate;
import com.temenos.infinity.api.arrangements.config.UserManagementAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.arrangements.constants.Constants;

public class SubmitChangeRepaymentBackendDelegateImpl implements SubmitChangeRepaymentBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public MortgageRepaymentDTO SubmitChangeRepaymentDay(MortgageRepaymentDTO changeRepaymentday,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = ArrangementsUtils.loadProps(Constants.CHANGE_REPAYMENT_PARAM_PROPERTY);

		JSONObject requestBody = new JSONObject();
		requestBody.put("facilityName", changeRepaymentday.getFacilityName());
		requestBody.put("arrangementId", changeRepaymentday.getArrangementId());
		requestBody.put("customerName", changeRepaymentday.getCustomerName());
		requestBody.put("customerId", changeRepaymentday.getCustomerId());
		requestBody.put("numOfLoans", changeRepaymentday.getNumOfLoans());
		requestBody.put("currentOutstandingBalanceCurrency", changeRepaymentday.getCurrentOutstandingBalanceCurrency());
		requestBody.put("currentOutstandingBalanceAmount", changeRepaymentday.getCurrentOutstandingBalanceAmount());
		requestBody.put("currentMaturityDate", changeRepaymentday.getCurrentMaturityDate());
		requestBody.put("supportingDocumentData", changeRepaymentday.getSupportingDocumentIds());
		requestBody.put("requestDetails", changeRepaymentday.getRequestDetails());
		String escapedReqBody =  requestBody.toString().replace("'","\\'");
		escapedReqBody = escapedReqBody.replace("\"", "'");
		// Set Input Parameters for create Order service
		Map<String, Object> inputMap = new HashMap<>();
		/*
		 * inputMap.put("requestConfigId", props.getProperty("repaymentDayrequestConfigId"));
		 * inputMap.put("product", props.getProperty("product"));
		 */
		inputMap.put("type", props.getProperty("repaymentDaytype"));
		inputMap.put("subtype", props.getProperty("repaymentDaysubType"));
		inputMap.put("accountId", changeRepaymentday.getAccountId());
		//inputMap.put("customerName", changeRepaymentday.getCustomerName());
		inputMap.put("requestBody", escapedReqBody);
		alert.prepareError("OMS Request" + inputMap.toString()).log();

		// Making a call to order request API
		String repaymentDayResponse = null;
		JSONObject Response = new JSONObject();
		String MORTGAGESERVICING_BACKEND = EnvironmentConfigurationsHandler.getValue("MORTGAGESERVICING_BACKEND");
    	if ("SRMS".equalsIgnoreCase(MORTGAGESERVICING_BACKEND)) {
		try {
			repaymentDayResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
					.withOperationId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

		} catch (Exception e) {
			alert.prepareError("Unable to create user account settings order " + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20053);
		}

		if (StringUtils.isNotBlank(repaymentDayResponse)) {
			alert.prepareError("OMS Response " + repaymentDayResponse).log();
			Response = new JSONObject(repaymentDayResponse);
		}

		if (Response.has(UserAccountSettingConstants.PARAM_ORDER_ID)
				&& StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID))) {
			changeRepaymentday.setId(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID));
			changeRepaymentday.setStatus(Response.getString(UserAccountSettingConstants.PARAM_ORDER_STATUS));

			if (Response.has(UserAccountSettingConstants.PARAM_ERROR_MESSAGE)) {
				if (StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE))) {
					changeRepaymentday
							.setErrorMessage(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE));
				}
			}
		}
    }
    	else {
    		changeRepaymentday.setId(String.format("%06d", new java.util.Date().getTime()%1000000));
    		changeRepaymentday.setStatus("Admin Approval Pending");
    	}
    	
		return changeRepaymentday;
	}
	
	@Override
	public MortgageRepaymentDTO SubmitChangeRepaymentAccount(MortgageRepaymentDTO changeRepaymentAccount,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = ArrangementsUtils.loadProps(Constants.CHANGE_REPAYMENT_PARAM_PROPERTY);

		JSONObject requestBody = new JSONObject();
		requestBody.put("facilityName", changeRepaymentAccount.getFacilityName());
		requestBody.put("arrangementId", changeRepaymentAccount.getArrangementId());
		requestBody.put("customerName", changeRepaymentAccount.getCustomerName());
		requestBody.put("customerId", changeRepaymentAccount.getCustomerId());
		requestBody.put("loanName", changeRepaymentAccount.getLoanName());
		requestBody.put("loanAccountNumber", changeRepaymentAccount.getLoanAccountNumber());
		requestBody.put("supportingDocumentData", changeRepaymentAccount.getSupportingDocumentIds());
		requestBody.put("requestDetails", changeRepaymentAccount.getRequestDetails());
		String escapedReqBody =  requestBody.toString().replace("'","\\'");
		escapedReqBody = escapedReqBody.replace("\"", "'");
		// Set Input Parameters for create Order service
		Map<String, Object> inputMap = new HashMap<>();
		/*
		 * inputMap.put("requestConfigId", props.getProperty("repaymentAccountrequestConfigId"));
		 * inputMap.put("product", props.getProperty("product"));
		 */
		inputMap.put("type", props.getProperty("repaymentAccounttype"));
		inputMap.put("subtype", props.getProperty("repaymentccountsubType"));
		inputMap.put("accountId", changeRepaymentAccount.getAccountId());
		//inputMap.put("customerName", changeRepaymentAccount.getCustomerName());
		inputMap.put("requestBody", escapedReqBody);
		alert.prepareError("OMS Request" + inputMap.toString()).log();

		// Making a call to order request API
		String repaymentAccountResponse = null;
		JSONObject Response = new JSONObject();
		String MORTGAGESERVICING_BACKEND = EnvironmentConfigurationsHandler.getValue("MORTGAGESERVICING_BACKEND");
    	if ("SRMS".equalsIgnoreCase(MORTGAGESERVICING_BACKEND)) {
		try {
			repaymentAccountResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
					.withOperationId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

		} catch (Exception e) {
			alert.prepareError("Unable to create user account settings order " + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20053);
		}

		if (StringUtils.isNotBlank(repaymentAccountResponse)) {
			alert.prepareError("OMS Response " + repaymentAccountResponse).log();
			Response = new JSONObject(repaymentAccountResponse);
		}

		if (Response.has(UserAccountSettingConstants.PARAM_ORDER_ID)
				&& StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID))) {
			changeRepaymentAccount.setId(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID));
			changeRepaymentAccount.setStatus(Response.getString(UserAccountSettingConstants.PARAM_ORDER_STATUS));

			if (Response.has(UserAccountSettingConstants.PARAM_ERROR_MESSAGE)) {
				if (StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE))) {
					changeRepaymentAccount
							.setErrorMessage(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE));
				}
			}
		}
    }
    	else {
    		changeRepaymentAccount.setId(String.format("%06d", new java.util.Date().getTime()%1000000));
			changeRepaymentAccount.setStatus("Admin Approval Pending");
    	}
		return changeRepaymentAccount;
	}

}
