package com.kony.adminconsole.service.customerrequest;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.handler.AuditHandler;
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

/**
 * Service to manage the Message Templates (Create,Update,Delete)
 *
 * @author Aditya Mankal
 */
public class MessageTemplateManageService implements JavaService2 {

    private static final int TEMPLATE_NAME_MIN_CHARS = 5;
    private static final int TEMPLATE_NAME_MAX_CHARS = 80;
    private static final int TEMPLATE_BODY_MIN_CHARS = 5;
    private static final int TEMPLATE_BODY_MAX_CHARS = 1500;

    private static final String CREATE_MESSAGE_TEMPLATE_METHOD_ID = "createMessageTemplate";
    private static final String UPDATE_MESSAGE_TEMPLATE_METHOD_ID = "updateMessageTemplate";
    private static final String DELETE_MESSAGE_TEMPLATE_METHOD_ID = "deleteMessageTemplate";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
        	alert.prepareError("Method Id" + methodID).log();
            diagnostic.prepareDebug("Method Id" + methodID).log();

            // Fetch logged in user details
            UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
            String userId = userDetailsBeanInstance.getId();

            // Read Inputs
            String templateID = requestInstance.getParameter("templateID");
            
            if (methodID.equalsIgnoreCase(DELETE_MESSAGE_TEMPLATE_METHOD_ID)) {
                return deleteMessageTemplate(userId, templateID, requestInstance);
            }
            
            String templateName = getXSSSanitizedString(
            							CommonUtilities.messageDescriptionValidation(requestInstance.getParameter("templateName")));
            String templateBody = requestInstance.getParameter("templateBody");
            String additionalNote = getXSSSanitizedString(
            							CommonUtilities.messageDescriptionValidation(requestInstance.getParameter("additionalNote")));

            if (methodID.equalsIgnoreCase(CREATE_MESSAGE_TEMPLATE_METHOD_ID)) {
                return createMessageTemplate(userId, templateName, templateBody, additionalNote, requestInstance);
            }

            else if (methodID.equalsIgnoreCase(UPDATE_MESSAGE_TEMPLATE_METHOD_ID)) {
                return updateMessageTemplate(userId, templateID, templateName, templateBody, additionalNote,
                        requestInstance);
            }

