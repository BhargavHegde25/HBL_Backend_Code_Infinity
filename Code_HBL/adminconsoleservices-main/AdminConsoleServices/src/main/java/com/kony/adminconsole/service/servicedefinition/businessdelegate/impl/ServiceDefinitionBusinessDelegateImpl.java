package com.kony.adminconsole.service.servicedefinition.businessdelegate.impl;

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

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.servicedefinition.dto.MemberGroupDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionFeatureActionViewDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionGroupDTO;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;

public class ServiceDefinitionBusinessDelegateImpl implements ServiceDefinitionBusinessDelegate{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public boolean deleteServiceDefinition(ServiceDefinitionDTO serviceDefinitionDTO) {
		
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_DELETE_PROC;
	        
		Map<String, Object> requestParameters = null;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(serviceDefinitionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the request params: " + e).log();
			return false;
		}
		
		requestParameters.put("_servicedefinitionid", requestParameters.get("id"));
		
		String deleteResponse = null;
		try {
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject jsonRsponse = new JSONObject(deleteResponse);
			if (jsonRsponse != null && jsonRsponse.has(FabricConstants.OPSTATUS)
				&& jsonRsponse.getInt(FabricConstants.OPSTATUS) == 0) {
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
	public boolean isAssociatedToContract(String serviceDefinitionId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_CONTRACT_GET;
        
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "servicedefinitionId eq '" + serviceDefinitionId + "'";
		requestParameters.put(ODataQueryConstants.FILTER, filter);
		String serviceDefinitionResponse = null;
		JSONArray jsonArray = null;
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = CommonUtilities.getStringAsJSONObject(serviceDefinitionResponse);
			if (responseObj != null && responseObj.has(FabricConstants.OPSTATUS) && responseObj.getInt(FabricConstants.OPSTATUS) == 0
					&& responseObj.has("contract")) {
		    jsonArray = responseObj.optJSONArray("contract");
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch contract: " + e).log();
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at isAssociatedToContract : " + e).log();
			return false;
		}
		if (jsonArray != null && jsonArray.length() > 0) {
			alert.prepareError("More than one customer linked to service defintion id").log();
			return false;
		}
		return true;
	}

	@Override
	public List<ServiceDefinitionDTO> fetchAllServiceDefinition(String typeId, String companyLegalUnit) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_VIEW_PROC;
       
        List<ServiceDefinitionDTO> serviceDefinitionDTOs = null;
		String serviceDefinitionResponse = null;
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("_typeId", typeId);
		params.put("_companyLegalUnit", companyLegalUnit);
		
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(params).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("records");
			serviceDefinitionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch service definition from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
			return null;
		}
		
		return serviceDefinitionDTOs;
	}

	@Override
	public ServiceDefinitionDTO createServiceDefinition(ServiceDefinitionDTO serviceDefinitionDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_CREATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(serviceDefinitionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("servicedefinition");
			serviceDefinitionDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ServiceDefinitionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
			return null;
		}

		return serviceDefinitionDTO;
	}

	@Override
	public ServiceDefinitionActionLimitDTO createServiceDefinitionActionLimit(ServiceDefinitionActionLimitDTO serviceDefinitionActionLimitDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITIONACTIONLIMIT_CREATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(serviceDefinitionActionLimitDTO).toString(), String.class, Object.class);
            requestParameters.put("createdby",serviceDefinitionActionLimitDTO.getCreatedBy());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("servicedefinitionactionlimit");
			serviceDefinitionActionLimitDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ServiceDefinitionActionLimitDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
			return null;
		}

