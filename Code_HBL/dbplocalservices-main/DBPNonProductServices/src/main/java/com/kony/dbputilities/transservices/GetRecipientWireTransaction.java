package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetRecipientWireTransaction implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.TRANSACTION_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        Param amountTransferedTillNow = new Param();
        amountTransferedTillNow.setName("amountTransferedTillNow");
        String amountTransferedTillDate = "0";
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        Map<String, String> accountTypes = getAccountTypes(dcRequest, user.get("countryCode"));
        for (Record transaction : transactions) {
            transaction.addParam(new Param("transactionType", "Wire", DBPUtilitiesConstants.STRING_TYPE));
            String description = HelperMethods.getFieldValue(transaction, "description");
            if (StringUtils.isBlank(description)) {
                transaction.addParam(new Param("description", "None", DBPUtilitiesConstants.STRING_TYPE));
            }
            // adding amount and sharing same param to get sum of amount across
            // all records
            amountTransferedTillDate = addAmount(amountTransferedTillDate,
                    HelperMethods.getFieldValue(transaction, "amount"));
            amountTransferedTillNow.setValue(amountTransferedTillDate);
            transaction.addParam(amountTransferedTillNow);
            Record accountDetails = getAccountDetails(dcRequest,
                    HelperMethods.getFieldValue(transaction, "fromAccountNumber"));
            transaction.addParam(new Param("fromAccountName",
                    HelperMethods.getFieldValue(accountDetails, "accountName"), DBPUtilitiesConstants.STRING_TYPE));
            transaction.addParam(new Param("fromAccountNickName",
                    HelperMethods.getFieldValue(accountDetails, "nickName"), DBPUtilitiesConstants.STRING_TYPE));
            String accountType = accountTypes.get(HelperMethods.getFieldValue(accountDetails, "Type_id"));
            transaction.addParam(new Param("fromAccountType", accountType, DBPUtilitiesConstants.STRING_TYPE));

            accountDetails = getAccountDetails(dcRequest, HelperMethods.getFieldValue(transaction, "toAccountNumber"));
            accountType = accountTypes.get(HelperMethods.getFieldValue(accountDetails, "Type_id"));
            transaction.addParam(new Param("fromAccountType", accountType, DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(accountDetails, "accountName");
            if (StringUtils.isBlank(accountName)) {
                accountName = HelperMethods.getFieldValue(accountDetails, "nickName");
            }
            transaction.addParam(new Param("toAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            try {
                transaction.addParam(new Param("transactionDate", HelperMethods.convertDateFormat(
                        HelperMethods.getFieldValue(result, "transactionDate"), "yyyy-MM-dd'T'HH:mm:ss"), "String"));
            } catch (ParseException e) {
                alert.prepareError(e.getMessage()).log();
            }
            updatePayeeDetails(dcRequest, transaction);
        }
    }

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        Record payeeDetails = getPayeeDetails(dcRequest, payeeId);
        payeeDetails.removeParamByName("notes");
        for (Param field : payeeDetails.getAllParams()) {
            transaction.addParam(field);
        }
    }

    private Record getPayeeDetails(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        return payee.getAllDatasets().get(0).getRecord(0);
    }

    private String addAmount(String amountTransferedTillDate, String amount) {
        return new BigDecimal(amountTransferedTillDate).add(new BigDecimal(amount)).toPlainString();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String typeId = getTransTypeId(dcRequest, "Wire");
        String payeeId = (String) inputParams.get("payeeId");
        String limit = (String) inputParams.get("limit");

        String userid = HelperMethods.getCustomerIdFromSession(dcRequest);
        if (StringUtils.isNotBlank(userid)) {
            status = checkForScope(dcRequest, payeeId, userid);
        }

        StringBuilder filter = new StringBuilder();
        filter.append("Type_id").append(DBPUtilitiesConstants.EQUAL).append(typeId);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("Payee_id").append(DBPUtilitiesConstants.EQUAL).append(payeeId);
        if (StringUtils.isNotBlank(limit)) {
            inputParams.put(DBPUtilitiesConstants.TOP, limit);
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        inputParams.put(DBPUtilitiesConstants.ORDERBY, "createdDate desc");
        return status;
    }

    private boolean checkForScope(DataControllerRequest dcRequest, String payeeId, String userid) {
        boolean status = true;
        String filterQuery = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result result = new Result();
        try {
            result = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (HelperMethods.hasRecords(result)) {
            String customerid = HelperMethods.getFieldValue(result, "User_Id");
            return userid.equals(customerid);
        } else {
            status = false;
        }
        return status;
    }

    public String getTransTypeId(DataControllerRequest dcRequest, String transType) throws HttpCallException {
        String filterQuery = DBPUtilitiesConstants.TRANS_TYPE_DESC + DBPUtilitiesConstants.EQUAL + transType;
        Result rs = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        return HelperMethods.getFieldValue(rs, DBPUtilitiesConstants.T_TRANS_ID);
    }

    private Map<String, String> getAccountTypes(DataControllerRequest dcRequest, String countryCode)
            throws HttpCallException {
        String filter = null;
        if (StringUtils.isNotBlank(countryCode)) {
            filter = "countryCode" + DBPUtilitiesConstants.EQUAL + countryCode;
        }
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

    public Record getAccountDetails(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        Record result = new Record();
        if (StringUtils.isNotBlank(accountId)) {
            String filter = DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL + accountId;
            Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            if (HelperMethods.hasRecords(rs)) {
                result = rs.getAllDatasets().get(0).getRecord(0);
            }
        }
        return result;
    }
}
