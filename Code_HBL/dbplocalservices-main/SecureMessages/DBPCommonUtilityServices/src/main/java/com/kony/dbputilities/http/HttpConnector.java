package com.kony.dbputilities.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;

public class HttpConnector {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public JsonObject invokeHttpPost(String url, Map<?, ?> inputParams, Map<String, String> headerParams)
            throws HttpCallException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        JsonObject result = new JsonObject();
        try {
            HttpPost postRequest = new HttpPost(url);
            updatePostBody(postRequest, inputParams, headerParams);
            updateHeader(postRequest, headerParams);
            response = client.execute(postRequest);
            result = processResponse(response);
        } catch (IOException e) {
            alert.prepareError(e.getMessage()).log();
            handleException("Error while processing response", e);
        } catch (Exception e) {
            handleException("Error while calling url: " + url, e);
        } finally {
            try {
                if (null != response) {
                    ((CloseableHttpResponse) response).close();
                }
                if (null != client) {
                    ((CloseableHttpClient) client).close();
                }
            } catch (Exception e) {
                handleException("Error while closing url resources: " + url, e);
            }
        }
        return result;
    }

    public JsonObject invokeHttpPost(JsonObject inputParams, Map<String, String> headerParams, String url)
            throws HttpCallException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        JsonObject result = new JsonObject();
        try {
            HttpPost postRequest = new HttpPost(url);
            Gson gson = new Gson();
            postRequest.setEntity(new StringEntity(gson.toJson(inputParams), ContentType.APPLICATION_JSON));
            updateHeader(postRequest, headerParams);
            response = client.execute(postRequest);
            result = processResponse(response);
        } catch (IOException e) {
            handleException("Error while processing response", e);
        } catch (Exception e) {
            handleException("Error while calling url: " + url, e);
        } finally {
            try {
                if (null != response) {
                    ((CloseableHttpResponse) response).close();
                }
                if (null != client) {
                    ((CloseableHttpClient) client).close();
                }
            } catch (Exception e) {
                handleException("Error while closing url resources: " + url, e);
            }
        }
        return result;
    }

    public JsonObject invokeHttpGet(String url, Map<String, String> headerParams) throws HttpCallException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        JsonObject result = new JsonObject();
        try {
            HttpGet getRequest = new HttpGet(url);
            updateHeader(getRequest, headerParams);
            response = client.execute(getRequest);
            result = processResponse(response);
        } catch (IOException e) {
            handleException("Error while processing response", e);
        } catch (Exception e) {
            handleException("Error while calling url: " + url, e);
        } finally {
            try {
                if (null != response) {
                    ((CloseableHttpResponse) response).close();
                }
                if (null != client) {
                    ((CloseableHttpClient) client).close();
                }
            } catch (IOException e) {
                handleException("Error while closing url resources: " + url, e);
            }
        }
        return result;
    }

    public JsonObject invokeHttpGet(String url, Map<String, String> inputParams, Map<String, String> headerParams)
            throws HttpCallException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        JsonObject result = new JsonObject();
        try {
            HttpGet getRequest = new HttpGet(url);
            updateQueryParameter(getRequest, inputParams);
            updateHeader(getRequest, headerParams);
            response = client.execute(getRequest);
            result = processResponse(response);
        } catch (IOException e) {
            handleException("Error while processing response", e);
        } catch (Exception e) {
            handleException("Error while calling url: " + url, e);
        } finally {
            try {
                if (null != response) {
                    ((CloseableHttpResponse) response).close();
                }
                if (null != client) {
                    ((CloseableHttpClient) client).close();
                }
            } catch (IOException e) {
                handleException("Error while closing url resources: " + url, e);
            }
        }
        return result;
    }

    public JsonObject invokeHttpPatch(String url, Map<?, ?> inputParams, Map<String, String> headerParams)
            throws HttpCallException, ClientProtocolException, IOException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = null;
        JsonObject result = new JsonObject();
        try {
            HttpPatch putRequest = new HttpPatch(url);
            updatePatchBody(putRequest, inputParams, headerParams);
            updateHeader(putRequest, headerParams);
            response = client.execute(putRequest);
            result = processResponse(response);
        } finally {
            try {
                if (null != response) {
                    ((CloseableHttpResponse) response).close();
                }
                if (null != client) {
                    ((CloseableHttpClient) client).close();
                }
            } catch (IOException e) {
                handleException("Error while closing url resources: " + url, e);
            }
        }
        return result;
    }

    private void updatePatchBody(HttpPatch patchRequest, Map<?, ?> inputParams, Map<String, String> headerParams)
            throws UnsupportedEncodingException {
        HttpEntity patchBody = null;
        if (!(headerParams == null)
                && (ContentType.APPLICATION_JSON.getMimeType().equals(headerParams.get(HttpHeaders.CONTENT_TYPE)))) {
            Gson gson = new Gson();
            patchRequest.setEntity(new StringEntity(gson.toJson(inputParams), ContentType.APPLICATION_JSON));
        } else {
            patchBody = getFormEncodedHttpEntity(inputParams);
        }
        if (null != patchBody) {
            patchRequest.setEntity(patchBody);
        }
    }

    private void updateQueryParameter(HttpGet request, Map<String, String> inputParams) throws URISyntaxException {
        if (null != inputParams) {
            Iterator<String> itr = inputParams.keySet().iterator();
            URIBuilder builder = new URIBuilder(request.getURI());
            String key = null;
            while (itr.hasNext()) {
                key = itr.next();
                builder.addParameter(key, inputParams.get(key));
            }
            request.setURI(builder.build());
        }
    }

    private void updatePostBody(HttpPost postRequest, Map<?, ?> inputParams, Map<String, String> headerParams)
            throws UnsupportedEncodingException {
        HttpEntity postBody = getFormEncodedHttpEntity(inputParams);
        if (null != postBody) {
            if (!(headerParams == null) && (ContentType.APPLICATION_JSON.getMimeType()
                    .equals(headerParams.get(HttpHeaders.CONTENT_TYPE)))) {
                Gson gson = new Gson();
                postRequest.setEntity(new StringEntity(gson.toJson(inputParams), ContentType.APPLICATION_JSON));
            } else {
                postRequest.setEntity(postBody);
            }
        }
    }

    private HttpEntity getFormEncodedHttpEntity(Map<?, ?> inputParams) throws UnsupportedEncodingException {
        UrlEncodedFormEntity entity = null;
        if (null == inputParams) {
            return entity;
        }
        List<NameValuePair> inputParamList = new ArrayList<>();
        Iterator<?> itr = inputParams.keySet().iterator();
        while (itr.hasNext()) {
            String key = (String) itr.next();
            inputParamList.add(new BasicNameValuePair(key, (String) inputParams.get(key)));
        }
        entity = new UrlEncodedFormEntity(inputParamList, StandardCharsets.UTF_8.name());
        return entity;
    }

    private void updateHeader(HttpRequest request, Map<String, String> headerParams) {
        if (null == headerParams) {
            return;
        }
        Iterator<String> itr = headerParams.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String value = headerParams.get(key);
            request.addHeader(key, value);
        }
    }

    private JsonObject processResponse(HttpResponse response) throws IOException {
        HttpEntity responseEntity = response.getEntity();
        JsonObject jsonObject = new JsonObject();
        if (null != responseEntity) {
            InputStream responseStream = null;
            try {
                responseStream = responseEntity.getContent();
                JsonParser parser = new JsonParser();
                jsonObject = (JsonObject) parser.parse(new InputStreamReader(responseStream));
            } finally {
                if (null != responseStream) {
                    responseStream.close();
                }
            }
        }
        return jsonObject;
    }

    private void handleException(String message, Exception e) throws HttpCallException {
        alert.prepareError(message, e).log();
        throw new HttpCallException(e.getMessage());
    }

}