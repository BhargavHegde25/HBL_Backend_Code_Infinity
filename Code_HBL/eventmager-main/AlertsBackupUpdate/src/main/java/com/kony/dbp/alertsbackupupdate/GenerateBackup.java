package com.kony.dbp.alertsbackupupdate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import com.kony.dbp.alertsbackupupdateutil.AlertsBackupUpdateConstants;
import com.kony.dbp.alertsbackupupdateutil.PropertiesCache;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class GenerateBackup {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
	public static boolean dumpDB() {

		DatabaseMetaData dbMetaData = null;
		String url = PropertiesCache.getInstance().getProperty("MYSQL_DB_URL");
		String user = PropertiesCache.getInstance().getProperty("MYSQL_DB_USERNAME");
		String password = PropertiesCache.getInstance().getProperty("MYSQL_DB_PASSWORD");
		try (Connection dbConn = DriverManager.getConnection(url, user, password)) {
			dbMetaData = dbConn.getMetaData();

			String tables = AlertsBackupUpdateConstants.TABLES;

			if (tables == null)
				return true;

			String[] tablearray = tables.split(",");
			for (int i = 0; i < tablearray.length; i++) {
				try (ResultSet rs = dbMetaData.getTables(null, null, tablearray[i], null)) {
					// To get all table and schema, we can use null
					// to loop all ResultSet rs = dbMetaData.getTables(null, null, null, null);

					if (!rs.next()) {
						System.err.println("Unable to find any tables matching:" + tables);
					} else {
						do {
							String tableName = rs.getString("TABLE_NAME");
							String tableType = rs.getString("TABLE_TYPE");
							if ("TABLE".equalsIgnoreCase(tableType)) {
								dumpTable(dbConn, tableName);
							}
						} while (rs.next());
					}
				}
			}

		} catch (Exception e) {
			alert.prepareError("Exception", e).log();
			return false;
		}
		return true;

	}

	/** dump this particular table to the string buffer */
	private static void dumpTable(Connection dbConn, String tableName) {

		StringBuilder result = new StringBuilder();
		// First we output the create table stuff
		try (PreparedStatement stmt = dbConn.prepareStatement("SELECT * FROM " + tableName)) {
			try (ResultSet rs = stmt.executeQuery()) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				// Now we can output the actual data
				result.append("\n\n-- Data for " + tableName + "\n");
				while (rs.next()) {
					result.append("INSERT INTO " + tableName + " VALUES (");
					for (int i = 0; i < columnCount; i++) {

						int datatype = metaData.getColumnType(i + 1);
						if (i > 0) {
							result.append(", ");
						}
						Object value = rs.getObject(i + 1);
						if (value == null) {
							result.append("NULL");
						} else {
							String outputValue = value.toString();
							if ((datatype == -7 || datatype == -6) && outputValue.equals("true")) {
								outputValue = "1";
								result.append(outputValue);
							} else if ((datatype == -7 || datatype == -6) && outputValue.equals("false")) {
								outputValue = "0";
								result.append(outputValue);
							} else {
								outputValue = outputValue.replace("'", "\\'");
								result.append("'" + outputValue + "'");
							}
						}
					}
					result.append(");\n");
				}
				writeTofile(result, tableName);
			}
		} catch (SQLException e) {
			System.err.println("Unable to dump table " + tableName + " because: " + e);
		}
	}

	public static File createTempDir(String dirname) {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));

		File tempDir = new File(baseDir, dirname);
		if (tempDir.mkdir()) {
			return tempDir;
		}
		return tempDir;

	}

	public static File createTempFile(File baseDir, String dirname) {
		File tempDir = new File(baseDir, dirname);
		try {
			if (tempDir.createNewFile()) {
				return tempDir;
			}
		} catch (IOException e) {
			alert.prepareError("Exception", e).log();
		}
		return null;

	}

	public static void writeTofile(StringBuilder result, String tablename) {

		File folder = createTempDir("AlertsDatabaseScripts");
		File file = createTempFile(folder, tablename + ".sql");
		try {
			if (file != null) {
				try (FileWriter fo = new FileWriter(file)) {
					fo.write(result.toString());
				}
			}
		} catch (IOException e) {
			alert.prepareError("Exception", e).log();
		}
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.io.tmpdir"));

	}

}
