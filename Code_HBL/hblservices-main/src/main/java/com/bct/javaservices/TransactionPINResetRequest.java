package com.bct.javaservices;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.eSewa.eSewaLimitCheck;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
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
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class TransactionPINResetRequest implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(TransactionPINResetRequest.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			LOG.debug("HBL:ResetTransactionPINResetRequest :");
			String UserName = request.getParameter("username");

			boolean pin = getPINOfCustomer(request, UserName);
			LOG.debug("HBL:pin boolean value :"+ pin);
			if (pin) {
				LOG.debug("HBL:pin If condition :");
				result.setParam(new Param("pinStatus", Boolean.toString(pin)));
				if (!getCustomerReqStatus(request)) {
					String requestid = createRestPINRequest(request);
					if (StringUtils.isNotBlank(requestid))
						result.setParam(new Param("referenceId", requestid));
					// push reset transaction pin request into infinity table
				} else {
					result.setParam(new Param("isReqExists", "true"));
				}
			} else {
				LOG.debug("HBL:pin Else condition :"+ pin);
				result.setParam(new Param("pinStatus", Boolean.toString(pin)));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in TransactionPINResetRequest:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public static String createRestPINRequest(DataControllerRequest request) throws HttpCallException {
		String requestId = "";
		try {
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request, "FirstName");
			String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request, "LastName");
			String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");

			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			String channel = "";
			String plat = "";
			String os = "";

			JSONObject contactinfo = Utils.getContactDetails(request, customerId);
			String email = contactinfo.getString("email");
			LOG.debug("email ##" + email);
			String phone = contactinfo.getString("phone");
			LOG.debug("phone ##" + phone);

			String reportingParams = request.getHeader("X-Kony-ReportingParams");
			if (StringUtils.isNotBlank(reportingParams)) {
				JSONObject reportingParamsJson = null;
				reportingParamsJson = new JSONObject(URLDecoder.decode(reportingParams, StandardCharsets.UTF_8.name()));
				if (null != reportingParamsJson) {
					//channel = reportingParamsJson.optString("chnl");
					 channel = eSewaLimitCheck.getCurrentChannel(request);
					 LOG.debug("channel ##"+ channel);
					plat = reportingParamsJson.optString("plat");
					os = reportingParamsJson.optString("os");
				}
			}
			LOG.debug("channel: ##" + channel);
			LOG.debug("plat: ##" + plat);
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			//String coreIdentifier = "279714";

			inputParams.put("id", id);
			inputParams.put("Customer_id", customerId);
			inputParams.put("coreIdentifier", coreIdentifier);
			inputParams.put("customerName", FirstName + " " + LastName);
			inputParams.put("channelName", channel);
			inputParams.put("browser", plat);
			inputParams.put("os", os);
			inputParams.put("status", "PENDING");//As it is requested, then value will be always PENDING
			inputParams.put("requestDate", getTimestamp());
			inputParams.put("serviceReqProcessedTime", getTimestamp());
			inputParams.put("serviceReqStatus", "Pending");
			if (StringUtils.isNotBlank(email))
				inputParams.put("email", email);
			else
				inputParams.put("email", "");
			if (StringUtils.isNotBlank(phone))
				inputParams.put("phone", phone);
			else
				inputParams.put("phone", "");
			
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("lastsynctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			LOG.debug("BCT::createRestPINRequest: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId("dbxdb_transactionpinResetReq_create").withRequestParameters(inputParams)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(request.getHeaderMap())
					.build().getResponse();
			LOG.debug("BCT::createRestPINRequest: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (!responseJSON.has("errmsg")) {
				requestId = id;
				sendEmailToBank(request, requestId, coreIdentifier, channel, plat, os, getTimestamp());
			}
		} catch (Exception e) {
			LOG.debug("Couldn't create createRestPINRequest");
		}

		return requestId;
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

	private boolean getPINOfCustomer(DataControllerRequest request, String UserName) {
		boolean pinStatus = false;
		try {
			String pin = "";
			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);

			if (null != customerDataset) {
				pin = customerDataset.getRecord(0).getParamValueByName("Pin");
				if (StringUtils.isNotBlank(pin))
					pinStatus = true;
				else
					pinStatus = false;
			}
			LOG.debug("getPINOfCustomer customerInfo:" + pinStatus);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerIDFromUsername for Customer " + UserName);
		}
		return pinStatus;
	}

	public static void sendEmailToBank(DataControllerRequest request, String requestId, String coreIdentifier,
			String channel, String browser, String osversion, String reqDate) throws HttpCallException {
		try {
			String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request, "FirstName");
			String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request, "LastName");
			String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");

			//String FirstName = "Dev";
			//String LastName = "Agrawal";
			//String customerId = "2062635101";

			LOG.debug("customerId :###" + customerId);
			//String email = getCustomerEmail(request, customerId);
			String Toemail = EnvironmentConfigurationsHandler.getServerProperty("HBL_TRANSACTIONPIN_TO_EMAIL");
			String CCemail = EnvironmentConfigurationsHandler.getServerProperty("HBL_TRANSACTIONPIN_CC_EMAIL");
			LOG.debug("Toemail :###" + Toemail);
			LOG.debug("CCemail :###" + CCemail);

			String emailTemplate = "emailTempleteResetPINReq";

			LOG.debug("triggerEmail value:" + Toemail);
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("EmailType", emailTemplate);
			JSONObject addContext = new JSONObject();
			addContext.put("customerid", coreIdentifier);
			addContext.put("requestId", requestId);
			addContext.put("customername", FirstName + " " + LastName);
			addContext.put("channel", channel);
			addContext.put("browser", browser);
			addContext.put("osversion", osversion);
			addContext.put("reqDate", reqDate);

			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", Toemail);
			Map<String, String> headers = HelperMethods.getHeaders(request);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean getCustomerReqStatus(DataControllerRequest request) {
		//select * from dbxdb.transactionpinResetReq where Customer_id = "4981730766" and status = "PENDING";
		boolean status = false;
		String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
		LOG.debug("customerID **:"+ customerId);
		JSONArray records = new JSONArray();
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(DBPUtilitiesConstants.FILTER, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId
				+ DBPUtilitiesConstants.AND + "status" + DBPUtilitiesConstants.EQUAL + "PENDING");

		try {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPRBLOCALSERVICEDB)
					.withObjectId(null).withOperationId("dbxdb_transactionpinResetReq_get")
					.withRequestParameters(inputParams).build().getResponse();
			
			LOG.debug("transactionpinResetReq response ##:" + response);
			JSONObject responseJSON = new JSONObject(response);
			records = responseJSON.getJSONArray("transactionpinResetReq");

			LOG.debug("transactionpinResetReq response ##" + records);
			
			if(records.length() > 0)
				status = true;
			else
				status = false;
			LOG.debug("status ##:::" + status);

		} catch (Exception e) {
			LOG.error("Exception caught while getCustomerReqStatus", e);
		}
		return status;
	}
}
