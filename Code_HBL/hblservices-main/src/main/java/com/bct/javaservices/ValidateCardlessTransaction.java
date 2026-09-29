package com.bct.javaservices;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;

public class ValidateCardlessTransaction implements JavaService2 {
	public static LoggerUtil logger = new LoggerUtil(ValidateCardlessTransaction.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		CardlessCashValidationDTO cardlessDto = new CardlessCashValidationDTO();
		try {
			String isAuthValidationEnabled = EnvironmentConfigurationsHandler
					.getServerProperty("CARDLESS_CASH_AUTH_VALIDATION_ENABLED");
			logger.debug("In ValidateCardlessTransaction isAuthValidationEnabled:::" + isAuthValidationEnabled);
			boolean isValidationEnabled = Boolean.parseBoolean(isAuthValidationEnabled);
			cardlessDto.setIsAuthValidationEnabled(isAuthValidationEnabled);
			if (isValidationEnabled) {
				logger.debug("In ValidateCardlessTransaction request.getHeaderMap():::" + request.getHeaderMap());
				cardlessDto.setRequestHeaders(request.getHeaderMap().toString());
				String authHeader = request.getHeader("X-Custom-Auth");
				logger.debug("In ValidateCardlessTransaction authHeader:::" + authHeader);
				Result authResult = validateAuthorizationHeader(authHeader);
				String authResultStr = ResultToJSON.convert(authResult).toString();
				cardlessDto.setAuthResult(authResultStr);
				String responseCode = authResult.getParamValueByName("responseCode");
				if (!"00".equals(responseCode)) {
					logger.error("Authentication validation failed");
					insertCardlessCardResult(cardlessDto);
					return authResult;
				}
				logger.debug("Authentication validation successful");
			}

			// ==================================================================================================================================================
			// Actual business logic
			// ==================================================================================================================================================
			try {
				Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
				logger.debug("In ValidateCardlessTransaction inputParams:::" + inputParams);
				cardlessDto.setSwitch_request_payload(inputParams.toString());
				String fullCode = (String) inputParams.get("pintotp");
				logger.debug("In ValidateCardlessTransaction fullCode:::" + fullCode.length());
				fullCode = fullCode != null ? fullCode.trim() : null;
				if (fullCode == null || !fullCode.matches("\\d{10}")) {
					result.addParam(new Param("responseCode", "E03", FabricConstants.STRING));
					result.addParam(new Param("responseMessage", "Error validating request: Invalid security code",
							FabricConstants.STRING));
					result.addParam(new Param("accountNumber", ""));
					result.addParam(new Param("amount", ""));
					cardlessDto.setResponse_code("E03");
					cardlessDto.setResponse_message("Error validating request: Invalid security code");
					cardlessDto.setStatus("FAILURE");
					insertCardlessCardResult(cardlessDto);
					return result;
				}

				String secureAccessCode = fullCode.substring(0, 4);
				String withdrawalCode = fullCode.substring(4, 10);
				logger.debug("In ValidateCardlessTransaction secureAccessCode:::" + secureAccessCode);
				logger.debug("In ValidateCardlessTransaction withdrawalCode:::" + withdrawalCode);

				String encSecureAccessCode = Utils.hashPin(secureAccessCode);
				logger.debug("In ValidateCardlessTransaction encSecureAccessCode:::" + encSecureAccessCode);

				String mobileNum = (String) inputParams.get("mobile");
				String filter = "cashlessSecurityCode" + DBPUtilitiesConstants.EQUAL + encSecureAccessCode
						+ DBPUtilitiesConstants.AND + "cashlessOTP" + DBPUtilitiesConstants.EQUAL + withdrawalCode;
				inputParams.put(DBPUtilitiesConstants.FILTER, filter);
				logger.debug("In ValidateCardlessTransaction filter:::" + filter);
				cardlessDto.setValidationRequest(inputParams.toString());

				Result transactionResult = HelperMethods.callApi(request, inputParams,
						HelperMethods.getHeaders(request), URLConstants.TRANSACTION_GET);
				logger.debug("In ValidateCardlessTransaction result:::" + ResultToJSON.convert(transactionResult));
				if (HelperMethods.hasRecords(transactionResult)) {
					Record transaction = transactionResult.getAllDatasets().get(0).getRecord(0);
					String transactionPhone = HelperMethods.getFieldValue(transaction, "cashlessPhone");
					String transactionEmail = HelperMethods.getFieldValue(transaction, "cashlessEmail");
					logger.debug("transactionPhone:::" + transactionPhone);
					logger.debug("transactionEmail:::" + transactionEmail);
					boolean isValidTransaction = false;
					// Phone-based transaction
					if (StringUtils.isNotBlank(transactionPhone)) {
						if (!isValidMobileNumber(mobileNum)) {
							result.addParam(new Param("responseCode", "E03", FabricConstants.STRING));
							result.addParam(new Param("responseMessage",
									"Error validating request: Invalid mobile number", FabricConstants.STRING));
							result.addParam(new Param("accountNumber", ""));
							result.addParam(new Param("amount", ""));
							cardlessDto.setResponse_code("E03");
							cardlessDto.setResponse_message("Error validating request: Invalid mobile number");
							cardlessDto.setStatus("FAILURE");
							insertCardlessCardResult(cardlessDto);
							return result;
						}
						if (mobileNum.trim().equals(transactionPhone.trim())) {
							isValidTransaction = true;
						}
					}
					// Email-based transaction
					else if (StringUtils.isNotBlank(transactionEmail)) {
						isValidTransaction = true;
					}
					logger.debug("isValidTransaction:::" + isValidTransaction);
					if (!isValidTransaction) {
						result.addParam(new Param("responseCode", "02", FabricConstants.STRING));
						result.addParam(new Param("responseMessage",
								"No matching record found or the transaction is not active.", FabricConstants.STRING));
						result.addParam(new Param("accountNumber", ""));
						result.addParam(new Param("amount", ""));
						cardlessDto.setResponse_code("02");
						cardlessDto.setResponse_message("No matching record found or the transaction is not active.");
						cardlessDto.setStatus("FAILURE");
						insertCardlessCardResult(cardlessDto);
						return result;
					}
					// valid
					result = postProcess(request, transactionResult, cardlessDto);
				} else {
					result.addParam(new Param("responseCode", "02", FabricConstants.STRING));
					result.addParam(new Param("responseMessage",
							"No matching record found or the transaction is not active.", FabricConstants.STRING));
					result.addParam(new Param("accountNumber", ""));
					result.addParam(new Param("amount", ""));
					cardlessDto.setResponse_code("02");
					cardlessDto.setResponse_message("No matching record found or the transaction is not active.");
					cardlessDto.setStatus("FAILURE");
					insertCardlessCardResult(cardlessDto);
					return result;
				}
				logger.debug("In ValidateCardlessTransaction final result:::" + ResultToJSON.convert(result));
				String validationResult = ResultToJSON.convert(result).toString();
				cardlessDto.setValidationResult(validationResult);
			} catch (Exception e) {
				logger.error("Exception occured while validating the cardless transaction:::" + e.getMessage(), e);
				result.addParam(new Param("responseCode", "1001"));
				result.addParam(new Param("responseMessage", e.getLocalizedMessage()));
				String validationResult = "ERROR: " + e.getMessage();
				String status = "FAILED";
				cardlessDto.setResponse_code("1001");
				cardlessDto.setResponse_message(e.getLocalizedMessage());
				cardlessDto.setValidationResult(validationResult);
				cardlessDto.setStatus(status);
			}
			logger.debug("insertCardlessCardValidationResult started:::");
			insertCardlessCardResult(cardlessDto);
			return result;
		} catch (Exception e) {
			logger.error("Exception occured while validating the Authorization header:::", e);
			result.addParam(new Param("responseCode", "1002"));
			result.addParam(new Param("responseMessage", e.getLocalizedMessage()));
			insertCardlessCardResult(cardlessDto);
			return result;
		}
	}

