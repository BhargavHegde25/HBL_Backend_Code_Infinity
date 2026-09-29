package com.kony.adminconsole.multientity.upgrade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseUpdater {
	
	private static String PROP_USERNAME = "db.user";
	private static String PROP_PASSWORD = "db.password";
	private static String PROP_HOST = "db.host";
	private static String PROP_PORT = "db.port";
	private static String PROP_CONN_PROPS = "db.conn.props";
	private static String PROP_NAME = "db.name";
	private static String PROP_DB_TYPE = "db.type";
	private static String PROP_SCHEMANAME = "db.schemaName";
	private static String CONFIG_PROPERTIES = "db.config";
	private static Properties dbProperties = new Properties();
	
	private static String PROP_UPDATELEGALENTITY = "db.updateLe";
	private static String PROP_INSERTLEGALENTITY = "db.insertLe";
	private static String PROP_OPERATIONNAME = "db.operationname";
	private static String PROP_JSONCONFIGVALUE = "jsonConfigValue";
	private static String PROP_UPGRADEDFROM202210 = "isUpgradedFromVersion202210";
	private static String PROP_UPGRADEDFROM202301 = "isUpgradedFromVersion202301";
	private static String PROP_UPGRADEDFROM = "upgradedFromVersion";
	private static String PROP_UPGRADEDTO = "upgradedToVersion";
	private static String DBVERSION;
	
	private static String createDBObjects = "createDbObjects";
	private static String updateDBObjects = "updateLegalEntity";
	private static String reconcileDBObjects = "validateUpdateStatus";
	private static String deleteDBObjects = "deleteDbObjects";
	private static String reconcileAndCleanDBObjects = "completerun";
	private static String createMasterData = "createLEMasterData";
	
	private static String v202210 = "2022.10";
	private static String v202301 = "2023.01";
	private static String v202304 = "2023.04";	
	private static Connection dbConnection = null;

	public static void main(String[] args) {
		PrintStream stdout = null;
		try {
			// redirecting the standard output and error to a result file
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss");
			stdout = new PrintStream(
			new File("MultiEntityUpgrade_Result_" + dateFormat.format(new Date()) + ".log"));
			
			System.setOut(stdout);
			System.setErr(stdout);
			String dbConfigProperty = System.getProperty(CONFIG_PROPERTIES);
			String dbOperationName = System.getProperty(PROP_OPERATIONNAME);
			
			if(dbConfigProperty == null || (dbConfigProperty = dbConfigProperty.trim()).isEmpty()) {
				System.out.println("Command format to run the tool: ");
				System.out.println("java -Ddb.config='location of the propertyfile' -Ddb.operationname='TypeOfOperationToRun' -jar 'JarFileName'"+"\n");
				throw new RuntimeException("System property -D" + CONFIG_PROPERTIES + " is required");
			}
			if(dbOperationName == null || (dbOperationName = dbOperationName.trim()).isEmpty()) {
				System.out.println("Command format to run the tool: ");
				System.out.println("java -Ddb.config='location of the propertyfile' -Ddb.operationname='TypeOfOperationToRun' -jar 'JarFileName'"+"\n");
				throw new RuntimeException("System property -D" + PROP_OPERATIONNAME + " is required");
			}
			
			try {
				loadProperties(dbConfigProperty);
			}catch(Exception e) {
				System.out.print("Failed to load properties:"+e.getMessage());
				return;
			}
	
			//Reading db connection details from config.properties file
		    String dbUser = dbProperties.getProperty(PROP_USERNAME);
		    String dbPassword = dbProperties.getProperty(PROP_PASSWORD);
		    String dbHost = dbProperties.getProperty(PROP_HOST);
			String dbPortProp = dbProperties.getProperty(PROP_PORT);
			Integer dbPort = null;
			String dbSchemaName = dbProperties.getProperty(PROP_SCHEMANAME);
			String dbConnProps = dbProperties.getProperty(PROP_CONN_PROPS);
			String dbName = dbProperties.getProperty(PROP_NAME);
			String dbTypeProp =dbProperties.getProperty(PROP_DB_TYPE);
			String upgradedFromVersion = dbProperties.getProperty(PROP_UPGRADEDFROM);
			String upgradedToVersion = dbProperties.getProperty(PROP_UPGRADEDTO);
			DBTypes dbType = null;		
			
			if (dbUser == null || (dbUser = dbUser.trim()).isEmpty()) {
				throw new RuntimeException("Configuration property " + PROP_USERNAME + " is required");
			}

			if (dbPassword == null || (dbPassword = dbPassword.trim()).isEmpty()) {
				throw new RuntimeException("Configuration property " + PROP_PASSWORD + " is required");
			}

			if (dbHost == null || (dbHost = dbHost.trim()).isEmpty()) {
				throw new RuntimeException("Configuration property " + PROP_HOST + " is required");
			}

			if (dbPortProp == null || (dbPortProp = dbPortProp.trim()).isEmpty()) {
				throw new RuntimeException("Configuration property " + PROP_PORT + " is required");
			}
			
			try {
				dbPort = Integer.parseInt(dbPortProp);
			} catch (NumberFormatException nfe) {
				throw new RuntimeException("Configuration property " + PROP_PORT + " provided must be a number");
			}

			if (dbName == null || (dbName = dbName.trim()).isEmpty()) {
				throw new RuntimeException("Configuration property " + PROP_NAME + " is required");
			}

			if (dbTypeProp == null || (dbTypeProp = dbTypeProp.trim()).isEmpty()) {
				System.out.println("Configuration property " + PROP_DB_TYPE + " is not provided and defaults to \""
						+ DBTypes.MYSQL.name() + "\"");
				dbTypeProp = DBTypes.MYSQL.name();
			}
		
			try {
				dbType = DBTypes.valueOf(dbTypeProp.toUpperCase());
			} catch (Exception e) {
				System.out.println("Error occured while reading DBType "+e.getMessage());
				throw new RuntimeException("dbType value "+ "'" +dbType + "'" +" is not supported");
			}
			
			if (dbType == DBTypes.MSSQL && StringUtils.isEmpty(dbSchemaName)) {
				dbSchemaName = dbName;
			}
			
			dbConnection = dbConnection(dbType, dbUser, dbPassword, dbHost, dbPort, dbConnProps, dbName, dbSchemaName);
			
			DBVERSION = versionInFlyWaySchemaHistory(dbConnection, dbType);
			
			if (upgradedFromVersion != null && upgradedFromVersion.equalsIgnoreCase("202210") && upgradedToVersion.equals("202301")) {
				//User is in 202210 version, hence only 202301 changes need to be updated.
				//User must first migrate to 202301 version.
				if (DBVERSION.equals(v202301)) {
					upgradeTo2301(dbOperationName, dbType);

				} else {
					System.out.println(
							"The database is not migrated to 202301 version." + "\n" + "Currently the version is "
									+ DBVERSION + " Migrate to 202301 version and perform data upgrade");
				}
			}
			else if (StringUtils.isBlank(upgradedFromVersion) && upgradedToVersion.equals("202210")) {
				//User is in 202207 and upgrading to 202210
				//User must first migrate to 202210 version and then run this tool
				if(DBVERSION.equals(v202210)) {
					freshInstallationFor2210(dbOperationName, dbType);
				}else {
					System.out.println("The database is in the verison " +DBVERSION + " Migrate to 202210 version and perform data upgrade");
				}
			}
			else if (StringUtils.isBlank(upgradedFromVersion) && upgradedToVersion.equals("202301")) {
				//User is in 202207 and upgrading to 202301
				//User must first migrate to 202210 version and run this too.
				//After the data is updated for 202210 version, user now should migrate to 202301 version and run the tool again
				if (DBVERSION.equals(v202301)) {
					freshInstallationFor2301(dbOperationName, dbType);
				} else if (!DBVERSION.equals(v202301)) {
					System.out.println("The database is in the verison " + DBVERSION
							+ " Migrate to 202301 version and perform data upgrade");
				}
			}
			else if (upgradedFromVersion != null && upgradedFromVersion.equalsIgnoreCase("202301") && upgradedToVersion.equals("202304")) {
				
				//If upgraded version is 2304 --> only then below method will be executed
				if (DBVERSION.equals(v202304)) {
					alertsDataUpgradeFor2304(dbOperationName, dbType);

				} else {
					System.out.println(
							"The database is not migrated to 202304 version." + "\n" + "Currently the version is "
									+ DBVERSION + " Migrate to 202304 version and perform data upgrade");
				}
			}
			
			else if (StringUtils.isBlank(upgradedFromVersion) && upgradedToVersion.equals("202304")) {
				
					freshInstallationFor2301(dbOperationName, dbType);
					
//					if(DBVERSION.equals(v202304)) {
						alertsDataUpgradeFor2304(dbOperationName, dbType);
//					}
//					else if (!DBVERSION.equals(v202304)){
//							System.out.println("The database is in the verison " +DBVERSION + " Migrate to 202304 version and perform data upgrade");
//					}
			}
			else if (upgradedFromVersion != null && upgradedFromVersion.equalsIgnoreCase("202210") && upgradedToVersion.equals("202304")) {
				//If upgraded version is 2301 --> only then below method will execute
					upgradeTo2301(dbOperationName, dbType);

				//If upgraded version is 2304 --> only then below method will be executed
				if (DBVERSION.equals(v202304)) {
					alertsDataUpgradeFor2304(dbOperationName, dbType);
				} else {
					System.out.println(
							"The database is not migrated to 202304 version." + "\n" + "Currently the version is "
									+ DBVERSION + " Migrate to 202304 version and perform data upgrade");
				}
			}
			else if (StringUtils.isBlank(upgradedFromVersion) && upgradedToVersion.compareTo("202306") >= 0) {
				freshInstallationFor2306(dbOperationName, dbType);
			}

		} catch (Exception e) {
			System.out.println("Error occurred:"+e.getMessage());
			System.exit(1);
		}finally {
        	if( stdout != null) {
        		try {
        			stdout.close();
        		}catch(Exception e1) {
        			System.out.println("Exception while closing stream :"+e1.getMessage());
        		}
        	}
        	if( dbConnection != null) {
        		try {
        			dbConnection.close();
        		}catch(Exception e1) {
        			System.out.println("Exception while closing database connections :"+e1.getMessage());
        		}
        	}
        }	
}

	private static void freshInstallationFor2306( String dbOperationName, DBTypes dbType ) throws Exception{
		if(dbOperationName.equals(createDBObjects)) {
			create2306DBObjects(dbConnection, dbType);
		}
		if(dbOperationName.equals(updateDBObjects)) {
			update2306DBObjects(dbOperationName, dbType);
			update2306Records(dbType);
		}
		if(dbOperationName.equals(reconcileAndCleanDBObjects)) {
			create2306DBObjects(dbConnection, dbType);
			update2306DBObjects(dbOperationName, dbType);
			update2306Records(dbType);
		}
		if(dbOperationName.equals(createMasterData)) {
			create2306DBObjects(dbConnection, dbType);
			createMasterDataForLE(dbConnection, dbType);
			createAlertsMasterDataForLE(dbConnection,dbType);
			update2306Records(dbType);
			create2306NewFeatureActionMultiEntityRecords(dbType);
			delete202306DBObjects(dbConnection,dbType);
		}
	}

	private static void create2306NewFeatureActionMultiEntityRecords( DBTypes dbType ) {
		CallableStatement cs=null;
		try {
			String insertQuery = MultiEntitySqlQueryEnum.valueOf(dbType+"_INSERT_FEATUREACTIONDATA_V2306_PROCEDURE").getQuery();
			String[] insertLeIds = dbProperties.getProperty(PROP_INSERTLEGALENTITY).split(",");

			if(ArrayUtils.isEmpty(insertLeIds)) {
				System.out.println("The input for creating masterdata to a new Legal Entity is empty"+"\n");
				System.out.println("Kindly provide legal entity values in "+ PROP_INSERTLEGALENTITY +" to create master data");
			}else {
				for(int i=0; i<insertLeIds.length;i++) {
					cs = dbConnection.prepareCall(insertQuery);
					cs.setString(1, insertLeIds[i]);
					cs.execute();
				}
			}
		}catch (Exception e) {
			System.out.println("Process failed while creating master data for new LegalEntity:"+e.getMessage());
		}finally {
			if( cs != null) {
				try {
					cs.close();
				}catch(Exception e1) {
					System.out.println("Exception while closing ResultSet :"+e1.getMessage());
				}
			}
		}
	}

	private static void update2306DBObjects( String dbOperationName, DBTypes dbType ) throws Exception {
		updateDBObjects(dbConnection, dbType);
		customerDataUpgrade(dbConnection, dbType);
		boolean status1 = reconcile(dbConnection, dbType);
		boolean status2 = customerUpgradeReconcile(dbConnection, dbType);
		if (status1 && status2) {
			deleteDBObjects(dbConnection, dbType);
		}else {
			System.out.println("Data is not updated properly for master data or customer related tables");
		}
		updateAlertDBObjects(dbConnection, dbType);
		boolean status3 = alertsDataUpdateReconcile(dbConnection, dbType);
		if (status3) {
			deleteAlertsDBObjects(dbConnection, dbType);
		}else {
			System.out.println("Data is not updated properly for alert related tables");
		}
	}

	private static void update2306Records( DBTypes dbType ) {
		CallableStatement cs = null;
		try {
			String upgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_FEATURE_ACTION_UPDATE_PROCEDURE").getQuery();
			cs = dbConnection.prepareCall(upgradeSP);
			cs.execute();
			System.out.println();
			System.out.println("Existing Feature Actions updated successfully");
		}catch(Exception e){
			System.out.println("\n"+"Process failed while updating the Featureactions");
			System.out.println("update process failed with erorr: " + e);
			System.out.println("\n"+"Also please make sure whether the required tables and stored procedures are available in the database");
			System.out.println("If not, run createDBObjects operaion to create the required dB objects.");
		}finally {
			if( cs != null) {
				try {
					cs.close();
				}catch(Exception e1) {
					System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
				}
			}
		}
	}

	private static void create2306DBObjects( Connection dbConnection, DBTypes dbType ) {
		try {
			createDBObjects(dbConnection, dbType, "2210");
		} catch ( Exception e) {
			System.out.println("Failed to create v 2210 DB Objects");
			return;
		}
		try {
			createDBObjects(dbConnection, dbType, "2301");
		} catch ( Exception e ) {
			System.out.println("Failed to create v 2301 DB Objects");
			return;
		}
		try {
			createDBObjects(dbConnection, dbType, "2304");
		} catch ( Exception e ) {
			System.out.println("Failed to create v 2304 DB Objects");
			return;
		}
		try {
			createDBObjects(dbConnection, dbType, "2306");
		} catch ( Exception e ) {
			System.out.println("Failed to create v 2306 DB Objects");
		}
	}
	private static void delete202306DBObjects(Connection con, DBTypes dbType) {
		deleteDBObjects(con, dbType);
		deleteAlertsDBObjects(con, dbType);
		String deleteMultiUpdateSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETE202306MULTIENTITYDATAUPGRADE").getQuery();
	    String deleteUpdateOldFeatureSP202306 = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETEUPDATEACTIONLEVELOFOLDFEATUREACTIONS202306").getQuery();
	    CallableStatement cs = null;
	    try {
			cs = con.prepareCall(deleteMultiUpdateSP);
			cs.execute();
		} catch (Exception e) {
			System.out.println("Failed to delete stored procedure. "+e.getMessage());
		}
	    try {
		    cs = con.prepareCall(deleteUpdateOldFeatureSP202306);
		    cs.execute();
	    } catch (Exception e) {
			System.out.println("Failed to delete stored procedure. "+e.getMessage());
		}	    
	}
	private static void freshInstallationFor2301(String dbOperationName, DBTypes dbType) throws Exception {
	
			if (dbOperationName.equals(createDBObjects)) {
				createDBObjects(dbConnection, dbType, "2301");	
			}
			if (dbOperationName.equals(updateDBObjects)) {
				updateDBObjects(dbConnection, dbType);
				customerDataUpgrade(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileDBObjects)) {
				reconcile(dbConnection, dbType);
				customerUpgradeReconcile(dbConnection, dbType);
			}
			if (dbOperationName.equals(deleteDBObjects)) {
				deleteDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(createMasterData)) {
				createDBObjects(dbConnection, dbType, "2301");
				createMasterDataForLE(dbConnection, dbType);
				deleteDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileAndCleanDBObjects)) {
				createDBObjects(dbConnection, dbType, "2301");
				updateDBObjects(dbConnection, dbType);
				customerDataUpgrade(dbConnection, dbType);
				boolean status1 = reconcile(dbConnection, dbType);
				boolean status2 = customerUpgradeReconcile(dbConnection, dbType);
				if (status1 && status2) {
					deleteDBObjects(dbConnection, dbType);
				}else {
					System.out.println("Data is not updated properly");
				}	
			}
		
	}
	
	private static void alertsDataUpgradeFor2304(String dbOperationName, DBTypes dbType) throws Exception {
	
			if (dbOperationName.equals(createDBObjects)) {
				createDBObjects(dbConnection, dbType, "2304");	
			}
			if (dbOperationName.equals(updateDBObjects)) {
				updateAlertDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileDBObjects)) {
				alertsDataUpdateReconcile(dbConnection, dbType);
			}
			if (dbOperationName.equals(deleteDBObjects)) {
				deleteAlertsDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(createMasterData)) {
				createDBObjects(dbConnection, dbType, "2304");
				createAlertsMasterDataForLE(dbConnection, dbType);
				deleteAlertsDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileAndCleanDBObjects)) {
				createDBObjects(dbConnection, dbType, "2304");
				updateAlertDBObjects(dbConnection, dbType);
				boolean status1 = alertsDataUpdateReconcile(dbConnection, dbType);
				if (status1) {
					deleteAlertsDBObjects(dbConnection, dbType);
				}else {
					System.out.println("Data is not updated properly");
				}	
			}
		
	}

	private static void freshInstallationFor2210(String dbOperationName, DBTypes dbType) throws Exception {
		
			if (dbOperationName.equals(createDBObjects)) {
				createDBObjects(dbConnection, dbType, "2210");	
			}
			if (dbOperationName.equals(updateDBObjects)) {
				updateDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileDBObjects)) {
				reconcile(dbConnection, dbType);
			}
			if (dbOperationName.equals(deleteDBObjects)) {
				deleteDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(createMasterData)) {
				createDBObjects(dbConnection, dbType, "2210");
				createMasterDataForLE(dbConnection, dbType);
				deleteDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileAndCleanDBObjects)) {
				createDBObjects(dbConnection, dbType, "2210");
				updateDBObjects(dbConnection, dbType);
				boolean status = reconcile(dbConnection, dbType);
				if (status) {
					deleteDBObjects(dbConnection, dbType);
				}else {
					System.out.println("Data is not updated properly");
				}	
			}
			
	}

	private static void upgradeTo2301(String dbOperationName, DBTypes dbType) throws Exception {
		
			if (dbOperationName.equals(createDBObjects)) {
				createDBObjects(dbConnection, dbType, "2301");	
			}
			if (dbOperationName.equals(updateDBObjects)) {
				customerDataUpgrade(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileDBObjects)) {
				customerUpgradeReconcile(dbConnection, dbType);
			}
			if (dbOperationName.equals(deleteDBObjects)) {
				deleteDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(createMasterData)) {
				createDBObjects(dbConnection, dbType, "2301");
				createMasterDataForLE(dbConnection, dbType);
				deleteDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileAndCleanDBObjects)) {
				createDBObjects(dbConnection, dbType, "2301");
				customerDataUpgrade(dbConnection, dbType);
				boolean status = customerUpgradeReconcile(dbConnection, dbType);
				if (status) {
					deleteDBObjects(dbConnection, dbType);
				}else {
					System.out.println("Data is not updated properly");
				}	
			}
	}
	/*
	private static void upgradeTo2304(String dbOperationName, DBTypes dbType) throws Exception {
		
			if (dbOperationName.equals(createDBObjects)) {
				createDBObjects(dbConnection, dbType, "2304");	
			}
			
			if (dbOperationName.equals(createDBObjects)) {
				createDBObjects(dbConnection, dbType, "2304");	
			}
			
			if (dbOperationName.equals(deleteDBObjects)) {
				deleteAlertsDBObjects(dbConnection, dbType);
			}
			if (dbOperationName.equals(createMasterData)) {
				createAlertsMasterDataForLE(dbConnection, dbType);
			}
			if (dbOperationName.equals(reconcileAndCleanDBObjects)) {
				createDBObjects(dbConnection, dbType, "2304");
				customerDataUpgrade(dbConnection, dbType);
				boolean status = customerUpgradeReconcile(dbConnection, dbType);
				if (status) {
					deleteAlertsDBObjects(dbConnection, dbType);
				}else {
					System.out.println("Data is not updated properly");
				}	
			}
	}
	*/
		
