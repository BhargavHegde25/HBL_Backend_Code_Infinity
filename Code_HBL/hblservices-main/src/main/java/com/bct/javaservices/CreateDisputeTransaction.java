package com.bct.javaservices;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class CreateDisputeTransaction implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(CreateDisputeTransaction.class);
	private static String referenceNumber;

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String CustomerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
			// String CustomerId = "0385021877";
			LOG.debug("CustomerId ##: " + CustomerId);
			if (!createDisputeTransactionInDB(request, CustomerId)) {
				result.setParam(new Param("ErrMsg", "Create dispute failed!"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			} else {
				result.setParam(new Param("orderId", referenceNumber));
				result.setParam(new Param("message", "Success"));
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			}
		} catch (Exception e) {
			LOG.error("Exception occured in CreateDisputeTransaction:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public static boolean createDisputeTransactionInDB(DataControllerRequest request, String customerId)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();

			String transactionId = request.getParameter("transactionId");
			String disputeReason = request.getParameter("disputeReason");
			String disputeDescription = request.getParameter("disputeDescription");
			String transactionType = request.getParameter("transactionType");
			String transactionsNotes = request.getParameter("transactionsNotes");
			String amount = request.getParameter("amount");
			String description = request.getParameter("description");
			String fromAccountName = request.getParameter("fromAccountName");
			String fromAccountNumber = request.getParameter("fromAccountNumber");
			String toAccountName = request.getParameter("toAccountName");
			String toAccountNumber = request.getParameter("toAccountNumber");
			String transactionDate = request.getParameter("transactionDate");
			String secureMessageId = request.getParameter("secureMessageId");
			String merchantCity = request.getParameter("merchantCity");
			String merchantAddressName = request.getParameter("merchantAddressName");

			inputParams.put("id", id);
			inputParams.put("Customer_id", customerId);
			inputParams.put("amount", amount);
			inputParams.put("fromAccountName", fromAccountName);
			inputParams.put("fromAccountNumber", fromAccountNumber);
			inputParams.put("toAccountName", toAccountName);
			inputParams.put("toAccountNumber", toAccountNumber);
			inputParams.put("transactionDate", transactionDate);
			inputParams.put("transactionId", transactionId);
			inputParams.put("transactionsNotes", transactionsNotes);
			inputParams.put("transactionType", transactionType);
			inputParams.put("disputeReason", disputeReason);
			inputParams.put("disputeStatus", "Disputed");
			//inputParams.put("remarks", disputeDescription);
            inputParams.put("remarks", "");
			inputParams.put("serviceReqId", transactionId);
			inputParams.put("description", disputeDescription);
			inputParams.put("serviceReqProcessedTime", getTimestamp());
			inputParams.put("serviceReqStatus", "Request Failed");
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("lastsynctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			LOG.debug("BCT::createDisputeTransactionInDB: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.DISPUTE_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			LOG.debug("BCT::createDisputeTransactionInDB: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
			} else {
				isinvalidattemptEntryMade = true;
				referenceNumber = id;
				// Trigger email to customer and Bank
				disputeEmailToCustomer(request, null, id);
				disputeEmailToBank(request, null, id);
			}
		} catch (Exception e) {
			LOG.debug("Couldn't create createDisputeTransactionInDB");
			return false;
		}

		return isinvalidattemptEntryMade;
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

	private static void disputeEmailToBank(DataControllerRequest request, JSONObject customerInfo, String disputeId)
			throws HttpCallException, AppRegistryException {

		Map<String, String> input = new HashMap<>();
		ServicesManager sm = request.getServicesManager();
		input.put("Subscribe", "true");
		String requestedDate = formatedDate(getTimestamp());
		String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request, "FirstName");
		String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request, "LastName");
		input.put("EmailType", "disputeEmailToBank");
		String toAcc = "-";

		if (StringUtils.isNotBlank(request.getParameter("toAccountNumber")))
			toAcc = request.getParameter("toAccountNumber");
		else if (StringUtils.isNotBlank(request.getParameter("toAccountName")))
			toAcc = request.getParameter("toAccountName");
		toAcc = HBLCommonUtility.maskAccountNumber(toAcc, 0, toAcc.length() - 4, "X");
		
		String frmAcc = request.getParameter("fromAccountNumber");
		frmAcc = HBLCommonUtility.maskAccountNumber(frmAcc, 0, frmAcc.length() - 4, "X");

		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String email = paramHelper.getServerProperty("DISPUTE_BANK_DL");

		JSONObject addContext = new JSONObject();
		addContext.put("userName", FirstName + " " + LastName);
		addContext.put("id", disputeId);
		addContext.put("customerName", FirstName + " " + LastName);
		addContext.put("reason", request.getParameter("disputeReason"));
		addContext.put("disputeDescription", request.getParameter("disputeDescription"));
		addContext.put("date", request.getParameter("transactionDate"));
		addContext.put("amount", request.getParameter("amount"));
		addContext.put("referenceNumber", request.getParameter("transactionId"));
		addContext.put("transactionType", request.getParameter("transactionType"));
		addContext.put("fromAccountr", frmAcc);
		addContext.put("toAccount", toAcc);
		addContext.put("notes", request.getParameter("transactionsNotes"));
		addContext.put("requestedDate", requestedDate);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

	}

	private static void disputeEmailToCustomer(DataControllerRequest request, JSONObject customerInfo, String disputeId)
			throws HttpCallException {

		String CustomerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		//String email = getCustomerEmail(request, CustomerId);
		String email = Utils.customerEmailFromSession(request);
		String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request, "FirstName");
		String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request, "LastName");
		String requestedDate = formatedDate(getTimestamp());
		LOG.debug("triggerEmail value:" + email);
		String transactionType = request.getParameter("transactionType");
		
		String currentDate = getCurrentTimeStamp();
		LOG.debug("getCurrentTimeStamp:" + currentDate);
		
		String toAcc = "NA";
		String frmAcc = request.getParameter("fromAccountNumber");

		if (StringUtils.isNotBlank(request.getParameter("toAccountNumber")))
			toAcc = request.getParameter("toAccountNumber");
		else if (StringUtils.isNotBlank(request.getParameter("toAccountName")))
			toAcc = request.getParameter("toAccountName");
		
		toAcc = HBLCommonUtility.maskAccountNumber(toAcc, 0, toAcc.length() - 4, "X");
		frmAcc = HBLCommonUtility.maskAccountNumber(frmAcc, 0, frmAcc.length() - 4, "X");
		Map<String, String> input = new HashMap<>();
		input.put("Subscribe", "true");

		if (transactionType.equalsIgnoreCase("Cards"))
			input.put("EmailType", "disputeCardEmailToCustomer");
		else
			input.put("EmailType", "disputeEmailToCustomer");

		JSONObject addContext = new JSONObject();
		addContext.put("userName", FirstName + " " + LastName);
		addContext.put("id", disputeId);
		addContext.put("customerName", FirstName + " " + LastName);
		addContext.put("reason", request.getParameter("disputeReason"));
		addContext.put("disputeDescription", request.getParameter("disputeDescription"));
		addContext.put("date", request.getParameter("transactionDate"));
		addContext.put("amount", request.getParameter("amount"));
		addContext.put("referenceNumber", request.getParameter("transactionId"));
		addContext.put("transactionType", request.getParameter("transactionType"));
		addContext.put("fromAccountr", frmAcc);
		addContext.put("toAccount", toAcc);
		addContext.put("notes", request.getParameter("transactionsNotes"));
		addContext.put("requestedDate", requestedDate);
		
		
		if (transactionType.equalsIgnoreCase("Cards"))
			addContext.put("transactionDate", currentDate);

		input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
		input.put("Email", email);
		Map<String, String> headers = HelperMethods.getHeaders(request);
		headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

	}
	
	public static String getCurrentTimeStamp() {
		return getFormattedTimeStamp(new Date(), null);
	}
	
	public static String formatedDate(String inputDateString) {
		
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(inputDateString, inputFormatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String outputDateString = dateTime.format(outputFormatter);

        LOG.debug("Original Date: " + inputDateString);
        LOG.debug("Formatted Date: " + outputDateString);
		return outputDateString;
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

}
