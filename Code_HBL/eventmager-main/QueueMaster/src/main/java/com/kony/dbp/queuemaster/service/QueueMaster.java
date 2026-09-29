
package com.kony.dbp.queuemaster.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.queuemaster.utils.Config;
import com.kony.dbp.queuemaster.utils.DatabaseUtils;
import com.kony.dbp.queuemaster.utils.DatabaseUtils.ConnectionPoolType;
import com.kony.dbp.queuemaster.utils.MiscUtils;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbp.queuemaster.utils.Constants;
import com.kony.dbp.queuemaster.utils.QueryFormer;
import com.kony.dbp.queuemaster.utils.EventQueries;


public class QueueMaster implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
  private final SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private static Object overseerLock = new Object();
  private static EventDisbursementOverseer overseer = null;

  // Main entry point for invocation of the Fabric service.
  public Object invoke(String methodID, Object[] maps, DataControllerRequest request,
      DataControllerResponse response) throws Exception {
    Thread.currentThread().setContextClassLoader(QueueMaster.class.getClassLoader());
    Result result = new Result();
    if ("PushEventQueue".equalsIgnoreCase(methodID)) {
      performPushEventQueueOperation(maps, request, result);
    } else if ("Ping".equalsIgnoreCase(methodID)) {
      performPingOperation(request, result);
    } else if ("Restart".equalsIgnoreCase(methodID)) {
      performRestartOperation(request, result);
    } else if ("Start".equalsIgnoreCase(methodID)) {
      performStartOperation(request, result);
    } else if ("Stop".equalsIgnoreCase(methodID)) {
      performStopOperation(request, result);
    } else {
      MiscUtils.handleFailure(alert, result, 57777, "Unknown operation: '" + methodID + "'");
    }
    return result;
  }

  // Check to ensure that maintenance mode is enabled (for queue restarts etc.)
  private boolean checkForMaintenanceMode(DataControllerRequest request, Result result) {
    try {
      if (Config.getBooleanValue("QUEUEMASTER_MAINTENANCE_MODE", false)) {
        return true;
      }
    }
    catch (Exception ex) {
      MiscUtils.handleFailure(alert, result, ex);
      return false;
    }
    MiscUtils.handleFailure(alert, result, 57777,
      "QUEUEMASTER_MAINTENANCE_MODE is required for this operation and has not been enabled");
    return false;
  }

  // Check we are initialised.
  private boolean checkInitialised(DataControllerRequest request, Result result)
    throws AppRegistryException {
    synchronized (overseerLock) {
      ServicesManager servicesManager = request.getServicesManager();
      // Ensure that the event disbursement overseer is running.
      if (overseer == null || !overseer.isAlive()) {
        // Event overseer is not running, so we need to start it. Before doing this, we first ensure that
        // any extant event disbursement overseer instances stop running, by deleting their associated
        // temporary files. Unfortunately due to ClassLoader segmentation of the JVM, the only reliable
        // way of shutting down any existing event disbursement overseer instances is to use something
        // external to the JVM, in this case the file system.
        File newTempFile;
        try {
          String tempDirName = System.getProperty("java.io.tmpdir");
          File tempDir = new File(tempDirName);
          String [] tempFiles = tempDir.list();
          for (int i = 0; i < tempFiles.length; ++i) {
            String tempFileName = tempFiles[i];
            if (tempFileName.startsWith("QueueMaster-") && tempFileName.endsWith(".tmp")) {
              File tempFile = new File(tempDir, tempFileName);
              if (tempFile.isFile()) {
                tempFile.delete();
                diagnostic.prepareTrace("Deleted temp file " + tempFile.getPath()).log();
              }
            }
          }
          // Now create a temp file to be associated with the new event disbursement overseer instance.
          // If this gets deleted then the instance will stop running.
          Path newTempPath = Files.createTempFile(tempDir.toPath(), "QueueMaster-", ".tmp");
          newTempFile = newTempPath.toFile();
          diagnostic.prepareTrace("Created temp file " + newTempFile.getPath()).log();
        }
        catch (Exception ex) {
          MiscUtils.handleFailure(alert, result, ex);
          return false;
        }
        // Temp file setup complete, start the new event disbursement overseer.
        overseer = new EventDisbursementOverseer(servicesManager, newTempFile);
        overseer.startAndWait();
      }
    }
    return true;
  }

  // Processing associated with pushing some events onto the queue.
  @SuppressWarnings({ "resource", "unchecked" })
  private void performPushEventQueueOperation(Object[] maps, DataControllerRequest request, Result result)
    throws AppRegistryException {
    diagnostic.prepareInfo("QueueMaster received PushEventQueue call!").log();
    if (!checkInitialised(request, result)) {
      return;
    }
    Map<String, String> inputParams = null;
    if (maps != null && maps.length > 1) {
      inputParams = (Map<String, String>)maps[1];
    }
    String producer = null;
    String events = null;
    String token = null;
    if (inputParams != null) {
      producer = inputParams.get("producer");
      events = inputParams.get("events");
      token = inputParams.get("token");
    }
    if (producer == null || producer.length() == 0) {
      MiscUtils.handleFailure(alert, result, 57777,
        "Required parameter 'producer' was missing from the request");
      return;
    }
    if (events == null) {
      MiscUtils.handleFailure(alert, result, 57777,
        "Required parameter 'events' was missing from the request");
      return;
    }
    if (token == null) {
      MiscUtils.handleFailure(alert, result, 57777,
        "Required parameter 'token' was missing from the request");
      return;
    }
    String expectedToken = MiscUtils.deriveToken(events);
    if (!token.equals(expectedToken)) {
      MiscUtils.handleFailure(alert, result, 57777, "Token sent with request is not valid");
      return;
    }
    JsonElement eventsElement;
    try {
      eventsElement = new JsonParser().parse(events);
    }
    catch (Exception e) {
      MiscUtils.handleFailure(alert, result, 57777,
        "An exception occurred whilst parsing the JSON in parameter 'events'");
      return;
    }
    if (!eventsElement.isJsonArray()) {
      MiscUtils.handleFailure(alert, result, 57777, "Parameter 'events' does not contain a JSON array");
      return;
    }
    JsonArray eventsArray = eventsElement.getAsJsonArray();
    Connection connection = null;
    PreparedStatement statement = null;
    boolean multipleEvents = (eventsArray.size() > 1);
    try {
     
      String insertSql = EventQueries.valueOf(QueryFormer.getQueryForDBType
    			("EVENT_INSERT",Constants.QUEUEMASTER_DATABASE_URL)).getQuery() + 
      		  "(" +
      		          "?,?,?,?,?,?,?,?,?,?,?" +
      		          ")";
      connection = DatabaseUtils.getDatabaseConnectionFromPool(ConnectionPoolType.REQUESTS);
      if (multipleEvents) {
        connection.setAutoCommit(false);
      }
      statement = connection.prepareStatement(insertSql);
      Iterator<JsonElement> eventIter = eventsArray.iterator();
      while (eventIter.hasNext()) {
        JsonElement eventElement = eventIter.next();
        if (!eventElement.isJsonObject()) {
          throw new IllegalArgumentException("Unexpected element in JSON events array");
        }
        JsonObject event = eventElement.getAsJsonObject();

        String eventType = MiscUtils.getStringFromJsonObject(event, "eventType", true);
        String eventSubType = MiscUtils.getStringFromJsonObject(event, "eventSubType", false);
        String status = MiscUtils.getStringFromJsonObject(event, "status", false);
        JsonObject eventData = MiscUtils.getJsonObjectFromJsonObject(event, "eventData", false);
        JsonObject otherData = MiscUtils.getJsonObjectFromJsonObject(event, "otherData", false);
        String preProcessorResult = MiscUtils.getStringFromJsonObject(event, "preProcessorResult", false);
        String postProcessorResult = MiscUtils.getStringFromJsonObject(event, "postProcessorResult", false);
        String session = MiscUtils.getStringFromJsonObject(event, "session", false);
        String timestampStr = MiscUtils.getStringFromJsonObject(event, "timestamp", false);

        statement.setString(1, eventType);
        statement.setString(2, eventSubType);
        statement.setString(3, status);
        statement.setString(4, ((eventData == null) ? null : eventData.toString()));
        statement.setString(5, ((otherData == null) ? null : otherData.toString()));
        statement.setBoolean(6, false); // Always set IsProcessed to false
        statement.setString(7, producer);
        statement.setString(8, preProcessorResult);
        statement.setString(9, postProcessorResult);
        statement.setString(10, session);
        Timestamp timestamp = null;
        if (timestampStr != null && timestampStr.length() > 0) {
          try {
            java.util.Date timestampDate = timestampFormatter.parse(timestampStr);
            timestamp = new Timestamp(timestampDate.getTime());
          }
          catch (ParseException pex) { }
        }
        if (timestamp == null) {
          timestamp = new Timestamp(System.currentTimeMillis());
        }
        statement.setTimestamp(11, timestamp);
        
        statement.executeUpdate();
      }
      if (multipleEvents) {
        connection.commit();
        connection.setAutoCommit(true);
      }
      MiscUtils.handleSuccess(result);
      synchronized (overseerLock) {
        if (overseer != null) {
          overseer.notifyNewEvents();
        }
      }
    } catch (Exception ex) {
      if (multipleEvents) {
        DatabaseUtils.rollbackAndReenableAutocommit(connection);
      }
      MiscUtils.handleFailure(alert, result, ex);
    }
    finally {
      DatabaseUtils.close(statement);
      DatabaseUtils.returnDatabaseConnectionToPool(connection);
    }
  }

  // Processing associated with a ping.
  private void performPingOperation(DataControllerRequest request, Result result) {
    diagnostic.prepareInfo("QueueMaster received Ping call!").log();
    if (!checkForMaintenanceMode(request, result)) {
      return;
    }
    MiscUtils.handleSuccess(result);
  }

  // Processing associated with a restart of the event disbursement threads.
  private void performRestartOperation(DataControllerRequest request, Result result)
    throws AppRegistryException {
    diagnostic.prepareInfo("QueueMaster received Restart call!").log();
    if (!checkForMaintenanceMode(request, result)) {
      return;
    }
    if (!stopEventDisbursement(result)) {
      return;
    }
    if (!startEventDisbursement(request, result)) {
      return;
    }
    MiscUtils.handleSuccess(result);
  }
  // Processing associated with starting (initialising) the event disbursement threads.
  private void performStartOperation(DataControllerRequest request, Result result)
    throws Exception {
    diagnostic.prepareInfo("QueueMaster received Start call!").log();
    if (!checkForMaintenanceMode(request, result)) {
      return;
    }
    if (!startEventDisbursement(request, result)) {
      return;
    }
    MiscUtils.handleSuccess( result);
  }

  // Processing associated with stopping (shutting down) the event disbursement threads.
  private void performStopOperation( DataControllerRequest request, Result result) {
    diagnostic.prepareInfo("QueueMaster received Stop call!").log();
    if (!checkForMaintenanceMode(request, result)) {
      return;
    }
    if (!stopEventDisbursement(result)) {
      return;
    }
    MiscUtils.handleSuccess(result);
  }

  // Start the event disbursement threads.
  private boolean startEventDisbursement(DataControllerRequest request, Result result)
    throws AppRegistryException {
    return checkInitialised(request, result);
  }

  // Stop the event disbursement threads.
  private boolean stopEventDisbursement(Result result) {
    EventDisbursementOverseer o = null;
    synchronized (overseerLock) {
      if (overseer != null) {
        o = overseer;
        overseer.haltThread();
        overseer = null;
      }
    }
    if (o != null && !o.waitForHalt(30000)) {
        MiscUtils.handleFailure(alert, result, 57777,
          "Timeout while waiting for event disbursement shutdown!");
        return false;
    }
    return true;
  }
}
