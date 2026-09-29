package com.kony.adminconsole.service.kms;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.Map;
//import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
//import com.kony.adminconsole.dto.EmailHandlerBean;
import com.kony.adminconsole.dto.EmailServiceBean;
import com.kony.adminconsole.handler.AuditHandler;
//import com.kony.adminconsole.handler.EmailHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EmailService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            Result processedResult = new Result();

            String baseURL = EnvironmentConfiguration.AC_HOST_URL.getValue(requestInstance);
            EmailServiceBean emailServiceBean = new EmailServiceBean();

            emailServiceBean.setEmailSubject(requestInstance.getParameter("emailSubject"));
            emailServiceBean.setRecipientEmailId(requestInstance.getParameter("recipientEmailId"));
            emailServiceBean.setCc(requestInstance.getParameter("cc"));
            emailServiceBean.setEmailType(requestInstance.getParameter("emailType"));
            if (requestInstance.getParameter("vizServerURL") != null
                    && requestInstance.getParameter("vizServerURL").contains("localhost")) {
                emailServiceBean.setVizServerURL(requestInstance.getParameter("vizServerURL"));
            } else {
                emailServiceBean.setVizServerURL(baseURL + "/apps/Spotlight/#_frmErrorLogin");
            }
            emailServiceBean.setPasswordUUID(CommonUtilities.getNewId().toString());
            emailServiceBean.setAdditionalContext(
                    CommonUtilities.getStringAsJSONObject(requestInstance.getParameter("AdditionalContext")));

