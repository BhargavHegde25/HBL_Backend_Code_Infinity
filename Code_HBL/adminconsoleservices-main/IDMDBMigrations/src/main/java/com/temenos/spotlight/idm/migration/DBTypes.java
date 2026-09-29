package com.temenos.spotlight.idm.migration;

public enum DBTypes {

    MYSQL("jdbc:mysql://$HOSTNAME$:$PORT$?"), MSSQL("jdbc:sqlserver://$HOSTNAME$:$PORT$;databaseName=$DB_NAME$;"),
    ORACLE("jdbc:oracle:thin:@$HOSTNAME$:$PORT$"), POSTGRES("jdbc:postgresql://$HOSTNAME$:$PORT$/") 
    ;

    private String jdbcURL;

    private DBTypes(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public String replaceInJDBCURL(String dbHost, int dbPort, String connectionPropsStr, String dbName) {
        String jdbcURL = this.jdbcURL.replace("$HOSTNAME$", dbHost).replace("$PORT$", String.valueOf(dbPort))
                .replace("$DB_NAME$", dbName);         
        if (connectionPropsStr != null && !connectionPropsStr.isEmpty()) {
            jdbcURL = jdbcURL.concat(connectionPropsStr);
        }
        return jdbcURL;
    }

}