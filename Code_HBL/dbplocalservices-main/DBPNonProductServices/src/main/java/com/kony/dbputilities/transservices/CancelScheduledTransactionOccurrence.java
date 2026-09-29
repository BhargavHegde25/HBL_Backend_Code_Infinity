package com.kony.dbputilities.transservices;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CancelScheduledTransactionOccurrence implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            if (inputParams.containsKey("remove")) {
                HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.TRANSACTION_DELETE);
                if(HelperMethods.hasParam(result, "deletedRecords"))
                	result.addParam(new Param("referenceId", inputParams.get("transactionId").toString()));
            } else {
                HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.TRANSACTION_UPDATE);
                if(HelperMethods.hasParam(result, "updatedRecords"))
                	result.addParam(new Param("referenceId", inputParams.get("transactionId").toString()));
            }
        } else {
            return new Result();
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String transactionId = inputParams.get("transactionId");
        Record transaction = getTransaction(dcRequest, transactionId);
        if (null != transaction) {
            result.addParam(new Param("transactionId", transactionId, DBPUtilitiesConstants.STRING_TYPE));
            String frequencyType = HelperMethods.getFieldValue(transaction, "frequencyType");
            Integer noOfRecurences = Integer.parseInt(HelperMethods.getFieldValue(transaction, "numberOfRecurrences"));
            String description = HelperMethods.getFieldValue(transaction, "recurrenceDesc");
            String frequencyEndDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
            if (null != frequencyType && null != HelperMethods.getFieldValue(transaction, "scheduledDate")
                    && null != HelperMethods.getFieldValue(transaction, "transactionDate")) {
                Date scheduledDate = HelperMethods
                        .getFormattedTimeStamp(HelperMethods.getFieldValue(transaction, "scheduledDate"));
                Date transactionDate = HelperMethods
                        .getFormattedTimeStamp(HelperMethods.getFieldValue(transaction, "transactionDate"));
                Calendar c = Calendar.getInstance();
                Calendar c2 = Calendar.getInstance();
                c.setTime(scheduledDate);
                c2.setTime(transactionDate);
                if ("Daily".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.DATE, 1);
                    c2.add(Calendar.DATE, 1);
                }
                if ("Weekly".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.DATE, 7);
                    c2.add(Calendar.DATE, 7);
                }
                if ("BiWeekly".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.DATE, 14);
                    c2.add(Calendar.DATE, 14);
                }
                if ("Monthly".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.MONTH, 1);
                    c2.add(Calendar.MONTH, 1);
                }
                if ("Yearly".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.YEAR, 1);
                    c2.add(Calendar.YEAR, 1);
                }
                if ("Quarterly".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.MONTH, 3);
                    c2.add(Calendar.MONTH, 3);
                }
                if ("Every Two Weeks".equalsIgnoreCase(frequencyType)) {
                    c.add(Calendar.DATE, 14);
                    c2.add(Calendar.DATE, 14);
                }
                // ARB-6551
                if (description != null && noOfRecurences != null && StringUtils.isBlank(frequencyEndDate)) {
                    String s[] = description.split(" ");
                    if (s[0].matches("[0-9]+") && s[0].length() > 0) {
                        int firstEle = Integer.parseInt(s[0]);
                        firstEle++;
                        if (firstEle > noOfRecurences) {
                            inputParams.put("remove", "true");
                        } else {
                            // inputParams.put("numberOfRecurrences", String.valueOf(firstEle));
                            inputParams.put("recurrenceDesc", firstEle + " of " + noOfRecurences);
                        }
                    } else {
                        if (noOfRecurences < 2) {
                            inputParams.put("remove", "true");
                        } else {
                            // inputParams.put("numberOfRecurrences", "2");
                            inputParams.put("recurrenceDesc", 2 + " of " + noOfRecurences);
                        }
                    }
                }
                // ARB-6551
                if (StringUtils.isNotBlank(frequencyEndDate)) {
                    Date fEndDate = HelperMethods
                            .getFormattedTimeStamp(HelperMethods.getFieldValue(transaction, "frequencyEndDate"));
                    if (c2.getTime().after(fEndDate)) {
                        inputParams.put("remove", "true");
                    }
                }
                // ARB-6551
                if (StringUtils.isNotBlank(frequencyEndDate)) {
                    Date fEndDate = HelperMethods
                            .getFormattedTimeStamp(HelperMethods.getFieldValue(transaction, "frequencyEndDate"));
                    if (c2.getTime().after(fEndDate)) {
                        inputParams.put("remove", "true");
                    }
                }
                inputParams.put("Id", transactionId);
                inputParams.put("scheduledDate", HelperMethods.getFormattedTimeStamp(c.getTime(), null));
                inputParams.put("transactionDate", HelperMethods.getFormattedTimeStamp(c2.getTime(), null));
            }
        }
        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        String userid = user.get("user_id");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        String customerType = user.get("customerType");
        if (StringUtils.isNotBlank(userid)) {
            StringBuffer filter = new StringBuffer();
            if (HelperMethods.isBusinessUserType(customerType)){
				/*
				 * filter.append("select t1.Id from " +
				 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
				 * ".transaction t1 ");
				 * filter.append("where (t1.FromAccountNumber in (select account_id from " +
				 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
				 * ".customeraccounts where Customer_id = '" + userid + "')");
				 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
				 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
				 * ".customeraccounts where Customer_id = '" + userid + "')) and t1.Id =" +
				 * transactionId);
				 */
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_CancelScheduledTransactionOccurrence_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userid).replace("?3", transactionId));
                Map<String, String> proc = new HashMap<>();
                proc.put("transactions_query", filter.toString());
                Result checkResult = HelperMethods.callApi(dcRequest, proc, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_TRANSACTION_PROC);
                if (HelperMethods.hasRecords(checkResult)) {
                    return true;
                } else {
                    result = new Result();
                    return false;
                }
            } else {
				/*
				 * filter.append("select t1.Id from " +
				 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
				 * ".transaction t1 ");
				 * filter.append("where (t1.FromAccountNumber in (select account_id from " +
				 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
				 * ".accounts where User_id = '" + userid + "')");
				 * filter.append(" or t1.ToAccountNumber in (select account_id from " +
				 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
				 * ".accounts where User_id = '" + userid + "')) and t1.Id =" + transactionId);
				 */
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_CancelScheduledTransactionOccurrence_ByCustomerId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userid).replace("?3", transactionId));
                Map<String, String> proc = new HashMap<>();
                proc.put("transactions_query", filter.toString());
                Result checkResult = HelperMethods.callApi(dcRequest, proc, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_TRANSACTION_PROC);
                if (HelperMethods.hasRecords(checkResult)) {
                    return true;
                } else {
                    result = new Result();
                    return false;
                }
            }
        }
        return null != transaction;
    }

    private Record getTransaction(DataControllerRequest dcRequest, String transactionId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + transactionId;
        Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);
        return payee.getAllDatasets().get(0).getRecord(0);
    }
}