//            EmailHandlerBean emailHandlerBeanInstance = new EmailHandlerBean();
//            EmailHandler.getKMSAuthToken(emailHandlerBeanInstance, requestInstance);
//            emailServiceBean.setClaimsToken(emailHandlerBeanInstance.getKMSClaimsToken());

            if (emailServiceBean.getEmailType().equals("resetPassword")) {
            	// as per AAC-6672 - for both valid OR invalid account details, there should be a generic response 
            	// network call response cannot include emailPresentInDB

                // ** Checking if email id exists in database **
                Map<String, String> headerParametersMap = new HashMap<String, String>();

                Map<String, String> postParametersMap = new HashMap<String, String>();
                postParametersMap.put(ODataQueryConstants.SELECT, "id, Username, FirstName, LastName");
                postParametersMap.put(ODataQueryConstants.FILTER,
                        "Email eq '" + emailServiceBean.getRecipientEmailId() + "'");

                String readSystemUserResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERDETAILS_VIEW_READ,
                        postParametersMap, headerParametersMap, requestInstance);
                JSONObject readSystemUserJSON = CommonUtilities.getStringAsJSONObject(readSystemUserResponse);

                if (readSystemUserJSON != null && readSystemUserJSON.has(FabricConstants.OPSTATUS)
                        && readSystemUserJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                    JSONObject userProfile = null;
                    JSONArray array = readSystemUserJSON.optJSONArray("internaluserdetails_view");
                    if (array != null && array.length() == 1) {
                        userProfile = array.optJSONObject(0);
                    } else if(array != null && array.length() > 1) {
                    	ErrorCodeEnum.ERR_22216.setErrorCode(processedResult);
                    	return processedResult;
                    }

                    if (userProfile != null) {
                        emailServiceBean.setId(userProfile.optString("id"));
                        emailServiceBean.setUsername(userProfile.optString("Username"));
                        emailServiceBean.setFirstName(userProfile.optString("FirstName"));
                        emailServiceBean.setLastName(userProfile.optString("LastName"));

                        // ** Updating UUID against email in database **
                        postParametersMap.clear();
                        postParametersMap.put("id", emailServiceBean.getId());
                        postParametersMap.put("ResetpasswordLink", emailServiceBean.getPasswordUUID());
                        postParametersMap.put("ResetPasswordExpdts", getTodaysTimestamp());

                        String updateSystemUserResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_UPDATE,
                                postParametersMap, headerParametersMap, requestInstance);
                        JSONObject updateSystemUserResponseJSON = CommonUtilities
                                .getStringAsJSONObject(updateSystemUserResponse);

                        if (updateSystemUserResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                            int updatedRecords = updateSystemUserResponseJSON.getInt("updatedRecords");

                            if (updatedRecords == 1) {

                                JSONObject sendKMSEmailJSON = createUserAndSendEmail(emailServiceBean, requestInstance);
                                if(StringUtils.isNotBlank(sendKMSEmailJSON.getString("referenceId"))) {
                                	diagnostic.prepareDebug("Reset password status Success, referenceId :" + sendKMSEmailJSON.getString("referenceId")).log();
                                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN,
                                            EventEnum.COMMUNICATION, ActivityStatusEnum.SUCCESSFUL,
                                            "Successfully sent email. Email address: "
                                                    + emailServiceBean.getRecipientEmailId());
                                } else {
                                	
                                	diagnostic.prepareDebug("Reset password status Failed, dbpErrMsg :" + sendKMSEmailJSON.getString("dbpErrMsg")).log();
                                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN,
                                            EventEnum.COMMUNICATION, ActivityStatusEnum.FAILED,
                                            "SFailed to send email. Email address: "
                                                    + emailServiceBean.getRecipientEmailId());
                                }
                            } else {
                                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN,
                                        EventEnum.COMMUNICATION, ActivityStatusEnum.FAILED,
                                        "Failed to send email. Email address: "
                                                + emailServiceBean.getRecipientEmailId());
                                diagnostic.prepareDebug("Reset Password- Failed to send email. Email address: " + emailServiceBean.getRecipientEmailId()).log();
                            }
                        } else {
                            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN,
                                    EventEnum.COMMUNICATION, ActivityStatusEnum.FAILED,
                                    "Failed to send email. Email address: " + emailServiceBean.getRecipientEmailId());
                            diagnostic.prepareDebug("Reset Password- Failed to send email. Email address: " + emailServiceBean.getRecipientEmailId()).log();
                        }
                    } else{
                    	ErrorCodeEnum.ERR_22216.setErrorCode(processedResult);
                    	return processedResult;
                    }
                    
                } else {
                	diagnostic.prepareDebug("Reset Password - Cannot fetch account details for email address: " + emailServiceBean.getRecipientEmailId()).log();
                	ErrorCodeEnum.ERR_22122.setErrorCode(processedResult);
                	return processedResult;
                }
                return processedResult;
            } 
            else if (emailServiceBean.getEmailType().equals("createUser")) {

                // ** Checking if email id exists in database **
                Map<String, String> headerParametersMap = new HashMap<String, String>();

                Map<String, String> postParametersMap = new HashMap<String, String>();
                postParametersMap.put(ODataQueryConstants.SELECT, "id, Username, FirstName, LastName");
                postParametersMap.put(ODataQueryConstants.FILTER,
                        "Email eq '" + emailServiceBean.getRecipientEmailId() + "'");

                String readSystemUserResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERDETAILS_VIEW_READ,
                        postParametersMap, headerParametersMap, requestInstance);
                JSONObject readSystemUserJSON = CommonUtilities.getStringAsJSONObject(readSystemUserResponse);

                if (readSystemUserJSON != null && readSystemUserJSON.has(FabricConstants.OPSTATUS)
                        && readSystemUserJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                    JSONObject userProfile = null;
                    JSONArray array = readSystemUserJSON.optJSONArray("internaluserdetails_view");
                    if (array != null && array.length() == 1) {
                        userProfile = array.optJSONObject(0);
                    }

                    if (userProfile != null) {
                        emailServiceBean.setId(userProfile.optString("id"));
                        emailServiceBean.setUsername(userProfile.optString("Username"));
                        emailServiceBean.setFirstName(userProfile.optString("FirstName"));
                        emailServiceBean.setLastName(userProfile.optString("LastName"));

                        // ** Updating UUID against email in database **
                        postParametersMap.clear();
                        postParametersMap.put("id", emailServiceBean.getId());
                        postParametersMap.put("ResetpasswordLink", emailServiceBean.getPasswordUUID());
                        postParametersMap.put("ResetPasswordExpdts", getTodaysTimestamp());

                        String updateSystemUserResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_UPDATE,
                                postParametersMap, headerParametersMap, requestInstance);
                        JSONObject updateSystemUserResponseJSON = CommonUtilities
                                .getStringAsJSONObject(updateSystemUserResponse);

                        if (updateSystemUserResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                            int updatedRecords = updateSystemUserResponseJSON.getInt("updatedRecords");

                            if (updatedRecords == 1) {

                                JSONObject sendKMSEmailJSON = createUserAndSendEmail(emailServiceBean, requestInstance);

                                if(StringUtils.isNotBlank(sendKMSEmailJSON.getString("referenceId"))) {
                                	diagnostic.prepareDebug("Reset password status Success, referenceId :" + sendKMSEmailJSON.getString("referenceId")).log();
                                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS,
                                            EventEnum.COMMUNICATION, ActivityStatusEnum.SUCCESSFUL,
                                            "Successfully sent email. Email address: "
                                                    + emailServiceBean.getRecipientEmailId());
                                    processedResult.addParam(new Param("message", "Success",
                                            FabricConstants.STRING));
                                } else {
                                	
                                	diagnostic.prepareDebug("Reset password status Failed, dbpErrMsg :" + sendKMSEmailJSON.getString("dbpErrMsg")).log();
                                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS,
                                            EventEnum.COMMUNICATION, ActivityStatusEnum.FAILED,
                                            "SFailed to send email. Email address: "
                                                    + emailServiceBean.getRecipientEmailId());
                                    processedResult.addParam(new Param("message", "Failed",
                                            FabricConstants.STRING));
                                }
                                
                            } else {
                                AuditHandler.auditAdminActivity(requestInstance,ModuleNameEnum.USERS,
                                        EventEnum.COMMUNICATION, ActivityStatusEnum.FAILED,
                                        "Failed to send email. Email address: "
                                                + emailServiceBean.getRecipientEmailId());
                                ErrorCodeEnum.ERR_21003.setErrorCode(processedResult);
                                return processedResult;
                            }
                        } else {
                            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.USERS,
                                    EventEnum.COMMUNICATION, ActivityStatusEnum.FAILED,
                                    "Failed to send email. Email address: " + emailServiceBean.getRecipientEmailId());
                            ErrorCodeEnum.ERR_21003.setErrorCode(processedResult);
                            return processedResult;
                        }
                        processedResult.addParam(new Param("emailPresentInDB", "true", "boolean"));
                    } else {
                        processedResult.addParam(new Param("emailPresentInDB", "false", "boolean"));
                    }
                } else {
                    ErrorCodeEnum.ERR_21003.setErrorCode(processedResult);
                    return processedResult;
                }
            } else if (emailServiceBean.getEmailType().equals("EnrollCustomer")
                    || emailServiceBean.getEmailType().equals("InternalUserEdit")
                    || emailServiceBean.getEmailType().equals("InternalUserStatusChange")) {

                emailServiceBean.setFirstName(emailServiceBean.getAdditionalContext().getString("name"));
                emailServiceBean.setLastName(emailServiceBean.getAdditionalContext().getString("name"));
                emailServiceBean.setId(emailServiceBean.getAdditionalContext().getString("InternalUser_id"));

                JSONObject sendKMSEmailJSON = createUserAndSendEmail(emailServiceBean, requestInstance);
                if(StringUtils.isNotBlank(sendKMSEmailJSON.getString("referenceId"))) {
                	
                	processedResult.addParam(new Param("message", "Success", FabricConstants.STRING));
                
                } else {
                		
                	processedResult.addParam(new Param("message", "Failed", FabricConstants.STRING));
                }
                
            }

            return processedResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

    private JSONObject createUserAndSendEmail(EmailServiceBean emailServiceBean,
            DataControllerRequest requestInstance) {

//        Map<String, String> headers = new HashMap<>();
//        headers.put("KMSAuthToken", emailServiceBean.getClaimsToken());

        // ** Creating the user in the subscriber list **
//        Map<String, String> createKMSUserJSONObject = constructCreateKMSUser(emailServiceBean);
//
//        String createKMSUserResponse = Executor.invokeService(ServiceURLEnum.EMAILSERVICE_ENROLLUSER,
//                createKMSUserJSONObject, headers, requestInstance);
//        diagnostic.prepareDebug("Create KMS User Response:" + createKMSUserResponse).log();

        // ** Sending the mail **
//        Map<String, String> sendKMSEmailJSONObject = constructSendKMSEmail(emailServiceBean, requestInstance);
    	String content = buildEmailContent(getEmailContent(emailServiceBean, requestInstance));
    	
    	Map<String, Object> inputmap = new HashMap<>();
        inputmap.put("inputparams", buildpayload(emailServiceBean, content));
        
        String responseString = StringUtils.EMPTY;
		try {
			responseString = DBPServiceExecutorBuilder.builder().withOperationId("sendEmail")
			        .withRequestParameters(inputmap).withServiceId("notificationInvokeService").build().getResponse();
		} catch (DBPApplicationException e) {
			
			alert.prepareError("Something went wrong in sendEmal service api:  "+ e).log();
		}
        
//        String sendKMSEmailResponse = Executor.invokeService(ServiceURLEnum.EMAILSERVICE_SENDEMAIL,
//                sendKMSEmailJSONObject, headers, requestInstance);
        JSONObject sendKMSEmailJSON = CommonUtilities.getStringAsJSONObject(responseString);

        ModuleNameEnum moduleName = null;
        switch (emailServiceBean.getEmailType()) {
            case "createUser":
                moduleName = ModuleNameEnum.USERS;
                break;
            case "resetPassword":
                moduleName = ModuleNameEnum.LOGIN;
                break;
            case "InternalUserEdit":
                moduleName = ModuleNameEnum.USERS;
                break;
            case "InternalUserStatusChange":
                moduleName = ModuleNameEnum.USERS;
                break;
            case "EnrollCustomer":
                moduleName = ModuleNameEnum.CUSTOMERS;
                break;
        }
        if (sendKMSEmailJSON != null && StringUtils.isNotBlank(sendKMSEmailJSON.optString("referenceId"))) {
            // SUCCESS :: Email
            AuditHandler.auditAdminActivity(requestInstance, moduleName, EventEnum.COMMUNICATION,
                    ActivityStatusEnum.SUCCESSFUL,
                    "Failed to send email. Email address: " + emailServiceBean.getRecipientEmailId());
        } else {
            // FAILURE :: Email
            AuditHandler.auditAdminActivity(requestInstance, moduleName, EventEnum.COMMUNICATION,
                    ActivityStatusEnum.FAILED,
                    "Failed to send email. Email address: " + emailServiceBean.getRecipientEmailId());
        }
        return sendKMSEmailJSON;
    }

    private static String getTodaysTimestamp() {
        // --OData query timestamp--
        DateFormat dfD = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dfT = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String dateString = dfD.format(date) + "T" + dfT.format(date);

        return dateString;
    }

    public String getEmailContent(EmailServiceBean emailServiceBean, DataControllerRequest requestInstance) {

        String content = StringUtils.EMPTY;
        String parameterString = emailServiceBean.getPasswordUUID() + "_-_" + emailServiceBean.getId() + "_-_"
                + emailServiceBean.getUsername();
        String resetPasswordLink = emailServiceBean.getVizServerURL() + "?qp="
                + CommonUtilities.encodeToBase64(parameterString);

        String templateSource = StringUtils.EMPTY;
        if (emailServiceBean.getEmailType().equals("createUser")) {
            templateSource = "emailTemplates/createUser.html";
        } else if (emailServiceBean.getEmailType().equals("resetPassword")) {
            templateSource = "emailTemplates/resetPassword.html";
        } else if (emailServiceBean.getEmailType().equals("EnrollCustomer")) {
            templateSource = "emailTemplates/enrollCustomer.html";
        } else if (emailServiceBean.getEmailType().equals("InternalUserEdit")) {
            templateSource = "emailTemplates/InternalUserEdit.html";
        } else if (emailServiceBean.getEmailType().equals("InternalUserStatusChange")) {
            templateSource = "emailTemplates/InternalUserStatusChange.html";
        }
        InputStream templateStream =null;

        try {
            templateStream = this.getClass().getClassLoader().getResourceAsStream(templateSource);
            content = IOUtils.toString(templateStream, StandardCharsets.UTF_8);
            if (content != null) {
                if (emailServiceBean.getEmailType().equals("createUser")
                        || emailServiceBean.getEmailType().equals("resetPassword")) {
                    content = content.replaceAll("firstName", emailServiceBean.getFirstName());
                    content = content.replaceAll("resetPasswordLink", resetPasswordLink);
                } else if (emailServiceBean.getEmailType().equals("EnrollCustomer")) {
                    content = content.replaceAll("%customername%",
                            emailServiceBean.getAdditionalContext().getString("name"));
                    content = content.replaceAll("corebankingurl",
                            emailServiceBean.getAdditionalContext().getString("corebankingurl"));
                } else if (emailServiceBean.getEmailType().equals("InternalUserEdit")) {
                    content = content.replaceAll("%firstname%",
                            emailServiceBean.getAdditionalContext().getString("name"));
                    content = content.replaceAll("%username%",
                            emailServiceBean.getAdditionalContext().getString("username"));
                    content = content.replaceAll("%changedfields%",
                            emailServiceBean.getAdditionalContext().getString("ChangedFields"));
                } else if (emailServiceBean.getEmailType().equals("InternalUserStatusChange")) {
                    content = content.replaceAll("%firstname%",
                            emailServiceBean.getAdditionalContext().getString("name"));
                    content = content.replaceAll("%username%",
                            emailServiceBean.getAdditionalContext().getString("username"));
                    content = content.replaceAll("%status%",
                            emailServiceBean.getAdditionalContext().getString("status"));
                }

                String emailTemplateLogo = EnvironmentConfiguration.AC_EMAIL_TEMPLATE_LOGO_URL
                        .getValue(requestInstance);
                content = content.replaceAll("emailTemplateLogo", emailTemplateLogo);
                emailTemplateLogo = EnvironmentConfiguration.AC_EMAIL_TEMPLATE_TEMENOS_LOGO_URL
                        .getValue(requestInstance);
                content = content.replaceAll("emailTemplateTemenosLogo", emailTemplateLogo);
                }
        } catch (Exception e) {
            alert.prepareError("Failed while executing get transaction logs", e).log();
        }
        finally {
        	try {
        	templateStream.close();
        	} 
        	catch (Exception e) {
        	alert.prepareError("ErrorÂ  Occured :", e).log();
        	}
        }
        return content;
    }

