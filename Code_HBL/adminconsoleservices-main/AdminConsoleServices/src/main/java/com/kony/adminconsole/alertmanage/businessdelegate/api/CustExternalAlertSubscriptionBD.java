package com.kony.adminconsole.alertmanage.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.service.alertmanagement.staging.util.AlertSubscription;

public interface CustExternalAlertSubscriptionBD extends BusinessDelegate {
	
	
	/**
	 * @param alertSubObj   DTO having input alert subscriptions
	 * @param alertExtlReferenceMap Map of alertSubType and external system reference
	 * @param externalAlertGroupMap  Map of external alerts and its groups
	 * @return
	 */
	public Map<String, String> registerExtnlSubcription(AlertSubscription alertSubObj,
			Map<String, String> alertExtlReferenceMap, Map<String, String> externalAlertGroupMap, String legalEntityId) ;

}
