package com.kony.AdminConsole.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

public class HTTPOperations {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    public String hitPOSTServiceAndGetResponse(String URL, HashMap<String, String> postParameters, String contentType,
            String konyFabricAuthToken, HashMap<String, String> customHeaderParameters) {

        HttpClient httpClientInstance = HttpClients.createDefault();
        HttpPost httpPostRequestInstance = new HttpPost(URL);
        List<NameValuePair> postParametersNameValuePairList = new ArrayList<>();

        if (postParameters != null) {
            for (String currKey : postParameters.keySet()) {
                String currValue = postParameters.get(currKey);
                postParametersNameValuePairList.add(new BasicNameValuePair(currKey, currValue));
            }
        }

        try {
            httpPostRequestInstance.setHeader("X-Kony-AC-API-Access-By", "OLB");
            httpPostRequestInstance.setEntity(
                    new UrlEncodedFormEntity(postParametersNameValuePairList, StandardCharsets.UTF_8.name()));
            if (!CommonUtilities.isEmptyString(contentType)) {
                httpPostRequestInstance.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }
            if (!CommonUtilities.isEmptyString(konyFabricAuthToken)) {
                httpPostRequestInstance.setHeader("X-Kony-Authorization", konyFabricAuthToken);
            }
            if (customHeaderParameters != null) {
                for (String currKey : customHeaderParameters.keySet()) {
                    String currValue = customHeaderParameters.get(currKey);
                    httpPostRequestInstance.setHeader(currKey, currValue);
                }
            }

            // Execute and get the response.
            HttpResponse httpResponseInstance = httpClientInstance.execute(httpPostRequestInstance);

            HttpEntity httpResponseEntity = httpResponseInstance.getEntity();

            if (httpResponseEntity != null) {
                InputStream responseStream = httpResponseEntity.getContent();
                String responseOutput = getInputStreamAsString(responseStream);
                responseStream.close();
                return responseOutput;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    public String hitPOSTServiceAndGetResponse(String URL, JSONObject jsonPostParameter, String contentType,
            String konyFabricAuthToken, HashMap<String, String> customHeaderParameters) {

        HttpClient httpClientInstance = HttpClients.createDefault();
        HttpPost httpPostRequestInstance = new HttpPost(URL);

        String jsonString = "";
        if (jsonPostParameter != null) {
            jsonString = jsonPostParameter.toString();
        }
        StringEntity requestEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);

        try {

            httpPostRequestInstance.setEntity(requestEntity);
            httpPostRequestInstance.setHeader("X-Kony-AC-API-Access-By", "OLB");
            if (!CommonUtilities.isEmptyString(contentType)) {
                httpPostRequestInstance.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }
            if (!CommonUtilities.isEmptyString(konyFabricAuthToken)) {
                httpPostRequestInstance.setHeader("X-Kony-Authorization", konyFabricAuthToken);
            }
            if (customHeaderParameters != null) {
                for (String currKey : customHeaderParameters.keySet()) {
                    String currValue = customHeaderParameters.get(currKey);
                    httpPostRequestInstance.setHeader(currKey, currValue);
                }
            }

            // Execute and get the response.
            HttpResponse httpResponseInstance = httpClientInstance.execute(httpPostRequestInstance);

            HttpEntity httpResponseEntity = httpResponseInstance.getEntity();

            if (httpResponseEntity != null) {
                InputStream responseStream = httpResponseEntity.getContent();
                String responseOutput = getInputStreamAsString(responseStream);
                responseStream.close();
                return responseOutput;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    public String hitPOSTStreamServiceAndGetResponse(String URL, InputStream inputStream, String contentType,
            String konyFabricAuthToken, HashMap<String, String> customHeaderParameters, String username) {

        HttpClient httpClientInstance = HttpClients.createDefault();
        HttpPost httpPostRequestInstance = new HttpPost(URL);
        try {

            InputStreamEntity entity = new InputStreamEntity(inputStream);
            httpPostRequestInstance.setEntity(entity);
            httpPostRequestInstance.setHeader(HttpHeaders.CONTENT_TYPE, contentType);

            if (!CommonUtilities.isEmptyString(konyFabricAuthToken)) {
                httpPostRequestInstance.setHeader("X-Kony-Authorization", konyFabricAuthToken);
            }
            if (customHeaderParameters != null) {
                for (String currKey : customHeaderParameters.keySet()) {
                    String currValue = customHeaderParameters.get(currKey);
                    httpPostRequestInstance.setHeader(currKey, currValue);
                }
            }
            httpPostRequestInstance.setHeader("X-Kony-AC-API-Access-By", "OLB");
            httpPostRequestInstance.setHeader("username", username);
            // Execute and get the response.
            HttpResponse httpResponseInstance = httpClientInstance.execute(httpPostRequestInstance);

            HttpEntity httpResponseEntity = httpResponseInstance.getEntity();

            if (httpResponseEntity != null) {
                InputStream responseStream = httpResponseEntity.getContent();
                String responseOutput = getInputStreamAsString(responseStream);
                responseStream.close();
                return responseOutput;
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    public File hitPOSTServiceAndGetResponseForFile(String URL, JSONObject jsonPostParameter, String contentType,
            String konyFabricAuthToken, HashMap<String, String> customHeaderParameters) {

        HttpClient httpClientInstance = HttpClients.createDefault();
        HttpPost httpPostRequestInstance = new HttpPost(URL);

        String jsonString = "";
        if (jsonPostParameter != null) {
            jsonString = jsonPostParameter.toString();
        }
        StringEntity requestEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);

        try {

            httpPostRequestInstance.setEntity(requestEntity);

            if (!CommonUtilities.isEmptyString(contentType)) {
                httpPostRequestInstance.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }
            if (!CommonUtilities.isEmptyString(konyFabricAuthToken)) {
                httpPostRequestInstance.setHeader("X-Kony-Authorization", konyFabricAuthToken);
            }
            if (customHeaderParameters != null) {
                for (String currKey : customHeaderParameters.keySet()) {
                    String currValue = customHeaderParameters.get(currKey);
                    httpPostRequestInstance.setHeader(currKey, currValue);
                }
            }
            httpPostRequestInstance.setHeader("X-Kony-AC-API-Access-By", "OLB");
            // Execute and get the response.
            HttpResponse httpResponseInstance = httpClientInstance.execute(httpPostRequestInstance);

            HttpEntity httpResponseEntity = httpResponseInstance.getEntity();

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

    public String hitGETServiceAndGetResponse(String URL, String konyFabricAuthToken,
            HashMap<String, String> customHeaderParameters) {

        HttpClient httpClientInstance = HttpClients.createDefault();
        HttpGet httpGetRequestInstance = new HttpGet(URL);

        try {

            if (!CommonUtilities.isEmptyString(konyFabricAuthToken)) {
                httpGetRequestInstance.setHeader("X-Kony-Authorization", konyFabricAuthToken);
                httpGetRequestInstance.setHeader("X-Kony-AC-API-Access-By", "OLB");
            }
            // Execute and get the response.
            HttpResponse httpResponseInstance = httpClientInstance.execute(httpGetRequestInstance);

            HttpEntity httpResponseEntity = httpResponseInstance.getEntity();

            if (httpResponseEntity != null) {
                InputStream responseStream = httpResponseEntity.getContent();
                String responseOutput = getInputStreamAsString(responseStream);
                responseStream.close();
                return responseOutput;
            }
            if (customHeaderParameters != null) {
                for (String currKey : customHeaderParameters.keySet()) {
                    String currValue = customHeaderParameters.get(currKey);
                    httpGetRequestInstance.setHeader(currKey, currValue);
                }
            }
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    public String getInputStreamAsString(InputStream sourceInputStream) {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = sourceInputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString(StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return "";
    }

    public File getInputStreamAsFile(InputStream sourceInputStream) {
        File file = null;
        try {
            file = File.createTempFile("a", "a");
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