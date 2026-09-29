package com.kony.kmsinvoke.util;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.kony.kmsinvoke.businessdelegate.api.LogNotificationStatusBusinessDelegate;
import com.kony.kmsinvoke.httputils.HttpCallException;
import com.kony.kmsinvoke.httputils.HttpConnector;

public class HelperMethods {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static boolean isThreadCreated = false;

	private HelperMethods() {
	}

	public static Result returnResult(boolean flag, KmsInvokeEnum err) {
		Result res = new Result();
		if (flag) {
			res.addParam(new Param(KmsInvokeConstants.SUCCESS, KmsInvokeConstants.TRUE, KmsInvokeConstants.STRING));
		} else {
			res.addParam(new Param(KmsInvokeConstants.SUCCESS, KmsInvokeConstants.FALSE, KmsInvokeConstants.STRING));
			res.addParam(new Param(KmsInvokeConstants.DBPERRMSG, err.getErrMsg(), KmsInvokeConstants.STRING));
			res.addParam(new Param(KmsInvokeConstants.DBPERRCODE, err.getErrCode(), KmsInvokeConstants.STRING));
		}
		return res;
	}

	public static JsonObject returnResultJSonObject(boolean flag, KmsInvokeEnum err) {
		JsonObject res = new JsonObject();
		if (flag) {
			res.addProperty(KmsInvokeConstants.SUCCESS, KmsInvokeConstants.TRUE);
		} else {
			res.addProperty(KmsInvokeConstants.SUCCESS, KmsInvokeConstants.FALSE);
			res.addProperty(KmsInvokeConstants.DBPERRMSG, err.getErrMsg());
			res.addProperty(KmsInvokeConstants.DBPERRCODE, err.getErrCode());
		}
		return res;
	}

	public static String getConfigProperty(String key) throws Exception {
		return EnvironmentConfigurationsHandler.getServerAppProperty(key);
	}

	public static JsonObject callhttpApi(Map<String, Object> inputparams, Map<String, String> headerparams, String url)
			throws HttpCallException {
		JsonObject response = HttpConnector.invokeHttpPost(url, inputparams, headerparams);
		return (null == response) ? new JsonObject() : response;
	}

	public static boolean isValidInput(String input) {
		if (StringUtils.isBlank(input))
			return false;
		JsonElement obj = new JsonParser().parse(input);
		return obj.isJsonObject() && !obj.isJsonNull();
	}

	public static boolean isValidInputParams(JsonObject inputparamsObj, String key) {
		if (inputparamsObj == null || inputparamsObj.isJsonNull())
			return false;
		JsonElement emailRequestObj = inputparamsObj.get(key);
		return emailRequestObj != null && emailRequestObj.isJsonObject() && !emailRequestObj.isJsonNull();

	}

	public static JsonObject returnResultJSonObject(boolean flag, String id, String message, String errorCode) {
		JsonObject resultObj = new JsonObject();
		if (flag) {
			resultObj.addProperty(KmsInvokeConstants.REFERENCEID, id);
		} else {
			resultObj.addProperty(KmsInvokeConstants.DBPERRMSG, message);
			resultObj.addProperty(KmsInvokeConstants.DBPERRCODE, errorCode);
		}
		return resultObj;
	}

	public static String callInternalService(Map<String, Object> requestParameters, String serviceid,
			String operationid, String objectid) {
		if (serviceid == null || operationid == null)
			return null;
		try {
			DBPServiceExecutorBuilder db = DBPServiceExecutorBuilder.builder().withServiceId(serviceid)
					.withOperationId(operationid);
			if (objectid != null)
				db = db.withObjectId(objectid);
			return db.withRequestParameters(requestParameters).build().getResponse();
		} catch (Exception e) {
			alert.prepareError("error db service ", e).log();

		}
		return null;
	}

	public static String replaceSchemaName(String operationid, String schemaname) {
		if (operationid == null || schemaname == null)
			return operationid;
		if (operationid.contains("{schema_name}"))
			operationid = operationid.replace("{schema_name}", schemaname);
		return operationid;

	}

	public static void insertToAlertHistory(JsonObject inputParamsObj, JsonObject logparamsObject,
			JsonObject kmsResponse, String channel) {
		if (ThreadExecutor.getExecutor() == null || ThreadExecutor.getExecutor().isShutdown())
			ThreadExecutor.createExecutor(getExecutorSize());
		if (QueryClass.getQueries() == null) {
			QueryClass.setQueries();
		}
		BlockingQueue<Map<String, Object>> queue = QueryClass.getQueries();
		synchronized (queue) {
			queue.add(insert(inputParamsObj, logparamsObject, kmsResponse, channel));
		}
		if (!isThreadCreated) {
			Runnable worker = new Worker();
			isThreadCreated = true;
			ThreadExecutor.execute(worker);
		}

	}

	private static Map<String, Object> insert(JsonObject inputParamsObj, JsonObject logparamsObject,
			JsonObject kmsResponse, String channel) {
		LogNotificationStatusBusinessDelegate logEmailBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(LogNotificationStatusBusinessDelegate.class);
		return logEmailBusinessDelegate.insertToAlertHistory(channel, inputParamsObj, kmsResponse, logparamsObject);

	}

	private static int getExecutorSize() {
		int executorsize = KmsInvokeConstants.THREADSIZE;
		try {
			String poolsize = HelperMethods.getConfigProperty(KmsInvokeConstants.KMS_INVOKE_THREADPOOLSIZE);

			if (poolsize != null) {
				executorsize = Integer.parseInt(poolsize);
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return executorsize;
	}

}
