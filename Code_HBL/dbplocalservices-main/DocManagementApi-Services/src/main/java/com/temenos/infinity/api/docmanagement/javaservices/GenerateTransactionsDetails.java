package com.temenos.infinity.api.docmanagement.javaservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.fileutil.FileGenerator;
import com.kony.dbputilities.fileutil.FileGeneratorFactory;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServerConfigurations;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.infinity.api.transactionadvice.constants.ErrorCodeEnum;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;


public class GenerateTransactionsDetails implements JavaService2 {
    private final String TRANSACTION_ARRAY = "Transactions";
    private final String TRANSACTION_CARD_ARRAY = "transactions";
    private final String ACCOUNTS_ARRAY = "Accounts";
    private String[] blockedheaders = new String[] {  "Reference Number", "Description", "From Date" ,"To Date","Amount"};
    private String[] blockedfields = new String[] { "transactionReference", "lockReason", "fromDate", "toDate", "lockedAmount" };

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	private static final String PRIMARY_SERVICE_ID  = "ArrangementsT24ISTransactions";
	private static final String FALLBACK_SERVICE_ID = "ArrangementsT24ISTransCustom"; // TODO: put the real fallback service name here
	private static final String TIMED_OPERATION_ID  = "searchTransactionsTimedHBL";

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        String format = "MM/dd/YYYY";
        final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (validateFileType(inputParams, dcRequest, result)) {
            FileGenerator generator = FileGeneratorFactory.getFileGenerator(inputParams.get("fileType"));
            // to get identity handler
            dcRequest.addRequestParam_(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, inputParams.get("Auth_Token"));
            dcRequest.getHeaderMap().put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, inputParams.get("Auth_Token"));

            JsonObject transactions = new JsonObject();
            String transactionType = (String) inputParams.get(DBPUtilitiesConstants.SEARCH_TRANS_TYPE);
            String installmentType = (String) inputParams.get(DBPUtilitiesConstants.INSTALLMENT_TYPE);
            String paymentDateFormat = (String) inputParams.get(DBPUtilitiesConstants.DATE_FORMAT);
            String accountNumber = (String)inputParams.get("accountNumber");
            String requestType =  (String)inputParams.get("requestType");
           
            if (StringUtils.isBlank(accountNumber)) {
            	return ErrorCodeEnum.ERR_25006.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(transactionType)) {
            	return ErrorCodeEnum.ERR_25002.setErrorCode(new Result());
            }
            
           
            
            if (transactionType.equalsIgnoreCase("LoanSchedule")) {
                transactions = getLoanTransactions(inputParams, dcRequest);
                
            } else if(transactionType.equalsIgnoreCase("Pending") || transactionType.equalsIgnoreCase("Posted") || transactionType.equalsIgnoreCase("Both")) {
            	 
            	 dcRequest.addRequestParam_("accountID", accountNumber);
            	 dcRequest.addRequestParam_("transactionType", transactionType);
            	 dcRequest.addRequestParam_("isScheduled", "false");
            	 dcRequest.addRequestParam_("order", "desc");
            	 inputParams.put("accountID", accountNumber);
            	 inputParams.put("transactionType", transactionType);
            	 inputParams.put("isScheduled", "false");
            	 inputParams.put("order", "desc");
            	 diagnostic.prepareDebug("HBL## Pending posted transactions"+ transactions.toString()).log();
//            	 transactions = getRecentTransactions(inputParams, dcRequest);
            	 transactions = getHBLTransactions(inputParams, dcRequest);
            }else if(transactionType.equalsIgnoreCase("Blocked")) {
	           	 dcRequest.addRequestParam_("accountID", accountNumber);
	           	 dcRequest.addRequestParam_("transactionType", "All");
	           	 dcRequest.addRequestParam_("isScheduled", "false");
	           	 dcRequest.addRequestParam_("order", "desc");
	           	dcRequest.addRequestParam_("requestType", "");
	           	 inputParams.put("accountID", accountNumber);
	           	 inputParams.put("transactionType", "All");
	           	 inputParams.put("isScheduled", "false");
	           	 inputParams.put("order", "desc");
	             inputParams.put("requestType", "");
            	transactions = getBlockedTransactions(inputParams, dcRequest);
            }else if(transactionType.equalsIgnoreCase("Cards")) {
            	diagnostic.prepareDebug("HBL## In cards condition").log();
				String cardNumber = (String) inputParams.get("accountNumber");
				String statementDate = (String) inputParams.get("searchStartDate");
				dcRequest.addRequestParam_("cardNumber", cardNumber);
				dcRequest.addRequestParam_("statementDate", statementDate);
				inputParams.put("cardNumber", cardNumber);
				inputParams.put("statementDate", statementDate);
				
				transactions = getCardTransactions(inputParams, dcRequest);
            	diagnostic.prepareDebug("HBL## getCardTransactions"+ transactions.toString()).log();
            }
            else {
            	diagnostic.prepareDebug("HBL## In else condition").log();
            	dcRequest.addRequestParam_("requestType", "download");
            	 inputParams.put("requestType", "download");
//            	transactions = getTransactions(inputParams, dcRequest);
            	 transactions = getHBLTransactions(inputParams, dcRequest);
            	diagnostic.prepareDebug("HBL## transactions"+ transactions.toString()).log();
            }
            inputParams.put("requestType", requestType);
            alert.prepareError("transactions >> "+transactions).log();
            diagnostic.prepareDebug("transactions >> "+transactions).log();
         
