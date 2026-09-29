package com.dbp.externalevent.resource.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.externalevent.businessdelegate.api.UpdateEventConfigBusinessDelegate;
import com.dbp.externalevent.resource.api.PushExternalCampaignResource;
import com.dbp.externalevent.utils.ExternalEventsConstants;
import com.dbp.externalevent.utils.ExternalEventsEnum;
import com.dbp.externalevent.utils.HelperMethods;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class PushExternalCampaignResourceImpl implements PushExternalCampaignResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Result pushExternalCampaign(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) {
		JsonObject eventDetails = new JsonObject();
		Result res = new Result();
		try {

			UpdateEventConfigBusinessDelegate updateeventconfigBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(UpdateEventConfigBusinessDelegate.class);
			Map<String, String> eventconfdata = updateeventconfigBusinessDelegate.getEventConfigData();
			String eventcode = dcRequest.getParameter("eventCode");
			String eventdata = dcRequest.getParameter("eventData");
			@SuppressWarnings("unchecked")
			HashMap<String, Object> inputRequestmap = (HashMap<String, Object>) inputArray[1];
			if (eventcode == null || eventcode.equals("null"))
				eventcode = String.valueOf(inputRequestmap.get("eventCode"));
			if (eventdata == null || eventdata.equals("null"))
				eventdata = String.valueOf(inputRequestmap.get("eventData"));
			if (eventcode == null || eventdata == null) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INVALID);
			}
			if (eventcode.equals("null") || eventdata.equals("null")) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INVALID);
			}

			if (!eventconfdata.containsKey(eventcode)) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_NOT_CONFIGURE);
			}
			/* if eventdata contains script tag, this might be a security vulnerability */
			String scriptpattern = "<script\\b[^>]*>([\\s\\S]*?)<\\/script>";
			Pattern scriptregex = Pattern.compile(scriptpattern, Pattern.CASE_INSENSITIVE);
			Matcher m = scriptregex.matcher(eventdata);
			if (m.find()) {
				return HelperMethods.returnResult(false, ExternalEventsEnum.ERROR_INSECURE);
			}
			JsonElement eventdatajsonelement = new JsonParser().parse(eventdata);
			eventDetails.addProperty("eventCode", eventcode);
			if (eventdatajsonelement.isJsonObject())
				eventDetails.add("eventData", eventdatajsonelement.getAsJsonObject());
			JsonObject events = new JsonObject();
			events.add("events", eventDetails);
			EventData eventtag = new EventData(eventconfdata.get(eventcode), events.toString());
			dcRequest.getServicesManager().getEventNotifier().notify(eventtag);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
			res.addParam(new Param(ExternalEventsConstants.SUCCESS, ExternalEventsConstants.FALSE,
					ExternalEventsConstants.STRING));
			res.addParam(new Param(ExternalEventsConstants.DBPERRMSG, e.toString(), ExternalEventsConstants.STRING));
			return res;
		}
		res.addParam(new Param(ExternalEventsConstants.SUCCESS, ExternalEventsConstants.TRUE,
				ExternalEventsConstants.STRING));
		return res;
	}

}
