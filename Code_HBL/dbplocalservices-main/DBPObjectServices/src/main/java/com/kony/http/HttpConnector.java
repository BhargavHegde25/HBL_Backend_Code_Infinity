package com.kony.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("rawtypes")
	public JsonObject invokeHttpPost(String url, Map inputparams, Map headerparams) throws HttpCallException {
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		JsonObject result = new JsonObject();
		try {
			HttpPost postrequest = new HttpPost(url);
			updatePostBody(postrequest, inputparams, headerparams);
			updateHeader(postrequest, headerparams);
			response = client.execute(postrequest);
			result = processResponse(response);
		} catch (IOException e) {
			handleException("Error while processing response", e);
		} catch (Exception e) {
			handleException("Error while calling url: " + url, e);
		} finally {
			try {
				((CloseableHttpClient) client).close();
				if (response != null) {
					((CloseableHttpResponse) response).close();
				}
			} catch (Exception e) {
				handleException("Error while closing url resources: " + url, e);
			}

		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	public JsonObject invokeHttpGet(String url, Map headerparams) throws HttpCallException {
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		JsonObject result = new JsonObject();
		try {
			HttpGet getRequest = new HttpGet(url);
			updateHeader(getRequest, headerparams);
			response = client.execute(getRequest);
			result = processResponse(response);
		} catch (IOException e) {
			handleException("Error while processing response", e);
		} catch (Exception e) {
			handleException("Error while calling url: " + url, e);
		} finally {
			try {
				((CloseableHttpClient) client).close();
				if (response != null) {
					((CloseableHttpResponse) response).close();
				}
			} catch (IOException e) {
				handleException("Error while closing url resources: " + url, e);
			}
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void updatePostBody(HttpPost postRequest, Map inputparams, Map headerparams)
			throws UnsupportedEncodingException {
		if ((headerparams != null) && ("application/json".equals(headerparams.get("Content-Type")))) {
			Gson gson = new Gson();
			postRequest.setEntity(new StringEntity(gson.toJson(inputparams)));
		} else {
			HttpEntity postBody = getFormEncodedHttpEntity(inputparams);
			postRequest.setEntity(postBody);
		}
	}

	private void updatePutBody(HttpPut putrequest, Map inputparams, Map headerparams)
			throws UnsupportedEncodingException {
		if ((headerparams != null) && ("application/json".equals(headerparams.get("Content-Type")))) {
			Gson gson = new Gson();
			putrequest.setEntity(new StringEntity(gson.toJson(inputparams)));
		} else {
			HttpEntity postbody = getFormEncodedHttpEntity(inputparams);
			putrequest.setEntity(postbody);
		}
	}

	public JsonObject invokeHttpPut(String url, Map inputparams, Map headerParams) throws IOException {
		HttpClient client = HttpClients.createDefault();
		HttpResponse response = null;
		JsonObject result = new JsonObject();
		try {
			HttpPut putrequest = new HttpPut(url);
			updatePutBody(putrequest, inputparams, headerParams);
			updateHeader(putrequest, headerParams);
			response = client.execute(putrequest);
			result = processResponse(response);
		} finally {
			((CloseableHttpClient) client).close();
			if (response != null) {
				((CloseableHttpResponse) response).close();
			}

		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private HttpEntity getFormEncodedHttpEntity(Map inputparams) throws UnsupportedEncodingException {
		UrlEncodedFormEntity entity = null;
		if (null == inputparams) {
			return entity;
		}
		List<NameValuePair> inputparamlist = new ArrayList<>();
		Iterator itr = inputparams.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			inputparamlist.add(new BasicNameValuePair(key, (String) inputparams.get(key)));
		}
		entity = new UrlEncodedFormEntity(inputparamlist, "UTF-8");
		return entity;
	}

	@SuppressWarnings("rawtypes")
	private void updateHeader(HttpRequest request, Map headerparams) {
		if (null == headerparams) {
			return;
		}
		Iterator itr = headerparams.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String value = (String) headerparams.get(key);
			request.addHeader(key, value);
		}
	}

	private JsonObject processResponse(HttpResponse response) throws IOException {
		HttpEntity responseentity = response.getEntity();
		JsonObject jsonobject = new JsonObject();
		if (null != responseentity) {
			try (InputStream responsestream = responseentity.getContent()) {
				JsonParser parser = new JsonParser();
				jsonobject = (JsonObject) parser.parse(new InputStreamReader(responsestream));
			}
		}
		return jsonobject;
	}

	private void handleException(String message, Exception e) throws HttpCallException {
		alert.prepareError(message, e).log();
		throw new HttpCallException(e.getMessage());
	}

}