	private Result postProcess(DataControllerRequest dcRequest, Result transactionResult,
			CardlessCashValidationDTO cardlessDto) throws HttpCallException {
		Result result = new Result();
		Record transaction = transactionResult.getAllDatasets().get(0).getRecord(0);
		String cashWithdrawalTransactionStatus = HelperMethods.getFieldValue(transaction,
				"cashWithdrawalTransactionStatus");
		logger.debug(
				"In ValidateCardlessTransaction cashWithdrawalTransactionStatus:::" + cashWithdrawalTransactionStatus);
		String cashlessOTPValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
		logger.debug("In ValidateCardlessTransaction cashlessOTPValidDate:::" + cashlessOTPValidDate);
		Timestamp ts = Timestamp.valueOf(cashlessOTPValidDate);
		if (cashWithdrawalTransactionStatus.equals("pending")) {
			long now = System.currentTimeMillis();
			logger.debug("In ValidateCardlessTransaction now:::" + now);
			logger.debug("In ValidateCardlessTransaction ts.getTime():::" + ts.getTime());
			logger.debug("In ValidateCardlessTransaction ts:::" + ts);
			if (now < ts.getTime()) {
				// current time is less than DB time
				String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
				logger.debug("In ValidateCardlessTransaction frmAccountNum:::" + frmAccountNum);
				String amount = HelperMethods.getFieldValue(transaction, "amount");
				logger.debug("In ValidateCardlessTransaction amount:::" + amount);
				result.addParam(new Param("responseCode", "00", FabricConstants.STRING));
				result.addParam(new Param("responseMessage", "Success", FabricConstants.STRING));
				result.addParam(new Param("accountNumber", frmAccountNum, FabricConstants.STRING));
				result.addParam(new Param("amount", amount, FabricConstants.STRING));
				cardlessDto.setResponse_code("00");
				cardlessDto.setResponse_message("Success");
				cardlessDto.setStatus("SUCCESS");
				// NEED TO UPDATE STATUS TO VALIDATED AFTER SUCCESSFUL VALIDATION
				try {
					String Id = HelperMethods.getFieldValue(transaction, "Id");
					// int ID = Integer.parseInt(Id);
					Map<String, String> inputMap = new HashMap<>();
					inputMap.put("Id", Id);
					inputMap.put("cashWithdrawalTransactionStatus", "validated");
					logger.debug("In ValidateCardlessTransaction inputMap:::" + inputMap);
					Result updateResult = HelperMethods.callApi(dcRequest, inputMap,
							HelperMethods.getHeaders(dcRequest), URLConstants.TRANSACTION_UPDATE);
					logger.debug("In ValidateCardlessTransaction result:::" + ResultToJSON.convert(updateResult));
				} catch (Exception e) {
					logger.error(
							"Exception while updating CardlessCard Transaction validation result:::" + e.getMessage(),
							e);
				}

			} else {
				result.addParam(new Param("responseCode", "03", FabricConstants.STRING));
				result.addParam(
						new Param("responseMessage", "The OTP validity period has expired.", FabricConstants.STRING));
				result.addParam(new Param("accountNumber", ""));
				result.addParam(new Param("amount", ""));
				cardlessDto.setResponse_code("03");
				cardlessDto.setResponse_message("The OTP validity period has expired.");
				cardlessDto.setStatus("FAILURE");
				return result;
			}
		} else {
			result.addParam(new Param("responseCode", "04", FabricConstants.STRING));
			result.addParam(new Param("responseMessage", "Transaction already validated.", FabricConstants.STRING));
			result.addParam(new Param("accountNumber", ""));
			result.addParam(new Param("amount", ""));
			cardlessDto.setResponse_code("04");
			cardlessDto.setResponse_message("Transaction already validated.");
			cardlessDto.setStatus("FAILURE");
			return result;
		}
		return result;
	}

