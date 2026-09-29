package com.kony.dbp.fileprocessingengine;

public class FileProcessingEngineConstants {

	private FileProcessingEngineConstants() {

	}

	
	public static final String FILEPROCESSINGENGINE_DELETE_URL ="/services/data/v1/FileSystemStorage/objects/File";
	public static final String FILEPROCESSINGENGINE_DATABASE_DRIVER = "ALERTSENGINE_DATABASE_DRIVER";
	public static final String FILEPROCESSINGENGINE_DATABASE_URL = "ALERTSENGINE_DATABASE_URL";
	public static final String FILEPROCESSINGENGINE_DATABASE_USER = "ALERTSENGINE_DATABASE_USER";
	public static final String FILEPROCESSINGENGINE_DATABASE_PSWD = "ALERTSENGINE_DATABASE_PASSWORD";

	public static final String FILEPROCESSINGENGINE_DB_MAXPOOL_SIZE = "ALERTENGINE_DB_MAXPOOL_SIZE";
	public static final String FILEPROCESSINGENGINE_THREADGROUP_SIZE = "ALERTENGINE_THREADGROUP_SIZE";

	public static final String FIELPROCESSINGENGINE_QUERY_EVENTSUBTYPE = "select id, eventtypeid, filename from dbxdb.eventsubtype where filename is not null";

	public static final String FIELPROCESSINGENGINE_SERVICEID = "FileSystemStorage";
	public static final String FIELPROCESSINGENGINE_OBJECTID = "File";
	public static final String FIELPROCESSINGENGINE_OBJECT_VERSION = "1.0";
	public static final String FIELPROCESSINGENGINE_OPERATIONID = "getBinary";
	public static final String FILE_PROCESSING_ENGINE_BATCHLIMIT = "FILE_PROCESSING_ENGINE_BATCHLIMIT";
	public static final String FIELPROCESSINGENGINE_DB_COL = "filename";
	
	public static final String FIELPROCESSINGENGINE_IDENTITY_URL = "FIELPROCESSINGENGINE_IDENTITY_URL";
	
	public static final String FIELPROCESSINGENGINE_IDENTITY_USER = "FIELPROCESSINGENGINE_IDENTITY_USER";
	public static final String FIELPROCESSINGENGINE_IDENTITY_PSWD = "FIELPROCESSINGENGINE_IDENTITY_PSWD";
	
	public static final String DBPEVENTMANAGER_APPKEY = "DBPEVENTMANAGER_APPKEY";
	public static final String DBPEVENTMANAGER_APPSECRET = "DBPEVENTMANAGER_APPSECRET";
	


	
	
}
