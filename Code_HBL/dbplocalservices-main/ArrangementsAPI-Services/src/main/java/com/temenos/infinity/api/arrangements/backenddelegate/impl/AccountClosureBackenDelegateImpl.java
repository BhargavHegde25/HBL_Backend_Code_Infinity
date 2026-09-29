package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.infinity.api.arrangements.backenddelegate.api.AccountClosureBackendDelegate;
import com.temenos.infinity.api.arrangements.config.UserManagementAPIServices;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class AccountClosureBackenDelegateImpl implements AccountClosureBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");


	@Override
	public AccountClosureDTO CloseAccount(AccountClosureDTO accountClosureDTO,
			HashMap<String, Object> headerMap) throws ApplicationException {

		// Load Check Book Request properties
		Properties props = ArrangementsUtils.loadProps(Constants.ACCOUNT_CLOSURE_PARAM_PROPERTY);

		JSONObject requestBody = new JSONObject();
		requestBody.put("accountName", accountClosureDTO.getAccountName());
		requestBody.put("accountNumber", accountClosureDTO.getAccountNumber());
		requestBody.put("accountType", accountClosureDTO.getAccountType());
		requestBody.put("currentBalance", accountClosureDTO.getCurrentBalance());
		requestBody.put("closingReason", accountClosureDTO.getClosingReason());
		requestBody.put("IBAN", accountClosureDTO.getIBAN());
		requestBody.put("SWIFTCode", accountClosureDTO.getSWIFTCode());
		requestBody.put("supportingDocumentData", accountClosureDTO.getSupportingDocumentData());
		requestBody.put("customerid", accountClosureDTO.getCustomerid());
		
		
		String escapedReqBody =  requestBody.toString().replace("'","\\'");
		escapedReqBody = escapedReqBody.replace("\"", "'");
		// Set Input Parameters for create Order service
		Map<String, Object> inputMap = new HashMap<>();
		/*
		 * inputMap.put("requestConfigId", props.getProperty("repaymentDayrequestConfigId"));
		 * inputMap.put("product", props.getProperty("product"));
		 */
		
		if(accountClosureDTO.getAccountType().equalsIgnoreCase("Current") || accountClosureDTO.getAccountType().equalsIgnoreCase("Checking")) {
			inputMap.put("type", props.getProperty("CurrentAccountClosureType"));
			inputMap.put("subtype", props.getProperty("CurrentAccountClosureSubType"));
		}
		else {
		inputMap.put("type", props.getProperty("SavingAccountClosureType"));
		inputMap.put("subtype", props.getProperty("SavingAccountClosureSubType"));
		}
		inputMap.put("accountId", accountClosureDTO.getAccountNumber());
		//inputMap.put("customerName", changeRepaymentday.getCustomerName());
		inputMap.put("requestBody", escapedReqBody);
		alert.prepareError("OMS Request" + inputMap.toString()).log();

		// Making a call to order request API
		String closeAccountResponse = null;
		JSONObject Response = new JSONObject();
		try {
			closeAccountResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
					.withOperationId(UserManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
					.withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

		} catch (Exception e) {
			alert.prepareError("Unable to create user account settings order " + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20053);
		}

		if (StringUtils.isNotBlank(closeAccountResponse)) {
			alert.prepareError("OMS Response " + closeAccountResponse).log();
			Response = new JSONObject(closeAccountResponse);
		}

		if (Response.has(UserAccountSettingConstants.PARAM_ORDER_ID)
				&& StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID))) {
			accountClosureDTO.setId(Response.getString(UserAccountSettingConstants.PARAM_ORDER_ID));
			accountClosureDTO.setStatus(Response.getString(UserAccountSettingConstants.PARAM_ORDER_STATUS));

			if (Response.has(UserAccountSettingConstants.PARAM_ERROR_MESSAGE)) {
				if (StringUtils.isNotBlank(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE))) {
					accountClosureDTO
							.setErrorMessage(Response.getString(UserAccountSettingConstants.PARAM_ERROR_MESSAGE));
				}
			}
		}
		return accountClosureDTO;
	}
	
}
