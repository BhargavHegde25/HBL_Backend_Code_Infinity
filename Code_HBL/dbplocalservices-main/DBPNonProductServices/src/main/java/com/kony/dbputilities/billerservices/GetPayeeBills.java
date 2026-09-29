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
import com.temenos.dbx.product.commonsutils.CustomerSession;

public class GetPayeeBills implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.TRANSACTION_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result, inputParams);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(DataControllerRequest dcRequest, Result result, Map inputParams) throws HttpCallException {
        List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
        for (Record transaction : transactions) {
            String payeeId = HelperMethods.getFieldValue(transaction, "Payee_id");

            Record payee = getPayeeDetails(dcRequest, payeeId);
            createAndaddParam(transaction, "payeeNickName", HelperMethods.getFieldValue(payee, "nickName"));
            createAndaddParam(transaction, "payeeName", HelperMethods.getFieldValue(payee, "name"));
            createAndaddParam(transaction, "payeeAccountNumber", HelperMethods.getFieldValue(payee, "accountNumber"));
            createAndaddParam(transaction, "eBillEnable", HelperMethods.getFieldValue(payee, "paidDate"));
            String billMasterId = HelperMethods.getFieldValue(payee, "billermaster_id");
            if (StringUtils.isNotBlank(billMasterId)) {
                createAndaddParam(transaction, "ebillSupport", getEbillSupport(dcRequest, billMasterId));
            }
            String frmAccount = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
            if (StringUtils.isNotBlank(frmAccount)) {
                Record frmAccountdetails = getAccountDetails(dcRequest, frmAccount);
                createAndaddParam(transaction, "fromAccountName",
                        HelperMethods.getFieldValue(frmAccountdetails, "accountName"));
            }
            String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
            if (StringUtils.isNotBlank(billId)) {
                Record bill = getBillDetails(dcRequest, billId);
                createAndaddParam(transaction, "billDueAmount", HelperMethods.getFieldValue(bill, "dueAmount"));
                createAndaddParam(transaction, "billGeneratedDate",
                        HelperMethods.getFieldValue(bill, "billGeneratedDate"));
                createAndaddParam(transaction, "paidDate", HelperMethods.getFieldValue(bill, "paidDate"));
                createAndaddParam(transaction, "paidAmount", HelperMethods.getFieldValue(bill, "paidAmount"));
                createAndaddParam(transaction, "billDueDate", HelperMethods.getFieldValue(bill, "billDueDate"));
                createAndaddParam(transaction, "ebillURL", HelperMethods.getFieldValue(bill, "ebillURL"));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String typeId = getTransTypeId(dcRequest, "BillPay");
        String payeeId = (String) inputParams.get("payeeId");
        String limit = (String) inputParams.get("limit");
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		String legalEntityId = (String) customer.get("legalEntityId");	
        StringBuilder filter = new StringBuilder();

        filter.append("User_Id").append(DBPUtilitiesConstants.EQUAL)
                .append(HelperMethods.getCustomerIdFromSession(dcRequest)).append(DBPUtilitiesConstants.AND)
                .append("Id").append(DBPUtilitiesConstants.EQUAL).append(payeeId);

        Result result2 = HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);

        if (!HelperMethods.hasRecords(result2)) {
            return false;
        }

        filter = new StringBuilder();
        filter.append("Type_id").append(DBPUtilitiesConstants.EQUAL).append(typeId);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("Payee_id").append(DBPUtilitiesConstants.EQUAL).append(payeeId);
        
        if (StringUtils.isNotBlank(legalEntityId)) {
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("legalEntityId").append(DBPUtilitiesConstants.EQUAL).append(legalEntityId);
        }
        
        if (StringUtils.isNotBlank(limit)) {
            inputParams.put(DBPUtilitiesConstants.TOP, limit);
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        inputParams.put(DBPUtilitiesConstants.ORDERBY, "createdDate desc");
        return true;
    }

    public String getTransTypeId(DataControllerRequest dcRequest, String transType) throws HttpCallException {
        String filterQuery = DBPUtilitiesConstants.TRANS_TYPE_DESC + DBPUtilitiesConstants.EQUAL + transType;
        Result rs = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        return HelperMethods.getFieldValue(rs, DBPUtilitiesConstants.T_TRANS_ID);
    }

    private Record getPayeeDetails(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        if (HelperMethods.hasRecords(result)) {
            return result.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private Record getAccountDetails(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        if (HelperMethods.hasRecords(result)) {
            return result.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private Record getBillDetails(DataControllerRequest dcRequest, String billId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_GET);
        if (HelperMethods.hasRecords(result)) {
            return result.getAllDatasets().get(0).getRecord(0);
        }
        return null;
    }

    private String getEbillSupport(DataControllerRequest dcRequest, String billMasterId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billMasterId;
        Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_MASTER_GET);
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "ebillSupport");
        }
        return null;
    }

    private void createAndaddParam(Record record, String paramName, String paramValue) {
        record.addParam(new Param(paramName, paramValue, DBPUtilitiesConstants.STRING_TYPE));
    }
}