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
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.hbl.adminconsoleextn.dto.LimitsDTOExtn;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.service.customerrole.businessdelegate.api.CustomerRoleBusinessDelegate;
import com.kony.adminconsole.service.customerrole.dto.CustomerGroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupActionLimitDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupServiceDefinitionDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.MemberGroupDTO;
import com.kony.adminconsole.service.customerrole.resource.impl.CustomerRoleResourceImpl;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.servicedefinition.dto.LimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CustomerRoleResourceImplExtn extends CustomerRoleResourceImpl {
	private static final Logger LOG = Logger.getLogger(CustomerRoleResourceImplExtn.class);
	CustomerRoleBusinessDelegate customerRoleBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerRoleBusinessDelegate.class);

	ServiceDefinitionBusinessDelegate servicedefinitionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);

	JSONArray featureActions = new JSONArray();

	@Override
	public Result editGroup(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		MemberGroupDTO groupdto = new MemberGroupDTO();

		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		String id = inputParams.get("id");
		String roleType = inputParams.get("typeId");
		String legalEntityId = inputParams.get("legalEntityId");
		if (StringUtils.isBlank(id)) {
			LOG.error("Id cannot be null ");
			return ErrorCodeEnum.ERR_21858.setErrorCode(new Result());
		}

		String userId = StringUtils.EMPTY;

		try {
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
			if (userDetailsBeanInstance != null) {
				userId = userDetailsBeanInstance.getId();
			}
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_21977.setErrorCode(new Result());
		}
		inputParams.put("modifiedBy", userId);
		inputParams.put("companyLegalUnit", legalEntityId);

		try {
			groupdto = JSONUtils.parse(new JSONObject(inputParams).toString(), MemberGroupDTO.class);
		} catch (IOException e) {
			LOG.error("Caught exception while converting input params to DTO: ", e);
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}

		try {

			groupdto = ValidateRequest(groupdto, request);
			if (groupdto.getErrCode() != null) {
				return groupdto.getErrCode().setErrorCode(new Result());
			}

			boolean actionLimitStatus = false;
			boolean serviceStatus = false;

			JSONArray addedActions = new JSONArray();
			JSONArray addedServices = new JSONArray();
			try {
				addedActions = new JSONArray(request.getParameter("featureactions"));
				addedServices = new JSONArray(request.getParameter("servicedefinitions"));

			} catch (Exception e) {
				LOG.error("Caught exception while converting input params to DTO: ", e);
				return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
			}

			actionLimitStatus = validateActions(groupdto.getTypeId(), addedActions, legalEntityId);
			if (actionLimitStatus == false) {
				LOG.error("Invalid actions or limits not in the given range");
				return ErrorCodeEnum.ERR_21925.setErrorCode(new Result());
			}

			serviceStatus = validateServiceDefinition(addedServices, roleType, legalEntityId);
			if (serviceStatus == false) {
				LOG.error("Invalid Service Definitions found in Request");
				return ErrorCodeEnum.ERR_21949.setErrorCode(new Result());
			}

			groupdto.setSoftDeleteFlag(null);

			groupdto = customerRoleBusinessDelegate.editGroup(groupdto);
			if (groupdto == null) {
				LOG.error("Failed to edit Customer Group: ");
				return ErrorCodeEnum.ERR_21981.setErrorCode(new Result());
			}

			HashMap<String, GroupActionLimitDTO> existingMap = getExistingActionMap(groupdto);
			Map<JSONArray, Boolean> resultMap = new HashMap<>();
			JSONArray featureActions = new JSONArray();
			resultMap = updateGroupActions(addedActions, id, existingMap, userId, legalEntityId);
			for (Map.Entry<JSONArray, Boolean> entry : resultMap.entrySet()) {
				featureActions = entry.getKey();
				actionLimitStatus = entry.getValue();
				break;
			}
			if (actionLimitStatus == false) {
				LOG.error("Failed to edit Customer Group ");
				return ErrorCodeEnum.ERR_21982.setErrorCode(new Result());
			}
			List<String> removeActionList = getRemoveActionList(addedActions, existingMap);
			actionLimitStatus = removeGroupActions(removeActionList, existingMap);
			if (actionLimitStatus == false) {
				LOG.error("Failed to edit Customer Group ");
				return ErrorCodeEnum.ERR_21982.setErrorCode(new Result());
			}
			result = updateGroupServiceMapping(id, addedServices, userId, legalEntityId);
			if (result.hasParamByName("dbpErrCode") || result.hasParamByName("dbpErrMsg")) {
				return result;
			}
			if (!limitsAndPermissionsUpdate(id, featureActions.toString(), removeActionList.toString(), request)) {
				LOG.debug("Runtime Exception.Exception Trace:");
				return ErrorCodeEnum.ERR_20001.setErrorCode(result);
			}

		} catch (Exception e) {
			LOG.debug("Runtime Exception.Exception Trace:", e);
			return ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		result = JSONToResult.convert(new JSONObject(groupdto).toString());
		result.addParam(new Param("legalEntityId", result.getParamValueByName("companyLegalUnit")));
		result.removeParamByName("companyLegalUnit");
		return result;
	}

	private boolean removeGroupActions(List<String> removeList, HashMap<String, GroupActionLimitDTO> existingMap) {
		GroupActionLimitDTO actionLimitDTO = new GroupActionLimitDTO();
		boolean status = true;
		for (String s : removeList) {
			actionLimitDTO = new GroupActionLimitDTO();
			actionLimitDTO.setId(existingMap.get(s).getId());
			status = customerRoleBusinessDelegate.deleteGroupActionLimit(actionLimitDTO);
			if (status == false) {
				LOG.error("Failed to update the service definition actions");
				return false;
			}
		}
		return true;
	}

	private Result updateGroupServiceMapping(String id, JSONArray addedServices, String userId, String legalEntityId) {
		Result result = new Result();
		try {
			List<GroupServiceDefinitionDTO> existingServices = customerRoleBusinessDelegate
					.getServiceDefinitionsForGroup(id);
			List<String> serviceIds = existingServices.stream().map(GroupServiceDefinitionDTO::getServiceDefinitionId)
					.collect(Collectors.toList());
			String serviceId = null;
			String isDefault = null;
			boolean defaultStatus = false;
			boolean status = false;
			List<String> removeservicelist = getRemoveServicesList(addedServices, serviceIds);
			GroupServiceDefinitionDTO groupservicedto = new GroupServiceDefinitionDTO();
			Map<String, GroupServiceDefinitionDTO> servicemap = new HashMap<>();
			for (GroupServiceDefinitionDTO servicedto : existingServices) {
				servicemap.put(servicedto.getServiceDefinitionId(), servicedto);
			}
			Map<String, Map<String, String>> CustomerCountmap = new HashMap<>();
			try {
				List<CustomerGroupsViewDTO> customergroups = customerRoleBusinessDelegate
						.getCustomerGroupsView(legalEntityId);

				for (CustomerGroupsViewDTO s : customergroups) {
					if (!CustomerCountmap.containsKey(s.getGroupId())) {
						Map<String, String> servicedefmap = new HashMap<>();
						servicedefmap.put(s.getServicedefinitionId(), s.getCustomerCount());
						CustomerCountmap.put(s.getGroupId(), servicedefmap);
					} else {
						Map<String, String> servicedefmap = CustomerCountmap.get(s.getGroupId());
						servicedefmap.put(s.getServicedefinitionId(), s.getCustomerCount());
					}

				}
			} catch (Exception e) {
				return ErrorCodeEnum.ERR_22012.setErrorCode(result);
			}

			groupservicedto.setCompanyLegalUnit(legalEntityId);
			List<GroupServiceDefinitionDTO> existingServiceAssociations = customerRoleBusinessDelegate
					.fetchGroupServiceDefinitions(groupservicedto.getCompanyLegalUnit());
			List<String> existingrolesmap = existingServiceAssociations.stream()
					.map(GroupServiceDefinitionDTO::getServiceDefinitionId).collect(Collectors.toList());

			// adds new role service definition association
			for (int ind = 0; ind < addedServices.length(); ind++) {
				serviceId = addedServices.getJSONObject(ind).getString("id");
				isDefault = addedServices.getJSONObject(ind).getString("isDefault");
				if (!serviceIds.contains(serviceId)) {
					groupservicedto.setGroupId(id);
					groupservicedto.setServiceDefinitionId(serviceId);
					groupservicedto.setCreatedBy(userId);

					// sets current role as default to service definition if there are no previous
					// roles associated
					if (existingrolesmap.contains(serviceId))
						groupservicedto.setIsDefaultGroup(isDefault);
					else
						groupservicedto.setIsDefaultGroup("1");

					groupservicedto.setCompanyLegalUnit(legalEntityId);
					groupservicedto = customerRoleBusinessDelegate.createGroupServiceDefinition(groupservicedto);
					if (groupservicedto == null) {
						return ErrorCodeEnum.ERR_22004.setErrorCode(result);
					}

					// updates existing default role to the current role
					if (existingrolesmap.contains(serviceId) && isDefault.equals("1")) {
						defaultStatus = customerRoleBusinessDelegate.editDefaultGroupServiceDefinition(groupservicedto);
						if (defaultStatus == false)
							return ErrorCodeEnum.ERR_22005.setErrorCode(result);
					}

				} else if (servicemap.containsKey(serviceId) && isDefault.equals("0")
						&& servicemap.get(serviceId).getIsDefaultGroup().equalsIgnoreCase("true")) {
					return ErrorCodeEnum.ERR_22009.setErrorCode(result);
				} else if (servicemap.containsKey(serviceId)
						&& !servicemap.get(serviceId).getIsDefaultGroup().equalsIgnoreCase(isDefault)) {

					groupservicedto.setGroupId(id);
					groupservicedto.setServiceDefinitionId(serviceId);
					groupservicedto.setIsDefaultGroup(isDefault);
					groupservicedto.setModifiedBy(userId);

					status = customerRoleBusinessDelegate.editDefaultGroupServiceDefinition(groupservicedto);
					if (status == false) {
						return ErrorCodeEnum.ERR_22005.setErrorCode(result);
					}

				}
			}

			// removes role service definition associations
			boolean removestatus = false;
			for (String sid : removeservicelist) {
				String count = null;
				if (CustomerCountmap.get(id) != null) {
					count = StringUtils.isBlank(CustomerCountmap.get(id).get(sid)) ? "0"
							: CustomerCountmap.get(id).get(sid);
				} else {
					count = "0";
				}
				if (servicemap.get(sid).getIsDefaultGroup().equalsIgnoreCase("false")) {
					groupservicedto.setGroupId(id);
					groupservicedto.setServiceDefinitionId(sid);
					removestatus = customerRoleBusinessDelegate.deleteGroupServiceDefinition(groupservicedto);
					if (removestatus == false) {
						return ErrorCodeEnum.ERR_22007.setErrorCode(result);
					}
				} else if (servicemap.get(sid).getIsDefaultGroup().equalsIgnoreCase("true")) {
					return ErrorCodeEnum.ERR_22006.setErrorCode(result);
				} else if (Integer.parseInt(count) > 0) {
					return ErrorCodeEnum.ERR_22008.setErrorCode(result);
				}
			}
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_21980.setErrorCode(result);
		}

		return result;
	}

	private List<String> getRemoveServicesList(JSONArray services, List<String> existingMap) {
		List<String> removeServiceList = new ArrayList<>();

		for (int i = 0; i < services.length(); i++) {

			String serviceId = services.getJSONObject(i).getString("id");
			removeServiceList.add(serviceId);
		}

		List<String> li = new ArrayList<>(existingMap);
		li.removeAll(removeServiceList);
		return li;
	}

	private boolean limitsAndPermissionsUpdate(String id, String actions, String removedActions,
			DataControllerRequest request) throws DBPAuthenticationException {
		DBPServices.updateCustomerRoleLimitsAndPermissions(id, actions, removedActions, request);
		return true;
	}

	private List<String> getRemoveActionList(JSONArray actionlimits, HashMap<String, GroupActionLimitDTO> existingMap) {
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

	private String constructKey(String action, String limit) {
		return action + "." + limit;
	}

	private Map<JSONArray, Boolean> updateGroupActions(JSONArray actionlimits, String groupId,
			HashMap<String, GroupActionLimitDTO> existingMap, String userId, String companyLegalUnit) {
		GroupActionLimitDTO actionLimitDTO = new GroupActionLimitDTO();

		boolean flag = false;
		JSONArray featureActions = new JSONArray();
		Map<JSONArray, Boolean> resultMap = new HashMap<>();
		for (int i = 0; i < actionlimits.length(); i++) {
			String actionId = actionlimits.getJSONObject(i).getString("id");
			boolean isNewAction = false;
			try {
				isNewAction = actionlimits.getJSONObject(i).has("isNewAction")
						? actionlimits.getJSONObject(i).getBoolean("isNewAction")
						: false;
			} catch (Exception e) {
				isNewAction = false;
			}

			actionLimitDTO = new GroupActionLimitDTO();
			actionLimitDTO.setIsNewAction(isNewAction);
			actionLimitDTO.setCompanyLegalUnit(companyLegalUnit);

			if (actionlimits.getJSONObject(i).has("limits")
					&& (actionlimits.getJSONObject(i).getJSONArray("limits").length() > 0)) {
				JSONArray limits = actionlimits.getJSONObject(i).getJSONArray("limits");

				for (int j = 0; j < limits.length(); j++) {
					actionLimitDTO.setGroupId(groupId);
					actionLimitDTO.setActionId(actionId);
					actionLimitDTO.setLimitTypeId(limits.getJSONObject(j).getString("id"));
					String key = constructKey(actionId, actionLimitDTO.getLimitTypeId());
					actionLimitDTO.setValue(limits.getJSONObject(j).getString("value"));
					if (existingMap.containsKey(key)) {
						if (existingMap.get(key).getValue() != actionLimitDTO.getValue()) {
							if (Float.parseFloat(existingMap.get(key).getValue()) > Float
									.parseFloat(actionLimitDTO.getValue())) {
								flag = true;
							}
							actionLimitDTO.setModifiedBy(userId);
							actionLimitDTO.setId(existingMap.get(key).getId());
							actionLimitDTO = customerRoleBusinessDelegate.editGroupActionLimit(actionLimitDTO);
						}
					} else {
						actionLimitDTO.setCreatedBy(userId);
						actionLimitDTO.setId(String.valueOf(CommonUtilities.getNewId()));
						actionLimitDTO = customerRoleBusinessDelegate.createGroupActionLimit(actionLimitDTO);
					}
					if (actionLimitDTO == null) {
						LOG.error("Failed to add/edit the service definition actions limits");
						resultMap.put(featureActions, false);
						return resultMap;
					}
				}
				if (flag) {
					featureActions.put(actionlimits.getJSONObject(i));
					flag = false;
				}
			} else {
				actionLimitDTO.setGroupId(groupId);
				actionLimitDTO.setActionId(actionId);
				actionLimitDTO.setCreatedBy(userId);
				if (!existingMap.containsKey(actionId)) {
					actionLimitDTO.setId(String.valueOf(CommonUtilities.getNewId()));
					actionLimitDTO = customerRoleBusinessDelegate.createGroupActionLimit(actionLimitDTO);
				}
				if (actionLimitDTO == null) {
					LOG.error("Failed to update the service definition service definition actions  ");
					resultMap.put(featureActions, false);
					return resultMap;
				}
			}
		}

		resultMap.put(featureActions, true);
		return resultMap;
	}

	private HashMap<String, GroupActionLimitDTO> getExistingActionMap(MemberGroupDTO memberGroupdto) {
		List<GroupActionLimitDTO> actionList = customerRoleBusinessDelegate.getGroupActionLimit(memberGroupdto);

		HashMap<String, GroupActionLimitDTO> existingMap = new HashMap<>();
		for (GroupActionLimitDTO groupLimit : actionList) {
			String key = groupLimit.getActionId();
			if (!StringUtils.isBlank(groupLimit.getLimitTypeId())) {
				key = constructKey(key, groupLimit.getLimitTypeId());
			}
			existingMap.put(key, groupLimit);
		}

		return existingMap;
	}

	private boolean validateServiceDefinition(JSONArray serviceDefinitions, String roleType, String companyLegalUnit) {
		try {
			List<ServiceDefinitionDTO> validServicedtos = customerRoleBusinessDelegate
					.getServiceDefinitionsByType(roleType, companyLegalUnit);
			List<String> validServiceIds = validServicedtos.stream().map(ServiceDefinitionDTO::getId)
					.collect(Collectors.toList());

			if (validServicedtos.size() == 0)
				return false;

			for (int ind = 0; ind < serviceDefinitions.length(); ind++) {
				String serviceDefinitionId = serviceDefinitions.getJSONObject(ind).getString("id");
				if (!validServiceIds.contains(serviceDefinitionId)) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private MemberGroupDTO ValidateRequest(MemberGroupDTO memberGroupdto, DataControllerRequest request) {
		try {
			JSONArray services = new JSONArray(request.getParameter("servicedefinitions"));
			JSONArray features = new JSONArray(request.getParameter("featureactions"));
			;

			if (StringUtils.isBlank(memberGroupdto.getName())
					|| CommonUtilities.containAnySpecialCharacters(memberGroupdto.getName())) {
				LOG.error("Name is mandatory. Please use allowed characters only.");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21917);
				return memberGroupdto;
			} else if (StringUtils.isBlank(memberGroupdto.getDescription())
					|| CommonUtilities.containAnySpecialCharactersForDescription(memberGroupdto.getDescription())) {
				LOG.error("Description is mandatory. Please use allowed characters only.");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21919);
				return memberGroupdto;
			} else if (memberGroupdto.getDescription().length() > 250) {
				LOG.error("description cannot have more than 250 characters ");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22011);
				return memberGroupdto;
			} else if (memberGroupdto.getName().length() > 100) {
				LOG.error("Name cannot have more than 100 characters ");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22010);
				return memberGroupdto;
			} else if (StringUtils.isBlank(memberGroupdto.getTypeId())
					|| (!memberGroupdto.getTypeId().equalsIgnoreCase("TYPE_ID_BUSINESS")
							&& !memberGroupdto.getTypeId().equalsIgnoreCase("TYPE_ID_RETAIL")
							&& !memberGroupdto.getTypeId().equalsIgnoreCase("TYPE_ID_WEALTH"))) {
				LOG.error("roleType should be TYPE_ID_RETAIL or TYPE_ID_BUSINESS or TYPE_ID_WEALTH");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21951);
				return memberGroupdto;
			} else if (StringUtils.isBlank(memberGroupdto.getStatus())
					|| (!memberGroupdto.getStatus().equalsIgnoreCase("SID_ACTIVE")
							&& !memberGroupdto.getStatus().equalsIgnoreCase("SID_INACTIVE"))) {
				LOG.error("status should be SID_ACTIVE or SID_INACTIVE");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21915);
				return memberGroupdto;
			} else if (StringUtils.isBlank(request.getParameter("featureactions")) || features.length() == 0) {
				LOG.error("Feature actions cannot be empty");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21922);
				return memberGroupdto;
			} else if (StringUtils.isBlank(request.getParameter("servicedefinitions")) || services.length() == 0) {
				LOG.error("Service Definitions cannot be empty");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21948);
				return memberGroupdto;
			} else if (StringUtils.isBlank(request.getParameter("legalEntityId"))) {
				LOG.error("legalEntityId cannot be empty");
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22232);
				return memberGroupdto;
			}
		} catch (Exception e) {
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21908);
			return memberGroupdto;
		}

		// For Edit Scenario - Validations for type_id modifications
		if (StringUtils.isNotBlank(memberGroupdto.getId())) {
			GroupsViewDTO actualgroup = customerRoleBusinessDelegate.getGroupById(memberGroupdto.getId());
			if (actualgroup == null) {
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21979);
				return memberGroupdto;
			}
			if (!actualgroup.getTypeId().equals(memberGroupdto.getTypeId())) {
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21952);
				return memberGroupdto;
			}
			if (!actualgroup.getCompanyLegalUnit().equals(memberGroupdto.getCompanyLegalUnit())) {
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21957);
				return memberGroupdto;
			}
			if (Integer.parseInt(actualgroup.getCustomersCount()) > 0
					&& memberGroupdto.getStatus().equalsIgnoreCase("SID_INACTIVE")) {
				memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21978);
				return memberGroupdto;
			}
			if (memberGroupdto.getStatus().equalsIgnoreCase("SID_INACTIVE")) {
				List<GroupServiceDefinitionDTO> servicedeflist = customerRoleBusinessDelegate
						.getServiceDefinitionsForGroup(memberGroupdto.getId());
				if (servicedeflist != null) {
					for (int i = 0; i < servicedeflist.size(); i++) {
						if (servicedeflist.get(i).getIsDefaultGroup().equalsIgnoreCase("true")) {
							memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22083);
							return memberGroupdto;
						}
					}
					;
				}
			}
		}

		return memberGroupdto;
	}

	private boolean validateActions(String roleTypeId, JSONArray actionlimits, String companyLegalUnit) {
		List<FeatureActionRoleTypeDTO> validRoleTypeActions = customerRoleBusinessDelegate
				.getValidRoleTypeActions(roleTypeId, companyLegalUnit);
		if (validRoleTypeActions == null) {
			LOG.error("Failed to fetch featureactions valid for role type");
			return false;
		}
		List<String> validRoleTypeActionIds = validRoleTypeActions.stream().map(FeatureActionRoleTypeDTO::getActionId)
				.collect(Collectors.toList());
		boolean isVaild = true;
		List<ActionLimitDTO> actionLimitDTOs = customerRoleBusinessDelegate.fetchActionLimits(companyLegalUnit);
		if (actionLimitDTOs == null) {
			LOG.error("Failed to fetch actions limits");
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
			LOG.debug("HBL:CustomerRoleResourceImplExtn:actionId:"+actionId);
			LOG.debug("HBL:CustomerRoleResourceImplExtn:actionlimits:"+actionlimits);
			boolean hasLimits= actionlimits.getJSONObject(i).has("limits");
			
			LOG.debug("HBL:CustomerRoleResourceImplExtn:actionlimits.hasLimits:"+hasLimits);
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
				}else if (actionlimits.getJSONObject(i).has("limits")
						&& (actionlimits.getJSONObject(i).getJSONArray("limits").length() >6)) {
					removeMinTransactionLimit(actionlimits.getJSONObject(i).getJSONArray("limits"));
				}
				else {
					isVaild = setMBLimits(actionlimits, actions, masterMap, actionId, i);
				}
			}
		}

		return isVaild;
	}
	public JSONArray removeMinTransactionLimit(JSONArray limits) {
		for (int j = 0; j < limits.length(); j++) {
		JSONObject limitObj = limits.getJSONObject(j);
		String limitId = limitObj.getString("id");
		if(limitId.equalsIgnoreCase("MB_MIN_TRANSACTION_LIMIT")) {
			limits.remove(j);
		}
		if(limitId.equalsIgnoreCase("MIN_TRANSACTION_LIMIT")) {
			limits.remove(j);
		}
		}
		return limits;
	}

	public boolean setMBLimits(JSONArray actionlimits, Set<String> actions, HashMap<String, Double> masterMap,
			String actionId, int index) {
		LOG.debug("HBL:CustomerRoleResourceImplExtn:setMBLimits:");
		int length = actionlimits.getJSONObject(index).getJSONArray("limits").length();
		LOG.debug("HBL:CustomerRoleResourceImplExtn:validateMBActions: limits length:" + length);
		LOG.debug("HBL:CustomerRoleResourceImplExtn:validateMBActions:current  actionId:" + actionId);
		LOG.debug("HBL:CustomerRoleResourceImplExtn:validateMBActions: actions.contains(actionId):"
				+ actions.contains(actionId));
		if (actions.contains(actionId)) {
			if (actionlimits.getJSONObject(index).has("limits")
					&& (actionlimits.getJSONObject(index).getJSONArray("limits").length() == 6)) {
				JSONArray limits = actionlimits.getJSONObject(index).getJSONArray("limits");
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

	private boolean isValidMBLimitRange(LimitsDTOExtn limit, HashMap<String, Double> masterMap) {
		LOG.debug("HBL::isValidMBLimits:");
		LOG.debug("HBL::isValidLimitRange:getMaxTransactionLimit:" + limit.getMaxTransactionLimit());
		LOG.debug("HBL::isValidLimitRange:getDailyLimit:" + limit.getDailyLimit());
		LOG.debug("HBL::isValidLimitRange:getWeeklyLimit:" + limit.getWeeklyLimit());
		LOG.debug("HBL::isValidLimitRange:getMinTransactionLimit:" + limit.getMinTransactionLimit());
		LOG.debug("HBL::isValidLimitRange:masterMap:MAX_TRANSACTION_LIMIT:"
				+ masterMap.get(constructKey(limit.getActionId(), "MAX_TRANSACTION_LIMIT")));
		LOG.debug("HBL::isValidLimitRange:masterMap:DAILY_LIMIT:"
				+ masterMap.get(constructKey(limit.getActionId(), "DAILY_LIMIT")));
		LOG.debug("HBL::isValidLimitRange:masterMap:WEEKLY_LIMIT:"
				+ masterMap.get(constructKey(limit.getActionId(), "WEEKLY_LIMIT")));
		if (limit.getMaxTransactionLimit() > masterMap.get(constructKey(limit.getActionId(), "MAX_TRANSACTION_LIMIT"))
				|| limit.getMaxTransactionLimit() < limit.getMinTransactionLimit()
				|| limit.getMaxTransactionLimit() > limit.getDailyLimit()
				|| limit.getMaxTransactionLimit() > limit.getWeeklyLimit()) {
			return false;
		} else if (limit.getDailyLimit() > masterMap.get(constructKey(limit.getActionId(), "DAILY_LIMIT"))
				|| limit.getDailyLimit() < limit.getMaxTransactionLimit()
				|| limit.getDailyLimit() > limit.getWeeklyLimit()) {
			return false;
		} else if (limit.getWeeklyLimit() > masterMap.get(constructKey(limit.getActionId(), "WEEKLY_LIMIT"))
				|| limit.getWeeklyLimit() < limit.getDailyLimit()
				|| limit.getWeeklyLimit() < limit.getMaxTransactionLimit()) {
			return false;
		}
		LOG.debug("HBL::isValidMBLimits:");
		LOG.debug("HBL::isValidLimitRange:getMaxMbTransactionLimit:" + limit.getMaxMbTransactionLimit());
		LOG.debug("HBL::isValidLimitRange:getDailyMBLimit:" + limit.getDailyMBLimit());
		LOG.debug("HBL::isValidLimitRange:getWeeklyMBLimit:" + limit.getWeeklyMBLimit());
		LOG.debug("HBL::isValidLimitRange:getMinMBTransactionLimit:" + limit.getMinMBTransactionLimit());

		LOG.debug("HBL::isValidLimitRange:masterMap:MAX_TRANSACTION_LIMIT:"
				+ masterMap.get(constructKey(limit.getActionId(), "MB_MAX_TRANSACTION_LIMIT")));
		LOG.debug("HBL::isValidLimitRange:masterMap:DAILY_LIMIT:"
				+ masterMap.get(constructKey(limit.getActionId(), "MB_DAILY_LIMIT")));
		LOG.debug("HBL::isValidLimitRange:masterMap:WEEKLY_LIMIT:"
				+ masterMap.get(constructKey(limit.getActionId(), "MB_WEEKLY_LIMIT")));
		if (limit.getMaxMbTransactionLimit() > masterMap
				.get(constructKey(limit.getActionId(), "MB_MAX_TRANSACTION_LIMIT"))
				|| limit.getMaxMbTransactionLimit() < limit.getMinMBTransactionLimit()
				|| limit.getMaxMbTransactionLimit() > limit.getDailyMBLimit()
				|| limit.getMaxMbTransactionLimit() > limit.getWeeklyMBLimit()) {
			return false;
		} else if (limit.getDailyMBLimit() > masterMap.get(constructKey(limit.getActionId(), "MB_DAILY_LIMIT"))
				|| limit.getDailyMBLimit() < limit.getMaxMbTransactionLimit()
				|| limit.getDailyMBLimit() > limit.getWeeklyMBLimit()) {
			return false;
		} else if (limit.getWeeklyMBLimit() > masterMap.get(constructKey(limit.getActionId(), "MB_WEEKLY_LIMIT"))
				|| limit.getWeeklyMBLimit() < limit.getDailyMBLimit()
				|| limit.getWeeklyMBLimit() < limit.getMaxMbTransactionLimit()) {
			return false;
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

	private boolean isValidLimitRange(LimitDTO limit, HashMap<String, Double> masterMap) {
		if (limit.getMaxTransactionLimit() > masterMap.get(constructKey(limit.getActionId(), "MAX_TRANSACTION_LIMIT"))
				|| limit.getMaxTransactionLimit() < limit.getMinTransactionLimit()
				|| limit.getMaxTransactionLimit() > limit.getDailyLimit()
				|| limit.getMaxTransactionLimit() > limit.getWeeklyLimit()) {
			LOG.error("Maximum Transaction limit isn't within the specified range");
			return false;
		} else if (limit.getDailyLimit() > masterMap.get(constructKey(limit.getActionId(), "DAILY_LIMIT"))
				|| limit.getDailyLimit() < limit.getMaxTransactionLimit()
				|| limit.getDailyLimit() > limit.getWeeklyLimit()) {
			LOG.error("Daily Transaction limit isn't within the specified range");
			return false;
		} else if (limit.getWeeklyLimit() > masterMap.get(constructKey(limit.getActionId(), "WEEKLY_LIMIT"))
				|| limit.getWeeklyLimit() < limit.getDailyLimit()
				|| limit.getWeeklyLimit() < limit.getMaxTransactionLimit()) {
			LOG.error("Weekly Transaction limit isn't within the specified range");
			return false;
		}
		return true;
	}

}
