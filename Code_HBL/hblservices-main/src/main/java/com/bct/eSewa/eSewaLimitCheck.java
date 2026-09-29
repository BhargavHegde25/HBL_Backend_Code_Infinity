package com.bct.eSewa;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.hbl.productservicesExtn.constants.HBLConstants;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.UserAgentUtil;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class eSewaLimitCheck {
	private static final Logger logger = LogManager.getLogger(eSewaLimitCheck.class);

	public static double getTodayTransactionAmountTotal(DataControllerRequest request, String frmAccNumber,
			String esewaId, String channel) throws Exception {

		logger.debug("From Account Number: " + frmAccNumber);
		logger.debug("esewaId: " + esewaId);
		logger.debug("channel: " + channel);

		LocalDate today = LocalDate.now();
		// Get next date (tomorrow)
		LocalDate tomorrow = today.plusDays(1);
		// Format dates as strings in YYYY-MM-DD
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String todayStr = today.format(formatter) + "T00:00:00Z";
		String tomorrowStr = tomorrow.format(formatter) + "T00:00:00Z";

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		String filter = "SourceAccountNo eq " + frmAccNumber + " and EsewaId eq " + esewaId + " and channelName eq "
				+ channel + " and TransactionDate ge " + todayStr + " and TransactionDate lt " + tomorrowStr;
		// $filter=SourceAccountNo eq '11001021260027' and EsewaId eq '9849659447' and
		// channel eq 'OLB' and TransactionDate ge 2025-09-15T00:00:00Z and
		// TransactionDate lt 2025-09-16T00:00:00Z

		inputParams.put("$filter", filter);
		request.addRequestParam_("$filter", filter);

		HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
		Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders, "HBLMerchantCRUDService",
				"dbxdb_esewaTransactionLog_get", false);

		logger.debug("esewaTransactionLog Result Current day: " + ResultToJSON.convert(result));
		Dataset customerDataset = result.getDatasetById("esewaTransactionLog");
		double finalAmount = getDailyTotalAmount(customerDataset);
		logger.debug("finalAmount current day: ##" + finalAmount);

		return finalAmount;
	}

	public static double getThisMonthTotalTransactionAmount(DataControllerRequest request, String frmAccNumber,
			String esewaId, String channel) throws Exception {

		logger.debug("From Account Number: " + frmAccNumber);
		logger.debug("esewaId: " + esewaId);
		logger.debug("channel: " + channel);

		// Formatter for YYYY-MM-DD
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// Current date
		LocalDate today = LocalDate.now();
		// Start of the month
		LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
		// End of the month
		LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
		// Format to string
		String startDateStr = startOfMonth.format(formatter) + "T00:00:00Z";
		String endDateStr = endOfMonth.format(formatter) + "T00:00:00Z";

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		String filter = "SourceAccountNo eq " + frmAccNumber + " and EsewaId eq " + esewaId + " and channelName eq "
				+ channel + " and TransactionDate ge " + startDateStr + " and TransactionDate lt " + endDateStr;
		inputParams.put("$filter", filter);
		request.addRequestParam_("$filter", filter);
		HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
		Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders, "HBLMerchantCRUDService",
				"dbxdb_esewaTransactionLog_get", false);
		logger.debug("esewaTransactionLog Result Month: " + ResultToJSON.convert(result));
		Dataset customerDataset = result.getDatasetById("esewaTransactionLog");
		double finalAmount = getDailyTotalAmount(customerDataset);
		logger.debug("finalAmount Month: ##" + finalAmount);

		return finalAmount;
	}

	public static int getMonthlyTransactionCountByAccount(DataControllerRequest request, String frmAccNumber,
			String channel) throws Exception {

		logger.debug("From Account Number: " + frmAccNumber);
		logger.debug("channel: " + channel);

		// Formatter for YYYY-MM-DD
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		// Current date
		LocalDate today = LocalDate.now();
		// Start of the month
		LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
		// End of the month
		LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());
		// Format to string
		String startDateStr = startOfMonth.format(formatter) + "T00:00:00Z";
		String endDateStr = endOfMonth.format(formatter) + "T00:00:00Z";

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		String filter = "SourceAccountNo eq " + frmAccNumber + " and channelName eq " + channel + " and TransactionDate ge "
				+ startDateStr + " and TransactionDate lt " + endDateStr;
		inputParams.put("$filter", filter);
		request.addRequestParam_("$filter", filter);
		HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
		Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders, "HBLMerchantCRUDService",
				"dbxdb_esewaTransactionLog_get", false);
		logger.debug("esewaTransactionLog Result Month: " + ResultToJSON.convert(result));
		Dataset customerDataset = result.getDatasetById("esewaTransactionLog");
		int totalRecords = customerDataset.getAllRecords().size();
		logger.debug("total records current month: ##" + totalRecords + "");

		return totalRecords + 1;
	}

	public static int getDailyTransactionCountByAccount(DataControllerRequest request, String frmAccNumber,
			String channel) throws Exception {

		logger.debug("From Account Number: " + frmAccNumber);
		logger.debug("channel: " + channel);

		LocalDate today = LocalDate.now();
		// Get next date (tomorrow)
		LocalDate tomorrow = today.plusDays(1);
		// Format dates as strings in YYYY-MM-DD
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String todayStr = today.format(formatter) + "T00:00:00Z";
		String tomorrowStr = tomorrow.format(formatter) + "T00:00:00Z";

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		String filter = "SourceAccountNo eq " + frmAccNumber + " and channelName eq " + channel + " and TransactionDate ge "
				+ todayStr + " and TransactionDate lt " + tomorrowStr;

		inputParams.put("$filter", filter);
		request.addRequestParam_("$filter", filter);

		HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
		Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders, "HBLMerchantCRUDService",
				"dbxdb_esewaTransactionLog_get", false);

		logger.debug("esewaTransactionLog Result Current day: " + ResultToJSON.convert(result));
		Dataset customerDataset = result.getDatasetById("esewaTransactionLog");
		int totalRecords = customerDataset.getAllRecords().size();
		logger.debug("total records current day: ##" + totalRecords + "");

		return totalRecords + 1 ;
	}

	public static int getDailyTransactionCountByWallet(DataControllerRequest request, String eSewaId, String channel)
			throws Exception {

		logger.debug("eSewaId: " + eSewaId);
		logger.debug("channel: " + channel);

		LocalDate today = LocalDate.now();
		// Get next date (tomorrow)
		LocalDate tomorrow = today.plusDays(1);
		// Format dates as strings in YYYY-MM-DD
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String todayStr = today.format(formatter) + "T00:00:00Z";
		String tomorrowStr = tomorrow.format(formatter) + "T00:00:00Z";

		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		String filter = "EsewaId eq " + eSewaId + " and channelName eq " + channel + " and TransactionDate ge " + todayStr
				+ " and TransactionDate lt " + tomorrowStr;

		inputParams.put("$filter", filter);
		request.addRequestParam_("$filter", filter);

		HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
		Result result = CommonUtils.callIntegrationService(request, inputParams, svcHeaders, "HBLMerchantCRUDService",
				"dbxdb_esewaTransactionLog_get", false);

		logger.debug("esewaTransactionLog Result  of a wallet: " + ResultToJSON.convert(result));
		Dataset customerDataset = result.getDatasetById("esewaTransactionLog");
		int totalRecords = customerDataset.getAllRecords().size();
		logger.debug("total records current day of a wallet: ##" + totalRecords + "");

		return totalRecords + 1;
	}

	private static double getDailyTotalAmount(Dataset ds) {
		int totalRecords = ds.getAllRecords().size();
		double totalAmount = 0.0;
		try {

			for (int i = 0; i < totalRecords; i++) {
				logger.debug("Inside Loop i value:" + i);
				logger.debug("record size :" + totalRecords);
				double amnt = Double.parseDouble(ds.getRecord(i).getParamValueByName("Amount"));

				totalAmount += amnt;
				logger.error("totalAmount :" + i + " " + amnt);
			}

			logger.error("Total amount :" + totalAmount);
		} catch (Exception e) {
			logger.error("Error while retrieving CustomerType_id for Customer constructMockData:" + e);
		}
		return totalAmount;
	}

	public static String geteSewaChannelLimit(DataControllerRequest request, String key) throws Exception {

		JSONObject supportedAccs = ArrangementsUtils.getBundleConfigurations(TemenosConstants.ACCOUNT_TYPE_BUNDLE_NAME,
				"ESEWA_CHANNEL_LIMIT", request);
		String configVal = "";
		logger.debug("Config Key Value:##"+ key);
		JSONArray dataobj = new JSONArray();
		JSONObject configData = new JSONObject();
		String data = "";
		if (supportedAccs != null) {
			JSONArray configurations = supportedAccs.optJSONArray(TemenosConstants.CONFIGURATIONS);
			if (configurations != null && configurations.length() > 0) {
				configData = configurations.optJSONObject(0);
				if (configData.has(TemenosConstants.DBP_CONFIG_TABLE_VALUE))
					data = configData.getString(TemenosConstants.DBP_CONFIG_TABLE_VALUE);
			}
		}
		logger.debug("geteSewaChannelLimit data::::" + data);
		dataobj = new JSONArray(data);

		JSONArray array = new JSONArray(dataobj);
		logger.debug("geteSewaChannelLimit Length::::" + array.length()+"");
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObj = array.getJSONObject(i);
			
			if (jsonObj.has(key)) {
				configVal = jsonObj.getString(key);
                logger.debug(key + "## Value is "+ configVal);
                break; // found it, exit loop
            }
			
		}
		return configVal;
	}

	public static String getTimestamp() {
		String localDateTime;
		if (LocalDateTime.now().getSecond() == 0) {
			localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
		} else {
			localDateTime = LocalDateTime.now().withNano(0).toString();
		}
		return localDateTime;
	}
	
	public static boolean isWithinDailyLimit(double enteredAmount, double totalToday, double dailyLimit) {
        double projectedTotal = totalToday + enteredAmount;
        return projectedTotal <= dailyLimit;
    }
	
	public static boolean isWithinMonthlyLimit(double enteredAmount, double totalMonth, double monthlyLimit) {
        double projectedTotal = totalMonth + enteredAmount;
        return projectedTotal <= monthlyLimit;
    }
	
	public static String getCurrentChannelOld(DataControllerRequest dcRequest) {
		String channel = "";
		String reportingParams = dcRequest.getHeader("X-Kony-ReportingParams");
		if (StringUtils.isNotBlank(reportingParams)) {
			JSONObject reportingParamsJson;
			try {
				reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
				logger.debug("reportingParamsJson ##", reportingParamsJson.toString());
				String channelId = reportingParamsJson.optString("chnl");
				logger.debug("channelId ##", channelId);
				if (channelId.equalsIgnoreCase("mobile")) {
					channel = "MOBILE";
				} else if (channelId.equalsIgnoreCase("desktop")) {
					channel = "ONLINEBANKING";
				}
			} catch (Exception e) {
				logger.debug("Exception getCurrentDeviceID ", e);
			}

		}
		return channel;
	}
	
	public static String getCurrentChannel(DataControllerRequest dcRequest) {
		String channel = "";
		try {
			UserAgentUtil ua = new UserAgentUtil(dcRequest);
			channel = ua.getChannel();
			logger.debug("HBL::getCurrentChannel1::channel:" + channel);
		} catch (Exception e) {
			logger.debug("HBL::getCurrentChannel::channel error:" + e);
			return channel;
		}
		logger.debug("HBL::getCurrentChannel2::channel:" + channel);
		if (channel.equalsIgnoreCase("desktop")) {
			channel = HBLConstants.ONLINE_BANKING;
		} else if (channel.equalsIgnoreCase("mobile")) {
			channel = HBLConstants.MOBILE_BANKING;
		}
		return channel;
	}
	
	public static int updateeSewaTransLog(DataControllerRequest request, String id, String status, String response_code,
			String responseData, String transaction_status, String originating_unique_id, String transaction_id)
			throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		String currentDate = getTimestamp();
		logger.debug("getCurrentTimeStamp:" + currentDate);
		logger.debug("idVal:" + id);
		inputParams.put("lastmodifiedts", currentDate);
		inputParams.put("id", id);
		inputParams.put("Status", status);
		inputParams.put("StatusCode", status);
		inputParams.put("TransactionStatus", transaction_status);
		inputParams.put("ResponseCode", response_code);
		inputParams.put("RawResponse", responseData);
		inputParams.put("OriginatingUniqueId", originating_unique_id);
		inputParams.put("TransactionDetailOriginatingUniqueId", transaction_id);

		String serviceName = "HBLMerchantCRUDService";
		String operationName = "dbxdb_esewaTransactionLog_update";
		Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
				operationName, false);
		logger.debug("Post updateeSewaTransLog status check");

		String errMessage = result.getParamValueByName(AccountsConstants.PARAM_ERROR_MESSAGE);
		if (StringUtils.isNotBlank(errMessage)) {
			logger.error("Couldn't create entry in dbxDb updateeSewaTransLog Table due to : " + errMessage);
			return 0;
		} else if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
			try {
				if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
					return 1;
				} else
					return 0;
			} catch (Exception e) {
				logger.debug("Couldn't Parse updated records Integer from String");
				return 1;
			}
		}
		return 0;
	}
	
	public static boolean createEsewaTransactionReprocessLog(DataControllerRequest request, String OriginatingUniqueId) {
		boolean isinvalidattemptEntryMade = false;
		try {
			ServicesManager servicesManager;
			servicesManager = request.getServicesManager();
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			
			 String frmAccNumber = request.getParameter("frmAccNumber");
			 logger.debug("BCT::createEsewaTransactionReprocessLog: frmAccNumber param:" + frmAccNumber);
			 String eSewaId = request.getParameter("eSewaId");
			 logger.debug("BCT::createEsewaTransactionReprocessLog: eSewaId param:" + eSewaId);
			 String amount = request.getParameter("amount");
			 logger.debug("BCT::createEsewaTransactionReprocessLog: amount param:" + amount);
			 String paymentReferenceId = request.getParameter("paymentReferenceId");
			 logger.debug("BCT::createEsewaTransactionReprocessLog: paymentReferenceId param:" + paymentReferenceId);
			 String referenceId = request.getParameter("referenceId");
			 logger.debug("BCT::createEsewaTransactionReprocessLog: referenceId param:" + referenceId);
			 String transactionId = request.getParameter("transactionId");
			 logger.debug("BCT::createEsewaTransactionReprocessLog: transactionId param:" + transactionId);
			 
			 logger.debug("BCT::createEsewaTransactionReprocessLog: OriginatingUniqueId param:" + OriginatingUniqueId);
			
			inputParams.put("id", id);
			inputParams.put("OriginatingUniqueId", OriginatingUniqueId);
			inputParams.put("SourceAccountNo", frmAccNumber);
			inputParams.put("EsewaId", eSewaId);
			inputParams.put("Amount",amount );
			inputParams.put("SourceFTID", referenceId);
			inputParams.put("ContraFTID", "");
			inputParams.put("LoggedDate", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::createEsewaTransactionReprocessLog: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.ESEWA_REPROCESS_LOG_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::createEsewaTransactionReprocessLog: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::createEsewaTransactionReprocessLog failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::createEsewaTransactionReprocessLog success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create createEsewaTransactionReprocessLog");
			return false;
		}

		return isinvalidattemptEntryMade;
	}
	
	
	
	
	
	public static String getIdFromTransactionLogTable(DataControllerRequest request, String transactionid) {

		String id = "";
		try {
			String filter = CommonUtils.buildOdataCondition("OriginatingUniqueId", Constants.EQUAL, transactionid);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();
			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders, "HBLMerchantCRUDService",
					"dbxdb_esewaTransactionLog_get", false);
			Dataset customerDataset = result.getDatasetById("esewaTransactionLog");
			if (null != customerDataset) {
				id = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				logger.debug("Else getIdFromTransactionLogTable:");
			}
			logger.debug("getIdFromTransactionLogTable:" + id);
		} catch (Exception e) {
			logger.error("Error while retrieving getIdFromTransactionLogTable for Customer " + e);
		}
		return id;

	}
	
	

}
