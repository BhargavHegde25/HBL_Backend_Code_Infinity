package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.payeeservices.VariableUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetUserWiredTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        if (preProcess(inputParams, dcRequest, result, user)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }
        if (!HelperMethods.hasRecords(result)) {
            Dataset ds = new Dataset();
            ds.setId("wireaccounttransactionview");
            result.addDataset(ds);
            return result;
        }
        if (!HelperMethods.hasError(result)) {
            result.getAllDatasets().get(0).setId("wireaccounttransactionview");
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateNullFieldValues(transaction);
            updateFromAccountDetails(dcRequest, transaction, accountTypes);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updatePayeeDetails(dcRequest, transaction);
            updateDateFormat(transaction);
        }
    }

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        Record payeeDetails = getPayeeDetails(dcRequest, payeeId);
        if(payeeDetails == null) 
        	return;
        payeeDetails.removeParamByName("notes");
        for (Param field : payeeDetails.getAllParams()) {
            transaction.addParam(field);
        }
        transaction.addParam("payeeType", payeeDetails.getParamValueByName("wireAccountType"));
        transaction.addParam("payeeAccountNumber", payeeDetails.getParamValueByName("accountNumber"));
    }

    private Record getPayeeDetails(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        return payee.getAllDatasets().get(0).getRecord(0);
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
            String nickName = HelperMethods.getFieldValue(toAccount, "nickName");
            String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
            if (StringUtils.isBlank(nickName)) {
                nickName = accountName;
            }
            transaction.addParam(new Param("toAccountName", nickName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction,
            Map<String, String> accountTypes) throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String typeId = HelperMethods.getFieldValue(frmAccount, "Type_id");
            transaction.addParam(
                    new Param("fromAccountType", accountTypes.get(typeId), DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            String nickName = HelperMethods.getFieldValue(frmAccount, "nickName");
            transaction.addParam(new Param("fromAccountNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
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

    private void updateNullFieldValues(Record transaction) {
        String value = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isBlank(value)) {
            transaction.addParam(new Param("description", "None", "String"));
        }
        value = HelperMethods.getFieldValue(transaction, "frequencyType");
        if (StringUtils.isBlank(value)) {
            transaction.addParam(new Param("frequencyType", "Once", "String"));
        }
        value = HelperMethods.getFieldValue(transaction, "numberOfRecurrences");
        if (StringUtils.isBlank(value)) {
            transaction.addParam(new Param("numberOfRecurrences", "0", "String"));
        }
    }

    private void updateDateFormat(Record transaction) {
        String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        try {
            if (StringUtils.isNotBlank(scheduledDate)) {
                transaction.addParam(new Param("scheduledDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(new Param("transactionDate",
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
    	String jdbcUrl=QueryFormer.getDBType(dcRequest);
        inputParams.put("countryCode", user.get("countryCode"));
        String start = inputParams.get("firstRecordNumber");
        String end = inputParams.get("lastRecordNumber");
        String sortBy = inputParams.get("sortBy");
        String order = inputParams.get("order");
        if (StringUtils.isBlank(sortBy)) {
        	sortBy = VariableUtils.quote("transactionDate", dcRequest);
        }
        else {
        	sortBy = VariableUtils.quote(sortBy, dcRequest);
        }
        if (StringUtils.isBlank(order)) {
            order = "asc";
        }
        
        StringBuilder filter = new StringBuilder();
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserWiredTransactions").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * filter.
			 * append("select t.*,tt.description as transactionType, IF(p.organizationId or a.Organization_id, 1, 0) as isBusinessPayee from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".payee p on (p.Id = t.Payee_id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts a on (a.Account_id = t.ToAccountNumber) , " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype tt ");
			 * filter.append("where (t.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "')");
			 * filter.append(" or t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "') ");
			 * filter.append(" ) and t.Type_id = tt.id and tt.description = 'Wire'");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserWiredTransactions").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        } else {
			
			 * filter.
			 * append("select t.*,tt.description as transactionType, IF(p.organizationId or a.Organization_id, 1, 0) as isBusinessPayee from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".payee p on (p.Id = t.Payee_id) left join " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts a on (a.Account_id = t.ToAccountNumber) , " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transactiontype tt ");
			 * filter.append("where (t.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("user_id") + "')");
			 * filter.append(" or t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("user_id") + "') ");
			 * filter.append(" ) and t.Type_id = tt.id and tt.description = 'Wire'");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserWiredTransactions_ELSE").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        }*/
        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
           // filter.append(" order by ").append(sortBy).append(" " + order);
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserWiredTransactions_order").getQuery().replace("?1", sortBy).replace("?2", order));
        }
        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
            //filter.append(" limit ").append(start).append(",").append(end);
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserWiredTransactions_limit").getQuery().replace("?1", start).replace("?2", end));
        }
        inputParams.put("transactions_query", filter.toString());
        return true;
    }

}
