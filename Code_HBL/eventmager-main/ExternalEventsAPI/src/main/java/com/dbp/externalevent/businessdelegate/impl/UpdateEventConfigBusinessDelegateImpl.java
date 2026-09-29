package com.dbp.externalevent.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.externalevent.businessdelegate.api.UpdateEventConfigBusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class UpdateEventConfigBusinessDelegateImpl implements UpdateEventConfigBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	private static Map<String, String> eventmap = new HashMap<>();

	public UpdateEventConfigBusinessDelegateImpl() {
		updateEventConfigData();
	}

	@Override
	public boolean updateEventConfigData() {

		DataControllerRequest dcr = null;
		Map<String, Object> reqin = new HashMap<>();
		Result res = null;
		try {
			res = DBPServiceInvocationWrapper.invokeServiceAndGetResult("ExternalEventsDBService", null,
					"dbxdb_eventtopicconfiguration_get", reqin, null, dcr);

			if (res == null)
				return false;
			Dataset ds = res.getDatasetById("eventtopicconfiguration");
			eventmap.clear();
			for (Record rec : ds.getAllRecords()) {
				if (rec.getParam("eventCode") != null && rec.getParam("topic") != null) {
					String eventcode = rec.getParam("eventCode").getValue();
					String topic = rec.getParam("topic").getValue();
					eventmap.put(eventcode, topic);
				}

			}
		} catch (Exception e) {
			alert.prepareError("Error in updating eventcode data", e).log();
			return false;
		}
		return true;

	}

	@Override
	public Map<String, String> getEventConfigData() {
		return eventmap;
	}

}