private static void loadProperties(String dbConfigProperty) throws Exception{
	    FileInputStream file = null;
		   //the base folder is ./, the root of the config.properties file  
	    try {
	    	file = new FileInputStream(dbConfigProperty);
		    dbProperties.load(file);
		    file.close();
	    }catch (Exception e) {
	    	System.out.println("Error Occurred while loading property file."+e.getMessage());
			file.close();
		}
	}

private static Connection dbConnection(DBTypes dbType, String dbUser, String dbPassword, String dbHost, int dbPort,
		String connectionPropsStr, String dbName, String dbSchemaName) throws Exception{
	Connection Con = null;
	try {
		String jdbcURL = dbType.replaceInJDBCURL(dbHost, dbPort, connectionPropsStr, dbName);
		String driverManager = dbType.getDriverManager();
		Class.forName(driverManager);
		System.out.println(dbType+" Driver Opened");
		Connection con=DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
		System.out.println("Driver connection established successfully");
		return con;
	}catch (Exception e){ 
		System.out.println("Error occured while creating database connection"+e.getMessage());
	}
	return Con;  
}

private static void createDBObjects(Connection con, DBTypes dbType, String dbVersion) throws Exception {
	
		final String LINE_SEPARATOR = "\n";
		StringBuilder script = new StringBuilder();
		Statement stmnt = null;
		String dbTypeString=dbType.toString();
		String filePath="";
		if(dbTypeString.equals("MYSQL") && (dbVersion.equals("2210") || dbVersion.equals("2301"))) {
			filePath = MultiEntitySqlQueryEnum.valueOf(dbType+"_SCRIPTSFILEPATH").getQuery();
		}
		else if(dbTypeString.equals("MSSQL")  && (dbVersion.equals("2210") || dbVersion.equals("2301"))) {
			filePath = MultiEntitySqlQueryEnum.valueOf(dbType+"_SCRIPTSFILEPATH").getQuery();
		}
		else if(dbTypeString.equals("MYSQL") && dbVersion.equals("2304")) {
			filePath = MultiEntitySqlQueryEnum.valueOf(dbType+"_SCRIPTS_V2304_FILEPATH").getQuery();
		}
		else if(dbTypeString.equals("MSSQL")  && dbVersion.equals("2304")) {
			filePath = MultiEntitySqlQueryEnum.valueOf(dbType+"_SCRIPTS_V2304_FILEPATH").getQuery();
		}
		else if(dbTypeString.equals("MYSQL") && dbVersion.equals("2306")) {
			filePath = MultiEntitySqlQueryEnum.valueOf(dbType + "_SCRIPTS_V2306_FILEPATH").getQuery();
		}
		else if(dbTypeString.equals("MSSQL") && dbVersion.equals("2306")) {
			filePath = MultiEntitySqlQueryEnum.valueOf(dbType + "_SCRIPTS_V2306_FILEPATH").getQuery();
		}
		InputStream inputStream = DatabaseUpdater.class.getClassLoader().getResourceAsStream(filePath);
		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		stmnt = con.createStatement();
		try {
			String line = new String();
			BufferedReader lineReader = new BufferedReader(streamReader);
			while ((line = lineReader.readLine()) != null) {
				 line = line.trim();
					if (line.startsWith("$$") || line.equalsIgnoreCase("GO")) {
						stmnt.execute(script.toString());
						script.setLength(0);
					} else if (line.length() > 0) {
						script.append(line);
						script.append(LINE_SEPARATOR);
					}
			}
			lineReader.close();
		System.out.println("\n"+"All the queries are executed and the respective tables and procedures are created successfully");
	}catch(Exception e){ 
		System.out.println("Process failed while creating the dB Objects: "+e.getMessage());
	}finally {
	      if (stmnt != null) {
	          try {
	            stmnt.close();
	          } catch (Exception e1) {
	            System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
	          }  
	      }
	      if (streamReader != null) {
	    	  try {
	    		  streamReader.close();
	    	  } catch (Exception e1) {
		            System.out.println("Exception while closing StreamReader :"+e1.getMessage());
		          }  
	      }
	      if (inputStream != null) {
	    	  try {
	    		  inputStream.close();
	    	  } catch (Exception e1) {
		            System.out.println("Exception while closing InputStream :"+e1.getMessage());
		          } 
	      }
	      } 
}
private static void updateDBObjects(Connection con, DBTypes dbType) throws Exception {
	CallableStatement cs = null;
	try {
		String upgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_UPGREADEPROCEDURE").getQuery();
		String upgradeLE = dbProperties.getProperty(PROP_UPDATELEGALENTITY);
		String jsonConfigValue = dbProperties.getProperty(PROP_JSONCONFIGVALUE);
		cs = con.prepareCall(upgradeSP);
		cs.setString(1, upgradeLE);
		cs.setString(2, jsonConfigValue);
		cs.execute();
		System.out.println();
		System.out.println("companyLegalUnit " + upgradeLE + " updated successfully in all the tables");
	}catch(Exception e){ 
		System.out.println("\n"+"Process failed while updating the LegalEntity");
		System.out.println("update process failed with erorr: " + e);
		System.out.println("\n"+"Also please make sure whether the required tables and stored procedures are available in the database");
        System.out.println("If not, run createDBObjects operaion to create the required dB objects.");
	}finally {
    	if( cs != null) {
    		try {
    			cs.close();
    		}catch(Exception e1) {
    			System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
    		}
    	}
    }	 
	createFinancialInstituteMasterData(con, dbType);
}