//    public static Map<String, String> constructCreateKMSUser(EmailServiceBean emailServiceBean) {
//
//        Map<String, String> map = new HashMap<>();
//        map.put("firstName", emailServiceBean.getFirstName());
//        map.put("lastName", emailServiceBean.getLastName());
//        map.put("email", emailServiceBean.getRecipientEmailId());
//        map.put("mobileNumber", "+919" + CommonUtilities.genPhoneNumber().substring(1));
//        map.put("state", "Telangana");
//        map.put("country", "India");
//        return map;
//    }

//    public Map<String, String> constructSendKMSEmail(EmailServiceBean emailServiceBean,
//            DataControllerRequest requestInstance) {
//
//        Map<String, String> emailPayloadMap = new HashMap<>();
//        emailPayloadMap.put("emailId", emailServiceBean.getRecipientEmailId());
//        emailPayloadMap.put("subject", emailServiceBean.getEmailSubject());
//        emailPayloadMap.put("content",
//                CommonUtilities.encodeToBase64(getEmailContent(emailServiceBean, requestInstance)));
//
//        return emailPayloadMap;
//    }
    
    private static JsonObject buildpayload(EmailServiceBean emailServiceBean, String content) {
        
        JsonObject inputparams = new JsonObject(); 
        
        JsonObject emailservicerequest = new JsonObject();

        String subject = emailServiceBean.getEmailSubject();
        JsonObject email = new JsonObject();
        JsonObject emails = new JsonObject(); 
        JsonObject recipients = new JsonObject();
        JsonArray recipient = new JsonArray();
        recipients.add("recipient", recipient);

    	JsonObject recipientto = new JsonObject();
    	recipientto.addProperty("type", "TO"); 
    	recipientto.addProperty("emailId", emailServiceBean.getRecipientEmailId()); 
    	recipient.add(recipientto);

        email.add("recipients", recipients);
       
        email.addProperty("subject", subject);
        email.addProperty("content", content);
        email.addProperty("priority", "true");
        emails.add("email", email);
        
        emailservicerequest.add("emails", emails);
        
        inputparams.add("emailServiceRequest", emailservicerequest);
        
        return   inputparams;
       
  }
    
  private String buildEmailContent(String content) {
	  
	  if (StringUtils.isNotBlank(content)) {
		  
          content = content.replaceAll("\t", "");
          content = content.replaceAll("\r", "");
          content = content.replaceAll("\n", "");
          content = content.replaceAll("\"", "'");
      }
	  
	  return content;
  }
    
}
