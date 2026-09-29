package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.IntraBankFundTransferBackendDTOExtn;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.IntraBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.impl.InterBankFundTransferBackendDelegateImpl;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class InterBankFundTransferBackendDelegateImplExtn extends InterBankFundTransferBackendDelegateImpl{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(InterBankFundTransferResourceImplExtn.class);
	private String PayableAccountId="";
	private String reversalTransactionId="";
	//private String intraBankDbxTransID = "";
	private static final String REVERSE_TRANSACTION_SERVICE="HBL-T24ISPaymentOrders";
	private static final String REVERSE_TRANSACTION_OPEARATION="reverseTransaction";
	private static final String GET_TRANSACTION_STAUS_SERVICE_ORCH="T24-IS-TransactOrch";
	private static final String GET_TRANSACTION_STAUS_OPERATION="getTransactionStatus";
	private static final String PARKING_ACCOUNT_TRANSFER = "PARKING_ACCOUNT_TRANSFER";
	
	@Override
	public InterBankFundTransferDTO createTransactionWithoutApproval(InterBankFundTransferBackendDTO interBankFundTransferBackendDTO, DataControllerRequest request) {
		LOG.debug("Enter:InterBankFundTransferBackendDelegateImplExtn");
		String serviceName = HBLConstants.INTER_BANK_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = HBLConstants.INTER_BANK_FUND_TRANSFER_BACKEND_WITHOUT_APPROVER;
		String paymentType="CIPS";

		String createResponse = null;
		InterBankFundTransferDTO interbankfundtransferdto = new InterBankFundTransferDTO();
		
		Map<String, Object> externalApiPayload= interBankFundTransferBackendDTO.getExternalApiPayload();
		Map<String, Object> internalApiPayload= interBankFundTransferBackendDTO.getInternalApiPayload();
		
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:internalApiPayload:"+internalApiPayload);
		String requestId=interBankFundTransferBackendDTO.getRequestId();
		internalApiPayload.put("requestId", requestId);
		/*
		Result intraBankTransferResult=createIntraBankTransaction(internalApiPayload, externalApiPayload, request);
		interbankfundtransferdto.setRequestId(requestId);
		String paymentOrderId = intraBankTransferResult.getParamValueByName("referenceId");
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("paymentOrderId", paymentOrderId);
		String transactionStatus = intraBankTransferResult.getParamValueByName("transactionStatus");
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:paymentOrderId:" + paymentOrderId);
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:transactionStatus:" + transactionStatus);
		interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
		if(StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("success")) {
	
			String intraBankDbxTransID =  intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
			externalApiPayload.put("debitReferenceId", paymentOrderId);
			externalApiPayload.put("intraBankDbxTransID", intraBankDbxTransID);
			externalApiPayload.put("paymentSystemId", intraBankTransferResult.getParamValueByName("paymentSystemId"));
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:"+externalApiPayload);
			interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
			JSONObject paymentObj =getTransactionStatus(requestParams, request);
			transactionStatus=paymentObj!=null?paymentObj.optString("currentStatus"):"";
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:current transactionStatus:" + transactionStatus);
						if (StringUtils.isNotBlank(transactionStatus) && !transactionStatus.equalsIgnoreCase("Complete")) {
							intraBankTransferResult.addParam(new Param("dbpErrCode",ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
							intraBankTransferResult.addParam(new Param("dbpErrMsg", ErrorCodeEnum.ERR_21210.getMessage()));
							intraBankTransferResult.addParam(new Param("errorDetails", "Payment Order cannot be completed at this movement."));
							interbankfundtransferdto.setStatus("Payment Order Cannot be Completed.");
							interbankfundtransferdto.setPaymentNote("PAYMENT_ORDER_NOT_COMPLETED");
							interbankfundtransferdto.setErrorDetails("Payment Order cannot be completed at this movement.");
							interbankfundtransferdto.setMessage("Payment Order cannot be completed at this movement.");
							interbankfundtransferdto.setPaymentId(paymentOrderId);
							//interbankfundtransferdto.setExternalApiPayload(null);
						}
						else if (StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("Complete")){
							intraBankTransferResult.addParam(new Param("paymentSystemId", paymentObj!=null?paymentObj.optString("paymentSystemId"):""));
						}
						
		}
		
		if(intraBankTransferResult.getParamValueByName("dbpErrCode") ==null  && StringUtils.isNotBlank(paymentOrderId) && StringUtils.isNotBlank(transactionStatus) ) {
			String intraBankDbxTransID =  intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
			externalApiPayload.put("debitReferenceId", paymentOrderId);
			externalApiPayload.put("intraBankDbxTransID", intraBankDbxTransID);
			externalApiPayload.put("paymentSystemId", intraBankTransferResult.getParamValueByName("paymentSystemId"));
			if(externalApiPayload.get("amount")!=null ) {
				Double amount = new Double(externalApiPayload.get("amount").toString());
				String domesticTransferThreshould= EnvironmentConfigurationsHandler.getServerProperty("DOMESTIC_TRANSFER_THRESHOULD_LIMIT");
				LOG.debug("InterBankFundTransferBackendDelegateImplExtn:transactionAmount:"+amount+",domesticTransferThreshould"+domesticTransferThreshould);
				Double domesticTransferThreshouldLimit = new Double(domesticTransferThreshould);
				if (amount >= domesticTransferThreshouldLimit) {
				operationName=HBLConstants.INTER_BANK_FUND_TRANSFER_POST_IPS_BATCH_TRANSFERS_OP;
				paymentType="IPS";
				}
			}
			externalApiPayload.put("paymentType", paymentType);
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:"+externalApiPayload);
			interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(externalApiPayload).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createResponse:"+createResponse);
			interbankfundtransferdto = processExtenalServiceResponse(createResponse, interbankfundtransferdto,request);//JSONUtils.parse(createResponse, InterBankFundTransferDTO.class);
			if(interbankfundtransferdto.getTransactionId() != null && !"".equals(interbankfundtransferdto.getTransactionId())) {
				interbankfundtransferdto.setReferenceId(interbankfundtransferdto.getTransactionId());
				interbankfundtransferdto.setPaymentNote("PAYMENT_COMPLETED");
				interbankfundtransferdto.setPaymentId(paymentOrderId);

			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create interbank transaction: ", e).log();
			interbankfundtransferdto.setErrorDetails(e.toString());
			JSONObject errorObj= new JSONObject();
			errorObj.put("errorMessage", e.toString());
			interbankfundtransferdto.setExternalServiceResponse(errorObj);
			interbankfundtransferdto= processReversalTransaction(interbankfundtransferdto, request);
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create interbank transaction: ", e).log();
			interbankfundtransferdto.setErrorDetails(e.toString());
			JSONObject errorObj= new JSONObject();
			errorObj.put("errorMessage", e.toString());
			interbankfundtransferdto.setExternalServiceResponse(errorObj);
			interbankfundtransferdto= processReversalTransaction(interbankfundtransferdto, request);
		}
		}else
		{
			interbankfundtransferdto.setDbpErrCode(intraBankTransferResult.getParamValueByName("dbpErrCode"));	
			interbankfundtransferdto.setDbpErrMsg(intraBankTransferResult.getParamValueByName("dbpErrMsg"));	
			interbankfundtransferdto.setErrorDetails(intraBankTransferResult.getParamValueByName("errorDetails"));	
			interbankfundtransferdto.setExternalApiPayload(null);
		}
		*/
		String paymentOrderId=null;
		interbankfundtransferdto.setRequestId(requestId);
		interbankfundtransferdto.setExternalApiPayload((HashMap<String, Object>) externalApiPayload);
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:"+externalApiPayload);
		JSONObject batchDetails= null;
		JSONArray transactionDetailList= null;
		paymentType=externalApiPayload.get("paymentType")!=null?externalApiPayload.get("paymentType").toString():"";
		String token=externalApiPayload.get("token").toString();
		if(paymentType.equalsIgnoreCase("IPS") ) {
			operationName=HBLConstants.INTER_BANK_FUND_TRANSFER_POST_IPS_BATCH_TRANSFERS_OP;
			batchDetails= (JSONObject) externalApiPayload.get("nchlIpsBatchDetail");
			transactionDetailList= (JSONArray) externalApiPayload.get("nchlIpsTransactionDetailList");
		}else {
			operationName=HBLConstants.INTER_BANK_FUND_TRANSFER_CIPS_BATCH_TRANSFERS_OP;
			batchDetails= (JSONObject) externalApiPayload.get("cipsBatchDetail");
			transactionDetailList= (JSONArray) externalApiPayload.get("cipsTransactionDetailList");
		}
		JSONObject transactionDetailListObj=transactionDetailList.getJSONObject(0);
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalApiPayload:"+externalApiPayload);
		
		externalApiPayload= new HashMap<String, Object>();
		externalApiPayload.put("token",token);
		splitRequestPayload(batchDetails, externalApiPayload);
		splitRequestPayload(transactionDetailListObj, externalApiPayload);
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:final externalApiPayload:"+externalApiPayload);
		
	try {
		createResponse = DBPServiceExecutorBuilder.builder().
				withServiceId(serviceName).
				withObjectId(null).
				withOperationId(operationName).
				withRequestParameters(externalApiPayload).
				withRequestHeaders(request.getHeaderMap()).
				withDataControllerRequest(request).
				build().getResponse();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createResponse:"+createResponse);
		interbankfundtransferdto = processExtenalServiceResponseNew(createResponse, interbankfundtransferdto,request);//JSONUtils.parse(createResponse, InterBankFundTransferDTO.class);
		if(interbankfundtransferdto.getTransactionId() != null && !"".equals(interbankfundtransferdto.getTransactionId())) {
			interbankfundtransferdto.setReferenceId(interbankfundtransferdto.getTransactionId());
			interbankfundtransferdto.setPaymentNote("PAYMENT_COMPLETED");
			interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getTransactionId());

		}
	}
	catch (JSONException e) {
		alert.prepareError("Failed to create interbank transaction: ", e).log();
		interbankfundtransferdto.setErrorDetails(e.toString());
		JSONObject errorObj= new JSONObject();
		errorObj.put("errorMessage", e.toString());
		interbankfundtransferdto.setExternalServiceResponse(errorObj);
		interbankfundtransferdto= processReversalTransaction(interbankfundtransferdto, request);
	}
	catch (Exception e) {
		alert.prepareError("Caught exception at create interbank transaction: ", e).log();
		interbankfundtransferdto.setErrorDetails(e.toString());
		JSONObject errorObj= new JSONObject();
		errorObj.put("errorMessage", e.toString());
		interbankfundtransferdto.setExternalServiceResponse(errorObj);
		interbankfundtransferdto= processReversalTransaction(interbankfundtransferdto, request);
	}
		return interbankfundtransferdto;
	}
	public void splitRequestPayload(JSONObject requestJson, Map<String, Object> requestMap) {
		if(requestJson!=null) {
		Iterator<String> iter = requestJson.keys();
		while (iter.hasNext()) {
		    String key = iter.next();
		    String value=requestJson.optString(key);
		    requestMap.put(key, value);
			}
		}
		
	}
	
	public InterBankFundTransferDTO processExtenalServiceResponse(String response, InterBankFundTransferDTO interbankfundtransferdto, DataControllerRequest request) {
		JSONObject externalResponseObj = new JSONObject(response);
		interbankfundtransferdto.setExternalServiceResponse(externalResponseObj);
		String paymentType=interbankfundtransferdto.getExternalApiPayload().get("paymentType")!=null?interbankfundtransferdto.getExternalApiPayload().get("paymentType").toString():"";
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalResponseObj:"+externalResponseObj);
		JSONArray cipsTxnResponseList = externalResponseObj.has("cipsTxnResponseList")? externalResponseObj.getJSONArray("cipsTxnResponseList"): new JSONArray();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:cipsTxnResponseList:"+cipsTxnResponseList);
		String transactionId="";
		String status="";
		String creditStatus="";
		String responseCode="";
		if (cipsTxnResponseList.length() > 0) {
			transactionId = cipsTxnResponseList.getJSONObject(0).getString("id");
			status = cipsTxnResponseList.getJSONObject(0).getString("responseMessage");
			creditStatus = cipsTxnResponseList.getJSONObject(0).getString("creditStatus");
			responseCode = cipsTxnResponseList.getJSONObject(0).getString("responseCode");
			if (paymentType.equalsIgnoreCase("CIPS") && creditStatus.equalsIgnoreCase("000")
					&& responseCode.equalsIgnoreCase("000")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
			} else if (paymentType.equalsIgnoreCase("IPS") && creditStatus.equalsIgnoreCase("ENTR")
					&& responseCode.equalsIgnoreCase("ENTR")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
			}
			else {
				interbankfundtransferdto = processReversalTransaction(interbankfundtransferdto, request);
			}
		} else {
			interbankfundtransferdto = processReversalTransaction(interbankfundtransferdto, request);
		}
		return interbankfundtransferdto;
		
		
	}
	public InterBankFundTransferDTO processReversalTransaction(InterBankFundTransferDTO interbankfundtransferdto, DataControllerRequest request) {
		Map<String, Object> externalApiPayload= interbankfundtransferdto.getExternalApiPayload();
		Map<String, Object> reverseTxPayload=new HashMap<String, Object>();
		String debitReferenceId=externalApiPayload.get("debitReferenceId")!=null?externalApiPayload.get("debitReferenceId").toString():"";
		reverseTxPayload.put("intraBankDbxTransID", externalApiPayload.get("intraBankDbxTransID"));
		reverseTxPayload.put("paymentReferenceId", externalApiPayload.get("paymentSystemId")); 
		IntraBankFundTransferDTO reversalResponse = reverseTransaction(reverseTxPayload, request);
		String status="";
		String message="";
		interbankfundtransferdto.setMessage(reversalResponse.getMessage());
		if(reversalResponse.getDbpErrCode() != null || reversalResponse.getDbpErrMsg() != null) {
			interbankfundtransferdto.setTransactionId(null);
			interbankfundtransferdto.setConfirmationNumber(externalApiPayload.get("debitReferenceId").toString());
			interbankfundtransferdto.setDbpErrCode(reversalResponse.getDbpErrCode());	
			interbankfundtransferdto.setDbpErrMsg(reversalResponse.getDbpErrMsg());	
			interbankfundtransferdto.setPaymentNote("REVERSAL_FAILED");
			interbankfundtransferdto.setPaymentId(debitReferenceId);
			status=reversalResponse.getStatus();
			message=reversalResponse.getErrorDetails();
		}
		else if(reversalResponse.getStatus().equalsIgnoreCase(TransactionStatusEnum.REVERSED.getStatus())) {
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:reverseTransaction: debitReferenceId:"+debitReferenceId);
			interbankfundtransferdto.setTransactionId(null);
			//interbankfundtransferdto.setConfirmationNumber(externalApiPayload.get("debitReferenceId").toString());
			interbankfundtransferdto.setPaymentId(debitReferenceId);
		     interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
		     interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSED.getMessage());
		     interbankfundtransferdto.setPaymentNote("REVERSAL_COMPLETED");
		     //transferDtO.setTransactionts(externalApiPayload.get("debitReferenceId").toString());
			 message= TransactionStatusEnum.REVERSED.getMessage();
		}
		else { // Reversal failed
			interbankfundtransferdto.setTransactionId(null);
			//interbankfundtransferdto.setConfirmationNumber(externalApiPayload.get("debitReferenceId").toString());
			interbankfundtransferdto.setPaymentId(debitReferenceId);
			interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
			interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage()); 
			interbankfundtransferdto.setPaymentNote("REVERSAL_FAILED");
			//transferDtO.setTransactionts(externalApiPayload.get("debitReferenceId").toString());
			message = TransactionStatusEnum.REVERSAL_FAILED.getMessage();
		}
		interbankfundtransferdto.setMessage(message);
		interbankfundtransferdto.setStatus(reversalResponse.getStatus());
		
		return interbankfundtransferdto;
	}
	public Result createIntraBankTransaction(Map<String, Object> inputParams, Map<String, Object> externalApiPayload, DataControllerRequest request) {
		IntraBankFundTransferDTO intrabankDTO = null;
		IntraBankFundTransferBackendDelegate intrabankfundBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(IntraBankFundTransferBackendDelegate.class);
		IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
		IntraBankFundTransferBackendDTOExtn intrabankBackendDTO = new IntraBankFundTransferBackendDTOExtn();
		IntraBankFundTransferDTO intrabanktransactionDTO = new IntraBankFundTransferDTO();
		Result result = new Result();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:"+inputParams);
		String featureActionId = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
		
		String serviceName=inputParams.get("serviceName")!=null?inputParams.get("serviceName").toString():"";
		
		if(serviceName.equalsIgnoreCase(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE)) {
		 PayableAccountId= EnvironmentConfigurationsHandler.getServerProperty("INTER_BANK_PAYABLE_ACCNOUNT_NO");
		}
		
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:PayableAccountId:"+PayableAccountId);
		String PayableAccountHolderName= "PayableAccountHolderName";
		inputParams.put("featureActionId", featureActionId);
		inputParams.put("serviceName", serviceName);
		inputParams.put("transactionType", PARKING_ACCOUNT_TRANSFER);
		inputParams.put("toAccountNumber", PayableAccountId);
		inputParams.put("ExternalAccountNumber", PayableAccountId);
		inputParams.put("beneficiaryName", PayableAccountHolderName);
		inputParams.put("transactionId", inputParams.get("interbankDbxTransactionId"));
		inputParams.put("validate", "false");
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:inputParams for Payment Order:"+inputParams);
		
		try {
			intrabankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IntraBankFundTransferDTO.class);
			if(inputParams.get("beneficiaryBankName")!=null)
			intrabankDTO.setBeneficiaryBankName(inputParams.get("beneficiaryBankName").toString());
		} catch (IOException e) {
			LOG.error("Error occured at InterBankFundTransferBackendDelegateImplExtn while fetching the input params: "+ e.getMessage());
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		IntraBankFundTransferDTO intrabankdbxDTO = intrabankTransactionDelegate.createTransactionAtDBX(intrabankDTO);
		if(intrabankdbxDTO == null) {
			LOG.error("Error occured while creating entry into the DBX table: ");
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if(intrabankdbxDTO.getDbpErrCode() != null || intrabankdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", intrabankdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabankdbxDTO.getDbpErrMsg()));
			return result;
		}
		String intraBankDbxTransID = intrabankdbxDTO.getTransactionId();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:intraBankTransTxID:"+intraBankDbxTransID);
		String confirmationNumber = null;
		intrabankBackendDTO = intrabankBackendDTO.convert(intrabankdbxDTO);
		intrabankBackendDTO.setTransactionId(null);
		intrabankBackendDTO.setChargeCurrency("NPR");
		intrabankBackendDTO.setChargeType("TRANSACTIONFEE");
		intrabankBackendDTO.setChargeName("Transaction Fee");
		intrabankBackendDTO.setChargeAmount(intrabankdbxDTO.getServiceCharge());
		intrabankBackendDTO.setTransactionCurrency("NPR");
		intrabankBackendDTO.setExchangeRate(intrabankDTO.getExchangeRate());
		if(inputParams.get("paymentType")!=null) {
		intrabankBackendDTO.setPaymentType(inputParams.get("paymentType").toString());
		}
		if(inputParams.get("transactionAmount")!=null)
		intrabankBackendDTO.setTotalAmount(inputParams.get("transactionAmount").toString());
		try {
		String jsonString=JSONUtils.stringify(externalApiPayload);
		intrabankBackendDTO.setAdditionalInformation(new JSONObject(jsonString));
		if(inputParams.get("isAdmin")!=null) {
		intrabankBackendDTO.setIsAdmin(inputParams.get("isAdmin").toString());
		}
		}catch (JSONException | IOException e) {
			LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:JSONException:"+e.getMessage());
		}
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:getAdditionalInformation():"+intrabankBackendDTO.getAdditionalInformation());
		//intrabankBackendDTO.setExchangeRate(intrabankDTO.getExchangeRate());
		//intrabanktransactionDTO = intrabankfundBackendDelegate.createTransactionWithoutApproval(intrabankBackendDTO, request);
		IntraBankFundTransferBackendDelegateImplExtn intrabankfundBackendDelegateExtn= new IntraBankFundTransferBackendDelegateImplExtn();
		intrabanktransactionDTO=intrabankfundBackendDelegateExtn.createTransactionWithoutApproval(intrabankBackendDTO, request);
		String status= "";
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:intrabanktransactionDTO:"+intrabanktransactionDTO.toString());
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:intrabanktransactionDTO.getReferenceId():"+intrabanktransactionDTO.getReferenceId());
		if(intrabanktransactionDTO == null) {	
			status= TransactionStatusEnum.FAILED.getStatus();
		    ErrorCodeEnum.ERR_12601.setErrorCode(result); 
		}
		if(intrabanktransactionDTO.getDbpErrCode() != null || intrabanktransactionDTO.getDbpErrMsg() != null) {
			status= TransactionStatusEnum.FAILED.getStatus();
			result.addParam(new Param("errorDetails", intrabanktransactionDTO.getErrorDetails()));
			result.addParam(new Param("dbpErrCode", intrabanktransactionDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabanktransactionDTO.getDbpErrMsg()));
	       
		}
		else if(intrabanktransactionDTO.getReferenceId() == null || "".equals(intrabanktransactionDTO.getReferenceId())) {
			status= TransactionStatusEnum.FAILED.getStatus();
			ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		else {
		confirmationNumber = intrabanktransactionDTO.getReferenceId();
		status= TransactionStatusEnum.EXECUTED.getStatus();
		result.addParam(new Param("referenceId", confirmationNumber));
		result.addParam(new Param("intraBankDbxTransID", intraBankDbxTransID));
		result.addParam(new Param("paymentSystemId", intrabanktransactionDTO.getPaymentId()));
		result.addParam(new Param("status",TransactionStatusEnum.EXECUTED.getStatus()));
	    result.addParam(new Param("message", TransactionStatusEnum.EXECUTED.getMessage()));
	    result.addParam(new Param("transactionStatus", intrabanktransactionDTO.getStatus()));
		}
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:status:"+status);
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:createIntraBankTransaction:confirmationNumber:"+confirmationNumber);
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("confirmationNumber", confirmationNumber);
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		confirmationDetails.put("paymentSystemId", intrabanktransactionDTO.getPaymentId());
		confirmationDetails.put("paymentId", confirmationNumber);
		updateIntraBankTransaction(confirmationDetails);
		//intrabankTransactionDelegate.updateStatusUsingTransactionId(intraBankDbxTransID,status, confirmationNumber);
		
		return result;
		
	}
	public IntraBankFundTransferDTO updateIntraBankTransaction(Map<String, Object> confirmationDetails) {

		List<IntraBankFundTransferDTO> intrabankfundtransferdto = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKTRANSFERS_UPDATE;
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:updateIntraBankTransaction:confirmationDetails:"+ confirmationDetails);
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(confirmationDetails).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			JSONArray intrabankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			intrabankfundtransferdto = JSONUtils.parseAsList(intrabankJsonArray.toString(), IntraBankFundTransferDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while updating the intrabanktransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while updating the intrabanktransaction",exp).log();
			return null;
		}
		
		if(intrabankfundtransferdto != null && intrabankfundtransferdto.size() != 0)
			return intrabankfundtransferdto.get(0);
		
		return null;
	}
	public IntraBankFundTransferDTO reverseTransaction(Map<String, Object> inputParams, DataControllerRequest request) {
		Result result = new Result();
		IntraBankFundTransferDTO intrabankDTO = new IntraBankFundTransferDTO();
		String status="";
		String message="";
		String intraBankDbxTransID=inputParams.get("intraBankDbxTransID")!=null?inputParams.get("intraBankDbxTransID").toString():"";
		try {
		result = callInternalServiceAndGetResult(REVERSE_TRANSACTION_SERVICE, REVERSE_TRANSACTION_OPEARATION, inputParams,request.getHeaderMap());
		JSONObject reverseTxResponse=  new JSONObject(ResultToJSON.convert(result));
		LOG.debug("reverseTransaction response:"+reverseTxResponse);
		if(result.getParamValueByName("dbpErrCode")!= null || result.getParamValueByName("dbpErrMsg")!= null) {
			status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
			message = TransactionStatusEnum.FAILED.getStatus();
			intrabankDTO.setDbpErrCode(result.getParamValueByName("dbpErrCode"));
			intrabankDTO.setDbpErrMsg(result.getParamValueByName("dbpErrMsg"));
		}
		String reversalStatus=result.getParamValueByName("status");
		String id=result.getParamValueByName("id");
		String transactionStatus=result.getParamValueByName("transactionStatus");
		String transactionMessage=result.getParamValueByName("message");
		 if (StringUtils.isNotBlank(reversalStatus)&& reversalStatus.equalsIgnoreCase("success")) {
			 message = TransactionStatusEnum.REVERSED.getStatus();
			 status = TransactionStatusEnum.REVERSED.getStatus();
			 intrabankDTO.setReferenceId(id);
		} else {
			status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
			message = transactionMessage;
		}
		} catch (DBPApplicationException e) {
			LOG.debug("Exception Occured while reversing the transaction" + e.toString());
			status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
			message = e.getLocalizedMessage();
			intrabankDTO.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());
			intrabankDTO.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage());
			intrabankDTO.setErrorDetails(message);
		}
		intrabankDTO.setErrorDetails(message);
		intrabankDTO.setStatus(status);
		intrabankDTO.setMessage(message);
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		confirmationDetails.put("paymentSystemId", inputParams.get("paymentReferenceId"));
		updateIntraBankTransaction(confirmationDetails);
		return intrabankDTO;

	}
	private Result callInternalServiceAndGetResult(String serviceid, String operationid, Map<String, Object> inputmap,
			Map<String, Object> headers) throws DBPApplicationException {
		LOG.debug(
				"HBL::InterBankFundTransferBackendDelegateImplExtn: callInternalServiceAndGetString: inputmap:" + inputmap.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		LOG.debug("HBL::InterBankFundTransferBackendDelegateImplExtn: callInternalServiceAndGetString: response:"
				+ res.getHttpStatusCodeParamValue());

		return res;
	}
	
public JSONObject getTransactionStatus(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest) {
		
		JSONArray resArray = new JSONArray();
		String serviceName = GET_TRANSACTION_STAUS_SERVICE_ORCH;
		String operationName = GET_TRANSACTION_STAUS_OPERATION;
		String status="";
		String orchServiceResponse = null;
		JSONObject paymentObj=new JSONObject();
 
		try {
			diagnostic.prepareDebug("getTransactionStatus:OrchService:requestParameters:"+requestParameters).log();
			orchServiceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					requestParameters, null, dataControllerRequest);
			 //authorization=com.kony.dbputilities.util.TokenUtils.getT24AuthToken(dataControllerRequest);
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			 String USER_ID = "userID";
			 String CONSTANT_TEMPLATE_NAME = "T24";

			
			dataControllerRequest.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
			 String authToken = TokenUtils.getT24AuthToken(dataControllerRequest);
			 diagnostic.prepareDebug("getTransactionStatus:OrchService:Authorization:"+authToken).log();
			//dataControllerRequest.getHeaderMap().put("Authorization", authToken);
			 orchServiceResponse =  DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dataControllerRequest.getHeaderMap()).
					withDataControllerRequest(dataControllerRequest).
					build().getResponse();
			JSONObject transactionResponse = new JSONObject(orchServiceResponse);
			diagnostic.prepareDebug("getTransactionStatus:OrchService:transactionResponse:"+transactionResponse).log();
			resArray = transactionResponse.getJSONArray("LoopDataset");
			paymentObj=processOrchServceResponse(resArray);
			return paymentObj;
		} catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured getTransactionStatus: ", jsonExp).log();
			return paymentObj;
		} catch (Exception exp) {
			alert.prepareError("Excpetion occured getTransactionStatus: ", exp).log();
			return paymentObj;
		}
	}
	public JSONObject processOrchServceResponse(JSONArray resArray){
		String status="";
		JSONObject obj =new JSONObject();
		if(resArray!=null) {
		for(int i=0;i<resArray.length();i++) {
			obj = resArray.getJSONObject(i);
			status=obj.getString("currentStatus");
			if(status.equalsIgnoreCase("Complete")) {
				break;
			}
		}
		}
		return obj;
		
	}
	public InterBankFundTransferDTO processExtenalServiceResponseNew(String response, InterBankFundTransferDTO interbankfundtransferdto, DataControllerRequest request) {
		JSONObject externalResponseObj = new JSONObject(response);
		interbankfundtransferdto.setExternalServiceResponse(externalResponseObj);
		String paymentType=interbankfundtransferdto.getExternalApiPayload().get("paymentType")!=null?interbankfundtransferdto.getExternalApiPayload().get("paymentType").toString():"";
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:externalResponseObj:"+externalResponseObj);
		JSONArray cipsTxnResponseList = externalResponseObj.has("cipsTxnResponseList")? externalResponseObj.getJSONArray("cipsTxnResponseList"): new JSONArray();
		LOG.debug("InterBankFundTransferBackendDelegateImplExtn:cipsTxnResponseList:"+cipsTxnResponseList);
		String transactionId="";
		String status="";
		String creditStatus="";
		String responseCode="";
		if (cipsTxnResponseList.length() > 0) {
			transactionId = cipsTxnResponseList.getJSONObject(0).getString("id");
			status = cipsTxnResponseList.getJSONObject(0).getString("responseMessage");
			creditStatus = cipsTxnResponseList.getJSONObject(0).getString("creditStatus");
			responseCode = cipsTxnResponseList.getJSONObject(0).getString("responseCode");
			if (paymentType.equalsIgnoreCase("CIPS") && creditStatus.equalsIgnoreCase("000")
					&& responseCode.equalsIgnoreCase("000")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
			} else if (paymentType.equalsIgnoreCase("IPS") && creditStatus.equalsIgnoreCase("ENTR")
					&& responseCode.equalsIgnoreCase("ENTR")) {
				interbankfundtransferdto.setTransactionId(transactionId);
				interbankfundtransferdto.setStatus(status);
				interbankfundtransferdto.setMessage(creditStatus);
			}
			else {
				interbankfundtransferdto.setMessage(status);
				interbankfundtransferdto = processReversalTransactionNew(interbankfundtransferdto, request);
			}
		} else {
			interbankfundtransferdto = processReversalTransactionNew(interbankfundtransferdto, request);
		}
		return interbankfundtransferdto;
		
		
	}
	public InterBankFundTransferDTO processReversalTransactionNew(InterBankFundTransferDTO interbankfundtransferdto, DataControllerRequest request) {
			/*interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
			interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage()); 
			interbankfundtransferdto.setPaymentNote("FAILED AT NCHL");
		interbankfundtransferdto.setStatus(TransactionStatusEnum.FAILED.getStatus());
		interbankfundtransferdto.setErrorDetails("FAILED AT NCHL");
		interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getRequestId());
		*/
		
		interbankfundtransferdto.setTransactionId(null);
		interbankfundtransferdto.setPaymentId(interbankfundtransferdto.getRequestId());
		interbankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
	     interbankfundtransferdto.setDbpErrMsg(TransactionStatusEnum.REVERSED.getMessage());
		interbankfundtransferdto.setPaymentNote("FAILED AT NCHL");
		interbankfundtransferdto.setMessage(TransactionStatusEnum.REVERSED.getMessage());
		interbankfundtransferdto.setStatus(TransactionStatusEnum.REVERSED.getStatus());
	
		return interbankfundtransferdto;
	}

}