          /*  if (transactions.has("MoreRecordsflag") ) {
            	String moreRecordFlag = transactions.get("MoreRecordsflag").getAsString();
            	if("Adhoc Statement".equalsIgnoreCase(moreRecordFlag)) {
                	transactions.addProperty("fileId", "Adhoc Statement");
                	Result res = ConvertJsonToResult.convert(transactions);
                	res.addParam(new Param("opstatus","1234"));
                	res.addParam(new Param("errmsg","We are preparing your statement, once the statement is ready, you can download it from Account statements under Adhoc Statements tab"));
                	return res;
            	}

            }*/
            if(inputParams.get("accountNumber").equalsIgnoreCase("MORT12345") || inputParams.get("accountNumber").equalsIgnoreCase("122877") ||
            	inputParams.get("accountNumber").equalsIgnoreCase("122878")) {
            	JsonParser parser = new JsonParser();
            	String MortgageTransactionsInSession = (String) CommonUtils.retreiveFromSession("MortgageTransactions", dcRequest);
            	alert.prepareError("MortgageTransactionsInSession >> "+MortgageTransactionsInSession).log();
            	transactions = parser.parse(MortgageTransactionsInSession).getAsJsonObject();
            	JsonArray transactionsArray = transactions.getAsJsonArray("Transactions");
            	alert.prepareError ("HBL All Transactions *************** "+transactionsArray.toString()).log();
            	JsonArray transactionsArrayNew = new JsonArray();
            	JsonObject jsonObject =new JsonObject();
            	for (JsonElement json : transactionsArray) {
            		jsonObject = json.getAsJsonObject();
            		jsonObject.addProperty("InstallmentType",JSONUtil.getString(jsonObject, "installmentType"));
            		jsonObject.addProperty("Date",JSONUtil.getString(jsonObject, "date"));
            		transactionsArrayNew.add(jsonObject);
            	}
            	transactions.add("Transactions", transactionsArrayNew);
            	transactions.addProperty("opstatus", "0");
            	transactions.addProperty("httpStatusCode", "0");
            	alert.prepareError("transactions >> "+transactions).log();
            }
            paymentDateFormat = updatePaymentFormat(paymentDateFormat);
            if ("0".equals(transactions.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            	diagnostic.prepareDebug("HBL## In TRANSACTION_ARRAY").log();
            	JsonArray data;
            	if(transactionType.equalsIgnoreCase("Cards")) {
            		data = getJsonArray(transactions, TRANSACTION_CARD_ARRAY);
            	}else {
            		data = getJsonArray(transactions, TRANSACTION_ARRAY);
            	}
                byte[] bytes;
                Map<String, Object> otherData = getOtherRequiredData(inputParams, dcRequest);
                otherData.put("accountNumber", accountNumber);
                
                String accountName ="",currencyCode="",branchName="",accountType="";
                
                ResultCache resultCache = null;
                ServicesManager servicesManager = dcRequest.getServicesManager();
                String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
                String key="INTERNAL_BANK_ACCOUNTS"+customerID;
                try {
                    resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
                    if (resultCache != null && StringUtils.isNotBlank(key)) {
                        String value = (String) resultCache.retrieveFromCache(key);
                        SessionMap sessionData = new SessionMap();
                        sessionData.setData(value);
                       // return sessionData;
                        
                        Map<String, String> account = sessionData.getValue(accountNumber);
                        if(account!=null) {
                        	accountType = account.get("accountType");
                        	accountName =  account.get("accountName");
                        	currencyCode =  account.get("currencyCode");
                        	branchName = account.get("branchName")==null?"":account.get("branchName");
                        	
                        }
                    }
                } catch (Exception e) {
                    alert.prepareError("Exception occured while fetching ResultCache instance from Services Manager API", e).log();
                }
                JsonObject userDetails = new JsonObject();
                if(otherData.containsKey("userDetails") && otherData.get("userDetails")!=null ) {
                	 userDetails = (JsonObject) otherData.get("userDetails");
                }else {
                	 userDetails = new JsonObject();
                }
                
				if (transactionType.equalsIgnoreCase("Cards") && transactions != null
						&& transactions.has("accountNumber")) {
					String accNbr = transactions.get("accountNumber").getAsString();
					accNbr = accNbr.replace("'", "");
					diagnostic.prepareDebug("HBL## Card Linked AccountNumber  " + accNbr).log();
					userDetails.addProperty("account", accNbr);
					userDetails.addProperty("cardNumber", (String) inputParams.get("accountNumber"));
				} else {
					userDetails.addProperty("account", accountNumber);
				}

				userDetails.addProperty("accountName", accountName);
				userDetails.addProperty("currency", currencyCode);
				userDetails.addProperty("branch", branchName);
				userDetails.addProperty("accountType", accountType);
				
				diagnostic.prepareDebug("HBL## userDetails" + userDetails.toString()).log();
				
				otherData.put("userDetails", userDetails);
                
                if((StringUtils.isNotBlank(transactionType)
                        && transactionType.equals("Blocked"))) {
                	 bytes = generator.generateFile(data, inputParams.get("title"), inputParams.get("generatedBy"),
                             HelperMethods.convertDateFormat(inputParams.get("searchStartDate"), format),
                             HelperMethods.convertDateFormat(inputParams.get("searchEndDate"), format),
                             getBlockedFundsFieldList(),
                             otherData, inputParams.get("filters"),transactionType,inputParams);
                }else if ((StringUtils.isNotBlank(transactionType)
                        && transactionType.equals("Cards"))) {
                    bytes = generator.generateFile(data, inputParams.get("title"), inputParams.get("generatedBy"),
                            HelperMethods.convertDateFormat(inputParams.get("searchStartDate"), format),
                            HelperMethods.convertDateFormat(inputParams.get("searchEndDate"), format),
                            getFieldList(transactionType),
                            otherData, inputParams.get("filters"),transactionType,inputParams);
                    diagnostic.prepareDebug(" HBL## title in cards"+ inputParams.get("title")).log();
                }
				else if (!StringUtils.isNotBlank(transactionType) || !transactionType.equals("LoanSchedule")) {
					diagnostic.prepareDebug(" HBL## inside All" + inputParams.get("title")).log();
					bytes = generator.generateFile(data, inputParams.get("title"), inputParams.get("generatedBy"),
							HelperMethods.convertDateFormat(inputParams.get("searchStartDate"), format),
							HelperMethods.convertDateFormat(inputParams.get("searchEndDate"), format),
							getFieldList(transactionType), otherData, inputParams.get("filters"), transactionType,
							inputParams);
				} else if (transactionType.equals("eSewa")) {
					bytes = generator.generateFile(data, inputParams.get("title"), inputParams.get("generatedBy"),
							HelperMethods.convertDateFormat(inputParams.get("searchStartDate"), format),
							HelperMethods.convertDateFormat(inputParams.get("searchEndDate"), format),
							getFieldList(transactionType), otherData, inputParams.get("filters"), transactionType,
							inputParams);
				} else {
					diagnostic.prepareDebug(" HBL## In bytes condition").log();
					Map<String, String> summaryDetails = new HashMap<String, String>();
					summaryDetails = getLoanScheduleSummaryDetails(inputParams, dcRequest);
					bytes = generator.generateLoanFile(data, inputParams.get("title"), inputParams.get("generatedBy"),
							getFieldList(transactionType), otherData, inputParams.get("filters"), installmentType,
							summaryDetails, paymentDateFormat);
				}

                String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
                MemoryManager.saveIntoCache(fileId, bytes, 120);
                diagnostic.prepareDebug("HBL##  fileId value"+ fileId).log();
                result.addParam("fileId", fileId, DBPUtilitiesConstants.STRING_TYPE);

            } else {
            	diagnostic.prepareDebug(" HBL## result:"+ transactions.toString()).log();
                result = ConvertJsonToResult.convert(transactions);
            }
        }
        return result;
    }
    
    //HBL Changes
    private JsonObject getHBLTransactions(Map<String, String> inputParams,
            DataControllerRequest dcRequest) throws Exception {

        Map<String, Object> serviceParams = new HashMap<>();
        for (Map.Entry<String, String> entry : inputParams.entrySet()) {
            serviceParams.put(entry.getKey(), entry.getValue());
        }

        // Class 2 normally derives these; supply them explicitly on the direct path
        serviceParams.put("accountID", inputParams.get("accountNumber"));
        serviceParams.put("searchStartDate", inputParams.get("searchStartDate"));
        serviceParams.put("searchEndDate", inputParams.get("searchEndDate"));
        serviceParams.put("requestType", "download");

     // Posted downloads go to the completed-transactions operation;
        // everything else keeps the timed search.
        String searchTransactionType = inputParams.get("searchTransactionType");
        boolean isPosted = searchTransactionType != null
                && "Posted".equalsIgnoreCase(searchTransactionType.trim());
        String operationId = isPosted ? "getCompletedTransactions" : TIMED_OPERATION_ID;

        if (isPosted) {
            serviceParams.put("transactionType", "Posted");
        }

        alert.prepareError("HBL## searchTransactionType=" + searchTransactionType
                + " -> operationId=" + operationId).log();

        String raw = invokeTransactionService(operationId, serviceParams, dcRequest, alert);
        alert.prepareError("HBL## " + operationId + " raw :: " + raw).log();

        JsonObject response = JsonParser.parseString(raw).getAsJsonObject();

        if (response.has(TRANSACTION_ARRAY) && response.get(TRANSACTION_ARRAY).isJsonArray()) {
            for (JsonElement element : response.getAsJsonArray(TRANSACTION_ARRAY)) {
                normalizeHBLTransaction(element.getAsJsonObject());
            }
        }
        return response;
    }
    
	private String invokeTransactionService(String operationId, Map<String, Object> serviceParams,
			DataControllerRequest dcRequest, Alert alert) throws Exception {
		try {
			return callService(PRIMARY_SERVICE_ID, operationId, serviceParams, dcRequest);
		} catch (Exception e) {
			// Fallback only for the timed search operation
			if (!TIMED_OPERATION_ID.equalsIgnoreCase(operationId)) {
				throw e;
			}
			alert.prepareError("HBL## " + PRIMARY_SERVICE_ID + "/" + operationId + " failed ("
					+ e.getClass().getSimpleName() + ": " + e.getMessage() + "). Retrying with " + FALLBACK_SERVICE_ID)
					.log();

			return callService(FALLBACK_SERVICE_ID, operationId, serviceParams, dcRequest);
		}
	}

	private String callService(String serviceId, String operationId, Map<String, Object> serviceParams,
			DataControllerRequest dcRequest) throws Exception {
		return DBPServiceExecutorBuilder.builder().withServiceId(serviceId).withOperationId(operationId)
				.withRequestParameters(serviceParams).withRequestHeaders(dcRequest.getHeaderMap())
				.withDataControllerRequest(dcRequest).build().getResponse();
	}

    /** Reproduces the object-service field mapping, keeping the balance fields. */
    private void normalizeHBLTransaction(JsonObject txn) {
        copyIfPresent(txn, "valueDate", "scheduledDate");
        copyIfPresent(txn, "description", "transactionNotes");
        copyIfPresent(txn, "notes", "transactionsNotes");
        copyIfPresent(txn, "statusDescription", "cashWithdrawalTransactionStatus");
    }

    private void copyIfPresent(JsonObject txn, String source, String target) {
        if (txn.has(source) && !txn.get(source).isJsonNull() && !txn.has(target)) {
            txn.addProperty(target, txn.get(source).getAsString());
        }
    }

    //End of HBL Changes
    
    private String updatePaymentFormat(String paymentDateFormat) {
        if (StringUtils.isNotBlank(paymentDateFormat)) {
            if (paymentDateFormat.equals("m/d/Y")) {
                paymentDateFormat = "MM/dd/yyyy";
            } else if (paymentDateFormat.equals("d/m/Y")) {
                paymentDateFormat = "dd/MM/yyyy";
            } else if (paymentDateFormat.equals("d.m.Y")) {
                paymentDateFormat = "dd.MM.yyyy";
            } else if (paymentDateFormat.equals("Y-m-d")) {
                paymentDateFormat = "yyyy-MM-dd";
            } else {
                paymentDateFormat = "MM/dd/yyyy";
            }
        } else {
            paymentDateFormat = "MM/dd/yyyy";
        }
        return paymentDateFormat;
    }

    private Map<String, String> getLoanScheduleSummaryDetails(Map<String, String> inputParams,
            DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, String> summaryResult = new HashMap<String, String>();
        if (inputParams.containsKey("accountNumber")) {
            inputParams.put("accountID", inputParams.get("accountNumber"));
        }
        JsonObject accountDetails =
                callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.ACCOUNT_DETAILS_GET);
        if (accountDetails.has("Accounts")) {
            JsonArray accountsArray = (JsonArray) accountDetails.get("Accounts");
            if (accountsArray.size() > 0) {
                JsonObject accounts = (JsonObject) accountsArray.get(0);
                if (accounts.has("futureInstallmentsCount") && accounts.get("futureInstallmentsCount") != null) {
                    summaryResult.put("FutureInstallmentsCount", accounts.get("futureInstallmentsCount").getAsString());
                }
                if (accounts.has("paidInstallmentsCount") && accounts.get("paidInstallmentsCount") != null) {
                    summaryResult.put("PaidInstallmentsCount", accounts.get("paidInstallmentsCount").getAsString());
                }
                if (accounts.has("overDueInstallmentsCount") && accounts.get("overDueInstallmentsCount") != null) {
                    summaryResult.put("OverDueInstallmentsCount",
                            accounts.get("overDueInstallmentsCount").getAsString());
                }
            }
        }
        return summaryResult;
    }
    private JsonArray getJsonArray(JsonObject jsonObject, String fieldName) {
        if (jsonObject.has(fieldName) && jsonObject.get(fieldName).isJsonArray()) {
            return jsonObject.getAsJsonArray(fieldName);
        }
        // alert.prepareError("No data for..."+fieldName).log();
        return new JsonArray();
    }
    
    public Map<String, Object> getOtherRequiredData(Map<String, String> inputParams, DataControllerRequest dcRequest)
        throws HttpCallException {
        if ("qfx".equalsIgnoreCase(inputParams.get("fileType"))) {
            return getOtherDataForQFX(inputParams, dcRequest);
        } else if ("qbo".equalsIgnoreCase(inputParams.get("fileType"))) {
            return getOtherDataForQBO(inputParams, dcRequest);
        } else if ("pdf".equalsIgnoreCase(inputParams.get("fileType"))) {
            return getOtherDataForPDF(inputParams, dcRequest);
        } else {
            return getOtherData(inputParams, dcRequest);
        }
    }

    
    private Map<String, Object> getOtherDataForQFX(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("intuitBuid", EnvironmentConfigurationsHandler.getValue("QFX_INTUIT_BUILD", dcRequest));
        otherData.put("financeId", EnvironmentConfigurationsHandler.getValue("QFX_FINANCE_ID", dcRequest));
        otherData.put("orgName", EnvironmentConfigurationsHandler.getValue("QFX_ORG_NAME", dcRequest));
        otherData.put("userName", EnvironmentConfigurationsHandler.getValue("QFX_USER_NAME", dcRequest));
        JsonObject accounts = getAccounts(inputParams, dcRequest);
        if ("0".equals(accounts.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            JsonArray accountList = getJsonArray(accounts, ACCOUNTS_ARRAY);
            otherData.put("accounts", accountList);
        }
        return otherData;
    }

    private Map<String, Object> getOtherDataForQBO(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        otherData.put("intuitBuid", EnvironmentConfigurationsHandler.getValue("QBO_INTUIT_BUILD", dcRequest));
        otherData.put("financeId", EnvironmentConfigurationsHandler.getValue("QBO_FINANCE_ID", dcRequest));
        otherData.put("orgName", EnvironmentConfigurationsHandler.getValue("QBO_ORG_NAME", dcRequest));
        otherData.put("userName", EnvironmentConfigurationsHandler.getValue("QBO_USER_NAME", dcRequest));
        JsonObject accounts = getAccounts(inputParams, dcRequest);
        if ("0".equals(accounts.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString())) {
            JsonArray accountList = getJsonArray(accounts, ACCOUNTS_ARRAY);
            otherData.put("accounts", accountList);
        }
        return otherData;
    }

    private Map<String, Object> getOtherDataForPDF(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        try {
        	
			otherData.put("imgFileName",
			        EnvironmentConfigurationsHandler.getValue(URLConstants.ACCOUNT_STMNT_IMG, dcRequest));
			JsonObject userData = getUser(dcRequest, inputParams);
			if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString()) && userData.has("ExternalUsers")) {
			    JsonObject user = (JsonObject) userData.getAsJsonArray("ExternalUsers").get(0);
			    user.addProperty("account", inputParams.get("accountNumber"));
			    if (inputParams.containsKey("accountName")) {
			        user.addProperty("accountName", inputParams.get("accountName"));
			    }
			    otherData.put("userDetails", user);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert.prepareError(e.getMessage()).log();
		}
        return otherData;
    }

    private Map<String, Object> getOtherData(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, Object> otherData = new HashMap<>();
        JsonObject userData = getUser(dcRequest, inputParams);
        if ("0".equals(userData.get(DBPConstants.FABRIC_OPSTATUS_KEY).getAsString()) && userData.has("ExternalUsers")) {
            JsonObject user = (JsonObject) userData.getAsJsonArray("ExternalUsers").get(0);
            if (HelperMethods.isJsonNotNull(user.get("bankName"))) {
                otherData.put("bankName", user.get("bankName").getAsString());
            }
        }
        if (inputParams.containsKey("accountNumber")) {
          	 otherData.put("accountNumber", inputParams.get("accountNumber"));
        }
        
        if (inputParams.containsKey("accountName")) {
            otherData.put("accountName", inputParams.get("accountName"));
        }
        return otherData;
    }

    public JsonObject getUser(DataControllerRequest dcRequest, Map<String, String> inputParams)
            throws HttpCallException {
        return callApi(dcRequest, null, getHeaders(dcRequest, inputParams), URLConstants.USER_DETAILS);
    }

    private boolean validateFileType(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        String fileType = inputParams.get("fileType");
        boolean status = false;
        if (StringUtils.isNotBlank(fileType)) {
            switch (fileType) {
                case "xls":
                case "xlsx":
                case "csv":
                case "qfx":
                case "qbo":
                case "pdf":
                    status = true;
            }
        }
        if (!status) {
            HelperMethods.setValidationMsg("file type is either empty or not supported", dcRequest, result);
        }
        return status;
    }

    private JsonObject getTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        //  String ARRANGEMENTS_BACKEND = ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists(); 
          
          if(StringUtils.isNotBlank(inputParams.get("transactionId"))) {                            
                    String   transactionDetails = inputParams.get("transactionDetails");
                	JsonObject transactionObject = new JsonObject();
                	JsonObject response = new JsonObject();
                	JsonArray transactionArray = new JsonArray();
                	JsonParser parser = new JsonParser();
                	transactionObject = parser.parse(transactionDetails).getAsJsonObject();
                	transactionArray.add(transactionObject);
                     response.addProperty("opstatus", "0");
                     response.addProperty("httpStatusCode", "0");
                     response.add("Transactions", transactionArray);
                    return response;     
                          
              }
          
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.TRANSACTIONS_POST);
        
    }
    
    private JsonObject getRecentTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
          
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.TRANSACTIONS_RECENT);
    }
    
    private JsonObject getCardTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
          
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.CARD_TRANSACTIONS);
    }
    private JsonObject getBlockedTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
          
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.TRANSACTIONS_BLOCKED);
    }

    private JsonObject getLoanTransactions(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        if (inputParams.containsKey("accountNumber")) {
            inputParams.put("accountID", inputParams.get("accountNumber"));
        }
        inputParams.put("isFutureRequired", "true");
		
		  String ARRANGEMENTS_BACKEND =
		  ServerConfigurations.ARRANGEMENTS_BACKEND.getValueIfExists(); 
		  if(StringUtils.isNotBlank(ARRANGEMENTS_BACKEND)) {
			  if (ARRANGEMENTS_BACKEND.equals("MOCK")) { 
				  Map<String, Object> inputMap = new HashMap<>();
  	            Map<String, Object> headerMap = new HashMap<>();
  	            try {
					/*
					 * String transactionResponse =
					 * Executor.invokePassThroughServiceAndGetString((InfinityServices)
					 * TransactionAdviceAPIServices.LOAN_SCHEDULED_TRANSACTIONS_MOCK, inputMap,
					 * headerMap);
					 */
  	            	String transactionResponse = "";
				  JsonObject transactionObject = new JsonObject();
              	JsonObject response = new JsonObject();
              	JsonArray transactionArray = new JsonArray();
              	JsonParser parser = new JsonParser();
              	transactionObject = parser.parse(transactionResponse).getAsJsonObject();
              	transactionArray.add(transactionObject);
              	response.addProperty("opstatus", "0");
                response.addProperty("httpStatusCode", "0");
              	response.add("Transactions", transactionArray);
              	return response;
				  } 
			  
		  
			  catch (Exception e) {
				  alert.prepareError("Error on reading MOCK Transactions" + e).log();
		        }
		  }
			  }
		  
		 
        return callApi(dcRequest, inputParams, getHeaders(dcRequest, inputParams), URLConstants.LOAN_SCHEDULE_GET);
    
    }

    private JsonObject getAccounts(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("accountID", inputParams.get("accountNumber"));
        Map<String, String> headerMap = getHeaders(dcRequest, inputParams);
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return HelperMethods.callApiJson(dcRequest, input, headerMap, URLConstants.ACCOUNTS_POST);
    }

    public JsonObject callApi(DataControllerRequest dcRequest, Map<String, String> inputParams,
            Map<String, String> headerParams, String url) throws HttpCallException {
        JsonObject jsonResponse = HelperMethods.callApiJson(dcRequest, inputParams, headerParams, url);
        return (null == jsonResponse) ? new JsonObject() : jsonResponse;
    }

    public Map<String, String> getHeaders(DataControllerRequest dcRequest, Map<String, String> inputParams) {
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> reqHeader = dcRequest.getHeaderMap();
        for (Map.Entry<String, Object> entry : reqHeader.entrySet()) {
            headers.put(entry.getKey(), (String) entry.getValue());
        }
        headers.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, inputParams.get("Auth_Token"));
        headers.put(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS,
                dcRequest.getParameter(DBPUtilitiesConstants.X_KONY_REPORTING_PARAMS));
        return headers;
    }

    public Map<String, String> getFieldList(String transactionType) {
        String resourceName = "TransactionsHeader.properties";
        if (StringUtils.isNotBlank(transactionType)
                && transactionType.equals(DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN_SCHEDULE)) {
            resourceName = "LoanShedule.properties";
        }
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
            alert.prepareError("Error on reading TransactionsHeader.properties..." + e).log();
        }
        return fieldList;
    }
    
    private Map<String, String> getBlockedFundsFieldList() {

        Map<String, String> fieldList = new LinkedHashMap<>();
        for(int i=0;i<blockedheaders.length;i++) {
        	fieldList.put(blockedfields[i],blockedheaders[i]);
        }
        return fieldList;
    }
    
    
}
