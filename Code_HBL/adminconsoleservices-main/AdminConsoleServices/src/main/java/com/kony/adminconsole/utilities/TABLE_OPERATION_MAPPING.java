package com.kony.adminconsole.utilities;

import static com.kony.adminconsole.utilities.ACConstants.ACCOUNT_TYPE_ID;
import static com.kony.adminconsole.utilities.ACConstants.ALERTCATEGORYCHANNEL_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERTSUBTYPEACCOUNTTYPE_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERTSUBTYPEAPP_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERTSUBTYPECHANNEL_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERTSUBTYPECUSTOMERTYPE_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERTSUBTYPETEXT_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERTTYPECHANNEL_TN;
import static com.kony.adminconsole.utilities.ACConstants.ALERT_SUB_TYPE_ID;
import static com.kony.adminconsole.utilities.ACConstants.ALERT_TYPE_ID;
import static com.kony.adminconsole.utilities.ACConstants.APP_ID;
import static com.kony.adminconsole.utilities.ACConstants.CHANNEL_ID;
import static com.kony.adminconsole.utilities.ACConstants.CHANNEL_ID_CAPITAL;
import static com.kony.adminconsole.utilities.ACConstants.CUSTOMERALERTCHANNEL_TN;
import static com.kony.adminconsole.utilities.ACConstants.CUSTOMERALERTFREQUENCY_TN;
import static com.kony.adminconsole.utilities.ACConstants.CUSTOMERVIEWALERTCONFIGURATION_TN;
import static com.kony.adminconsole.utilities.ACConstants.CUSTOMER_TYPE_ID;
import static com.kony.adminconsole.utilities.ACConstants.ID_LOWERCASE;
import static com.kony.adminconsole.utilities.ACConstants.LANGUAGE_CODE;
import static com.kony.adminconsole.utilities.ACConstants.ALERT_FREQUENCY_ID;
public enum TABLE_OPERATION_MAPPING {		

	ALERTSUBTYPEAPP(ALERTSUBTYPEAPP_TN, ALERT_SUB_TYPE_ID, APP_ID, 
			ServiceURLEnum.ALERTSUBTYPEAPP_CREATE,	ServiceURLEnum.ALERTSUBTYPEAPP_UPDATE,
			ServiceURLEnum.ALERTSUBTYPEAPP_DELETE, ServiceURLEnum.ALERTSUBTYPEAPP_READ),

	ALERTSUBTYPEACCOUNTTYPE(ALERTSUBTYPEACCOUNTTYPE_TN, ALERT_SUB_TYPE_ID, ACCOUNT_TYPE_ID,
			ServiceURLEnum.ALERTSUBTYPEACCOUNTTYPE_CREATE, ServiceURLEnum.ALERTSUBTYPEACCOUNTTYPE_UPDATE,
			ServiceURLEnum.ALERTSUBTYPEACCOUNTTYPE_DELETE, ServiceURLEnum.ALERTSUBTYPEACCOUNTTYPE_READ),

	ALERTSUBTYPECUSTOMERTYPE(ALERTSUBTYPECUSTOMERTYPE_TN, ALERT_SUB_TYPE_ID, CUSTOMER_TYPE_ID,
			ServiceURLEnum.ALERTSUBTYPECUSTOMERTYPE_CREATE, ServiceURLEnum.ALERTSUBTYPECUSTOMERTYPE_UPDATE,
			ServiceURLEnum.ALERTSUBTYPECUSTOMERTYPE_DELETE, ServiceURLEnum.ALERTSUBTYPECUSTOMERTYPE_READ),

	ALERTSUBTYPECHANNEL(ALERTSUBTYPECHANNEL_TN, ALERT_SUB_TYPE_ID, CHANNEL_ID,
			ServiceURLEnum.ALERTSUBTYPECHANNEL_CREATE, ServiceURLEnum.ALERTSUBTYPECHANNEL_UPDATE,
			ServiceURLEnum.ALERTSUBTYPECHANNEL_DELETE, ServiceURLEnum.ALERTSUBTYPECHANNEL_READ),

