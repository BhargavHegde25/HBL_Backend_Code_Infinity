package com.kony.objectserviceutils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;

import com.dbp.core.util.MemoryManager;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.utils.HelperMethods;
import com.kony.utils.TokenUtils;
import com.kony.utils.URLConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

import eu.bitwalker.useragentutils.UserAgent;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

public class EventsDispatcher {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	enum Constants {
		REQUESTINPUT("requestInput"), RESPONSEOUTPUT("responseOutput"), ACCOUNTNUMBER("accountnumber"),
		SESSIONID("sessionId"), EVENTDISPATCHERSERVERDATE("eventDispatcherServerDate"), CUSTOMPARAMS("customParams"),
		EVENTTYPE("eventType"), EVENTSUBTYPE("eventSubType"), STATUS("status"), OTHERDATA("otherData"),
		EVENTDATA("eventData"), TOKEN("token"), XKONYAUTHORIZATION("X-Kony-Authorization"), SESSION("session"),
		APPSESSIONID("appSessionId"), EVENTS("events"), PRODUCER("producer"), CSRUSERNAME("CSR_Username"),
		CSRUSERID("CSR_User_Id"), CSRROLE("CSR_Role"), CSRNAME("CSR_Name"), CSRUSER("CSR_User"),
		XKONYREPORTINGPARAMS("X-Kony-ReportingParams"), EXTERNALPHONE("externalphone"), EXTERNALMAIL("externalemail"),
		APPID("appid"), INCLUDEPREFERREDCONTACT("includepreferredcontact"), LEGALENTITYID("legalEntityId");

		private String name;

