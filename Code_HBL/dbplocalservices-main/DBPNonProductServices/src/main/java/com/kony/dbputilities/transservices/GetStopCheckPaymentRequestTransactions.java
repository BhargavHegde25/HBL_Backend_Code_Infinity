package com.kony.dbputilities.transservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetStopCheckPaymentRequestTransactions implements JavaService2 {

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        if (preProcess(inputParams, dcRequest, result, user)) {
            String url = URLConstants.ACCOUNT_TRANSACTION_PROC;
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
        }
        if (!HelperMethods.hasError(result)) {
            if (!HelperMethods.hasRecords(result)) {
                result.addDataset(new Dataset());
            }
            result.getAllDatasets().get(0).setId("accountransactionview");
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Map inputParams, Result result) throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateRequestType(transaction);
            updatePayeeName(dcRequest, transaction);
            updateFromAccountDetails(dcRequest, transaction);
            updateDateFormat(transaction);
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
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            String date = HelperMethods.getFieldValue(transaction, "requestValidity");
            if (StringUtils.isNotBlank(date)) {
                transaction.addParam(new Param("requestValidity",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            date = HelperMethods.getFieldValue(transaction, "checkDateOfIssue");
            if (StringUtils.isNotBlank(date)) {
                transaction.addParam(new Param("checkDateOfIssue",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private void updatePayeeName(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        if (StringUtils.isBlank(HelperMethods.getFieldValue(transaction, "payeeName"))
                && StringUtils.isNotBlank(payeeId)) {
            Record payee = getPayeeDetails(dcRequest, payeeId);
            transaction.addParam(new Param("payeeName", HelperMethods.getFieldValue(payee, "name"),
                    DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private Record getPayeeDetails(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        if (HelperMethods.hasRecords(result)) {
            return result.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String toAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
        Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
        String nickName = HelperMethods.getFieldValue(toAccount, "nickName");
        transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        transaction.addParam(new Param("fromAccountNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
    }

    private void updateRequestType(Record transaction) {
        String chkNum2 = HelperMethods.getFieldValue(transaction, "checkNumber2");
        Param p = new Param("requestType", "Single", DBPUtilitiesConstants.STRING_TYPE);
        if (StringUtils.isNotBlank(chkNum2)) {
            p.setValue("Series");
        }
        transaction.addParam(p);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        String userId = user.get("user_id");
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "TransactionDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        StringBuilder filter = new StringBuilder();
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ForSpecificAccounts_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST));

        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append("where (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "'))"); filter.append(
			 * " and tt.description = '" +
			 * DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST + "'");
			 
        	
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ForSpecificAccounts_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST));

        } else {
			
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
			 * filter.append("where (t1.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')");
			 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "'))"); filter.append(
			 * " and tt.description = '" +
			 * DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST + "'");
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ForSpecificAccounts_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId).replace("?3", DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST));
        }*/

        filter.append(" order by " + sortBy + " " + order);

        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().
        			replace("?1", offset).replace("?2", limit));
        }

        inputParams.put("transactions_query", filter.toString());

        return true;
    }
}
