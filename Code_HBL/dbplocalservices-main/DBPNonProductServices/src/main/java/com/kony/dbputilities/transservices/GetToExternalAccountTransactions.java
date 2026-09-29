package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.SortingUtil;
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

public class GetToExternalAccountTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);

        String sortBy = inputParams.get("sortBy");
        String order = inputParams.get("order");

        if (preProcess(inputParams, dcRequest, result, user)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        } else {
            result.addParam(new Param("errmsg", "Not a valid user", "String"));
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
        if (StringUtils.isNotBlank(sortBy)) {
            SortingUtil util = new SortingUtil(sortBy, order);
            return util.sort(result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        Map<String, String> accountTypes = getAccountTypes(dcRequest, user.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        Param amountSum = new Param("totalAmount", "0", DBPUtilitiesConstants.STRING_TYPE);
        String accountId = inputParams.get("accountNumber");
        for (Record transaction : transactions) {
            addToTotalSum(transaction, amountSum);
            updateToAccountName(dcRequest, transaction, user.get("user_id"));
            transaction.addParam(amountSum);
            updateAmount(accountId, transaction);
            updateFromAccountDetails(dcRequest, transaction, accountTypes);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updatePayeeDetails(dcRequest, transaction);
            updateBillDetails(dcRequest, transaction);
            updatePayPersonDetails(dcRequest, transaction);
            updateCashlessOTPValidDate(transaction);
            updateDateFormat(transaction);
        }
    }

    private void updateDateFormat(Record transaction) {
        String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        String frequencyEndDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
        String frequencyStartDate = HelperMethods.getFieldValue(transaction, "frequencyStartDate");
        try {
            if (StringUtils.isNotBlank(scheduledDate)) {
                transaction.addParam(new Param("scheduledDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(new Param("transactionDate",
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
            if (StringUtils.isNotBlank(frequencyEndDate)) {
                transaction.addParam(new Param("frequencyEndDate",
                        HelperMethods.convertDateFormat(frequencyEndDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
            if (StringUtils.isNotBlank(frequencyStartDate)) {
                transaction.addParam(new Param("frequencyStartDate",
                        HelperMethods.convertDateFormat(frequencyStartDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private void updateBillDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String billId = HelperMethods.getFieldValue(transaction, "billid");
        if (StringUtils.isNotBlank(billId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
            Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(biller)) {
                Record bill = biller.getAllDatasets().get(0).getRecord(0);
                String payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
                String payeeNickName = getPayeeNickName(dcRequest, payeeId);
                transaction.addParam(new Param("payeeNickName", payeeNickName, DBPUtilitiesConstants.STRING_TYPE));
            }
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

    private void updateCashlessOTPValidDate(Record transaction) {
        String otpValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(otpValidDate)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            transaction.addParam(
                    new Param("cashlessOTPValidDate", String.valueOf(timeDiff), DBPUtilitiesConstants.STRING_TYPE));
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

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        transaction.addParam(
                new Param("payeeNickName", getPayeeNickName(dcRequest, payeeId), DBPUtilitiesConstants.STRING_TYPE));
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

    private void updateAmount(String accountId, Record transaction) {
        String frmAccount = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        String description = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isNotBlank(frmAccount) && StringUtils.isNotBlank(accountId)
                && !"InterestCredit".equalsIgnoreCase(description) && accountId.equals(frmAccount)) {
            String amount = "-" + HelperMethods.getFieldValue(transaction, "amount");
            transaction.addParam(new Param("amount", amount, DBPUtilitiesConstants.STRING_TYPE));
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

    private void addToTotalSum(Record transaction, Param amountSum) {
        BigDecimal amount = new BigDecimal(HelperMethods.getFieldValue(transaction, "amount"));
        BigDecimal totalAmt = new BigDecimal(amountSum.getValue());
        amountSum.setValue(amount.add(totalAmt).toPlainString());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) throws HttpCallException {

        boolean status = true;
        if (checkRecordExists(inputParams, dcRequest, result, user)) {
            String toExtAccountNumber = (String) inputParams.get("accountNumber");
            String iBAN = (String) inputParams.get("IBAN");
            String start = (String) inputParams.get("firstRecordNumber");
            String end = (String) inputParams.get("lastRecordNumber");
            String jdbcUrl=QueryFormer.getDBType(dcRequest);
            
            StringBuilder filter = new StringBuilder();
			/*
			 * filter.append("select t1.*,tt.description as transactionType from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id) where");
			 */
            if (StringUtils.isNotBlank(toExtAccountNumber)) {
				/*
				 * filter.append(" t1.toExternalAccountNumber = '" + toExtAccountNumber + "'");
				 */
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ByExternalAccountNumber").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", toExtAccountNumber));
            } else {
				/* filter.append(" t1.IBAN = '" + iBAN + "'"); */
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetTransactionDetails_ByIBAN").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", iBAN));
            }
            if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().
            			replace("?1", start).replace("?2", end));
            }
            inputParams.put("transactions_query", filter.toString());
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    private boolean checkRecordExists(Map inputParams, DataControllerRequest dcRequest, Result result,
            Map<String, String> user) throws HttpCallException {
        boolean status = true;
        String filter = "(" + "accountNumber" + DBPUtilitiesConstants.EQUAL + inputParams.get("accountNumber")
                + DBPUtilitiesConstants.OR + "IBAN" + DBPUtilitiesConstants.EQUAL + inputParams.get("accountNumber")
                + ")" + DBPUtilitiesConstants.AND + "User_id" + DBPUtilitiesConstants.EQUAL + user.get("user_id");

        Result chkresult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXTACCOUNTS_GET);
        if (HelperMethods.hasRecords(chkresult)) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }
}
