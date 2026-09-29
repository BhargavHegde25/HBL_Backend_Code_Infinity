package com.bct.javaservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.bct.utilities.Utils;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class TransactionPINResetValidation implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(TransactionPINResetValidation.class);

	@SuppressWarnings("deprecation")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			LOG.debug("HBL:ResetTransactionPINResetRequest :");
			String TemporaryPIN = request.getParameter("temporaryPIN");
			String NewPIN = request.getParameter("newPIN");

			String errcode = validateTemporaryPIN(request, TemporaryPIN, NewPIN);

			if (errcode.equalsIgnoreCase("000")) {
				result.setParam(new Param("message",
						"You have successfully reset your Transaction PIN. Now, you can seamlessly execute all your financial transactions by providing the new PIN"));
				result.setParam(new Param("code", "000"));
			} else if (errcode.equalsIgnoreCase("001")) {
				result.setParam(new Param("message",
						"Temporary PIN you entered does not match the one sent to you in the email. Please try entering a valid PIN."));
				result.setParam(new Param("code", "001"));
			} else if (errcode.equalsIgnoreCase("002")) {
				result.setParam(new Param("message",
						"You have entered an Invalid/expired PIN. Please try entering a valid PIN"));
				result.setParam(new Param("code", "002"));
			} else if (errcode.equalsIgnoreCase("004")) {
				result.setParam(
						new Param("message", "Pin Should not be a repetition of last 5 Transaction PIN’s used."));
				result.setParam(new Param("code", "004"));
			} else {
				result.setParam(
						new Param("message", "Sorry, we are unable to process your request. Kindly try again later"));
				result.setParam(new Param("code", "003"));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in TransactionPINResetValidation:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	@SuppressWarnings("unused")
	private String validateTemporaryPIN(DataControllerRequest request, String TemporaryPIN, String NewPIN)
			throws AppRegistryException {
		String errcode = "";
		boolean pinStatus = false;
		String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		//String UserName = ArrangementsUtils.getUserAttributeFromIdentity(request, "UserName");
		// String customerId = "2062635101";
		LOG.debug("TemporaryPIN ##" + TemporaryPIN);
		LOG.debug("customerId ##" + customerId);
		String UserName = getUserNamefromCustomerId(request, customerId);
		//String UserName = "8062735165";
		
		LOG.debug("UserName ##" + UserName);
		LOG.debug("NewPIN ##" + NewPIN);
		try {
			// String filter = CommonUtils.buildOdataCondition("UserName", Constants.EQUAL,
			// customerId);

			HashMap<String, Object> inputParams = new HashMap<String, Object>();
			// inputParams.put("$filter", "UserName" + " eq " + customerId + " ORDER BY
			// createdts DESC LIMIT 1");
			// request.addRequestParam_("$filter", "UserName" + " eq " + customerId + "
			// ORDER BY createdts DESC LIMIT 1");
			// inputParams.put("$filter", "UserName eq " + customerId + " order by createdts
			// desc limit 1");
			inputParams.put(DBPUtilitiesConstants.FILTER, "UserName eq " + customerId);
			inputParams.put(DBPUtilitiesConstants.ORDERBY, "createdts desc");
			inputParams.put(DBPUtilitiesConstants.TOP, "1");

			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();

			Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, "dbxdb_credentialchecker_get", false);
			Dataset credentialcheckerDataset = result.getDatasetById("credentialchecker");

			LOG.debug("credentialcheckerDataset ##" + credentialcheckerDataset.toString());
			// String currentDate = HelperMethods.getCurrentTimeStamp();
			// LOG.debug("currentDate ##"+ currentDate);
			if (null != credentialcheckerDataset) {
				String dateFromDB = credentialcheckerDataset.getRecord(0).getParamValueByName("createdts");
				LOG.debug("dateFromDB ##" + dateFromDB);
				if (!isTemproryPINExpired(request, dateFromDB)) {
					/*** If entered Temporary PIN not expired ******/
					String tempPINFromApp = TemporaryPIN;
					String tempPINFromDB = tempPINFromCustomerTable(request, customerId);
					LOG.debug("tempPINFromApp ##" + tempPINFromApp);
					LOG.debug("tempPINFromDB ##" + tempPINFromDB);
					if (tempPINFromApp.equalsIgnoreCase(tempPINFromDB)) {
						//check last five pin entries
						if (checkPinLastFiveEntriesCheck(request, customerId, NewPIN)) {
							pinEntryinDB(request, customerId, NewPIN);
							updateTempPINInCustomerTable(request, customerId, NewPIN);
							errcode = "000";
							// You have successfully reset your Transaction PIN. Now, you can seamlessly
							// execute all your financial transactions by providing the new PIN
						}else {
							//Entered new pin matching with last five entries
							errcode = "004";
						}
						
					} else {
						errcode = "001";
						// Temporary PIN you entered does not match the one sent to you in the email.
						// Please try entering a valid PIN
					}

				} else {
					errcode = "002";
					// You have entered an Invalid/expired PIN. Please try entering a valid PIN
				}
			} else {
				errcode = "003";
			}

			LOG.debug("getPINOfCustomer customerInfo:" + pinStatus);
		} catch (Exception e) {
			LOG.error("Error at validateTemporaryPIN function: " + e);
		}

		return errcode;

	}
	
	public static boolean pinEntryinDB(DataControllerRequest dcRequest, String customerId, String pin)
			throws HttpCallException {
		boolean isPinEntryMade = false;
		HashMap<String, String> hashMap = new HashMap<>();
		String id = UUID.randomUUID().toString();
		LOG.debug("ID Value : " + id);
		hashMap.put("id", id);
		if (StringUtils.isBlank(customerId)) {
			customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		}
		hashMap.put("Customer_id", customerId);
		hashMap.put("PreviousPin", pin);
		hashMap.put("createdby", HelperMethods.getUserFromIdentityService(dcRequest).get("userName"));
		Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
				URLConstants.PIN_HISTORY_CREATE);

		if (HelperMethods.hasRecords(result)) {
			isPinEntryMade = true;
		} else {
			isPinEntryMade = false;
		}
		LOG.debug("isPinEntryMade at the end of makePINEntry : " + isPinEntryMade);

		return isPinEntryMade;
	}
	
