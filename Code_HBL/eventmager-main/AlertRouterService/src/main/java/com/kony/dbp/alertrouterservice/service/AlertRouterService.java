
package com.kony.dbp.alertrouterservice.service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kony.dbp.alertrouterservice.email.Email;
import com.kony.dbp.alertrouterservice.email.EmailServiceRequest;
import com.kony.dbp.alertrouterservice.email.Emails;
import com.kony.dbp.alertrouterservice.email.Recipient;
import com.kony.dbp.alertrouterservice.email.Recipients;
import com.kony.dbp.alertrouterservice.messagedata.MessageData;
import com.kony.dbp.alertrouterservice.sms.Message;
import com.kony.dbp.alertrouterservice.sms.Messages;
import com.kony.dbp.alertrouterservice.sms.SmsServiceRequest;
import com.kony.dbp.alertrouterservice.eventheader.EventHeader;
import com.kony.dbp.alertrouterservice.userdata.UserData;
import com.kony.dbp.alertrouterservice.utils.Config;
import com.kony.dbp.alertrouterservice.utils.ErrorCodeEnum;
import com.kony.dbp.alertrouterservice.utils.HelperMethods;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.kony.dbp.alertrouterservice.pojo.AlertMessageTypeConfig;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



public class AlertRouterService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	private static  ConcurrentHashMap<String,AlertMessageTypeConfig> msgtypemap = null;
	private static String schemaname = null;
	private static String ALERTS_CORETYPE = null;

	// Main entry point for invocation of the Fabric service.
	public Object invoke(String methodID, Object[] maps, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		Result result = null;
		
		if(schemaname == null)
		{
			schemaname = Config.getValue("DBX_SCHEMA_NAME");
		}
		if(ALERTS_CORETYPE == null)
		{
			ALERTS_CORETYPE = Config.getValue("ALERTS_CORETYPE");
		}
		

		if(methodID.equalsIgnoreCase("routeAlert"))
		{
			 synchronized(this)
		     {
				if(msgtypemap == null)
				{
					loadConfig(request);
				}
		    }
			result = routeAlert(maps, request);
		}
		else if(methodID.equalsIgnoreCase("reloadConfig"))
		{
			loadConfig(request);
			result = HelperMethods.returnResult(true, "Success");
		}
		
		return result;

	}


	private Result routeAlert(Object[] maps, DataControllerRequest request) {
		try {

			if (diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug("Inside Invoke Method of Alert Service").log();
			}
			Thread.currentThread().setContextClassLoader(AlertRouterService.class.getClassLoader());
			Map<String, String> inputParams = null;

			if (maps != null) {

				if (maps.length > 1) {

					inputParams = (Map<String, String>) maps[1];

				}

			}
			if (diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug("inputParams : " + inputParams).log();
			}
			
			String userdata_string = null;
			String eventheader_string = null;
			String messagedata_string = null;
			String variableParams_string = null;

			if (inputParams != null) {
				userdata_string = inputParams.get("userdata");
				eventheader_string = inputParams.get("eventheader");
				messagedata_string = inputParams.get("messagedata");
				variableParams_string = inputParams.get("variableParams");
			}

			if (diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug("userdata : " + userdata_string).log();
				diagnostic.prepareDebug("eventheader : " + eventheader_string).log();
				diagnostic.prepareDebug("messagedata : " + messagedata_string).log();
				diagnostic.prepareDebug("variableParams : " + variableParams_string).log();
			}

			ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);

			if (userdata_string == null || (userdata_string != null && userdata_string.length() == 0)) {
				return HelperMethods.returnResult(false, ErrorCodeEnum.ERROR_MISSING);
			}
			
			if (eventheader_string == null || (eventheader_string != null && eventheader_string.length() == 0)) {
				return HelperMethods.returnResult(false, ErrorCodeEnum.ERROR_MISSING);
			}

			if (messagedata_string == null || (messagedata_string != null && messagedata_string.length() == 0)) {
				return HelperMethods.returnResult(false, ErrorCodeEnum.ERROR_MISSING);
			}

			if (variableParams_string == null
					|| (variableParams_string != null && variableParams_string.length() == 0)) {
				return HelperMethods.returnResult(false, ErrorCodeEnum.ERROR_MISSING);
			}

			UserData userData = mapper.readValue(userdata_string, UserData.class);
			EventHeader eventHeader = mapper.readValue(eventheader_string, EventHeader.class);
			MessageData messageData = mapper.readValue(messagedata_string, MessageData.class);

			if (diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug("MessageType : " + eventHeader.getMessageType()).log();
				diagnostic.prepareDebug("msgtypemap : " + msgtypemap).log();
			}
			
			if(eventHeader.getMessageType() != null && eventHeader.getMessageType().length() > 0)
			{
				if( msgtypemap.containsKey(eventHeader.getMessageType()))
				{
					routeInputRequest(request, mapper, variableParams_string, userData, messageData,eventHeader);
				}
				else
				{
					alert.prepareError("No Msg Type Config , ignoring the event").log();
				}
			}
			else
			{
				routeInputRequest(request, mapper, variableParams_string, userData, messageData,eventHeader);
			}
			

		} catch (Exception e) {
			//e.printStackTrace();
			alert.prepareError(e.toString()).log();
			return HelperMethods.returnResult(false, e.getMessage(), "160012");
		}

		return HelperMethods.returnResult(true, "Success");
	}

	private void routeInputRequest(DataControllerRequest request, ObjectMapper mapper, String variableParams_string,
			UserData userData, MessageData messageData, EventHeader eventHeader) throws MiddlewareException, JsonProcessingException, Exception {
		
		boolean passVariableData = true;
		if (eventHeader.getEventType() == null || eventHeader.getEventSubtype() == null || (eventHeader.getEventType() != null && eventHeader.getEventType().length() == 0) || (eventHeader.getEventSubtype() != null && eventHeader.getEventSubtype().length() == 0)) 
		{
			
			if (eventHeader.getMessageType() != null && eventHeader.getMessageType().length() > 0) {
				if (diagnostic.isDebugEnabled()) {
					diagnostic.prepareDebug("Extracting event details").log();
				}
				eventHeader = getEventDetailsfromMsgType(eventHeader);
				passVariableData = msgtypemap.get(eventHeader.getMessageType()).isPassVariableData();
				
				if (diagnostic.isDebugEnabled()) {
					diagnostic.prepareDebug("eventHeader : " + eventHeader).log();
				}
			}
			else
			{
				alert.prepareError("Message Type is null or empty").log();
			}
			if (eventHeader.getEventType() == null || eventHeader.getEventSubtype() == null || (eventHeader.getEventType() != null && eventHeader.getEventType().length() == 0) || (eventHeader.getEventSubtype() != null && eventHeader.getEventSubtype().length() == 0)) 
			{
				alert.prepareError("INVALIDEVENTTYPE and INVALIDEVENTSUBTYPE").log();
				eventHeader.setEventType("INVALIDEVENTTYPE");
				eventHeader.setEventSubtype("INVALIDEVENTSUBTYPE");
			}
		}
		if (eventHeader.getEventStatus() == null || (eventHeader.getEventStatus() != null && eventHeader.getEventStatus().length() == 0)) {
			eventHeader.setEventStatus("SID_EVENT_SUCCESS");
		}

		if (userData.getCustomerId() != null && userData.getCustomerId().length() > 0) {
			
			constructPayLoadAndInvokeAlertsEngine(request, variableParams_string, userData,eventHeader,passVariableData);

		} else {

			if (userData.getCorecustomerid() != null && userData.getCorecustomerid().length() > 0) {
				
				boolean isInfinityUser = checkforInfinityUser(userData.getCorecustomerid(), request);

				if (isInfinityUser) {
					
					constructPayLoadAndInvokeAlertsEngine(request, variableParams_string, userData,eventHeader,passVariableData);
					
				} else {
					
					routeToKMS(request, mapper, userData, messageData, eventHeader,variableParams_string);
				}
			} else {
				
				routeToKMS(request, mapper, userData, messageData, eventHeader,variableParams_string);
			}

		}
	}

	private void routeToKMS(DataControllerRequest request, ObjectMapper mapper, UserData userData,
			MessageData messageData, EventHeader eventHeader, String variableParams_string) throws Exception {

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("Routing to KMS").log();
		}
		if (messageData.getEmail() != null) {
			
			if(messageData.getEmail().getMessage() == null || (messageData.getEmail().getMessage() != null && messageData.getEmail().getMessage().length() == 0))
			{
				messageData.getEmail().setMessage(variableParams_string);
			}
			
			if(messageData.getEmail().getSubject() == null || (messageData.getEmail().getSubject() != null && messageData.getEmail().getSubject().length() == 0))
			{
				if(eventHeader.getMessageType() != null && eventHeader.getMessageType().length() > 0)
				{
					messageData.getEmail().setSubject(msgtypemap.get(eventHeader.getMessageType()).getSubject());
				}
			}
			constructPayloadAndInvokeEmailService(request, mapper, userData, messageData, eventHeader);
			
		} else if (messageData.getPhone() != null) {
			if(messageData.getPhone().getMessage() == null || (messageData.getPhone().getMessage() != null && messageData.getPhone().getMessage().length() == 0))
			{
				messageData.getPhone().setMessage(variableParams_string);
			}
			constructPayloadAndInvokeSMSService(request, mapper, userData, messageData, eventHeader);
		}

	}

	private EventHeader populateEventDetailsfromMsgType(EventHeader eventHeader, DataControllerRequest request)
			throws Exception {

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("$filter", "messagetype eq " + eventHeader.getMessageType());
		Result AlertMessageTypeConfig_res = callOtherService("EventManagerDBService",
				replaceSchemaName("{schema_name}_alertmessagetypeconfig_get", schemaname), inputMap, null, request);

		if (diagnostic.isDebugEnabled()) {
			for (String param : AlertMessageTypeConfig_res.getIdOfAllDatasets()) {
				diagnostic.prepareDebug(param + " : " + AlertMessageTypeConfig_res.getDatasetById(param)).log();
			}
		}

		JSONArray AlertMessageTypeConfig_json = ResultToJSON
				.convertDataset(AlertMessageTypeConfig_res.getDatasetById("alertmessagetypeconfig"));

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("AlertMessageTypeConfig json : " + AlertMessageTypeConfig_json).log();
		}
		ArrayList<AlertMessageTypeConfig> alertMessageTypeConfig = new Gson()
				.fromJson(AlertMessageTypeConfig_json.toString(), new TypeToken<List<AlertMessageTypeConfig>>() {
				}.getType());

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(alertMessageTypeConfig.toString()).log();
		}

		if (alertMessageTypeConfig.size() > 0) {
			eventHeader.setEventType(alertMessageTypeConfig.get(0).getAlerttype());
			eventHeader.setEventSubtype(alertMessageTypeConfig.get(0).getAlertsubtype());
		}

		return eventHeader;
	}
	
	private EventHeader getEventDetailsfromMsgType(EventHeader eventHeader)
			throws Exception {

		AlertMessageTypeConfig data = msgtypemap.get(eventHeader.getMessageType());
		eventHeader.setEventType(data.getAlerttype());
		eventHeader.setEventSubtype(data.getAlertsubtype());

		return eventHeader;
	}
	
	private void loadConfig( DataControllerRequest request) throws Exception {
		
		Result AlertMessageTypeConfig_res = callOtherService("EventManagerDBService",
				replaceSchemaName("{schema_name}_alertmessagetypeconfig_get", schemaname), null, null, request);
		
		if(msgtypemap == null)
		{
			msgtypemap = new ConcurrentHashMap<String,AlertMessageTypeConfig>();
		}
		else
		{
			msgtypemap.clear();
		}

		for(Record rec : AlertMessageTypeConfig_res.getDatasetById("alertmessagetypeconfig").getAllRecords())
		{
			AlertMessageTypeConfig obj = new AlertMessageTypeConfig();
			obj.setMessagetype(rec.getParamValueByName("messagetype"));
			obj.setAlerttype(rec.getParamValueByName("alerttype"));
			obj.setAlertsubtype(rec.getParamValueByName("alertsubtype"));
			obj.setPassVariableData(Boolean.valueOf(rec.getParamValueByName("passVariableData")));
			obj.setSubject(rec.getParamValueByName("subject"));
			msgtypemap.put(rec.getParamValueByName("messagetype"), obj);
		}

		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("msgtypemap : " + msgtypemap).log();
		}
		
		
	}

	private boolean checkforInfinityUser(String corecustomerid, DataControllerRequest request)
			throws Exception {

		Map<String, Object> inputMap = new HashMap<>();

		String whereclause = "BackendId  eq " + corecustomerid + " and BackendType eq "
				+ ALERTS_CORETYPE;
		inputMap.put("$filter", whereclause);

		Result backendidentifier_res = callOtherService("EventManagerDBService", replaceSchemaName("{schema_name}_backendidentifier_get", schemaname),
				inputMap, null, request);

		if (diagnostic.isDebugEnabled()) {
			for (String param : backendidentifier_res.getIdOfAllDatasets()) {
				diagnostic.prepareDebug(param + " : " + backendidentifier_res.getDatasetById(param)).log();
			}
		}

		JSONArray backendidentifier_json = ResultToJSON
				.convertDataset(backendidentifier_res.getDatasetById("backendidentifier"));

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("backendidentifier json : " + backendidentifier_json).log();
		}

		return backendidentifier_json.length() > 0 ? true : false;
	}

	private void constructPayloadAndInvokeSMSService(DataControllerRequest request, ObjectMapper mapper,
			UserData userData, MessageData messageData,EventHeader eventHeader) throws Exception {

		JsonObject smsServiceRequest_json = constructSMSPayload(mapper, messageData);

		JsonObject logparam_json = constructLogParams(userData,eventHeader);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("inputparams", smsServiceRequest_json);
		inputMap.put("logparams", logparam_json);

		Result result = callOtherService("notificationInvokeService", "sendSms", inputMap, null, request);

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("sms result : " + result).log();
			for (String param : result.getNameOfAllParams()) {
				diagnostic.prepareDebug(param + " : " + result.getParamValueByName(param)).log();
			}
		}

	}

	private JsonObject constructSMSPayload(ObjectMapper mapper, MessageData messageData)
			throws JsonProcessingException {

		SmsServiceRequest smsServiceRequest = new SmsServiceRequest();
		Messages messages = new Messages();
		Message message = new Message();

		message.setPriorityService("true");

		com.kony.dbp.alertrouterservice.sms.Recipients recipients_sms = new com.kony.dbp.alertrouterservice.sms.Recipients();
		com.kony.dbp.alertrouterservice.sms.Recipient recipient_sms = new com.kony.dbp.alertrouterservice.sms.Recipient();

		recipient_sms.setMobile(messageData.getPhone().getContact());

		List<com.kony.dbp.alertrouterservice.sms.Recipient> recipient_sms_lst = new ArrayList<com.kony.dbp.alertrouterservice.sms.Recipient>();
		recipient_sms_lst.add(recipient_sms);
		recipients_sms.setRecipient(recipient_sms_lst);

		message.setRecipients(recipients_sms);
		message.setContent(messageData.getPhone().getMessage());

		messages.setMessage(message);
		smsServiceRequest.setMessages(messages);

		String messages_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(smsServiceRequest);

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(messages_json).log();
		}

		JsonObject smsServiceRequest_json = new JsonObject();
		smsServiceRequest_json.add("smsServiceRequest", new JsonParser().parse(messages_json));

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(smsServiceRequest_json.toString()).log();
		}
		return smsServiceRequest_json;
	}

	private void constructPayloadAndInvokeEmailService(DataControllerRequest request, ObjectMapper mapper,
			UserData userData, MessageData messageData,EventHeader eventHeader) throws Exception {

		JsonObject emailServiceRequest_json = constructEmailPayload(mapper, messageData);

		JsonObject logparam_json = constructLogParams(userData,eventHeader);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("inputparams", emailServiceRequest_json);
		inputMap.put("logparams", logparam_json);

		Result result = callOtherService("notificationInvokeService", "sendEmail", inputMap, null, request);

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("email result : " + result).log();

			for (String param : result.getNameOfAllParams()) {
				diagnostic.prepareDebug(param + " : " + result.getParamValueByName(param)).log();
			}
		}
	}

	private JsonObject constructLogParams(UserData userData,EventHeader eventHeader) {
		JsonObject logparam_json = new JsonObject();

		logparam_json.addProperty("eventType", eventHeader.getEventType());
		logparam_json.addProperty("eventSubtype", eventHeader.getEventSubtype());
		logparam_json.addProperty("coreCustomerId", userData.getCorecustomerid());
		logparam_json.addProperty("eventStatus", eventHeader.getEventStatus());
		logparam_json.addProperty("isAlertsEngine", "false");

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("logparams : " + logparam_json).log();
		}
		return logparam_json;
	}

	private JsonObject constructEmailPayload(ObjectMapper mapper, MessageData messageData)
			throws JsonProcessingException {

		EmailServiceRequest emailServiceRequest = new EmailServiceRequest();
		Emails emails = new Emails();
		Email email = new Email();
		Recipients recipients = new Recipients();
		Recipient recipient = new Recipient();

		recipient.setEmailId(messageData.getEmail().getContact());
		recipient.setType("TO");

		List<Recipient> recipient_list = new ArrayList<Recipient>();
		recipient_list.add(recipient);
		recipients.setRecipient(recipient_list);
		email.setRecipients(recipients);
		email.setSubject(messageData.getEmail().getSubject());
		email.setContent(messageData.getEmail().getMessage());
		email.setPriority("true");
		email.setSenderName(Config.getValue("ALERT_EMAIL_SENDER_NAME"));

		emails.setEmail(email);
		emailServiceRequest.setEmails(emails);

		String email_json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(emailServiceRequest);

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(email_json).log();
		}
		JsonObject emailServiceRequest_json = new JsonObject();

		emailServiceRequest_json.add("emailServiceRequest", new JsonParser().parse(email_json));

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(emailServiceRequest_json.toString()).log();
		}
		return emailServiceRequest_json;
	}

	private void constructPayLoadAndInvokeAlertsEngine(DataControllerRequest request, String variableParams_string,
			UserData userData,EventHeader eventHeader,boolean passVariableData) throws Exception {
		
		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("Routing to QueueMaster").log();
		}

		JsonArray events = constructEventsPayload(variableParams_string, userData,eventHeader,passVariableData);

		String token = deriveToken(events.toString(), request);

		Map<String, Object> inputMap = new HashMap<>();
		inputMap.put("token", token);
		inputMap.put("events", events);
		inputMap.put("producer", "External Members");

		Result result = callOtherService("QueueMaster", "PushEventQueue", inputMap, null, request);

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("event result : " + result).log();

			for (String param : result.getNameOfAllParams()) {
				diagnostic.prepareDebug(param + " : " + result.getParamValueByName(param)).log();
			}
		}

	}

	private JsonArray constructEventsPayload(String variableParams_string, UserData userData,EventHeader eventHeader,boolean passVariableData) {
		JsonObject eventDetails = new JsonObject();

		eventDetails.addProperty("eventType", eventHeader.getEventType());
		eventDetails.addProperty("eventSubType", eventHeader.getEventSubtype());
		eventDetails.addProperty("status", eventHeader.getEventStatus());

		JsonObject otherData = new JsonObject();
		otherData.addProperty("customerId", userData.getCustomerId());
		otherData.addProperty("corecustomerid", userData.getCorecustomerid());
		otherData.addProperty("accountnumber", userData.getAccountnumber());
		otherData.addProperty("appid", "RETAIL_AND_BUSINESS_BANKING");
		otherData.addProperty("companyLegalUnit", userData.getCompanyId());
		eventDetails.add("otherData", otherData);

		JsonObject eventData = new JsonObject();
		
		if(!passVariableData)
		{
			JsonObject customParams = new JsonObject();
			customParams.addProperty("MESSAGETYPE", variableParams_string);
			eventData.add("customParams", customParams);
		}
		else
		{
			eventData.add("customParams", new JsonParser().parse(variableParams_string));
		}
		eventDetails.add("eventData", eventData);

		JsonArray events = new JsonArray();

		events.add(eventDetails);

		if (diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(events.toString()).log();
		}
		return events;
	}

	public static Result callOtherService(String serviceID, String operationID, Map<String, Object> inputmap,
			Map<String, Object> headermap, DataControllerRequest dcRequest) throws Exception {

		Result result = null;
		try
		{
			OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder().withServiceId(serviceID)
					.withOperationId(operationID).build();
			
			ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
					.withInputs(inputmap).withHeaders(headermap).build();
			result = serviceRequest.invokeServiceAndGetResult();
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : " + operationID + " ; " + e.getMessage() );
		}
		return result;
	
	}

	public static String deriveToken(String events, DataControllerRequest dcRequest) {

		String secret = Config.getValue("QUEUEMASTER_SHARED_SECRET");
		if (secret == null || secret.length() == 0) {
			alert.prepareError("AlertEngine shared secret has not been configured!").log();
			return secret;
		}
		String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString(); // Hashing using Guava lib
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}
	
	public static String replaceSchemaName(String operationid, String schemaname) {
	    if (operationid == null || schemaname == null)
	      return operationid; 
	    if (operationid.contains("{schema_name}"))
	      operationid = operationid.replace("{schema_name}", schemaname); 
	    return operationid;
	  }
}
