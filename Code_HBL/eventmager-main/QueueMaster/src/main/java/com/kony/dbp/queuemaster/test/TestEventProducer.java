
package com.kony.dbp.queuemaster.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbp.queuemaster.utils.MiscUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import java.util.Map;
import java.util.Random;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


public class TestEventProducer implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");
  private static final Random random = new Random();

  // Main entry point for invocation of the Fabric service.
  @SuppressWarnings("unchecked")
  public Object invoke(String methodID, Object[] maps, DataControllerRequest request, DataControllerResponse response)
      throws Exception {
    Thread.currentThread().setContextClassLoader(TestEventProducer.class.getClassLoader());
    Map<String, String> inputParams = null;
    if (maps != null && maps.length > 1) {
      inputParams = (Map<String, String>)maps[1];
    }
    String eventType = null;
    String eventSubType = null;
    String customerId = null;
    String message = null;
    int eventCount = 100;
    if (inputParams != null) {
      try {
        eventCount = Integer.parseInt(inputParams.get("eventCount"));
        if (eventCount <= 0) {
          throw new IllegalArgumentException();
        }
      }
      catch (Exception e) {eventCount =100; }
      eventType = inputParams.get("eventType");
      eventSubType = inputParams.get("eventSubType");
      customerId = inputParams.get("customerId");
      message = inputParams.get("message");
    }
    if (eventType == null) {
      eventType = "TEST_EVENT_TYPE";
    }
    if (eventSubType == null) {
      eventSubType = "TEST_EVENT_SUBTYPE";
    }
    JsonArray eventArray = new JsonArray();
    for (int i = 0; i < eventCount; ++i) {
      JsonObject event = new JsonObject();
      event.addProperty("eventType", eventType);
      event.addProperty("eventSubType", eventSubType);
      JsonObject eventData = new JsonObject();
      eventData.addProperty("randomNumber", random.nextInt(99) + 1);
      event.add("eventData", eventData);
      if (customerId != null) {
        event.addProperty("customerId", customerId);
      }
      if (message != null) {
        event.addProperty("message", message);
      }
      eventArray.add(event);
    }
    Result result = new Result();
    try {
      EventDispatcher eventDispatcher = new EventDispatcher(request);
      result = eventDispatcher.dispatch(eventArray, "TestEventProducer");
      Param successParam = result.getParamByName("success");
      boolean success = (successParam == null) ? false : Boolean.valueOf(successParam.getValue());
      if (success) {
        if (diagnostic.isTraceEnabled()) {
          diagnostic.prepareTrace("TestEventProducer sent these events: ").log();
          diagnostic.prepareTrace(eventArray.toString()).log();
        } else if (diagnostic.isInfoEnabled()) {
          diagnostic.prepareInfo("TestEventProducer sent " + eventCount + " events.").log();
        }
      }
    }
    catch (Exception ex) {
      MiscUtils.handleFailure(alert, result, ex);
    }
    return result;
  }
}