private static void updateAlertDBObjects(Connection con, DBTypes dbType) throws Exception {
	CallableStatement cs = null;
	try {
		String upgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_UPGREADEALERTSPROCEDURE").getQuery();
		String upgradeLE = dbProperties.getProperty(PROP_UPDATELEGALENTITY);
		
		cs = con.prepareCall(upgradeSP);
		cs.setString(1, upgradeLE);
		cs.execute();
		System.out.println();
		System.out.println("companyLegalUnit " + upgradeLE + " updated successfully in all the alerts tables");
	}catch(Exception e){ 
		System.out.println("\n"+"Process failed while updating the LegalEntity");
		System.out.println("update process failed with erorr: " + e);
		System.out.println("\n"+"Also please make sure whether the required alert tables and stored procedures are available in the database");
        System.out.println("If not, run createDBObjects operaion to create the required dB objects.");
	}finally {
    	if( cs != null) {
    		try {
    			cs.close();
    		}catch(Exception e1) {
    			System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
    		}
    	}
    }	 
}

private static boolean reconcile(Connection con, DBTypes dbType) {
	 boolean status = false;
	 CallableStatement cs = null;
	 ResultSet rs = null;
	try {
		String reconcileSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_RECONCILEPROCEDRUE").getQuery();
        String upgradeLE = dbProperties.getProperty(PROP_UPDATELEGALENTITY);
        cs = con.prepareCall(reconcileSP);
        rs = cs.executeQuery();
        ArrayList<Object> tablenames = new ArrayList<>();
        while (rs.next()){
        	if (StringUtils.isNotBlank(rs.getString(1))) {
        		tablenames.add(rs.getString(1));
        	}
        }
        System.out.println();
        
        if(!tablenames.isEmpty()) {
        	status = false;
        	System.out.println("CompanyLegalUnit " + upgradeLE + " is not updated properly in the database"+"\n");
        	System.out.println("The tables with companyLegalUnit 'ALL' or NULL are listed below");
        	System.out.println(tablenames);
        }else if(tablenames.isEmpty()) {
        	System.out.println();
        	System.out.println("The reconciliation procedure executed successfully");
        	System.out.println("and companyLegalUnit " + upgradeLE + " has been updated successfully in the dB");
        	status = true;
        }
    }catch(Exception e) {
    	System.out.println("Failed to reconcile database tables.");
    	System.out.println("\n"+"Reconciliation process failed with the erorr " + e.getMessage());
        System.out.println("\n"+"Also please make sure, whether the required tables and stored procedures are available in the database");
        System.out.println("If not, kindly run createDBObjects operaion to create the required dB objects.");
    }finally {
        if (rs != null)
            try {
              rs.close();
            } catch (Exception e1) {
              System.out.println("Exception while closing ResultSet :"+e1.getMessage());
            }  
          if (cs != null)
            try {
              cs.close();
            } catch (Exception e1) {
              System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
            }  
        } 
	return status;
}

