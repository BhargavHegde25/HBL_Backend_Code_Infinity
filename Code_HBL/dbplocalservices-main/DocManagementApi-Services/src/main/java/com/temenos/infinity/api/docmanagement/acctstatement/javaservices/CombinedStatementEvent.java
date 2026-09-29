package com.temenos.infinity.api.docmanagement.acctstatement.javaservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
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
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.api.events.EventSubscriber;
import com.konylabs.middleware.api.events.IntegrationEventSubscriber;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

@IntegrationEventSubscriber(topics = { "/events/combinedstatement" })
public class CombinedStatementEvent implements EventSubscriber {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public void onEvent(EventData eventData) {
		alert.prepareError("CombinedStatementEvent event Triggered").log();
		String data = eventData.getData().toString();
		JSONObject dataObject;
		String fileId = "";
		String authToken = null;
		try {
			dataObject = new JSONObject(data);
			JSONObject eventObject = dataObject.getJSONObject("events");
			JSONObject eventDataObject = eventObject.getJSONObject("eventData");
			String accounts = eventDataObject.has("accountIds") ? eventDataObject.get("accountIds").toString() : null;

			if (StringUtils.isBlank(accounts)) {
				return;
			}
			String fromDate = eventDataObject.has("fromDate") ? eventDataObject.get("fromDate").toString() : null;
			String toDate = eventDataObject.has("toDate") ? eventDataObject.get("toDate").toString() : null;
			String userId = eventDataObject.has("userId") ? eventDataObject.get("userId").toString() : null;
			String fileType = eventDataObject.has("fileType") ? eventDataObject.get("fileType").toString() : null;
			String currencyCode = eventDataObject.has("currencyCode") ? eventDataObject.get("currencyCode").toString()
					: null;
			String paymentDateFormat = eventDataObject.has("dateFormat") ? eventDataObject.get("dateFormat").toString()
					: null;

			String generatedBy = eventDataObject.has("generatedBy") ? eventDataObject.get("generatedBy").toString()
					: null;
			String customerType = eventDataObject.has("customerType") ? eventDataObject.get("customerType").toString() : null;
			String companyId = eventDataObject.has("companyId") ? eventDataObject.get("companyId").toString() : "";
			String bankName = eventDataObject.has("bankName") ? eventDataObject.get("bankName").toString() : null;
			fileId = eventDataObject.has("id") ? eventDataObject.get("id").toString() : null;

			// Get Auth Token
			HashMap<String, Object> identityParam = new HashMap<String, Object>();
			Map<String, Object> identitytHeaders = new HashMap<>();
			String dbpAppKey = EnvironmentConfigurationsHandler.getValue("DBP_CORE_APPKEY");
			String dbpAppSecret = EnvironmentConfigurationsHandler.getValue("DBP_CORE_SECRET");
			String sharedSecret = EnvironmentConfigurationsHandler.getValue("DBP_CORE_SHARED_SECRET");
			identitytHeaders.put("X-Kony-App-Key", dbpAppKey);
			identitytHeaders.put("X-Kony-App-Secret", dbpAppSecret);
			identitytHeaders.put("x-kony-dbp-api-access-token", sharedSecret);
			if (StringUtils.isBlank(dbpAppKey) || StringUtils.isBlank(dbpAppSecret)
					|| StringUtils.isBlank(sharedSecret)) {
				alert.prepareError("Error while file fetching DBP_CORE_APPKEY or DBP_CORE_SECRET or DBP_CORE_SHARED_SECRET").log();
			}
			JsonObject identityResult = ServiceCallHelper.invokeServiceAndGetJson(identityParam, identitytHeaders,
					"dm.api.login");
			if (identityResult.has("claimsToken")) {
				authToken = identityResult.get("claimsToken").getAsString();
			} else {
				alert.prepareError("Error while file fetching auth Token in combined statements").log();
				return;
			}

			JSONObject accountsObject = new JSONObject(accounts);
			Set<String> backendIdList = accountsObject.keySet();
			
			JSONArray accountList = new JSONArray();
			backendIdList.forEach(key -> accountsObject.getJSONArray(key).forEach(value -> accountList.put(value)));

			JsonArray transactionsArray = new JsonArray();

			HashMap<String, Object> failureUpdateParam = new HashMap<String, Object>();
			Map<String, Object> failureUpdateHeader = new HashMap<>();
			failureUpdateHeader.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
			failureUpdateParam.put("id", fileId);
			failureUpdateParam.put("status", Constants.STATUS_FAIL);
			
			
			String authToken1 = eventDataObject.has(DBPUtilitiesConstants.X_KONY_AUTHORIZATION)
					? eventDataObject.get(DBPUtilitiesConstants.X_KONY_AUTHORIZATION).toString()
					: null;
			String deviceId = eventDataObject.has(DBPUtilitiesConstants.X_KONY_DEVICEID)
					? eventDataObject.get(DBPUtilitiesConstants.X_KONY_DEVICEID).toString()
					: null;
			HashMap<String, Object> headers = new HashMap<>();
			headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken1);
			headers.put(DBPUtilitiesConstants.X_KONY_DEVICEID, deviceId);
			
