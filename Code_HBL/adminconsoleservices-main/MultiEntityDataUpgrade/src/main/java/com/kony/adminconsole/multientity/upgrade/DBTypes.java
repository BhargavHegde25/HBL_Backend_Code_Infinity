package com.kony.adminconsole.multientity.upgrade;

public enum DBTypes {
	
	MYSQL("jdbc:mysql://$HOSTNAME$:$PORT$?","com.mysql.cj.jdbc.Driver"), MSSQL("jdbc:sqlserver://$HOSTNAME$:$PORT$;databaseName=$DB_NAME$;", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    ORACLE_CONTAINER_DB("jdbc:oracle:thin:@$HOSTNAME$:$PORT$:$SID$","oracle.jdbc.driver.OracleDriver") ,
    ORACLE_PLUGGABLE_DB("jdbc:oracle:thin:@//$HOSTNAME$:$PORT$/$SERVICENAME$","oracle.jdbc.driver.OracleDriver")
    {
    };

    private String jdbcURL;
    private String driverManager;

    private DBTypes(String jdbcURL, String driverManager) {
        this.jdbcURL = jdbcURL;
        this.driverManager = driverManager;
     
    }

    public String replaceInJDBCURL(String dbHost, int dbPort, String connectionPropsStr, String dbName) {
        String jdbcURL = this.jdbcURL.replace("$HOSTNAME$", dbHost).replace("$PORT$", String.valueOf(dbPort))
                .replace("$DB_NAME$", dbName);
        /*if(Sid!=null) {
        	jdbcURL =jdbcURL.replace("$SID$", Sid);
        }
        if(ServiceName!=null) {
        	jdbcURL =jdbcURL.replace("$SERVICENAME$", ServiceName);
        }*/
        if (connectionPropsStr != null && !connectionPropsStr.isEmpty()) {
            jdbcURL = jdbcURL.concat(connectionPropsStr);
        }
        
        return jdbcURL;
    }
    
    public String getDriverManager() { 
    	return this.driverManager;
    }
}