private static boolean alertsDataUpdateReconcile(Connection con, DBTypes dbType) {
	 boolean status = false;
	 CallableStatement cs = null;
	 ResultSet rs = null;
	try {
	   String reconcileSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_ALERTSDATAUPGRADERECONCILEPROCEDRUE").getQuery();
       String upgradeLE = dbProperties.getProperty(PROP_UPDATELEGALENTITY);
       cs = con.prepareCall(reconcileSP);
       rs = cs.executeQuery();
       ArrayList<Object> tablenames = new ArrayList<>();
       while (rs.next()){
       	if (StringUtils.isNotBlank(rs.getString(1)))  {
       		tablenames.add(rs.getString(1));
       	}
       }
       System.out.println();
       
       if(!tablenames.isEmpty()) {
       	status = false;
       	System.out.println("CompanyLegalUnit " + upgradeLE + " is not updated properly in the database"+"\n");
       	System.out.println("The alert tables with companyLegalUnit 'ALL' or NULL are listed below");
       	System.out.println(tablenames);
       }else if(tablenames.isEmpty()) {
       	System.out.println();
       	System.out.println("The reconciliation procedure executed successfully");
       	System.out.println("and companyLegalUnit " + upgradeLE + " has been updated successfully in the alert tables");
       	status = true;
       }
   }catch(Exception e) {
   	System.out.println("Failed to reconcile database alert tables.");
   	System.out.println("\n"+"Reconciliation process failed with the erorr " + e.getMessage());
       System.out.println("\n"+"Also please make sure, whether the required tables and stored procedures are available in the database");
       System.out.println("If not, kindly run createDBObjects operaion to create the required dB objects.");
   }finally {
       if (rs != null)
           try {
             rs.close();
           } catch (Exception e1) {
             System.out.println("Exception while closing ResultSet :"+e1.getMessage());
           }  
         if (cs != null)
           try {
             cs.close();
           } catch (Exception e1) {
             System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
           }  
       } 
	return status;
}

