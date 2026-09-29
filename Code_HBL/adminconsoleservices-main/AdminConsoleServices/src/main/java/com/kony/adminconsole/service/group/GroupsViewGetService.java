package com.kony.adminconsole.service.group;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.handler.GroupHandler;
import com.kony.adminconsole.handler.PaginationHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GroupsViewGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			Result processedResult = new Result();
			Map<String, String> postParametersMap = new HashMap<String, String>();
			HashMap<String, String> businessIdNameMap = new HashMap<String, String>();
			businessIdNameMap = GroupHandler.createBusinessIdNameMap(requestInstance);
			PaginationHandler.setOffset(requestInstance, postParametersMap);
			JSONObject readGroupsServicesViewResponseJSON = null;
			JSONArray readGroupsServicesJSONArray = null;
			if (StringUtils.isNotBlank(requestInstance.getParameter("typeId"))) {
				postParametersMap.put(ODataQueryConstants.FILTER,
						"Type_id eq '" + requestInstance.getParameter("typeId") + "' and Status_id eq 'SID_ACTIVE'");
				readGroupsServicesViewResponseJSON = PaginationHandler.getPaginatedData(ServiceURLEnum.MEMBERGROUP_READ,
						postParametersMap, null, requestInstance);

			} else {
				readGroupsServicesViewResponseJSON = PaginationHandler.getPaginatedData(ServiceURLEnum.GROUPS_VIEW_READ,
						postParametersMap, null, requestInstance);
			}
			if (readGroupsServicesViewResponseJSON != null
					&& readGroupsServicesViewResponseJSON.has(FabricConstants.OPSTATUS)
					&& readGroupsServicesViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readGroupsServicesViewResponseJSON.has("groups_view")) {
				diagnostic.prepareDebug("Fetch Groups Status:Successful").log();
				readGroupsServicesJSONArray = readGroupsServicesViewResponseJSON.getJSONArray("groups_view");

			} else if (readGroupsServicesViewResponseJSON != null
					&& readGroupsServicesViewResponseJSON.has(FabricConstants.OPSTATUS)
					&& readGroupsServicesViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readGroupsServicesViewResponseJSON.has("membergroup")) {
				diagnostic.prepareDebug("Fetch Groups Status:Successful").log();
				readGroupsServicesJSONArray = readGroupsServicesViewResponseJSON.getJSONArray("membergroup");
			}
			if (readGroupsServicesJSONArray != null) {
				PaginationHandler.addPaginationMetadataToResultObject(processedResult,
						readGroupsServicesViewResponseJSON);
				Dataset groupsDataSet = new Dataset();
				groupsDataSet.setId("GroupRecords");
				JSONObject currQuestionJSONObject;

				for (int indexVar = 0; indexVar < readGroupsServicesJSONArray.length(); indexVar++) {
					currQuestionJSONObject = readGroupsServicesJSONArray.getJSONObject(indexVar);
					Record currRecord = new Record();
					String groupId = currQuestionJSONObject.optString("Group_id");
					if (StringUtils.isBlank(groupId)) {
						groupId = currQuestionJSONObject.optString("id");
					}
					Dataset businessTypeDataset = GroupHandler.createBusinessTypeDataset(groupId, businessIdNameMap,
							requestInstance, processedResult);
					if (processedResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
						return processedResult;
					}
					currRecord.addDataset(businessTypeDataset);

					for (String currKey : currQuestionJSONObject.keySet()) {
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