package com.temenos.auth.kms;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.http.HttpConnector;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class KMSUtil {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	 private static Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    public static boolean subscribeKMSUser(DataControllerRequest dcRequest, Map<String, String> headers, Result result)
            throws HttpCallException {
        JsonObject input = KMSUtil.constructCreateKMSUserJSONObject(dcRequest.getParameter("firstName"),
                dcRequest.getParameter("lastName"), dcRequest.getParameter("email"), dcRequest.getParameter("phone"));
        HttpConnector httpConn = new HttpConnector();
        JsonObject response = httpConn.invokeHttpPost(input, headers,
                URLFinder.getPathUrl(URLConstants.CREATE_KMS_USER, dcRequest));
        boolean status = null != response.get("id");
        result.addParam(new Param("id", response.get("id").getAsString(), MWConstants.STRING));
        result.addParam(new Param("KMSuserMsg", response.get("message").getAsString(), MWConstants.STRING));
        result.addParam(new Param("KMSuserStatus", Boolean.toString(status), MWConstants.STRING));
        return status;
    }

    public static boolean createKMSUser(DataControllerRequest dcRequest, JsonObject input, Map<String, String> headers,
            Result result) throws HttpCallException {
        HttpConnector httpConn = new HttpConnector();
        JsonObject response = httpConn.invokeHttpPost(input, headers,
                URLFinder.getPathUrl(URLConstants.CREATE_KMS_USER, dcRequest));
        boolean status = null != response.get("id");
        result.addParam(new Param("id", response.get("id").getAsString(), MWConstants.STRING));
        result.addParam(new Param("KMSuserMsg", response.get("message").getAsString(), MWConstants.STRING));
        result.addParam(new Param("KMSuserStatus", Boolean.toString(status), MWConstants.STRING));
        return status;
    }

    public static void deleteUser(DataControllerRequest dcRequest, Map<String, String> headers, String id)
            throws HttpCallException {
        headers.put("X-HTTP-Method-Override", "DELETE");
        HttpConnector httpConn = new HttpConnector();
        httpConn.invokeHttpPost(null, headers,
                URLFinder.getPathUrl(URLConstants.CREATE_KMS_USER, dcRequest) + "/" + id);
    }

    public static JsonObject constructKMSEmailJSONObject(DataControllerRequest dcRequest) throws HttpCallException {
        JsonObject JSONObject = new JsonObject();
        JsonObject emailServiceRequest = new JsonObject();
        JsonObject emailsObject = new JsonObject();
        JsonObject emailObject = new JsonObject();
        JsonObject recipientsObject = new JsonObject();
        JsonArray recipientArray = new JsonArray();

        JsonObject recipientArrayItem1 = new JsonObject();
        String emailType=dcRequest.getParameter("emailType");
        diagnostic.prepareDebug("emailType:"+emailType);
       if(emailType != null && emailType.equalsIgnoreCase("HBL_HIMAL_REMIT_FD_REQUEST")) {
        try {
        String emiails=dcRequest.getParameter("email");
        diagnostic.prepareDebug("email parameter:"+emiails);
        JSONArray emiailsArray = new JSONArray(emiails);
        for (int i=0;i<emiailsArray.length();i++) {
        	JsonObject recipientArrayItem2= new JsonObject();
        	recipientArrayItem2.addProperty("emailId", emiailsArray.getJSONObject(i).get("email").toString());
        	recipientArrayItem2.addProperty("type", "TO");
        	recipientArray.add(recipientArrayItem2);
        }
        }catch (Exception e) {
        	 alert.prepareError("Error while converting emails Array:"+e.getLocalizedMessage());
        	throw new HttpCallException(e.getMessage());
		}
       }else {
        recipientArrayItem1.addProperty("emailId", dcRequest.getParameter("email"));
        recipientArrayItem1.addProperty("type", "TO");
        recipientArray.add(recipientArrayItem1);
       }

        recipientsObject.add("recipient", recipientArray);
        emailObject.add("recipients", recipientsObject);

        emailObject.addProperty("priority", "true");
        emailObject.addProperty("startTimeStamp", 0);
        emailObject.addProperty("endTimeStamp", 0);

        Result template = getTemplateFromDB(dcRequest);
        String contextParams = dcRequest.getParameter("additionalContext");

        String content = "";
        String subject = dcRequest.getParameter(MFAConstants.EMAIL_SUBJECT);
        if (StringUtils.isNotBlank(dcRequest.getParameter(MFAConstants.EMAIL_BODY))) {
            content = dcRequest.getParameter(MFAConstants.EMAIL_BODY);
            emailObject.addProperty("subject", dcRequest.getParameter(MFAConstants.EMAIL_SUBJECT));
        } else {
            content = getEmailContent(dcRequest, template, contextParams);
            if (StringUtils.isBlank(subject))
                subject = HelperMethods.getFieldValue(template, "Subject");
            
           try {
        	   
        	   byte[] subjectBytes = subject.getBytes();
        	   String asciiencodedsubject = new String(subjectBytes, StandardCharsets.ISO_8859_1);	
               emailObject.addProperty("subject",constructSMSContent(asciiencodedsubject,contextParams));
           }catch(Exception e) {
        	   alert.prepareError("Error while converting utf8 for subject in constructKMSEmailJSONObject method :",e).log();
           }
        }
        try {
        	byte[] contentBytes = content.getBytes();
    		String asciiencodedcontent = new String(contentBytes, StandardCharsets.ISO_8859_1);	
            emailObject.addProperty("content", asciiencodedcontent);
        }catch(Exception e) {
        	 alert.prepareError("Error while converting utf8 for content in constructKMSEmailJSONObject method :",e).log();
        }

        emailsObject.add("email", emailObject);
        emailServiceRequest.add("emails", emailsObject);
        JSONObject.add("emailServiceRequest", emailServiceRequest);

        return JSONObject;
    }
    

    private static String getEmailContent(DataControllerRequest dcRequest, Result template, String contextParams)
            throws HttpCallException {

        String content = HelperMethods.getFieldValue(template, "TemplateText");
        if (null != dcRequest.getParameter("firstName")) {
            content = content.replaceAll("%firstName%", dcRequest.getParameter("firstName"));
        }
        if (null != dcRequest.getParameter("lastName")) {
            content = content.replaceAll("%lastName%", dcRequest.getParameter("lastName"));
        }
        if (null != dcRequest.getParameter("email")) {
            content = content.replaceAll("%email%", dcRequest.getParameter("email"));
        }
        content = constructSMSContent(content, contextParams);
        return content;
    }

    private static Result getTemplateFromDB(DataControllerRequest dcRequest) throws HttpCallException {
        String templateType = null;
        if (StringUtils.isNotBlank(dcRequest.getParameter("emailType"))) {
            templateType = dcRequest.getParameter("emailType");
        } else {
            templateType = dcRequest.getParameter("messageType");
        }
        String filter = "TemplateName" + DBPUtilitiesConstants.EQUAL + templateType;
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.EMAIL_TEMPLATE_READ);
    }

    public static JsonObject constructCreateKMSUserJSONObject(String firstName, String lastName, String email,
            String phone) {
        JsonObject JSONObject = new JsonObject();
        if (StringUtils.isBlank(firstName)) {
            firstName = "kmsuser" + HelperMethods.getNumericId();
        }
        if (StringUtils.isBlank(lastName)) {
            lastName = "dbx";
        }
        if (StringUtils.isBlank(phone)) {
            phone = "+919" + genPhoneNumber();
        }
        if (StringUtils.isBlank(email)) {
            email = firstName + "." + lastName + "@kony.com";
        }
        JSONObject.addProperty("firstName", firstName);
        JSONObject.addProperty("lastName", lastName);
        JSONObject.addProperty("email", email);
        JSONObject.addProperty("mobileNumber", phone);
        JSONObject.addProperty("state", "Telangana");
        JSONObject.addProperty("country", "India");
        JSONObject.addProperty("smsSubscription", true);
        JSONObject.addProperty("emailSubscription", true);
        return JSONObject;
    }

    public static String genPhoneNumber() {
        SecureRandom generator = new SecureRandom();
        return generator.nextInt(9999) + 10000 + "" + (generator.nextInt(9999) + 10000);
    }

    public static JsonObject convertTOJson(String jsonString) {
        if (StringUtils.isNotBlank(jsonString)) {
            JsonParser jsonParser = new JsonParser();
            return (JsonObject) jsonParser.parse(jsonString);
        }
        return new JsonObject();
    }

    public static String getAuthToken(DataControllerRequest dcRequest) {
        return (null != dcRequest.getSession().getAttribute("X-Kony-Authorization"))
                ? dcRequest.getSession().getAttribute("X-Kony-Authorization").toString()
                : "";
    }

    public static JSONObject constructSMSPayload(Map<String, String> inputParams, DataControllerRequest dcRequest)
            throws Exception {
        try {
            String sendToMobiles = inputParams.get("sendToMobiles");
            String inputContext = inputParams.get("content");

            if (StringUtils.isBlank(sendToMobiles)) {
                sendToMobiles = dcRequest.getParameter("sendToMobiles");
            }
            if (StringUtils.isBlank(inputContext)) {
                inputContext = dcRequest.getParameter("content");
            }

            JSONArray recipient = constructSMSRecipientArray(sendToMobiles);
            JSONObject recipients = new JSONObject();
            recipients.put("recipient", recipient);

            Result template = getTemplateFromDB(dcRequest);
            String content = "";
            if (dcRequest.getParameter(MFAConstants.SMS_TEXT) != null) {
                content = dcRequest.getParameter(MFAConstants.SMS_TEXT);
            } else {
                content = constructSMSContent(HelperMethods.getFieldValue(template, "TemplateText"), inputContext);
            }

            JSONObject message = new JSONObject();
            message.put("recipients", recipients);
            message.put("content", content);
            message.put("priorityService", "true");
            message.put("startTimestamp", "0");
            message.put("expiryTimestamp", "0");

            JSONObject messages = new JSONObject();
            messages.put("message", message);

            JSONObject smsServiceRequest = new JSONObject();
            smsServiceRequest.put("messages", messages);
            JSONObject finalPayload = new JSONObject();
            finalPayload.put("smsServiceRequest", smsServiceRequest);
            return finalPayload;
        } catch (Exception loansException) {
            throw loansException;
        }
    }

    public static String constructSMSContent(String content, String inputContext) {

        if (StringUtils.isNotBlank(inputContext)) {
            String[] strings = inputContext.split(";");
            for (String str : strings) {
                String key = str.split(":")[0];
                String value = str.substring(key.length() + 1);
                if (value.contains("$")) {
                    List<Character> charList = new ArrayList<>();
                    int index = 0;
                    while (index < value.length()) {
                        if (value.charAt(index) == '$') {
                            charList.add('\\');
                            charList.add(value.charAt(index));
                            index++;
                            continue;
                        }
                        charList.add(value.charAt(index));
                        index++;
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Character ch : charList) {
                        sb.append(ch);
                    }
                    value = sb.toString();
                }
                content = content.replaceAll("%" + key + "%", value);
            }
        }

        return content;
    }

    public static JSONArray constructSMSRecipientArray(String mobileNumbers) {
        String[] mobileNumberArray = mobileNumbers.split(",");
        JSONArray recipientArray = new JSONArray();
        for (int i = 0; i < mobileNumberArray.length; i++) {
            String mobileNumber = mobileNumberArray[i];
            if (!mobileNumber.isEmpty()) {
                JSONObject recipientJSON = new JSONObject();
                recipientJSON.put("mobile", "+" + mobileNumber);
                recipientArray.put(i, recipientJSON);
            }
        }
        return recipientArray;
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    private static boolean isRequestAllowed(DataControllerRequest dcRequest, String requestType, String value,
            int allowedCount) throws HttpCallException {
        if (StringUtils.isNotBlank(value)) {
            value = "'" + value + "'";
            String today = HelperMethods.getCurrentDate();
            String startDate = today + "T00:00:00";
            String endDate = today + "T23:59:59";
            String filter = requestType + DBPUtilitiesConstants.EQUAL + value + DBPUtilitiesConstants.AND + "("
                    + "createdts" + DBPUtilitiesConstants.GREATER_EQUAL + startDate + DBPUtilitiesConstants.AND
                    + "createdts" + DBPUtilitiesConstants.LESS_EQUAL + endDate + ")";
            Result otpCount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.OTP_GET);
            if (HelperMethods.hasRecords(otpCount)) {
                int count = otpCount.getAllDatasets().get(0).getAllRecords().size();
                if (count >= allowedCount) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void insertAuditRecord(DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put("Phone", dcRequest.getParameter("sendToMobiles"));
        inputParams.put("Email", dcRequest.getParameter("email"));
        inputParams.put(MFAConstants.SECURITY_KEY, UUID.randomUUID().toString());
        HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest), URLConstants.OTP_CREATE);
    }

    public static boolean isSMSRequestAllowed(DataControllerRequest dcRequest) throws HttpCallException {
        int allowedCount = getAllowedCount(dcRequest, URLConstants.OTP_REQUEST_LIMIT);
        return isRequestAllowed(dcRequest, "Phone", dcRequest.getParameter("sendToMobiles"), allowedCount);
    }

    public static boolean isEmailRequestAllowed(DataControllerRequest dcRequest) throws HttpCallException {
        int allowedCount = getAllowedCount(dcRequest, URLConstants.EMAIL_REQUEST_LIMIT);
        return isRequestAllowed(dcRequest, "Email", dcRequest.getParameter("email"), allowedCount);
    }

    private static int getAllowedCount(DataControllerRequest dcRequest, String requestType) {
        String requestLimit = URLFinder.getPathUrl(requestType, dcRequest);
        if (StringUtils.isNotBlank(requestLimit)) {
            return Integer.parseInt(requestLimit);
        }
        return 6;
    }

    public static String getOTPContent(String otp, String content, JSONObject contentJsonObject) {

        String response = "";
        if (StringUtils.isNotBlank(content) && KMSUtil.isJSONValid(content)) {
            JSONObject contentJson = new JSONObject(content);
            for (String key : contentJson.keySet()) {
                response += key + ":" + contentJson.getString(key) + ";";
            }
        }

        if (contentJsonObject != null) {
            for (String key : contentJsonObject.keySet()) {
                response += key + ":" + contentJsonObject.getString(key) + ";";
            }
        }

        if (StringUtils.isNotBlank(otp)) {
            response += "otp:" + otp;
        }

        return response;
    }
}
