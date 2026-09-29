package com.temenos.auth.usermanagement.operation;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.PasswordLockoutSettings;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class PasswordHistoryManagement {

    private PasswordLockoutSettings lockoutSettings = null;
    private Alert alert;
    private Diagnostic diagnostic;

    public PasswordHistoryManagement(DataControllerRequest dcRequest, boolean getLockoutSettings) {
        if (getLockoutSettings) {
            lockoutSettings = new PasswordLockoutSettings(dcRequest);
        }
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    }

    public synchronized boolean checkForPasswordEntry(DataControllerRequest dcRequest, String userName, String password)
            throws HttpCallException {

        HashMap<String, String> hashMap = new HashMap<>();
        String customerId = getCustomerId(dcRequest, userName);

        hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + customerId);
        hashMap.put(DBPUtilitiesConstants.ORDERBY, "createdts desc");
        hashMap.put(DBPUtilitiesConstants.TOP, Integer.toString(lockoutSettings.getPasswordHistoryCount()));
        hashMap.put(DBPUtilitiesConstants.SKIP, "0");
        diagnostic.prepareDebug("InputParams for" + URLConstants.PASSWORDHISTORY_GET + " : " + hashMap).log();

        Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.PASSWORDHISTORY_GET);

        diagnostic.prepareDebug("Result for" + URLConstants.PASSWORDHISTORY_GET + " : " + ResultToJSON.convert(result)).log();

        if (!HelperMethods.hasError(result) && null != result.getAllDatasets()
                && result.getAllDatasets().get(0).getAllRecords().isEmpty()) {
            return true;
        }
        if (HelperMethods.hasRecords(result)) {
            List<Record> records = result.getAllDatasets().get(0).getAllRecords();
            for (Record record : records) {
                if (record.getNameOfAllParams().contains("PreviousPassword")
                        && StringUtils.isNotBlank(record.getParam("PreviousPassword").getValue())
                        && BCrypt.checkpw(password, record.getParam("PreviousPassword").getValue())) {
                    return false;
                }
            }
        }

        return true;
    }

    public synchronized boolean makePasswordEntry(DataControllerRequest dcRequest, String customerId, String password)
            throws HttpCallException {

        boolean isPasswordEntryMade = false;
        HashMap<String, String> hashMap = new HashMap<>();
        String id = UUID.randomUUID().toString();

        hashMap.put("id", id);
        if (StringUtils.isBlank(customerId)) {
            customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        }
        hashMap.put("Customer_id", customerId);
        hashMap.put("PreviousPassword", password);
        hashMap.put("createdby", HelperMethods.getUserFromIdentityService(dcRequest).get("userName"));
        Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.PASSWORDHISTORY_CREATE);

        if (HelperMethods.hasRecords(result)) {
            isPasswordEntryMade = true;
        } else {
            isPasswordEntryMade = false;
        }
        diagnostic.prepareDebug("isPasswordEntryMade at the end of makePasswordEntry : " + isPasswordEntryMade).log();

        return isPasswordEntryMade;
    }

    public synchronized boolean isPasswordExpired(DataControllerRequest dcRequest, String customerId,
            boolean isProspect)
            throws HttpCallException {

        boolean isPasswordExpired = false;
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(DBPUtilitiesConstants.FILTER, "Customer_id eq " + customerId);
        hashMap.put(DBPUtilitiesConstants.ORDERBY, "createdts desc");
        hashMap.put(DBPUtilitiesConstants.TOP, "1");
        hashMap.put(DBPUtilitiesConstants.SKIP, "0");
        Result result = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                URLConstants.PASSWORDHISTORY_GET);
        if (HelperMethods.hasRecords(result)) {
            Date createdts = HelperMethods.getFormattedTimeStamp(HelperMethods.getFieldValue(result, "createdts"));
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(createdts);
            int passwordValidtity = isProspect ? HelperMethods.generateProspectExpiryTime(dcRequest)
                    : lockoutSettings.getPasswordValidity();
            cal.add(Calendar.DATE, passwordValidtity);
            createdts = cal.getTime();
            if (now.after(createdts)) {
                isPasswordExpired = true;
            } else {
                isPasswordExpired = false;
            }
        } else {
            isPasswordExpired = true;
        }

        diagnostic.prepareDebug("isPasswordExpired at the end of isPasswordExpired : " + isPasswordExpired).log();

        return isPasswordExpired;
    }

    public synchronized boolean isEmailRecoveryLinkExpired(DataControllerRequest dcRequest, String createdDate) {

        boolean isEmailRecoveryLinkExpired = false;
        Date createdts = HelperMethods.getFormattedTimeStamp(createdDate);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdts);
        cal.add(Calendar.MINUTE, lockoutSettings.getRecoveryEmailLinkValidity());
        createdts = cal.getTime();
        if (now.after(createdts)) {
            isEmailRecoveryLinkExpired = true;
        } else {
            isEmailRecoveryLinkExpired = false;
        }

        diagnostic.prepareDebug(
                "isEmailRecoveryLinkExpired at the end of isEmailRecoveryLinkExpired : " + isEmailRecoveryLinkExpired).log();

        return isEmailRecoveryLinkExpired;
    }

    private String getCustomerId(DataControllerRequest dcRequest, String userName) throws HttpCallException {
        Result result = new Result();
        String filter = "";
        filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
        result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERVERIFY_GET);
        if (HelperMethods.hasRecords(result)) {
            return HelperMethods.getFieldValue(result, "id");
        }
        return null;
    }

    public String getDbpErrorCode() {
        return lockoutSettings.getDbpErrorCode();
    }

    public void setDbpErrorCode(String dbpErrorCode) {
        lockoutSettings.setDbpErrorCode(dbpErrorCode);
    }

    public String getDbpErrorMessage() {
        return lockoutSettings.getDbpErrorMessage();
    }

    public void setDbpErrorMessage(String dbpErrorMessage) {
        lockoutSettings.setDbpErrorMessage(dbpErrorMessage);
    }

    public int getAutoUnLockPeriod() {

        return lockoutSettings.getAutoUnLockPeriod();
    }

    public int getAccountLockoutThreshold() {

        return lockoutSettings.getAccountLockoutThreshold();
    }

    public int getPasswordHistoryCount() {

        return lockoutSettings.getPasswordHistoryCount();
    }

    public int getRecoveryEmailLinkValidity() {
        return lockoutSettings.getRecoveryEmailLinkValidity();
    }

}
