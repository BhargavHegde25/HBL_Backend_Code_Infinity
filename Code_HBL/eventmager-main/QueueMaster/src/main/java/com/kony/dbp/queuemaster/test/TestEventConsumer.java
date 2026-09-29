
package com.kony.dbp.queuemaster.test;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.dbp.queuemaster.utils.MiscUtils;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;


public class TestEventConsumer implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

  // Main entry point for invocation of the Fabric service.
  @SuppressWarnings("unchecked")
  public Object invoke(String methodID, Object[] maps, DataControllerRequest request, DataControllerResponse response)
      throws Exception {
    Thread.currentThread().setContextClassLoader(TestEventConsumer.class.getClassLoader());
    Result result = new Result();
    Map<String, String> inputParams = null;
    if (maps != null && maps.length > 1) {
      inputParams = (Map<String, String>)maps[1];
    }
    String events = null;
    String token = null;
    if (inputParams != null) {
      events = inputParams.get("events");
      token = inputParams.get("token");
    }
    if (events == null || events.length() == 0) {
      MiscUtils.handleFailure(alert, result, 57777,
        "Required parameter 'events' was missing from the request");
      return result;
    }
    if (token == null || token.length() == 0) {
      MiscUtils.handleFailure(alert, result, 57777,
        "Required parameter 'token' was missing from the request");
      return result;
    }
    JsonElement eventsElement;
    try {
      eventsElement = new JsonParser().parse(events);
    }
    catch (Exception e) {
      MiscUtils.handleFailure(alert, result, 57777,
        "An exception occurred whilst parsing the JSON in parameter 'events'");
      return result;
    }
    if (!eventsElement.isJsonArray()) {
      MiscUtils.handleFailure(alert, result, 57777, "Parameter 'events' does not contain a JSON array");
      return result;
    }
    JsonArray eventsArray = eventsElement.getAsJsonArray();
    if (diagnostic.isTraceEnabled()) {
      diagnostic.prepareTrace("TestEventConsumer received these events: ").log();
      diagnostic.prepareTrace(eventsArray.toString()).log();
      diagnostic.prepareTrace("Token = " + token).log();
      String expectedToken = deriveToken(request.getServicesManager(), events);
      diagnostic.prepareTrace("Expected Token = " + expectedToken).log();
      diagnostic.prepareTrace(token.equals(expectedToken) ? "Tokens match!" : "Tokens do NOT match!").log();
    } else if (diagnostic.isInfoEnabled()) {
      int eventCount = eventsArray.size();
      diagnostic.prepareInfo("TestEventConsumer received " + eventCount + " events.").log();
    }
    MiscUtils.handleSuccess(result);
    return result;
  }

  private static String deriveToken(ServicesManager servicesManager, String events) {
    ConfigurableParametersHelper configHelper = servicesManager.getConfigurableParametersHelper();
    String secret = configHelper.getServerProperty("QUEUEMASTER_SHARED_SECRET");
    if (secret == null || secret.length() == 0) {
      throw new RuntimeException("QueueMaster shared secret has not been configured!");
    }
    String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString();
    String saltedSecret = eventsHash + secret;
    return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
  }
}
