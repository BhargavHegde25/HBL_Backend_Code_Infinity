package com.temenos.infinity.api.arrangements.javaservice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.resource.impl.ArrangementsResourceImpl;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.invocation.Executor;

public class GetAccounts implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String username = dcRequest.getParameter("userName");
        String did = inputParams.get("deviceID");
        String userId = inputParams.get("userId");
        String customerType = inputParams.get("customerType");
        String accountId = inputParams.get("accountID");
        String mortgageString = null;
        String companyId = CommonUtils.getCompanyId(dcRequest);
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        if(accountId != null) {
        	if(accountId.equalsIgnoreCase("MORT12345") || accountId.equalsIgnoreCase("122877")|| accountId.equalsIgnoreCase("122878")) {
            
            try {          mortgageString = Executor.invokePassThroughServiceAndGetString((InfinityServices)ArrangementsAPIServices.MOCKMORTGAGEMS_GETMORTGAGEDETAILS, inputMap, headerMap);
            alert.prepareError("AMS Response getDetails" + mortgageString).log();
          } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
          } 
          JSONArray mortgages = new JSONArray(mortgageString);
          List<ArrangementsDTO> mDTOs = new ArrayList<>();
          ArrangementsDTO mDTO = new ArrangementsDTO();
          JsonObject priAccHolder = new JsonObject();
          for(int i=0;i<mortgages.length();i++) {
        	  JSONObject mortgage = mortgages.getJSONObject(i);
        	  if(mortgage.get("accountID").equals(accountId)) {
        		  String accHolderString = mortgage.get("accountHolder").toString();
        		  JsonObject accHolder = new JsonParser().parse(accHolderString).getAsJsonObject();
        		  priAccHolder.addProperty("username", accHolder.has("username") ? accHolder.get("username").getAsString() :"");
                  priAccHolder.addProperty("fullname", accHolder.has("fullname") ? accHolder.get("fullname").getAsString() :"");
                  mDTO.setAccountHolder(priAccHolder.toString());
        		  mDTO.setAccount_id(mortgage.has("accountID") ? mortgage.get("accountID").toString() : "");
        		  mDTO.setIBAN(mortgage.has("IBAN") ? mortgage.get("IBAN").toString() : "");
        		  mDTO.setOriginalAmount(mortgage.has("originalAmount") ? mortgage.get("originalAmount").toString() : "");
        		  mDTO.setNextPaymentAmount(mortgage.has("nextPaymentAmount") ? mortgage.get("nextPaymentAmount").toString() : "");
        		  mDTO.setPaymentDue(mortgage.has("NextPaymentDue") ? mortgage.get("NextPaymentDue").toString() : "");
        		  mDTO.setOutstandingBalance(mortgage.has("outstandingBalance") ? mortgage.get("outstandingBalance").toString() : "");
        		  mDTO.setPaidInstallmentsCount(mortgage.has("paidInstallmentsCount") ? mortgage.get("paidInstallmentsCount").toString() : "");
        		  mDTO.setOverDueInstallmentsCount(mortgage.has("overDueInstallmentsCount") ? mortgage.get("overDueInstallmentsCount").toString() : "");
        		  mDTO.setFutureInstallmentsCount(mortgage.has("futureInstallmentsCount") ? mortgage.get("futureInstallmentsCount").toString() : "");
        		  mDTO.setInterestPaidYTD(mortgage.has("interestPaidYTD") ? mortgage.get("interestPaidYTD").toString() : "");
        		  mDTO.setLastPaymentAmount(mortgage.has("lastPaymentAmount") ? mortgage.get("lastPaymentAmount").toString() : "");
        		  mDTO.setLastPaymentDate(mortgage.has("lastPaymentDate") ? mortgage.get("lastPaymentDate").toString() : "");
        		  mDTO.setRePaymentFrequency(mortgage.has("rePaymentFrequency") ? mortgage.get("rePaymentFrequency").toString() : "");
        		  mDTO.setInterestRate(mortgage.has("interestRate") ? mortgage.get("interestRate").toString() : "");
        		  mDTO.setSanctionedDate(mortgage.has("sanctionedDate") ? mortgage.get("sanctionedDate").toString() : "");
        		  mDTO.setMaturityDate(mortgage.has("maturityDate") ? mortgage.get("maturityDate").toString() : "");
        		  mDTO.setTermAmount(mortgage.has("sanctionedAmount") ? mortgage.get("sanctionedAmount").toString() : "");
        		  mDTO.setAccountType(mortgage.has("accountType") ? mortgage.get("accountType").toString() : "");
        		  mDTO.setTypeDescription(mortgage.has("accountType") ? mortgage.get("accountType").toString() : "");

        		  mDTO.setAccountName(mortgage.has("accountName") ? mortgage.get("accountName").toString() : "");
        		  mDTO.setArrangementId(mortgage.has("arrangementId") ? mortgage.get("arrangementId").toString() : "");
        		  mDTO.setBankname(mortgage.has("bankName") ? mortgage.get("bankName").toString() : "");
        		  mDTO.setCompanyCode(mortgage.has("companyId") ? mortgage.get("companyId").toString() : "");
        		  mDTO.setCurrencyCode(mortgage.has("currencyCode") ? mortgage.get("currencyCode").toString() : "");
        		  mDTO.setOpeningDate(mortgage.has("openingDate") ? mortgage.get("openingDate").toString() : "");
        		  mDTO.setDisplayName(mortgage.has("displayName") ? mortgage.get("displayName").toString() : "");
        		  mDTO.setExternalIndicator(mortgage.has("externalIndicator") ? mortgage.get("externalIndicator").toString() : "");
        		  mDTO.setNickName(mortgage.has("nickName") ? mortgage.get("nickName").toString() : "");
        		  mDTO.setPrincipalValue(mortgage.has("principalValue") ? mortgage.get("principalValue").toString() : "");
        		  mDTO.setProcessingTime(mortgage.has("processingTime") ? mortgage.get("processingTime").toString() : "");
        		  mDTO.setProduct(mortgage.has("productId") ? mortgage.get("productId").toString() : "");
        		  mDTO.setStatusDesc(mortgage.has("statusDesc") ? mortgage.get("statusDesc").toString() : "");
        		  mDTO.setSupportBillPay(mortgage.has("supportBillPay") ? mortgage.get("supportBillPay").toString() : "");
        		  mDTO.setSupportChecks(mortgage.has("supportChecks") ? mortgage.get("supportChecks").toString() : "");
        		  mDTO.setSupportTransferFrom(mortgage.has("supportTransferFrom") ? mortgage.get("supportTransferFrom").toString() : "");
        		  mDTO.setSupportTransferTo(mortgage.has("supportTransferTo") ? mortgage.get("supportTransferTo").toString() : "");
        		  mDTO.setAvailableBalance(mortgage.has("availableBalance") ? Double.valueOf(mortgage.get("availableBalance").toString()).doubleValue() : 0.0);
        		  mDTO.setCurrentBalance(mortgage.has("currentBalance") ? Double.valueOf(mortgage.get("currentBalance").toString()).doubleValue() : 0.0);
        		  mDTO.setDividendLastPaidAmount(mortgage.has("dividendLastPaidAmount") ? Double.valueOf(mortgage.get("dividendLastPaidAmount").toString()).doubleValue() : 0.0);
        		  mDTO.setDividendPaidYTD(mortgage.has("dividendPaidYTD") ? Double.valueOf(mortgage.get("dividendPaidYTD").toString()).doubleValue() : 0.0);
        		  mDTO.setPendingDeposit(mortgage.has("pendingDeposit") ? Double.valueOf(mortgage.get("pendingDeposit").toString()).doubleValue() : 0.0);
        		  mDTO.setPendingWithdrawal(mortgage.has("pendingWithdrawal") ? Double.valueOf(mortgage.get("pendingWithdrawal").toString()).doubleValue() : 0.0);
        		  mDTOs.add(mDTO);
        	  }
          }
        	JSONArray accountsDTOArray = new JSONArray(mDTOs);

            JSONObject responseObj = new JSONObject();
            responseObj.put("Accounts", accountsDTOArray);
            result = JSONToResult.convert(responseObj.toString());
            return result;
            }
        }
        
       /* List<ArrangementsDTO> accountsDTO = new ArrayList<ArrangementsDTO>();
        String mockMortgageResponse = ServerConfigurations.MOCK_MORTGAGE_RESPONSE.getValue();
        if(mockMortgageResponse != null && mockMortgageResponse.equalsIgnoreCase("Yes")) {
        	List<ArrangementsDTO> mortgageDTOs = getMortgages();
        	accountsDTO.addAll(mortgageDTOs);
        	JSONArray accountsDTOArray = new JSONArray(accountsDTO);

            JSONObject responseObj =
                    new JSONObject();
            responseObj.put("Accounts", accountsDTOArray);
            result = JSONToResult.convert(responseObj.toString());
            alert.prepareError("Accounts  " + accountsDTOArray.toString()).log();
            return result;
        }*/
       

        if (StringUtils.isBlank(userId) && StringUtils.isNotBlank(username)) {
            Result userResult = getUserId(dcRequest, username);
            userId = HelperMethods.getFieldValue(userResult, "id");
            customerType = HelperMethods.getFieldValue(userResult, "CustomerType_id");
            inputParams.put("userId", userId);
            inputParams.put("customerType", customerType);
        }
        if (StringUtils.isNotBlank(did) && StringUtils.isNotBlank(username)) {
            if (!isDeviceRegistered(dcRequest, did, userId)) {
                Dataset ds = new Dataset("Accounts");
                result.addDataset(ds);
                return result;
            }
        }

        Result newResult = new Result();
        Result  mockreslt = new Result();
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERACCOUNTSVIEW_GET);
            Dataset accountsFromDB = result.getDatasetById("customeraccountsview");
            List<ArrangementsDTO> accountsDTO = new ArrayList<ArrangementsDTO>();
            String mockMortgageResponse = ServerConfigurations.MOCK_MORTGAGE_RESPONSE.getValue();
           
            if(accountId == null && mockMortgageResponse != null && mockMortgageResponse.equalsIgnoreCase("Yes")) {
            	List<ArrangementsDTO> mortgageDTOs = getMortgages();
            	accountsDTO.addAll(mortgageDTOs);
            	JSONArray accountsDTOArray = new JSONArray(accountsDTO);
            	JSONObject responseObj =
                        new JSONObject();
                responseObj.put("MortgageAccounts", accountsDTOArray);
                mockreslt = JSONToResult.convert(responseObj.toString());
            	 //mockreslt = JSONToResult.convert(accountsDTOArray.toString());
                Dataset mockFromDB = mockreslt.getDatasetById("MortgageAccounts");
                List<Record> mockrcords = mockFromDB.getAllRecords();
				/*
				 * List<Record> mockrcordsnew = new ArrayList<Record>(); for(int i= 0;
				 * i<mockrcords.size(); i++) { Record mr = mockrcords.get(i);
				 * mr.addParam("supportCardlessCash", "1"); mockrcordsnew.add(mr); }
				 */
                List<Record> totalrecords = new ArrayList<Record>();
                List<Record> customrecords = accountsFromDB.getAllRecords();
                totalrecords.addAll(customrecords);
                totalrecords.addAll(mockrcords);
                Dataset ds = new Dataset("Accounts");
                ds.addAllRecords(totalrecords);
                
                newResult.addDataset(ds);
                result = newResult;
              
        }
        }
        if (HelperMethods.hasRecords(result)) {
        	
            result.getAllDatasets().get(0).setId("Accounts");
            postProcess(result, dcRequest);
        }
        if (!HelperMethods.hasRecords(result)) {
            Dataset ds = new Dataset();
            ds.setId("Accounts");
            result.addDataset(ds);
        }
        return result;
    }

    private boolean isDeviceRegistered(DataControllerRequest dcRequest, String deviceId, String userId)
            throws HttpCallException {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId).append(DBPUtilitiesConstants.AND)
                .append("Device_id").append(DBPUtilitiesConstants.EQUAL).append(deviceId)
                .append(DBPUtilitiesConstants.AND).append("Status_id").append(DBPUtilitiesConstants.EQUAL)
                .append("SID_DEVICE_REGISTERED");
        Result device = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
                URLConstants.DEVICEREGISTRATION_GET);
        return HelperMethods.hasRecords(device);
    }

    private void postProcess(Result result, DataControllerRequest dcRequest) throws HttpCallException {
        List<Record> accounts = result.getAllDatasets().get(0).getAllRecords();
        for (Record account : accounts) {
            updateDateFormat(account);
            account.addParam(new Param("rePaymentFrequency", "Monthly", DBPUtilitiesConstants.STRING_TYPE));
            String accountType = account.getParamValueByName("typeDescription");
            String dueDate = account.getParamValueByName("dueDate");
            account.addParam(new Param("nextPaymentDate", dueDate, DBPUtilitiesConstants.STRING_TYPE));
            String accountId = account.getParamValueByName("Account_id");
            String unpaidInterest = account.getParamValueByName("unpaidInterest");
       	 account.addParam(new Param("accruedInterest", unpaidInterest, DBPUtilitiesConstants.STRING_TYPE));
            if (StringUtils.isNotBlank(accountType) && accountType.equals("Loan")
                    && StringUtils.isNotBlank(accountId)) {
            	 account.addParam(new Param("futureInstallmentsCount", "59", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("nextPaymentAmount", "0", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("nextPaymentDate", "2022-05-01", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("overDueInstallmentsCount", "0", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("paidInstallmentsCount", "2", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("product", "MORTGAGE", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("termAmount", "26000", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("sanctionedDate", "2022-03-01", DBPUtilitiesConstants.STRING_TYPE));
					/*
					 * String unpaidInterest = account.getParamValueByName("unpaidInterest");
					 * account.addParam(new Param("accruedInterest", unpaidInterest,
					 * DBPUtilitiesConstants.STRING_TYPE));
					 */
                updateAccountwithInstallmentCount(account, accountId, dcRequest);
            }
            if (StringUtils.isNotBlank(accountType) && accountType.equals("Checking")
                    && StringUtils.isNotBlank(accountId)) {
            	 account.addParam(new Param("arrangementId", "AA221099YC3H", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("blockedAmount", "203.3", DBPUtilitiesConstants.STRING_TYPE));
            	 account.addParam(new Param("product", "CURRENT.ACCOUNT", DBPUtilitiesConstants.STRING_TYPE));
            }
            if (StringUtils.isNotBlank(accountType) && accountType.equals("Savings")
                    && StringUtils.isNotBlank(accountId)) {
            	account.addParam(new Param("arrangementId", "AA22109DHPGK", DBPUtilitiesConstants.STRING_TYPE));
           	 account.addParam(new Param("blockedAmount", "0", DBPUtilitiesConstants.STRING_TYPE));
           	 account.addParam(new Param("product", "SAVINGS.ACCOUNT", DBPUtilitiesConstants.STRING_TYPE));
            }
            String creditcardNumber = account.getParamValueByName("creditCardNumber");
            if (StringUtils.isNotBlank(creditcardNumber)) {
                creditcardNumber = getMaskedValue(creditcardNumber);
                account.addParam(new Param("creditCardNumber", creditcardNumber, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }


    private String getMaskedValue(String creditcardNumber) {
        String lastFourDigits;
        if (StringUtils.isNotBlank(creditcardNumber)) {
            if (creditcardNumber.length() > 4) {
                lastFourDigits = creditcardNumber.substring(creditcardNumber.length() - 4);
                creditcardNumber = "XXXX" + lastFourDigits;
            } else {
                creditcardNumber = "XXXX" + creditcardNumber;
            }
        }

        return creditcardNumber;
    }

    public void updateAccountwithInstallmentCount(Record account, String accountId, DataControllerRequest dcRequest)
            throws HttpCallException {
        Result installmentsCountResult = new Result();
        Map<String, String> countInputParams = new HashMap<String, String>();
        countInputParams.put("transactions_query", getInstallmentsCountQuery(accountId, dcRequest));
        installmentsCountResult =
                HelperMethods.callApi(dcRequest, countInputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_TRANSACTION_PROC);
        if (installmentsCountResult.getAllDatasets().size() > 0) {
            List<Record> records = installmentsCountResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : records) {
                if (record.getParamValueByName("InstallmentType").equalsIgnoreCase(DBPUtilitiesConstants.FUTURE)) {
                    account.addParam(new Param("FutureInstallmentsCount", record.getParamValueByName("count")));
                }
                if (record.getParamValueByName("InstallmentType").equalsIgnoreCase(DBPUtilitiesConstants.DUE)) {
                    account.addParam(new Param("OverDueInstallmentsCount", record.getParamValueByName("count")));
                }
                if (record.getParamValueByName("InstallmentType").equalsIgnoreCase(DBPUtilitiesConstants.PAID)) {
                    account.addParam(new Param("PaidInstallmentsCount", record.getParamValueByName("count")));
                }
            }
        }
    }

    public String getInstallmentsCountQuery(String accountId, DataControllerRequest dcRequest) {
        String jdbcUrl = QueryFormer.getDBType(dcRequest);
        return SqlQueryEnum.valueOf(jdbcUrl + "_GetInstallmentsCountQuery").getQuery()
                .replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", accountId);

    }

    private void updateDateFormat(Record account) {
        try {
            String scheduledDate = HelperMethods.getFieldValue(account, "dueDate");
            if (StringUtils.isNotBlank(scheduledDate)) {
                account.addParam(new Param("dueDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            String date = HelperMethods.getFieldValue(account, "closingDate");
            if (StringUtils.isNotBlank(date)) {
                account.addParam(new Param("closingDate",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            date = HelperMethods.getFieldValue(account, "openingDate");
            if (StringUtils.isNotBlank(date)) {
                account.addParam(new Param("openingDate",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'HH:mm:ss"), "String"));
            }
            date = HelperMethods.getFieldValue(account, "lastPaymentDate");
            if (StringUtils.isNotBlank(date)) {
                account.addParam(new Param("lastPaymentDate",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'HH:mm:ss"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String accountId = inputParams.get("accountID");
        inputParams.get("userName");
        String deviceId = inputParams.get("deviceID");
        String phone = inputParams.get("phone");
        String userId = inputParams.get("userId");
        String legalEntityId = inputParams.get("legalEntityId");
        String filter = "";
        if (StringUtils.isNotBlank(deviceId)) {
            filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isBlank(accountId)) {
            filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
            if (StringUtils.isNotBlank(phone)) {
                filter = filter + DBPUtilitiesConstants.AND + "phone" + DBPUtilitiesConstants.EQUAL + phone;
            }
            if (!HelperMethods.getCustomerTypes().get("Customer").equals(inputParams.get("customerType"))) {
                filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
            }
        } else {
            if (StringUtils.isNotBlank(userId)) {
                filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
                if (!HelperMethods.getCustomerTypes().get("Customer").equals(inputParams.get("customerType"))) {
                    filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
                }
                if (StringUtils.isNotBlank(legalEntityId)) {
                	filter += DBPUtilitiesConstants.AND;
                    filter += "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
                }
                filter += DBPUtilitiesConstants.AND;
            }

            filter += "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        }
        // inputParams.clear();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        inputParams.put(DBPUtilitiesConstants.ORDERBY, "accountPreference asc");

        return status;
    }

    private Result getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERVERIFY_GET);
        return user;
    }
    private List<ArrangementsDTO> getMortgages() {
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        String mortgageString = null;
        try {          mortgageString = Executor.invokePassThroughServiceAndGetString((InfinityServices)ArrangementsAPIServices.MOCKMORTGAGEMS_FETCHMORTGAGEACCOUNT, inputMap, headerMap);
          alert.prepareError("AMS Response" + mortgageString).log();
        } catch (Exception e) {
          alert.prepareError("Unable to fetch Arrangements " + e).log();
        } 

        List<ArrangementsDTO> mortgagesDTOs = new ArrayList<>();
        JSONArray mortgages = new JSONArray(mortgageString);
        for(int i=0;i<mortgages.length();i++) {
        JSONObject mortgage = mortgages.getJSONObject(i);
        ArrangementsDTO mortgageDTO = new ArrangementsDTO();
        String companyId = mortgage.has("company") ? mortgage.getJSONObject("company").getString("companyReference") : "";
        String aid = mortgage.has("linkedReference") ? mortgage.getString("linkedReference") : "";
        String accountId = aid.replace(companyId + "-", "");
        mortgageDTO.setAccount_id(accountId);
        mortgageDTO.setAccountType("mortgageFacility");
        mortgageDTO.setTypeDescription("mortgageFacility");
        mortgageDTO.setCompanyCode(companyId);
        mortgageDTO.setCurrencyCode(mortgage.has("currency") ? mortgage.getString("currency") : "");
        mortgageDTO.setProductGroup(mortgage.has("productGroup") ? mortgage.getString("productGroup") : "");
        mortgageDTO.setProduct(mortgage.has("product") ? mortgage.getString("product") : "");
        mortgageDTO.setArrangementId(accountId);
        mortgageDTO.setServiceType(mortgage.has("productDescription") ? mortgage.getString("productDescription") : "");
        mortgageDTO.setAccountName(mortgage.has("shortTitle") ? mortgage.getString("shortTitle") : "");
        mortgageDTO.setPartyRole("Owner");
        mortgageDTO.setOutstandingBalance(mortgage.has("outstandingBalance") ? mortgage.getString("outstandingBalance") : "");
        mortgageDTO.setProcessingTime(mortgage.has("processDate") ? mortgage.getString("processDate"):"");
        mortgageDTO.setNickName(mortgage.has("shortTitle") ? mortgage.getString("shortTitle") : "");
        mortgagesDTOs.add(mortgageDTO);
        }
        return mortgagesDTOs;
      }
}
