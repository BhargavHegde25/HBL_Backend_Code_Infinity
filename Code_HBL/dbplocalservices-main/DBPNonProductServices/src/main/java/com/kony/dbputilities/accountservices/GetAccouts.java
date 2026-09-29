package com.kony.dbputilities.accountservices;

import java.util.HashMap;
import java.util.List;
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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccouts implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String username = dcRequest.getParameter("userName");
        String did = inputParams.get("deviceID");
        String userId = inputParams.get("userId");
        String customerType = inputParams.get("customerType");

        if (StringUtils.isBlank(userId) && StringUtils.isNotBlank(username)) {
            Result userResult = getUserId(dcRequest, username);
            userId = HelperMethods.getFieldValue(userResult, "id");
            customerType = HelperMethods.getFieldValue(userResult, "CustomerType_id");
            inputParams.put("userId", userId);
            inputParams.put("customerType", customerType);
        }
        if (StringUtils.isNotBlank(did) && StringUtils.isNotBlank(username)) {
            if (!isDeviceRegistered(dcRequest, did, userId)) {
                Dataset ds = new Dataset("Accounts");
                result.addDataset(ds);
                return result;
            }
        }

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERACCOUNTSVIEW_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            result.getAllDatasets().get(0).setId("Accounts");
            postProcess(result, dcRequest);
        }
        if (!HelperMethods.hasRecords(result)) {
            Dataset ds = new Dataset();
            ds.setId("Accounts");
            result.addDataset(ds);
        }
        return result;
    }

    private boolean isDeviceRegistered(DataControllerRequest dcRequest, String deviceId, String userId)
            throws HttpCallException {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer_id").append(DBPUtilitiesConstants.EQUAL).append(userId).append(DBPUtilitiesConstants.AND)
                .append("Device_id").append(DBPUtilitiesConstants.EQUAL).append(deviceId)
                .append(DBPUtilitiesConstants.AND).append("Status_id").append(DBPUtilitiesConstants.EQUAL)
                .append("SID_DEVICE_REGISTERED");
        Result device = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
                URLConstants.DEVICEREGISTRATION_GET);
        return HelperMethods.hasRecords(device);
    }

    private void postProcess(Result result, DataControllerRequest dcRequest) throws HttpCallException {
        List<Record> accounts = result.getAllDatasets().get(0).getAllRecords();
        for (Record account : accounts) {
            updateDateFormat(account);
            account.addParam(new Param("rePaymentFrequency", "Monthly", DBPUtilitiesConstants.STRING_TYPE));
            String accountType = account.getParamValueByName("typeDescription");
            String dueDate = account.getParamValueByName("dueDate");
            account.addParam(new Param("nextPaymentDate", dueDate, DBPUtilitiesConstants.STRING_TYPE));
            String accountId = account.getParamValueByName("Account_id");
            if (StringUtils.isNotBlank(accountType) && accountType.equals("Loan")
                    && StringUtils.isNotBlank(accountId)) {
                updateAccountwithInstallmentCount(account, accountId, dcRequest);
            }
            String creditcardNumber = account.getParamValueByName("creditCardNumber");
            if (StringUtils.isNotBlank(creditcardNumber)) {
                creditcardNumber = getMaskedValue(creditcardNumber);
                account.addParam(new Param("creditCardNumber", creditcardNumber, DBPUtilitiesConstants.STRING_TYPE));
            }
        }
    }

    private String getMaskedValue(String creditcardNumber) {
        String lastFourDigits;
        if (StringUtils.isNotBlank(creditcardNumber)) {
            if (creditcardNumber.length() > 4) {
                lastFourDigits = creditcardNumber.substring(creditcardNumber.length() - 4);
                creditcardNumber = "XXXX" + lastFourDigits;
            } else {
                creditcardNumber = "XXXX" + creditcardNumber;
            }
        }

        return creditcardNumber;
    }

    public void updateAccountwithInstallmentCount(Record account, String accountId, DataControllerRequest dcRequest)
            throws HttpCallException {
        Result installmentsCountResult = new Result();
        Map<String, String> countInputParams = new HashMap<String, String>();
        countInputParams.put("transactions_query", getInstallmentsCountQuery(accountId, dcRequest));
        installmentsCountResult =
                HelperMethods.callApi(dcRequest, countInputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNT_TRANSACTION_PROC);
        if (installmentsCountResult.getAllDatasets().size() > 0) {
            List<Record> records = installmentsCountResult.getAllDatasets().get(0).getAllRecords();
            for (Record record : records) {
                if (record.getParamValueByName("InstallmentType").equalsIgnoreCase(DBPUtilitiesConstants.FUTURE)) {
                    account.addParam(new Param("FutureInstallmentsCount", record.getParamValueByName("count")));
                }
                if (record.getParamValueByName("InstallmentType").equalsIgnoreCase(DBPUtilitiesConstants.DUE)) {
                    account.addParam(new Param("OverDueInstallmentsCount", record.getParamValueByName("count")));
                }
                if (record.getParamValueByName("InstallmentType").equalsIgnoreCase(DBPUtilitiesConstants.PAID)) {
                    account.addParam(new Param("PaidInstallmentsCount", record.getParamValueByName("count")));
                }
            }
        }
    }

    public String getInstallmentsCountQuery(String accountId, DataControllerRequest dcRequest) {
        String jdbcUrl = QueryFormer.getDBType(dcRequest);
        return SqlQueryEnum.valueOf(jdbcUrl + "_GetInstallmentsCountQuery").getQuery()
                .replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", accountId);

    }

    private void updateDateFormat(Record account) {
        try {
            String scheduledDate = HelperMethods.getFieldValue(account, "dueDate");
            if (StringUtils.isNotBlank(scheduledDate)) {
                account.addParam(new Param("dueDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            String date = HelperMethods.getFieldValue(account, "closingDate");
            if (StringUtils.isNotBlank(date)) {
                account.addParam(new Param("closingDate",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            date = HelperMethods.getFieldValue(account, "openingDate");
            if (StringUtils.isNotBlank(date)) {
                account.addParam(new Param("openingDate",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'HH:mm:ss"), "String"));
            }
            date = HelperMethods.getFieldValue(account, "lastPaymentDate");
            if (StringUtils.isNotBlank(date)) {
                account.addParam(new Param("lastPaymentDate",
                        HelperMethods.convertDateFormat(date, "yyyy-MM-dd'T'HH:mm:ss"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String accountId = inputParams.get("accountID");
        inputParams.get("userName");
        String deviceId = inputParams.get("deviceID");
        String phone = inputParams.get("phone");
        String userId = inputParams.get("userId");
        String filter = "";
        if (StringUtils.isNotBlank(deviceId)) {
            filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
        } else if (StringUtils.isBlank(accountId)) {
            filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
            if (StringUtils.isNotBlank(phone)) {
                filter = filter + DBPUtilitiesConstants.AND + "phone" + DBPUtilitiesConstants.EQUAL + phone;
            }
            if (!HelperMethods.getCustomerTypes().get("Customer").equals(inputParams.get("customerType"))) {
                filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
            }
        } else {
            if (StringUtils.isNotBlank(userId)) {
                filter = DBPUtilitiesConstants.UA_USR_ID + DBPUtilitiesConstants.EQUAL + userId;
                if (!HelperMethods.getCustomerTypes().get("Customer").equals(inputParams.get("customerType"))) {
                    filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
                }
                filter += DBPUtilitiesConstants.AND;
            }

            filter += "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        }
        // inputParams.clear();
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        inputParams.put(DBPUtilitiesConstants.ORDERBY, "accountPreference asc");

        return status;
    }

    private Result getUserId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        Result user = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERVERIFY_GET);
        return user;
    }

}
