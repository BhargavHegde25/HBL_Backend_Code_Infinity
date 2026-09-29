package com.bct.eSewa;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class eSewaLoadAmountServiceBCTCopy implements JavaService2 {
	private static final Logger logger = LogManager.getLogger(eSewaLoadAmountService.class);
	private static final String GET_TRANSACTION_STAUS_SERVICE_ORCH = "T24-IS-TransactOrch";
	private static final String GET_TRANSACTION_STAUS_OPERATION = "getTransactionStatus";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String ESEWA_BASE_URL = paramHelper.getServerProperty("ESEWA_BASE_URL");
		String ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY = paramHelper
				.getServerProperty("ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY");
		String ESEWA_CLIENT_SIGNATURE_PRIVATEKEY = paramHelper.getServerProperty("ESEWA_CLIENT_SIGNATURE_PRIVATEKEY");
		String ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY = paramHelper
				.getServerProperty("ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY");
		String ESEWA_SERVER_SIGNATURE_PUBLICKEY = paramHelper.getServerProperty("ESEWA_SERVER_SIGNATURE_PUBLICKEY");
		String ESEWA_CLINET_ID = paramHelper.getServerProperty("ESEWA_CLINET_ID");
		String ESEWA_SWIFT_CODE = paramHelper.getServerProperty("ESEWA_SWIFT_CODE");

		String PAYMENT_URL = ESEWA_BASE_URL + "/api/auth/load";

		Result result = new Result();

		String frmAccNumber = request.getParameter("frmAccNumber");
		String frmAccName = request.getParameter("frmAccName");
		if (!StringUtils.isNotBlank(frmAccName))
			frmAccName = "NA";
		String paymentDesc = request.getParameter("paymentDesc");
		String field1 = request.getParameter("field1");
		String field2 = request.getParameter("field2");
		String eSewaId = request.getParameter("eSewaId");
		String amount = request.getParameter("amount");
		String Fee = request.getParameter("Fee");
		String referenceId = request.getParameter("referenceId");

		String paymentSystemIdFrmStatusCheck = "";

		try {
			if (StringUtils.isNotBlank(referenceId)) {
				Map<String, Object> requestParams = new HashMap<String, Object>();
				requestParams.put("paymentOrderId", referenceId);
				String transactionStatusT24 = "";
				JSONObject paymentObj = getTransactionStatus(requestParams, request);
				transactionStatusT24 = paymentObj != null ? paymentObj.optString("currentStatus") : "";
				logger.debug("eSewaLoadAmountService :current transactionStatus:" + transactionStatusT24);
				paymentSystemIdFrmStatusCheck = paymentObj.optString("paymentSystemId");
				logger.debug("eSewaLoadAmountService :paymentSystemIdFrmStatusCheck:" + paymentSystemIdFrmStatusCheck);

				if (StringUtils.isNotBlank(transactionStatusT24)
						&& !transactionStatusT24.equalsIgnoreCase("Complete")) {

					result.setParam(new Param("response_code", ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
					result.setParam(new Param("message", "Payment Order cannot be completed at this movement."));
					result.addParam("errmsg", "Payment Order cannot be completed at this movement.");
					result.setParam(new Param("opstatus", "0"));

					return result;
				}
			}
		} catch (Exception e) {
			logger.debug("eSewaLoadAmountService :Error ##" + e);
			result.setParam(new Param("response_code", ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
			result.setParam(new Param("message", "Payment Order cannot be completed at this movement."));
			result.addParam("errmsg", "Payment Order cannot be completed at this movement.");
			result.setParam(new Param("opstatus", "0"));
			return result;
		}

		String batchId = "FT" + generateRandom10DigitNumber();

		logger.debug("AccNumber ###: " + frmAccNumber);
		logger.debug("AccountName ###: " + frmAccName);
		logger.debug("paymentDesc ##: " + paymentDesc);
		logger.debug("field1 ##: " + field1);
		logger.debug("field2 ##: " + field2);
		logger.debug("amount ##: " + amount);
		logger.debug("eSewaId ##: " + eSewaId);
		logger.debug("Fee ##: " + Fee);
		logger.debug("ReferenceId ##: " + referenceId);

		String channel = eSewaLimitCheck.getCurrentChannel(request);
		logger.debug("channel ##" + channel);

		String res = "";
		try {
			res = EsewaValidationClient.performPaymentRequest(request, frmAccNumber, frmAccName, paymentDesc, field1,
					field2, amount, eSewaId, referenceId, ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY,
					ESEWA_CLIENT_SIGNATURE_PRIVATEKEY, ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY,
					ESEWA_SERVER_SIGNATURE_PUBLICKEY, ESEWA_CLINET_ID, ESEWA_SWIFT_CODE, PAYMENT_URL);
		} catch (Exception e) {
			logger.error("Exception in eSewa Payment: " + e);
			result.setParam(
					new Param("message", "We’re experiencing issues with eSewa services. Please try again later."));
			result.addParam("errmsg", "We’re experiencing issues with eSewa services. Please try again later.");
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		}

		/*
		 * if (res.equalsIgnoreCase("Fail")) {
		 * logger.debug("Inside fail doreversal :###" ); (request); }
		 */

		ObjectMapper mapper = new ObjectMapper();
		EsewaPaymentResponse resData = mapper.readValue(res, EsewaPaymentResponse.class);
		logger.debug("response_code: " + resData.getResponse_code());
		String EsewaReceiverName = "";
		String transactionStatus = resData.getEsewa_load_transaction_detail().get(0).getTransaction_status();
		logger.debug("transactionStatus: " + transactionStatus);

		String BatchId = batchId;
		String SwiftCode = ESEWA_SWIFT_CODE;
		String EsewaId = eSewaId;
		if (StringUtils.isNotBlank(field1))
			EsewaReceiverName = field1;
		String Amount = amount;
		String OriginatingUniqueId = resData.getEsewa_load_transaction_detail().get(0).getOriginating_unique_id();
		String Status = resData.getStatus() + "";
		String ResponseCode = resData.getResponse_code();
		String StatusCode = resData.getStatus() + "";
		String RawResponse = resData.toString();
		String SourceAccountNo = frmAccNumber;
		String SenderName = frmAccName;
		String SenderMobileNo = "";
		String SenderAddress = "";
		String TransactionPurpose = paymentDesc;
		String TransactionStatus = resData.getEsewa_load_transaction_detail().get(0).getTransaction_status();
		String TransactionDetailOriginatingUniqueId = resData.getEsewa_load_transaction_detail().get(0)
				.getTransaction_id();
		String statuscheck = "COMPLETE".equals(Status) ? "0" : "1";
		// String channell = eSewaLimitCheck.getCurrentChannel(request);

		EsewaValidationClient.esewaTransactionLog(request, BatchId, SwiftCode, EsewaId, EsewaReceiverName, Amount,
				OriginatingUniqueId, Status, ResponseCode, StatusCode, RawResponse, SourceAccountNo, SenderName,
				SenderMobileNo, SenderAddress, TransactionPurpose, TransactionStatus,
				TransactionDetailOriginatingUniqueId, statuscheck, channel, Fee);

		/**
		 * Reversal API call when eSewa Load Amount API fails with status as FAILED
		 ******/
		if (transactionStatus.equalsIgnoreCase("FAILED") || transactionStatus.equalsIgnoreCase("NOT_FOUND")) {
			// Reversal
			String reversalMsg = ". Sorry, Your Top-Up could not be processed at this time. If the amount has been debited, it will be refunded back to your account within 5 business days. We apologize for the inconvenience.";
			String paymentReferenceId = request.getParameter("paymentReferenceId");
			String referenceId1 = request.getParameter("referenceId");
			String transactionId = request.getParameter("transactionId");
			String reversalPaymentId = "";

			logger.debug("paymentReferenceId :###" + paymentReferenceId);
			logger.debug("referenceId :###" + referenceId1);
			logger.debug("transactionId :###" + transactionId);

			if (StringUtils.isBlank(paymentReferenceId))
				reversalPaymentId = paymentSystemIdFrmStatusCheck;
			else
				reversalPaymentId = paymentReferenceId;

			logger.debug("reversalPaymentId :###" + reversalPaymentId);

			try {
				String REVERSE_TRANSACTION_JAVA_SERVICE = "HBLCreateExternalTransfers";
				String REVERSE_TRANSACTION_JAVA_OPEARATION = "reverseTransaction";
				Result result1 = new Result();
				HashMap<String, Object> inputParams = new HashMap<>();
				inputParams.put("paymentReferenceId", reversalPaymentId);
				inputParams.put("referenceId", referenceId1);
				inputParams.put("transactionId", transactionId);
				request.addRequestParam_("paymentReferenceId", reversalPaymentId);
				request.addRequestParam_("referenceId", referenceId1);
				request.addRequestParam_("transactionId", transactionId);
				result1 = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
						REVERSE_TRANSACTION_JAVA_SERVICE, REVERSE_TRANSACTION_JAVA_OPEARATION, true);

				JSONObject reverseTxResponse = new JSONObject(ResultToJSON.convert(result1));
				logger.debug("reverseTxResponse java :###" + reverseTxResponse);
				if (result1.getParamValueByName("dbpErrCode") != null
						|| result1.getParamValueByName("dbpErrMsg") != null) {
					logger.debug("reverseTxResponse failed :###");
					result1.getParamValueByName("status");
					logger.debug("reverseTxResponse failed status:###" + result1.getParamValueByName("status"));
				} else {
					logger.debug("reverseTxResponse success :###");
					logger.debug("reverseTxResponse success status:###" + result1.getParamValueByName("status"));
					logger.debug("reverseTxResponse success message:###" + result1.getParamValueByName("message"));
				}

				result.setParam(new Param("response_code", resData.getResponse_code()));
				result.setParam(new Param("message", resData.getMessage()));
				result.setParam(new Param("responseData", resData.toString()));
				result.addParam("errmsg", reversalMsg);
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));

				/*** Updating esewaTransactionReprocessLog once reversal is done *****/
				String originating_unique_id = resData.getEsewa_load_transaction_detail().get(0)
						.getOriginating_unique_id();
				eSewaLimitCheck.createEsewaTransactionReprocessLog(request, originating_unique_id);
			} catch (Exception e) {
				result.setParam(new Param("response_code", resData.getResponse_code()));
				result.setParam(new Param("message", resData.getMessage()));
				result.setParam(new Param("responseData", resData.toString()));
				result.addParam("errmsg", reversalMsg);
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			}

			return result;
		}

		if (resData.getResponse_code().equalsIgnoreCase("ELR000") && transactionStatus.equalsIgnoreCase("COMPLETE")) {

			sendeSewaEmailToCustomer(request, result, resData);

			result.setParam(new Param("status", resData.getStatus() + ""));
			result.setParam(new Param("response_code", resData.getResponse_code()));
			result.setParam(new Param("responseData", resData.toString()));
			result.setParam(new Param("transaction_status",
					resData.getEsewa_load_transaction_detail().get(0).getTransaction_status()));
			result.setParam(new Param("originating_unique_id",
					resData.getEsewa_load_transaction_detail().get(0).getOriginating_unique_id()));
			result.setParam(
					new Param("transaction_id", resData.getEsewa_load_transaction_detail().get(0).getTransaction_id()));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} else if (transactionStatus.equalsIgnoreCase("PENDING") || transactionStatus.equalsIgnoreCase("AMBIGUOUS")
				|| transactionStatus.equalsIgnoreCase("PARTIAL_COMPLETE")) {

			/***
			 * Storing PENDING/AMBIGUOUS/PARTIAL_COMPLETE status records are storing into
			 * the esewaTransactionPendingLog table and using Fabric job fetching last five
			 * minutes records from this table if any exists and processing with eSewa
			 * status check API and storing latest Status response in 'esewaApiAuditLog'
			 * table with API name 'TransactionStatusCheck'
			 */

			EsewaValidationClient.esewaTransactionPendingLog(request, BatchId, SwiftCode, EsewaId, EsewaReceiverName,
					Amount, OriginatingUniqueId, Status, ResponseCode, StatusCode, RawResponse, SourceAccountNo,
					SenderName, SenderMobileNo, SenderAddress, TransactionPurpose, TransactionStatus,
					TransactionDetailOriginatingUniqueId, statuscheck, channel, Fee);

			/*	
				*//***
					 * status check API call to check status when load API status is PENDING
					 * AMBIGUOUS PARTIAL_COMPLETE
					 */
			/*
			 * String originating_unique_id =
			 * resData.getEsewa_load_transaction_detail().get(0).getOriginating_unique_id();
			 * logger.debug("transactionId in status check call :###" +
			 * originating_unique_id);
			 * 
			 * 
			 * try { String reslt = eSewaStatusCheck(originating_unique_id, request);
			 * JSONObject jsonRsponse = new JSONObject(reslt); String status =
			 * jsonRsponse.getString("transactionStatus"); String response_code =
			 * jsonRsponse.getString("responseCode"); String responseData =
			 * jsonRsponse.toString(); String transaction_status =
			 * jsonRsponse.getString("transactionStatus"); String originating_unique_id1 =
			 * jsonRsponse.getString("originatingUniqueId"); String transaction_id =
			 * jsonRsponse.getString("transId");
			 * 
			 * Map<String, Object> requestParameters = new HashMap<String, Object>();
			 * requestParameters.put("transactionId", originating_unique_id);
			 * logger.debug("INPUT to BACKEND: " + new
			 * JSONObject(requestParameters).toString());
			 * EsewaValidationClient.recordApiAuditLog(request, "eSewaStatusCheck", new
			 * JSONObject(requestParameters).toString(), new
			 * JSONObject(requestParameters).toString(), jsonRsponse.toString(),
			 * jsonRsponse.toString());
			 * 
			 * if (jsonRsponse.has("transactionStatus")) { String id =
			 * eSewaLimitCheck.getIdFromTransactionLogTable(request,
			 * originating_unique_id1); logger.debug(" Transaction log recod Id ###" + id);
			 *//**
				 * updating esewaTransactionLog table with Latest status of the existing
				 * Transaction
				 ***//*
						 * eSewaLimitCheck.updateeSewaTransLog(request, id, status, response_code,
						 * responseData, transaction_status, originating_unique_id1, transaction_id); }
						 * } catch (Exception e) { e.printStackTrace(); logger.error(e.toString()); }
						 * 
						 * 
						 * result.setParam(new Param("status", resData.getStatus() + ""));
						 * result.setParam(new Param("response_code", resData.getResponse_code()));
						 * result.setParam(new Param("responseData", resData.toString()));
						 * result.setParam(new Param("transaction_status",
						 * resData.getEsewa_load_transaction_detail().get(0).getTransaction_status()));
						 * result.setParam(new Param("originating_unique_id",
						 * resData.getEsewa_load_transaction_detail().get(0).getOriginating_unique_id())
						 * ); result.setParam(new Param("transaction_id",
						 * resData.getEsewa_load_transaction_detail().get(0).getTransaction_id()));
						 */

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));

		} else {
			result.setParam(new Param("response_code", resData.getResponse_code()));
			result.setParam(new Param("message", resData.getMessage()));
			result.setParam(new Param("responseData", resData.toString()));
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		}

		return result;
	}

	public static long generateRandom10DigitNumber() {
		Random random = new Random();
		// Generate number between 1000000000 (inclusive) and 9999999999 (inclusive)
		return 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
	}

	public static String getChannel(DataControllerRequest request) throws JSONException, UnsupportedEncodingException {
		String channel = "";

		UserAgentUtil ua = new UserAgentUtil(request);
		channel = ua.getChannel();
		logger.debug("HBL::eSewa Load API channel:::" + channel);
		if (channel.equalsIgnoreCase("desktop")) {
			channel = "ONLINEBANKING";
		} else if (channel.equalsIgnoreCase("mobile")) {
			channel = "MOBILE";
		}

		return channel;
	}

	private String eSewaStatusCheck(String transactionId, DataControllerRequest request) {
		try {
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("transactionId", transactionId);
			logger.debug("INPUT to BACKEND: " + new JSONObject(requestParameters).toString());
			return DBPServiceExecutorBuilder.builder().withServiceId("eSewaServices").withObjectId(null)
					.withOperationId("eSewaStatusCheck").withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap()).withDataControllerRequest(request).build()
					.getResponse();
		} catch (Exception e) {
			logger.debug("Caught exception at  eSewaStatusCheck: ", e);
			return "{\"errormsg\":\"" + e.getMessage() + "\"}";
		}
	}

	private void doReversal(DataControllerRequest request) {
		Result result = new Result();
		String reversalMsg = ". Sorry, Your Top-Up could not be processed at this time. If the amount has been debited, it will be refunded back to your account within 5 business days. We apologize for the inconvenience.";
		String paymentReferenceId = request.getParameter("paymentReferenceId");
		String referenceId1 = request.getParameter("referenceId");
		String transactionId = request.getParameter("transactionId");

		logger.debug("paymentReferenceId :###" + paymentReferenceId);
		logger.debug("referenceId :###" + referenceId1);
		logger.debug("transactionId :###" + transactionId);
		try {
			String REVERSE_TRANSACTION_JAVA_SERVICE = "HBLCreateExternalTransfers";
			String REVERSE_TRANSACTION_JAVA_OPEARATION = "reverseTransaction";
			Result result1 = new Result();
			HashMap<String, Object> inputParams = new HashMap<>();
			inputParams.put("paymentReferenceId", paymentReferenceId);
			inputParams.put("referenceId", referenceId1);
			inputParams.put("transactionId", transactionId);
			request.addRequestParam_("paymentReferenceId", paymentReferenceId);
			request.addRequestParam_("referenceId", referenceId1);
			request.addRequestParam_("transactionId", transactionId);
			result1 = CommonUtils.callIntegrationService(request, inputParams, request.getHeaderMap(),
					REVERSE_TRANSACTION_JAVA_SERVICE, REVERSE_TRANSACTION_JAVA_OPEARATION, true);

			JSONObject reverseTxResponse = new JSONObject(ResultToJSON.convert(result1));
			logger.debug("reverseTxResponse java :###" + reverseTxResponse);
			if (result1.getParamValueByName("dbpErrCode") != null || result1.getParamValueByName("dbpErrMsg") != null) {
				logger.debug("reverseTxResponse failed :###");
				result1.getParamValueByName("status");
				logger.debug("reverseTxResponse failed status:###" + result1.getParamValueByName("status"));
			} else {
				logger.debug("reverseTxResponse success :###");
				logger.debug("reverseTxResponse success status:###" + result1.getParamValueByName("status"));
				logger.debug("reverseTxResponse success message:###" + result1.getParamValueByName("message"));
			}

			result.setParam(new Param("message", reversalMsg));
			result.addParam("errmsg", reversalMsg);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));

			/*** Updating esewaTransactionReprocessLog once reversal is done *****/
			String paymentReferenceId1 = request.getParameter("paymentReferenceId");
			eSewaLimitCheck.createEsewaTransactionReprocessLog(request, paymentReferenceId1);
		} catch (Exception e) {
			result.setParam(new Param("message", reversalMsg));
			result.addParam("errmsg", reversalMsg);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		}

	}

	public static void sendeSewaEmailToCustomer(DataControllerRequest request, Result result, EsewaPaymentResponse res)
			throws HttpCallException, AppRegistryException {
		String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		logger.debug("customerId :###" + customerId);
		String email = Utils.customerEmailFromSession(request);
		logger.debug("email :###" + email);

		// Transaction Id
		String transactionId = res.getEsewa_load_transaction_detail().get(0).getTransaction_id();
		// Transaction Status
		String transactionStatus = res.getEsewa_load_transaction_detail().get(0).getTransaction_status();
		// Transaction Purpose
		String transactionPurpose = request.getParameter("paymentDesc");
		// Transaction Amount
		String transactionAmount = request.getParameter("amount");
		// Transaction Date
		Date date = new Date();
		String transactionDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
		logger.debug("requestDate ##:" + transactionDate);
		// Reference Id
		String referenceId = res.getEsewa_load_transaction_detail().get(0).getOriginating_unique_id();
		// Bank Name
		String bankName = "Himalayan Bank Ltd";
		// Account Name
		String accountName = request.getParameter("frmAccName");
		// Account Number
		String accountNumber = request.getParameter("frmAccNumber");
		// Receiver Name
		String receiverName = request.getParameter("field1");
		// eSewa Id
		String eSewaId = request.getParameter("eSewaId");
		String emailTemplate = "emailTempleteeSewaLoad";
		accountNumber = HBLCommonUtility.maskAccountNumber(accountNumber, 0, accountNumber.length() - 4, "X");

		logger.debug("triggerEmail value:" + email);
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("transactionId", transactionId);
		addContext.put("transactionStatus", transactionStatus);
		addContext.put("transactionPurpose", transactionPurpose);
		addContext.put("transactionAmount", transactionAmount);
		addContext.put("transactionDate", transactionDate);
		addContext.put("referenceId", referenceId);
		addContext.put("bankName", bankName);
		addContext.put("accountName", accountName);
		addContext.put("accountNumber", accountNumber);
		addContext.put("receiverName", receiverName);
		addContext.put("eSewaId", eSewaId);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
	}

	public JSONObject getTransactionStatus(Map<String, Object> requestParameters,
			DataControllerRequest dataControllerRequest) {
		JSONArray resArray = new JSONArray();
		String serviceName = GET_TRANSACTION_STAUS_SERVICE_ORCH;
		String operationName = GET_TRANSACTION_STAUS_OPERATION;
		String orchServiceResponse = null;
		JSONObject paymentObj = new JSONObject();

		try {
			logger.debug("getTransactionStatus:OrchService:requestParameters:" + requestParameters);
			orchServiceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					requestParameters, null, dataControllerRequest);
			dataControllerRequest.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
			String authToken = TokenUtils.getT24AuthToken(dataControllerRequest);
			logger.debug("getTransactionStatus:OrchService:Authorization:" + authToken);
			// dataControllerRequest.getHeaderMap().put("Authorization", authToken);
			orchServiceResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(dataControllerRequest.getHeaderMap())
					.withDataControllerRequest(dataControllerRequest).build().getResponse();
			JSONObject transactionResponse = new JSONObject(orchServiceResponse);
			logger.debug("getTransactionStatus:OrchService:transactionResponse:" + transactionResponse);
			resArray = transactionResponse.getJSONArray("LoopDataset");
			paymentObj = processOrchServceResponse(resArray);
			return paymentObj;
		} catch (JSONException jsonExp) {
			logger.error("JSONExcpetion occured getTransactionStatus: ", jsonExp);
			return paymentObj;
		} catch (Exception exp) {
			logger.error("Excpetion occured getTransactionStatus: ", exp);
			return paymentObj;
		}
	}

	public JSONObject processOrchServceResponse(JSONArray resArray) {
		String status = "";
		JSONObject obj = new JSONObject();
		if (resArray != null) {
			for (int i = 0; i < resArray.length(); i++) {
				obj = resArray.getJSONObject(i);
				status = obj.getString("currentStatus");
				if (status.equalsIgnoreCase("Complete")) {
					break;
				}
			}
		}
		return obj;
	}
}
