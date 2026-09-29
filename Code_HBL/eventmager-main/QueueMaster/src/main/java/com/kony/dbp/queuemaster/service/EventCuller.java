
package com.kony.dbp.queuemaster.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.queuemaster.utils.Constants;
import com.kony.dbp.queuemaster.utils.DatabaseUtils;
import com.kony.dbp.queuemaster.utils.DatabaseUtils.ConnectionPoolType;
import com.kony.dbp.queuemaster.utils.EnvironmentConfigurationsHandler;
import com.kony.dbp.queuemaster.utils.EventQueries;
import com.kony.dbp.queuemaster.utils.HaltableThread;
import com.kony.dbp.queuemaster.utils.MiscUtils;
import com.kony.dbp.queuemaster.utils.QueryFormer;

public class EventCuller extends HaltableThread {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

  public EventCuller() {
    super("QueueMaster-EventCuller");
    setDaemon(true);
  }

  public void runThread() {
    diagnostic.prepareInfo("Event culler thread started!").log();
    try {
      while (!this.haltRequested) {
        cullProcessedEvents();
        snooze(30000);
      }
    } finally {
      diagnostic.prepareInfo("Event culler thread stopped!").log();
    }
  }

  private void cullProcessedEvents() {
    Connection connection = null;
    Statement selectStatement = null;
    ResultSet resultSet = null;
    PreparedStatement deleteStatement = null;
    try {
      connection = DatabaseUtils.getDatabaseConnectionFromPool(ConnectionPoolType.INTERNAL);
      selectStatement = connection.createStatement();
			String selectSql = null;
			String deleteSql = null;
			
			selectSql = EventQueries.valueOf(QueryFormer.getQueryForDBType("EVENT_CONSUMER",Constants.QUEUEMASTER_DATABASE_URL)).getQuery();
      resultSet = selectStatement.executeQuery(selectSql);
      resultSet.next();
      int lowestProcessedEventId = resultSet.getInt(1);
      if (resultSet.wasNull()) {
        alert.prepareWarn("No registered event consumers were found, skipping cull of processed events...").log();
        return;
      }
      // Subtract 1 from the deletion point to ensure that at least 1 row remains in the event
      // table. This prevents the auto-increment on the table from restarting at 1 and screwing
      // everything up.
      --lowestProcessedEventId;
			deleteSql = EventQueries.valueOf(QueryFormer.getQueryForDBType("EVENT_DELETE",Constants.QUEUEMASTER_DATABASE_URL)).getQuery();
		
			
			deleteStatement = connection.prepareStatement(deleteSql);
			deleteStatement.setInt(1, lowestProcessedEventId);
			int rowsCulled = deleteStatement.executeUpdate();
			if (rowsCulled > 0) {
				diagnostic.prepareInfo("Culled " + rowsCulled + " processed event" + ((rowsCulled > 1) ? "s" : "")
						+ " (up to event ID " + lowestProcessedEventId + ")").log();
			}
		} catch (Exception ex) {
			MiscUtils.handleFailure(alert, null, ex);
		} finally {
			DatabaseUtils.close(resultSet);
			DatabaseUtils.close(deleteStatement, selectStatement);
			DatabaseUtils.returnDatabaseConnectionToPool(connection);
		}
	}
}
