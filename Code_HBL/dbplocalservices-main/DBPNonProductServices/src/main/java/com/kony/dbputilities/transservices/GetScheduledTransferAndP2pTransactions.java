package com.kony.dbputilities.transservices;

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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetScheduledTransferAndP2pTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        if (preProcess(inputParams, dcRequest, result, user)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
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

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateToAccountName(dcRequest, transaction, HelperMethods.getUserIdFromSession(dcRequest));
            updatePayPersonDetails(dcRequest, transaction);
            updateFromAccountDetails(dcRequest, transaction, accountTypes);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updateDateFormat(transaction);
        }
    }

    private void updateDateFormat(Record transaction) {
        String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        try {
            if (StringUtils.isNotBlank(scheduledDate)) {
                transaction.addParam(HelperMethods.convertedDateParam("scheduledDate", scheduledDate));
            }
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(HelperMethods.convertedDateParam("transactionDate", transactionDate));
            }
            String frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(HelperMethods.convertedDateParam("frequencyEndDate", frequencyDate));
            }
            frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyStartDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(HelperMethods.convertedDateParam("frequencyStartDate", frequencyDate));
            }
        } catch (Exception e) {
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

    private void updatePayPersonDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payPersonId = HelperMethods.getFieldValue(transaction, "Person_Id");
        if (StringUtils.isNotBlank(payPersonId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + payPersonId;
            Result payPerson = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_GET);
            if (HelperMethods.hasRecords(payPerson)) {
                Record person = payPerson.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(new Param("phone", HelperMethods.getFieldValue(person, "phone"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("email", HelperMethods.getFieldValue(person, "email"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("name", HelperMethods.getFieldValue(person, "name"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) {
        inputParams.put("countryCode", user.get("countryCode"));
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        StringBuilder filter = new StringBuilder();
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetScheduledTransferAndP2pTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * filter.append("select t.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt "); filter.
			 * append("where t.isScheduled = 1 and (t.FromAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "')");
			 * filter.append(" or t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + user.get("user_id") + "') ");
			 * filter.append(
			 * " ) and t.Type_id = tt.id and tt.description in ('Deposit', 'StopCheckPaymentRequest','P2P')"
			 * );
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetScheduledTransferAndP2pTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        } else {
			
			 * filter.append("select t.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt "); filter.
			 * append("where t.isScheduled = 1 and (t.FromAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("user_id") + "')");
			 * filter.append(" or t.ToAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + user.get("user_id") + "') "); filter.append(
			 * " ) and t.Type_id = tt.id and tt.description in ('InternalTransfer', 'ExternalTransfer','P2P')"
			 * );
			 
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetScheduledTransferAndP2pTransactions_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("user_id")));
        }*/

        inputParams.put("transactions_query", filter.toString());
        return true;
    }
}
