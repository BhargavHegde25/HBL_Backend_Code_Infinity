package com.temenos.infinity.api.transactionadviceapi.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.transactionadviceapi.backenddelegate.api.TransactionAdviceAPIBackendDelegate;
import com.temenos.infinity.api.transactionadviceapi.config.ServerConfigurations;
import com.temenos.infinity.api.transactionadviceapi.config.TransactionAdviceAPIServices;
import com.temenos.infinity.api.transactionadviceapi.constants.ErrorCodeEnum;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormCookie;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormDownload;

public class TransactionAdviceAPIBackendDelegateImpl implements TransactionAdviceAPIBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public AutoFormCookie login(String auth_token) {

        AutoFormCookie outputDto = new AutoFormCookie();
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("username", ServerConfigurations.AUTOFORM_USERNAME.getValue());
			inputMap.put("password", ServerConfigurations.AUTOFORM_PASSWORD.getValue());
            headerMap.put("X-XSRF-TOKEN", "");
            if (!StringUtils.isBlank(auth_token)) {
                diagnostic.prepareDebug("Using Auth Token from Param-Login").log();
                headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, auth_token);
            }
            String serviceResponse =
                    Executor.invokeService(TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_LOGIN, inputMap, headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null)
                diagnostic.prepareDebug("failed").log();
            else {
                outputDto.setXsrftoken(serviceResponseJSON.getString("DM-XSRF-TOKEN"));
                outputDto.setJSESSIONID(serviceResponseJSON.getString("JSESSIONID"));
            }
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return outputDto;
    }

    @Override
    public byte[] download(String documentId, String revision, String xsrf, String jsessionid, String auth_token) {
        byte[] serviceResponse = null;
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("documentId", documentId);
            inputMap.put("revision", revision);
            headerMap.put("X-XSRF-TOKEN", xsrf);
            String cookie = "DM-XSRF-TOKEN=" + xsrf + "; " + "JSESSIONID=" + jsessionid;
            headerMap.put("COOKIE", cookie);
            if (!StringUtils.isBlank(auth_token)) {
                diagnostic.prepareDebug("Using Auth Token from Param-Download").log();
                headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, auth_token);
            }
            diagnostic.prepareDebug("Header=" + headerMap.toString()).log();
            serviceResponse = Executor.invokePassThroughServiceAndGetBytes(
                    TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_DOWNLOAD, inputMap, headerMap);
            diagnostic.prepareDebug("ServiceResponse=" + serviceResponse).log();
            if (serviceResponse == null)
                alert.prepareError("failed").log();

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return serviceResponse;

    }

    @Override
    public AutoFormDownload search(String cuk, String xsrf, String jsessionid, String auth_token)
            throws ApplicationException {
        // TODO Auto-generated method stub
        AutoFormDownload outputDto = new AutoFormDownload();
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            String serviceResponse = new String();
            inputMap.put("cuk", cuk);
            headerMap.put("X-XSRF-TOKEN", xsrf);
            String cookie = "DM-XSRF-TOKEN=" + xsrf + "; " + "JSESSIONID=" + jsessionid;
            headerMap.put("COOKIE", cookie);
            if (!StringUtils.isBlank(auth_token)) {
                diagnostic.prepareDebug("Using Auth Token from Param-Search").log();
                headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER, auth_token);
            }
            diagnostic.prepareDebug("SearchHeader=" + headerMap.toString()).log();
            serviceResponse = Executor.invokePassThroughServiceAndGetString(
                    TransactionAdviceAPIServices.TRANSACTIONADVICEJSON_SEARCH, inputMap, headerMap);
            diagnostic.prepareDebug("After call").log();
            diagnostic.prepareDebug("SearchServiceResponse=" + serviceResponse).log();
            if (serviceResponse == null || serviceResponse.length() == 0) {
                alert.prepareError("failed").log();
                diagnostic.prepareDebug("EmptyResponse").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            } else {
                diagnostic.prepareDebug("SearchServiceResponse=" + serviceResponse.substring(0, 10)).log();
                JSONArray responseArray = new JSONArray(serviceResponse);
                int size = responseArray.length();
                while (size > 0) {
                    JSONObject obj = responseArray.getJSONObject(size - 1);
                    if (obj.has("fileProperties")) {
                        JSONObject properties = obj.getJSONObject("properties");
                        outputDto.setDocumentId(properties.get("id").toString());
                        outputDto.setRevision(properties.get("revision").toString());
                        return outputDto;
                    } else
                        size--;
                }
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
    }
}
