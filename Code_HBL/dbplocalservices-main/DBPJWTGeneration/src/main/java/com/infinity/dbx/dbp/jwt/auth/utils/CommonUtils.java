package com.infinity.dbx.dbp.jwt.auth.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.session.Session;

public class CommonUtils{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	/**
	 * Delete an object from the KonyFabric cache
	 * 
	 * @param String
	 *            Key for cache
	 **/
	public static void cacheDelete(String key) throws Exception {

		// Sanity checks
		if (key == null)
			throw new Exception("Cache key must be provided");

		// Fetch the result from cache
		ResultCache resultCache = ResultCacheImpl.getInstance();
		resultCache.removeFromCache(key);
	}

	/**
	 * Fetch an object from the KonyFabric cache
	 * 
	 * @param String
	 *            Key for cache
	 * @return Result Result object
	 **/
	public static Result cacheFetch(String key) throws Exception {

		// Sanity checks
		if (key == null)
			throw new Exception("Cache key must be provided");

		// Fetch the result from cache
		ResultCache resultCache = ResultCacheImpl.getInstance();
		Result result = (Result) resultCache.retrieveFromCache(key);
		return result;
	}

	/**
	 * Insert an object into the KonyFabric cache
	 * 
	 * @param String
	 *            Key for cache
	 * @param Result
	 *            The result object to cache
	 * @param int
	 *            Lifetime to cache object for
	 **/
	public static void cacheInsert(String key, Result result, int life) throws Exception {

		// Sanity checks
		if (key == null)
			throw new Exception("Cache key must be provided");
		if (life == 0)
			life = 90;

		// Only bother if we have something to cache
		if (result != null) {

			// Insert the result into the cache
			ResultCache resultCache = ResultCacheImpl.getInstance();
			resultCache.insertIntoCache(key, result, life);
		}
	}

	/**
	 * Call an integration service operation
	 * 
	 * @param setRequest
	 *            TODO
	 * @param DataControllerRequest
	 *            The request object
	 * @param Hashmap
	 *            The hashmap containing the request input parameters
	 * @param Hashmap
	 *            The hashmap containing the request headers
	 * @param String
	 *            The name of the service operation to call
	 * @return Result Result object
	 **/
	public static Result callIntegrationService(DataControllerRequest request, Map<String, Object> params,
			Map<String, Object> headers, String serviceName, String operationName, boolean setRequest)
					throws Exception {

		// Create service data
		OperationData serviceData = createOperationData(request, serviceName, operationName);

		// Create service request
		ServiceRequest serviceRequest = createServiceRequest(request, serviceData, params, headers, setRequest);

		// Ok now call the service
		Result result = serviceRequest.invokeServiceAndGetResult();
		return result;
	}

