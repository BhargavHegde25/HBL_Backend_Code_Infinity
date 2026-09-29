package com.kony.logservices.migration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.flywaydb.core.Flyway;

/**
 * Main class performs the Admin Console database migrations
 * 
 * @author Venkateswara Rao Alla
 *
 */

public class DatabaseMigrator {
	//private static final Logger LOG = (Logger) LogManager.getLogger(DatabaseMigrator.class);
    private static final String SYSTEM_LINE_SEPERATOR = System.getProperty("line.separator");
    private static final String GENERATED_CUSTOM_MIGRATIONS_DIR_NAME = "generated_custom_migrations";
    //private static final String DEFAULT_SQLSERVER_SCHEMA_NAME = "dbxdb";

    private static String PROP_USERNAME = "db.user";
    private static String PROP_PASSWORD = "db.password";
    private static String PROP_HOST = "db.host";
    private static String PROP_PORT = "db.port";
    private static String PROP_CONN_PROPS = "db.conn.props";
    private static String PROP_NAME = "db.name";
    private static String PROP_SID ="db.sid";
    private static String PROP_SERVICENAME ="db.servicename";
    private static String PROP_DB_TYPE = "db.type";
    private static String PROP_DB_CUSTOM_MIGRATIONS_PATH = "db.custom.migrations.path";
    private static String PROP_SCHEMANAME="db.schemaName";
    private static String FIX_VERSION_4_0_0_1 = "4.0.0.1";

    public static void main(String[] args) {
    	PrintStream stdout = null;
        try {
            // redirecting the standard ouput and error to a result file
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
            stdout = new PrintStream(
                    new File("migration_result_" + dateFormat.format(new Date()) + ".log"));
            System.setOut(stdout);
            System.setErr(stdout);

            // validate the provided database details
            String dbUser = System.getProperty(PROP_USERNAME);
            String dbPassword = System.getProperty(PROP_PASSWORD);
            String dbHost = System.getProperty(PROP_HOST);
            String dbPortProp = System.getProperty(PROP_PORT);
            Integer dbPort = null;
            String dbSchemaName=System.getProperty(PROP_SCHEMANAME);
            String dbConnProps = System.getProperty(PROP_CONN_PROPS);
            String dbName = System.getProperty(PROP_NAME);
            String Sid = System.getProperty(PROP_SID);
            String ServiceName = System.getProperty(PROP_SERVICENAME);
            String dbTypeProp = System.getProperty(PROP_DB_TYPE);
            String dbCustomMigrationsPath = System.getProperty(PROP_DB_CUSTOM_MIGRATIONS_PATH);
            DBTypes dbType = null;

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
            String dbTypes = dbTypeProp;
            try {
            	if(Sid != null && !Sid.equals("")) {
					dbTypeProp = dbTypeProp + "_CONTAINER_DB";
				}
				else if(ServiceName != null && !ServiceName.equals("")) {
					dbTypeProp = dbTypeProp + "_PLUGGABLE_DB";
				}
                dbType = DBTypes.valueOf(dbTypeProp.toUpperCase());
            } catch (IllegalArgumentException iae) {
                throw new RuntimeException("System property -D" + PROP_DB_TYPE
                        + " provided must be one of following supported databases: " + Arrays.asList(DBTypes.values()));
            }
            
            if (dbType == DBTypes.MSSQL && StringUtils.isBlank(dbSchemaName)){
            		dbSchemaName = dbName;
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
            migrate(dbType, dbUser, dbPassword, dbHost, dbPort, dbConnProps, dbName, Sid, ServiceName,
                    "classpath:dbscripts/" + dbTypes.toLowerCase(), false, dbSchemaName);

            // migrate custom scripts
            if (dbCustomMigrationsDir != null) {
                migrate(dbType, dbUser, dbPassword, dbHost, dbPort, dbConnProps, dbName, Sid, ServiceName,
                        "filesystem:" + dbCustomMigrationsDir.getAbsolutePath() + File.separator
                                + GENERATED_CUSTOM_MIGRATIONS_DIR_NAME,
                        true, dbSchemaName);
            }
        } catch (Exception e) {
        	//LOG.error("Error occurred: ", e);
        	System.out.print("Error occurred: " + e);
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
            String connectionPropsStr, String dbName, String Sid, String ServiceName, String dbScriptsLocation, boolean isCustomMigration, String dbSchemaName)
            throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dbType.replaceInJDBCURL(dbHost, dbPort, connectionPropsStr, dbName, Sid, ServiceName), dbUser, dbPassword);
        if (dbType == DBTypes.MSSQL) {
        	Map<String,String> placeholders=new HashMap<>();
            flyway.setSchemas(dbSchemaName);
            placeholders.put("logdbname",dbName);
            placeholders.put("logschemaname",dbSchemaName);
            flyway.setPlaceholders(placeholders);
        } 
        else {
            flyway.setSchemas(dbName);
        }
        flyway.setLocations(dbScriptsLocation);
        flyway.setBaselineOnMigrate(true);
        if (isCustomMigration) {
            flyway.setTable("flyway_custom_schema_history");
            flyway.setBaselineDescription("Baseline to custom as this schema already exists");
            flyway.setBaselineVersionAsString("0.0");
        } else {
            flyway.setBaselineDescription("Baseline to 4.0 version as this schema already exists");
            flyway.setBaselineVersionAsString("4.0");
        }
        checkAndApplyFixVersion(flyway, dbType, dbHost, dbPort, dbName, Sid, ServiceName, dbSchemaName,
				dbUser, dbPassword, connectionPropsStr);
        flyway.migrate();
    }
    
	private static void checkAndApplyFixVersion(Flyway flyway, DBTypes dbType, String dbHost, int dbPort, String dbName,
			String Sid, String ServiceName, String dbSchemaName, String dbUser, String dbPassword, String connectionPropsStr) throws Exception {
		String version = null;
		String jdbcURL = dbType.replaceInJDBCURL(dbHost, dbPort, connectionPropsStr, dbName, Sid, ServiceName);
		if (flyway.info() != null && flyway.info().current() != null && flyway.info().current().getVersion() != null)
			version = flyway.info().current().getVersion().toString();
		mssqlChecksumFixFor4001(dbType, dbSchemaName, dbUser, dbPassword, version, jdbcURL);
	}
    
    
    private static void mssqlChecksumFixFor4001(DBTypes dbType, String dbSchemaName, String dbUser,
			String dbPassword, String version, String jdbcURL) throws Exception {
		if (dbType == DBTypes.MSSQL && version != null && version.contains(FIX_VERSION_4_0_0_1)) {
			try (Connection con = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)){
				try (Statement st = con.createStatement();) {
						// Initialize the script runner
						con.setAutoCommit(false);
						st.addBatch(
								"UPDATE ["+dbSchemaName+"].[flyway_schema_history] SET [checksum] = '1844040660', [installed_on] = current_timestamp WHERE ([installed_rank] = '1');");
						st.executeBatch();
						con.commit();
				}catch(Exception e) {
					if (con != null) {
						try {
							con.rollback();
						} catch (SQLException ex1) {
							System.err.println("Failure while roll back");
							throw new Exception("Process failed while unifying migrations", e);
						}
					}
				}
			}
		}
	}

}
