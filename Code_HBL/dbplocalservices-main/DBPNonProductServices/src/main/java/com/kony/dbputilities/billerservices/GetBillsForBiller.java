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

public class GetBillsForBiller implements JavaService2 {
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
            postProcess(dcRequest, inputParams, result);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Map inputParams, Result result) throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            updateAccountName(dcRequest, transaction);
            updatePayeeDetails(dcRequest, transaction);
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

    private void updateAccountName(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String accountNum = HelperMethods.getFieldValue(transaction, "account_id");
        if (StringUtils.isNotBlank(accountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountNum;
            Result account = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String accountName = HelperMethods.getFieldValue(account, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String billerName = (String) inputParams.get("billerName");
        String billerMasterId = getBillerMasterId(dcRequest, billerName);
        String filter = "billermaster_id" + DBPUtilitiesConstants.EQUAL + billerMasterId;
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }

    private String getBillerMasterId(DataControllerRequest dcRequest, String billerName) throws HttpCallException {
        String filter = "name" + DBPUtilitiesConstants.EQUAL + billerName;
        Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_MASTER_GET);
        return HelperMethods.getFieldValue(billerMaster, "id");
    }
}