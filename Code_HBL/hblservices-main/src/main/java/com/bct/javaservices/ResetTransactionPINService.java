package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.utilities.Utils;
import com.google.common.net.HttpHeaders;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class ResetTransactionPINService implements JavaService2{
	private static final Logger LOG = LogManager.getLogger(ResetTransactionPINService.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result1 = new Result();
		try {
			LOG.debug("HBL:ResetTransactionPINService :");
			String UserName = request.getParameter("customerUsername");
			JSONObject customerInfo = getCustomerInfoFromUsername(request, UserName);
			String customerId = customerInfo.getString("id");
			String transactionPin = "";
			Integer flagUpdate = updateTransactionPinInCustomerTable(request, transactionPin, UserName, customerId);
			if (flagUpdate == 1) {
				result1.setParam(new Param("status", "Email sent successfully."));
				// send email notification to the user as he ADMIN disable third party AUTH
				triggerEmail(request, customerInfo);
			} else {
				result1.addParam(new Param("status", "Failed to send Email.", MWConstants.STRING));
				HelperMethods.setSuccessMsgwithCode("Failed to send Email", "10057", result1);
			}

			result1.setParam(new Param("opstatus", "0"));
			result1.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in ResetTransactionPINService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result1);
			result1.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result1.addParam(new Param("success", "false"));
		}
		return result1;
	}
	private JSONObject getCustomerInfoFromUsername(DataControllerRequest request, String UserName) {

		String customerid = "";
		JSONObject customerInfo = new JSONObject();
		try {

			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
				// Converting dataset to json array
				JSONArray array = ResultToJSON.convertDataset(customerDataset);
				customerInfo=array.getJSONObject(0);
			}
			LOG.debug("getCustomerIDFromUsername customerInfo:" + customerInfo);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerIDFromUsername for Customer " + UserName);
		}
		return customerInfo;

	}
	private int updateTransactionPinInCustomerTable(DataControllerRequest request, String pin,
			String UserName, String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String hashPin = Utils.hashPin(pin);
		LOG.debug("hashPin: ##"+ hashPin);
		inputParams.put("Pin", hashPin);
		inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";
		int isSuccess=0;
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post update Transaction Pin CustomerTable");
		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create pin entry in dbxDb Customer Table due to : " + errMessage);
			isSuccess= 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0)
				{
					isSuccess =1;
				}
			} catch (Exception e) {
				LOG.debug("Couldn't  update TransactionPin record");
				isSuccess= 0;
			}
		}
		return isSuccess;

	}
	/**
	 * Fetching email of the customer From identity session 
	 * 
	 * @Author:JANAKIRAM D
	 * */
	
	
	
private void triggerEmail(DataControllerRequest dcRequest, JSONObject customerInfo) throws HttpCallException {
		
		//String email = getCustomerEmail(dcRequest, customerInfo.getString("id"));
	    String email = Utils.customerEmailFromSession(dcRequest);
		LOG.debug("triggerEmail value:" + email);
		String userName = dcRequest.getParameter("userName");
		
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		String customerName=customerInfo.has("FullName")?customerInfo.getString("FullName"):"";
		if(StringUtils.isBlank(customerName)) {
		input.put("FirstName", customerInfo.getString("FirstName"));
		input.put("LastName",  customerInfo.getString("LastName"));
		}else if(StringUtils.isNotBlank(customerName)) {
		input.put("FirstName", customerName);
		}
		input.put("EmailType", "HBL_RESET_TRANSACTION_PIN");
		String activationLink=EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL"); 
		JSONObject addContext = new JSONObject();
		addContext.put("resetPasswordLink", activationLink);
		addContext.put("userName", userName);
		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
		
	}

}
