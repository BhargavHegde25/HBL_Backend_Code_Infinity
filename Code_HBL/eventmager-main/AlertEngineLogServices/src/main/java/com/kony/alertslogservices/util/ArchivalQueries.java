package com.kony.alertslogservices.util;

import java.util.EnumMap;


public class ArchivalQueries {
	private ArchivalQueries() {
	}

	private static EnumMap<SQLQueriesEnum, String> loadMap() {
		EnumMap<SQLQueriesEnum, String> tempmap = new EnumMap<>(SQLQueriesEnum.class);
	
		if(QueryFormer.MSSQL_DB.equalsIgnoreCase(QueryFormer.getDBType())) {
			tempmap = loadMssqlMap();
		}else if(QueryFormer.MYSQL_DB.equalsIgnoreCase(QueryFormer.getDBType())) {
			tempmap = loadMysqlMap();
		}else {
			tempmap = loadOracleMap();
		}
		
		return tempmap;

	}
	
	public static EnumMap<SQLQueriesEnum, String> loadMysqlMap(){
		EnumMap<SQLQueriesEnum,String> tempmap = new EnumMap<>(SQLQueriesEnum.class);
		tempmap.put(SQLQueriesEnum.ALERTLOG_READ,QueryFormer.replaceSchameName(MYSQL_ALERTLOG_READ));
		tempmap.put(SQLQueriesEnum.ALERTLOG_DELETE, QueryFormer.replaceSchameName(MYSQL_ALERTLOG_DELETE));
		tempmap.put(SQLQueriesEnum.ALERTLOG_INSERT, QueryFormer.replaceSchameName(MYSQL_ALERTLOG_INSERT));
		tempmap.put(SQLQueriesEnum.LIMIT , MYSQL_LIMIT);
		tempmap.put(SQLQueriesEnum.DISPATCH_DATE, MYSQL_DISPATCHDATE);
		tempmap.put(SQLQueriesEnum.DELETEQUERY_WHERECLAUSE, MYSQL_DELETEQUERY_WHERECLAUSE);
		return tempmap;
	}
	
	public static EnumMap<SQLQueriesEnum,String> loadMssqlMap(){
		EnumMap<SQLQueriesEnum,String> tempmap = new EnumMap<>(SQLQueriesEnum.class);
		tempmap.put(SQLQueriesEnum.ALERTLOG_READ, QueryFormer.replaceSchameName(MSSQL_ALERTLOG_READ));
		tempmap.put(SQLQueriesEnum.ALERTLOG_DELETE,QueryFormer.replaceSchameName(MSSQL_ALERTLOG_DELETE));
		tempmap.put(SQLQueriesEnum.ALERTLOG_INSERT,QueryFormer.replaceSchameName(MSSQL_ALERTLOG_INSERT));
		tempmap.put(SQLQueriesEnum.LIMIT , MSSQL_LIMIT);
		tempmap.put(SQLQueriesEnum.DISPATCH_DATE, MSSQL_DISPATCHDATE);
		tempmap.put(SQLQueriesEnum.DELETEQUERY_WHERECLAUSE, MSSQL_DELETEQUERY_WHERECLAUSE);
		return tempmap;
	}
	
