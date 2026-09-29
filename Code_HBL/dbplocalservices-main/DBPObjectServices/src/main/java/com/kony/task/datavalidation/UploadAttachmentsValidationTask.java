package com.kony.task.datavalidation;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.utilities.HelperMethods;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class UploadAttachmentsValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final int FILENAME_INDEX = 0;
    private static final int FILETYPE_INDEX = 1;
    private static final int FILECONTENTS_INDEX = 2;
    private static final int MAX_FILESIZE_ALLOWED = 2;

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
    throws Exception {
        if (!isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		diagnostic.prepareDebug("data access control is enabled").log();
    	if (!HelperMethods.isJsonEleNull(fabricRequestManager.getPayloadHandler().getPayloadAsJson())) {
        	JsonObject resPayload = null;
    		JsonObject requestPayload = fabricRequestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
            String uploadedAttachments = HelperMethods.getStringFromJsonObject(requestPayload, "uploadedattachments");
            diagnostic.prepareDebug("validating uploadedAttachments {}", uploadedAttachments).log();
            if (StringUtils.isNotBlank(uploadedAttachments)) {
                String uploadedAttachmentsArray[] = uploadedAttachments.split(",");
                for (int i = 0; i < uploadedAttachmentsArray.length; i++) {
                    String attachmentDetails[] = uploadedAttachmentsArray[i].split("-");
                    if (attachmentDetails.length == 3) {
                        String uploadedFileName = attachmentDetails[FILENAME_INDEX];
                        String fileExtension = attachmentDetails[FILETYPE_INDEX];
                        String fileContents = attachmentDetails[FILECONTENTS_INDEX];
                        String[] extensionArray = uploadedFileName.split("[.]", 0);
                        String mimeType = null;
                        if (extensionArray.length == 2) {
                            mimeType = extensionArray[1];
                            int fileSize = (int)((fileContents.length() * 0.75) / 1024);
							if ((!fileExtension.equalsIgnoreCase("pdf") || !fileExtension.equalsIgnoreCase("application/pdf"))
									&& (!fileExtension.equalsIgnoreCase("jpeg") || !fileExtension.equalsIgnoreCase("image/jpeg"))
									&& StringUtils.isNotBlank(mimeType) && !mimeType.equalsIgnoreCase("pdf")
									&& !mimeType.equalsIgnoreCase("jpeg")) {
                            	alert.prepareError("The selected file " + uploadedFileName + " cannot be uploaded. Only files with the following extensions are allowed: .pdf, .jpeg").log();
                            	if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
                    				resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
                    			}
                    			resPayload = ErrorCodeEnum.ERR_12464.setErrorCode(resPayload);
                    			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                                return false;
                            }	
							 String regExp = "^[a-zA-Z0-9]*.[a-zA-Z0-9]*$";
                            if (!uploadedFileName.substring(0, uploadedFileName.length() - uploadedFileName.lastIndexOf(".") - 1).matches(regExp)) {
                                alert.prepareError("Incorrect file name " + uploadedFileName + ". Please upload file with a valid name").log();
                                if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
                    				resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
                    			}
                    			resPayload = ErrorCodeEnum.ERR_12463.setErrorCode(resPayload);
                    			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                    			return false;
                            }
                            if(extensionArray[0].length() >=150 ){
								alert.prepareError("Incorrect file name " + uploadedFileName + ". Please upload file name length should be less than 150 characters").log();
                                if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
                    				resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
                    			}
                    			resPayload = ErrorCodeEnum.ERR_12465.setErrorCode(resPayload);
                    			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                    			return false;
                            }
                            if (fileSize > (MAX_FILESIZE_ALLOWED * 1000)) {
                                alert.prepareError("The selected file " + uploadedFileName + " exceeds the maximum file size of 2mb.\r\n").log();
                                if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
                    				resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
                    			}
                    			resPayload = ErrorCodeEnum.ERR_12462.setErrorCode(resPayload);
                    			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
                    			return false;
                            }
                        } 
                    }
                }
            }
        }
    	return true;
    }

    public boolean isDACEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDacEnabled = configurableParametersHelper.getServerProperty("DAC_ENABLED");
			return StringUtils.isBlank(isDacEnabled)
					|| BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DAC_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}
}