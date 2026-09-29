package com.hbl.productservicesExtn.javaservice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.error.DBPError;
import com.dbp.core.error.DBPErrorCodeSetter;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.dbp.transactionslimitengine.utils.TransactionsLimitConstants;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hbl.productservicesExtn.exception.CustomException;
import com.hbl.productservicesExtn.utills.GenerateNCHLPayload;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.InterBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.InterBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.dataobject.Record;

public class ExecuteTodayScheduledTransactions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		int count=0;
		try {
			JSONArray scheduledTransactions = GetPendingScheduledTrasactions();
			alert.prepareError("scheduledTransactions Response:" + scheduledTransactions).log();
			JSONArray transactionResponse= new JSONArray();
			if (scheduledTransactions != null) {
				if (scheduledTransactions.length() > 0) {
					for (int i = 0; i < scheduledTransactions.length(); i++) {
						JSONObject obj = scheduledTransactions.getJSONObject(i);
						String scheduledDt = obj.optString("scheduledDate");
						String pattern="yyyy-MM-dd";
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
						String scheduledDate = HelperMethods.getFormattedLocaleDate(scheduledDt,pattern);
						String todayDate = formatter.format(LocalDate.now());
						boolean isScheduledToday = scheduledDate.equals(todayDate);
						if (isScheduledToday) {
						count++;
						String transactionId = obj.optString("transactionId");
						String stringPayload = obj.optString("internalApiPayload");
						String customerId = obj.optString("createdby");
						JSONObject payload = new JSONObject(stringPayload);
						payload.put("isScheduled", "0");
						payload.put("isAdmin", "1");
						payload.put("transactionId", transactionId);
						Map<String, Object> inputmap = payload.toMap();
						alert.prepareError("Executing Scheduled Domestic Transaction inputmap:" + inputmap).log();
						Result transactionResult = createTransaction(inputmap, inputArray, request, response);
						JSONObject resultObj = new JSONObject(ResultToJSON.convert(transactionResult));
						alert.prepareError("Executing Scheduled Domestic Transaction resultObj:" + resultObj).log();
						String dbpErrCode = resultObj.has("dbpErrCode") ? resultObj.getString("dbpErrCode") : "";
						String dbpErrMsg = resultObj.has("dbpErrMsg") ? resultObj.getString("dbpErrMsg") : "";
						if (StringUtils.isNotBlank(dbpErrMsg)) {
							result.addParam(new Param("dbpErrCode",dbpErrCode));
							result.addParam(new Param("dbpErrMsg", dbpErrMsg));
							result.addParam(new Param("success", "false"));
						} else {
							result.addParam(new Param("success", "true"));
							result.appendResult(transactionResult);
						}
						obj.put("transactionResponse", new JSONObject(ResultToJSON.convert(result)));
						transactionResponse.put(obj);
						}
					}
					if(count==0) {
						result.addParam(new Param("success", "true"));
						result.addParam(new Param("message", "There are no scheduled transactions today."));
					}
				} else {
					result.addParam(new Param("success", "true"));
					result.addParam(new Param("message", "There are no scheduled transactions."));
				}
			}
			if(transactionResponse!=null &&transactionResponse.length()>0) {
				postProcess(transactionResponse, request);
			}
		} catch (CustomException e) {
			alert.prepareError("CustomException Occured in Executing Scheduled Domestic Transaction:", e).log();
			result.addParam(new Param("dbpErrCode", e.getErrorCode()));
			result.addParam(new Param("dbpErrMsg", e.getErrorMessage()));
			result.addParam(new Param("success", "false"));
		}catch (Exception e) {
			alert.prepareError("Exception Occured in Executing Scheduled Domestic Transaction:", e).log();
			result.addParam(new Param("dbpErrCode", e.getLocalizedMessage()));
			result.addParam(new Param("dbpErrMsg", e.getMessage()));
			result.addParam(new Param("success", "false"));
		}

		return result;
	}
	
	public void postProcess(JSONArray transactionResponse, DataControllerRequest request) {
		if(transactionResponse!=null && transactionResponse.length()>0) {
			for(int i=0;i<transactionResponse.length();i++) {
				JSONObject transRecord=transactionResponse.getJSONObject(i);
				alert.prepareError("postProcessTransactions:transactionRecord:" + transRecord).log();
				String requestid = transRecord.optString("requestId");
				String customerId = transRecord.optString("createdby");
				String customerName="Customer";
				Map<String, Object> customer;
				try {
					customer = getCustomerMap(customerId, request);
					customerName=customer.get("FullName")!=null? customer.get("FullName").toString():customer.get("FirstName").toString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JSONObject result= transRecord.getJSONObject("transactionResponse");
				boolean isTransactionSuccess=result.has("success")&& result.getString("success").equalsIgnoreCase("true")?true:false;
				JSONObject contactDetails = getContactDetails(customerId, request);
				String msg="";
				String debitStatus="";
				String creditStatus="";
				if(isTransactionSuccess) {
					msg="Dear "+customerName+", We are pleased to inform you that your scheduled transaction #" +requestid+ " has been successfully processed.";
					debitStatus="Success";
					creditStatus="Success";
				}else {
					 msg="Dear "+customerName+", We regret to inform you that your scheduled transaction #"+requestid+" has been unsuccessful.";
					 debitStatus=result.optString("dbpErrMsg");
					 creditStatus="Failed";
				}
				contactDetails.put("FirstName",customerName);
				transRecord.put("Message", msg);
				transRecord.put("requestId", requestid);
				transRecord.put("debitStatus", debitStatus);
				transRecord.put("creditStatus", creditStatus);
				transRecord.remove("internalServiceResponse");
				transRecord.remove("internalApiPayload");
				transRecord.remove("transactionResponse");
				try {
					triggerEmail(request, contactDetails,transRecord);
				} catch (HttpCallException e) {
					alert.prepareError("Failed to send an email:transactionRecord:",e).log();
				}
			}
		}
		
	}
private void triggerEmail(DataControllerRequest dcRequest, JSONObject customerInfo, JSONObject transRecord) throws HttpCallException {
	alert.prepareError("triggerEmail:customerInfo" + customerInfo).log();
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("FirstName", customerInfo.optString("FirstName"));
		input.put("EmailType", "HBL_TRANSACTION_TEMPLATE");
		String activationLink=EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL"); 
		//JSONObject addContext = new JSONObject();
		//addContext.put("resetPasswordLink", activationLink);
		//addContext.put("userName", customerInfo.optString("requestId"));
		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, transRecord));
		input.put("Email", customerInfo.optString("email"));
		Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
		
	}
	public JSONObject getContactDetails(String customerId, DataControllerRequest request) {
			CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
			CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
			customerCommunicationDTO.setCustomer_id(customerId);
			DBXResult communicationResponse = communicationBackendDelegate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, request.getHeaderMap());
			JsonObject customerCommunication = ((JsonObject) communicationResponse.getResponse());
			JSONObject communicationObj= new JSONObject();
			if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
			JsonArray communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
				for (JsonElement jsonelement : communicationArray) {
					JsonObject object = jsonelement.getAsJsonObject();
						if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
							communicationObj.put("email", JSONUtil.getString(object, "Value"));
						if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
							communicationObj.put("phone", JSONUtil.getString(object, "Value"));
					}
				}
			alert.prepareError("getContactDetails:" + communicationObj).log();
			return communicationObj;
	}

	public JSONArray GetPendingScheduledTrasactions() throws CustomException {
		List<InterBankFundTransferDTO> interbankfundtransferdto = null;
		String serviceName = "HBLOtherbankTrnsferJavaService";
		String operationName = "GetPendingScheduledTrasactions";
		Map<String, Object> requestParams = new HashMap<String, Object>();
		JSONArray interbankJsonArray = null;
		alert.prepareError("inputmap:" + requestParams).log();
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);

			alert.prepareError("Scheduled Interbank Json Response:" + jsonRsponse).log();
			String dbpErrCode = jsonRsponse.has("dbpErrCode") ? jsonRsponse.getString("dbpErrCode") : "";
			String dbpErrMsg = jsonRsponse.has("dbpErrMsg") ? jsonRsponse.getString("dbpErrMsg") : "";
			alert.prepareError("Scheduled Interbank Json Response has dbpErrCode:" + dbpErrCode).log();
			if (StringUtils.isNotBlank(dbpErrCode) || StringUtils.isNotBlank(dbpErrMsg)) {
				throw new CustomException(dbpErrCode, dbpErrMsg);
			} else if (jsonRsponse.has("success") && jsonRsponse.get("success").toString().equalsIgnoreCase("true")
					&& jsonRsponse.has("scheduledTransactions")) {
				interbankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			}

		} catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while GetPendingScheduledTrasactions:"+ jsonExp).log();
			throw new CustomException("20001",jsonExp.getMessage());
		} catch (CustomException exp) {
			alert.prepareError("CustomException occured while GetPendingScheduledTrasactions:"+ exp).log();
			throw new CustomException(exp.getErrorCode(),exp.getErrorMessage());
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured while GetPendingScheduledTrasactions:"+ exp).log();
			throw new CustomException("20001",exp.getMessage());
		}
		return interbankJsonArray;
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
			alert.prepareError("Exception Occured at callObjectService ").log();
			throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : "
					+ operationID + " objectid : " + objid + " ; " + e.getMessage());
		}
		return result;
	}

	public Result createTransaction(Map<String, Object> inputParams, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws IOException,Exception {
		InterBankFundTransferBusinessDelegate interbanktransferDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InterBankFundTransferBusinessDelegate.class);
		InterBankFundTransferBackendDelegate interbankBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InterBankFundTransferBackendDelegate.class);
		diagnostic.debug("ExecuteTodayScheduledTransactions:createTransaction:inputParams:" + inputParams);
		Result result = null;
		InterBankFundTransferDTO interbanktransactionDTO = new InterBankFundTransferDTO();
		InterBankFundTransferDTO interbankDTO = null;
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		TransactionStatusEnum transactionStatus = TransactionStatusEnum.SENT;
		String legalEntityId = inputParams.get("legalEntityId") != null ? inputParams.get("legalEntityId").toString()
				: null;
		String requestid = inputParams.get("requestId") != null ? inputParams.get("requestId").toString() : null;
		String customerId = inputParams.get("createdby") != null ? inputParams.get("createdby").toString() : null;
		InterBankFundTransferDTO interbankdbxDTO;
		try {
			interbankdbxDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InterBankFundTransferDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		InterBankFundTransferBackendDTO interbankBackendDTO = new InterBankFundTransferBackendDTO();
		interbankBackendDTO = interbankBackendDTO.convert(interbankdbxDTO);
		interbankBackendDTO.setRequestId(requestid);
		String transactionid = interbankdbxDTO.getTransactionId();
		String confirmationNumber = Constants.REFERENCE_KEY + inputParams.get("requestId") != null
				? inputParams.get("requestId").toString()
				: null;
		alert.prepareError("ExecuteTodayScheduledTransactions:transactionid:" + transactionid).log();
		try {
			String responseObj = new JSONObject(interbankBackendDTO).toString();
			result = JSONToResult.convert(responseObj);
		} catch (JSONException e) {
			alert.prepareError(
					"Error occured while converting the response from Line of Business service for interbank transfer: ",
					e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		HashMap<String, Object> payload = payloadForServiceCalls(inputParams, transactionid);
		interbankBackendDTO.setExternalApiPayload((HashMap<String, Object>) payload.get("externalServicepayload"));
		interbankBackendDTO.setInternalApiPayload((HashMap<String, Object>) payload.get("internalServicePayload"));
		interbanktransactionDTO = interbankBackendDelegate.createTransactionWithoutApproval(interbankBackendDTO, request);
		if (interbanktransactionDTO == null) {
			interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,
					TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			return ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		alert.prepareError("ExecuteTodayScheduledTransactions:interbanktransactionDTO.getDbpErrMsg():"+interbanktransactionDTO.getDbpErrMsg()).log();
		String externalServiceResponse = interbanktransactionDTO.getExternalServiceResponse() != null? interbanktransactionDTO.getExternalServiceResponse().toString(): null;
		HashMap<String,Object> externalServicePayload =interbanktransactionDTO.getExternalApiPayload();
		String dbpErrMsg = interbanktransactionDTO.getDbpErrMsg();
		String dbpErrCode = interbanktransactionDTO.getDbpErrCode();
		String status = interbanktransactionDTO.getStatus();
		String paymentId = interbanktransactionDTO.getPaymentId();
		String paymentRequestId = interbanktransactionDTO.getRequestId();
		String paymentTransactionsNotes = interbanktransactionDTO.getPaymentNote();
		Map<String, Object> updateParams = new HashMap<String, Object>();
		updateParams.put("requestId", paymentRequestId);
		updateParams.put("paymentId", paymentId);
		updateParams.put("transactionsNotes", paymentTransactionsNotes);
		updateParams.put("externalServicePayload", externalServicePayload!=null?new JSONObject(externalServicePayload).toString():null);
		updateParams.put("externalServiceResponse", externalServiceResponse);
		if (interbanktransactionDTO.getDbpErrCode() != null || interbanktransactionDTO.getDbpErrMsg() != null) {
			alert.prepareError("ExecuteTodayScheduledTransactions:interbanktransactionDTO.getDbpErrCode():"+ interbanktransactionDTO.getDbpErrCode()).log();
			result.addParam(new Param("referenceId", paymentId));
			if (dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSED.getMessage()) || dbpErrMsg.equalsIgnoreCase(TransactionStatusEnum.REVERSAL_FAILED.getMessage())) {
				result.addParam(new Param("referenceId", paymentId));
				result.addParam(new Param("paymentId", paymentId));
				result.addParam(new Param("legalEntityId", legalEntityId));
			}
			result.addParam(new Param("message", dbpErrMsg));
			result.addParam(new Param("errorDetails", interbanktransactionDTO.getErrorDetails()));
			result.addParam(new Param("dbpErrCode", dbpErrCode));
			result.addParam(new Param("dbpErrMsg", dbpErrMsg));
			result.addParam(new Param("status", status));
			result.addParam(new Param("externalServiceResponse", externalServiceResponse));
			updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber,
					updateParams);
			alert.prepareError("ExecuteTodayScheduledTransactions:interbanktransactionDTO.final result::" + result)
					.log();
			return result;
		}

		if (interbanktransactionDTO.getReferenceId() == null || "".equals(interbanktransactionDTO.getReferenceId())) {
			interbanktransferDelegate.updateStatusUsingTransactionId(transactionid,
					TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
			return ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		interbankDTO = interbanktransactionDTO;
		updateStatusUsingTransactionId(transactionid, TransactionStatusEnum.EXECUTED.getStatus(),
				interbanktransactionDTO.getReferenceId(), updateParams);
		result.appendResult(JSONToResult.convert(interbanktransactionDTO.getExternalServiceResponse().toString()));
		result.addParam(new Param("referenceId", interbanktransactionDTO.getReferenceId()));
		result.addParam(new Param("paymentId", interbanktransactionDTO.getPaymentId()));
		result.addParam(new Param("requestId", interbanktransactionDTO.getRequestId()));
		result.addParam(new Param("transactionsNotes", interbanktransactionDTO.getPaymentNote()));
		result.addParam(new Param("status", transactionStatus.getStatus()));
		result.addParam(new Param("message", transactionStatus.getMessage()));
		result.addParam(new Param("legalEntityId", legalEntityId));
		result.addParam(new Param("httpStatusCode", "200"));
		result.addParam(new Param("externalServiceResponse", externalServiceResponse));
		if (interbankDTO.getOverrides() != null) {
			result.addParam(new Param("overrides", interbankDTO.getOverrides()));
		}
		if (interbankDTO.getOverrideList() != null) {
			result.addParam(new Param("overrideList", interbankDTO.getOverrideList()));
		}

		if (interbankDTO.getCharges() != null) {
			result.addParam(new Param("charges", interbankDTO.getCharges()));
		}
		if (interbankDTO.getExchangeRate() != null) {
			result.addParam(new Param("exchangeRate", interbankDTO.getExchangeRate()));
		}
		if (interbankDTO.getTotalAmount() != null) {
			result.addParam(new Param("totalAmount", interbankDTO.getTotalAmount()));
		}
		if (interbankDTO.getMessageDetails() != null) {
			result.addParam(new Param("messageDetails", interbankDTO.getMessageDetails()));
		}
		if (interbankDTO.getQuoteCurrency() != null) {
			result.addParam(new Param("quoteCurrency", interbankDTO.getQuoteCurrency()));
		}
		try {
			_logTransaction(request, response, inputArray, result, transactionStatus,
					transactionStatusDTO.getConfirmationNumber(), interbankdbxDTO, requestid, customerId);
		} catch (Exception e) {
			alert.prepareError("Error occured while audit logging.", e).log();
		}

		return result;
	}

	public InterBankFundTransferDTO updateStatusUsingTransactionId(String transactionId, String status,
			String confirmationNumber, Map<String, Object> extraParams) {
		List<InterBankFundTransferDTO> interbankfundtransferdto = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_UPDATE;
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("transactionId", transactionId);
		requestParams.put("status", status);
		requestParams.put("confirmationNumber", confirmationNumber);
		requestParams.putAll(extraParams);
		alert.prepareError("InterBankFundTransferDTO updateStatusUsingTransactionId :requestParams:" + requestParams)
				.log();
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray interbankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			interbankfundtransferdto = JSONUtils.parseAsList(interbankJsonArray.toString(),
					InterBankFundTransferDTO.class);
		} catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the interbankfundtransfer", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured while updating the interbankfundtransfer", exp).log();
			return null;
		}
		if (interbankfundtransferdto != null && interbankfundtransferdto.size() != 0)
			return interbankfundtransferdto.get(0);
		return null;
	}

	private void _logTransaction(DataControllerRequest request, DataControllerResponse response, Object[] inputArray,
			Result result, TransactionStatusEnum transactionStatus, String referenceId,
			InterBankFundTransferDTO interbankDTO, String requestId, String customerId) {

		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE))
			return;
		try {
			ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApproversBusinessDelegate.class);
			Map<String, Object> customer = getCustomerMap(customerId, request);

			AuditLog auditLog = new AuditLog();

			String eventType = Constants.MAKE_TRANSFER;
			String eventSubType = "";
			String producer = "Transactions/POST(createTransfer)";
			String statusID = "";
			String frequencyType = result.getParamValueByName(Constants.FREQUENCYTYPE);
			String isScheduled = result.getParamValueByName(Constants.ISSCHEDULED);
			boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
			String fromAccountNumber = "";
			String toAccountNumber = "";

			if (request.containsKeyInRequest("validate")) {
				String validate = request.getParameter("validate");
				if (StringUtils.isNotBlank(validate) && validate.equalsIgnoreCase("true"))
					return;
			}

			if (request.containsKeyInRequest("fromAccountNumber")) {
				fromAccountNumber = request.getParameter("fromAccountNumber");
			}
			if (request.containsKeyInRequest("toAccountNumber")) {
				toAccountNumber = request.getParameter("toAccountNumber");
			}
			;

			JsonObject customParams = new JsonObject();
			customParams.addProperty("FirstName",
					customer.get("FullName") != null ? customer.get("FullName").toString() : "");
			customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName(Constants.REFERENCEID));
			customParams = auditLog.buildCustomParamsForAlertEngine(fromAccountNumber, toAccountNumber, customParams);
			alert.prepareError("ExecuteTodayScheduledTransactions:_logTransaction:eventType:" + eventType
					+ ",eventSubType:" + eventSubType + ",statusID:" + statusID + ",customParams:" + customParams
					+ ",transactionStatus:" + transactionStatus).log();
			eventSubType = auditLog.deriveSubTypeForExternalTransfer(isScheduled, frequencyType,
					FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE);
			alert.prepareError("ExecuteTodayScheduledTransactions:_logTransaction:eventType:" + eventType
					+ ",eventSubType:" + eventSubType).log();
			alert.prepareError(
					"ExecuteTodayScheduledTransactions:_logTransaction:result:" + ResultToJSON.convert(result)).log();
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
					if (interbankDTO == null) {
						statusID = Constants.SID_EVENT_FAILURE;
					}
					if (interbankDTO.getDbpErrMsg() != null && !interbankDTO.getDbpErrMsg().isEmpty()) {
						statusID = Constants.SID_EVENT_FAILURE;
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
				case PENDING:
					statusID = Constants.SID_EVENT_SUCCESS;
					customParams.addProperty(Constants.REFERENCEID, referenceId);
					eventSubType = Constants.PENDING_APPROVAL_ + eventSubType;
					List<String> approvers = approversBusinessDelegate.getRequestApproversList(requestId);
					if (approvers == null) {
						customParams.addProperty(Constants.APPROVERS, "");
					} else {
						customParams.addProperty(Constants.APPROVERS, approvers.toString());
					}
					break;
				default:
					break;
				}
			}
			alert.prepareError("ExecuteTodayScheduledTransactions:_logTransaction:isSMEUser:" + isSMEUser).log();
			if (isSMEUser) {
				customParams.addProperty("approvedBy", "N/A");
				customParams.addProperty("rejectedBy", "N/A");
			}

			AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);
			alert.prepareError("ExecuteTodayScheduledTransactions:_logTransaction:eventType:" + eventType
					+ ",eventSubType:" + eventSubType + ",statusID:" + statusID + ",customParams:" + customParams)
					.log();
			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null, customerId,
					null, customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine." + e).log();
		}
	}

	public HashMap<String, Object> payloadForServiceCalls_old(Map<String, Object> inputParams, String transactionid) {
		HashMap<String, Object> externalServicepayload = new HashMap<String, Object>();
		HashMap<String, Object> payload = new HashMap<String, Object>();
		String transactionCurrency = inputParams.get("transactionCurrency") != null
				? inputParams.get("transactionCurrency").toString()
				: "";
		if (transactionCurrency.equalsIgnoreCase("NPR")) {
			externalServicepayload.put("amount", inputParams.get("transactionAmount"));
		} else if (transactionCurrency.equalsIgnoreCase("USD")) {
			externalServicepayload.put("amount", inputParams.get("convertedAmount"));
		}
		externalServicepayload.put("currency", "NPR"/* inputParams.get("transactionCurrency") */);
		externalServicepayload.put("debtorAgent", inputParams.get("debtorAgent"));
		externalServicepayload.put("debtorBranch", inputParams.get("debtorBranch"));
		externalServicepayload.put("debtorName", inputParams.get("debtorName"));
		externalServicepayload.put("debtorAccount", inputParams.get("fromAccountNumber"));
		externalServicepayload.put("creditorAgent", inputParams.get("bankId"));
		externalServicepayload.put("creditorBranch",
				inputParams.get("creditorBranch") != null ? inputParams.get("creditorBranch").toString() : "1");
		externalServicepayload.put("creditorName", inputParams.get("beneficiaryName"));
		externalServicepayload.put("creditorAccount", inputParams.get("toAccountNumber"));
		externalServicepayload.put("remarks", inputParams.get("transactionsNotes"));
		if (StringUtils.isNotBlank(transactionid)) {
			inputParams.put("interbankDbxTransactionId", transactionid);
			externalServicepayload.put("interbankDbxTransactionId", transactionid);
		}
		payload.put("internalServicePayload", inputParams);
		payload.put("externalServicepayload", externalServicepayload);
		alert.prepareError("ExecuteTodayScheduledTransactions:payloadForServiceCalls:" + payload).log();
		return payload;

	}
	public HashMap<String, Object>  payloadForServiceCalls(Map<String, Object> inputParams, String requestId) throws Exception {
		HashMap<String, Object> payload = new HashMap<String, Object>();
		HashMap<String, Object>internalPayload = (HashMap<String, Object>) inputParams;
		internalPayload.put("transactionRefNo", requestId);	
		String transactionCurrency=inputParams.get("transactionCurrency")!=null?inputParams.get("transactionCurrency").toString():"";
		String amount= "";
		if(transactionCurrency.equalsIgnoreCase("NPR")) {
			amount=inputParams.get("transactionAmount").toString();
		}else if(transactionCurrency.equalsIgnoreCase("USD")) {
			amount=inputParams.get("convertedAmount").toString();
		}
		HashMap<String, Object> externalServicepayload = internalPayload;
		externalServicepayload.put("amount", amount);
		try {
		externalServicepayload = GenerateNCHLPayload.prepareOtherBankTransferPayload(externalServicepayload, null);
		}catch (ApplicationException e) {
			throw new Exception(e.getMessage());
		}
		externalServicepayload.put("transactionRefNo", requestId);
		payload.put("internalServicePayload", internalPayload);
		payload.put("externalServicepayload", externalServicepayload);
		return payload;
		
	}

	public static Map<String, Object> getCustomerMap(String id, DataControllerRequest dcRequest) throws Exception {
		{
			Map<String, Object> resultMap = new HashMap<>();
			try {
				Map inputParams = new HashMap<String, Object>();
				inputParams.put("$filter", "id eq '" + id + "'");
				Result cusResult = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMER_GET);
				resultMap = formatUserAttributeRecordtoMap(cusResult);
			} catch (HttpCallException e) {
				diagnostic.prepareDebug("Error occured while fetching user attributes.. ").log();
			}

			if (resultMap.isEmpty()) {
				return dcRequest.getServicesManager().getIdentityHandler().getUserAttributes();
			}

			return resultMap;
		}
	}

	public static Map<String, Object> formatUserAttributeRecordtoMap(Result result) throws Exception {
		Dataset customer = result.getDatasetById("customer");
		Map<String, Object> userAttributesMap = new HashMap<String, Object>();
		if (customer.getAllRecords().size() > 0) {
			Record userAttributeRecord = customer.getAllRecords().get(0);
			if (userAttributeRecord != null) {
				for (Param param : userAttributeRecord.getAllParams()) {
					userAttributesMap.put(param.getName(), param.getValue());
				}
			}
		}
		return userAttributesMap;
	}

}
