
package com.kony.dbp.queuemaster.test;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.constants.DBPConstants;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonArray;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

/**
 * This class serves as a common interface to the QueueMaster service, and is designed for use by other
 * services needing to produce and then dispatch events.
 */
public class EventDispatcher {

	private ServicesManager servicesManager;

	/**
	 * Constructor using a DataControllerRequest.
	 * 
	 * @param request The original DataControllerRequest.
	 * @throws AppRegistryException If the DataControllerRequest fails to grant access to the ServicesManager.
	 */
	public EventDispatcher(DataControllerRequest request) throws AppRegistryException {
		this(request.getServicesManager());
	}

	/**
	 * Constructor using a FabricRequestManager.
	 * 
	 * @param request The original FabricRequestManager.
	 */
	public EventDispatcher(FabricRequestManager requestManager) {
		this(requestManager.getServicesManager());
	}

	/**
	 * Constructor using a reference to the ServicesManager.
	 * 
	 * @param servicesManager A reference to the ServicesManager.
	 */
	private EventDispatcher(ServicesManager servicesManager) {
		if (servicesManager == null) {
			throw new NullPointerException();
		}
		this.servicesManager = servicesManager;
	}

	/**
	 * This method calls the QueueMaster's PushEventQueue service to dispatch one or more events contained
	 * within a JSON array.
	 * 
	 * @param events   A JSON array containing the events to be dispatched as JSON objects.
	 * @param producer Identifying name of the event producing process.
	 * @return A Result object describing the outcome of the operation.
	 */
	public Result dispatch(JsonArray events, String producer) {
		Result result;

		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> headerMap = new HashMap<>();

		String eventsString = events.toString();
		String token = deriveToken(this.servicesManager, eventsString);

		inputMap.put("events", eventsString);
		inputMap.put("producer", producer);
		inputMap.put("token", token);

		try {
			OperationData operationData = this.servicesManager.getOperationDataBuilder()
					.withServiceId("QueueMaster").withOperationId("PushEventQueue").build();
			ServiceRequest serviceRequest = this.servicesManager.getRequestBuilder(operationData)
					.withInputs(inputMap).withHeaders(headerMap).build();
			result = serviceRequest.invokeServiceAndGetResult();
		} catch (AppRegistryException arex) {
			result = getErrorResult(1012, "Could not access QueueManager service via app registry");
		} catch (Exception ex) {
			result = getErrorResult(ex);
		}

		return result;
	}

	/**
	 * Derive a QueueMaster access token.
	 * 
	 * @param servicesManager A reference to the ServicesManager.
	 * @param events          The events to be sent, represented as a string containing a JSON array.
	 * @return The QueueMaster access token.
	 */
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

	/**
	 * Get an error result from a thrown error or exception.
	 * 
	 * @param ex The error or exception.
	 * @return The error result.
	 */
	public static Result getErrorResult(Throwable ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg == null) {
			StackTraceElement ste = ex.getStackTrace()[0];
			errorMsg = ex.getClass().getName() + " thrown in " + ste.getClassName() + "." + ste.getMethodName();
		}
		return getErrorResult(57777, errorMsg);
	}

	/**
	 * Get an error result from an error number and message.
	 * 
	 * @param errorNumber The error number.
	 * @param errorMsg    The error message (description).
	 * @return The error result.
	 */
	public static Result getErrorResult(int errorNumber, String errorMsg) {
		Result result = new Result();
		result.addParam(new Param("dbpErrCode", Integer.toString(errorNumber), DBPConstants.FABRIC_INT_CONSTANT_KEY));
		result.addParam(new Param("dbpErrMsg", errorMsg, DBPConstants.FABRIC_INT_CONSTANT_KEY));
		result.addParam(new Param("success", Boolean.toString(false), DBPConstants.FABRIC_INT_CONSTANT_KEY));
		return result;
	}
}
