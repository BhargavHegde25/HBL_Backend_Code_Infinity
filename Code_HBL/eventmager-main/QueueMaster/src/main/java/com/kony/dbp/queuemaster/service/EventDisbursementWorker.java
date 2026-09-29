
package com.kony.dbp.queuemaster.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.kony.dbp.queuemaster.utils.Constants;
import com.kony.dbp.queuemaster.utils.DatabaseUtils;
import com.kony.dbp.queuemaster.utils.DatabaseUtils.ConnectionPoolType;
import com.kony.dbp.queuemaster.utils.EnvironmentConfigurationsHandler;
import com.kony.dbp.queuemaster.utils.EventQueries;
import com.kony.dbp.queuemaster.utils.HaltableThread;
import com.kony.dbp.queuemaster.utils.MiscUtils;
import com.kony.dbp.queuemaster.utils.QueryFormer;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class EventDisbursementWorker extends HaltableThread {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
  private  final SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private ConsumerRegistration consumerRegistration;
  private ServicesManager servicesManager;

  private String identifier;
  private boolean allEventTypes;

  public EventDisbursementWorker(ConsumerRegistration consumerRegistration, ServicesManager servicesManager) {
    super("QueueMaster-EventDisbursementWorker-" + consumerRegistration.getServiceId() + "-" +
      consumerRegistration.getOperationId());
    this.consumerRegistration = consumerRegistration;
    this.servicesManager = servicesManager;

    this.identifier = consumerRegistration.getServiceId() + "/" + consumerRegistration.getOperationId();
    this.allEventTypes = consumerRegistration.getEventTypes().contains("*");

    setDaemon(true);
  }

  public void runThread() {
    diagnostic.prepareInfo("Event disbursement worker thread " + this.identifier + " started!").log();
    try {
      while (!this.haltRequested) {
        if (!disburseEventsToConsumer()) {
          snooze(30000);
        }
      }
    } finally {
      diagnostic.prepareInfo("Event disbursement worker thread " + this.identifier + " stopped!").log();
    }
  }

  private boolean disburseEventsToConsumer() {
    diagnostic.prepareTrace("Event disbursement worker thread " + this.identifier + " disbursing events to consumer.").log();
    Connection connection = null;
    PreparedStatement selectStatement1 = null;
    PreparedStatement selectStatement2 = null;
    PreparedStatement updateStatement = null;
    ResultSet resultSet1 = null;
    ResultSet resultSet2 = null;
    try {
      // Get a database connection.
      connection = DatabaseUtils.getDatabaseConnectionFromPool(ConnectionPoolType.INTERNAL);

			// Select the last event ID from the DB.
			String selectSql1 = null;
			
			selectSql1 = EventQueries.valueOf(QueryFormer.getQueryForDBType("LAST_EVENT_ID",Constants.QUEUEMASTER_DATABASE_URL)).getQuery();
			
			//selectSql1 = selectSql1 + " where ServiceId = ? " + "and OperationId = ?";
			selectStatement1 = connection.prepareStatement(selectSql1);
			selectStatement1.setString(1, this.consumerRegistration.getServiceId());
			selectStatement1.setString(2, this.consumerRegistration.getOperationId());
			resultSet1 = selectStatement1.executeQuery();
			if (!resultSet1.next()) {
				MiscUtils.handleFailure(alert, null, 0, "Event disbursement worker thread " + this.identifier
						+ " failed while selecting last event ID.");
				return false;
			}
			int lastEventId = resultSet1.getInt(1);

      // Select all events after the last event ID.
			String selectSql2 = null;
			selectSql2 = EventQueries.valueOf(QueryFormer.getQueryForDBType("ALL_EVENTS",Constants.QUEUEMASTER_DATABASE_URL)).getQuery();
			//selectSql2 = selectSql2 + " where Event_id > ? " + "order by 1";
      selectStatement2 = connection.prepareStatement(selectSql2);
      if (this.consumerRegistration.getBatchLimit() > 0) {
        selectStatement2.setMaxRows(this.consumerRegistration.getBatchLimit());
      }
      selectStatement2.setInt(1, lastEventId);
      resultSet2 = selectStatement2.executeQuery();

      // Build a batch from the selected events.
      JsonParser parser = new JsonParser();
      JsonArray eventArray = new JsonArray();
      int highestEventId = lastEventId;
      while (resultSet2.next()) {
        int eventId = resultSet2.getInt(1);
        highestEventId = eventId;
        String eventType = resultSet2.getString(2);
        // Throw away irrelevant events (i.e. event types we aren't subscribed for).
        if (!this.allEventTypes && !this.consumerRegistration.getEventTypes().contains(eventType)) {
          continue;
        }
        // Throw away events that are already processed.
        boolean isProcessed = resultSet2.getBoolean(7);
        if (isProcessed) {
          continue;
        }
        // Event looks good, add it to our batch.
        String eventSubType = resultSet2.getString(3);
        String status = resultSet2.getString(4);
        String eventDataStr = resultSet2.getString(5);
        String otherDataStr = resultSet2.getString(6);
        String producer = resultSet2.getString(8);
        String preProcessorResult = resultSet2.getString(9);
        String postProcessorResult = resultSet2.getString(10);
        String session = resultSet2.getString(11);
        Timestamp timestampDate = resultSet2.getTimestamp(12);

        // Parse any event data.
        JsonObject eventData = null;
        if (eventDataStr != null && eventDataStr.trim().length() > 0) {
          try {
            JsonElement eventDataElement = parser.parse(eventDataStr);
            if (!eventDataElement.isJsonObject()) {
              MiscUtils.handleFailure(alert, null, 0, "Event data in event " + eventId +
                " is not a JSON object! Skipping this event, as cannot process it.");
              continue;
            }
            eventData = eventDataElement.getAsJsonObject();
          }
          catch (JsonSyntaxException jsex) {
            MiscUtils.handleFailure(alert, null, 0, "Event data in event " + eventId +
              " could not be parsed as valid JSON. Skipping this event, as cannot process it.");
            continue;
          }
        }

        // Parse any other data.
        JsonObject otherData = null;
        if (otherDataStr != null && otherDataStr.trim().length() > 0) {
          try {
            JsonElement otherDataElement = parser.parse(otherDataStr);
            if (!otherDataElement.isJsonObject()) {
              MiscUtils.handleFailure(alert, null, 0, "Other data in event " + eventId +
                " is not a JSON object!. Skipping this event, as cannot process it.");
              continue;
            }
            otherData = otherDataElement.getAsJsonObject();
          }
          catch (JsonSyntaxException jsex) {
            MiscUtils.handleFailure(alert, null, 0, "Other data in event " + eventId +
              " could not be parsed as valid JSON. Skipping this event, as cannot process it.");
            continue;
          }
        }

        // Instantiate an event object.
        JsonObject event = new JsonObject();
        event.addProperty("eventId", eventId);
        event.addProperty("eventType", eventType);
        if (eventSubType != null) {
          event.addProperty("eventSubType", eventSubType);
        }
        if (status != null) {
          event.addProperty("status", status);
        }
        if (eventData != null) {
          event.add("eventData", eventData);
        }
        if (otherData != null) {
          event.add("otherData", otherData);
        }
        event.addProperty("isProcessed", true);
        event.addProperty("producer", producer);
        if (preProcessorResult != null) {
          event.addProperty("preProcessorResult", preProcessorResult);
        }
        if (postProcessorResult != null) {
          event.addProperty("postProcessorResult", postProcessorResult);
        }
        if (session != null) {
          event.addProperty("session", session);
        }
        if (timestampDate != null) {
          String timestamp = timestampFormatter.format(timestampDate);
          event.addProperty("timestamp", timestamp);
        }

        // Add the event to the array.
        eventArray.add(event);

        // Double-check to make sure batch size is not exceeded. In theory this check should not be needed
        // because maxrows gets set, but it doesn't hurt to make sure!
        if (this.consumerRegistration.getBatchLimit() > 0 &&
          eventArray.size() >= this.consumerRegistration.getBatchLimit()) {
          break;
        }
      }

      // Stop here if there's nothing to process.
      if (highestEventId <= lastEventId) {
        return false;
      }

      // Take out an update lock on the event consumer row.
      connection.setAutoCommit(false);
			String updateSql = null;
			updateSql = EventQueries.valueOf(QueryFormer.getQueryForDBType("UPDATE_EVENT_CONSUMER",Constants.QUEUEMASTER_DATABASE_URL)).getQuery();
			//updateSql = updateSql + " ? " + "where ServiceId = ? " + "and OperationId = ? " + "and LastEventId = ?";
      updateStatement = connection.prepareStatement(updateSql);
      updateStatement.setInt(1, highestEventId);
      updateStatement.setString(2, this.consumerRegistration.getServiceId());
      updateStatement.setString(3, this.consumerRegistration.getOperationId());
      updateStatement.setInt(4, lastEventId);
      int rowCount = updateStatement.executeUpdate();
      if (rowCount == 0) {
        // Row not updated ==> another QueueMaster node has updated the LastEventId. Bail out and retry.
        connection.rollback();
        connection.setAutoCommit(true);
        diagnostic.prepareTrace("Event disbursement worker thread " + this.identifier +
          " found last event ID changed, bailing out.").log();
        return true;
      }

      // Invoke consumer service with ServicesManager, passing event array.
      if (eventArray.size() > 0) {
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        String eventString = eventArray.toString();
        String token = MiscUtils.deriveToken(eventString);
        inputMap.put("events", eventString);
        inputMap.put("token", token);
        OperationData operationData = this.servicesManager
          .getOperationDataBuilder()
          .withServiceId(this.consumerRegistration.getServiceId())
          .withOperationId(this.consumerRegistration.getOperationId())
          .build();
        ServiceRequest serviceRequest = this.servicesManager
          .getRequestBuilder(operationData)
          .withInputs(inputMap)
          .withHeaders(headerMap)
          .build();
        diagnostic.prepareTrace("Event disbursement worker thread " + this.identifier +
          " is now invoking consumer service.").log();
        Result result = serviceRequest.invokeServiceAndGetResult();
        Param successParam = (result == null) ? null : result.getParamByName("success");
        if ((successParam == null || !Boolean.valueOf(successParam.getValue()))&& result!=null) {
          MiscUtils.handleFailure(alert, null, 0, "Failed to call service '" +
            this.consumerRegistration.getServiceId() + "' using operation '" +
            this.consumerRegistration.getOperationId() + "': " + result.toString());
          return false;
        }
      }

      // Commit, to release the update lock on the event consumer row.
      connection.commit();
      connection.setAutoCommit(true);
      return true;
    }
    catch (Exception ex) {
      DatabaseUtils.rollbackAndReenableAutocommit(connection);
      MiscUtils.handleFailure(alert, null, ex);
      return false;
    }
    finally {
      DatabaseUtils.close(resultSet1, resultSet2);
      DatabaseUtils.close(selectStatement1, selectStatement2, updateStatement);
      DatabaseUtils.returnDatabaseConnectionToPool(connection);
    }
  }
}
