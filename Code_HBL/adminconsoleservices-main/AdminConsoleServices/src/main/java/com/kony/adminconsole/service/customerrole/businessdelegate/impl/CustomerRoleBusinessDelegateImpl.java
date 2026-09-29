package com.kony.adminconsole.service.customerrole.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.customerrole.businessdelegate.api.CustomerRoleBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.customerrole.dto.CustomerGroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupActionLimitDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupFeatureActionViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupServiceDefinitionDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.MemberGroupDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class CustomerRoleBusinessDelegateImpl implements CustomerRoleBusinessDelegate{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@Override
	public  List<GroupFeatureActionViewDTO> getGroupFeatureActions(String  groupId, String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUP_FEATURES_ACTIONS_VIEW_PROC;
        List<GroupFeatureActionViewDTO> groupfeaturedtos = null;
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("_groupId", groupId);
        requestParameters.put("_companyLegalUnit", companyLegalUnit);
		
		String groupActionLimitResponse = null;
		JSONArray jsonArray = null;
		try {
			groupActionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupActionLimitResponse);
			jsonArray = responseObj.optJSONArray("records");
		    groupfeaturedtos = JSONUtils.parseAsList(jsonArray.toString(), GroupFeatureActionViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch action limits from service definition table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return groupfeaturedtos;
       
	}

	@Override
	public List<GroupsViewDTO> getAllGroups(String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPS_VIEW_GET;
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        if(companyLegalUnit!= null && companyLegalUnit.length()>0) {
		String filter = "companyLegalUnit eq "+ companyLegalUnit;
		requestParameters.put("$filter", filter);
        }
		String groupsResponse = null;
		JSONArray jsonArray = null;
		List<GroupsViewDTO> memberGroupDTOs=null;
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupsResponse);
		    jsonArray = responseObj.optJSONArray("groups_view");
		    memberGroupDTOs = JSONUtils.parseAsList(jsonArray.toString(), GroupsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch groups: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return memberGroupDTOs;
	}
	
	@Override
	public List<GroupServiceDefinitionDTO> getServiceDefinitionsForGroup(String groupId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPSERVICEDEFINITION_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Group_id eq "+ groupId;
		requestParameters.put("$filter", filter);
		
		List<GroupServiceDefinitionDTO> groupservicedefinitiondtos= null;
		String groupsResponse = null;
		JSONArray jsonArray = null;
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupsResponse);
		    jsonArray = responseObj.optJSONArray("groupservicedefinition");
		    groupservicedefinitiondtos =  JSONUtils.parseAsList(jsonArray.toString(), GroupServiceDefinitionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch groups: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return groupservicedefinitiondtos;
	}

	@Override
	public MemberGroupDTO createGroup(MemberGroupDTO memberGroupdto) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_MEMBERGROUP_CREATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(memberGroupdto).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		requestParameters.put("companyLegalUnit", memberGroupdto.getCompanyLegalUnit());		
		requestParameters.put("Name", memberGroupdto.getName());
		requestParameters.put("Description", memberGroupdto.getDescription());
		requestParameters.put("Type_id", memberGroupdto.getTypeId());
		requestParameters.put("Status_id", memberGroupdto.getStatus());
		requestParameters.put("createdby", memberGroupdto.getCreatedBy());
		
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("membergroup");
			memberGroupdto = JSONUtils.parse(responseArray.getJSONObject(0).toString(), MemberGroupDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create Customer group: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create GroupActionLimit method: " + e).log();
			return null;
		}

		return memberGroupdto;
	}

	@Override
	public GroupActionLimitDTO editGroupActionLimit(GroupActionLimitDTO actionLimitDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPACTIONLIMIT_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionLimitDTO).toString(), String.class, Object.class);
			if(!StringUtils.isBlank(actionLimitDTO.getSoftDeleteFlag())) {
				requestParameters.put("softdeleteflag",actionLimitDTO.getSoftDeleteFlag());
			}
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		requestParameters.put("companyLegalUnit", actionLimitDTO.getCompanyLegalUnit());
		requestParameters.put("Group_id", actionLimitDTO.getGroupId());
		requestParameters.put("Action_id", actionLimitDTO.getActionId());
		requestParameters.put("LimitType_id", actionLimitDTO.getLimitTypeId());
		requestParameters.put("createdby", actionLimitDTO.getCreatedBy());
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("groupactionlimit");
			actionLimitDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), GroupActionLimitDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create Customer group: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update GroupActionLimit method: " + e).log();
			return null;
		}

		return actionLimitDTO;
	}

	@Override
	public GroupActionLimitDTO createGroupActionLimit(GroupActionLimitDTO actionLimitDTO) {
		String serviceName = ServiceId.CRUDLAYER;
			String operationName = OperationName.DB_GROUPACTIONLIMIT_CREATE;

			Map<String, Object> requestParameters =  new HashMap<String, Object>();
			
			try {
				requestParameters = JSONUtils.parseAsMap(new JSONObject(actionLimitDTO).toString(), String.class, Object.class);
			} catch (IOException e) {
				alert.prepareError("Error occured while fetching the input params: " + e).log();
				return null;
			}
			
			requestParameters.put("companyLegalUnit", actionLimitDTO.getCompanyLegalUnit());
			requestParameters.put("Group_id", actionLimitDTO.getGroupId());
			requestParameters.put("Action_id", actionLimitDTO.getActionId());
			  requestParameters.put("LimitType_id", actionLimitDTO.getLimitTypeId());
			  requestParameters.put("createdby", actionLimitDTO.getCreatedBy());
			 
			
			
			String response = null;
			try {
				response = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(requestParameters).
						build().getResponse();
				JSONObject responseObj = new JSONObject(response);
				JSONArray responseArray = responseObj.getJSONArray("groupactionlimit");
				actionLimitDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), GroupActionLimitDTO.class);
			} catch (JSONException e) {
				alert.prepareError("Unable to Create service definition: " + e).log();
				return null;
			} catch (Exception e) {
				alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
				return null;
			}

			return actionLimitDTO;
		}
	
	@Override
	public boolean deleteGroup(MemberGroupDTO memberGroupdto) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_MEMBERGROUP_DELETE;
	        
        Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(memberGroupdto).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return false;
		}
		
		requestParameters.put("id", requestParameters.get("id"));
		requestParameters.put("companyLegalUnit", memberGroupdto.getCompanyLegalUnit());
		
		String deleteResponse = null;
		try {
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete from member group table: " + e).log();
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deleteGroup: " + e).log();
			return false;
		}
		
		return false;
	}

	@Override
	public List<FeatureActionRoleTypeDTO> getValidRoleTypeActions(String roleTypeId, String companyLegalUnit) {
		List<FeatureActionRoleTypeDTO> featureActionRoleTypeDTOs = null;
		
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATUREACTIONROLETYPE_GET;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "RoleType_id eq '"+ roleTypeId + "'";
		filter += " and ";
		filter += "companyLegalUnit eq '" + companyLegalUnit + "'";
		requestParameters.put("$filter", filter);
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		
		String serviceDefinitionResponse = null;
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("featureactionroletype");
			featureActionRoleTypeDTOs = JSONUtils.parseAsList(jsonArray.toString(), FeatureActionRoleTypeDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch feature actions from featureactionroletype table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getValidRoleTypeActions: " + e).log();
			return null;
		}
		
		return featureActionRoleTypeDTOs;
	}

	@Override
	public List<ActionLimitDTO> fetchActionLimits(String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLIMIT_GET;
       
        List<ActionLimitDTO> actionLimitDTOs = null;
		String actionLimitResponse = null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "companyLegalUnit eq '" + companyLegalUnit + "'";
		requestParameters.put("$filter", filter);
		
		try {
			actionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(actionLimitResponse);
			JSONArray jsonArray = responseObj.optJSONArray("actionlimit");
			actionLimitDTOs = JSONUtils.parseAsList(jsonArray.toString(), ActionLimitDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch action limits from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchActionLimits: " + e).log();
			return null;
		}
		
		return actionLimitDTOs;
	}

	@Override
	public List<ServiceDefinitionDTO> getServiceDefinitionsByType(String roleType, String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_VIEW_GET;
        Map<String, Object> requestParameters = new HashMap<String, Object>();
        String filter = "serviceType eq '"+ roleType + "'";
		filter += " and ";
		filter += "companyLegalUnit eq '" + companyLegalUnit + "'";
		requestParameters.put("$filter", filter);
        List<ServiceDefinitionDTO> serviceDefinitionDTOs = null;
		String serviceDefinitionResponse = null;
		
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("servicedefinition_view");
			serviceDefinitionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch service definitions from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
			return null;
		}
		
		return serviceDefinitionDTOs;
	}

	@Override
	public GroupServiceDefinitionDTO createGroupServiceDefinition(GroupServiceDefinitionDTO groupServiceDefinition) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPSERVICEDEFINITION_CREATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(groupServiceDefinition).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		requestParameters.put("companyLegalUnit", groupServiceDefinition.getCompanyLegalUnit());
		requestParameters.put("Group_id", groupServiceDefinition.getGroupId());
		requestParameters.put("createdby", groupServiceDefinition.getCreatedBy());

		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("groupservicedefinition");
			groupServiceDefinition = JSONUtils.parse(responseArray.getJSONObject(0).toString(), GroupServiceDefinitionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
			return null;
		}

		return groupServiceDefinition;
	}
	
	@Override
	public boolean editDefaultGroupServiceDefinition(GroupServiceDefinitionDTO groupServiceDefinition) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_DEFAULTGROUP_UPDATE_PROC;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		requestParameters.put("_companyLegalUnit", groupServiceDefinition.getCompanyLegalUnit());
		requestParameters.put("_serviceDefinitionId", groupServiceDefinition.getServiceDefinitionId());
		requestParameters.put("_groupId", groupServiceDefinition.getGroupId());
		requestParameters.put("_isDefault", groupServiceDefinition.getIsDefaultGroup());

		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			if((responseObj != null)
					&& responseObj.has(FabricConstants.OPSTATUS)
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0) {
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return false;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
			return false;
		}
	}
	
	
	@Override
	public List<ServiceDefinitionDTO> getServiceDefinitions() {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_VIEW_GET;
        List<ServiceDefinitionDTO> serviceDefinitionDTOs = null;
		String serviceDefinitionResponse = null;
			
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("servicedefinition_view");
			serviceDefinitionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch service definitions from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
			return null;
		}
		
		return serviceDefinitionDTOs;
	}
	
	
	@Override
	public List<ServiceDefinitionDTO> getServiceDefinitions(String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_VIEW_GET;
        List<ServiceDefinitionDTO> serviceDefinitionDTOs = null;
		String serviceDefinitionResponse = null;
		HashMap<String, Object> params = new HashMap<String,Object>();

		if(companyLegalUnit!=null&& !companyLegalUnit.equals("")) {
			params.put(ODataQueryConstants.FILTER, "companyLegalUnit eq "+companyLegalUnit);
		}
		
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(params).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("servicedefinition_view");
			serviceDefinitionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch service definitions from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
			return null;
		}
		
		return serviceDefinitionDTOs;
	}

	@Override
	public MemberGroupDTO editGroup(MemberGroupDTO groupdto) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_MEMBERGROUP_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(groupdto).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		requestParameters.put("Name", groupdto.getName());
		requestParameters.put("Description", groupdto.getDescription());
		requestParameters.put("Status_id", groupdto.getStatus());
		requestParameters.put("Type_id", groupdto.getTypeId());
		requestParameters.put("modifiedby", groupdto.getModifiedBy());
		requestParameters.put("companyLegalUnit", groupdto.getCompanyLegalUnit());
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("membergroup");
			groupdto = JSONUtils.parse(responseArray.getJSONObject(0).toString(), MemberGroupDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to edit customer group : " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at edit customer group : " + e).log();
			return null;
		}

		return groupdto;
	}

	@Override
	public List<GroupActionLimitDTO> getGroupActionLimit(MemberGroupDTO memberGroupdto) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPACTIONLIMIT_GET;
       
        List<GroupActionLimitDTO> groupdtos = null;
		String serviceResponse = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Group_id eq "+ memberGroupdto.getId();
		requestParameters.put("$filter", filter);
		
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceResponse);
			JSONArray jsonArray = responseObj.optJSONArray("groupactionlimit");
			groupdtos = JSONUtils.parseAsList(jsonArray.toString(), GroupActionLimitDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch member group  table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAllGroups: " + e).log();
			return null;
		}
		
		return groupdtos;
	}

	@Override
	public GroupsViewDTO getGroupById(String id) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPS_VIEW_GET;
		
		String groupsResponse = null;
		JSONArray jsonArray = null;
		GroupsViewDTO memberGroupDTO=null;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Group_id eq "+id;
		requestParameters.put("$filter", filter);
		
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupsResponse);
		    jsonArray = responseObj.optJSONArray("groups_view");
		    memberGroupDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), GroupsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch groups: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at get groups_view: " + e).log();
			return null;
		}
		
		return memberGroupDTO;
	}

	@Override
	public boolean deleteGroupActionLimit(GroupActionLimitDTO actionLimitDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPACTIONLIMIT_DELETE;
	        
        Map<String, Object> requestParameters= new HashMap<String, Object>();
        
		requestParameters.put("id", actionLimitDTO.getId());
		String deleteResponse = null;
		try {
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete service definition table: " + e).log();
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deleteServiceDefinition: " + e).log();
			return false;
		}
		
		return false;
	}

	@Override
	public boolean deleteGroupServiceDefinition(GroupServiceDefinitionDTO groupservicedto) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPSERVICEDEFINITION_DELETE;
	        
        Map<String, Object> requestParameters= new HashMap<String, Object>();
        
		requestParameters.put("Group_id", groupservicedto.getGroupId());
		requestParameters.put("serviceDefinitionId", groupservicedto.getServiceDefinitionId());
		String deleteResponse = null;
		try {
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if(jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
				return true;
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete service definition table: " + e).log();
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deleteServiceDefinition: " + e).log();
			return false;
		}
		
		return false;
	}

	@Override
	public List <GroupServiceDefinitionDTO> fetchGroupServiceDefinitions(String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPSERVICEDEFINITION_GET;

		List <GroupServiceDefinitionDTO> groupservicedtos= null;

		String response = null;
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		String filter = "companyLegalUnit eq " +companyLegalUnit;
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("groupservicedefinition");
			groupservicedtos = JSONUtils.parseAsList(responseArray.toString(), GroupServiceDefinitionDTO.class);

		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
			return null;
		}

		return groupservicedtos;
	}

	@Override
	public List<GroupsViewDTO> fetchGroupsViewWithFilter(String filter) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPS_VIEW_GET;
        
        Map<String, Object> requestParameters= new HashMap<String, Object>();
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		String groupsResponse = null;
		JSONArray jsonArray = null;
		List<GroupsViewDTO> memberGroupDTOs=null;
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupsResponse);
		    jsonArray = responseObj.optJSONArray("groups_view");
		    memberGroupDTOs = JSONUtils.parseAsList(jsonArray.toString(), GroupsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch groups: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return memberGroupDTOs;
	}

	@Override
	public boolean checkGroupServicedefinitionAssociation(String serviceId, String groupId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPSERVICEDEFINITION_GET;
        
        JSONArray jsonArray = null;
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Group_id eq "+ groupId + " and serviceDefinitionId eq "+ serviceId;
		requestParameters.put("$filter", filter);
		
		String groupsResponse = null;
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupsResponse);
			jsonArray = responseObj.optJSONArray("groupservicedefinition");
			if((responseObj != null)
					&& responseObj.has(FabricConstants.OPSTATUS)
					&& responseObj.getInt(FabricConstants.OPSTATUS) == 0 && jsonArray.length()>0)  {
				return true;
			}else {
				return false;
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch groups: " + e).log();
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return false;
		}
		
	}

	@Override
	public List<CustomerGroupsViewDTO> getCustomerGroupsView(String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_CUSTOMERGROUPSVIEW_GET;
		
		String groupsResponse = null;
		JSONArray jsonArray = null;
		List<CustomerGroupsViewDTO> customergroupsviewDTOs=null;
		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		String filter = "companyLegalUnit eq " +companyLegalUnit;
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		try {
			groupsResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(groupsResponse);
		    jsonArray = responseObj.optJSONArray("customergroups_view");
		    customergroupsviewDTOs = JSONUtils.parseAsList(jsonArray.toString(), CustomerGroupsViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch groups: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return customergroupsviewDTOs;
	}

}