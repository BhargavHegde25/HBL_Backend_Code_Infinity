package com.kony.adminconsole.campaign.businessdelegate.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.adminconsole.campaign.businessdelegate.api.DataContextBusinessDelegate;
import com.kony.adminconsole.campaign.utilities.CampaignUtil;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.dto.campaign.DataContextRequestDTO;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class DataContextBusinessDelegateImpl implements DataContextBusinessDelegate{
		
	private static final String ERROR_WHILE_PROCESSING_THE_RESPONSE = "Error while processing the response ";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public String getActiveCustomersForSegment(DataContextRequestDTO dcReqDTO) throws 	ApplicationException{		
		try {
			String userName = getDCUserName(dcReqDTO.getDataContextID());
			String password = getDCPassword(dcReqDTO.getDataContextID());
			if( userName == null || password == null ) {
				alert.prepareError(ACConstants.DATACONTEXT_SERVICE_NO_USERNAME_PASSWORDS_ERRMSG).log();
				throw new ApplicationException(ErrorCodeEnum.ERR_21804,
						new Throwable(ACConstants.DATACONTEXT_SERVICE_NO_USERNAME_PASSWORDS_ERRMSG));			
			 }		
			return invokeDataContextService(dcReqDTO, userName, password);				
		}catch(Exception e) {		
			alert.prepareError("Error while hitting data context", e).log();	
			return ACConstants.DCCUSTOMERRMSG;
		}
			
	}
		
	private String invokeDataContextService(DataContextRequestDTO dcReqDTO, String userName, String password) 
	throws IOException, URISyntaxException, ApplicationException	 {
			
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("SegmentID is " + dcReqDTO.getRequesterID()).log();
			diagnostic.prepareDebug("ENDPoint is " + dcReqDTO.getEndPointURL()).log();
			diagnostic.prepareDebug("filter is " + dcReqDTO.getFilter()).log();
			diagnostic.prepareDebug("datacontextId is " + dcReqDTO.getDataContextID()).log();
		}
		
		String source = getSource(dcReqDTO.getDataContextID());
		if(source.equalsIgnoreCase("Internal")) {
			String[] endpoint = dcReqDTO.getEndPointURL().split(":");
			String serviceName = endpoint[0];
			String operationName = endpoint[1];
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			//Doubt on this parameter.
			requestParameters.put(ODataQueryConstants.FILTER,"");//getDecodedConditionExpression(dcReqDTO.getFilter()));
			
			try {
				String response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
				
				JsonObject responseJson = new JsonParser().parse(response).getAsJsonObject();
				return processResponse(responseJson);
			} catch (Exception e) {
				alert.prepareError("Error while fetching response : ", e.getMessage()).log();

			}
		}
		else if (source.equalsIgnoreCase("External")) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				final URIBuilder builder = new URIBuilder(dcReqDTO.getEndPointURL());
				builder.setParameter("filter", getDecodedConditionExpression(dcReqDTO.getFilter()));
				HttpGet httpGetObj = new HttpGet(builder.build());
				setHeaders(dcReqDTO, userName, password, httpGetObj);
				try (CloseableHttpResponse httpResponse = httpClient.execute(httpGetObj)) {
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity);
						JsonObject responseJson = new JsonParser().parse(response).getAsJsonObject();
						if (diagnostic.isDebugEnabled()) {
							diagnostic.prepareDebug("Analytics response is " + responseJson).log();
						}
						return processResponse(responseJson);
					} else {
						alert.prepareError(ACConstants.DATACONTEXT_RETURNED_NON_200_CODE
								+ httpResponse.getStatusLine().getStatusCode()).log();

						throw new ApplicationException(ErrorCodeEnum.ERR_21800,
								new Throwable(ACConstants.DATACONTEXT_RETURNED_NON_200_CODE
										+ httpResponse.getStatusLine().getStatusCode()));
					}
				}
			}
		}else {
			alert.prepareError("Unknown Source!! Cannot process request.").log();
		}
		return ACConstants.DCCUSTOMERRMSG;
	}

	protected void setHeaders(DataContextRequestDTO dcReqDTO, String userName, String password, HttpGet httpGetObj) {
		httpGetObj.setHeader("Authorization", getAuthHeader(userName,password));
		Map<String, Object> headerMap = dcReqDTO.getHeaderMap() ;
		for (Entry<String, Object> headerEntry : headerMap.entrySet()) {
			if(StringUtils.isNotBlank(headerEntry.getValue().toString())) {
			httpGetObj.setHeader(headerEntry.getKey(), headerEntry.getValue().toString());
			}
		}
	}
	
	protected String processResponse(JsonObject responseJson) throws ApplicationException {
		if(responseJson.has(ACConstants.ERROR)) {
			JsonObject errorObj = responseJson.get(ACConstants.ERROR).getAsJsonObject();
			alert.prepareError("DataContext thrown error " + errorObj.get(ACConstants.MESSAGE).getAsString()).log();
			alert.prepareError("DataContext thrown error " + errorObj.get(ACConstants.CODE).getAsString()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_21800,
					new Throwable(errorObj.get(ACConstants.MESSAGE).getAsString()));		
		}else {
			if (responseJson.has(ACConstants.VALUE)) {
				Set<String> customerSet = new HashSet<>();
				JsonArray responseJSONArray = responseJson.get(ACConstants.VALUE).getAsJsonArray();
				populateCustomerSet(customerSet, responseJSONArray);
				return customerSet.stream().collect(Collectors.joining(","));
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_21800, new Throwable(ERROR_WHILE_PROCESSING_THE_RESPONSE));								
			}
		}
	}

	private void populateCustomerSet(Set<String> customerSet, JsonArray responseJSONArray) {
		if(responseJSONArray.size() > 0) {				  
			for (JsonElement jsonElement : responseJSONArray){
				String customerId = jsonElement.getAsJsonObject().get(ACConstants.CUSTOMER_NUMBER).getAsString();
				if(!customerSet.contains(customerId)) {
					customerSet.add(customerId);
				}									
			}					
		}
	}

	private String getDCUserName(String datacontextId) {
		String userNameDC = CampaignUtil.getServerProperty(
				ACConstants.DC_PREFIX+datacontextId+ACConstants.DC_SUFFIX_USERNAME, null);
		
		if( userNameDC == null ) {			
			userNameDC = CampaignUtil.getServerProperty(ACConstants.CAMPAIGN_DC_USERNAME, null);			
		}
		return userNameDC;
	}	
	
	private String getDCPassword(String datacontextId) {
		String passwordDC = CampaignUtil.getServerProperty(
				ACConstants.DC_PREFIX+datacontextId+ACConstants.DC_SUFFIX_PASSWORD, null);
		if(  passwordDC == null ) {			
			passwordDC = CampaignUtil.getServerProperty(ACConstants.CAMPAIGN_DC_PASSWORD, null);
		}
		return passwordDC;
	}	

	private String getAuthHeader(String userName, String password) {
		byte[] userAndPassword = (userName+":"+password).getBytes();
		return ACConstants.BASICHEADER + Base64.getEncoder().encodeToString(userAndPassword);
	}
	
	
	private String getDecodedConditionExpression(String profileCondition) {
		try {
			profileCondition =  CampaignUtil.decodeValue(profileCondition);
		} catch (UnsupportedEncodingException e) {
			alert.prepareError("Encoding the condition failed " + profileCondition).log();
		}
		return profileCondition;
	}
	
	public String getSource(String dcId) {
		String source = "";
		JSONObject responseObj = new JSONObject();
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_MODEL_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.SELECT, "source");
		requestParameters.put(ODataQueryConstants.FILTER, "id eq "+dcId);
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters).build().getResponse();
			responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.optJSONArray("model");
			if(responseArray.length()>0) {
				source = responseArray.getJSONObject(0).getString("source");
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception at get Model Source : ", e).log();
		}
		return source;
	}

}