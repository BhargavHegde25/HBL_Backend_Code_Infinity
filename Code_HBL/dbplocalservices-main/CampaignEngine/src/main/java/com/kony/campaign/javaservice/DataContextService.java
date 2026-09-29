package com.kony.campaign.javaservice;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.util.CampaignUtil;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DataContextService implements JavaService2 {	
	private static final String ERROR_WHILE_PROCESSING_THE_RESPONSE = "Error while processing the response";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {				
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, String> inputMap = (HashMap<String, String>) inputArray[1];     
		String campaignId =  inputMap.get(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID);
		String endpointUrl = inputMap.get(CampaignConstants.LOOP_PARAM_ENDPOINT_URL);
		String filter = inputMap.get(CampaignConstants.LOOP_PARAM_FILTER);
		String datacontextId  = inputMap.get(CampaignConstants.LOOP_PARAM_DATACONTEXT_ID);
		Result result = new Result();
		try {			
			hitDataContextAndGetResponse(endpointUrl, filter,datacontextId, result);
			result.addParam(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID, campaignId);			
		}catch(Exception e) {
			result.addErrMsgParam("DataContext service failed with error " + e.getMessage());
			result.addParam(CampaignConstants.ELIGABLE, Boolean.toString(false));
			alert.prepareError("error while hitting data context", e).log();			
		}
		return result;	
	}

	private void hitDataContextAndGetResponse(String endpointUrl, String filter, String datacontextId, Result result)
			throws IOException, URISyntaxException	 {
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("ENDPoint is " + endpointUrl).log();
			diagnostic.prepareDebug("filter is " + filter).log();
			diagnostic.prepareDebug("datacontextId is " + datacontextId).log();
		}
		String source = null;
		if (RefreshModelData.getModelData() == null) {
			synchronized (this) {
				RefreshModelData obj = new RefreshModelData();
				obj.GetDatafromDB(false);
			}
		}
		source = RefreshModelData.getModelData().get(datacontextId);
		if (source.equalsIgnoreCase("Internal")) {
			String[] endpoint = endpointUrl.split(":");
			String serviceName = endpoint[0];
			String operationName = endpoint[1];
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("$filter", filter);

			try {
				Result response = CampaignUtil.invokeService(serviceName, operationName, requestParameters);
				JsonObject responseJson = new JsonParser().parse(ResultToJSON.convert(response)).getAsJsonObject();
				processResponse(result, responseJson);
			} catch (Exception e) {
				alert.prepareError("Error while fetching response : "+e.getMessage()).log();
				result.addErrMsgParam("Error occured while getting fetching response");
				result.addParam(CampaignConstants.ELIGABLE, Boolean.toString(false));

			}
		} else if (source.equalsIgnoreCase("External")) {
			try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
				final URIBuilder builder = new URIBuilder(endpointUrl);
				builder.setParameter("$filter", filter);
				HttpGet httpGetObj = new HttpGet(builder.build());

				String userName = getDCUserName(datacontextId);
				String password = getDCPassword(datacontextId);
				if (userName == null || password == null) {
					alert.prepareError(CampaignConstants.DATACONTEXT_SERVICE_NO_USERNAME_PASSWORDS_ERRMSG).log();
					result.addErrMsgParam(CampaignConstants.DATACONTEXT_SERVICE_NO_USERNAME_PASSWORDS_ERRMSG);
					result.addParam(CampaignConstants.ELIGABLE, Boolean.toString(false));
					return;
				}

				httpGetObj.setHeader("Authorization", getAuthHeader(userName, password));

				try (CloseableHttpResponse httpResponse = httpClient.execute(httpGetObj)) {
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity);
						JsonObject responseJson = new JsonParser().parse(response).getAsJsonObject();
						if (diagnostic.isDebugEnabled()) {
							diagnostic.prepareDebug("Analytics response is " + responseJson);
						}
						processResponse(result, responseJson);
					} else {
						result.addErrMsgParam(CampaignConstants.DATACONTEXT_RETURNED_NON_200_CODE
								+ httpResponse.getStatusLine().getStatusCode());
						result.addParam(CampaignConstants.ELIGABLE, Boolean.toString(false));
					}
				}
			}
		}
		else {
			result.addErrMsgParam("Invalid source, cannot process request.");
			result.addParam(CampaignConstants.ELIGABLE, Boolean.toString(false));
		}
	}

	private String getDCUserName(String datacontextId) {
		String userNameDC = CampaignUtil.getServerProperty(
				CampaignConstants.DC_PREFIX+datacontextId+CampaignConstants.DC_SUFFIX_USERNAME, null);
		
		if( userNameDC == null ) {			
			userNameDC = CampaignUtil.getServerProperty(CampaignConstants.CAMPAIGN_DC_USERNAME, null);			
		}
		return userNameDC;
	}
	
	
	private String getDCPassword(String datacontextId) {
		String passwordDC = CampaignUtil.getServerProperty(
				CampaignConstants.DC_PREFIX+datacontextId+CampaignConstants.DC_SUFFIX_PASSWORD, null);
		if(  passwordDC == null ) {			
			passwordDC = CampaignUtil.getServerProperty(CampaignConstants.CAMPAIGN_DC_PASSWORD, null);
		}
		return passwordDC;
	}
	

	protected void processResponse(Result result, JsonObject responseJson) {
		if(responseJson.has(CampaignConstants.ERROR)) {
			JsonObject errorObj = responseJson.get(CampaignConstants.ERROR).getAsJsonObject();
			alert.prepareError("DataContext thrown error " + errorObj.get(CampaignConstants.MESSAGE).getAsString()).log();
			alert.prepareError("DataContext thrown error " + errorObj.get(CampaignConstants.CODE).getAsString()).log();
			result.addParam(CampaignConstants.ELIGABLE, Boolean.toString(false));
			result.addErrMsgParam(errorObj.get(CampaignConstants.MESSAGE).getAsString());
		}else {
			if (responseJson.has(CampaignConstants.VALUE)) {
				JsonArray responseJSONArray = responseJson.get(CampaignConstants.VALUE).getAsJsonArray();
				if(responseJSONArray.size() > 0) {
					result.addParam(CampaignConstants.ELIGABLE, Boolean.TRUE.toString());
					Dataset dset = new Dataset(CampaignConstants.CUSTOMERS_DATASET);            
					for (JsonElement jsonElement : responseJSONArray){
						Record customerRec = new Record();
						String customerId = jsonElement.getAsJsonObject().get(CampaignConstants.CUSTOMER_NUMBER).getAsString();
						customerRec.addParam(CampaignConstants.CUSTOMER_ID, customerId);
						dset.addRecord(customerRec);
					}
					result.addDataset(dset);
				}else {
					result.addParam(CampaignConstants.ELIGABLE, Boolean.FALSE.toString());
				}
			} else {
				result.addErrMsgParam(ERROR_WHILE_PROCESSING_THE_RESPONSE);
				result.addParam(CampaignConstants.ELIGABLE, Boolean.FALSE.toString());				
			}
		}
	}


	private String getAuthHeader(String userName, String password) {
		byte[] userAndPassword = (userName+":"+password).getBytes();
		return CampaignConstants.BASICHEADER + Base64.getEncoder().encodeToString(userAndPassword);
	}
	
}