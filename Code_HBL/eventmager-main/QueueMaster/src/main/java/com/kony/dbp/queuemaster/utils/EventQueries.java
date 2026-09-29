package com.kony.dbp.queuemaster.utils;

import org.apache.commons.lang3.StringUtils;

public enum EventQueries {

	MYSQL_EVENT_CONSUMER ("select min(LastEventId) from eventconsumer;"),
	MSSQL_EVENT_CONSUMER ("select min(LastEventId) from [:schemaname].[eventconsumer];"),
	ORACLE_EVENT_CONSUMER ("select min(\"LastEventId\") from \":schemaname\".\"eventconsumer\""),
	
	MYSQL_EVENT_DELETE ("delete from event where Event_id <= ? and IsProcessed = 0"),
	MSSQL_EVENT_DELETE ("delete from [:schemaname].[event] where Event_id <= ? and IsProcessed = 0"),
	ORACLE_EVENT_DELETE ("delete from \":schemaname\".\"event\" where \"Event_id\" <= ? and \"IsProcessed\" = 0 "),
	
	MYSQL_EVENT_INSERT ("insert into event (EventType, EventSubType, Status_id,EventData, OtherData,IsProcessed,Producer,PreProcessorResult,PostProcessorResult,Session,Timestamp) values "),
	MSSQL_EVENT_INSERT ("insert into [:schemaname].[event] (EventType, EventSubType, Status_id, EventData, OtherData,IsProcessed,Producer,PreProcessorResult,PostProcessorResult,Session,Timestamp) values "),
	ORACLE_EVENT_INSERT ("insert into \":schemaname\".\"event\" (\"EventType\", \"EventSubType\", \"Status_id\",\"EventData\", \"OtherData\",\"IsProcessed\",\"Producer\",\"PreProcessorResult\",\"PostProcessorResult\",\"Session\",\"Timestamp\") values "),

	
	MYSQL_LAST_EVENT_ID ("select LastEventId from eventconsumer where ServiceId = ? and OperationId = ?;"),
	MSSQL_LAST_EVENT_ID ("select LastEventId from [:schemaname].[eventconsumer]  where ServiceId = ? and OperationId = ?;"),
	ORACLE_LAST_EVENT_ID ("select \"LastEventId\" from \":schemaname\".\"eventconsumer\" where \"ServiceId\" = ? and \"OperationId\" = ?"),
	
	MYSQL_UPDATE_EVENT_CONSUMER ("update eventconsumer set LastEventId =? where ServiceId = ? and OperationId = ? and LastEventId = ?; "),
	MSSQL_UPDATE_EVENT_CONSUMER ("update [:schemaname].[eventconsumer] set LastEventId = ? where ServiceId = ? and OperationId = ? and LastEventId = ?;"),
    ORACLE_UPDATE_EVENT_CONSUMER ("update \":schemaname\".\"eventconsumer\" set \"LastEventId\" = ? where \"ServiceId\" = ? and \"OperationId\" = ? and \"LastEventId\" = ?"),
	
	MYSQL_ALL_EVENTS ("select Event_id, EventType, EventSubType, Status_id, EventData, OtherData, isProcessed, Producer, PreProcessorResult, PostProcessorResult, Session, Timestamp from event where Event_id > ? order by 1;"),
	MSSQL_ALL_EVENTS ("select Event_id, EventType, EventSubType, Status_id, EventData, OtherData, isProcessed, Producer, PreProcessorResult, PostProcessorResult, Session, Timestamp from [:schemaname].[event] where Event_id > ? order by 1;"),
	ORACLE_ALL_EVENTS ("select \"Event_id\", \"EventType\", \"EventSubType\", \"Status_id\",\"EventData\", \"OtherData\", \"IsProcessed\", \"Producer\", \"PreProcessorResult\", \"PostProcessorResult\", \"Session\", \"Timestamp\" from \":schemaname\".\"event\" where \"Event_id\" > ? order by 1"),
	
	
	MYSQL_EVENT_CONSUMER_DETAILS ("select eventconsumer.ServiceId, eventconsumer.OperationId, eventconsumer.BatchLimit, eventconsumertypes.EventType from eventconsumer, eventconsumertypes where eventconsumer.ServiceId = eventconsumertypes.ServiceId and eventconsumer.OperationId = eventconsumertypes.OperationId order by 1, 2;"),
	MSSQL_EVENT_CONSUMER_DETAILS ("select eventconsumer.ServiceId, eventconsumer.OperationId, eventconsumer.BatchLimit, eventconsumertypes.EventType from [:schemaname].[eventconsumer], [:schemaname].[eventconsumertypes] where eventconsumer.ServiceId = eventconsumertypes.ServiceId and eventconsumer.OperationId = eventconsumertypes.OperationId order by 1, 2;"),

	ORACLE_EVENT_CONSUMER_DETAILS ("select \"eventconsumer\".\"ServiceId\", \"eventconsumer\".\"OperationId\", \"eventconsumer\".\"BatchLimit\", \"eventconsumertypes\".\"EventType\" from \":schemaname\".\"eventconsumer\", \":schemaname\".\"eventconsumertypes\" where \"eventconsumer\".\"ServiceId\" = \"eventconsumertypes\".\"ServiceId\" and \"eventconsumer\".\"OperationId\" = \"eventconsumertypes\".\"OperationId\" order by 1, 2");
	
	
	String query;

	EventQueries(String query) {
		String schemaName = EnvironmentConfigurationsHandler.getValue(Constants.SCHEMA_NAME);
		if(StringUtils.isNotBlank(schemaName)) {
			schemaName = "dbxdb";
		}
	    this.query = query.replace(":schemaname", schemaName);
	}

	public String getQuery() {
	    return this.query;
	}

}