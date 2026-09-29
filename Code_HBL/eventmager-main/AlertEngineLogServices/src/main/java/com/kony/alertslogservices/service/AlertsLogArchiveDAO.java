package com.kony.alertslogservices.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2oException;

import com.kony.alertslogservices.core.BaseActivity;
import com.kony.alertslogservices.dbutils.AlertsLogDataSourceHandler;
import com.kony.alertslogservices.util.SQLQueriesEnum;

public class AlertsLogArchiveDAO {
	private AlertsLogArchiveDAO() {

	}

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static final int BATCH_SIZE = 50;

	public static <T extends BaseActivity> boolean archiveLogs(Class<T> logClazz, String archiveBefore) {

		int endOffset = BATCH_SIZE;
		boolean isArchived = false;
		List<T> logs = null;
		try {
			logs = fetchLogData(logClazz, archiveBefore, 0, endOffset);
		} catch (Exception e) {
			diagnostic.prepareDebug("Failed to archive logs", e).log();
		}
		// continue archiving logs in batches
		int count = 0;
		while (logs != null && !logs.isEmpty()) {
			try {
				// save logs in a batch. On failure, save logs one by one.
				// One possible reason of failure situation is, logs got saved in archive table
				// and failed in
				// deleting from main log table. Next run will pick the same archived logs from
				// main log table and
				// tries to save it in archive table and fails due to primary key constraint
				// violations.
				saveLogsInBatch(logs);
				isArchived = true;
			} catch (Sql2oException sql2oe) {
				if (sql2oe.getCause() instanceof SQLIntegrityConstraintViolationException) {
					isArchived = saveLogs(logs) == logs.size();
				}
			}

			// delete archived logs
			if (isArchived) {
				deleteLogs(logs);
				count = 0;
			} else {
				count++;
			}
			if (count == 5) {
				diagnostic.prepareDebug("Error in archiving data.").log();
				break;
			}
			logs = fetchLogData(logClazz, archiveBefore, 0, BATCH_SIZE);
		}
		return true;
	}

	/**
	 * Returns list of logs for the provided class. The maximum size of logs
	 * returned would be based on the provided offsets.
	 * 
	 * @param logClazz
	 * @param archiveBefore
	 * @param startOffset
	 * @param endOffset
	 * @return
	 */
	public static <T extends BaseActivity> List<T> fetchLogData(Class<T> logClazz, String archiveBefore, int startOffset,
			int endOffset) {
		List<T> logs = new ArrayList<>();

		try (Connection con = AlertsLogDataSourceHandler.getLogSql2oInstance().open()) {
			//String fetchQuery = new StringBuilder(SQLQueriesEnum.getActivityReadQuery(logClazz)).append(" WHERE")
			//		.append("  \"DispatchDate\" < :archiveBefore order by \"DispatchDate\" ").append(SQLQueriesEnum.LIMIT.getQuery()).toString();

			String fetchQuery = new StringBuilder(SQLQueriesEnum.getActivityReadQuery(logClazz)).append(" WHERE")
					.append(SQLQueriesEnum.DISPATCH_DATE.getQuery()).append(" < :archiveBefore order by ")
					.append(SQLQueriesEnum.DISPATCH_DATE.getQuery()).append(SQLQueriesEnum.LIMIT.getQuery()).toString();
			try (Query query = con.createQuery(fetchQuery).addParameter("archiveBefore", archiveBefore)
					.addParameter("startOffset", startOffset).addParameter("endOffset", endOffset)) {
				logs = query.executeAndFetch(logClazz);
			}
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception occured while fetching log data", e).log();
		}
		return logs;
	}

	public static <T extends BaseActivity> void saveLogsInBatch(List<T> logs) {
		if (logs != null && !logs.isEmpty()) {
			Class<? extends BaseActivity> logClazz = logs.get(0).getClass();
			try (Connection con = AlertsLogDataSourceHandler.getLogSql2oInstance().beginTransaction()) {
				for (T log : logs) {
					try (Query query = con.createQuery(SQLQueriesEnum.getActivityInsertQuery(logClazz)).bind(log)) {
						query.executeUpdate();
					}
				}
				con.commit();
			}
		}
	}

	/**
	 * Saves each log from the provided list in a separate transaction. Returns the
	 * count of logs being saved successfully, -1 incase if the argument is null or
	 * an empty list
	 * 
	 * @param logs
	 * @return
	 */
	public static <T extends BaseActivity> long saveLogs(List<T> logs) {
		long savedCount = -1;
		if (logs != null && !logs.isEmpty()) {
			savedCount = 0;
			Class<? extends BaseActivity> logClazz = logs.get(0).getClass();
			for (T log : logs) {
				try (Connection con = AlertsLogDataSourceHandler.getLogSql2oInstance().beginTransaction()) {
					try (Query query = con.createQuery(SQLQueriesEnum.getActivityInsertQuery(logClazz)).bind(log)) {
						query.executeUpdate();
					}
					con.commit();
					savedCount++;
				} catch (Sql2oException sql2oe) {
					if (sql2oe.getCause() instanceof SQLIntegrityConstraintViolationException) {
						// incrementing the saved count assuming the log already being saved previously
						savedCount++;
					} else {
						diagnostic.prepareDebug(
								"Failure in saving archived log, continuing by saving another log from the provided list of logs",
								sql2oe).log();
					}

				}
			}
		}
		return savedCount;
	}

	/**
	 * Deletes the list of provided logs from the main log table
	 * 
	 * @param logs
	 */
	public static <T extends BaseActivity> void deleteLogs(List<T> logs) {
		if (logs != null && !logs.isEmpty()) {
			Class<? extends BaseActivity> logClazz = logs.get(0).getClass();
			Map<String, Object> parameters = new HashMap<>();

			ArrayList<String> idsList = new ArrayList<>(logs.size());
			for (T log : logs) {
				idsList.add(log.getId());
			}

			StringBuilder deleteQuery = new StringBuilder(SQLQueriesEnum.getActivityDeleteQuery(logClazz));
		//	deleteQuery.append(" WHERE \"id\" IN (").append(generateInClauseParameters("id", idsList, parameters))
			//		.append(") ");
			deleteQuery.append(SQLQueriesEnum.valueOf("DELETEQUERY_WHERECLAUSE").getQuery() + " (").append(generateInClauseParameters("id", idsList, parameters))
            .append(") ");
			try (Connection con = AlertsLogDataSourceHandler.getLogSql2oInstance().beginTransaction()) {
				try (Query query = con.createQuery(deleteQuery.toString())) {
					for (Map.Entry<String, Object> entry : parameters.entrySet()) {
						query.addParameter(entry.getKey(), entry.getValue());
					}
					query.executeUpdate();
				}
				con.commit();
			}
		}
	}

	public static String generateInClauseParameters(String basename, List<String> array,
			Map<String, Object> parameters) {
		StringBuilder pattern = new StringBuilder();
		String placeHolder = null;
		for (int index = 0; index < array.size(); index++) {
			placeHolder = basename + index;
			pattern.append(":" + placeHolder + ",");
			parameters.put(placeHolder, array.get(index));
		}
		pattern.deleteCharAt((pattern.length() - 1));
		return pattern.toString();
	}

}
