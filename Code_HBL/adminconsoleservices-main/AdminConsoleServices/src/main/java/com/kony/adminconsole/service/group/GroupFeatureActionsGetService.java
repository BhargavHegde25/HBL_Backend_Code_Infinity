package com.kony.adminconsole.service.group;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

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

public class GroupFeatureActionsGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        try {

            String TypeId = requestInstance.getParameter("Type_id");
            if (StringUtils.isBlank(TypeId)) {
                ErrorCodeEnum.ERR_21858.setErrorCode(processedResult);
                return processedResult;
            }
            JSONArray groupActions = FeatureandActionHandler.getGroupFeatureActionsByType(TypeId, requestInstance,
                    processedResult);
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

                    /*
                     * if (group.getParam("id") == null) { group.addParam(new Param("name",
                     * groupActionLimitView.getFeature_name())); group.addParam(new Param("status",
                     * groupActionLimitView.getFeature_Status_id())); }
                     */
                    Record feature = new Record();
                    feature.addParam(new Param("name", groupActionLimitView.getFeature_name()));
                    feature.addParam(new Param("status", groupActionLimitView.getFeature_Status_id()));

                    Dataset actions = new Dataset("actions");
                    for (Map.Entry<String, String> l : a.getValue().getFeatureactions().entrySet()) {
                        Record action = new Record();
						String value = l.getValue();
					//	action.addParam(new Param("name", value.substring(0, value.indexOf('|')-1)));
						action.addParam(new Param("id", l.getKey()));
						action.addParam(new Param("name", value.substring(0, value.indexOf('|'))));
						action.addParam(new Param("description", value.substring(value.indexOf('|')+1)));
						
                        //action.addParam(new Param("name", groupActionLimitView.getAction_name()));
                        //action.addParam(new Param("description", groupActionLimitView.getAction_description()));
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
            processedResult.addDataset(groupsDataset);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(processedResult);
            alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. ApplicationException: ", e).log();
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
            alert.prepareError("Exception occured in GroupFeaturesAndActionsGet JAVA service. Error: ", e).log();
        }

        return processedResult;

    }

}