package com.bct.javaservices;

import static com.temenos.infinity.api.QRPayments.constants.Constants.INTERNAL_BANK_ACCOUNTS;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.javaservices.QRErrorCode.ErrorCode;
import com.bct.utilities.CustomerCacheUtil;
import com.bct.utilities.QRCodeEMVUtil;
import com.bct.utilities.QRStringMatchUtil;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.emv.qrcode.validators.Crc16Validate;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.QRPayments.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.utils.CustomerSession;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;
import com.temenos.infinity.api.srmstransactions.utils.MemoryManagerUtils;

import br.com.fluentvalidator.context.ValidationResult;

public class QRValidationService implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(QRValidationService.class);
	private static final Map<String, Double> transactionAmountCache = new ConcurrentHashMap<>();

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		logger.debug("In QRValidationService:::");
		QRValidationDTO qrValidationDTO = new QRValidationDTO();

		try {
			String qrDataEncoded = request.getParameter("qrData");
			String amount = request.getParameter("amount");
			String aggSelected = request.getParameter("aggSelected");
			String aggPayload = request.getParameter("aggPayload");
			String fromAccountNumber = request.getParameter("fromAccountNumber");
			qrValidationDTO.setFromAccountNumber(fromAccountNumber);
			if (StringUtils.isBlank(qrDataEncoded)) {
				ErrorCode.ERR_1000.setErrorCode(result);
				return result;
			}
			byte[] decodedBytes = Base64.getDecoder().decode(qrDataEncoded);
			String decodedQRData = new String(decodedBytes, StandardCharsets.UTF_8).trim();
			if (decodedQRData.startsWith("\"") && decodedQRData.endsWith("\"")) {
				decodedQRData = decodedQRData.substring(1, decodedQRData.length() - 1).replace("\\\"", "\"");
			}
			logger.debug("Decoded QR Data:::" + decodedQRData);
			String actualPayload;
			actualPayload = decodedQRData;
			qrValidationDTO.setActualPayload(actualPayload);
			String requestType;
			requestType = isJson(decodedQRData) ? "json" : "emv";
			qrValidationDTO.setRequestType(requestType);
			// Validating customerId from session
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			logger.debug("customer object in session::" + customer);
			String customerId;
			customerId = CustomerSession.getCustomerId(customer);
			logger.debug("customerId from session obj::" + customerId);
			if (customerId == null)
				return ErrorCodeEnum.ERR_12603.setErrorCode(result);
			JSONObject customerDetails;
			customerDetails = CustomerCacheUtil.fetchAndCacheCustomerDetails(customerId, request);
			logger.debug("customerDetails from CustomerCacheUtil:::" + customerDetails);
			// Validating Account Number for the customer
			if (!validateFromAccount(fromAccountNumber, customerId)) {
				logger.debug("User is not Authorized for the account number");
				return ErrorCodeEnum.ERR_12604.setErrorCode(result);
			}
			double amountValue = Double.parseDouble(String.valueOf(amount.equals("") ? 0.0 : amount));
			logger.debug("amountValue:::" + amountValue);
			// Validating amount value
			if (amountValue <= 0.0)
				return ErrorCodeEnum.ERR_27017.setErrorCode(new Result());
			if (isJson(decodedQRData)) {
				return validatePersonalQR(new JSONObject(decodedQRData), amount, fromAccountNumber, result, request,
						qrValidationDTO);
			} else {
				return validateMerchantQR(decodedQRData, amount, fromAccountNumber, aggSelected, result, request,
						customerId, qrValidationDTO);
			}

		} catch (IllegalArgumentException e) {
			logger.error("Invalid Base64 input for QR Data", e);
			ErrorCode.ERR_1010.setErrorCode(result);
		} catch (Exception e) {
			logger.error("Exception occured in QRValidationService:::" + e.getMessage(), e);
			result.addParam(new Param("dbpErrCode", "1001"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("success", "false"));
		}

		return result;
	}

	// Method to check if input is a valid JSON string
	private boolean isJson(String data) {
		try {
			new JSONObject(data); // Attempt to parse JSON
			return true;
		} catch (JSONException e) {
			return false;
		}
	}

	// Handling Personal QR Validation
	private Result validatePersonalQR(JSONObject qrJson, String amount, String fromAccountNumber, Result result,
			DataControllerRequest request, QRValidationDTO qrValidationDTO) {
		logger.debug("In QRValidationService validatePersonalQR method qrJson:::" + qrJson.toString());
		String transactionId = UUID.randomUUID().toString();
		qrValidationDTO.setTransactionId(transactionId);
		String validationRequest = qrJson.toString();
		qrValidationDTO.setValidationRequest(validationRequest);
		String accountNumber = qrJson.optString("accountNumber");
		String accountName = qrJson.optString("accountName");
		String bankCode = qrJson.optString("bankCode");
		logger.debug("In QRValidationService validatePersonalQR method bankCode:::" + bankCode);
		String aggregatorType = "";
		String aggregatorName = "";
		double transactionAmount = 0.00;
		double transactionFee = 0.00;
		double debitAmount = 0.00;
		logger.debug("In QRValidationService validatePersonalQR qrJson.has(\"bankCodeCIPS\"):::"
				+ qrJson.has("bankCodeCIPS"));

		if ((qrJson.has("bankCode") && !"HIMANPKA".equalsIgnoreCase(qrJson.optString("bankCode")))
				|| qrJson.has("bankCodeCIPS")) {
			logger.debug("validationType:::Other Domestic Bank Account");
			String bankCodeCIPS = "";
			if (qrJson.has("bankCodeCIPS"))
				bankCodeCIPS = qrJson.optString("bankCodeCIPS");
			logger.debug("validationType:::Other Domestic Bank Account bankCodeCIPS:::" + bankCodeCIPS);
			try {
				callOtherBankAccountValidationAPI(accountNumber, bankCodeCIPS, accountName, transactionId, amount,
						fromAccountNumber, bankCode, result, request, qrValidationDTO);
			} catch (Exception e) {
				logger.error("Exception occured while calling other bank account validation api:::" + e.getMessage(),
						e);
				result.addParam(new Param("dbpErrCode", "1013"));
				result.addParam(new Param("dbpErrMsg", e.getMessage()));
				result.addParam(new Param("success", "false"));
				String validationResult = "ERROR: " + e.getMessage();
				String status = "FAILED";
				qrValidationDTO.setValidationResult(validationResult);
				qrValidationDTO.setStatus(status);
			}
		} else {
			logger.debug("validationType:::HBL Bank Account");
			aggregatorType = "agg-3";
			aggregatorName = HBLConstants.PERSONAL_QR_SAME_BANK;
			qrValidationDTO.setAggregatorType(aggregatorType);
			qrValidationDTO.setAggregatorName(aggregatorName);
			// Validating fromAccountNumber and toAccountNumber
			if ((fromAccountNumber != null && fromAccountNumber != "") && (accountNumber != null && accountNumber != "")
					&& fromAccountNumber.equals(accountNumber)) {
				return ErrorCodeEnum.ERR_12307.setErrorCode(new Result());
			}
			String isSameBankValidationEnabled = EnvironmentConfigurationsHandler
					.getServerProperty("QR_SAMEBANK_VALIDATION_ENABLED");
			logger.debug("isSameBankValidationEnabled from server property ::: " + isSameBankValidationEnabled);
			try {
				if (isSameBankValidationEnabled.equals("true")) {
					logger.debug("Performing same bank validation:::");
					// Validating toAccountNumber
					Map<String, Object> inputMap = new HashMap<>();
					inputMap.put("accountNumber", accountNumber);
					Map<String, Object> headerMap = request.getHeaderMap();
					logger.debug("validationType:::HBL Bank Account params:::" + inputMap);
					String response = DBPServiceExecutorBuilder.builder().withOperationId("getBeneficiaryNameQR")
							.withRequestParameters(inputMap).withServiceId("T24ISPaymentsView")
							.withRequestHeaders(headerMap).build().getResponse();
					logger.debug("response:::" + response);
					JSONObject jsonResponse = new JSONObject(response);
					String beneficiaryName = jsonResponse.getString("beneficiaryName");
					logger.debug("beneficiaryName:::" + beneficiaryName);
					String supportTransferTo = jsonResponse.getString("supportTransferTo");
					logger.debug("supportTransferTo:::" + supportTransferTo);

					if (StringUtils.isNotBlank(beneficiaryName)) {
						String matchPercentage = EnvironmentConfigurationsHandler
								.getServerProperty("QR_SAME_BANK_VALIDATION_MATCH_PERCENTAGE");
						logger.debug("matchPercentage from server property:::" + matchPercentage);
						int percentageMatch = Integer.parseInt(matchPercentage);
						int percentage = QRStringMatchUtil.getAccountMatchPercentage(beneficiaryName, accountName);
						logger.debug("percentage match in same bank validation:::" + percentage);
						if (percentage >= percentageMatch && supportTransferTo.equals("1")) {
							transactionAmount = Double.parseDouble(amount);
							transactionFee = FeeCacheUtil.getFee(aggregatorType, transactionAmount);
							debitAmount = transactionAmount + transactionFee;
							result.addParam("amount", String.format("%.2f", transactionAmount));
							result.addParam("transactionFee", String.format("%.2f", transactionFee));
							result.addParam("debitAmount", String.format("%.2f", debitAmount));
							result.addParam("transactionId", transactionId);
							result.addParam("success", "true");
							result.addParam("aggregatorType", aggregatorName);
							String validationResult = ResultToJSON.convert(result).toString();
							String status = "SUCCESS";
							qrValidationDTO.setValidationResult(validationResult);
							qrValidationDTO.setStatus(status);
							qrValidationDTO.setAmount(transactionAmount);
							qrValidationDTO.setDebitAmount(debitAmount);
							qrValidationDTO.setTransactionFee(transactionFee);
						} else if (supportTransferTo.equals("0")) {
							ErrorCode.ERR_1020.setErrorCode(result);
							String validationResult = "ERROR: "
									+ "The account number entered is not eligible to receive funds, try using a different account number "
									+ accountNumber;
							String status = "FAILED";
							result.addParam("aggregatorType", aggregatorName);
							qrValidationDTO.setValidationResult(validationResult);
							qrValidationDTO.setStatus(status);
						} else {
							ErrorCode.ERR_1011.setErrorCode(result);
							String validationResult = "ERROR: " + "Beneficiary name does not match the account name "
									+ beneficiaryName;
							String status = "FAILED";
							result.addParam("aggregatorType", aggregatorName);
							qrValidationDTO.setValidationResult(validationResult);
							qrValidationDTO.setStatus(status);
						}
					} else {
						ErrorCode.ERR_1012.setErrorCode(result);
						result.addParam("aggregatorType", aggregatorName);
						String validationResult = "ERROR: " + "Account Number is not valid";
						String status = "FAILED";
						qrValidationDTO.setValidationResult(validationResult);
						qrValidationDTO.setStatus(status);
					}
				} else {
					logger.debug("Skipped same bank validation:::");
					transactionAmount = Double.parseDouble(amount);
					transactionFee = FeeCacheUtil.getFee(aggregatorType, transactionAmount);
					debitAmount = transactionAmount + transactionFee;
					result.addParam("amount", String.format("%.2f", transactionAmount));
					result.addParam("transactionFee", String.format("%.2f", transactionFee));
					result.addParam("debitAmount", String.format("%.2f", debitAmount));
					result.addParam("transactionId", transactionId);
					result.addParam("success", "true");
					result.addParam("aggregatorType", aggregatorName);
					String validationResult = ResultToJSON.convert(result).toString();
					String status = "SUCCESS";
					qrValidationDTO.setValidationResult(validationResult);
					qrValidationDTO.setStatus(status);
					qrValidationDTO.setAmount(transactionAmount);
					qrValidationDTO.setDebitAmount(debitAmount);
					qrValidationDTO.setTransactionFee(transactionFee);
				}
			} catch (Exception e) {
				logger.error("Exception occured while validating the personal qr:::" + e.getMessage(), e);
				result.addParam(new Param("dbpErrCode", "1002"));
				result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
				result.addParam(new Param("success", "false"));
				result.addParam("aggregatorType", aggregatorName);
				String validationResult = "ERROR: " + e.getMessage();
				String status = "FAILED";
				qrValidationDTO.setValidationResult(validationResult);
				qrValidationDTO.setStatus(status);
			}
		}
		logger.debug("insertQRValidationResult started:::");
		insertQRValidationResult(qrValidationDTO);
		return result;
	}

	// Handling Merchant QR Validation
	private Result validateMerchantQR(String qrString, String amount, String fromAccountNumber, String aggSelected,
			Result result, DataControllerRequest request, String customerId, QRValidationDTO qrValidationDTO) {
		logger.debug("In QRValidationService validateMerchantQR:::");

		ValidationResult validationResult = Crc16Validate.validate(qrString);

		if (!validationResult.isValid()) {
			result.addParam(new Param("dbpErrCode", "1003"));
			result.addParam(new Param("dbpErrMsg", "CRC validation failed: " + validationResult.getErrors()));
			result.addParam(new Param("success", "false"));
			return result;
		}
		Map<String, String> emvMap;
		// Parse the EMV QR using the utility method if CRC is valid
		emvMap = QRCodeEMVUtil.parseEMVQRCode(qrString);
		logger.debug("In QRValidationService validateMerchantQR emvMap:::" + emvMap);

		String qrTransactionAmount = emvMap.getOrDefault("54", "0").trim();
		logger.debug("Raw QR Transaction Amount:::" + qrTransactionAmount);

		// Extract only the numeric portion
		qrTransactionAmount = qrTransactionAmount.replaceAll("[^0-9.]", "");
		logger.debug("Cleaned QR Transaction Amount:::" + qrTransactionAmount);

		double qrTxnAmount = qrTransactionAmount.isEmpty() ? 0.0 : Double.parseDouble(qrTransactionAmount);

		String finalTransactionAmount = (qrTxnAmount > 0) ? qrTransactionAmount : amount;
		logger.debug("Final Transaction Amount: " + finalTransactionAmount);

		if (isPersonalQR(emvMap)) {
			String accountName = emvMap.getOrDefault("59", ""); // Tag 59 → Account Name
			if (accountName != null && !accountName.isEmpty()) {
				if (accountName.contains(":")) {
					accountName = accountName.substring(accountName.indexOf(":") + 1).trim();
				}
			}
			String accountNumber = "";// Tag 62.07 → Account Number
			String tag62Value = emvMap.get("62");
			if (tag62Value != null && !tag62Value.isEmpty()) {
				if (tag62Value.contains(":")) {
					tag62Value = tag62Value.substring(tag62Value.indexOf(":") + 1).trim();
				}
				if (tag62Value.startsWith("62")) {
					tag62Value = tag62Value.substring(4);
				}
				Map<String, String> tag62Map = flattenTag62(tag62Value);
				logger.debug("Flattened Tag62 Map:::" + tag62Map);
				accountNumber = tag62Map.getOrDefault("62.07", "").trim();
			}
			// Build JSON
			JSONObject personalQRJson = new JSONObject();
			personalQRJson.put("bankCode", "HIMANPKA"); // HBL Swift/BIC code
			personalQRJson.put("accountName", accountName);
			personalQRJson.put("accountNumber", accountNumber);
			logger.info("Personal QR detected: " + personalQRJson.toString());
			// continue personal QR flow
			return validatePersonalQR(personalQRJson, amount, fromAccountNumber, result, request, qrValidationDTO);
		} else {
			logger.info("Not a personal QR");
		}
		String transactionId = UUID.randomUUID().toString();
		qrValidationDTO.setTransactionId(transactionId);
		// Check if tag29 (Nepal Pay) or tag27 (Smart QR) is present in the map
		boolean hasNepalPay = emvMap.containsKey("29");
		boolean hasSmartQR = emvMap.containsKey("27");
		logger.debug("In QRValidationService validateMerchantQR hasNepalPay:::" + hasNepalPay);
		logger.debug("In QRValidationService validateMerchantQR hasSmartQR:::" + hasSmartQR);
		// Fetch aggregator type from server property (force config)
		String configuredAggType = EnvironmentConfigurationsHandler.getServerProperty("QR_AGGREGATOR_TYPE");
		logger.debug("Configured aggregator type from server property ::: " + configuredAggType);

		String finalAggType = null;
		if (StringUtils.isNotBlank(configuredAggType)) {
			finalAggType = configuredAggType;
		} else if (StringUtils.isNotBlank(aggSelected)) {
			finalAggType = aggSelected;
		}
		// If both tags are present, use the finalAggType
		if (hasNepalPay && hasSmartQR) {
			if (StringUtils.isBlank(finalAggType)) {
				result.addParam(new Param("dbpErrCode", "1004"));
				result.addParam(new Param("dbpErrMsg", "Aggregator type not configured or selected"));
				result.addParam(new Param("success", "false"));
				return result;
			} else if (HBLConstants.NEPAL_PAY_AGGREGATOR.equalsIgnoreCase(finalAggType)) {
				callNepalPayValidationAPI(qrString, transactionId, finalTransactionAmount, result, fromAccountNumber,
						customerId, qrValidationDTO);
			} else if (HBLConstants.SMART_QR_AGGREGATOR.equalsIgnoreCase(finalAggType)) {
//				result.addParam(new Param("dbpErrCode", "1006"));
//				result.addParam(new Param("dbpErrMsg", "Smart QR not supported 1"));
//				result.addParam(new Param("success", "false"));
//				return result;
//				callSmartQRValidationAPI(qrString, transactionId, finalTransactionAmount,
//				result, fromAccountNumber);
				callNepalPayValidationAPI(qrString, transactionId, finalTransactionAmount, result, fromAccountNumber,
						customerId, qrValidationDTO);
				qrValidationDTO.setAggregatorType("agg-2");
				qrValidationDTO.setAggregatorName(HBLConstants.SMART_QR_AGGREGATOR);
			} else {
				result.addParam(new Param("dbpErrCode", "1005"));
				result.addParam(new Param("dbpErrMsg", "Invalid aggregator type configured/selected"));
				result.addParam(new Param("success", "false"));
				return result;
			}
		} else if (hasNepalPay) {
			callNepalPayValidationAPI(qrString, transactionId, finalTransactionAmount, result, fromAccountNumber,
					customerId, qrValidationDTO);
		} else if (hasSmartQR) {
//			result.addParam(new Param("dbpErrCode", "1006"));
//			result.addParam(new Param("dbpErrMsg", "Smart QR not supported 2"));
//			result.addParam(new Param("success", "false"));
//			return result;
//			 callSmartQRValidationAPI(qrString, transactionId, finalTransactionAmount,
//			 result, fromAccountNumber);
			callNepalPayValidationAPI(qrString, transactionId, finalTransactionAmount, result, fromAccountNumber,
					customerId, qrValidationDTO);
			qrValidationDTO.setAggregatorType("agg-2");
			qrValidationDTO.setAggregatorName(HBLConstants.SMART_QR_AGGREGATOR);
		} else {
			result.addParam(new Param("dbpErrCode", "1006"));
			result.addParam(new Param("dbpErrMsg", "QR not supported"));
			result.addParam(new Param("success", "false"));
			return result;
		}
		logger.debug("insertQRValidationResult started:::");
		insertQRValidationResult(qrValidationDTO);
		return result;
	}

	// Method for calling Nepal Pay validation API
	private void callNepalPayValidationAPI(String qrString, String transactionId, String finalTransactionAmount,
			Result result, String fromAccountNumber, String customerId, QRValidationDTO qrValidationDTO) {
		String aggregatorType = "agg-1";
		String aggregatorName = HBLConstants.NEPAL_PAY_AGGREGATOR;
		qrValidationDTO.setAggregatorType(aggregatorType);
		qrValidationDTO.setAggregatorName(aggregatorName);
		double amount = 0.00;
		amount = Double.parseDouble(finalTransactionAmount);
		double FinalTransactionFee = 0.00;
		double debitAmount = 0.00;
		try {
			String insId = generateInsId(customerId);
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();
			inputMap.put("insId", insId);
			inputMap.put("qrString", qrString);
			logger.debug("callNepalPayValidationAPI inputmap:::" + inputMap.toString());
			String validationRequest = new JSONObject(inputMap).toString();
			qrValidationDTO.setValidationRequest(validationRequest);

			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CONNECTIPS_OP_VALIDATE_QR).withRequestParameters(inputMap)
					.withServiceId(HBLURLConstants.CONNECTIPS_QR_SERVICEID).withRequestHeaders(headerMap).build()
					.getResponse();
			logger.debug("callNepalPayValidationAPI response:::" + dbresponse);

			JSONObject jsonResponse = new JSONObject(dbresponse);
			String validationResult = jsonResponse.toString();
			qrValidationDTO.setValidationResult(validationResult);
			// Check for any error message and return early
			if (jsonResponse.has("errMsg")) {
				String errMsg = jsonResponse.optString("errMsg");
				logger.error("Error while validating the qr:::" + errMsg);
				result.addParam(new Param("transactionId", transactionId));
				result.addParam(new Param("dbpErrMsg", errMsg));
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("aggregatorType", HBLConstants.NEPAL_PAY_AGGREGATOR));
				qrValidationDTO.setStatus("FAILED");
				qrValidationDTO.setAmount(amount);
				qrValidationDTO.setDebitAmount(debitAmount);
				qrValidationDTO.setTransactionFee(FinalTransactionFee);
				return;
			}
			String responseCode = jsonResponse.optString("responseCode", "");
			String responseMessage = jsonResponse.optString("responseMessage", "");
			String status = (responseCode.equals("000") && responseMessage.equalsIgnoreCase("SUCCESS")) ? "SUCCESS"
					: "FAILED";
			if (responseCode.equals("000") && responseMessage.equalsIgnoreCase("SUCCESS")) {
				double apiTransactionFee = jsonResponse.optDouble("transactionFee", 0.00); // Fetch transactionFee from
																							// API
				double cachedFee = FeeCacheUtil.getFee(aggregatorType, amount); // Fetch fee from cache
				FinalTransactionFee = apiTransactionFee + cachedFee; // Total transaction fee
				debitAmount = amount + FinalTransactionFee;

				logger.debug("API Transaction Fee: " + apiTransactionFee);
				logger.debug("Cached Fee: " + cachedFee);
				logger.debug("Final Transaction Fee: " + FinalTransactionFee);
				logger.debug("Debit Amount: " + debitAmount);

				result.addParam(new Param("amount", String.format("%.2f", amount)));
				result.addParam(new Param("transactionFee", String.format("%.2f", FinalTransactionFee)));
				result.addParam(new Param("debitAmount", String.format("%.2f", debitAmount)));
				result.addParam(new Param("success", "true"));
				result.addParam(new Param("aggregatorType", HBLConstants.NEPAL_PAY_AGGREGATOR));
				result.addParam(new Param("transactionId", transactionId));
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("responseMessage", responseMessage));
			} else if (responseCode.equals("001")) {
				result.addParam(new Param("aggregatorType", HBLConstants.NEPAL_PAY_AGGREGATOR));
				result.addParam(new Param("transactionId", transactionId));
				ErrorCode.ERR_1019.setErrorCode(result);
			} else {
				result.addParam(new Param("aggregatorType", HBLConstants.NEPAL_PAY_AGGREGATOR));
				result.addParam(new Param("transactionId", transactionId));
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("dbpErrMsg", responseMessage));
				result.addParam(new Param("success", "false"));
			}
			qrValidationDTO.setStatus(status);
			qrValidationDTO.setAmount(amount);
			qrValidationDTO.setDebitAmount(debitAmount);
			qrValidationDTO.setTransactionFee(FinalTransactionFee);
			logger.debug("Response Code: " + responseCode);
			logger.debug("Response Message: " + responseMessage);
		} catch (Exception e) {
			logger.error("Exception occured while validating the qr:::" + e.getMessage(), e);
			result.addParam(new Param("dbpErrCode", "1007"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
			result.addParam(new Param("aggregatorType", HBLConstants.NEPAL_PAY_AGGREGATOR));
			String validationResult = "ERROR: " + e.getMessage();
			String status = "FAILED";
			qrValidationDTO.setValidationResult(validationResult);
			qrValidationDTO.setStatus(status);
		}
	}

	public static String generateInsId(String customerId) {
		final int TOTAL_LENGTH = 20;
		final String PREFIX = HBLConstants.INSTRUCTION_ID_PREFIX;
		if (customerId == null || customerId.trim().isEmpty()) {
			logger.warn("Customer ID is null or empty — using default 00000");
			customerId = "00000";
		}
		String base = PREFIX + customerId;
		logger.debug("generateInsId base: " + base);
		int remainingLength = TOTAL_LENGTH - base.length();
		if (remainingLength <= 0) {
			throw new IllegalArgumentException("Customer ID too long to generate 20-char Instruction ID");
		}
		int random = (int) (Math.random() * 900) + 100; // 100–999
		// Timestamp with milliseconds (yyMMddHHmmssSSS → 15 chars)
		String timestamp = new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date());
		String uniquePart = random + timestamp;
		if (uniquePart.length() > remainingLength) {
			uniquePart = uniquePart.substring(0, remainingLength);
		} else if (uniquePart.length() < remainingLength) {
			uniquePart = String.format("%1$-" + remainingLength + "s", uniquePart).replace(' ', '0');
		}
		String instructionId = base + uniquePart;
		logger.debug("Generated Instruction ID (final): " + instructionId);
		return instructionId;
	}

	// Method for calling Smart QR validation API
	private void callSmartQRValidationAPI(String qrString, String transId, String finalTransactionAmount, Result result,
			String fromAccountNumber, Map<String, String> emvMap, JSONObject customerDetails, String customerId,
			String actualPayload, String requestType, QRValidationDTO qrValidationDTO) {
		double amount = 0.00;
		double cachedFee = 0.00;
		double debitAmount = 0.00;
		try {
			String validationTraceId = generateInsId(customerId);
			String transactionId = convertUUIDtoChar(transId);
			String merchantID;
			merchantID = EnvironmentConfigurationsHandler.getServerProperty("SMARTQR_VALIDATE_SERVICE_MERCHANT_ID");
			if (StringUtils.isBlank(merchantID)) {
				logger.debug("callSmartQRValidationAPI merchantID is blank:::");
				merchantID = emvMap.get("27.00");
			}
			logger.debug("callSmartQRValidationAPI merchantID is {}", merchantID);
			JSONObject userObj = customerDetails.getJSONArray("user").getJSONObject(0);

			String custId = "";
			JSONArray customersArr = userObj.optJSONArray("customers");
			if (customersArr != null && customersArr.length() > 0) {
				JSONObject customerObj = customersArr.optJSONObject(0);
				if (customerObj != null) {
					custId = customerObj.optString("customerId");
				}
			}
			if (StringUtils.isBlank(custId)) {
				custId = userObj.optString("id");
			}

			String mobileNo = "";
			JSONArray contactNumbers = userObj.optJSONArray("ContactNumbers");
			if (contactNumbers != null && contactNumbers.length() > 0) {
				JSONObject mobileObj = contactNumbers.getJSONObject(0);
				mobileNo = mobileObj.optString("Value");
			} else {
				JSONArray contactDetails = userObj.optJSONArray("contactDetails");
				if (contactDetails != null && contactDetails.length() > 0) {
					mobileNo = contactDetails.getJSONObject(0).optString("phone");
				}
			}

			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();
			inputMap.put("validationTraceId", validationTraceId);
			inputMap.put("CustId", custId);
			inputMap.put("CustMob", mobileNo);
			inputMap.put("TranID", transactionId);
			inputMap.put("TranAmount", finalTransactionAmount);
			inputMap.put("qrString", qrString);
			inputMap.put("MerchantID", merchantID);

			logger.debug("callSmartQRValidationAPI inputMap:::" + inputMap.toString());
			String validationRequest = new JSONObject(inputMap).toString();

			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.SMARTQR_OP_MERCHANT_VERIFICATION).withRequestParameters(inputMap)
					.withServiceId(HBLURLConstants.SMARTQR_SERVICEID).withRequestHeaders(headerMap).build()
					.getResponse();

			logger.debug("callSmartQRValidationAPI response:::" + response);

			JSONObject jsonResponse = new JSONObject(response);
			String responseStatus = jsonResponse.optString("Status", "");
			String responseMessage = jsonResponse.optString("Message", "");
			String dbpErrMsg = "";

			String status;
			switch (responseStatus) {
			case "00":
				status = "SUCCESS";
				responseMessage = "Approved";
				break;
			case "05":
				status = "FAILED";
				dbpErrMsg = "Unable to Process";
				break;
			default:
				status = "FAILED";
				dbpErrMsg = "Not Approved";
				break;
			}

			String aggregatorType = "agg-2";
			String aggregatorName = HBLConstants.SMART_QR_AGGREGATOR;
			String validationResult = jsonResponse.toString();

			if (status.equals("SUCCESS")) {
				amount = Double.parseDouble(finalTransactionAmount);
				cachedFee = FeeCacheUtil.getFee(aggregatorType, amount);
				debitAmount = amount + cachedFee;

				logger.debug("Cached Fee: " + cachedFee);
				logger.debug("Debit Amount: " + debitAmount);

				result.addParam(new Param("amount", String.format("%.2f", amount)));
				result.addParam(new Param("transactionFee", String.format("%.2f", cachedFee)));
				result.addParam(new Param("debitAmount", String.format("%.2f", debitAmount)));
				result.addParam(new Param("responseMessage", responseMessage));
			} else {
				result.addParam(new Param("dbpErrMsg", dbpErrMsg));
			}
			qrValidationDTO.setTransactionId(transactionId);
			qrValidationDTO.setAggregatorType(aggregatorType);
			qrValidationDTO.setAggregatorName(aggregatorName);
			qrValidationDTO.setValidationRequest(validationRequest);
			qrValidationDTO.setValidationResult(validationResult);
			qrValidationDTO.setStatus(status);
			qrValidationDTO.setAmount(amount);
			qrValidationDTO.setDebitAmount(debitAmount);
			qrValidationDTO.setTransactionFee(cachedFee);
			result.addParam(new Param("aggregatorType", HBLConstants.SMART_QR_AGGREGATOR));
			result.addParam(new Param("transactionId", transactionId));
			result.addParam(new Param("responseStatus", responseStatus));
			result.addParam(new Param("success", status.equals("SUCCESS") ? "true" : "false"));
		} catch (Exception e) {
			logger.error("Exception occurred while validating Smart QR:::" + e.getMessage(), e);
			result.addParam(new Param("dbpErrCode", "1008"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
			result.addParam(new Param("aggregatorType", HBLConstants.SMART_QR_AGGREGATOR));
		}
	}

	private void insertQRValidationResult(QRValidationDTO qrValidationDTO) {
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();

			inputMap.put("transactionId", qrValidationDTO.getTransactionId());
			inputMap.put("aggregatorType", qrValidationDTO.getAggregatorType());
			inputMap.put("aggregatorName", qrValidationDTO.getAggregatorName());
			inputMap.put("fromAccountNumber", qrValidationDTO.getFromAccountNumber());
			inputMap.put("validationRequest", qrValidationDTO.getValidationRequest());
			inputMap.put("validationResult", qrValidationDTO.getValidationResult());
			inputMap.put("amount", qrValidationDTO.getAmount());
			inputMap.put("debitAmount", qrValidationDTO.getDebitAmount());
			inputMap.put("transactionFee", qrValidationDTO.getTransactionFee());
			inputMap.put("status", qrValidationDTO.getStatus());
			inputMap.put("actualPayload", qrValidationDTO.getActualPayload());
			inputMap.put("requestType", qrValidationDTO.getRequestType());
			logger.debug("insertQRValidationResult inputMap:::" + inputMap.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.QR_VALIDATION_RESULTS_CREATE).withRequestParameters(inputMap)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(headerMap).build()
					.getResponse();
			logger.debug(" QRValidationService insertQRValidationResult:::" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				logger.debug(" QRValidationService insertQRValidationResult insertion failed:::");
			} else if (responseJSON.has("qr_validation_results")
					&& responseJSON.getJSONArray("qr_validation_results").length() > 0) {
				logger.debug(" QRValidationService insertQRValidationResult insertion success:::");
			}
			logger.debug(" QRValidationService before inserting amount::");
			putAmount(qrValidationDTO.getTransactionId(), qrValidationDTO.getAmount());
			logger.debug("QRValidationService Stored amount {} in local cache for transactionId {}",
					qrValidationDTO.getAmount(), qrValidationDTO.getTransactionId());
			logger.debug("insertQRValidationResult response:::" + dbResponse);

		} catch (Exception e) {
			logger.error("Exception while inserting QR validation result:::" + e.getMessage(), e);
		}
	}

	private String convertUUIDtoChar(String uuidString) {
		UUID uuid = UUID.fromString(uuidString);
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
		byteBuffer.putLong(uuid.getMostSignificantBits());
		byteBuffer.putLong(uuid.getLeastSignificantBits());

		// Base64 encode (URL-safe and no padding)
		String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
		return base64;
	}

	public static void putAmount(String transactionId, Double amount) {
		logger.debug("QRValidationService putAmount:::");
		transactionAmountCache.put(transactionId, amount);
	}

	public static Double getAmount(String transactionId) {
		return transactionAmountCache.get(transactionId);
	}

	public static void removeAmount(String transactionId) {
		transactionAmountCache.remove(transactionId);
	}

	// Method for calling other bank account validation API
	private void callOtherBankAccountValidationAPI(String accountId, String bankCodeCIPS, String accountName,
			String transactionId, String finalTransactionAmount, String fromAccountNumber, String bankCode,
			Result result, DataControllerRequest request, QRValidationDTO qrValidationDTO) throws Exception {
		String aggregatorType = "agg-4";
		String aggregatorName = HBLConstants.PERSONAL_QR_OTHER_BANK;
		qrValidationDTO.setAggregatorType(aggregatorType);
		qrValidationDTO.setAggregatorName(aggregatorName);
		logger.debug("bankCodeCIPS {}", bankCodeCIPS);
		logger.debug("bankCodeCIPS {}", StringUtils.isBlank(bankCodeCIPS));
		if (StringUtils.isBlank(bankCodeCIPS) || bankCodeCIPS == null || bankCodeCIPS.isEmpty()) {
			logger.debug("bankCodeCIPS was empty. bankCode::: {}", bankCode);
			BankCIPSCacheUtil bankCIPSCacheUtil = new BankCIPSCacheUtil();
			bankCodeCIPS = bankCIPSCacheUtil.getBankCIPSCode(bankCode, request);
			logger.debug("bankCodeCIPS was empty. Retrieved from cache. Resolved value: {}", bankCodeCIPS);
		}
		double amount = 0.00;
		double FinalTransactionFee = 0.00;
		double debitAmount = 0.00;
		amount = Double.parseDouble(finalTransactionAmount);
		double cachedFee = FeeCacheUtil.getFee(aggregatorType, amount); // Fetch fee from cache
		FinalTransactionFee = cachedFee; // Total transaction fee
		debitAmount = amount + FinalTransactionFee;
		logger.debug("Cached Fee: " + cachedFee);
		logger.debug("Final Transaction Fee: " + FinalTransactionFee);
		logger.debug("Debit Amount: " + debitAmount);
		try {
			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();
			inputMap.put("accountId", accountId);
			inputMap.put("bankId", bankCodeCIPS);
			inputMap.put("accountName", accountName);
			logger.debug("callOtherBankAccountValidationAPI inputmap:::" + inputMap.toString());
			String validationRequest = new JSONObject(inputMap).toString();
			qrValidationDTO.setValidationRequest(validationRequest);
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CONNECTIPS_OP_VALIDATE_OTHER_BANK_ACCOUNT)
					.withRequestParameters(inputMap).withServiceId(HBLURLConstants.CONNECTIPS_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("callOtherBankAccountValidationAPI response:::" + dbresponse);

			JSONObject jsonResponse = new JSONObject(dbresponse);
			String validationResult = jsonResponse.toString();
			qrValidationDTO.setValidationResult(validationResult);

			// Check for any error message and return early
			if (jsonResponse.has("errMsg")) {
				String errMsg = jsonResponse.optString("errMsg");
				logger.error("Error while validating the qr:::" + errMsg);
				result.addParam(new Param("dbpErrMsg", errMsg));
				result.addParam(new Param("success", "false"));
				result.addParam(new Param("aggregatorType", HBLConstants.PERSONAL_QR_OTHER_BANK));
				qrValidationDTO.setStatus("FAILED");
				qrValidationDTO.setAmount(amount);
				qrValidationDTO.setDebitAmount(debitAmount);
				qrValidationDTO.setTransactionFee(FinalTransactionFee);
				return;
			}
			JSONObject validateObj = jsonResponse.getJSONObject("validateOtherBankAccount");
			int statusInRes = validateObj.optInt("status", 0);
			String error = validateObj.optString("error", "");
			if (statusInRes == 500 || error.equals("Internal Server Error")) {
				ErrorCode.ERR_1021.setErrorCode(result);
			}

			String responseCode = validateObj.optString("responseCode", "");
			String responseMessage = validateObj.optString("responseMessage", "");
			logger.debug("Response Code: " + responseCode);
			logger.debug("Response Message: " + responseMessage);
			String status = (responseCode.equals("000")
					|| responseMessage.equalsIgnoreCase("Account successfully validated.")) ? "SUCCESS" : "FAILED";
			result.addParam(new Param("aggregatorType", HBLConstants.PERSONAL_QR_OTHER_BANK));
			result.addParam(new Param("transactionId", transactionId));
			if (responseCode.equals("000") || responseMessage.equalsIgnoreCase("Account successfully validated.")) {
				result.addParam(new Param("amount", String.format("%.2f", amount)));
				result.addParam(new Param("transactionFee", String.format("%.2f", FinalTransactionFee)));
				result.addParam(new Param("debitAmount", String.format("%.2f", debitAmount)));
				result.addParam(new Param("success", "true"));
				result.addParam(new Param("aggregatorType", HBLConstants.PERSONAL_QR_OTHER_BANK));
				result.addParam(new Param("transactionId", transactionId));
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("responseMessage", responseMessage));
			} else if (responseCode.equals("999")) {
				int matchPercentage = validateObj.optInt("matchPercentate", 0);
				logger.debug("matchPercentage::: " + matchPercentage);
				if (matchPercentage <= 60) {
					ErrorCode.ERR_1015.setErrorCode(result);
				} else {
					result.addParam(new Param("amount", String.format("%.2f", amount)));
					result.addParam(new Param("transactionFee", String.format("%.2f", FinalTransactionFee)));
					result.addParam(new Param("debitAmount", String.format("%.2f", debitAmount)));
					JSONObject errorObj = new JSONObject();
					errorObj.put("dbpErrCode", "1016");
					errorObj.put("dbpErrMsg",
							"Some difference in beneficiary account name observed. Transaction once sent is irreversible, please reconfirm the beneficiary account number.");
					result.addParam(new Param("errorDetails", errorObj.toString()));
					result.addParam(new Param("success", "true"));
					result.addParam(new Param("responseCode", responseCode));
					result.addParam(new Param("responseMessage", responseMessage));
				}
			} else if (responseCode.equals("502")) {
				ErrorCode.ERR_1014.setErrorCode(result);
			} else if (responseCode.equals("523")) {
				ErrorCode.ERR_1015.setErrorCode(result);
			} else if (responseCode.equals("E999")) {
				ErrorCode.ERR_1017.setErrorCode(result);
			} else if (responseCode.equals("1000")) {
				ErrorCode.ERR_1017.setErrorCode(result);
			} else if (responseCode.equals("504")) {
				ErrorCode.ERR_1018.setErrorCode(result);
			} else {
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("dbpErrMsg", responseMessage));
				result.addParam(new Param("success", "false"));
			}

			qrValidationDTO.setStatus(status);
			qrValidationDTO.setAmount(amount);
			qrValidationDTO.setDebitAmount(debitAmount);
			qrValidationDTO.setTransactionFee(FinalTransactionFee);
		} catch (Exception e) {
			logger.error("Exception occured while validating the other bank account:::" + e.getMessage(), e);
			result.addParam(new Param("dbpErrCode", "1009"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
			result.addParam(new Param("aggregatorType", HBLConstants.PERSONAL_QR_OTHER_BANK));
			String validationResult = "ERROR: " + e.getMessage();
			String status = "FAILED";
			qrValidationDTO.setValidationResult(validationResult);
			qrValidationDTO.setStatus(status);
		}
	}

	private boolean validateFromAccount(String accountId, String customerId) {
		SessionMap accountMap = getInternalAccountsFromSession(customerId);
		logger.debug("accountMap from validateAccount:::" + accountMap);
		if (!accountMap.hasKey(accountId))
			return false;
		String accountType = accountMap.getAttributeValueForKey(accountId, "accountType");
		return accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Checking");
	}

	private SessionMap getInternalAccountsFromSession(String customerId) {
		return (SessionMap) MemoryManagerUtils.retrieve(INTERNAL_BANK_ACCOUNTS + customerId);
	}

	/**
	 * Check if QR is a Personal QR (HBL) based on EMV tags.
	 */
	public static boolean isPersonalQR(Map<String, String> emvMap) {
		if (emvMap == null || emvMap.isEmpty()) {
			return false;
		}
		// Tag 29 → Merchant Account Information
		String tag29Value = emvMap.get("29");
		logger.debug("isPersonalQR tag29Value:::" + tag29Value);
		// --- Flatten Additional Data Field Template (Tag 62) ---
		String purposeOfTxn = "";
		String tag62Value = emvMap.get("62");
		if (tag62Value != null && !tag62Value.isEmpty()) {
			if (tag62Value.contains(":")) {
				tag62Value = tag62Value.substring(tag62Value.indexOf(":") + 1).trim();
			}
			// Skip outer tag and length → start parsing actual sub-tags
			if (tag62Value.startsWith("62")) {
				tag62Value = tag62Value.substring(4);
			}
			Map<String, String> tag62Map = flattenTag62(tag62Value);
			purposeOfTxn = tag62Map.getOrDefault("62.08", "").trim();
		}
		logger.debug("isPersonalQR purposeOfTxn:::" + purposeOfTxn);
		if (tag29Value != null && tag29Value.contains(HBLConstants.MERCHANT_SUB_ID)
				&& HBLConstants.PURPOSE_OF_TRANSACTION.equalsIgnoreCase(purposeOfTxn)) {
			logger.debug("Matched MERCHANT_SUB_ID and PURPOSE_OF_TRANSACTION");
			// Extract 4 digits before MERCHANT_SUB_ID
			int idx = tag29Value.indexOf(HBLConstants.MERCHANT_SUB_ID);
			if (idx >= 4) {
				String fourDigits = tag29Value.substring(idx - 4, idx);
				logger.debug("Extracted Bank Code from tag29Value:::" + fourDigits);
				return HBLConstants.HBL_BANK_CODE.equals(fourDigits);
			}
		}
		return false;
	}

	/**
	 * Parser for Tag 62 (Additional Data Field Template). Produces sub-tags like
	 * 62.01, 62.03, 62.07, 62.08.
	 */
	private static Map<String, String> flattenTag62(String value) {
		Map<String, String> result = new HashMap<>();
		int index = 0;
		while (index + 4 <= value.length()) {
			String tag = value.substring(index, index + 2);
			String lenStr = value.substring(index + 2, index + 4);
			if (!lenStr.matches("\\d+")) {
				// stop if length field is invalid
				break;
			}
			int len = Integer.parseInt(lenStr);
			int start = index + 4;
			int end = start + len;
			if (end > value.length())
				break; // prevent overflow
			String val = value.substring(start, end);
			result.put("62." + tag, val);
			index = end;
		}
		return result;
	}

	public static Result callObjectService(String serviceID, String objid, String operationID,
			Map<String, Object> inputmap, Map<String, Object> headermap, DataControllerRequest dcRequest)
			throws Exception {
		Result result = null;
		try {
			OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder()
					.withServiceId(serviceID).withObjectId(objid).withOperationId(operationID).build();

			ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputmap).withHeaders(headermap)
					.withAuthorizationToken(dcRequest.getParameter("X-Kony-Authorization")).build();
			result = serviceRequest.invokeServiceAndGetResult();
		} catch (Exception e) {
			logger.error("Exception Occured at callObjectService ");
			throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : "
					+ operationID + " objectid : " + objid + " ; " + e.getMessage());
		}
		return result;
	}

}
