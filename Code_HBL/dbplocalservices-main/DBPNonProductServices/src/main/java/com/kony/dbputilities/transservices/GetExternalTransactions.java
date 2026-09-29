package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * getExternalTransactionsForAccount
 *
 */
public class GetExternalTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.TRANSACTION_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result, inputParams);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result, Map<String, String> inputParams)
            throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        String accountId = inputParams.get("accountID");
        Map<String, String> transactionTypes = getTransactionTypes(dcRequest);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        Map<String, String> accountTypes = getAccountTypes(dcRequest, user.get("countryCode"));
        for (Record transaction : transactions) {
            String extAccount = HelperMethods.getFieldValue(transaction, "toExternalAccountNumber");
            String iBAN = HelperMethods.getFieldValue(transaction, DBPInputConstants.IBAN);
            String accountName = getExtAccountName(dcRequest, extAccount, iBAN);
            createAndaddParam(transaction, "toAccountName", accountName);
            String typeDescription = transactionTypes.get(HelperMethods.getFieldValue(transaction, "Type_id"));
            createAndaddParam(transaction, "transactiontype", typeDescription);
            String frmAccount = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
            if (StringUtils.isNotBlank(frmAccount)) {
                if (frmAccount.equals(accountId)) {
                    String amount = HelperMethods.getFieldValue(transaction, "amount");
                    createAndaddParam(transaction, "amount", "-" + amount);
                }
                Record frmAccountdetails = getAccountDetails(dcRequest, frmAccount);
                String frmNickName = getNickName(frmAccountdetails);
                createAndaddParam(transaction, "fromAccountNickName", frmNickName);
                String accountType = accountTypes.get(HelperMethods.getFieldValue(frmAccountdetails, "Type_id"));
                createAndaddParam(transaction, "fromAccountType", accountType);
            }
            String toAccount = HelperMethods.getFieldValue(transaction, "toAccountNumber");
            if (StringUtils.isNotBlank(toAccount)) {
                Record toAccountdetails = getAccountDetails(dcRequest, toAccount);
                createAndaddParam(transaction, "toAccountName",
                        HelperMethods.getFieldValue(toAccountdetails, "accountName"));
                String accountType = accountTypes.get(HelperMethods.getFieldValue(toAccountdetails, "Type_id"));
                createAndaddParam(transaction, "toAccountType", accountType);
            }
            String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
            if (StringUtils.isNotBlank(payeeId)) {
                createAndaddParam(transaction, "payeeNickName", getPayeeNickName(dcRequest, payeeId));
            }
            String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
            if (StringUtils.isNotBlank(billId)) {
                payeeId = getPayeeId(dcRequest, billId);
                if (StringUtils.isNotBlank(payeeId)) {
                    createAndaddParam(transaction, "payeeNickName", getPayeeNickName(dcRequest, payeeId));
                }
            }
            String personId = HelperMethods.getFieldValue(transaction, "Person_Id");
            if (StringUtils.isNotBlank(billId)) {
                Record personDetails = getPersonDetails(dcRequest, personId);
                createAndaddParam(transaction, "payPersonPhone", HelperMethods.getFieldValue(personDetails, "phone"));
                createAndaddParam(transaction, "payPersonEmail", HelperMethods.getFieldValue(personDetails, "email"));
                createAndaddParam(transaction, "payPersonName",
                        HelperMethods.getFieldValue(personDetails, "firstName"));
            }
        }
    }

    private void createAndaddParam(Record record, String paramName, String paramValue) {
        record.addParam(new Param(paramName, paramValue, DBPUtilitiesConstants.STRING_TYPE));
    }

    private Record getPersonDetails(DataControllerRequest dcRequest, String personId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + personId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYPERSON_GET);
        if (HelperMethods.hasRecords(result)) {
            return result.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private String getPayeeId(DataControllerRequest dcRequest, String billId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_GET);
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "Payee_id");
        }
        return null;
    }

    private String getPayeeNickName(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "nickName");
        }
        return null;
    }

    private String getNickName(Record frmAccountdetails) {
        String nickName = HelperMethods.getFieldValue(frmAccountdetails, "nickName");
        if (StringUtils.isBlank(nickName)) {
            nickName = HelperMethods.getFieldValue(frmAccountdetails, "accountName");
        }
        return nickName;
    }

    private Record getAccountDetails(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        if (HelperMethods.hasRecords(result)) {
            return result.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
            throws HttpCallException {
        String filter = "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
        Result accountTypes = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTTYPE_GET);
        Map<String, String> typeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            typeMap.put(HelperMethods.getFieldValue(type, "TypeID"),
                    HelperMethods.getFieldValue(type, "TypeDescription"));
        }
        return typeMap;
    }

    private Map<String, String> getTransactionTypes(DataControllerRequest dcRequest) throws HttpCallException {
        Result accountTypes = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        Map<String, String> typeMap = new HashMap<>();
        List<Record> types = accountTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            typeMap.put(HelperMethods.getFieldValue(type, "Id"), HelperMethods.getFieldValue(type, "description"));
        }
        return typeMap;
    }

    private String getExtAccountName(DataControllerRequest dcRequest, String extAccount, String iBAN)
            throws HttpCallException {
        String filter = "";
        if (StringUtils.isNotBlank(extAccount)) {
            filter += "Id" + DBPUtilitiesConstants.EQUAL + extAccount;
        } else {
            filter += DBPInputConstants.IBAN + DBPUtilitiesConstants.EQUAL + iBAN;

            String userID = HelperMethods.getUserIdFromSession(dcRequest);
            if (StringUtils.isNotBlank(userID)) {
                filter += DBPUtilitiesConstants.AND;

                filter += "User_id" + DBPUtilitiesConstants.EQUAL + userID;
            }
        }

        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_GET);
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result.getAllDatasets().get(0).getRecord(0), "firstName");
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String accountid = (String) inputParams.get("accountID");
        String nullValue = null;
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        String start = (String) inputParams.get("firstRecordNumber");
        String end = (String) inputParams.get("lastRecordNumber");
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "transactionDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        StringBuilder filter = new StringBuilder();
        filter.append("fromAccountNumber").append(DBPUtilitiesConstants.EQUAL).append(accountid);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("toExternalAccountNumber").append(DBPUtilitiesConstants.NOT_EQ).append(nullValue);
        if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
            inputParams.put(DBPUtilitiesConstants.SKIP, start);
            inputParams.put(DBPUtilitiesConstants.TOP, end);
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy + " " + order);
        return true;
    }
}
