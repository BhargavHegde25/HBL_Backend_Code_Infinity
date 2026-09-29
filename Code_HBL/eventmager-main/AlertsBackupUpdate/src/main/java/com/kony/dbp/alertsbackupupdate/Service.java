package com.kony.dbp.alertsbackupupdate;

import com.kony.dbp.alertsbackupupdateutil.AlertsBackupUpdateConstants;

public class Service {
	public static void main(String[] args) {
		// BACKUP
		// UPDATE
		String servicetype = System.getProperty(AlertsBackupUpdateConstants.OPERATION);
		if (servicetype == null || (!servicetype.equals(AlertsBackupUpdateConstants.UPDATE)
				&& !servicetype.equals(AlertsBackupUpdateConstants.BACKUP))) {
			System.out.println("Invalid operation..!");
			System.out.println("operation should be either backup or update");
			System.out.println("Example: java -Doperation=backup -jar dbp-alertsbackupupdate.jar");
			return;
		}
		if (servicetype.equals(AlertsBackupUpdateConstants.BACKUP)) {
			if (GenerateBackup.dumpDB()) {
				System.out.println("Backup is success, please check " + System.getProperty("java.io.tmpdir")
						+ " folder for scripts.");
			} else {
				System.out.println("Backup is failed, refer the above errors");
			}
		}
		if (servicetype.equals(AlertsBackupUpdateConstants.UPDATE)) {
			if (UpdateAlertsData.updateAlertScripts()) {
				System.out.println("DataBase update is success, please check DataBase.");
			} else {
				System.out.println("DataBase update is failed, refer the above errors");
			}
		}
	}

}