private static void deleteDBObjects(Connection con, DBTypes dbType) {
	CallableStatement cs = null;
	Statement stmt = null;
	 try {
		 	String deleteTable = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETETABLE").getQuery();
		    String deleteUpgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETEUPGRADESP").getQuery();
		    String deleteReconcileSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETERECONCILESP").getQuery();
		    String deleteCustomerUpgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETECUSTOMERUPGRADESP").getQuery();
		    String deleteInsertSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETECREATESP").getQuery();
		    String deleteCustomerUpgradeReconcileSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETECUSTOMERUPGRADERECONCILESP").getQuery();
	
		    stmt = con.createStatement();
		    stmt.execute(deleteTable);
		    System.out.println();
		    System.out.println("The temporary table 'tablelist' is deleted successfully");
	
		    cs = con.prepareCall(deleteUpgradeSP);
		    cs.execute();
		    System.out.println("MultiEntity data upgrade procedure is deleted successfully");
		    cs.close();

		    cs = con.prepareCall(deleteReconcileSP);
		    cs.execute();
		    cs.close();
		    
		    cs = con.prepareCall(deleteCustomerUpgradeSP);
		    cs.execute();
		    System.out.println("customer data upgrade procedure is deleted successfully");
		    cs.close();
		    
		    cs = con.prepareCall(deleteCustomerUpgradeReconcileSP);
		    cs.execute();
		    System.out.println("Reconcile procedures are deleted successfully");
		    cs.close();
		    
		    cs = con.prepareCall(deleteInsertSP);
		    cs.execute();
		    System.out.println("Master data create procedure is deleted successfully");
		    
		} catch(Exception e) {
			System.out.println("Failed to clean db objects.");
			System.out.println("Delete operation failed with error: "+e.getMessage());
		} finally {
		      if (cs != null)
		          try {
		            cs.close();
		          } catch (Exception e1) {
		            System.out.println("Exception while closing ResultSet :"+e1.getMessage());
		          }  
		        if (stmt != null)
		          try {
		            stmt.close();
		          } catch (Exception e1) {
		            System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
		          }  
		      } 
}

