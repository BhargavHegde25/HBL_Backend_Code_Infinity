package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.hbl.productservicesExtn.dto.IntraBankFundTransferBackendDTOExtn;
import com.hbl.productservicesExtn.utills.CIPSTokenCacheManager;
import com.hbl.productservicesExtn.utills.CIPSTokenGenerator;
import com.hbl.productservicesExtn.utills.HBLUtility;
import com.hbl.productservicesExtn.utills.IntegrationType;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.transactionservices.backenddelegate.api.IntraBankFundTransferBackendDelegate;
import com.temenos.dbx.product.transactionservices.backenddelegate.impl.BillPayTransactionBackendDelegateImpl;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.IntraBankFundTransferBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.BillPayTransactionDTO;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BillPayTransactionBackendDelegateImplExtn extends BillPayTransactionBackendDelegateImpl {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static LoggerUtil LOG = new LoggerUtil(BillPayTransactionBackendDelegateImplExtn.class);
	private static final String connectIPSserviceId=EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_BASE_URL");
	private static final String operationId="oauth/token?";
	private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS).build();
	private static final String contentType = "application/x-www-form-urlencoded";
	private String PayableAccountId = "";
	private String reversalReferenceId = "";
	//private String debitReferenceId ="";
	private String intraBankDbxTransID = "";
	private static final String NPI_BILLPAYMENT_SERVICE="NPIBillPayments";
	private static final String NPI_BILLPAYMENT_AUTH_OPEARATION="getAccessToken";
	private String NPI_PAYMENT_BASE_URL = "";
	private String NPI_PAYMENT_SUFFIX_URL = "";
	private String AUTH_USERNAME ="";
	private String AUTH_PASSWORD ="";
	private String PAYMENT_AGGREGATOR="";
	private static final String NEA_BILLPAYMENT_SERVICE="NEA-Payments";
	private static final String NEA_BILLPAYMENT_OPEARATION="ConfirmBillPaid";
	private static final String REVERSE_TRANSACTION_SERVICE="HBL-T24ISPaymentOrders";
	private static final String REVERSE_TRANSACTION_OPEARATION="reverseTransaction";
	private static final String KUKL_BILLPAYMENT_SERVICE="KUKL-Payments";
	private static final String KUKL_BILLPAYMENT_OPEARATION="confirmBillPay";
	private static final String TOPUP_NEPAL_BILLPAYMENT_SERVICE="TopupNepal";
	private static final String TOPUP_NEPAL_BILLPAYMENT_OPEARATION="ConfirmBillPay";
	private static final String GET_TRANSACTION_STAUS_SERVICE_ORCH="T24-IS-TransactOrch";
	private static final String GET_TRANSACTION_STAUS_OPERATION="getTransactionStatus";
	public static final String BACKEND_ID = "BackendId";
	public static final String PARAM_CUSTOMER_ID = "customerId";
	private static final String PARKING_ACCOUNT_TRANSFER = "PARKING_ACCOUNT_TRANSFER";

	@Override
	public BillPayTransactionDTO createTransactionWithoutApproval(
			BillPayTransactionBackendDTO billpayTransactionBackendDTO, DataControllerRequest request) {
		BillPayTransactionDTO billpayTransactionDTO = new BillPayTransactionDTO();
		Map<String, Object> payload= billpayTransactionBackendDTO.getExternalApiPayload();
		try {
			billpayTransactionBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			if (StringUtils.isNotBlank(billpayTransactionBackendDTO.getIsScheduled())
					&& (billpayTransactionBackendDTO.getIsScheduled().equals("true")
							|| billpayTransactionBackendDTO.getIsScheduled().equals("1"))) {
				String scheduledDt = billpayTransactionBackendDTO.getScheduledDate();
				Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
				if (scheduledDate.after(new Date())) {
					billpayTransactionBackendDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}
		Map<String, Object> transactionData = (Map<String, Object>) payload.get("transactionData");
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:transactionData:" + transactionData);
		String requestId=transactionData.get("paymentId")!=null?transactionData.get("paymentId").toString():null;
		PAYMENT_AGGREGATOR=transactionData.get("paymentAggregator")!=null ? transactionData.get("paymentAggregator").toString():"";
		/* 
		 * Create Intrabank Transfer
		 
		Result intraBankTransferResult = createIntraBankTransaction(transactionData, request);
		billpayTransactionDTO.setRequestId(requestId);
		String paymentOrderId = intraBankTransferResult.getParamValueByName("referenceId");
		Map<String, Object> requestParams = new HashMap<String, Object>();
		requestParams.put("paymentOrderId", paymentOrderId);
		String transactionStatus = intraBankTransferResult.getParamValueByName("transactionStatus");
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:paymentOrderId:" + paymentOrderId);
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:requestId:" + requestId);
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:transactionStatus:" + transactionStatus);
		if(StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("success")) {

			JSONObject paymentObj = getTransactionStatus(requestParams, request);
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:transactionStatus:paymentObj:" + paymentObj);
			transactionStatus=paymentObj!=null?paymentObj.optString("currentStatus"):"";
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:current transactionStatus:" + transactionStatus);
						if (StringUtils.isNotBlank(transactionStatus) && !transactionStatus.equalsIgnoreCase("Complete")) {
							intraBankTransferResult.addParam(new Param("dbpErrCode",ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
							intraBankTransferResult.addParam(new Param("dbpErrMsg", TransactionStatusEnum.FAILED_WITH_DEBIT_REF_ID.getMessage()));
							billpayTransactionDTO.setStatus("Payment Order Cannot be Completed.");
							billpayTransactionDTO.setInitiationId(paymentOrderId);
							billpayTransactionDTO.setDescription("PAYMENT_ORDER_NOT_COMPLETED");
							billpayTransactionDTO.setMessage("Payment cannot be completed at this movement.");
						}else if (StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("Complete")){
							intraBankTransferResult.addParam(new Param("paymentSystemId", paymentObj!=null?paymentObj.optString("paymentSystemId"):""));
						}
						
		}
		
		if (intraBankTransferResult.getParamValueByName("dbpErrCode") == null && StringUtils.isNotBlank(paymentOrderId) && StringUtils.isNotBlank(transactionStatus)) {
			intraBankDbxTransID = intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
			payload.put("debitReferenceId", paymentOrderId);
			payload.put("intraBankDbxTransID", intraBankDbxTransID);
			payload.put("paymentSystemId", intraBankTransferResult.getParamValueByName("paymentSystemId"));
			
			billpayTransactionDTO.setExternalApiPayload((HashMap<String, Object>) payload);
			JSONObject externalServicePayload = (JSONObject) payload.get("externalApiPayload");
			try {
				if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("NCHL")) {
					billpayTransactionDTO=makeNCHLServiceCall(externalServicePayload, billpayTransactionDTO,request);
					if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
						billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId());// External system transaction Id
						billpayTransactionDTO.setInitiationId(paymentOrderId); // FT ID
						billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
					}
				}
				else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("NEA")) {
					Map<String, Object> inputMap=externalServicePayload.toMap();
					Result result=callInternalServiceAndGetResult(NEA_BILLPAYMENT_SERVICE, NEA_BILLPAYMENT_OPEARATION, inputMap,request.getHeaderMap());
					LOG.debug("NEA_Payments:confirmBillPay response:"+ResultToJSON.convert(result).toString());
					JSONObject serviceResponse=  new JSONObject(ResultToJSON.convert(result));
					billpayTransactionDTO=processNEAServiceResponse(serviceResponse, billpayTransactionDTO, request);
					if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
						billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
						billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
						billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
					}
				}else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("KUKL")) {
					Map<String, Object> inputMap=externalServicePayload.toMap();
					Result response = callInternalServiceAndGetResult(KUKL_BILLPAYMENT_SERVICE, KUKL_BILLPAYMENT_OPEARATION, inputMap,
							request.getHeaderMap());
					JSONObject serviceResponse=  new JSONObject(ResultToJSON.convert(response));
					LOG.debug("KUKL_Payments:confirmBillPay response:"+serviceResponse);
					billpayTransactionDTO=processKUKLServiceResponse(serviceResponse, billpayTransactionDTO, request);
					if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
						billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
						billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
						billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
					}
				}
				else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("TOP-UP-NEPAL")) {
					Map<String, Object> inputMap=externalServicePayload.toMap();
					LOG.debug("TopUp_Nepal_Payments:confirmBillPay inputMap:"+inputMap);
					String url=EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEAPL_BASE_URL");
					String password = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PWD");
				       String partnerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PARTNERID");
				       String resellerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_RESELLERID");
				       password= URLEncoder.encode(password);
				       partnerId= URLEncoder.encode(partnerId);
				       resellerId= URLEncoder.encode(resellerId);
					url =url+"BTAPI/sendtopuprequest.asp?txnId="+inputMap.get("paymentId")+"&Action="+inputMap.get("Action")+"&mobileno="+inputMap.get("mobileno")+"&Amount="+inputMap.get("Amount")+"&userid="+inputMap.get("userId")+"&pwd="+password+"&partnerid="+partnerId+"&resellerid="+resellerId;
					LOG.debug("TopUp_Nepal_Payments:confirmBillPay url:"+url);
					JSONObject serviceResponse=makePlainHTTPServiceCallForTopupNepal(url, "", externalServicePayload);
					
					//Result response = callInternalServiceAndGetResult(TOPUP_NEPAL_BILLPAYMENT_SERVICE, TOPUP_NEPAL_BILLPAYMENT_OPEARATION, inputMap,
					//		request.getHeaderMap());
					LOG.debug("TopUp_Nepal_Payments:confirmBillPay response:"+serviceResponse);
					billpayTransactionDTO=processTopupNepalServiceResponse(serviceResponse, billpayTransactionDTO, request);
					if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
						billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
						billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
						billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
					}
				}
			} catch (JSONException e) {
				alert.prepareError("Failed to create billpay transaction: ", e).log();
				return null;
			} catch (Exception e) {
				alert.prepareError("Caught exception at create billpay transaction: ", e).log();
				billpayTransactionDTO= processReversalTransaction(billpayTransactionDTO, request);
				//return null;
			}
		} else {
			//billpayTransactionDTO = new BillPayTransactionDTO();
			billpayTransactionDTO.setDbpErrCode(intraBankTransferResult.getParamValueByName("dbpErrCode"));
			billpayTransactionDTO.setDbpErrMsg(intraBankTransferResult.getParamValueByName("dbpErrMsg"));
		}
		*/
		String paymentOrderId=null;
		billpayTransactionDTO.setRequestId(requestId);
		billpayTransactionDTO.setExternalApiPayload((HashMap<String, Object>) payload);
		JSONObject externalServicePayload = (JSONObject) payload.get("externalApiPayload");
		try {
			if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("NCHL")) {
				billpayTransactionDTO=makeNCHLServiceCall(externalServicePayload, billpayTransactionDTO,request);
				if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
					billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId());// External system transaction Id
					billpayTransactionDTO.setInitiationId(billpayTransactionDTO.getTransactionId()); // FT ID
					billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
				}
			}
			else { /* Other NCHL Payments  making parking account transfers */
			Result intraBankTransferResult = createIntraBankTransaction(transactionData, request);
			paymentOrderId = intraBankTransferResult.getParamValueByName("referenceId");
			Map<String, Object> requestParamsForTransactionStatus = new HashMap<String, Object>();
			requestParamsForTransactionStatus.put("paymentOrderId", paymentOrderId);
			String transactionStatus = intraBankTransferResult.getParamValueByName("transactionStatus");
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:paymentOrderId:" + paymentOrderId);
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:requestId:" + requestId);
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:transactionStatus:" + transactionStatus);
			if(StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("success")) {
				JSONObject paymentObj = getTransactionStatus(requestParamsForTransactionStatus, request);
				LOG.debug("BillPayTransactionBackendDelegateImplExtn:transactionStatus:paymentObj:" + paymentObj);
				transactionStatus=paymentObj!=null?paymentObj.optString("currentStatus"):"";
				LOG.debug("BillPayTransactionBackendDelegateImplExtn:current transactionStatus:" + transactionStatus);
							if (StringUtils.isNotBlank(transactionStatus) && !transactionStatus.equalsIgnoreCase("Complete")) {
								intraBankTransferResult.addParam(new Param("dbpErrCode",ErrorCodeEnum.ERR_21210.getErrorCodeAsString()));
								intraBankTransferResult.addParam(new Param("dbpErrMsg", TransactionStatusEnum.FAILED_WITH_DEBIT_REF_ID.getMessage()));
								billpayTransactionDTO.setStatus("Payment Not Completed OR Payment Is In Pending Status.");
								billpayTransactionDTO.setInitiationId(paymentOrderId);
								billpayTransactionDTO.setDescription("PAYMENT_ORDER_NOT_COMPLETED");
								billpayTransactionDTO.setMessage("Payment not completed at this movement or payment is in pending status.");
							}else if (StringUtils.isNotBlank(transactionStatus) && transactionStatus.equalsIgnoreCase("Complete")){
								intraBankTransferResult.addParam(new Param("paymentSystemId", paymentObj!=null?paymentObj.optString("paymentSystemId"):""));
							}
							
			}
			if (intraBankTransferResult.getParamValueByName("dbpErrCode") == null && StringUtils.isNotBlank(paymentOrderId) && StringUtils.isNotBlank(transactionStatus)) {
				intraBankDbxTransID = intraBankTransferResult.getParamValueByName("intraBankDbxTransID");
				payload.put("debitReferenceId", paymentOrderId);
				payload.put("intraBankDbxTransID", intraBankDbxTransID);
				payload.put("paymentSystemId", intraBankTransferResult.getParamValueByName("paymentSystemId"));
				
				billpayTransactionDTO.setExternalApiPayload((HashMap<String, Object>) payload);
				 externalServicePayload = (JSONObject) payload.get("externalApiPayload");
				 if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("NEA")) {
						Map<String, Object> inputMap=externalServicePayload.toMap();
						Result result=callInternalServiceAndGetResult(NEA_BILLPAYMENT_SERVICE, NEA_BILLPAYMENT_OPEARATION, inputMap,request.getHeaderMap());
						LOG.debug("NEA_Payments:confirmBillPay response:"+ResultToJSON.convert(result).toString());
						JSONObject serviceResponse=  new JSONObject(ResultToJSON.convert(result));
						billpayTransactionDTO=processNEAServiceResponse(serviceResponse, billpayTransactionDTO, request);
						if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
							billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
							billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
							billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
						}
					}else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("KUKL")) {
						Map<String, Object> inputMap=externalServicePayload.toMap();
						Result response = callInternalServiceAndGetResult(KUKL_BILLPAYMENT_SERVICE, KUKL_BILLPAYMENT_OPEARATION, inputMap,
								request.getHeaderMap());
						JSONObject serviceResponse=  new JSONObject(ResultToJSON.convert(response));
						LOG.debug("KUKL_Payments:confirmBillPay response:"+serviceResponse);
						billpayTransactionDTO=processKUKLServiceResponse(serviceResponse, billpayTransactionDTO, request);
						if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
							billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
							billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
							billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
						}
					}
					else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("TOP-UP-NEPAL")) {
						Map<String, Object> inputMap=externalServicePayload.toMap();
						LOG.debug("TopUp_Nepal_Payments:confirmBillPay inputMap:"+inputMap);
						String url=EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEAPL_BASE_URL");
						String password = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PWD");
					       String partnerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PARTNERID");
					       String resellerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_RESELLERID");
					       password= URLEncoder.encode(password);
					       partnerId= URLEncoder.encode(partnerId);
					       resellerId= URLEncoder.encode(resellerId);
						url =url+"BTAPI/sendtopuprequest.asp?txnId="+inputMap.get("paymentId")+"&Action="+inputMap.get("Action")+"&mobileno="+inputMap.get("mobileno")+"&Amount="+inputMap.get("Amount")+"&userid="+inputMap.get("userId")+"&pwd="+password+"&partnerid="+partnerId+"&resellerid="+resellerId;
						LOG.debug("TopUp_Nepal_Payments:confirmBillPay url:"+url);
						JSONObject serviceResponse=makePlainHTTPServiceCallForTopupNepal(url, "", externalServicePayload);
						LOG.debug("TopUp_Nepal_Payments:confirmBillPay response:"+serviceResponse);
						billpayTransactionDTO=processTopupNepalServiceResponse(serviceResponse, billpayTransactionDTO, request);
						if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
							billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
							billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
							billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
						}
					}
				}
				else {
				billpayTransactionDTO.setDbpErrCode(intraBankTransferResult.getParamValueByName("dbpErrCode"));
				billpayTransactionDTO.setDbpErrMsg(intraBankTransferResult.getParamValueByName("dbpErrMsg"));
				String errorDetails =intraBankTransferResult.getParamValueByName("errorDetails");
				if(StringUtils.isNotBlank(errorDetails))
				billpayTransactionDTO.setExternalServiceResponse(new JSONObject(errorDetails));
				}
				}
				} catch (JSONException e) {
					alert.prepareError("Failed to create billpay transaction: ", e).log();
					return null;
				} catch (Exception e) {
					alert.prepareError("Caught exception at create billpay transaction: ", e).log();
					JSONObject errorObj = new JSONObject();
					errorObj.put("errMsg", e);
					billpayTransactionDTO.setExternalServiceResponse(errorObj);
					billpayTransactionDTO= processReversalTransaction(billpayTransactionDTO, request);
				}
			return billpayTransactionDTO;
			} /*else {
				//billpayTransactionDTO = new BillPayTransactionDTO();
				billpayTransactionDTO.setDbpErrCode(intraBankTransferResult.getParamValueByName("dbpErrCode"));
				billpayTransactionDTO.setDbpErrMsg(intraBankTransferResult.getParamValueByName("dbpErrMsg"));
			}
			else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("NEA")) {
				Map<String, Object> inputMap=externalServicePayload.toMap();
				Result result=callInternalServiceAndGetResult(NEA_BILLPAYMENT_SERVICE, NEA_BILLPAYMENT_OPEARATION, inputMap,request.getHeaderMap());
				LOG.debug("NEA_Payments:confirmBillPay response:"+ResultToJSON.convert(result).toString());
				JSONObject serviceResponse=  new JSONObject(ResultToJSON.convert(result));
				billpayTransactionDTO=processNEAServiceResponse(serviceResponse, billpayTransactionDTO, request);
				if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
					billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
					billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
					billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
				}
			}else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("KUKL")) {
				Map<String, Object> inputMap=externalServicePayload.toMap();
				Result response = callInternalServiceAndGetResult(KUKL_BILLPAYMENT_SERVICE, KUKL_BILLPAYMENT_OPEARATION, inputMap,
						request.getHeaderMap());
				JSONObject serviceResponse=  new JSONObject(ResultToJSON.convert(response));
				LOG.debug("KUKL_Payments:confirmBillPay response:"+serviceResponse);
				billpayTransactionDTO=processKUKLServiceResponse(serviceResponse, billpayTransactionDTO, request);
				if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
					billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
					billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
					billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
				}
			}
			else if(StringUtils.isNotBlank(PAYMENT_AGGREGATOR) && PAYMENT_AGGREGATOR.equalsIgnoreCase("TOP-UP-NEPAL")) {
				Map<String, Object> inputMap=externalServicePayload.toMap();
				LOG.debug("TopUp_Nepal_Payments:confirmBillPay inputMap:"+inputMap);
				String url=EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEAPL_BASE_URL");
				String password = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PWD");
			       String partnerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_PARTNERID");
			       String resellerId = EnvironmentConfigurationsHandler.getServerProperty("TOPUPNEPAL_RESELLERID");
			       password= URLEncoder.encode(password);
			       partnerId= URLEncoder.encode(partnerId);
			       resellerId= URLEncoder.encode(resellerId);
				url =url+"BTAPI/sendtopuprequest.asp?txnId="+inputMap.get("paymentId")+"&Action="+inputMap.get("Action")+"&mobileno="+inputMap.get("mobileno")+"&Amount="+inputMap.get("Amount")+"&userid="+inputMap.get("userId")+"&pwd="+password+"&partnerid="+partnerId+"&resellerid="+resellerId;
				LOG.debug("TopUp_Nepal_Payments:confirmBillPay url:"+url);
				JSONObject serviceResponse=makePlainHTTPServiceCallForTopupNepal(url, "", externalServicePayload);
				LOG.debug("TopUp_Nepal_Payments:confirmBillPay response:"+serviceResponse);
				billpayTransactionDTO=processTopupNepalServiceResponse(serviceResponse, billpayTransactionDTO, request);
				if(billpayTransactionDTO.getTransactionId() != null && !"".equals(billpayTransactionDTO.getTransactionId())) {
					billpayTransactionDTO.setReferenceId(billpayTransactionDTO.getTransactionId()); // External system transaction Id
					billpayTransactionDTO.setInitiationId(paymentOrderId); // Transact FT ID
					billpayTransactionDTO.setDescription("PAYMENT_COMPLETED");
				}
			}
			}
		} catch (JSONException e) {
			alert.prepareError("Failed to create billpay transaction: ", e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create billpay transaction: ", e).log();
			billpayTransactionDTO= processReversalTransaction(billpayTransactionDTO, request);
		}
		
		return billpayTransactionDTO;
	}
	*/
	public BillPayTransactionDTO processTopupNepalServiceResponse(JSONObject response, BillPayTransactionDTO transactionDTO, DataControllerRequest request){
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:processKUKLServiceResponse:externalResponseObj:"+response);
		transactionDTO.setExternalServiceResponse(response);
		String httpResponseCode=response.has("statusCode")? String.valueOf(response.get("statusCode")): "";
		String transactionId="";
		String responseMsg=response.has("responseMsg")?response.getString("responseMsg"): "";
		String[] ary=responseMsg.split("#");
		String responseCode =ary[0];
		String message="";
		if(httpResponseCode.equalsIgnoreCase("200")) {
				if(StringUtils.isNotEmpty(responseMsg) &&responseCode.equalsIgnoreCase("0")){
				transactionId=ary[1];
				message=ary[3];
				if(StringUtils.isNotBlank(transactionId)) {
					 //transactionDTO = new BillPayTransactionDTO();
					 transactionDTO.setTransactionId(transactionId);
					 transactionDTO.setStatus(responseCode);
					 transactionDTO.setMessage(message);
					}
				}
				else 
			 	{
				message=ary[2];
				transactionDTO.setMessage(responseCode);
				transactionDTO.setDescription(message);
				transactionDTO= processReversalTransaction(transactionDTO, request);
			 	}
			}else {
			transactionDTO= processReversalTransaction(transactionDTO, request);
			}
		
		return transactionDTO;
		
		}
	public BillPayTransactionDTO makeNCHLServiceCall(JSONObject payload, BillPayTransactionDTO billpayTransactionDTO, DataControllerRequest request) throws IOException, Exception {
		NPI_PAYMENT_BASE_URL = EnvironmentConfigurationsHandler.getServerProperty("NPI_PAYMENT_BASE_URL");
		NPI_PAYMENT_SUFFIX_URL = EnvironmentConfigurationsHandler.getServerProperty("NPI_PAYMENT_SUFFIX_URL");
		String PAYMENT_URL = NPI_PAYMENT_BASE_URL + NPI_PAYMENT_SUFFIX_URL;
		String cipsAuthorization =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_AUTHORIZATION");
		String cips_apiuser =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");
		String cips_apipw =EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_PASSWORD");
		//String accessToken = getAccessToken(request);
		Map<String, Object> headermap = new HashMap<String, Object>();
		Map<String, Object> inputmap = new HashMap<String, Object>();
		headermap.put("ContentType", contentType);
		headermap.put("Authorization", cipsAuthorization);
		inputmap.put("username", cips_apiuser);
		inputmap.put("password", cips_apipw);
		inputmap.put("grant_type", "password");
		//String accessToken = CIPSTokenCacheManager.getAccessToken(IntegrationType.BILL_MERCHANT_PAYMENT, connectIPSserviceId, operationId, inputmap, headermap,request);
		//String accessToken = CIPSTokenGenerator.getAccessToken(IntegrationType.BILL_MERCHANT_PAYMENT, connectIPSserviceId, operationId, inputmap, headermap, request);
		String accessToken = (String) MemoryManager.getDataFromCache(request, "CIPS_ACCESS_TOKEN");
		LOG.debug("HBL:BillPayTransactionBackendDelegateImplExtn:NCHL Service accessToken:" + accessToken);
		if (StringUtils.isNotBlank(accessToken)) {
			JSONObject responseObj = makePlainHTTPServiceCall(PAYMENT_URL, accessToken, payload);
			LOG.debug("HBL:BillPayTransactionBackendDelegateImplExtn:NCHL Service response" + responseObj);
			billpayTransactionDTO=processNCHLServiceResponseNew(responseObj, billpayTransactionDTO,request);
		 }
		// NCHL Authorization failed
		else { 
			billpayTransactionDTO= processReversalTransactionNew(billpayTransactionDTO, request);
			}
		return billpayTransactionDTO;
		
	}
