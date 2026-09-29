package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.accountservices.GetAccountsPostLogin;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountTransactionsByType implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        if (preProcess(inputParams, dcRequest, result, user, methodID, inputArray, dcResponse)) {
            String url = URLConstants.ACCOUNT_TRANSACTION_GET;
            if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
                url = URLConstants.CUST_ACCOUNT_TRANSACTION_GET;
            }
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), url);
            if (!HelperMethods.hasError(result)) {
                if (!HelperMethods.hasRecords(result)) {
                    result.addDataset(new Dataset());
                }
                result.getAllDatasets().get(0).setId("accountransactionview");
            }
            postProcess(dcRequest, inputParams, result);
        } else {
            Dataset dataset = new Dataset();
            dataset.setId("accountransactionview");
            result.addDataset(dataset);
        }

        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        if (HelperMethods.hasRecords(result)) {
            List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
            Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
            Map<String, String> accountTypes = getAccountTypes(dcRequest, user.get("countryCode"));
            String accountId = inputParams.get("accountID");
            for (Record transaction : transactions) {
                updateToAccountName(dcRequest, transaction, user.get("user_id"));
                updateCheckImage(transaction);
                updateNullFieldValues(transaction);
                updateFromAccountDetails(dcRequest, transaction, accountTypes);
                updateToAccountDetails(dcRequest, transaction, accountTypes);
                updateAmount(accountId, transaction);
                updateCheckNumber(transaction);
                updateTotalChkAmount(transaction);
                updatePayPersonDetails(dcRequest, transaction);
                updateBillDetails(dcRequest, transaction);
                updatePayeeDetails(dcRequest, transaction);
                updateDateFormat(transaction);
            }
        }
    }

    private void updateDateFormat(Record transaction) {
        String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        String postedDate = HelperMethods.getFieldValue(transaction, "postedDate");
        try {
            if (StringUtils.isNotBlank(scheduledDate)) {
                transaction.addParam(new Param("scheduledDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(new Param("transactionDate",
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(postedDate)) {
                transaction.addParam(new Param("postedDate",
                        HelperMethods.convertDateFormat(postedDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            String frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(new Param("frequencyEndDate",
                        HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyStartDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(new Param("frequencyStartDate",
                        HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
        } catch (Exception e) {
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
                transaction.addParam(new Param("secondaryEmail", HelperMethods.getFieldValue(person, "secondaryEmail"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(
                        new Param("secondoryPhoneNumber", HelperMethods.getFieldValue(person, "secondoryPhoneNumber"),
                                DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void updateBillDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
        if (StringUtils.isNotBlank(billId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
            Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(biller)) {
                Record bill = biller.getAllDatasets().get(0).getRecord(0);
                String payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
                fetchAndUpdatePayee(dcRequest, payeeId, transaction);
                transaction.addParam(new Param("billDueDate", HelperMethods.getFieldValue(bill, "billDueDate"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("dueAmount", HelperMethods.getFieldValue(bill, "dueAmount"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("billGeneratedDate",
                        HelperMethods.getFieldValue(bill, "billGeneratedDate"), DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("paidDate", HelperMethods.getFieldValue(bill, "paidDate"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("ebillURL", HelperMethods.getFieldValue(bill, "ebillURL"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void fetchAndUpdatePayee(DataControllerRequest dcRequest, String payeeId, Record transaction)
            throws HttpCallException {
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                fetchAndUpdateBillMaster(dcRequest, HelperMethods.getFieldValue(payee, "billermaster_id"), transaction);
                transaction.addParam(new Param("payeeNickName", HelperMethods.getFieldValue(payee, "nickName"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("eBillEnable", HelperMethods.getFieldValue(payee, "eBillEnable"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void fetchAndUpdateBillMaster(DataControllerRequest dcRequest, String billermasterId, Record transaction)
            throws HttpCallException {
        if (StringUtils.isNotBlank(billermasterId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billermasterId;
            Result billMasters = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_MASTER_GET);
            if (HelperMethods.hasRecords(billMasters)) {
                Record bMaster = billMasters.getAllDatasets().get(0).getRecord(0);
                fetchAndUpdateBillCategory(dcRequest, HelperMethods.getFieldValue(bMaster, "billerCategoryId"),
                        transaction);
                transaction.addParam(new Param("ebillSupport", HelperMethods.getFieldValue(bMaster, "ebillSupport"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("billerCategoryId",
                        HelperMethods.getFieldValue(bMaster, "billerCategoryId"), DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void fetchAndUpdateBillCategory(DataControllerRequest dcRequest, String billerCategoryId,
            Record transaction) throws HttpCallException {
        if (StringUtils.isNotBlank(billerCategoryId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billerCategoryId;
            Result billCategories = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_CATEGORY_GET);
            if (HelperMethods.hasRecords(billCategories)) {
                Record bCategory = billCategories.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(new Param("categoryName", HelperMethods.getFieldValue(bCategory, "categoryName"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "payeeId");
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(new Param("payeeNickName", HelperMethods.getFieldValue(payee, "nickName"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("payeeName", HelperMethods.getFieldValue(payee, "name"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("routingCode", HelperMethods.getFieldValue(payee, "routingCode"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("internationalRoutingNumber",
                        HelperMethods.getFieldValue(payee, "internationalRoutingNumber"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("bankName", HelperMethods.getFieldValue(payee, "bankName"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("iban", HelperMethods.getFieldValue(payee, "internationalAccountNumber"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("wireAccountType", HelperMethods.getFieldValue(payee, "wireAccountType"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("country", HelperMethods.getFieldValue(payee, "country"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("swiftCode", HelperMethods.getFieldValue(payee, "swiftCode"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("payeeAddressLine1", HelperMethods.getFieldValue(payee, "addressLine1"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("payeeAddressLine2", HelperMethods.getFieldValue(payee, "addressLine2"),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
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
            if (StringUtils.isBlank(nickName)) {
                nickName = accountName;
            }
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

    private void updateTotalChkAmount(Record transaction) {
        String amt1 = HelperMethods.getFieldValue(transaction, "withdrawlAmount1");
        String amt2 = HelperMethods.getFieldValue(transaction, "withdrawlAmount2");
        String amt3 = HelperMethods.getFieldValue(transaction, "cashAmount");
        BigDecimal total = new BigDecimal(amt1).add(new BigDecimal(amt2)).add(new BigDecimal(amt3));
        transaction.addParam(new Param("totalCheckAmount", total.toPlainString(), "String"));
    }

    private void updateCheckNumber(Record transaction) {
        String chkNum1 = HelperMethods.getFieldValue(transaction, "checkNumber1");
        String chkNum2 = HelperMethods.getFieldValue(transaction, "checkNumber2");
        transaction.addParam(new Param("checkNumber1", "Check #" + chkNum1, "String"));
        transaction.addParam(new Param("checkNumber2", "Check #" + chkNum2, "String"));
    }

    private void updateAmount(String accountId, Record transaction) {
        String frmAccount = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        String description = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isNotBlank(frmAccount) && StringUtils.isNotBlank(accountId)
                && !"InterestCredit".equalsIgnoreCase(description) && accountId.equals(frmAccount)) {
            String amount = "-" + HelperMethods.getFieldValue(transaction, "amount");
            transaction.addParam(new Param("amount", amount, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateNullFieldValues(Record transaction) {
        String value = HelperMethods.getFieldValue(transaction, "transactionDesc");
        if (StringUtils.isBlank(value)) {
            transaction.addParam(new Param("transactionDesc", "None", "String"));
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

    private void updateCheckImage(Record transaction) {
        String value = HelperMethods.getFieldValue(transaction, "description");
        if ("Deposit".equalsIgnoreCase(value)) {
            transaction.addParam(
                    new Param("checkImage", "http://pmqa.konylabs.net/KonyWebBanking/check_front.png", "String"));
            transaction.addParam(
                    new Param("checkImageBack", "http://pmqa.konylabs.net/KonyWebBanking/check_back.png", "String"));
        }
    }

    private void updateToAccountName(DataControllerRequest dcRequest, Record transaction, String userId)
            throws HttpCallException {
        String toExtAccountNum = HelperMethods.getFieldValue(transaction, "ExternalAccountNumber");

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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user, String methodID, Object[] inputArray, DataControllerResponse dcResponse)
            throws Exception {

        GetAccountsPostLogin accountsPostLogin = new GetAccountsPostLogin();
        String accountId = (String) inputParams.get("accountID");

        if (StringUtils.isBlank(accountId)) {
            return false;
        }
        Result result1 = (Result) accountsPostLogin.invoke(methodID, getInputArray(inputArray), dcRequest, dcResponse);
        boolean status = false;
        if (HelperMethods.hasRecords(result1)) {
            List<Record> accounts = HelperMethods.getDataSet(result1).getAllRecords();
            for (Record record : accounts) {
                if (accountId.equals(HelperMethods.getFieldValue(record, "Account_id"))) {
                    status = true;
                }
            }
        }
        if (!status) {
            return status;
        }

        String filter = getFilterQuery(inputParams, dcRequest, user.get("user_id"));
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "transactionDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy + " " + order);
        inputParams.put(DBPUtilitiesConstants.TOP, limit);
        inputParams.put(DBPUtilitiesConstants.SKIP, offset);
        return status;
    }

    private Object[] getInputArray(Object[] inputArray) {
        Object[] newArray = new Object[3];
        Map map = (HashMap) inputArray[1];
        Map<Object, Object> newmap = new HashMap<>();
        newmap.putAll(map);
        newArray[1] = newmap;
        return newArray;
    }

    @SuppressWarnings({ "rawtypes" })
    private String getFilterQuery(Map inputMap, DataControllerRequest dcRequest, String userId) {
        StringBuilder sb = new StringBuilder();
        String transactionType = (String) inputMap.get("transactionType");
        String accountId = (String) inputMap.get("accountID");
        String isScheduled = (String) inputMap.get("isScheduled");

        switch (transactionType) {
            case "All":
                sb.append("(");
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.OR);
                sb.append(DBPUtilitiesConstants.TO_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(")");
                sb.append(DBPUtilitiesConstants.AND);
                sb.append("description").append(DBPUtilitiesConstants.NOT_EQ).append("Request");
                break;
            case "Transfers":
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.AND).append("(");
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("InternalTransfer");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("ExternalTransfer");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("P2P");
                sb.append(")");
                break;
            case "Deposits":
                sb.append(DBPUtilitiesConstants.TO_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.AND).append("(");
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("InternalTransfer");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("ExternalTransfer");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("P2P");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("Deposit");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("Interest");
                sb.append(")");
                break;
            case "Checks":
                sb.append("(");
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.OR);
                sb.append(DBPUtilitiesConstants.TO_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(")");
                sb.append(DBPUtilitiesConstants.AND);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("CheckWithdrawal");
                break;
            case "Wire":
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.AND);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("Wire");
                break;
            case "Loans":
            case "Loan":
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.AND);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("Loan");
                break;
            case "Withdrawals":
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                sb.append(DBPUtilitiesConstants.AND).append("(");
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("Withdrawal");
                sb.append(DBPUtilitiesConstants.OR);
                sb.append("description").append(DBPUtilitiesConstants.EQUAL).append("BillPay");
                sb.append(")");
                break;
            case "Purchases":
                sb.append(DBPUtilitiesConstants.FRM_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                break;
            case "Payments":
            case "Interest":
                sb.append(DBPUtilitiesConstants.TO_ACCONT).append(DBPUtilitiesConstants.EQUAL).append(accountId);
                break;
        }
        sb.append(DBPUtilitiesConstants.AND);
        sb.append("isScheduled").append(DBPUtilitiesConstants.EQUAL).append(isScheduled);
        if (StringUtils.isNotBlank(userId)) {
            sb.append(DBPUtilitiesConstants.AND);
            sb.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
        }
        return sb.toString();
    }
}
