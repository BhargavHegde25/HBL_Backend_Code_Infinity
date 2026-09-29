package com.kony.dbputilities.accountservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
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

public class GetRecentAccountsList implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
            result = postProcess(dcRequest, result);
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {

        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if (!StringUtils.isNotBlank(userId)) {
            ErrorCodeEnum.ERR_10178.setErrorCode(result);
            return false;
        }

        // select t.*,tt.Id as typeId,tt.description as
        // TransactionType,at.TypeDescription as AccountType from accounts a inner join
        // transaction t inner join transactiontype tt inner join accounttype at on
        // at.TypeID = a.Type_id
        // where t.fromAccountNumber = a.Account_id and a.User_id='1002496540' and
        // t.Type_id = tt.ID
        // and tt.description in ('InternalTransfer','ExternalTransfer') order by
        // t.transactionDate desc

        StringBuilder filter = new StringBuilder();
		/*
		 * filter.
		 * append("select t.*,at.TypeDescription as AccountType,tt.description as TransactionType  from  "
		 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".accounts a "
		 * + " inner join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
		 * + ".transaction t " + " on t.fromAccountNumber = a.Account_id " +
		 * " inner join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transactiontype tt " + " on t.Type_id = tt.ID " + " inner join " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".accounttype at on at.TypeID = a.Type_id"); filter.append(" where ");
		 * filter.append(" a.User_id=" + "'" + userId + "'"); filter.append(" and ");
		 * filter.
		 * append(" tt.description in ('ExternalTransfer')   order by t.transactionDate desc"
		 * );
		 */
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetRecentAccountsList").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        inputParams.put("transactions_query", filter.toString());
        return true;
    }

    private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        Result retResult = new Result();
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            Dataset resultds = new Dataset();
            resultds.setId("accounttransaction");
            Set<String> accounts = new HashSet<>();
            int recordNum = 0;
            int size = ds.getAllRecords().size();
            int added = 0;
            String userID = HelperMethods.getUserIdFromSession(dcRequest);
            while (recordNum < size && added <= 3) {
                Record rec = ds.getRecord(recordNum);
                recordNum++;
                String transactionDate = HelperMethods.getParamValue(rec.getParam("transactionDate"));
                String accounType = HelperMethods.getParamValue(rec.getParam("AccountType"));
                String transactionType = HelperMethods.getParamValue(rec.getParam("TransactionType"));
                String toAccount = HelperMethods.getParamValue(rec.getParam(DBPUtilitiesConstants.TO_ACCONT));
                String extAccount = HelperMethods.getParamValue(rec.getParam(DBPUtilitiesConstants.TO_EXT_ACCT_NUM));

                if (accounts.contains(extAccount) || accounts.contains(toAccount)) {
                    continue;
                }
                if (StringUtils.isNotBlank(extAccount) && !"0".equals(extAccount)) {
                    accounts.add(extAccount);
                    addExternalAccount(resultds, dcRequest, extAccount, null, userID, transactionDate, transactionType);
                    added++;
                } else if (StringUtils.isNotBlank(toAccount)) {
                    accounts.add(toAccount);
                    addToAccount(resultds, dcRequest, toAccount, userID, transactionDate, accounType, transactionType);
                    added++;
                }

            }
            retResult.addDataset(resultds);
        } else {
            Dataset ds = new Dataset();
            ds.setId("accounttransaction");
            result.addDataset(ds);
            retResult = result;
        }
        return retResult;
    }

    private void addExternalAccount(Dataset ds, DataControllerRequest dcRequest, String extAccount, String iBAN,
            String userID, String transactionDate, String transactionType) throws HttpCallException {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(extAccount)) {
            sb.append("accountNumber").append(DBPUtilitiesConstants.EQUAL).append(extAccount);
        }
        sb.append(DBPUtilitiesConstants.AND);
        sb.append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");

        if (StringUtils.isNotBlank(userID)) {
            sb.append(DBPUtilitiesConstants.AND);

            sb.append("User_id" + DBPUtilitiesConstants.EQUAL + userID);
        }

        Map<String, String> input = new HashMap<>();
        input.put(DBPUtilitiesConstants.FILTER, sb.toString());
        Result account = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_GET);
        if (HelperMethods.hasRecords(account)) {
            Record data = account.getAllDatasets().get(0).getRecord(0);
            Record rec = new Record();
            rec.addParam(new Param("accountNumber", HelperMethods.getFieldValue(data, "accountNumber")));
            rec.addParam(new Param("accountID", HelperMethods.getFieldValue(data, "accountNumber")));
            rec.addParam(new Param("beneficiaryName", HelperMethods.getFieldValue(data, "nickName")));
            rec.addParam(new Param("nickName", HelperMethods.getFieldValue(data, "nickName")));
            rec.addParam(new Param("accountType", HelperMethods.getFieldValue(data, "accountType")));
            rec.addParam(
                    new Param("IsInternationalAccount", HelperMethods.getFieldValue(data, "isInternationalAccount")));
            rec.addParam(new Param("BankName", HelperMethods.getFieldValue(data, "bankName")));
            rec.addParam(new Param("bankName", HelperMethods.getFieldValue(data, "bankName")));
            rec.addParam(new Param("isSameBankAccount", HelperMethods.getFieldValue(data, "isSameBankAccount")));
            rec.addParam(new Param("transactionDate", transactionDate, "String"));
            rec.addParam(new Param("TransactionType", transactionType, "String"));
            rec.addParam(new Param("IBAN",HelperMethods.getFieldValue(data, "IBAN")));

            ds.addRecord(rec);
        }
    }

    private void addToAccount(Dataset ds, DataControllerRequest dcRequest, String toAccount, String userID,
            String transactionDate, String accounType, String transactionType) throws HttpCallException {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(toAccount)) {
            sb.append("Account_id").append(DBPUtilitiesConstants.EQUAL).append(toAccount);
        }

        if (StringUtils.isNotBlank(userID)) {
            sb.append(DBPUtilitiesConstants.AND);

            sb.append("User_id" + DBPUtilitiesConstants.EQUAL + userID);
        }

        Map<String, String> input = new HashMap<>();
        input.put(DBPUtilitiesConstants.FILTER, sb.toString());
        Result account = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNT_GET);
        if (HelperMethods.hasRecords(account)) {
            Record data = account.getAllDatasets().get(0).getRecord(0);
            data.addParam(new Param("transactionDate", transactionDate, "String"));
            data.addParam(new Param("accountType", accounType, "String"));
            data.addParam(new Param("nickName", HelperMethods.getFieldValue(data, "NickName"), "String"));
            data.addParam(new Param("accountName", HelperMethods.getFieldValue(data, "AccountName"), "String"));
            data.addParam(new Param("accountID", HelperMethods.getFieldValue(data, "Account_id"), "String"));
            data.addParam(new Param("currencyCode", HelperMethods.getFieldValue(data, "CurrencyCode"), "String"));
            data.addParam(
                    new Param("availableBalance", HelperMethods.getFieldValue(data, "AvailableBalance"), "String"));
            data.addParam(
                    new Param("accountPreference", HelperMethods.getFieldValue(data, "AccountPreference"), "String"));
            data.addParam(new Param("availableCredit", HelperMethods.getFieldValue(data, "AvailableCredit"), "String"));
            data.addParam(
                    new Param("outstandingBalance", HelperMethods.getFieldValue(data, "OutstandingBalance"), "String"));
            data.addParam(new Param("bankName", "Infinity", "String"));

            if ("InternalTransfer".equals(transactionType)) {
                data.addParam(new Param("TransactionType", "MyOwnAccounts", "String"));
            }
            ds.addRecord(data);
        }
    }
}
