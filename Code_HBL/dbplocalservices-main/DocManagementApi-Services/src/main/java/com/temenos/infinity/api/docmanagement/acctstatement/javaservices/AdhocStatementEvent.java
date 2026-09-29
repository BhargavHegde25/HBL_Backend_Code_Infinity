package com.temenos.infinity.api.docmanagement.acctstatement.javaservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.fileutil.FileGenerator;
import com.kony.dbputilities.fileutil.FileGeneratorFactory;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.TransactionsCountProperties;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.api.events.EventSubscriber;
import com.konylabs.middleware.api.events.IntegrationEventSubscriber;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.infinity.api.docmanagement.javaservices.GenerateTransactionsDetails;

@IntegrationEventSubscriber(topics = { "/events/adhocstatement" })
public class AdhocStatementEvent implements EventSubscriber {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("deprecation")
	@Override
	public void onEvent(EventData eventData) {
		//alert.prepareError("********************* Adhoc statement event Triggered *******************").log();
		String data = eventData.getData().toString();
		JSONObject dataObject;
		String fileId = "";
		String authToken = null;
		String deviceId = null;
		
		try {
			dataObject = new JSONObject(data);
			JSONObject eventObject = dataObject.getJSONObject("events");
			JSONObject eventDataObject = eventObject.getJSONObject("eventData");
			//alert.prepareError("**************** AdhocStatementEvent eventDataObject :"+eventDataObject).log();
			
			String accountID = eventDataObject.has("accountID") ? eventDataObject.get("accountID").toString() : null;

			if (StringUtils.isBlank(accountID)) {
				return;
			}
			String accountName = eventDataObject.has("accountName") ? eventDataObject.get("accountName").toString() : "";
			String transactionType = eventDataObject.has("transactionType") ? eventDataObject.get("transactionType").toString() : null;
			//String offset = eventDataObject.has("offset") ? eventDataObject.get("offset").toString() : null;
			//int limit = eventDataObject.has("limit") ? Integer.parseInt(eventDataObject.get("limit").toString()) : 999;
			
			int limit = Integer.parseInt(DBPUtilitiesConstants.ADHOC_DOWNLOAD_PAGE_SIZE);
		
			String isScheduled = eventDataObject.has("isScheduled") ? eventDataObject.get("isScheduled").toString() : null;
			String order = eventDataObject.has("order") ? eventDataObject.get("order").toString() : null;
			String requestType = eventDataObject.has("requestType") ? eventDataObject.get("requestType").toString()	: "search";
			String searchStartDate = eventDataObject.has("searchStartDate") ? eventDataObject.get("searchStartDate").toString() : null;
			String searchEndDate = eventDataObject.has("searchEndDate") ? eventDataObject.get("searchEndDate").toString() : "";
			String description = eventDataObject.has(DBPUtilitiesConstants.SEARCH_DESCRIPTION) ? eventDataObject.get(DBPUtilitiesConstants.SEARCH_DESCRIPTION).toString() : null;
			String searchMinAmount = eventDataObject.has("searchMinAmount") ? eventDataObject.get("searchMinAmount").toString() : null;
			String searchMaxAmount = eventDataObject.has("searchMaxAmount") ? eventDataObject.get("searchMaxAmount").toString() : null;
			String fromCheckNumber = eventDataObject.has("fromCheckNumber") ? eventDataObject.get("fromCheckNumber").toString() : null;
			String toCheckNumber = eventDataObject.has("toCheckNumber") ? eventDataObject.get("toCheckNumber").toString() : null;
			String searchDateRange = eventDataObject.has("searchDateRange") ? eventDataObject.get("searchDateRange").toString() : null;
			String searchTransactionType = eventDataObject.has("searchTransactionType") ? eventDataObject.get("searchTransactionType").toString() : null;
			
			String userId = eventDataObject.has("userId") ? eventDataObject.get("userId").toString() : null;
			String fileType = eventDataObject.has("fileType") ? eventDataObject.get("fileType").toString() : "pdf";		
			fileId = eventDataObject.has("id") ? eventDataObject.get("id").toString() : null;
			
			
			String fromDate = eventDataObject.has("searchStartDate") ? eventDataObject.get("searchStartDate").toString() : null;
			String toDate = eventDataObject.has("searchEndDate") ? eventDataObject.get("searchEndDate").toString() : null;
			String currencyCode = eventDataObject.has("currencyCode") ? eventDataObject.get("currencyCode").toString() : null;
			String paymentDateFormat = eventDataObject.has("dateFormat") ? eventDataObject.get("dateFormat").toString() : null;
			String generatedBy = eventDataObject.has("generatedBy") ? eventDataObject.get("generatedBy").toString() : null;
			
			
			authToken = eventDataObject.has(DBPUtilitiesConstants.X_KONY_AUTHORIZATION)
					? eventDataObject.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION).toString()
					: null;
			deviceId = eventDataObject.has(DBPUtilitiesConstants.X_KONY_DEVICEID)
					? eventDataObject.get(DBPUtilitiesConstants.X_KONY_DEVICEID).toString()
					: null;

			JsonArray transactionsArray = new JsonArray();

			
			HashMap<String, Object> headers = new HashMap<>();
			headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
			headers.put(DBPUtilitiesConstants.X_KONY_DEVICEID, deviceId);
			
		//	alert.prepareError("****************** AdhocStatementEvent headers :"+headers).log();
			
			
			HashMap<String, Object> failureUpdateParam = new HashMap<String, Object>();
			failureUpdateParam.put("id", fileId);
			failureUpdateParam.put("status", Constants.STATUS_FAIL);
			
			int totalSize = 0;
			int offset = 1;
			if (eventDataObject.has("fileType") && eventDataObject.get("fileType")==null)eventDataObject.put("fileType", "pdf");
			Map<String, Object> userData1 = getOtherRequiredData(eventDataObject, headers, authToken);

		//	alert.prepareError("userData ::"+userData1).log();

			HashMap<String, Object> transactionsParam = new HashMap<String, Object>();
		//	alert.prepareError("****************** AdhocStatementEvent limit :"+limit).log();
			do {
				// fetch Transactions
				JsonObject individualAccountTransaction = new JsonObject();
				
			
				transactionsParam.put("accountID",accountID);
				transactionsParam.put("transactionType",transactionType);
				transactionsParam.put("offset",offset);
				transactionsParam.put("limit",limit);
				transactionsParam.put("isScheduled",isScheduled);
				transactionsParam.put("order",order);
				transactionsParam.put("requestType","adhoc");
				transactionsParam.put("searchStartDate",searchStartDate);
				transactionsParam.put("searchEndDate",searchEndDate);
				
				transactionsParam.put("searchDateRange",searchDateRange);
				transactionsParam.put("searchTransactionType",searchTransactionType);
				
				totalSize = 0;
				
				if(null != description && !"".equals(description.trim()))
					transactionsParam.put(DBPUtilitiesConstants.SEARCH_DESCRIPTION,description);
				if(null != searchMinAmount && !"".equals(searchMinAmount.trim()))
					transactionsParam.put("searchMinAmount",searchMinAmount);
				if(null != searchMaxAmount && !"".equals(searchMaxAmount.trim()))
					transactionsParam.put("searchMaxAmount",searchMaxAmount);
				if(null != fromCheckNumber && !"".equals(fromCheckNumber.trim()))
					transactionsParam.put("fromCheckNumber",fromCheckNumber);
				if(null != toCheckNumber && !"".equals(toCheckNumber.trim()))
					transactionsParam.put("toCheckNumber",toCheckNumber);
				transactionsParam.put("searchTransactionType", "All");
	//			alert.prepareError("******************* AdhocStatementEvent transactionsParam :"+transactionsParam).log();
//				Map<String, Object> headers = new HashMap<>();
//				headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
				
				
				
				JsonObject transactionObject = ServiceCallHelper.invokeServiceAndGetJson(transactionsParam, headers, URLConstants.TRANSACTIONS_POST, authToken);
				//alert.prepareError("******************* AdhocStatementEvent transactionObject :"+transactionObject).log();

				if (transactionObject.has(Constants.OPSTATUS) && transactionObject.get(Constants.OPSTATUS) != null
						&& !transactionObject.get(Constants.OPSTATUS).toString().equalsIgnoreCase("0")) {
					failureUpdateParam.put("failureMessage", "Error while fetching transaction for accounId" + accountID);
					updateAccountStatement(failureUpdateParam, headers);
					return;
				}
				if (transactionObject.has(ErrorCodeEnum.ERROR_CODE_KEY)
						&& transactionObject.get(ErrorCodeEnum.ERROR_CODE_KEY) != null
						&& StringUtils.isNotBlank(transactionObject.get(ErrorCodeEnum.ERROR_CODE_KEY).toString())) {
					failureUpdateParam.put("failureMessage", "Error while fetching transaction for accounId" + accountID);
					updateAccountStatement(failureUpdateParam, headers);
					return;
				}

				// generate object
				if (transactionObject.has("Transactions")) {
					transactionsArray.addAll(transactionObject.get("Transactions").getAsJsonArray());
					totalSize = transactionObject.get("Transactions").getAsJsonArray().size();
				//	alert.prepareError("******************* AdhocStatementEvent transactions array size :"+totalSize).log();
				}
			//	alert.prepareError("********************* AdhocStatementEvent totalSize :"+totalSize).log();			
				offset +=1;				
			}while(totalSize == limit);
			
		//	alert.prepareError("******************** AdhocStatementEvent transactionsArray :"+transactionsArray.size()).log();
			
			FileGenerator generator = FileGeneratorFactory.getFileGenerator(fileType);
			
			HashMap<String, String> inputParams = new HashMap<String, String>();
			inputParams.put("accountID",accountID);
			inputParams.put("transactionType",transactionType);
			inputParams.put("offset","0");
			inputParams.put("limit",Integer.toString(limit));
			inputParams.put("isScheduled",isScheduled);
			inputParams.put("order",order);
			inputParams.put("requestType",requestType);
			inputParams.put("searchStartDate",searchStartDate);
			inputParams.put("searchEndDate",searchEndDate);
			inputParams.put("searchDateRange",searchDateRange);
			inputParams.put("searchTransactionType",searchTransactionType);			
			
			
			if(null != description && !"".equals(description.trim()))
				inputParams.put(DBPUtilitiesConstants.SEARCH_DESCRIPTION,description);
			if(null != searchMinAmount && !"".equals(searchMinAmount.trim()))
				inputParams.put(DBPUtilitiesConstants.SEARCH_MIN_AMOUNT,searchMinAmount);
			if(null != searchMaxAmount && !"".equals(searchMaxAmount.trim()))
				inputParams.put(DBPUtilitiesConstants.SEARCH_MAX_AMOUNT,searchMaxAmount);
			if(null != fromCheckNumber && !"".equals(fromCheckNumber.trim()))
				inputParams.put(DBPUtilitiesConstants.FRM_CHK_NUMBER,fromCheckNumber);
			if(null != toCheckNumber && !"".equals(toCheckNumber.trim()))
				inputParams.put(DBPUtilitiesConstants.TO_CHK_NUMBER,toCheckNumber);
			
			GenerateTransactionsDetails generateTransactionsDetails = new GenerateTransactionsDetails();
			
		//	alert.prepareError("******************* AdhocStatementEvent inputParams :"+inputParams).log();;
			
			byte[] base64 = generator.generateFile(transactionsArray, "Adhoc Statement", generatedBy,
                    HelperMethods.convertDateFormat(searchStartDate, paymentDateFormat),
                    HelperMethods.convertDateFormat(searchEndDate, paymentDateFormat),
                    generateTransactionsDetails.getFieldList(transactionType),
                    userData1, "filters",transactionType,inputParams);
            //getOtherRequiredData(eventDataObject, headers, authToken), "filters",transactionType,inputParams);

		//	alert.prepareError("******************** AdhocStatementEvent base64 :"+base64).log();
			if (base64 == null) {
				failureUpdateParam.put("failureMessage", "Error while generating adhocstatement file");
				updateAccountStatement(failureUpdateParam, headers);
				return;

			}
			HashMap<String, Object> updateParam = new HashMap<String, Object>();
//				Map<String, Object> updateHeader = new HashMap<>();
//				updateHeader.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
			String encodedText = Base64.getEncoder().encodeToString(base64);
			updateParam.put("id", fileId);
			updateParam.put("fileContent", encodedText);
			updateParam.put("status", Constants.STATUS_SUCCESS);
			updateAccountStatement(updateParam, headers);

		} catch (Exception e) {

			alert.prepareError("Error getting generating adhoc statements", e).log();
			HashMap<String, Object> failureUpdateParam = new HashMap<String, Object>();
			Map<String, Object> failureUpdateHeader = new HashMap<>();
			failureUpdateHeader.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
			failureUpdateHeader.put(DBPUtilitiesConstants.X_KONY_DEVICEID, deviceId);
			
			failureUpdateParam.put("id", fileId);
			failureUpdateParam.put("status", Constants.STATUS_FAIL);
			failureUpdateParam.put("failureMessage", e.getMessage());
			try {
				updateAccountStatement(failureUpdateParam, failureUpdateHeader);
			} catch (Exception exception) {
				alert.prepareError("Error while updating the database status ", exception).log();
			}
		}
	}

	private static Map<String, Object> getOtherData() {
		Map<String, Object> otherData = new HashMap<>();
		otherData.put("imgFileName", "kony_logo.png");
		otherData.put("accountNumber", "126028");
		JsonObject user = new JsonObject();
		/*
		 * user.addProperty("account", "700000000677");
		 * 
		 * user.addProperty("userfirstname", "ANDREAS PROTOPAPAS");
		 * user.addProperty("accountName", "CURRENT ACCOUNT");
		 * user.addProperty("accountNumber", "700000000677");
		 * user.addProperty("bankName", "Eurobank Cyprus");
		 * user.addProperty("accountType", "Checking"); user.addProperty("currency",
		 * "EUR");
		 * 
		 * String emailIds =
		 * "[{\"isPrimary\":\"true\",\"Value\":\"t.srinivasulu@temenos.com\",\"Type_id\":\"COMM_TYPE_EMAIL\"}]";
		 * JsonArray dataEmailIds = new JsonArray(); try { dataEmailIds = new
		 * Gson().fromJson(emailIds, JsonArray.class);
		 * 
		 * } catch (Exception e1) { // TODO Auto-generated catch block
		 * } user.add("EmailIds", dataEmailIds);
		 * 
		 * // String address = "Address\", \"18 EVAGORA PAPACHRISTOFOROU, PEROUSSIS //
		 * BUILDING, FLAT/OFFICE 101, LIMASSOL CY, 303"; String address =
		 * "[{\"AddressLine1\":\"18 EVAGORA, PEROUSSIS BUILDING, FLAT/OFFICE 101\",\"Address_id\":\"1\",\"ZipCode\":\"586966\",\"City_id\":\"Nicosia\",\"isPrimary\":\"true\",\"CityName\":\"Nicosia\",\"AddressType\":\"ADR_TYPE_HOME\",\"CountryCode\":\"IN\"}]"
		 * ; JsonArray dataAddress = new JsonArray(); try { dataAddress = new
		 * Gson().fromJson(address, JsonArray.class);
		 * 
		 * } catch (Exception e1) { // TODO Auto-generated catch block
		 *  } user.add("Addresses", dataAddress);
		 */

		user = getOtherUserData();

		otherData.put("userDetails", user);

//	otherData.put("membershipDetails", membershipDetails);
		return otherData;

	}
	
	private static JsonObject getOtherUserData() {
		// String transactions =
		// "[{\"OutstandingBalance\":\"-50000\",\"InstallmentType\":\"PAID\",\"Charges\":\"500\",\"Principal\":\"50000\",\"Date\":\"2023-03-02\"}]";
		String userData = "{\"isWireTransferEligible\":\"true\",\"userlastname\":\"Jones\",\"isActivationLinkSent\":\"true\",\"city\":\"\",\"userfirstname\":\"Ava\",\"bankName\":\"Infinity\",\"isAssociated\":\"true\",\"feedbackUserId\":\"f687f2e9-a31b-4667-8304-494ca5da727e\",\"lastlogintime\":\"2023-08-09 09:35:05.0\",\"customerStatus\":\"SID_CUS_ACTIVE\",\"Addresses\":[{\"Address_id\":\"1\",\"isPrimary\":\"true\",\"AddressType\":\"ADR_TYPE_HOME\"}],\"isPinSet\":\"false\",\"addressLine1\":\"\",\"email\":\"ranjith.r@temenos.com\",\"isWireTransferActivated\":\"false\",\"DateOfBirth\":\"1980-01-01\",\"isBillPayActivated\":\"false\",\"isCustomerEnrolled\":\"true\",\"EmailIds\":[{\"isPrimary\":\"true\",\"Value\":\"ranjith.r@temenos.com\",\"id\":\"1\",\"Type_id\":\"COMM_TYPE_EMAIL\"}],\"dateOfBirth\":\"1980-01-01\",\"userName\":\"4173020430\",\"isBillPaySupported\":\"true\",\"userId\":\"8775019467\",\"zipcode\":\"\",\"phone\":\"+91-9500622692\",\"ContactNumbers\":[{\"Extension\":\"Mobile\",\"phoneNumber\":\"9500622692\",\"phoneCountryCode\":\"+91\",\"isPrimary\":\"true\",\"Value\":\"9500622692\",\"id\":\"1\",\"Type_id\":\"COMM_TYPE_PHONE\"}],\"CoreCustomers\":[{\"coreCustomerID\":\"190748\",\"isBusiness\":\"false\",\"isPrimary\":\"true\",\"contractId\":\"0716316862\",\"contractName\":\"Ava Jones RETAIL 190748\",\"coreCustomerName\":\"Ava Jones RETAIL\"}],\"alertsTurnedOn\":\"false\",\"showBillPayFromAccPopup\":\"false\",\"LastName\":\"Jones\",\"isP2PActivated\":\"false\",\"account\":\"126028\",\"accountName\":null}";
		JsonObject data = new JsonObject();
		try {

			data = new Gson().fromJson(userData, JsonObject.class);
			// JsonObject jsonObject = new JsonParser().parse(userData).getAsJsonObject();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			alert.prepareError(e1.getMessage()).log();
		}
		return data;

	}

	
	public static void updateAccountStatement(HashMap<String, Object> updateParam, Map<String, Object> updateHeader) {
		//alert.prepareError("********************* AdhocStatementEvent updateAccountStatement updateParam :"+updateParam).log();
		ServiceCallHelper.invokeServiceAndGetJson(updateParam, updateHeader,
				URLConstants.ACCOUNTS_STATEMENT_FILES_UPDATE);

	}
	
	public String getCurrencySymbol(String currencyCode) {

		switch (currencyCode) {

		case "USD":
			return "$";
		case "EUR":
			return "€"; // Euro
		case "CRC":
			return "₡"; // Costa Rican Colón
		case "GBP":
			return "£"; // British Pound Sterling
		case "ILS":
			return "₪";// Israeli New Sheqel
		case "INR":
			return "₹"; // Indian Rupee
		case "JPY":
			return "¥"; // Japanese Yen
		case "KRW":
			return "₩"; // South Korean Won
		case "NGN":
			return "₦"; // Nigerian Naira
		case "PHP":
			return "₱"; // Philippine Peso
		case "PLN":
			return "zł"; // Polish Zloty
		case "PYG":
			return "₲"; // Paraguayan Guarani
		case "THB":
			return "฿"; // Thai Baht
		case "UAH":
			return "₴"; // Ukrainian Hryvnia
		case "VND":
			return "₫"; // Vietnamese Dong
		case "AUD":
			return "$"; // Australian Dollar
		case "CAD":
			return "$"; // Canadian Dollar
		case "CHF":
			return "Fr."; // Swiss Franc
		}
		return "";
	}

	
	public Map<String, Object> getOtherRequiredData(JSONObject inputParams,HashMap<String, Object> headers,String authToken)
	        throws HttpCallException {
		//alert.prepareError("getOtherRequiredData:File Type:"+inputParams.getString("fileType")).log();
		if ("qfx".equalsIgnoreCase(inputParams.getString("fileType"))) {
			return getOtherDataForQFX(inputParams, headers,authToken);
		} else if ("qbo".equalsIgnoreCase(inputParams.getString("fileType"))) {
			return getOtherDataForQFX(inputParams, headers,authToken);
		} else if ("pdf".equalsIgnoreCase(inputParams.getString("fileType"))) {
			return getOtherDataForPDF(inputParams, headers, authToken);
		} else {
			return getOtherData(inputParams, headers, authToken);
		}
	    }

	    
	  
	    private Map<String, Object> getOtherDataForPDF(JSONObject inputParams, HashMap<String, Object> headers,String authToken)
	            throws HttpCallException {
	        Map<String, Object> otherData = new HashMap<>();
	        try {
	        	
				JsonObject userData = ServiceCallHelper.invokeServiceAndGetJson(new HashMap<String, Object>(), headers, URLConstants.USER_DETAILS, authToken);
			//	alert.prepareError("inputParams ::"+inputParams).log();	
				if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString()) && userData.has("ExternalUsers")) {
				    JsonObject user = (JsonObject) userData.getAsJsonArray("ExternalUsers").get(0);
				    otherData.put("userDetails", user);				    		    
				    if (inputParams.has("accountName")) {
				        user.addProperty("accountName", inputParams.getString("accountName"));
				    }
				    if (inputParams.has("currency")) {	
				        user.addProperty("currency", inputParams.getString("currency"));
				    }
				    if (inputParams.has("branch")) {	
				        user.addProperty("branch", inputParams.getString("branch"));
				    }
				    user.addProperty("account", inputParams.get("accountID").toString());				    
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				alert.prepareError("catch block:"+e.getMessage()).log();
			}
	        //alert.prepareError("otherData ::"+otherData).log();
	        return otherData;
	    }

	    private Map<String, Object> getOtherData(JSONObject inputParams,HashMap<String, Object> headers,String authToken)
	            throws HttpCallException {
	        Map<String, Object> otherData = new HashMap<>();
	        JsonObject userData =  ServiceCallHelper.invokeServiceAndGetJson(new HashMap<String, Object>(), headers, URLConstants.USER_DETAILS, authToken);
	        if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString()) && userData.has("ExternalUsers")) {
	            JsonObject user = (JsonObject) userData.getAsJsonArray("ExternalUsers").get(0);
	            if (HelperMethods.isJsonNotNull(user.get("bankName"))) {
	                otherData.put("bankName", user.get("bankName").getAsString());
	            }
	        }
	        if (inputParams.has("accountID")) {
	          	 otherData.put("account", inputParams.get("accountID").toString());
	          	 otherData.put("accountNumber", inputParams.get("accountID").toString());
	          	
	        }
	        
	        if (inputParams.has("accountName")) {
	            otherData.put("accountName", inputParams.getString("accountName"));
	        }
	      //  diagnostic.prepareDebug("otherData ::"+otherData).log();
	        return otherData;
	    }
	    private Map<String, Object> getOtherDataForQFX(JSONObject inputParams,HashMap<String, Object> headers,String authToken)
	            throws HttpCallException {
	    	
	 //   	alert.prepareError("getOtherDataForQFX Start").log();
	        Map<String, Object> otherData = new HashMap<>();
	        otherData.put("intuitBuid", EnvironmentConfigurationsHandler.getValue("QFX_INTUIT_BUILD"));
	        otherData.put("financeId", EnvironmentConfigurationsHandler.getValue("QFX_FINANCE_ID"));
	        otherData.put("orgName", EnvironmentConfigurationsHandler.getValue("QFX_ORG_NAME"));
	        otherData.put("userName", EnvironmentConfigurationsHandler.getValue("QFX_USER_NAME"));
	        
		  /*  otherData.put("intuitBuid", "6157");
	        otherData.put("financeId","6157");
	        otherData.put("orgName", "Kony DBX Bank");
	        otherData.put("userName", "SRAJESH2K");*/
	        otherData.put("accountNumber", inputParams.get("accountID").toString());
	        try {
				JsonObject accounts = getAccounts(inputParams, headers,authToken);
				if ("0".equals(accounts.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
				    JsonArray accountList = getJsonArray(accounts, "Accounts");
				    otherData.put("accounts", accountList);
				}
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
	        	PrintWriter pw = new PrintWriter(sw);
	        	e.printStackTrace(pw);
	            alert.prepareError("Error while getting getOtherDataForQFX:"+sw.toString()).log();
			}
	 //       alert.prepareError("getOtherDataForQFX otherData"+otherData).log();
	        return otherData;
	    }

	    private Map<String, Object> getOtherDataForQBO(Map<String, String> inputParams, DataControllerRequest dcRequest)
	            throws HttpCallException {
	        Map<String, Object> otherData = new HashMap<>();
	        otherData.put("intuitBuid", EnvironmentConfigurationsHandler.getValue("QBO_INTUIT_BUILD", dcRequest));
	        otherData.put("financeId", EnvironmentConfigurationsHandler.getValue("QBO_FINANCE_ID", dcRequest));
	        otherData.put("orgName", EnvironmentConfigurationsHandler.getValue("QBO_ORG_NAME", dcRequest));
	        otherData.put("userName", EnvironmentConfigurationsHandler.getValue("QBO_USER_NAME", dcRequest));
			/*
			 * JsonObject accounts = getAccounts(inputParams, dcRequest); if
			 * ("0".equals(accounts.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
			 * JsonArray accountList = getJsonArray(accounts, "Accounts");
			 * otherData.put("accounts", accountList); }
			 */
	        return otherData;
	    }
	    
	    private JsonObject getAccounts(JSONObject inputParams,HashMap<String, Object> headers,String authToken)
	            throws HttpCallException {
	    	 
	        Map<String, String> input = new HashMap<>();
	        input.put("accountID", inputParams.get("accountID").toString());
	       // Map<String, String> headerMap = getHeaders(dcRequest, inputParams);
	       // headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
	       // return HelperMethods.callApiJson(dcRequest, input, headerMap, URLConstants.ACCOUNTS_POST);
	        JsonObject accountsData =  ServiceCallHelper.invokeServiceAndGetJson(new HashMap<String, Object>(), headers,  URLConstants.ACCOUNTS_POST, authToken);
	        return accountsData;
	        
	    }
	    private JsonArray getJsonArray(JsonObject jsonObject, String fieldName) {
	        if (jsonObject.has(fieldName) && jsonObject.get(fieldName).isJsonArray()) {
	            return jsonObject.getAsJsonArray(fieldName);
	        }
	        // alert.prepareError("No data for..."+fieldName).log();
	        return new JsonArray();
	    }
}