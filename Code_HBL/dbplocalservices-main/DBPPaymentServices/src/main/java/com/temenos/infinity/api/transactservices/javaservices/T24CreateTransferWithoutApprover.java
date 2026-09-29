package com.temenos.infinity.api.transactservices.javaservices;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class T24CreateTransferWithoutApprover implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>) inputArray[1];
		String transactionType= requestparameters.get("transactionType")!=null?requestparameters.get("transactionType").toString():"";
		String frequency = (requestparameters.get(Constants.PARAM_FREQUENCY_TYPE) != null) ? requestparameters.get(Constants.PARAM_FREQUENCY_TYPE).toString() : null;
		if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
			return createOneTimeTransactionWithoutApproval(requestparameters,request);
		} else {
			return createRecurringTransactionWithoutApproval(requestparameters,request);
		}
	}
	
	private static Result createOneTimeTransactionWithoutApproval(Map<String, Object> requestParameters, DataControllerRequest request) {
		Result result = new Result();
		try {
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
//			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
//			result =  CommonUtils.callIntegrationService(request, requestParameters, serviceHeaders, TemenosConstants.SERVICE_T24IS_PAYMENTORDERS, TemenosConstants.OP_CREATE_PAYMENT_WITHOUT_APPROVER,
//					true);
//			return result;
			boolean isAdminFlow=requestParameters.get("isAdmin")!=null &&(requestParameters.get("isAdmin").equals("1") || requestParameters.get("isAdmin").toString().equalsIgnoreCase("true"))?true:false;
			if (isAdminFlow) {
				String response = DBPServiceExecutorBuilder.builder()
						.withServiceId(TemenosConstants.SERVICE_T24IS_PAYMENTORDERS).withObjectId(null)
						.withOperationId("createPaymentWithoutApproverAdmin")
						.withRequestParameters(requestParameters).withRequestHeaders(request.getHeaderMap())
						.withDataControllerRequest(request).build().getResponse();
				result = JSONToResult.convert(response);
				return result;
			}
			String response =  DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.SERVICE_T24IS_PAYMENTORDERS).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_CREATE_PAYMENT_WITHOUT_APPROVER).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
//			String response =  DBPServiceExecutorBuilder.builder().
//					withServiceId("T24ISPayments").
//					withObjectId(null).
//					withOperationId(TemenosConstants.OP_CREATE_PAYMENT_WITHOUT_APPROVER).
//					withRequestParameters(requestParameters).
//					withRequestHeaders(request.getHeaderMap()).
//					withDataControllerRequest(request).
//					build().getResponse();
//			result = JSONToResult.convert(response);
			alert.prepareError("Response from Backend T24CreateTransferWithoutApprover : "+response).log();
			
			//HBL Changes --- mapping Account Restriction Error meesage
			String errorDetails = result.getParamValueByName("errorDetails");
			if (errorDetails != null && errorDetails.toUpperCase().contains("POSTING.RESTRICT")) {
			    Matcher m = Pattern.compile("(?i)bucket\\s*override\\s*[:\\-]?\\s*([A-Za-z0-9\\-]+)").matcher(errorDetails);
			    if (m.find()) {
			        String bucketCode = m.group(1).toUpperCase();   // "O-11270"
			        String msg = BUCKET_MSGS.containsKey(bucketCode)
			                ? BUCKET_MSGS.get(bucketCode)
			                : "Debit Restriction on Account. Please Contact Branch";
			        result.addParam(new Param("dbpErrMsg", msg, "string"));
			    }
			}
			return result;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create transaction without approval: ", e).log();
			//return "{\"errormsg\":\""+e.getMessage()+"\"}";
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\""+e.getMessage()+"\"}"));
			result.addParam(new Param("dbpErrMsg", "{\"errormsg\":\""+e.getMessage()+"\"}"));
			return result;		
		}
	}
	
	private static Result createRecurringTransactionWithoutApproval(Map<String, Object> requestParameters, DataControllerRequest request) {
		Result result = new Result();
		try {
			alert.prepareError("INPUT to BACKEND: "+new JSONObject(requestParameters).toString()).log();
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			//result =  CommonUtils.callIntegrationService(request, requestParameters, serviceHeaders, TemenosConstants.SERVICE_T24IS_STANDINGORDERS, TemenosConstants.OP_CREATE_STANDINGORDER_WITHOUT_APPROVER,
					//true);
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(TemenosConstants.SERVICE_T24IS_STANDINGORDERS).
					withObjectId(null).
					withOperationId(TemenosConstants.OP_CREATE_STANDINGORDER_WITHOUT_APPROVER).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			result = JSONToResult.convert(response);
			return result;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create transaction without approval: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\""+e.getMessage()+"\"}"));
			return result;			
		}
	}
	
	private static final Map<String, String> BUCKET_MSGS = new HashMap<String, String>();
	static {
	    BUCKET_MSGS.put("O-11622", "Debit Restriction on Account. Please Contact Branch");
	    BUCKET_MSGS.put("O-11270", "Debit Restriction on Account. Please Contact Branch");
	}
}
