package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.util.ErrorConstants;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.infinity.api.arrangements.backenddelegate.api.AccountClosureBackendDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.UserManagementAPIServices;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;
import com.temenos.infinity.api.arrangements.dto.AccountClosureDTO;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class AccountClosureDBXBackenDelegateImpl implements AccountClosureBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");


	@Override
	public AccountClosureDTO CloseAccount(AccountClosureDTO accountClosureDTO, HashMap<String, Object> headerMap)
			throws ApplicationException {
		// TODO Auto-generated method stub
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
				inputMap.put("customerid", accountClosureDTO.getCustomerid());
				inputMap.put("accountNumber", accountClosureDTO.getAccountNumber());
				inputMap.put("accountId", accountClosureDTO.getAccountNumber());
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
				
				
				//inputMap.put("customerName", changeRepaymentday.getCustomerName());
				inputMap.put("requestBody", escapedReqBody);
				alert.prepareError("OMS Request" + inputMap.toString()).log();

				// Making a call to order request API
				String closeAccountResponse = null;
				JSONObject Response = new JSONObject();
				if(inputMap.get("subtype").equals("SavingAccountClosure")) {
				try {
					closeAccountResponse = DBPServiceExecutorBuilder.builder()
							.withServiceId(ArrangementsAPIServices.ARRANGEMENTS_SAVINGSACCOUNT_CLOSURE.getServiceName())
							.withOperationId(ArrangementsAPIServices.ARRANGEMENTS_SAVINGSACCOUNT_CLOSURE.getOperationName())
							.withRequestParameters(inputMap).withRequestHeaders(headerMap)
							.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

				} catch (Exception e) {
					alert.prepareError("Unable to create user account settings order " + e).log();
					throw new ApplicationException(ErrorCodeEnum.ERR_20053);
				}
			}
				else if(inputMap.get("subtype").equals("CurrentAccountClosure")) {
					try {
						closeAccountResponse = DBPServiceExecutorBuilder.builder()
								.withServiceId(ArrangementsAPIServices.ARRANGEMENTS_CHECKINGACCOUNT_CLOSURE.getServiceName())
								.withOperationId(ArrangementsAPIServices.ARRANGEMENTS_CHECKINGACCOUNT_CLOSURE.getOperationName())
								.withRequestParameters(inputMap).withRequestHeaders(headerMap)
								.withFabricAuthToken(headerMap.get("X-Kony-Authorization").toString()).build().getResponse();

					} catch (Exception e) {
						alert.prepareError("Unable to create user account settings order " + e).log();
						throw new ApplicationException(ErrorCodeEnum.ERR_20053);
					}
				}
				

				if (StringUtils.isNotBlank(closeAccountResponse)) {
					alert.prepareError("OMS Response " + closeAccountResponse).log();
					Response = new JSONObject(closeAccountResponse);
				}
				if (Response.has("opstatus") && Response.getInt("opstatus") == 0
						&& !(Response.has("errorMessage") || Response.has("errmsg"))) {
					accountClosureDTO.setId("");
					accountClosureDTO.setStatus("Request Initiated");
					accountClosureDTO.setMessage("Request Initiated");
				}
				else {
					accountClosureDTO.setId("");
					accountClosureDTO.setMessage("Request Failed");
					accountClosureDTO.setStatus("Request Failed");
					accountClosureDTO.setErrorMessage(ErrorCodeEnum.ERR_20058.getMessage());
				}
				
				return accountClosureDTO;
			}
			
	}

