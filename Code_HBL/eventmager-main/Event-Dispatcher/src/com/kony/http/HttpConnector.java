package com.kony.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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

public class HttpConnector {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@SuppressWarnings("rawtypes")
	public JsonObject invokeHttpPost(String url, Map inputParams, Map headerParams) throws HttpCallException {
		diagnostic.prepareDebug("url to connect " + url + " input params " + inputParams + " header params " + headerParams).log();
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
			handleException("Error while processing response", e);
		} catch (Exception e) {
			handleException("Error while calling url: " + url, e);
		} finally {
			try {
				if(response!=null) {
				((CloseableHttpResponse) response).close();}
								
				((CloseableHttpClient) client).close();
			} catch (Exception e) {
				handleException("Error while closing url resources: " + url, e);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public JsonObject invokeHttpGet(String url, Map headerParams) throws HttpCallException {
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
				if(response!=null) {
				((CloseableHttpResponse) response).close();}
				((CloseableHttpClient) client).close();
			} catch (IOException e) {
				handleException("Error while closing url resources: " + url, e);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void updatePostBody(HttpPost postRequest, Map inputParams, Map headerParams)
			throws UnsupportedEncodingException {
		if ((headerParams != null) && ("application/json".equals(headerParams.get("Content-Type")))) {
			Gson gson = new Gson();
			postRequest.setEntity(new StringEntity(gson.toJson(inputParams)));
		} else {
			HttpEntity postBody = getFormEncodedHttpEntity(inputParams);
			postRequest.setEntity(postBody);
		}
	}

	private void updatePutBody(HttpPut putRequest, Map inputParams, Map headerParams)
			throws UnsupportedEncodingException {
		if ((headerParams != null) && ("application/json".equals(headerParams.get("Content-Type")))) {
			Gson gson = new Gson();
			putRequest.setEntity(new StringEntity(gson.toJson(inputParams)));
		} else {
			HttpEntity postBody = getFormEncodedHttpEntity(inputParams);
			putRequest.setEntity(postBody);
		}
	}

	public JsonObject invokeHttpPut(String url, Map inputParams, Map headerParams)
			throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		JsonObject result = new JsonObject();
		try {
			HttpPut putRequest = new HttpPut(url);
			updatePutBody(putRequest, inputParams, headerParams);
			updateHeader(putRequest, headerParams);
			response = client.execute(putRequest);
			result = processResponse(response);
		} finally {
			if(response!=null) {
			((CloseableHttpResponse) response).close();}
			((CloseableHttpClient) client).close();
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private HttpEntity getFormEncodedHttpEntity(Map inputParams) throws UnsupportedEncodingException {
		UrlEncodedFormEntity entity = null;
		if (null == inputParams) {
			return entity;
		}
		List<NameValuePair> inputParamList = new ArrayList<>();
		Iterator itr = inputParams.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			inputParamList.add(new BasicNameValuePair(key, (String) inputParams.get(key)));
		}
		entity = new UrlEncodedFormEntity(inputParamList, "UTF-8");
		return entity;
	}

	@SuppressWarnings("rawtypes")
	private void updateHeader(HttpRequest request, Map headerParams) {
		if (null == headerParams) {
			return;
		}
		Iterator itr = headerParams.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String value = (String) headerParams.get(key);
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
				if(responseStream!=null) {
				responseStream.close();}
			}
		}
		return jsonObject;
	}

	private void handleException(String message, Exception e) throws HttpCallException {
		alert.prepareError(message, e).log();
		throw new HttpCallException(e.getMessage());
	}

}