private static void deleteAlertsDBObjects(Connection con, DBTypes dbType) {
	CallableStatement cs = null;
	Statement stmt = null;
	 try {
		 	String deleteTable = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETEALERTTABLE").getQuery();
		    String deleteUpgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETEALERTSDATAUPGRADESP").getQuery();
		    String deleteReconcileSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETEALERTSDATAUPGRADERECONCILESP").getQuery();
		    String deleteInsertSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_DELETE202304MASTERDATACREATESP").getQuery();
	
		    stmt = con.createStatement();
		    stmt.execute(deleteTable);
		    System.out.println();
		    System.out.println("The temporary table 'alerttablelist' is deleted successfully");
	
		    cs = con.prepareCall(deleteUpgradeSP);
		    cs.execute();
		    System.out.println("MultiEntity data upgrade for alerts procedure is deleted successfully");
		    cs.close();

		    cs = con.prepareCall(deleteReconcileSP);
		    cs.execute();
		    cs.close();
		    
		    cs = con.prepareCall(deleteInsertSP);
		    cs.execute();
		    System.out.println("Master data for alerts create procedure is deleted successfully");
		    
		} catch(Exception e) {
			System.out.println("Failed to clean db objects.");
			System.out.println("Delete operation failed with error: "+e.getMessage());
		} finally {
		      if (cs != null)
		          try {
		            cs.close();
		          } catch (Exception e1) {
		            System.out.println("Exception while closing ResultSet :"+e1.getMessage());
		          }  
		        if (stmt != null)
		          try {
		            stmt.close();
		          } catch (Exception e1) {
		            System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
		          }  
		      } 
}

private static void customerDataUpgrade(Connection con, DBTypes dbType) {
	CallableStatement cs = null;
	try {
		String upgradeSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_CUSTOMERDATAUPGRADEPROCEDURE").getQuery();	
		cs = con.prepareCall(upgradeSP);
		cs.execute();
		System.out.println();
		System.out.println("homeLegalEntity value has been updated in customer Table");
		System.out.println("Customer data has been inserted into 'customerlegalentity' table");
	}catch (Exception e) {
		System.out.println("\n"+"Process failed while updating the 202301 db changes: "+e.getMessage());
	}finally {
	      if (cs != null)
	          try {
	            cs.close();
	          } catch (Exception e1) {
	            System.out.println("Exception while closing ResultSet :"+e1.getMessage());
	          }  
	      } 
}

