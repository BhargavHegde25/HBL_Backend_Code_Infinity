package com.kony.adminconsole.service.servicedefinition.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.customerrole.businessdelegate.api.CustomerRoleBusinessDelegate;
import com.kony.adminconsole.service.customerrole.dto.GroupsViewDTO;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.servicedefinition.dto.LimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.MemberGroupDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionFeatureActionViewDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionGroupDTO;
import com.kony.adminconsole.service.servicedefinition.resource.api.ServiceDefinitionResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.EnvironmentParamRead;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.PermissionName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ServiceDefinitionResourceImpl implements ServiceDefinitionResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    ServiceDefinitionBusinessDelegate serviceDefinitionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class)
            .getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);

    CustomerRoleBusinessDelegate customerRoleBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerRoleBusinessDelegate.class);
    
    @Override
    public Result deleteServiceDefinition(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        @SuppressWarnings("unchecked")
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        Result result = new Result();

        String serviceDefinitionId = null;

        if (inputParams.get("id") != null) {
            serviceDefinitionId = inputParams.get("id").toString();
        }

        if (serviceDefinitionId == null || serviceDefinitionId.isEmpty()) {
            alert.prepareError("Missing servicedefinitionId").log();
            return ErrorCodeEnum.ERR_21902.setErrorCode(new Result());
        }

        ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
        try {
            serviceDefinitionDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ServiceDefinitionDTO.class);
        } catch (IOException e) {
            alert.prepareError("Error occured while fetching the input params: " + e).log();
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }

        boolean isAssociated = serviceDefinitionBusinessDelegate.isAssociatedToContract(serviceDefinitionId);

        if (isAssociated) {
            if (!serviceDefinitionBusinessDelegate.deleteServiceDefinition(serviceDefinitionDTO)) {
                alert.prepareError("Failed to delete service definition: ").log();
                return ErrorCodeEnum.ERR_21909.setErrorCode(new Result());
            }
        }

        result = JSONToResult.convert(new JSONObject(serviceDefinitionDTO).toString());
        return result;
    }

    @Override
    public Result fetchAllServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        String isGetAllServDefs = request.getParameter("isGetAllServiceDefinitions");
        String companyLegalUnits = "";
        if((StringUtils.isBlank(isGetAllServDefs) || !isGetAllServDefs.equals("true"))) {
    	List<String> legalEntities = new ArrayList<String>();
    	try {
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
			JSONObject entry = new JSONObject(userDetailsBeanInstance.getRoleToLEMapping());
			legalEntities=new ArrayList<String>();
			Iterator<String> keys = entry.keys();
			while(keys.hasNext()) {
			     JSONArray value = (JSONArray) entry.get(keys.next());
			     for (int i = 0; i < value.length(); i++) {
			    	 legalEntities.add((String) value.get(i));
			        }
			}
		} catch (ApplicationException e) {
			alert.prepareError("Error occurred while reading legalEntities from user response ").log();
		}
    	String companyLegalUnit = request.getParameter("legalEntityId");
    	if(companyLegalUnit == null || companyLegalUnit=="" ) {
    		companyLegalUnit= legalEntities.get(0);// as of now we are treating it as single entity need to extend this logic to get all the entities
    	}
        String[] companyLegalUnitsList = companyLegalUnit.trim().split(",");
        if (companyLegalUnitsList.length != 0) {
            for (int index = 0; index < companyLegalUnitsList.length; index++) {
                String companyLegalUnitId = companyLegalUnitsList[index];
                if (!StringUtils.isBlank(companyLegalUnitId)) {
                    companyLegalUnits = companyLegalUnits +  companyLegalUnitId.trim();
                    if (index != companyLegalUnitsList.length - 1) {
                        companyLegalUnits = companyLegalUnits + "|";
                    }
                }
            }
        }
        }
        Result result = new Result();
        String typeId= "";
        
        if(!StringUtils.isBlank(request.getParameter("typeId"))) {
			 typeId = request.getParameter("typeId");
		}
        List<ServiceDefinitionDTO> serviceDefinitionDTOs =
        		serviceDefinitionBusinessDelegate.fetchAllServiceDefinition(typeId, companyLegalUnits);
        if (serviceDefinitionDTOs == null) {
            alert.prepareError("Error occurred while fetching service definition").log();
            return ErrorCodeEnum.ERR_21903.setErrorCode(new Result());
        }

        if (serviceDefinitionDTOs.size() == 0) {
            return JSONToResult.convert(new JSONObject().put("ServiceDefinitionRecords", new JSONArray()).toString());
        }

        try {
            JSONArray records = new JSONArray(serviceDefinitionDTOs);
            for(int i=0;i<records.length();i++)
            {
            	records.getJSONObject(i).put("legalEntityId",records.getJSONObject(i).get("companyLegalUnit"));
            	records.getJSONObject(i).remove("companyLegalUnit");
            }
            JSONObject resultObject = new JSONObject();
            resultObject.put("ServiceDefinitionRecords", records);
            result = JSONToResult.convert(resultObject.toString());
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ", exp).log();
            return ErrorCodeEnum.ERR_21903.setErrorCode(new Result());
        }

        return result;
    }

    @Override
    public Result createServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();

        @SuppressWarnings("unchecked")
        Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
        String userId = StringUtils.EMPTY;
        UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
        if (userDetailsBeanInstance != null) {
            userId = userDetailsBeanInstance.getId();
        }
        String defaultGroup = inputParams.get("defaultRole");
        inputParams.put("createdby", userId);

        try {
            serviceDefinitionDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ServiceDefinitionDTO.class);
            String legalEntityId = request.getParameter("legalEntityId");
            serviceDefinitionDTO.setCompanyLegalUnit(legalEntityId);
            if(StringUtils.isBlank(legalEntityId)){
                return ErrorCodeEnum.ERR_22230.setErrorCode(new Result());      
            }
        } catch (IOException e) {
            alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }

        try {
            String serviceType = serviceDefinitionDTO.getServiceType();
            List<MemberGroupDTO> memberGroupList = new ArrayList<>();
            memberGroupList = serviceDefinitionBusinessDelegate.fetchAllGroups(serviceType);
            List<String> groups = memberGroupList.stream().map(MemberGroupDTO::getId).collect(Collectors.toList());

            serviceDefinitionDTO = ValidateRequest(serviceDefinitionDTO, request, groups);
            if (serviceDefinitionDTO.getErrCode() != null) {
                return serviceDefinitionDTO.getErrCode().setErrorCode(new Result());
            }

            String id = CommonUtilities.getNewId().toString();
            serviceDefinitionDTO.setId(id);

            serviceDefinitionDTO = serviceDefinitionBusinessDelegate.createServiceDefinition(serviceDefinitionDTO);
            if (serviceDefinitionDTO == null) {
                alert.prepareError("Failed to create a service definition view: ").log();
                return ErrorCodeEnum.ERR_21916.setErrorCode(new Result());
            }

            boolean actionLimitStatus = false;
            JSONArray addedActions = new JSONArray();
            try {
                addedActions = new JSONArray(request.getParameter("featureactions"));
            } catch (Exception e) {
                alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
                return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
            }

            actionLimitStatus = validateActions(serviceDefinitionDTO.getServiceType(), addedActions, serviceDefinitionDTO.getCompanyLegalUnit());
            if (actionLimitStatus == false) {
                alert.prepareError("Invalid actions or limits not in the given range").log();
                if (!serviceDefinitionBusinessDelegate.deleteServiceDefinition(serviceDefinitionDTO)) {
                    return ErrorCodeEnum.ERR_21909.setErrorCode(new Result());
                }
                return ErrorCodeEnum.ERR_21925.setErrorCode(new Result());
            }
            HashMap<String, ServiceDefinitionActionLimitDTO> existingMap = new HashMap<>();
            Map<JSONArray,Boolean> resultMap = new HashMap<>();
            resultMap = updateServiceDefinitionActions(addedActions, id, existingMap, userId,request);
            for(Map.Entry<JSONArray,Boolean> entry : resultMap.entrySet()) {
				actionLimitStatus  = entry.getValue();
				break;
			}
            if (actionLimitStatus == false) {
                alert.prepareError("Failed to create a service definition: ").log();
                if (!serviceDefinitionBusinessDelegate.deleteServiceDefinition(serviceDefinitionDTO)) {
                    return ErrorCodeEnum.ERR_21909.setErrorCode(new Result());
                }
                return ErrorCodeEnum.ERR_21923.setErrorCode(new Result());
            }

            result = createServiceDefinitionGroup(serviceDefinitionDTO, defaultGroup, memberGroupList);
            if (result.hasParamByName("dbpErrCode") || result.hasParamByName("dbpErrMsg")) {
                return result;
            }

        } catch (Exception e) {
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            return ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        result = JSONToResult.convert(new JSONObject(serviceDefinitionDTO).toString());
        return result;
    }

    /*
     * Method to convert action limits to hashmap for default limits
     * 
     * @param actionLimitDTOs - List of masterdata limits which needs to be converted
     * 
     * @return hashmap containing actionId as key and limittypeId as value
     */
    private HashMap<String, Double> convertActionLimits(List<ActionLimitDTO> actionLimitDTOs) {
        HashMap<String, Double> masterMap = new HashMap<>();
        try {
            for (ActionLimitDTO a : actionLimitDTOs) {
                double value = Double.parseDouble(a.getValue());
                masterMap.put(constructKey(a.getActionId(), a.getLimitTypeId()), value);
            }
        } catch (Exception e) {
            alert.prepareError("Exception while converting the list").log();
        }
        return masterMap;
    }

    /*
     * Method to validate the request payload for both create and edit
     * 
     * @param serviceDefinitionDTO - contains payload of service defintion create or edit service
     * 
     * @param request - To fetch the featureactions - as it is a JSON Array cannot be stored in DTO so sending
     * explicitly
     * 
     * @return ServiceDefinitionDTO - contains Error code in case of failure otherwise contains input service definition
     * payload only
     */
    private ServiceDefinitionDTO ValidateRequest(ServiceDefinitionDTO serviceDefinitionDTO,
            DataControllerRequest request, List<String> existinglist) {
    	String companyLegalUnit = request.getParameter("legalEntityId");
        try {
            JSONArray features = new JSONArray(request.getParameter("featureactions"));
            if (StringUtils.isBlank(serviceDefinitionDTO.getName())
                    || CommonUtilities.containAnySpecialCharacters(serviceDefinitionDTO.getName())) {
                alert.prepareError("Name is mandatory. Please use allowed characters only.").log();
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21917);
                return serviceDefinitionDTO;
            } else if (StringUtils.isBlank(serviceDefinitionDTO.getDescription())
                    || CommonUtilities.containAnySpecialCharactersForDescription(serviceDefinitionDTO.getDescription())) {
                alert.prepareError("Description is mandatory. Please use allowed characters only.").log();
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21919);
                return serviceDefinitionDTO;
            } else if (StringUtils.isBlank(serviceDefinitionDTO.getServiceType())
                    || (!serviceDefinitionDTO.getServiceType().equalsIgnoreCase("TYPE_ID_BUSINESS")
                            && !serviceDefinitionDTO.getServiceType().equalsIgnoreCase("TYPE_ID_RETAIL")
                            && !serviceDefinitionDTO.getServiceType().equalsIgnoreCase("TYPE_ID_WEALTH"))) {
                alert.prepareError("serviceType should be TYPE_ID_RETAIL or TYPE_ID_BUSINESS or TYPE_ID_WEALTH").log();
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21921);
                return serviceDefinitionDTO;
            } else if (StringUtils.isBlank(serviceDefinitionDTO.getStatus())
                    || (!serviceDefinitionDTO.getStatus().equalsIgnoreCase("SID_ACTIVE")
                            && !serviceDefinitionDTO.getStatus().equalsIgnoreCase("SID_INACTIVE"))) {
                alert.prepareError("status should be SID_ACTIVE or SID_INACTIVE").log();
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21915);
                return serviceDefinitionDTO;
            } else if (StringUtils.isBlank(request.getParameter("featureactions")) || features.length() == 0) {
                alert.prepareError("Feature actions cannot be empty").log();
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21922);
                return serviceDefinitionDTO;
            } else if (!existinglist.isEmpty() && StringUtils.isNotBlank(serviceDefinitionDTO.getDefaultGroup())
                    && !existinglist.contains(serviceDefinitionDTO.getDefaultGroup())) {
                alert.prepareError("Default Group Id found in request is not a valid customer group").log();
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_22028);
                return serviceDefinitionDTO;
            }
        } catch (Exception e) {
            serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21908);
            return serviceDefinitionDTO;
        }

        List<ServiceDefinitionDTO> serviceList = serviceDefinitionBusinessDelegate.fetchAllServiceDefinition("",companyLegalUnit);
        boolean serviceExists = false;
        // For Edit Scenario - Validations for servicetype and name modifications
        if (StringUtils.isNotBlank(serviceDefinitionDTO.getId())) {
            for (ServiceDefinitionDTO service : serviceList) {
                if (service.getId().equals(serviceDefinitionDTO.getId())) {
                    serviceExists = true;
                    if (!service.getServiceType().equals(serviceDefinitionDTO.getServiceType())) {
                        serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21926);
                        return serviceDefinitionDTO;
                    }
                    if (serviceDefinitionDTO.getStatus().equals("SID_INACTIVE")
                            && Integer.parseInt(service.getNumberOfContracts()) > 0) {
                        serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_22029);
                        return serviceDefinitionDTO;
                    }
                    if (service.getName().equals(serviceDefinitionDTO.getName())) {
                        return serviceDefinitionDTO;
                    }
                    break;
                }
            }
            if (!serviceExists) {
                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_22024);
                return serviceDefinitionDTO;
            }
        }

        if (!isValidName(serviceList, serviceDefinitionDTO.getName())) {
            serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21918);
            return serviceDefinitionDTO;
        }

        return serviceDefinitionDTO;
    }

    /*
     * Method to validate name which has to be unique
     * 
     * @param serviceList - All the Service defintions list
     * 
     * @param name - input name
     * 
     * @return boolean - true if name is valid otherwise false
     */
    private boolean isValidName(List<ServiceDefinitionDTO> serviceList, String name) {
        List<String> names = serviceList.stream().map(ServiceDefinitionDTO::getName).collect(Collectors.toList());
        if (names.contains(name)) {
            return false;
        }
        return true;
    }

    @Override
    public Result fetchAllServiceDefinitionActionLimits(String methodID, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) {
        Result result = new Result();

        try {
            String serviceDefinitionId = request.getParameter("serviceDefinitionId");
            if (StringUtils.isBlank(serviceDefinitionId)) {
                ErrorCodeEnum.ERR_21901.setErrorCode(result);
                return result;
            }
            List<ServiceDefinitionFeatureActionViewDTO> groupActions =
                    serviceDefinitionBusinessDelegate.getServiceDefinitionFeatureActions(serviceDefinitionId);
            if (groupActions == null) {
                ErrorCodeEnum.ERR_21904.setErrorCode(result);
                return result;
            }
            result = convertServiceDefinitionActionLimits(groupActions);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            alert.prepareError("Exception occured in fetchAllServiceDefinitionActionLimits JAVA service. Error: ", e).log();
        }

        return result;
    }

    /*
     * Method to convert JSON array response to Result containing features, actions and limits in hierarchial form
     * 
     * @param groupActions - actions response
     * 
     * @return Result - to be returned to user
     */
    private Result convertServiceDefinitionActionLimits(List<ServiceDefinitionFeatureActionViewDTO> groupActions) {
        Result result = new Result();

        Map<String, Integer> featuretoActionsCount = new HashMap<>();
        Map<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> features = new HashMap<>();
        try {
            groupActions.forEach((action) -> {
                if ("false".equalsIgnoreCase(action.getSoftdelete())) {
                    if (features.containsKey(action.getFeatureId())) {
                        Map<String, ServiceDefinitionFeatureActionViewDTO> actionsMap =
                                features.get(action.getFeatureId());
                        if (actionsMap.containsKey(action.getActionId())) {
                            ServiceDefinitionFeatureActionViewDTO existingAction = actionsMap.get(action.getActionId());
                            if (StringUtils.isNotBlank(action.getLimitTypeId())
                                    && StringUtils.isNotBlank(action.getValue())) {
                                existingAction.insertLimit(action.getLimitTypeId(), action.getValue());
                            }

                        } else {
                            if (StringUtils.isNotBlank(action.getLimitTypeId())
                                    && StringUtils.isNotBlank(action.getValue())) {
                                action.insertLimit(action.getLimitTypeId(), action.getValue());
                            }
                            actionsMap.put(action.getActionId(), action);
                            featuretoActionsCount.put(action.getFeatureId(),
                                    (featuretoActionsCount.get(action.getFeatureId())) == null ? 1
                                            : featuretoActionsCount.get(action.getFeatureId()) + 1);
                        }
                    } else {
                        Map<String, ServiceDefinitionFeatureActionViewDTO> actionsMap = new HashMap<>();
                        if (StringUtils.isNotBlank(action.getLimitTypeId())
                                && StringUtils.isNotBlank(action.getValue())) {
                            action.insertLimit(action.getLimitTypeId(), action.getValue());
                        }
                        actionsMap.put(action.getActionId(), action);
                        featuretoActionsCount.put(action.getFeatureId(),
                                (featuretoActionsCount.get(action.getFeatureId())) == null ? 1
                                        : featuretoActionsCount.get(action.getFeatureId()) + 1);
                        features.put(action.getFeatureId(), actionsMap);
                    }
                }
            });

            Dataset featuresDataset = new Dataset("features");

            for (Map.Entry<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> f : features.entrySet()) {
                Record feature = new Record();
                feature.addParam(new Param("id", f.getKey()));
                Dataset actions = new Dataset("actions");

                for (Map.Entry<String, ServiceDefinitionFeatureActionViewDTO> a : f.getValue().entrySet()) {
                    ServiceDefinitionFeatureActionViewDTO serviceDefinitionActionLimitView = a.getValue();

                    if (feature.getParam("name") == null) {
                        Integer totalActions =
                                featuretoActionsCount.get(serviceDefinitionActionLimitView.getFeatureId());
                        feature.addParam(new Param("name", serviceDefinitionActionLimitView.getFeatureName()));
                        feature.addParam(
                                new Param("description", serviceDefinitionActionLimitView.getFeatureDescription()));
                        feature.addParam(new Param("status", serviceDefinitionActionLimitView.getFeatureStatusId()));
                        feature.addParam(new Param("type", serviceDefinitionActionLimitView.getFeatureTypeId()));
                        feature.addParam(
                                new Param("displaySequence",
                                        serviceDefinitionActionLimitView.getFeatureDisplaysequence()));
                        feature.addParam(new Param("totalActions", totalActions.toString()));
                        feature.addParam(
                                new Param("isPrimary", serviceDefinitionActionLimitView.getFeatureIsPrimary()));

                    }
                    Record action = new Record();
                    action.addParam(new Param("id", a.getKey()));
                    action.addParam(new Param("name", serviceDefinitionActionLimitView.getActionName()));
                    action.addParam(new Param("description", serviceDefinitionActionLimitView.getActionName()));
                    action.addParam(new Param("type", serviceDefinitionActionLimitView.getActionTypeId()));
                    action.addParam(
                            new Param("isMFAApplicable", serviceDefinitionActionLimitView.getIsMFAApplicable()));
                    action.addParam(new Param("isAccountLevel", serviceDefinitionActionLimitView.getIsAccountLevel()));
                    action.addParam(new Param("isPrimary", serviceDefinitionActionLimitView.getIsPrimary()));
                    action.addParam(
                            new Param("displaySequence", serviceDefinitionActionLimitView.getActionDisplaysequence()));
                    action.addParam(new Param("accessPolicy", serviceDefinitionActionLimitView.getAccessPolicy()));
                    action.addParam(new Param("accessPolicyId", serviceDefinitionActionLimitView.getAccessPolicyId()));
                    action.addParam(new Param("actionlevel", serviceDefinitionActionLimitView.getActionlevel()));
                    action.addParam(new Param("actionlevelId", serviceDefinitionActionLimitView.getActionlevelId()));
                    if (StringUtils.isBlank(serviceDefinitionActionLimitView.getLimitGroup())) {
                        action.addParam(new Param("limitgroup", "N/A"));
                    } else {
                        action.addParam(new Param("limitgroup", serviceDefinitionActionLimitView.getLimitGroup()));
                    }
                    if (StringUtils.isBlank(serviceDefinitionActionLimitView.getLimitGroupId())) {
                        action.addParam(new Param("limitgroupId", "N/A"));
                    } else {
                        action.addParam(new Param("limitgroupId", serviceDefinitionActionLimitView.getLimitGroupId()));
                    }
                    action.addParam(new Param("actionStatus", serviceDefinitionActionLimitView.getActionStatus()));
                    if (StringUtils.isNotBlank(serviceDefinitionActionLimitView.getActionDependency())) {
                        action.addParam(
                                new Param("dependency", serviceDefinitionActionLimitView.getActionDependency()));
                    }
                    Dataset limits = new Dataset("limits");
                    for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
                        Record limit = new Record();
                        limit.addParam(new Param("id", l.getKey()));
                        limit.addParam(new Param("value", l.getValue()));
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
        } catch (Exception e) {
            ErrorCodeEnum.ERR_21912.setErrorCode(result);
            alert.prepareError("Exception occured in ServiceDefinitionActionLimitsGetService JAVA service. Error: ", e).log();
        }
        return result;
    }

    @Override
    public Result editServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();

        @SuppressWarnings("unchecked")
        Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
        String defaultGroup = inputParams.get("defaultRole");
        String id = inputParams.get("id");
        String legalEntityId = inputParams.get("legalEntityId");
        if (StringUtils.isBlank(id)) {
            alert.prepareError("Id cannot be null ").log();
            return ErrorCodeEnum.ERR_21902.setErrorCode(new Result());
        }

        String userId = StringUtils.EMPTY;
        UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
        if (userDetailsBeanInstance != null) {
            userId = userDetailsBeanInstance.getId();
        }
        inputParams.put("modifiedby", userId);

        try {
            serviceDefinitionDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ServiceDefinitionDTO.class);
            legalEntityId = request.getParameter("legalEntityId");
            serviceDefinitionDTO.setCompanyLegalUnit(legalEntityId);
            if(StringUtils.isBlank(legalEntityId)){
                return ErrorCodeEnum.ERR_22230.setErrorCode(new Result());      
            }
        } catch (IOException e) {
            alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }

        try {
            List<ServiceDefinitionGroupDTO> serviceRoles = new ArrayList<>();
            serviceRoles = serviceDefinitionBusinessDelegate.fetchAllRolesForServiceDefinition(id);
            List<String> existingroles =
                    serviceRoles.stream().map(ServiceDefinitionGroupDTO::getGroupId).collect(Collectors.toList());

            serviceDefinitionDTO = ValidateRequest(serviceDefinitionDTO, request, existingroles);
            if (serviceDefinitionDTO.getErrCode() != null) {
                return serviceDefinitionDTO.getErrCode().setErrorCode(new Result());
            }
            serviceDefinitionDTO = serviceDefinitionBusinessDelegate.editServiceDefinition(serviceDefinitionDTO);
            if (serviceDefinitionDTO == null) {
                alert.prepareError("Failed to edit service definition: ").log();
                return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
            }
            List<String> removeActionList = new ArrayList<>();
            JSONArray featureActions = new JSONArray();
            boolean actionLimitStatus = false;
            if (request.getParameter("featureactions") != null) {
                JSONArray addedActions = new JSONArray();
                try {
                    addedActions = new JSONArray(request.getParameter("featureactions"));
                } catch (Exception e) {
                    alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
                    return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
                }
                actionLimitStatus = validateActions(serviceDefinitionDTO.getServiceType(), addedActions, serviceDefinitionDTO.getCompanyLegalUnit());
                if (actionLimitStatus == false) {
                    alert.prepareError("Invalid actions or limits not in the given range").log();
                    return ErrorCodeEnum.ERR_21925.setErrorCode(new Result());
                }
                HashMap<String, ServiceDefinitionActionLimitDTO> existingMap = getExistingActionMap(serviceDefinitionDTO);
                Map<JSONArray,Boolean> resultMap = new HashMap<>();
    			resultMap = updateServiceDefinitionActions(addedActions, id, existingMap, userId,request);
    			for(Map.Entry<JSONArray,Boolean> entry : resultMap.entrySet()) {
    				featureActions = entry.getKey();
    				actionLimitStatus  = entry.getValue();
    				break;
    			}
                if (actionLimitStatus == false) {
                    alert.prepareError("Failed to edit service definition ").log();
                    return ErrorCodeEnum.ERR_21905.setErrorCode(new Result());
                }
                removeActionList = getRemoveActionList(addedActions, existingMap);
                actionLimitStatus = removeServiceDefinitionActions(removeActionList, existingMap);
                if (actionLimitStatus == false) {
                    alert.prepareError("Failed to edit service definition ").log();
                    return ErrorCodeEnum.ERR_21924.setErrorCode(new Result());
                }
            }
            ServiceDefinitionGroupDTO servicegroupDTO = new ServiceDefinitionGroupDTO();
            if(!existingroles.isEmpty()) {
            servicegroupDTO = serviceDefinitionBusinessDelegate.getDefaultRoleForServiceDefinition(id);
            
            /* Commenting the below code as part of fix for TSR-222573 */
//            if (servicegroupDTO == null) {
//                alert.prepareError("Failed to fetch default role for service definition: ").log();
//                return ErrorCodeEnum.ERR_22025.setErrorCode(new Result());
//            }
            
            boolean defaultStatus = false;
            if (servicegroupDTO!=null && !servicegroupDTO.getGroupId().equalsIgnoreCase(defaultGroup)) {
                ServiceDefinitionGroupDTO groupServiceDefinition = new ServiceDefinitionGroupDTO();
                groupServiceDefinition.setGroupId(defaultGroup);
                groupServiceDefinition.setServiceDefinitionId(id);
                groupServiceDefinition.setCompanyLegalUnit(legalEntityId);
                defaultStatus =
                        serviceDefinitionBusinessDelegate.editDefaultGroupServiceDefinition(groupServiceDefinition);
                if (defaultStatus == false)
                    return ErrorCodeEnum.ERR_22005.setErrorCode(result);
            }
           if(!limitsAndPermissionsUpdate(id, featureActions.toString(), removeActionList.toString(), request)) {
            	 diagnostic.prepareDebug("Runtime Exception.Exception Trace:").log();
                 return ErrorCodeEnum.ERR_20001.setErrorCode(result);
            }
            }
            }catch (Exception e) {
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            return ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        
        result = JSONToResult.convert(new JSONObject(serviceDefinitionDTO).toString());
        return result;
    }

    /*
     * Method to get existing feature actions of given service definition
     * 
     * @param serviceDefinitionDTO - contains all the details of service definition
     * 
     * @return Hashmap of existing featureactions with featureaction as id and limittypeId as value
     */
    private HashMap<String, ServiceDefinitionActionLimitDTO> getExistingActionMap(ServiceDefinitionDTO serviceDefinitionDTO) {
        List<ServiceDefinitionActionLimitDTO> actionList =
                serviceDefinitionBusinessDelegate.fetchServiceDefinitionActionLimit(serviceDefinitionDTO);

        HashMap<String, ServiceDefinitionActionLimitDTO> existingMap = new HashMap<>();
        for (ServiceDefinitionActionLimitDTO serviceLimit : actionList) {
            String key = serviceLimit.getActionId();
            if (!StringUtils.isBlank(serviceLimit.getLimitTypeId())) {
                key = constructKey(key, serviceLimit.getLimitTypeId());
            }
            existingMap.put(key, serviceLimit);
        }

        return existingMap;
    }

    /*
     * Method to get update/edit feature actions
     * 
     * @param actionlimits - List of all actions
     * 
     * @param serviceDefinitionId
     * 
     * @param existingMap - Existing featureactions for that service definition
     * 
     * @return true of update is successful otherwise false
     */
	private Map<JSONArray, Boolean> updateServiceDefinitionActions(JSONArray actionlimits, String serviceDefinitionId,
			HashMap<String, ServiceDefinitionActionLimitDTO> existingMap, String userId,
			DataControllerRequest request) {

		ServiceDefinitionActionLimitDTO actionLimitDTO = new ServiceDefinitionActionLimitDTO();
		boolean flag = false;
		JSONArray featureActions = new JSONArray();
		Map<JSONArray, Boolean> resultMap = new HashMap<>();
		String legalEntityId = request.getParameter("legalEntityId");
		String contractJobSchedlingConfig = getContractJobSchedulingConfig(request);
		boolean isImmediate = false;
		if (StringUtils.isNotEmpty(contractJobSchedlingConfig) && contractJobSchedlingConfig.equals("IMMEDIATE")) {
			isImmediate = true;
		}

		for (int i = 0; i < actionlimits.length(); i++) {
			String actionId = actionlimits.getJSONObject(i).getString("id");
			Boolean isNewActionVal = false;
			if(actionlimits.getJSONObject(i).has("isNewAction")) {
				isNewActionVal = actionlimits.getJSONObject(i).getBoolean("isNewAction");
			}
			actionLimitDTO = new ServiceDefinitionActionLimitDTO();
			actionLimitDTO.setServiceDefinitionId(serviceDefinitionId);
			actionLimitDTO.setActionId(actionId);
			actionLimitDTO.setCreatedBy(userId);
			actionLimitDTO.setCompanyLegalUnit(legalEntityId);
			if (actionlimits.getJSONObject(i).has("limits")
					&& (actionlimits.getJSONObject(i).getJSONArray("limits").length() > 0)) {
				JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");
				for (int j = 0; j < limits.length(); j++) {
					actionLimitDTO.setLimitTypeId(limits.getJSONObject(j).getString("id"));
					String key = constructKey(actionId, actionLimitDTO.getLimitTypeId());
					actionLimitDTO.setValue(limits.getJSONObject(j).getString("value"));
					if (existingMap.containsKey(key)) {
						if (existingMap.get(key).getValue() != actionLimitDTO.getValue()) {
							if (Float.parseFloat(existingMap.get(key).getValue()) > Float
									.parseFloat(actionLimitDTO.getValue())) {
								flag = true;
							}
							actionLimitDTO.setId(existingMap.get(key).getId());
							actionLimitDTO = serviceDefinitionBusinessDelegate
									.editServiceDefinitionActionLimit(actionLimitDTO);
						}
					} else {
						actionLimitDTO.setIsNewAction(isNewActionVal);
						actionLimitDTO.setId(String.valueOf(CommonUtilities.getNewId()));
						actionLimitDTO = serviceDefinitionBusinessDelegate
								.createServiceDefinitionActionLimit(actionLimitDTO);
					}
					if (actionLimitDTO == null) {
						alert.prepareError("Failed to edit the service definition actions limits").log();
						resultMap.put(featureActions, false);
						return resultMap;
					}
				}
				if (flag) {
					featureActions.put(actionlimits.getJSONObject(i));
					flag = false;
				}
			} else {
				if (!existingMap.containsKey(actionId)) {
					actionLimitDTO.setIsNewAction(isNewActionVal);
					actionLimitDTO.setId(String.valueOf(CommonUtilities.getNewId()));
					actionLimitDTO = serviceDefinitionBusinessDelegate
							.createServiceDefinitionActionLimit(actionLimitDTO);
				}
				if (actionLimitDTO == null) {
					alert.prepareError("Failed to update the service definition service definition actions  ").log();
					resultMap.put(featureActions, false);
					return resultMap;
				}
			}
		}
		if (isImmediate) {
			try {
				ThreadExecutor.execute(() -> callApi(serviceDefinitionId, request));
			} catch (Exception e) {
				alert.prepareError("Error while calling addNewFeaturesToContractFromSD ",e).log();
			}
		}
		resultMap.put(featureActions, true);
		return resultMap;
	}

	private String getContractJobSchedulingConfig(DataControllerRequest request) {
		Map<String, String> configurationsTableMap = new HashMap<>();
		String bundleId = "C360_CONFIG_BUNDLE";
		String configKey = "CONTRACT_JOB_SCHEDULING_CONFIG";
		configurationsTableMap.put(ODataQueryConstants.FILTER,
				"bundle_id eq '" + bundleId + "'" + " and config_key eq '" + configKey + "'");
		try {
			String readConfigurationsResponse = Executor.invokeService(ServiceURLEnum.CONFIGURATIONS_READ,
					configurationsTableMap, null, request);
			if (StringUtils.isNotBlank(readConfigurationsResponse)) {
				JSONObject configurationsJson = new JSONObject(readConfigurationsResponse);
				if (configurationsJson.has("configurations")) {
					JSONArray configurationsArray = configurationsJson.getJSONArray("configurations");
					return configurationsArray.getJSONObject(0).getString("config_value");
				}

			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching configurations ", e).log();
		}

		return null;
	}

	private boolean callApi(String serviceDefinitionId, DataControllerRequest request) throws Exception {
		String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
		Map<String, Object> postParametersMap = new HashMap<>();
		postParametersMap.put("serviceDefinitionId", serviceDefinitionId);
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put("backendToken", dbpServicesClaimsToken);
		try {
			String res = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
					.withOperationId(OperationName.OP_ADDNEWFEATURESTOCONTRACTFROMSD).withRequestHeaders(headerMap)
					.withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
			diagnostic.prepareDebug("respoonse from addNewFeaturesToContractFromSD "+res).log();
		} catch (DBPApplicationException e) {
			alert.prepareError(e.toString()).log();
			return false;
		}

		return true;

	}

	/*
     * Method to get feature actions which are deselected by the user
     * 
     * @param actionlimits - List of all actions
     * 
     * @param existingMap - Existing featureactions for that service definition
     * 
     * @return List of Strings containing featureactionIds which must be removed
     */
    private List<String> getRemoveActionList(JSONArray actionlimits, HashMap<String, ServiceDefinitionActionLimitDTO> existingMap) {
        List<String> removeActionList = new ArrayList<>();

        for (int i = 0; i < actionlimits.length(); i++) {
            String actionId = actionlimits.getJSONObject(i).getString("id");
            if (actionlimits.getJSONObject(i).has("limits")
                    && (actionlimits.getJSONObject(i).getJSONArray("limits").length() > 0)) {
                JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");
                for (int j = 0; j < limits.length(); j++) {
                    removeActionList.add(constructKey(actionId, limits.getJSONObject(j).getString("id")));
                }
            } else {
                removeActionList.add(actionId);
            }
        }

        Set<String> existingSet = existingMap.keySet();
        List<String> li = new ArrayList<>(existingSet);
        li.removeAll(removeActionList);
        return li;
    }

    /*
     * Method to remove service definition action when user deselects the feature actions using edit
     * 
     * @param removeList - List of actions to be de-asscoiated with the service definition
     * 
     * @param existingMap - Existing featureactions for that service definition
     * 
     * @return boolean - true if remove is successful otherwise false
     */
    private boolean removeServiceDefinitionActions(List<String> removeList, HashMap<String, ServiceDefinitionActionLimitDTO> existingMap) {
        ServiceDefinitionActionLimitDTO actionLimitDTO = new ServiceDefinitionActionLimitDTO();
        boolean status = false;
        for (String s : removeList) {
            actionLimitDTO = new ServiceDefinitionActionLimitDTO();
            actionLimitDTO.setId(existingMap.get(s).getId());
            status = serviceDefinitionBusinessDelegate.deleteServiceDefinitionActionLimit(actionLimitDTO);
            if (!status) {
                alert.prepareError("Failed to update the service definition actions").log();
                return false;
            }
        }
        return true;
    }

    @Override
    public Result deactivateServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        Result result = new Result();

        String serviceDefinitionId = null;

        if (inputParams.get("id") != null) {
            serviceDefinitionId = inputParams.get("id").toString();
        }

        String status = inputParams.get("status").toString();

        if (serviceDefinitionId == null || serviceDefinitionId.isEmpty()) {
            alert.prepareError("Missing servicedefinitionId").log();
            return ErrorCodeEnum.ERR_21902.setErrorCode(new Result());
        } else if (StringUtils.isBlank(status)
                || (!status.equalsIgnoreCase("SID_ACTIVE") && !status.equalsIgnoreCase("SID_INACTIVE"))) {
            alert.prepareError("Status cannot be other than SID_ACTIVE or SID_INACTIVE").log();
            return ErrorCodeEnum.ERR_21915.setErrorCode(new Result());
        }

        ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
        try {
            serviceDefinitionDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), ServiceDefinitionDTO.class);
        } catch (IOException e) {
            alert.prepareError("Error occured while fetching the input params: " + e).log();
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }

        boolean isAssociated = serviceDefinitionBusinessDelegate.isAssociatedToContract(serviceDefinitionId);
        
		if (isAssociated) {
			serviceDefinitionDTO = serviceDefinitionBusinessDelegate.editServiceDefinition(serviceDefinitionDTO);
			if (serviceDefinitionDTO == null) {
				alert.prepareError("Failed to deactivate/inactivate the service definition: ").log();
				return ErrorCodeEnum.ERR_21913.setErrorCode(new Result());
			}
		}else {
			alert.prepareError("Failed to deactivate/inactivate the service definition: ").log();
			return ErrorCodeEnum.ERR_21913.setErrorCode(new Result());
		}
        result = JSONToResult.convert(new JSONObject(serviceDefinitionDTO).toString());
        return result;
    }

    private Result createServiceDefinitionGroup(ServiceDefinitionDTO serviceDefinitionDTO, String defaultGroup,
            List<MemberGroupDTO> memberGroupList) {
        List<String> groups = memberGroupList.stream().map(MemberGroupDTO::getId).collect(Collectors.toList());
        Result result = new Result();
        if (!memberGroupList.isEmpty() && StringUtils.isNotBlank(defaultGroup) && groups.contains(defaultGroup)) {
            ServiceDefinitionGroupDTO serviceDefinitionGroupDTO = new ServiceDefinitionGroupDTO();
            for (MemberGroupDTO m : memberGroupList) {
                serviceDefinitionGroupDTO = new ServiceDefinitionGroupDTO();
                serviceDefinitionGroupDTO.setGroupId(m.getId());
                serviceDefinitionGroupDTO.setServiceDefinitionId(serviceDefinitionDTO.getId());
                serviceDefinitionGroupDTO.setCreatedBy(serviceDefinitionDTO.getCreatedby());
                serviceDefinitionGroupDTO.setCompanyLegalUnit(serviceDefinitionDTO.getCompanyLegalUnit());
                if (m.getId().equals(defaultGroup)) {
                    serviceDefinitionGroupDTO.setIsDefaultGroup("1");
                } else {
                    serviceDefinitionGroupDTO.setIsDefaultGroup("0");
                }
                serviceDefinitionGroupDTO =
                        serviceDefinitionBusinessDelegate.createServiceDefinitionGroup(serviceDefinitionGroupDTO);
                if (serviceDefinitionGroupDTO == null) {
                    alert.prepareError("Failed to create service definition ").log();
                    if (!serviceDefinitionBusinessDelegate.deleteServiceDefinition(serviceDefinitionDTO)) {
                        ErrorCodeEnum.ERR_21909.setErrorCode(result);
                        return result;
                    }
                    ErrorCodeEnum.ERR_22004.setErrorCode(result);
                    return result;
                }
            }
        } else if (memberGroupList.isEmpty() && StringUtils.isNotBlank(defaultGroup)) {
            alert.prepareError("Default group cannot be selected as the member group list is empty: ").log();
            ErrorCodeEnum.ERR_22027.setErrorCode(result);
            return result;
        }
        return result;
    }

    /*
     * Method to validate the user input feature actions based on service defnition type and also to validate action
     * limits using master data
     * 
     * @param roleTypeId - String value of service definition type - retail or business
     * 
     * @param actionlimits - JSON Array of action limits from user input
     * 
     * @return boolean - true if it contains valid actions otherwise false
     */
    private boolean validateActions(String roleTypeId, JSONArray actionlimits, String legalEntityId) {
        List<FeatureActionRoleTypeDTO> validRoleTypeActions =
                serviceDefinitionBusinessDelegate.getValidRoleTypeActions(roleTypeId);
        if (validRoleTypeActions == null) {
            alert.prepareError("Failed to fetch featureactions valid for role type").log();
            return false;
        }
        List<String> validRoleTypeActionIds =
                validRoleTypeActions.stream().map(FeatureActionRoleTypeDTO::getActionId).collect(Collectors.toList());

        String filterQuery = "companyLegalUnit eq "+legalEntityId;

        List<ActionLimitDTO> actionLimitDTOs = serviceDefinitionBusinessDelegate.fetchActionLimits(filterQuery);
        if (actionLimitDTOs == null) {
            alert.prepareError("Failed to fetch actions limits").log();
            return false;
        }
        HashMap<String, Double> masterMap = convertActionLimits(actionLimitDTOs);

        Set<String> actions = new HashSet<>();
        for (ActionLimitDTO a : actionLimitDTOs) {
            actions.add(a.getActionId());
        }

        LimitDTO limitDto = new LimitDTO();
        for (int i = 0; i < actionlimits.length(); i++) {
            String actionId = actionlimits.getJSONObject(i).getString("id");
            if (!validRoleTypeActionIds.contains(actionId)) {
                return false;
            }
            if (actions.contains(actionId)) {
                if (actionlimits.getJSONObject(i).has("limits")
                        && (actionlimits.getJSONObject(i).getJSONArray("limits").length() == 3)) {
                    JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");
                    limitDto = new LimitDTO();
                    limitDto.setActionId(actionId);
                    for (int j = 0; j < limits.length(); j++) {
                        String limitId = limits.getJSONObject(j).getString("id");
                        String key = actionId + ".MIN_TRANSACTION_LIMIT";
                        double value = Double.parseDouble(limits.getJSONObject(j).getString("value"));
                        if (limitId.equals("MAX_TRANSACTION_LIMIT")) {
                            limitDto.setMaxTransactionLimit(value);
                            limitDto.setMinTransactionLimit(masterMap.get(key));
                        } else if (limitId.equals("DAILY_LIMIT")) {
                            limitDto.setDailyLimit(value);
                        } else if (limitId.equals("WEEKLY_LIMIT")) {
                            limitDto.setWeeklyLimit(value);
                        } else {
                            return false;
                        }

                    }
                    if (!isValidLimitRange(limitDto, masterMap)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    /*
     * Method to validate the user input limit range with default limit range
     * 
     * @param limit - LimitDTO of each monetary action
     * 
     * @param masterMap - HashMap containing masterdata limits
     * 
     * @return boolean - true if it is valid otherwise false
     */
    private boolean isValidLimitRange(LimitDTO limit, HashMap<String, Double> masterMap) {
        if (limit.getMaxTransactionLimit() > masterMap.get(constructKey(limit.getActionId(), "MAX_TRANSACTION_LIMIT"))
        		|| limit.getMaxTransactionLimit() < limit.getMinTransactionLimit() || limit.getMaxTransactionLimit() >limit.getDailyLimit()
                || limit.getMaxTransactionLimit() > limit.getWeeklyLimit()) {
            return false;
        } else if (limit.getDailyLimit() > masterMap.get(constructKey(limit.getActionId(), "DAILY_LIMIT"))
        		|| limit.getDailyLimit() < limit.getMaxTransactionLimit() || limit.getDailyLimit() > limit.getWeeklyLimit()) {    	
            return false;
        } else if (limit.getWeeklyLimit() > masterMap.get(constructKey(limit.getActionId(), "WEEKLY_LIMIT"))
        		|| limit.getWeeklyLimit() < limit.getDailyLimit() || limit.getWeeklyLimit() < limit.getMaxTransactionLimit() ) {
            return false;
        }
        return true;
    }

    private String constructKey(String action, String limit) {
        return action + "." + limit;
    }

    @Override
    public Result fetchAllRolesForServiceDefinition(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
    	String companyLegalUnit = request.getParameter("legalEntityId");
        Result result = new Result();
        @SuppressWarnings("unchecked")
        Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
        Dataset rolesDataset = new Dataset("roles");
        String id = inputParams.get("serviceDefinitionId");

        if (id == null || StringUtils.isBlank(id)) {
            alert.prepareError("Service Definition Id cannot be null").log();
            return ErrorCodeEnum.ERR_21902.setErrorCode(result);
        }
        List<ServiceDefinitionGroupDTO> serviceDefinitiongroupDTOs =
                serviceDefinitionBusinessDelegate.fetchAllRolesForServiceDefinition(id);
        if (serviceDefinitiongroupDTOs == null) {
            alert.prepareError("Error occurred while fetching roles for service definition").log();
            return ErrorCodeEnum.ERR_22014.setErrorCode(result);
        }
        if (serviceDefinitiongroupDTOs.size() == 0) {
            result.addParam(new Param("serviceDefinitionId", id));
            result.addDataset(rolesDataset);
            return result;
        }
        List<GroupsViewDTO> groups = customerRoleBusinessDelegate.getAllGroups(companyLegalUnit);
        Map<String, GroupsViewDTO> groupsmap = new HashMap<>();

        if (groups == null) {
            alert.prepareError("Error occurred while fetching roles information").log();
            return ErrorCodeEnum.ERR_22015.setErrorCode(result);
        }

        groups.forEach((group) -> {
            groupsmap.put(group.getGroupId(), group);
        });
        try {
            serviceDefinitiongroupDTOs.forEach((servGroup) -> {
                Record role = new Record();
                GroupsViewDTO group = groupsmap.get(servGroup.getGroupId());
                role.addParam(new Param("id", servGroup.getGroupId()));
                role.addParam(new Param("name", group.getGroupName()));
                role.addParam(new Param("isDefaultGroup", servGroup.getIsDefaultGroup()));
                role.addParam(new Param("typeId", group.getTypeId()));
                role.addParam(new Param("status", group.getStatusId()));
                role.addParam(new Param("description", group.getGroupDesc()));
                rolesDataset.addRecord(role);
            });
            result.addParam(new Param("serviceDefinitionId", id));
            result.addDataset(rolesDataset);
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ", exp).log();
            return ErrorCodeEnum.ERR_22016.setErrorCode(new Result());
        }

        return result;
    }

    @Override
    public Result updateDefaultRole(String methodId, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        @SuppressWarnings("unchecked")
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        Result result = new Result();

        String serviceDefinitionId = null;

        if (inputParams.get("id") != null) {
            serviceDefinitionId = inputParams.get("id").toString();
        }

        String defaultRole = inputParams.get("defaultRole").toString();

        if (serviceDefinitionId == null || serviceDefinitionId.isEmpty()) {
            alert.prepareError("Missing servicedefinitionId").log();
            return ErrorCodeEnum.ERR_21902.setErrorCode(new Result());
        } else if (StringUtils.isBlank(defaultRole)) {
            alert.prepareError("Default Role cannot be empty").log();
            return ErrorCodeEnum.ERR_22026.setErrorCode(new Result());
        }
        List<ServiceDefinitionGroupDTO> serviceRoles = new ArrayList<>();
        serviceRoles = serviceDefinitionBusinessDelegate.fetchAllRolesForServiceDefinition(serviceDefinitionId);
        List<String> existingroles =
                serviceRoles.stream().map(ServiceDefinitionGroupDTO::getGroupId).collect(Collectors.toList());
        if (existingroles.isEmpty() || !existingroles.contains(defaultRole)) {
            alert.prepareError("Default Group Id found in request is not a valid customer group").log();
            return ErrorCodeEnum.ERR_22028.setErrorCode(new Result());
        }

        ServiceDefinitionGroupDTO groupServiceDefinition = new ServiceDefinitionGroupDTO();
        groupServiceDefinition.setGroupId(defaultRole);
        groupServiceDefinition.setServiceDefinitionId(serviceDefinitionId);
        boolean status = false;
        status = serviceDefinitionBusinessDelegate.editDefaultGroupServiceDefinition(groupServiceDefinition);
        if (!status) {
            alert.prepareError("Failed to update default role for service definition: ").log();
            return ErrorCodeEnum.ERR_22005.setErrorCode(new Result());
        }

        result = JSONToResult.convert(new JSONObject(groupServiceDefinition).toString());
        return result;
    }

    @Override
    public Result fetchAllServiceDefinitionsForContract(String methodId, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) {
        Result result = new Result();
        String legalEntityId = request.getParameter("legalEntityId");
        
        if(StringUtils.isBlank(legalEntityId)){
           // return ErrorCodeEnum.ERR_22230.setErrorCode(new Result());  
        	legalEntityId = EnvironmentConfiguration.BRANCH_ID_REFERENCE.getValue(request);
        	
        }
  
		String[] reqPermissions = {PermissionName.API_ACCESS,PermissionName.VIEW_SERVICEDEFINITION};
		try {
			if(!LoggedInUserHandler.hasAccessToLegalEntity(request,reqPermissions))
			{
				result.addParam(new Param("Status", "Get servicedefnitions for contract failed", FabricConstants.STRING));
				ErrorCodeEnum.ERR_22231.setErrorCode(result);
				alert.prepareError("Logged in user do not have access to this legalEntity ").log();
				return result;
				
			}
		} catch (Exception e) {
			 alert.prepareError("Unexpected error occured while fetching service definitions for contract : ", e).log();
	            return ErrorCodeEnum.ERR_22030.setErrorCode(new Result());
}
        List<ServiceDefinitionDTO> serviceDefinitionDTOs =
                serviceDefinitionBusinessDelegate.fetchAllServiceDefinitionsForContract(legalEntityId);
        if (serviceDefinitionDTOs == null) {
            alert.prepareError("Error occurred while fetching service definition").log();
            return ErrorCodeEnum.ERR_21903.setErrorCode(new Result());
        }

        if (serviceDefinitionDTOs.size() == 0) {
            return JSONToResult.convert(new JSONObject().put("ServiceDefinitionRecords", new JSONArray()).toString());
        }

        try {
            JSONArray records = new JSONArray(serviceDefinitionDTOs);
            JSONObject resultObject = new JSONObject();
            resultObject.put("ServiceDefinitionRecords", records);
            result = JSONToResult.convert(resultObject.toString());
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ", exp).log();
            return ErrorCodeEnum.ERR_22030.setErrorCode(new Result());
        }

        return result;
    }

    @Override
    public Result fetchAllServiceDefinitionFeatureLimits(String methodId, Object[] inputArray,
            DataControllerRequest request, DataControllerResponse response) {
        Result result = new Result();
        Map<String,String> minactionlimits= new HashMap<String, String>();

        try {
            String serviceDefinitionId = request.getParameter("serviceDefinitionId");
            if (StringUtils.isBlank(serviceDefinitionId)) {
                ErrorCodeEnum.ERR_21902.setErrorCode(result);
                return result;
            }
            List<ServiceDefinitionFeatureActionViewDTO> groupActions =
                    serviceDefinitionBusinessDelegate.getServiceDefinitionFeatureActions(serviceDefinitionId);
            if (groupActions == null) {
                ErrorCodeEnum.ERR_21904.setErrorCode(result);
                return result;
            }
            List<ActionLimitDTO> actionlimits =serviceDefinitionBusinessDelegate.getMinTransactionLimits();
            actionlimits.forEach((action)->{
            	minactionlimits.put(action.getActionId(), action.getValue());
            });
            result = convertServiceDefinitionFeatureLimits(groupActions, minactionlimits);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            alert.prepareError("Exception occured in fetchAllServiceDefinitionActionLimits JAVA service. Error: ", e).log();
        }

        return result;
    }

    private Result convertServiceDefinitionFeatureLimits(List<ServiceDefinitionFeatureActionViewDTO> groupActions, Map<String,String> minactionlimits) {
        Result result = new Result();
        Map<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> features = new HashMap<>();
        try {
            groupActions.forEach((action) -> {
                if ("false".equalsIgnoreCase(action.getSoftdelete())) {
                    if (features.containsKey(action.getFeatureId())) {
                        Map<String, ServiceDefinitionFeatureActionViewDTO> actionsMap =
                                features.get(action.getFeatureId());
                        if (actionsMap.containsKey(action.getActionId())) {
                            ServiceDefinitionFeatureActionViewDTO existingAction = actionsMap.get(action.getActionId());
                            if (StringUtils.isNotBlank(action.getLimitTypeId())
                                    && StringUtils.isNotBlank(action.getValue())) {
                                existingAction.insertLimit(action.getLimitTypeId(), action.getValue());
                            }
                            if (StringUtils.isNotBlank(action.getDependentactionId())
                                    && StringUtils.isNotBlank(action.getDependentActionName())
                                    && StringUtils.isNotBlank(action.getDependentFeatureName())) {
                                existingAction.insertDependentActions(action.getDependentactionId(),
                                        action.getDependentActionName(), action.getDependentFeatureName());
                            }

                        } else {
                            if (StringUtils.isNotBlank(action.getLimitTypeId())
                                    && StringUtils.isNotBlank(action.getValue())) {
                                action.insertLimit(action.getLimitTypeId(), action.getValue());
                            }
                            if (StringUtils.isNotBlank(action.getDependentactionId())
                                    && StringUtils.isNotBlank(action.getDependentActionName())
                                    && StringUtils.isNotBlank(action.getDependentFeatureName())) {
                                action.insertDependentActions(action.getDependentactionId(),
                                        action.getDependentActionName(), action.getDependentFeatureName());
                            }
                            actionsMap.put(action.getActionId(), action);
                        }
                    } else {
                        Map<String, ServiceDefinitionFeatureActionViewDTO> actionsMap = new HashMap<>();
                        if (StringUtils.isNotBlank(action.getLimitTypeId())
                                && StringUtils.isNotBlank(action.getValue())) {
                            action.insertLimit(action.getLimitTypeId(), action.getValue());
                        }
                        if (StringUtils.isNotBlank(action.getDependentactionId())
                                && StringUtils.isNotBlank(action.getDependentActionName())
                                && StringUtils.isNotBlank(action.getDependentFeatureName())) {
                            action.insertDependentActions(action.getDependentactionId(),
                                    action.getDependentActionName(), action.getDependentFeatureName());
                        }
                        actionsMap.put(action.getActionId(), action);
                        features.put(action.getFeatureId(), actionsMap);
                    }
                }
            });
            result = getFormattedResult(features,minactionlimits);
        } catch (Exception e) {
            alert.prepareError("Failed to fetch common features and actions list", e).log();
            ErrorCodeEnum.ERR_22031.setErrorCode(result);
        }
        return result;
    }

    private Result getFormattedResult(Map<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> features, Map<String,String> minactionlimits) {
        Result result = new Result();
        Dataset featuresDataset = new Dataset("features");
        Dataset limitsDataset = new Dataset("limits");

        for (Map.Entry<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> f : features.entrySet()) {
            Record feature = new Record();
            Record limitfeature = new Record();

            Dataset actions = new Dataset("actions");
            Dataset limitActions = new Dataset("actions");

            for (Map.Entry<String, ServiceDefinitionFeatureActionViewDTO> a : f.getValue().entrySet()) {
                ServiceDefinitionFeatureActionViewDTO serviceFeatureActionView = a.getValue();
                Record action = new Record();

                if (feature.getParam("featureName") == null) {
                    feature.addParam(new Param("featureId", serviceFeatureActionView.getFeatureId()));
                    feature.addParam(new Param("featureName", serviceFeatureActionView.getFeatureName()));
                    feature.addParam(new Param("featureDescription", serviceFeatureActionView.getFeatureDescription()));
                    feature.addParam(new Param("featureStatus", serviceFeatureActionView.getFeatureStatusId()));
                    feature.addParam(new Param("legalEntityId", serviceFeatureActionView.getCompanyLegalUnit()));
                }
                action.addParam(new Param("actionId", a.getKey()));
                action.addParam(new Param("actionName", serviceFeatureActionView.getActionName()));
                action.addParam(new Param("accessPolicy", serviceFeatureActionView.getAccessPolicy()));
                action.addParam(new Param("accessPolicyId", serviceFeatureActionView.getAccessPolicyId()));
                action.addParam(new Param("actionLevel", serviceFeatureActionView.getActionlevel()));
                action.addParam(new Param("actionLevelId", serviceFeatureActionView.getActionlevelId()));
                action.addParam(new Param("actionDescription", serviceFeatureActionView.getActionName()));
                action.addParam(new Param("isAccountLevel", serviceFeatureActionView.getIsAccountLevel()));
                action.addParam(new Param("actionStatus", serviceFeatureActionView.getActionStatus()));

                Dataset dependentActions = new Dataset("dependentActions");
                for (Map.Entry<String, Map<String, String>> l : a.getValue().getDependentFeatureAndActions()
                        .entrySet()) {
                    for (Map.Entry<String, String> m : l.getValue().entrySet()) {
                        Record dependentAction = new Record();
                        dependentAction.addParam(new Param("id", m.getKey()));
                        dependentAction.addParam(new Param("name", m.getValue()));
                        dependentAction.addParam(new Param("featureName", l.getKey()));
                        dependentActions.addRecord(dependentAction);
                    }

                }
                action.addDataset(dependentActions);
                actions.addRecord(action);

                if (!serviceFeatureActionView.getLimits().isEmpty()) {
                    Record lmtaction = new Record();
                    limitfeature.addAllParams(feature.getAllParams());
                    lmtaction.addAllParams(action.getAllParams());
                    lmtaction.addParam(new Param("limitgroup",
                            (!StringUtils.isBlank(serviceFeatureActionView.getLimitGroup())
                                    ? serviceFeatureActionView.getLimitGroup()
                                    : "N/A")));
                    lmtaction.addParam(new Param("limitgroupId",
                            (!StringUtils.isBlank(serviceFeatureActionView.getLimitGroupId())
                                    ? serviceFeatureActionView.getLimitGroupId()
                                    : "N/A")));
                    Dataset limits = new Dataset("limits");
                    for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
                        Record limit = new Record();
                        limit.addParam(new Param("id", l.getKey()));
                        limit.addParam(new Param("value", l.getValue()));
                        limits.addRecord(limit);
                    }
                    if (limits.getAllRecords().size() > 0) {
                    	if(minactionlimits!=null) {
                    	if(StringUtils.isNotBlank(minactionlimits.get(a.getKey()))) {
                    	Record minLimit = new Record();
                        minLimit.addParam(new Param("id", "MIN_TRANSACTION_LIMIT"));
                        minLimit.addParam(new Param("value", minactionlimits.get(a.getKey())));
                        limits.addRecord(minLimit);
                    	}
                    	}
                        lmtaction.addDataset(limits);
                    }
                    limitActions.addRecord(lmtaction);
                }
            }
            feature.addDataset(actions);
            featuresDataset.addRecord(feature);
            if (limitActions.getAllRecords().size() > 0) {
                limitfeature.addDataset(limitActions);
                limitsDataset.addRecord(limitfeature);
            }
        }
        result.addDataset(featuresDataset);
        result.addDataset(limitsDataset);
        return result;
    }
    
    private boolean containSpecialChars(String name) {
		Pattern regex = Pattern.compile("[+=\\\\|<>^*%]");
		if (regex.matcher(name).find()) {
		    return true;
		} 
		return false;
	}

	@Override
	public Result getServiceDefinitionMonetaryActions(String methodId, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		Result result = new Result();
        Map<String,String> minactionlimits= new HashMap<String, String>();

        try {
            String serviceDefinitionId = request.getParameter("serviceDefinitionId");
            String legalEntityId = request.getParameter("legalEntityId");
            if (StringUtils.isBlank(serviceDefinitionId)) {
                ErrorCodeEnum.ERR_21902.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(legalEntityId)) {
                ErrorCodeEnum.ERR_22232.setErrorCode(result);
                return result;
            }
            List<ServiceDefinitionFeatureActionViewDTO> groupActions =
                    serviceDefinitionBusinessDelegate.getServiceDefinitionMonetaryActions(serviceDefinitionId, legalEntityId);
            if (groupActions == null) {
                ErrorCodeEnum.ERR_21904.setErrorCode(result);
                return result;
            }
            List<ActionLimitDTO> actionlimits =serviceDefinitionBusinessDelegate.getMinTransactionLimits();
            actionlimits.forEach((action)->{
            	minactionlimits.put(action.getActionId(), action.getValue());
            });
            result = convertServiceDefinitionFeatureLimits(groupActions,minactionlimits);
            result.removeDatasetById("features");
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            alert.prepareError("Exception occured in getServiceDefinitionMonetaryActions JAVA service. Error: ", e).log();
        }

        return result;
	}
	
	public boolean limitsAndPermissionsUpdate(String id,String actions,String removedActions, DataControllerRequest request) throws DBPAuthenticationException {
		 DBPServices.updateServiceDefinitionLimitsAndPermissions(id, actions, removedActions, request);
        return true;
	}

	@Override
	public Result searchServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = null;
		try {
			
			String searchText = request.getParameter("searchText");
			String limit = String.valueOf(EnvironmentParamRead.getSuggestionRecordLimit(request));
			if(StringUtils.isNotBlank(searchText)) {
				JSONObject responseObj = serviceDefinitionBusinessDelegate.searchServiceDefinition(searchText, limit);
				if(null != responseObj) {
					result = CommonUtilities.constructResultFromJSONObject(responseObj);
					return result;
				}
			}

		}catch(Exception exp) {
			 alert.prepareError("Exception occured in searchServiceDefinition ResourceImpl: ", exp).log();
		}
		
		result = new Result();
		Dataset serviceDataset = new Dataset();
		serviceDataset.setId("servicedefinition");
		result.addDataset(serviceDataset);
		
		return result;
	}

	@Override
	public Result getServiceDefinitionProductIdPermissions(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {
		Result result = new Result();
		try {
			if (request.getParameter("serviceDefinitionId") == null || request.getParameter("productIdList") == null || request.getParameter("legalEntityId") == null) {
				ErrorCodeEnum.ERR_22194.setErrorCode(result);
				return result;
			} else {
				String serviceDefinitionId = request.getParameter("serviceDefinitionId");
				String productIdList = request.getParameter("productIdList");
				String legalEntityId = request.getParameter("legalEntityId");
				Map<String, Object> postParametersMap = new HashMap<>();
				postParametersMap.put("serviceDefinitionId", serviceDefinitionId);
				postParametersMap.put("productIdList", productIdList);
				postParametersMap.put("legalEntityId", legalEntityId);
				diagnostic.prepareDebug("postparamsmap " + postParametersMap).log();
				String dbpServicesClaimsToken = DBPServices.getDBPServicesClaimsToken(request);
				JSONObject getServiceDefinitionProductIdPermissionsResponse = serviceDefinitionBusinessDelegate
						.getServiceDefinitionProductIdPermissions(postParametersMap, dbpServicesClaimsToken);
				diagnostic.prepareDebug("response from getServiceDefinitionProductIdPermissions "
						+ getServiceDefinitionProductIdPermissionsResponse).log();
				if (getServiceDefinitionProductIdPermissionsResponse == null
						|| !getServiceDefinitionProductIdPermissionsResponse.has(FabricConstants.OPSTATUS)
						|| getServiceDefinitionProductIdPermissionsResponse.getInt(FabricConstants.OPSTATUS) != 0) {
					ErrorCodeEnum.ERR_22195.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					AuditHandler.auditAdminActivity(request, ModuleNameEnum.SERVICE_DEFINITION, EventEnum.SEARCH,
							ActivityStatusEnum.FAILED, "Service Definition Product Id permissions fetch Failed");
					return result;
				} else if (getServiceDefinitionProductIdPermissionsResponse.has("dbpErrMsg")) {
					result = CommonUtilities
							.constructResultFromJSONObject(getServiceDefinitionProductIdPermissionsResponse);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				} else {

					JSONArray features = getServiceDefinitionProductIdPermissionsResponse.getJSONArray("features");
					Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(features);
					recordsDataset.setId("features");
					result.addDataset(recordsDataset);
					result.addParam(new Param("status", "Success", FabricConstants.STRING));
					result.addParam(new Param("opstatus",
							getServiceDefinitionProductIdPermissionsResponse.get("opstatus").toString(),
							FabricConstants.STRING));

					AuditHandler.auditAdminActivity(request, ModuleNameEnum.SERVICE_DEFINITION, EventEnum.SEARCH,
							ActivityStatusEnum.SUCCESSFUL,
							"Successfully fetched ProductId Permissions " + serviceDefinitionId);

				}
			}

		} catch (Exception e) {
			alert.prepareError("Unexepected Error in get Service Definition Product Id permissions", e).log();
			result.addParam(new Param("FailureReason", "Unexpected Error", FabricConstants.STRING));
			ErrorCodeEnum.ERR_22196.setErrorCode(result);
			AuditHandler.auditAdminActivity(request, ModuleNameEnum.SERVICE_DEFINITION, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Service Definition Product Id permissions fetch Failed");
		}
		return result;
	}
}