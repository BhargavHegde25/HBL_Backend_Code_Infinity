package com.kony.adminconsole.service.group;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.dto.GroupActionLimitView;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.FeatureandActionHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GroupFeaturesAndActionsGetForEdit implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result result = new Result();

		try {
			String groupId = requestInstance.getParameter("group_id");
			if (StringUtils.isBlank(groupId)) {
				ErrorCodeEnum.ERR_21858.setErrorCode(result);
				return result;
			}

			String groupTypeId = FeatureandActionHandler.getGroupType(groupId, requestInstance, result);
			if (StringUtils.isBlank(groupTypeId)) {
				ErrorCodeEnum.ERR_20789.setErrorCode(result);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			Map<String, Map<String, GroupActionLimitView>> features = new HashMap<>();
			JSONArray allActions = FeatureandActionHandler.getTypeFeatureActions(groupTypeId, requestInstance, result);
			for (int i = 0; i < allActions.length(); i++) {
				GroupActionLimitView action = FeatureandActionHandler
						.getGroupActionObjectFromAction(allActions.getJSONObject(i));
				action.setSelected(false);
				updateFeatures(action, features);
			}
			JSONArray groupActions = FeatureandActionHandler.getGroupFeatureActions(groupId, requestInstance, result);
			for (int i = 0; i < groupActions.length(); i++) {
				GroupActionLimitView action = FeatureandActionHandler
						.getGroupActionObject(groupActions.getJSONObject(i));
				action.setSelected(true);
				updateFeatures(action, features);
			}

			Dataset featuresDataset = new Dataset("features");

			for (Map.Entry<String, Map<String, GroupActionLimitView>> f : features.entrySet()) {
				Record feature = new Record();
				feature.addParam(new Param("id", f.getKey()));
				Dataset actions = new Dataset("actions");

				for (Map.Entry<String, GroupActionLimitView> a : f.getValue().entrySet()) {
					GroupActionLimitView groupActionLimitView = a.getValue();

					if (feature.getParam("name") == null) {
						feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
						feature.addParam(new Param("description", groupActionLimitView.getFeature_description()));
						feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));
						feature.addParam(new Param("type", groupActionLimitView.getFeature_Type_id()));
						feature.addParam(new Param("isSelected", "false"));
						feature.addParam(
								new Param("displaySequence", groupActionLimitView.getFeature_displaysequence()));
						feature.addParam(new Param("isPrimary",groupActionLimitView.getFeature_isPrimary()));
						
					}
					Record action = new Record();
					action.addParam(new Param("id", a.getKey()));
					action.addParam(new Param("name", groupActionLimitView.getAction_name()));
					action.addParam(new Param("description", groupActionLimitView.getAction_name()));
					action.addParam(new Param("type", groupActionLimitView.getAction_Type_id()));
					action.addParam(new Param("isMFAApplicable", groupActionLimitView.getIsMFAApplicable()));
					action.addParam(new Param("isAccountLevel", groupActionLimitView.getIsAccountLevel()));
					action.addParam(new Param("isPrimary", groupActionLimitView.getIsPrimary()));
					action.addParam(new Param("isSelected", String.valueOf(groupActionLimitView.isSelected())));
					action.addParam(new Param("displaySequence", groupActionLimitView.getAction_displaysequence()));
					if (StringUtils.isNotBlank(groupActionLimitView.getAction_dependency())) {
						action.addParam(new Param("dependency", groupActionLimitView.getAction_dependency()));
					}
					if (groupActionLimitView.isSelected()) {
						feature.addParam(new Param("isSelected", "true"));
					}

					Dataset limits = new Dataset("limits");
					for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
						Record limit = new Record();
						limit.addParam(new Param("id", l.getKey()));
						String value = l.getValue();
						if (StringUtils.isNotBlank(value)) {
						  value = String.format("%100.2f", Double.valueOf(value)).trim();
						  limit.addParam(new Param("value", value));
						}
						limits.addRecord(limit);
					}
					if (limits.getAllRecords().size() > 0) {
						action.addDataset(limits);
					}
					actions.addRecord(action);
				}
				feature.addDataset(actions);
				featuresDataset.addRecord(feature);
			}

			result.addDataset(featuresDataset);

		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. ApplicationException: ", e).log();
		} catch (Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. Error: ", e).log();
		}

		return result;
	}

	private void updateFeatures(GroupActionLimitView action, Map<String, Map<String, GroupActionLimitView>> features) {

		if (features.containsKey(action.getFeature_id())) {
			Map<String, GroupActionLimitView> actionsMap = features.get(action.getFeature_id());
			if (actionsMap.containsKey(action.getAction_id())) {
				
				GroupActionLimitView existingAction = actionsMap.get(action.getAction_id());
				existingAction.setSelected(action.isSelected());				
				if (StringUtils.isNotBlank(action.getLimitType_id()) && StringUtils.isNotBlank(action.getValue())) {
					existingAction.insertLimit(action.getLimitType_id(), action.getValue());
				}

			} else {
				if (StringUtils.isNotBlank(action.getLimitType_id()) && StringUtils.isNotBlank(action.getValue())) {
					action.insertLimit(action.getLimitType_id(), action.getValue());
				}
				actionsMap.put(action.getAction_id(), action);
			}
		} else {
			Map<String, GroupActionLimitView> actionsMap = new HashMap<>();
			if (StringUtils.isNotBlank(action.getLimitType_id()) && StringUtils.isNotBlank(action.getValue())) {
				action.insertLimit(action.getLimitType_id(), action.getValue());
			}
			actionsMap.put(action.getAction_id(), action);
			features.put(action.getFeature_id(), actionsMap);
		}
	}

}
