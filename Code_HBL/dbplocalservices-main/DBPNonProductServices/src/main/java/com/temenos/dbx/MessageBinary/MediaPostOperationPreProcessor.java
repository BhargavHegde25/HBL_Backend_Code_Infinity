package com.temenos.dbx.MessageBinary;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.HeadersHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.api.commons.constants.FabricConstants;

/**
 * <p>
 * Preprocessor for Media post operation
 * </p>
 * 
 * @author Shivam Marwaha
 *
 */
public class MediaPostOperationPreProcessor implements ObjectServicePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    public static final String MEDIA_DOWNLOAD_OBJECT_SERVICE_URL =
            "services/data/v1/CustomerManagementObjService/operations/CustomerRequest/downloadMessageAttachment?mediaId=";
    
    
    /**
     * @param file
     * @return
     */
    public  String getFileExtension(String filename) {
        if (StringUtils.isBlank(filename) || !filename.contains(".") || filename.endsWith(".")) {
            return StringUtils.EMPTY;
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    /**
     * @param timeStamp
     * @param timeStampFormat
     * @return
     */
    public static long getTimeElapsedFromTimestampToNowInMinutes(String timeStamp, String timeStampFormat) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(timeStampFormat);
        Date userCreationDateTimeInstance;
        try {
            userCreationDateTimeInstance = dateTimeFormat.parse(timeStamp);
            Date currentDateTimeInstance = new Date();
            long diffInMilliSeconds = currentDateTimeInstance.getTime() - userCreationDateTimeInstance.getTime();
            return TimeUnit.MILLISECONDS.toMinutes(diffInMilliSeconds);
        } catch (ParseException e) {
            alert.prepareError("Unexpected error has occurred", e).log();
        }
        return 0l;
    }
    
    /**
     * @return
     */
    public  String getISOFormattedLocalTimestamp() {
        String localDateTime;
        if (LocalDateTime.now().getSecond() == 0) {
            localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
        } else {
            localDateTime = LocalDateTime.now().withNano(0).toString();
        }
        return localDateTime;
    }

    
    public  String validateFileTypeAndGetAttachmentTypeId(String fileExtension) {
        if (StringUtils.isBlank(fileExtension)) {
            return "INVALID_FILE";
        } else if (fileExtension.equalsIgnoreCase("txt")) {
            return "ATTACH_TYPE_TXT";
        } else if (fileExtension.equalsIgnoreCase("doc")) {
            return "ATTACH_TYPE_DOC";
        } else if (fileExtension.equalsIgnoreCase("docx")) {
            return "ATTACH_TYPE_DOCX";
        } else if (fileExtension.equalsIgnoreCase("pdf")) {
            return "ATTACH_TYPE_PDF";
        } else if (fileExtension.equalsIgnoreCase("png")) {
            return "ATTACH_TYPE_PNG";
        } else if (fileExtension.equalsIgnoreCase("jpeg")) {
            return "ATTACH_TYPE_JPEG";
        } else if (fileExtension.equalsIgnoreCase("jpg")) {
            return "ATTACH_TYPE_JPG";
        }
        return "INVALID_FILE";
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
            HeadersHandler headersHandler = fabricRequestManager.getHeadersHandler();
            ServicesManager servicesManager = fabricRequestManager.getServicesManager();

            // Get Request JSON Element
            JsonElement requestJsonElement = fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            JsonObject requestJsonObject = requestJsonElement.getAsJsonObject();

            // Validate File type
            String fileName = requestJsonObject.get("Name").getAsString();
//            String attachmentTypeId =
//                    validateFileTypeAndGetAttachmentTypeId(this.getFileExtension(fileName));
            String attachmentTypeId =
            		validatePatternandGetAttachmentTypeId(fileName);
            if (StringUtils.equalsIgnoreCase("INVALID_FILE", attachmentTypeId)) {
                alert.prepareError("Invalid File Type. Returning error message").log();
                responseJsonObject.addProperty("message",
                        "Invalid File Type. Allowed file types are .txt .doc .docx .pdf .png .jpeg .jpg");
                 return;
            }

            // Fetch logged-in user information
            String loggedInUserUserId = HelperMethods.getAPIUserIdFromSession(fabricRequestManager);
            // Create File metadata
            String mediaId = UUID.randomUUID().toString();
            requestJsonObject.addProperty("id", mediaId);// Primary key
            String downloadURL =
                    "/" + MEDIA_DOWNLOAD_OBJECT_SERVICE_URL + mediaId + "&authToken=";
            requestJsonObject.addProperty("Type", attachmentTypeId);
            requestJsonObject.addProperty("Url", downloadURL);
            requestJsonObject.addProperty("Description", "MEDIA: " + fileName);
            requestJsonObject.addProperty("createdby", loggedInUserUserId);
            requestJsonObject.addProperty("createdts", this.getISOFormattedLocalTimestamp());
            if(requestJsonObject.has("Size")) {
            	String size = requestJsonObject.get("Size").getAsString();
            	if(!StringUtils.isBlank(size))
            		requestJsonObject.addProperty("Size", size);
            }
            
            // Update request body
            fabricRequestManager.getPayloadHandler().updatePayloadAsJson(requestJsonObject);

            // Update response body
            responseJsonObject.addProperty("id", mediaId);

            // Execute request
            fabricRequestChain.execute();
            responseJsonObject.addProperty(FabricConstants.OPSTATUS, 0);
            responseJsonObject.addProperty(FabricConstants.HTTP_STATUS_CODE, 0);
        } catch (Exception e) {
            alert.prepareError("Exception in MediaPostOperationPreProcessor", e).log();
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
            responseJsonObject.addProperty(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_20001.getMessage());
        } finally {
            fabricResponseManager.getPayloadHandler().updatePayloadAsJson(responseJsonElement);
        }
    }
    public static String validatePatternandGetAttachmentTypeId(String fileName) {
    	 if (Pattern.matches("^[A-Za-z0-9_\\s]*+(.txt)$",
    			 fileName)) {
    		 return "ATTACH_TYPE_TXT";
    	 }else if(Pattern.matches("^[A-Za-z0-9_\\s]*+(.doc)$",
    			 fileName)) {
    	 return "ATTACH_TYPE_DOC";
    	 } else if(Pattern.matches("^[A-Za-z0-9_\\s]*+(.docx)$",
    			 fileName)) {
    	 return "ATTACH_TYPE_DOCX";
    	 }else if(Pattern.matches("^[A-Za-z0-9_\\s]*+(.pdf)$",
    			 fileName)) {
    	 return "ATTACH_TYPE_PDF";
    	 }else if(Pattern.matches("^[A-Za-z0-9_\\s]*+(.png)$",
    			 fileName)) {
    	 return "ATTACH_TYPE_PNG";
    	 }else if(Pattern.matches("^[A-Za-z0-9_\\s]*+(.jpeg)$",
    			 fileName)) {
    	 return "ATTACH_TYPE_JPEG";
    	 }else if(Pattern.matches("^[A-Za-z0-9_\\s]*+(.jpg)$",
    			 fileName)) {
    	 return "ATTACH_TYPE_JPG";
    	 }
         return "INVALID_FILE";
  }

}
