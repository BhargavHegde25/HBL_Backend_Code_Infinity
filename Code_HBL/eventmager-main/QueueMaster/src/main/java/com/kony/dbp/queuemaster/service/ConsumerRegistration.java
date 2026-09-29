package com.kony.dbp.queuemaster.service;

import java.util.List;

public class ConsumerRegistration {

  private String serviceId;
  private String operationId;
  private int batchLimit;
  private List<String> eventTypes;
  private EventDisbursementWorker workerThread;

  public ConsumerRegistration(String serviceId, String operationId, int batchLimit, List<String> eventTypes) {
    this.setServiceId(serviceId);
    this.setOperationId(operationId);
    this.setBatchLimit(batchLimit);
    this.setEventTypes(eventTypes);
    this.setWorkerThread(null);
  }

public String getServiceId() {
	return serviceId;
}

public void setServiceId(String serviceId) {
	this.serviceId = serviceId;
}

public String getOperationId() {
	return operationId;
}

public void setOperationId(String operationId) {
	this.operationId = operationId;
}

public int getBatchLimit() {
	return batchLimit;
}

public void setBatchLimit(int batchLimit) {
	this.batchLimit = batchLimit;
}

public List<String> getEventTypes() {
	return eventTypes;
}

public void setEventTypes(List<String> eventTypes) {
	this.eventTypes = eventTypes;
}

public EventDisbursementWorker getWorkerThread() {
	return workerThread;
}

public void setWorkerThread(EventDisbursementWorker workerThread) {
	this.workerThread = workerThread;
}
}