private static boolean customerUpgradeReconcile(Connection con, DBTypes dbType) {
	boolean status = true;
	CallableStatement cs=null;
	ResultSet rs = null;
	try {
		String reconcileSP = MultiEntitySqlQueryEnum.valueOf(dbType+"_CUSTOMERDATAUPGRADERECONCILESP").getQuery();
        cs = con.prepareCall(reconcileSP);  
        try {
        	rs = cs.executeQuery();
        }catch (SQLException e) {
        	System.out.println("Failed to reconcile database tables."+e.getMessage());           
            System.out.println("\n"+"Please make sure whether the required tables and stored procedures are available in the database");
            System.out.println("Kindly run createDBObjects operaion to create the required dB objects.");
		}
        
        String delimeter = "~";
        String[] idsArray;
        String customerIds=null;
		String customerLegalEntities=null;
        if(rs.next()) {
        	String idsList = rs.getString(1);
        	if(idsList == null || "".equals(idsList)) {
        		System.out.println("Customer Table updated properly"+"\n"+"Data inserted properly in customerlegalEntities for all customerIds");
        	}
        	else {
        		idsArray = idsList.split(delimeter);
        		if(idsArray.length>0) {
        			customerIds=idsArray[0];
        		}
        		if(idsArray.length>1) {
        			customerLegalEntities=idsArray[1]; 
        		}
        	}
        	if(customerLegalEntities==null || "".equals(customerLegalEntities)) {
        		System.out.println("Data inserted properly in customerlegalentity table");
        	}else {
   			 	System.out.println("\n"+"Data is not inserted in customerlegalentity table, for the below customerIds "+"\n"+customerLegalEntities.substring(0,customerLegalEntities.length()-1));
   			 status = false;
   		 	}
   		 
   		 	if(customerIds==null || "".equals(customerIds)) {
   		 		System.out.println("homeLegalEntity is properly updated in customer table");
   		 	}else {
   			 System.out.println("\n"+"homeLegalEntity is not synced with companyLegalUnit for the below customerIds in customer table: "+"\n"+customerIds.substring(0,customerIds.length()-1));
   			status = false;
   		 	}	
        }   
    }catch(Exception e) {
    	System.out.println("Failed to reconcile database tables."+e.getMessage());
    }finally {
    	if( rs != null) {
    		try {
    			rs.close();
    		}catch(Exception e1) {
    			System.out.println("Exception while closing ResultSet :"+e1.getMessage());
    		}
    	}
    	if( cs != null) {
    		try {
    			cs.close();
    		}catch(Exception e1) {
    			System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
    		}
    	}
    }	
	return status;
}
private static String versionInFlyWaySchemaHistory(Connection con, DBTypes dbType) {
	String version = "";
	ResultSet rs = null;
	Statement stmt = null;
	try {
		String versionQuery =MultiEntitySqlQueryEnum.valueOf(dbType+"_FLYWAYSCHEMAVERSION").getQuery();
		stmt = con.createStatement();
		rs = stmt.executeQuery(versionQuery);
		while(rs.next()) {
			version = rs.getString(1);
		}		
	}catch (Exception e) {		
		System.out.println("Failed to get the data base version: "+e.getMessage());
	}finally {
	      if (rs != null)
	          try {
	            rs.close();
	          } catch (Exception e1) {
	            System.out.println("Exception while closing ResultSet :"+e1.getMessage());
	          }  
	        if (stmt != null)
	          try {
	            stmt.close();
	          } catch (Exception e1) {
	            System.out.println("Exception while closing SQL Statement :"+e1.getMessage());
	          }  
	      } 
	return version;	
}

private static void createMasterDataForLE(Connection con, DBTypes dbType){
	CallableStatement cs=null;
	try {
		String insertQuery = MultiEntitySqlQueryEnum.valueOf(dbType+"_DATAINSERTPROCEDURE").getQuery();
		String[] insertLeIds = dbProperties.getProperty(PROP_INSERTLEGALENTITY).split(",");
		
		if(ArrayUtils.isEmpty(insertLeIds)) {
			System.out.println("The input for creating masterdata to a new Legal Entity is empty"+"\n");
			System.out.println("Kindly provide legal entity values in "+ PROP_INSERTLEGALENTITY +" to create master data");			
		}else {
			for(int i=0; i<insertLeIds.length;i++) {
				cs = con.prepareCall(insertQuery);
				cs.setString(1, insertLeIds[i]);
				cs.execute();
			}
		}
	}catch (Exception e) {
		System.out.println("Process failed while creating master data for new LegalEntity:"+e.getMessage());
	}finally {
    	if( cs != null) {
    		try {
    			cs.close();
    		}catch(Exception e1) {
    			System.out.println("Exception while closing ResultSet :"+e1.getMessage());
    		}
    	}
	}
	createFinancialInstituteMasterData(con,dbType);
}

private static void createAlertsMasterDataForLE(Connection con, DBTypes dbType){
	CallableStatement cs=null;
	try {
		String insertQuery = MultiEntitySqlQueryEnum.valueOf(dbType+"_202304MASTERDATAINSERTPROCEDURE").getQuery();
		String[] insertLeIds = dbProperties.getProperty(PROP_INSERTLEGALENTITY).split(",");
		
		if(ArrayUtils.isEmpty(insertLeIds)) {
			System.out.println("The input for creating masterdata for alerts to a new Legal Entity is empty"+"\n");
			System.out.println("Kindly provide legal entity values in "+ dbProperties.getProperty(PROP_INSERTLEGALENTITY) +" to create master data for alerts");			
		}else {
			for(int i=0; i<insertLeIds.length;i++) {
				cs = con.prepareCall(insertQuery);
				cs.setString(1, insertLeIds[i]);
				cs.execute();
			}
			System.out.println("Master data created successfully for the given legalentity "+dbProperties.getProperty(PROP_INSERTLEGALENTITY));
		}
	}catch (Exception e) {
		System.out.println("Process failed while creating master data for new LegalEntity:"+e.getMessage());
	}finally {
    	if( cs != null) {
    		try {
    			cs.close();
    		}catch(Exception e1) {
    			System.out.println("Exception while closing ResultSet :"+e1.getMessage());
    		}
    	}
	}
}

