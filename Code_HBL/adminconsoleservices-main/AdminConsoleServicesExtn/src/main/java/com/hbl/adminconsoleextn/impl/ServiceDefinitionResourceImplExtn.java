package com.hbl.adminconsoleextn.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.hbl.adminconsoleextn.dto.LimitsDTOExtn;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.commons.utils.ThreadExecutor;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.service.customerrole.businessdelegate.api.CustomerRoleBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.servicedefinition.dto.LimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionFeatureActionViewDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionGroupDTO;
import com.kony.adminconsole.service.servicedefinition.resource.impl.ServiceDefinitionResourceImpl;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.logging.log4j.LogManager;

public class ServiceDefinitionResourceImplExtn extends ServiceDefinitionResourceImpl{
	private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ServiceDefinitionResourceImplExtn.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    ServiceDefinitionBusinessDelegate serviceDefinitionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class)
            .getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);

    CustomerRoleBusinessDelegate customerRoleBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerRoleBusinessDelegate.class);
	@Override
    public Result editServiceDefinition(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        Result result = new Result();
        ServiceDefinitionDTO serviceDefinitionDTO = new ServiceDefinitionDTO();
        LOG.debug("HBL:ServiceDefinitionResourceImplExtn:editServiceDefinition:");
        @SuppressWarnings("unchecked")
        Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
        String defaultGroup = inputParams.get("defaultRole");
        String id = inputParams.get("id");
        String legalEntityId = inputParams.get("legalEntityId");
        if (StringUtils.isBlank(id)) {
            LOG.error("Id cannot be null ");
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
            LOG.error("Caught exception while converting input params to DTO: ", e);
            return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
        }

        try {
            List<ServiceDefinitionGroupDTO> serviceRoles = new ArrayList<>();
            serviceRoles = serviceDefinitionBusinessDelegate.fetchAllRolesForServiceDefinition(id);
            List<String> existingroles =
                    serviceRoles.stream().map(ServiceDefinitionGroupDTO::getGroupId).collect(Collectors.toList());
            LOG.debug("ServiceDefinitionResourceImplExtn:editServiceDefinition:existingroles:"+existingroles);
            serviceDefinitionDTO = ValidateRequest(serviceDefinitionDTO, request, existingroles);
            if (serviceDefinitionDTO.getErrCode() != null) {
                return serviceDefinitionDTO.getErrCode().setErrorCode(new Result());
            }
            serviceDefinitionDTO = serviceDefinitionBusinessDelegate.editServiceDefinition(serviceDefinitionDTO);
            if (serviceDefinitionDTO == null) {
                LOG.error("Failed to edit service definition: ");
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
                    LOG.error("Caught exception while converting input params to DTO: ", e);
                    return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
                }
                actionLimitStatus = validateActions(serviceDefinitionDTO.getServiceType(), addedActions, serviceDefinitionDTO.getCompanyLegalUnit());
                if (actionLimitStatus == false) {
                    LOG.error("Invalid actions or limits not in the given range");
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
                    LOG.error("Failed to edit service definition ");
                    return ErrorCodeEnum.ERR_21905.setErrorCode(new Result());
                }
                removeActionList = getRemoveActionList(addedActions, existingMap);
                actionLimitStatus = removeServiceDefinitionActions(removeActionList, existingMap);
                if (actionLimitStatus == false) {
                    LOG.error("Failed to edit service definition ");
                    return ErrorCodeEnum.ERR_21924.setErrorCode(new Result());
                }
            }
            ServiceDefinitionGroupDTO servicegroupDTO = new ServiceDefinitionGroupDTO();
            if(!existingroles.isEmpty()) {
            servicegroupDTO = serviceDefinitionBusinessDelegate.getDefaultRoleForServiceDefinition(id);
            
            /* Commenting the below code as part of fix for TSR-222573 */
//            if (servicegroupDTO == null) {
//                LOG.error("Failed to fetch default role for service definition: ");
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
            LOG.debug("ServiceDefinitionResourceImplExtn:editServiceDefinition:removeActionList:"+removeActionList);
           if(!limitsAndPermissionsUpdate(id, featureActions.toString(), removeActionList.toString(), request)) {
            	 LOG.debug("Runtime Exception.Exception Trace:");
                 return ErrorCodeEnum.ERR_20001.setErrorCode(result);
            }
            }
            }catch (Exception e) {
            LOG.debug("Runtime Exception.Exception Trace:", e);
            return ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        
        result = JSONToResult.convert(new JSONObject(serviceDefinitionDTO).toString());
        return result;
    }
	public boolean limitsAndPermissionsUpdate(String id,String actions,String removedActions, DataControllerRequest request) throws DBPAuthenticationException {
		 DBPServices.updateServiceDefinitionLimitsAndPermissions(id, actions, removedActions, request);
       return true;
	}

    public boolean validateActions(String roleTypeId, JSONArray actionlimits, String legalEntityId) {
        List<FeatureActionRoleTypeDTO> validRoleTypeActions =
                serviceDefinitionBusinessDelegate.getValidRoleTypeActions(roleTypeId);
        if (validRoleTypeActions == null) {
            LOG.error("Failed to fetch featureactions valid for role type");
            return false;
        }
        List<String> validRoleTypeActionIds =
                validRoleTypeActions.stream().map(FeatureActionRoleTypeDTO::getActionId).collect(Collectors.toList());

        String filterQuery = "companyLegalUnit eq "+legalEntityId;

        List<ActionLimitDTO> actionLimitDTOs = serviceDefinitionBusinessDelegate.fetchActionLimits(filterQuery);
        if (actionLimitDTOs == null) {
            LOG.error("Failed to fetch actions limits");
            return false;
        }
        HashMap<String, Double> masterMap = convertActionLimits(actionLimitDTOs);

        Set<String> actions = new HashSet<>();
        for (ActionLimitDTO a : actionLimitDTOs) {
            actions.add(a.getActionId());
        }


        LimitsDTOExtn limitDto = new LimitsDTOExtn();
        LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateActions:frontEnd: ActionLimitDTOs"+actionlimits.toString());
        LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateActions: DBactions masterMap:"+masterMap.toString());
        boolean isVaild=false;
        for (int i = 0; i < actionlimits.length(); i++) {
            String actionId = actionlimits.getJSONObject(i).getString("id");
            if (!validRoleTypeActionIds.contains(actionId)) {
                return false;
            }
            int length=actionlimits.getJSONObject(i).getJSONArray("limits").length();
            LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateActions: limits length:"+length);
            LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateActions:current  actionId:"+actionId);
            LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateActions: actions.contains(actionId):"+actions.contains(actionId));
            if (actions.contains(actionId)) {
                if (actionlimits.getJSONObject(i).has("limits")
                        && (actionlimits.getJSONObject(i).getJSONArray("limits").length() == 3)) {
                    JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");
                    limitDto = new LimitsDTOExtn();
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
                    if (!isValidLimitRange(limitDto, masterMap, false)) {
                        return false;
                    }
                } else {
                	isVaild=setMBLimits(actionlimits, actions, masterMap, actionId, i);
                }
            }
            
        }

        return isVaild;
    }
    public boolean setMBLimits(JSONArray actionlimits, Set<String> actions, HashMap<String, Double> masterMap, String actionId, int i) {
    	LOG.debug("HBL:ServiceDefinitionResourceImplExtn:setMBLimits:");
            int length=actionlimits.getJSONObject(i).getJSONArray("limits").length();
            LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateMBActions: limits length:"+length);
            LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateMBActions:current  actionId:"+actionId);
            LOG.debug("HBL:ServiceDefinitionResourceImplExtn:validateMBActions: actions.contains(actionId):"+actions.contains(actionId));
            if (actions.contains(actionId)) {
                if (actionlimits.getJSONObject(i).has("limits")
                        && (actionlimits.getJSONObject(i).getJSONArray("limits").length() == 6)) {
                    JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");
                    LimitsDTOExtn limitDto = new LimitsDTOExtn();
                    limitDto.setActionId(actionId);
                    for (int j = 0; j < limits.length(); j++) {
                        String limitId = limits.getJSONObject(j).getString("id");
                        String key = actionId + ".MIN_TRANSACTION_LIMIT";
                        String key2 = actionId + ".MB_MIN_TRANSACTION_LIMIT";
                        double value = Double.parseDouble(limits.getJSONObject(j).getString("value"));
                        if (limitId.equals("MAX_TRANSACTION_LIMIT")) {
                            limitDto.setMaxTransactionLimit(value);
                            limitDto.setMinTransactionLimit(masterMap.get(key));
                        } else if (limitId.equals("DAILY_LIMIT")) {
                            limitDto.setDailyLimit(value);
                        } else if (limitId.equals("WEEKLY_LIMIT")) {
                            limitDto.setWeeklyLimit(value);
                        } else if (limitId.equals("MB_MAX_TRANSACTION_LIMIT")) {
                            limitDto.setMaxMbTransactionLimit(value);
                            limitDto.setMinMBTransactionLimit(masterMap.get(key2));
                        } else if (limitId.equals("MB_DAILY_LIMIT")) {
                            limitDto.setDailyMBLimit(value);
                        } else if (limitId.equals("MB_WEEKLY_LIMIT")) {
                            limitDto.setWeeklyMBLimit(value);
                        } else {
                            return false;
                        }

                    }
                    if (!isValidMBLimitRange(limitDto, masterMap)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        return true;
    }
	 private HashMap<String, Double> convertActionLimits(List<ActionLimitDTO> actionLimitDTOs) {
	        HashMap<String, Double> masterMap = new HashMap<>();
	        try {
	            for (ActionLimitDTO a : actionLimitDTOs) {
	                double value = Double.parseDouble(a.getValue());
	                masterMap.put(constructKey(a.getActionId(), a.getLimitTypeId()), value);
	            }
	        } catch (Exception e) {
	            LOG.error("Exception while converting the list");
	        }
	        return masterMap;
	    }
	 private boolean isValidLimitRange(LimitsDTOExtn limit, HashMap<String, Double> masterMap,boolean mbValidationRequired) {
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
	 private boolean isValidMBLimitRange(LimitsDTOExtn limit, HashMap<String, Double> masterMap) {
		 LOG.debug("HBL::isValidMBLimits:");
		 LOG.debug("HBL::isValidLimitRange:getMaxTransactionLimit:"+limit.getMaxTransactionLimit());
		 LOG.debug("HBL::isValidLimitRange:getDailyLimit:"+limit.getDailyLimit());
		 LOG.debug("HBL::isValidLimitRange:getWeeklyLimit:"+limit.getWeeklyLimit());
		 LOG.debug("HBL::isValidLimitRange:getMinTransactionLimit:"+limit.getMinTransactionLimit());
		 LOG.debug("HBL::isValidLimitRange:masterMap:MAX_TRANSACTION_LIMIT:"+ masterMap.get(constructKey(limit.getActionId(), "MAX_TRANSACTION_LIMIT")));
		 LOG.debug("HBL::isValidLimitRange:masterMap:DAILY_LIMIT:"+ masterMap.get(constructKey(limit.getActionId(), "DAILY_LIMIT")));
		 LOG.debug("HBL::isValidLimitRange:masterMap:WEEKLY_LIMIT:"+ masterMap.get(constructKey(limit.getActionId(), "WEEKLY_LIMIT")));
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
	        LOG.debug("HBL::isValidMBLimits:");
			 LOG.debug("HBL::isValidLimitRange:getMaxMbTransactionLimit:"+limit.getMaxMbTransactionLimit());
			 LOG.debug("HBL::isValidLimitRange:getDailyMBLimit:"+limit.getDailyMBLimit());
			 LOG.debug("HBL::isValidLimitRange:getWeeklyMBLimit:"+limit.getWeeklyMBLimit());
			 LOG.debug("HBL::isValidLimitRange:getMinMBTransactionLimit:"+limit.getMinMBTransactionLimit());
			 
			 LOG.debug("HBL::isValidLimitRange:masterMap:MAX_TRANSACTION_LIMIT:"+ masterMap.get(constructKey(limit.getActionId(), "MB_MAX_TRANSACTION_LIMIT")));
			 LOG.debug("HBL::isValidLimitRange:masterMap:DAILY_LIMIT:"+ masterMap.get(constructKey(limit.getActionId(), "MB_DAILY_LIMIT")));
			 LOG.debug("HBL::isValidLimitRange:masterMap:WEEKLY_LIMIT:"+ masterMap.get(constructKey(limit.getActionId(), "MB_WEEKLY_LIMIT")));
	        if (limit.getMaxMbTransactionLimit() > masterMap.get(constructKey(limit.getActionId(), "MB_MAX_TRANSACTION_LIMIT"))
	        		|| limit.getMaxMbTransactionLimit() < limit.getMinMBTransactionLimit() || limit.getMaxMbTransactionLimit() >limit.getDailyMBLimit()
	                || limit.getMaxMbTransactionLimit() > limit.getWeeklyMBLimit()) {
	            return false;
	        } else if (limit.getDailyMBLimit() > masterMap.get(constructKey(limit.getActionId(), "MB_DAILY_LIMIT"))
	        		|| limit.getDailyMBLimit() < limit.getMaxMbTransactionLimit() || limit.getDailyMBLimit() > limit.getWeeklyMBLimit()) {    	
	            return false;
	        } else if (limit.getWeeklyMBLimit() > masterMap.get(constructKey(limit.getActionId(), "MB_WEEKLY_LIMIT"))
	        		|| limit.getWeeklyMBLimit() < limit.getDailyMBLimit() || limit.getWeeklyMBLimit() < limit.getMaxMbTransactionLimit() ) {
	            return false;
	        }
	        return true;
	    }

	    private String constructKey(String action, String limit) {
	        return action + "." + limit;
	    }
	    private ServiceDefinitionDTO ValidateRequest(ServiceDefinitionDTO serviceDefinitionDTO,
	            DataControllerRequest request, List<String> existinglist) {
	    	String companyLegalUnit = request.getParameter("legalEntityId");
	        try {
	            JSONArray features = new JSONArray(request.getParameter("featureactions"));
	            if (StringUtils.isBlank(serviceDefinitionDTO.getName())
	                    || CommonUtilities.containAnySpecialCharacters(serviceDefinitionDTO.getName())) {
	                LOG.error("Name is mandatory. Please use allowed characters only.");
	                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21917);
	                return serviceDefinitionDTO;
	            } else if (StringUtils.isBlank(serviceDefinitionDTO.getDescription())
	                    || CommonUtilities.containAnySpecialCharactersForDescription(serviceDefinitionDTO.getDescription())) {
	                LOG.error("Description is mandatory. Please use allowed characters only.");
	                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21919);
	                return serviceDefinitionDTO;
	            } else if (StringUtils.isBlank(serviceDefinitionDTO.getServiceType())
	                    || (!serviceDefinitionDTO.getServiceType().equalsIgnoreCase("TYPE_ID_BUSINESS")
	                            && !serviceDefinitionDTO.getServiceType().equalsIgnoreCase("TYPE_ID_RETAIL")
	                            && !serviceDefinitionDTO.getServiceType().equalsIgnoreCase("TYPE_ID_WEALTH"))) {
	                LOG.error("serviceType should be TYPE_ID_RETAIL or TYPE_ID_BUSINESS or TYPE_ID_WEALTH");
	                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21921);
	                return serviceDefinitionDTO;
	            } else if (StringUtils.isBlank(serviceDefinitionDTO.getStatus())
	                    || (!serviceDefinitionDTO.getStatus().equalsIgnoreCase("SID_ACTIVE")
	                            && !serviceDefinitionDTO.getStatus().equalsIgnoreCase("SID_INACTIVE"))) {
	                LOG.error("status should be SID_ACTIVE or SID_INACTIVE");
	                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21915);
	                return serviceDefinitionDTO;
	            } else if (StringUtils.isBlank(request.getParameter("featureactions")) || features.length() == 0) {
	                LOG.error("Feature actions cannot be empty");
	                serviceDefinitionDTO.setErrCode(ErrorCodeEnum.ERR_21922);
	                return serviceDefinitionDTO;
	            } else if (!existinglist.isEmpty() && StringUtils.isNotBlank(serviceDefinitionDTO.getDefaultGroup())
	                    && !existinglist.contains(serviceDefinitionDTO.getDefaultGroup())) {
	                LOG.error("Default Group Id found in request is not a valid customer group");
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
							LOG.error("Failed to edit the service definition actions limits");
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
						LOG.error("Failed to update the service definition service definition actions  ");
						resultMap.put(featureActions, false);
						return resultMap;
					}
				}
			}
			if (isImmediate) {
				try {
					ThreadExecutor.execute(() -> callApi(serviceDefinitionId, request));
				} catch (Exception e) {
					LOG.error("Error while calling addNewFeaturesToContractFromSD ",e);
				}
			}
			resultMap.put(featureActions, true);
			return resultMap;
		}
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
	    private boolean removeServiceDefinitionActions(List<String> removeList, HashMap<String, ServiceDefinitionActionLimitDTO> existingMap) {
	        ServiceDefinitionActionLimitDTO actionLimitDTO = new ServiceDefinitionActionLimitDTO();
	        boolean status = false;
	        for (String s : removeList) {
	            actionLimitDTO = new ServiceDefinitionActionLimitDTO();
	            actionLimitDTO.setId(existingMap.get(s).getId());
	            status = serviceDefinitionBusinessDelegate.deleteServiceDefinitionActionLimit(actionLimitDTO);
	            if (!status) {
	                LOG.error("Failed to update the service definition actions");
	                return false;
	            }
	        }
	        return true;
	    }
	    private boolean isValidName(List<ServiceDefinitionDTO> serviceList, String name) {
	        List<String> names = serviceList.stream().map(ServiceDefinitionDTO::getName).collect(Collectors.toList());
	        if (names.contains(name)) {
	            return false;
	        }
	        return true;
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
				LOG.error("Exception occured while fetching configurations ", e);
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
				LOG.debug("respoonse from addNewFeaturesToContractFromSD "+res);
			} catch (DBPApplicationException e) {
				LOG.error(e);
				return false;
			}

			return true;

		}
	    @Override
		public Result getServiceDefinitionMonetaryActions(String methodId, Object[] inputArray,
				DataControllerRequest request, DataControllerResponse response) {
			Result result = new Result();
	        Map<String,String> minactionlimits= new HashMap<String, String>();
	        Map<String,String> mb_minactionlimits= new HashMap<String, String>();

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
	            	if(action.getLimitTypeId().equalsIgnoreCase("MIN_TRANSACTION_LIMIT"))
	            	minactionlimits.put(action.getActionId(), action.getValue());
	            	if(action.getLimitTypeId().equalsIgnoreCase("MB_MIN_TRANSACTION_LIMIT"))
	            		mb_minactionlimits.put(action.getActionId(), action.getValue());
	            });
	            result = convertServiceDefinitionFeatureLimits(groupActions,minactionlimits, mb_minactionlimits);
	            result.removeDatasetById("features");
	        } catch (Exception e) {
	            ErrorCodeEnum.ERR_20001.setErrorCode(result);
	            alert.prepareError("Exception occured in getServiceDefinitionMonetaryActions JAVA service. Error: ", e).log();
	        }

	        return result;
		}
	    private Result convertServiceDefinitionFeatureLimits(List<ServiceDefinitionFeatureActionViewDTO> groupActions, Map<String,String> minactionlimits, Map<String,String> mb_minactionlimits) {
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
	            result = getFormattedResult(features,minactionlimits, mb_minactionlimits);
	        } catch (Exception e) {
	            alert.prepareError("Failed to fetch common features and actions list", e).log();
	            ErrorCodeEnum.ERR_22031.setErrorCode(result);
	        }
	        return result;
	    }
	    private Result getFormattedResult(Map<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> features, Map<String,String> minactionlimits, Map<String, String> mb_minactionlimits) {
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
	                    	Record mbminLimit = new Record();
	                        minLimit.addParam(new Param("id", "MIN_TRANSACTION_LIMIT"));
	                        minLimit.addParam(new Param("value", minactionlimits.get(a.getKey())));
	                        mbminLimit.addParam(new Param("id", "MB_MIN_TRANSACTION_LIMIT"));
	                        mbminLimit.addParam(new Param("value", mb_minactionlimits.get(a.getKey())));
	                        limits.addRecord(minLimit);
	                        limits.addRecord(mbminLimit);
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

}
