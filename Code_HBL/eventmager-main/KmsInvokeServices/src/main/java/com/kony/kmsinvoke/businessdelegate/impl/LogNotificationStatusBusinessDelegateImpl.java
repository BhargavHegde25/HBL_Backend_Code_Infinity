package com.kony.kmsinvoke.businessdelegate.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.google.gson.JsonObject;
import com.kony.kmsinvoke.businessdelegate.api.LogNotificationStatusBusinessDelegate;
import com.kony.kmsinvoke.util.JsonParsingEngine;
import com.kony.kmsinvoke.util.KmsInvokeConstants;

public class LogNotificationStatusBusinessDelegateImpl implements LogNotificationStatusBusinessDelegate {
	private static final String ALERTHISTORYFEILDS = "Customer_Id,coreCustomerId,ReferenceNumber,ErrorMessage,createdby,modifiedby";
	private static String[] alerthhistory = ALERTHISTORYFEILDS.split(",");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");

	@Override
	public Map<String, Object> insertToAlertHistory(String channel, JsonObject inputparams, JsonObject kmsResponse,
			JsonObject logparams) {
		Map<String, Object> input = new HashMap<>();
		try {
			String alertsubtype = JsonParsingEngine.getJsonObjects(logparams, KmsInvokeConstants.EVENTSUBTYPE, true);
			String alerttype = JsonParsingEngine.getJsonObjects(logparams, KmsInvokeConstants.EVENTTYPE, true);
			String alertid = UUID.randomUUID().toString().replace("-", "");
			String status = "";
			String refid = JsonParsingEngine.getJsonObjects(kmsResponse, KmsInvokeConstants.REFERENCEID, false);
			String message = JsonParsingEngine.getJsonObjects(kmsResponse, KmsInvokeConstants.DBPERRMSG, false);
			if ((refid == null || refid.equals("") || refid.equals("-1")))
				status = KmsInvokeConstants.SID_DELIVERYFAILED;
			else {
				status = KmsInvokeConstants.SID_DELIVERY_SUBMITTED;
				message = "";
			}
			String flag = "0";
			Calendar calendar = Calendar.getInstance();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			String customerid = JsonParsingEngine.getJsonObjects(logparams, KmsInvokeConstants.CUSTOMERID, false);
			String username = JsonParsingEngine.getJsonObjects(logparams, KmsInvokeConstants.USERNAME, false);
			String coreCustomerId = JsonParsingEngine.getJsonObjects(logparams, KmsInvokeConstants.CORECUSTOMERID,
					false);
			String commstatusid = KmsInvokeConstants.ALERTSTATUSID;
			input.put("id", alertid);
			input.put("AlertSubTypeId", alertsubtype);
			input.put("AlertTypeId", alerttype);
			input.put("AlertStatusId", commstatusid);
			input.put("ChannelId", channel);
			input.put("Status", status);
			input.put("DispatchDate", timestamp);
			input.put("createdts", timestamp);
			input.put("lastmodifiedts", timestamp);
			input.put("synctimestamp", timestamp);
			input.put("softdeleteflag", flag);
			input.put("Message", inputparams.toString());
			for (int i = 0; i < alerthhistory.length; i++) {
				if (alerthhistory[i].equals("Customer_Id")) {
					if (customerid != null && !customerid.equals("")) {
						input.put("Customer_Id", customerid);
					}
				} else if (alerthhistory[i].equals("coreCustomerId")) {
					if (coreCustomerId != null && !coreCustomerId.equals("")) {
						input.put("coreCustomerId", coreCustomerId);
					}
				} else if (alerthhistory[i].equals("modifiedby")) {
					if (username != null && !username.equals("")) {
						input.put("modifiedby", username);
					}
				} else if (alerthhistory[i].equals("createdby")) {
					if (username != null && !username.equals("")) {
						input.put("createdby", username);
					}
				} else if (alerthhistory[i].equals("ReferenceNumber")) {
					if (refid != null && !refid.equals("")) {
						input.put("ReferenceNumber", refid);
					}
				} else if (alerthhistory[i].equals("ErrorMessage") && message != null && !message.equals("")) {
					input.put("ErrorMessage", message);
				}

			}

		} catch (Exception e) {
			alert.prepareError("error occurred ", e).log();

		}
		return input;
	}
}
