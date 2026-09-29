package com.kony.adminconsole.licensing.javaservice;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.fasterxml.jackson.databind.deser.std.MapEntryDeserializer;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.utils.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

public class PushMetricsToMeteringStore implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	private static final String INPUT_RETAIL_END_USERS = "RetailEndUsers";
	private static final String INPUT_RETAIL_CLIENT_USERS = "RetailClientUsers";
	private static final String INPUT_WEALTH_END_USERS = "WealthEndUsers";
	private static final String INPUT_WEALTH_CLIENT_USERS = "WealthClientUsers";
	private static final String INPUT_BUSINESS_END_USERS = "BusinessEndUsers";
	private static final String INPUT_BUSINESS_CLIENT_USERS = "BusinessClientUsers";
	private static final String INPUT_INFINITY = "INFINITY";
	private static final String INPUT_LICENSING_UNIT = "LicensingUnit";
	private static final String BOOLEAN_FALSE = "false";

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();

		try {
			result = pushMetricData(methodId, inputArray, request, response);

		} catch (Exception e) {
			alert.prepareError("Caught exception while pushing metrics to metering store : ", e).log();
			return ErrorCodeEnum.ERR_22200.setErrorCode(new Result());
		}
		return result;
	}

	private Result pushMetricData(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String meteredDate = formatter.format(date);
		String requestId = UUID.randomUUID().toString();

		String authToken = CommonUtilities.getAuthToken(request);

		Map<String, String> headerMap = new HashMap<>();
		Map<String, String> postParametersMap = new HashMap<>();

		HashMap<String, String> countMap = new HashMap<>();
		countMap.put("Retail-EndUsers", request.getParameter(INPUT_RETAIL_END_USERS));
		countMap.put("Retail-ClientUsers", request.getParameter(INPUT_RETAIL_CLIENT_USERS));
		countMap.put("Wealth-EndUsers", request.getParameter(INPUT_WEALTH_END_USERS));
		countMap.put("Wealth-ClientUsers", request.getParameter(INPUT_WEALTH_CLIENT_USERS));
		countMap.put("Business-EndUsers", request.getParameter(INPUT_BUSINESS_END_USERS));
		countMap.put("Business-ClientUsers", request.getParameter(INPUT_BUSINESS_CLIENT_USERS));

		for (Entry<String, String> entry : countMap.entrySet()) {

			postParametersMap.put("appId", INPUT_INFINITY);
			postParametersMap.put("requestId", requestId);
			postParametersMap.put("resourceId", INPUT_LICENSING_UNIT);
			postParametersMap.put("metricId", entry.getKey());
			postParametersMap.put("meteredDate", meteredDate);
			postParametersMap.put("usedCount", entry.getValue());
			postParametersMap.put("isIncremental", BOOLEAN_FALSE);

			String serviceResponse = null;

			headerMap.put("X-Kony-Authorization", authToken);

			serviceResponse = Executor.invokeService(ServiceURLEnum.METERING_MS_JSON_PUSH_METRICS, postParametersMap, headerMap,
					request);

			JSONObject json = CommonUtilities.getStringAsJSONObject(serviceResponse);
			if (json != null && json.has(FabricConstants.OPSTATUS) && json.getInt(FabricConstants.OPSTATUS) == 0
					&& json.has(FabricConstants.HTTP_STATUS_CODE)
					&& json.getInt(FabricConstants.HTTP_STATUS_CODE) == 200) {

				result.appendResult(CommonUtilities.constructResultFromJSONObject(json));
				result.addParam("isJobSuccess", "true");
			} else {

				result.addParam("isJobSuccess", "false");
				ErrorCodeEnum.ERR_22202.setErrorCode(result);

			}

		}

		return result;
	}

}