			Map<String, Object> userData1 = new HashMap<String, Object>();
			if ("pdf".equalsIgnoreCase(fileType)) {
				JsonParser parser = new JsonParser();
	            JsonObject accountMappings = parser.parse(accounts).getAsJsonObject();
				userData1.put("accountMappings", accountMappings);
				Map<String, String> backendIdCustIdMap = new HashMap<String, String>();
				String backendIds = String.join(",", backendIdList);
				backendIdCustIdMap = fetchCustomerIdByCoreCustomerId(backendIds);
				for (String key : backendIdCustIdMap.keySet()) {
					String loginUserId = backendIdCustIdMap.get(key);
					eventDataObject.put("loginUserId", loginUserId);
					eventDataObject.put("isLoggedInUser", "false");
					Map<String, Object> userData = getOtherRequiredData(eventDataObject, headers, authToken1);
					userData1.put(key, userData.get("userDetails"));
					alert.prepareError("CombinedStatement userData ::" + userData1);
				}
			} else {
				userData1 = getOtherRequiredData(eventDataObject, headers, authToken1);
				alert.prepareError("CombinedStatement userData ::" + userData1).log();
			}

			for (Object individualAccountIdObj : accountList) {
				
				String individualAccountId = individualAccountIdObj.toString();
				alert.prepareError("*********CombinedStatementEvent For each account:"+individualAccountId).log();
				// Fetch Account Details

				String accountName = "";
				String accountCurrencyCode = "";
				String nickName = "";
				String accountType = "";
			
				HashMap<String, Object> accountsParam = new HashMap<String, Object>();
				accountsParam.put("accountID", individualAccountId);
				accountsParam.put("customerID", userId);
				accountsParam.put("customerType",customerType);
				accountsParam.put("companyId",companyId);
				Map<String, Object> accountHeaders = new HashMap<>();
				accountHeaders.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
				JsonObject accountJsonObject = ServiceCallHelper.invokeServiceAndGetJson(accountsParam, accountHeaders,
						URLConstants.GET_COMBINED_STATEMENT_ACCOUNT_DETAILS);
				
				if (accountJsonObject.has("accountName") && accountJsonObject.get("accountName") != null) {
					accountName = accountJsonObject.get("accountName").getAsString();
				}
				if (accountJsonObject.has("currencyCode") && accountJsonObject.get("currencyCode") != null) {
					accountCurrencyCode = accountJsonObject.get("currencyCode").getAsString();
				}
				if (accountJsonObject.has("nickName") && accountJsonObject.get("nickName") != null) {
					nickName = accountJsonObject.get("nickName").getAsString();
				}
				if (accountJsonObject.has("accountType") && accountJsonObject.get("accountType") != null) {
					accountType = accountJsonObject.get("accountType").getAsString();
				}
				
				if(nickName!=null && !"".equals(nickName)) {
					accountName = nickName;
				}
				
				//Added to get account details from cache instead of service call
				
 /*               ServicesManager servicesManager = eventData.getServicesManager();
                String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
                String key="INTERNAL_BANK_ACCOUNTS"+customerID;
                try {
                	ResultCache resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
                    if (resultCache != null && StringUtils.isNotBlank(key)) {
                        String value = (String) resultCache.retrieveFromCache(key);
                        SessionMap sessionData = new SessionMap();
                        sessionData.setData(value);
                       // return sessionData;
                        
                        Map<String, String> account = sessionData.getValue(individualAccountId);
                        if(account!=null) {
                        	String accountType = account.get("accountType");
                        	accountName =  account.get("accountName");
                        	currencyCode =  account.get("currencyCode");
                        	//branchName = account.get("branchName")==null?"":account.get("branchName");
                        	
                        }
                    }
                } catch (Exception e) {
                    alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
                }
				*/
				//End of changes for account details
				
				

				// fetch Transactions
				JsonObject individualAccountTransaction = new JsonObject();
				HashMap<String, Object> transactionsParam = new HashMap<String, Object>();
				transactionsParam.put("accountNumber", individualAccountId);
				transactionsParam.put("searchTransactionType", "Both");
				transactionsParam.put("searchStartDate", fromDate);
				transactionsParam.put("searchEndDate", toDate);
				transactionsParam.put("isScheduled", "0");
				transactionsParam.put("searchType", "Search");
				transactionsParam.put("User_id", userId);
				transactionsParam.put("companyId",companyId);
				transactionsParam.put("requestType","combined");
				transactionsParam.put("searchDateRange","CustomDateRange");
				transactionsParam.put("limit",DBPUtilitiesConstants.COMBINED_DOWNLOAD_PAGE_SIZE);
				
				//	alert.prepareError("*********CombinedStatementEvent before getting transactions:"+transactionsParam).log();

				headers = new HashMap<>();
				headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken1);
				JsonObject transactionObject = ServiceCallHelper.invokeServiceAndGetJson(transactionsParam, headers,
						URLConstants.TRANSACTIONS_POST,authToken1);
			//	LOG.error("*********CombinedStatementEvent After getting transactions:"+transactionObject);
				if (transactionObject.has(Constants.OPSTATUS) && transactionObject.get(Constants.OPSTATUS) != null
						&& !transactionObject.get(Constants.OPSTATUS).toString().equalsIgnoreCase("0")) {
					
					failureUpdateParam.put("failureMessage",
							"Error while fetching transaction for accounId" + individualAccountId);
					updateAccountStatement(failureUpdateParam, failureUpdateHeader);
					return;
				}
				if (transactionObject.has(ErrorCodeEnum.ERROR_CODE_KEY)
						&& transactionObject.get(ErrorCodeEnum.ERROR_CODE_KEY) != null
						&& StringUtils.isNotBlank(transactionObject.get(ErrorCodeEnum.ERROR_CODE_KEY).toString())) {
					failureUpdateParam.put("failureMessage",
							"Error while fetching transaction for accounId" + individualAccountId);
					updateAccountStatement(failureUpdateParam, failureUpdateHeader);
					return;
				}

