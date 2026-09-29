package com.temenos.dbx.mfa.utils;

import java.io.IOException;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.mfa.dto.MFA;

public class MFAConfigurationUtil {

    private Alert alert;


    private void initLOG() {
        if(alert ==null) {
            alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        }
    }

    private MFA mfa;

    public MFAConfigurationUtil(JSONObject mfaConf) {
        try {
            initLOG();
            mfa = new ObjectMapper().readValue(mfaConf.toString(), MFA.class);
            setMFAType();
        } catch (IOException e) {
            alert.prepareError("Error while getting MFa conficuration ", e).log();
        }
    }


    public String getPrimaryMFATypeId(){
        return mfa.getPrimaryMFATypeId();
    }


    public void setBackupMFAType(boolean areSecurityQuestionsPresent) {
        mfa.setBackupMFAType(areSecurityQuestionsPresent);
    }


    public void setMFAType() {
        mfa.setPrimaryMFAType();
    }

    public int getnumberofSecurityQuestions() {

        String value = mfa.getConfiguration(MFAConstants.SQ_NUMBER_OF_QUESTION_ASKED);

        if(value != null) return Integer.parseInt(value);

        return 2;
    }

    public String getCommunicationType() {
        String communicationType = MFAConstants.DISPLAY_PRIMARY;

        String value = mfa.getConfiguration(MFAConstants.SAC_PREFERENCE_CRITERIA);

        if(value != null) return value;

        return communicationType;
    }

    public String getMFAType() {

        return mfa.getPrimaryMFATypeId();
    }

    public boolean isMFARequired() {
        return Boolean.parseBoolean(mfa.getIsMFARequired());
    }

    public int getOTPLength() {

        String value = mfa.getConfiguration(MFAConstants.SAC_CODE_LENGTH);

        if(value != null) return Integer.parseInt(value);
        return 6;
    }

    public int getSACCodeExpiretime() {

        String value = mfa.getConfiguration(MFAConstants.SAC_CODE_EXPIRES_AFTER);

        if(value != null) return Integer.parseInt(value);

        return 3;

    }


    public boolean shouldLockUser() {

        return Boolean.parseBoolean(mfa.getConfiguration(MFAConstants.LOCK_USER));

    }

    public int maxFailedAttemptsAllowed() {

        String value = mfa.getConfiguration(MFAConstants.MAX_FAILED_ATTEMPTS_ALLOWED);

        if(value != null) return Integer.parseInt(value);

        return 1;
    }

    public int sacMaxResendRequestsAllowed() {

        String value = mfa.getConfiguration(MFAConstants.SAC_MAX_RESEND_REQUESTS_ALLOWED);

        if(value != null) return Integer.parseInt(value);

        return 1;
    }


    public boolean shouldLogoutUser() {
        // TODO Auto-generated method stub

        return Boolean.parseBoolean(mfa.getConfiguration(MFAConstants.LOGOUT_USER));

    }

    public String getSMSText() {
        return mfa.getPrimaryMFAType().getSmsText();
    }

    public String getEmailBody() {
        return mfa.getPrimaryMFAType().getEmailBody();
    }

    public String getEmailSubject() {

        String value =  mfa.getPrimaryMFAType().getEmailSubject();
        if(value !=null) return value;

        return "OTP";
    }


    public boolean isValidMFA() {
        return mfa != null;
    }


    public double getFrequencyAmount() {

        try {
            return Double.parseDouble(mfa.getFrequencyValue());
        }catch (Exception e) {
            return 0;
        }
    }


    public boolean isMFARequiredAlways() {
        return "ALWAYS".equalsIgnoreCase(mfa.getFrequencyTypeId());
    }

    public String getSMSText(String otp, JsonObject payload) {

        return replacetext(payload, otp, getSMSText());
    }

    public String getEmailBody(String otp, JsonObject payload) {

        return replacetext(payload, otp, getEmailBody());
    }

    public String replacetext(JsonObject payload, String otp, String text) {

        if (text.contains("[#]OTP[/#]")) {
            text = text.replace("[#]OTP[/#]", otp);
        }

        if (text.contains("[#]Transfer Amount[/#]")) {
            if (payload.has("amount") && !payload.get("amount").isJsonNull()) {
                text = text.replace("[#]Transfer Amount[/#]", payload.get("amount").getAsString());
            } else {
                if (payload.has("paidAmount") && !payload.get("paidAmount").isJsonNull()) {
                    text = text.replace("[#]Transfer Amount[/#]", payload.get("paidAmount").getAsString());
                }
            }
        }

        if (text.contains("[#]Account Number[/#]")) {
            if (payload.has("fromAccountNumber") && !payload.get("fromAccountNumber").isJsonNull()) {
                String accoutnNumber = payload.get("fromAccountNumber").getAsString();
                accoutnNumber = accoutnNumber.substring(accoutnNumber.length() - 4, accoutnNumber.length());
                text = text.replace("[#]Account Number[/#]", accoutnNumber);
            }
        }

        if (text.contains("[#]Payee Name[/#]")) {
            if (payload.has("toAccountNumber") && !payload.get("toAccountNumber").isJsonNull()) {
                String accoutnNumber = payload.get("toAccountNumber").getAsString();
                accoutnNumber = accoutnNumber.substring(accoutnNumber.length() - 4, accoutnNumber.length());
                text = text.replace("[#]Payee Name[/#]", accoutnNumber);
            }
        }

        if (text.contains("[#]Server Date[/#]")) {
            String date = HelperMethods.getCurrentDate();
            text = text.replace("[#]Server Date[/#]", date);
        }

        if (text.contains("[#]Server Time[/#]")) {
            String date = HelperMethods.getCurrentTime();
            text = text.replace("[#]Server Time[/#]", date);
        }

        return text;
    }


    public boolean isMFARequired(JsonObject requestpayload) {

        initLOG();

        boolean isMFARequired = isMFARequired();
        boolean isMFARequiredAlways = isMFARequiredAlways();

        if (!isMFARequired){
            return false;
        }
        if (isMFARequired && isMFARequiredAlways) {
            return true;
        }

        double amount = 0;
        double configuredAmount = getFrequencyAmount();

        if (!isMFARequiredAlways && isMFARequired) {

            if (requestpayload.has("amount")) {
                amount = requestpayload.get("amount").getAsDouble();
                return isMFARequired && configuredAmount <= amount;

            } else if (requestpayload.has("Amount")) {
                amount = requestpayload.get("Amount").getAsDouble();
                return isMFARequired && configuredAmount <= amount;
            }

            JsonArray transactions = new JsonArray();
            if (requestpayload.has("bulkPayString")) {
                String bulkPayString = requestpayload.get("bulkPayString").getAsString();

                try {
                    transactions = new JsonParser().parse(bulkPayString).getAsJsonArray();
                } catch (Exception e) {
                    alert.prepareError(e.getMessage()).log();
                }

                for (int i = 0; i < transactions.size(); i++) {
                    JsonObject transaction = transactions.get(i).getAsJsonObject();
                    if (transaction.has("paidAmount")) {
                        amount = transaction.get("paidAmount").getAsDouble();
                        if (configuredAmount <= amount) {
                            return isMFARequired && true;
                        }
                    }
                    else if (transaction.has("amount")) {
                        amount = transaction.get("amount").getAsDouble();
                        if (configuredAmount <= amount) {
                            return isMFARequired && true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
