package com.kony.adminconsole.service.businesstype;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.dto.GroupActionLimitView;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.FeatureandActionHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class BusinessTypeGroupsGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result processedResult = new Result();
		try {
			requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			Map<String, String> postParametersMap = new HashMap<String, String>();
			if (requestInstance.getParameter("id") == null) {
				Param result_param = new Param("Invalid", "id cannot be null", FabricConstants.STRING);
				processedResult.addParam(result_param);
				return processedResult;
			}
			String businessType_id = requestInstance.getParameter("id");
			postParametersMap.put(ODataQueryConstants.FILTER, "BusinessType_id eq '" + businessType_id + "'");
			postParametersMap.put(ODataQueryConstants.SELECT, "Group_id");
			String readBusinessTypeGroupsResponse = Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPE_READ,
					postParametersMap, null, requestInstance);
			JSONObject readBusinessTypeGroupsResponseJSON = CommonUtilities
					.getStringAsJSONObject(readBusinessTypeGroupsResponse);
			if (readBusinessTypeGroupsResponseJSON == null
					|| !readBusinessTypeGroupsResponseJSON.has(FabricConstants.OPSTATUS)
					|| readBusinessTypeGroupsResponseJSON.getInt(FabricConstants.OPSTATUS) != 0
					|| !readBusinessTypeGroupsResponseJSON.has("groupbusinesstype")) {
				processedResult.addParam(new Param("FailureReason", String.valueOf(readBusinessTypeGroupsResponseJSON),
						FabricConstants.STRING));
				throw new ApplicationException(ErrorCodeEnum.ERR_21851);
			}
			JSONArray readBusinessTypeGroupIdsJSONArray = readBusinessTypeGroupsResponseJSON
					.optJSONArray("groupbusinesstype");
			if(readBusinessTypeGroupIdsJSONArray.length() == 0)
			{				
				Dataset groupsDataset = new Dataset("groups");
                processedResult.addDataset(groupsDataset);
                return processedResult;
			}
			StringBuffer filterQueryBuffer = new StringBuffer();
			String currGroupId;
			JSONObject currJSONObject;
			filterQueryBuffer.append("(");
			for (int index = 0; index < readBusinessTypeGroupIdsJSONArray.length(); index++) {

				if (readBusinessTypeGroupIdsJSONArray.get(index) instanceof JSONObject) {
					currJSONObject = readBusinessTypeGroupIdsJSONArray.getJSONObject(index);
					currGroupId = currJSONObject.optString("Group_id");
					if (StringUtils.isNotBlank(currGroupId)) {

						filterQueryBuffer.append("Group_id eq '" + currGroupId + "'");
					}
					if (index < readBusinessTypeGroupIdsJSONArray.length() - 1) {
						filterQueryBuffer.append(" or ");
					}
				}

			}
			filterQueryBuffer.append(")");
			postParametersMap.clear();
			if (StringUtils.isNotBlank(filterQueryBuffer)) {
				postParametersMap.put(ODataQueryConstants.FILTER, filterQueryBuffer.toString());
			}
			JSONObject readResponse = CommonUtilities.getStringAsJSONObject(Executor.invokeService(
					ServiceURLEnum.GROUP_FEATURES_ACTIONS_VIEW_READ, postParametersMap, null, requestInstance));
			if (readResponse == null || !readResponse.has(FabricConstants.OPSTATUS)
					|| readResponse.getInt(FabricConstants.OPSTATUS) != 0
					|| !readResponse.has("group_features_actions_view")) {
				processedResult
						.addParam(new Param("FailureReason", String.valueOf(readResponse), FabricConstants.STRING));
				throw new ApplicationException(ErrorCodeEnum.ERR_21857);
			}

			JSONArray groupActions = readResponse.getJSONArray("group_features_actions_view");
			Dataset groupsDataset = groupFeaturesAndActions(groupActions, requestInstance, responseInstance);
			processedResult.addDataset(groupsDataset);
			return processedResult;

		}catch (ApplicationException ae) {
            alert.prepareError("Unexpected error", ae).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
        } catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
			alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. Error: ", e).log();
		}
		return processedResult;

	}

	public Dataset groupFeaturesAndActions(JSONArray groupActions, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {

		try {
			Map<String, Map<String, GroupActionLimitView>> groups = new HashMap<>();
			groupActions.forEach((actionObject) -> {
				GroupActionLimitView feature = FeatureandActionHandler.getGroupActionObject((JSONObject) actionObject);

				if (groups.containsKey(feature.getGroup_id())) {
					Map<String, GroupActionLimitView> featuresMap = groups.get(feature.getGroup_id());
					if (featuresMap.containsKey(feature.getFeature_id())) {
						GroupActionLimitView existingFeature = featuresMap.get(feature.getFeature_id());
						if (StringUtils.isNotBlank(feature.getAction_id())) {
							existingFeature.insertAction(feature.getAction_id(), feature.getAction_name()+"|"+feature.getAction_description());
						}
					} else {
						if (StringUtils.isNotBlank(feature.getAction_id())) {
							//feature.insertAction(feature.getAction_id(), feature.getAction_id());
							feature.insertAction(feature.getAction_id(),feature.getAction_name()+"|"+feature.getAction_description());
						}
						featuresMap.put(feature.getFeature_id(), feature);
					}
				} else {
					Map<String, GroupActionLimitView> featuresMap = new HashMap<>();
					if (StringUtils.isNotBlank(feature.getAction_id())) {
						//feature.insertAction(feature.getAction_id(), feature.getAction_id());
						feature.insertAction(feature.getAction_id(), feature.getAction_name()+"|"+feature.getAction_description());
					}
					featuresMap.put(feature.getFeature_id(), feature);
					groups.put(feature.getGroup_id(), featuresMap);
				}
			});
			Dataset groupsDataset = new Dataset("groups");

			for (Map.Entry<String, Map<String, GroupActionLimitView>> f : groups.entrySet()) {
				Record group = new Record();
				group.addParam(new Param("id", f.getKey()));
				Map<String, GroupActionLimitView> ftr = f.getValue();
				group.addParam(new Param("name", ftr.get(ftr.keySet().toArray()[0]).getGroup_name()));
				group.addParam(new Param("description", ftr.get(ftr.keySet().toArray()[0]).getGroup_description()));
                group.addParam(new Param("type", ftr.get(ftr.keySet().toArray()[0]).getGroup_type()));
				Dataset features = new Dataset("features");

				for (Map.Entry<String, GroupActionLimitView> a : f.getValue().entrySet()) {
					GroupActionLimitView groupActionLimitView = a.getValue();
					Record feature = new Record();
					feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
					feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));
					Dataset actions = new Dataset("actions");
					for (Map.Entry<String, String> l : a.getValue().getFeatureactions().entrySet()) {
						Record action = new Record();
						String value = l.getValue();
						action.addParam(new Param("id", l.getKey()));
						action.addParam(new Param("name", value.substring(0, value.indexOf('|'))));
						action.addParam(new Param("description", value.substring(value.indexOf('|')+1)));
						actions.addRecord(action);
					}
					if (actions.getAllRecords().size() > 0) {
						feature.addDataset(actions);
					}
					features.addRecord(feature);
				}
				group.addDataset(features);

				groupsDataset.addRecord(group);
			}
			return groupsDataset;
		} catch (Exception e) {
			alert.prepareError("Exception occured in groupFeaturesAndActions JAVA service. Error: ", e).log();
		}
		return null;
	}

}