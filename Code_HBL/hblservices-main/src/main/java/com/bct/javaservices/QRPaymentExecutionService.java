package com.bct.javaservices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bct.custom.constants.HBLEnums;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.NepalPayTransactionDTO;
import com.bct.custom.dto.QRTransactionDTO;
import com.bct.utilities.CustomerCacheUtil;
import com.bct.utilities.NCHLEncodeDecodeUtil;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.IntraBankFundTransferBackendDTOExtn;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.IntraBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BillPayTransactionBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.infinity.api.arrangements.utils.CustomerSession;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class QRPaymentExecutionService implements JavaService2 {
	public static LoggerUtil logger = new LoggerUtil(QRPaymentExecutionService.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	private static final String PARKING_ACCOUNT_TRANSFER = "PARKING_ACCOUNT_TRANSFER";
	private static final String REVERSE_TRANSACTION_SERVICE = "HBL-T24ISPaymentOrders";
	private static final String REVERSE_TRANSACTION_OPEARATION = "reverseTransaction";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(CustomerBusinessDelegate.class);
		AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(AccountBusinessDelegate.class);
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApplicationBusinessDelegate.class);

		QRTransactionDTO qrTransactionDto = new QRTransactionDTO();
		BillPayTransactionDTO billpaydbxdto = new BillPayTransactionDTO();
		JSONObject customerDetails;
		String customerId;
		String confirmationNumber;
		Result result = new Result();
		logger.debug("In QRPaymentExecutionService:::");

		try {
			String transactionId = request.getParameter("transactionId");
			logger.debug("In QRPaymentExecutionService transactionId:::" + transactionId);
			if (StringUtils.isBlank(transactionId)) {
				result.addParam(new Param("dbpErrCode", "1000"));
				result.addParam(new Param("dbpErrMsg", "Invalid transactionId"));
				result.addParam(new Param("success", "false"));
				return result;
			}
			qrTransactionDto.setTransactionId(transactionId);
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			logger.debug("customer object in session::" + customer);
			customerId = CustomerSession.getCustomerId(customer);
			logger.debug("customerId from session obj::" + customerId);
			if (customerId == null)
				return ErrorCodeEnum.ERR_12603.setErrorCode(result);
			qrTransactionDto.setCustomerId(customerId);
			if (isTransactionAlreadyProcessed(transactionId)) {
				result.addParam(new Param("dbpErrCode", "1008"));
				result.addParam(new Param("dbpErrMsg", "Transaction has already been processed."));
				result.addParam(new Param("success", "false"));
				qrTransactionDto.setStatus("Failed");
				qrTransactionDto.setErrmsg("Transaction has already been processed.");
				insertQRTransactionHistory(request, qrTransactionDto);
				return result;
			}
			// Continue with payment flow
			JSONObject validationDetails = getQRValidationDetails(transactionId);
			logger.debug("validationDetails:::" + validationDetails);
			JSONObject firstResult = validationDetails.optJSONArray("qr_validation_results").optJSONObject(0);
			String fromAccountNumber = firstResult.optString("fromAccountNumber", "");
			String aggregatorType = getAggregatorType(validationDetails);
			if (StringUtils.isBlank(aggregatorType)) {
				result.addParam(new Param("dbpErrCode", "1001"));
				result.addParam(new Param("dbpErrMsg", "Invalid transactionId. Cannot determine aggregator type."));
				result.addParam(new Param("success", "false"));
				return result;
			}
			String createdby = CustomerSession.getCustomerId(customer);
			logger.debug("customerId from session createdby::" + createdby);
			String legalEntityId = (String) customer.get("legalEntityId");
			qrTransactionDto.setCreatedBy(createdby);
			customerDetails = CustomerCacheUtil.getCustomerDetailsFromCache(customerId);
			logger.debug("customerDetails from CustomerCacheUtil:::" + customerDetails);

			CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccountNumber);
			String contractId = account.getContractId();
			String coreCustomerId = account.getCoreCustomerId();
			String companyId = account.getOrganizationId();
			logger.debug("companyId:::" + companyId);
			qrTransactionDto.setCompanyId(companyId);
			String baseCurrency = "NPR";// application.getBaseCurrencyFromCache();
			logger.debug("baseCurrency:::" + baseCurrency);
			String featureActionId = null;
			String toAccountNumber = "";
			switch (aggregatorType) {
			case "agg-1":
				featureActionId = FeatureAction.BILL_PAY_CREATE;
				// featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
				break;
			case "agg-2":
				featureActionId = FeatureAction.BILL_PAY_CREATE;
				// featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
				break;
			case "agg-3":
				featureActionId = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
				String validationRequestStr = firstResult.optString("validationRequest", "{}").replace("'", "\"");
				JSONObject validationRequestJson = new JSONObject(validationRequestStr);
				toAccountNumber = validationRequestJson.optString("accountNumber", "");
				break;
			case "agg-4":
				featureActionId = FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE;
				break;
			default:
				logger.error("Unknown aggregatorType: " + aggregatorType);
			}
			logger.debug("In QRPaymentExecutionService featureActionId:::" + featureActionId);

			AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

			logger.debug("In QRPaymentExecutionService entitlement check:::"
					+ authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
							fromAccountNumber, CustomerSession.IsCombinedUser(customer)));

			if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
					fromAccountNumber, CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
			}
			logger.debug("In QRPaymentExecutionService 186:::");
