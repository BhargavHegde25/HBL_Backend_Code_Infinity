package com.kony.dbputilities.transservices;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.dbutil.QueryFormer;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CreateBulkBillPay implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);

        processHelper(inputParams, dcRequest, result);

        return result;
    }

    @SuppressWarnings({ "rawtypes" })
    private boolean processHelper(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        boolean status = true;
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        Result rs=null;
        if (StringUtils.isBlank(customerId)) {
            return false;
        }

        Dataset ds = new Dataset("transaction");
        result.addDataset(ds);
        List<Map<String, String>> inputs = getFormattedInput(inputParams);
        for (Map<String, String> input : inputs) {
            String billId = input.get("billid");
            String payeeId = input.get("payeeId");
            String toAccountNumber = "";
            if (StringUtils.isNotBlank(billId) && !"0".equals(billId)) {
                updateBillDetails(dcRequest, input);
            } else if (StringUtils.isNotBlank(payeeId) && !"0".equals(payeeId)) {
                updateDefaultBillDetails(dcRequest, input);
            } else if (StringUtils.isNotBlank(toAccountNumber)) {
                input.put("toExternalAccountNumber", toAccountNumber);
            }
            if (accountValidation(input, dcRequest)) {
                if (payeeValidation(input, dcRequest)) {
                    processTransaction(dcRequest, input);
                    if("MSSQL".equalsIgnoreCase(QueryFormer.getDBType(dcRequest)))
                	{
                    	
                    	try {
                    		rs =	TransOperations.createDataSetInsert(dcRequest, inputParams);
						} catch (SQLException e) {
							
						}

                	}
                    else {
                    rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                            URLConstants.ACCOUNT_TRANSACTION_CREATE);}
                    if (HelperMethods.hasRecords(rs)) {
                    
                        postProcess(dcRequest, rs, result, input);
                    } else {
                        HelperMethods.setValidationMsg(rs.getParamValueByName(DBPUtilitiesConstants.VALIDATION_ERROR),
                                dcRequest, result);
                        status = false;
                    }}
                 else {
                    status = false;
                    HelperMethods.setValidationMsg("You are using an unauthorized payee", dcRequest, result);
                }
            } else {
                status = false;
                HelperMethods.setValidationMsg("You don’t have access to this account", dcRequest, result);
            }

        }
        return status;
    }

    private boolean payeeValidation(Map<String, String> input, DataControllerRequest dcRequest)
            throws HttpCallException {
        Result result = new Result();
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String userId = user.get("customer_id");
        String payeeId = input.get("payeeId");

        // Added to take payee id set using updateBillDetails method.
        if (StringUtils.isBlank(payeeId)) {
            payeeId = input.get("Payee_id");
        }

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(payeeId)) {
            String filter = "User_Id" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND + "Id"
                    + DBPUtilitiesConstants.EQUAL + payeeId;
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(result)) {
                return true;
            }
        }
        return false;
    }

    private boolean accountValidation(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws HttpCallException {
        Result userResult = new Result();
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String fromAccountNum = inputParams.get("accountNumber");
        String userId = user.get("customer_id");
        String userType = user.get("customerType");
        if (StringUtils.isNotBlank(fromAccountNum) && StringUtils.isNotBlank(userId)) {
            if (HelperMethods.isBusinessUserType(userType)) {
                String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNum + DBPUtilitiesConstants.AND
                        + "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
                userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERACCOUNTS_GET);
            } else {
                String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNum + DBPUtilitiesConstants.AND
                        + DBPUtilitiesConstants.USER_ID + DBPUtilitiesConstants.EQUAL + userId;
                userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNTS_GET);
            }
            return HelperMethods.hasRecords(userResult);
        }
        return true;
    }

    private void postProcess(DataControllerRequest dcRequest, Result rs, Result result, Map<String, String> input)
            throws HttpCallException {
        Record transaction = rs.getAllDatasets().get(0).getRecord(0);
        updatePayeeDetails(dcRequest, transaction);
        updateFromAccountDetails(dcRequest, transaction);
        try {
            transaction.addParam(HelperMethods.convertedDateParam("deliverBy", input.get("deliverBy")));
        } catch (ParseException e) {

            alert.prepareError(e.getMessage()).log();
        }
        result.getAllDatasets().get(0).addRecord(transaction);
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, DBPUtilitiesConstants.STRING_TYPE));

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
                String name = HelperMethods.getFieldValue(payee, "name");
                String addressLine1 = HelperMethods.getFieldValue(payee, "addressLine1");
                if (StringUtils.isBlank(name)) {
                    name = nickName;
                }
                transaction.addParam(new Param("payeeName", name, DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("payeeNickName", nickName, DBPUtilitiesConstants.STRING_TYPE));
                transaction.addParam(new Param("payeeAddressLine1", addressLine1, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void processTransaction(DataControllerRequest dcRequest, Map inputParams)
            throws HttpCallException, ParseException {
        String frmAccount = (String) inputParams.get("accountNumber");
        String typeId = getTransTypeId(dcRequest, "BillPay");
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put("deliverBy", inputParams.get("deliverBy"));
        inputParams.put("Type_id", typeId);
        inputParams.put("createdDate", HelperMethods.getCurrentTimeStamp());
        inputParams.put("transactionDate", HelperMethods.getCurrentTimeStamp());
        inputParams.put("fromAccountNumber", frmAccount);
        String isScheduled = (String) inputParams.get("isScheduled");
        if (StringUtils.isBlank(isScheduled)) {
            inputParams.put("isScheduled", "false");
        }
        if (StringUtils.isNotBlank((String) inputParams.get("scheduledDate"))) {
            inputParams.put("scheduledDate",
                    HelperMethods.convertDateFormat((String) inputParams.get("scheduledDate"), null));
        }
        inputParams.put("description", "Bill Pay To " + getPayeeName(dcRequest, (String) inputParams.get("Payee_id")));
        inputParams.put("statusDesc", "Successful");
        Map frmAccountDetails = getAccountDetails(dcRequest, frmAccount);
        BigDecimal transAmnt = new BigDecimal((String) inputParams.get("paidAmount"));
        BigDecimal availBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
        BigDecimal newBal = availBal.subtract(transAmnt);
        inputParams.put("fromAccountBalance", newBal.toPlainString());
        inputParams.put("amount", inputParams.get("paidAmount"));
        if (newBal.signum() >= 0) {
            updateAccountBalance(dcRequest, frmAccount, newBal.toPlainString());
        }
        HelperMethods.removeNullValues(inputParams);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void updateDefaultBillDetails(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
        String DEFAULT_BILL_ID = "4";
        String transAmount = (String) inputParams.get("paidAmount");
        Result bill = getBillDetails(dcRequest, DEFAULT_BILL_ID);
        String paidAmount = HelperMethods.getFieldValue(bill, "paidAmount");
        if (StringUtils.isBlank(paidAmount)) {
            paidAmount = "0";
        }
        if (StringUtils.isBlank(transAmount)) {
            transAmount = "0";
        }
        paidAmount = new BigDecimal(paidAmount).add(new BigDecimal(transAmount)).toPlainString();
        updateBill(dcRequest, DEFAULT_BILL_ID, paidAmount, bill);
        inputParams.put("Bill_id", DEFAULT_BILL_ID);
        inputParams.put("Payee_id", inputParams.get("payeeId"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void updateBillDetails(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
        String billid = (String) inputParams.get("billid");
        String transAmount = (String) inputParams.get("paidAmount");
        Result bill = getBillDetails(dcRequest, billid);
        String payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
        String paidAmount = HelperMethods.getFieldValue(bill, "paidAmount");
        if (StringUtils.isBlank(paidAmount)) {
            paidAmount = "0";
        }
        if (StringUtils.isBlank(transAmount)) {
            transAmount = "0";
        }
        paidAmount = new BigDecimal(paidAmount).add(new BigDecimal(transAmount)).toPlainString();
        updateBill(dcRequest, billid, paidAmount, bill);
        inputParams.put("Payee_id", payeeId);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void updateBill(DataControllerRequest dcRequest, String billid, String paidAmount, Result bill)
            throws HttpCallException {
        Map input = new HashMap<>();
        input.put("id", billid);
        input.put("paidAmount", paidAmount);
        input.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        input.put("paidDate", HelperMethods.getCurrentTimeStamp());
        String billDueDate = HelperMethods.getFieldValue(bill, "billDueDate");
        Calendar cal = Calendar.getInstance();
        cal.setTime(HelperMethods.getFormattedTimeStamp(billDueDate));
        cal.add(Calendar.MONTH, 1);
        input.put("billDueDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), "yyyy-MM-dd"));
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.BILL_UPDATE);
    }

    private Result getBillDetails(DataControllerRequest dcRequest, String billid) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billid;
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.BILL_GET);
    }

    public String getTransTypeId(DataControllerRequest dcRequest, String transType) throws HttpCallException {
        String typeId = "";
        String filterQuery = DBPUtilitiesConstants.TRANS_TYPE_DESC + DBPUtilitiesConstants.EQUAL + transType;
        Result rs = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        typeId = HelperMethods.getFieldValue(rs, DBPUtilitiesConstants.T_TRANS_ID);
        return typeId;
    }

    public String getPayeeName(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filterQuery = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result rs = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        return HelperMethods.getFieldValue(rs, "nickName");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccountDetails(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        Map result = new HashMap();
        String filter = DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL + accountId;
        Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        result.put(DBPUtilitiesConstants.AVAILABLE_BAL_S,
                HelperMethods.getFieldValue(rs, DBPUtilitiesConstants.AVAILABLE_BAL_S));
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void updateAccountBalance(DataControllerRequest dcRequest, String accountId, String balance)
            throws HttpCallException {
        Map input = new HashMap();
        input.put(DBPUtilitiesConstants.ACCOUNT_ID, accountId);
        input.put(DBPUtilitiesConstants.AVAILABLE_BAL, balance);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, String>> getFormattedInput(Map inputParams) {
        List<Map<String, String>> inputs = new ArrayList<>();
        String bulkPayString = (String) inputParams.get("bulkPayString");
        JsonArray jArray = getJsonArray(bulkPayString);
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Gson gson = new Gson();
        if (jArray.isJsonArray()) {
            for (int i = 0; i < jArray.size(); i++) {
                Map<String, String> temp = (Map<String, String>) gson.fromJson(jArray.get(i), type);
                inputs.add(temp);
            }
        }
        return inputs;
    }

    private JsonArray getJsonArray(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        return (JsonArray) jsonParser.parse(jsonString);
    }
}
