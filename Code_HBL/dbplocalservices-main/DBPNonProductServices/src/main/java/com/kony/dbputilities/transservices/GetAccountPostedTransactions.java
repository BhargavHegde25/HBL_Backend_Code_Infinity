package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.util.Date;
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

public class GetAccountPostedTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        Map<String, String> accountTypes = getAccountTypes(dcRequest, user.get("countryCode"));
        String accountId = inputParams.get("accountID");
        for (Record transaction : transactions) {
            if (shouldTransactionBeAdded(transaction)) {
                updateToAccountName(dcRequest, transaction, user.get("user_id"));
                updateCheckImage(transaction);
                updateCashlessOTPValidDate(transaction);
                updateNullFieldValues(transaction);
                updateAmount(accountId, transaction);
                updateFromAccountDetails(dcRequest, transaction, accountTypes);
                updateToAccountDetails(dcRequest, transaction, accountTypes);
                updatePayeeDetails(dcRequest, transaction);
                updateBillDetails(dcRequest, transaction);
                updatePayPersonDetails(dcRequest, transaction);
                updateCheckNumber(transaction);
                updateTotalChkAmount(transaction);
                updateCashWithdrawalTransactionStatus(transaction);
                updateDateFormat(transaction);
            }
        }
    }
    
    private void updateDateFormat(Record transaction) {
        try {
            HelperMethods.updateDateFormat(transaction, "scheduledDate");
            HelperMethods.updateDateFormat(transaction, "transactionDate");
            HelperMethods.updateDateFormat(transaction, "postedDate");
            HelperMethods.updateDateFormat(transaction, "frequencyEndDate");
            HelperMethods.updateDateFormat(transaction, "frequencyStartDate");
        } catch (Exception e) {

        }
    }

    private void updateCashWithdrawalTransactionStatus(Record transaction) {
        String description = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isNotBlank(description) && "Cardless".equals(description)) {
            transaction.addParam(new Param("cashWithdrawalTransactionStatus", "posted", "String"));
        }
    }

    private void updateTotalChkAmount(Record transaction) {
        String amt1 = HelperMethods.getFieldValue(transaction, "withdrawlAmount1");
        String amt2 = HelperMethods.getFieldValue(transaction, "withdrawlAmount2");
        String amt3 = HelperMethods.getFieldValue(transaction, "cashAmount");
        String total = "0";
        if (StringUtils.isNotBlank(amt1) && StringUtils.isNotBlank(amt2) && StringUtils.isNotBlank(amt3)) {
            total = new BigDecimal(amt1).add(new BigDecimal(amt2)).add(new BigDecimal(amt3)).toString();
        }
        transaction.addParam(new Param("totalCheckAmount", total, "String"));
    }

    private void updateCheckNumber(Record transaction) {
        String chkNum1 = HelperMethods.getFieldValue(transaction, "checkNumber1");
        String chkNum2 = HelperMethods.getFieldValue(transaction, "checkNumber2");
        transaction.addParam(new Param("checkNumber1", "Check #" + chkNum1, "String"));
        transaction.addParam(new Param("checkNumber2", "Check #" + chkNum2, "String"));
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
        String billId = HelperMethods.getFieldValue(transaction, "billid");
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
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(new Param("payeeAccountNumber",
                        HelperMethods.getFieldValue(payee, "accountNumber"), DBPUtilitiesConstants.STRING_TYPE));
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
                transaction.addParam(new Param("payeeAddressLine1", HelperMethods.getFieldValue(payee, "addressLine1"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("payeeAddressLine2", HelperMethods.getFieldValue(payee, "addressLine2"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("cityName", HelperMethods.getFieldValue(payee, "cityName"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("state", HelperMethods.getFieldValue(payee, "state"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("zipCode", HelperMethods.getFieldValue(payee, "zipCode"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("bankAddressLine1",
                        HelperMethods.getFieldValue(payee, "bankAddressLine1"), DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("bankAddressLine2",
                        HelperMethods.getFieldValue(payee, "bankAddressLine2"), DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("country", HelperMethods.getFieldValue(payee, "country"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("swiftCode", HelperMethods.getFieldValue(payee, "swiftCode"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("bankCity", HelperMethods.getFieldValue(payee, "bankCity"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("bankState", HelperMethods.getFieldValue(payee, "bankState"),
                        DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("bankZip", HelperMethods.getFieldValue(payee, "bankZip"),
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

    private boolean shouldTransactionBeAdded(Record transaction) {
        String otpValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        String description = HelperMethods.getFieldValue(transaction, "transactionType");
        if (StringUtils.isNotBlank(otpValidDate) && StringUtils.isNotBlank(description)
                && "Cardless".equals(description)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            return timeDiff < 0;
        }
        return true;
    }

    private void updateCashlessOTPValidDate(Record transaction) {
        String otpValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(otpValidDate)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            if (timeDiff > 0) {
                timeDiff = timeDiff / 1000;
                long m = (timeDiff / 60) % 60;
                long h = (timeDiff / (60 * 60)) % 24;
                transaction.addParam(new Param("cashlessOTPValidDate",
                        (String.valueOf(h) + "h:" + String.valueOf(m) + "m"), DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private void updateCheckImage(Record transaction) {
        String value = HelperMethods.getFieldValue(transaction, "transactionType");
        if ("Deposit".equalsIgnoreCase(value)) {
            transaction
                    .addParam(new Param("checkImage", "https://retailbanking1.konycloud.com/dbimages/check_front.png",
                            DBPUtilitiesConstants.STRING_TYPE));
            transaction.addParam(new Param("checkImageBack",
                    "https://retailbanking1.konycloud.com/dbimages/check_back.png", DBPUtilitiesConstants.STRING_TYPE));
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
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String accountId = (String) inputParams.get("accountID");
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "transactionDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        StringBuilder queryBuf = new StringBuilder();
        /*
         * sb.append("(").append(DBPUtilitiesConstants.FRM_ACCONT)
         * .append(DBPUtilitiesConstants.EQUAL).append(accountId); sb.append(DBPUtilitiesConstants.OR);
         * sb.append(DBPUtilitiesConstants.TO_ACCONT) .append(DBPUtilitiesConstants.EQUAL).append(accountId)
         * .append(")"); sb.append(DBPUtilitiesConstants.AND);
         * sb.append("isScheduled").append(DBPUtilitiesConstants.EQUAL) .append("false");
         * sb.append(DBPUtilitiesConstants.AND); sb.append("StatusDesc").append(DBPUtilitiesConstants.EQUAL)
         * .append("Successful");
         */
		/*
		 * queryBuf.
		 * append("select transaction.*,transactiontype.description as transactionType from "
		 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction left outer join " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transactiontype on transaction.Type_id=transactiontype.id where ((transaction.FromAccountNumber = "
		 * ); queryBuf.append(accountId);
		 * queryBuf.append(" or transaction.ToAccountNumber = ");
		 * queryBuf.append(accountId);
		 * queryBuf.append(") and transaction.isScheduled = "); queryBuf.append("0");
		 * queryBuf.append(" and transaction.statusDesc = 'Successful'");
		 * queryBuf.append(") ORDER BY " + sortBy + " " + order);
		 */
        queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetAccountPostedTransactionsPreview").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", accountId).replace("?3", sortBy).replace("?4", order));
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
            queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().replace("?1",offset).replace("?2",limit));
        }
        /*
         * String id = HelperMethods.getCustomerIdFromSession(dcRequest); if(StringUtils.isNotBlank(id)) {
         * sb.append(DBPUtilitiesConstants.AND); sb.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(id); }
         */
        /*
         * if (StringUtils.isNotBlank(offset)) { inputParams.put(DBPUtilitiesConstants.SKIP, offset); } if
         * (StringUtils.isNotBlank(limit)) { inputParams.put(DBPUtilitiesConstants.TOP, limit); }
         * inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString()); inputParams.put(DBPUtilitiesConstants.ORDERBY,
         * sortBy+ " " + order);
         */
        inputParams.put("transactions_query", queryBuf.toString());

        return true;
    }
}
