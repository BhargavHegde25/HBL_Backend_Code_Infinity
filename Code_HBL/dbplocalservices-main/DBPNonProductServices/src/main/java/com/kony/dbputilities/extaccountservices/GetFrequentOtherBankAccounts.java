package com.kony.dbputilities.extaccountservices;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetFrequentOtherBankAccounts implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
            result = postProcess(dcRequest, result);
        }

        return result;
    }

    private Result postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        if (HelperMethods.hasRecords(result)) {
            Result retResult = new Result();
            Dataset resutds = new Dataset("externalaccount");
            Dataset ds = result.getAllDatasets().get(0);
            Set<String> accounts = new HashSet<>();
            int recordNum = 0;
            int size = ds.getAllRecords().size();
            String userID = HelperMethods.getUserIdFromSession(dcRequest);
            while (recordNum < size && resutds.getAllRecords().size() < 5) {
                Record rec = ds.getRecord(recordNum);
                recordNum++;
                String extAccount = HelperMethods.getFieldValue(rec, "toExternalAccountNumber");
                String iBAN = HelperMethods.getFieldValue(rec, DBPInputConstants.IBAN);
                if (StringUtils.isNotBlank(extAccount) && !"0".equals(extAccount) && !accounts.contains(extAccount)) {
                    accounts.add(extAccount);
                    addExternalAccount(resutds, dcRequest, extAccount, null, userID);
                } else if (StringUtils.isNotBlank(iBAN) && !"0".equals(iBAN) && !accounts.contains(iBAN)) {
                    accounts.add(iBAN);
                    addExternalAccount(resutds, dcRequest, null, iBAN, userID);
                }
            }
            retResult.addDataset(resutds);
            return retResult;
        }
        return result;
    }

    private void addExternalAccount(Dataset ds, DataControllerRequest dcRequest, String extAccount, String iBAN,
            String userID) throws HttpCallException {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(extAccount)) {
            sb.append("accountNumber").append(DBPUtilitiesConstants.EQUAL).append(extAccount);
        } else {
            sb.append(DBPInputConstants.IBAN).append(DBPUtilitiesConstants.EQUAL).append(iBAN);
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
            ds.addRecord(account.getAllDatasets().get(0).getRecord(0));
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        StringBuilder filter = new StringBuilder();
		/*
		 * filter.
		 * append("select t1.toExternalAccountNumber,t1.IBAN,tt.description as transactionType from "
		 * + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".transaction t1 left join " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".transactiontype tt on (t1.Type_id = tt.id)");
		 * filter.append(" where ");
		 * filter.append(" (t1.toExternalAccountNumber in (select accountNumber from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".externalaccount where User_id = '" + userId + "')");
		 * filter.append(" OR t1.IBAN in (select IBAN from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".externalaccount where User_id = '" + userId + "'))");
		 * filter.append(" AND tt.description = 'ExternalTransfer' ");
		 * filter.append(" ORDER BY t1.createdDate desc limit 0, 50");
		 */
        filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetFrequentOtherBankAccounts").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userId));
        
        inputParams.put("transactions_query", filter.toString());
        return true;
    }
}
