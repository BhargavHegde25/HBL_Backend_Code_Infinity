package com.kony.dbputilities.billerservices;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPreviousBillsForBiller implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
        List<Record> bills = result.getAllDatasets().get(0).getAllRecords();
        for (Record bill : bills) {
            updateAccountName(dcRequest, bill);
            updatePayeeDetails(dcRequest, bill);
        }
    }

    private void updateAccountName(DataControllerRequest dcRequest, Record bill) throws HttpCallException {
        String accountNum = HelperMethods.getFieldValue(bill, "account_id");
        if (StringUtils.isNotBlank(accountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNum;
            Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String accountName = HelperMethods.getFieldValue(account, "accountName");
            bill.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
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
                String nickName = HelperMethods.getFieldValue(payee, "nickName");
                transaction.addParam(new Param("payeeName", nickName, DBPUtilitiesConstants.STRING_TYPE));
                String eBillEnable = HelperMethods.getFieldValue(payee, "eBillEnable");
                transaction.addParam(new Param("ebillStatus", eBillEnable, DBPUtilitiesConstants.STRING_TYPE));
                String billMasterId = HelperMethods.getFieldValue(payee, "billermaster_id");
                String categoryName = getCategoryName(dcRequest, billMasterId);
                transaction.addParam(new Param("billerCategory", categoryName, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private String getCategoryName(DataControllerRequest dcRequest, String billMasterId) throws HttpCallException {
        String billerCategoryId = getBillerCategoryId(dcRequest, billMasterId);
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billerCategoryId;
        Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_CATEGORY_GET);
        return HelperMethods.getFieldValue(billerMaster, "cattegoryName");
    }

    private String getBillerCategoryId(DataControllerRequest dcRequest, String billMasterId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billMasterId;
        Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_MASTER_GET);
        return HelperMethods.getFieldValue(billerMaster, "billerCategoryId");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String today = HelperMethods.getCurrentTimeStamp();
        String payeeId = (String) inputParams.get("PayeeId");
        StringBuilder filter = new StringBuilder();
        filter.append("Payee_id").append(DBPUtilitiesConstants.EQUAL).append(payeeId);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("billDueDate").append(DBPUtilitiesConstants.LESS_EQUAL).append(today);
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        inputParams.put(DBPUtilitiesConstants.ORDERBY, "billDueDate");
        return true;
    }
}