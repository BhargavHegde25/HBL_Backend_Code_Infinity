package com.kony.adminconsole.service.customerrequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * <p>
 * Service to download the Message Attachment
 * </p>
 * 
 * @author Aditya Mankal
 * 
 */
public class CustomerRequestMessageAttachmentDownloadService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    private static final String CONTENT_DISPOSITION_HEADER = "Content-Disposition";

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();

        try {
            Map<String, String> queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
            String mediaId = queryParamsMap.get("mediaId");

            // Read Media
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(ODataQueryConstants.SELECT, "Name,Content");
            inputMap.put(ODataQueryConstants.FILTER, "id eq '" + mediaId + "'");
            String readMediaResponse =
                    Executor.invokeService(ServiceURLEnum.MEDIA_READ, inputMap, null, requestInstance);
            JSONObject readMediaResponseJSON = CommonUtilities.getStringAsJSONObject(readMediaResponse);
            if (readMediaResponseJSON == null || !readMediaResponseJSON.has(FabricConstants.OPSTATUS)
                    || readMediaResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
                    || !readMediaResponseJSON.has("media")) {
                alert.prepareError("Failed to read Media Content").log();
                ErrorCodeEnum.ERR_20130.setErrorCode(result);
                return result;
            }
            diagnostic.prepareDebug("Succesful Read Media Table Operation").log();

            // Prepare response
            Map<String, String> responseHeaders = new HashMap<String, String>();
            responseHeaders.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_OCTET_STREAM.getMimeType());

            if (readMediaResponseJSON.optJSONArray("media") != null
                    && readMediaResponseJSON.optJSONArray("media").length() > 0) {
                // Construct file object
                JSONObject currFileJSONObject = readMediaResponseJSON.optJSONArray("media").optJSONObject(0);
                responseHeaders.put(CONTENT_DISPOSITION_HEADER,
                        "attachment; filename=\"" + currFileJSONObject.getString("Name") + "\"");
                File file = CommonUtilities.constructFileFromBase64String(currFileJSONObject.getString("Content"));
                responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON,
                        new BufferedHttpEntity(new FileEntity(file)));
                responseInstance.getHeaders().putAll(responseHeaders);
                file.deleteOnExit();
                responseInstance.setStatusCode(HttpStatus.SC_OK);
                diagnostic.prepareInfo("Succesfully constructed File object").log();
            } else {
                alert.prepareError("Attempted to retrieve non existing file. Request rejected.").log();
                ErrorCodeEnum.ERR_20562.setErrorCode(result);
                return result;
            }
        } catch (ApplicationException e) {
            alert.prepareError("ApplicationException in MediaPostOperationPreProcessor", e).log();
            e.getErrorCodeEnum().setErrorCode(result);
        } catch (Exception e) {
            alert.prepareError("Failed while downloading customer request message attachment", e).log();
            ErrorCodeEnum.ERR_20687.setErrorCode(result);
            String errorMessage =
                    "Failed to download customer request message attachment. Please contact administrator.";
            CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
        }

        return result;
    }

}