				// generate object
				if (transactionObject.has("Transactions")) {
					individualAccountTransaction.add("transactionsList",
							transactionObject.get("Transactions").getAsJsonArray());
				}
				individualAccountTransaction.addProperty("accountName", accountName);
				individualAccountTransaction.addProperty("accountNumber", individualAccountId);
				individualAccountTransaction.addProperty("nickName", nickName);
				individualAccountTransaction.addProperty("accountType", accountType);
				//individualAccountTransaction.addProperty("currencySymbol", currencyCode);
				individualAccountTransaction.addProperty("currencySymbol", accountCurrencyCode);
				//alert.prepareError("Currency Code : currencyCode ::"+currencyCode+ " accountCurrencyCode :"+accountCurrencyCode ).log();
				// add into final Array
				transactionsArray.add(individualAccountTransaction);
			}
		//	alert.prepareError("CombinedStatement userData ::"+userData1).log();
		//	alert.prepareError("Before generating combined statement file:"+transactionsArray.size()).log();
			String base64 = GenerateFile(transactionsArray, fileType, fromDate, toDate, currencyCode, paymentDateFormat,
					generatedBy, bankName,userData1);

			if (StringUtils.isBlank(base64)) {
				failureUpdateParam.put("failureMessage", "Error while generating combinedstatement file");
				updateAccountStatement(failureUpdateParam, failureUpdateHeader);
				return;
			}
			HashMap<String, Object> updateParam = new HashMap<String, Object>();
			Map<String, Object> updateHeader = new HashMap<>();
			updateHeader.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
			updateParam.put("id", fileId);
			updateParam.put("fileContent", base64);
			updateParam.put("status", Constants.STATUS_SUCCESS);
			updateAccountStatement(updateParam, updateHeader);

		} catch (Exception e) {
			alert.prepareError("Error getting generating combined statements", e).log();
			HashMap<String, Object> failureUpdateParam = new HashMap<String, Object>();
			Map<String, Object> failureUpdateHeader = new HashMap<>();
			failureUpdateHeader.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, authToken);
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

	public static void updateAccountStatement(HashMap<String, Object> updateParam, Map<String, Object> updateHeader) {
		ServiceCallHelper.invokeServiceAndGetJson(updateParam, updateHeader,
				URLConstants.ACCOUNTS_STATEMENT_FILES_UPDATE);

	}

	private String GenerateFile(JsonArray transactionsArray, String fileType, String startDate, String endDate,
			String currencyCode, String paymentDateFormat, String generatedBy, String bankName,Map<String, Object> otherData) {
		FileGenerator generator = FileGeneratorFactory.getFileGenerator(fileType);
		String title = "Combined Statement";
		String currencySymbol = getCurrencySymbol(currencyCode);
		byte[] bytes = null;
		try {
			bytes = generator.generateCombinedStatementsFile(transactionsArray, title, generatedBy, startDate, endDate,
					getFieldList(), bankName, currencySymbol, paymentDateFormat,otherData);
		} catch (Exception e) {
			alert.prepareError("Error while generating combined statement file").log();
			return null;
		}
		//String encodedText = Base64.encodeBytes(bytes);
		String encodedText = Base64.getEncoder().encodeToString(bytes);
		return encodedText;
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

	private Map<String, String> getFieldList() {
		String resourceName = "CombinedStatementsHeader.properties";
		Map<String, String> fieldList = new LinkedHashMap<>();
		try (InputStream is = URLFinder.class.getClassLoader().getResourceAsStream(resourceName);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {
			String line = null;
			String[] words = null;
			while ((line = reader.readLine()) != null) {
				if (!line.startsWith("#")) {
					words = line.split("=");
					fieldList.put(words[0], words[1]);
				}
			}
		} catch (IOException e) {
			alert.prepareError("Error on reading CombinedStatementsHeader.properties..." + e).log();
		}
		return fieldList;
	}
	

	public Map<String, Object> getOtherRequiredData(JSONObject inputParams,HashMap<String, Object> headers,String authToken)
	        throws HttpCallException {
	        if ("pdf".equalsIgnoreCase(inputParams.getString("fileType"))) {
	            return getOtherDataForPDF(inputParams,headers,authToken);
	        } else {
	            return getOtherData(inputParams,headers,authToken);
	        }
	    }
	  
	    private Map<String, Object> getOtherDataForPDF(JSONObject inputParams, HashMap<String, Object> headers,String authToken)
	            throws HttpCallException {
	        Map<String, Object> otherData = new HashMap<>();
	        try {
	        	alert.prepareError("CombinedStatements getOtherDataForPDF inputParams ::"+inputParams).log();	
	        	alert.prepareError("CombinedStatements getOtherDataForPDF headers ::"+headers).log();
	        	Map<String,Object> requestPayload = new HashMap<String, Object>();
	        	requestPayload.put("loginUserId", inputParams.getString("loginUserId"));
	        	requestPayload.put("isLoggedInUser", inputParams.getString("isLoggedInUser"));
				JsonObject userData = ServiceCallHelper.invokeServiceAndGetJson(requestPayload, headers, URLConstants.GET_USER_DETAILS, authToken);
				
				if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString()) && userData.has("user")) {
				    JsonObject user = (JsonObject) userData.getAsJsonArray("user").get(0);
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
				alert.prepareError("catch block",e).log();
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
	        }
	        
	        if (inputParams.has("accountName")) {
	            otherData.put("accountName", inputParams.getString("accountName"));
	        }
	        diagnostic.prepareDebug("otherData ::"+otherData).log();
	        return otherData;
	    }
	    
		private static Map<String, String> fetchCustomerIdByCoreCustomerId(String cifsList) {

			Map<String, String> custIdBackendIdMap = new HashMap<String, String>();
			Map<String, Object> serviceReqMap = new HashMap<String, Object>();
			String[] cifsArr = cifsList.split(",");
			List<String> cifList = new ArrayList<String>();
			for (int i = 0; i < cifsArr.length; i++) {
				cifList.add("BackendId" + DBPUtilitiesConstants.EQUAL + cifsArr[i]);
			}
			String cifIds = null;
			if (cifList == null || cifList.isEmpty()) {
				custIdBackendIdMap = null;
			} else {
				cifIds = String.join(",", cifList);
				if (cifIds != null) {
					serviceReqMap.put(DBPUtilitiesConstants.FILTER, cifIds);
					serviceReqMap.put("loop_count", Integer.toString(cifList.size()));
					custIdBackendIdMap = fetchCustomerId(serviceReqMap, null);
				}
			}
			return custIdBackendIdMap;
		}

		private static Map<String, String> fetchCustomerId(Map<String, Object> serviceReqMap,
				Map<String, Object> headerParams) {
			Map<String, String> backendIdCustIdMap = new HashMap<>();

			String serviceName = "CustomerAndCustomerGroup";
			String operationName = "getUsernameByCoreCustomerId";

			String backendResponse = null;
			try {
				backendResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName)
						.withOperationId(operationName).withRequestParameters(serviceReqMap)
						.withRequestHeaders(headerParams).build().getResponse();

				JSONObject responseObj = new JSONObject(backendResponse);
				JSONArray records = responseObj.getJSONArray("LoopDataset");
				for (int i = 0; i < records.length(); i++) {
					responseObj = records.getJSONObject(i);
					if (responseObj.has("errmsg") || responseObj.has("dbpErrMsg") || responseObj.has("errorMessage")) {
						backendIdCustIdMap = null;
						return backendIdCustIdMap;
					}
					JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
					if (jsonArray != null) {
						JSONObject respObj = jsonArray.getJSONObject(0);
						backendIdCustIdMap.put(respObj.getString("BackendId"), respObj.getString("customerId"));
					}
				}

			} catch (JSONException e) {
				alert.prepareError("Failed to fetch details: " + e).log();
				return null;
			} catch (Exception e) {
				alert.prepareError("Caught exception at fetchCustomerId: " + e).log();
				return null;
			}
			return backendIdCustIdMap;
		}
}