	public static EnumMap<SQLQueriesEnum,String> loadOracleMap(){
		EnumMap<SQLQueriesEnum,String> tempmap = new EnumMap<>(SQLQueriesEnum.class);
		tempmap.put(SQLQueriesEnum.ALERTLOG_READ,QueryFormer.replaceSchameName(ORACLE_ALERTLOG_READ));
		tempmap.put(SQLQueriesEnum.ALERTLOG_DELETE,QueryFormer.replaceSchameName(ORACLE_ALERTLOG_DELETE));
		tempmap.put(SQLQueriesEnum.ALERTLOG_INSERT,QueryFormer.replaceSchameName(ORACLE_ALERTLOG_INSERT));
		tempmap.put(SQLQueriesEnum.LIMIT , ORACLE_LIMIT);
		tempmap.put(SQLQueriesEnum.DISPATCH_DATE, ORACLE_DISPATCHDATE);
		tempmap.put(SQLQueriesEnum.DELETEQUERY_WHERECLAUSE, ORACLE_DELETEQUERY_WHERECLAUSE);
		return tempmap;
	}
	protected static final EnumMap<SQLQueriesEnum,String> querymap = loadMap();
	private static final String MYSQL_ALERTLOG_READ = "select * from :schemaName.alerthistory";
	private static final String MYSQL_ALERTLOG_DELETE = "DELETE FROM :schemaName.alerthistory";
	private static final String MYSQL_ALERTLOG_INSERT = "INSERT INTO :schemaName.archivedalerthistory(Id,EventId,AlertSubTypeId,AlertTypeId,AlertCategoryId,AlertStatusId,Customer_Id,LanguageCode,ChannelId,Status,Subject,Message,SenderName,SenderEmail,ReferenceNumber,DispatchDate,ErrorMessage,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag)VALUES(:id,:eventid,:alertsubtypeid,:alerttypeid,:alertcategoryid,:alertstatusid,:customer_id,:languagecode,:channelid,:status,:subject,:message,:sendername,:senderemail,:referencenumber,:dispatchdate,:errormessage,:createdby,:modifiedby,:createdts,:lastmodifiedts,:synctimestamp,:softdeleteflag)";
	private static final String MYSQL_DISPATCHDATE = " DispatchDate ";
	private static final String MYSQL_LIMIT = " LIMIT :startOffset, :endOffset";
	private static final String MYSQL_DELETEQUERY_WHERECLAUSE = " WHERE id IN ";
	
	private static final String MSSQL_ALERTLOG_READ = "select * from :schemaName.alerthistory offset :startOffset rows fetch next :endOffset rows only";
	private static final String MSSQL_ALERTLOG_DELETE = "DELETE FROM :schemaName.alerthistory";
	private static final String MSSQL_ALERTLOG_INSERT = "INSERT INTO :schemaName.archivedalerthistory(Id,EventId,AlertSubTypeId,AlertTypeId,AlertCategoryId,AlertStatusId,Customer_Id,LanguageCode,ChannelId,Status,Subject,Message,SenderName,SenderEmail,ReferenceNumber,DispatchDate,ErrorMessage,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag)VALUES(:id,:eventid,:alertsubtypeid,:alerttypeid,:alertcategoryid,:alertstatusid,:customer_id,:languagecode,:channelid,:status,:subject,:message,:sendername,:senderemail,:referencenumber,:dispatchdate,:errormessage,:createdby,:modifiedby,:createdts,:lastmodifiedts,:synctimestamp,:softdeleteflag)";
	private static final String MSSQL_LIMIT = " offset :startOffset rows fetch next :endOffset rows only";
	private static final String MSSQL_DISPATCHDATE = " DispatchDate ";
	private static final String MSSQL_DELETEQUERY_WHERECLAUSE = " WHERE id IN ";
	
	private static final String ORACLE_ALERTLOG_READ = "select * from \":schemaName\".\"alerthistory\" offset :startOffset rows fetch next :endOffset rows only";
	private static final String ORACLE_ALERTLOG_DELETE = "DELETE FROM \":schemaName\".\"alerthistory\"";
	private static final String ORACLE_ALERTLOG_INSERT = "INSERT INTO \":schemaName\".\"archivedalerthistory\"(\"Id\",\"EventId\",\"AlertSubTypeId\",\"AlertTypeId\",\"AlertCategoryId\",\"AlertStatusId\",\"Customer_Id\",\"LanguageCode\",\"ChannelId\",\"Status\",\"Subject\",\"Message\",\"SenderName\",\"SenderEmail\",\"ReferenceNumber\",\"DispatchDate\",\"ErrorMessage\",\"createdby\",\"modifiedby\",\"createdts\",\"lastmodifiedts\",\"synctimestamp\",\"softdeleteflag\")VALUES(:id,:eventid,:alertsubtypeid,:alerttypeid,:alertcategoryid,:alertstatusid,:customer_id,:languagecode,:channelid,:status,:subject,:message,:sendername,:senderemail,:referencenumber,:dispatchdate,:errormessage,:createdby,:modifiedby,:createdts,:lastmodifiedts,:synctimestamp,:softdeleteflag)";
	private static final String ORACLE_LIMIT = " offset :startOffset rows fetch next :endOffset rows only";
	private static final String ORACLE_DISPATCHDATE = " \"DispatchDate\" ";
	private static final String ORACLE_DELETEQUERY_WHERECLAUSE = " WHERE \"id\" IN ";

}
