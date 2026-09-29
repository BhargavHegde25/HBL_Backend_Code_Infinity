package com.kony.logservices.migration;

public enum DBTypes {

	MYSQL("jdbc:mysql://$HOSTNAME$:$PORT$?"), MSSQL("jdbc:sqlserver://$HOSTNAME$:$PORT$;databaseName=$DB_NAME$;"),
	ORACLE_CONTAINER_DB("jdbc:oracle:thin:@$HOSTNAME$:$PORT$:$SID$") ,
    ORACLE_PLUGGABLE_DB("jdbc:oracle:thin:@//$HOSTNAME$:$PORT$/$SERVICENAME$"){
    };

    private String jdbcURL;

    private DBTypes(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public String replaceInJDBCURL(String dbHost, int dbPort, String connectionPropsStr, String dbName, String Sid, String ServiceName) {
        String jdbcURL = this.jdbcURL.replace("$HOSTNAME$", dbHost).replace("$PORT$", String.valueOf(dbPort))
                .replace("$DB_NAME$", dbName);
        if(Sid!=null) {
        	jdbcURL =jdbcURL.replace("$SID$", Sid);
        }
        if(ServiceName!=null) {
        	jdbcURL =jdbcURL.replace("$SERVICENAME$", ServiceName);
        }
        if (connectionPropsStr != null && !connectionPropsStr.isEmpty()) {
            jdbcURL = jdbcURL.concat(connectionPropsStr);
        }
        return jdbcURL;
    }

}