	/**
	 * Check that the specified input parameter exists & has a value
	 * 
	 * @param Hashmap
	 *            The hashmap containing the parameters
	 * @param String
	 *            The name of the parameter
	 * @return boolean If the parameter exists & has a value
	 **/
	public static boolean checkInputParam(@SuppressWarnings("rawtypes") HashMap params, String name) {

		boolean isValid = false;
		String value = getParamValue(params, name);
		if (!value.equalsIgnoreCase("")) {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Create a cache key
	 * 
	 * @param String
	 *            User ID
	 * @param String
	 *            Object type
	 * @return String Cache key
	 **/
	public static String createCacheKey(String user, String objectType) throws Exception {

		// Sanity check
		if (user == null || user.equalsIgnoreCase(""))
			throw new Exception("User must be provided");
		if (objectType == null || objectType.equalsIgnoreCase(""))
			throw new Exception("Object type must be provided");

		// Build the key
		StringBuilder sb = new StringBuilder();
		sb.append("ObjectID eq ");
		sb.append(objectType);
		sb.append(" CacheID eq *");
		sb.append(" UserId eq ");
		sb.append(user);
		return sb.toString();
	}

	/**
	 * Create exception message
	 * 
	 * @param Exception
	 *            Exception object
	 * @return String String message
	 **/
	public static String createExceptiontext(Exception ex) throws Exception {

		StringBuilder sb = new StringBuilder();
		sb.append("Exception: ");
		sb.append(ex.getMessage());
		alert.prepareError(ex.toString()).log();
		return sb.toString();
	}

	/**
	 * Create an Operation Data Object
	 * 
	 * @param DataControllerRequest
	 *            Request object
	 * @param String
	 *            Service Name
	 * @param String
	 *            Operation Name
	 * @return OperationData An operation data object
	 **/
	private static OperationData createOperationData(DataControllerRequest request, String service, String operation)
			throws Exception {

		// Generate OperationData object
		OperationData serviceData = request.getServicesManager().getOperationDataBuilder().withServiceId(service)
				.withOperationId(operation).build();

		return serviceData;
	}

	/**
	 * Create a Service Request Object
	 * 
	 * @param setRequest
	 *            TODO
	 * @param DataControllerRequest
	 *            Request object
	 * @param OperationData
	 *            Operation Data object
	 * @param Map<String,Object>
	 *            Input parameters map
	 * @param Map<String,Object>
	 *            Headers map
	 * @return ServiceRequest A service request object
	 **/
	private static ServiceRequest createServiceRequest(DataControllerRequest request, OperationData serviceData,
			Map<String, Object> params, Map<String, Object> headers, boolean setRequest) throws Exception {
		ServiceRequest serviceRequest;
		if (setRequest) {
			serviceRequest = request.getServicesManager().getRequestBuilder(serviceData).withInputs(params)
					.withDCRRequest(request).withHeaders(headers).build();
		} else {
			serviceRequest = request.getServicesManager().getRequestBuilder(serviceData).withInputs(params)
					.withHeaders(headers).build();
		}
		// Generate ServiceRequest object

		return serviceRequest;
	}


	/**
	 * Get the day of the month
	 * 
	 * @param String
	 *            Date to parse
	 **/
	public static String getDayOfMonth(String date) {

		// Parse the date string
		String dayOfMonth = "";
		int day;
		LocalDate localDate = LocalDate.parse(date);
		day = localDate.getDayOfMonth();
		dayOfMonth = String.valueOf(day);
		return dayOfMonth;
	}

	/**
	 * Get the day of the week
	 * 
	 * @param String
	 *            Date to parse
	 **/
	public static String getDayOfWeek(String date) {

		// Parse the date string
		String day = "";
		LocalDate localDate = LocalDate.parse(date);
		DayOfWeek dow = localDate.getDayOfWeek();
		day = dow.getDisplayName(TextStyle.FULL, Locale.getDefault());
		return day;
	}


	/**
	 * Get a parameter value from a hashmap
	 * 
	 * @param Hashmap
	 *            The hashmap containing the parameter
	 * @param String
	 *            The name of the parameter key
	 * @return String The value
	 **/
	public static String getParamValue(@SuppressWarnings("rawtypes") HashMap params, String name) {

		// Get the parameter value
		String value = "";
		if (params != null && name != null) {
			if (params.get(name) != null) {
				value = params.get(name).toString();
			}
		}
		return value;
	}

	/**
	 * Get a parameter value from a record object
	 * 
	 * @param Record
	 *            The record containing the parameter
	 * @param String
	 *            The name of the parameter key
	 * @return String The value
	 **/
	public static String getParamValue(Record record, String name) {

		// Get the parameter value
		String value = "";
		if (record != null && name != null) {
			if (record.getParam(name) != null) {
				if (record.getParam(name).getValue() != null) {
					value = record.getParam(name).getValue();
				}
			}
		}
		return value;
	}

	/**
	 * Get a parameter value from a result object
	 * 
	 * @param Result
	 *            The result containing the parameter
	 * @param String
	 *            The name of the parameter key
	 * @return String The value
	 **/
	public static String getParamValue(Result result, String name) {

		// Get the parameter value
		String value = "";
		if (result != null && name != null) {
			if (result.getParamByName(name) != null) {
				if (result.getParamByName(name).getValue() != null) {
					value = result.getParamByName(name).getValue();
				}
			}
		}
		return value;
	}

	/**
	 * Parse a request query string to get the parameters
	 * 
	 * @param Request
	 *            The request object
	 * @return Map<String, String> The request query string parameters
	 **/
	public static Map<String, String> getRequestQueryParams(DataControllerRequest request) throws Exception {

		// Sanity checks
		if (request == null)
			throw new Exception("A valid request object must be provided");

		// Get the query string
		Map<String, String> query_parms = new LinkedHashMap<String, String>();
		String queryString = request.getQueryString();
		if (queryString == null || queryString.equalsIgnoreCase("")) {

			// Nothing to do so just bail.
		} else {

			// Extract the params
			String[] pairs = queryString.split("&");
			for (String pair : pairs) {
				int idx = pair.indexOf("=");
				query_parms.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
						URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
			}
		}
		return query_parms;
	}

	/**
	 * Get a session attribute
	 * 
	 * @param Session
	 *            The user's session object
	 * @param String
	 *            The name of the attribute value to extract
	 * @return Object The session attribute
	 **/
	public static Object getSessionAttribute(Session session, String name) {

		// Get the attribute if it exists
		Object obj = null;
		if (session != null && name != null && !name.equalsIgnoreCase("")) {
			if (session.getAttribute(name) != null) {
				obj = session.getAttribute(name);
			}
		}
		return obj;
	}

	/**
	 * Get a session attribute value
	 * 
	 * @param Session
	 *            The user's session object
	 * @param String
	 *            The name of the attribute value to extract
	 * @return String The value
	 **/
	public static String getSessionAttributeValue(Session session, String name) throws Exception {

		// Sanity checks
		if (session == null)
			throw new Exception("Session must be provided");
		if (name == null || name.equalsIgnoreCase(""))
			throw new Exception("Name must be provided");

		// Get the attribute value if it exists
		String value = "";
		if (session.getAttribute(name) != null) {
			value = session.getAttribute(name).toString();
		}
		return value;
	}

	/**
	 * Get a unique transaction id string
	 * 
	 * @return String The transaction id value
	 **/
	public static String getTransactionId() throws Exception {

		// Build the transaction id
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * Get a parameter value & trim any whitespace
	 * 
	 * @param Record
	 *            The record containing the desired parameter
	 * @param String
	 *            The name of the parameter value to extract
	 * @return String The value
	 **/
	public static String getTrimmedParamValue(Record record, String name) throws Exception {

		// Sanity checks
		if (record == null)
			throw new Exception("Record must be provided");
		if (name == null || name.equalsIgnoreCase(""))
			throw new Exception("Name must be provided");

		// Get the parameter value if it exists & trim any whitespace
		String value = "";
		Param param = record.getParam(name);
		if (param != null) {
			if (param.getValue() != null) {
				value = param.getValue().trim();
			}
		}
		return value;
	}

	/**
	 * Log the provided input parameters
	 * 
	 * @param Hashmap
	 *            The hashmap containing the parameters
	 * @param Logger
	 *            The logger object for the relevant pre/post processor
	 **/
	@SuppressWarnings("unchecked")
	public static void logInputParams(@SuppressWarnings("rawtypes") HashMap params, Diagnostic exitLogger)
			throws Exception {

		// Sanity checks
		if (params == null)
			throw new Exception("Populated hashmap must be provided");
		if (exitLogger == null)
			throw new Exception("Valid logger must be provided");

		// Log all input parameters
		Set<String> keySet = params.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		while (keySetIterator.hasNext()) {
			StringBuilder sb = new StringBuilder();
			String key = keySetIterator.next();
			sb.append("Using parameter ");
			sb.append(key);
			sb.append(" with value: ");
			if (params.get(key) != null) {
				sb.append(params.get(key).toString());
			} else {
				sb.append("NULL");
			}
			exitLogger.prepareDebug(sb.toString()).log();
		}
	}

	/**
	 * Log the details of a parameter object
	 * 
	 * @param Param
	 *            The parameter object
	 * @param Logger
	 *            The logger object for the relevant pre/post processor
	 **/
	public static void logParamDetails(Param param, Diagnostic exitLogger) throws Exception {

		// Sanity checks
		if (exitLogger == null)
			throw new Exception("Valid logger must be provided");

		// Only bother if we have something
		if (param != null) {

			StringBuilder sb = new StringBuilder();
			sb.append("Parameter \"");
			sb.append(param.getName());
			sb.append("\" with value: ");
			sb.append(param.getValue());
			exitLogger.prepareTrace(sb.toString()).log();
		}
	}

	/**
	 * Log the details of a record object
	 * 
	 * @param Record
	 *            The record object
	 * @param Logger
	 *            The logger object for the relevant pre/post processor
	 **/
	public static void logRecordDetails(Record record, Diagnostic exitLogger) throws Exception {

		// Sanity checks
		if (exitLogger == null)
			throw new Exception("Valid logger must be provided");

		// Only bother if we have something
		if (record != null) {

			StringBuilder sb = new StringBuilder();
			sb.append("Record id \"");
			sb.append(record.getId());
			sb.append("\" with ");
			sb.append(record.getAllParams().size());
			sb.append(" parameters: ");
			exitLogger.prepareTrace(sb.toString()).log();

			// Log any record parameters
			for (Param param : record.getAllParams()) {

				// Log param details
				logParamDetails(param, exitLogger);
			}
		}
	}

	/**
	 * Log the key details of a result object
	 * 
	 * @param Result
	 *            The result object
	 * @param Logger
	 *            The logger object for the relevant pre/post processor
	 **/
	public static void logResultDetails(Result result, Diagnostic exitLogger) throws Exception {

		// Sanity checks
		if (exitLogger == null)
			throw new Exception("Valid logger must be provided");

		// Only bother if we have something
		if (result != null) {

			// What stand-alone parameters do we have?
			if (result.getAllParams().size() > 0) {
				exitLogger.prepareTrace("Result includes stand-alone parameter(s)...").log();
			}
			for (Param param : result.getAllParams()) {

				// Log param details
				logParamDetails(param, exitLogger);
			}

			// What stand-alone records do we have?
			List<Record> recsList = result.getAllRecords();
			StringBuilder sb = new StringBuilder();
			sb.append("Result includes ");
			sb.append(recsList.size());
			sb.append(" records...");
			exitLogger.prepareTrace(sb.toString()).log();
			for (Record rec : recsList) {

				// Log record details
				logRecordDetails(rec, exitLogger);
			}

			// What datasets do we have?
			List<Dataset> dsList = result.getAllDatasets();
			for (Dataset ds : dsList) {
				StringBuilder sb2 = new StringBuilder();
				sb2.append("Result includes dataset \"");
				sb2.append(ds.getId());
				sb2.append("\" with ");
				sb2.append(ds.getAllRecords().size());
				sb2.append(" records.");
				exitLogger.prepareTrace(sb2.toString()).log();

				// What dataset records do we have?
				List<Record> dsRecsList = ds.getAllRecords();
				for (Record rec : dsRecsList) {

					// Log record details
					logRecordDetails(rec, exitLogger);
				}
			}
		}
	}

	public static String buildOdataCondition(String field, String operator, String value) {

		StringBuilder sb = new StringBuilder();
		sb.append(field);
		sb.append(" ");
		sb.append(operator);
		sb.append(" ");
		sb.append(value);
		return sb.toString();
	}

	/**
	 * get the value of Configurable param value
	 * 
	 * @param value
	 *            The value of param
	 **/
	public static String getPropertyValue(Map<String, String> serverProperties, String prop) {
		// read value from runtime configurations
		String value;
		value = serverProperties.get(prop) != null ? serverProperties.get(prop) : "";
		return value;
	}

	/**
	 * @author Gopinath Vaddepally KH2453
	 * @param key
	 * @param request
	 * @return environment property
	 */
	public static String getServerEnvironmentProperty(String key, DataControllerRequest request) {
		String serverProperty = null;
		try {
			serverProperty = request.getServicesManager().getConfigurableParametersHelper().getServerProperty(key);
		} catch (Exception e) {
			// TODO: handle exception
			alert.prepareError("error occured ").log();
		}
		return serverProperty;
	}

	/**
	 * @author Gopinath Vaddepally KH2453
	 * @see converts JSON string to dataset
	 * @param jsonString
	 * @param id
	 * @return dataset
	 */
	public static Dataset convertJSON2Dataset(String jsonString, String id) {
		Dataset dataset = new Dataset();
		dataset.setId(id);
		if ((jsonString == null) || (jsonString.length() == 0)) {
			return dataset;
		}
		try {
			if (jsonString.startsWith("{")) {
				JSONObject jsonObject = new JSONObject(jsonString);

				Iterator<?> itr = jsonObject.keys();
				if (itr.hasNext()) {
					String key = (String) itr.next();
					dataset.setId(key);

					JSONArray jsonArray = (JSONArray) jsonObject.get(key);
					convertJSONArray2Dataset(jsonArray, dataset);
				}
			} else if (jsonString.startsWith("[")) {
				convertJSONArray2Dataset(new JSONArray(jsonString), dataset);
			} else {
				alert.prepareError("JSON string is not starting with { or [").log();
			}
		} catch (JSONException jExe) {
			alert.prepareError("JSON to Dataset conversion exception", jExe).log();
		}
		return dataset;
	}

	/**
	 * @author Gopinath Vaddepally KH2453
	 * @param jsonArray
	 * @param dataset
	 * @throws JSONException
	 *             converts json array string to dataset
	 */
	private static void convertJSONArray2Dataset(JSONArray jsonArray, Dataset dataset) throws JSONException {
		if (jsonArray != null) {
			for (int idx = 0; idx < jsonArray.length(); idx++) {
				JSONObject jsonObj = (JSONObject) jsonArray.get(idx);
				Record record = new Record();
				for (Iterator<?> it = jsonObj.keys(); it.hasNext();) {
					String chKey = (String) it.next();
					Object obj = jsonObj.get(chKey);
					if ((obj instanceof JSONArray)) {
						Dataset dset = new Dataset();
						dset.setId(chKey);
						Record rec = new Record();
						for (int i = 0; i < ((JSONArray) obj).length(); i++) {
							rec = new Record();
							JSONObject jsonOb = (JSONObject) ((JSONArray) obj).get(i);
							for (Iterator<?> j = jsonOb.keys(); j.hasNext();) {
								String chhKey = (String) j.next();
								Object innerObj = jsonOb.get(chhKey);
								if ((innerObj instanceof JSONArray)) {
									Dataset innerDataset = new Dataset();
									innerDataset.setId(chhKey);
									convertJSONArray2Dataset((JSONArray) innerObj, innerDataset);
									rec.addDataset(innerDataset);
								} else {
									Param p = new Param();
									p.setName(chhKey);
									if (!jsonOb.isNull(chhKey)) {
										p.setValue(jsonOb.get(chhKey).toString());
									} else {
										p.setValue("");
									}
									rec.addParam(p);
								}
							}
							if ((rec.getAllParams().size() > 0) || (rec.getAllDatasets().size() > 0)) {
								dset.addRecord(rec);
							}
						}
						record.addDataset(dset);
					} else {
						Param param = new Param();
						param.setName(chKey);
						if (!jsonObj.isNull(chKey)) {
							param.setValue(jsonObj.get(chKey).toString());
						} else {
							param.setValue("");
						}
						record.addParam(param);
					}
				}
				dataset.addRecord(record);
			}
		}
	}


	/**
	 * @author Gopinath Vaddepally - KH2453
	 * @param serviceID
	 * @param operationID
	 * @param inputmap
	 * @param headermap
	 * @param request
	 * @param resultType
	 * @param setRequest
	 * @return Object
	 */
	public static Object callInternalService(String serviceID, String operationID, HashMap<String, Object> inputmap,
			HashMap<String, Object> headermap, DataControllerRequest request, int resultType, boolean setRequest) {
		Result result = null;
		String response = null;
		try {
			OperationData operationData = request.getServicesManager().getOperationDataBuilder()
					.withServiceId(serviceID).withOperationId(operationID).build();

			ServiceRequest serviceRequest = null;
			if (setRequest) {
				serviceRequest = request.getServicesManager()
						.getRequestBuilder(operationData).withDCRRequest(request).withInputs(inputmap)
						.withHeaders(headermap)
						.build();
			} else {
				serviceRequest = request.getServicesManager()
						.getRequestBuilder(operationData).withInputs(inputmap).withHeaders(headermap)
						.build();
			}

			switch (resultType) {
			case 1:
				result = serviceRequest.invokeServiceAndGetResult();
				break;
			case 2:
				BufferedHttpEntity bufferedResponse = serviceRequest.invokePassThroughServiceAndGetEntity();
				response = EntityUtils.toString(bufferedResponse, StandardCharsets.UTF_8);
				break;
			default:
				result = serviceRequest.invokeServiceAndGetResult();
				break;
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		switch (resultType) {
		case 1:
			return result;
		case 2:
			return response;
		default:
			return result;
		}
	}

	/**
	 * @author Gopinath Vaddepally - KH2453
	 * @param serviceID
	 * @param operationID
	 * @param inputmap
	 * @param headermap
	 * @param request
	 * @param resultType
	 * @param setRequest
	 * @return Object
	 */
	public static Object callInternalService(String serviceID, String operationID, HashMap<String, Object> inputmap,
			HashMap<String, Object> headermap, Map<String, String> input, int resultType) {
		Result result = null;
		String response = null;
		try {
			OperationData operationData = ServicesManagerHelper.getServicesManager().getOperationDataBuilder()
					.withServiceId(serviceID).withOperationId(operationID).build();

			ServiceRequest serviceRequest = null;
			serviceRequest = ServicesManagerHelper.getServicesManager()
					.getRequestBuilder(operationData).withInputs(inputmap).withHeaders(headermap)
					.build();

			switch (resultType) {
			case 1:
				result = serviceRequest.invokeServiceAndGetResult();
				break;
			case 2:
				BufferedHttpEntity bufferedResponse = serviceRequest.invokePassThroughServiceAndGetEntity();
				response = EntityUtils.toString(bufferedResponse, StandardCharsets.UTF_8);
				break;
			default:
				result = serviceRequest.invokeServiceAndGetResult();
				break;
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		switch (resultType) {
		case 1:
			return result;
		case 2:
			return response;
		default:
			return result;
		}
	}

}
