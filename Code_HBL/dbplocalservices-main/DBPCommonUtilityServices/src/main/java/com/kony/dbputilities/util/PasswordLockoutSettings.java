package com.kony.dbputilities.util;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class PasswordLockoutSettings {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private int passwordValidity;
    private boolean passwordExpiryWarningRequired = false;
    private int passwordExpiryWarningThreshold = 100;
    private int passwordHistoryCount = 5;
    private int accountLockoutThreshold = 5;
    private int accountLockoutTime = 5;
    private int recoveryEmailLinkValidity = 24;
    private String dbpErrorCode;
    private String dbpErrorMessage;

    public PasswordLockoutSettings(FabricRequestManager requestManager) {

        Map<String, String> input = new HashMap<>();
        Result response = HelperMethods.callApi(requestManager, input, HelperMethods.getHeaders(requestManager),
                URLConstants.PASSWORDLOCKOUTSETTINGS_GET);
        if (HelperMethods.hasRecords(response)) {
            postProcess(response);
        }
    }

    public PasswordLockoutSettings(DataControllerRequest dcRequest) {
        Map<String, String> input = new HashMap<>();
        Result response = new Result();
        try {
            response = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PASSWORDLOCKOUTSETTINGS_GET);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (HelperMethods.hasRecords(response)) {
            postProcess(response);
        }
    }

    private void postProcess(Result response) {
        Record passwordlockoutsettings = HelperMethods.getRecord(response);
        this.setPasswordValidity(Integer.parseInt(passwordlockoutsettings.getParamValueByName("passwordValidity")));
        this.setPasswordExpiryWarningRequired(
                Boolean.parseBoolean(passwordlockoutsettings.getParamValueByName("passwordExpiryWarningRequired")));
        this.setPasswordExpiryWarningThreshold(
                Integer.parseInt(passwordlockoutsettings.getParamValueByName("passwordExpiryWarningThreshold")));
        this.passwordHistoryCount = Integer
                .parseInt(passwordlockoutsettings.getParamValueByName("passwordHistoryCount"));
        this.accountLockoutThreshold = Integer
                .parseInt(passwordlockoutsettings.getParamValueByName("accountLockoutThreshold"));
        this.accountLockoutTime = Integer.parseInt(passwordlockoutsettings.getParamValueByName("accountLockoutTime"));
        this.setRecoveryEmailLinkValidity(
                Integer.parseInt(passwordlockoutsettings.getParamValueByName("recoveryEmailLinkValidity")));
    }

    public int getAccountLockoutThreshold() {
        return this.accountLockoutThreshold;
    }

    public int getAutoUnLockPeriod() {
        return this.accountLockoutTime;
    }

    public int getPasswordHistoryCount() {
        return this.passwordHistoryCount;
    }

    public int getPasswordValidity() {
        return passwordValidity;
    }

    public void setPasswordValidity(int passwordValidity) {
        this.passwordValidity = passwordValidity;
    }

    public int getRecoveryEmailLinkValidity() {
        return recoveryEmailLinkValidity;
    }

    public void setRecoveryEmailLinkValidity(int recoveryEmailLinkValidity) {
        this.recoveryEmailLinkValidity = recoveryEmailLinkValidity;
    }

    public String getDbpErrorCode() {
        return dbpErrorCode;
    }

    public void setDbpErrorCode(String dbpErrorCode) {
        this.dbpErrorCode = dbpErrorCode;
    }

    public String getDbpErrorMessage() {
        return dbpErrorMessage;
    }

    public void setDbpErrorMessage(String dbpErrorMessage) {
        this.dbpErrorMessage = dbpErrorMessage;
    }

    public boolean isPasswordExpiryWarningRequired() {
        return passwordExpiryWarningRequired;
    }

    public void setPasswordExpiryWarningRequired(boolean passwordExpiryWarningRequired) {
        this.passwordExpiryWarningRequired = passwordExpiryWarningRequired;
    }

    public int getPasswordExpiryWarningThreshold() {
        return passwordExpiryWarningThreshold;
    }

    public void setPasswordExpiryWarningThreshold(int passwordExpiryWarningThreshold) {
        this.passwordExpiryWarningThreshold = passwordExpiryWarningThreshold;
    }

}
