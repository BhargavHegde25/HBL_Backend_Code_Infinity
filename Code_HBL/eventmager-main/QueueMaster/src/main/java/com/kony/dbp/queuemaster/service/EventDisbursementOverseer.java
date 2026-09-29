package com.kony.dbp.queuemaster.service;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.queuemaster.utils.Constants;
import com.kony.dbp.queuemaster.utils.DatabaseUtils;
import com.kony.dbp.queuemaster.utils.DatabaseUtils.ConnectionPoolType;
import com.kony.dbp.queuemaster.utils.EventQueries;
import com.kony.dbp.queuemaster.utils.HaltableThread;
import com.kony.dbp.queuemaster.utils.MiscUtils;
import com.kony.dbp.queuemaster.utils.QueryFormer;
import com.konylabs.middleware.api.ServicesManager;

public class EventDisbursementOverseer extends HaltableThread {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

  private EventCuller eventCuller;
  private List<ConsumerRegistration> consumerRegistrations;
  private ServicesManager servicesManager;
  private File tempFile;
  private int eventNotificationCount = 0;
  private int lastEventNotificationCount = 0;

  public EventDisbursementOverseer(ServicesManager servicesManager, File tempFile) {
    super("QueueMaster-EventDisbursementOverseer");
    this.servicesManager = servicesManager;
    this.tempFile = tempFile;
    setDaemon(true);
  }

  private void dereferenceSubordinateThreads() {
    this.eventCuller = null;
    if (this.consumerRegistrations != null) {
      for (ConsumerRegistration consReg : this.consumerRegistrations) {
        consReg.setWorkerThread(null);
      }
    }
  }

  private void haltSubordinateThreads() {
    diagnostic.prepareTrace("Event disbursement overseer thread is halting subordinate threads.").log();
    requestHaltOfSubordinateThreads();
    waitForExitOfSubordinateThreads();
    dereferenceSubordinateThreads();
  }

  private void loadEventConsumers() {
    diagnostic.prepareTrace("Event disbusement overseer thread is loading consumers.").log();
    Connection connection = null;
    Statement statement = null;
    ResultSet rs = null;
    try {
      List<ConsumerRegistration> consRegs = new LinkedList<>();
      connection = DatabaseUtils.getDatabaseConnectionFromPool(ConnectionPoolType.INTERNAL);
      statement = connection.createStatement();
      String selectSql = EventQueries.valueOf(QueryFormer.getQueryForDBType("EVENT_CONSUMER_DETAILS",Constants.QUEUEMASTER_DATABASE_URL)).getQuery();

      rs = statement.executeQuery(selectSql);
      while (rs.next()) {
        String serviceId = rs.getString(1);
        String operationId = rs.getString(2);
        int batchLimit = rs.getInt(3);
        String eventType = rs.getString(4);
        boolean found = false;
        for (ConsumerRegistration consReg : consRegs) {
          if (consReg.getServiceId().equals(serviceId) && consReg.getOperationId().equals(operationId)) {
            diagnostic.prepareTrace("Added additional event type " + eventType +
              " to event consumer registration for service ID " + serviceId +
              " and operation ID " + operationId).log();
            consReg.getEventTypes().add(eventType);
            found = true;
            break;
          }
        }
        if (!found) {
          diagnostic.prepareTrace("Adding event consumer registration for service ID " + serviceId +
            " and operation ID " + operationId + " (event type " + eventType + ")").log();
          consRegs.add(new ConsumerRegistration(serviceId, operationId, batchLimit,
            new LinkedList<String>(Arrays.asList(eventType))));
        }
      }
      this.consumerRegistrations = consRegs;
    }
    catch (Exception ex) {
      MiscUtils.handleFailure(alert, null, ex);
    }
    finally {
      DatabaseUtils.close(rs);
      DatabaseUtils.close(statement);
      DatabaseUtils.returnDatabaseConnectionToPool(connection);
    }
  }

  public void notifyNewEvents() {
    synchronized (this) {
      ++this.eventNotificationCount;
      wakeup();
    }
  }

  private void performHealthCheckAndNotifyNewEvents() {
    diagnostic.prepareTrace("Event disbursement overseer thread is checking thread health and pushing new events.").log();
    boolean newEvents;
    synchronized (this) {
      newEvents = (this.eventNotificationCount > this.lastEventNotificationCount);
      this.lastEventNotificationCount = this.eventNotificationCount;
    }
    for (ConsumerRegistration consReg : this.consumerRegistrations) {
      if (consReg.getWorkerThread() == null || !consReg.getWorkerThread().isAlive()) {
        consReg.setWorkerThread(new EventDisbursementWorker(consReg, this.servicesManager));
        consReg.getWorkerThread().setContextClassLoader(Thread.currentThread().getContextClassLoader());
        consReg.getWorkerThread().start();
      }
      if (newEvents) {
        consReg.getWorkerThread().wakeup();
      }
      if (this.eventCuller == null || !this.eventCuller.isAlive()) {
        this.eventCuller = new EventCuller();
        this.eventCuller.setContextClassLoader(Thread.currentThread().getContextClassLoader());
        this.eventCuller.start();
      }
    }
  }

  private void requestHaltOfSubordinateThreads() {
    if (this.eventCuller != null) {
        this.eventCuller.haltThread();
    }
    if (this.consumerRegistrations != null) {
      for (ConsumerRegistration consReg : this.consumerRegistrations) {
        if (consReg.getWorkerThread() != null) {
          consReg.getWorkerThread().haltThread();
        }
      }
    }
  }

  public void runThread() {
    diagnostic.prepareInfo("Event disbursement overseer thread started!").log();
    try {
      while (!this.haltRequested) {
        if (consumerRegistrations == null) {
          loadEventConsumers();
        }
        if (consumerRegistrations != null) {
          performHealthCheckAndNotifyNewEvents();
        }
        snooze(10000);
        if (!this.tempFile.exists()) {
          diagnostic.prepareTrace("Temp file " + this.tempFile.getPath() + " is not present, shutting down.").log();
          break;
        }
      }
    } finally {
      haltSubordinateThreads();
      DatabaseUtils.flushDatabaseConnectionPool(
        ConnectionPoolType.INTERNAL,
        ConnectionPoolType.REQUESTS
      );
      this.tempFile.delete();
      diagnostic.prepareInfo("Event disbursement overseer thread stopped!").log();
    }
  }

  private void waitForExitOfSubordinateThreads() {
    long timeout = System.currentTimeMillis() + 30000L;
    for ( ; ; ) {
      Thread liveThread = null;
      if (this.eventCuller != null && this.eventCuller.isAlive()) {
        liveThread = this.eventCuller;
      } else {
        if (this.consumerRegistrations != null) {
          for (ConsumerRegistration consReg : this.consumerRegistrations) {
            if (consReg.getWorkerThread() != null && consReg.getWorkerThread().isAlive()) {
              liveThread = consReg.getWorkerThread();
              break;
            }
          }
        }
      }
      if (liveThread == null) {
        return;
      }
      if (System.currentTimeMillis() >= timeout) {
        alert.prepareWarn("Timeout occurred while waiting for thread " +
          liveThread.getName() + " to exit.").log();
        return;
      }
      Thread.yield();
    }
  }
}
