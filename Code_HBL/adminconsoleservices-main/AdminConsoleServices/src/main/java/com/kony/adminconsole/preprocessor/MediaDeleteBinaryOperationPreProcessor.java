package com.kony.adminconsole.preprocessor;

import java.util.Collections;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.handler.CustomerRequestAndMessagesHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;

/**
 * <p>
 * Preprocessor for Media delete binary operation
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class MediaDeleteBinaryOperationPreProcessor implements ObjectServicePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

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
                return;
            }
            JsonObject requestJsonObject = requestJsonElement.getAsJsonObject();

            // Fetch logged-in user information
            UserDetailsBean userDetails = LoggedInUserHandler.getUserDetails(servicesManager);
            String loggedInUserUserId = userDetails.getId();
            if (StringUtils.equalsIgnoreCase(userDetails.getId(), APICustomIdentityService.API_USER_ID)) {
                // Request is from OLB/MB
                diagnostic.prepareDebug("Request is from API User").log();
                String customerUsername = headersHandler.getHeader("username");
                loggedInUserUserId = CustomerHandler.getCustomerId(customerUsername);
            }

            // Authorization check
            String mediaId = requestJsonObject.get("id").getAsString();
            boolean hasAccessToMedia = CustomerRequestAndMessagesHandler
                    .hasAccessToMedia(Collections.singletonList(mediaId), loggedInUserUserId);
            if (hasAccessToMedia == false) {
                diagnostic.prepareDebug("Unathorized access to get media").log();
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_21027.getErrorCode());
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_21027.getMessage());
                return;
            }

            // Update request body
            fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestJsonObject);

            // Update response body
            responseJsonObject.addProperty("id", mediaId);

            // Execute request
            fabricRequestChain.execute();
            responseJsonObject.addProperty(FabricConstants.OPSTATUS, 0);
            responseJsonObject.addProperty(FabricConstants.HTTP_STATUS_CODE, 0);

        } catch (ApplicationException e) {
            alert.prepareError("ApplicationException in MediaDeleteBinaryOperationPreProcessor", e).log();
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, e.getErrorCodeEnum().getErrorCode());
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, e.getErrorCodeEnum().getMessage());
        } catch (Exception e) {
            alert.prepareError("Exception in MediaDeleteBinaryOperationPreProcessor", e).log();
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20001.getMessage());
        } finally {
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responseJsonElement);
        }
    }

}