		private Constants(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static Result callQueueMaster(ServicesManager servicesManager, Map<String, Object> inputMap,
			Map<String, Object> headerMap) {
		Result result = new Result();
		try {
			OperationData operationData = servicesManager.getOperationDataBuilder().withServiceId("QueueMaster")
					.withOperationId("PushEventQueue").build();
			ServiceRequest serviceRequest = servicesManager.getRequestBuilder(operationData).withInputs(inputMap)
					.withHeaders(headerMap).build();
			result = serviceRequest.invokeServiceAndGetResult();
		} catch (AppRegistryException arex) {
			result = getErrorResult(1012, "Could not access QueueManager service via app registry");
		} catch (Exception ex) {
			result = getErrorResult(ex);
		}
		return result;
	}

	public static Result dispatch(FabricRequestManager requestManager, JsonObject responseData, String eventType,
			String eventSubType, String producer, String statusId, String account, String customerId,
			JsonObject customParams) {
		try {
			if (producer == null || producer.equals("")) {
				producer = requestManager.getServicesManager().getOperationData().getServiceId() + "/"
						+ requestManager.getServicesManager().getOperationData().getObjectId() + "/"
						+ requestManager.getServicesManager().getOperationData().getOperationId();
				String className = Thread.currentThread().getStackTrace()[2].getClassName();
				String[] feilds = className.split("\\.");
				className = null;
				if (feilds.length > 0)
					className = feilds[feilds.length - 1];
				if (className != null)
					producer = producer + "/" + className;
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured in building producer string ", e).log();
		}

		if (customParams != null) {
			customParams.addProperty(Constants.PRODUCER.toString(), producer);
		} else {
			JsonObject newobj = new JsonObject();
			newobj.addProperty(Constants.PRODUCER.toString(), producer);
			customParams = newobj;
		}
		return dispatch(requestManager, null, responseData, eventType, eventSubType, producer, statusId, account,
				customerId, customParams);
	}

	private static JsonObject getCSRrelatedParams(FabricRequestManager requestManager, JsonObject otherData) {
		try {
			if (requestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(Constants.CSRUSERNAME.toString()) != null) {
				otherData.addProperty(Constants.CSRUSERNAME.toString(), (String) requestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(Constants.CSRUSERNAME.toString()));
			}

			if (requestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(Constants.CSRUSERID.toString()) != null) {
				otherData.addProperty(Constants.CSRUSERID.toString(), (String) requestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(Constants.CSRUSERID.toString()));
			}

			if (requestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(Constants.CSRROLE.toString()) != null) {
				otherData.addProperty(Constants.CSRROLE.toString(), (String) requestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(Constants.CSRROLE.toString()));
			}

			if (requestManager.getServicesManager().getIdentityHandler().getUserAttributes()
					.get(Constants.CSRNAME.toString()) != null) {
				otherData.addProperty(Constants.CSRNAME.toString(), (String) requestManager.getServicesManager()
						.getIdentityHandler().getUserAttributes().get(Constants.CSRNAME.toString()));
			}
		} catch (Exception e) {
			alert.prepareError("Error while fetching csr related params").log();
		}
		return otherData;
	}

	public static Result dispatch(FabricRequestManager requestManager, FabricResponseManager responseManager,
			String eventType, String eventSubType, String producer, String statusId, String account, String customerId,
			JsonObject customParams) {
		JsonObject responseData = new JsonObject();
		try {
			if (responseManager != null && responseManager.getPayloadHandler() != null
					&& responseManager.getPayloadHandler().getPayloadAsJson() != null) {
				responseData = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
			}
		} catch (Exception err1) {
			alert.prepareError("Response Payload Fetch Exception", err1).log();
		}
		try {
			if (producer == null || producer.equals("")) {
				producer = requestManager.getServicesManager().getOperationData().getServiceId() + "/"
						+ requestManager.getServicesManager().getOperationData().getObjectId() + "/"
						+ requestManager.getServicesManager().getOperationData().getOperationId();
				String className = Thread.currentThread().getStackTrace()[2].getClassName();
				String[] feilds = className.split("\\.");
				className = null;
				if (feilds.length > 0)
					className = feilds[feilds.length - 1];
				if (className != null)
					producer = producer + "/" + className;
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured in building producer string", e).log();
		}

		if (customParams != null) {
			customParams.addProperty(Constants.PRODUCER.toString(), producer);
		} else {
			JsonObject newobj = new JsonObject();
			newobj.addProperty(Constants.PRODUCER.toString(), producer);
			customParams = newobj;
		}
		return dispatch(requestManager, null, responseData, eventType, eventSubType, producer, statusId, account,
				customerId, customParams);

	}
	public static String getLegalEntityIdFromSessionOrCache(DataControllerRequest dcRequest){
        String leid = "";
        try {
        	String session_token = getSessionTokenFromIdentityService(dcRequest);
            leid = (String) HelperMethods.getFromCache(session_token + URLConstants.SUFFIX_CACHE_NAME);
            
        } catch (Exception e) {
        	alert.prepareError("exception while retieving leid from cache", e).log();
        }
        return leid;  
        
    }
	
	public static String getLegalEntityIdFromSessionOrCache(FabricRequestManager dcRequest){
        String leid = "";
        try {
           // String userId = dcRequest.getServicesManager().getIdentityHandler().getUserId();
            String session_token = getSessionTokenFromIdentityService(dcRequest);
            leid = (String) HelperMethods.getFromCache(session_token + URLConstants.SUFFIX_CACHE_NAME);
            

        } catch (Exception e) {
        	alert.prepareError("exception while retieving leid from cache", e).log();
        }
       
        return leid;    
    }
	
	public static String getSessionTokenFromIdentityService(FabricRequestManager dcRequest) {
		String sessionToken = "";
		try {
			if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
				sessionToken = dcRequest.getServicesManager().getIdentityHandler().getSecurityAttributes()
						.get("session_token").toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert.prepareError("exception occurred while fetching token from Identity Service", e.getMessage()).log();
		}
		return sessionToken;
	}
	public static String getSessionTokenFromIdentityService(DataControllerRequest dcRequest) {
		String sessionToken = "";
		try {
			if (dcRequest.getServicesManager() != null && dcRequest.getServicesManager().getIdentityHandler() != null) {
				sessionToken = dcRequest.getServicesManager().getIdentityHandler().getSecurityAttributes()
						.get("session_token").toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			alert.prepareError("exception occurred while fetching session token from Identity Service", e.getMessage()).log();
		}
		return sessionToken;
	}
	
	
	
	
	public static Result dispatch(FabricRequestManager requestManager, JsonObject requestData, JsonObject responseData,
			String eventType, String eventSubType, String producer, String statusId, String account, String customerId,
			JsonObject customParams) {
		try {
			if (requestManager != null && requestManager.getPayloadHandler() != null
					&& requestManager.getPayloadHandler().getPayloadAsJson() != null
					&& (requestData == null || requestData.isJsonNull()))
				requestData = requestManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
		} catch (Exception err) {
			alert.prepareError("Request Payload Fetch Exception", err).log();
		}
		JsonObject eventData = new JsonObject();
		JsonObject eventDetails = new JsonObject();
		JsonObject otherData = new JsonObject();
		String reportingParamsString = "";
		String sessionid = null;
		String appsessionid = null;
		String authkey = "";
		String companyLegalUnit =null;
		if("LOGIN".equalsIgnoreCase(eventType)) {
			companyLegalUnit=HelperMethods.getLoginEntityId(requestManager);
		}else {
			companyLegalUnit=getLegalEntityIdFromSessionOrCache(requestManager);
		}
		
		
		if(companyLegalUnit == null || companyLegalUnit.isEmpty())
		{
			companyLegalUnit=HelperMethods.getCompanyId(requestManager);
		}
		
		boolean iscustidneeded = true;
		if (customParams == null)
			customParams = new JsonObject();

		otherData = getCSRrelatedParams(requestManager, otherData);

		if (customParams != null && customParams.get(Constants.SESSIONID.toString()) != null
				&& !customParams.get(Constants.SESSIONID.toString()).isJsonNull()) {
			sessionid = customParams.get(Constants.SESSIONID.toString()).getAsString();
		} else {
			try {
				sessionid = requestManager.getServicesManager().getIdentityHandler().getSecurityAttributes()
						.get("session_token").toString();

			} catch (Exception e1) {
				alert.prepareError("exception occured while fetching sessionid", e1).log();
			}
		}

		try {
			reportingParamsString = requestManager.getServicesManager().getDeviceRequestData().getReportingParams();
			authkey = requestManager.getHeadersHandler().getHeader(Constants.XKONYAUTHORIZATION.toString());
			if (customParams != null && customParams.get("user") != null) {
				iscustidneeded = false;
			}

			if (iscustidneeded && (customerId == null || customerId.equals(""))) {
				customerId = getParamFromIToken(authkey, URLConstants.PROVIDER_USER_ID);
			}

			appsessionid = getParamFromIToken(authkey, URLConstants.SESSIONID);

		} catch (Exception e) {
			alert.prepareError("ReportingParamException", e).log();
		}

		try {
			if (customerId == null || customerId.equalsIgnoreCase("anonymous") || customerId.equals("")) {
				customerId = requestManager.getServicesManager().getIdentityHandler().getUserAttributes().get("user_id")
						.toString();
			}
		} catch (Exception e) {
			alert.prepareError("error while fetching fetching customerid from userattributes").log();
		}
		
		try {
			eventDetails.addProperty(Constants.SESSION.toString(),
					getCustomReportingParams(reportingParamsString, requestManager).toString());
		} catch (Exception e) {
			alert.prepareError("ReportingParamException", e).log();
		}
		if (customParams != null && customParams.has(Constants.APPID.toString())) {
			String appid = customParams.get(Constants.APPID.toString()).getAsString();
			if (StringUtils.isNotBlank(appid))
				otherData.addProperty(Constants.APPID.toString(), appid);
		}
		customParams.addProperty(Constants.EVENTDISPATCHERSERVERDATE.toString(), getServerDate());
		if (appsessionid != null) {
			customParams.addProperty(Constants.APPSESSIONID.toString(), appsessionid);
		}
		eventData.add(Constants.REQUESTINPUT.toString(), requestData);
		if (responseData.isJsonObject()) {
			eventData.add(Constants.RESPONSEOUTPUT.toString(), responseData);
		} else {
			eventData.add(Constants.RESPONSEOUTPUT.toString(), new JsonObject());
		}
		eventData.add(Constants.CUSTOMPARAMS.toString(), customParams);
		// my change
		if (iscustidneeded && customerId != null && !customerId.equals("")
				&& !customerId.equalsIgnoreCase("schedulingengine")) {
			otherData.addProperty("customerId", customerId);
		}

		addExternalCommunicationData(otherData, customParams);
		otherData.addProperty(Constants.SESSIONID.toString(), sessionid);
		if (customParams.has("user") && customParams.get("user") != null) {
			otherData.addProperty("user", customParams.get("user").getAsString());
		}

		if (account != null) {
			otherData.addProperty(Constants.ACCOUNTNUMBER.toString(), account);
		}
		otherData.addProperty("companyLegalUnit", companyLegalUnit);
		eventDetails.addProperty(Constants.EVENTTYPE.toString(), eventType);
		eventDetails.addProperty(Constants.EVENTSUBTYPE.toString(), eventSubType);
		eventDetails.addProperty(Constants.STATUS.toString(), statusId);
		eventDetails.add(Constants.OTHERDATA.toString(), otherData);
		eventDetails.add(Constants.EVENTDATA.toString(), eventData);
		JsonArray events = new JsonArray();
		events.add(eventDetails);
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();
		inputMap.put(Constants.EVENTS.toString(), events);
		ServicesManager servicesManager = requestManager.getServicesManager();

		if (customParams.has(Constants.PRODUCER.toString())) {
			producer = customParams.get(Constants.PRODUCER.toString()).getAsString();
		}

		else {
			try {
				if (producer == null || producer.equals("")) {

					producer = servicesManager.getOperationData().getServiceId() + "/"
							+ requestManager.getServicesManager().getOperationData().getObjectId() + "/"
							+ requestManager.getServicesManager().getOperationData().getOperationId();

				}
				String className = Thread.currentThread().getStackTrace()[2].getClassName();
				String[] feilds = className.split("\\.");
				className = null;
				if (feilds.length > 0)
					className = feilds[feilds.length - 1];
				if (className != null)
					producer = producer + "/" + className;
			} catch (Exception e) {
				producer = "";
				alert.prepareError("unable to get producer").log();
			}

		}
		inputMap.put(Constants.PRODUCER.toString(), producer);
		String eventsString = events.toString();
		String derivedToken = deriveToken(servicesManager, eventsString);
		inputMap.put(Constants.TOKEN.toString(), derivedToken);

		return callQueueMaster(servicesManager, inputMap, headerMap);
	}

	public static Result dispatch(FabricRequestManager requestManager, JsonObject requestData,
			FabricResponseManager responseManager, String eventType, String eventSubType, String producer,
			String statusId, String account, String customerId, JsonObject customParams) {
		JsonObject responseData = new JsonObject();
		try {
			if (responseManager != null && responseManager.getPayloadHandler() != null
					&& responseManager.getPayloadHandler().getPayloadAsJson() != null) {
				responseData = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
			}
		} catch (Exception err1) {
			alert.prepareError("Response Payload Fetch Exception", err1).log();
		}
		try {
			if (producer == null || producer.equals("")) {
				producer = requestManager.getServicesManager().getOperationData().getServiceId() + "/"
						+ requestManager.getServicesManager().getOperationData().getObjectId() + "/"
						+ requestManager.getServicesManager().getOperationData().getOperationId();
				String className = Thread.currentThread().getStackTrace()[2].getClassName();
				String[] feilds = className.split("\\.");
				className = null;
				if (feilds.length > 0)
					className = feilds[feilds.length - 1];
				if (className != null)
					producer = producer + "/" + className;
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured in building producer string" + e).log();
		}

		if (customParams != null) {
			customParams.addProperty(Constants.PRODUCER.toString(), producer);
		} else {
			JsonObject newobj = new JsonObject();
			newobj.addProperty(Constants.PRODUCER.toString(), producer);
			customParams = newobj;
		}
		return dispatch(requestManager, requestData, responseData, eventType, eventSubType, producer, statusId, account,
				customerId, customParams);

	}

	private static String getServerDate() {
		try {
			String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

			return simpleDateFormat.format(new Date());

		} catch (Exception e) {
			alert.prepareError("date formatter failed in event dispatcher", e).log();

		}
		return null;
	}

	public static Result dispatch(DataControllerRequest requestManager, DataControllerResponse responseManager,
			String eventType, String eventSubType, String producer, String statusId, String account, String user,
			JsonObject customParams) {
		Result result = new Result();
		JsonObject requestData = HelperMethods.convertDcRequestToJson(requestManager);
		JsonObject responseData = new JsonObject();
		JsonObject eventData = new JsonObject();
		JsonObject eventDetails = new JsonObject();
		JsonObject otherData = new JsonObject();
		String customerId = null;
		String authkey = "";
		String appsessionid = null;
		String companyLegalUnit =null;
		alert.prepareError("Event Dispatcher dispatch2: eventtype", eventType).log();
		
		if("LOGIN".equalsIgnoreCase(eventType)) {
			companyLegalUnit=HelperMethods.getLoginEntityId(requestManager);
	
		}else if("BATCH_ALERT".equalsIgnoreCase(eventType)) {
			if (customParams.has("accounts") && !customParams.get("accounts").isJsonNull()) {
			   JsonArray accounts = customParams.get("accounts").getAsJsonArray();
			   companyLegalUnit = accounts.get(0).getAsJsonObject().get("companyLegalUnit").getAsString();
	         }			
			if (customParams.has("transactions") && !customParams.get("transactions").isJsonNull()) {
				diagnostic.prepareDebug("##Event Dispatcher dispatch batch job transactions Inside if").log();
				   JsonArray accounts = customParams.get("transactions").getAsJsonArray();
				   companyLegalUnit = accounts.get(0).getAsJsonObject().get("companyLegalUnit").getAsString();
				   diagnostic.prepareDebug("##Event Dispatcher dispatch batch job transactions companyLegalUnit "+companyLegalUnit).log();
		     }
		}else {
			companyLegalUnit=getLegalEntityIdFromSessionOrCache(requestManager);
		}
		
		
		if(companyLegalUnit == null || companyLegalUnit.isEmpty())
		{
			companyLegalUnit=HelperMethods.getCompanyId(requestManager);
		}
		otherData = getCSRrelatedParams(requestManager, otherData, customParams);
		try {
			authkey = requestManager.getHeader(Constants.XKONYAUTHORIZATION.toString());
			appsessionid = getParamFromIToken(authkey, URLConstants.SESSIONID);
			if (customParams != null && customParams.get(Constants.SESSIONID.toString()) != null
					&& !customParams.get(Constants.SESSIONID.toString()).isJsonNull())
				otherData.addProperty(Constants.SESSIONID.toString(),
						customParams.get(Constants.SESSIONID.toString()).getAsString());

		} catch (Exception e) {
			alert.prepareError("error occured while fetching sessionid=", e).log();
		}
		try {
			eventDetails.addProperty(Constants.SESSION.toString(),
					getCustomReportingParams(
							requestManager.getServicesManager().getDeviceRequestData().getReportingParams(),
							requestManager).toString());

		} catch (Exception e) {
			alert.prepareError("Exception occured in fetching session", e).log();
		}
		customParams.addProperty(Constants.EVENTDISPATCHERSERVERDATE.toString(), getServerDate());
		customParams.addProperty(Constants.APPSESSIONID.toString(), appsessionid);
		eventData.add(Constants.REQUESTINPUT.toString(), requestData);
		eventData.add(Constants.RESPONSEOUTPUT.toString(), responseData);
		eventData.add(Constants.CUSTOMPARAMS.toString(), customParams);
		otherData.addProperty("companyLegalUnit", companyLegalUnit);
		otherData.addProperty("user", user);

		if (account != null) {
			otherData.addProperty(Constants.ACCOUNTNUMBER.toString(), account);
		}

		boolean iscustidneeded = true;
		if ((customParams != null && customParams.get("user") != null) || user != null) {
			iscustidneeded = false;
		}
		if (iscustidneeded && (customerId == null || customerId.equals(""))) {
			customerId = getParamFromIToken(authkey, URLConstants.PROVIDER_USER_ID);
		}

		try {
			if (customerId == null || customerId.equalsIgnoreCase("anonymous") || customerId.equals("")) {
				customerId = requestManager.getServicesManager().getIdentityHandler().getUserAttributes().get("user_id")
						.toString();
			}
		} catch (Exception e) {
			alert.prepareError("error while fetching fetching customerid from userattributes").log();
		}

		if (iscustidneeded && customerId != null && !customerId.equals("")
				&& !customerId.equalsIgnoreCase("schedulingengine")) {
			otherData.addProperty("customerId", customerId);
		}
		addExternalCommunicationData(otherData, customParams);
		eventDetails.addProperty(Constants.EVENTTYPE.toString(), eventType);
		eventDetails.addProperty(Constants.EVENTSUBTYPE.toString(), eventSubType);
		eventDetails.addProperty(Constants.STATUS.toString(), statusId);
		eventDetails.add(Constants.OTHERDATA.toString(), otherData);
		eventDetails.add(Constants.EVENTDATA.toString(), eventData);

		JsonArray events = new JsonArray();

		events.add(eventDetails);

		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();

		inputMap.put(Constants.EVENTS.toString(), events);
		inputMap.put(Constants.PRODUCER.toString(), producer);

		ServicesManager servicesManager = null;
		try {
			servicesManager = requestManager.getServicesManager();
		} catch (AppRegistryException e) {
			alert.prepareError("error in fetching services manager ", e).log();
		}
		String eventsString = events.toString();
		String derivedToken = deriveToken(servicesManager, eventsString);
		inputMap.put(Constants.TOKEN.toString(), derivedToken);
		if (servicesManager != null)
			result = callQueueMaster(servicesManager, inputMap, headerMap);
		return result;

	}

	private static void addExternalCommunicationData(JsonObject otherdata, JsonObject customparams) {
		if (otherdata == null || customparams == null)
			return;
		if (customparams.has(Constants.EXTERNALPHONE.toString()))
			otherdata.addProperty(Constants.EXTERNALPHONE.toString(),
					customparams.get(Constants.EXTERNALPHONE.toString()).getAsString());
		if (customparams.has(Constants.EXTERNALMAIL.toString()))
			otherdata.addProperty(Constants.EXTERNALMAIL.toString(),
					customparams.get(Constants.EXTERNALMAIL.toString()).getAsString());
		if (customparams.has(Constants.INCLUDEPREFERREDCONTACT.toString()))
			otherdata.addProperty(Constants.INCLUDEPREFERREDCONTACT.toString(),
					customparams.get(Constants.INCLUDEPREFERREDCONTACT.toString()).getAsString());

	}

	public static Result dispatch(DataControllerRequest requestManager, DataControllerResponse responseManager,
			String eventType, String eventSubType, String producer, String statusId, String account, String customerId,
			String appId, JsonObject customParams) {
		Result result = new Result();
		JsonObject requestData = HelperMethods.convertDcRequestToJson(requestManager);
		JsonObject responseData = new JsonObject();
		JsonObject eventData = new JsonObject();
		JsonObject eventDetails = new JsonObject();
		JsonObject otherData = new JsonObject();
		String authkey = "";
		String appsessionid = null;
		String companyLegalUnit =null;
		if("LOGIN".equalsIgnoreCase(eventType)) {
			companyLegalUnit=HelperMethods.getLoginEntityId(requestManager);
		}else {
			companyLegalUnit=getLegalEntityIdFromSessionOrCache(requestManager);
		}
		
		if(companyLegalUnit == null || companyLegalUnit.isEmpty())
		{
			companyLegalUnit=HelperMethods.getCompanyId(requestManager);
		}
		otherData = getCSRrelatedParams(requestManager, otherData, customParams);
		boolean iscustidneeded = true;
		try {
			authkey = requestManager.getHeader(Constants.XKONYAUTHORIZATION.toString());
			appsessionid = getParamFromIToken(authkey, URLConstants.SESSIONID);
			if (customParams != null && customParams.get(Constants.SESSIONID.toString()) != null
					&& !customParams.get(Constants.SESSIONID.toString()).isJsonNull())
				otherData.addProperty(Constants.SESSIONID.toString(),
						customParams.get(Constants.SESSIONID.toString()).getAsString());
		} catch (Exception e) {
			alert.prepareError("error occured while fetching sessionid=", e).log();
		}

		if (customParams != null && customParams.get("user") != null) {
			iscustidneeded = false;
		}
		if (iscustidneeded && (customerId == null || customerId.equals(""))) {
			customerId = getParamFromIToken(authkey, URLConstants.PROVIDER_USER_ID);
		}
		try {
			if (iscustidneeded
					&& (customerId == null || customerId.equalsIgnoreCase("anonymous") || customerId.equals(""))) {
				customerId = requestManager.getServicesManager().getIdentityHandler().getUserAttributes().get("user_id")
						.toString();
			}
		} catch (Exception e) {
			alert.prepareError("error while fetching fetching customerid from userattributes").log();
		}
		if (iscustidneeded)
			otherData.addProperty("customerId", customerId);
		else
			otherData.addProperty("user", customParams.get("user").getAsString());

		if (account != null)
			otherData.addProperty(Constants.ACCOUNTNUMBER.toString(), account);
		addExternalCommunicationData(otherData, customParams);
		if (StringUtils.isNotBlank(appId))
			otherData.addProperty("appId", appId);
		try {
			if (requestManager.getHeader(Constants.XKONYREPORTINGPARAMS.toString()) != null) {
				JsonObject reportingParamsObject = getCustomReportingParams(
						requestManager.getHeader(Constants.XKONYREPORTINGPARAMS.toString()), requestManager);
				eventDetails.addProperty(Constants.SESSION.toString(), reportingParamsObject.toString());
			}
		} catch (Exception e) {
			alert.prepareError("error in fetching x kony reporting params", e).log();
			otherData.addProperty("appId", appId);
		}
		otherData.addProperty("companyLegalUnit", companyLegalUnit);
		customParams.addProperty(Constants.EVENTDISPATCHERSERVERDATE.toString(), getServerDate());
		customParams.addProperty(Constants.APPSESSIONID.toString(), appsessionid);
		eventData.add(Constants.REQUESTINPUT.toString(), requestData);
		eventData.add(Constants.RESPONSEOUTPUT.toString(), responseData);
		eventData.add(Constants.CUSTOMPARAMS.toString(), customParams);

		eventDetails.addProperty(Constants.EVENTTYPE.toString(), eventType);
		eventDetails.addProperty(Constants.EVENTSUBTYPE.toString(), eventSubType);
		eventDetails.addProperty(Constants.STATUS.toString(), statusId);
		eventDetails.add(Constants.OTHERDATA.toString(), otherData);
		eventDetails.add(Constants.EVENTDATA.toString(), eventData);

		JsonArray events = new JsonArray();

		events.add(eventDetails);

		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();
		inputMap.put(Constants.EVENTS.toString(), events);
		ServicesManager servicesManager = null;
		try {
			servicesManager = requestManager.getServicesManager();
		} catch (AppRegistryException e) {

			alert.prepareError("Exception occurred while fetching Service Manager", e).log();
		}

		if (customParams.has(Constants.PRODUCER.toString())) {
			producer = customParams.get(Constants.PRODUCER.toString()).getAsString();
		}

		else {
			try {
				if (producer == null || producer.equals("")) {

					producer = servicesManager.getOperationData().getServiceId() + "/"
							+ requestManager.getServicesManager().getOperationData().getObjectId() + "/"
							+ requestManager.getServicesManager().getOperationData().getOperationId();

				}
				String className = Thread.currentThread().getStackTrace()[2].getClassName();
				String[] feilds = className.split("\\.");
				className = null;
				if (feilds.length > 0)
					className = feilds[feilds.length - 1];
				if (className != null)
					producer = producer + "/" + className;
			} catch (Exception e) {
				producer = "";
				alert.prepareError("unable to get producer").log();
			}
		}
		inputMap.put(Constants.PRODUCER.toString(), producer);
		String eventsString = events.toString();
		String derivedToken = deriveToken(servicesManager, eventsString);
		inputMap.put(Constants.TOKEN.toString(), derivedToken);
		if (servicesManager != null)
			result = callQueueMaster(servicesManager, inputMap, headerMap);
		return result;

	}

	private static JsonObject getCSRrelatedParams(DataControllerRequest requestManager, JsonObject otherData,
			JsonObject customParams) {
		try {
			if (customParams.has(Constants.CSRUSERNAME.toString()))
				otherData.addProperty(Constants.CSRUSERNAME.toString(),
						getStringFromJsonObject(customParams, Constants.CSRUSERNAME.toString(), true));

			if (customParams.has(Constants.CSRUSER.toString()))
				otherData.addProperty(Constants.CSRUSER.toString(),
						getStringFromJsonObject(customParams, Constants.CSRUSER.toString(), true));

			if (customParams.has(Constants.CSRROLE.toString()))
				otherData.addProperty(Constants.CSRROLE.toString(),
						getStringFromJsonObject(customParams, Constants.CSRROLE.toString(), true));

			if (customParams.has(Constants.CSRNAME.toString()))
				otherData.addProperty(Constants.CSRNAME.toString(),
						getStringFromJsonObject(customParams, Constants.CSRNAME.toString(), true));
			
			
			
		} catch (Exception e) {
			alert.prepareError("Error while fetching csr UserName").log();
		}
		return otherData;
	}

	private static JsonObject getCustomReportingParams(String reportingParamsString,
			FabricRequestManager requestManager) throws Exception {
		String decodedString = java.net.URLDecoder.decode(reportingParamsString, StandardCharsets.UTF_8.name());
		JsonObject reportingParamsObject = new Gson().fromJson(decodedString, JsonObject.class);
		UserAgent userAgent = null;
		JsonObject userAgentObj = null;
		try {
			String userAgentstr = reportingParamsObject.get("ua").getAsString();
			userAgentObj = getUserAgentStr(userAgentstr);
			userAgentstr = userAgentObj.get(URLConstants.USERAGENTSTRING).getAsString();
			userAgent = UserAgent.parseUserAgentString(userAgentstr);
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		try {
			JsonElement channelId = reportingParamsObject.get("chnl");
			String devicename = "";
			String os = "";
			if (channelId != null && (channelId.toString().equalsIgnoreCase("desktop")
					|| channelId.toString().equalsIgnoreCase("\"desktop\""))) {

				if (requestManager.getHeadersHandler().getHeader("User-Agent") != null && userAgent == null) {
					userAgentObj = getUserAgentStr(requestManager.getHeadersHandler().getHeader("User-Agent"));
					String userAgentStr = userAgentObj.get(URLConstants.USERAGENTSTRING).getAsString();
					userAgent = UserAgent.parseUserAgentString(userAgentStr);
				}
				if (userAgent != null) {
					StringBuilder sb = new StringBuilder();
					String version = userAgentObj.get(URLConstants.VERSION) != null
							? userAgentObj.get(URLConstants.VERSION).getAsString()
							: userAgent.getBrowserVersion().getVersion();
					sb.append(userAgent.getBrowser().getName()).append(" ").append(version);
					devicename = sb.toString();
					os = userAgent.getOperatingSystem().getName();
					reportingParamsObject.addProperty("os", os);
					reportingParamsObject.addProperty("dm", devicename);
				}
			} else {
				if (reportingParamsObject.get("") != null)
					reportingParamsObject.addProperty("os",
							reportingParamsObject.get("plat") + " " + reportingParamsObject.get("os"));
			}
			if (userAgent != null)
				reportingParamsObject.addProperty("ua", userAgent.toString());
			
			String ip = requestManager.getHeadersHandler().getHeader(URLConstants.CLIENT_IP);
			ip = StringUtils.isBlank(ip)
					? HelperMethods.getDecyptedClientIpAddressFromCache(requestManager)
					: HelperMethods.decryptClientIp(ip);
			reportingParamsObject.addProperty(URLConstants.IPADDRESS, ip);
			
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching OS", e).log();
		}
		return reportingParamsObject;

	}

	private static JsonObject getUserAgentStr(String userAgentstr) {
		JsonObject obj = new JsonObject();
		obj.addProperty(URLConstants.USERAGENTSTRING, userAgentstr);
		if (StringUtils.isBlank(userAgentstr))
			return obj;
		Pattern pattern = Pattern.compile(URLConstants.PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgentstr);
		if (matcher.find()) {
			String substr = userAgentstr.substring(matcher.start());
			obj.addProperty(URLConstants.VERSION, substr.substring(substr.indexOf("/") + 1));
			if (!substr.contains(URLConstants.REPLACE_WITH)) {
				obj.addProperty(URLConstants.USERAGENTSTRING, userAgentstr.replace(matcher.group(),
						matcher.group().replace(URLConstants.REPLACE, URLConstants.REPLACE_WITH)));
			}
		}
		return obj;
	}

	private static JsonObject getCustomReportingParams(String reportingParamsString,
			DataControllerRequest requestManager) throws Exception {
		String decodedString = java.net.URLDecoder.decode(reportingParamsString, StandardCharsets.UTF_8.name());
		JsonObject reportingParamsObject = new Gson().fromJson(decodedString, JsonObject.class);
		UserAgent parsedUserAgentString = null;
		JsonObject userAgentObj = null;
		try {
			String userAgent = reportingParamsObject.get("ua").getAsString();
			userAgentObj = getUserAgentStr(userAgent);
			userAgent = userAgentObj.get(URLConstants.USERAGENTSTRING).getAsString();
			parsedUserAgentString = UserAgent.parseUserAgentString(userAgent);
			if (parsedUserAgentString != null)
				reportingParamsObject.addProperty("ua", parsedUserAgentString.toString());
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		try {
			JsonElement channelId = reportingParamsObject.get("chnl");
			String devicename = "";
			String os = "";
			if (channelId != null && (channelId.toString().equalsIgnoreCase("desktop")
					|| channelId.toString().equalsIgnoreCase("\"desktop\""))) {
				UserAgent userAgent = parsedUserAgentString;
				if (userAgent != null) {
					StringBuilder sb = new StringBuilder();
					String version = userAgentObj.get(URLConstants.VERSION) != null
							? userAgentObj.get(URLConstants.VERSION).getAsString()
							: userAgent.getBrowserVersion().getVersion();
					sb.append(userAgent.getBrowser().getName()).append(" ").append(version);
					devicename = sb.toString();
					os = userAgent.getOperatingSystem().getName();
					reportingParamsObject.addProperty("os", os);
					reportingParamsObject.addProperty("dm", devicename);
				}
			} else {
				if (reportingParamsObject.get("") != null)
					reportingParamsObject.addProperty("os",
							reportingParamsObject.get("plat") + " " + reportingParamsObject.get("os"));
			}
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching OS", e).log();
		}
		try {
			String ip = HelperMethods.getDecyptedClientIpAddressFromCache(requestManager);
			reportingParamsObject.addProperty(URLConstants.IPADDRESS, ip);
		} catch (Exception e) {
			alert.prepareError("Exception occurred while fetching Ip Address ", e).log();
		}

		return reportingParamsObject;
	}

	private static String getParamFromIToken(String authkey, String tokenName) {
		TokenUtils tokenobj = new TokenUtils(authkey);
		return tokenobj.getValue(tokenName);
	}

	private static Result getErrorResult(Throwable ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg == null) {
			StackTraceElement ste = ex.getStackTrace()[0];
			errorMsg = ex.getClass().getName() + "' thrown in " + ste.getClassName() + "." + ste.getMethodName();
		}
		return getErrorResult(7777, errorMsg);
	}

	private static Result getErrorResult(int errorNumber, String errorMsg) {
		Result result = new Result();
		result.addParam(new Param("errornumber", Integer.toString(errorNumber)));
		result.addParam(new Param("errormsg", errorMsg));
		result.addParam(new Param("success", "false"));
		return result;
	}

	private static String getStringFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = getElementFromJsonObject(object, key, required);
		return element == null ? null : element.getAsString();
	}

	private static JsonElement getElementFromJsonObject(JsonObject object, String key, boolean required) {
		JsonElement element = object.get(key);
		if ((element == null) && (required)) {
			throw new IllegalArgumentException("Required attribute '" + key + "' was not present");
		}
		return element;
	}

	private static String deriveToken(ServicesManager servicesManager, String events) {
		ConfigurableParametersHelper configHelper = servicesManager.getConfigurableParametersHelper();
		String secret = configHelper.getServerProperty("QUEUEMASTER_SHARED_SECRET");
		if (secret == null || secret.length() == 0) {
			throw new RuntimeException("QueueMaster shared secret has not been configured!");
		}
		String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString(); // Hashing using Guava lib
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}

	public static JsonObject triggerEvent(String eventType, String eventSubType, String statusId, String producer,
			JsonObject requestObj, JsonObject responseObj, JsonObject otherData, JsonObject customParams,
			JsonObject reportingParamsObj, String customerId, String user, String accountId, String appId) {
		if (otherData == null)
			otherData = new JsonObject();
		if (customParams == null)
			customParams = new JsonObject();
		JsonObject result = new JsonObject();
		JsonObject eventData = new JsonObject();
		JsonObject eventDetails = new JsonObject();
		if (StringUtils.isNotBlank(appId)) {
			otherData.addProperty(Constants.APPID.toString(), appId);
		}
		try {
			eventDetails.addProperty(Constants.SESSION.toString(), reportingParamsObj.toString());
		} catch (Exception e) {
			alert.prepareError("Reporting params exception ", e).log();
		}
		customParams.addProperty(Constants.EVENTDISPATCHERSERVERDATE.toString(), getServerDate());
		eventData.add(Constants.REQUESTINPUT.toString(), requestObj);
		eventData.add(Constants.RESPONSEOUTPUT.toString(), responseObj);
		eventData.add(Constants.CUSTOMPARAMS.toString(), customParams);
		if (accountId != null) {
			otherData.addProperty(Constants.ACCOUNTNUMBER.toString(), accountId);
		}
		if (StringUtils.isNotBlank(user)) {
			otherData.addProperty("user", user);
		} else if (StringUtils.isNotBlank(customerId)) {
			otherData.addProperty("customerId", customerId);
		}
		eventDetails.addProperty(Constants.EVENTTYPE.toString(), eventType);
		eventDetails.addProperty(Constants.EVENTSUBTYPE.toString(), eventSubType);
		eventDetails.addProperty(Constants.STATUS.toString(), statusId);
		eventDetails.add(Constants.OTHERDATA.toString(), otherData);
		eventDetails.add(Constants.EVENTDATA.toString(), eventData);

		JsonArray events = new JsonArray();

		events.add(eventDetails);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put(Constants.EVENTS.toString(), events);
		inputMap.put(Constants.PRODUCER.toString(), producer);
		String eventsString = events.toString();
		String derivedToken = deriveToken(eventsString);
		inputMap.put(Constants.TOKEN.toString(), derivedToken);
		String response = HelperMethods.callInternalService(inputMap, "QueueMaster", "PushEventQueue", null);
		try {
			return new JsonParser().parse(response).getAsJsonObject();
		} catch (Exception e) {
			alert.prepareError("Exception ", e).log();
		}
		return result;

	}

	private static String deriveToken(String eventsString) {
		String secret = null;
		try {
			secret = EnvironmentConfigurationsHandler.getServerAppProperty("QUEUEMASTER_SHARED_SECRET");
		} catch (Exception e) {
			alert.prepareError("Unable to get QueueMaster shared secret ", e).log();
		}
		if (secret == null || secret.length() == 0) {
			throw new RuntimeException("QueueMaster shared secret has not been configured!");
		}
		String eventsHash = Hashing.sha512().hashString(eventsString, Charsets.UTF_8).toString(); // Hashing using Guava
																									// lib
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}

}
