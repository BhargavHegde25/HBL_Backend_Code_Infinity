package com.kony.campaign.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.campaign.common.CampaignConstants;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public final class CampaignUtil {	
	public static final String DBXDB_DEFAULT_SCHEMA_NAME = "dbxdb";
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public static Result invokeService(String serviceId,String operationId, Map<String, Object> inputMap)
			throws MiddlewareException {	
		if(diagnostic.isDebugEnabled()){
			diagnostic.prepareDebug("Service is being hit"+serviceId+" "+ operationId +"with input Map" + inputMap).log();
		}
		ServiceRequest serviceRequest = ServicesManagerHelper.getServicesManager().
				getRequestBuilder(getOperationData(serviceId,operationId))
				.withInputs(inputMap).build();
		Result res = serviceRequest.invokeServiceAndGetResult();
		if(diagnostic.isDebugEnabled()){
		diagnostic.prepareDebug(operationId + "response is " + ResultToJSON.convert(res)).log();
		}
		return res;
	}

	public static OperationData getOperationData(String serviceId,String operationId) throws MiddlewareException {
		try {
			return ServicesManagerHelper.getServicesManager().getOperationDataBuilder()
					.withServiceId(serviceId).withOperationId(operationId).build();
		} catch (MiddlewareException e) {
			alert.prepareError("Error while invoking operation "+ operationId + " of service "+ serviceId ).log();
			throw e;
		}		
	}	
	
	public static final Function<Record, Boolean> funcRecordToboolean = (Record rec)-> Boolean.valueOf(rec.getParamByName("eligable").getValue());
	
	
	public static String getServerProperty(String propertyName, String defaultValue) {
		String value = null;
		try {
			value =  EnvironmentConfigurationsHandler.getServerAppProperty(propertyName);
		} catch (Exception e) {
			alert.prepareError("Error while fetching server property "+ propertyName ).log();
			
		}
		return value != null ? value : defaultValue;		
	}
	
	public static int getCacheExpiry() {
	    return Integer.parseInt(CampaignUtil.getServerProperty(CampaignConstants.CAMPAIGN_CACHE_EXPIRY, String.valueOf(86400)));
	}
	
	public static int getIntServerProperty(String propertyName, int defaultValue) {
	    return Integer.parseInt(CampaignUtil.getServerProperty(propertyName, String.valueOf(defaultValue)));
	}
	
	
	public static synchronized void updateCache(String userId, String cacheValue, int cacheExpiryTimeInSecs) throws MiddlewareException {
		if(diagnostic.isDebugEnabled()){
		 diagnostic.prepareDebug("UpdateCache for user with Value"+ userId + ":" +cacheValue).log();
		}
		ServicesManagerHelper.getServicesManager().getResultCache().insertIntoCache(userId, cacheValue,cacheExpiryTimeInSecs);
	}

	public static Object getFromCache(String userId) throws MiddlewareException {
		return ServicesManagerHelper.getServicesManager().getResultCache().retrieveFromCache(userId);
	}
	
	public static String decodeValue(String value) throws UnsupportedEncodingException  {
                 return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());       
    }
	
	public static String getOnlineCampaignResponsiveProperty() {
		String campaignFetchType = CampaignUtil.getServerProperty(CampaignConstants.CAMPAIGN_FETCHTYPE, CampaignConstants.REALTIME);
		campaignFetchType = campaignFetchType.equalsIgnoreCase(CampaignConstants.CACHE) ?
				  CampaignConstants.CACHE : CampaignConstants.REALTIME ;		    
		return campaignFetchType;
	}
	
	public static boolean isCacheUpdateRequired(String campFetchType) {
		return campFetchType.equalsIgnoreCase(CampaignConstants.CACHE) ? true : false;
	}
	
	public static boolean isCacheUpdateRequired() {
		return isCacheUpdateRequired(getOnlineCampaignResponsiveProperty());
	}	
	
	public static void addDBPErrCodeAndmsg(Result campaignsSelected, String errmsgString, int errorCode) {
		campaignsSelected.addParam(CampaignConstants.DBP_ERROR_MESSAGE, 
				errmsgString);	
		campaignsSelected.addIntParam(CampaignConstants.DBP_ERROR_CODE, 
				errorCode);
	}
	
	public static Map<String, JsonObject> getOCMap(JsonArray oCArray) {
		Map<String, JsonObject> ocMap = new HashMap<>();
		for (JsonElement jsonElement : oCArray) {
			JsonObject onlineObject = jsonElement.getAsJsonObject();							      
			ocMap.put(onlineObject.get(CampaignConstants.ONLINE_CONTENT_ID).getAsString(), onlineObject);
		}
		return ocMap;
	}
	
	 public static String getDatabaseServiceNames(String dbserviceName) {	        
		 if(dbserviceName.contains(CampaignConstants.SCHEMA_NAME_PLACE_HOLDER)) {
			 String schemaName = getServerProperty(CampaignConstants.DBX_SCHEMA_NAME , DBXDB_DEFAULT_SCHEMA_NAME);
			 if( StringUtils.isNotBlank(schemaName) ) {
				 dbserviceName = dbserviceName.replace(CampaignConstants.SCHEMA_NAME_PLACE_HOLDER,schemaName );	         
	        }
		 }	        
	     return dbserviceName;
	 } 
	 
	
	private CampaignUtil() { }
	
}
