package com.kony.adminconsole.service.customerrole.resource.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.service.customerrole.businessdelegate.api.CustomerRoleBusinessDelegate;
import com.kony.adminconsole.service.customerrole.dto.CustomerGroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupActionLimitDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupFeatureActionViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupServiceDefinitionDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.MemberGroupDTO;
import com.kony.adminconsole.service.customerrole.resource.api.CustomerRoleResource;
import com.kony.adminconsole.service.servicedefinition.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.servicedefinition.dto.LimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionFeatureActionViewDTO;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerRoleResourceImpl implements CustomerRoleResource{

	CustomerRoleBusinessDelegate customerRoleBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerRoleBusinessDelegate.class);
	
	ServiceDefinitionBusinessDelegate servicedefinitionBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ServiceDefinitionBusinessDelegate.class);
	
	JSONArray featureActions = new JSONArray();
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Result fetchAllGroupActionLimits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();

		try {
			String groupId = request.getParameter("group_id");
			String companyLegalUnit = request.getParameter("legalEntityId");
			if (StringUtils.isBlank(groupId)) {
				ErrorCodeEnum.ERR_21858.setErrorCode(result);
				return result;
			}
			if (StringUtils.isBlank(companyLegalUnit)) {
				ErrorCodeEnum.ERR_22232.setErrorCode(result);
				return result;
			}
			List<GroupFeatureActionViewDTO> groupActions = customerRoleBusinessDelegate.getGroupFeatureActions(groupId, companyLegalUnit);
			if(groupActions == null) {
				ErrorCodeEnum.ERR_21857.setErrorCode(result);
				return result;
			}
			result = convertGroupActionLimits(groupActions);
		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllGroupActionLimits JAVA service. Error: ", e).log();
		}
		
		return result;
	}
	
	/*
	 * Method to convert JSON array response to Result containing features, actions and limits in hierarchial form 
	 * @param groupActions - actions response
	 * @return Result - to be returned to user
	 */
	private Result convertGroupActionLimits(List<GroupFeatureActionViewDTO> groupActions) {
		Result result = new Result();
		
		Map<String, Integer> featuretoActionsCount = new HashMap<>();
		Map<String, Map<String, GroupFeatureActionViewDTO>> features = new LinkedHashMap<>();
        try {
		groupActions.forEach((action) -> {
			
           if("false".equalsIgnoreCase(action.getSoftdelete())) {
			if (features.containsKey(action.getFeatureId())) {
				Map<String, GroupFeatureActionViewDTO> actionsMap = features.get(action.getFeatureId());
				if (actionsMap.containsKey(action.getActionId())) {
					GroupFeatureActionViewDTO existingAction = actionsMap.get(action.getActionId());
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
					featuretoActionsCount.put(action.getFeatureId(), (featuretoActionsCount.get(action.getFeatureId())) == null ? 1
							: featuretoActionsCount.get(action.getFeatureId()) + 1);
				}
			} else {
				Map<String, GroupFeatureActionViewDTO> actionsMap = new HashMap<>();
				if (StringUtils.isNotBlank(action.getLimitTypeId()) && StringUtils.isNotBlank(action.getValue())) {
					action.insertLimit(action.getLimitTypeId(), action.getValue());
				}
				actionsMap.put(action.getActionId(), action);
				featuretoActionsCount.put(action.getFeatureId(), (featuretoActionsCount.get(action.getFeatureId())) == null ? 1
						: featuretoActionsCount.get(action.getFeatureId()) + 1);
				features.put(action.getFeatureId(), actionsMap);
			}
           }
		});

		Dataset featuresDataset = new Dataset("features");

		for (Map.Entry<String, Map<String, GroupFeatureActionViewDTO>> f : features.entrySet()) {
			Record feature = new Record();
			feature.addParam(new Param("id", f.getKey()));
			Dataset actions = new Dataset("actions");

			for (Map.Entry<String, GroupFeatureActionViewDTO> a : f.getValue().entrySet()) {
				GroupFeatureActionViewDTO groupFeatureActionView = a.getValue();

				if (feature.getParam("name") == null) {
					Integer totalActions = featuretoActionsCount.get(groupFeatureActionView.getFeatureId());
					feature.addParam(new Param("name", groupFeatureActionView.getFeatureName()));
					feature.addParam(new Param("description", groupFeatureActionView.getFeatureDescription()));
					feature.addParam(new Param("status", groupFeatureActionView.getFeatureStatusId()));
					feature.addParam(new Param("type", groupFeatureActionView.getFeatureTypeId()));
					feature.addParam(
							new Param("displaySequence", groupFeatureActionView.getFeatureDisplaysequence()));
					 feature.addParam(new Param("totalActions", totalActions.toString()));
					feature.addParam(new Param("isPrimary",groupFeatureActionView.getFeatureIsPrimary()));
					feature.addParam(new Param("legalEntityId",groupFeatureActionView.getCompanyLegalUnit()));

				}
				Record action = new Record();
				action.addParam(new Param("id", a.getKey()));
				action.addParam(new Param("name", groupFeatureActionView.getActionName()));
				action.addParam(new Param("accessPolicy", groupFeatureActionView.getAccessPolicy()));
				action.addParam(new Param("accessPolicyId", groupFeatureActionView.getAccessPolicyId()));
				action.addParam(new Param("actionlevel", groupFeatureActionView.getActionlevel()));
				action.addParam(new Param("actionlevelId", groupFeatureActionView.getActionlevelId()));
				action.addParam(new Param("limitgroup", (!StringUtils.isBlank(groupFeatureActionView.getLimitGroup()) ? groupFeatureActionView.getLimitGroup() : "N/A")));
				action.addParam(new Param("limitgroupId", (!StringUtils.isBlank(groupFeatureActionView.getLimitGroupId()) ? groupFeatureActionView.getLimitGroupId() : "N/A")));
				action.addParam(new Param("description", groupFeatureActionView.getActionName()));
				action.addParam(new Param("type", groupFeatureActionView.getActionTypeId()));
				action.addParam(new Param("isMFAApplicable", groupFeatureActionView.getIsMFAApplicable()));
				action.addParam(new Param("isAccountLevel", groupFeatureActionView.getIsAccountLevel()));
				action.addParam(new Param("isPrimary", groupFeatureActionView.getIsPrimary()));
				action.addParam(new Param("displaySequence", groupFeatureActionView.getActionDisplaysequence()));
				action.addParam(new Param ("actionStatus", groupFeatureActionView.getActionStatus()));
				action.addParam(new Param("legalEntityId",groupFeatureActionView.getCompanyLegalUnit()));
				if (StringUtils.isNotBlank(groupFeatureActionView.getActionDependency())) {
					action.addParam(new Param("dependency", groupFeatureActionView.getActionDependency()));
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
        }
        catch(Exception e) {
        	ErrorCodeEnum.ERR_21912.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllGroupActionLimits JAVA service. Error: ", e).log();
        }
		return result;
	}

	@Override
	public Result fetchAllGroups(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		String companyLegalUnit = request.getParameter("legalEntityId");
		Result result = new Result();

		try {
			
			List<GroupsViewDTO> groups = customerRoleBusinessDelegate.getAllGroups(companyLegalUnit);
			if(groups == null) {
				ErrorCodeEnum.ERR_21865.setErrorCode(result);
				return result;
			}
			else if(groups.size() == 0) {
				return JSONToResult.convert(new JSONObject().put("GroupRecords", new JSONArray()).toString());
			}
			if(groups.size()>0)
			result=fetchServiceDefinitionsForGroups(groups, companyLegalUnit);
			
		}
		catch(Exception e) {
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			alert.prepareError("Exception occured in fetchAllGroups JAVA service. Error: ", e).log();
		}
		
		return result;
	}

	private Result fetchServiceDefinitionsForGroups(List<GroupsViewDTO> inputArray, String companyLegalUnit) {
		Result processedResult = new Result();
		
		Map<String, String> servicedefinitionmap = new HashMap<String, String>();
		Map<String, Map<String, String>> CustomerCountmap = new HashMap<>();
		try {
			try {
				List<ServiceDefinitionDTO> servicedefinitions = customerRoleBusinessDelegate.getServiceDefinitions(companyLegalUnit);
				for (ServiceDefinitionDTO s : servicedefinitions) {
					servicedefinitionmap.put(s.getId(),s.getName());
				}
			}catch(Exception e) {
				return ErrorCodeEnum.ERR_21910.setErrorCode(new Result());
			}
			try {
				CustomerGroupsViewDTO customergroupsviewDTO = new CustomerGroupsViewDTO();
				List<CustomerGroupsViewDTO> customergroups = customerRoleBusinessDelegate.getCustomerGroupsView(companyLegalUnit);
				
				for (CustomerGroupsViewDTO s : customergroups) {
					if(!CustomerCountmap.containsKey(s.getGroupId())) {
					Map<String,String> servicemap=new HashMap<>();
					servicemap.put(s.getServicedefinitionId(),s.getCustomerCount());
					CustomerCountmap.put(s.getGroupId(),servicemap);
					}else {
						Map<String,String> servicemap = CustomerCountmap.get(s.getGroupId());
						servicemap.put(s.getServicedefinitionId(),s.getCustomerCount());
					}
					
					}
			}catch(Exception e) {
				return ErrorCodeEnum.ERR_22013.setErrorCode(new Result());
			}
			
			

			Dataset groupsDataSet = new Dataset();
			groupsDataSet.setId("GroupRecords");

			for (GroupsViewDTO group :inputArray) {

				Record currRecord = new Record();
				String groupId = group.getGroupId();
				
				currRecord.addParam(new Param("Status", group.getStatus(), FabricConstants.STRING));
				currRecord.addParam(new Param("Customers_Count", group.getCustomersCount(), FabricConstants.STRING));
				currRecord.addParam(new Param("Group_id", group.getGroupId(), FabricConstants.STRING));
				currRecord.addParam(new Param("Status_id", group.getStatusId(), FabricConstants.STRING));
				currRecord.addParam(new Param("Type_Name", group.getTypeName(), FabricConstants.STRING));
				currRecord.addParam(new Param("Group_Name", group.getGroupName(), FabricConstants.STRING));
				currRecord.addParam(new Param("isEAgreementActive", group.getIsEAgreementActive(), FabricConstants.STRING));
				currRecord.addParam(new Param("Group_Desc", group.getGroupDesc(), FabricConstants.STRING));
				currRecord.addParam(new Param("Entitlements_Count", group.getEntitlementsCount(), FabricConstants.STRING));
				currRecord.addParam(new Param("Type_id", group.getTypeId(), FabricConstants.STRING));
				currRecord.addParam(new Param("isApplicabletoAllServices", group.getIsApplicabletoAllServices(), FabricConstants.STRING));
				currRecord.addParam(new Param("legalEntityId", group.getCompanyLegalUnit(), FabricConstants.STRING));

				Dataset businessTypeDataset = new Dataset();
				businessTypeDataset.setId("businessTypes");
				try {

					
					List <GroupServiceDefinitionDTO> groupservicedtos = customerRoleBusinessDelegate.getServiceDefinitionsForGroup(groupId);
						if ((groupservicedtos != null) && (groupservicedtos.size() > 0)) {
							groupservicedtos.forEach((groupBusinesstype) -> {
								Record businessRecord = new Record();
								String count=null;
								if(CustomerCountmap.get(groupId)!=null) {
									count = StringUtils.isBlank(CustomerCountmap.get(groupId).get(groupBusinesstype.getServiceDefinitionId())) ? "0" : CustomerCountmap.get(groupId).get(groupBusinesstype.getServiceDefinitionId());
								}else {
									count="0";
								}
								Param businessTypeId_Param = new Param("id", groupBusinesstype.getServiceDefinitionId(), FabricConstants.STRING);
								businessRecord.addParam(businessTypeId_Param);
								Param businessTypeName_Param = new Param("name",servicedefinitionmap.get(groupBusinesstype.getServiceDefinitionId()),
										FabricConstants.STRING);
								businessRecord.addParam(businessTypeName_Param);
								Param isDefaultGroup_Param = new Param("isDefaultGroup", groupBusinesstype.getIsDefaultGroup(),
										FabricConstants.STRING);
								businessRecord.addParam(isDefaultGroup_Param);
								Param customerCount_Param = new Param("customerCount", count,FabricConstants.STRING);
								businessRecord.addParam(customerCount_Param);
								businessTypeDataset.addRecord(businessRecord);
							});
							
						}

				} catch (Exception e) {
					alert.prepareError("Unexepected Error in Fetching Business Types. Exception: ", e).log();
					return null;
				}
				
				currRecord.addDataset(businessTypeDataset);

				groupsDataSet.addRecord(currRecord);
			}
			processedResult.addDataset(groupsDataSet);
			return processedResult;
		}catch(Exception e) {
			alert.prepareError("Unexepected Error in Fetching Business Types. Exception: ", e).log();
			return null;
		}
		}

	@Override
	public Result createGroup(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception  {
		Result result = new Result();
		MemberGroupDTO memberGroupdto = new MemberGroupDTO();
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];

		String userId = StringUtils.EMPTY;
		UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
		if (userDetailsBeanInstance != null) {
			userId = userDetailsBeanInstance.getId();
		}
		inputParams.put("createdBy", userId);
		inputParams.put("companyLegalUnit", request.getParameter("legalEntityId"));
		
		try {
			memberGroupdto = JSONUtils.parse(new JSONObject(inputParams).toString(), MemberGroupDTO.class);
		} catch (IOException e) {
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
			return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
		}
		String companyLegalUnit=memberGroupdto.getCompanyLegalUnit();
		try {
			memberGroupdto = ValidateRequest(memberGroupdto, request);
			if (memberGroupdto.getErrCode() != null) {
				return memberGroupdto.getErrCode().setErrorCode(new Result());
			}
			
			String id = CommonUtilities.getNewId().toString();
			memberGroupdto.setId(id);
			String roleType=memberGroupdto.getTypeId();
			memberGroupdto = customerRoleBusinessDelegate.createGroup(memberGroupdto);
			if (memberGroupdto == null) {
				alert.prepareError("Failed to create a Customer Group: ").log();
				return ErrorCodeEnum.ERR_20505.setErrorCode(new Result());
			}

			boolean actionLimitStatus = false;
			JSONArray addedActions = new JSONArray();
			try {
				addedActions = new JSONArray(request.getParameter("featureactions"));
			} catch (Exception e) {
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
			}

				actionLimitStatus = validateActions(roleType, addedActions, companyLegalUnit);
				if (actionLimitStatus == false) {
					alert.prepareError("Invalid actions or limits not in the given range").log();
					if (!customerRoleBusinessDelegate.deleteGroup(memberGroupdto)) {
						return ErrorCodeEnum.ERR_20507.setErrorCode(new Result());
					}
					return ErrorCodeEnum.ERR_21925.setErrorCode(new Result());
				}
				HashMap<String, GroupActionLimitDTO> existingMap = new HashMap<>();
				Map<JSONArray,Boolean> resultMap = new HashMap<>();
				resultMap = updateGroupActions(addedActions, id, existingMap,userId, companyLegalUnit);
				for(Map.Entry<JSONArray,Boolean> entry : resultMap.entrySet()) {
					actionLimitStatus  = entry.getValue();
					break;
				}
				
				if (actionLimitStatus == false) {
					alert.prepareError("Failed to create a Customer Group: ").log();
					if (!customerRoleBusinessDelegate.deleteGroup(memberGroupdto)) {
						return ErrorCodeEnum.ERR_20507.setErrorCode(new Result());
					}
					return ErrorCodeEnum.ERR_21861.setErrorCode(new Result());
				}
			
			JSONArray serviceDefinitions = new JSONArray();
			try {
				serviceDefinitions = new JSONArray(request.getParameter("servicedefinitions"));
			} catch (Exception e) {
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
			}
			
			 if (validateServiceDefinition(serviceDefinitions,roleType, companyLegalUnit)) { 
				  memberGroupdto.setCompanyLegalUnit(companyLegalUnit);
				  boolean status = createGroupServiceDefinition(memberGroupdto,serviceDefinitions,userId);
				  if (!status) {
					  alert.prepareError("Failed to create a service definition: ").log();
						if (!customerRoleBusinessDelegate.deleteGroup(memberGroupdto)) {
							return ErrorCodeEnum.ERR_20507.setErrorCode(new Result());
						}
						return ErrorCodeEnum.ERR_21861.setErrorCode(new Result());
					}
			  }else {
				  if (!customerRoleBusinessDelegate.deleteGroup(memberGroupdto)) {
						return ErrorCodeEnum.ERR_20507.setErrorCode(new Result());
					}
				  return ErrorCodeEnum.ERR_21949.setErrorCode(new Result());  
			  }

			 

		} catch (Exception e) {
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			return ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		result = JSONToResult.convert(new JSONObject(memberGroupdto).toString());
		result.addParam(new Param("legalEntityId", result.getParamValueByName("companyLegalUnit")));
        result.removeParamByName("companyLegalUnit");
		return result;
	}
	
	private boolean createGroupServiceDefinition(MemberGroupDTO memberGroupdto, JSONArray serviceDefinitions, String userId) {
		GroupServiceDefinitionDTO groupServiceDefinition = new GroupServiceDefinitionDTO();
		String serviceId= null;
		String defaultGroup= null;
		boolean status=false;
		List<GroupServiceDefinitionDTO> existingServiceAssociations=customerRoleBusinessDelegate.fetchGroupServiceDefinitions(memberGroupdto.getCompanyLegalUnit());
		List<String> existingrolesmap = existingServiceAssociations.stream().map(GroupServiceDefinitionDTO::getServiceDefinitionId).collect(Collectors.toList());
		for(int ind = 0 ;ind< serviceDefinitions.length();ind++) {
			serviceId=serviceDefinitions.getJSONObject(ind).getString("id");
			defaultGroup=serviceDefinitions.getJSONObject(ind).getString("isDefault");
			groupServiceDefinition.setGroupId(memberGroupdto.getId());
			groupServiceDefinition.setCompanyLegalUnit(memberGroupdto.getCompanyLegalUnit());
			groupServiceDefinition.setCreatedBy(userId);
			
			groupServiceDefinition.setServiceDefinitionId(serviceId);
			
			// sets current role as default to service definition if there are no previous roles associated
			 if(existingrolesmap.contains(serviceId))
			 groupServiceDefinition.setIsDefaultGroup(defaultGroup); 
			 else
			 groupServiceDefinition.setIsDefaultGroup("1");
 
		
			groupServiceDefinition=customerRoleBusinessDelegate.createGroupServiceDefinition(groupServiceDefinition);
			if (groupServiceDefinition== null)
			{
				return false;
			}
			
			// updates existing default role to the current role 
			if(existingrolesmap.contains(serviceId) && defaultGroup.equals("1") ) {
				status=customerRoleBusinessDelegate.editDefaultGroupServiceDefinition(groupServiceDefinition);
				if(status==false)
					return false;
			
			}
			
			
		}
		
		return true;
		
	}

	private boolean validateServiceDefinition(JSONArray serviceDefinitions, String roleType, String companyLegalUnit) {
		try {
		List<ServiceDefinitionDTO> validServicedtos = customerRoleBusinessDelegate.getServiceDefinitionsByType(roleType, companyLegalUnit);
		List<String> validServiceIds = validServicedtos.stream().map(ServiceDefinitionDTO::getId).collect(Collectors.toList());
		
		if(validServicedtos.size()==0)
			return false;
		
		for(int ind =0 ;ind< serviceDefinitions.length();ind++) {
			String serviceDefinitionId = serviceDefinitions.getJSONObject(ind).getString("id");
			if(!validServiceIds.contains(serviceDefinitionId)) {
				return false;
			}
		}
		}catch(Exception e) {
			return false;
		}

		return true;
	}

	/*
	 * Method to validate the request payload for both create and edit
	 * @param MemberGroupDTO - contains payload of Customer Group create or edit service
	 * @param request - To fetch the featureactions - as it is a JSON Array cannot be stored in DTO so sending explicitly
	 * @return MemberGroupDTO - contains Error code in case of failure otherwise contains input service definition payload only
	 */
	private MemberGroupDTO ValidateRequest(MemberGroupDTO memberGroupdto, DataControllerRequest request) {
		try {
			JSONArray services = new JSONArray(request.getParameter("servicedefinitions")) ;
			JSONArray features= new JSONArray(request.getParameter("featureactions")) ;;
			
		if (StringUtils.isBlank(memberGroupdto.getName()) || CommonUtilities.containAnySpecialCharacters(memberGroupdto.getName())) { 
			alert.prepareError("Name is mandatory. Please use allowed characters only.").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21917);
			return memberGroupdto;
		} else if (StringUtils.isBlank(memberGroupdto.getDescription()) || CommonUtilities.containAnySpecialCharactersForDescription(memberGroupdto.getDescription())) {
			alert.prepareError("Description is mandatory. Please use allowed characters only.").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21919);
			return memberGroupdto;
		} else if (memberGroupdto.getDescription().length()>250) {
			alert.prepareError("description cannot have more than 250 characters ").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22011);
			return memberGroupdto;			
		} else if (memberGroupdto.getName().length()>100) {
			alert.prepareError("Name cannot have more than 100 characters ").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22010);
			return memberGroupdto;			
		} else if ( StringUtils.isBlank(memberGroupdto.getTypeId()) || (!memberGroupdto.getTypeId().equalsIgnoreCase("TYPE_ID_BUSINESS") && !memberGroupdto.getTypeId().equalsIgnoreCase("TYPE_ID_RETAIL") && !memberGroupdto.getTypeId().equalsIgnoreCase("TYPE_ID_WEALTH"))){
			alert.prepareError("roleType should be TYPE_ID_RETAIL or TYPE_ID_BUSINESS or TYPE_ID_WEALTH").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21951);
			return memberGroupdto;
		} else if ( StringUtils.isBlank(memberGroupdto.getStatus()) ||(!memberGroupdto.getStatus().equalsIgnoreCase("SID_ACTIVE") && !memberGroupdto.getStatus().equalsIgnoreCase("SID_INACTIVE"))) {
			alert.prepareError("status should be SID_ACTIVE or SID_INACTIVE").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21915);
			return memberGroupdto;
		} else if (StringUtils.isBlank(request.getParameter("featureactions")) || features.length()==0 ) {
			alert.prepareError("Feature actions cannot be empty").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21922);
			return memberGroupdto;
		} else if (StringUtils.isBlank(request.getParameter("servicedefinitions"))|| services.length()==0) {
			alert.prepareError("Service Definitions cannot be empty").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21948);
			return memberGroupdto;
		} else if (StringUtils.isBlank(request.getParameter("legalEntityId"))) {
			alert.prepareError("legalEntityId cannot be empty").log();
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22232);
			return memberGroupdto;
		}
		}
		catch(Exception e) {
			memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21908);
			return memberGroupdto;
		}
		
		
		
		//For Edit Scenario - Validations for type_id modifications
		if (StringUtils.isNotBlank(memberGroupdto.getId())) {
			GroupsViewDTO actualgroup = customerRoleBusinessDelegate.getGroupById(memberGroupdto.getId());
				if(actualgroup== null){
					memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21979);
					return memberGroupdto;
				}
				if(!actualgroup.getTypeId().equals(memberGroupdto.getTypeId())) {
					memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21952);
					return memberGroupdto;
				}
				if(!actualgroup.getCompanyLegalUnit().equals(memberGroupdto.getCompanyLegalUnit())) {
					memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21957);
					return memberGroupdto;
				}
				if(Integer.parseInt(actualgroup.getCustomersCount())>0 && memberGroupdto.getStatus().equalsIgnoreCase("SID_INACTIVE")) {
					memberGroupdto.setErrCode(ErrorCodeEnum.ERR_21978);
					return memberGroupdto;
				}  
				if(memberGroupdto.getStatus().equalsIgnoreCase("SID_INACTIVE")) {
				List<GroupServiceDefinitionDTO> servicedeflist = customerRoleBusinessDelegate.getServiceDefinitionsForGroup(memberGroupdto.getId());
				if(servicedeflist!=null) {
				for( int i=0;i<servicedeflist.size();i++) {
					if(servicedeflist.get(i).getIsDefaultGroup().equalsIgnoreCase("true")) {
						memberGroupdto.setErrCode(ErrorCodeEnum.ERR_22083);
						return memberGroupdto;
					}
				};
				}
				}
		}
		
		return memberGroupdto;
	}
	
	private boolean containSpecialChars(String name) {
		Pattern regex = Pattern.compile("[+=\\\\|<>^*%]");
		if (regex.matcher(name).find()) {
		    return true;
		} 
		return false;
	}

	/*
	 * Method to validate the user input feature actions based on customer role type and also to validate action limits using master data
	 * @param roleTypeId - String value of service definition type - retail or business
	 * @param actionlimits - JSON Array of action limits from user input
	 * @return boolean - true if it contains valid actions otherwise false
	 */
	private boolean validateActions(String roleTypeId, JSONArray actionlimits, String companyLegalUnit) {
		List<FeatureActionRoleTypeDTO> validRoleTypeActions = customerRoleBusinessDelegate.getValidRoleTypeActions(roleTypeId, companyLegalUnit);
		if(validRoleTypeActions == null) {
			alert.prepareError("Failed to fetch featureactions valid for role type").log();
			return false;
		}
		List<String> validRoleTypeActionIds = validRoleTypeActions.stream().map(FeatureActionRoleTypeDTO::getActionId).collect(Collectors.toList());

		List<ActionLimitDTO> actionLimitDTOs = customerRoleBusinessDelegate.fetchActionLimits(companyLegalUnit);
		if(actionLimitDTOs == null) {
			alert.prepareError("Failed to fetch actions limits").log();
			return false;
		}
		HashMap<String,Double> masterMap = convertActionLimits(actionLimitDTOs);

		Set<String> actions = new HashSet<>();
		for (ActionLimitDTO a : actionLimitDTOs) {
			actions.add(a.getActionId());
		}

		LimitDTO limitDto = new LimitDTO();
		for (int i = 0; i < actionlimits.length(); i++) {
			String actionId = actionlimits.getJSONObject(i).getString("id");
			if(!validRoleTypeActionIds.contains(actionId)) {
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
					if(!isValidLimitRange(limitDto,masterMap)) {
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
	 * Method to convert action limits to hashmap for default limits
	 * @param actionLimitDTOs - List of masterdata limits which needs to be converted
	 * @return hashmap containing actionId as key and limittypeId as value
	 */
	private HashMap<String,Double> convertActionLimits(List<ActionLimitDTO> actionLimitDTOs) {
		HashMap<String,Double> masterMap = new HashMap<>();
		try {
			for(ActionLimitDTO a: actionLimitDTOs) {
				double value = Double.parseDouble(a.getValue());
				masterMap.put(constructKey(a.getActionId(), a.getLimitTypeId()),value);
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception while converting the list").log();
		}
		return masterMap;
	}
	
	/*
	 * Method to validate the user input limit range with default limit range
	 * @param limit - LimitDTO of each monetary action
	 * @param masterMap - HashMap containing masterdata limits
	 * @return boolean - true if it is valid otherwise false
	 */
	private boolean isValidLimitRange(LimitDTO limit, HashMap<String, Double> masterMap) {
		if (limit.getMaxTransactionLimit() > masterMap.get(constructKey(limit.getActionId(), "MAX_TRANSACTION_LIMIT"))
				|| limit.getMaxTransactionLimit() < limit.getMinTransactionLimit() || limit.getMaxTransactionLimit() >limit.getDailyLimit()
                || limit.getMaxTransactionLimit() > limit.getWeeklyLimit()) {
			alert.prepareError("Maximum Transaction limit isn't within the specified range").log();
			return false;
		} else if (limit.getDailyLimit() > masterMap.get(constructKey(limit.getActionId(), "DAILY_LIMIT"))
				|| limit.getDailyLimit() < limit.getMaxTransactionLimit() || limit.getDailyLimit() > limit.getWeeklyLimit()) {
			alert.prepareError("Daily Transaction limit isn't within the specified range").log();
			return false;
		} else if (limit.getWeeklyLimit() > masterMap.get(constructKey(limit.getActionId(), "WEEKLY_LIMIT"))
				|| limit.getWeeklyLimit() < limit.getDailyLimit() || limit.getWeeklyLimit() < limit.getMaxTransactionLimit() ) {
			alert.prepareError("Weekly Transaction limit isn't within the specified range").log();
			return false;
		}
		return true;
	}
  
	private String constructKey(String action, String limit) {
	  return action+"."+limit;
	}
	
	/*
	 * Method to get update/edit feature actions
	 * @param actionlimits - List of all actions
	 * @param groupId
	 * @param existingMap - Existing featureactions for that service definition
	 * @return true of update is successful otherwise false
	 */
	private Map<JSONArray,Boolean> updateGroupActions(JSONArray actionlimits,String groupId,HashMap<String,GroupActionLimitDTO> existingMap, String userId, String companyLegalUnit) {
		GroupActionLimitDTO actionLimitDTO = new GroupActionLimitDTO();
		
		boolean flag = false;
		JSONArray featureActions = new JSONArray();
		Map<JSONArray,Boolean> resultMap = new HashMap<>();
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
						if (existingMap.containsKey(key) ) {
							if(existingMap.get(key).getValue()!= actionLimitDTO.getValue()) {
								if(Float.parseFloat(existingMap.get(key).getValue()) > Float.parseFloat(actionLimitDTO.getValue())) {
		                    		flag =true;
		                    		}
							actionLimitDTO.setModifiedBy(userId);	
							actionLimitDTO.setId(existingMap.get(key).getId());
							actionLimitDTO = customerRoleBusinessDelegate
									.editGroupActionLimit(actionLimitDTO);
							}
						} else {
							actionLimitDTO.setCreatedBy(userId);
							actionLimitDTO.setId(String.valueOf(CommonUtilities.getNewId()));
							actionLimitDTO = customerRoleBusinessDelegate.createGroupActionLimit(actionLimitDTO);
						}
						if (actionLimitDTO == null) {
							alert.prepareError("Failed to add/edit the service definition actions limits").log();
							resultMap.put(featureActions,false);
							return resultMap;
						}
				}
				if(flag) {
					featureActions.put(actionlimits.getJSONObject(i));
               	    flag = false;
				}
			} else {
				actionLimitDTO.setGroupId(groupId);
				actionLimitDTO.setActionId(actionId);
				actionLimitDTO.setCreatedBy(userId);
				if (!existingMap.containsKey(actionId)) {
					actionLimitDTO.setId(String.valueOf(CommonUtilities.getNewId()));
					actionLimitDTO = customerRoleBusinessDelegate
							.createGroupActionLimit(actionLimitDTO);
				}
				if (actionLimitDTO == null) {
					alert.prepareError("Failed to update the service definition service definition actions  ").log();
					resultMap.put(featureActions,false);
					return resultMap;
				}
			}
		}
		
		resultMap.put(featureActions,true);
		return resultMap;
	}

	@Override
	public Result manageStatus(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
			@SuppressWarnings("unchecked")
			Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
			Result result = new Result();
			String companyLegalUnit = request.getParameter("legalEntityId");
	        String groupId = null;
	        String userId = StringUtils.EMPTY;
	        String status=null;
			
			if(inputParams.get("Group_id") != null) {
				groupId = inputParams.get("Group_id").toString();
			}
			if(inputParams.get("Status_id") != null) {
			status = inputParams.get("Status_id").toString();
			}
			
			
			try {
			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(request);
			if (userDetailsBeanInstance != null) {
				userId = userDetailsBeanInstance.getId();
			}
			}catch(Exception e) {
				return ErrorCodeEnum.ERR_21977.setErrorCode(new Result());
			}
			
			if(groupId == null || groupId.isEmpty()) {
				alert.prepareError("Missing groupId").log();
				return ErrorCodeEnum.ERR_20569.setErrorCode(new Result());
			} else if (status == null || status.isEmpty() || (!status.equalsIgnoreCase("SID_ACTIVE") && !status.equalsIgnoreCase("SID_INACTIVE"))) {
				alert.prepareError("Status cannot be other than SID_ACTIVE or SID_INACTIVE").log();
				return ErrorCodeEnum.ERR_21915.setErrorCode(new Result());
			} 
			
			GroupsViewDTO actualgroup = customerRoleBusinessDelegate.getGroupById(groupId);
			if(actualgroup== null)
			{
				return ErrorCodeEnum.ERR_21979.setErrorCode(new Result());
			}
			
			if(Integer.parseInt(actualgroup.getCustomersCount())>0 && status.equalsIgnoreCase("SID_INACTIVE")) {
				return ErrorCodeEnum.ERR_21978.setErrorCode(new Result());
			}  
			
			if(status.equalsIgnoreCase("SID_INACTIVE")) {
			List<GroupServiceDefinitionDTO> servicedeflist = customerRoleBusinessDelegate.getServiceDefinitionsForGroup(groupId);
			if(servicedeflist!=null) {
			for( int i=0;i<servicedeflist.size();i++) {
				if(servicedeflist.get(i).getIsDefaultGroup().equalsIgnoreCase("true"))
					return ErrorCodeEnum.ERR_22083.setErrorCode(new Result());
			};
			}
			}
			
			if(!companyLegalUnit.equals(actualgroup.getCompanyLegalUnit()))
            {
                alert.prepareError("CompanyLegalUnit cannot be changed ").log();
                return ErrorCodeEnum.ERR_21957.setErrorCode(new Result());
            }
			
			
			MemberGroupDTO groupdto = new MemberGroupDTO();
			try {
				groupdto.setId(groupId);
				groupdto.setStatus(status);
				groupdto.setModifiedBy(userId);
				groupdto.setCompanyLegalUnit(companyLegalUnit);
				
				
			} catch (Exception e) {
				alert.prepareError("Error occured while fetching the input params: " + e).log();
				return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
			}
			
			
			groupdto = customerRoleBusinessDelegate.editGroup(groupdto);
			if (groupdto == null) {
				alert.prepareError("Failed to deactivate/inactivate the group : ").log();
				return ErrorCodeEnum.ERR_21976.setErrorCode(new Result());
			}
			JSONObject manageStatusResponse = new JSONObject(groupdto);
			manageStatusResponse.put("legalEntityId", manageStatusResponse.get("companyLegalUnit"));
            manageStatusResponse.remove("companyLegalUnit");
            result = JSONToResult.convert(manageStatusResponse.toString());
			return result;
	}
	
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
			alert.prepareError("Id cannot be null ").log();
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
			alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
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
				alert.prepareError("Caught exception while converting input params to DTO: ", e).log();
				return ErrorCodeEnum.ERR_21908.setErrorCode(new Result());
			}

			actionLimitStatus = validateActions(groupdto.getTypeId(), addedActions,  legalEntityId);
			if (actionLimitStatus == false) {
				alert.prepareError("Invalid actions or limits not in the given range").log();
				return ErrorCodeEnum.ERR_21925.setErrorCode(new Result());
			}

			serviceStatus = validateServiceDefinition(addedServices, roleType,  legalEntityId);
			if (serviceStatus == false) {
				alert.prepareError("Invalid Service Definitions found in Request").log();
				return ErrorCodeEnum.ERR_21949.setErrorCode(new Result());
			}

			groupdto.setSoftDeleteFlag(null);

			groupdto = customerRoleBusinessDelegate.editGroup(groupdto);
			if (groupdto == null) {
				alert.prepareError("Failed to edit Customer Group: ").log();
				return ErrorCodeEnum.ERR_21981.setErrorCode(new Result());
			}

			HashMap<String, GroupActionLimitDTO> existingMap = getExistingActionMap(groupdto);
			Map<JSONArray, Boolean> resultMap = new HashMap<>();
			JSONArray featureActions = new JSONArray();
			resultMap = updateGroupActions(addedActions, id, existingMap, userId,  legalEntityId);
			for (Map.Entry<JSONArray, Boolean> entry : resultMap.entrySet()) {
				featureActions = entry.getKey();
				actionLimitStatus = entry.getValue();
				break;
			}
			if (actionLimitStatus == false) {
				alert.prepareError("Failed to edit Customer Group ").log();
				return ErrorCodeEnum.ERR_21982.setErrorCode(new Result());
			}
			List<String> removeActionList = getRemoveActionList(addedActions, existingMap);
			actionLimitStatus = removeGroupActions(removeActionList, existingMap);
			if (actionLimitStatus == false) {
				alert.prepareError("Failed to edit Customer Group ").log();
				return ErrorCodeEnum.ERR_21982.setErrorCode(new Result());
			}
			result = updateGroupServiceMapping(id, addedServices, userId,  legalEntityId);
			if (result.hasParamByName("dbpErrCode") || result.hasParamByName("dbpErrMsg")) {
				return result;
			}
			if (!limitsAndPermissionsUpdate(id, featureActions.toString(), removeActionList.toString(), request)) {
				diagnostic.prepareDebug("Runtime Exception.Exception Trace:").log();
				return ErrorCodeEnum.ERR_20001.setErrorCode(result);
			}

		} catch (Exception e) {
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			return ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		result = JSONToResult.convert(new JSONObject(groupdto).toString());
		result.addParam(new Param("legalEntityId", result.getParamValueByName("companyLegalUnit")));
        result.removeParamByName("companyLegalUnit");
		return result;
	}
	
	/*
	 * Method to add/remove group service definition associations while editing 
	 * @param id - group_id
	 * @param addedServices - List of all service definitions to be updated
	 * @param userId - Logged in userid
	 * @return boolean - true if update is successful otherwise false
	 */
	private Result updateGroupServiceMapping(String id, JSONArray addedServices, String userId, String legalEntityId) {
		Result result = new Result();
		try {
		List<GroupServiceDefinitionDTO> existingServices = customerRoleBusinessDelegate.getServiceDefinitionsForGroup(id);
		List<String> serviceIds= existingServices.stream().map(GroupServiceDefinitionDTO::getServiceDefinitionId).collect(Collectors.toList());
		String serviceId= null;
		String isDefault= null;
		boolean defaultStatus= false;
		boolean status=false;
		List<String> removeservicelist = getRemoveServicesList(addedServices, serviceIds);
		GroupServiceDefinitionDTO groupservicedto = new GroupServiceDefinitionDTO();
		Map<String,GroupServiceDefinitionDTO> servicemap = new HashMap<>();
		for(GroupServiceDefinitionDTO servicedto : existingServices) {
			servicemap.put(servicedto.getServiceDefinitionId(),servicedto);
		}
		Map<String, Map<String, String>> CustomerCountmap = new HashMap<>();
		try {
			List<CustomerGroupsViewDTO> customergroups = customerRoleBusinessDelegate.getCustomerGroupsView(legalEntityId);
			
			for (CustomerGroupsViewDTO s : customergroups) {
				if(!CustomerCountmap.containsKey(s.getGroupId())) {
				Map<String,String> servicedefmap=new HashMap<>();
				servicedefmap.put(s.getServicedefinitionId(),s.getCustomerCount());
				CustomerCountmap.put(s.getGroupId(),servicedefmap);
				}else {
					Map<String,String> servicedefmap = CustomerCountmap.get(s.getGroupId());
					servicedefmap.put(s.getServicedefinitionId(),s.getCustomerCount());
				}
				
				}
		}catch(Exception e) {
			return ErrorCodeEnum.ERR_22012.setErrorCode(result);
		}
		
		groupservicedto.setCompanyLegalUnit(legalEntityId);
		List<GroupServiceDefinitionDTO> existingServiceAssociations=customerRoleBusinessDelegate.fetchGroupServiceDefinitions(groupservicedto.getCompanyLegalUnit());
		List<String> existingrolesmap = existingServiceAssociations.stream().map(GroupServiceDefinitionDTO::getServiceDefinitionId).collect(Collectors.toList());
		
		// adds new role service definition association
		for (int ind=0; ind < addedServices.length() ;ind++ ) {
			serviceId= addedServices.getJSONObject(ind).getString("id");
			isDefault= addedServices.getJSONObject(ind).getString("isDefault");
			if(!serviceIds.contains(serviceId)){
				groupservicedto.setGroupId(id);
				groupservicedto.setServiceDefinitionId(serviceId);
				groupservicedto.setCreatedBy(userId);
				
				// sets current role as default to service definition if there are no previous roles associated
				 if(existingrolesmap.contains(serviceId))
					 groupservicedto.setIsDefaultGroup(isDefault); 
				 else
					 groupservicedto.setIsDefaultGroup("1");
	 
				groupservicedto.setCompanyLegalUnit(legalEntityId);
				groupservicedto=customerRoleBusinessDelegate.createGroupServiceDefinition(groupservicedto);
				if(groupservicedto==null) {
					return ErrorCodeEnum.ERR_22004.setErrorCode(result);
				}
				
				// updates existing default role to the current role 
				if(existingrolesmap.contains(serviceId) && isDefault.equals("1") ) {
					defaultStatus=customerRoleBusinessDelegate.editDefaultGroupServiceDefinition(groupservicedto);
					if(defaultStatus==false)
						return ErrorCodeEnum.ERR_22005.setErrorCode(result);
				}
				
				
			}else if(servicemap.containsKey(serviceId) && isDefault.equals("0") && 
					servicemap.get(serviceId).getIsDefaultGroup().equalsIgnoreCase("true") ) {
				return ErrorCodeEnum.ERR_22009.setErrorCode(result);	
			}else if(servicemap.containsKey(serviceId)  && 
					!servicemap.get(serviceId).getIsDefaultGroup().equalsIgnoreCase(isDefault)){
				
				groupservicedto.setGroupId(id);
				groupservicedto.setServiceDefinitionId(serviceId);
				groupservicedto.setIsDefaultGroup(isDefault);
				groupservicedto.setModifiedBy(userId);
				
				status=customerRoleBusinessDelegate.editDefaultGroupServiceDefinition(groupservicedto);
				if(status==false) {
					return ErrorCodeEnum.ERR_22005.setErrorCode(result);
				}
				
			}
		}
		
		// removes role service definition associations
		boolean removestatus= false;
			for(String sid : removeservicelist) {
				String count=null;
				if(CustomerCountmap.get(id)!=null) {
					count = StringUtils.isBlank(CustomerCountmap.get(id).get(sid)) ? "0" : CustomerCountmap.get(id).get(sid);
				}else {
					count="0";
				}
				if(servicemap.get(sid).getIsDefaultGroup().equalsIgnoreCase("false")) {
					groupservicedto.setGroupId(id);
					groupservicedto.setServiceDefinitionId(sid);
					removestatus=customerRoleBusinessDelegate.deleteGroupServiceDefinition(groupservicedto);
					if(removestatus==false) {
						return ErrorCodeEnum.ERR_22007.setErrorCode(result);
					}
				}else if(servicemap.get(sid).getIsDefaultGroup().equalsIgnoreCase("true")) {
					return ErrorCodeEnum.ERR_22006.setErrorCode(result);
				}else if(Integer.parseInt(count)>0) {
					return ErrorCodeEnum.ERR_22008.setErrorCode(result);
				}
		}
		}catch(Exception e) {
			return ErrorCodeEnum.ERR_21980.setErrorCode(result);
		}
			
		return result;
	}

	/*
	 * Method to remove group action when user deselects the feature actions using edit 
	 * @param removeList - List of actions to be de-asscoiated with the service definition
	 * @param existingMap - Existing featureactions for that service definition
	 * @return boolean - true if remove is successful otherwise false
	 */
	private boolean removeGroupActions(List<String> removeList, HashMap<String, GroupActionLimitDTO> existingMap) {
	GroupActionLimitDTO actionLimitDTO = new GroupActionLimitDTO();
	boolean status=true;
			for(String s: removeList) {
				actionLimitDTO = new GroupActionLimitDTO();
				actionLimitDTO.setId(existingMap.get(s).getId());
				status = customerRoleBusinessDelegate.deleteGroupActionLimit(actionLimitDTO);
				if (status == false) {
					alert.prepareError("Failed to update the service definition actions").log();
					return false;
				}
			}
			return true;
		}

	/*
	 * Method to get existing feature actions of given customer group
	 * @param GroupActionLimitDTO - contains all the details of service definition
	 * @return Hashmap of existing featureactions with featureaction as id and limittypeId as value
	 */
	private HashMap<String,GroupActionLimitDTO> getExistingActionMap(MemberGroupDTO memberGroupdto){
		List<GroupActionLimitDTO> actionList = customerRoleBusinessDelegate.getGroupActionLimit(memberGroupdto);
		
		HashMap<String,GroupActionLimitDTO> existingMap = new HashMap<>();
		for(GroupActionLimitDTO groupLimit : actionList) {
			String key = groupLimit.getActionId();
			if(!StringUtils.isBlank(groupLimit.getLimitTypeId())) {
				key = constructKey(key, groupLimit.getLimitTypeId());
			}
			existingMap.put(key,groupLimit);
		}
		
		return existingMap;
	}
	
	/*
	 * Method to get feature actions which are deselected by the user
	 * @param actionlimits - List of all actions
	 * @param existingMap - Existing featureactions for that customer group
	 * @return List of Strings containing featureactionIds which must be removed
	 */
	private List<String> getRemoveActionList(JSONArray actionlimits, HashMap<String,GroupActionLimitDTO> existingMap) {
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
		List <String> li =  new ArrayList<>(existingSet);
		li.removeAll(removeActionList);
		return li;
	}
	
	/*
	 * Method to get service definitions which are deselected by the user
	 * @param services  - List of all added service definition
	 * @param existingMap - Existing service definition ids for that customer group
	 * @return List of Strings containing servicedefinitionIds which must be removed
	 */
	private List<String> getRemoveServicesList(JSONArray services, List<String> existingMap) {
		List<String> removeServiceList = new ArrayList<>();
		
		for (int i = 0; i < services.length(); i++) {

			String serviceId = services.getJSONObject(i).getString("id");
			removeServiceList.add(serviceId);
		}
		
		List <String> li =  new ArrayList<>(existingMap);
		li.removeAll(removeServiceList);
		return li;
	}

	@Override
	public Result downloadGroupsList(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		
		Result result = new Result();
		Map<String, String> servicedefinitionmap = new HashMap<String, String>();
		Map<String, String> queryParamsMap = new HashMap<String, String>();
		try {
			//@SuppressWarnings("unchecked")
			queryParamsMap = (Map<String, String>) requestInstance.getAttribute("queryparams");
			String companyLegalUnit= queryParamsMap.containsKey("legalEntityId") ? queryParamsMap.get("legalEntityId") : null;
			if(StringUtils.isBlank(companyLegalUnit)) {
				alert.prepareError("LegalEntity Id cannot be null or empty").log();
				ErrorCodeEnum.ERR_22232.setErrorCode(result);
			      return result;
			}
			List<ServiceDefinitionDTO> servicedefinitions = customerRoleBusinessDelegate.getServiceDefinitions(companyLegalUnit);
			for (ServiceDefinitionDTO s : servicedefinitions) {
				servicedefinitionmap.put(s.getId(),s.getName());
			}
		}catch(Exception e) {
			return ErrorCodeEnum.ERR_21910.setErrorCode(new Result());
		}
		try {
			
			
			String searchText = queryParamsMap.containsKey("searchText") ? queryParamsMap.get("searchText") : null;


			String authToken = CommonUtilities.getAuthToken(requestInstance);
			if (StringUtils.isBlank(authToken)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20000);
			}
			StringBuilder filterString = getFilterForDownloadGroups(queryParamsMap);

			List <GroupsViewDTO> groupviewdtos = customerRoleBusinessDelegate.fetchGroupsViewWithFilter(filterString.toString());

			if ( groupviewdtos != null) {

				StringBuilder responseCsvBuilder = new StringBuilder(); // Contains the text for response CSV file
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
						.withHeader("Name","Role Type","Services", "Description", "Users", "Status", "LegalEntityId")
						.print(responseCsvBuilder);

				groupviewdtos.forEach((groups) ->{
					List <GroupServiceDefinitionDTO> groupservicedtos=null;
					try {
					 groupservicedtos = customerRoleBusinessDelegate.getServiceDefinitionsForGroup(groups.getGroupId());
					 if(groupservicedtos==null) {
						ErrorCodeEnum.ERR_22020.setErrorCode(new Result());
					 }
					}catch(Exception e) {
						alert.prepareError("Error while fetching ServiceDefintions for a Customer Role", e).log();
						ErrorCodeEnum.ERR_22020.setErrorCode(result);
					}
					String nameColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(groups.getGroupName());
					String roleTypeColumn= CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(groups.getTypeName());
					String servDefsColumn=getServiceDefinitionNames(groupservicedtos,servicedefinitionmap);
					String descriptionColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(groups.getGroupDesc());
					String customersCountColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(groups.getCustomersCount());
					String statusColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(groups.getStatus());
					String legalEntityColumn = CommonUtilities.prependSingleQuoteIfFirstCharIsTriggerChar(groups.getCompanyLegalUnit());

					if (searchText == null
							|| (searchText != null && (nameColumn.toLowerCase().contains(searchText.toLowerCase())))) {
						try {
							responseCsvPrinter.printRecord(nameColumn,roleTypeColumn,servDefsColumn, descriptionColumn, customersCountColumn,
									 statusColumn,legalEntityColumn);
						} catch (IOException e) {
							alert.prepareError("Failed while downloading groups list", e).log();
							ErrorCodeEnum.ERR_21984.setErrorCode(result);
						}
					}
				});
				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"Groups_List.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
						new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
				responseInstance.getHeaders().putAll(customHeaders);
			} else {
				alert.prepareError("Failed to fetch groups").log();
				ErrorCodeEnum.ERR_20404.setErrorCode(result);
			}

		} catch (Exception e) {
			alert.prepareError("Failed while downloading groups list", e).log();
			ErrorCodeEnum.ERR_21984.setErrorCode(result);
		}
		return result;
	}

	private String getServiceDefinitionNames(List<GroupServiceDefinitionDTO> groupservicedtos, Map<String, String> servicedefinitionmap) {
		StringBuilder result= new StringBuilder();
		String serviceDefinition="";
		groupservicedtos.forEach((rowdata)->{
			String sid =rowdata.getServiceDefinitionId();
			result.append(servicedefinitionmap.get(sid));
			result.append(',');
		});
		if (result.length()>0) {
	 serviceDefinition =  result.substring(0, result.length()-1);
		}
		return serviceDefinition;
	}

	private StringBuilder getFilterForDownloadGroups(Map<String, String> queryParamsMap) {
		String type = queryParamsMap.containsKey("type") ? queryParamsMap.get("type") : null;
		String status = queryParamsMap.containsKey("status") ? queryParamsMap.get("status") : null;
		String legalEntity = queryParamsMap.containsKey("legalEntityId") ? queryParamsMap.get("legalEntityId") : null;
		StringBuilder filterString= new StringBuilder();
		
		if (type != null) {
			String[] types = type.split(",");
			filterString.append("(");
			for (int i = 0; i < types.length - 1; ++i) {
				filterString.append("Type_Name eq '" + types[i] + "'");
				filterString.append(" or ");
			}
			filterString.append("Type_Name eq '" + types[types.length - 1] + "')");
		}

		if (status != null) {
			if (filterString.length() != 0) {
				filterString.append(" and ");
			}

			String[] statuses = status.split(",");
			filterString.append("(");
			for (int i = 0; i < statuses.length - 1; ++i) {
				filterString.append("Status_id eq '" + statuses[i] + "'");
				filterString.append(" or ");
			}
			filterString.append("Status_id eq '" + statuses[statuses.length - 1] + "')");
		}
		
		if (legalEntity != null) {
			if (filterString.length() != 0) {
				filterString.append(" and ");
			}

			String[] legalEntities = legalEntity.split(",");
			filterString.append("(");
			for (int i = 0; i < legalEntities.length - 1; ++i) {
				filterString.append("companyLegalUnit eq '" + legalEntities[i] + "'");
				filterString.append(" or ");
			}
			filterString.append("companyLegalUnit eq '" + legalEntities[legalEntities.length - 1] + "')");
		}
		return filterString;
	}

	@Override
	public Result getCommonFeatureActions(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result =new Result();
		boolean status=false;
		
		try {
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>)inputArray[1];
		
		String serviceId= inputParams.get("serviceDefinitionId");
		String groupId= inputParams.get("groupId");
		String companyLegalUnit= inputParams.get("legalEntityId");
		
		if(StringUtils.isBlank(groupId)) {
			alert.prepareError("Group Id cannot be null or empty").log();
			ErrorCodeEnum.ERR_20569.setErrorCode(result);
			return result;
		}
		if(StringUtils.isBlank(serviceId)) {
			alert.prepareError("Servicedefinition Id cannot be null or empty").log();
			ErrorCodeEnum.ERR_22000.setErrorCode(result);
			return result;
		}
		if(StringUtils.isBlank(companyLegalUnit)) {
			companyLegalUnit = EnvironmentConfiguration.BRANCH_ID_REFERENCE.getValue(request);
		}
		status=customerRoleBusinessDelegate.checkGroupServicedefinitionAssociation(serviceId,groupId);
		if(status==false) {
			alert.prepareError("Customer Role, Servicedefinition association doesnot exist").log();
			ErrorCodeEnum.ERR_21999.setErrorCode(result);
			return result;
		}		
		
		MemberGroupDTO memberGroupdto = new MemberGroupDTO();
		ServiceDefinitionDTO serviceDefdto = new ServiceDefinitionDTO();
		serviceDefdto.setId(serviceId);
		memberGroupdto.setId(groupId);
		List<GroupFeatureActionViewDTO> groupactiondtos= customerRoleBusinessDelegate.getGroupFeatureActions(groupId, companyLegalUnit);
		List<ServiceDefinitionFeatureActionViewDTO> serviceactiondtos = servicedefinitionBusinessDelegate.getServiceDefinitionFeatureActions(serviceId);
		
		if ( groupactiondtos.size()==0 || serviceactiondtos.size()==0) {
			Dataset featuresDataset = new Dataset("features");
			result.addDataset(featuresDataset);
			return result;
		}
		
		result = getConvertedCommonFeatureActions(groupactiondtos, serviceactiondtos);
		result.addParam(new Param("roleId",groupId));
		result.addParam(new Param("servicedefinitionId",serviceId));

		}catch(Exception e) {
			alert.prepareError("Failed to fetch common features and actions list", e).log();
			ErrorCodeEnum.ERR_22001.setErrorCode(result);
		}
		return result;
	}


	private Result getConvertedCommonFeatureActions(List<GroupFeatureActionViewDTO> groupactiondtos,
			List<ServiceDefinitionFeatureActionViewDTO> serviceactiondtos) {
		
		Result result = new Result();
		Map<String, GroupFeatureActionViewDTO> groupMap = new HashMap<>();
		Map<String, ServiceDefinitionFeatureActionViewDTO> serviceMap = new HashMap<>();

		groupactiondtos.forEach((action)->{ 
			groupMap.put(getActionKey(action),action);
		});
		
		serviceactiondtos.forEach((action)->{
				serviceMap.put(getActionKey(action),action);
		});
		Map<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> features = new HashMap<>();
        try {
        	serviceactiondtos.forEach((serviceaction) -> {
			
           if("false".equalsIgnoreCase(serviceaction.getSoftdelete())) {
        	 String actionKey=getActionKey(serviceaction);
        	 
        	  if(groupMap.containsKey(actionKey)) {
        	   
        		 String value = null;
             	 if (StringUtils.isNotBlank(serviceaction.getLimitTypeId()) && StringUtils.isNotBlank(serviceaction.getValue())) 
     				value = String.valueOf(Math.min(Float.parseFloat(groupMap.get(actionKey).getValue()), Float.parseFloat(serviceMap.get(actionKey).getValue())));
	  
			if (features.containsKey(serviceaction.getFeatureId())) {
				Map<String, ServiceDefinitionFeatureActionViewDTO> actionsMap = features.get(serviceaction.getFeatureId());
				if (actionsMap.containsKey(serviceaction.getActionId())) {
					ServiceDefinitionFeatureActionViewDTO existingAction = actionsMap.get(serviceaction.getActionId());
					if (StringUtils.isNotBlank(serviceaction.getLimitTypeId())
							&& StringUtils.isNotBlank(serviceaction.getValue())) {
						existingAction.insertLimit(serviceaction.getLimitTypeId(), value );
					}
					if(StringUtils.isNotBlank(serviceaction.getDependentactionId()) && StringUtils.isNotBlank(serviceaction.getDependentActionName()) && StringUtils.isNotBlank(serviceaction.getDependentFeatureName()) ) {
						existingAction.insertDependentActions(serviceaction.getDependentactionId(), serviceaction.getDependentActionName(),serviceaction.getDependentFeatureName());
					}

				} else {
					if (StringUtils.isNotBlank(serviceaction.getLimitTypeId())
							&& StringUtils.isNotBlank(serviceaction.getValue())) {
						serviceaction.insertLimit(serviceaction.getLimitTypeId(), value);
					}
					if(StringUtils.isNotBlank(serviceaction.getDependentactionId()) && StringUtils.isNotBlank(serviceaction.getDependentActionName()) && StringUtils.isNotBlank(serviceaction.getDependentFeatureName()) ) {
						serviceaction.insertDependentActions(serviceaction.getDependentactionId(), serviceaction.getDependentActionName(),serviceaction.getDependentFeatureName());
					}
					actionsMap.put(serviceaction.getActionId(), serviceaction);
				}
			} else {
				Map<String, ServiceDefinitionFeatureActionViewDTO> actionsMap = new HashMap<>();
				if (StringUtils.isNotBlank(serviceaction.getLimitTypeId()) && StringUtils.isNotBlank(serviceaction.getValue())) {
					serviceaction.insertLimit(serviceaction.getLimitTypeId(), value);
				}
				if(StringUtils.isNotBlank(serviceaction.getDependentactionId()) && StringUtils.isNotBlank(serviceaction.getDependentActionName()) && StringUtils.isNotBlank(serviceaction.getDependentFeatureName()) ) {
					serviceaction.insertDependentActions(serviceaction.getDependentactionId(), serviceaction.getDependentActionName(),serviceaction.getDependentFeatureName());
				}
				actionsMap.put(serviceaction.getActionId(), serviceaction);
				features.put(serviceaction.getFeatureId(), actionsMap);
			}
        	  }
           }
		});
		result = getFormattedResult(features);
        	
        }catch(Exception e) {
        	alert.prepareError("Failed to fetch common features and actions list", e).log();
			ErrorCodeEnum.ERR_22001.setErrorCode(result);
        }
      return result;  
	}

	private Result getFormattedResult(Map<String, Map<String, ServiceDefinitionFeatureActionViewDTO>> features) {
		Result result = new Result();
		
		Dataset featuresDataset = new Dataset("features");
		Dataset limitsDataset= new Dataset("limits");

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
				}	
				action.addParam(new Param("actionId", a.getKey()));
				action.addParam(new Param("actionName", serviceFeatureActionView.getActionName()));
				action.addParam(new Param("accessPolicy", serviceFeatureActionView.getAccessPolicy()));
				action.addParam(new Param("accessPolicyId", serviceFeatureActionView.getAccessPolicyId()));
				action.addParam(new Param("actionLevel", serviceFeatureActionView.getActionlevel()));
				action.addParam(new Param("actionLevelId", serviceFeatureActionView.getActionlevelId()));
				action.addParam(new Param("actionDescription", serviceFeatureActionView.getActionName()));
				action.addParam(new Param("isAccountLevel", serviceFeatureActionView.getIsAccountLevel()));
				action.addParam(new Param ("actionStatus", serviceFeatureActionView.getActionStatus()));
				
				Dataset dependentActions = new Dataset("dependentActions");
				for (Map.Entry<String, Map<String,String>> l : a.getValue().getDependentFeatureAndActions().entrySet()) {
					for (Map.Entry<String, String> m : l.getValue().entrySet()) {
						Record dependentAction = new Record();
						dependentAction.addParam(new Param("id", m.getKey()));
						dependentAction.addParam(new Param("name",m.getValue()));
						dependentAction.addParam(new Param("featureName",l.getKey()));
						dependentActions.addRecord(dependentAction);
					}
					
				}
				action.addDataset(dependentActions);
				actions.addRecord(action);
				
			if(!serviceFeatureActionView.getLimits().isEmpty()) {
				Record lmtaction=new Record();
				limitfeature.addAllParams(feature.getAllParams());
				lmtaction.addAllParams(action.getAllParams());
				lmtaction.addParam(new Param("limitgroup", (!StringUtils.isBlank(serviceFeatureActionView.getLimitGroup()) ? serviceFeatureActionView.getLimitGroup() : "N/A")));
				lmtaction.addParam(new Param("limitgroupId", (!StringUtils.isBlank(serviceFeatureActionView.getLimitGroupId()) ? serviceFeatureActionView.getLimitGroupId() : "N/A")));
				Dataset limits = new Dataset("limits");
				for (Map.Entry<String, String> l : a.getValue().getLimits().entrySet()) {
					Record limit = new Record();
					limit.addParam(new Param("id", l.getKey()));
					limit.addParam(new Param("value", l.getValue()));
					limits.addRecord(limit);
				}
				if (limits.getAllRecords().size() > 0) {
					lmtaction.addDataset(limits);
				}
				limitActions.addRecord(lmtaction);
			}	
		}
			feature.addDataset(actions);
			featuresDataset.addRecord(feature);
			if(limitActions.getAllRecords().size()>0) {
				limitfeature.addDataset(limitActions);
				limitsDataset.addRecord(limitfeature);
			}
		}
		result.addDataset(featuresDataset);
		result.addDataset(limitsDataset);
		return result;
		
	}

	private String getActionKey(ServiceDefinitionFeatureActionViewDTO action) {
		if(!StringUtils.isBlank(action.getLimitTypeId()))
			 return constructKey(action.getActionId(),action.getLimitTypeId());
		else
			return action.getActionId();	
	}
	
	private String getActionKey(GroupFeatureActionViewDTO action) {
		if(!StringUtils.isBlank(action.getLimitTypeId()))
			 return constructKey(action.getActionId(),action.getLimitTypeId());
		else
			return action.getActionId();	
	}
	
	private boolean limitsAndPermissionsUpdate(String id,String actions,String removedActions, DataControllerRequest request) throws DBPAuthenticationException {
		 DBPServices.updateCustomerRoleLimitsAndPermissions(id, actions, removedActions, request);
        return true;
	}

}


