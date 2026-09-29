package com.kony.dbx.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.util.EntityUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kony.dbx.objects.Account;
import com.kony.dbx.objects.Customer;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.ehcache.ResultCacheImpl;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.session.Session;

public class CommonUtils implements Constants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

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
     * 
     * @param dcRequest
     * @param inputParams
     * @param headerParams
     * @param url
     * @return
     * @throws HttpCallException
     */
    public static Result invokeIntegrationServiceAndGetResult(DataControllerRequest request, Map<String, Object> params,
            Map<String, Object> headers, String serviceName, String operationName) {
        try {
            Result result =
                    DBPServiceInvocationWrapper.invokeServiceAndGetResult(serviceName, null, operationName,
                            params, headers, request);
            
            printLog(null + "_" + serviceName + "_" + operationName, params, headers, result, null);
            return result;
        } catch (Exception e) {
            alert.prepareError(e).log();
            return getExceptionMsgAsResult(null + "_" + serviceName + "_" + operationName +  e.getStackTrace());
        }
        
    }

    private static Result getExceptionMsgAsResult(String serviceURL) {
        Result result = new Result();
        StringBuilder message = new StringBuilder();
        message.append("Exception occured while invoking service with [ServiceId_ObjectId_OperationId] [")
                .append(serviceURL).append("]");
        result.addParam("errmsg", message.toString());
        return result;
    }

    private static void printLog(String URL, Map inputParams, Map headerParams, Result result, String response) {
        if (inputParams != null) {
            alert.prepareError("InputParams for call " + URL + " : " + inputParams).log();
        }
        if (headerParams != null) {
            alert.prepareError("HeaderParams for call " + URL + " : " + headerParams).log();
        }
        if (result != null) {
            alert.prepareError("Response from call " + URL + " : " + ResultToJSON.convert(result)).log();
        } else {
            alert.prepareError("Response from call " + URL + " : " + response).log();
        }
    }
	
	/*
	 * Call an object service operation
	 */

	public static Result callObjectService(DataControllerRequest request, Map<String, Object> params,
			Map<String, Object> headers, String serviceName, String objectName, String operationName,
			boolean setRequest) throws Exception {

		// Create service data
		OperationData serviceData = createOperationData(request, serviceName, objectName, operationName);

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
		alert.prepareError(ex).log();
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

	/*
	 * Overloaded method to build OperationData for Object service
	 */
	private static OperationData createOperationData(DataControllerRequest request, String service, String object,
			String operation) throws Exception {

		// Generate OperationData object
		OperationData serviceData = request.getServicesManager().getOperationDataBuilder().withServiceId(service)
				.withObjectId(object).withOperationId(operation).build();

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
			Enumeration<String> attributeNames = (Enumeration<String>) request.getSession().getAttributeNames();
			Map<String, Object> sessionMap = new HashMap<>();
			while (attributeNames.hasMoreElements()) {
				String attributeName = (String) attributeNames.nextElement();
				sessionMap.put(attributeName, request.getSession().getAttribute(attributeName));
			}
			String authToken = request.getHeader("X-Kony-Authorization");
			//For GET requests we need to take token from Auth_Token
            if (StringUtils.isBlank(authToken)) {
                authToken = request.getParameter("Auth_Token");
                request.getHeaderMap().put("X-Kony-Authorization", authToken);
            }
			serviceRequest = request.getServicesManager().getRequestBuilder(serviceData).withInputs(params)
					.withSessionMap(sessionMap).withAuthorizationToken(authToken).withHeaders(headers).build();
		}
		// Generate ServiceRequest object

		return serviceRequest;
	}

	/**
	 * Get the customer object from the session
	 * 
	 * @param String
	 *            Date to parse
	 **/
	public static Customer getCustomerObj(DataControllerRequest request) {

		Customer customer = null;
		// Get the object from the session
		Object obj = CommonUtils.getSessionAttribute(request, Constants.SESSION_ATTRIB_CUSTOMER);

		// Cast & return
		if (obj instanceof Customer) {
			customer = (Customer) obj;
		}
		return customer;
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
	 * Get the opstatus
	 * 
	 * @param Result
	 *            The result object containing the parameters
	 **/
	public static int getOpStatus(Result result) throws Exception {

		// Sanity checks
		if (result == null)
			throw new Exception("Populated result must be provided");

		// Get the opstatus param
		int opStatus = 0;
		if (result.getParamByName(Constants.PARAM_OP_STATUS) != null) {
			if (result.getParamByName(Constants.PARAM_OP_STATUS).getValue() != null) {
				opStatus = Integer.parseInt(result.getParamByName(Constants.PARAM_OP_STATUS).getValue());
			}
		}
		return opStatus;
	}

	/**
	 * Get a parameter value from a record object
	 * 
	 * @param Dataset
	 *            The dataset containing the records to be paged
	 * @param int
	 *            The offset to use for paging
	 * @param int
	 *            The number of records to use for paging
	 * @return Dataset A result dataset containing the requested batch of records
	 **/
	public static Dataset getPagedRecords(Dataset ds, int offset, int size) {

		// Setup
		Dataset resultDs = new Dataset();
		if (ds != null) {

			// Setup
			if (size == 0)
				size = 10;
			int toIdx = offset + size;
			List<Record> recordList = ds.getAllRecords();
			int listSize = recordList.size();
			if (toIdx > listSize)
				toIdx = listSize;
			List<Record> pagedList = new ArrayList<Record>();

			// Get the batch
			pagedList = recordList.subList(offset, toIdx);
			resultDs.addAllRecords(pagedList);
			resultDs.setId(ds.getId());
		}

		// Return the result
		return resultDs;
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
	 * Get a property value for the specified key from the Kony Digital Banking
	 * properties file
	 * 
	 * @param String
	 *            Property file to lookup
	 * @param String
	 *            Section to lookup
	 * @param String
	 *            Key to lookup
	 * @param Boolean
	 *            Is the property required
	 * @return String Property value
	 **/
	public static String getProperty(String propFile, String section, String key, boolean required) throws Exception {

		// Sanity checks
		if (propFile == null || propFile.equalsIgnoreCase(""))
			throw new Exception("Properties file must be provided");
		if (section == null || section.equalsIgnoreCase(""))
			throw new Exception("Section must be provided");
		if (key == null || key.equalsIgnoreCase(""))
			throw new Exception("Key must be provided");

		// First we try to read in the properties file
		Properties properties = getProperties(propFile);

		// Now return the value for the specified section.key
		StringBuilder sb = new StringBuilder();
		sb.append(section);
		sb.append(".");
		sb.append(key);
		String propKey = sb.toString();
		String value = properties.getProperty(propKey);
		if (required) {

			// Property is required so check it exists
			if (value == null || value.equalsIgnoreCase("")) {

				StringBuilder errMsg = new StringBuilder();
				errMsg.append("Property: ");
				errMsg.append(propKey);
				errMsg.append(" not found in file: ");
				errMsg.append(propFile);
				throw new Exception(errMsg.toString());
			}
		}
		return value;
	}

	/**
	 * Get a property value for the specified key from the Kony Digital Banking
	 * properties file
	 * 
	 * @param String
	 *            Property file to lookup
	 * @param String
	 *            Group to lookup
	 * @param String
	 *            Section to lookup
	 * @param String
	 *            Key to lookup
	 * @return String Property value
	 **/
	public static String getProperty(String propFile, String group, String section, String key) throws Exception {

		// Sanity checks
		if (propFile == null || propFile.equalsIgnoreCase(""))
			throw new Exception("Properties file must be provided");
		if (group == null || group.equalsIgnoreCase(""))
			throw new Exception("Group must be provided");
		if (section == null || section.equalsIgnoreCase(""))
			throw new Exception("Section must be provided");
		if (key == null || key.equalsIgnoreCase(""))
			throw new Exception("Key must be provided");

		// First we try to read in the properties file
		Properties properties = getProperties(propFile);

		// Now return the value for the specified group.section.key
		StringBuilder sb = new StringBuilder();
		sb.append(group);
		sb.append(".");
		sb.append(section);
		sb.append(".");
		sb.append(key);
		return properties.getProperty(sb.toString());
	}

	/**
	 * Get properties from the specified properties file
	 * 
	 * @param String
	 *            Property file to lookup
	 * @return String Property value
	 **/
	private static Properties getProperties(String propFile) throws Exception {

		// Validations
		if (propFile == null || propFile.equalsIgnoreCase(""))
			throw new Exception("Properties file must be provided");

		Properties properties = new Properties();
		InputStream propertiesStream = null;

		// First we try to read in the properties file
		try {
			propertiesStream = CommonUtils.class.getClassLoader().getResourceAsStream(propFile);
			if (propertiesStream != null)
				properties.load(propertiesStream);
		} catch (IOException ex) {
			alert.prepareError("Unable to read the properties file: " + propFile).log();
		} finally {
			if (propertiesStream != null) {
				try {
					propertiesStream.close();
				} catch (IOException e) {
					alert.prepareError("Unable to close the properties file inputstream").log();
				}
			}
		}
		return properties;
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
	 * Get the limit query parameter
	 * 
	 * @param Request
	 *            The request object
	 * @return String The request sortBy parameter
	 **/
	public static String getRequestLimit(DataControllerRequest request) throws Exception {

		String limit = null;
		Map<String, String> query_parms = getRequestQueryParams(request);
		if (query_parms.get(Constants.QUERY_PARAM_LIMIT) != null) {
			limit = query_parms.get(Constants.QUERY_PARAM_LIMIT);
		}
		return limit;
	}

	/**
	 * Get the offset query parameter
	 * 
	 * @param Request
	 *            The request object
	 * @return String The request sortBy parameter
	 **/
	public static String getRequestOffset(DataControllerRequest request) throws Exception {

		String offset = null;
		Map<String, String> query_parms = getRequestQueryParams(request);
		if (query_parms.get(Constants.QUERY_PARAM_OFFSET) != null) {
			offset = query_parms.get(Constants.QUERY_PARAM_OFFSET);
		}
		return offset;
	}

	/**
	 * Get the sortBy query parameter
	 * 
	 * @param Request
	 *            The request object
	 * @return String The request sortBy parameter
	 **/
	public static String getRequestSortBy(DataControllerRequest request) throws Exception {

		String sortBy = null;
		Map<String, String> query_parms = getRequestQueryParams(request);
		if (query_parms.get(Constants.QUERY_PARAM_SORTBY) != null) {
			sortBy = query_parms.get(Constants.QUERY_PARAM_SORTBY);
		}
		return sortBy;
	}

	/**
	 * Get the order query parameter
	 * 
	 * @param Request
	 *            The request object
	 * @return String The request sortBy parameter
	 **/
	public static String getRequestSortOrder(DataControllerRequest request) throws Exception {

		String order = null;
		Map<String, String> query_parms = getRequestQueryParams(request);
		if (query_parms.get(Constants.QUERY_PARAM_ORDER) != null) {
			order = query_parms.get(Constants.QUERY_PARAM_ORDER);
		}
		return order;
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
	public static Object getSessionAttribute(DataControllerRequest request, String name) {

		// Get the attribute if it exists
		Object obj = CommonUtils.retreiveFromSession(name, request);
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
	public static String getSessionAttributeValue(DataControllerRequest request, String name) throws Exception {

		// Get the attribute value if it exists
		String value = (String) CommonUtils.retreiveFromSession(name, request);
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
	public static void logInputParams(@SuppressWarnings("rawtypes") HashMap params, Logger exitLogger)
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
			exitLogger.debug(sb.toString());
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
	public static void logParamDetails(Param param, Logger exitLogger) throws Exception {

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
			exitLogger.trace(sb.toString());
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
	public static void logRecordDetails(Record record, Logger exitLogger) throws Exception {

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
			exitLogger.trace(sb.toString());

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
	public static void logResultDetails(Result result, Logger exitLogger) throws Exception {

		// Sanity checks
		if (exitLogger == null)
			throw new Exception("Valid logger must be provided");

		// Only bother if we have something
		if (result != null) {

			// What stand-alone parameters do we have?
			if (result.getAllParams().size() > 0) {
				exitLogger.trace("Result includes stand-alone parameter(s)...");
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
			exitLogger.trace(sb.toString());
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
				exitLogger.trace(sb2.toString());

				// What dataset records do we have?
				List<Record> dsRecsList = ds.getAllRecords();
				for (Record rec : dsRecsList) {

					// Log record details
					logRecordDetails(rec, exitLogger);
				}
			}
		}
	}

	/**
	 * Log the opstatus & errmsg parameters
	 * 
	 * @param Result
	 *            The result object containing the parameters
	 * @param Logger
	 *            The logger object for the relevant pre/post processor
	 **/
	public static void logOpStatus(Result result, Logger exitLogger) throws Exception {

		// Sanity checks
		if (result == null)
			throw new Exception("Populated result must be provided");
		if (exitLogger == null)
			throw new Exception("Valid logger must be provided");

		// Process the opstatus param
		if (result.getParamByName(Constants.PARAM_OP_STATUS) != null) {
			if (result.getParamByName(Constants.PARAM_OP_STATUS).getValue() != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("opstatus set to: ");
				sb.append(result.getParamByName(Constants.PARAM_OP_STATUS).getValue());
				exitLogger.debug(sb.toString());
			}
		}

		// Process the errmsg param
		if (result.getParamByName(Constants.PARAM_ERR_MSG) != null) {
			if (result.getParamByName(Constants.PARAM_ERR_MSG).getValue() != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("errmsg set to: ");
				sb.append(result.getParamByName(Constants.PARAM_ERR_MSG).getValue());
				exitLogger.debug(sb.toString());
			}
		}
	}

	/**
	 * Set the errmsg parameter to the specified string
	 * 
	 * @param Result
	 *            The result object
	 * @param String
	 *            The value to set errmsg to
	 **/
	public static void setErrMsg(Result result, String text) {

		// Sanity checks
		if (text == null)
			text = "Error detected";

		// Set the errmsg parameter
		result.addParam(new Param(Constants.PARAM_ERR_MSG, text, Constants.PARAM_DATATYPE_STRING));
	}

	/**
	 * Set the opstatus parameter to a value of 5000
	 * 
	 * @param Result
	 *            The result object
	 **/
	public static void setOpStatusError(Result result) {

		// Set the opstatus parameter
		result.addParam(
				new Param(Constants.PARAM_OP_STATUS, Constants.PARAM_OP_STATUS_ERROR, Constants.PARAM_DATATYPE_INT));
	}

	/**
	 * Set the opstatus parameter to a value of zero
	 * 
	 * @param Result
	 *            The result object
	 **/
	public static void setOpStatusOk(Result result) throws Exception {

		// Sanity checks
		if (result == null)
			throw new Exception("Result object must be provided");

		// Set the opstatus parameter
		result.addParam(
				new Param(Constants.PARAM_OP_STATUS, Constants.PARAM_OP_STATUS_OK, Constants.PARAM_DATATYPE_INT));
	}

	/**
	 * Set the errmsg parameter to the specified string
	 * 
	 * @param Result
	 *            The result object
	 * @param String
	 *            The value to set errmsg to
	 **/
	public static void setErrCode(Result result, String value) {

		// Sanity checks
		if (value == null)
			value = "99";

		// Set the errcode parameter
		result.addParam(new Param(Constants.PARAM_ERR_CODE, value, Constants.PARAM_DATATYPE_STRING));
	}

	/**
	 * Check whether email address is valid
	 * 
	 * @param Email
	 *            address to validate
	 * @return Boolean Whether email address is valid
	 **/
	public static boolean validateEmailAddress(String emailAddress) {

		boolean validEmailAddress = false;
		if (emailAddress == null || emailAddress.equalsIgnoreCase("")) {
		} else {
			Pattern pattern = Pattern.compile(Constants.VALID_EMAIL_ADDRESS_REGEX);
			Matcher matcher = pattern.matcher(emailAddress);
			if (matcher.matches()) {
				validEmailAddress = true;
			} else {
			}
		}
		return validEmailAddress;
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
		return replacePlaceHolders(value,serverProperties);
		
	}
	/**
	 * get the values of placeholders from server properties and replaces them
	 * 
	 * @param value
	 *            The value of param
	 **/

	public static String replacePlaceHolders(String value, Map<String, String> serverProperties) {
		String placeHolder=StringUtils.substringBetween(value, "${", "}");
		while(StringUtils.isNotBlank(placeHolder))
		{
		  value= value.replace("${"+placeHolder+"}", CommonUtils.getPropertyValue(serverProperties, placeHolder));
		  placeHolder=StringUtils.substringBetween(value, "${", "}");
		}
			
		return value;
	}

	/**
	 * get the decrypted value of Configurable encrypted param value
	 * 
	 * @param decrypedValue
	 *            The decrypted value of param
	 **/
	public static String getPropertyDecryptValue(Map<String, String> serverProperties, String prop) throws Exception {
		// read value from runtime configurations
		String value;
		String decrypedValue = "";
		value = serverProperties.get(prop) != null ? serverProperties.get(prop) : "";
		if (!"".equalsIgnoreCase(value)) {
			String encryptionKey = serverProperties.get(Constants.ENCRYPTED_KEY) != null
					? serverProperties.get(Constants.ENCRYPTED_KEY)
					: "";
			decrypedValue = EncryptionUtils.decrypt(value, encryptionKey);
		}
		return decrypedValue;
	}

	/**
	 * @see fetches stored user accounts from session and converts to map
	 * @param session
	 * @return Map contains all user accounts
	 */
	public static HashMap<String, Account> getAccountsMapFromSession(Session session) {
		HashMap<String, Account> accounts = null;
		if (session != null) {
			String AccountsInSessionGson = session.getAttribute(SESSION_ATTRIB_ACCOUNT) != null
					? session.getAttribute(SESSION_ATTRIB_ACCOUNT).toString()
					: "";
			Gson gson = new Gson();
			Type AccountMapType = new TypeToken<HashMap<String, Account>>() {
			}.getType();
			HashMap<String, Account> accountsInSession = gson.fromJson(AccountsInSessionGson, AccountMapType);
			if (accountsInSession != null) {
				accounts = (HashMap<String, Account>) accountsInSession;
			}
		}
		return accounts;
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
			 return com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler
	                    .getServerAppProperty(key, request.getServicesManager());
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

	public static String convertDateToYYYYMMDD(String date) {
		if (date.length() > 10) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_YYYYMMDD);
			Date parsedDate;
			try {
				parsedDate = simpleDateFormat.parse(date);
				return simpleDateFormat.format(parsedDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				alert.prepareError(e).log();
				return "";
			}
		}
		return date;
	}

	public static HashMap<String, Account> getAccountsMapFromSession(Session session, String attributeName) {
		HashMap<String, Account> accounts = null;
		if (session != null) {
			String AccountsInSessionGson = session.getAttribute(attributeName) != null
					? session.getAttribute(attributeName).toString()
					: "";
			Gson gson = new Gson();
			Type AccountMapType = new TypeToken<HashMap<String, Account>>() {
			}.getType();
			HashMap<String, Account> accountsInSession = gson.fromJson(AccountsInSessionGson, AccountMapType);
			if (accountsInSession != null) {
				accounts = (HashMap<String, Account>) accountsInSession;
			}
		}
		return accounts;
	}

	/**
	 * @author Gopinath Vaddepally - KH2453
	 * @param records
	 *            creates hash map with account guid as key and value as account
	 *            object
	 * @return HashMap<String, Account>
	 */
	public static HashMap<String, Account> createAccountsObjectFromRecords(List<Record> records) {
		HashMap<String, Account> accounts = new HashMap<String, Account>();
		if (records != null && records.size() > 0) {
			for (Record record : records) {
				Account account = copyToAccount(Account.class, record);
				accounts.put(account.getAccountGuid(), account);
			}
		}
		return accounts;
	}

	/**
	 * @author Gopinath Vaddepally - KH2453
	 * @param request
	 * @param records
	 *            stores account object in session
	 */
	public static void storeAccountsInSession(DataControllerRequest request, Dataset accounts) {
		List<Record> records = accounts.getAllRecords();
		HashMap<String, Account> accountsMap = createAccountsObjectFromRecords(records);
		Session session = request.getSession();
		Gson gson = new Gson();
		String gsonAccounts = gson.toJson(accountsMap);
		session.setAttribute(SESSION_ATTRIB_ACCOUNT, gsonAccounts);
		alert.prepareError("inside storeAccountsInSession gsonAccounts=>" + gsonAccounts).log();
	}

	/**
	 * Copy a record to POJO Object
	 * 
	 * @param object
	 *            Object where to copy
	 * @param record
	 *            Record from backend
	 * 
	 * @return Account
	 **/
	public static Account copyToAccount(Class<Account> destinationClass, Record record) {
		Account account = new Account();
		List<Param> params = record.getAllParams();
		for (Param param : params) {
			String name = param.getName();
			String value = param.getValue();
			String methodName = "set" + toCamelCase(name);
			try {
				Method method = destinationClass.getDeclaredMethod(methodName, String.class);
				method.invoke(account, value);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// Simply ignore coping the data if the method doesn't exist
				// alert.prepareError(e).log();
			}
		}
		return account;
	}

	/**
	 * To change the given string to camel case, which is useful in cases where we
	 * don't need to define multiple constants.
	 * 
	 * @param String
	 *            input
	 * 
	 * @return String
	 **/
	public static String toCamelCase(String inputString) {
		String result = "";
		if (inputString.length() == 0) {
			return result;
		}
		char firstChar = inputString.charAt(0);
		char firstCharToUpperCase = Character.toUpperCase(firstChar);
		result = result + firstCharToUpperCase;
		for (int i = 1; i < inputString.length(); i++) {
			char currentChar = inputString.charAt(i);
			result = result + currentChar;
		}
		return result;
	}

	/**
	 * @param number
	 * @return masked string
	 */
	public static String mask(String number) {
		// TODO Auto-generated method stub
		if (number == null || number.equals(""))
			return "";
		int end = number.length() - 4;
		if (end > number.length())
			end = number.length();

		int maskLength = end - 0;

		if (maskLength == 0)
			return number;

		StringBuilder sbMaskString = new StringBuilder(maskLength);

		for (int i = 0; i < maskLength; i++) {
			sbMaskString.append('X');
		}

		return number.substring(0, 0) + sbMaskString.toString() + number.substring(0 + maskLength);

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
	 * @param sessionMap
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
				serviceRequest = request.getServicesManager().getRequestBuilder(operationData).withDCRRequest(request)
						.withInputs(inputmap).withHeaders(headermap).build();
			} else {
				Enumeration<String> attributeNames = (Enumeration<String>) request.getSession().getAttributeNames();
				Map<String, Object> sessionMap = new HashMap<>();
				while (attributeNames.hasMoreElements()) {
					String attributeName = (String) attributeNames.nextElement();
					sessionMap.put(attributeName, request.getSession().getAttribute(attributeName));
				}
				serviceRequest = request.getServicesManager().getRequestBuilder(operationData).withInputs(inputmap)
						.withHeaders(headermap).withSessionMap(sessionMap)
						.withAuthorizationToken(request.getHeader("X-Kony-Authorization")).build();
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
			alert.prepareError(e).log();
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

	public static String getUserAttributeFromIdentity(DataControllerRequest request, String attribute) {
		// TODO Auto-generated method stub
		try {
			if (request.getServicesManager() != null && request.getServicesManager().getIdentityHandler() != null) {
				Map<String, Object> userMap = request.getServicesManager().getIdentityHandler().getUserAttributes();
				if (userMap.get(attribute) != null) {
					String attributeValue = userMap.get(attribute) + "";
					alert.prepareError("value of " + attribute + "from identity is " + attributeValue).log();
					return attributeValue;
				}
				alert.prepareError("value of " + attribute + "from identity is null").log();
			}
		} catch (Exception e) {
			alert.prepareError(e).log();
		}

		return "";
	}

	/**
	 * with DCR
	 * 
	 * @param key
	 * @param value
	 * @param request
	 * @throws Exception
	 */
	public static <T> void insertIntoSession(String key, T value, DataControllerRequest request) {
		try {
			ServicesManager servicesManager = request.getServicesManager();
			String sessionId = request.getHeader(Constants.DEVICE_ID);
			insertDataIntoCache(servicesManager, key, value, sessionId);
		} catch (Exception e) {
			alert.prepareError("Exception occured:" + e).log();
		}
	}

	public static <T> void insertIntoSession(String key, T value, FabricRequestManager request) {
		try {
			ServicesManager servicesManager = request.getServicesManager();
			String sessionId = request.getHeadersHandler().getHeader(Constants.DEVICE_ID);
			insertDataIntoCache(servicesManager, key, value, sessionId);
		} catch (Exception e) {
			alert.prepareError("Exception occured:" + e).log();
		}
	}

	/**
	 * inserts data into cache
	 * 
	 * @param servicesManager
	 * @param key
	 * @param value
	 * @param sessionId
	 */
	private static <T> void insertDataIntoCache(ServicesManager servicesManager, String key, T value,
			String sessionId) {
		try {
			HashMap<String, T> sessionCacheMap;
			String cacheKey = "";
			/*
			 * check whether already the cache exists
			 */
			String userId = servicesManager.getIdentityHandler() != null

					? servicesManager.getIdentityHandler().getUserId()
					: "";
			/*
			 * checking the flow - if userid exists post login else pre login
			 */
			cacheKey = StringUtils.isNotBlank(userId)
					&& !Constants.USER_ID_ANONYMOUS.equalsIgnoreCase(servicesManager.getIdentityHandler().getUserId())
							? userId
							: sessionId;
			cacheKey = cacheKey+"_"+key;
			ResultCache resultCache = null;
			resultCache = ServicesManagerHelper.getServicesManager().getResultCache();

			/*Object cachedData = resultCache.retrieveFromCache(cacheKey);
			if (cachedData == null) {
				sessionCacheMap = new HashMap<>();
			} else {
				Gson gson = new Gson();
				Type type = new TypeToken<HashMap<String, Object>>() {
				}.getType();
				sessionCacheMap = gson.fromJson((String) cachedData, type);
			}
			sessionCacheMap.put(key, value);
			Gson gson = new Gson();
			String jsonString = gson.toJson(sessionCacheMap);
			resultCache.insertIntoCache(cacheKey, jsonString, 3600);*/
			resultCache.insertIntoCache(cacheKey, value, 3600);
		} catch (Exception e) {
			alert.prepareError("Exception occured while storing data into ResultCache" + e).log();
		}
	}

	/**
	 * with DCR
	 * 
	 * @param key
	 * @param dcRequest
	 * @return
	 * @throws Exception
	 */
	public static Object retreiveFromSession(String key, DataControllerRequest dcRequest) {
		try {
			ServicesManager servicesManager = dcRequest.getServicesManager();
			String sessionId = dcRequest.getHeader(Constants.DEVICE_ID);
			return retriveDataFromCache(servicesManager, key, sessionId);
		} catch (Exception e) {
			alert.prepareError("Exception occured:" + e).log();
			return null;
		}

	}

	public static Object retreiveFromSession(String key, FabricRequestManager dcRequest) {
		try {
			ServicesManager servicesManager = dcRequest.getServicesManager();
			String header = dcRequest.getHeadersHandler().getHeader(Constants.DEVICE_ID);
			return retriveDataFromCache(servicesManager, key, header);
		} catch (Exception e) {
			alert.prepareError("Exception occured:" + e).log();
			return null;
		}

	}

	/**
	 * retrieves data from cache
	 * 
	 * @param servicesManager
	 * @param result
	 * @param key
	 * @param sessionId
	 */
	private static Object retriveDataFromCache(ServicesManager servicesManager, String key, String sessionId) {
		Object result = null;
		try {
			String cacheKey = "";
			String userId = servicesManager.getIdentityHandler() != null
					? servicesManager.getIdentityHandler().getUserId()
					: "";
			cacheKey = StringUtils.isNotBlank(userId) && !Constants.USER_ID_ANONYMOUS.equalsIgnoreCase(userId) ? userId
					: sessionId;
			cacheKey = cacheKey + "_" + key;
			ResultCache resultCache = servicesManager.getResultCache();
			String valueInCache = "";
			try {
				valueInCache = resultCache.retrieveFromCache(cacheKey) != null
						? (String) resultCache.retrieveFromCache(cacheKey)
						: null;
			} catch (Exception e) {
				try {
					valueInCache = (String) ServicesManagerHelper.getServicesManager().getResultCache()
							.retrieveFromCache(cacheKey);
				} catch (MiddlewareException e1) {
					alert.prepareError(e1).log();
					valueInCache = (String) ResultCacheImpl.getInstance().retrieveFromCache(cacheKey);
				}
			}
			/*if (StringUtils.isNotBlank(valueInCache)) {
				Gson gson = new Gson();
				Type type = new TypeToken<HashMap<String, Object>>() {
				}.getType();
				Map<String, Object> resultMap = gson.fromJson(valueInCache, type);
				if (resultMap != null) {
					if (resultMap.get(key) != null) {
						result = resultMap.get(key);
					}
				}
			}*/
			result = valueInCache;
		} catch (Exception e) {
			alert.prepareError("Exception occured while retrieving from cache" + e).log();
			return null;
		}
		return result;
	}

	public static String getBackendId(String backendIdentifiers, String templateName) {
		String BackendId = getCoreId(backendIdentifiers, templateName, PARAM_CUSTOMER_ID, Constants.YES);
		return BackendId;
	}

	public static String getCoreId(String backendIdentifiers, String BackendType, String IdentifierName,
			String SequenceNumber) {

		JSONObject backendIdentifiersJSON = new JSONObject(backendIdentifiers);
		JSONArray templateIdentifiers = backendIdentifiersJSON.getJSONArray(BackendType);
		String BackendId = null;
		for (int i = 0; i < templateIdentifiers.length(); i++) {
			String identifier_name = templateIdentifiers.getJSONObject(i).getString(IDENTIFIER_NAME);
			String sequenceNumber = templateIdentifiers.getJSONObject(i).getString(SEQUENCE_NUMBER);

			if (IdentifierName.equals(identifier_name) && SequenceNumber.equals(sequenceNumber)) {
				BackendId = templateIdentifiers.getJSONObject(0).getString(BACKEND_ID);
			}
		}
		return BackendId;
	}

	/**
	 * Build a group search query
	 * 
	 * @param String
	 *            The name of the field
	 * @param String
	 *            The operator to use
	 * @param String
	 *            The value to use
	 * @return String The query
	 **/
	public static String buildSearchGroupQuery(String left, String operator, String right, Boolean group) {

		StringBuilder sb = new StringBuilder();
		if (group) {
			sb.append("(");
		}
		sb.append(left);
		sb.append(" ");
		sb.append(operator);
		sb.append(" ");
		sb.append(right);
		if (group) {
			sb.append(")");
		}
		return sb.toString();
	}

	public static String getBackendIdFromCache(DataControllerRequest request, String backendType,
			String identifier_name, String sequenceNumber) {
		String backendId = "";
		try {
			/*
			 * Check in session whether we are retrieve it or not
			 */
			backendId = CommonUtils.retreiveFromSession(BACKEND_ID, request) != null
					? CommonUtils.retreiveFromSession(BACKEND_ID, request).toString()
					: "";
			if (StringUtils.isBlank(backendId)) {
				String customerId = CommonUtils.getUserAttributeFromIdentity(request, Constants.PARAM_USER_ID);
				if (StringUtils.isNotBlank(customerId)) {
					HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
					HashMap<String, Object> svcParams = new HashMap<String, Object>();
					String filter = CommonUtils.buildOdataCondition(Constants.CUSTOMER_ID, Constants.EQUAL, customerId);
					filter = StringUtils.isNotBlank(sequenceNumber)
							? CommonUtils
									.buildSearchGroupQuery(filter, Constants.AND,
											CommonUtils.buildOdataCondition(Constants.PARAM_SEQUENCENUMBER,
													Constants.EQUAL, sequenceNumber),
											false)
							: filter;
					filter = StringUtils.isNotBlank(backendType)
							? CommonUtils.buildSearchGroupQuery(filter, Constants.AND,
									CommonUtils.buildOdataCondition(BACKEND_TYPE, Constants.EQUAL, backendType), false)
							: filter;
					svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
					Result DB_CustResult = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
							Constants.DBX_DB_SERVICE_NAME, Constants.DBX_DB_BACKEND_IDENTIFIER_GET, false);
					Dataset backendIdentifiersDataset = DB_CustResult.getDatasetById(Constants.DS_BACKEND_IDENTIFIER);
					if (backendIdentifiersDataset != null) {
						for (Record rec : backendIdentifiersDataset.getAllRecords()) {
							if (identifier_name.equals(rec.getParamValueByName(Constants.PARAM_IDENTIFIER_NAME))) {
								backendId = rec.getParamValueByName(Constants.BACKEND_ID);
								CommonUtils.insertIntoSession(Constants.BACKEND_ID, backendId, request);
								return backendId;
							}

						}
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error while retrieving backend").log();
		}
		return backendId;

	}

	/**
	 * Create a unique account ID for all accounts
	 * 
	 * @param id
	 *            Account id
	 * @param CustomerId
	 *            CustomerId from Core
	 * @param type
	 *            Account type from core
	 * @param code
	 *            code from core Number
	 * @return String Compound account ID.
	 **/
	public static String compossAccountID(String id, String CustomerId, String type, String code) {
		StringBuffer accountID = new StringBuffer();

		if (StringUtils.isNotBlank(type)) {
			accountID.append(type);
			accountID.append(Constants.CONSTANT_DELIMETER);
		}

		if (StringUtils.isNotBlank(code)) {
			accountID.append(code);
			accountID.append(Constants.CONSTANT_DELIMETER);
		}

		if (StringUtils.isNotBlank(CustomerId)) {
			accountID.append(CustomerId);
			accountID.append(Constants.CONSTANT_DELIMETER);
		}

		// Default ID is id returned from SymXchanage
		if (StringUtils.isNotBlank(id)) {
			accountID.append(id);
		}

		return accountID.toString();
	}

	/**
	 * Decompose the account ID from client application
	 * 
	 * @param accountID
	 *            The compound accountID from client application
	 * 
	 * @return HashMap Split values of the accountID. It may contain Account Number
	 *         or Member Number, Share or Loan Id and type string
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap<String, String> decompossAccountID(String accountID) {
		HashMap accountInfo = new HashMap<String, String>();

		String[] accountIDValues = accountID.split(Constants.CONSTANT_DELIMETER);

		if (accountIDValues.length == 1) {
			accountInfo.put(Constants.PARAM_ID, accountIDValues[0]);
		} else if (accountIDValues.length == 2) {
			accountInfo.put(Constants.CONSTANT_UNIQUE_ID, accountIDValues[0]);
			accountInfo.put(Constants.PARAM_ID, accountIDValues[1]);
		} else if (accountIDValues.length == 3) {
			accountInfo.put(Constants.PARAM_TYPE, accountIDValues[0]);
			accountInfo.put(Constants.CONSTANT_UNIQUE_ID, accountIDValues[1]);
			accountInfo.put(Constants.PARAM_ID, accountIDValues[2]);
		} else if (accountIDValues.length == 4) {
			accountInfo.put(Constants.PARAM_TYPE, accountIDValues[0]);
			accountInfo.put(Constants.PARAM_CODE, accountIDValues[1]);
			accountInfo.put(Constants.CONSTANT_UNIQUE_ID, accountIDValues[2]);
			accountInfo.put(Constants.PARAM_ID, accountIDValues[3]);
		}

		return accountInfo;
	}
	public static String callPassthroughIntegrationService(DataControllerRequest request, Map<String, Object> params,
			Map<String, Object> headers, String serviceName, String operationName, boolean setRequest)
			throws Exception {

		// Create service data
		OperationData serviceData = createOperationData(request, serviceName, operationName);

		// Create service request
		ServiceRequest serviceRequest = createServiceRequest(request, serviceData, params, headers, setRequest);
		
		// Ok now call the service
		BufferedHttpEntity response = serviceRequest.invokePassThroughServiceAndGetEntity();
		return EntityUtils.toString(response, StandardCharsets.UTF_8);
	}
	 
	public static boolean isDMSIntegrationEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDMSIntegrationEnabled = configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED");
			if(StringUtils.isBlank(isDMSIntegrationEnabled))
				return false;
			return BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}

}