/*	private String checkPinFiveEntries(DataControllerRequest request, String Pin, String UserName, String customerid)
			throws Exception {

		HashMap<String, Object> headerParams = new HashMap<String, Object>();
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("pin", Pin);
		inputParams.put("userName", UserName);

		Result result = CommonUtils.callIntegrationService(request, inputParams, headerParams, "TransactionPIN",
				"pinHistoryEntry", false);
		LOG.debug("Result***" + ResultToJSON.convert(result));
		LOG.debug("Result of pinHistoryEntrySuccess***" + result.getParamValueByName("pinHistoryEntrySuccess"));

		return result.getParamValueByName("pinHistoryEntrySuccess");
	}
	*/
	public boolean checkPinLastFiveEntriesCheck(DataControllerRequest dcRequest, String customerId, String pin)
			throws HttpCallException {

		HashMap<String, String> hashMap = new HashMap<>();
		//String customerId = getCustomerId(dcRequest, userName);
		boolean status = true;

		hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + customerId);
		hashMap.put(DBPUtilitiesConstants.ORDERBY, "createdts desc");
		hashMap.put(DBPUtilitiesConstants.TOP, "5");
		hashMap.put(DBPUtilitiesConstants.SKIP, "0");
		LOG.debug("InputParams for HBL" + URLConstants.PIN_HISTORY_GET + " : " + hashMap);

		Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
				URLConstants.PIN_HISTORY_GET);

		LOG.debug("Result for HBL" + URLConstants.PIN_HISTORY_GET + " : " + ResultToJSON.convert(result));

		if (!HelperMethods.hasError(result) && null != result.getAllDatasets()
				&& result.getAllDatasets().get(0).getAllRecords().isEmpty()) {
			return true;
		}
		if (HelperMethods.hasRecords(result)) {
			List<Record> records = result.getAllDatasets().get(0).getAllRecords();
			for (Record record : records) {
				LOG.debug("PIN value ###" + pin);
				LOG.debug("previous pin value ###" + record.getParam("PreviousPin").getValue());
				if (record.getNameOfAllParams().contains("PreviousPin")
						&& StringUtils.isNotBlank(record.getParam("PreviousPin").getValue())
						&& pin.equalsIgnoreCase(record.getParam("PreviousPin").getValue())) {
					status = false;
				}
			}
		}
		LOG.debug("Status value ###" + status);

		return status;
	}

	public static boolean isTemproryPINExpired(DataControllerRequest dcRequest, String createdDate)
			throws AppRegistryException {
		ServicesManager sm;
		sm = dcRequest.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		LOG.debug("Created Date ##" + createdDate);
		String hoursToAdd = paramHelper.getServerProperty("RESET_TEMPORARY_VALIDITY");
		LOG.debug("hoursToAdd ##" + hoursToAdd);
		LOG.debug("hours to minutes ##" + (Integer.parseInt(hoursToAdd) * 60));
		boolean isTemproryPINExpired = false;
		Date createdts = HelperMethods.getFormattedTimeStamp(createdDate);
		Date now = new Date();
		LOG.debug("Current date##" + now);
		Calendar cal = Calendar.getInstance();
		cal.setTime(createdts);
		cal.add(Calendar.MINUTE, (Integer.parseInt(hoursToAdd) * 60));
		createdts = cal.getTime();
		LOG.debug("Created Date after adding hours##" + createdts);
		if (now.after(createdts)) {
			isTemproryPINExpired = true;
		} else {
			isTemproryPINExpired = false;
		}

		LOG.debug("isTemproryPINExpired at the end of isTemproryPINExpired : " + isTemproryPINExpired);

		return isTemproryPINExpired;
	}

	public static String addHoursToStringDate(String dateString, String pattern, int hoursToAdd) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		Date date = sdf.parse(dateString);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hoursToAdd);

		return sdf.format(calendar.getTime());
	}

	public static String tempPINFromCustomerTable(DataControllerRequest request, String customerid) {
		String Pin = "";
		try {
			String filter = CommonUtils.buildOdataCondition("id", Constants.EQUAL, customerid);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				Pin = customerDataset.getRecord(0).getParamValueByName("Pin");
			} else {
				LOG.debug("Else tempPINFromCustomerTable:");
			}
			LOG.debug("tempPINFromCustomerTable id:" + Pin);

		} catch (Exception e) {
			LOG.error("Error while retrieving Pin for Customer " + customerid);
		}
		return Pin;
	}

	public int updateTempPINInCustomerTable(DataControllerRequest request, String customerid, String newPIN)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		LOG.debug("newPIN ##" + newPIN);
		LOG.debug("customerid ##" + customerid);
		
		String hashPin = Utils.hashPin(newPIN);
		LOG.debug("new hashPIN: ##"+ hashPin);
		
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
			isSuccess = 1;
		}
		return isSuccess;
	}
	
	private String getUserNamefromCustomerId(DataControllerRequest request, String customerid) {

		String username = "";
		try {

			String filter = CommonUtils.buildOdataCondition("id", Constants.EQUAL, customerid);
			LOG.debug("getUserNamefromCustomerId filter:"+ filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			LOG.debug("getUserNamefromCustomerId Result:"+ ResultToJSON.convert(result));
			Dataset customerDataset =  result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				username = customerDataset.getRecord(0).getParamValueByName("UserName");
				LOG.debug("getUserNamefromCustomerId:"+ username);
			} else {
				LOG.debug("Else getUserNamefromCustomerId:");
			}
			LOG.debug("getUserNamefromCustomerId username:" + username);
		} catch (Exception e) {
			LOG.error("Error while retrieving username for Customer id " + username);
		}
		return username;

	}
}
