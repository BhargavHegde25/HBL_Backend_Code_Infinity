package com.kony.dbp.alertsbackupupdate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import com.kony.dbp.alertsbackupupdateutil.AlertsBackupUpdateQueries;
import com.kony.dbp.alertsbackupupdateutil.PropertiesCache;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class UpdateAlertsData {

	private UpdateAlertsData() {
	}
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	public static boolean updateAlertScripts() {
		String url = PropertiesCache.getInstance().getProperty("MYSQL_DB_URL");
		String user = PropertiesCache.getInstance().getProperty("MYSQL_DB_USERNAME");
		String password = PropertiesCache.getInstance().getProperty("MYSQL_DB_PASSWORD");
		String outerquery = null;
		try (Connection dbConn = DriverManager.getConnection(url, user, password)) {
			dbConn.setAutoCommit(false);
			for (String query : AlertsBackupUpdateQueries.queries) {
				outerquery = query;
				try (PreparedStatement stmt = dbConn.prepareStatement(query)) {
					stmt.execute();

				}
			}
			dbConn.commit();
		} catch (Exception e) {
			System.out.println("Error occured while executing query: \n\n" + outerquery+"\n\n");
			alert.prepareError("Error occurred: ", e).log();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		updateAlertScripts();
	}

}
