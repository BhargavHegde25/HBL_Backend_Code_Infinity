package com.temenos.infinity.api.holdings.resource.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.kony.dbputilities.util.TransactionsCountProperties;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.holdings.businessdelegate.api.AccountTransactionsBusinessDelegate;
import com.temenos.infinity.api.holdings.config.HoldingsAPIServices;
import com.temenos.infinity.api.holdings.constants.ErrorCodeEnum;
import com.temenos.infinity.api.holdings.constants.TemenosConstants;
import com.temenos.infinity.api.holdings.dto.AccountTransactionsDTO;
import com.temenos.infinity.api.holdings.resource.api.AccountTransactionsResource;
import com.temenos.infinity.api.holdings.util.HoldingsUtils;
import com.temenos.infinity.api.holdings.util.TransactionTypeProperties;

/**
 * 
 * @author KH2281
 * @version 1.0 Extends the {@link AccountTransactionsResource}
 */
public class AccountTransactionsResourceImpl implements AccountTransactionsResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getAccountTransactions(String order, String offset, String limit, String accountID,
            String transactionType, String sortBy, String authToken, DataControllerRequest request) throws Exception {

        AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountTransactionsBusinessDelegate.class);
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20045.setErrorCode(new Result());
        }
        Result result = new Result();
        int postedTransactionsLimit=10;
        try {
			TransactionsCountProperties transactionsCountProperties = new TransactionsCountProperties(request,"Posted"); 
			postedTransactionsLimit =Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert.prepareError(e.getMessage()).log();
		}
        
        
        // Set Request Parameters
        AccountTransactionsDTO inputPayLoad = new AccountTransactionsDTO();
        inputPayLoad.setOrder(order);
        inputPayLoad.setOffset(offset);
       // inputPayLoad.setLimit(limit);
        inputPayLoad.setLimit(Integer.toString(postedTransactionsLimit));
        inputPayLoad.setAccountId(accountID);
        inputPayLoad.setTransactionType(transactionType);
        inputPayLoad.setSortBy(sortBy);
        alert.prepareError("Posted Transactions Input : " + inputPayLoad.toString()).log();
        // Invoke and get the Account Transaction details from MicroServices
        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                .getDetailsFromHoldingsMicroService(inputPayLoad, authToken);
        JSONObject responseObj = new JSONObject();
        if (accountTransactionsDTO == null) {
            JSONArray NoRecords = new JSONArray();
            responseObj.put("accountransactionview", NoRecords);
            result = JSONToResult.convert(responseObj.toString());
            result.getAllDatasets().get(0).setId("records");
            return result;
        }
        responseObj.put("accountransactionview", accountTransactionsDTO);
        result = JSONToResult.convert(responseObj.toString());
        result.getAllDatasets().get(0).setId("records");
        return result;
    }

    @Override
    public Result searchAccountTransactions(Map<String, Object> inputParams, DataControllerRequest request,String authToken)
            throws Exception {
        Result result = new Result();
        String accountID = "";
        String offset = "";
        String limit = "";
        String transactionType = "";
        String searchMinAmount = "";
        String searchMaxAmount = "";
        String searchStartDate = "";
        String searchEndDate = "";
        String searchDescription = "";
        String searchType = "";
        String transactionId = "";
        String companyId = "";
        int firstRec = 0;
        int lastRec;
        int noOfRecords = 0;
        diagnostic.debug("searchAccountTransactions1 Inside ###");
        AccountTransactionsDTO inputPayLoad = new AccountTransactionsDTO();
        AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountTransactionsBusinessDelegate.class);
        
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20045.setErrorCode(new Result());
        }

        if (inputParams.containsKey("accountID") && inputParams.get("accountID") != null
                && StringUtils.isNotBlank(inputParams.get("accountID").toString())) {
            accountID = inputParams.get("accountID").toString();
        } else if (inputParams.containsKey("accountNumber") && inputParams.get("accountNumber") != null
                && StringUtils.isNotBlank(inputParams.get("accountNumber").toString())) {
            accountID = inputParams.get("accountNumber").toString();
        } else {
            return ErrorCodeEnum.ERR_20044.setErrorCode(new Result());
        }
        if (inputParams.containsKey("companyId") && inputParams.get("companyId") != null) {
            companyId = inputParams.get("companyId").toString();
        }
        if (inputParams.containsKey("requestType") && inputParams.get("requestType") != null) {
        	searchType = inputParams.get("requestType").toString();
        }
        diagnostic.debug("searchAccountTransactions2 Inside ###");
        String AccIdWithCompanyId = accountID;
        if (accountID != null && !accountID.contains("-")) {
            if (StringUtils.isNotBlank(companyId)) {
                AccIdWithCompanyId = companyId + "-" + accountID;
            } else {
                try {
                    if (accountID.contains("-")) {
                        AccIdWithCompanyId = accountID;
                    } else {
                        AccIdWithCompanyId = HoldingsUtils.getAccountIdWithCompanyFromCache(accountID, request);
                    }
                }  catch (Exception e) {
                    alert.prepareError("Unable to get account with company id from cache " + e).log();
                    return ErrorCodeEnum.ERR_20046.setErrorCode(new Result());
                }
            }
        }

        /*
         * try { AccIdWithCompanyId =
         * HoldingsUtils.getAccountIdWithCompanyFromCache(accountID, request); } catch
         * (Exception e) { alert.prepareError("Unable to get account with company id from cache "
         * + e).log(); return ErrorCodeEnum.ERR_20046.setErrorCode(new Result()); }
         */
        inputPayLoad.setAccountId(AccIdWithCompanyId);

        if (inputParams.containsKey("transactionId") && inputParams.get("transactionId") != null
                && StringUtils.isNotBlank(inputParams.get("transactionId").toString())) {
            transactionId = inputParams.get("transactionId").toString();
            inputPayLoad.setTransactionId(transactionId);
            inputPayLoad.setSearchType(searchType);
            List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                    .getSearchDetailsFromHoldingsMicroService(inputPayLoad,authToken);

            result = AccoutDTOtoResult(accountTransactionsDTO);
            return result;
        } else {
            inputPayLoad.setTransactionId(transactionId);
        }
        diagnostic.debug("searchAccountTransactions3 Inside ###");
        String requestType = "";
        if(inputParams.containsKey("requestType") && inputParams.get("requestType") != null) {
         requestType = inputParams.get("requestType").toString();
        }

       
        if (inputParams.containsKey("offset") && inputParams.get("offset") != null) {
            offset = inputParams.get("offset").toString();
        }
        if(requestType != null && requestType.equals("search")) {
        	if (inputParams.containsKey("offset") && inputParams.get("offset") != null) {
                offset = inputParams.get("offset").toString();
            }
        	else {
        	offset = "1";	
        	}
        }
        
        if(requestType != null && requestType.equals("search")) {
        	 int perPageTransactionsLimit = 10;
        	 try {
				TransactionsCountProperties transactionsCountProperties = new TransactionsCountProperties(request,"perPage"); 
				 perPageTransactionsLimit =Integer.parseInt(TransactionsCountProperties.getValue("config_value"));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.getMessage()).log();
			}
   		  limit = Integer.toString(perPageTransactionsLimit);
        }
        else if (inputParams.containsKey("limit") && inputParams.get("limit") != null) {
            limit = inputParams.get("limit").toString();
        }
        diagnostic.debug("searchAccountTransactions4 Inside ###");
   /*     if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
            firstRec = Integer.parseInt(offset);
            lastRec = Integer.parseInt(limit);
            noOfRecords = lastRec - firstRec + 1;
            firstRec = firstRec / noOfRecords + 1;
        }*/
        // TODO
        // Making transaction type empty always as ms is not supporting
        // transaction type.
        //  searchTransactionType
          if (inputParams.containsKey("searchTransactionType") &&
          inputParams.get("searchTransactionType") != null) { transactionType =
         inputParams.get("searchTransactionType").toString(); transactionType =
          getTransactionType(transactionType); }
         
        if (inputParams.containsKey("searchMinAmount") && inputParams.get("searchMinAmount") != null) {
            searchMinAmount = inputParams.get("searchMinAmount").toString();
        }
        if (inputParams.containsKey("searchMaxAmount") && inputParams.get("searchMaxAmount") != null) {
            searchMaxAmount = inputParams.get("searchMaxAmount").toString();
        }

        if (inputParams.containsKey("searchStartDate") && inputParams.get("searchStartDate") != null) {
            searchStartDate = inputParams.get("searchStartDate").toString();
        }

        if (inputParams.containsKey("searchEndDate") && inputParams.get("searchEndDate") != null) {
            searchEndDate = inputParams.get("searchEndDate").toString();
        }
        if (inputParams.containsKey("searchDescription") && inputParams.get("searchDescription") != null) {
            searchDescription = inputParams.get("searchDescription").toString();
        }
        if (inputParams.containsKey("searchType") && inputParams.get("searchType") != null) {
            searchType = inputParams.get("searchType").toString();
        }
        diagnostic.debug("searchAccountTransactions5 Inside ###");
        inputPayLoad.setTransactionType(transactionType);
        inputPayLoad.setOffset(String.valueOf(offset));
        inputPayLoad.setLimit(String.valueOf(limit));
        inputPayLoad.setSearchMinAmount(searchMinAmount);
        inputPayLoad.setSearchMaxAmount(searchMaxAmount);
        inputPayLoad.setSearchDescription(searchDescription);
        inputPayLoad.setSearchType(searchType);

        if (StringUtils.isBlank(searchStartDate)) {
            String noOfDays = TemenosConstants.NO_OF_DAYS;
            searchStartDate = getMinusDays(noOfDays);
        }
        inputPayLoad.setSearchStartDate(searchStartDate);
        inputPayLoad.setSearchEndDate(searchEndDate);
        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                .getSearchDetailsFromHoldingsMicroService(inputPayLoad,authToken);
        diagnostic.debug("searchAccountTransactions6 Inside ###");
        result = AccoutDTOtoResult(accountTransactionsDTO);
        diagnostic.debug("searchAccountTransactions7 Inside ###"+ ResultToJSON.convert(result));
        return result;

    }

    public String getTransactionType(String transactionType) {
        return TransactionTypeProperties.getCategorizationId(transactionType);
    }

    public static Result AccoutDTOtoResult(List<AccountTransactionsDTO> accountTransactionsDTO) {
        JSONObject responseObj = new JSONObject();
        Result result = new Result();
        if (accountTransactionsDTO == null) {
            JSONArray NoRecords = new JSONArray();
            responseObj.put("Transactions", NoRecords);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        }

        responseObj.put("Transactions", accountTransactionsDTO);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    public static String getMinusDays(String noOfDays) {
        String fromDate = "";
        if (StringUtils.isNotBlank(noOfDays) && StringUtils.isNumeric(noOfDays)) {
            int numberOfDays = Integer.parseInt(noOfDays);
            LocalDate currentDate = LocalDate.now();
            LocalDate minusDays = currentDate.minusDays(numberOfDays);
            fromDate = minusDays.toString();
        }
        return fromDate;
    }

    @Override
    public Result getAccountPendingTransactions(String accountID, String searchStartDate, DataControllerRequest request)
            throws ApplicationException {
        AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountTransactionsBusinessDelegate.class);

        Result result = new Result();

        // Set Request Parameters
        AccountTransactionsDTO inputPayLoad = new AccountTransactionsDTO();
        if (accountID.contains("-")) {
            accountID = accountID.split("-")[1];
        }
        inputPayLoad.setAccountId(accountID);
        inputPayLoad.setSearchStartDate(searchStartDate);
        // Invoke and get the Account Transaction details from MicroServices
        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                .getPendingTransactionsDetailsFromT24(inputPayLoad, request);
        JSONObject responseObj = new JSONObject();
        if (accountTransactionsDTO == null) {
            JSONArray NoRecords = new JSONArray();
            responseObj.put("accountransactionview", NoRecords);
            result = JSONToResult.convert(responseObj.toString());
            result.getAllDatasets().get(0).setId("records");
            return result;
        }
        responseObj.put("accountransactionview", accountTransactionsDTO);
        result = JSONToResult.convert(responseObj.toString());
        result.getAllDatasets().get(0).setId("records");
        return result;
    }

    @Override
    public Result getAccountPendingAndPostedTransactions(String order, String offset, String limit, String accountID,
            String transactionType, String sortBy, String searchStartDate, DataControllerRequest request,
            String authToken) throws DBPApplicationException, Exception {
        HashMap<String, String> requestMap = new HashMap<String, String>();
        JSONObject responseObj = new JSONObject();
        Result result = new Result();
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20045.setErrorCode(new Result());
        }
        
        Map<String, Object> inputMap = new HashMap<>();
        String mortgageTransactions = null;
        
   	 List<AccountTransactionsDTO> mortgageTransactionsDTOList = new ArrayList<AccountTransactionsDTO>();

        
		/*
		 * if(accountID.equalsIgnoreCase("MORT12345") ||
		 * accountID.equalsIgnoreCase("122877")|| accountID.equalsIgnoreCase("122878"))
		 * { Map<String, Object> headerMap = new HashMap<>(); try { mortgageTransactions
		 * = Executor.invokePassThroughServiceAndGetString((InfinityServices)
		 * HoldingsAPIServices.MOCKMORTGAGEMS_GETPOSTEDANDPENDINGTARNSACTIONS, inputMap,
		 * headerMap); alert.prepareError("AMS Response" +mortgageTransactions).log(); } catch
		 * (Exception e) { alert.prepareError("Unable to fetch mortgageTransactions " + e).log(); }
		 * HashMap<String, List<AccountTransactionsDTO>> accounts = new HashMap<>();
		 * AccountTransactionsDTO mDTO = null; JSONArray mortgages = new
		 * JSONArray(mortgageTransactions); for(int i = 0; i<mortgages.length();i++) {
		 * JSONObject mortgage = mortgages.getJSONObject(i); mDTO = new
		 * AccountTransactionsDTO(); String aid = mortgage.has("fromAccountNumber") ?
		 * mortgage.getString("fromAccountNumber") : ""; String[] accountIdArray =
		 * aid.split("-"); String accountIdResponse = accountIdArray[1];
		 * if(accountIdResponse != null && accountIdResponse.equals(accountID)) {
		 * mDTO.setAccountId(accountIdResponse); mDTO.setId(mortgage.has("Id") ?
		 * mortgage.get("Id").toString() : ""); mDTO.setDate(mortgage.has("postedDate")
		 * ? mortgage.get("postedDate").toString() : "");
		 * mDTO.setDescription(mortgage.has("description") ?
		 * mortgage.get("description").toString() : "");
		 * mDTO.setTransactionType(mortgage.has("transactionType") ?
		 * mortgage.get("transactionType").toString() : "");
		 * mDTO.setAmount(mortgage.has("amount") ?
		 * Double.valueOf(mortgage.get("amount").toString()).doubleValue(): 0.0);
		 * mDTO.setFromAccountBalance(mortgage.has("fromAccountBalance") ?
		 * Double.valueOf(mortgage.get("fromAccountBalance").toString()).doubleValue() :
		 * 0.0); mDTO.setFromAccountNumber(mortgage.has("fromAccountNumber") ?
		 * mortgage.get("fromAccountNumber").toString() : "");
		 * mDTO.setIsScheduled(mortgage.has("isScheduled") ?
		 * mortgage.get("isScheduled").toString() : "");
		 * mDTO.setPayeeCurrency(mortgage.has("payeeCurrency") ?
		 * mortgage.get("payeeCurrency").toString() : "");
		 * mDTO.setPostedDate(mortgage.has("postedDate") ?
		 * mortgage.get("postedDate").toString() : "");
		 * mDTO.setScheduledDate(mortgage.has("scheduledDate") ?
		 * mortgage.get("scheduledDate").toString() : "");
		 * mDTO.setStatusDesc(mortgage.has("statusDescription") ?
		 * mortgage.get("statusDescription").toString() : "");
		 * mDTO.setTransactionCurrency(mortgage.has("transactionDate") ?
		 * mortgage.get("transactionDate").toString() : "");
		 * mDTO.setNotes(mortgage.has("transactionsNotes") ?
		 * mortgage.get("transactionsNotes").toString() : "");
		 * mDTO.setTransactionId(mortgage.has("Id") ? mortgage.get("Id").toString() :
		 * ""); mDTO.setTransaction_Id(mortgage.has("transactionId") ?
		 * mortgage.get("transactionId").toString() : "");
		 * mDTO.setTransactionDate(mortgage.has("transactionDate") ?
		 * mortgage.get("transactionDate").toString() : "");
		 * mDTO.setInstallmentType(mortgage.has("installmentType") ?
		 * mortgage.get("installmentType").toString() : "");
		 * mortgageTransactionsDTOList.add(mDTO); } } accounts.put("Transactions",
		 * mortgageTransactionsDTOList); Gson gson = new Gson(); String
		 * mortgageTransactionsgson = gson.toJson(accounts);
		 * CommonUtils.insertIntoSession("MortgageTransactions",
		 * mortgageTransactionsgson,request);
		 * 
		 * JSONArray accountsDTOArray = new JSONArray(mortgageTransactionsDTOList);
		 * responseObj.put("accountransactionview", accountsDTOArray); result =
		 * JSONToResult.convert(responseObj.toString());
		 * alert.prepareError("mDto ******* = "+com.konylabs.middleware.dataobject.ResultToJSON.
		 * convert(result)).log(); return result; }
		 */
        
        
        AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountTransactionsBusinessDelegate.class);
        AccountTransactionsDTO inputPayLoad = new AccountTransactionsDTO();
        inputPayLoad.setOrder(order);
        inputPayLoad.setOffset(offset);
        inputPayLoad.setLimit(limit);
        String AccIdWithCompanyId = StringUtils.EMPTY;
        try {
            if (accountID.contains("-")) {
                AccIdWithCompanyId = accountID;
            } else {
                AccIdWithCompanyId = HoldingsUtils.getAccountIdWithCompanyFromCache(accountID, request);
            }
        } catch (Exception e) {
            alert.prepareError("Unable to get account with company id from cache " + e).log(); 
            return ErrorCodeEnum.ERR_20046.setErrorCode(new Result());
        }
        inputPayLoad.setAccountId(AccIdWithCompanyId);
        if (transactionType != null) {
            inputPayLoad.setTransactionType(transactionType);
        } else {
            inputPayLoad.setTransactionType("All");
        }
        inputPayLoad.setSortBy(sortBy);
        inputPayLoad.setSearchStartDate(searchStartDate);
        requestMap.put("order", order);
        requestMap.put("offset", offset);
        requestMap.put("limit", limit);
        if (sortBy == null) {
            sortBy = "transactionDate";
        }
        requestMap.put("sortBy", sortBy);
        if (transactionType != null) {
            requestMap.put("transactionType", transactionType);
        } else {
            requestMap.put("transactionType", "All");
        }
        
        // Invoke and get the Account Transaction details from MicroServices
        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                .getPendingAndPostedTransactions(inputPayLoad, request, authToken);

        if (requestMap.get("transactionType") != null && !requestMap.get("transactionType").equals("All") && !requestMap.get("transactionType").equalsIgnoreCase("Both") && !requestMap.get("transactionType").equalsIgnoreCase("Pending") && !requestMap.get("transactionType").equalsIgnoreCase("Posted")) {
            if (!requestMap.get("transactionType").equals("Transfers")) {
                accountTransactionsDTO = GetMicroServiceBDInstance
                        .filterRecordsBasedOnTransactionType(requestMap.get("transactionType"), accountTransactionsDTO);
            }
        }

        // sorting based on the order and the sortBy fields from the input
        // request
        if (requestMap.get("order") != null) {
            if (requestMap.get("sortBy").equals("amount")) {
                accountTransactionsDTO = GetMicroServiceBDInstance.sortBasedOnAmount(accountTransactionsDTO,
                        requestMap.get("order"), requestMap.get("sortBy"));
            } else {
                accountTransactionsDTO = GetMicroServiceBDInstance.sortBasedOnTransactionDate(accountTransactionsDTO,
                        requestMap.get("order"), requestMap.get("sortBy"));
            }

        }

        if (requestMap.get("offset") != null && requestMap.get("limit") != null) {
            accountTransactionsDTO = GetMicroServiceBDInstance.paginationBasedOnOffsetAndLimit(
                    Integer.parseInt(requestMap.get("offset")), Integer.parseInt(requestMap.get("limit")),
                    accountTransactionsDTO);
        }

        JSONArray accountsTxnsDTOArray = new JSONArray(accountTransactionsDTO);
        for (int i = 0; i < accountsTxnsDTOArray.length(); i++) {
            JSONObject arrangement = accountsTxnsDTOArray.getJSONObject(i);
            String id = arrangement.getString("id");
            arrangement.put("Id", id);
            arrangement.remove("id");
        }

        responseObj.put("accountransactionview", accountsTxnsDTOArray);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    @Override
    public Result getLoanScheduleTransactions(String order, String offset, String limit, String accountID,
            String transactionType, String sortBy, String searchStartDate, String isFutureRequired,
            DataControllerRequest request, String authToken) throws ApplicationException {
        HashMap<String, String> requestMap = new HashMap<String, String>();
        JSONObject responseObj = new JSONObject();
        Result result = new Result();
        
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        
        String loanMortgageTransactions = null;
      
        if(accountID.equalsIgnoreCase("MORT12345")||accountID.equalsIgnoreCase("122877")||accountID.equalsIgnoreCase("122878")) {
        	 try {          loanMortgageTransactions = Executor.invokePassThroughServiceAndGetString((InfinityServices)HoldingsAPIServices.MOCKMORTGAGEMS_GETLOANSCHEDULETRANSACTIONS, inputMap, headerMap);
             alert.prepareError("AMS Response" +loanMortgageTransactions).log();
           } catch (Exception e) {
             alert.prepareError("Unable to fetch mortgageTransactions " + e).log();
           } 
        	 List<AccountTransactionsDTO> loanAccountTransactionsDTOList = new ArrayList<AccountTransactionsDTO>();
        	 JSONArray mortgages = new JSONArray(loanMortgageTransactions);
        	 for(int i = 0; i<mortgages.length();i++) {
        	 JSONObject mortgage = mortgages.getJSONObject(i);
        	 AccountTransactionsDTO lmDTO = new AccountTransactionsDTO();
        	 lmDTO.setDate(mortgage.has("Date") ? mortgage.get("Date").toString() : "");
        	 lmDTO.setAmount(mortgage.has("amount") ? Double.valueOf(mortgage.get("amount").toString()).doubleValue() : 0.0);
        	 lmDTO.setFromAccountBalance(mortgage.has("fromAccountBalance") ? Double.valueOf(mortgage.get("fromAccountBalance").toString()).doubleValue() : 0.0);
        	 lmDTO.setInstallmentAmount(mortgage.has("installmentAmount") ? mortgage.get("installmentAmount").toString() : ""); 
        	 lmDTO.setPrincipal(mortgage.has("principal") ? mortgage.get("principal").toString() : ""); 
        	 lmDTO.setTransactionDate(mortgage.has("transactionDate") ? mortgage.get("transactionDate").toString() : "");
        	 lmDTO.setInterest(mortgage.has("interest") ? mortgage.get("interest").toString() : "");
        	 lmDTO.setOutstandingBalance(mortgage.has("outstandingBalance") ? mortgage.get("outstandingBalance").toString() : "");
        	 lmDTO.setCharges(mortgage.has("charges") ? mortgage.get("charges").toString() : "");
        	 lmDTO.setTax(mortgage.has("tax") ? mortgage.get("tax").toString() : "");
        	 lmDTO.setInstallmentType(mortgage.has("installmentType") ? mortgage.get("installmentType").toString() : "");
        	 loanAccountTransactionsDTOList.add(lmDTO);
        	 }
        	 
        	 JSONArray accountsDTOArray = new JSONArray(loanAccountTransactionsDTOList);
             responseObj.put("accountransactionview", accountsDTOArray);
             result = JSONToResult.convert(responseObj.toString());
             return result;
        }

        AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountTransactionsBusinessDelegate.class);
        AccountTransactionsDTO inputPayLoad = new AccountTransactionsDTO();
        inputPayLoad.setOrder(order);
        inputPayLoad.setOffset(offset);
        inputPayLoad.setLimit(limit);
        String AccIdWithCompanyId = StringUtils.EMPTY;
        try {
            if (accountID.contains("-")) {
                AccIdWithCompanyId = accountID;
            } else {
                AccIdWithCompanyId = HoldingsUtils.getAccountIdWithCompanyFromCache(accountID, request);
            }
        }catch (Exception e) {
            alert.prepareError("Unable to get account with company id from cache " + e).log();
            return ErrorCodeEnum.ERR_20046.setErrorCode(new Result());
        }
        inputPayLoad.setAccountId(AccIdWithCompanyId);
        inputPayLoad.setTransactionType(transactionType);
        inputPayLoad.setSortBy(sortBy);
        requestMap.put("order", order);
        requestMap.put("offset", offset);
        requestMap.put("limit", limit);

        // Invoke and get the Account Transaction details from MicroServices
        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                .getloanScheduleTransactions(inputPayLoad, isFutureRequired, request, authToken);

        if (requestMap.get("offset") != null && requestMap.get("limit") != null) {
            accountTransactionsDTO = GetMicroServiceBDInstance.paginationBasedOnOffsetAndLimit(
                    Integer.parseInt(requestMap.get("offset")), Integer.parseInt(requestMap.get("limit")),
                    accountTransactionsDTO);
        }

        JSONArray accountsTxnsDTOArray = new JSONArray(accountTransactionsDTO);
        responseObj.put("accountransactionview", accountsTxnsDTOArray);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    @Override
    public Result getAccountPostedTransactionsPreview(String userName, String did, String offset, String limit,
            String accountId, DataControllerRequest request, String authToken) throws Exception {
        JSONObject responseObj = new JSONObject();
        Result result = new Result();

        AccountTransactionsBusinessDelegate GetMicroServiceBDInstance = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountTransactionsBusinessDelegate.class);
        AccountTransactionsDTO inputPayLoad = new AccountTransactionsDTO();

        inputPayLoad.setDeviceId(did);
        inputPayLoad.setUserName(userName);

        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20045.setErrorCode(new Result());
        }

        List<AccountTransactionsDTO> accountTransactionsDTO = GetMicroServiceBDInstance
                .getDeviceRegistrationDetails(inputPayLoad, request);

        if (accountTransactionsDTO == null || accountTransactionsDTO.size() == 0) {
            JSONArray accountsDTOArray = new JSONArray();
            responseObj.put("accountransactionview", accountsDTOArray);
            result = JSONToResult.convert(responseObj.toString());
            result.getAllDatasets().get(0).setId("records");
            return result;
        } else {
            String sortBy = "transactionDate";
            String transactionType = "All";
            String order = "desc";
            String AccIdWithCompanyId = StringUtils.EMPTY;
            try {
                if (accountId.contains("-")) {
                    AccIdWithCompanyId = accountId;
                } else {
                    AccIdWithCompanyId = HoldingsUtils.getAccountIdWithCompanyFromCache(accountId, request);
                }
            } catch (Exception e) {
                alert.prepareError("Unable to get account with company id from cache " + e).log();
                return ErrorCodeEnum.ERR_20046.setErrorCode(new Result());
            }
            result = getAccountTransactions(order, offset, limit, AccIdWithCompanyId, transactionType, sortBy,
                    authToken, request);
            Dataset dataSet = result.getDatasetById("records");
            List<Record> recordList = dataSet.getAllRecords();
            List<JSONObject> accountTransactionsJSON = new ArrayList<JSONObject>();
            for (int index = 0; index < recordList.size(); index++) {
                accountTransactionsJSON.add(ResultToJSON.convertRecord(recordList.get(index)));
            }
            JSONArray accountsDTOArray = new JSONArray(accountTransactionsJSON);
            List<AccountTransactionsDTO> accountTransactionsDTOList = new ArrayList<AccountTransactionsDTO>();
            accountTransactionsDTOList = JSONUtils.parseAsList(accountsDTOArray.toString(),
                    AccountTransactionsDTO.class);

            // sorting based on the order and the sortBy fields from the input
            // request
            if (order != null) {
                accountTransactionsDTOList = GetMicroServiceBDInstance
                        .sortBasedOnTransactionDate(accountTransactionsDTOList, order, sortBy);
            }

            if (offset != null && limit != null) {
                accountTransactionsDTOList = GetMicroServiceBDInstance.paginationBasedOnOffsetAndLimit(
                        Integer.parseInt(offset), Integer.parseInt(limit), accountTransactionsDTOList);
            }
            responseObj = new JSONObject();
            responseObj.put("accountransactionview", accountTransactionsDTOList);
            result = JSONToResult.convert(responseObj.toString());
            result.getAllDatasets().get(0).setId("records");
            return result;
        }
    }

}