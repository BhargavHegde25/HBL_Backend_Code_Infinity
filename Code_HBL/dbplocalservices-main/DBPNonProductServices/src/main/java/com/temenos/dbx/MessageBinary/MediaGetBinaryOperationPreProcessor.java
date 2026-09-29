package com.temenos.dbx.MessageBinary;
import java.util.Collections;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.QueryParamsHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.api.commons.constants.FabricConstants;

/**
 * <p>
 * Preprocessor for Media get binary operation
 * </p>
 * 
 * @author Shivam Marwaha
 *
 */
public class MediaGetBinaryOperationPreProcessor implements ObjectServicePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    /**
     * @param sourceString
     * @return
     */
    public String decodeFromBase64(String sourceString) {
        if (sourceString == null) {
            return null;
        }
        return new String(Base64.decodeBase64(sourceString));
    }

    @Override
    public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
            FabricRequestChain fabricRequestChain) throws Exception {

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
            QueryParamsHandler queryParamsHandler = fabricRequestManager.getQueryParamsHandler();
            
            // Read query parameter
            String mediaId = queryParamsHandler.getParameter("id");
            String fieldName = queryParamsHandler.getParameter("fieldName");
            
            // Read Request JSON Element
            JsonElement requestJsonElement = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            String requestJsonMediaId = new String(), requestJsonFieldName = new String();
            if (requestJsonElement != null && requestJsonElement.getAsJsonObject() != null) {
	            JsonObject requestJsonObject = requestJsonElement.getAsJsonObject();
	            if(requestJsonObject.has("id"))
	            	requestJsonMediaId = requestJsonObject.get("id").getAsString();
	            if(requestJsonObject.has("fieldName"))
	            	requestJsonFieldName = requestJsonObject.get("fieldName").getAsString();
            }
            
            if(!StringUtils.isBlank(requestJsonMediaId))
            	mediaId = requestJsonMediaId;
            
            // MediaId null check
            if (StringUtils.isBlank(mediaId)) {
                diagnostic.prepareDebug("Media Id not provided").log();
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
                responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20001.getMessage());
                return;
            }

        

            
            // column name
            if(StringUtils.isBlank(requestJsonFieldName)) {
            	if(StringUtils.isBlank(fieldName))
            		fieldName = "Content";
            }
            else
            	fieldName = requestJsonFieldName;
            queryParamsHandler.addParameter("fieldName", fieldName);

            // // Get Request JSON Element
            // JsonElement requestJsonElement = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            // if (requestJsonElement == null || requestJsonElement.getAsJsonObject() == null) {
            // diagnostic.prepareDebug("Input Body is empty. Initializing.").log();
            // fabricRequestManager.getPayloadHandler().updatePayloadAsJson(new JsonObject());
            // requestJsonElement = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            // }
            // JsonObject requestJsonObject = requestJsonElement.getAsJsonObject();
            //
            // // Update request body
            // fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestJsonObject);

            // Execute request
            fabricRequestChain.execute();
            JsonElement reponseJsonElement = fabricResponseManager.getPayloadHandler().getPayloadAsJson();
            if (reponseJsonElement != null && reponseJsonElement.getAsJsonObject() != null) {
            	JsonObject dataJsonObject = reponseJsonElement.getAsJsonObject();
            	if(dataJsonObject != null && dataJsonObject.has("data"))
            		responseJsonObject.addProperty("data", dataJsonObject.get("data").getAsString());
            }
            responseJsonObject.addProperty(FabricConstants.OPSTATUS, 0);
            responseJsonObject.addProperty(FabricConstants.HTTP_STATUS_CODE, 0);

        }  catch (Exception e) {
            alert.prepareError("Exception in MediaGetBinaryOperationPreProcessor", e).log();
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20001.getMessage());
        } finally {
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responseJsonElement);
        }
    }

}