//			Date date = new Date();
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String str = application.getServerTimeStamp();
			qrTransactionDto.setDate(str);
			logger.debug("In QRPaymentExecutionService str:::" + str);
			Double amount = Double.parseDouble(firstResult.optString("debitAmount", "0.00"));
			logger.debug("In QRPaymentExecutionService 186 amount:::" + amount);
			String serviceCharge = firstResult.optString("transactionFee", "0.00");
			String transactionAmount = firstResult.optString("amount", "0.00");
			Double transAmount = Double.parseDouble(firstResult.optString("amount", "0.00"));
			logger.debug("In QRPaymentExecutionService 186 transAmount:::" + transAmount);
			TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
			transactionStatusDTO.setCustomerId(createdby);
			transactionStatusDTO.setCompanyId(companyId);
			transactionStatusDTO.setAccountId(fromAccountNumber);
			transactionStatusDTO.setAmount(amount);
			transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
			transactionStatusDTO.setDate(str);
			transactionStatusDTO.setTransactionCurrency(baseCurrency);
			transactionStatusDTO.setFeatureActionID(featureActionId);
			transactionStatusDTO.setConfirmationNumber(transactionId);
			transactionStatusDTO.setServiceCharge(serviceCharge);
			transactionStatusDTO.setTransactionAmount(transactionAmount);
			logger.debug("In QRPaymentExecutionService transactionStatusDTO before validation:::"
					+ transactionStatusDTO.toString());
			transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);
			if (transactionStatusDTO == null) {
				logger.debug("In QRPaymentExecutionService transactionStatusDTO:::");
				return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
			}
			if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				logger.debug("In QRPaymentExecutionService transactionStatusDTO.getDbpErrCode():::"
						+ transactionStatusDTO.getDbpErrCode());
				logger.debug("In QRPaymentExecutionService transactionStatusDTO.getDbpErrMsg():::"
						+ transactionStatusDTO.getDbpErrMsg());
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				result.addParam(new Param("status", ErrorCodeEnum.ERR_10420.getMessage()));
				return result;
			}
			logger.debug("In QRPaymentExecutionService transactionStatusDTO:::" + transactionStatusDTO.toString());
			TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
			qrTransactionDto.setStatus(transactionStatus.getStatus());
			qrTransactionDto.setTransactionAmount(transAmount);
			if (transactionStatus == TransactionStatusEnum.SENT) {
				if (aggregatorType.equals("agg-1") || aggregatorType.equals("agg-2")) {
					BillPayTransactionDTO billpayDTO = new BillPayTransactionDTO();
					billpayDTO.setLegalEntityId(legalEntityId);
					billpayDTO.setFromAccountNumber(fromAccountNumber);
					billpayDTO.setTransactionCurrency(baseCurrency);
					billpayDTO.setTransactionAmount(transactionAmount);
					billpayDTO.setFeatureActionId(featureActionId);
					billpayDTO.setCompanyId(companyId);
					billpayDTO.setRoleId(
							customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
					billpayDTO.setCreatedby(createdby);
					billpayDTO.setIsScheduled("0");
					billpayDTO.setStatus(transactionStatus.getStatus());
					billpayDTO.setAmount(transactionStatusDTO.getAmount().doubleValue());
					billpayDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
					billpayDTO.setTransactionId(null);
					billpayDTO.setRequestId(transactionStatusDTO.getRequestId());
					confirmationNumber = (StringUtils.isEmpty(transactionId))
							? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId()
							: transactionId;
					billpayDTO.setConfirmationNumber(confirmationNumber);
					billpayDTO.setPaidBy(HBLConstants.MOBILE_BANKING);
					toAccountNumber = "agg-1".equals(aggregatorType) ? "Nepal Pay"
							: "agg-2".equals(aggregatorType) ? "Smart QR" : "Unknown Aggregator";
					if (StringUtils.isBlank(billpayDTO.getToAccountNumber())) {
						billpayDTO.setToAccountNumber(toAccountNumber);
					}
					billpayDTO.setPayPersonName(aggregatorType);
					billpayDTO.setTransactionType("BillPay");
					String transactionType = "agg-1".equals(aggregatorType) ? "NepalPay"
							: "agg-2".equals(aggregatorType) ? "SmartQR" : "Unknown Aggregator";
					qrTransactionDto.setTransactionType(transactionType);
					billpayDTO.setTransactionCurrency("NPR");
					billpayDTO.setFrequencyTypeId("Once");
					String notes = request.getParameter("narration");
					billpayDTO.setNotes(notes);
					billpayDTO.setIsScheduled("0");
					billpayDTO.setProfileId("QRPayment");
					Date scheduledDate = new Date();
					SimpleDateFormat scheduledDateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String scheduledDateStr = scheduledDateformatter.format(scheduledDate);
					logger.debug("In QRPaymentExecutionService scheduledDateStr:::" + scheduledDateStr);
					billpayDTO.setScheduledDate(scheduledDateStr);
					logger.debug("In QRPaymentExecutionService billpayDTO:::" + billpayDTO.toString());

					billpaydbxdto = createTransactionAtDBX(billpayDTO);
					if (billpaydbxdto == null) {
						alert.prepareError("Error occured while creating entry into the DBX table: ").log();
						return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
					}
					if (billpaydbxdto.getDbpErrCode() != null || billpaydbxdto.getDbpErrMsg() != null) {
						result.addParam(new Param("dbpErrCode", billpaydbxdto.getDbpErrCode()));
						result.addParam(new Param("dbpErrMsg", billpaydbxdto.getDbpErrMsg()));
						return result;
					}
					result = processInternalFundTransfer(validationDetails, request, result, response, qrTransactionDto,
							customerDetails, customerId, confirmationNumber, billpaydbxdto);
				} else if (aggregatorType.equals("agg-4")) {
					qrTransactionDto.setTransactionType("ExternalTransfer");
					result = processConnectIPS(validationDetails, request, result, response, qrTransactionDto,
							customerId, customerDetails);
				} else {
					qrTransactionDto.setTransactionType("InternalTransfer");
					result = processInternalFundTransfer(validationDetails, request, result, response, qrTransactionDto,
							customerDetails, customerId, "", billpaydbxdto);
				}
				logger.debug("HBL:QRInterBankFundTransfer:Final result:" + ResultToJSON.convert(result));
				insertQRTransactionHistory(request, qrTransactionDto);
				try {
					logger.debug("In QRPaymentExecutionService inside log transaction block:::");
					_logTransaction(request, response, inputArray, result, transactionStatus,
							transactionStatusDTO.getConfirmationNumber(), fromAccountNumber, toAccountNumber,
							aggregatorType);
				} catch (Exception e) {
					alert.prepareError("Error occured while audit logging.", e).log();
				}
				// ADP-7058 update additional meta data
				try {
					approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(),
							request);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
			}

		} catch (Exception e) {
			logger.error("Exception occured in QRPaymentExecutionService:::" + e.getMessage(), e);
			result.addParam(new Param("dbpErrCode", "1002"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("success", "false"));
		}
		logger.debug("In QRPaymentExecutionService final result:::" + ResultToJSON.convert(result));
		return result;
	}

	private JSONObject getQRValidationDetails(String transactionId) {
		JSONObject responseObj = new JSONObject();
		Map<String, Object> inputmap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();
		String filter = "transactionId eq '" + transactionId + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("getQRValidationDetails inputmap:::" + inputmap.toString());
		JSONArray results = new JSONArray();
		try {
			String qrValidationResultResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_QR_VALIDATION_RESULTS).withRequestParameters(inputmap)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(headerMap).build()
					.getResponse();
			JSONObject responseJSON = new JSONObject(qrValidationResultResponse);
			logger.debug("qrValidationResultResponse:::" + responseJSON);
			results = responseJSON.getJSONArray("qr_validation_results");
			responseObj.put("qr_validation_results", results);
		} catch (Exception e) {
			logger.error("Exception while fetching QRValidation Results:::", e);
		}
		return responseObj;
	}

	/**
	 * Extracts aggregatorType from the validationDetails JSON.
	 */
	private String getAggregatorType(JSONObject validationDetails) {
		if (validationDetails.has("qr_validation_results")) {
			JSONArray results = validationDetails.optJSONArray("qr_validation_results");
			if (results != null && results.length() > 0) {
				JSONObject firstResult = results.optJSONObject(0);
				return firstResult.optString("aggregatorType", null);
			}
		}
		return null;
	}

	/**
	 * Routes the request to the appropriate payment service based on
	 * aggregatorType.
	 * 
	 * @param result
	 */
	private Result routePaymentService(String aggregatorType, JSONObject validationDetails,
			DataControllerRequest request, Result result, DataControllerResponse response,
			QRTransactionDTO qrTransactionDto, JSONObject customerDetails, String customerId, String confirmationNumber,
			String intraBankDbxTransID, String paymentOrderId, BillPayTransactionDTO billpaydbxdto) {
		logger.debug("Routing to payment service for AggregatorType:::" + aggregatorType);
		switch (aggregatorType) {
		case "agg-1":
			result = populateNepalPayTransactionDTO(validationDetails, request, result, qrTransactionDto,
					customerDetails, customerId, confirmationNumber, intraBankDbxTransID, paymentOrderId, billpaydbxdto,
					aggregatorType);
			return result;
		case "agg-2":
//			result = processSmartQR(validationDetails, request, result);
//			return result;
			result = populateNepalPayTransactionDTO(validationDetails, request, result, qrTransactionDto,
					customerDetails, customerId, confirmationNumber, intraBankDbxTransID, paymentOrderId, billpaydbxdto,
					aggregatorType);
			return result;
		case "agg-3":
			result = processInternalFundTransfer(validationDetails, request, result, response, qrTransactionDto,
					customerDetails, customerId, confirmationNumber, billpaydbxdto);
			return result;
		case "agg-4":
			result = processConnectIPS(validationDetails, request, result, response, qrTransactionDto, customerId,
					customerDetails);
			return result;
		default:
			logger.error("Unknown aggregatorType: " + aggregatorType);
			result.addParam(new Param("dbpErrCode", "1003"));
			result.addParam(new Param("dbpErrMsg", "Unknown aggregatorType: " + aggregatorType));
			result.addParam(new Param("success", "false"));
			return result;
		}
	}

	private Result processInternalFundTransfer(JSONObject validationDetails, DataControllerRequest request,
			Result intraBankTransferResult, DataControllerResponse response, QRTransactionDTO qrTransactionDto,
			JSONObject customerDetails, String customerId, String confirmationNumber,
			BillPayTransactionDTO billpaydbxdto) {
		logger.debug("Processing Internal Fund Transfer in QR flow...");
		try {
			JSONObject firstResult = validationDetails.optJSONArray("qr_validation_results").optJSONObject(0);
			String fromAccount = firstResult.optString("fromAccountNumber", "");
			String aggregatorType = firstResult.optString("aggregatorType");
			// Parse amount and transactionFee
			double amount = 0.0;
			double transactionFee = 0.0;
			try {
				amount = Double.parseDouble(firstResult.optString("debitAmount", "0.00"));
				transactionFee = Double.parseDouble(firstResult.optString("transactionFee", "0.00"));
			} catch (NumberFormatException e) {
				logger.error("Error parsing amount or transactionFee", e);
			}

			String notes = request.getParameter("narration");
			qrTransactionDto.setNotes(notes);

			// Prepare input params
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("fromAccountNumber", fromAccount);
			inputParams.put("paymentAggregator", aggregatorType);
			inputParams.put("amount", amount);
			inputParams.put("serviceCharge", transactionFee);
			inputParams.put("notes", notes);
			inputParams.put("paymentId", UUID.randomUUID().toString());
			inputParams.put("createdby", customerId);

			if ("agg-1".equalsIgnoreCase(aggregatorType)) {
				String validationResultString = firstResult.optString("validationResult", "{}").replace("'", "\"");
				JSONObject validationJson = new JSONObject(validationResultString);
				inputParams.put("transactionCurrency", validationJson.optString("currencyCode", ""));
			} else if ("agg-2".equalsIgnoreCase(aggregatorType)) {
				// logger.debug("Yet to implement for Smart QR");
				String validationResultString = firstResult.optString("validationResult", "{}").replace("'", "\"");
				JSONObject validationJson = new JSONObject(validationResultString);
				inputParams.put("transactionCurrency", validationJson.optString("currencyCode", ""));
			} else if ("agg-3".equalsIgnoreCase(aggregatorType)) {
				String validationRequestStr = firstResult.optString("validationRequest", "{}").replace("'", "\"");
				JSONObject validationRequestJson = new JSONObject(validationRequestStr);
				String toAccount = validationRequestJson.optString("accountNumber", "");
				qrTransactionDto.setToAccountNumber(toAccount);
				String toAccountName = validationRequestJson.optString("accountName", "");
				qrTransactionDto.setToAccountName(toAccountName);
				// To-account details
				inputParams.put("toAccountNumber", toAccount);
				inputParams.put("toAccountName", toAccountName);
				inputParams.put("beneficiaryName", toAccountName);
				inputParams.put("transactionCurrency", "NPR");
			}
			if (customerDetails != null && customerDetails.has("user")
					&& customerDetails.getJSONArray("user").length() > 0) {
				JSONObject userObj = customerDetails.getJSONArray("user").getJSONObject(0);
				String legalEntityId = userObj.optString("companyLegalUnit", "");
				inputParams.put("legalEntityId", legalEntityId);
				if (userObj.has("customers") && userObj.optJSONArray("customers").length() > 0) {
					JSONObject customerObj = userObj.optJSONArray("customers").optJSONObject(0);
					String coreCustomerId = customerObj.optString("coreCustomerId", "");
					inputParams.put("companyId", customerId + "_" + coreCustomerId);
				} else {
					inputParams.put("companyId", customerId + "_");
				}
				String customerName = "";
				JSONArray customerNames = userObj.optJSONArray("customerNames");
				if (customerNames != null && customerNames.length() > 0) {
					JSONObject customerObj = customerNames.getJSONObject(0);
					customerName = customerObj.optString("customerName");
				}
				if (StringUtils.isBlank(customerName)) {
					String first = userObj.optString("FirstName");
					String middle = userObj.optString("MiddleName");
					String last = userObj.optString("LastName");
					customerName = first + (StringUtils.isNotBlank(middle) ? " " + middle : "")
							+ (StringUtils.isNotBlank(last) ? " " + last : "");
				}
				inputParams.put("PayeeName", customerName);
				qrTransactionDto.setFromAccountName(customerName);
				logger.debug("Customer Name set: {}" + qrTransactionDto.getFromAccountName());
			} else {
				inputParams.put("legalEntityId", "");
				inputParams.put("companyId", customerId + "_");
				inputParams.put("PayeeName", "");
			}

			inputParams.put("roleId", "DEFAULT_GROUP");
			inputParams.put("frequencyType", "Once");
			inputParams.put("MFAAttributes", null);
			String parkingAccTransfer = "";
			if ("agg-1".equalsIgnoreCase(aggregatorType) || "agg-2".equalsIgnoreCase(aggregatorType)) {
				String isMerchantQRParkingEnabled = EnvironmentConfigurationsHandler
						.getServerProperty("PARKING_ACC_TRANSFER_MERCHANTQR_ENABLED");
				logger.debug("isMerchantQRParkingEnabled:::" + isMerchantQRParkingEnabled);
				parkingAccTransfer = isMerchantQRParkingEnabled;
			} else {
				parkingAccTransfer = "true";
			}
			logger.debug("parkingAccTransfer:::" + parkingAccTransfer);
			if (parkingAccTransfer.equals("true")) {
				// Call intra bank transaction
				logger.debug("Input params before calling the API:::" + inputParams);
				intraBankTransferResult = createQRIntraBankTransaction(inputParams, request, qrTransactionDto,
						customerId);
				logger.debug("intraBankTransferResult:::" + ResultToJSON.convert(intraBankTransferResult));
				qrTransactionDto.setExternalServiceResponse(ResultToJSON.convert(intraBankTransferResult));
				logger.debug("setExternalServiceResponse:::" + qrTransactionDto.getExternalServiceResponse());
				String paymentOrderId = intraBankTransferResult.getParamValueByName("referenceId");
				Map<String, Object> requestParams = new HashMap<String, Object>();
				requestParams.put("paymentOrderId", paymentOrderId);

				String transactionStatus = intraBankTransferResult.getParamValueByName("transactionStatus");
				logger.debug("QRIntraBankFundTransfer:paymentOrderId:" + paymentOrderId);
				logger.debug("QRIntraBankFundTransfer:transactionStatus:" + transactionStatus);
				if (StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("success")) {
					/*
					 * Checking Transaction current status
					 */
					// String intraBankDbxTransID =
					// intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
					QRInterBankFundTransfer qRInterBankFundTransfer = new QRInterBankFundTransfer();
					JSONObject paymentObj = qRInterBankFundTransfer.getTransactionStatus(requestParams, request);
					transactionStatus = paymentObj != null ? paymentObj.optString("currentStatus") : "";
					logger.debug("QRIntraBankFundTransfer:current transactionStatus:" + transactionStatus);
					if (StringUtils.isNotBlank(transactionStatus) && !transactionStatus.equalsIgnoreCase("Complete")) {
						intraBankTransferResult
								.addParam(new Param("dbpErrCode", ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
						intraBankTransferResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_21210.getMessage()));
						intraBankTransferResult
								.addParam(new Param("message", "Payment Order cannot be completed at this movement."));
						qrTransactionDto.setDescription("PAYMENT_ORDER_NOT_COMPLETED");
						qrTransactionDto.setStatus("FAILED");
						intraBankTransferResult.addParam(new Param("success", "false"));
						JSONObject jsonResponse = new JSONObject();
						JSONObject jsonRequest = new JSONObject(requestParams);
						if (!"agg-3".equalsIgnoreCase(firstResult.optString("aggregatorType"))) {

							// need to update billpaytransfers table here
							updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
									TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, paymentOrderId, "",
									jsonResponse, "PAYMENT_ORDER_NOT_COMPLETED", jsonRequest);
						} else {
							logger.debug("QRIntraBankFundTransfer updating intrabanktransfer transaction");
							Map<String, Object> confirmationDetails = new HashMap<String, Object>();
							confirmationDetails.put("status", "FAILED");
							confirmationDetails.put("transactionId",
									intraBankTransferResult.getParamValueByName("intraBankDbxTransID"));
							confirmationDetails.put("confirmationNumber", paymentOrderId);
							updateIntraBankTransaction(
									intraBankTransferResult.getParamValueByName("intraBankDbxTransID"),
									transactionStatus, confirmationDetails);
						}
					} else if (StringUtils.isNotBlank(transactionStatus)
							&& transactionStatus.equalsIgnoreCase("Complete")) {
						intraBankTransferResult.addParam(new Param("paymentSystemId",
								paymentObj != null ? paymentObj.optString("paymentSystemId") : ""));
					}

				}

				if (intraBankTransferResult.getParamValueByName("dbpErrCode") == null
						&& StringUtils.isNotBlank(paymentOrderId) && StringUtils.isNotBlank(transactionStatus)) {
					logger.debug("Intra-bank debit success. Proceeding with aggregator call...");
					String intraBankDbxTransID = intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
					Map<String, Object> payload = new HashMap<>();
					payload.put("debitReferenceId", paymentOrderId);
					payload.put("intraBankDbxTransID", intraBankDbxTransID);
					payload.put("paymentSystemId", intraBankTransferResult.getParamValueByName("paymentSystemId"));

					qrTransactionDto.setExternalApiPayload((HashMap<String, Object>) payload);
					try {
						if (!"agg-3".equalsIgnoreCase(firstResult.optString("aggregatorType"))) {
							intraBankTransferResult = routePaymentService(firstResult.optString("aggregatorType"),
									validationDetails, request, intraBankTransferResult, response, qrTransactionDto,
									customerDetails, customerId, confirmationNumber, intraBankDbxTransID,
									paymentOrderId, billpaydbxdto);
						}
					} catch (Exception e) {
						alert.prepareError("Caught exception while making the external service calls", e).log();
						intraBankTransferResult = processReversalTransaction(request, qrTransactionDto,
								intraBankDbxTransID);
					}
				} else {
					logger.error("Intra-bank transaction failed.");
					intraBankTransferResult.addParam(
							new Param("dbpErrCode", intraBankTransferResult.getParamValueByName("dbpErrCode")));
					intraBankTransferResult
							.addParam(new Param("dbpErrMsg", intraBankTransferResult.getParamValueByName("dbpErrMsg")));
					intraBankTransferResult.addParam(new Param("success", "false"));
					if (!"agg-3".equalsIgnoreCase(firstResult.optString("aggregatorType"))) {
						JSONObject jsonResponse = new JSONObject();
						JSONObject jsonRequest = new JSONObject(requestParams);
						logger.debug("QRIntraBankFundTransfer updating billpayTransferss transaction");
						// need to update billpaytransfers table here
						updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
								TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, paymentOrderId, "",
								jsonResponse, "FAILED", jsonRequest);
					} else {
						logger.debug("QRIntraBankFundTransfer updating intrabanktransfer transaction");
						Map<String, Object> confirmationDetails = new HashMap<String, Object>();
						confirmationDetails.put("status", "FAILED");
						confirmationDetails.put("transactionId",
								intraBankTransferResult.getParamValueByName("intraBankDbxTransID"));
						confirmationDetails.put("confirmationNumber", paymentOrderId);
						updateIntraBankTransaction(intraBankTransferResult.getParamValueByName("intraBankDbxTransID"),
								transactionStatus, confirmationDetails);

					}
				}
			} else {
				logger.debug("parkingAccTransfer disabled inside else:::" + parkingAccTransfer);
				try {
					if (!"agg-3".equalsIgnoreCase(firstResult.optString("aggregatorType"))) {
						intraBankTransferResult = routePaymentService(firstResult.optString("aggregatorType"),
								validationDetails, request, intraBankTransferResult, response, qrTransactionDto,
								customerDetails, customerId, confirmationNumber, "", "", billpaydbxdto);
					}
				} catch (Exception e) {
					alert.prepareError("Caught exception while making the external service calls", e).log();
					intraBankTransferResult = processReversalTransaction(request, qrTransactionDto, "");
				}
			}
			// Insert history only once after successful routing or internal transfer
			// insertQRTransactionHistory(request);
			return intraBankTransferResult;
		} catch (Exception e) {
			logger.error("Exception in processInternalFundTransfer: {}", e);
			intraBankTransferResult.addParam(new Param("dbpErrCode", "1006"));
			intraBankTransferResult.addParam(new Param("dbpErrMsg", e.getMessage()));
			intraBankTransferResult.addParam(new Param("success", "false"));
			// insertQRTransactionHistory(request);
		}
		return intraBankTransferResult;
	}

	private Result processConnectIPS(JSONObject validationDetails, DataControllerRequest request, Result result,
			DataControllerResponse response, QRTransactionDTO qrTransactionDto, String customerId,
			JSONObject customerDetails) {
		logger.debug("Processing Connect IPS Payment...");
		try {
			JSONObject firstResult = validationDetails.optJSONArray("qr_validation_results").optJSONObject(0);
			String fromAccountNumber = firstResult.optString("fromAccountNumber", "");
			qrTransactionDto.setFromAccountNumber(fromAccountNumber);
			// Parse amount and transactionFee
			double amount = 0.0;
			double transactionFee = 0.0;
			double transactionAmount = 0.0;
			try {
				amount = Double.parseDouble(firstResult.optString("debitAmount", "0.00"));
				transactionFee = Double.parseDouble(firstResult.optString("transactionFee", "0.00"));
				transactionAmount = Double.parseDouble(firstResult.optString("amount", "0.00"));
				qrTransactionDto.setAmount(amount);
				qrTransactionDto.setFee(transactionFee);
				qrTransactionDto.setTransactionAmount(transactionAmount);
			} catch (NumberFormatException e) {
				logger.error("Error parsing amount or transactionFee", e);
			}

			String notes = request.getParameter("narration");
			qrTransactionDto.setNotes(notes);
			// qrTransactionDto.setCreatedBy(customerId);
			String validationRequestStr = firstResult.optString("validationRequest", "{}").replace("'", "\"");
			String validationResponseStr = firstResult.optString("validationResult", "{}").replace("'", "\"");
			logger.debug("Inside Connect IPS Payment...validationResponseStr:::" + validationResponseStr);
			JSONObject validationRequestJson = new JSONObject(validationRequestStr);
			JSONObject validationResponseJson = new JSONObject(validationResponseStr);
			String toAccountNumber = validationRequestJson.optString("accountId", "");
			qrTransactionDto.setToAccountNumber(toAccountNumber);
			String toAccountName = validationRequestJson.optString("accountName", "");
			qrTransactionDto.setToAccountName(toAccountName);
			String bankCodeCIPS = validationRequestJson.optString("bankId", "");
			JSONObject accountValidation = validationResponseJson.getJSONObject("validateOtherBankAccount");
			String branchId = accountValidation.optString("branchId", "1");
			logger.debug("branchId::: " + branchId);

			ZonedDateTime dateTime = ZonedDateTime.of(2024, 12, 12, 0, 0, 0, 0, ZoneOffset.UTC);
			String formattedDate = dateTime.format(DateTimeFormatter.ISO_INSTANT);
			String amountStr = Double.toString(amount);
			String transactionFeeStr = Double.toString(transactionFee);
			String transactionAmountStr = Double.toString(transactionAmount);

			// Payer details
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			logger.debug("Payer details customer object in session::" + customer);
			customerId = CustomerSession.getCustomerId(customer);
			logger.debug("Payer details customerId from session obj::" + customerId);
			qrTransactionDto.setCustomerId(customerId);
			customerDetails = CustomerCacheUtil.getCustomerDetailsFromCache(customerId);
			logger.debug("Payer details customerDetails from CustomerCacheUtil:::" + customerDetails);
			JSONObject userObj = customerDetails.getJSONArray("user").getJSONObject(0);
			String customerName = "";
			JSONArray customerNames = userObj.optJSONArray("customerNames");
			if (customerNames != null && customerNames.length() > 0) {
				JSONObject customerObj = customerNames.getJSONObject(0);
				customerName = customerObj.optString("customerName");
			}
			if (StringUtils.isBlank(customerName)) {
				String first = userObj.optString("FirstName");
				String middle = userObj.optString("MiddleName");
				String last = userObj.optString("LastName");
				customerName = first + (StringUtils.isNotBlank(middle) ? " " + middle : "")
						+ (StringUtils.isNotBlank(last) ? " " + last : "");
			}
			qrTransactionDto.setFromAccountName(customerName);
			BankCIPSCacheUtil cacheUtil = new BankCIPSCacheUtil();
			String beneficiaryBankName = cacheUtil.getBankDetails(bankCodeCIPS, request).optString("bank_name");
			logger.debug("beneficiaryBankName from cache => " + beneficiaryBankName);

			// Prepare input params
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put("amount", amountStr);
			inputParams.put("currency", "NPR");
			inputParams.put("debtorAgent", "0701");
			inputParams.put("debtorBranch", "1");
			inputParams.put("debtorName", customerName);
			inputParams.put("debtorAccount", fromAccountNumber);
			inputParams.put("creditorAgent", bankCodeCIPS);
			inputParams.put("beneficiaryBankName", beneficiaryBankName);
			inputParams.put("creditorBranch", branchId);
			inputParams.put("creditorName", toAccountName);
			inputParams.put("creditorAccount", toAccountNumber);
			inputParams.put("serviceCharge", transactionFeeStr);
			inputParams.put("bankId", bankCodeCIPS);
			inputParams.put("feeCurrency", "NPR");
			inputParams.put("transactionId", "");
			inputParams.put("frequencyType", "Once");
			inputParams.put("fromAccountNumber", fromAccountNumber);
			inputParams.put("iban", "");
			inputParams.put("isScheduled", "0");
			inputParams.put("frequencyStartDate", formattedDate);
			inputParams.put("frequencyEndDate", formattedDate);
			inputParams.put("scheduledDate", formattedDate);
			inputParams.put("numberOfRecurrences", "");
			inputParams.put("toAccountNumber", toAccountNumber);
			inputParams.put("paymentType", "CIPS");
			inputParams.put("paidBy", customerId);
			inputParams.put("swiftCode", "");
			inputParams.put("serviceName", "INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE");
			inputParams.put("beneficiaryName", toAccountName);
			inputParams.put("beneficiaryNickname", "");
			inputParams.put("transactionsNotes", notes);
			inputParams.put("transactionType", "ExternalTransfer");
			qrTransactionDto.setTransactionType("ExternalTransfer");
			inputParams.put("transactionCurrency", "NPR");
			qrTransactionDto.setTransactionCurrency("NPR");
			inputParams.put("fromAccountCurrency", "NPR");
			qrTransactionDto.setFromAccountCurrency("NPR");
			inputParams.put("toAccountCurrency", "NPR");
			qrTransactionDto.setToAccountCurrency("NPR");
			inputParams.put("ExternalAccountNumber", toAccountNumber);
			inputParams.put("uploadedattachments", "");
			inputParams.put("clearingCode", "");
			inputParams.put("intermediaryBicCode", "");
			inputParams.put("e2eReference", "");
			inputParams.put("charges", "");
			inputParams.put("exchangeRate", "");
			inputParams.put("totalAmount", amountStr);
			inputParams.put("creditValueDate", "");
			inputParams.put("transactionAmount", transactionAmountStr);
			inputParams.put("userId", "");
			inputParams.put("deletedDocuments", "");
			inputParams.put("createWithPaymentId", "false");
			inputParams.put("beneficiaryAddressLine1", "");
			inputParams.put("beneficiaryAddressLine2", "");
			inputParams.put("beneficiarycountry", "");
			inputParams.put("beneficiaryState", "");
			inputParams.put("beneficiaryCity", "");
			inputParams.put("beneficiaryZipcode", "");
			inputParams.put("beneficiaryPhone", "");
			inputParams.put("beneficiaryEmail", "");
			inputParams.put("clearingIdentifierCode", "");
			inputParams.put("verifyPayee", "true");
			inputParams.put("payeeCurrency", "NPR");

			logger.debug("Input params before calling the API:::" + inputParams);
			String methodID = ""; // Optional method name
			Object[] inputArray = new Object[inputParams.size() * 2];
			int i = 0;

			for (Map.Entry<String, Object> entry : inputParams.entrySet()) {
				inputArray[i++] = entry.getKey(); // Parameter name

				Map<String, Object> valueMap = new HashMap<>();
				valueMap.put("value", entry.getValue()); // Add even if null
				inputArray[i++] = valueMap;
			}

			// Optional: Log the array contents
			for (int j = 0; j < inputArray.length; j += 2) {
				logger.debug("Param: " + inputArray[j] + " => " + inputArray[j + 1]);
			}

			try {
				QRInterBankFundTransfer qRInterBankFundTransfer = new QRInterBankFundTransfer();
				result = qRInterBankFundTransfer.createTransaction(methodID, inputArray, inputParams, request, response,
						qrTransactionDto);
				String dbpErrMsg = result.getParamValueByName("dbpErrMsg");
				String dbpErrCode = result.getParamValueByName("dbpErrCode");
				String refId = result.getParamValueByName("referenceId");
				String status = result.getParamValueByName("status");
				String message = result.getParamValueByName("message");
				String paymentId = result.getParamValueByName("paymentId");
				qrTransactionDto.setTransactRefId(paymentId);
				qrTransactionDto.setStatus(status);
				qrTransactionDto.setDescription(message);
				logger.debug("processConnectIPS createInterBankFundTransfer transactionResult:::"
						+ ResultToJSON.convert(result));
				if (StringUtils.isNotBlank(dbpErrCode) || StringUtils.isNotBlank(dbpErrMsg)) {
					result.addParam(new Param("status", "failed"));
					qrTransactionDto.setErrmsg(dbpErrMsg);
					qrTransactionDto.setStatus("Failed");
					if (dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage())
							|| dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())) {
						result.addParam(new Param("status", "failed"));
						result.addParam(new Param("success", "false"));
						result.addHttpStatusCodeParam(200);
						result.addParam(new Param("errorMessage", HBLEnums.ERR_20001.getErrorMsg()));
					} else if (StringUtils.isNotBlank(status)
							&& status.equalsIgnoreCase(ErrorCodeEnum.ERR_10420.getMessage())) {
						result.addParam(new Param("dbpErrCode", dbpErrCode));
						result.addParam(new Param("dbpErrMsg", dbpErrMsg));
						result.addParam(new Param("status", "failed"));
						result.addParam(new Param("success", "false"));
					} else if (dbpErrMsg.equalsIgnoreCase(ErrorCodeEnum.ERR_21210.getMessage())) {
						result.addParam(new Param("dbpErrCode", dbpErrCode));
						result.addParam(new Param("dbpErrMsg", dbpErrMsg));
						result.addParam(new Param("status", "failed"));
						result.addParam(new Param("success", "false"));
					} else {
						result.addParam(new Param("status", "failed"));
						result.addParam(new Param("success", "false"));
						return HBLEnums.ERR_20000.setErrorCode(result);
					}
				} else if (StringUtils.isNotBlank(refId)) {
					result.addParam(new Param("opstatus", "0"));
					result.addParam(new Param("status", "success"));
					result.addParam(new Param("success", "true"));
					result.addParam(new Param("referenceId", refId));
					qrTransactionDto.setReferenceId(refId);
					qrTransactionDto.setStatus("Success");
				} else if (StringUtils.isBlank(refId)) {
					qrTransactionDto.setStatus("Failed");
					result.addParam(new Param("opstatus", "0"));
					result.addParam(new Param("status", "failed"));
					result.addParam(new Param("success", "false"));
					HBLEnums.ERR_20000.setErrorCode(result);
				}
				qrTransactionDto.setCustomerId(customerId);
			} catch (Exception e) {
				logger.error("Error occured while invoking CreateInterBankTransaction: " + e.getMessage());
				result.addParam(new Param("status", "failed"));
				qrTransactionDto.setStatus("Failed");
				qrTransactionDto.setErrmsg(e.toString());
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		} catch (Exception e) {
			logger.error("Exception in processInternalFundTransfer: {}", e);
			result.addParam(new Param("dbpErrCode", "1006"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("success", "false"));
			qrTransactionDto.setStatus("Failed");
			qrTransactionDto.setErrmsg(e.toString());
		}
		logger.debug("processConnectIPS final Result:::" + ResultToJSON.convert(result));
		// insertQRTransactionHistory(request);
		return result;
	}

	private Result processNCHL(NepalPayTransactionDTO dto, JSONObject validationDetails, DataControllerRequest request,
			Result result, QRTransactionDTO qrTransactionDto, String confirmationNumber, String paymentOrderId,
			String intraBankDbxTransID, BillPayTransactionDTO billpaydbxdto) {
		logger.debug("Processing NCHL Payment...");
		String isMerchantQRParkingEnabled = EnvironmentConfigurationsHandler
				.getServerProperty("PARKING_ACC_TRANSFER_MERCHANTQR_ENABLED");
		if (isMerchantQRParkingEnabled.equals("false")) {
			result = processNCHLDirectDebit(dto, validationDetails, request, result, qrTransactionDto,
					confirmationNumber, paymentOrderId, intraBankDbxTransID, billpaydbxdto);
		} else {
			result = processNCHLWithoutDirectDebit(dto, validationDetails, request, result, qrTransactionDto,
					confirmationNumber, paymentOrderId, intraBankDbxTransID, billpaydbxdto);
		}
		return result;
	}

	private Result processNCHLWithoutDirectDebit(NepalPayTransactionDTO dto, JSONObject validationDetails,
			DataControllerRequest request, Result result, QRTransactionDTO qrTransactionDto, String confirmationNumber,
			String paymentOrderId, String intraBankDbxTransID, BillPayTransactionDTO billpaydbxdto) {
		logger.debug("Processing NCHL Payment processNCHLWithoutDirectDebit...");
		try {
			Map<String, Object> inputMap = new HashMap<>();

			inputMap.put("instructionId", dto.getInstructionId());
			inputMap.put("validationTraceId", dto.getValidationTraceId());
			inputMap.put("acquirerId", dto.getAcquirerId());
			inputMap.put("merchantPan", dto.getMerchantPan());
			inputMap.put("qrType", dto.getQrType());
			inputMap.put("amount", dto.getAmount());
			qrTransactionDto.setAmount(dto.getAmount());
			inputMap.put("interchangeFee", dto.getInterchangeFee());
			inputMap.put("transactionFee", dto.getTransactionFee());
			inputMap.put("currencyCode", dto.getCurrencyCode());
			inputMap.put("merchantBillNo", dto.getMerchantBillNo());
			inputMap.put("payerName", dto.getPayerName());
			inputMap.put("payerPanId", dto.getPayerPanId());
			inputMap.put("payerMobileNumber", dto.getPayerMobileNumber());
			inputMap.put("payerEmailAddress", dto.getPayerEmailAddress());
			inputMap.put("issuerId", dto.getIssuerId());
			inputMap.put("debtorAccount", dto.getDebtorAccount());
			inputMap.put("debtorAgent", dto.getDebtorAgent());
			inputMap.put("debtorAgentBranch", dto.getDebtorAgentBranch());
			inputMap.put("narration", dto.getNarration());
			inputMap.put("network", dto.getNetwork());
			inputMap.put("acquirerCountryCode", dto.getAcquirerCountryCode());
			inputMap.put("merchantName", dto.getMerchantName());
			inputMap.put("merchantCity", dto.getMerchantCity());
			inputMap.put("merchantCountryCode", dto.getMerchantCountryCode());
			inputMap.put("localTransactionDateTime", dto.getLocalTransactionDateTime());
			inputMap.put("merchantCategoryCode", dto.getMerchantCategoryCode());
			inputMap.put("merchantTxnRef", dto.getMerchantTxnRef());
			inputMap.put("instrumentValue", dto.getInstrument());
			inputMap.put("terminal", dto.getTerminal());
			inputMap.put("encKeySerial", dto.getEncKeySerial());
			inputMap.put("token", dto.getToken());
			inputMap.put("merchantPostalCode", dto.getMerchantPostalCode());
			inputMap.put("categoryPurpose", "DNRQ");

			// Flatten addenda fields
			Map<String, String> addendaMap = dto.getAddenda();
			if (addendaMap != null) {
				for (int i = 1; i <= 10; i++) {
					String key = "addenda" + i;
					inputMap.put(key, addendaMap.getOrDefault(key, ""));
				}
			}

			// Mandatory fields
			List<String> mandatoryFields = Arrays.asList("instructionId", "validationTraceId", "acquirerId",
					"merchantPan", "qrType", "amount", "interchangeFee", "transactionFee", "currencyCode",
					"merchantBillNo", "payerName", "payerPanId", "payerMobileNumber", "issuerId", "debtorAccount",
					"debtorAgent", "debtorAgentBranch", "acquirerCountryCode", "merchantName", "merchantCity",
					"merchantPostalCode", "merchantCountryCode", "localTransactionDateTime", "merchantCategoryCode",
					"instrumentValue", "encKeySerial", "network", "token");

			// Optional fields
			List<String> optionalFields = Arrays.asList("narration", "merchantTxnRef", "terminal", "payerEmailAddress",
					"addenda1", "addenda2", "addenda3", "addenda4", "addenda5", "addenda6", "addenda7", "addenda8",
					"addenda9", "addenda10");

			// Check for missing mandatory fields
			List<String> missingMandatory = new ArrayList<>();
			for (String field : mandatoryFields) {
				Object value = inputMap.get(field);
				if (value == null || value.toString().trim().isEmpty()) {
					missingMandatory.add(field);
				}
			}

			if (!missingMandatory.isEmpty()) {
				String errorMsg = "Missing or empty required fields: " + String.join(", ", missingMandatory);
				logger.error(errorMsg);
				result.addParam(new Param("responseMessage", errorMsg));
				result.addParam(new Param("success", "false"));
				return result;
			}
			// Log empty optional fields as warnings
			for (String field : optionalFields) {
				Object value = inputMap.get(field);
				if (value == null || value.toString().trim().isEmpty()) {
					logger.debug("Optional field is missing or empty: " + field);
				}
			}

			Map<String, Object> headerMap = new HashMap<>();

			logger.debug("callNepalPayTransactionAPI inputMap: {}" + inputMap);
			String paymentServiceRequest = new JSONObject(inputMap).toString();
			qrTransactionDto.setExternalServiceRequest(paymentServiceRequest);
			logger.debug("setExternalServiceRequest:::" + qrTransactionDto.getExternalServiceRequest());
			JSONObject jsonRequest = new JSONObject(inputMap);

			try {
				logger.debug(
						"callNepalPayTransactionAPI inputMap: {}" + new ObjectMapper().writeValueAsString(inputMap));
			} catch (Exception e) {
				logger.error("Error converting inputMap to JSON", e);
			}
			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CONNECTIPS_OP_QR_PAYMENT).withRequestParameters(inputMap)
					.withServiceId(HBLURLConstants.CONNECTIPS_QR_SERVICEID).withRequestHeaders(headerMap).build()
					.getResponse();

			logger.debug("callNepalPayTransactionAPI response: {}" + dbresponse);
			qrTransactionDto.setExternalServiceResponse(dbresponse);
			logger.debug("setExternalServiceResponse:::" + qrTransactionDto.getExternalServiceResponse());
			JSONObject jsonResponse = new JSONObject(dbresponse);
			String responseCode = jsonResponse.optString("responseCode", "");
			String responseMessage = jsonResponse.optString("responseMessage", "");
			qrTransactionDto.setDescription(responseMessage);
			if (responseCode.equals("000") && responseMessage.equalsIgnoreCase("SUCCESS")) {
				String nQrTxnId = jsonResponse.optString("nQrTxnId", "");
				logger.debug("nQrTxnId:::" + nQrTxnId);
				qrTransactionDto.setReferenceId(nQrTxnId);
				qrTransactionDto.setStatus("Success");
				updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
						TransactionStatusEnum.EXECUTED.getStatus(), nQrTxnId, paymentOrderId, "", jsonResponse,
						responseMessage, jsonRequest);
				result.addParam(new Param("success", "true"));
				result.addParam(new Param("referenceId", nQrTxnId));
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("responseMessage", responseMessage));
			} else {
				qrTransactionDto.setStatus("Failed");
				String errmsg = jsonResponse.optString("errmsg", "");
				String responseDescription = jsonResponse.optString("responseDescription", "");
				if (StringUtils.isNotBlank(responseDescription)) {
					qrTransactionDto.setErrmsg(responseDescription);
				} else if (StringUtils.isNotBlank(responseMessage)) {
					qrTransactionDto.setErrmsg(responseMessage);
				} else if (StringUtils.isNotBlank(errmsg)) {
					qrTransactionDto.setErrmsg(errmsg);
				}
				updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
						TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, paymentOrderId, "", jsonResponse,
						responseDescription, jsonRequest);
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("responseMessage", responseMessage));
				result.addParam(new Param("success", "false"));
				result = processReversalTransaction(request, qrTransactionDto, intraBankDbxTransID);
			}
		} catch (Exception e) {
			logger.error("Error in callNepalPayTransactionAPI", e);
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonRequest = new JSONObject();
			updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(), TransactionStatusEnum.FAILED.getStatus(),
					confirmationNumber, paymentOrderId, "", jsonResponse, "", jsonRequest);
			result = processReversalTransaction(request, qrTransactionDto, intraBankDbxTransID);
			result.addParam(new Param("dbpErrCode", "1005"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}

		return result;
	}

	private Result processNCHLDirectDebit(NepalPayTransactionDTO dto, JSONObject validationDetails,
			DataControllerRequest request, Result result, QRTransactionDTO qrTransactionDto, String confirmationNumber,
			String paymentOrderId, String intraBankDbxTransID, BillPayTransactionDTO billpaydbxdto) {
		logger.debug("Processing NCHL Payment processNCHLDirectDebit...");
		try {
			Map<String, Object> inputMap = new HashMap<>();

			inputMap.put("instructionId", dto.getInstructionId());
			inputMap.put("validationTraceId", dto.getValidationTraceId());
			inputMap.put("acquirerId", dto.getAcquirerId());
			inputMap.put("merchantPan", dto.getMerchantPan());
			inputMap.put("qrType", dto.getQrType());
			inputMap.put("amount", dto.getAmount());
			qrTransactionDto.setAmount(dto.getAmount());
			inputMap.put("interchangeFee", dto.getInterchangeFee());
			inputMap.put("transactionFee", dto.getTransactionFee());
			inputMap.put("currencyCode", dto.getCurrencyCode());
			inputMap.put("merchantBillNo", dto.getMerchantBillNo());
			inputMap.put("payerName", dto.getPayerName());
			inputMap.put("payerPanId", dto.getPayerPanId());
			inputMap.put("payerMobileNumber", dto.getPayerMobileNumber());
			inputMap.put("payerEmailAddress", dto.getPayerEmailAddress());
			inputMap.put("issuerId", dto.getIssuerId());
			inputMap.put("debtorAccount", dto.getDebtorAccount());
			inputMap.put("debtorAgent", dto.getDebtorAgent());
			inputMap.put("debtorAgentBranch", dto.getDebtorAgentBranch());
			inputMap.put("narration", dto.getNarration());
			inputMap.put("network", dto.getNetwork());
			inputMap.put("acquirerCountryCode", dto.getAcquirerCountryCode());
			inputMap.put("merchantName", dto.getMerchantName());
			inputMap.put("merchantCity", dto.getMerchantCity());
			inputMap.put("merchantCountryCode", dto.getMerchantCountryCode());
			inputMap.put("localTransactionDateTime", dto.getLocalTransactionDateTime());
			inputMap.put("merchantCategoryCode", dto.getMerchantCategoryCode());
			inputMap.put("merchantTxnRef", dto.getMerchantTxnRef());
			inputMap.put("instrumentValue", dto.getInstrument());
			inputMap.put("terminal", dto.getTerminal());
			inputMap.put("encKeySerial", dto.getEncKeySerial());
			inputMap.put("token", dto.getToken());
			inputMap.put("merchantPostalCode", dto.getMerchantPostalCode());
			// inputMap.put("categoryPurpose", "DNRQ");

			// Flatten addenda fields
			Map<String, String> addendaMap = dto.getAddenda();
			if (addendaMap != null) {
				for (int i = 1; i <= 10; i++) {
					String key = "addenda" + i;
					inputMap.put(key, addendaMap.getOrDefault(key, ""));
				}
			}

			// Mandatory fields
			List<String> mandatoryFields = Arrays.asList("instructionId", "validationTraceId", "acquirerId",
					"merchantPan", "qrType", "amount", "interchangeFee", "transactionFee", "currencyCode",
					"merchantBillNo", "payerName", "payerPanId", "payerMobileNumber", "issuerId", "debtorAccount",
					"debtorAgent", "debtorAgentBranch", "acquirerCountryCode", "merchantName", "merchantCity",
					"merchantPostalCode", "merchantCountryCode", "localTransactionDateTime", "merchantCategoryCode",
					"instrumentValue", "encKeySerial", "network", "token");

			// Optional fields
			List<String> optionalFields = Arrays.asList("narration", "merchantTxnRef", "terminal", "payerEmailAddress",
					"addenda1", "addenda2", "addenda3", "addenda4", "addenda5", "addenda6", "addenda7", "addenda8",
					"addenda9", "addenda10");

			// Check for missing mandatory fields
			List<String> missingMandatory = new ArrayList<>();
			for (String field : mandatoryFields) {
				Object value = inputMap.get(field);
				if (value == null || value.toString().trim().isEmpty()) {
					missingMandatory.add(field);
				}
			}

			if (!missingMandatory.isEmpty()) {
				String errorMsg = "Missing or empty required fields: " + String.join(", ", missingMandatory);
				logger.error(errorMsg);
				result.addParam(new Param("responseMessage", errorMsg));
				result.addParam(new Param("success", "false"));
				return result;
			}
			// Log empty optional fields as warnings
			for (String field : optionalFields) {
				Object value = inputMap.get(field);
				if (value == null || value.toString().trim().isEmpty()) {
					logger.debug("Optional field is missing or empty: " + field);
				}
			}

			Map<String, Object> headerMap = new HashMap<>();

			logger.debug("callNepalPayTransactionAPI inputMap: {}" + inputMap);
			String paymentServiceRequest = new JSONObject(inputMap).toString();
			qrTransactionDto.setExternalServiceRequest(paymentServiceRequest);
			logger.debug("setExternalServiceRequest:::" + qrTransactionDto.getExternalServiceRequest());
			JSONObject jsonRequest = new JSONObject(inputMap);

			try {
				logger.debug(
						"callNepalPayTransactionAPI inputMap: {}" + new ObjectMapper().writeValueAsString(inputMap));
			} catch (Exception e) {
				logger.error("Error converting inputMap to JSON", e);
			}

			String dbresponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.CONNECTIPS_OP_QR_PAYMENT_DIRECT_DEBIT)
					.withRequestParameters(inputMap).withServiceId(HBLURLConstants.CONNECTIPS_QR_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();

			logger.debug("callNepalPayTransactionAPI response: {}" + dbresponse);
			qrTransactionDto.setExternalServiceResponse(dbresponse);
			logger.debug("setExternalServiceResponse:::" + qrTransactionDto.getExternalServiceResponse());
			JSONObject jsonResponse = new JSONObject(dbresponse);
			String responseCode = jsonResponse.optString("responseCode", "");
			String responseMessage = jsonResponse.optString("responseMessage", "");
			qrTransactionDto.setDescription(responseMessage);
			if (responseCode.equals("000") && responseMessage.equalsIgnoreCase("SUCCESS")) {
				String nQrTxnId = jsonResponse.optString("nQrTxnId", "");
				logger.debug("nQrTxnId:::" + nQrTxnId);
				qrTransactionDto.setReferenceId(nQrTxnId);
				qrTransactionDto.setStatus("Success");
				updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
						TransactionStatusEnum.EXECUTED.getStatus(), nQrTxnId, paymentOrderId, "", jsonResponse,
						responseMessage, jsonRequest);
				result.addParam(new Param("success", "true"));
				result.addParam(new Param("referenceId", nQrTxnId));
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("responseMessage", responseMessage));
			} else {
				qrTransactionDto.setStatus("Failed");
				String errmsg = jsonResponse.optString("errmsg", "");
				String responseDescription = jsonResponse.optString("responseDescription", "");
				if (StringUtils.isNotBlank(responseDescription)) {
					qrTransactionDto.setErrmsg(responseDescription);
				} else if (StringUtils.isNotBlank(responseMessage)) {
					qrTransactionDto.setErrmsg(responseMessage);
				} else if (StringUtils.isNotBlank(errmsg)) {
					qrTransactionDto.setErrmsg(errmsg);
				}
				updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
						TransactionStatusEnum.FAILED.getStatus(), confirmationNumber, paymentOrderId, "", jsonResponse,
						responseDescription, jsonRequest);
				result.addParam(new Param("responseCode", responseCode));
				result.addParam(new Param("responseMessage", responseMessage));
				result.addParam(new Param("success", "false"));
			}
		} catch (Exception e) {
			logger.error("Error in callNepalPayTransactionAPI", e);
			JSONObject jsonResponse = new JSONObject();
			JSONObject jsonRequest = new JSONObject();
			updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(), TransactionStatusEnum.FAILED.getStatus(),
					confirmationNumber, paymentOrderId, "", jsonResponse, "", jsonRequest);
			result.addParam(new Param("dbpErrCode", "1005"));
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}

		return result;
	}

	private Result processSmartQR(JSONObject validationDetails, DataControllerRequest request, Result result) {
		logger.debug("Processing Smart QR Payment...");
		// Call Smart QR service here
		return result;
	}

	private Result populateNepalPayTransactionDTO(JSONObject validationDetails, DataControllerRequest request,
			Result result, QRTransactionDTO qrTransactionDto, JSONObject customerDetails, String customerId,
			String confirmationNumber, String intraBankDbxTransID, String paymentOrderId,
			BillPayTransactionDTO billpaydbxdto, String aggregatorType) {
		BillPayTransactionBusinessDelegate billpayTransactionDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(BillPayTransactionBusinessDelegate.class);
		try {
			logger.debug("Starting populateNepalPayTransactionDTO with validationDetails: {}" + validationDetails);

			NepalPayTransactionDTO dto = new NepalPayTransactionDTO();

			if (validationDetails.has("qr_validation_results")) {
				JSONArray results = validationDetails.optJSONArray("qr_validation_results");

				if (results != null && results.length() > 0) {
					JSONObject firstResult = results.optJSONObject(0);
					String validationResultString = firstResult.optString("validationResult", "{}");
					JSONObject validationJson = new JSONObject(validationResultString);
					logger.debug("Extracted validationResult JSON: {}" + validationJson);

					// Setting basic fields from validationResult
					logger.debug("Setting QR fields from validationResult");
					dto.setInstructionId(validationJson.optString("instructionId", ""));
					dto.setValidationTraceId(validationJson.optString("validationTraceId", ""));
					dto.setAcquirerId(validationJson.optString("acquirerId", ""));
					dto.setQrType(validationJson.optString("qrType", ""));
					dto.setCurrencyCode(validationJson.optString("currencyCode", ""));
					qrTransactionDto.setFromAccountCurrency(dto.getCurrencyCode());
					qrTransactionDto.setToAccountCurrency(dto.getCurrencyCode());
					qrTransactionDto.setTransactionCurrency(dto.getCurrencyCode());
					dto.setMerchantPostalCode(validationJson.optString("merchantPostalcode", ""));
					dto.setAcquirerCountryCode(validationJson.optString("acquirerCountryCode", ""));
					dto.setMerchantName(validationJson.optString("merchantName", ""));
					qrTransactionDto.setToAccountName(dto.getMerchantName());
					dto.setMerchantCity(validationJson.optString("merchantCity", ""));
					dto.setMerchantCountryCode(validationJson.optString("merchantCountryCode", ""));
					dto.setMerchantTxnRef(validationJson.optString("merchantTxnRef", ""));
					dto.setTerminal(validationJson.optString("terminal", ""));
					dto.setEncKeySerial(validationJson.optString("encKeySerial", ""));
					dto.setNetwork(validationJson.optString("network", ""));

					String merchantCategoryCode = validationJson.optString("merchantCategoryCode", "");
					logger.debug("merchantCategoryCode: {}" + validationJson.optString("merchantCategoryCode", ""));
					dto.setMerchantCategoryCode(merchantCategoryCode);
					double interchangeFee = validationJson.optDouble("interchangeFee", 0.00);
					dto.setInterchangeFee(interchangeFee);
					logger.debug("Basic QR fields set");

					// PAN decryption & re-encryption
					String encryptedMerchantPan = validationJson.optString("merchantPan", "");
					logger.debug("Encrypted merchantPan retrieved: {}" + encryptedMerchantPan);

					String pfx_File = EnvironmentConfigurationsHandler.getServerProperty("QR_CONNECTIPS_CERT_PATH");
					String pfx_Password = EnvironmentConfigurationsHandler
							.getServerProperty("QR_CONNECTIPS_CERT_PASSWORD");
					String cert_File = EnvironmentConfigurationsHandler
							.getServerProperty("QR_CONNECTIPS_CERT_FILE_PATH");

					logger.debug("Decrypting merchantPan using PFX cert");
					String decryptedMerchantPan = NCHLEncodeDecodeUtil.decryption(encryptedMerchantPan, pfx_File,
							pfx_Password);
					logger.debug("Decrypted merchantPan: {}" + decryptedMerchantPan);

					logger.debug("Re-encrypting merchantPan using public cert");
					String reEncryptedMerchantPan = NCHLEncodeDecodeUtil.encryption(decryptedMerchantPan, cert_File);
					logger.debug("reEncryptedMerchantPan merchantPan: {}" + reEncryptedMerchantPan);
					dto.setMerchantPan(reEncryptedMerchantPan);

					// Addenda
					logger.debug("Setting addenda fields");
					for (int i = 1; i <= 10; i++) {
						String key = "addenda" + i;
						String value = validationJson.optString(key, "");
						dto.getAddenda().put(key, value);
					}
					logger.debug("Addenda fields set");

					// Amount & fees
					double amount = 0.0;
					double transactionFee = 0.0;

					try {
						amount = Double.parseDouble(firstResult.optString("amount", "0.00"));
						transactionFee = Double.parseDouble(firstResult.optString("transactionFee", "0.00"));
					} catch (NumberFormatException e) {
						logger.error("Error parsing amount or transactionFee", e);
					}

					String narration = request.getParameter("narration");

					logger.debug(
							"Transaction details - Amount: {}, Transaction Fee: {}, Interchange Fee: {}, Narration: {}"
									+ amount + transactionFee + interchangeFee + narration);

					dto.setAmount(amount);
					dto.setTransactionFee(transactionFee);
					dto.setNarration(narration);
					// qrTransactionDto.setAmount(dto.getAmount());
					qrTransactionDto.setFee(dto.getTransactionFee());
					qrTransactionDto.setNotes(dto.getNarration());
					// String qrType = dto.getQrType();
					dto.setMerchantBillNo(validationJson.optString("merchantBillNo", "0"));
					logger.debug("Merchant Bill No set based on QR type: {}" + dto.getMerchantBillNo());

					// Payer details
					JSONObject userObj = customerDetails.getJSONArray("user").getJSONObject(0);
					String customerName = "";
					JSONArray customerNames = userObj.optJSONArray("customerNames");
					if (customerNames != null && customerNames.length() > 0) {
						JSONObject customerObj = customerNames.getJSONObject(0);
						customerName = customerObj.optString("customerName");
					}
					if (StringUtils.isBlank(customerName)) {
						String first = userObj.optString("FirstName");
						String middle = userObj.optString("MiddleName");
						String last = userObj.optString("LastName");
						customerName = first + (StringUtils.isNotBlank(middle) ? " " + middle : "")
								+ (StringUtils.isNotBlank(last) ? " " + last : "");
					}
					dto.setPayerName(customerName);
					logger.debug("Payer Name set: {}" + dto.getPayerName());
					qrTransactionDto.setFromAccountName(dto.getPayerName());
					// qrTransactionDto.setCreatedBy(customerId);
					String payerPanId = "M" + customerId;
					logger.debug("Generated Payer PAN ID: {}" + payerPanId);

					String encryptedPayerPan = NCHLEncodeDecodeUtil.encryption(payerPanId, cert_File);
					logger.debug("encryptedPayerPan: {}" + encryptedPayerPan);
					dto.setPayerPanId(encryptedPayerPan);
					logger.debug("Encrypted Payer PAN set");

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
					if (StringUtils.isNotBlank(mobileNo) && mobileNo.startsWith("+")) {
						mobileNo = mobileNo.replaceAll("^\\+\\d{1,3}-?", "");
					}
					dto.setPayerMobileNumber(mobileNo);

					String email = userObj.optString("email");
					if (StringUtils.isBlank(email)) {
						JSONArray emailIds = userObj.optJSONArray("EmailIds");
						if (emailIds != null && emailIds.length() > 0) {
							email = emailIds.getJSONObject(0).optString("Value");
						} else {
							JSONArray contactDetails = userObj.optJSONArray("contactDetails");
							if (contactDetails != null && contactDetails.length() > 0) {
								email = contactDetails.getJSONObject(0).optString("email");
							}
						}
					}
					dto.setPayerEmailAddress(email);

					logger.debug("Payer contact info - Mobile: {}, Email: {}" + dto.getPayerMobileNumber()
							+ dto.getPayerEmailAddress());

					String issuerId = EnvironmentConfigurationsHandler.getServerProperty("NCHL_QR_PAY_ISSUER_ID");
					String debtorAgent = EnvironmentConfigurationsHandler.getServerProperty("NCHL_QR_PAY_DEBTOR_AGENT");
					String debtorAgentBranch = EnvironmentConfigurationsHandler
							.getServerProperty("NCHL_QR_PAY_DEBTOR_AGENT_BRANCH");
					dto.setIssuerId(issuerId);
					String isMerchantQRParkingEnabled = EnvironmentConfigurationsHandler
							.getServerProperty("PARKING_ACC_TRANSFER_MERCHANTQR_ENABLED");
					logger.debug("isMerchantQRParkingEnabled {}" + isMerchantQRParkingEnabled);
					if (isMerchantQRParkingEnabled.equals("false"))
						dto.setDebtorAccount(firstResult.optString("fromAccountNumber", ""));
					else
						dto.setDebtorAccount(qrTransactionDto.getToAccountNumber());
					logger.debug("dto.getDebtorAccount() {}" + dto.getDebtorAccount());
					qrTransactionDto.setFromAccountNumber(dto.getDebtorAccount());
					qrTransactionDto.setToAccountNumber("");
					String transactionType = "agg-1".equals(aggregatorType) ? "NepalPay"
							: "agg-2".equals(aggregatorType) ? "SmartQR" : "Unknown Aggregator";
					qrTransactionDto.setTransactionType(transactionType);
					// qrTransactionDto.setTransactionType("NepalPay");
					dto.setDebtorAgent(debtorAgent);
					dto.setDebtorAgentBranch(debtorAgentBranch);
					logger.debug("Debtor details set");

					String localTxnTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
					dto.setLocalTransactionDateTime(localTxnTime);
					logger.debug("Local Transaction Time: {}" + localTxnTime);

					dto.setInstrument("MOB");
					logger.debug("Instrument::: {}" + dto.getInstrument());
					// Token creation
					String token = dto.getValidationTraceId() + "," + dto.getInstructionId() + "," + dto.getAcquirerId()
							+ "," + decryptedMerchantPan + "," + dto.getMerchantCategoryCode() + "," + dto.getAmount()
							+ "," + dto.getTransactionFee() + "," + dto.getInterchangeFee() + ","
							+ dto.getCurrencyCode() + "," + dto.getMerchantName() + "," + dto.getMerchantBillNo() + ","
							+ dto.getTerminal() + "," + dto.getIssuerId() + "," + dto.getPayerName() + ","
							+ dto.getDebtorAgent() + "," + dto.getDebtorAccount();
					logger.debug("Constructed token string: {}" + token);

					String signedToken = signTokenString(token);
					dto.setToken(signedToken);
					logger.debug("Signed token: {}" + signedToken);

					logger.debug("NepalPayTransactionDTO fully populated: {}" + dto);

					result = processNCHL(dto, validationDetails, request, result, qrTransactionDto, confirmationNumber,
							paymentOrderId, intraBankDbxTransID, billpaydbxdto);
					return result;
				} else {
					logger.debug("qr_validation_results is empty or null");
					billpayTransactionDelegate.updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
							TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					result = processReversalTransaction(request, qrTransactionDto, intraBankDbxTransID);
				}
			} else {
				logger.debug("validationDetails does not contain 'qr_validation_results'");
				billpayTransactionDelegate.updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
						TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				result = processReversalTransaction(request, qrTransactionDto, intraBankDbxTransID);
			}
		} catch (Exception e) {
			logger.error("Exception in populateNepalPayTransactionDTO: {}", e);
			billpayTransactionDelegate.updateStatusUsingTransactionId(billpaydbxdto.getTransactionId(),
					TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			result = processReversalTransaction(request, qrTransactionDto, intraBankDbxTransID);
			result.addParam(new Param("dbpErrCode", "1004"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public String signTokenString(String contentToEncode) throws Exception {
		String cips_cert_path = EnvironmentConfigurationsHandler.getServerProperty("QR_CONNECTIPS_CERT_PATH");
		String cips_cert_password = EnvironmentConfigurationsHandler.getServerProperty("QR_CONNECTIPS_CERT_PASSWORD");
		String certPath = cips_cert_path;
		byte[] certStore = Files.readAllBytes(Paths.get(certPath));
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		keyStore.load(new ByteArrayInputStream(certStore), cips_cert_password.toCharArray());
		String alias = keyStore.aliases().nextElement();
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());
		byte[] utf8Data = contentToEncode.getBytes("UTF-8");
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(utf8Data);
		byte[] signedData = signature.sign();
		return Base64.getEncoder().encodeToString(signedData);
	}

	public Result createQRIntraBankTransaction(Map<String, Object> inputParams, DataControllerRequest request,
			QRTransactionDTO qrTransactionDto, String customerId) {
		Result result = new Result();
		try {
			IntraBankFundTransferDTO intrabankDTO = null;
			IntraBankFundTransferBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(IntraBankFundTransferBackendDelegate.class);
			IntraBankFundTransferBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
			IntraBankFundTransferBackendDTOExtn backendDTO = new IntraBankFundTransferBackendDTOExtn();
			IntraBankFundTransferDTO transactionDTO = new IntraBankFundTransferDTO();

			String featureActionId = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
			String serviceName = inputParams.getOrDefault("serviceName", "QR_PAY_CREATE").toString();
			String paymentAggregator = inputParams.getOrDefault("paymentAggregator", "").toString();
			String payableAccountId = null;
			String transactionType = null;

			// Get payable account number from env config
			switch (paymentAggregator.toLowerCase()) {
			case "agg-1": // Nepal Pay
				payableAccountId = EnvironmentConfigurationsHandler
						.getServerProperty("NCHL_QR_PAY_PAYABLE_ACCNOUNT_NO");
				transactionType = PARKING_ACCOUNT_TRANSFER;
				break;
			case "agg-2": // Smart QR
				payableAccountId = EnvironmentConfigurationsHandler
						.getServerProperty("SMARTQR_QR_PAY_PAYABLE_ACCNOUNT_NO");
				transactionType = PARKING_ACCOUNT_TRANSFER;
				break;
			case "agg-3": // Internal Transfer
				payableAccountId = inputParams.getOrDefault("toAccountNumber", "").toString();// EnvironmentConfigurationsHandler.getServerProperty("INTRABANK_PAYABLE_ACCNOUNT_NO");
				qrTransactionDto.setFromAccountCurrency("NPR");
				qrTransactionDto.setToAccountCurrency("NPR");
				// qrTransactionDto.setCreatedBy(customerId);
				transactionType = "InternalTransfer";
				break;
			case "agg-4": // Connect IPS
				payableAccountId = EnvironmentConfigurationsHandler
						.getServerProperty("CONNECTIPS_QR_PAY_PAYABLE_ACCNOUNT_NO");
				transactionType = PARKING_ACCOUNT_TRANSFER;
				break;
			default:
				logger.error("Unknown aggregator type '{}', payable account not set." + paymentAggregator);
				break;
			}

			logger.debug("createQRIntraBankTransaction: PayableAccountId: {}" + payableAccountId);

			// Build inputParams
			inputParams.put("featureActionId", featureActionId);
			inputParams.put("serviceName", serviceName);
			inputParams.put("transactionType", transactionType);
			inputParams.put("toAccountNumber", payableAccountId);
			inputParams.put("ExternalAccountNumber", payableAccountId);
			inputParams.put("beneficiaryName", inputParams.getOrDefault("beneficiaryName", "QR Payment"));
			qrTransactionDto.setToAccountNumber(payableAccountId);

			logger.debug("createQRIntraBankTransaction input: {}" + inputParams);

			try {
				intrabankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IntraBankFundTransferDTO.class);
			} catch (IOException e) {
				logger.error("Failed to parse input params for IntraBankTransfer: {}", e);
				return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
			}

			// Determine channel
			String channel = HBLConstants.MOBILE_BANKING;

			intrabankDTO.setPaidBy(channel);
			intrabankDTO.setpaymentMethod("PAYABLE_ACCOUNT_TRANSFER");
			intrabankDTO.setPayPersonName((String) inputParams.getOrDefault("toAccountName", ""));
			intrabankDTO.setPaymentId((String) inputParams.getOrDefault("paymentId", null));
			intrabankDTO.setTransactionAmount(String.valueOf(inputParams.getOrDefault("amount", "0.00")));
			intrabankDTO.setServiceCharge(String.valueOf(inputParams.getOrDefault("serviceCharge", "0.00")));
			intrabankDTO.setProfileId("QRPayment");
			Date scheduledDate = new Date();
			SimpleDateFormat scheduledDateformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String scheduledDateStr = scheduledDateformatter.format(scheduledDate);
			logger.debug("In QRPaymentExecutionService scheduledDateStr:::" + scheduledDateStr);
			intrabankDTO.setScheduledDate(scheduledDateStr);
			// Create DBX transaction
			IntraBankFundTransferDTO dbxDTO = createIntraBankTransactionAtDBX(intrabankDTO);
			if (dbxDTO == null) {
				logger.error("DBX transaction creation failed");
				return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
			}

			if (dbxDTO.getDbpErrCode() != null || dbxDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", dbxDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", dbxDTO.getDbpErrMsg()));
				return result;
			}

			String intraBankDbxTransID = dbxDTO.getTransactionId();

			// Prepare backend DTO and call without approval
			backendDTO = mapIntrabankPayloadForHBLTransaction(dbxDTO, qrTransactionDto);
			logger.debug("createQRIntraBankTransaction: backendDTO: {}" + backendDTO);
			Map<String, Object> requestParameters;
			backendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(backendDTO).toString(), String.class, Object.class);
			String intraBankPaymentServiceRequest = new JSONObject(requestParameters).toString();
			qrTransactionDto.setExternalServiceRequest(intraBankPaymentServiceRequest);
			logger.debug("setExternalServiceRequest:::" + qrTransactionDto.getExternalServiceRequest());
			transactionDTO = backendDelegate.createTransactionWithoutApproval(backendDTO, request);
			logger.debug("createQRIntraBankTransaction: Final transactionDTO: {}" + transactionDTO);

			String status = "";
			String referenceId = null;

			if (transactionDTO == null) {
				logger.error("Null response from createTransactionWithoutApproval");
				status = TransactionStatusEnum.FAILED.getStatus();
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}

			if (StringUtils.isNotBlank(transactionDTO.getDbpErrCode())
					|| StringUtils.isNotBlank(transactionDTO.getDbpErrMsg())) {
				result.addParam(new Param("dbpErrCode", transactionDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionDTO.getDbpErrMsg()));
				qrTransactionDto.setErrmsg(transactionDTO.getDbpErrMsg());
				result.addParam(new Param("errorDetails", transactionDTO.getErrorDetails()));
				status = TransactionStatusEnum.FAILED.getStatus();
				result.addParam(new Param("status", status));
			} else if (StringUtils.isBlank(transactionDTO.getReferenceId())) {
				logger.error("Missing reference ID in transactionDTO");
				return ErrorCodeEnum.ERR_12601.setErrorCode(new Result());
			} else {
				logger.debug("createQRIntraBankTransaction: transactionDTO.getReferenceId() {}"
						+ transactionDTO.getReferenceId());
				referenceId = transactionDTO.getReferenceId();
				qrTransactionDto.setReferenceId(referenceId);
				qrTransactionDto.setTransactRefId(referenceId);
				status = TransactionStatusEnum.EXECUTED.getStatus();

				result.addParam(new Param("referenceId", referenceId));
				result.addParam(new Param("paymentSystemId", transactionDTO.getPaymentId()));
				result.addParam(new Param("transactionStatus", transactionDTO.getStatus()));
				result.addParam(new Param("intraBankDbxTransID", intraBankDbxTransID));
				result.addParam(new Param("status", status));
				result.addParam(new Param("message", TransactionStatusEnum.EXECUTED.getMessage()));
				result.addParam(new Param("success", "true"));
			}
			qrTransactionDto.setStatus(status);
			// Final transaction update
			Map<String, Object> confirmationDetails = new HashMap<>();
			confirmationDetails.put("confirmationNumber", referenceId);
			confirmationDetails.put("status", status);
			confirmationDetails.put("transactionId", intraBankDbxTransID);
			confirmationDetails.put("paymentSystemId", transactionDTO.getPaymentId());
			updateIntraBankTransaction(featureActionId, status, confirmationDetails);
			logger.debug("createQRIntraBankTransaction: result {}" + ResultToJSON.convert(result));
		} catch (Exception e) {
			logger.error("Exception occurred in createQRIntraBankTransaction: {}", e);
			result.addParam(new Param("dbpErrCode", "1007"));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public static IntraBankFundTransferBackendDTOExtn mapIntrabankPayloadForHBLTransaction(
			IntraBankFundTransferDTO intrabankdbxDTO, QRTransactionDTO qrTransactionDto) {
		IntraBankFundTransferBackendDTOExtn hblIntrabankBackendDTO = new IntraBankFundTransferBackendDTOExtn();
		try {
			hblIntrabankBackendDTO = hblIntrabankBackendDTO.convert(intrabankdbxDTO);
			String serviceCharge = intrabankdbxDTO.getCharges() == null ? intrabankdbxDTO.getServiceCharge() : "";
			String serviceChargeCurrency = intrabankdbxDTO.getTransactionCurrency();
			String serviceChargeType = "CORRBKCHG";
			hblIntrabankBackendDTO.setTransactionId(null);
			logger.debug("QRPaymentExecutionService:mapIntrabankPayloadForHBLTransaction:getFrequencyTypeId "
					+ intrabankdbxDTO.getFrequencyTypeId());
			hblIntrabankBackendDTO.setFrequencyType(intrabankdbxDTO.getFrequencyTypeId());
			hblIntrabankBackendDTO.setFrequencyTypeId(intrabankdbxDTO.getFrequencyTypeId());
			hblIntrabankBackendDTO.setPaymentOrderProduct("ACTRF");
			hblIntrabankBackendDTO.setCreditAccount(intrabankdbxDTO.getToAccountNumber());
			qrTransactionDto.setToAccountNumber(intrabankdbxDTO.getToAccountNumber());
			hblIntrabankBackendDTO.setDebitAccount(intrabankdbxDTO.getFromAccountNumber());
			qrTransactionDto.setFromAccountNumber(intrabankdbxDTO.getFromAccountNumber());
			hblIntrabankBackendDTO.setPaymentCurrency(intrabankdbxDTO.getTransactionCurrency());
			qrTransactionDto.setTransactionCurrency(intrabankdbxDTO.getTransactionCurrency());
			hblIntrabankBackendDTO.setPaymentAmount(intrabankdbxDTO.getTransactionAmount());
			logger.debug(
					"QRPaymentExecutionService:mapIntrabankPayloadForHBLTransaction:intrabankdbxDTO.getTransactionAmount():::"
							+ intrabankdbxDTO.getTransactionAmount());
			qrTransactionDto.setAmount(Double.parseDouble(intrabankdbxDTO.getTransactionAmount()));
			if (StringUtils.isNotBlank(serviceCharge)) {
				hblIntrabankBackendDTO.setCharges(serviceCharge);
				qrTransactionDto.setFee(Double.parseDouble(serviceCharge));
				hblIntrabankBackendDTO.setServiceCharge(serviceCharge);
				hblIntrabankBackendDTO.setServiceChargeCurrency(serviceChargeCurrency);
				hblIntrabankBackendDTO.setServiceChargeType(serviceChargeType);
			}
			logger.debug(
					"QRPaymentExecutionService:mapIntrabankPayloadForHBLTransaction:intrabankdbxDTO.getTransactionAmount():::"
							+ intrabankdbxDTO.getTransactionAmount());
			hblIntrabankBackendDTO.setTransactionType(intrabankdbxDTO.getTransactionType());
			qrTransactionDto.setTransactionType(intrabankdbxDTO.getTransactionType());
		} catch (Exception e) {
			logger.error("Exception occurred in IntraBankFundTransferBackendDTOExtn: {}", e);
		}
		return hblIntrabankBackendDTO;
	}

	public IntraBankFundTransferDTO updateIntraBankTransaction(String transactionId, String status,
			Map<String, Object> confirmationDetails) {

		List<IntraBankFundTransferDTO> intrabankfundtransferdto = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKTRANSFERS_UPDATE;
		logger.debug(
				"QRPaymentExecutionService::: updateIntraBankTransaction:confirmationDetails:::" + confirmationDetails);
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(confirmationDetails).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray intrabankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			intrabankfundtransferdto = JSONUtils.parseAsList(intrabankJsonArray.toString(),
					IntraBankFundTransferDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONException occured while updating the intrabanktransaction", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Exception occured while updating the intrabanktransaction", exp).log();
			return null;
		}

		if (intrabankfundtransferdto != null && intrabankfundtransferdto.size() != 0)
			return intrabankfundtransferdto.get(0);

		return null;
	}

	private void insertQRTransactionHistory(DataControllerRequest request, QRTransactionDTO qrTransactionDto) {
		try {
//			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
//			logger.debug("Payer details customer object in session::" + customer);
//			String customerId = CustomerSession.getCustomerId(customer);
//			logger.debug("Payer details customerId from session obj::" + customerId);
//			qrTransactionDto.setCustomerId(customerId);

			Map<String, Object> inputMap = new HashMap<>();
			Map<String, Object> headerMap = new HashMap<>();

			inputMap.put("Customer_id", qrTransactionDto.getCustomerId());
			inputMap.put("fromAccountNumber", qrTransactionDto.getFromAccountNumber());
			inputMap.put("toAccountNumber", qrTransactionDto.getToAccountNumber());
			inputMap.put("transactionType", qrTransactionDto.getTransactionType());
			inputMap.put("transactionAmount", qrTransactionDto.getTransactionAmount());
			inputMap.put("transactRefId", qrTransactionDto.getTransactRefId());
			inputMap.put("amount", qrTransactionDto.getAmount());
			inputMap.put("referenceId", qrTransactionDto.getReferenceId());
			inputMap.put("fromAccountName", qrTransactionDto.getFromAccountName());
			inputMap.put("toAccountName", qrTransactionDto.getToAccountName());
			inputMap.put("fromAccountCurrency", qrTransactionDto.getFromAccountCurrency());
			inputMap.put("toAccountCurrency", qrTransactionDto.getToAccountCurrency());
			inputMap.put("transactionCurrency", qrTransactionDto.getTransactionCurrency());
			inputMap.put("description", qrTransactionDto.getDescription());
			inputMap.put("merchantCode", qrTransactionDto.getMerchantCode());
			inputMap.put("status", qrTransactionDto.getStatus());
			inputMap.put("notes", qrTransactionDto.getNotes());
			inputMap.put("errmsg", qrTransactionDto.getErrmsg());
			inputMap.put("fee", qrTransactionDto.getFee());
			inputMap.put("createdby", qrTransactionDto.getCreatedBy());
			inputMap.put("softdeleteflag", qrTransactionDto.getSoftDeleteFlag());
			inputMap.put("companyId", qrTransactionDto.getCompanyId());
			inputMap.put("externalServiceRequest", qrTransactionDto.getExternalServiceRequest());
			inputMap.put("externalServiceResponse", qrTransactionDto.getExternalServiceResponse());
			inputMap.put("transactionId", qrTransactionDto.getTransactionId());
			logger.debug("insertQRTransactionHistory inputMap:::" + inputMap);

			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.QR_TRANSACTION_HISTORY_CREATE).withRequestParameters(inputMap)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(headerMap).build()
					.getResponse();

			logger.debug("insertQRTransactionHistory response:::" + dbResponse);

			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				logger.error("QRTransaction insertion failed with error: " + responseJSON.getString("errmsg"));
			} else if (responseJSON.has("qrtransaction_history")
					&& responseJSON.getJSONArray("qrtransaction_history").length() > 0) {
				logger.debug("QRTransaction insertion successful.");
			}

		} catch (Exception e) {
			logger.error("Exception while inserting QR transaction history:::" + e.getMessage(), e);
		}
	}

	public Result processReversalTransaction(DataControllerRequest request, QRTransactionDTO qrTransactionDto,
			String intraBankDbxTransID) {
		logger.debug("In processReversalTransaction method:::");
		Map<String, Object> externalApiPayload = qrTransactionDto.getExternalApiPayload();
		Map<String, Object> reverseTxPayload = new HashMap<>();

		String debitReferenceId = externalApiPayload.get("debitReferenceId") != null
				? externalApiPayload.get("debitReferenceId").toString()
				: "";

		reverseTxPayload.put("paymentReferenceId", externalApiPayload.get("paymentSystemId"));

		IntraBankFundTransferDTO reversalResponse = reverseTransaction(reverseTxPayload, request, qrTransactionDto,
				intraBankDbxTransID);
		logger.debug("In processReversalTransaction method reversalResponse:::" + reversalResponse);

		String message = "";

		// Result object to return
		Result result = new Result();
		result.addParam("initiationId", debitReferenceId);
		// result.addParam("externalApiPayload", externalApiPayload);
		result.addParam("message", reversalResponse.getMessage());

		if (reversalResponse.getDbpErrCode() != null || reversalResponse.getDbpErrMsg() != null) {
			result.addParam("transactionId", null);
			result.addParam("dbpErrCode", reversalResponse.getDbpErrCode());
			result.addParam("dbpErrMsg", reversalResponse.getDbpErrMsg());
			result.addParam("description", "REVERSAL_FAILED");
			message = reversalResponse.getErrorDetails();
		} else if (TransactionStatusEnum.REVERSED.getStatus().equalsIgnoreCase(reversalResponse.getStatus())) {
			logger.debug("processReversalTransaction debitReferenceId:::" + debitReferenceId);

			result.addParam("transactionId", null);
			result.addParam("dbpErrCode", ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			result.addParam("dbpErrMsg", TransactionStatusEnum.REVERSED.getMessage());
			result.addParam("description", "REVERSAL_COMPLETED");
			message = TransactionStatusEnum.REVERSED.getMessage();
		} else {
			result.addParam("transactionId", null);
			result.addParam("dbpErrCode", ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			result.addParam("dbpErrMsg", TransactionStatusEnum.REVERSAL_FAILED.getMessage());
			result.addParam("description", "REVERSAL_FAILED");
			message = TransactionStatusEnum.REVERSAL_FAILED.getMessage();
		}

		result.addParam("message", message);
		result.addParam("status", reversalResponse.getStatus());
		logger.debug("processReversalTransaction result:::" + ResultToJSON.convert(result));

		return result;
	}

	public IntraBankFundTransferDTO reverseTransaction(Map<String, Object> inputParams, DataControllerRequest request,
			QRTransactionDTO qrTransactionDto, String intraBankDbxTransID) {
		Result result = new Result();
		IntraBankFundTransferDTO intrabankDTO = new IntraBankFundTransferDTO();
		String status = "";
		String message = "";
		try {
			result = callInternalServiceAndGetResult(REVERSE_TRANSACTION_SERVICE, REVERSE_TRANSACTION_OPEARATION,
					inputParams, request.getHeaderMap());
			JSONObject reverseTxResponse = new JSONObject(ResultToJSON.convert(result));
			logger.debug("reverseTransaction response:" + reverseTxResponse);
			if (result.getParamValueByName("dbpErrCode") != null || result.getParamValueByName("dbpErrMsg") != null) {
				status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
				message = TransactionStatusEnum.FAILED.getStatus();
				intrabankDTO.setDbpErrCode(result.getParamValueByName("dbpErrCode"));
				intrabankDTO.setDbpErrMsg(result.getParamValueByName("dbpErrMsg"));
				intrabankDTO.setErrorDetails(message);
				qrTransactionDto.setErrmsg(result.getParamValueByName("dbpErrMsg"));
			}
			String reversalStatus = result.getParamValueByName("status");
			String id = result.getParamValueByName("id");
			// String transactionStatus = result.getParamValueByName("transactionStatus");
			String transactionMessage = result.getParamValueByName("message");
			if (StringUtils.isNotBlank(reversalStatus) && reversalStatus.equalsIgnoreCase("success")) {
				message = TransactionStatusEnum.REVERSED.getStatus();
				status = TransactionStatusEnum.REVERSED.getStatus();
				intrabankDTO.setReferenceId(id);
				qrTransactionDto.setReferenceId(id);
			} else {
				status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
				message = transactionMessage;
			}
		} catch (DBPApplicationException e) {
			logger.debug("Exception Occured while reversing the transaction" + e.toString());
			status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
			message = e.getLocalizedMessage();
			intrabankDTO.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			intrabankDTO.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage());
			intrabankDTO.setErrorDetails(message);
			qrTransactionDto.setErrmsg(message);
		}
		intrabankDTO.setStatus(status);
		// qrTransactionDto.setStatus(status);
		intrabankDTO.setMessage(message);
		qrTransactionDto.setDescription(message);
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		updateIntraBankTransaction(intraBankDbxTransID, status, confirmationDetails);
		return intrabankDTO;

	}

	private Result callInternalServiceAndGetResult(String serviceid, String operationid, Map<String, Object> inputmap,
			Map<String, Object> headers) throws DBPApplicationException {
		logger.debug("QRPaymentExecutionService: callInternalServiceAndGetString: inputmap:::" + inputmap.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		logger.debug("QRPaymentExecutionService: callInternalServiceAndGetString: response:::"
				+ res.getHttpStatusCodeParamValue());

		return res;
	}

	public BillPayTransactionDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber, String paymentOrderId, String requestId, JSONObject externalServiceResponse,
			String description, JSONObject externalServicePayload) {

		List<BillPayTransactionDTO> billpayTransactionDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BILLPAYTRANSFERS_UPDATE;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		requestParams.put("status", status);
		if (StringUtils.isNotBlank(paymentOrderId))
			requestParams.put("paymentId", paymentOrderId);
		if (StringUtils.isNotBlank(confirmationNumber))
			requestParams.put("confirmationNumber", confirmationNumber);
		if (externalServiceResponse != null)
			requestParams.put("externalServiceResponse", externalServiceResponse.toString());
		if (StringUtils.isNotBlank(requestId))
			requestParams.put("requestId", requestId);
		if (StringUtils.isNotBlank(description))
			requestParams.put("description", description);
		if (externalServicePayload != null)
			requestParams.put("externalServicePayload", externalServicePayload.toString());
		// requestParams.put("description", paymentOrderId);
		alert.prepareError("updateStatusUsingTransactionId :requestParams:" + requestParams).log();
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray billpayJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			billpayTransactionDTO = JSONUtils.parseAsList(billpayJsonArray.toString(), BillPayTransactionDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONException occured while updating the billpaytransaction", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Exception occured while updating the billpaytransaction", exp).log();
			return null;
		}

		if (billpayTransactionDTO != null && billpayTransactionDTO.size() != 0)
			return billpayTransactionDTO.get(0);

		return null;
	}

	private void _logTransaction(DataControllerRequest request, DataControllerResponse response, Object[] inputArray,
			Result result, TransactionStatusEnum transactionStatus, String referenceId, String fromAccountNum,
			String toAccountNum, String aggregatorType) {
		alert.prepareError("_logTransaction in QRPaymentExecutionService aggregatorType" + aggregatorType).log();
		String enableEvents = EnvironmentConfigurationsHandler.getValue("ENABLE_EVENTS", request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE))
			return;
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		AuditLog auditLog = new AuditLog();
		String eventType = "";
		String producer = "";
		if (aggregatorType.equals("agg-3")) {
			eventType = Constants.MAKE_TRANSFER;
			producer = "Transactions/POST(createTransfer)";
		} else {
			eventType = Constants.FEATURE_BILL_PAY;
			producer = Constants.BILL_PAY_CREATE;
		}
		String eventSubType = "QRPayment";
		String statusID = "";
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		String fromAccountNumber = fromAccountNum;
		String toAccountNumber = toAccountNum;

		if (request.containsKeyInRequest("validate")) {
			String validate = request.getParameter("validate");
			if (StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase("true"))
				return;
		}

		JsonObject customParams = new JsonObject();
		if (aggregatorType.equals("agg-3")) {
			customParams.addProperty("FirstName",
					customer.get("FullName") != null ? customer.get("FullName").toString() : "");
		} else {
			customParams.addProperty("referenceId", result.getParamValueByName("referenceId"));
		}
		customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);
		alert.prepareError("_logTransaction in QRPaymentExecutionService customParams::" + customParams).log();
		List<Param> params = result.getAllParams();
		for (Param param : params) {
			if (request.containsKeyInRequest(param.getName())) {
				continue;
			} else {
				customParams.addProperty(param.getName(), param.getValue());
			}
		}
		if (transactionStatus.toString().contains("DENIED")) {
			statusID = Constants.SID_EVENT_FAILURE;
			customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
		} else {
			switch (transactionStatus) {
			case SENT:
				referenceId = customParams.get("referenceId").toString();
				alert.prepareError("_logTransaction in QRPaymentExecutionService referenceId:::" + referenceId).log();
				if (aggregatorType.equals("agg-3")) {
					if (result != null && (result.getParamValueByName("dbpErrCode") != null
							|| (result.getParamValueByName("dbpErrMsg") != null
									&& !result.getParamValueByName("dbpErrMsg").isEmpty()))) {
						statusID = Constants.SID_EVENT_FAILURE;
					}
				} else {
					if (result == null || !"000".equals(result.getParamValueByName("responseCode"))
							|| !"SUCCESS".equalsIgnoreCase(result.getParamValueByName("responseMessage"))) {
						statusID = Constants.SID_EVENT_FAILURE;
					}
				}

				if (referenceId == null || "".equals(referenceId)) {
					statusID = Constants.SID_EVENT_FAILURE;
				} else {
					statusID = Constants.SID_EVENT_SUCCESS;
					customParams.addProperty(Constants.REFERENCEID, referenceId);
					if (isSMEUser) {
						customParams.addProperty(Constants.APPROVERS, "Pre-Approved");
						customParams.addProperty("approvedBy", "Pre-Approved");
					}
				}
				break;
			default:
				break;
			}
		}
		if (isSMEUser) {
			customParams.addProperty("approvedBy", "N/A");
			customParams.addProperty("rejectedBy", "N/A");
		}

		AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);
		alert.prepareError("Before Dispatch Logs at in QRPaymentExecutionService.producer:::" + producer).log();
		Result event = EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, "",
				CustomerSession.getCustomerId(customer), "", customParams);
		alert.prepareError(
				"After Dispatch Logs at in QRPaymentExecutionService result:::" + ResultToJSON.convert(event)).log();
	}

	public BillPayTransactionDTO createTransactionAtDBX(BillPayTransactionDTO billpayTransactionDTO) {
		ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApplicationBusinessDelegate.class);
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BILLPAYTRANSFERS_CREATE;
		String createResponse = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(billpayTransactionDTO).toString(), String.class,
					Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		try {
			requestParameters.put("createdts",
					new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			requestParameters.put("scheduledDate", Timestamp.valueOf(billpayTransactionDTO.getScheduledDate()));
			logger.debug("In QRPaymentExecutionService requestParameters:::" + requestParameters.toString());
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			billpayTransactionDTO = JSONUtils.parse(resposneArray.getJSONObject(0).toString(),
					BillPayTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create billpay transaction entry into billpaytransfers table: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create billpay transaction entry: ", e).log();
			return null;
		}

		return billpayTransactionDTO;
	}

	public IntraBankFundTransferDTO createIntraBankTransactionAtDBX(IntraBankFundTransferDTO intrabankfundtransferdto) {
		ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApplicationBusinessDelegate.class);
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKTRANSFERS_CREATE;
		String createResponse = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(intrabankfundtransferdto).toString(), String.class,
					Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}

		try {
			requestParameters.put("createdts",
					new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			requestParameters.put("paymentType", intrabankfundtransferdto.getpaymentMethod());
			requestParameters.put("scheduledDate", Timestamp.valueOf(intrabankfundtransferdto.getScheduledDate()));
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);

			intrabankfundtransferdto = JSONUtils.parse(resposneArray.getJSONObject(0).toString(),
					IntraBankFundTransferDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create intrabank transaction entry into intrabanktransfers table: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create intrabank transaction entry: ", e).log();
			return null;
		}

		return intrabankfundtransferdto;
	}

	private boolean isTransactionAlreadyProcessed(String transactionId) {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();
		String filter = "transactionId eq '" + transactionId + "'";
		inputMap.put(HBLURLConstants.FILTER, filter);
		logger.debug("isTransactionAlreadyProcessed inputMap::: {}" + inputMap);
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.QR_TRANSACTION_HISTORY_GET).withRequestParameters(inputMap)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(headerMap).build()
					.getResponse();
			JSONObject responseJSON = new JSONObject(response);
			logger.debug("QR Transaction History Response::: {}" + responseJSON);
			JSONArray results = responseJSON.getJSONArray("qrtransaction_history");
			return results != null && results.length() > 0;
		} catch (Exception e) {
			logger.error("Exception while checking transaction history:::", e);
			return false;
		}
	}
}