	ALERTSUBTYPETEXT(ALERTSUBTYPETEXT_TN, ALERT_SUB_TYPE_ID, LANGUAGE_CODE,
			ServiceURLEnum.ALERTSUBTYPETEXT_CREATE, ServiceURLEnum.ALERTSUBTYPETEXT_UPDATE,
			ServiceURLEnum.ALERTSUBTYPETEXT_DELETE,	ServiceURLEnum.ALERTSUBTYPETEXT_READ),

	CUSTOMERVIEWALERTCONFIGURATION(CUSTOMERVIEWALERTCONFIGURATION_TN, ID_LOWERCASE,null,
			ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_CREATE, ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_UPDATE,
			ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_DELETE, ServiceURLEnum.CUSTOMERVIEWALERTCONFIGURATION_READ),

	ALERTCATEGORYCHANNEL(ALERTCATEGORYCHANNEL_TN, "AlertCategoryId", CHANNEL_ID_CAPITAL,
			ServiceURLEnum.ALERTCATEGORYCHANNEL_CREATE, ServiceURLEnum.ALERTCATEGORYCHANNEL_UPDATE,
			ServiceURLEnum.ALERTCATEGORYCHANNEL_DELETE, ServiceURLEnum.ALERTCATEGORYCHANNEL_READ),

	ALERTTYPECHANNEL(ALERTTYPECHANNEL_TN, ALERT_TYPE_ID, CHANNEL_ID,
			ServiceURLEnum.ALERTTYPECHANNEL_CREATE, ServiceURLEnum.ALERTTYPECHANNEL_UPDATE,
			ServiceURLEnum.ALERTTYPECHANNEL_DELETE, ServiceURLEnum.ALERTTYPECHANNEL_READ),
	
	CUSTOMERALERTFREQUENCY(CUSTOMERALERTFREQUENCY_TN, 
			"customerId,alertCategoryId,alertTypeId,alertSubTypeId,accountId,accountType" , ALERT_FREQUENCY_ID,
			ServiceURLEnum.CUSTOMERALERTFREQUENCY_CREATE, ServiceURLEnum.CUSTOMERALERTFREQUENCY_UPDATE,
			ServiceURLEnum.CUSTOMERALERTFREQUENCY_DELETE, ServiceURLEnum.CUSTOMERALERTFREQUENCY_READ),
	
	CUSTOMERALERTCHANNEL(CUSTOMERALERTCHANNEL_TN , 
			"customerId,alertCategoryId,alertTypeId,alertSubTypeId,accountId,accountType" , CHANNEL_ID,
			ServiceURLEnum.CUSTOMERALERTCHANNEL_CREATE, ServiceURLEnum.CUSTOMERALERTCHANNEL_UPDATE,
			ServiceURLEnum.CUSTOMERALERTCHANNEL_DELETE, ServiceURLEnum.CUSTOMERALERTCHANNEL_READ);
	 
	 
	

	private final String tableName;
	private final String alertpkId1;
	private final String compositePKid;	
	private final ServiceURLEnum createServiceName;
	private final ServiceURLEnum updateServiceName;
	private final ServiceURLEnum deleteServiceName;
	private final ServiceURLEnum readServiceName;

	private TABLE_OPERATION_MAPPING(String tableName, String alertpkId1, String compositePKid, ServiceURLEnum createServiceName,
			ServiceURLEnum updateServiceName, ServiceURLEnum deleteServiceName, ServiceURLEnum readServiceName) {
		this.tableName = tableName;
		this.alertpkId1 = alertpkId1;
		this.compositePKid = compositePKid;
		this.createServiceName = createServiceName;
		this.updateServiceName = updateServiceName;
		this.deleteServiceName = deleteServiceName;
		this.readServiceName = readServiceName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getAlertpkId1() {
		return alertpkId1;
	}

	public String getCompositePKid() {
		return compositePKid;
	}

	public ServiceURLEnum getCreateServiceName() {
		return createServiceName;
	}

	public ServiceURLEnum getUpdateServiceName() {
		return updateServiceName;
	}

	public ServiceURLEnum getDeleteServiceName() {
		return deleteServiceName;
	}

	public ServiceURLEnum getReadServiceName() {
		return readServiceName;
	}

}

