package com.temenos.spotlight.idm.migration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.flywaydb.core.Flyway;
/**
 * Main class performs the Idm database migrations
 * 
 * @author Rishi Gupta
 *
 */

public class DatabaseMigrator {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
    private static final String SYSTEM_LINE_SEPERATOR = System.getProperty("line.separator");
    private static final String GENERATED_CUSTOM_MIGRATIONS_DIR_NAME = "generated_custom_migrations";
    private static String PROP_USERNAME = "db.user";
    private static String PROP_PASSWORD = "db.password";
    private static String PROP_HOST = "db.host";
    private static String PROP_PORT = "db.port";
    private static String PROP_CONN_PROPS = "db.conn.props";
    private static String PROP_NAME = "db.name";
    private static String PROP_DB_TYPE = "db.type";
    private static String PROP_DB_CUSTOM_MIGRATIONS_PATH = "db.custom.migrations.path";
    private static String PROP_SCHEMANAME="db.schemaName";
    private static String PROP_REALMNAME="db.realmName";

    public static void main(String[] args) {
    	PrintStream stdout = null;
        try {
            // redirecting the standard ouput and error to a result file
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
            stdout = new PrintStream(
                    new File("migration_result_" + dateFormat.format(new Date()) + ".log"));
            System.setOut(stdout);
            System.setErr(stdout);

          //   validate the provided database details
            String dbUser = System.getProperty(PROP_USERNAME);
            String dbPassword = System.getProperty(PROP_PASSWORD);
            String dbHost = System.getProperty(PROP_HOST);
            String dbPortProp = System.getProperty(PROP_PORT);
            Integer dbPort = null;
            String dbSchemaName=System.getProperty(PROP_SCHEMANAME);
            String dbRealmName=System.getProperty(PROP_REALMNAME);
            String dbConnProps = System.getProperty(PROP_CONN_PROPS);
            String dbName = System.getProperty(PROP_NAME);
            String dbTypeProp = System.getProperty(PROP_DB_TYPE);
            String dbCustomMigrationsPath = System.getProperty(PROP_DB_CUSTOM_MIGRATIONS_PATH);
            DBTypes dbType = null;
//            String dbHost = "localhost";
//            String dbPortProp = "5432";
//            Integer dbPort = null;
//            String dbSchemaName="public";
//            String dbConnProps = null;
//            String dbName = "keycloak";
//            String dbTypeProp = "POSTGRES";
//            String dbCustomMigrationsPath = null;//System.getProperty(PROP_DB_CUSTOM_MIGRATIONS_PATH);
            
            if (dbUser == null || (dbUser = dbUser.trim()).isEmpty()) {
                throw new RuntimeException("System property -D" + PROP_USERNAME + " is required");
            }

            if (dbPassword == null || (dbPassword = dbPassword.trim()).isEmpty()) {
                throw new RuntimeException("System property -D" + PROP_PASSWORD + " is required");
            }

            if (dbHost == null || (dbHost = dbHost.trim()).isEmpty()) {
                throw new RuntimeException("System property -D" + PROP_HOST + " is required");
            }

            if (dbPortProp == null || (dbPortProp = dbPortProp.trim()).isEmpty()) {
                throw new RuntimeException("System property -D" + PROP_PORT + " is required");
            }

            try {
                dbPort = Integer.parseInt(dbPortProp);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException("System property -D" + PROP_PORT + " provided must be a number");
            }

            if (dbName == null || (dbName = dbName.trim()).isEmpty()) {
                throw new RuntimeException("System property -D" + PROP_NAME + " is required");
            }

            if (dbTypeProp == null || (dbTypeProp = dbTypeProp.trim()).isEmpty()) {
                System.out.println("System property -D" + PROP_DB_TYPE + " is not provided and defaults to \""
                        + DBTypes.MYSQL.name() + "\"");
                dbTypeProp = DBTypes.MYSQL.name();
            }

            try {
                dbType = DBTypes.valueOf(dbTypeProp.toUpperCase());
            } catch (IllegalArgumentException iae) {
                throw new RuntimeException("System property -D" + PROP_DB_TYPE
                        + " provided must be one of following supported databases: " + Arrays.asList(DBTypes.values()));
            }
            
            if (dbType == DBTypes.MSSQL && StringUtils.isEmpty(dbSchemaName)){
            		dbSchemaName = dbName;
            }
            if (dbRealmName == null || (dbRealmName = dbRealmName.trim()).isEmpty()) {
                throw new RuntimeException("System property -D" + PROP_REALMNAME + " is required");
            }

            File dbCustomMigrationsDir = null;
            if (dbCustomMigrationsPath != null && !(dbCustomMigrationsPath = dbCustomMigrationsPath.trim()).isEmpty()) {
                dbCustomMigrationsDir = new File(dbCustomMigrationsPath);
                if (!dbCustomMigrationsDir.exists() || !dbCustomMigrationsDir.isDirectory()
                        || !dbCustomMigrationsDir.canRead() || !dbCustomMigrationsDir.canWrite()) {
                    throw new RuntimeException("System property -D" + PROP_DB_CUSTOM_MIGRATIONS_PATH
                            + " supplied is either not a directory or it doesn't have read/write permissions");
                }
            }

            // generate custom DB migrations using the migrations scripts path
            if (dbCustomMigrationsDir != null) {
                generateCustomDBMigrations(dbCustomMigrationsDir);
            }

            // Flyway work starts here

            // migrate bundled scripts
            migrate(dbType, dbUser, dbPassword, dbHost, dbPort, dbConnProps, dbName,
                    "classpath:flyway-migrations/" + dbType.name().toLowerCase(), false, dbSchemaName, dbRealmName);

            // migrate custom scripts
            if (dbCustomMigrationsDir != null) {
                migrate(dbType, dbUser, dbPassword, dbHost, dbPort, dbConnProps, dbName,
                        "filesystem:" + dbCustomMigrationsDir.getAbsolutePath() + File.separator
                                + GENERATED_CUSTOM_MIGRATIONS_DIR_NAME,
                        true, dbSchemaName, dbRealmName);
            }
        } catch (Exception e) {
        	alert.prepareError("Error occurred: ", e).log();
            System.exit(1);
        }finally {
        	if( stdout != null) {
        		try {
        			stdout.close();
        		}catch(Exception e1) {
        			System.out.println("Exception while closing stream :"+e1.getMessage());
        		}
        	}
        }

    }

