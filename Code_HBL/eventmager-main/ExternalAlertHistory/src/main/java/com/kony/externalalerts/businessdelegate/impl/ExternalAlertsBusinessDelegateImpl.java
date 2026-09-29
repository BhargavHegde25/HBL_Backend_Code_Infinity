package com.kony.externalalerts.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonParser;
import com.kony.externalalerts.businessdelegate.api.ExternalAlertsBusinessDelegate;
import com.kony.externalalerts.util.ExternalAlertsConstants;
import com.kony.externalalerts.util.HelperMethods;
import com.kony.externalalerts.util.StaticDataHolder;

public class ExternalAlertsBusinessDelegateImpl implements ExternalAlertsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@Override
	public String getSFAccountId(String customerId) {
		String partyId = getPartyIdFromDb(customerId);
		diagnostic.prepareDebug("partyId " + partyId).log();
		if (StringUtils.isBlank(partyId))
			return null;
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put(ExternalAlertsConstants.PARTYID, partyId);
		try {

			String responseString = HelperMethods.callInternalService(inputmap,
					ExternalAlertsConstants.SFGETACCOUNTIDSERVICE, ExternalAlertsConstants.SFGETACCOUNTIDOPER, null);
			diagnostic.prepareDebug("responseString from sf" + responseString).log();
			return new JsonParser().parse(responseString).getAsJsonObject().get(ExternalAlertsConstants.SALESFORCEID)
					.getAsString();

		} catch (Exception e) {
			alert.prepareError("Exception occurred ", e).log();
		}
		return null;
	}

	private String getPartyIdFromDb(String customerId) {

		String coreType = StaticDataHolder.getCoreType();
		Map<String, Object> inputmap = new HashMap<>();
		inputmap.put(ExternalAlertsConstants.FILTER, getFilterForPartyId(customerId, coreType));
		try {
			String responseString = HelperMethods.callInternalService(inputmap, ExternalAlertsConstants.DBSERVICE,
					HelperMethods.replaceSchemaName(ExternalAlertsConstants.BACKENDIDGET,
							StaticDataHolder.getSchemaName()),
					null);
			diagnostic.prepareDebug("response from backendidentifier " + responseString).log();
			return new JsonParser().parse(responseString).getAsJsonObject().get("backendidentifier").getAsJsonArray()
					.get(0).getAsJsonObject().get(ExternalAlertsConstants.BACKENDID).getAsString();
		} catch (Exception e) {
			alert.prepareError("Exception occured ", e).log();
		}
		return null;
	}

	private String getFilterForPartyId(String customerId, String coreType) {
		return "Customer_id" + ExternalAlertsConstants.EQUAL + "'" + customerId + "'" + " and "
				+ ExternalAlertsConstants.BACKENDTYPE + ExternalAlertsConstants.EQUAL + "'" + coreType + "'";

	}

	@Override
	public void pushToSf(List<Map<String, Object>> input, String sfAccountId) {
		try {
			for (Map<String, Object> map : input) {
				Map<String, Object> inputmap = getInputMapForSF(map, sfAccountId);
				if (inputmap.isEmpty())
					continue;
				diagnostic.prepareDebug("inputmap for sf " + inputmap).log();

				String response = HelperMethods.callInternalService(inputmap, ExternalAlertsConstants.PUSHTOSFSERVCEID,
						ExternalAlertsConstants.PUSHTOSFOPERID, null);
				diagnostic.prepareDebug("response from sf push api " + response).log();

			}

		} catch (Exception e) {
			alert.prepareError("Exception occured ", e).log();
		}

	}

	private Map<String, Object> getInputMapForSF(Map<String, Object> map, String sfAccountId) {
		Map<String, Object> inputmap = new HashMap<>();
		if (map.containsKey(ExternalAlertsConstants.STATUS)
				&& (map.get(ExternalAlertsConstants.STATUS).equals("SID_DELIVERY_SUBMITTED")
						|| map.get(ExternalAlertsConstants.STATUS).equals("SID_DELIVERYFAILED"))) {
			inputmap.put("FinServ__Account__c", sfAccountId);
			inputmap.put("ExternalUserId__c", map.get("Customer_Id"));
			inputmap.put("AlertGroupId__c", map.get("AlertTypeId"));
			inputmap.put("AlertGroupName__c", map.get("AlertGroupName"));
			inputmap.put("AlertId__c", map.get("AlertSubTypeId"));
			inputmap.put("AlertName__c", map.get("AlertName"));
			inputmap.put("AlertStatus__c", map.get("AlertStatusId"));
			inputmap.put("Channel__c", map.get("ChannelId"));
			inputmap.put("DeliveryStatus__c", map.get(ExternalAlertsConstants.STATUS));
			inputmap.put("Subject__c", map.get("Subject"));
			inputmap.put("FinServ__Message__c", map.get("Message"));
			inputmap.put("DeliveryReference__c", map.get("ReferenceNumber"));
			inputmap.put("DispatchDate__c", map.get("DispatchDate"));
			inputmap.put("ErrorMessage__c", map.get("ErrorMessage"));
		}
		return inputmap;
	}

}
