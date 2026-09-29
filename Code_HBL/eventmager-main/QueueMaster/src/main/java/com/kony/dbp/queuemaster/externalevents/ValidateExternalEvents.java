package com.kony.dbp.queuemaster.externalevents;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.queuemaster.utils.Constants;
import com.kony.dbp.queuemaster.utils.ExternalEventsEnum;
import com.kony.dbp.queuemaster.utils.HelperMethods;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class ValidateExternalEvents implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");


	@Override
	public Object invoke(String arg0, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		JsonObject eventDetails = new JsonObject();
		Result res = null;
		try {
			String token = null;

			String eventtype = null;
			String eventsubtype = null;
			String eventstatus = null;
			String eventdata = null;
			String userdata = null;
			String producer = null;

			eventtype = dcRequest.getParameter(Constants.EVENTTYPE);
			eventsubtype = dcRequest.getParameter("eventSubtype");
			eventstatus = dcRequest.getParameter("eventStatus");
			eventdata = dcRequest.getParameter(Constants.EVENTDATA);
			userdata = dcRequest.getParameter("userData");
			producer = dcRequest.getParameter(Constants.PRODUCER);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> inputRequestmap = (HashMap<String, Object>) inputArray[1];
			if (eventtype == null || eventtype.equals("null"))
				eventtype = String.valueOf(inputRequestmap.get(Constants.EVENTTYPE));
			if (eventsubtype == null || eventsubtype.equals("null"))
				eventsubtype = String.valueOf(inputRequestmap.get("eventSubtype"));
			if (eventstatus == null || eventstatus.equals("null"))
				eventstatus = String.valueOf(inputRequestmap.get("eventStatus"));
			if (eventdata == null || eventdata.equals("null"))
				eventdata = String.valueOf(inputRequestmap.get(Constants.EVENTDATA));
			if (userdata == null || userdata.equals("null"))
				userdata = String.valueOf(inputRequestmap.get("userData"));
			if (producer == null || producer.equals("null"))
				producer = String.valueOf(inputRequestmap.get(Constants.PRODUCER));

			if (eventtype == null || eventsubtype == null || eventstatus == null || eventdata == null
					|| userdata == null) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INVALID);
			}
			if (eventtype.equals("null") || eventsubtype.equals("null") || eventstatus.equals("null")
					|| eventdata.equals("null") || userdata.equals("null")) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INVALID);
			}

			/* if eventdata contains script tag, this might be a security vulnerability */
			String scriptpattern = "<script\\b[^>]*>([\\s\\S]*?)<\\/script>";
			Pattern scriptregex = Pattern.compile(scriptpattern, Pattern.CASE_INSENSITIVE);
			Matcher m = scriptregex.matcher(eventdata);
			if (m.find()) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INSECURE);
			}

			JsonElement eventdatajsonelement = new JsonParser().parse(eventdata);
			JsonElement userdatadatajsonelement = new JsonParser().parse(userdata);

			if (!isUserDataValid(userdatadatajsonelement)) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INSECURE);
			}
			if (!isValidString(eventtype) || !isValidString(eventsubtype) || !isValidString(eventstatus)) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INSECURE);
			}
			eventDetails.addProperty(Constants.EVENTTYPE, eventtype);
			eventDetails.addProperty("eventSubType", eventsubtype);
			eventDetails.addProperty("status", eventstatus);
			if (userdatadatajsonelement != null && userdatadatajsonelement.isJsonObject())
				eventDetails.add("otherData", userdatadatajsonelement.getAsJsonObject());
			if (eventdatajsonelement.isJsonObject())
				eventDetails.add(Constants.EVENTDATA, eventdatajsonelement.getAsJsonObject());

			JsonArray events = new JsonArray();

			events.add(eventDetails);

			Map<String, Object> inputMap = new HashMap<>();
			token = deriveToken(events.toString(), dcRequest);
			if (token == null) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_NO_SHARESECRET);
			}
			inputMap.put("token", token);
			inputMap.put("events", events);
			if (producer == null || producer.equals("null") || producer.equals(""))
				inputMap.put(Constants.PRODUCER, "External Members");
			else
				inputMap.put(Constants.PRODUCER, producer);
			// calling queue master
			res = callQueueMasterService("QueueMaster", "PushEventQueue", inputMap, null, dcRequest);
		} catch (Exception e) {
			return HelperMethods.returnResult(false, e.getMessage());
		}

		return res;

	}

	public static Result callQueueMasterService(String serviceID, String operationID, Map<String, Object> inputmap,
			Map<String, Object> headermap, DataControllerRequest dcRequest) throws MiddlewareException {
		Result result = null;
		OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder().withServiceId(serviceID)
				.withOperationId(operationID).build();

		ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
				.withInputs(inputmap).withHeaders(headermap).build();
		result = serviceRequest.invokeServiceAndGetResult();
		return result;

	}

	public static String getEnvValue(String key, DataControllerRequest requestInstance) {
		try {
			ServicesManager serviceManager = requestInstance.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			return configurableParametersHelper.getServerProperty(key);
		} catch (Exception e) {

			alert.prepareError("Exception occured:", e).log();
		}
		return null;
	}

	private static boolean isValidString(String text) {
		if (text == null)
			return true;
		return text.matches("[a-zA-Z0-9_.]*");

	}

	private static boolean isUserDataValid(JsonElement userdatadatajsonelement) {
		if (userdatadatajsonelement != null && userdatadatajsonelement.isJsonObject()) {
			JsonObject userdatajsonobj = userdatadatajsonelement.getAsJsonObject();
			String data = null;
			if (userdatajsonobj.has("accountnumber")) {
				data = userdatajsonobj.get("accountnumber").getAsString();
				if (!isValidString(data))
					return false;
			}
			if (userdatajsonobj.has("customerId")) {
				data = userdatajsonobj.get("customerId").getAsString();
				if (!isValidString(data))
					return false;
			}
			if (userdatajsonobj.has("corecustomerid")) {
				data = userdatajsonobj.get("corecustomerid").getAsString();
				if (!isValidString(data))
					return false;
			}
			if (userdatajsonobj.has("user")) {
				data = userdatajsonobj.get("user").getAsString();
				if (!isValidString(data))
					return false;
			}
		}
		return true;
	}

	public static String deriveToken(String events, DataControllerRequest dcRequest) {

		String secret = getEnvValue("QUEUEMASTER_SHARED_SECRET", dcRequest);
		if (secret == null || secret.length() == 0) {
			alert.prepareError("AlertEngine shared secret has not been configured!").log();
			return secret;
		}
		String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString(); // Hashing using Guava lib
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}
}
