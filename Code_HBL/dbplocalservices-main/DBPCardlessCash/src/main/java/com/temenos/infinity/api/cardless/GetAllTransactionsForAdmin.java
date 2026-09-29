package com.temenos.infinity.api.cardless;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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

public class GetAllTransactionsForAdmin implements JavaService2 {

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
            postProcess(dcRequest, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        Map<String, String> transTypes = getTransTypes(dcRequest);
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateTransactionType(transaction, transTypes);
            updateFromAccountDetails(dcRequest, transaction);
            updateToAccountDetails(dcRequest, transaction);
            updatePayeeDetails(dcRequest, transaction);
            updatePayPersonDetails(dcRequest, transaction);
            updateCashlessOTPValidDate(transaction);
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
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private void updateTransactionType(Record transaction, Map<String, String> transTypes) {
        transaction.addParam(
                new Param("transactionType", transTypes.get(HelperMethods.getFieldValue(transaction, "Type_id")),
                        DBPUtilitiesConstants.STRING_TYPE));
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            transaction.addParam(new Param("fromAccountType",
                    HelperMethods.getFieldValue(frmAccount, "typeDescription"), DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            String nickName = HelperMethods.getFieldValue(frmAccount, "nickName");
            if (StringUtils.isBlank(nickName)) {
                nickName = accountName;
            }
            transaction.addParam(new Param("fromAccountNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateToAccountDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String toAccountNum = HelperMethods.getFieldValue(transaction, "toAccountNumber");
        if (StringUtils.isNotBlank(toAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
            Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            transaction.addParam(new Param("toAccountType", HelperMethods.getFieldValue(toAccount, "typeDescription"),
                    DBPUtilitiesConstants.STRING_TYPE));
            String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
            transaction.addParam(new Param("toAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        String payeeNickName = getPayeeNickName(dcRequest, payeeId);
        if (StringUtils.isBlank(payeeNickName)) {
            payeeNickName = getPayeeNameFromBill(dcRequest, transaction);
        }
        transaction.addParam(new Param("payeeNickName", payeeNickName, DBPUtilitiesConstants.STRING_TYPE));
    }

    private String getPayeeNameFromBill(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
        if (StringUtils.isNotBlank(billId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + billId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                return getPayeeNickName(dcRequest, HelperMethods.getFieldValue(payee, "Payee_id"));
            }
        }
        return null;
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

    private void updateCashlessOTPValidDate(Record transaction) {
        String cashlessOTPValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(cashlessOTPValidDate)) {
            transaction.addParam(new Param("cashlessOTPValidDate", cashlessOTPValidDate.split("\\.")[0], "String"));
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        String adminId = HelperMethods.getAPIUserIdFromSession(dcRequest);
        if (HelperMethods.isAdmin(dcRequest, adminId)) {
            String searchStartDate = inputParams.get("searchStartDate");
            String searchEndDate = inputParams.get("searchEndDate");
            String accountNumber = inputParams.get("accountNumber");

            if (StringUtils.isNotBlank(searchStartDate) && StringUtils.isNotBlank(searchEndDate)) {
                /*
                 * Calendar startCal = Calendar.getInstance(); Calendar endCal = Calendar.getInstance();
                 * startCal.setTime(HelperMethods.getFormattedTimeStamp(searchStartDate));
                 * endCal.setTime(HelperMethods.getFormattedTimeStamp(searchEndDate)); startCal.add(Calendar.YEAR, -30);
                 * startCal.add(Calendar.MONTH, -1); endCal.add(Calendar.YEAR, -30); endCal.add(Calendar.MONTH, -1);
                 * startCal.add(Calendar.HOUR,5); startCal.add(Calendar.MINUTE,30); endCal.add(Calendar.HOUR,5);
                 * endCal.add(Calendar.MINUTE,30);
                 */
                searchStartDate = HelperMethods.convertDateFormat(searchStartDate, null);
                searchEndDate = HelperMethods.convertDateFormat(searchEndDate, null);
            } else {
                searchStartDate = "1970-01-01'T'00:00:00";
                searchEndDate = "2100-01-01'T'00:00:00";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            sb.append("transactionDate").append(DBPUtilitiesConstants.GREATER_EQUAL).append(searchStartDate);
            sb.append(DBPUtilitiesConstants.AND);
            sb.append("transactionDate").append(DBPUtilitiesConstants.LESS_EQUAL).append(searchEndDate);
            sb.append(")");
            sb.append(DBPUtilitiesConstants.AND);
            sb.append("(");
            sb.append("fromAccountNumber").append(DBPUtilitiesConstants.EQUAL).append(accountNumber);
            sb.append(DBPUtilitiesConstants.OR);
            sb.append("toAccountNumber").append(DBPUtilitiesConstants.EQUAL).append(accountNumber);
            sb.append(")");
            inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
            inputParams.put(DBPUtilitiesConstants.ORDERBY, "transactionDate desc");
            return true;
        } else {
            HelperMethods.setValidationMsg("logged in user is not admin", dcRequest, result);
            return false;
        }
    }

    private String getPayeeNickName(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                return HelperMethods.getFieldValue(payee, "nickName");
            }
        }
        return null;
    }

    private Map<String, String> getTransTypes(DataControllerRequest dcRequest) throws HttpCallException {
        Result transTypes = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        Map<String, String> transTypeMap = new HashMap<>();
        List<Record> types = transTypes.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            transTypeMap.put(HelperMethods.getFieldValue(type, "Id"), HelperMethods.getFieldValue(type, "description"));
        }
        return transTypeMap;
    }

}
