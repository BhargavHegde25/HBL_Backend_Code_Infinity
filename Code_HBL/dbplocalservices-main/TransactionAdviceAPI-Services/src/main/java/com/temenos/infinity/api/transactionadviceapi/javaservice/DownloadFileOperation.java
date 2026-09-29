package com.temenos.infinity.api.transactionadviceapi.javaservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.FileEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.transactionadviceapi.constants.ErrorCodeEnum;
import com.temenos.infinity.api.transactionadviceapi.resource.api.TransactionAdviceResource;
import com.temenos.infinity.api.transactionadviceapi.resource.impl.TransactionAdviceResourceImpl;
import com.temenos.infinity.api.transactionadviceapi.utils.TransactionAdviceUtilities;

/**
 * 
 * @author suryaacharans
 * @version Java Service end point to login and download autoform file
 * 
 */
public class DownloadFileOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            TransactionAdviceResource downloadResource =
                    DBPAPIAbstractFactoryImpl.getResource(TransactionAdviceResource.class);
            String accountId = request.getParameter("accountId");
            String transactionRef = request.getParameter("transactionRef");
            String mediaType = request.getParameter("mediaType");
            String transactionType = request.getParameter("transactionType");
            String page = request.getParameter("page");
            // Since security level is already public so as to download the file,need to
            // overwrite. Hence getting input from Java Service.
            String auth_token = request.getParameter("Auth_Token");
            String operation = "download";
            String customerId = TransactionAdviceUtilities.getT24BackendIdUsingAuth(auth_token);
            
            if (StringUtils.isEmpty(customerId)) {
				diagnostic.prepareDebug("Not authorized to get backend id of customer,proceeding with customer ID").log();
				customerId=TransactionAdviceUtilities.getUserAttributeValue(auth_token, "customer_id"); 
			}
            
			if (StringUtils.isEmpty(customerId)) {
				Result errResult = new Result();
				alert.prepareError("Could not Extract Customer ID/Backend ID, Authorization Failed").log();
				ErrorCodeEnum.ERR_20006.setErrorCode(errResult);
				return errResult;
			}

            Result result = downloadResource.loginAndDownload(customerId, accountId, transactionRef, mediaType,
                    transactionType, page, operation, auth_token);
            if (StringUtils.isNotBlank(result.getParamValueByName("dbpErrMsg"))
                    && result.getParamValueByName("dbpErrMsg").length() > 0) {
                return result;
            }
            Map<String, String> responseHeaders = new HashMap<String, String>();
            if (mediaType.equalsIgnoreCase("png")) {
                responseHeaders.put(HttpHeaders.CONTENT_TYPE, "image/png");
                responseHeaders.put("Content-Disposition", "attachment; filename=\"" + transactionRef + ".png" + "\"");
            } else {
                responseHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
                responseHeaders.put("Content-Disposition", "attachment; filename=\"" + transactionRef + ".pdf" + "\"");
            }
            if (result.getParamValueByName("base64").length() > 0) {

                File file = constructFileObjectFromBase64String(result.getParamValueByName("base64"));
                response.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON,
                        new BufferedHttpEntity(new FileEntity(file)));
                response.getHeaders().putAll(responseHeaders);
                file.deleteOnExit();
                response.setStatusCode(HttpStatus.SC_OK);
                diagnostic.prepareInfo("Succesfully constructed the File object").log();
            } else {
                alert.prepareError("Attempted to retrieve non existing file. Request rejected.").log();
                ErrorCodeEnum.ERR_20003.setErrorCode(result);
                return result;
            }
            return result;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());

        }

    }

    public File constructFileObjectFromBase64String(String base64FileContent) throws Exception {
        byte[] decodedFileContent = Base64.decodeBase64(base64FileContent.getBytes("UTF-8"));
        File file = File.createTempFile("customerRequest", "messageattachment");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(decodedFileContent);
        } catch (IOException e) {
            alert.prepareError("Exception in Constructing  File Output Stream", e).log();
        }

        finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                alert.prepareError("Exception in Closing  File Output Stream", e).log();
            }

        }
        return file;
    }
}