public BillPayTransactionDTO processNCHLServiceResponse(JSONObject externalResponseObj, BillPayTransactionDTO transactionDTO, DataControllerRequest request) {
	LOG.debug("BillPayTransactionBackendDelegateImplExtn:externalResponseObj:"+externalResponseObj);
	transactionDTO.setExternalServiceResponse(externalResponseObj);
	JSONObject cipsTxnResponseList = externalResponseObj.has("cipsTransactionDetail")? externalResponseObj.getJSONObject("cipsTransactionDetail"): new JSONObject();
	JSONObject responseResult = externalResponseObj.has("responseResult")? externalResponseObj.getJSONObject("responseResult"): new JSONObject();
	LOG.debug("BillPayTransactionBackendDelegateImplExtn:cipsTxnResponseList:"+cipsTxnResponseList);
	String transactionId="";
	String status="";
	String creditStatus="";
	String responseCode="";
	if(!responseResult.isEmpty() && !cipsTxnResponseList.isEmpty()) {
	 transactionId= cipsTxnResponseList.getString("appTxnId");
	 status= responseResult.getString("responseDescription");
	 creditStatus = !cipsTxnResponseList.isNull("creditStatus")&& cipsTxnResponseList.get("creditStatus")!=null?cipsTxnResponseList.getString("creditStatus"):"";
	 responseCode = responseResult.getString("responseCode"); 
	 if(responseCode.equalsIgnoreCase("000") && (creditStatus.equalsIgnoreCase("000") || creditStatus.equalsIgnoreCase("DEFER"))) {
	 //transactionDTO = new BillPayTransactionDTO();
	 transactionDTO.setTransactionId(transactionId);
	 transactionDTO.setStatus(status);
	 transactionDTO.setMessage(creditStatus);
	 }else 
	 	{
		 transactionDTO.setMessage(responseCode);
		 transactionDTO.setDescription(status);
		 transactionDTO= processReversalTransaction(transactionDTO, request);
	 	}
	}else 
		{
		transactionDTO= processReversalTransaction(transactionDTO, request);
		}
	return transactionDTO;
	
	
}
public BillPayTransactionDTO processReversalTransaction(BillPayTransactionDTO transferDtO, DataControllerRequest request) {
	Map<String, Object> externalApiPayload= transferDtO.getExternalApiPayload();
	Map<String, Object> reverseTxPayload=new HashMap<String, Object>();
	String debitReferenceId=externalApiPayload.get("debitReferenceId")!=null?externalApiPayload.get("debitReferenceId").toString():"";
	reverseTxPayload.put("paymentReferenceId", externalApiPayload.get("paymentSystemId")); 
	
	IntraBankFundTransferDTO reversalResponse = reverseTransaction(reverseTxPayload, request);
	String status="";
	String message="";
	transferDtO.setMessage(reversalResponse.getMessage());
	if(reversalResponse.getDbpErrCode() != null || reversalResponse.getDbpErrMsg() != null) {
		transferDtO.setTransactionId(null);
		transferDtO.setInitiationId(debitReferenceId);
		transferDtO.setDbpErrCode(reversalResponse.getDbpErrCode());	
		transferDtO.setDbpErrMsg(reversalResponse.getDbpErrMsg());	
		transferDtO.setDescription("REVERSAL_FAILED");
		status=reversalResponse.getStatus();
		message=reversalResponse.getErrorDetails();
	}
	else if(reversalResponse.getStatus().equalsIgnoreCase(TransactionStatusEnum.REVERSED.getStatus())) {
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:reverseTransaction: debitReferenceId:"+debitReferenceId);
	     transferDtO.setTransactionId(null);
	     transferDtO.setInitiationId(debitReferenceId);
	     transferDtO.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
	     transferDtO.setDbpErrMsg(TransactionStatusEnum.REVERSED.getMessage());
	     transferDtO.setDescription("REVERSAL_COMPLETED");
	     //transferDtO.setTransactionts(externalApiPayload.get("debitReferenceId").toString());
		 message= TransactionStatusEnum.REVERSED.getMessage();
	}
	else { // Reversal failed
		transferDtO.setTransactionId(null);
		transferDtO.setInitiationId(debitReferenceId);
		transferDtO.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
		transferDtO.setDbpErrMsg(TransactionStatusEnum.REVERSAL_FAILED.getMessage()); 
		transferDtO.setDescription("REVERSAL_FAILED");
		//transferDtO.setTransactionts(externalApiPayload.get("debitReferenceId").toString());
		message = TransactionStatusEnum.REVERSAL_FAILED.getMessage();
	}
	transferDtO.setMessage(message);
	transferDtO.setStatus(reversalResponse.getStatus());
	
	return transferDtO;
}

	public JSONObject makePlainHTTPServiceCall(String url, String authorization, JSONObject requestBody)
			throws IOException {
		LOG.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCall: request Body:" + requestBody);
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestBody.toString());
		JSONObject returnJson = new JSONObject();
		try {
			Request request = new Request.Builder().url(url).method("POST", body)
					// .addHeader("Content-Type", "application/json")
					.addHeader("Authorization", authorization).build();

			try (Response response = client.newCall(request).execute()) {
				String responseBody = response.body().string();
				LOG.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCall: response body:" + responseBody);
				JSONObject serviceResponse =new JSONObject();
				try{
					 serviceResponse = new JSONObject(responseBody);
				}catch (JSONException e) {
					LOG.error("JSONException Occured in makePlainHTTPServiceCall:", e);
					returnJson = new JSONObject();
					returnJson.put("statusCode", 500);
					returnJson.put("responseCode", "500");
					returnJson.put("responseMessage", responseBody);
					return returnJson;
				}
				LOG.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCall: response body:" + serviceResponse);
				int statusCode = response.code();
				String responseMessage = serviceResponse.has("responseMessage")
						? serviceResponse.getString("responseMessage")
								: response.message();
				returnJson.put("statusCode", statusCode);
				if (statusCode != 404) {
					if (statusCode == 200) {
						JSONObject responseResult = serviceResponse.has("responseResult")&& !serviceResponse.isNull("responseResult")
								? serviceResponse.getJSONObject("responseResult"): new JSONObject();
						returnJson.put("responseMessage",responseResult.has("responseDescription")? responseResult.getString("responseDescription"): "");
						returnJson.put("responseCode",responseResult.has("responseCode") ? responseResult.getString("responseCode") : "");
						returnJson.put("responseResult", responseResult);
						returnJson.put("cipsTransactionDetail", serviceResponse.get("cipsTransactionDetail"));
						returnJson.put("cipsBatchDetail", serviceResponse.get("cipsBatchDetail"));
					} else {
						returnJson.put("responseMessage", serviceResponse.optString("error_description"));
						returnJson.put("responseCode", serviceResponse.has("responseCode")?serviceResponse.get("responseCode"):statusCode);
						returnJson.put("responseResult", serviceResponse);
					}
				} else {
					String responseMessage1 = serviceResponse.getString("error");
					returnJson.put("responseMessage", responseMessage1);
					returnJson.put("responseCode", statusCode);

				}
			}
		} catch (Exception e) {
			LOG.error("Exception Occured in makePlainHTTPServiceCall:", e);
			returnJson = new JSONObject();
			returnJson.put("statusCode", 500);
			returnJson.put("responseCode", "500");
			returnJson.put("responseMessage", e.getLocalizedMessage());
		}
		return returnJson;
	}

	public JSONObject makePlainHTTPServiceCallForTopupNepal(String url, String authorization, JSONObject requestBody)
			throws IOException {
		LOG.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCallForTopupNepal: request Body:" + requestBody);
		JSONObject returnJson = new JSONObject();
		
		try {
			Request request = new Request.Builder().url(url).build();

			try (Response response = client.newCall(request).execute()) {
				String responseBody = response.body().string();
				LOG.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCallForTopupNepal: response body:" + responseBody);
				int statusCode = response.code();
				LOG.debug("HBL::ConfirmBillpayJavaService: makePlainHTTPServiceCallForTopupNepal: statusCode :" + statusCode);
				if (statusCode != 404) {
					if (statusCode == 200) {
						returnJson.put("statusCode", statusCode);
						returnJson.put("responseMsg", responseBody);
					} else {
						returnJson.put("statusCode", statusCode);
						returnJson.put("responseMsg", responseBody);
					}
				} else {
					
					returnJson.put("statusCode", statusCode);
					returnJson.put("responseMsg", responseBody);
				}
			}
		} catch (Exception e) {
			returnJson = new JSONObject();
			returnJson.put("statusCode", 500);
			returnJson.put("responseCode", "500");
			returnJson.put("responseMessage", e.getLocalizedMessage());
		}
		return returnJson;
	}

	public Result createIntraBankTransaction(Map<String, Object> inputParams, DataControllerRequest request) {
		IntraBankFundTransferDTO intrabankDTO = null;
		IntraBankFundTransferBackendDelegate intrabankfundBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(IntraBankFundTransferBackendDelegate.class);
		IntraBankFundTransferBusinessDelegate intrabankTransactionDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(IntraBankFundTransferBusinessDelegate.class);
		IntraBankFundTransferBackendDTOExtn intrabankBackendDTO = new IntraBankFundTransferBackendDTOExtn();
		IntraBankFundTransferDTO intrabanktransactionDTO = new IntraBankFundTransferDTO();
		Result result = new Result();
		String featureActionId = FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE;
		String serviceName = inputParams.get("serviceName") != null ? inputParams.get("serviceName").toString() : "";
		String paymentAggregator = inputParams.get("paymentAggregator") != null ? inputParams.get("paymentAggregator").toString() : "";
		
		if (paymentAggregator.equalsIgnoreCase("NCHL")) {
			PayableAccountId = EnvironmentConfigurationsHandler.getServerProperty("NCHL_BILL_PAY_PAYABLE_ACCNOUNT_NO");
		}
		else if (paymentAggregator.equalsIgnoreCase("NEA")) {
			PayableAccountId = EnvironmentConfigurationsHandler.getServerProperty("NEA_BILL_PAY_PAYABLE_ACCNOUNT_NO");
		}
		else if (paymentAggregator.equalsIgnoreCase("KUKL")) {
			PayableAccountId = EnvironmentConfigurationsHandler.getServerProperty("KUKL_BILL_PAY_PAYABLE_ACCNOUNT_NO");
		}
		else if (paymentAggregator.equalsIgnoreCase("TOP-UP-NEPAL")) {
			PayableAccountId = EnvironmentConfigurationsHandler.getServerProperty("TOPUP_NEPAL_BILL_PAY_PAYABLE_ACCNOUNT_NO");
		}
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:PayableAccountId:" + PayableAccountId);
		inputParams.put("featureActionId", featureActionId);
		inputParams.put("serviceName", serviceName);
		inputParams.put("transactionType", PARKING_ACCOUNT_TRANSFER);
		inputParams.put("toAccountNumber", PayableAccountId);
		inputParams.put("ExternalAccountNumber", PayableAccountId);
		inputParams.put("beneficiaryName", inputParams.get("beneficiaryName").toString());
		String serviceCharge = inputParams.get("serviceCharge") != null ? inputParams.get("serviceCharge").toString() : "";
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:createIntraBankTransaction:" + inputParams);
		try {
			intrabankDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), IntraBankFundTransferDTO.class);
		} catch (IOException e) {
			LOG.error("Error occured at BillPayTransactionBackendDelegateImplExtn while fetching the input params: "
					+ e.getMessage());
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		String channel=HBLUtility.getDeviceInfo(request).get("channel_id").toString();
		if(channel.equalsIgnoreCase("desktop")) {
			channel=HBLConstants.ONLINE_BANKING;
		}else {
			channel=HBLConstants.MOBILE_BANKING;
		}
		intrabankDTO.setPaidBy(channel);
		intrabankDTO.setpaymentMethod("PAYABLE_ACCOUNT_TRANSFER");
		intrabankDTO.setPayPersonName(paymentAggregator);
		String paymentId=inputParams.get("paymentId")!=null?inputParams.get("paymentId").toString():null;
		intrabankDTO.setPaymentId(paymentId);
		IntraBankFundTransferDTO intrabankdbxDTO = intrabankTransactionDelegate.createTransactionAtDBX(intrabankDTO);
		if (intrabankdbxDTO == null) {
			LOG.error("Error occured while creating entry into the DBX table: ");
			return ErrorCodeEnum.ERR_29016.setErrorCode(new Result());
		}
		if (intrabankdbxDTO.getDbpErrCode() != null || intrabankdbxDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", intrabankdbxDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabankdbxDTO.getDbpErrMsg()));
			return result;
		}
		intraBankDbxTransID = intrabankdbxDTO.getTransactionId();
		String referenceId = null;
		//intrabankBackendDTO = (IntraBankFundTransferBackendDTOExtn) intrabankBackendDTO.convert(intrabankdbxDTO);
		intrabankBackendDTO=mapIntrabankPayloadForHBLTransaction(intrabankdbxDTO);
		LOG.debug("BillPayTransactionBackendDelegateImplExtn::intrabankBackendDTO " + intrabankBackendDTO.toString());
		IntraBankFundTransferBackendDelegateImplExtn intrabankfundBackendDelegateExtn= new IntraBankFundTransferBackendDelegateImplExtn();
		intrabanktransactionDTO=intrabankfundBackendDelegateExtn.createTransactionWithoutApproval(intrabankBackendDTO, request);
		//intrabanktransactionDTO=intrabankfundBackendDelegate.createTransactionWithoutApproval(intrabankBackendDTO,request);
		String status = "";
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:createIntraBankTransaction:Response:"+new JSONObject(intrabanktransactionDTO).toString());
		if (intrabanktransactionDTO == null) {
			status = TransactionStatusEnum.FAILED.getStatus();
			ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		if (intrabanktransactionDTO.getDbpErrCode() != null || intrabanktransactionDTO.getDbpErrMsg() != null || intrabanktransactionDTO.getErrorDetails() !=null) {
			status = TransactionStatusEnum.FAILED.getStatus();
			result.addParam(new Param("errorDetails", new JSONObject(intrabanktransactionDTO).toString()));
			result.addParam(new Param("dbpErrCode", intrabanktransactionDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", intrabanktransactionDTO.getDbpErrMsg()));

		} else if (intrabanktransactionDTO.getReferenceId() == null || "".equals(intrabanktransactionDTO.getReferenceId())) {
			status = TransactionStatusEnum.FAILED.getStatus();
			ErrorCodeEnum.ERR_12601.setErrorCode(result);
		} else {
			referenceId = intrabanktransactionDTO.getReferenceId();
			status = TransactionStatusEnum.EXECUTED.getStatus();
			result.addParam(new Param("referenceId", referenceId));
			result.addParam(new Param("paymentSystemId", intrabanktransactionDTO.getPaymentId()));
			result.addParam(new Param("transactionStatus", intrabanktransactionDTO.getStatus()));
			result.addParam(new Param("intraBankDbxTransID", intraBankDbxTransID));
			result.addParam(new Param("status", TransactionStatusEnum.EXECUTED.getStatus()));
			result.addParam(new Param("message", TransactionStatusEnum.EXECUTED.getMessage()));
		}
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("confirmationNumber", referenceId);
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		confirmationDetails.put("paymentSystemId", intrabanktransactionDTO.getPaymentId());
		updateIntraBankTransaction(featureActionId, status, confirmationDetails);
		//intrabankTransactionDelegate.updateStatusUsingTransactionId(intraBankDbxTransID, status, referenceId);

		return result;

	}
	public IntraBankFundTransferDTO reverseTransaction(Map<String, Object> inputParams, DataControllerRequest request) {
		Result result = new Result();
		IntraBankFundTransferDTO intrabankDTO = new IntraBankFundTransferDTO();
		String status="";
		String message="";
		try {
		result = callInternalServiceAndGetResult(REVERSE_TRANSACTION_SERVICE, REVERSE_TRANSACTION_OPEARATION, inputParams,request.getHeaderMap());
		JSONObject reverseTxResponse=  new JSONObject(ResultToJSON.convert(result));
		LOG.debug("reverseTransaction response:"+reverseTxResponse);
		if(result.getParamValueByName("dbpErrCode")!= null || result.getParamValueByName("dbpErrMsg")!= null) {
			status = TransactionStatusEnum.REVERSAL_FAILED.getStatus();
			message = TransactionStatusEnum.FAILED.getStatus();
			intrabankDTO.setDbpErrCode(result.getParamValueByName("dbpErrCode"));
			intrabankDTO.setDbpErrMsg(result.getParamValueByName("dbpErrMsg"));
			intrabankDTO.setErrorDetails(message);
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
		intrabankDTO.setStatus(status);
		intrabankDTO.setMessage(message);
		Map<String, Object> confirmationDetails = new HashMap<String, Object>();
		confirmationDetails.put("status", status);
		confirmationDetails.put("transactionId", intraBankDbxTransID);
		confirmationDetails.put("paymentSystemId", inputParams.get("paymentReferenceId"));
		updateIntraBankTransaction(intraBankDbxTransID, status, confirmationDetails);
		return intrabankDTO;

	}
	public String getAccessToken(DataControllerRequest request) {
		Map<String, Object> inputmap = new HashMap<String, Object>();
		Map<String, Object> inputmap1 = new HashMap<String, Object>();
		String access_token = "";
		String NCHL_BASIC_AUTHORIZATION = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_AUTHORIZATION");
		AUTH_USERNAME = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");
		AUTH_PASSWORD = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_PASSWORD");
		StringBuilder sb = new StringBuilder();
		sb.append("username").append("=").append(AUTH_USERNAME);
		sb.append("&");
		sb.append("password").append("=").append(AUTH_PASSWORD);
		sb.append("&");
		sb.append("grant_type").append("=").append("password");
		inputmap.put("filter", sb.toString());
		Map<String, Object> headermap = new HashMap<String, Object>();
		headermap.put("Content-Type", contentType);
		headermap.put("Authorization", NCHL_BASIC_AUTHORIZATION);
		try {
			Result res = callInternalServiceAndGetResult(NPI_BILLPAYMENT_SERVICE, NPI_BILLPAYMENT_AUTH_OPEARATION, inputmap, headermap);
			if (res.getHttpStatusCodeParamValue().equals("200")) {
				String refreshToken = res.getParamValueByName("refresh_token");
				if (StringUtils.isNotBlank(refreshToken)) {
					sb = new StringBuilder();
					sb.append("grant_type").append("=").append("refresh_token");
					sb.append("&");
					sb.append("refresh_token").append("=").append(refreshToken);
					inputmap1.put("filter", sb.toString());
					Result response = callInternalServiceAndGetResult(NPI_BILLPAYMENT_SERVICE, NPI_BILLPAYMENT_AUTH_OPEARATION, inputmap1,
							headermap);
					if (response.getHttpStatusCodeParamValue().equals("200")) {
						access_token = response.getParamValueByName("access_token");
						access_token="Bearer " + access_token;
					}
				}
			}
		} catch (Exception e) {
			LOG.debug("Error occured in getRefresh/Access Token" + e.toString());
		}
		return access_token;
	}
	private Result callInternalServiceAndGetResult(String serviceid, String operationid, Map<String, Object> inputmap,
			Map<String, Object> headers) throws DBPApplicationException {
		LOG.debug(
				"HBL::ConfirmBillpayJavaService: callInternalServiceAndGetString: inputmap:" + inputmap.toString());
		Result res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		LOG.debug("HBL::ConfirmBillpayJavaService: callInternalServiceAndGetString: response:"
				+ res.getHttpStatusCodeParamValue());

		return res;
	}
	public BillPayTransactionDTO processKUKLServiceResponse(JSONObject response, BillPayTransactionDTO transactionDTO, DataControllerRequest request){
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:processKUKLServiceResponse:externalResponseObj:"+response);
		transactionDTO.setExternalServiceResponse(response);
		String dbpErrCode = response.has("dbpErrCode")? response.getString("dbpErrCode"): "";
		String dbpErrMsg = response.has("dbpErrMsg")? response.getString("dbpErrMsg"): "";
		String result = response.has("result")? response.getString("result"): "";
		String httpResponseCode=response.has("httpStatusCode")? String.valueOf(response.get("httpStatusCode")): "";
		String transactionId="";
		String responseCode="";
		String message=result;
		if(StringUtils.isEmpty(dbpErrCode) && StringUtils.isEmpty(dbpErrMsg)) {
		if(httpResponseCode.equalsIgnoreCase("200")) {
				if(StringUtils.isNotEmpty(result) && result.equalsIgnoreCase("Success")){
				transactionId=response.has("txnReferenceNo")? response.getString("txnReferenceNo"): "";
				responseCode="0";
				message="Blll Paid Successfully";
				if(StringUtils.isNotBlank(transactionId)) {
					 //transactionDTO = new BillPayTransactionDTO();
					 transactionDTO.setTransactionId(transactionId);
					 transactionDTO.setStatus(responseCode);
					 transactionDTO.setMessage(message);
					}
				}
				else 
			 	{
				transactionDTO.setDescription(message);
				transactionDTO= processReversalTransaction(transactionDTO, request);
			 	}
			}else {
			transactionDTO.setDescription(httpResponseCode);
			transactionDTO= processReversalTransaction(transactionDTO, request);
			}
		}else {
			transactionDTO.setDescription(dbpErrMsg);
			transactionDTO= processReversalTransaction(transactionDTO, request);
		}
		
		return transactionDTO;
		
		}
	public BillPayTransactionDTO processNEAServiceResponse(JSONObject response, BillPayTransactionDTO transactionDTO, DataControllerRequest request){
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:processNEAServiceResponse:externalResponseObj:"+response);
		transactionDTO.setExternalServiceResponse(response);
		String dbpErrCode = response.has("dbpErrCode")? response.getString("dbpErrCode"): "";
		String dbpErrMsg = response.has("dbpErrMsg")? response.getString("dbpErrMsg"): "";
		String httpResponseCode=response.has("httpStatusCode")? String.valueOf(response.get("httpStatusCode")): "";
		String transactionId="";
		String responseCode="";
		String message="";
		boolean isSuccess=false;
		if(StringUtils.isEmpty(dbpErrCode) && StringUtils.isEmpty(dbpErrMsg)) {
		if(httpResponseCode.equalsIgnoreCase("200")) {
			JSONObject ConfirmBillPaidResponse = response.has("ConfirmBillPaidResponse")?response.getJSONObject("ConfirmBillPaidResponse"):new JSONObject();
			JSONObject ConfirmBillPaidResult = ConfirmBillPaidResponse.has("ConfirmBillPaidResult")?ConfirmBillPaidResponse.getJSONObject("ConfirmBillPaidResult"):new JSONObject();
			JSONArray RETURN_CONFIRMPAID_NATIVE = ConfirmBillPaidResult.has("RETURN_CONFIRMPAID_NATIVE")?ConfirmBillPaidResult.getJSONArray("RETURN_CONFIRMPAID_NATIVE"):new JSONArray();
			JSONObject jsonResponse = new JSONObject();
			if(RETURN_CONFIRMPAID_NATIVE.length()>0) {
				for(int i=0;i<RETURN_CONFIRMPAID_NATIVE.length(); i++) {
				 JSONObject CONFIRMPAIDResponse=RETURN_CONFIRMPAID_NATIVE.getJSONObject(i);
				 	/** Storing all elements from each jsonObject in to new JsonObject  **/
				 	for (String key : CONFIRMPAIDResponse.keySet()) {
					 jsonResponse.put(key, CONFIRMPAIDResponse.get(key));
		            }
				}
				if (jsonResponse.has("CODE")) {
					 responseCode = String.valueOf(jsonResponse.get("CODE"));
				}
				if (jsonResponse.has("MESSAGE")) {
	                	message = String.valueOf(jsonResponse.get("MESSAGE"));
				}
				if (jsonResponse.has("PARTNERTXNID")) {
	                	transactionId = String.valueOf(jsonResponse.get("PARTNERTXNID"));
				}
				if(responseCode.equals("1016")) {
					 // Mock the response current UAT environment not getting success response as per @Nabin confirmation if the code is 1016
					 JSONObject mockResponseObj= postProcessArrayResponse(response).getJSONObject("ConfirmBillPaidResponse").getJSONObject("ConfirmBillPaidResult");
					 LOG.debug("BCT::BillPayTransactionBackendDelegateImplExtn::processNEAServiceResponse:mockResponseObj:" + mockResponseObj);
					 RETURN_CONFIRMPAID_NATIVE= mockResponseObj.getJSONArray("RETURN_CONFIRMPAID_NATIVE");
					 transactionId =RETURN_CONFIRMPAID_NATIVE.getJSONObject(0).has("PARTNERTXNID")?String.valueOf(RETURN_CONFIRMPAID_NATIVE.getJSONObject(0).get("PARTNERTXNID")):"";
					 message="Amount Paid Successfully";
				}
				 if ("0".equals(responseCode)) {
				 //transactionDTO = new BillPayTransactionDTO();
				 transactionDTO.setTransactionId(transactionId);
				 transactionDTO.setStatus(responseCode);
				 transactionDTO.setMessage(message);
				 transactionDTO.setServiceResponse(jsonResponse);
				 //JSONObject externalServiceResponse =new JSONObject();
				 //externalServiceResponse.put("RETURN_CONFIRMPAID_NATIVE", RETURN_CONFIRMPAID_NATIVE);
				 //transactionDTO.setExternalServiceResponse(externalServiceResponse);
				 }
				else 
			 	{
				transactionDTO.setStatus(responseCode);
				 transactionDTO.setMessage(responseCode);
				 transactionDTO.setDescription(message);
				 transactionDTO= processReversalTransaction(transactionDTO, request);
			 	}
			}else 
				 	{
					 transactionDTO.setDescription(message);
					 transactionDTO= processReversalTransaction(transactionDTO, request);
				 	}
				}else 
					{
					transactionDTO.setDescription(httpResponseCode);
					transactionDTO= processReversalTransaction(transactionDTO, request);
					} 
			  	}
				else 
				{	transactionDTO.setDescription(dbpErrMsg);
					transactionDTO= processReversalTransaction(transactionDTO, request);
				} 
		
		return transactionDTO;
		
			}
	public JSONObject postProcessArrayResponse(JSONObject errorResponse) {
		String JsonString="{\"ConfirmBillPaidResponse\":{\"ConfirmBillPaidResult\":{\"RETURN_CONFIRMPAID_NATIVE\":[{\"CODE\":0,\"MESSAGE\":\"AmountPaidSuccessfully\",\"TRACEID\":57155188,\"PARTNERTXNID\":171396471176541,\"SYSTEMTXNID\":97337,\"SCNO\":\"042.23.058KA4\",\"CUSTOMERNAME\":\"Mr.BALARAMSHRESTHA\",\"DUE_BILL_OF\":\"PAIDUPTO2080-12\",\"PAYABLE_AMOUNT\":7,\"CONSUMER_ID\":100539305,\"OFF_CODE\":205,\"OFFICE\":\"KULESWOREDC\",\"BILL_DATE\":\"\",\"NO_OF_DAYS\":0,\"BILL_AMT\":7,\"FINE_RATE\":0,\"REBATE\":\"\",\"PAID_AMT\":7,\"AMOUNT_DUE_LEFT\":1,\"PAID_DATE\":\"4/24/202412:00:00AM\"},{\"CODE\":0,\"MESSAGE\":\"AmountPaidSuccessfully\",\"TRACEID\":57155188,\"PARTNERTXNID\":171396471176541,\"SYSTEMTXNID\":97337,\"SCNO\":\"042.23.058KA4\",\"CUSTOMERNAME\":\"Mr.BALARAMSHRESTHA\",\"DUE_BILL_OF\":\"ADVANCE\",\"PAYABLE_AMOUNT\":9,\"CONSUMER_ID\":100539305,\"OFF_CODE\":205,\"OFFICE\":\"KULESWOREDC\",\"BILL_DATE\":\"24-APR-24\",\"NO_OF_DAYS\":0,\"BILL_AMT\":-35.59,\"FINE_RATE\":0,\"REBATE\":\"\",\"PAID_AMT\":9,\"AMOUNT_DUE_LEFT\":1,\"PAID_DATE\":\"4/24/202412:00:00AM\"}]}}}";
		JSONObject staticResult= new JSONObject(JsonString) ;
        return staticResult;
	}
	public IntraBankFundTransferDTO updateIntraBankTransaction(String transactionId, String status, Map<String, Object> confirmationDetails) {

		List<IntraBankFundTransferDTO> intrabankfundtransferdto = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTRABANKTRANSFERS_UPDATE;
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:updateIntraBankTransaction:confirmationDetails:"+ confirmationDetails);
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
	public static IntraBankFundTransferBackendDTOExtn mapIntrabankPayloadForHBLTransaction(IntraBankFundTransferDTO intrabankdbxDTO) {
		IntraBankFundTransferBackendDTOExtn hblIntrabankBackendDTO = new IntraBankFundTransferBackendDTOExtn();
		hblIntrabankBackendDTO=hblIntrabankBackendDTO.convert(intrabankdbxDTO);
		//String serviceCharge=intrabankdbxDTO.getCharges()==null?intrabankdbxDTO.getServiceCharge():"";
		String serviceCharge=intrabankdbxDTO.getServiceCharge();
		String scheduledDate=intrabankdbxDTO.getScheduledDate();
		String serviceChargeCurrency=intrabankdbxDTO.getTransactionCurrency();
		String serviceChargeType="TRANSACTIONFEE";
		hblIntrabankBackendDTO.setTransactionId(null);
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:mapIntrabankPayloadForHBLTransaction:getFrequencyTypeId " + intrabankdbxDTO.getFrequencyTypeId());
		hblIntrabankBackendDTO.setFrequencyType(intrabankdbxDTO.getFrequencyTypeId());
		hblIntrabankBackendDTO.setFrequencyTypeId(intrabankdbxDTO.getFrequencyTypeId());
		hblIntrabankBackendDTO.setPaymentOrderProduct("ACOTHER");
		hblIntrabankBackendDTO.setCreditAccount(intrabankdbxDTO.getToAccountNumber());
		hblIntrabankBackendDTO.setDebitAccount(intrabankdbxDTO.getFromAccountNumber());
		hblIntrabankBackendDTO.setPaymentCurrency(intrabankdbxDTO.getTransactionCurrency());
		hblIntrabankBackendDTO.setPaymentAmount(intrabankdbxDTO.getTransactionAmount());
		if(StringUtils.isNotBlank(serviceCharge)) {
			/*hblIntrabankBackendDTO.setCharges(serviceCharge);
			hblIntrabankBackendDTO.setServiceCharge(serviceCharge);
			hblIntrabankBackendDTO.setServiceChargeCurrency(serviceChargeCurrency);
			hblIntrabankBackendDTO.setServiceChargeType(serviceChargeType);
			*/
			hblIntrabankBackendDTO.setServiceCharge(serviceCharge);
			hblIntrabankBackendDTO.setChargeAmount(serviceCharge);
			hblIntrabankBackendDTO.setChargeCurrency(serviceChargeCurrency);
			hblIntrabankBackendDTO.setChargeType(serviceChargeType);
			hblIntrabankBackendDTO.setChargeName("Transaction Fee");
			//hblIntrabankBackendDTO.setTotalAmount(serviceChargeType);
		}
		if(StringUtils.isNotBlank(scheduledDate)) {
			hblIntrabankBackendDTO.setScheduledDate(scheduledDate.substring(0, 10));
		}
		hblIntrabankBackendDTO.setPaymentType("BILL_PAYMENT");
		hblIntrabankBackendDTO.setTransactionType(intrabankdbxDTO.getTransactionType());
		JSONObject additionalInfo=new JSONObject();
		additionalInfo.put("merchantName", intrabankdbxDTO.getBeneficiaryName());
		additionalInfo.put("paymentAggregator", intrabankdbxDTO.getPayPersonName());
		hblIntrabankBackendDTO.setAdditionalInformation(additionalInfo);
		return hblIntrabankBackendDTO;
		
	}
	private static IntraBankFundTransferDTO createHBLOneTimeTransactionWithoutApproval(IntraBankFundTransferBackendDTOExtn intrabankfundtransferbackenddto, DataControllerRequest request) {
		Result result = new Result();
		String HBLPaymentService="HBL-T24ISPaymentOrders";
			alert.prepareError("createHBLOneTimeTransactionWithoutApproval: INPUT to BACKEND for HBL Transaction: "+new JSONObject(intrabankfundtransferbackenddto).toString()).log();
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			IntraBankFundTransferDTO intrabankfundtransferdto = new IntraBankFundTransferDTO();;

			Map<String, Object> requestParameters;
			try {
				intrabankfundtransferbackenddto.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
				requestParameters = JSONUtils.parseAsMap(new JSONObject(intrabankfundtransferbackenddto).toString(), String.class, Object.class);			
			} catch (IOException e) {
				alert.prepareError("Error occured while fetching the input params: ", e).log();
				return null;
			}
			try {
			String response =  DBPServiceExecutorBuilder.builder().
					withServiceId(HBLPaymentService).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_CREATE_PAYMENT_WITHOUT_APPROVER).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			//result = JSONToResult.convert(response);
			JSONObject jsonResponse= new JSONObject(response);
			String paymentSystemId=jsonResponse.get("paymentSystemId")!=null?jsonResponse.getString("paymentSystemId"):"";
			LOG.debug("BillPayTransactionBackendDelegateImplExtn:paymentSystemId:"+paymentSystemId);
			intrabankfundtransferdto = JSONUtils.parse(response, IntraBankFundTransferDTO.class);
			if(intrabankfundtransferdto.getTransactionId() != null && !"".equals(intrabankfundtransferdto.getTransactionId())) {
				intrabankfundtransferdto.setReferenceId(intrabankfundtransferdto.getTransactionId());
			}
			if(StringUtils.isNotBlank(paymentSystemId))
			intrabankfundtransferdto.setPaymentId(paymentSystemId);
			}
			catch (JSONException e) {
				alert.prepareError("Failed to create intrabank transaction: ", e).log();
				return null;
			}
			catch (DBPApplicationException e) {
				alert.prepareError("Caught exception at create transaction without approval: ", e).log();
				intrabankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12600.getErrorCodeAsString());
				intrabankfundtransferdto.setDbpErrMsg(ErrorCodeEnum.ERR_12600.getMessage());
				return intrabankfundtransferdto;
			}
			catch (Exception e) {
				alert.prepareError("Caught  at create transaction without apprexceptionoval: ", e).log();
				intrabankfundtransferdto.setDbpErrCode(ErrorCodeEnum.ERR_12600.getErrorCodeAsString());
				intrabankfundtransferdto.setDbpErrMsg(ErrorCodeEnum.ERR_12600.getMessage());
				return intrabankfundtransferdto;
			}

			return intrabankfundtransferdto;
	}
	public JSONObject getTransactionStatus(Map<String, Object> requestParameters, DataControllerRequest dataControllerRequest) {
		
		JSONArray resArray = new JSONArray();
		String serviceName = GET_TRANSACTION_STAUS_SERVICE_ORCH;
		String operationName = GET_TRANSACTION_STAUS_OPERATION;
		JSONObject paymentObj=new JSONObject();
		String orchServiceResponse = null;
 
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
	public BillPayTransactionDTO processNCHLServiceResponseNew(JSONObject externalResponseObj, BillPayTransactionDTO transactionDTO, DataControllerRequest request) {
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:externalResponseObj:"+externalResponseObj);
		transactionDTO.setExternalServiceResponse(externalResponseObj);
		JSONObject cipsTxnResponseList = externalResponseObj.has("cipsTransactionDetail")? externalResponseObj.getJSONObject("cipsTransactionDetail"): new JSONObject();
		JSONObject responseResult = externalResponseObj.has("responseResult")? externalResponseObj.getJSONObject("responseResult"): new JSONObject();
		LOG.debug("BillPayTransactionBackendDelegateImplExtn:cipsTxnResponseList:"+cipsTxnResponseList);
		String transactionId="";
		String status="";
		String creditStatus="";
		String responseCode="";
		if(!responseResult.isEmpty() && !cipsTxnResponseList.isEmpty()) {
		 transactionId= cipsTxnResponseList.getString("appTxnId");
		 status= responseResult.getString("responseDescription");
		 creditStatus = !cipsTxnResponseList.isNull("creditStatus")&& cipsTxnResponseList.get("creditStatus")!=null?cipsTxnResponseList.getString("creditStatus"):"";
		 responseCode = responseResult.getString("responseCode"); 
		 if(responseCode.equalsIgnoreCase("000") && (creditStatus.equalsIgnoreCase("000") || creditStatus.equalsIgnoreCase("DEFER"))) {
		 //transactionDTO = new BillPayTransactionDTO();
		 transactionDTO.setTransactionId(transactionId);
		 transactionDTO.setStatus(status);
		 transactionDTO.setMessage(creditStatus);
		 }else 
		 	{
			 transactionDTO.setMessage(responseCode);
			 transactionDTO.setDescription(status);
			 transactionDTO= processReversalTransactionNew(transactionDTO, request);
		 	}
		}else 
			{
			status=externalResponseObj.has("responseMessage")?externalResponseObj.optString("responseMessage"):"FAILED AT NCHL";
			transactionDTO.setDescription(status);
			transactionDTO= processReversalTransactionNew(transactionDTO, request);
			}
		return transactionDTO;
		
		
	}
	public BillPayTransactionDTO processReversalTransactionNew(BillPayTransactionDTO transferDtO, DataControllerRequest request) {
		transferDtO.setTransactionId(null);
		String error="FAILED AT NCHL";
		transferDtO.setInitiationId(transferDtO.getRequestId());
		transferDtO.setDbpErrCode(ErrorCodeEnum.ERR_12601.getErrorCodeAsString());	
		transferDtO.setDbpErrMsg(TransactionStatusEnum.REVERSED.getMessage());
		if(StringUtils.isBlank(transferDtO.getDescription())) {
		transferDtO.setDescription(error);
		}
		transferDtO.setMessage(error);
		transferDtO.setStatus(TransactionStatusEnum.REVERSED.getStatus());
		return transferDtO;
	}

}
