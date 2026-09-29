package com.kony.dbputilities.transservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeleteTransaction implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            if (StringUtils.isNotBlank(inputParams.get("isDisputed"))) {
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.TRANSACTION_UPDATE);
                if(HelperMethods.hasParam(result, "updatedRecords"))
                	result.addParam(new Param("referenceId", inputParams.get("transactionId").toString()));
            } else {
            	if("MSSQL".equalsIgnoreCase(QueryFormer.getDBType(dcRequest)))
            	{
                	
            		result=TransOperations.createDataSetDelete(dcRequest, inputParams);
            	}
                else { 
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.TRANSACTION_DELETE);
                }
                if(HelperMethods.hasParam(result, "deletedRecords"))
                	result.addParam(new Param("referenceId", inputParams.get("transactionId").toString()));
            }
            if (!HelperMethods.hasError(result)) {
                result.addParam(new Param("transactionId", inputParams.get("transactionId"), MWConstants.STRING));
            }
        }
        return result;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = false;
        String transactionId = inputParams.get("transactionId");
        Record transaction = getTransaction(dcRequest, transactionId);
        String isDisputed = HelperMethods.getFieldValue(transaction, "isDisputed");
        String typeId = HelperMethods.getFieldValue(transaction, "Type_id");
        String typeDescription = getTypeDescription(dcRequest, typeId);
        String cashlessMode = HelperMethods.getFieldValue(transaction, "cashlessMode");
        String statusDesc = HelperMethods.getFieldValue(transaction, "statusDesc");
        String isScheduled = HelperMethods.getFieldValue(transaction, "isScheduled");
        inputParams.put("Id", transactionId);
        if (StringUtils.isNotBlank(cashlessMode)
                || DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST.equals(typeDescription)) {
            status = true;
        } else if ("true".equalsIgnoreCase(isDisputed)) {
            inputParams.put("isDisputed", "false");
            // inputParams.put("disputeDate", null);
            inputParams.put("disputeStatus", null);
            inputParams.put("disputeReason", null);
            inputParams.put("disputeDescription", null);
            status = true;
        } else if ("false".equalsIgnoreCase(isScheduled)
                || DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL.equals(statusDesc)
                || DBPUtilitiesConstants.TRANSACTION_STATUS_FAILED.equals(statusDesc)
                || DBPUtilitiesConstants.TRANSACTION_STATUS_CANCELLED.equals(statusDesc)
                || DBPUtilitiesConstants.TRANSACTION_TYPE_DEPOSIT.equals(typeDescription)) {
            // HelperMethods.setValidationMsg("Edit operation not permitted for this
            // Transaction type", dcRequest, result);
            ErrorCodeEnum.ERR_12438.setErrorCode(result);

        } else {
            status = true;
        }

        Map<String, String> user = HelperMethods.getUserFromIdentityService(dcRequest);
        String userid = user.get("user_id");
        String customerType = user.get("customerType");
        String jdbcUrl=QueryFormer.getDBType(dcRequest);
        if (StringUtils.isNotBlank(userid)) {
            StringBuffer filter = new StringBuffer();
            
            if (HelperMethods.isBusinessUserType(customerType)) {
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
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_DeleteTransaction_ByCustomererId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userid).replace("?3", transactionId));
                Map<String, String> proc = new HashMap<>();
                proc.put("transactions_query", filter.toString());
                Result checkResult = HelperMethods.callApi(dcRequest, proc, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_TRANSACTION_PROC);
                if (HelperMethods.hasRecords(checkResult)) {
                    status = true;
                } else {
                    status = false;
                    ErrorCodeEnum.ERR_12437.setErrorCode(result);
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
            	filter.append(SqlQueryEnum.valueOf(jdbcUrl+"_DeleteTransaction_ByCustomererId").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", userid).replace("?3", transactionId));
                Map<String, String> proc = new HashMap<>();
                proc.put("transactions_query", filter.toString());
                Result checkResult = HelperMethods.callApi(dcRequest, proc, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_TRANSACTION_PROC);
                if (HelperMethods.hasRecords(checkResult)) {
                    status = true;
                } else {
                    status = false;
                    ErrorCodeEnum.ERR_12437.setErrorCode(result);
                }
            }
        }
        return status;
    }

    private String getTypeDescription(DataControllerRequest dcRequest, String typeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + typeId;
        Result type = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        return HelperMethods.getFieldValue(type, "description");
    }

    private Record getTransaction(DataControllerRequest dcRequest, String transactionId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + transactionId;
        Result transaction = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_GET);
        return transaction.getAllDatasets().get(0).getRecord(0);
    }
}
