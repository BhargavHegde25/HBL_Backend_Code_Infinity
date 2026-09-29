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
import com.temenos.infinity.api.arrangements.backenddelegate.api.SubmitPartialRepaymentBackendDelegate;
import com.temenos.infinity.api.arrangements.config.UserManagementAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;
import com.temenos.infinity.api.arrangements.dto.PartialRepaymentDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.arrangements.constants.Constants;

public class SubmitPartialRepaymentBackendDelegateImpl implements SubmitPartialRepaymentBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public PartialRepaymentDTO SubmitPartialRepayment(PartialRepaymentDTO partialRepayment,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = ArrangementsUtils.loadProps(Constants.CHANGE_REPAYMENT_PARAM_PROPERTY);

		JSONObject requestBody = new JSONObject();
		requestBody.put("facilityName", partialRepayment.getFacilityName());
		requestBody.put("arrangementId", partialRepayment.getArrangementId());
		requestBody.put("customerName", partialRepayment.getCustomerName());
		requestBody.put("customerId", partialRepayment.getCustomerId());
		requestBody.put("numOfLoans", partialRepayment.getNumOfLoans());
		requestBody.put("currentOutstandingBalanceCurrency", partialRepayment.getCurrentOutstandingBalanceCurrency());
		requestBody.put("currentOutstandingBalanceAmount", partialRepayment.getCurrentOutstandingBalanceAmount());
		requestBody.put("amountPaidToDate", partialRepayment.getAmountPaidToDate());
		requestBody.put("supportingDocumentIds", partialRepayment.getSupportingDocumentIds());
		requestBody.put("requestDetails", partialRepayment.getRequestDetails());
		requestBody.put("paymentDetails", partialRepayment.getPaymentDetails());
		requestBody.put("accountId", partialRepayment.getAccountId());
		String escapedReqBody =  requestBody.toString().replace("'","\\'");
		escapedReqBody = escapedReqBody.replace("\"", "'");
		// Set Input Parameters for create Order service
		Map<String, Object> inputMap = new HashMap<>();
		/*
		 * inputMap.put("requestConfigId", props.getProperty("repaymentDayrequestConfigId"));
		 * inputMap.put("product", props.getProperty("product"));
		 */
		inputMap.put("type", props.getProperty("partialRepaymenttype"));
		inputMap.put("subtype", props.getProperty("partialRepaymentsubType"));
		inputMap.put("accountId", partialRepayment.getAccountId());
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
    			partialRepayment.setId(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID));
    			partialRepayment.setStatus(Response.getString(UserAccountSettingConstants.PARAM_ORDER_STATUS));
    			partialRepayment.setStatus("SUCCESS");

    			if (Response.has(UserAccountSettingConstants.PARAM_ERROR_MESSAGE)) {
    				if (StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE))) {
    					partialRepayment
    							.setErrorMessage(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE));
    				}
    			}
    		}
    	}else {
			partialRepayment.setId(String.format("%06d", new java.util.Date().getTime()%1000000));
			partialRepayment.setStatus("SUCCESS");
    	}
		

		return partialRepayment;
	}
	

}
