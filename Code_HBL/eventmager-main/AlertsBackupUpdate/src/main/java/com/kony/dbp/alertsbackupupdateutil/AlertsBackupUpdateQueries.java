package com.kony.dbp.alertsbackupupdateutil;

public class AlertsBackupUpdateQueries {
	private AlertsBackupUpdateQueries() {
	}

	public static final String[] queries = {

			"update dbxalerttype set dbxalerttype.isAccountLevel = (select dbxalertcategory.accountLevel from dbxalertcategory  where  dbxalerttype.AlertCategoryId = dbxalertcategory.id),lastmodifiedts= CURRENT_TIMESTAMP, modifiedby = 'infinityuser',defaultFrequencyId='DAILY', defaultFrequencyTime='10:00:00'  where dbxalerttype.id !='';",

			"update alertsubtype \r\n" + 
			"set \r\n" + 
			"alertsubtype.isAccountLevel = (select dbxalerttype.isAccountLevel from dbxalerttype  where  alertsubtype.AlertTypeId = dbxalerttype.id), \r\n" + 
			"alertsubtype.isGlobal= (select dbxalerttype.IsGlobal from dbxalerttype  where  alertsubtype.AlertTypeId = dbxalerttype.id),\r\n" + 
			"alertsubtype.attributeId= (select dbxalerttype.AttributeId from dbxalerttype  where  alertsubtype.AlertTypeId = dbxalerttype.id),\r\n" + 
			"alertsubtype.alertConditionId= (select dbxalerttype.AlertConditionId from dbxalerttype  where  alertsubtype.AlertTypeId = dbxalerttype.id),\r\n" + 
			"alertsubtype.value1= (select dbxalerttype.Value1 from dbxalerttype  where  alertsubtype.AlertTypeId = dbxalerttype.id),\r\n" + 
			"alertsubtype.value2= (select dbxalerttype.Value2 from dbxalerttype  where  alertsubtype.AlertTypeId = dbxalerttype.id),\r\n" + 
			"alertsubtype.lastmodifiedts = CURRENT_TIMESTAMP,\r\n" + 
			"alertsubtype.modifiedby='infinityuser'\r\n" + 
			"where \r\n" + 
			"alertsubtype.id !='';",

			"delete from customeralertchannel  where customerId !='';",

			"insert into \r\n" + 
			"customeralertchannel(customerId,alertCategoryId,alertTypeId,alertSubTypeId,channelId,accountId,accountType,createdby)\r\n" + 
			" (SELECT\r\n" + 
			"customeralertcategorychannel.Customer_id, \r\n" + 
			"customeralertcategorychannel.AlertCategoryId ,\r\n" + 
			"dbxalerttype.id as alertTypeId,\r\n" + 
			"alertsubtype.id as alertSubTypeId,\r\n" + 
			"customeralertcategorychannel.ChannelId,\r\n" + 
			"customeralertcategorychannel.AccountId,\r\n" + 
			"customeralertcategorychannel.AccountType,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from  \r\n" + 
			"customeralertcategorychannel\r\n" + 
			"join dbxalerttype on dbxalerttype.AlertCategoryId= customeralertcategorychannel.AlertCategoryId\r\n" + 
			"join alertsubtype on alertsubtype.AlertTypeId=dbxalerttype.id);",

			"insert into dbxcustomeralertentitlement(Customer_id,AlertCategoryId, AlertTypeId,alertSubTypeId,AccountId, AccountType,value1, value2,createdby)\r\n" + 
			"(select dbxcustomeralertentitlement.Customer_id,\r\n" + 
			"dbxalerttype.AlertCategoryId,\r\n" + 
			"dbxalerttype.id as AlertTypeId,\r\n" + 
			"alertsubtype.id as ALertSubTypeId,\r\n" + 
			"dbxcustomeralertentitlement.AccountId,\r\n" + 
			"dbxcustomeralertentitlement.AccountType,\r\n" + 
			"dbxcustomeralertentitlement.Value1,\r\n" + 
			"dbxcustomeralertentitlement.Value2,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from dbxcustomeralertentitlement\r\n" + 
			"join \r\n" + 
			"dbxalerttype on dbxcustomeralertentitlement.AlertTypeId = dbxalerttype.id\r\n" + 
			"join\r\n" + 
			"alertsubtype on dbxcustomeralertentitlement.AlertTypeId= alertsubtype.AlertTypeId \r\n" + 
			"WHERE dbxcustomeralertentitlement.alertCategoryId = '*' and dbxcustomeralertentitlement.alertSubTypeId = '*');",

			"DELETE FROM dbxcustomeralertentitlement\r\n" + "WHERE alertCategoryId = '*' and alertSubTypeId = '*' ;",

			"delete from alertsubtypeapp where appId !='';",

			"insert into alertsubtypeapp(appId,alertSubTypeId,createdby)\r\n" + 
			"(select alerttypeapp.AppId as appId,\r\n" + 
			"alertsubtype.id as alertSubTypeId,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from alerttypeapp\r\n" + 
			"join alertsubtype on alerttypeapp.AlertTypeId = alertsubtype.AlertTypeId);",

			"delete from alertsubtypeaccounttype where accountTypeId != '';",

			"insert into alertsubtypeaccounttype (accountTypeId,alertSubTypeId,createdby)\r\n" + 
			"select \r\n" + 
			"alerttypeaccounttype.AccountTypeId as accountTypeId,\r\n" + 
			"alertsubtype.id as alertSubTypeId,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from alerttypeaccounttype\r\n" + 
			"join alertsubtype on alerttypeaccounttype.AlertTypeId = alertsubtype.AlertTypeId;",

			"delete from alertsubtypecustomertype where customerTypeId != '';",

			"insert into alertsubtypecustomertype(customerTypeId,alertSubTypeId,createdby)\r\n" + 
			"(select alerttypecustomertype.CustomerTypeId,\r\n" + 
			"alertsubtype.id as AlertSubType,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from \r\n" + 
			"alerttypecustomertype\r\n" + 
			"join alertsubtype on alerttypecustomertype.AlertTypeId = alertsubtype.AlertTypeId);",

			"delete from alerttypechannel where alertTypeId !='';",

			"insert into alerttypechannel(channelId,alertTypeId,createdby) \r\n" + 
			"(select alertcategorychannel.ChannelID,\r\n" + 
			"dbxalerttype.id,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from alertcategorychannel\r\n" + 
			"join dbxalerttype on alertcategorychannel.AlertCategoryId = dbxalerttype.AlertCategoryId);\r\n" + 
			"",

			"delete from alertsubtypechannel where alertSubTypeId !='';",

			"insert into alertsubtypechannel(channelId,alertSubTypeId,createdby)\r\n" + 
			"(select alertcategorychannel.ChannelID,\r\n" + 
			"alertsubtype.id,\r\n" + 
			"'infinityuser' as createdby\r\n" + 
			"from alertcategorychannel\r\n" + 
			"join dbxalerttype on alertcategorychannel.AlertCategoryId = dbxalerttype.AlertCategoryId\r\n" + 
			"join alertsubtype on alertsubtype.AlertTypeId = dbxalerttype.id);"

	};

	public static void main(String[] args) {
		for (String s : queries) {
			System.out.println(s);
			System.out.println("\n\n\n");
		}
	}

}