	public static boolean isValidMobileNumber(String mobile) {
		if (mobile == null || mobile.trim().isEmpty()) {
			return false;
		}
		return mobile.matches("\\d{10}");
	}

	private void insertCardlessCardResult(CardlessCashValidationDTO cardlessDto) {
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();

			inputMap.put("validationRequest", cardlessDto.getValidationRequest());
			inputMap.put("validationResult", cardlessDto.getValidationResult());
			inputMap.put("switch_request_payload", cardlessDto.getSwitch_request_payload());
			inputMap.put("status", cardlessDto.getStatus());
			inputMap.put("response_code", cardlessDto.getResponse_code());
			inputMap.put("response_message", cardlessDto.getResponse_message());
			inputMap.put("isAuthValidationEnabled", cardlessDto.getIsAuthValidationEnabled());
			inputMap.put("requestHeaders", cardlessDto.getRequestHeaders());
			inputMap.put("authResult", cardlessDto.getAuthResult());

			logger.debug("insertCardlessCardResult inputMap:::" + inputMap.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CARDLESSCARDTRANSACTION_RESULTS_CREATE)
					.withRequestParameters(inputMap).withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("insertCardlessCardResult:::" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				logger.debug("ValidateCardlessTransaction insertCardlessCardResult insertion failed:::");
			} else if (responseJSON.has("cardless_transaction_audit_log")
					&& responseJSON.getJSONArray("cardless_transaction_audit_log").length() > 0) {
				logger.debug("ValidateCardlessTransaction insertCardlessCardResult insertion success:::");
			}
			logger.debug("insertCardlessCardResult response:::" + dbResponse);
		} catch (Exception e) {
			logger.error("Exception while inserting CardlessCard Transaction validation result:::" + e.getMessage(), e);
		}
	}

	private Result validateAuthorizationHeader(String authHeader) {
		Result result = new Result();
		try {
			logger.debug("Inside validateAuthorizationHeader method authHeader:::" + authHeader);
			String authConfigJson = EnvironmentConfigurationsHandler.getServerProperty("CARDLESS_CASH_AUTH_CONFIG");
			logger.debug("Inside validateAuthorizationHeader method authConfigJson:::" + authConfigJson);
			JSONObject authConfig = new JSONObject(authConfigJson);
			String expectedUsername = authConfig.getString("username");
			logger.debug("Inside validateAuthorizationHeader method expectedUsername:::" + expectedUsername);

			String expectedPassword = authConfig.getString("password");
			logger.debug("Inside validateAuthorizationHeader method isBase64(expectedPassword):::"
					+ isBase64(expectedPassword));
			if (isBase64(expectedPassword)) {
				expectedPassword = decodeBase64Password(expectedPassword);
			}
			logger.debug("Inside validateAuthorizationHeader method expectedPassword:::" + expectedPassword);

			if (authHeader == null || authHeader.isEmpty()) {
				logger.error("Authorization header is missing");
				result.addParam(new Param("responseCode", "E01", FabricConstants.STRING));
				result.addParam(
						new Param("responseMessage", "Authorization header is missing", FabricConstants.STRING));
				return result;
			}

			// Checking Basic prefix
			if (!authHeader.startsWith("Basic ")) {
				logger.error("Invalid Authorization type");
				result.addParam(new Param("responseCode", "E02", FabricConstants.STRING));
				result.addParam(new Param("responseMessage", "Invalid Authorization type", FabricConstants.STRING));
				return result;
			}

			// Remove "Basic "
			String base64Credentials = authHeader.substring(6).trim();
			logger.debug("Inside validateAuthorizationHeader method base64Credentials:::" + base64Credentials);

			// Decode Base64
			byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);

			String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
			logger.debug("Inside validateAuthorizationHeader method credentials:::" + credentials);

			// username:password
			String[] values = credentials.split(":", 2);
			logger.debug("Inside validateAuthorizationHeader method values:::" + values);

			if (values.length != 2) {
				logger.error("Invalid credential format");
				result.addParam(new Param("responseCode", "E04", FabricConstants.STRING));
				result.addParam(new Param("responseMessage", "Invalid credential format", FabricConstants.STRING));
				return result;
			}

			String username = values[0];
			String password = values[1];

			logger.debug("Received username : " + username);
			logger.debug("Received password : " + password);

			// Validate credentials
			if (!expectedUsername.trim().equals(username.trim()) || !expectedPassword.trim().equals(password.trim())) {
				logger.error("Authentication failed");
				result.addParam(new Param("responseCode", "401"));
				result.addParam(new Param("responseMessage", "Unauthorized Request"));
				return result;
			}
			logger.debug("Authentication successful");
			result.addParam(new Param("responseCode", "00", FabricConstants.STRING));
			result.addParam(new Param("responseMessage", "Authentication successful", FabricConstants.STRING));
			logger.debug("In validateAuthorizationHeader final result:::" + ResultToJSON.convert(result));
			return result;
		} catch (IllegalArgumentException e) {
			logger.error("Invalid Base64 encoded credentials", e);
			result.addParam(new Param("responseCode", "1003", FabricConstants.STRING));
			result.addParam(new Param("responseMessage", "Invalid Base64 encoded credentials", FabricConstants.STRING));
			return result;
		} catch (Exception e) {
			logger.error("Exception while validating auth header", e);
			result.addParam(new Param("responseCode", "1004", FabricConstants.STRING));
			result.addParam(
					new Param("responseMessage", "Error validating authorization header", FabricConstants.STRING));
			return result;
		}
	}

	private boolean isBase64(String value) {
		if (value == null || value.trim().isEmpty()) {
			return false;
		}

		try {
			byte[] decodedBytes = Base64.getDecoder().decode(value);
			String reEncodedValue = Base64.getEncoder().encodeToString(decodedBytes);
			return value.trim().equals(reEncodedValue);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private String decodeBase64Password(String password) {
		return new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
	}
}
