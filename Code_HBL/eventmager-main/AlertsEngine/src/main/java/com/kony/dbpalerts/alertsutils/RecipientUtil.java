package com.kony.dbpalerts.alertsutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbpalerts.alertsprocess.PreProcessAlert;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public final class RecipientUtil {

	private static final String CUSTOMER_ID = "customerId";
	private static final String RECIPIENT_SERVICE_CONFIGURATION_ERRMSG = "Recipient Service is not configured properly";
	private static final String EMPTY_RECIPIENTLIST_ERRROR = "No recipients returned for this event";
	private static final String RECIPIENT_SERVICE_FAILED_ERRMSG = "Recipient service failed with errmsg %s and code %s";
	private static final String RECIPIENT_SERVICE_RESPONSE_ERRMSG1 = "Recipient service for id %s failed with error %s";
	private static final String RESPONSE_CUSTOMERS = "customers";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	public enum ALERTRECIPIENTTYPE_TABLE {
		ID("id"),SERVICE_NAME("servicename"),
		OPERATION_NAME("operationname"),INPUT_PARAM_MAPPING("inputparamsmapping");

		String columnName;

		private ALERTRECIPIENTTYPE_TABLE(String name) {
			this.columnName = name;
		}	

		public String getColumnName() {
			return columnName;
		}	
	}	

	public static List<RecipientTypeDTO> getRecipientList(List<Event> events) {
		List<RecipientTypeDTO> recipientList = new ArrayList<>();
		Map<String, Map<String, String>> recipientMasterData = StaticDataHolder.getRecipientTypes();
		if(!recipientMasterData.isEmpty()) {
			for (Event event : events) {
				try {
					if(StringUtils.isNotBlank(event.getRecipientType())) {
						Map<String, String> recipientMap = recipientMasterData.get(event.getRecipientType()); 	 
						if(StringUtils.isNotBlank(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.SERVICE_NAME.getColumnName())) && 
							StringUtils.isNotBlank(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.OPERATION_NAME.getColumnName()))) {		    		
							recipientList.add(getRecipientDTO(event, recipientMap));
						}else if((StringUtils.isNotBlank(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.SERVICE_NAME.getColumnName())) && 
								StringUtils.isBlank(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.OPERATION_NAME.getColumnName()))) ||
								(StringUtils.isBlank(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.SERVICE_NAME.getColumnName())) && 
										StringUtils.isNotBlank(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.OPERATION_NAME.getColumnName())))) {
							event.setRecipientErrMsg(RECIPIENT_SERVICE_CONFIGURATION_ERRMSG);
						}
					}
				} catch (Exception e) {
					alert.prepareError("RecipientMasterData processing failed", e).log();
					event.setRecipientErrMsg("RecipientMasterData processing failed");					
				}
			}
		}else {
			alert.prepareError("RecipientMasterData is empty").log();
			events.forEach(event -> event.setRecipientErrMsg("RecipientMasterData is not read properly"));           
		}
		return recipientList;
	}

	public static RecipientTypeDTO getRecipientDTO(Event event, Map<String, String> recipientMap) {
		RecipientTypeDTO recipDTO = new RecipientTypeDTO();
		recipDTO.setEventID(event.getEventid());
		recipDTO.setRecipientType(event.getRecipientType());
		recipDTO.setServiceName(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.SERVICE_NAME.getColumnName()));
		recipDTO.setOperationName(recipientMap.get(ALERTRECIPIENTTYPE_TABLE.OPERATION_NAME.getColumnName())); 
		recipDTO.setInputMap(getRequestParamsForRecipient(event, 
				recipientMap.get(ALERTRECIPIENTTYPE_TABLE.INPUT_PARAM_MAPPING.getColumnName())));
		return recipDTO;
	}

	public static Map<String, Object> getRequestParamsForRecipient(Event event, String inputMapStr ) {
		if(diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(String.format("Recipient inoutmapping string %s",  inputMapStr)).log();	
		}
		JsonObject paramMap = new JsonParser().parse(inputMapStr).getAsJsonObject();
		JsonElement eventjson = new JsonParser().parse(event.getEventJson());
		Map<String, String> fieldsfromeventdata = new HashMap<>();
		Map<String, String> fieldsfromotherdata = new HashMap<>();
		PreProcessAlert.getAllPairsFromJson(eventjson, fieldsfromeventdata, AlertConstants.EVENTDATA);
		PreProcessAlert.getAllPairsFromJson(eventjson, fieldsfromotherdata, AlertConstants.OTHERDATA);

		Map<String,Object> inputMap = new HashMap<>();
		for ( Entry<String, JsonElement> entrySet : paramMap.entrySet()) {
			String value = fieldsfromotherdata.containsKey(entrySet.getValue().getAsString().toLowerCase()) ?
					fieldsfromotherdata.get(entrySet.getValue().getAsString().toLowerCase()) 
					: fieldsfromeventdata.get(entrySet.getValue().getAsString().toLowerCase());
					//if value is null, might not require as it could be optional param
					if(diagnostic.isDebugEnabled()) {
						diagnostic.prepareDebug(String.format("key and value are %s and %s",  entrySet.getKey(), entrySet.getValue())).log();	
					}
					inputMap.put(entrySet.getKey(), value);					
		}
		return inputMap;
	}	


	public static Map<String, Object> generateRecipientOrchPayload(List<RecipientTypeDTO> recipientList) {
		Map<String, Object> requestParameters = new HashMap<>();
		StringBuilder recipientInfo = new StringBuilder();
		for (RecipientTypeDTO recipientDTO : recipientList) {
			String recipientStr = null;
			try {
				recipientStr = JSONUtils.stringify(recipientDTO);
				if(diagnostic.isDebugEnabled()) {
					diagnostic.prepareDebug(String.format("Recipient service input %s %s", 
							recipientDTO.getEventID() , recipientStr)).log();	
				}
			} catch (Exception e) {
				diagnostic.prepareDebug("Error in parsing recipientInfo object", e).log();
			}
			if (recipientStr == null)
				continue;
			recipientInfo.append(recipientStr + AlertConstants.ALERT_LOOP_SEPARATOR);
		}
		if(diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(String.format("Recipient service input %s ",  recipientInfo.toString())).log();	
		}

		requestParameters.put(AlertConstants.INPUT_RECIPIENTINFO, recipientInfo.toString());
		requestParameters.put(AlertConstants.EVENTID, recipientList.stream()
				.map(RecipientTypeDTO::getEventID).collect(Collectors.joining(AlertConstants.ALERT_LOOP_SEPARATOR)));
		requestParameters.put(AlertConstants.LOOP_SEPARATOR, AlertConstants.ALERT_LOOP_SEPARATOR);
		requestParameters.put(AlertConstants.LOOP_COUNT, recipientList.size());
		return requestParameters;
	}


	public static Result callOrchService(List<RecipientTypeDTO> recipientReqList) {
		try {
			Map<String, Object> requestParameters = generateRecipientOrchPayload(recipientReqList);

			return AlertsUtils.callInternalService(requestParameters, AlertConstants.ALERTS_ORCH_SERVICE,
					AlertConstants.ALERTS_RECIPIENT_ORCH_OPERATION, null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured in calling service", e).log();
		}
		return null;
	}

	public static List<Event> fetchRecipientListAndUpdateEvents(List<Event> events) {
		List<Event> updatedEvents =  events;
		List<RecipientTypeDTO> recipientRequestList = RecipientUtil.getRecipientList(events);
		if(diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug("RecipientRequestList size is :" + recipientRequestList.size()).log();
		}		
		if(!recipientRequestList.isEmpty()) {
			Result res = RecipientUtil.callOrchService(recipientRequestList);
			if(diagnostic.isDebugEnabled()) {
				diagnostic.prepareDebug(String.format("Recipient orchestration Response : %s" , ResultToJSON.convert(res))).log();
			}

			if(res == null ||  ( StringUtils.isNotBlank(res.getParamValueByName(AlertConstants.ERRCODE)) ||
					StringUtils.isNotBlank(res.getParamValueByName(AlertConstants.DBPERRCODE))) ||
							res.getDatasetById(AlertConstants.LOOP_DATASET) == null)  {				
				procesAndSetError(events, recipientRequestList, res);
			} else {
				Map<String, List<List<String>>> successEventsByRecipients = getSuccessfulRecipientsEvents(res);				 
				Map<String, List<String>> errorRecipientListByEvent = getFailureRecipientEvents(res);   				 
				List<String> erroredEventsId = errorRecipientListByEvent.keySet().stream().collect(Collectors.toList());
				updatedEvents = processEventRecipients(events, successEventsByRecipients,
							errorRecipientListByEvent,erroredEventsId);				
			}
		}
		return updatedEvents;
	}

	public static void procesAndSetError(List<Event> events, List<RecipientTypeDTO> recipientRequestList, Result res) {
		String errCoderes = null;
		StringBuilder errmsgBuilder = null;
		if (res != null) {
			errmsgBuilder = new StringBuilder();
			if (StringUtils.isNotBlank(res.getParamValueByName(AlertConstants.DBPERRMSG))) {
				errmsgBuilder.append(AlertConstants.DBPERRMSG)
				.append(res.getParamValueByName(AlertConstants.DBPERRMSG)).append(" ");
			}
			if (StringUtils.isNotBlank(res.getParamValueByName(AlertConstants.ERRMSG))) {
				errmsgBuilder.append(AlertConstants.ERRMSG).append(res.getParamValueByName(AlertConstants.ERRMSG));
			}
			errCoderes = StringUtils.isNotBlank(res.getParamValueByName(AlertConstants.DBPERRCODE))
					? res.getParamValueByName(AlertConstants.DBPERRCODE)
							: res.getParamValueByName(AlertConstants.ERRCODE);

			alert.prepareError( String.format(RECIPIENT_SERVICE_FAILED_ERRMSG ,
															errmsgBuilder , errCoderes) ).log();
		}			
		String  errCode = errCoderes != null ? errCoderes : "Uknown errorCode";
		String errmsg = errmsgBuilder != null ? errmsgBuilder.toString() : "Unknown error message";
		List<String> errRecipientEventList = recipientRequestList.stream()
				.map(RecipientTypeDTO :: getEventID).collect(Collectors.toList());
		events.stream().filter(e -> errRecipientEventList.contains(e.getEventid())).forEach(
				errevent -> errevent.setRecipientErrMsg(String.format(RECIPIENT_SERVICE_FAILED_ERRMSG,
						errmsg , errCode )));
	}

	public static Map<String, List<String>> getFailureRecipientEvents(Result res) {
		return res.getDatasetById(AlertConstants.LOOP_DATASET)
				.getAllRecords().stream()
				.filter(r2 -> (StringUtils.isNotBlank(r2.getParamValueByName(AlertConstants.ERRMSG))
						|| StringUtils.isNotBlank(r2.getParamValueByName(AlertConstants.DBPERRMSG))) )
				.collect(
						Collectors.groupingBy(getEventIdAsString,
								Collectors.mapping(getErrmsgFromLoopRecord, Collectors.toList())));

	}

	public static Map<String, List<List<String>>> getSuccessfulRecipientsEvents(Result res) {
		return res.getDatasetById(AlertConstants.LOOP_DATASET)
				.getAllRecords().stream()
				.filter(r2 -> StringUtils.isBlank( r2.getParamValueByName(AlertConstants.ERRMSG)))
				.collect(
						Collectors.groupingBy(getEventIdAsString,
								Collectors.mapping(getRecipientListFromLoopRecord, Collectors.toList())));		
	}

	public static List<Event> processEventRecipients(List<Event> events,
			Map<String, List<List<String>>> recipientListByEvent,
			Map<String, List<String>> errorRecipientListByEvent, List<String> erroredEventsId) {
		List<Event> newrecipientEventList = new ArrayList<>();
		List<String> deletedEventList = new ArrayList<>();		
		for (Event currEvent : events) {
			try {
				if(erroredEventsId.contains(currEvent.getEventid())) {
				  currEvent.setRecipientErrMsg(String.format(RECIPIENT_SERVICE_RESPONSE_ERRMSG1,
							currEvent.getRecipientType(),errorRecipientListByEvent.get(currEvent.getEventid()).get(0)));
				}else if(recipientListByEvent.get(currEvent.getEventid()) != null) {
					List<String> recipientList = recipientListByEvent.get(currEvent.getEventid()).get(0);
					recipientList = recipientList.stream().distinct().collect(Collectors.toList());
					if (recipientList != null && !recipientList.isEmpty()) {							
						for (int i = 0; i < recipientList.size(); i++) {
							Event recipientEvent = getRecipientEvent(currEvent);
							recipientEvent.setCustomerid(recipientList.get(i));
							newrecipientEventList.add(recipientEvent);						
						}
						deletedEventList.add(currEvent.getEventid());
					}else {
						// if no recipients then setting the error message as 
						currEvent.setRecipientErrMsg(EMPTY_RECIPIENTLIST_ERRROR);
					}
				}
			} catch (Exception e) {
				currEvent.setRecipientErrMsg("Error while processing the events");
			}
		}
		// Removes the events for which recipients events are created successfully
		if(! deletedEventList.isEmpty()) {
			events = events.stream().filter(e -> !deletedEventList.contains(e.getEventid())).collect(Collectors.toList());
		}
		if(diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(String.format("newrecipientEventList is %s" ,
					newrecipientEventList.stream().map(Event::getCustomerid).collect(Collectors.joining(",")))).log();
		}
		events.addAll(newrecipientEventList);
		if(diagnostic.isDebugEnabled()) {
			diagnostic.prepareDebug(String.format("events is %s", events.stream().map(Event::getEventid).collect(Collectors.joining(",")))).log();
		}
		return events;
	}

	public static final Function<Record,String> getEventIdAsString = (Record rec) ->  rec.getParamValueByName(AlertConstants.EVENTID);

	public static final Function<Record,List<String>> getRecipientListFromLoopRecord = (Record rec) -> {
		if(rec.getDatasetById(RESPONSE_CUSTOMERS) == null) {
			return new ArrayList<String>();
		}
		return rec.getDatasetById(RESPONSE_CUSTOMERS).getAllRecords() != null
				? rec.getDatasetById(RESPONSE_CUSTOMERS).getAllRecords().stream().
						map(r2 -> r2.getParamValueByName(CUSTOMER_ID)).collect(Collectors.toList()) : new ArrayList<String>();

	};	

	public static final Function<Record,String> getErrmsgFromLoopRecord = (Record rec) -> {
		return StringUtils.isNotBlank(rec.getParamValueByName(AlertConstants.DBPERRMSG)) ? 
				rec.getParamValueByName(AlertConstants.DBPERRMSG) : rec.getParamValueByName(AlertConstants.ERRMSG);
	};

	public static Event getRecipientEvent(Event inputevent) {
		Event event = new Event();
		event.setEventid(inputevent.getEventid());
		event.setAlertcategory(inputevent.getAlertcategory());
		event.setAlerttypestatus(inputevent.getAlerttypestatus());
		event.setAlertsubtypestatus(inputevent.getAlertsubtypestatus());
		event.setAlertcategorystatus(inputevent.getAlertcategorystatus());
		event.setAlertconditionid(inputevent.getAlertconditionid());
		event.setRecipientType(inputevent.getRecipientType());
		event.setAttributeid(inputevent.getAttributeid());
		event.setIsglobal(inputevent.getIsglobal());
		event.setIsaccountLevel(inputevent.getIsaccountLevel());
		event.setChemail(inputevent.getChemail());
		event.setChnotification(inputevent.getChnotification());
		event.setChpush(inputevent.getChpush());
		event.setChsms(inputevent.getChsms());
		event.setValue1(inputevent.getValue1());
		event.setValue2(inputevent.getValue2());
		// Filling from input
		event.setAlerttype(inputevent.getAlerttype());
		event.setAlertsubtype(inputevent.getAlertsubtype());
		event.setCommstatusid(inputevent.getCommstatusid());
		event.setEventJson(inputevent.getEventJson());
		event.setLanguagecode(inputevent.getLanguagecode());
		event.setAccountid(inputevent.getAccountid());
		event.setCorecustomerid(inputevent.getCorecustomerid());

		return event;		
	}

	private RecipientUtil() {
	}
}