private static void createFinancialInstituteMasterData(Connection con, DBTypes dbType) {
	CallableStatement cs = null;
	ArrayList<String> existingFinKeys = getExistingFinKeys(con, dbType);
	boolean isSingleEntity = checkIfSingleEntity(con, dbType);
	if(isSingleEntity && existingFinKeys.size() >= 1) {
		System.out.println("Financial institute master data already exists as configuration is set to single entity.");
		return;
	}
	try {
		String insertQuery = MultiEntitySqlQueryEnum.valueOf(dbType + "_FINANCIALINSTITUTE_DATAINSERT_PROC").getQuery();
		String jsonConfigValue = dbProperties.getProperty(PROP_JSONCONFIGVALUE);
		boolean isUpdated = false;
		JSONArray ConfigJsonArray = new JSONArray(jsonConfigValue);

		for (int i = 0; i < ConfigJsonArray.length(); i++) {

			JSONObject obj = ConfigJsonArray.getJSONObject(i);

			String name = getValueFromKey(obj, "companyName");
			String countryCode = getValueFromKey(obj, "countryCode");
			String baseCurrency = getValueFromKey(obj, "baseCurrency");
			String language = getValueFromKey(obj, "language");
			String effectiveDate = getValueFromKey(obj, "effectiveDate");
			String closeDate = getValueFromKey(obj, "closeDate");
			String alternateKey = getValueFromKey(obj, "id");
			
			if(existingFinKeys.contains(alternateKey)) {
				System.out.println("Financial institute master data already exists for LegalEntity: "+alternateKey);
				continue;
			}

			String finInstitutionId = obj.has("finInstitutionId") ? obj.getString("finInstitutionId")
					: "LE" + getUniqueIdString();
			String shortName = obj.has("shortName") ? obj.getString("shortName") : name;
			String typeId = obj.has("typeId") ? obj.getString("typeId") : "LEGALENTITY";
			String parentId = obj.has("parentId") ? obj.getString("parentId") : "";
			String comments = obj.has("description") ? obj.getString("description") : "";
			String alternateName = obj.has("alternateName") ? obj.getString("alternateName") : "coreBankingSystemId";

			cs = con.prepareCall(insertQuery);

			cs.setString(1, finInstitutionId);
			cs.setString(2, name);
			cs.setString(3, shortName);
			cs.setString(4, typeId);
			cs.setString(5, parentId);
			cs.setString(6, countryCode);
			cs.setString(7, baseCurrency);
			cs.setString(8, language);
			cs.setString(9, effectiveDate);
			cs.setString(10, closeDate);
			cs.setString(11, comments);
			cs.setString(12, alternateName);
			cs.setString(13, alternateKey);

			cs.execute();
			isUpdated = true;
			System.out.println("Financial institute master data successfully created for LegalEntity: "+alternateKey);
		}
		if(!isUpdated) {
			System.out.println("Process failed while creating Financial institute master data as provided Entities exist in system.");
		}

	} catch (Exception e) {
		System.out.println(
				"Process failed while creating Financial institute master data for new LegalEntity:" + e.getMessage());
	} finally {
		if (cs != null) {
			try {
				cs.close();
			} catch (Exception e1) {
				System.out.println("Exception while closing ResultSet :" + e1.getMessage());
			}
		}
	}
}

private static boolean checkIfSingleEntity(Connection con, DBTypes dbType) {

	boolean isSingleEntity = false;
	ResultSet rs = null;
	Statement stmt = null;
	System.out.println("Checking entity configuration...");
	try {
		String singleEntityQuery = MultiEntitySqlQueryEnum.valueOf(dbType + "_SINGLE_ENTITY_GET").getQuery();
		stmt = con.createStatement();
		rs = stmt.executeQuery(singleEntityQuery);
		while (rs.next()) {
			String res = rs.getString(1);
			isSingleEntity = StringUtils.equals(res, "1") || StringUtils.equals(res, "true") ? true : false;
		}
	} catch (Exception e) {
		System.out.println("Failed to get the data base version: " + e.getMessage());
	} finally {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e1) {
				System.out.println("Exception while closing ResultSet :" + e1.getMessage());
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (Exception e1) {
				System.out.println("Exception while closing SQL Statement :" + e1.getMessage());
			}
	}
	System.out.println("Single entity Configuration: " + isSingleEntity);
	return isSingleEntity;
}

private static ArrayList<String> getExistingFinKeys(Connection con, DBTypes dbType) {
	ArrayList<String> existingFinKeys = new ArrayList<>();
	ResultSet rs = null;
	Statement stmt = null;
	try {
		String finKeysQuery = MultiEntitySqlQueryEnum.valueOf(dbType + "_FINANCIAL_KEYS_GET").getQuery();
		stmt = con.createStatement();
		rs = stmt.executeQuery(finKeysQuery);
		while (rs.next()) {
			existingFinKeys.add(rs.getString(1));
		}
	} catch (Exception e) {
		System.out.println("Failed to get the data base version: " + e.getMessage());
	} finally {
		if (rs != null)
			try {
				rs.close();
			} catch (Exception e1) {
				System.out.println("Exception while closing ResultSet :" + e1.getMessage());
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (Exception e1) {
				System.out.println("Exception while closing SQL Statement :" + e1.getMessage());
			}
	}

	return existingFinKeys;
}

private static String getValueFromKey(JSONObject obj, String key) throws Exception {
	String value = obj.has(key) ? obj.getString(key) : "";
	if (StringUtils.isBlank(value)) {
		throw new Exception("Either Key :" + key + " or value :" + value + " is null");
	}
	return value;
}

private static String getUniqueIdString() {
	return java.util.UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
}

}

