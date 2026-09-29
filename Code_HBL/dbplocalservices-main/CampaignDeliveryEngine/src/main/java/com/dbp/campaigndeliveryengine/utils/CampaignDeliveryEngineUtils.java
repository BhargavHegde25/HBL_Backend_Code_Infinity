package com.dbp.campaigndeliveryengine.utils;

import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

public final class CampaignDeliveryEngineUtils {
	private CampaignDeliveryEngineUtils() {

	}
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static Result callInternalService(Map<String, Object> requestParameters, String serviceid,
			String operationid, String objectid) {
		if (serviceid == null || operationid == null)
			return new Result();
		try {
			DBPServiceExecutorBuilder db = DBPServiceExecutorBuilder.builder().withServiceId(serviceid)
					.withOperationId(operationid);
			if (objectid != null)
				db = db.withObjectId(objectid);
			return db.withRequestParameters(requestParameters).build().getResult();
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in calling service", e).log();
		}
		return new Result();
	}

	public static JsonElement parseString(String events) {
		JsonElement eventsElement = null;
		try {
			eventsElement = (new JsonParser()).parse(events);
		} catch (Exception e) {
			// alert.prepareError("Error occured in parsing:", e).log();
		}
		return eventsElement;
	}

	public static Result returnResult(boolean success, String dbperrmsg) {
		Result result = new Result();
		if (success) {
			result.addParam(new Param(CampaignDeliveryEngineConstants.SUCCESS, CampaignDeliveryEngineConstants.TRUE,
					CampaignDeliveryEngineConstants.STRING));
			if (!dbperrmsg.equals(""))
				result.addParam(new Param(CampaignDeliveryEngineConstants.DBPERRMSG, dbperrmsg,
						CampaignDeliveryEngineConstants.STRING));
			return result;
		}
		result.addParam(new Param(CampaignDeliveryEngineConstants.SUCCESS, CampaignDeliveryEngineConstants.FALSE,
				CampaignDeliveryEngineConstants.STRING));
		result.addParam(new Param(CampaignDeliveryEngineConstants.DBPERRMSG, dbperrmsg,
				CampaignDeliveryEngineConstants.STRING));
		return result;
	}

	public static JsonObject callhttpApi(Map inputparams, Map headerparams, String url)
			throws com.dbp.campaigndeliveryengine.httputils.HttpCallException {
		com.dbp.campaigndeliveryengine.httputils.HttpConnector httpConn = new com.dbp.campaigndeliveryengine.httputils.HttpConnector();
		JsonObject response = httpConn.invokeHttpPost(url, inputparams, headerparams);
		return (null == response) ? new JsonObject() : response;
	}

	public static String decodeFromBase64(String sourceString) {
		if (sourceString == null) {
			return null;
		}
		return new String(org.apache.commons.codec.binary.Base64.decodeBase64(sourceString));
	}

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static boolean isMemberExistsAndValid(JsonElement element, String key) {

		if (element == null || element.isJsonNull() || !element.isJsonObject())
			return false;
		JsonObject elemobj = element.getAsJsonObject();
		return !(!elemobj.has(key) || elemobj.get(key) == null || elemobj.get(key).isJsonNull());

	}
	
	public static JsonObject buildlogparams() {
		JsonObject logparams = new JsonObject();
		logparams.addProperty("eventType",CampaignDeliveryEngineConstants.EVENTTYPE);
		logparams.addProperty("eventSubtype",CampaignDeliveryEngineConstants.EVENTTYPE);
		return logparams;
	}

}