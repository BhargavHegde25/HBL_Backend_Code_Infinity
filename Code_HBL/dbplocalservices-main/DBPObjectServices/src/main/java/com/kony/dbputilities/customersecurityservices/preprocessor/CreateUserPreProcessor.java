package com.kony.dbputilities.customersecurityservices.preprocessor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.constants.DBPConstants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPInputConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.konylabs.middleware.dataobject.Result;

public class CreateUserPreProcessor implements ObjectServicePreProcessor {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager,
            FabricRequestChain requestChain) throws Exception {
		Log4j2Configurator.getInstance();

        responseManager.getHeadersHandler().addHeader(HttpHeaders.CONTENT_TYPE,
                ContentType.APPLICATION_JSON.getMimeType());
        String userName = "";
        String password = "";
        PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
        PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();

        JsonObject resultJson = new JsonObject();
        JsonObject requestpayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();
        if (requestpayload.get("UserName") != null) {
            userName = requestpayload.get("UserName").getAsString();
        }
        if (requestpayload.get("Password") != null) {
            password = requestpayload.get("Password").getAsString();
        }

        if (!isRulesSatisfied(userName, password, requestManager, responseManager)) {
            return;
        }

        if ((requestpayload.has("membershipID") && !requestpayload.get("membershipID").isJsonNull())
                || (requestpayload.has("MemberId") && !requestpayload.get("MemberId").isJsonNull())) {
            requestChain.execute();
        } else if (requestpayload.has("Ssn") && !requestpayload.get("Ssn").isJsonNull()
                && requestpayload.has("LastName")
                && !requestpayload.get("LastName").isJsonNull() && requestpayload.has("DateOfBirth")
                && !requestpayload.get("DateOfBirth").isJsonNull()) {

            resultJson = new JsonObject();

            String serviceKey = new String();

            if (requestpayload.has("serviceKey")) {
                serviceKey = requestpayload.get("serviceKey").getAsString();
            }

            if (StringUtils.isBlank(serviceKey)) {
                resultJson = new JsonObject();
                ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return;
            }

            String serviceName = MFAConstants.SERVICE_ID_PRELOGIN;

            if (StringUtils.isNotBlank(serviceKey)
                    && isStateVerified(requestManager, requestpayload, serviceKey, serviceName, resultJson)) {
                updateRequestPayloadAndExecute(resultJson, requestManager, requestChain);
                return;
            }
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson);
            responseManager.getPayloadHandler().updatePayloadAsJson(resultJson);

        } else {
            resultJson = new JsonObject();
            ErrorCodeEnum.ERR_10502.setErrorCode(resultJson, "Invalid Request Payload");
            responsePayloadHandler.updatePayloadAsJson(resultJson);
        }

    }

    private void updateRequestPayloadAndExecute(JsonObject resultJson, FabricRequestManager requestManager,
            FabricRequestChain requestChain) {
        JsonObject request = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        String phone = JSONUtil.hasKey(resultJson, "Phone") ? resultJson.get("Phone").getAsString() : "";
        String email = JSONUtil.hasKey(resultJson, "Email") ? resultJson.get("Email").getAsString() : "";
        String addressInfoString =
                JSONUtil.hasKey(resultJson, "addressInformation") ? resultJson.get("addressInformation").getAsString()
                        : "";
        String backendIdentifierString = JSONUtil.hasKey(resultJson, "backendIdentifierInfo")
                ? resultJson.get("backendIdentifierInfo").getAsString()
                : "";
        String personalInfoString =
                JSONUtil.hasKey(resultJson, "personalInformation") ? resultJson.get("personalInformation").getAsString()
                        : "";

        if (StringUtils.isNotBlank(phone)) {
            request.addProperty("Phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            request.addProperty("Email", email);
        }
        if (StringUtils.isNotBlank(addressInfoString)) {
            request.addProperty("addressInformation", addressInfoString);
        }
        if (StringUtils.isNotBlank(backendIdentifierString)) {
            request.addProperty("backendIdentifierInfo", backendIdentifierString);
        }
        if (StringUtils.isNotBlank(personalInfoString)) {
            request.addProperty("personalInformation", personalInfoString);
        }
        requestManager.getPayloadHandler().updatePayloadAsJson(request);
        requestChain.execute();
    }

    private boolean isRulesSatisfied(String userName, String password, FabricRequestManager requestManager,
            FabricResponseManager responseManager) {
        PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
        JsonObject resultJson = new JsonObject();

        Map<String, String> headerMap = new HashMap<>();
        Map<String, String> inputMap = new HashMap<>();

        JsonObject rules = null;
        try {
            rules = AdminUtil.invokeAPIAndGetJson(inputMap, headerMap,
                    URLConstants.ADMIN_USERNAME_PASSWORD_RULES, requestManager);
        } catch (HttpCallException e) {
           alert.prepareError(e.toString()).log();
        }
        if (JSONUtil.isJsonNull(rules) || !JSONUtil.hasKey(rules, "usernamerules")
                || !JSONUtil.hasKey(rules, "passwordrules")) {
            ErrorCodeEnum.ERR_10116.setErrorCode(resultJson);
            resultJson.addProperty(DBPConstants.FABRIC_OPSTATUS_KEY, 0);
            resultJson.addProperty(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, 0);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }
        Map<String, String> usernameRules = HelperMethods.getRecordMap(rules.get("usernamerules").toString());
        Map<String, String> passwordRules = HelperMethods.getRecordMap(rules.get("passwordrules").toString());

        if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
            if (validateUsername(userName, usernameRules, resultJson)
                    && ValidatePassword(password, passwordRules, resultJson)) {
            } else {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        } else if (StringUtils.isNotBlank(userName)) {
            if (validateUsername(userName, usernameRules, resultJson)) {
            } else {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        } else if (StringUtils.isNotBlank(password)) {
            if (ValidatePassword(password, passwordRules, resultJson)) {
            } else {
                responsePayloadHandler.updatePayloadAsJson(resultJson);
                return false;
            }
        } else {
            ErrorCodeEnum.ERR_10120.setErrorCode(resultJson);
            resultJson.addProperty(DBPConstants.FABRIC_OPSTATUS_KEY, 0);
            resultJson.addProperty(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, 0);
            responsePayloadHandler.updatePayloadAsJson(resultJson);
            return false;
        }
        return true;
    }

    private boolean validateUsername(String userName, Map<String, String> usernameRules, JsonObject resultJson) {

        int userMinLength = (usernameRules.get("minLength") != null ? Integer.parseInt((usernameRules.get("minLength")))
                : 8);
        Boolean symbolsAllowed = (usernameRules.get("symbolsAllowed") != null
                ? Boolean.parseBoolean((usernameRules.get("symbolsAllowed")))
                : true);
        int userMaxLength = (usernameRules.get("maxLength") != null ? Integer.parseInt((usernameRules.get("maxLength")))
                : 64);
        String supportedSymbols = (usernameRules.get("supportedSymbols") != null
                ? (usernameRules.get("supportedSymbols"))
                : ".,-,_,@,!,#,$");
        String givenSupportedSymbols = supportedSymbols;

        alert.prepareError("supported symbols before: " + supportedSymbols).log();
        supportedSymbols = supportedSymbols.replaceAll(",", "");
        if (supportedSymbols.contains("-")) {
            supportedSymbols = supportedSymbols.replace("-", "\\-");
        }
        alert.prepareError("supported symbols after: " + supportedSymbols).log();

        if (StringUtils.isBlank(userName)) {
            ErrorCodeEnum.ERR_10121.setErrorCode(resultJson);
            return false;
        }
        if (!(userName.length() >= userMinLength && userName.length() <= userMaxLength)) {
            ErrorCodeEnum.ERR_10122.setErrorCode(resultJson,
                    " Username length should be in between the limits " + userMinLength + " and " + userMaxLength);
            return false;
        }
        if (symbolsAllowed) {
            Pattern newpattern = Pattern.compile("^(?=.*[^\\s\\w\\d" + supportedSymbols + "])");
            Matcher newm = newpattern.matcher(userName);
            if (newm.find()) {
                ErrorCodeEnum.ERR_10124.setErrorCode(resultJson, "special characters other than these "
                        + givenSupportedSymbols + " are not allowed for username");
                return false;
            }
        } else {
            Pattern pattern = Pattern.compile("[$&+,:;=?@#|'<>.^*()%!-]");
            Matcher m = pattern.matcher(userName);
            if (m.find()) {
                ErrorCodeEnum.ERR_10153.setErrorCode(resultJson,
                        "Special Characters should not be included in your Username.");
                return false;
            }

        }
        return true;

    }

    private boolean ValidatePassword(String password, Map<String, String> passwordRules, JsonObject resultJson) {
        int passMinLength = (passwordRules.get("minLength") != null ? Integer.parseInt((passwordRules.get("minLength")))
                : 8);
        int passMaxLength = (passwordRules.get("maxLength") != null ? Integer.parseInt((passwordRules.get("maxLength")))
                : 64);
        Boolean lowerCase = (passwordRules.get("atleastOneLowerCase") != null
                ? Boolean.parseBoolean((passwordRules.get("maxLength")))
                : true);
        Boolean upperCase = (passwordRules.get("atleastOneUpperCase") != null
                ? Boolean.parseBoolean((passwordRules.get("atleastOneUpperCase")))
                : true);
        Boolean number = (passwordRules.get("atleastOneNumber") != null
                ? Boolean.parseBoolean((passwordRules.get("atleastOneNumber")))
                : true);
        Boolean symbol = (passwordRules.get("atleastOneSymbol") != null
                ? Boolean.parseBoolean((passwordRules.get("atleastOneSymbol")))
                : true);
        int charRepeatCount = (passwordRules.get("charRepeatCount") != null
                ? Integer.parseInt((passwordRules.get("charRepeatCount")))
                : 4);
        String supportedSymbols = (passwordRules.get("supportedSymbols") != null
                ? (passwordRules.get("supportedSymbols"))
                : ".,-,_,@,!,#,$");
        String givenSupportedSymbols = supportedSymbols;

        supportedSymbols = supportedSymbols.replaceAll(",", "");
        if (supportedSymbols.contains("-")) {
            supportedSymbols = supportedSymbols.replace("-", "\\-");
        }

        if (StringUtils.isBlank(password)) {
            ErrorCodeEnum.ERR_10125.setErrorCode(resultJson);
            return false;
        }
        if (!(password.length() >= passMinLength && password.length() <= passMaxLength)) {
            ErrorCodeEnum.ERR_10126.setErrorCode(resultJson,
                    " password length should be in between the limits " + passMinLength + " and " + passMaxLength);
            return false;
        }
        if (lowerCase) {
            Pattern pattern = Pattern.compile("^(?=[^a-z]*[a-z])");
            Matcher m = pattern.matcher(password);
            if (!m.lookingAt()) {
                ErrorCodeEnum.ERR_10127.setErrorCode(resultJson);
                return false;
            }
        }
        if (upperCase) {
            Pattern pattern = Pattern.compile("^(?=[^A-Z]*[A-Z])");
            Matcher m = pattern.matcher(password);
            if (!m.lookingAt()) {
                ErrorCodeEnum.ERR_10128.setErrorCode(resultJson);
                return false;
            }
        }
        if (number) {
            Pattern pattern = Pattern.compile("^(?=\\D*\\d)");
            Matcher m = pattern.matcher(password);
            if (!m.lookingAt()) {
                ErrorCodeEnum.ERR_10129.setErrorCode(resultJson);
                return false;
            }
        }
        if (symbol) {
            Pattern pattern = Pattern.compile("^(?=.*[" + supportedSymbols + "])");
            Matcher m = pattern.matcher(password);
            if (!m.lookingAt()) {
                ErrorCodeEnum.ERR_10130.setErrorCode(resultJson);
                return false;
            }
            pattern = Pattern.compile("^(?=.*[^\\s\\w\\d" + supportedSymbols + "])");
            m = pattern.matcher(password);
            if (m.find()) {
                ErrorCodeEnum.ERR_10131.setErrorCode(resultJson, "special characters other than these "
                        + givenSupportedSymbols + " are not allowed for password");
                return false;
            }

        }
        if (charRepeatCount > 0) {
            Pattern pattern = Pattern.compile("(.)\\1{" + charRepeatCount + ",}");
            Matcher m = pattern.matcher(password);
            if (m.find()) {
                ErrorCodeEnum.ERR_10132.setErrorCode(resultJson,
                        "Maximum number of times a character can be repeated consecutively in the password is "
                                + charRepeatCount);
                return false;
            }
        }

        return true;

    }

    public boolean isStateVerified(FabricRequestManager requestManager, JsonObject inputJson, String serviceKey,
            String serviceName, JsonObject resultJson) {
        alert.prepareError("Verifying serviceKey").log();
        Result result = new Result();
        boolean status = false;
        String filter = MFAConstants.SERVICE_KEY + DBPUtilitiesConstants.EQUAL + serviceKey;
        try {
            result = HelperMethods.callGetApi(requestManager, filter, HelperMethods.getHeaders(requestManager),
                    URLConstants.MFA_SERVICE_GET);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            status = false;
            return status;
        }

        status = "true".equals(HelperMethods.getFieldValue(result, MFAConstants.IS_VERIFIED));

        if (status && HelperMethods.hasRecords(result)
                && StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "User_id"))
                && StringUtils.isNotBlank(HelperMethods.getFieldValue(result, "payload"))
                && isValidPayload(HelperMethods.getFieldValue(result, "payload"), inputJson, resultJson)) {
            if (!isServiceKeyExpired(requestManager, result)) {
                Map<String, String> map = new HashMap<>();
                map.put(MFAConstants.SERVICE_KEY, serviceKey);
                try {
                    HelperMethods.callApi(requestManager, map, HelperMethods.getHeaders(requestManager),
                            URLConstants.MFA_SERVICE_DELETE);
                } catch (Exception e) {
                    alert.prepareError(e.getMessage()).log();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isValidPayload(String payload, JsonObject inputJson, JsonObject resultJson) {
        try {
            if (StringUtils.isNotBlank(payload)) {
                payload = CryptoText.decrypt(payload);
            } else {
                return false;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            return false;
        }
        JsonParser parser = new JsonParser();
        JsonObject payloadJson = parser.parse(payload).getAsJsonObject();
        if (!JSONUtil.hasKey(payloadJson, "requestPayload") || !JSONUtil.hasKey(payloadJson, "communication")) {
            return false;
        }
        JsonObject communicationPayload = payloadJson.get("communication").getAsJsonObject();
        JsonArray phonePayload = communicationPayload.has(MFAConstants.PHONE)
                ? communicationPayload.get(MFAConstants.PHONE).getAsJsonArray()
                : new JsonArray();
        JsonArray emailPayload = communicationPayload.has(MFAConstants.EMAIL)
                ? communicationPayload.get(MFAConstants.EMAIL).getAsJsonArray()
                : new JsonArray();
        if (!hasCommunicationDetails(phonePayload, emailPayload)) {
            return false;
        }
        updateCommunicationDetailsToresult(phonePayload, emailPayload, resultJson);
        getAndupdateAddressDetailsToResult(payloadJson, resultJson);
        getAndupdateBakendIdentifierDetailsToResult(payloadJson, resultJson);
        getAndupdatePersonalDetailsToResult(payloadJson, resultJson);

        JsonObject requestPayload = payloadJson.get("requestPayload").getAsJsonObject();
        String customerlastname = null;
        String ssn = null;
        String dob = null;
        String inputCustomerlastname = null;
        String inputSsn = null;
        String inputDob = null;
        if (requestPayload.has(DBPInputConstants.CUSTOMER_LAST_NAME)
                && inputJson.has(DBPInputConstants.CUSTOMER_LAST_NAME)
                && !inputJson.get(DBPUtilitiesConstants.CUSTOMER_LAST_NAME).isJsonNull()
                && !requestPayload.get(DBPInputConstants.CUSTOMER_LAST_NAME).isJsonNull()) {
            customerlastname = requestPayload.get(DBPInputConstants.CUSTOMER_LAST_NAME).getAsString();
            inputCustomerlastname = inputJson.get(DBPInputConstants.CUSTOMER_LAST_NAME).getAsString();
        } else {
            return false;
        }
        if (requestPayload.has(DBPUtilitiesConstants.C_SSN) && inputJson.has(DBPUtilitiesConstants.C_SSN)
                && !inputJson.get(DBPUtilitiesConstants.C_SSN).isJsonNull()
                && !requestPayload.get(DBPUtilitiesConstants.C_SSN).isJsonNull()) {
            ssn = requestPayload.get(DBPUtilitiesConstants.C_SSN).getAsString();
            inputSsn = inputJson.get(DBPUtilitiesConstants.C_SSN).getAsString();
        } else {
            return false;
        }
        if (requestPayload.has(DBPUtilitiesConstants.C_DOB) && inputJson.has(DBPUtilitiesConstants.C_DOB)
                && !inputJson.get(DBPUtilitiesConstants.C_DOB).isJsonNull()
                && !requestPayload.get(DBPUtilitiesConstants.C_DOB).isJsonNull()) {
            dob = requestPayload.get(DBPUtilitiesConstants.C_DOB).getAsString();
            inputDob = inputJson.get(DBPUtilitiesConstants.C_DOB).getAsString();
        } else {
            return false;
        }
        if (customerlastname.equalsIgnoreCase(inputCustomerlastname) && ssn.equalsIgnoreCase(inputSsn)
                && dob.equals(inputDob)) {
            return true;
        }

        return false;
    }

    private void getAndupdatePersonalDetailsToResult(JsonObject payloadJson, JsonObject resultJson) {
        JsonObject personalJsonInfo = JSONUtil.hasKey(payloadJson, "personalInformation")
                ? payloadJson.get("personalInformation").getAsJsonObject()
                : null;
        if (JSONUtil.isJsonNotNull(personalJsonInfo)) {
            resultJson.addProperty("personalInformation", personalJsonInfo.toString());
        }
    }

    private void getAndupdateBakendIdentifierDetailsToResult(JsonObject payloadJson, JsonObject resultJson) {
        JsonArray backendIdentiInfo = JSONUtil.hasKey(payloadJson, "backendIdentifierInfo")
                ? payloadJson.get("backendIdentifierInfo").getAsJsonArray()
                : null;
        if (JSONUtil.isJsonNotNull(backendIdentiInfo)) {
            resultJson.addProperty("backendIdentifierInfo", backendIdentiInfo.toString());
        }
    }

    private void getAndupdateAddressDetailsToResult(JsonObject payloadJson, JsonObject resultJson) {
        JsonObject addressInfo = JSONUtil.hasKey(payloadJson, "addressInformation")
                ? payloadJson.get("addressInformation").getAsJsonObject()
                : null;
        if (JSONUtil.isJsonNotNull(addressInfo)) {
            resultJson.addProperty("addressInformation", addressInfo.toString());
        }
    }

    private void updateCommunicationDetailsToresult(JsonArray phonePayload, JsonArray emailPayload,
            JsonObject resultJson) {
        JsonObject contactPayload = new JsonObject();

        for (int i = 0; i < phonePayload.size(); i++) {
            contactPayload = phonePayload.get(i).getAsJsonObject();

            String mobile = JSONUtil.isJsonNotNull(contactPayload.get(MFAConstants.UNMASKED))
                    ? contactPayload.get("unmasked").getAsString()
                    : "";

            String isPrimary = contactPayload.has("isPrimary") ? contactPayload.get("isPrimary").getAsString() : "";
            mobile = processMobile(mobile);

            if ("true".equals(isPrimary) && StringUtils.isNotBlank(mobile)) {
                resultJson.addProperty("Phone", mobile);
                break;
            }
            if (i == (phonePayload.size() - 1) && StringUtils.isNotBlank(mobile)) {
                resultJson.addProperty("Phone", mobile);
                break;
            }
        }

        for (int i = 0; i < emailPayload.size(); i++) {
            contactPayload = emailPayload.get(i).getAsJsonObject();

            String email = JSONUtil.isJsonNotNull(contactPayload.get(MFAConstants.UNMASKED))
                    ? contactPayload.get("unmasked").getAsString()
                    : "";

            String isPrimary = contactPayload.has("isPrimary") ? contactPayload.get("isPrimary").getAsString() : "";

            if ("true".equals(isPrimary) && StringUtils.isNotBlank(email)) {
                resultJson.addProperty("Email", email);
                break;
            }
            if (i == (emailPayload.size() - 1) && StringUtils.isNotBlank(email)) {
                resultJson.addProperty("Email", email);
                break;
            }
        }

    }

    private boolean hasCommunicationDetails(JsonArray phonePayload, JsonArray emailPayload) {

        boolean phoneStatus = false;
        boolean emailStatus = false;
        if (phonePayload.size() <= 0 && emailPayload.size() <= 0) {
            return false;
        }

        phoneStatus = getContactStatus(phonePayload);
        emailStatus = getContactStatus(emailPayload);

        if (!(phoneStatus || emailStatus)) {
            return false;
        }

        return true;

    }

    private boolean getContactStatus(JsonArray payload) {
        JsonObject contactPayload = new JsonObject();
        for (int i = 0; i < payload.size(); i++) {
            contactPayload = payload.get(i).getAsJsonObject();

            String contact = JSONUtil.isJsonNotNull(contactPayload.get(MFAConstants.UNMASKED))
                    ? contactPayload.get("unmasked").getAsString()
                    : "";
            if (StringUtils.isNotBlank(contact)) {
                return true;
            }
        }
        return false;
    }

    private boolean isServiceKeyExpired(FabricRequestManager requestManager, Result result) {

        String string = HelperMethods.getFieldValue(result, "Createddts");

        if (StringUtils.isNotBlank(string)) {
            Date createdts = HelperMethods.getFormattedTimeStamp(string);
            Calendar generatedCal = Calendar.getInstance();
            generatedCal.setTime(createdts);

            Date verifyDate = new Date();
            Calendar verifyingCal = Calendar.getInstance();
            verifyingCal.setTime(verifyDate);
            int otpValidityTime = getServiceKeyExpiretime(requestManager);
            generatedCal.add(Calendar.MINUTE, otpValidityTime);

            long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
            long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

            if (GeneratedMilliSeconds > verifyingMilliSeconds) {
                return false;
            }
        }
        return true;
    }

    private int getServiceKeyExpiretime(FabricRequestManager requestManager) {
        try {
            return Integer.parseInt(URLFinder.getPathUrl(URLConstants.SERVICEKEY_EXPIRE_TIME, requestManager));
        } catch (Exception e) {
        }
        return 10;
    }

    private String processMobile(String mobile) {

        if (StringUtils.isBlank(mobile)) {
            return mobile;
        }

        String mobilenumber = "";

        if (!mobile.contains("+")) {
            mobilenumber = "+";
        }
        String[] strings = mobile.split("-");
        for (int i = 0; i < strings.length; i++) {
            mobilenumber += strings[i];
        }

        return mobilenumber;
    }

}