package com.kony.adminconsole.preprocessor;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.handler.CustomerRequestAndMessagesHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

/**
 * <p>
 * Preprocessor for Media post operation
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class MediaPostOperationPreProcessor implements ObjectServicePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final int MAX_FILENAME_LENGTH = 255;
    private static final long MAX_FILE_SIZE = 1000000;

    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
            FabricRequestChain fabricRequestChain) throws Exception {
	Log4j2Configurator.getInstance();

        // Get Response JSON Element
        JsonElement responseJsonElement = fabricResponseManager.getPayloadHandler().getPayloadAsJson();
        if (responseJsonElement == null) {
            diagnostic.prepareDebug("Initializing response body to empty JSON").log();
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(new JsonObject());
            responseJsonElement = fabricResponseManager.getPayloadHandler().getPayloadAsJson();
        }
        JsonObject responseJsonObject = responseJsonElement.getAsJsonObject();

        try {

            // Handlers
            HeadersHandler headersHandler = fabricRequestManager.getHeadersHandler();
            ServicesManager servicesManager = fabricRequestManager.getServicesManager();

            // Get Request JSON Element
            JsonElement requestJsonElement = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            if (requestJsonElement == null || requestJsonElement.getAsJsonObject() == null) {
                // Return Error Message
                alert.prepareError("Input Body is empty. Returning error message").log();
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20131.getErrorCode());
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20131.getMessage());
                AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                        EventEnum.UPLOADFILE, ActivityStatusEnum.FAILED, "Input Body is empty.");
                return;
            }
            JsonObject requestJsonObject = requestJsonElement.getAsJsonObject();

            // Validate File type
            String fileName = requestJsonObject.get("Name").getAsString();
            String attachmentTypeId = CustomerRequestAndMessagesHandler
                    .validateFileTypeAndGetAttachmentTypeId(CommonUtilities.getFileExtension(fileName));
            if (StringUtils.equalsIgnoreCase("INVALID_FILE", attachmentTypeId)) {
                alert.prepareError("Invalid File Type. Returning error message").log();
                responseJsonObject.addProperty("message",
                        "Invalid File Type. Allowed file types are .txt .doc .docx .pdf .png .jpeg .jpg");
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20628.getErrorCode());
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20628.getMessage());
                AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                        EventEnum.UPLOADFILE, ActivityStatusEnum.FAILED, "Invalid File Type.");
                return;
            }
            if((fileName.length()>MAX_FILENAME_LENGTH) || (StringUtils.countMatches(fileName, '.')>1)){
            	alert.prepareError("File name length limit exceeded or file name contains more than 1 extensions.Returning error message").log();
                responseJsonObject.addProperty("message",
                        "Invalid File name, File name should be less than 255 characters and it should contain only 1 extenstion");
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_22236.getErrorCode());
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_22236.getMessage());
                AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                        EventEnum.UPLOADFILE, ActivityStatusEnum.FAILED, "File name length limit exceeded or file name contains more than 1 extensions");
                return;
            }

            // Fetch logged-in user information
            UserDetailsBean userDetails = LoggedInUserHandler.getUserDetails(servicesManager);
            String loggedInUserUserId = userDetails.getId();
            if (StringUtils.equalsIgnoreCase(userDetails.getId(), APICustomIdentityService.API_USER_ID)) {
                // Request is from OLB/MB
                diagnostic.prepareDebug("Request is from API User").log();
                String customerUsername = headersHandler.getHeader("username");
                loggedInUserUserId = CustomerHandler.getCustomerId(customerUsername);
            }

            // Create File metadata
            String mediaId = CommonUtilities.getNewId().toString();
            requestJsonObject.addProperty("id", mediaId);// Primary key
            String downloadURL =
                    "/" + CustomerRequestAndMessagesHandler.MEDIA_DOWNLOAD_OBJECT_SERVICE_URL + mediaId + "&authToken=";
            requestJsonObject.addProperty("Type", attachmentTypeId);
            requestJsonObject.addProperty("Url", downloadURL);
            requestJsonObject.addProperty("Description", "MEDIA: " + fileName);
            requestJsonObject.addProperty("createdby", loggedInUserUserId);
            requestJsonObject.addProperty("createdts", CommonUtilities.getISOFormattedLocalTimestamp());
            if(requestJsonObject.has("Size")) {
            	String size = requestJsonObject.get("Size").getAsString();
            	if(!StringUtils.isBlank(size)) {
            		if(Integer.parseInt(size)>MAX_FILE_SIZE) {
            			alert.prepareError("File size limit exceeded").log();
                        responseJsonObject.addProperty("message",
                                "File size limit exceeded");
                        responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20116.getErrorCode());
                        responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20116.getMessage());
                        AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                                EventEnum.UPLOADFILE, ActivityStatusEnum.FAILED, "File size limit exceeded");
                        return;
            		}
            		requestJsonObject.addProperty("Size", size);
            	}
            }
            
            // Update request body
            fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestJsonObject);

            // Update response body
            responseJsonObject.addProperty("id", mediaId);

            // Execute request
            fabricRequestChain.execute();
            responseJsonObject.addProperty(FabricConstants.OPSTATUS, 0);
            responseJsonObject.addProperty(FabricConstants.HTTP_STATUS_CODE, 0);
            AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                    EventEnum.UPLOADFILE, ActivityStatusEnum.SUCCESSFUL, "Media file uploaded successfully");

        } catch (ApplicationException e) {
            alert.prepareError("ApplicationException in MediaPostOperationPreProcessor", e).log();
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, e.getErrorCodeEnum().getErrorCode());
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, e.getErrorCodeEnum().getMessage());
            AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                    EventEnum.UPLOADFILE, ActivityStatusEnum.FAILED, "ApplicationException in MediaPostOperationPreProcessor.");
        } catch (Exception e) {
            alert.prepareError("Exception in MediaPostOperationPreProcessor", e).log();
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20001.getMessage());
            AuditHandler.auditAdminActivity(fabricRequestManager, fabricResponseManager, null, null, ModuleNameEnum.MESSAGES,
                    EventEnum.UPLOADFILE, ActivityStatusEnum.FAILED, "Exception in MediaPostOperationPreProcessor.");
        } finally {
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responseJsonElement);
        }
    }

}
