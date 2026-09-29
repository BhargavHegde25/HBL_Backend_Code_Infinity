package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class TransactionPINRequestApproveReject implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(TransactionPINRequestApproveReject.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		LOG.debug("HBL:TransactionPINRequestApproveReject :");
		/**
		 * parameters to be pass in the Payload request status customerid customername
		 * referenceid
		 */
		Result result = new Result();
		try {
			String adminStatus = request.getParameter("status"); // It can be APPROVED or REJECTED
			String customerid = request.getParameter("customerid");

			if (adminStatus.equalsIgnoreCase("APPROVED")) {
				// Update Temporary pin at customer table and trigger email
				updateStausinTransactionPINReqTable(request);
				updateTempPINInCustomerTable(request, customerid);
				result.setParam(new Param("ReqStatus", "Approved"));
			} else {
				// Trigger email as rejected to customer
				updateStausinTransactionPINReqTable(request);
				emailOfReqRejected(request);
				result.setParam(new Param("ReqStatus", "Rejected"));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));

		} catch (Exception e) {
			LOG.error("Exception occured in TransactionPINRequestApproveReject:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public int updateTempPINInCustomerTable(DataControllerRequest request, String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		String temproryPin = generateTemPIN();
		LOG.debug("temproryPin ##" + temproryPin);
		String hashPin = Utils.hashPin(temproryPin);
		LOG.debug("temprory hashPin: ##"+ hashPin);
		
		inputParams.put("Pin", hashPin);
		// inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";
		int isSuccess = 0;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post update Transaction Pin CustomerTable");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create pin entry in dbxDb Customer Table due to : " + errMessage);
			isSuccess = 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					isSuccess = 1;
					/*** Trigger Email to customer as Request **/
					/** Inserting record into credentialchecker table,
					 * TO validate EXPIRY of Temporary Transaction PIN based on createdts
					 */
					String activationToken = UUID.randomUUID().toString();
					Map<String, Object> map = new HashMap<>();
					map.put("id", activationToken);
					map.put("UserName", customerid);//infinity customer id
					map.put("linktype", "REQUESTTRANSACTIONPIN");
					map.put("createdts", HelperMethods.getCurrentTimeStamp());
					ServiceCallHelper.invokeServiceAndGetJson(map, serviceHeaders, URLConstants.CREDENTIAL_CHECKER_CREATE);
					
					emailOfReqApproved(request, temproryPin);
				}
			} catch (Exception e) {
				LOG.debug("Couldn't  update TransactionPin record");
				isSuccess = 0;
			}
		}
		return isSuccess;

	}

	public int updateStausinTransactionPINReqTable(DataControllerRequest request) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		String status = request.getParameter("status");
		String referenceid = request.getParameter("referenceid");
		inputParams.put("status", status);
		inputParams.put("id", referenceid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_transactionpinResetReq_update";
		int isSuccess = 0;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post update transactionpinResetReq");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't update status in transactionpinResetReq : " + errMessage);
			isSuccess = 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					isSuccess = 1;
				}
			} catch (Exception e) {
				LOG.debug("Couldn't  update transactionpinResetReq ");
				isSuccess = 0;
			}
		}
		return isSuccess;

	}

	public String generateTemPIN() {
		String TempPIN = "";
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		TempPIN = String.format("%06d", number);
		LOG.debug("TempPIN###" + TempPIN);
		// this will convert any number sequence into 6 character.
		return TempPIN;
	}

	private void emailOfReqApproved(DataControllerRequest request, String temproryPin)
			throws Exception {
		String customerid = request.getParameter("customerid");
		//String email = getCustomerEmail(request, customerid);
		JSONObject customerEmail = Utils.getContactDetails(request, customerid);
		LOG.debug("getContactDetails value:" + customerEmail.toString());
		String email = customerEmail.optString("email");
		String phone = customerEmail.optString("phone");
		String phone1 = phone.split("-")[1];
		LOG.debug("Customer email from Transact ### :"+ customerEmail);
		LOG.debug("Customer phone from Transact ### :"+ phone1);
		String customername = request.getParameter("customername");
		String referenceid = request.getParameter("referenceid");

		String emailTemplate = "emailTempleteResetPINReqApproved";

		ServicesManager sm;
		sm = request.getServicesManager();

		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String customerCareNumber = paramHelper.getServerProperty("RESETPIN_CUSTOMERCARE_NUMBER");
		String resetTemporaryValidity = paramHelper.getServerProperty("RESET_TEMPORARY_VALIDITY");
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("customername", customername);
		addContext.put("temporaryPin", temproryPin);
		addContext.put("resetTemporaryValidity", resetTemporaryValidity);
		addContext.put("customecarenumber", customerCareNumber);
		addContext.put("referenceid", referenceid);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		String smsBody = "Dear " +  customername + ", Your Digital Banking Transaction PIN has been reset. Temporary PIN is " + temproryPin +
				". Please update the PIN within " + resetTemporaryValidity +" hrs). For support; call "+ customerCareNumber + " - Himalayan Bank.";
		
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
		SMSOfReqApprovedReject(smsBody, phone1);
	}
	
	private void SMSOfReqApprovedReject(String body, String phonenumber) {
		try {
			LOG.debug("sendSms.phonenumber:" + phonenumber);
			LOG.debug("sendSms.body:" + body);
			HashMap<String, Object> headerParams = new HashMap<String, Object>();
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("contact", phonenumber);
			inputParams.put("body", body);
			String smsResponse = DBPServiceExecutorBuilder.builder().withServiceId("sendHBLSMS")
					.withOperationId("sendHBLSMS").withRequestParameters(inputParams).withRequestHeaders(headerParams)
					.withDataControllerRequest(null).build().getResponse();
			LOG.debug("sendSms.response:" + smsResponse);
		} catch (Exception e) {
			LOG.debug("Error occured while sending SMS." + e);
		}
	}

	private void emailOfReqRejected(DataControllerRequest request) throws Exception {
		String customerid = request.getParameter("customerid");
		LOG.debug("customerid :###" + customerid);
		//String customerEmail = Utils.getCustomerEmialfromCore(request, customerid);
		JSONObject customerEmail = Utils.getContactDetails(request, customerid);
		LOG.debug("getContactDetails value:" + customerEmail.toString());
		String email = customerEmail.optString("email");
		String phone = customerEmail.optString("phone");
		String phone1 = phone.split("-")[1];
		LOG.debug("Customer email from Transact ### :"+ customerEmail);
		LOG.debug("Customer phone from Transact ### :"+ phone1);
		String customername = request.getParameter("customername");
		String referenceid = request.getParameter("referenceid");

		String emailTemplate = "emailTempleteResetPINReqReject";

		ServicesManager sm;
		sm = request.getServicesManager();

		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String customerCareNumber = paramHelper.getServerProperty("RESETPIN_CUSTOMERCARE_NUMBER");
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		input.put("EmailType", emailTemplate);
		JSONObject addContext = new JSONObject();
		addContext.put("customername", customername);
		addContext.put("customecarenumber", customerCareNumber);
		addContext.put("referenceid", referenceid);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		
		String smsBody = "Dear " + customername + ", Your Digital Banking Transaction PIN reset request (Ref: " + referenceid +") was rejected due to security reasons. For support, call " + customerCareNumber +" - Himalayan Bank.";
		
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
		SMSOfReqApprovedReject(smsBody, phone1);

	}
	
}
