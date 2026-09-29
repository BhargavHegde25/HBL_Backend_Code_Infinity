package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.util.HashMap;
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

public class GetDisputedTransactions implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
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

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        Map<String, String> accountTypes = getAccountTypes(dcRequest, inputParams.get("countryCode"));
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateFromAccountDetails(dcRequest, transaction, accountTypes);
            updateToAccountDetails(dcRequest, transaction, accountTypes);
            updateCheckNumber(transaction);
            updateTotalChkAmount(transaction);
            formatNSetDisputeDate(transaction);
            updatePayPersonDetails(dcRequest, transaction);
            updatePayeeDetails(dcRequest, transaction);
            updateBillDetails(dcRequest, transaction);
            updateDateFormat(transaction);
            transaction.addParam(new Param("disputeStatus", "In-Progress", DBPUtilitiesConstants.STRING_TYPE));
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

    private void formatNSetDisputeDate(Record transaction) {
        String ddate = HelperMethods.getFieldValue(transaction, "disputeDate").split("\\.")[0];
        String[] temp = ddate.split(" ");
        transaction.addParam(new Param("disputeDate", temp[0] + "T" + temp[1], DBPUtilitiesConstants.STRING_TYPE));
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
            Map<String, String> user) {
        String offset = (String) inputParams.get("offset");
        String limit = (String) inputParams.get("limit");
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        inputParams.put("countryCode", user.get("countryCode"));
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "disputeDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        String userId = user.get("user_id");
        StringBuilder queryBuf = new StringBuilder();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        
        queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetDisputedTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        
        /*if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
			
			 * queryBuf.
			 * append(" select transaction.*,transactiontype.description as transactionType from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) +
			 * ".transactiontype on (transaction.Type_id = transactiontype.Id)"); queryBuf.
			 * append(" where (transaction.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "')");
			 * queryBuf.append(" or transaction.ToAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".customeraccounts where Customer_id = '" + userId + "') ");
			 * queryBuf.append(" )  and transaction.isDisputed='1'");
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetDisputedTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        } else {
			
			 * queryBuf.
			 * append(" select transaction.*,transactiontype.description as transactionType from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".transaction left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
			 * dcRequest) +
			 * ".transactiontype on (transaction.Type_id = transactiontype.Id)"); queryBuf.
			 * append(" where (transaction.FromAccountNumber in (select account_id from " +
			 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "')");
			 * queryBuf.append(" or transaction.ToAccountNumber in (select account_id from "
			 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
			 * ".accounts where User_id = '" + userId + "') ");
			 * queryBuf.append(" )  and transaction.isDisputed='1'");
			 
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetDisputedTransactions_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        }*/
        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
            queryBuf.append(" order by " + sortBy + " " + order + " ");
        }
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
        	queryBuf.append(SqlQueryEnum.valueOf(jdbcUrl+"_Limit").getQuery().
        			replace("?1", offset).replace("?2", limit));
        }
        inputParams.put("transactions_query", queryBuf.toString());
        return true;
    }

}