            return new Result();
        } catch (Exception e) {
            // Exception
            alert.prepareError("Exception. Trace:", e).log();
            Result errorResult = new Result();
            ErrorCodeEnum.ERR_20146.setErrorCode(errorResult);
            return errorResult;
        }
    }

    /**
     * Method to update a Message Template
     * 
     * @param userId
     * @param templateId
     * @param templateName
     * @param templateBody
     * @param additionalNote
     * @param requestInstance
     * @return operation result
     */
    private Result updateMessageTemplate(String userId, String templateId, String templateName, String templateBody,
            String additionalNote, DataControllerRequest requestInstance) {

        Result operationResult = new Result();

        boolean isValidData = true;
        Map<String, String> inputMap = new HashMap<>();
        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");

        // Validate Inputs
        if (StringUtils.isBlank(templateId)) {
            isValidData = false;
            errorMessageBuffer.append("Template Id is a mandatory input for an Update Request");
        }
        inputMap.put("id", templateId);
        if (templateName != null) {
            if (templateName.length() < TEMPLATE_NAME_MIN_CHARS || templateName.length() > TEMPLATE_NAME_MAX_CHARS) {
                isValidData = false;
                errorMessageBuffer.append("Template Name should have atleast " + TEMPLATE_NAME_MIN_CHARS
                        + " characters and a maximum of " + TEMPLATE_NAME_MAX_CHARS + " characters");
            }
            inputMap.put("Name", templateName);
        }
        if (templateBody != null) {
            String decodedTemplateBody = getXSSSanitizedString(
            								CommonUtilities.messageDescriptionValidation(
            									CommonUtilities.decodeFromBase64(templateBody)));
            String decodedStringWithTags = getStringFromXSSSanitizedString(decodedTemplateBody);
            if (decodedStringWithTags.length() < TEMPLATE_BODY_MIN_CHARS
                    || decodedStringWithTags.length() > TEMPLATE_BODY_MAX_CHARS) {
                isValidData = false;
                errorMessageBuffer.append("Template Body should have atleast " + TEMPLATE_BODY_MIN_CHARS
                        + " characters and a maximum of " + TEMPLATE_BODY_MAX_CHARS + " characters");
            } else {
                inputMap.put("Body", decodedTemplateBody);
            }
        }
        if (additionalNote != null) {
            inputMap.put("AdditionalInfo", additionalNote);
        }

        if (isValidData == false) {
            // Return Error Response
            alert.prepareError("Invalid Payload. Info:" + errorMessageBuffer.toString()).log();
            operationResult
                    .addParam(new Param("validationError", errorMessageBuffer.toString(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_20140.setErrorCode(operationResult);
            return operationResult;
        }

        // Update Message Template
        ActivityStatusEnum activityStatus = null;
        inputMap.put("modifiedby", userId);
        inputMap.put("lastmodifiedts", CommonUtilities.getISOFormattedLocalTimestamp());
        String operationResponse = Executor.invokeService(ServiceURLEnum.MESSAGETEMPLATE_UPDATE, inputMap, null,
                requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            activityStatus = ActivityStatusEnum.SUCCESSFUL;
            operationResult.addParam(new Param("status", "success", FabricConstants.STRING));
        } else {
            alert.prepareError("Failed CRUD Operation").log();
            activityStatus = ActivityStatusEnum.FAILED;
            ErrorCodeEnum.ERR_20141.setErrorCode(operationResult);
        }

        // Audit Activity
        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MESSAGES, EventEnum.UPDATE, activityStatus,
                "Template Id: " + templateId);

        // Return Response
        return operationResult;
    }

    /**
     * Method to create a Message Template
     * 
     * @param userId
     * @param templateName
     * @param templateBody
     * @param additionalNote
     * @param requestInstance
     * @return Operation Result
     */
    private Result createMessageTemplate(String userId, String templateName, String templateBody, String additionalNote,
            DataControllerRequest requestInstance) {

        Result operationResult = new Result();

        boolean isValidData = true;
        Map<String, String> inputMap = new HashMap<>();
        StringBuffer errorMessageBuffer = new StringBuffer();
        errorMessageBuffer.append("ERROR:");
        // Validate Inputs
        
        if (templateName == null || templateName.length() < TEMPLATE_NAME_MIN_CHARS
                || templateName.length() > TEMPLATE_NAME_MAX_CHARS) {
            isValidData = false;
            errorMessageBuffer.append("Template Name should have atleast " + TEMPLATE_NAME_MIN_CHARS
                    + " characters and a maximum of " + TEMPLATE_NAME_MAX_CHARS + " characters");
        } else {
            inputMap.put("Name", templateName);
        }
        if (templateBody != null) {
            String decodedTemplateBody = getXSSSanitizedString(
            								CommonUtilities.messageDescriptionValidation(
            									CommonUtilities.decodeFromBase64(templateBody)));
            String decodedStringWithTags = getStringFromXSSSanitizedString(decodedTemplateBody);
            if (decodedStringWithTags.length() < TEMPLATE_BODY_MIN_CHARS
                    || decodedStringWithTags.length() > TEMPLATE_BODY_MAX_CHARS) {
                isValidData = false;
                errorMessageBuffer.append("Template Body should have atleast " + TEMPLATE_BODY_MIN_CHARS
                        + " characters and a maximum of " + TEMPLATE_BODY_MAX_CHARS + " characters");
            } else {
                inputMap.put("Body", decodedTemplateBody);
            }
        }
        if (additionalNote != null) {
            inputMap.put("AdditionalInfo", additionalNote);
        }

        if (isValidData == false) {
            // Return Error Response
            alert.prepareError("Invalid Payload. Info:" + errorMessageBuffer.toString()).log();
            operationResult
                    .addParam(new Param("validationError", errorMessageBuffer.toString(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_20140.setErrorCode(operationResult);
            return operationResult;
        }

        // Create Message Template
        ActivityStatusEnum activityStatus = null;
        inputMap.put("id", CommonUtilities.getNewId().toString());
        inputMap.put("createdby", userId);
        inputMap.put("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
        String operationResponse = Executor.invokeService(ServiceURLEnum.MESSAGETEMPLATE_CREATE, inputMap, null,
                requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            activityStatus = ActivityStatusEnum.SUCCESSFUL;
            operationResult.addParam(new Param("status", "success", FabricConstants.STRING));
        } else {
            alert.prepareError("Failed CRUD Operation").log();
            activityStatus = ActivityStatusEnum.FAILED;
            ErrorCodeEnum.ERR_20142.setErrorCode(operationResult);
        }

        // Audit Activity
        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MESSAGES, EventEnum.CREATE, activityStatus,
                "Template Name: " + templateName);

        // Return Response
        return operationResult;

    }

    /**
     * Method to delete Message Template
     * 
     * @param userID
     * @param templateId
     * @param requestInstance
     * @return Operation Response
     */
    private Result deleteMessageTemplate(String userID, String templateId, DataControllerRequest requestInstance) {

        Result operationResult = new Result();

        // Validate Input
        if (StringUtils.isBlank(templateId)) {
            String errorMessage =
                    "Template Id is a mandatory input for a Delete Request.It cannot be a NULL/Empty input.";
            alert.prepareError("Invalid Payload. Info:" + errorMessage).log();
            operationResult.addParam(new Param("validationError", errorMessage, FabricConstants.STRING));
            ErrorCodeEnum.ERR_20140.setErrorCode(operationResult);
            return operationResult;
        }

        String templateName = StringUtils.EMPTY;
        try {
            // Fetch Template Name
            Map<String, String> readinputMap = new HashMap<>();
            readinputMap.put(ODataQueryConstants.FILTER, "id eq " + templateId);
            String operationResponse = Executor.invokeService(ServiceURLEnum.MESSAGETEMPLATE_READ, readinputMap, null,
                    requestInstance);
            JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
            templateName = operationResponseJSON.getJSONArray("messagetemplate").getJSONObject(0).getString("Name");
        } catch (Exception ignored) {
            // Supressed Exception
        }

        // Delete Message Template
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("id", templateId);
        String operationResponse = Executor.invokeService(ServiceURLEnum.MESSAGETEMPLATE_DELETE, inputMap, null,
                requestInstance);
        JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
        ActivityStatusEnum activityStatus = null;
        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
            diagnostic.prepareDebug("Successful CRUD Operation").log();
            activityStatus = ActivityStatusEnum.SUCCESSFUL;
            operationResult.addParam(new Param("status", "success", FabricConstants.STRING));
        } else {
            alert.prepareError("Failed CRUD Operation").log();
            activityStatus = ActivityStatusEnum.FAILED;
            ErrorCodeEnum.ERR_20143.setErrorCode(operationResult);
        }

        // Audit Activity
        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.MESSAGES, EventEnum.DELETE, activityStatus,
                "Template Name: " + templateName);

        // Return Response
        return operationResult;
    }

    private String getXSSSanitizedString(String inputString)
    { 			
    	String filteredOutput = new String(inputString);
    	if (filteredOutput != null) {
            // Avoid null characters
    		filteredOutput = filteredOutput.replaceAll("\0", "");
            // Eliminates the "<", ">", "\'", "\/", "\"", "=" from the string
            filteredOutput = filteredOutput.replaceAll(">", "&gt;");
            filteredOutput = filteredOutput.replaceAll("<", "&lt;");
            filteredOutput = filteredOutput.replaceAll("\"", "&quot;");
            filteredOutput = filteredOutput.replaceAll("\'", "&#x27;");
            filteredOutput = filteredOutput.replaceAll("/", "&#x2F;");
            filteredOutput = filteredOutput.replaceAll("=", "&eq;");
            filteredOutput = filteredOutput.replaceAll("\\^", "&#94;");
            filteredOutput = filteredOutput.replaceAll("\\*", "&#42;");
        }
        return filteredOutput;
    }
    
    private String getStringFromXSSSanitizedString(String inputString)
    { 			
    	String filteredOutput = new String(inputString);
    	if (filteredOutput != null) {
            // Eliminates the "<", ">", "\'", "\/", "\"", "=" from the string
            filteredOutput = filteredOutput.replaceAll("&gt;", ">");
            filteredOutput = filteredOutput.replaceAll("&lt;", "<");
            filteredOutput = filteredOutput.replaceAll("&quot;", "\"");
            filteredOutput = filteredOutput.replaceAll("&#x27;", "\'");
            filteredOutput = filteredOutput.replaceAll("&#x2F;", "/");
            filteredOutput = filteredOutput.replaceAll("&eq;", "=");
            filteredOutput = filteredOutput.replaceAll("&#94;", "\\^");
            filteredOutput = filteredOutput.replaceAll("&#42;", "\\*");
        }
        return filteredOutput;
    }
    
}