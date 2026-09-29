package com.bct.postprocessor;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.CardConstants;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

public class createFDSTPPostProcessor extends BasePostProcessor {
	static Logger logger = LogManager.getLogger(createFDSTPPostProcessor.class);

	@SuppressWarnings("deprecation")
	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		try {
			logger.debug("createFDSTPPostProcessor Result:###" + ResultToJSON.convert(result));
			String productId = request.getParameter("productId");
			logger.debug("BCT::productId:" + productId);
			String httpResponseCode = result.getHttpStatusCodeParamValue();
			String status = result.getParamValueByName("status");
			logger.debug("BCT::createFDSTPPostProcessor::httpResponseCode:" + httpResponseCode);
			logger.debug("BCT::createFDSTPPostProcessor::success:" + status);
			if (httpResponseCode.equalsIgnoreCase("200") && status.equalsIgnoreCase("success")) {
				sendEmailToCustomer(request, result);

				if (StringUtils.contains(productId, "STRUCTURED")) {
					/** Inserting creation data into structured deposit table ***/
					createFDCreationInDB(request, result, HBLURLConstants.STRUCTURED_FD_CREATE);
				} else if (StringUtils.contains(productId, "HIMAL")) {
					/** Inserting creation data into HIMAL REMITT deposit table ***/
					createFDCreationInDB(request, result, HBLURLConstants.HIMAL_REMIT_FD_CREATE);
				} else {
					/** Inserting creation data into normal deposit table ***/
					createFDCreationInDB(request, result, HBLURLConstants.NORMAL_FD_CREATE);
				}
				String fromAccountNumber = request.getParameter("fromAccount");
				String legalEntityId = CommonUtils.getUserAttributeFromIdentity(request, Constants.COMPANY_ID);
				logger.debug("BCT::createFDSTPPostProcessor::success:" + status);
				JSONObject accountBalanceArray = getAvailableBalanceByAccountId(fromAccountNumber, legalEntityId);
				String availableBalance = accountBalanceArray.get("availableBalance") != null
						? accountBalanceArray.getString("availableBalance")
						: "";
				String currencyCode = accountBalanceArray.get("currencyCode") != null
						? accountBalanceArray.getString("currencyCode")
						: "";
				if (StringUtils.isNotBlank(availableBalance)) {
					result.addParam(new Param("availableBalance", availableBalance));
					result.addParam(new Param("currencyCode", currencyCode));
				}
			}else {
				result.addParam("errcode", result.getParamValueByName("errcode"));
				result.addParam("errmsg", result.getParamValueByName("errmessage"));
				
				if (StringUtils.contains(productId, "STRUCTURED")) {
					/** Inserting creation data into structured deposit table ***/
					createFDCreationInDB(request, result, HBLURLConstants.STRUCTURED_FD_CREATE);
				} else if (StringUtils.contains(productId, "HIMAL")) {
					/** Inserting creation data into HIMAL REMITT deposit table ***/
					createFDCreationInDB(request, result, HBLURLConstants.HIMAL_REMIT_FD_CREATE);
				} else {
					/** Inserting creation data into normal deposit table ***/
					createFDCreationInDB(request, result, HBLURLConstants.NORMAL_FD_CREATE);
				}
			}

		} catch (Exception e) {
			logger.error(e);
			CommonUtils.setErrMsg(result, e.toString());
		}
		return result;
	}

	private JSONObject getAvailableBalanceByAccountId(String AccountId, String companyId) throws ApplicationException {

		HashMap<String, Object> inputParams = new HashMap<>();
		Map<String, Object> headerParams = new HashMap<>();
		String stringResponse = null;
		JSONObject balanceResponse = new JSONObject();
		JSONObject accountDetails = new JSONObject();
		if (StringUtils.isNotBlank(AccountId) && StringUtils.isNotBlank(companyId)) {
			try {
				inputParams.put("Account_id", AccountId);
				headerParams.put("companyId", companyId);
				stringResponse = Executor.invokeService(
						ArrangementsAPIServices.T24IRISARRANGEMENTSERVICES_GETLATESTBALANCES, inputParams,
						headerParams);
				logger.debug("Response from T24 GetLatestBalances Service: " + stringResponse);
				balanceResponse = new JSONObject(stringResponse);
				if (balanceResponse.has("Accounts")) {
					JSONArray accountBalanceArray = balanceResponse.getJSONArray("Accounts");
					if (accountBalanceArray.length() > 0) {
						accountDetails = accountBalanceArray.getJSONObject(0);
					}
				}
			} catch (Exception e) {
				logger.debug("Exception Occurred in getAvailableBalanceByAccountId:" + e.toString());
			}
		}
		return accountDetails;
	}

	public static void sendEmailToCustomer(DataControllerRequest request, Result result) throws HttpCallException {
		try {
			String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request, "FirstName");
			String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request, "LastName");
			String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");

			logger.debug("customerId :###" + customerId);
			// String email = getCustomerEmail(request, customerId);
			String email = Utils.customerEmailFromSession(request);
			logger.debug("email :###" + email);
			// String productName = request.getParameter("productId");
			String productName = result.getParamValueByName("accountType");
			String amount = request.getParameter("amount");
			String intrest = request.getParameter("intrestRate");
			String tenure = request.getParameter("tenure");// To get from request
			// String FDAccountNumber = request.getParameter("fromAccount");

			String requestId = result.getParamValueByName("referenceId");
			String depositAccountNumber = result.getParamValueByName("depositAccountNumber");
			
			logger.debug("depositAccountNumber ##:" + depositAccountNumber);
			depositAccountNumber = HBLCommonUtility.maskAccountNumber(depositAccountNumber, 0, depositAccountNumber.length() - 4, "X");
			String emailTemplate = "emailTempleteFDSTP";

			Date date = new Date();
			String startDate = new SimpleDateFormat("yyyy/MM/dd").format(date);
			logger.debug("requestDate ##:" + startDate);

			Date finalDate = DateUtils.addMonths(new Date(), Integer.parseInt(tenure));
			String maturityDate = new SimpleDateFormat("yyyy/MM/dd").format(finalDate);

			ServicesManager sm;

			sm = request.getServicesManager();

			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String customerCareNumber = paramHelper.getServerProperty("FD_CUSTOMERCARE_NUMBER");
			logger.debug("triggerEmail value:" + email);
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("EmailType", emailTemplate);
			JSONObject addContext = new JSONObject();
			addContext.put("customerName", FirstName + " " + LastName);
			addContext.put("requestId", requestId);
			addContext.put("productName", productName);
			addContext.put("amount", amount);
			addContext.put("intrest", intrest + " % p.a.");
			addContext.put("tenure", tenure + " Months");
			addContext.put("startDate", startDate);
			addContext.put("maturityDate", maturityDate);
			addContext.put("FDAccountNumber", depositAccountNumber);
			addContext.put("FDCustomerCareNumber", customerCareNumber);

			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(request);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

		} catch (AppRegistryException e) {
			e.printStackTrace();
		}
	}

	public static boolean createFDCreationInDB(DataControllerRequest request, Result result, String servicename)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			ServicesManager servicesManager;
			servicesManager = request.getServicesManager();
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName");
			String currency = request.getParameter("currency");
			String productId = request.getParameter("productId");
			String intrestRate = request.getParameter("intrestRate");
			String fromAccount = request.getParameter("fromAccount");
			String amount = request.getParameter("amount");
			String tenure = request.getParameter("tenure");

			String referenceId = result.getParamValueByName("referenceId");
			String arrangementId = result.getParamValueByName("arrangementId");
			String transactionStatus = result.getParamValueByName("transactionStatus");

			String externalServiceResponse = ResultToJSON.convert(result);
			String externalServicePayload = createRequestPayload(request).toString();

			inputParams.put("id", id);
			inputParams.put("Customer_id", customerID);
			inputParams.put("username", userSignOnName);
			inputParams.put("core_identifier", coreIdentifier);
			inputParams.put("currency", currency);
			inputParams.put("productId", productId);
			inputParams.put("intrestRate", intrestRate);
			inputParams.put("fromAccount", fromAccount);
			inputParams.put("amount", amount);
			inputParams.put("tenure", tenure);
			inputParams.put("referenceId", referenceId);
			inputParams.put("arrangementId", arrangementId);
			inputParams.put("transactionStatus", transactionStatus);

			inputParams.put("externalServicePayload", externalServicePayload);
			inputParams.put("externalServiceResponse", externalServiceResponse);
			inputParams.put("createdby", getTimestamp());
			inputParams.put("modifiedby", coreIdentifier);
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::createFDCreationInDB: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(servicename)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::createFDCreationInDB: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::createFDCreationInDB failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::createFDCreationInDB success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create createFDCreationInDB");
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
	
	public static JSONObject createRequestPayload(DataControllerRequest request) {
		JSONObject reqPayload = new JSONObject();
		
		String currency = request.getParameter("currency");
		String productId = request.getParameter("productId");
		String intrestRate = request.getParameter("intrestRate");
		String fromAccount = request.getParameter("fromAccount");
		String amount = request.getParameter("amount");
		String tenure = request.getParameter("tenure");
		
		reqPayload.put("currency", currency);
		reqPayload.put("productId", productId);
		reqPayload.put("intrestRate", intrestRate);
		reqPayload.put("fromAccount", fromAccount);
		reqPayload.put("amount", amount);
		reqPayload.put("tenure", tenure);
		
		logger.debug("Request payload for DB inset: ###" + reqPayload);
		
		return reqPayload;
		
	}

}
