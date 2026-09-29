package com.bct.javaservices;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.CardConstants;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.bct.utilities.Utils;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.common.net.HttpHeaders;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
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

public class RequestNewCardService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(RequestNewCardService.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			// String customerId = "7161419463";
			// String coreIdentifier = "101602";
			Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
			LOG.debug("HBL:RequestNewCardService: processPayment:inputParams:" + inputParams);

			LOG.debug("CustomerId ##: " + customerId);
			LOG.debug("coreIdentifier ##: " + coreIdentifier);

			String referenceNumber = insertCardRecordIntoDB(request, customerId, coreIdentifier);
			if (referenceNumber.equalsIgnoreCase("REQ_EXISTS")) {
				result.setParam(new Param("errmsg", referenceNumber));
				result.addParam("dbpErrMsg", referenceNumber);
			} else if (referenceNumber.equalsIgnoreCase("null")) {
				result.setParam(new Param("ErrMsg", "Request new card failed!"));
				result.setParam(new Param("errmsg", "Request new card failed!"));
			} else {
				result.setParam(new Param("ReferenceNumber", referenceNumber));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in RequestNewCardService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public static String insertCardRecordIntoDB(DataControllerRequest request, String customerId, String coreIdentifier)
			throws HttpCallException {
		String referenceId = "null";
		String totalDebitAmount = "";
		String cardType = "";
		String debitAccount = "";
		String serviceProvider = "";
		String cardDescription = "";
		String dailyWithdrawLimit = "";
		String dailyPurchaseLimit = "";
		String annualFee = "";
		String nameOnTheCard = "";
		String cardCategory = "";
		String panNo = "";
		String topupAmount = "";
		String branchname = "";
		String branchcode = "";
		String branchtoemail = "";
		String branchccemail = "";
		
		
		boolean isCardRequestExists = getCardRequestData(request, customerId, coreIdentifier);
		if(isCardRequestExists) {
			referenceId = "REQ_EXISTS";
			return referenceId;
		}
		
		try {
			String id = UUID.randomUUID().toString();
			Map<String, Object> inputParams = new HashMap<>();

			String currentTime = getTimestamp();
			if (!StringUtils.isBlank(request.getParameter("cardType")))
				cardType = request.getParameter("cardType");
			if (!StringUtils.isBlank(request.getParameter("debitAccount")))
				debitAccount = request.getParameter("debitAccount");
			if (!StringUtils.isBlank(request.getParameter("serviceProvider")))
				serviceProvider = request.getParameter("serviceProvider");
			if (!StringUtils.isBlank(request.getParameter("cardDescription")))
				cardDescription = request.getParameter("cardDescription");
			if (!StringUtils.isBlank(request.getParameter("dailyWithdrawLimit")))
				dailyWithdrawLimit = request.getParameter("dailyWithdrawLimit");
			if (!StringUtils.isBlank(request.getParameter("dailyPurchaseLimit")))
				dailyPurchaseLimit = request.getParameter("dailyPurchaseLimit");
			if (!StringUtils.isBlank(request.getParameter("annualFee")))
				annualFee = request.getParameter("annualFee");
			if (!StringUtils.isBlank(request.getParameter("nameOnTheCard")))
				nameOnTheCard = request.getParameter("nameOnTheCard");
			String referenceNumber = generateReferenceNumber(cardType);
			if (!StringUtils.isBlank(request.getParameter("cardCategory")))
				cardCategory = request.getParameter("cardCategory");
			String cardFee = getPrepaidCardFee(cardType, request);
			if (!StringUtils.isBlank(request.getParameter("panNo")))
				panNo = request.getParameter("panNo");
			if (!StringUtils.isBlank(request.getParameter("topupAmount")))
				topupAmount = request.getParameter("topupAmount");
			
			if (!StringUtils.isBlank(request.getParameter("totalDebitAmount"))) {
				totalDebitAmount = request.getParameter("totalDebitAmount");
			}
			
			if (!StringUtils.isBlank(request.getParameter("branchname"))) {
				branchname = request.getParameter("branchname");
			}
			
			if (!StringUtils.isBlank(request.getParameter("branchcode"))) {
				branchcode = request.getParameter("branchcode");
			}
			
			if (!StringUtils.isBlank(request.getParameter("branchtoemail"))) {
				branchtoemail = request.getParameter("branchtoemail");
			}
			
			if (!StringUtils.isBlank(request.getParameter("branchccemail"))) {
				branchccemail = request.getParameter("branchccemail");
			}
			
			inputParams.put("id", id);
			inputParams.put("customerId", customerId);
			inputParams.put("coreIdentifier", coreIdentifier);
			inputParams.put("cardType", cardType);
			inputParams.put("debitAccount", debitAccount);
			inputParams.put("serviceProvider", serviceProvider);
			inputParams.put("cardDescription", cardDescription);
			inputParams.put("dailyWithdrawLimit", dailyWithdrawLimit);
			inputParams.put("dailyPurchaseLimit", dailyPurchaseLimit);
			inputParams.put("annualFee", annualFee);
			inputParams.put("nameOnTheCard", nameOnTheCard);
			inputParams.put("referenceNumber", referenceNumber);
			inputParams.put("cardCategory", cardCategory);
			inputParams.put("cardFee", cardFee);
			inputParams.put("panNo", panNo);
			inputParams.put("topupAmount", topupAmount);
			inputParams.put("totalDebitAmount", totalDebitAmount);
			inputParams.put("status", "PENDING"); //by default this status is 'PENDING' for new requested card. Bank has to update this status after issued or rejected.
			inputParams.put("branchname", branchname);
			inputParams.put("branchcode", branchcode);
			inputParams.put("branchtoemail", branchtoemail);
			inputParams.put("branchccemail", branchccemail);
			inputParams.put("requestdate", currentTime);
			inputParams.put("createdts", currentTime);
			inputParams.put("lastmodifiedts", currentTime);
			inputParams.put("lastsynctimestamp", currentTime);
			inputParams.put("softdeleteflag", "");

			LOG.debug("BCT::insertCardRecordIntoDB: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.REQUEST_NEW_CARD_CREATE).withRequestParameters(inputParams)
					.withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE).withRequestHeaders(request.getHeaderMap())
					.build().getResponse();
			LOG.debug("BCT::insertCardRecordIntoDB: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				referenceId = "null";
			} else {
				referenceId = referenceNumber;
				// Trigger email to customer and Bank
				//String email = getCustomerEmail(request, customerId);
				String email = Utils.customerEmailFromSession(request);
				requestCardEmailToCustomer(request, cardType, referenceNumber, debitAccount, nameOnTheCard, currentTime,
						email ,cardFee,topupAmount,totalDebitAmount,branchname);
				requestCardEmailToBank(request, cardType, coreIdentifier, referenceNumber, debitAccount, nameOnTheCard,
						currentTime, email,branchtoemail,branchccemail,cardFee,topupAmount,totalDebitAmount,branchname);
				
				requestCardEmailToBankMainBranch(request, cardType, coreIdentifier, referenceNumber, debitAccount, nameOnTheCard,
						currentTime, email,branchtoemail,branchccemail,cardFee,topupAmount,totalDebitAmount,branchname);
			}
		} catch (Exception e) {
			LOG.debug("Couldn't create insertCardRecordIntoDB");
			return referenceId;
		}

		return referenceId;
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

	public static String getPrepaidCardFee(String cardType, DataControllerRequest request) {
		String cardFee = "";
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			if (cardType.equalsIgnoreCase("physicalPrepaidCard")) {
				cardFee = paramHelper.getServerProperty("PHYSICAL_PREPAID_CARD_FEE");
			} else if (cardType.equalsIgnoreCase("virtualPrepaidCard")) {
				cardFee = paramHelper.getServerProperty("VIRTUAL_PREPAID_CARD_FEE");
			}
		} catch (AppRegistryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cardFee;
	}
	
	public static String getCardType(String cardType) {
		String cardCategory = "";
		try {
			if (cardType.equalsIgnoreCase("physicalPrepaidCard")) {
				cardCategory = "Prepaid Card";
			} else if (cardType.equalsIgnoreCase("virtualPrepaidCard")) {
				cardCategory = "Dollar Card";
			}else if (cardType.equalsIgnoreCase("debitcard")) {
				cardCategory = "Debit Card";
			}else {
				cardCategory = "Credit Card";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cardCategory;
	}
	

	public static Double getTotalDebitAmount(String fee, String amount) {
		double val1 = Double.parseDouble(fee);
		double val2 = Double.parseDouble(amount);

		return Double.sum(val1, val2);
	}

	public static String generateReferenceNumber(String cardType) {
		String randomNumber = HBLCommonUtility.getRandomNumberString();
		if (cardType.equalsIgnoreCase("debitcard")) {
			randomNumber = "HBLDC" + randomNumber;
		} else if (cardType.equalsIgnoreCase("physicalPrepaidCard")) {
			randomNumber = "HBLPPC" + randomNumber;
		} else if (cardType.equalsIgnoreCase("virtualPrepaidCard")) {
			randomNumber = "HBLVPC" + randomNumber;
		}
		return randomNumber;
	}

	private static void requestCardEmailToCustomer(DataControllerRequest request, String cardType,
			String referenceNumber, String debitAccount, String nameOnTheCard, String requestdate, String email,
			String cardFee, String topupAmount, String totalDebitAmount, String branchname)
			throws HttpCallException {
		try {
			 String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request,
			 "FirstName");
			 String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request,
			 "LastName");
			 String cardName = "";
				if (!StringUtils.isBlank(request.getParameter("serviceProvider")))
					cardName = request.getParameter("serviceProvider");
			 String emailTemplate = " ";
			 
				if (!StringUtils.isBlank(nameOnTheCard))
					emailTemplate = "requestCardEmailToCustomer";
				else
					emailTemplate = "requestdollarCardEmailToCustomer";
				
				if(getCardType(cardType).equalsIgnoreCase("Prepaid Card"))
					emailTemplate = "requestPrepaidCardEmailToCustomer";
			ServicesManager sm;

			sm = request.getServicesManager();

			debitAccount = HBLCommonUtility.maskAccountNumber(debitAccount, 0, debitAccount.length() - 4, "X");
			
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String estimatedTime = paramHelper.getServerProperty("CARD_ESTIMATED_TIME");
			String customerCareNumber = paramHelper.getServerProperty("CARD_CUSTOMERCARE_NUMBER");
			String cardTeamName = paramHelper.getServerProperty("CARD_TEAM_NAME");
			LOG.debug("triggerEmail value:" + email);
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("EmailType", emailTemplate);
			JSONObject addContext = new JSONObject();
			addContext.put("cardType", getCardType(cardType));
			addContext.put("customerName", FirstName +" "+ LastName);
			addContext.put("referenceNumber", referenceNumber);
			addContext.put("debitAccount", debitAccount);
			addContext.put("nameOnTheCard", nameOnTheCard);
			addContext.put("requestdate", formatedDate(requestdate));
			addContext.put("cardTeamName", cardTeamName);
			addContext.put("cardName", cardName);
			addContext.put("branchname", branchname);
			addContext.put("estimatedTime", estimatedTime); // Configured in the server properties with key
															// CARD_ESTIMATED_TIME
			addContext.put("customerCareNumber", customerCareNumber); //// Configured in the server properties with key
																		//// CARD_CUSTOMERCARE_NUMBER
			
			if(emailTemplate.equalsIgnoreCase("requestdollarCardEmailToCustomer")) {
				addContext.put("cardFee", "USD "+cardFee);
				addContext.put("topupAmount", "USD "+topupAmount);
				addContext.put("totalDebitAmount", "NPR "+totalDebitAmount);
			}

			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(request);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

		} catch (AppRegistryException e) {
			e.printStackTrace();
		}
	}

	private static void requestCardEmailToBank(DataControllerRequest request, String cardType, String coreIdentifier,
			String referenceNumber, String debitAccount, String nameOnTheCard, String requestdate, String email
			, String TOemail,String CCemail,String cardFee, String topupAmount, String totalDebitAmount, String branchname)
			throws HttpCallException {
		try {
			String cardName = "";
			if (!StringUtils.isBlank(request.getParameter("serviceProvider")))
				cardName = request.getParameter("serviceProvider");
			String emailTemplate = " ";
			debitAccount = HBLCommonUtility.maskAccountNumber(debitAccount, 0, debitAccount.length() - 4, "X");
			
			String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request,
					 "FirstName");
		    String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request,
					 "LastName");
			 
			if (!StringUtils.isBlank(nameOnTheCard))
				emailTemplate = "requestCardEmailToBank";
			else
				emailTemplate = "requestdollarCardEmailToBank";
			
			if(getCardType(cardType).equalsIgnoreCase("Prepaid Card"))
				emailTemplate = "requestPrepaidCardEmailToBank";
			
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String hostname = paramHelper.getServerProperty("ENGAGE_URL");
			
			LOG.debug("triggerEmail value:" + email);
			String cardTeamName = paramHelper.getServerProperty("CARD_TEAM_NAME");
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("EmailType", emailTemplate);
			JSONObject addContext = new JSONObject();
			addContext.put("cardType", getCardType(cardType));
			addContext.put("coreIdentifier", FirstName +" " +LastName);
			addContext.put("referenceNumber", referenceNumber);
			addContext.put("debitAccount", debitAccount);
			addContext.put("nameOnTheCard", nameOnTheCard);
			addContext.put("requestdate", formatedDate(requestdate));
			addContext.put("cardTeamName", cardTeamName);
			addContext.put("cardName", cardName);
			addContext.put("branchname", branchname);
			if(emailTemplate.equalsIgnoreCase("requestdollarCardEmailToBank")) {
				addContext.put("cardFee", "USD "+cardFee);
				addContext.put("topupAmount", "USD "+topupAmount);
				addContext.put("totalDebitAmount", "NPR "+totalDebitAmount);
			}

			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			/*** enable this code for production, as email to be trigger for actual branch email ****/
		
			if (hostname.contains("himbplus")) {
				if (!StringUtils.isBlank(TOemail)) {
					input.put("Email", TOemail);
				} else {
					input.put("Email", paramHelper.getServerProperty("HBL_CARDS_TEAM_DL"));
				}
			} else {
				input.put("Email", paramHelper.getServerProperty("HBL_CARDS_TEAM_DL"));
			}
			
			Map<String, String> headers = HelperMethods.getHeaders(request);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void requestCardEmailToBankMainBranch(DataControllerRequest request, String cardType, String coreIdentifier,
			String referenceNumber, String debitAccount, String nameOnTheCard, String requestdate, String email
			, String TOemail,String CCemail,String cardFee, String topupAmount, String totalDebitAmount, String branchname)
			throws HttpCallException {
		try {
			String cardName = "";
			if (!StringUtils.isBlank(request.getParameter("serviceProvider")))
				cardName = request.getParameter("serviceProvider");
			String emailTemplate = " ";
			debitAccount = HBLCommonUtility.maskAccountNumber(debitAccount, 0, debitAccount.length() - 4, "X");
			
			String FirstName = ArrangementsUtils.getUserAttributeFromIdentity(request,
					 "FirstName");
		    String LastName = ArrangementsUtils.getUserAttributeFromIdentity(request,
					 "LastName");
			 
			if (!StringUtils.isBlank(nameOnTheCard))
				emailTemplate = "requestCardEmailToBank";
			else
				emailTemplate = "requestdollarCardEmailToBank";
			
			if(getCardType(cardType).equalsIgnoreCase("Prepaid Card"))
				emailTemplate = "requestPrepaidCardEmailToBank";
			
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
			String hostname = paramHelper.getServerProperty("ENGAGE_URL");
			
			LOG.debug("triggerEmail value:" + email);
			String cardTeamName = paramHelper.getServerProperty("CARD_TEAM_NAME");
			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("EmailType", emailTemplate);
			JSONObject addContext = new JSONObject();
			addContext.put("cardType", getCardType(cardType));
			addContext.put("coreIdentifier", FirstName +" " +LastName);
			addContext.put("referenceNumber", referenceNumber);
			addContext.put("debitAccount", debitAccount);
			addContext.put("nameOnTheCard", nameOnTheCard);
			addContext.put("requestdate", formatedDate(requestdate));
			addContext.put("cardTeamName", cardTeamName);
			addContext.put("cardName", cardName);
			addContext.put("branchname", branchname);
			if(emailTemplate.equalsIgnoreCase("requestdollarCardEmailToBank")) {
				addContext.put("cardFee", "USD "+cardFee);
				addContext.put("topupAmount", "USD "+topupAmount);
				addContext.put("totalDebitAmount", "NPR "+totalDebitAmount);
			}

			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			/*** enable this code for production, as email to be trigger for actual branch email ****/
			input.put("Email", paramHelper.getServerProperty("HBL_CARDS_TEAM_DL"));
			Map<String, String> headers = HelperMethods.getHeaders(request);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApi(request, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean getCardRequestData(DataControllerRequest request, String customerId, String coreIdentifier) {

		boolean IsCardAlreadyRequested = false;
		try {
			LOG.debug("customerId  getCardRequestData##:" + customerId);
			LOG.debug("coreIdentifier  getCardRequestData##:" + coreIdentifier);
			String cardType = request.getParameter("cardType");
			String debitAccount = request.getParameter("debitAccount");
			String serviceProvider = request.getParameter("serviceProvider");
			
			String filter =	"customerId" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + 
					"coreIdentifier" + DBPUtilitiesConstants.EQUAL + coreIdentifier + DBPUtilitiesConstants.AND + 
					"cardType" + DBPUtilitiesConstants.EQUAL + cardType + DBPUtilitiesConstants.AND + 
					"debitAccount" + DBPUtilitiesConstants.EQUAL + debitAccount + DBPUtilitiesConstants.AND + 
					"serviceProvider" + DBPUtilitiesConstants.EQUAL + serviceProvider + DBPUtilitiesConstants.AND + 
					"status" + DBPUtilitiesConstants.EQUAL + "PENDING" ;
			LOG.debug("filter##:" + filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, HBLURLConstants.REQUEST_NEW_CARD_GET, false);
			Dataset cardrequest = result.getDatasetById(HBLURLConstants.REQUEST_NEW_CARD_DATASET);
			if (null != cardrequest && cardrequest.getAllRecords().size()>0) {

				IsCardAlreadyRequested = true;
			} else {
				LOG.debug("Else IsCardAlreadyRequested:");
				IsCardAlreadyRequested = false;
			}

			LOG.debug("IsCardAlreadyRequested:" + IsCardAlreadyRequested);

		} catch (Exception e) {

			LOG.error("Error while retrieving getCardRequestData for Customer ");
		}
		return IsCardAlreadyRequested;

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
	
	static String maskCard(String input) {
	    return input.replaceAll(".(?=.{4})", "X");
	}


}
