package com.temenos.infinity.api.cardless;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.LegalEntityUtil;


public class GetPostedDepositTransactions implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Map inputParams, Result result) throws HttpCallException {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        Map<String, String> accountTypes = getAccountTypes(dcRequest, (String) inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateNullValues(transaction);
            updateCheckImage(transaction);
            updateToAccountName(dcRequest, transaction, userId);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updateDateFormat(transaction);
        }
    }

    private void updateDateFormat(Record transaction) {
        try {
            HelperMethods.updateDateFormat(transaction, "scheduledDate");
            HelperMethods.updateDateFormat(transaction, "transactionDate");
            HelperMethods.updateDateFormat(transaction, "frequencyEndDate");
            HelperMethods.updateDateFormat(transaction, "frequencyStartDate");
        } catch (Exception e) {

        }
    }

    private void updateToAccountName(DataControllerRequest dcRequest, Record transaction, String userId)
            throws HttpCallException {
        String toExtAccountNum = HelperMethods.getFieldValue(transaction, "toExternalAccountNumber");

        String iBAN = HelperMethods.getFieldValue(transaction, DBPInputConstants.IBAN);

        String filter = "";
        if (StringUtils.isNotBlank(toExtAccountNum)) {
            filter = "accountNumber" + DBPUtilitiesConstants.EQUAL + toExtAccountNum;
        } else if (StringUtils.isNotBlank(iBAN)) {
            filter = DBPInputConstants.IBAN + DBPUtilitiesConstants.EQUAL + iBAN;

            if (StringUtils.isNotBlank(userId)) {
                filter += DBPUtilitiesConstants.AND;

                filter += "User_id" + DBPUtilitiesConstants.EQUAL + userId;
            }
        }

        if (!filter.isEmpty()) {
            Result extAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.EXT_ACCOUNTS_GET);
            String toAccountName = HelperMethods.getFieldValue(extAccount, "nickName");
            if (StringUtils.isBlank(toAccountName)) {
                toAccountName = HelperMethods.getFieldValue(extAccount, "beneficiaryName");
            }
            if (StringUtils.isBlank(toAccountName)) {
                toAccountName = HelperMethods.getFieldValue(transaction, "beneficiaryName");
            }
            transaction.addParam(new Param("toAccountName", toAccountName, DBPUtilitiesConstants.STRING_TYPE));
            String isInternationalAccount = HelperMethods.getFieldValue(extAccount, "isInternationalAccount");
            transaction.addParam(
                    new Param("isInternationalAccount", isInternationalAccount, DBPUtilitiesConstants.STRING_TYPE));
            String swiftCode = HelperMethods.getFieldValue(transaction, "swiftCode");
            if (StringUtils.isBlank(swiftCode)) {
                swiftCode = HelperMethods.getFieldValue(extAccount, "swiftCode");
                transaction.addParam(new Param("swiftCode", swiftCode, "String"));
            }

        }
    }

    private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
            throws HttpCallException {
        String filter = "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
        Result accountTypes = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTTYPE_GET);
        Map<String, String> accountTypeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            accountTypeMap.put(HelperMethods.getFieldValue(type, "TypeID"),
                    HelperMethods.getFieldValue(type, "TypeDescription"));
        }
        return accountTypeMap;
    }

    private void updateToAccountDetails(DataControllerRequest dcRequest, Record transaction,
            Map<String, String> accountTypes) throws HttpCallException {
        String toAccountNum = HelperMethods.getFieldValue(transaction, "toAccountNumber");
        if (StringUtils.isNotBlank(toAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
            Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String typeId = HelperMethods.getFieldValue(toAccount, "Type_id");
            transaction
                    .addParam(new Param("toAccountType", accountTypes.get(typeId), DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
            transaction.addParam(new Param("toAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateCheckImage(Record transaction) {
        transaction.addParam(new Param("checkImage", "https://retailbanking1.temenos-cloud.net/dbimages/check_front.png",
                DBPUtilitiesConstants.STRING_TYPE));
        transaction.addParam(new Param("checkImageBack", "https://retailbanking1.temenos-cloud.net/dbimages/check_back.png",
                DBPUtilitiesConstants.STRING_TYPE));
    }

    private void updateNullValues(Record transaction) {
        String transDesc = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isBlank(transDesc)) {
            transDesc = "None";
            transaction.addParam(new Param("description", transDesc, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        StringBuilder filter = new StringBuilder();
        String legalEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPostedDepositTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")).replace("?3", legalEntityId));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append(" where t1.statusDesc = 'Successful' ");
			 * filter.append(" AND (t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("customer_id") + "')");
			 * filter.append(" OR t1.fromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("customer_id") + "'))");
			 * filter.append(" AND tt.description = 'Deposit' ");
			 * filter.append(" ORDER BY t1.createdDate desc limit 0, 10");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPostedDepositTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        } else {
			
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append(" where t1.statusDesc = 'Successful' ");
			 * filter.append(" AND (t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("customer_id") + "')");
			 * filter.append(" OR t1.fromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("customer_id") + "'))");
			 * filter.append(" AND tt.description = 'Deposit' ");
			 * filter.append(" ORDER BY t1.createdDate desc limit 0, 10");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPostedDepositTransactions_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        }*/
        inputParams.put("transactions_query", filter.toString());
        return true;
    }

}
