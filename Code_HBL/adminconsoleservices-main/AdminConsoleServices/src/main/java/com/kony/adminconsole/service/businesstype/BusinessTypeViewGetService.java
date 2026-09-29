package com.kony.adminconsole.service.businesstype;

import java.util.HashMap;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.BusinessTypeHandler;
import com.kony.adminconsole.handler.PaginationHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class BusinessTypeViewGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			Result processedResult = new Result();
			Map<String, String> postParametersMap = new HashMap<String, String>();

			PaginationHandler.setOffset(requestInstance, postParametersMap);
			JSONObject readBusinessTypesServicesViewResponseJSON = null;
			JSONArray readBusinessTypesServicesJSONArray = null;
			if (requestInstance.getParameter("id") != null) {			
				String businessType_id = requestInstance.getParameter("id");
				postParametersMap.put(ODataQueryConstants.FILTER, "BusinessType_id eq '" + businessType_id + "'");			
			}
			readBusinessTypesServicesViewResponseJSON = PaginationHandler
					.getPaginatedData(ServiceURLEnum.BUSINESSTYPES_VIEW_READ, postParametersMap, null, requestInstance);
			if (readBusinessTypesServicesViewResponseJSON != null
					&& readBusinessTypesServicesViewResponseJSON.has(FabricConstants.OPSTATUS)
					&& readBusinessTypesServicesViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readBusinessTypesServicesViewResponseJSON.has("businesstypes_view")) {
				diagnostic.prepareDebug("Fetch Groups Status:Successful").log();
				readBusinessTypesServicesJSONArray = readBusinessTypesServicesViewResponseJSON
						.getJSONArray("businesstypes_view");
			}
			if (readBusinessTypesServicesJSONArray != null) {
				PaginationHandler.addPaginationMetadataToResultObject(processedResult,
						readBusinessTypesServicesViewResponseJSON);
				Dataset groupsDataSet = new Dataset();
				groupsDataSet.setId("BusinessTypeRecords");
				JSONObject currQuestionJSONObject;

				for (int indexVar = 0; indexVar < readBusinessTypesServicesJSONArray.length(); indexVar++) {
					currQuestionJSONObject = readBusinessTypesServicesJSONArray.getJSONObject(indexVar);
					Record currRecord = new Record();
					for (String currKey : currQuestionJSONObject.keySet()) {
						String key = currKey;
						if (key.equalsIgnoreCase("BusinessType_id")) {
							Dataset signatoryDataSet = BusinessTypeHandler.getAuthorizedSignatories(
									currQuestionJSONObject.getString(currKey), requestInstance);
							currRecord.addDataset(signatoryDataSet);
						}
						currRecord.addParam(
								new Param(currKey, currQuestionJSONObject.optString(currKey), FabricConstants.STRING));
					}
					groupsDataSet.addRecord(currRecord);
				}
				processedResult.addDataset(groupsDataSet);
				return processedResult;
			}
			alert.prepareError("Fetch Groups Status:Failed").log();
			ErrorCodeEnum.ERR_20404.setErrorCode(processedResult);
			return processedResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}
}