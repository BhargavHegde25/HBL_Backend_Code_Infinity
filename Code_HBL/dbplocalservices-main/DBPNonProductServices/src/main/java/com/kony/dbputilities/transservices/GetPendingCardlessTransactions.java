package com.kony.dbputilities.transservices;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

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

public class GetPendingCardlessTransactions implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_PROC);
        }
        if (HelperMethods.hasRecords(result)) {
            result = postProcess(dcRequest, result);
        }
        return result;
    }

    private Result postProcess(DataControllerRequest dcRequest, Result result) {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        Result retSet = new Result();
        Dataset ds = new Dataset(result.getAllDatasets().get(0).getId());
        retSet.addDataset(ds);
        int count = 0;
        for (Record transaction : transactions) {
            if (shouldTransactionBeAdded(transaction)) {
                count++;
                updateAccountName(dcRequest, transaction);
                updateNullValues(transaction);
                updateCheckImage(transaction);
                updateCashlessOTPValidDate(transaction);
                updateDateFormat(transaction);
                ds.addRecord(transaction);
            }
            if (count >= 15) {
                break;
            }
        }
        return retSet;
    }

    private void updateDateFormat(Record transaction) {
        try {
            HelperMethods.updateDateFormat(transaction, "scheduledDate");
            HelperMethods.updateDateFormat(transaction, "transactionDate");
            HelperMethods.updateDateFormat(transaction, "frequencyEndDate");
            HelperMethods.updateDateFormat(transaction, "frequencyStartDate");
        } catch (Exception e) {

        }
    }

    private boolean shouldTransactionBeAdded(Record transaction) {
        String otpValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(otpValidDate)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            return timeDiff > 0;
        }
        return false;
    }

    private void updateCashlessOTPValidDate(Record transaction) {
        String otpValidDate = HelperMethods.getFieldValue(transaction, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(otpValidDate)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            timeDiff = timeDiff / 1000;
            long m = (timeDiff / 60) % 60;
            long h = (timeDiff / (60 * 60)) % 24;
            transaction.addParam(new Param("cashlessOTPValidDate", (String.valueOf(h) + "h:" + String.valueOf(m) + "m"),
                    DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private void updateCheckImage(Record transaction) {
        transaction.addParam(new Param("checkImage", "https://retailbanking1.konycloud.com/dbimages/check_front.png",
                DBPUtilitiesConstants.STRING_TYPE));
        transaction.addParam(new Param("checkImageBack", "https://retailbanking1.konycloud.com/dbimages/check_back.png",
                DBPUtilitiesConstants.STRING_TYPE));
    }

    private void updateNullValues(Record transaction) {
        String transDesc = HelperMethods.getFieldValue(transaction, "description");
        if (StringUtils.isBlank(transDesc)) {
            transDesc = "None";
            transaction.addParam(new Param("description", transDesc, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        StringBuilder filter = new StringBuilder();
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        if (HelperMethods.isBusinessUserType(user.get("customerType"))) {
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPendingCardlessTransactions_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        } else {
        	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPendingCardlessTransactions_ByUserId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", user.get("customer_id")));
        }
        inputParams.put("transactions_query", filter.toString());
        return true;
    }

    private void updateAccountName(DataControllerRequest dcRequest, Record transaction) {
        String accountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(accountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNum;
            Result account = null;
            try {
                account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNTS_GET);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
            if (HelperMethods.hasRecords(account)) {
                String accountName = HelperMethods.getFieldValue(account, "accountName");
                transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

}
