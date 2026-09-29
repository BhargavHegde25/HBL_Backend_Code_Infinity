package com.kony.dbx.http;

import java.util.Map;

import org.json.JSONObject;

import com.kony.dbx.util.Constants;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;

public abstract class HttpRequest {	

	private String errMsg;
	private int httpStatus;
	private JSONObject jsonObj;
	private int opstatus;
	private String url;
	protected HttpMethod method;
	protected Map<String, Object> fields;
	protected Map<String, String> headers;
	private HttpResponse<JsonNode> response;


	// Constructors
	public HttpRequest() throws Exception {

		// Initialise all the member variables
		this.errMsg = "";
		this.url = "";
		this.fields = null;
		this.headers = null;
		this.method = null;
	}


	/**
	 * Check that url is provided
	 **/	
	private void checkMethod() throws Exception {
		if (this.method == null) { 
			throw new Exception("Method must be provided"); 
		}
	}


	/**
	 * Check that url is provided
	 **/	
	private void checkUrl() throws Exception {
		if (this.url == null || this.url.equalsIgnoreCase("")) { 
			throw new Exception("Url must be provided"); 
		}
	}

	

	/**
	 * Execute the request
	 **/	
	protected abstract void executeRequest() throws Exception;

	
	/**
	 * Get the KonyFabric errMsg
	 * @return String errMsg
	 **/	
	public String getErrMsg() {
		return this.errMsg;
	}


	/**
	 * Get the HTTP status code
	 * @return int Status code
	 **/	
	public int getHttpStatus() {
		return this.httpStatus;
	}


	/**
	 * Get the KonyFabric OP status code
	 * @return int Status code
	 **/	
	public int getOpStatus() {
		return this.opstatus;
	}


	/**
	 * Get the KonyFabric OP status code
	 * @return String Status code
	 **/	
	public String getOpStatusAsString() {
		return String.valueOf(this.opstatus);
	}


	/**
	 * Get the HTTP response
	 * @return String Status code
	 **/	
	public HttpResponse<JsonNode> getResponse() {
		return this.response;
	}


	/**
	 * Set the KonyFabric errMsg
	 * @return String errMsg
	 **/	
	protected void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}


	/**
	 * Set the request body fields
	 **/	
	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}


	/**
	 * Set the HTTP headers
	 **/	
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}


	/**
	 * Set the HTTP method
	 **/	
	protected void setMethod(HttpMethod method) {
		this.method = method;
	}


	/**
	 * Set the KonyFabric opstatus
	 **/	
	protected void setOpstatus(int opstatus) {
		this.opstatus = opstatus;
	}
	
	
	/**
	 * Set the url
	 **/	
	protected void setUrl(String url) {
		this.url = url;
	}

	
	/**
	 * Submit an HTTP Request sending the body as form
	 **/	
	protected void submitForm() throws Exception {

		// Sanity checks
		checkMethod();
		checkUrl();
		
		// Perform request
		HttpResponse<JsonNode> jsonResponse = null;
		switch (this.method) {
		
		case DELETE:

			jsonResponse = Unirest.delete(this.url)
			  .headers(this.headers)
			  .fields(this.fields)
			  .asJson();
			break;
			
		case PATCH:

			jsonResponse = Unirest.patch(this.url)
			  .headers(this.headers)
			  .fields(this.fields)
			  .asJson();
			break;

		case POST:

			jsonResponse = Unirest.post(this.url)
			  .headers(this.headers)
			  .fields(this.fields)
			  .asJson();
			break;

		case PUT:

			jsonResponse = Unirest.put(this.url)
			  .headers(this.headers)
			  .fields(this.fields)
			  .asJson();
			break;

		default:
			break;
			
		}
		
		
		// Grab the bits we need
		this.response = jsonResponse;
		this.httpStatus = this.response.getStatus();
		this.jsonObj = this.response.getBody().getObject();
		if (this.jsonObj.has(Constants.PARAM_ERR_MSG)) {
			this.errMsg = this.jsonObj.getString(Constants.PARAM_ERR_MSG);
		}
		if (this.jsonObj.has(Constants.PARAM_OP_STATUS)) {		
			this.opstatus = this.jsonObj.getInt(Constants.PARAM_OP_STATUS);
		}
	}
	
	
	/**
	 * Submit an HTTP GET Request
	 **/	
	protected void submitGet() throws Exception {

		// Sanity checks
		checkUrl();		

		// Build request
		GetRequest httpRequest = null;
		HttpResponse<JsonNode> jsonResponse = null;
		httpRequest = Unirest.get(this.url);

		// Add headers
		if (this.headers != null && this.headers.size() > 0) {
			httpRequest.headers(this.headers);
		}
		
		// Submit the request
		jsonResponse = httpRequest.asJson();
		
		
		// Grab the bits we need
		this.response = jsonResponse;
		this.httpStatus = this.response.getStatus();
		this.jsonObj = this.response.getBody().getObject();
		if (this.jsonObj.has(Constants.PARAM_ERR_MSG)) {
			this.errMsg = this.jsonObj.getString(Constants.PARAM_ERR_MSG);
		}
		if (this.jsonObj.has(Constants.PARAM_OP_STATUS)) {		
			this.opstatus = this.jsonObj.getInt(Constants.PARAM_OP_STATUS);
		}
	}
	
	
	/**
	 * Submit an HTTP Request sending the body as JSON
	 **/	
	protected void submitJsonRequest() throws Exception {

		// Sanity checks
		checkMethod();
		checkUrl();
		
		// Build JSON body if required
		JSONObject jsonBody = null;
		if (this.fields != null && this.fields.size() > 0) {
			jsonBody = new JSONObject();
			for (String key : this.fields.keySet()) {
				String value = (String) this.fields.get(key);
				jsonBody.put(key, value);
			}
		}

		// Build request
		HttpRequestWithBody httpRequest = null;
		HttpResponse<JsonNode> jsonResponse = null;
		switch (this.method) {
		
		case DELETE:
			httpRequest = Unirest.delete(this.url);
			break;
			
		case PATCH:
			httpRequest = Unirest.patch(this.url);
			break;

		case POST:
			httpRequest = Unirest.post(this.url);
			break;

		case PUT:
			httpRequest = Unirest.put(this.url);
			break;
			
		default:
			throw new Exception("Unsupported method: " + method.name());
		}

		// Add headers
		if (this.headers != null && this.headers.size() > 0) {
			httpRequest.headers(this.headers);
		}
		
		// Add body
		if (jsonBody != null) {
			httpRequest.body(jsonBody);
		}
		
		// Submit the request
		jsonResponse = httpRequest.asJson();
		
		
		// Grab the bits we need
		this.response = jsonResponse;
		this.httpStatus = this.response.getStatus();
		this.jsonObj = this.response.getBody().getObject();
		if (this.jsonObj.has(Constants.PARAM_ERR_MSG)) {
			this.errMsg = this.jsonObj.getString(Constants.PARAM_ERR_MSG);
		}
		if (this.jsonObj.has(Constants.PARAM_OP_STATUS)) {		
			this.opstatus = this.jsonObj.getInt(Constants.PARAM_OP_STATUS);
		}
	}
}
