package com.kony.dbputilities.transservices;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.exceptions.HttpCallException;
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

public class UpdateTransaction implements JavaService2 {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
        if("MSSQL".equalsIgnoreCase(QueryFormer.getDBType(dcRequest)))
    	{
        	
    		result=TransOperations.createDataSetUpdate(dcRequest, inputParams);
    	}
        else {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_UPDATE);}
            if(HelperMethods.hasParam(result, "updatedRecords"))
            	result.addParam(new Param("referenceId", inputParams.get("transactionId").toString()));
        }
        
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws ParseException, HttpCallException {

        /*String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);

        if (StringUtils.isBlank(customerId)) {
            return false;
        } */

        boolean status = true;
        Date d = new Date();
        String id = (String) inputParams.get("transactionId");
        String type = (String) inputParams.get("transactionType");
        String toAccountNumber = (String) inputParams.get("toAccountNumber");
        inputParams.put("notes",inputParams.get("transactionsNotes"));
        if (DBPUtilitiesConstants.TRANSACTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(type)) {
        	inputParams.put(DBPUtilitiesConstants.TO_EXT_ACCT_NUM, toAccountNumber);
        	inputParams.put(DBPUtilitiesConstants.TO_ACCONT, null);
        }
        
        String personId = (String) inputParams.get("personId");
        String payeeId = (String) inputParams.get("payeeId");
        inputParams.get("fromAccountNumber");
        String scheduledDt = (String) inputParams.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        String disputeDescription = (String) inputParams.get("disputeDescription");
        String disputeReason = (String) inputParams.get("disputeReason");

        if ("Deposit".equalsIgnoreCase(type)) {
            HelperMethods.setValidationMsg("Edit operation not permitted for this Transaction type", dcRequest, result);
            status = false;
        }
        inputParams.put(DBPUtilitiesConstants.T_TRANS_ID, id);
        inputParams.put("Payee_id", payeeId);
        String frequencyType = (String) inputParams.get(DBPUtilitiesConstants.FREQUENCY_TYPE);
        if (StringUtils.isNotBlank(disputeReason) || StringUtils.isNotBlank(disputeDescription)) {
            inputParams.put("isDisputed", "true");
            inputParams.put("disputeDate", HelperMethods.getCurrentTimeStamp());
            inputParams.put("disputeStatus", "In-Progress");
        } else if (StringUtils.isBlank(personId) && StringUtils.isBlank(payeeId) && !isUpdateAllowed(dcRequest, inputParams)) {
            HelperMethods.setValidationMsg("Edit operation not permitted for this Transaction type", dcRequest, result);
            status = false;
        }

        if (status) {
            inputParams.put("transactionDate", HelperMethods.getCurrentTimeStamp());
            if (StringUtils.isNotBlank(scheduledDt)) {
                Date schedDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
                if (schedDate.after(d) || !"Once".equalsIgnoreCase(frequencyType)) {
                    inputParams.put(DBPUtilitiesConstants.STATUS_DESC,
                            DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
                    inputParams.put(DBPUtilitiesConstants.IS_SCHEDULED, "true");
                    inputParams.put("fromAccountBalance",
                            getAvailableBalance(dcRequest, (String) inputParams.get("fromAccountNumber"), null));
                    String noOfRecurrences = (String) inputParams.get(DBPUtilitiesConstants.NO_OF_RECURRENCES);
                    if (StringUtils.isNotBlank(noOfRecurrences)) {
                        inputParams.put(DBPUtilitiesConstants.RECUR_DESC, "1 of " + noOfRecurrences);
                    }
                } else {
                    inputParams.put(DBPUtilitiesConstants.IS_SCHEDULED, "false");
                    inputParams.put(DBPUtilitiesConstants.STATUS_DESC,
                            DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);  
                    String amount = (String) inputParams.get("amount");
                    if (StringUtils.isNotBlank(amount)) {
                        String availableBal = getAvailableBalance(dcRequest,
                                (String) inputParams.get("fromAccountNumber"), null);
                        BigDecimal newBal = new BigDecimal(availableBal).subtract(new BigDecimal(amount));
                        if (newBal.signum() >= 0) {
                            inputParams.put("fromAccountBalance", newBal.toPlainString());
                            updateAccounts(dcRequest, inputParams, newBal);
                        } else {
                            HelperMethods.setValidationMsg("Insufficient Balance to make this transfer", dcRequest,
                                    result);
                            status = false;
                        }
                    }
                }
            }
        }
        if (status) {
            HelperMethods.removeNullValues(inputParams);
        }

        return status;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void updateAccounts(DataControllerRequest dcRequest, Map inputParams, BigDecimal newBal)
            throws HttpCallException {
        String fromAccount = (String) inputParams.get("fromAccountNumber");
        String toAccount = (String) inputParams.get("toAccountNumber");
        String iBAN = (String) inputParams.get("IBAN");
        Map input = new HashMap();
        input.put("Account_id", fromAccount);
        input.put("AvailableBalance", newBal.toPlainString());
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
        if (StringUtils.isNotBlank(toAccount)) {
            String toAccountBal = getAvailableBalance(dcRequest, toAccount, iBAN);

            if (StringUtils.isNotBlank(toAccountBal)) {
                String toBal = new BigDecimal(toAccountBal).add(newBal).toPlainString();
                if (StringUtils.isNotBlank(toAccount)) {
                    input.put("Account_id", toAccount);
                } else {
                    input.put("IBAN", iBAN);
                }
                input.put("AvailableBalance", toBal);
                HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNTS_UPDATE);
                inputParams.put("toAccountBalance", toBal);
            }
        }
    }

    private boolean isUpdateAllowed(DataControllerRequest dcRequest, @SuppressWarnings("rawtypes") Map inputParams)
            throws HttpCallException {
        String id = (String) inputParams.get("transactionId");
        String fromAccountNumber = (String) inputParams.get("fromAccountNumber");
        //String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);

        if (!StringUtils.isNotBlank(fromAccountNumber)) {
            return false;
        }
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNumber;
        Result accounts = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNT_GET);
        if (!HelperMethods.hasRecords(accounts)) {
            return false;
        }
        
        /*String userId = HelperMethods.getFieldValue(accounts, "User_id");
        if (!customerId.equals(userId)) {
            return false;
        }
        */

        String query = DBPUtilitiesConstants.T_TRANS_ID + DBPUtilitiesConstants.EQUAL + id;
        Result result = HelperMethods.callGetApi(dcRequest, query, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);

        String isScheduled = "";
        String statusDesc = "";
        if (HelperMethods.hasRecords(result)) {
            Dataset ds = result.getAllDatasets().get(0);
            Record rec = ds.getRecord(0);
            isScheduled = HelperMethods.getFieldValue(rec, DBPUtilitiesConstants.IS_SCHEDULED);
            statusDesc = HelperMethods.getFieldValue(rec, DBPUtilitiesConstants.STATUS_DESC);
        }
        boolean status = true;
        if ("false".equalsIgnoreCase(isScheduled) || "0".equalsIgnoreCase(isScheduled)
                || statusDesc.equals(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL)
                || statusDesc.equals(DBPUtilitiesConstants.TRANSACTION_STATUS_FAILED)
                || statusDesc.equals(DBPUtilitiesConstants.TRANSACTION_STATUS_CANCELLED)) {
            status = false;
        }
        return status;
    }

    public String getAvailableBalance(DataControllerRequest dcRequest, String accountId, String iBAN)
            throws HttpCallException {
        String filter = "";
        if (StringUtils.isNotBlank(iBAN)) {
            filter += "IBAN" + DBPUtilitiesConstants.EQUAL + iBAN;
        } else {
            filter += DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL + accountId;
        }

        Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        return HelperMethods.getFieldValue(rs, DBPUtilitiesConstants.AVAILABLE_BAL_S);
    }
}