    private static void generateCustomDBMigrations(File dbCustomMigrationsDir) throws Exception {
        // generation of migrations starts here
        File ddlMigration = new File(dbCustomMigrationsDir, "ddl.sql");
        File dmlMigration = new File(dbCustomMigrationsDir, "dml.sql");

        List<File> migrations = new ArrayList<>(); // list for preserving the sql files order

        if (ddlMigration.exists() && ddlMigration.isFile()) {
            migrations.add(ddlMigration);
        }

        if (dmlMigration.exists() && dmlMigration.isFile()) {
            migrations.add(dmlMigration);
        }

        File generatedCustomMigrationsDir = new File(dbCustomMigrationsDir, GENERATED_CUSTOM_MIGRATIONS_DIR_NAME);
        if (generatedCustomMigrationsDir.exists()) {
            System.out.println("Trying to Cleaning up generated custom migrations directory: ["
                    + generatedCustomMigrationsDir.getAbsolutePath() + "] as it already exists.");
            if (!FileUtils.deleteQuietly(generatedCustomMigrationsDir)) {
                throw new Exception(
                        "Unable to cleanup following directory: [" + generatedCustomMigrationsDir.getAbsolutePath()
                                + "]. Re-run the tool by manually deleting the directory.");
            }
        }

        if (!generatedCustomMigrationsDir.exists()) {
            generatedCustomMigrationsDir.mkdirs();
        }

        // unify version migrations
        if (migrations != null && !migrations.isEmpty()) {
            System.out.println("Start of unifying custom migrations");

            String unifiedMigrationFileName = "R__custom_migration.sql";

            try (FileWriter fileWriter = new FileWriter(
                    new File(generatedCustomMigrationsDir, unifiedMigrationFileName), true)) {
                for (File migration : migrations) {
                    try (FileReader fileReader = new FileReader(migration)) {
                        fileWriter.append("-- " + migration.getName() + " starts").append(SYSTEM_LINE_SEPERATOR);
                        IOUtils.copy(fileReader, fileWriter);
                        fileWriter.append(SYSTEM_LINE_SEPERATOR).append(SYSTEM_LINE_SEPERATOR);
                    }
                }
            } catch (Exception e) {
                System.err.println("Failure while unifying custom migrations");
                throw new Exception("Process failed while unifying migrations", e);
            }
            System.out.println("End of unifying custom migrations");
        } else {
            System.out.println("No custom migrations exists to unify");
        }
    }

    private static void migrate(DBTypes dbType, String dbUser, String dbPassword, String dbHost, int dbPort,
            String connectionPropsStr, String dbName, String dbScriptsLocation, boolean isCustomMigration, String dbSchemaName, String dbRealmName)
            throws Exception {
        Flyway flyway = new Flyway();
        if (dbType == DBTypes.POSTGRES) {
        	String params =  dbName + "?user="+dbUser+"&password="+dbPassword;
        	if(connectionPropsStr != null) {
        	  connectionPropsStr = params + "&" +connectionPropsStr;        	  
        	}        	
        }
        flyway.setDataSource(dbType.replaceInJDBCURL(dbHost, dbPort, connectionPropsStr, dbName), dbUser, dbPassword);
        if(dbType == DBTypes.POSTGRES)
        {
        	flyway.setPlaceholderPrefix("[#{");
        	flyway.setPlaceholderSuffix("}]");
        }
        if (dbType == DBTypes.MSSQL || dbType == DBTypes.POSTGRES) {
        	Map<String,String> placeholders=new HashMap<>();
            flyway.setSchemas(dbSchemaName);
            placeholders.put("dbxdbname",dbName);
            placeholders.put("dbxschemaname",dbSchemaName);
            placeholders.put("dbxusername",dbUser);
            placeholders.put("dbxrealmname",dbRealmName);
            flyway.setPlaceholders(placeholders);
        } else {
            flyway.setSchemas(dbName);
        }
        flyway.setLocations(dbScriptsLocation);
        flyway.setBaselineOnMigrate(true);
        if (isCustomMigration) {
            flyway.setTable("flyway_custom_idm_schema_history");
            flyway.setBaselineDescription("Baseline to custom as this schema already exists");
            flyway.setBaselineVersionAsString("0.0");
        } else {
            flyway.setBaselineDescription("Baseline to 2021.01 version as this schema already exists");
            flyway.setBaselineVersionAsString("2021.01");
        }
        flyway.migrate();
    }

}
