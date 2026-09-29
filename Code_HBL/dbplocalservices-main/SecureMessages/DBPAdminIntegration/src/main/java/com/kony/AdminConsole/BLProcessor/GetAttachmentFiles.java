package com.kony.AdminConsole.BLProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClients;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.AdminConsole.Utilities.CommonUtilities;
import com.kony.AdminConsole.Utilities.HTTPOperations;
import com.kony.AdminConsole.Utilities.ServiceConfig;
import com.kony.AdminConsole.Utilities.URLConstants;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetAttachmentFiles implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("deprecation")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();

        String media_id = ((HttpServletRequest) requestInstance.getOriginalRequest()).getParameter("media_id");
        String olbClaimsToken = ((HttpServletRequest) requestInstance.getOriginalRequest())
                .getParameter("claims_token");

        String host = requestInstance.getHeader("host");
        if (StringUtils.isBlank(host)) {
            host = requestInstance.getHeader("Host");
        }

        HTTPOperations httpOperationInstance = new HTTPOperations();
        HashMap<String, String> postParamMap = new HashMap<>();
        String userAttributesResponse = httpOperationInstance.hitPOSTServiceAndGetResponse(
                "https://" + host + "/services/UserAttributes/getUserAttributes", postParamMap,
                "application/x-www-form-urlencoded", olbClaimsToken, null);
        JSONObject userJSONObject = CommonUtilities.getStringAsJSONObject(userAttributesResponse);
        if ((userJSONObject == null) || (!userJSONObject.has("username"))) {
            processedResult.addParam(new Param("FailureReason", "Unautorized access"));
            return processedResult;
        }

        File getResponse = getMessageAttachment(media_id, requestInstance);

        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "text/plain; charset=utf-8");
        customHeaders.put("Content-Disposition",
                "attachment; filename=\"" + requestInstance.getParameter("responseFilename") + "\"");

        responseInstance.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
                new BufferedHttpEntity(new FileEntity(getResponse)));
        responseInstance.getHeaders().putAll(customHeaders);

        return processedResult;

    }

    public File getMessageAttachment(String media_id, DataControllerRequest dcRequest) {

        String getRequestsURL = ServiceConfig.getValueFromRunTime(URLConstants.HOST_URL, dcRequest)
                + ServiceConfig.getValue("MessageAttachmentURL");
        String authToken = ServiceConfig.getValue("Auth_Token");
        getRequestsURL = getRequestsURL + "?mediaId=" + media_id + "&authToken=" + authToken;

        HashMap<String, String> customHeaders = new HashMap<>();
        customHeaders.put("X-Kony-AC-API-Access-By", "OLB");
        customHeaders.put("X-Kony-Authorization", authToken);
        JSONObject postParametersMap = new JSONObject();
        File getResponseString = hitPOSTServiceAndGetResponseForFile(getRequestsURL, postParametersMap,
                ContentType.APPLICATION_JSON.getMimeType(), authToken, customHeaders, dcRequest);
        return getResponseString;
    }

    public File hitPOSTServiceAndGetResponseForFile(String URL, JSONObject jsonPostParameter, String contentType,
            String konyFabricAuthToken, HashMap<String, String> requestHeaders, DataControllerRequest dcRequest) {

        HttpClient httpClientInstance = HttpClients.createDefault();
        HttpGet httpPostRequestInstance = new HttpGet(URL);

        if (jsonPostParameter != null) {
            jsonPostParameter.toString();
        }

        try {
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    httpPostRequestInstance.setHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse httpResponseInstance = httpClientInstance.execute(httpPostRequestInstance);
            HttpEntity httpResponseEntity = httpResponseInstance.getEntity();
            org.apache.http.HeaderElement[] headerElements = httpResponseInstance.getFirstHeader("Content-Disposition")
                    .getElements();
            for (org.apache.http.HeaderElement headerElement : headerElements) {
                if (headerElement.getParameterByName("filename") != null) {
                    dcRequest.addRequestParam_("responseFilename",
                            headerElement.getParameterByName("filename").getValue());
                }
            }
            if (httpResponseEntity != null) {
                InputStream responseStream = httpResponseEntity.getContent();
                File responseOutput = getInputStreamAsFile(responseStream);
                responseStream.close();
                return responseOutput;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    public File getInputStreamAsFile(InputStream sourceInputStream) {
        File file = null;
        try {
            file = File.createTempFile("aasdfghjk", "aasdfghjk");
        } catch (IOException e1) {
            alert.prepareError(e1.getMessage()).log();
        }
        try (FileOutputStream result = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = sourceInputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            result.close();
            return file;
        } catch (IOException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return file;
    }
}