		return serviceDefinitionActionLimitDTO;
	}

	@Override
	public List<MemberGroupDTO> fetchAllGroups(String typeId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_MEMBERGROUP_GET;
       
        List<MemberGroupDTO> memberGroupDTOs = null;
		String memberGroupResponse = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "Type_id eq "+ typeId + " and isApplicabletoAllServices eq 1";
		requestParameters.put("$filter", filter);
		
		try {
			memberGroupResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(memberGroupResponse);
			JSONArray jsonArray = responseObj.optJSONArray("membergroup");
			memberGroupDTOs = JSONUtils.parseAsList(jsonArray.toString(), MemberGroupDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch member group  table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAllGroups: " + e).log();
			return null;
		}
		
		return memberGroupDTOs;
	}

	@Override
	public ServiceDefinitionGroupDTO createServiceDefinitionGroup(ServiceDefinitionGroupDTO serviceDefinitionGroupDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPSERVICEDEFINITION_CREATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(serviceDefinitionGroupDTO).toString(), String.class, Object.class);
			requestParameters.put("Group_id",serviceDefinitionGroupDTO.getGroupId());
			requestParameters.put("serviceDefinitionId",serviceDefinitionGroupDTO.getServiceDefinitionId() );
			requestParameters.put("createdby",serviceDefinitionGroupDTO.getCreatedBy());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
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
			serviceDefinitionGroupDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ServiceDefinitionGroupDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition group: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinitionGroup method: " + e).log();
			return null;
		}

		return serviceDefinitionGroupDTO;
	}

	@Override
	public List<ActionLimitDTO> fetchActionLimits(String filterQuery) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLIMIT_GET;
       
        List<ActionLimitDTO> actionLimitDTOs = null;
		String actionLimitResponse = null;
		HashMap<String, Object> filterParams = new HashMap<>();
		filterParams.put(ODataQueryConstants.FILTER, filterQuery);
		
		try {
			actionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(filterParams).
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
	public List<ServiceDefinitionFeatureActionViewDTO> getServiceDefinitionFeatureActions(String serviceDefinitionId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_FEATURES_ACTIONS_VIEW_GET;
       
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "serviceDefinitionId eq "+ serviceDefinitionId;
		requestParameters.put("$filter", filter);
		List<ServiceDefinitionFeatureActionViewDTO> serviceDefinitionFeaturesActionDTOs= null;
		
		String serviceDefinitionActionLimitResponse = null;
		JSONArray jsonArray = null;
		try {
			serviceDefinitionActionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionActionLimitResponse);
		    jsonArray = responseObj.optJSONArray("servicedefinition_features_actions_view");
		    serviceDefinitionFeaturesActionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionFeatureActionViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch action limits from service definition table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return serviceDefinitionFeaturesActionDTOs;
	}

	@Override
	public ServiceDefinitionDTO editServiceDefinition(ServiceDefinitionDTO serviceDefinitionDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(serviceDefinitionDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("servicedefinition");
			serviceDefinitionDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ServiceDefinitionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createServiceDefinition method: " + e).log();
			return null;
		}

		return serviceDefinitionDTO;
	}

	@Override
	public List<ServiceDefinitionActionLimitDTO> fetchServiceDefinitionActionLimit(ServiceDefinitionDTO serviceDefinitionDTO) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITIONACTIONLIMIT_GET;
       
        List<ServiceDefinitionActionLimitDTO> serviceDefinitionActionLimitDTOs = null;
		String serviceResponse = null;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "serviceDefinitionId eq "+ serviceDefinitionDTO.getId();
		requestParameters.put("$filter", filter);
		
		try {
			serviceResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceResponse);
			JSONArray jsonArray = responseObj.optJSONArray("servicedefinitionactionlimit");
			serviceDefinitionActionLimitDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionActionLimitDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch member group  table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchAllGroups: " + e).log();
			return null;
		}
		
		return serviceDefinitionActionLimitDTOs;
	}

	@Override
	public ServiceDefinitionActionLimitDTO editServiceDefinitionActionLimit(ServiceDefinitionActionLimitDTO serviceDefinitionActionLimitDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITIONACTIONLIMIT_UPDATE;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(serviceDefinitionActionLimitDTO).toString(), String.class, Object.class);
			if(!StringUtils.isBlank(serviceDefinitionActionLimitDTO.getSoftDeleteFlag())) {
				requestParameters.put("softdeleteflag",serviceDefinitionActionLimitDTO.getSoftDeleteFlag());
			}
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		
		String response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray responseArray = responseObj.getJSONArray("servicedefinitionactionlimit");
			serviceDefinitionActionLimitDTO = JSONUtils.parse(responseArray.getJSONObject(0).toString(), ServiceDefinitionActionLimitDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Unable to Create service definition: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at updateServiceDefinition method: " + e).log();
			return null;
		}

		return serviceDefinitionActionLimitDTO;
	}

	@Override
	public List<ServiceDefinitionDTO> fetchServiceDefinition() {
			String serviceName = ServiceId.CRUDLAYER;
	        String operationName = OperationName.DB_SERVICEDEFINITION_GET;
	       
	        List<ServiceDefinitionDTO> serviceDefinitionDTOs = null;
			String serviceDefinitionResponse = null;
			
			try {
				serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						build().getResponse();
				JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
				JSONArray jsonArray = responseObj.optJSONArray("servicedefinition");
				serviceDefinitionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionDTO.class);
			}
			catch (JSONException e) {
				alert.prepareError("Failed to fetch service definition from table: " + e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
				return null;
			}
			
			return serviceDefinitionDTOs;
		
	}

	@Override
	public List<FeatureActionRoleTypeDTO> getValidRoleTypeActions(String roleTypeId) {
		List<FeatureActionRoleTypeDTO> featureActionRoleTypeDTOs = null;
		
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_FEATUREACTIONROLETYPE_GET;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "RoleType_id eq "+ roleTypeId;
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
	public List<ServiceDefinitionGroupDTO> fetchAllRolesForServiceDefinition(String sid) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_GROUPSERVICEDEFINITION_GET;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
			String filter = "serviceDefinitionId eq "+ sid +" and softdeleteflag eq 0 ";
			requestParameters.put(ODataQueryConstants.FILTER, filter);
			List<ServiceDefinitionGroupDTO> serviceDefinitionGroupDTOs =null;
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
			serviceDefinitionGroupDTOs =JSONUtils.parseAsList(responseArray.toString(), ServiceDefinitionGroupDTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception while fetching roles : " + e).log();
			return null;
		}

		return serviceDefinitionGroupDTOs;
	}

	@Override
	public ServiceDefinitionDTO getServiceDefinitionById(String id) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_GET;
        Map<String, Object> requestParameters =  new HashMap<String, Object>();
		String filter = "id eq "+ id;
		requestParameters.put(ODataQueryConstants.FILTER, filter);
       
        ServiceDefinitionDTO serviceDefinitionDTO = null;
		String serviceDefinitionResponse = null;
		
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("servicedefinition");
			serviceDefinitionDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), ServiceDefinitionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch service definition from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
			return null;
		}
		
		return serviceDefinitionDTO;
	}

	@Override
	public ServiceDefinitionGroupDTO getDefaultRoleForServiceDefinition(String id) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_GROUPSERVICEDEFINITION_GET;
        Map<String, Object> requestParameters =  new HashMap<String, Object>();
		String filter = "isDefaultGroup eq 1 and serviceDefinitionId eq "+ id;
		requestParameters.put(ODataQueryConstants.FILTER, filter);
 
        ServiceDefinitionGroupDTO servgroupDTO = null;
		String serviceDefinitionResponse = null;
		
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionResponse);
			JSONArray jsonArray = responseObj.optJSONArray("groupservicedefinition");
			if(jsonArray!=null)
				servgroupDTO = JSONUtils.parse(jsonArray.getJSONObject(0).toString(), ServiceDefinitionGroupDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch default role for service definition : " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchGroupServiceDefinition: " + e).log();
			return null;
		}
		
		return servgroupDTO;
	}
	
	@Override
	public boolean editDefaultGroupServiceDefinition(ServiceDefinitionGroupDTO groupServiceDefinition) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_DEFAULTGROUP_UPDATE_PROC;

		Map<String, Object> requestParameters =  new HashMap<String, Object>();
		
		requestParameters.put("_serviceDefinitionId", groupServiceDefinition.getServiceDefinitionId());
		requestParameters.put("_groupId", groupServiceDefinition.getGroupId());
		requestParameters.put("_isDefault", "1");
		requestParameters.put("_companyLegalUnit",groupServiceDefinition.getCompanyLegalUnit());

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
	public boolean deleteServiceDefinitionActionLimit(ServiceDefinitionActionLimitDTO actionLimitDTO) {
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITIONACTIONLIMIT_DELETE;
	        
        Map<String, Object> requestParameters= new HashMap<String, Object>();
        
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(actionLimitDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return false;
		}
        
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
			alert.prepareError("Failed to delete service definition actions : " + e).log();
			return false;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at deleteServiceDefinition action : " + e).log();
			return false;
		}
		
		return false;
	}

	@Override
	public List<ServiceDefinitionDTO> fetchAllServiceDefinitionsForContract(String legalEntityId) {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_VIEW_GET;
        Map<String, Object> requestParameters= new HashMap<String, Object>();
		String filter = "status eq SID_ACTIVE and numberOfActiveRoles ne 0 and companyLegalUnit eq " + legalEntityId;
		requestParameters.put(ODataQueryConstants.FILTER, filter);

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
			alert.prepareError("Failed to fetch service definition from table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at fetchServiceDefinition: " + e).log();
			return null;
		}
		
		return serviceDefinitionDTOs;
	}
	
	@Override
	public List<ActionLimitDTO> getMinTransactionLimits() {
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_ACTIONLIMIT_GET;
       
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "LimitType_id eq MIN_TRANSACTION_LIMIT";
		requestParameters.put("$filter", filter);
		List<ActionLimitDTO> actionDTOs= null;
		
		String serviceDefinitionActionLimitResponse = null;
		JSONArray jsonArray = null;
		try {
			serviceDefinitionActionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionActionLimitResponse);
		    jsonArray = responseObj.optJSONArray("actionlimit");
		    actionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ActionLimitDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch min action limits from actionlimit table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getactionlimit: " + e).log();
			return null;
		}
		
		return actionDTOs;
	}

	@Override
	public List<ServiceDefinitionFeatureActionViewDTO> getServiceDefinitionMonetaryActions(String serviceDefinitionId, String legalEntityId) {
		
		String serviceName = ServiceId.CRUDLAYER;
        String operationName = OperationName.DB_SERVICEDEFINITION_FEATURES_ACTIONS_VIEW_GET;
       
        Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "serviceDefinitionId eq "+ serviceDefinitionId +" and companyLegalUnit eq "+legalEntityId;
		requestParameters.put("$filter", filter);
		List<ServiceDefinitionFeatureActionViewDTO> serviceDefinitionFeaturesActionDTOs= null;
		
		String serviceDefinitionActionLimitResponse = null;
		JSONArray jsonArray = null;
		try {
			serviceDefinitionActionLimitResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			JSONObject responseObj = new JSONObject(serviceDefinitionActionLimitResponse);
		    jsonArray = responseObj.optJSONArray("servicedefinition_features_actions_view");
		    serviceDefinitionFeaturesActionDTOs = JSONUtils.parseAsList(jsonArray.toString(), ServiceDefinitionFeatureActionViewDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to fetch action limits from service definition table: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getServiceDefinitionFeatureActions: " + e).log();
			return null;
		}
		
		return serviceDefinitionFeaturesActionDTOs;
	}

	@Override
	public JSONObject searchServiceDefinition(String searchText, String limit) {
		
		String serviceName = ServiceId.CRUDLAYER;
		String operationName = OperationName.DB_SERVICEDEFINITION_GET;
		
		searchText = StringUtils.trim(searchText);
		
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put(ODataQueryConstants.SELECT, "id,name");
        queryMap.put(ODataQueryConstants.ORDER_BY, "name asc");
        queryMap.put(ODataQueryConstants.TOP, limit);
        queryMap.put(ODataQueryConstants.FILTER, "startswith(name,'" + searchText + "') eq true");
        
        String serviceDefinitionResponse = null;
        JSONObject responseObj = null;
		
		try {
			serviceDefinitionResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(queryMap).
					build().getResponse();
			
			responseObj = new JSONObject(serviceDefinitionResponse);
			
		}catch(Exception exp) {
			alert.prepareError("Caught exception while searching servicedefinition: " + exp).log();
		}
        
		return responseObj;
	}

	@Override
	public JSONObject getServiceDefinitionProductIdPermissions(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException {
		 Map<String, Object> headerMap = new HashMap<>();
	        headerMap.put("backendToken", dbpServicesClaimsToken);
	        String serviceResponse =
	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.DBPSERVICE)
	                        .withOperationId(OperationName.GETSERVICEDEFINITIONPRODUCTIDPERMISSIONS).withRequestHeaders(headerMap)
	                        .withRequestParameters(postParametersMap).withPassThroughOutput(true).build().getResponse();
	        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}


}