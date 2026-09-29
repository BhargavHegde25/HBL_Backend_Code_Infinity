package com.bct.javaservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import com.bct.utilities.HBLCommonUtility;

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
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CreatePaperStatements implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CreatePaperStatements.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		LOG.debug("HBL:CreatePaperStatements :");
		Result result = new Result();
		try {
			String UserName = request.getParameter("username");
			LOG.debug("UserName :" + UserName);
			String accountNumber = request.getParameter("accountNumber");
			LOG.debug("accountNumber :" + accountNumber);
			String requestedDate = request.getParameter("requestedDate");
			LOG.debug("requestedDate :" + requestedDate);
			String startDate = request.getParameter("startDate");
			LOG.debug("startDate :" + startDate);
			String endDate = request.getParameter("endDate");
			LOG.debug("endDate :" + endDate);
			JSONObject customerInfo = getCustomerInfoFromUsername(request, UserName);
			String customerId = customerInfo.getString("id");
			LOG.debug("customerId :" + customerId);
			// String customerEmail = getCustomerEmail(request, customerId);
			String customerEmail = Utils.customerEmailFromSession(request);
			LOG.debug("customerEmail :" + customerEmail);
			String customerPhone = getCustomerPhone(request, customerId);
			LOG.debug("customerPhone :" + customerPhone);

			String dateFromDB = getRequestedDatefrmContractAcc(request, customerId, accountNumber);
			LOG.debug("dateFromDB :" + dateFromDB);
			if (dateFromDB != null && !dateFromDB.trim().isEmpty()) {
				if (compareDates(requestedDate, dateFromDB) != 1) {
					result.setParam(new Param("isPrevReqInProcess", "true"));
					result.setParam(new Param("isEmaisent", "false"));
				} else {
					triggerEmailToBank(request, customerInfo);
					String id = getIdfrmContractAcc(request, customerId, accountNumber);
					updateContractAccWithRequestedDate(request, id, customerId, accountNumber);

					result.setParam(new Param("isEmaisent", "true"));
				}
			} else {
				triggerEmailToBank(request, customerInfo);
				String id = getIdfrmContractAcc(request, customerId, accountNumber);
				updateContractAccWithRequestedDate(request, id, customerId, accountNumber);

				result.setParam(new Param("isEmaisent", "true"));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in CreatePaperStatements:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	private void triggerEmailToBank(DataControllerRequest request, JSONObject customerInfo) throws HttpCallException {

		// portfolioName contractaccounts
		String customerId = customerInfo.getString("id");
		Map<String,String> ph = HBLCommonUtility.getCustomerDetails(customerId, request);
		String mobileNo = "";
		String email = "";
		String branch = "";
		String customerName = "";
		
		customerName = (String) ph.get("customerName");
		LOG.debug("customer ph :" + ph);
		
		branch = (String) ph.get("branch");
		LOG.debug("customer branch :" + branch);
		
		email = (String) ph.get("email");
		LOG.debug("customer email :" + email);
		
		mobileNo = (String) ph.get("mobile");
		LOG.debug("customer mobileNo :" + mobileNo);
		
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");
		/*String customerName = customerInfo.has("FullName") ? customerInfo.getString("FullName") : "";
		if (StringUtils.isBlank(customerName)) {
			input.put("FirstName", customerInfo.getString("FirstName"));
			input.put("LastName", customerInfo.getString("LastName"));
		} else if (StringUtils.isNotBlank(customerName)) {
			input.put("FirstName", customerName);
		}
		*/
		String frmAcc = request.getParameter("accountNumber");
		frmAcc = HBLCommonUtility.maskAccountNumber(frmAcc, 0, frmAcc.length() - 4, "X");
		
		input.put("EmailType", "paperStatement");
		String HBL_TO_EMAIL = EnvironmentConfigurationsHandler.getValue("HBL_TO_EMAIL");
		String HBL_CC_EMAIL = EnvironmentConfigurationsHandler.getValue("HBL_CC_EMAIL");
		JSONObject addContext = new JSONObject();
		addContext.put("accountNumber", frmAcc);
		addContext.put("customerName", customerName);
		addContext.put("branchCode", branch);
		addContext.put("mobile", mobileNo);
		addContext.put("email", email);
		addContext.put("stmtrequestDate", request.getParameter("requestedDate"));
		addContext.put("startDate", request.getParameter("startDate"));
		addContext.put("endDate", request.getParameter("endDate"));
		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", HBL_TO_EMAIL);
		input.put("cc", HBL_CC_EMAIL);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

	}

	private String getCustomerPhone(DataControllerRequest request, String customerId) {
		String customerPhone = "";
		try {
			String Type_id = "COMM_TYPE_PHONE";
			LOG.debug("customerId **:" + customerId);
			String contractId = getContractId(request, customerId);
			LOG.debug("contractId **:" + contractId);
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("$filter", "contractId eq " + contractId + " and typeId eq " + Type_id);
			request.addRequestParam_("$filter", "contractId eq " + contractId + " and typeId eq " + Type_id);

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();

			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CONTRACTCOMMUNICATION_GET, false);
			Dataset customerDataset = result.getDatasetById("contractcommunication");
			if (null != customerDataset) {
				customerPhone = customerDataset.getRecord(0).getParamValueByName("value");
			} else {
				LOG.debug("Else getCustomerPhone:");
			}
			LOG.debug("getCustomerPhone id:" + customerPhone);
		} catch (Exception e) {
			LOG.error("Error while retrieving customer Phone for Customer " + customerPhone);
		}
		return customerPhone;
	}

	private String getContractId(DataControllerRequest request, String customerId) {
		String contractId = "";
		try {
			LOG.debug("customerId **" + customerId);
			String filter = CommonUtils.buildOdataCondition("customerId", Constants.EQUAL, customerId);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CONTRACT_CUSTOMERS_GET, false);
			Dataset customerDataset = result.getDatasetById("contractcustomers");
			if (null != customerDataset) {

				contractId = customerDataset.getRecord(0).getParamValueByName("contractId");
			} else {
				LOG.debug("Else getContractId:");
			}

			LOG.debug("getContractId :" + contractId);

		} catch (Exception e) {
			LOG.error("Error while retrieving Contract Id for Customer " + contractId);
		}
		return contractId;
	}

	private JSONObject getCustomerInfoFromUsername(DataControllerRequest request, String UserName) {

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
				// Converting dataset to json array
				JSONArray array = ResultToJSON.convertDataset(customerDataset);
				customerInfo = array.getJSONObject(0);
			}
			LOG.debug("getCustomerIDFromUsername customerInfo:" + customerInfo);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerIDFromUsername for Customer " + UserName);
		}
		return customerInfo;

	}

	private int updateContractAccWithRequestedDate(DataControllerRequest request, String id, String customerId,
			String AccId) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String idVal = getIdfrmContractAcc(request, customerId, AccId);
		String currentDate = getCurrentTimeStamp();
		LOG.debug("getCurrentTimeStamp:" + currentDate);
		LOG.debug("idVal:" + idVal);
		inputParams.put("requestdate", currentDate);
		inputParams.put("id", idVal);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_contractaccounts_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		LOG.debug("Post updateContractAccWithRequestedDate");

		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			LOG.error("Couldn't create entry in dbxDb contractaccounts Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					return 1;
				} else
					return 0;
			} catch (Exception e) {
				LOG.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;
	}

	private String getRequestedDatefrmContractAcc(DataControllerRequest request, String customerId, String AccId) {
		String requestedDate = "";
		try {
			LOG.debug("customerId **:" + customerId);
			String contractId = getContractId(request, customerId);
			LOG.debug("contractId **:" + contractId);
			LOG.debug("AccId **:" + AccId);
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("$filter", "contractId eq " + contractId + " and accountId eq " + AccId);
			request.addRequestParam_("$filter", "contractId eq " + contractId + " and accountId eq " + AccId);

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();

			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, "dbxdb_contractaccounts_get", false);
			Dataset customerDataset = result.getDatasetById("contractaccounts");
			if (null != customerDataset) {
				requestedDate = customerDataset.getRecord(0).getParamValueByName("requestdate");
			} else {
				LOG.debug("Else requestedDate:");
			}
			LOG.debug("Requested Date is:" + requestedDate);
		} catch (Exception e) {
			LOG.error("Error while retrieving customer acc requested date " + requestedDate);
		}
		return requestedDate;

	}

	private String getIdfrmContractAcc(DataControllerRequest request, String customerId, String AccId) {
		String id = "";
		try {
			LOG.debug("customerId **:" + customerId);
			String contractId = getContractId(request, customerId);
			LOG.debug("contractId **:" + contractId);
			LOG.debug("AccId **:" + AccId);
			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			inputParams.put("$filter", "contractId eq " + contractId + " and accountId eq " + AccId);
			request.addRequestParam_("$filter", "contractId eq " + contractId + " and accountId eq " + AccId);

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();

			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, "dbxdb_contractaccounts_get", false);
			Dataset customerDataset = result.getDatasetById("contractaccounts");
			if (null != customerDataset) {
				id = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getIdfrmContractAcc:");
			}
			LOG.debug("Requested Date is:" + id);
		} catch (Exception e) {
			LOG.error("Error while retrieving id from getIdfrmContractAcc" + id);
		}
		return id;

	}

	public static String getCurrentTimeStamp() {
		return getFormattedTimeStamp(new Date(), null);
	}

	public static String getFormattedTimeStamp(Date dt, String format) {
		// String dtFormat = "yyyy-MM-dd'T'HH:mm:ss";//2024/10/24
		String dtFormat = "yyyy/MM/dd";
		if (StringUtils.isNotBlank(format)) {
			dtFormat = format;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(dtFormat);
		return formatter.format(dt);
	}

	public static int compareDates(String startDate, String endDate) throws ParseException {
		int val;
		Date start = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH).parse(startDate);
		Date end = new SimpleDateFormat("yyyy/mm/dd", Locale.ENGLISH).parse(endDate);

		LOG.debug(start);
		LOG.debug(end);

		if (start.compareTo(end) > 0) {
			LOG.debug("start is after end");
			val = 1;
		} else if (start.compareTo(end) < 0) {
			LOG.debug("start is before end");
			val = -1;
		} else if (start.compareTo(end) == 0) {
			LOG.debug("start is equal to end");
			val = 0;
		} else {
			LOG.debug("Something weird happened...");
			val = -1;
		}
		return val;
	}
}
