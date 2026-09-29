package com.temenos.dbx.product.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.dto.FeatureActionDTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.OrganizationGroupActionLimitBusinessDelegate;
import com.temenos.dbx.product.dto.GroupActionsDTO;
import com.temenos.dbx.product.dto.OrganizationActionsDTO;
import com.temenos.dbx.product.resource.api.OrganizationGroupActionLimitsResource;

/**
 * 
 * @author KH2627
 * @version 1.0 Extends the {@link OrganizationGroupActionLimitsResource}
 */
public class OrganizationGroupActionLimitsResourceImpl implements OrganizationGroupActionLimitsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getOrganizationGroupActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) {

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Result result = new Result();

        if (preProcess(dcRequest, inputParams, result)) {

            processOrganizationGroupActionLimits(inputParams.get("organizationId"),
                    inputParams.get("groupId"), inputParams.get("actionId"), result, dcRequest);

        }
        return result;
    }

    private void processOrganizationGroupActionLimits(String organizationId,
            String groupId, String inputActionId, Result result,
            DataControllerRequest dcRequest) {

        Map<String, Map<String, String>> groupActionLimits =
                fetchGroupActionLimits(groupId, inputActionId, result, dcRequest);

        if (HelperMethods.hasDBPErrorMSG(result))
            return;

        Map<String, FeatureActionDTO> featureInformation = new HashMap<>();
        Map<String, List<FeatureActionDTO>> actionTypeFeatureActionLimits =
                fetchOrganizationActionLimits(featureInformation, groupActionLimits, organizationId, inputActionId,
                        result, dcRequest);

        if (HelperMethods.hasDBPErrorMSG(result))
            return;

        processEndResult(result, actionTypeFeatureActionLimits, featureInformation);

        if (!result.getIdOfAllDatasets().contains("FeatureActions")) {
            ErrorCodeEnum.ERR_10716.setErrorCode(result);
        }
    }

    private Map<String, Map<String, String>> fetchGroupActionLimits(
            String groupId, String inputActionId, Result result, DataControllerRequest dcRequest) {

        if (groupId == null || StringUtils.isBlank(groupId))
            return null;

        OrganizationGroupActionLimitBusinessDelegate organizationGroupActionLimitBusinessDelegate = null;
        List<GroupActionsDTO> groupActions = new ArrayList<>();

        try {
            organizationGroupActionLimitBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(OrganizationGroupActionLimitBusinessDelegate.class);

            GroupActionsDTO inputDTO = new GroupActionsDTO();

            inputDTO.setGroupId(groupId);
            inputDTO.setActionId(inputActionId);
            inputDTO.setActionType("");
            groupActions = organizationGroupActionLimitBusinessDelegate.getGroupActionLimits(inputDTO,
                    getHeaders(dcRequest));

        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching groupActionLimits :" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10708.setErrorCode(result);
            return null;
        }

        Map<String, Map<String, String>> groupActionLimits = null;

        if (groupActions == null || groupActions.isEmpty()) {
            ErrorCodeEnum.ERR_10714.setErrorCode(result);
        } else {
            groupActionLimits = new HashMap<>();
            groupActionLimits = processGroupActionLimits(groupActionLimits, groupActions, result);
            if (groupActionLimits == null)
                ErrorCodeEnum.ERR_10708.setErrorCode(result);
        }
        return groupActionLimits;
    }

    private Map<String, Map<String, String>> processGroupActionLimits(
            Map<String, Map<String, String>> groupActionLimits, List<GroupActionsDTO> groupActions, Result result) {
        try {
            for (int i = 0; i < groupActions.size(); i++) {
                String actionId = groupActions.get(i).getActionId();
                Map<String, String> currGroupActionLimits = new HashMap<>();
                if (groupActions.get(i).getLimitTyeId() != null) {
                    String limitTypeId = groupActions.get(i).getLimitTyeId();
                    String value = groupActions.get(i).getValue();
                    if (groupActionLimits.containsKey(actionId))
                        currGroupActionLimits = groupActionLimits.get(actionId);
                    currGroupActionLimits.put(limitTypeId, value);
                }
                groupActionLimits.put(actionId, currGroupActionLimits);
            }
        } catch (Exception e) {
            alert.prepareError("Error while parsing the groupActionlimits response :" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10708.setErrorCode(result);
            return null;
        }
        return groupActionLimits;
    }

    public static Map<String, Object> getHeaders(DataControllerRequest dcRequest) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
                dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        return headerMap;
    }

    private Map<String, List<FeatureActionDTO>> fetchOrganizationActionLimits(
            Map<String, FeatureActionDTO> featureInformation, Map<String, Map<String, String>> groupActionLimits,
            String organizationId, String inputActionId,
            Result result,
            DataControllerRequest dcRequest) {

        OrganizationGroupActionLimitBusinessDelegate organizationGroupActionLimitBusinessDelegate = null;
        List<OrganizationActionsDTO> organizationActions = null;
        try {

            organizationGroupActionLimitBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(OrganizationGroupActionLimitBusinessDelegate.class);

            OrganizationActionsDTO inputDTO = new OrganizationActionsDTO();
            inputDTO.setActionId(inputActionId);
            inputDTO.setActionType("");
            inputDTO.setOrganizationId(organizationId);
            organizationActions = organizationGroupActionLimitBusinessDelegate.getOrganizationActionLimits(inputDTO,
                    getHeaders(dcRequest),
                    URLConstants.ORGANIZATION_ACTIONS_PROC);

        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching organizationGroupActionLimits :" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10709.setErrorCode(result);
            return null;
        }

        if (organizationActions == null || organizationActions.isEmpty()) {
            ErrorCodeEnum.ERR_10715.setErrorCode(result);
            return null;
        }

        Map<String, List<FeatureActionDTO>> actionIdFeatureActionLimits = new HashMap<>();
        Set<String> actionIdList = new HashSet<>();

        processOrganizationActions(organizationActions, actionIdFeatureActionLimits, actionIdList, featureInformation,
                groupActionLimits, result);

        return actionIdFeatureActionLimits;
    }

    private void processOrganizationActions(List<OrganizationActionsDTO> organizationActions,
            Map<String, List<FeatureActionDTO>> actionIdFeatureActionLimits, Set<String> actionIdList,
            Map<String, FeatureActionDTO> featureInformation, Map<String, Map<String, String>> groupActionLimits,
            Result result) {

        boolean isGroupActionLimitsCheckRequired = false;
        if (groupActionLimits != null && !groupActionLimits.isEmpty()) {
            isGroupActionLimitsCheckRequired = true;
        }
        try {
            for (int i = 0; i < organizationActions.size(); i++) {

                String actionType = organizationActions.get(i).getActionType();
                String featureId = organizationActions.get(i).getFeatureId();
                String actionId = organizationActions.get(i).getActionId();
                String actionName = organizationActions.get(i).getActionName();
                String actionDescription = organizationActions.get(i).getActionDescription();
                String isAccountLevel = organizationActions.get(i).getIsAccountLevel();
                String limitTypeId = null;
                String value = null;

                if (isGroupActionLimitsCheckRequired && !groupActionLimits.containsKey(actionId)) {
                    continue;
                }
                if (organizationActions.get(i).getLimitTypeId() != null) {
                    limitTypeId = organizationActions.get(i).getLimitTypeId();
                    String orgValue = organizationActions.get(i).getOrgLimitValue();
                    String fiValue = organizationActions.get(i).getFiLimitValue();
                    Double minOfOrgAndFI = Math.min(Double.parseDouble(orgValue), Double.parseDouble(fiValue));
                    value = String.valueOf(minOfOrgAndFI);
                    if (isGroupActionLimitsCheckRequired) {
                        Map<String, String> groupLimits = groupActionLimits.get(actionId);
                        if (groupLimits.containsKey(limitTypeId)) {
                            value = String
                                    .valueOf(Math.min(minOfOrgAndFI, Double.parseDouble(groupLimits.get(limitTypeId))));
                        } else {
                            limitTypeId = null;
                            value = null;
                        }
                    }
                }
                List<FeatureActionDTO> currFeatureActionLimitsList = new ArrayList<>();
                if (actionIdFeatureActionLimits.containsKey(featureId))
                    currFeatureActionLimitsList = actionIdFeatureActionLimits.get(featureId);

                if (!actionIdList.contains(actionId)) {
                    FeatureActionDTO featureActionDTO = new FeatureActionDTO();
                    featureActionDTO.setActionId(actionId);
                    featureActionDTO.setActionName(actionName);
                    featureActionDTO.setActionDescription(actionDescription);
                    featureActionDTO.setActiontype(actionType);
                    featureActionDTO.setIsAccountLevel(isAccountLevel);
                    if (StringUtils.isNotBlank(limitTypeId))
                        featureActionDTO.setActionLimit(limitTypeId, value);
                    currFeatureActionLimitsList.add(featureActionDTO);
                } else {
                    if (StringUtils.isNotBlank(limitTypeId)) {
                        for (int j = 0; j < currFeatureActionLimitsList.size(); j++) {
                            if (actionId.equalsIgnoreCase(currFeatureActionLimitsList.get(j).getActionId())) {
                                currFeatureActionLimitsList.get(j).setActionLimit(limitTypeId, value);
                            }
                        }
                    }
                }
                actionIdList.add(actionId);
                actionIdFeatureActionLimits.put(featureId, currFeatureActionLimitsList);
                featureDetailsInformation(featureInformation, organizationActions.get(i));
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing the organizationActions :" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10709.setErrorCode(result);
        }
    }

    private void featureDetailsInformation(Map<String, FeatureActionDTO> featureInformation,
            OrganizationActionsDTO organizationActionsDTO) {

        String featureId = organizationActionsDTO.getFeatureId();
        if (featureInformation.containsKey(featureId))
            return;
        FeatureActionDTO featureActionInformation = new FeatureActionDTO();
        featureActionInformation.setFeatureId(featureId);
        featureActionInformation.setFeatureName(organizationActionsDTO.getFeatureName());
        featureActionInformation.setFeatureDescription(organizationActionsDTO.getFeatureDescription());
        if (StringUtils.isNotBlank(organizationActionsDTO.getOrgFeatureStatus()))
            featureActionInformation.setFeatureStatus(organizationActionsDTO.getOrgFeatureStatus());
        else
            featureActionInformation.setFeatureStatus(organizationActionsDTO.getFiFeatureStatus());
        featureInformation.put(featureId, featureActionInformation);
    }

    private void processEndResult(Result result,
            Map<String, List<FeatureActionDTO>> featureActionLimits,
            Map<String, FeatureActionDTO> featureInformation) {
        try {
            Dataset featureActions = new Dataset();
            featureActions.setId("FeatureActions");
            for (Entry<String, List<FeatureActionDTO>> featureEntry : featureActionLimits.entrySet()) {

                Record featureRecord = new Record();
                featureRecord.addStringParam("featureName",
                        featureInformation.get(featureEntry.getKey()).getFeatureName());
                featureRecord.addStringParam("featureDescription",
                        featureInformation.get(featureEntry.getKey()).getFeatureDescritpion());
                featureRecord.addStringParam("featureId",
                        featureInformation.get(featureEntry.getKey()).getFeatureId());
                featureRecord.addStringParam("featureStatus",
                        featureInformation.get(featureEntry.getKey()).getFeatureStatus());
                Dataset actionsDataset = new Dataset();
                actionsDataset.setId("Actions");

                List<Record> actionRecords = new ArrayList<>();
                for (FeatureActionDTO childFeatureActionDTO : featureEntry.getValue()) {
                    Record record = new Record();
                    record.addParam("actionId", childFeatureActionDTO.getActionId());
                    record.addParam("actionName", childFeatureActionDTO.getActionName());
                    record.addParam("actionDescription", childFeatureActionDTO.getActionDescription());
                    record.addParam("actionType", childFeatureActionDTO.getActionType());
                    record.addParam("isAccountLevel", childFeatureActionDTO.getIsAccountLevel());

                    if (!childFeatureActionDTO.getActionLimit().isEmpty()) {
                        Dataset limits = new Dataset();
                        limits.setId("Limits");
                        List<Record> limitRecords = new ArrayList<>();
                        for (Entry<String, String> childActionLimits : childFeatureActionDTO.getActionLimit()
                                .entrySet()) {
                            Record limitRecord = new Record();
                            limitRecord.addStringParam("id", childActionLimits.getKey());
                            limitRecord.addStringParam("value", childActionLimits.getValue());
                            limitRecords.add(limitRecord);
                        }
                        limits.addAllRecords(limitRecords);
                        record.addDataset(limits);
                    }
                    actionRecords.add(record);
                }
                actionsDataset.addAllRecords(actionRecords);
                featureRecord.addDataset(actionsDataset);
                featureActions.addRecord(featureRecord);
            }
            result.addDataset(featureActions);
        } catch (Exception e) {
            alert.prepareError("Failed to parse the response of organization action limits :" + e.getMessage()).log();
            ErrorCodeEnum.ERR_10716.setErrorCode(result);
        }
    }

    private boolean preProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result) {

        String organizationId = null;
        Map<String, String> userDetails = HelperMethods.getUserFromIdentityService(dcRequest);

        String isC360Admin = userDetails.get("isC360Admin");

        if (StringUtils.isNotBlank(isC360Admin) && "true".equalsIgnoreCase(isC360Admin)) {
            organizationId = inputParams.get("organizationId");
        } else {
            organizationId = userDetails.get("Organization_Id");
        }
        if (StringUtils.isBlank(organizationId)) {
            ErrorCodeEnum.ERR_10707.setErrorCode(result);
            return false;
        }
        inputParams.put("organizationId", organizationId);

        return true;
    }

}
