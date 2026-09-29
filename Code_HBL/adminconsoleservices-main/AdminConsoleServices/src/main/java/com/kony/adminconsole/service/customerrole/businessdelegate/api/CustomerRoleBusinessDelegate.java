package com.kony.adminconsole.service.customerrole.businessdelegate.api;

import java.util.List;
import com.dbp.core.api.BusinessDelegate;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.customerrole.dto.CustomerGroupsViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupActionLimitDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupFeatureActionViewDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupServiceDefinitionDTO;
import com.kony.adminconsole.service.customerrole.dto.MemberGroupDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.service.customerrole.dto.GroupsViewDTO;

/**
 * @author kruthi.manojna
 *
 */
public interface CustomerRoleBusinessDelegate extends BusinessDelegate {
	

	/**
	 * @param groupId
	 * @return JSONArray of all feature action limits for given GroupId
	 */
	public List<GroupFeatureActionViewDTO> getGroupFeatureActions(String groupId, String companyLegalUnit);

	/**
	 * @return list of GroupsViewDTO containing all the group information
	 */
	public List<GroupsViewDTO> getAllGroups(String companyLegalUnit);

	/**
	 * @param groupId
	 * @return JsonArray of service Definitions associated to a GroupId
	 */
	public List<GroupServiceDefinitionDTO> getServiceDefinitionsForGroup(String groupId);

	/**
	 * @param memberGroupdto containing all the details of a group
	 * @return memberGroupdto with info in case of success otherwise null
	 */
	public MemberGroupDTO createGroup(MemberGroupDTO memberGroupdto);

	/**
	 * @param actionLimitDTO containing all the details of a group and its limits and actions
	 * @return actionLimitDTO  with info in case of success otherwise null
	 */
	public GroupActionLimitDTO editGroupActionLimit(GroupActionLimitDTO actionLimitDTO);

	/**
	 * @param actionLimitDTO containing all the details of a group and its limits and actions
	 * @return actionLimitDTO  with info in case of success otherwise null
	 */
	public GroupActionLimitDTO createGroupActionLimit(GroupActionLimitDTO actionLimitDTO);

	/**
	 * deletes Customer group 
	 * @param memberGroupdto 
	 * @return flag true if success otherwise false
	 */
	public boolean deleteGroup(MemberGroupDTO memberGroupdto);

	/**
	 * gets all actions based on role type
	 * @param roleTypeId
	 * @return List of FeatureActionRoleTypeDTOs
	 */
	public List<FeatureActionRoleTypeDTO> getValidRoleTypeActions(String roleTypeId, String companyLegalUnit);

	/**
	 * fetches master data of action limits
	 * @return List of ActionLimitDTOs
	 */
	public List<ActionLimitDTO> fetchActionLimits(String companyLegalUnit);

	/**
	 * 
	 * @param groupServiceDefinition
	 * @return groupServiceDefinition with info in case of success otherwise null
	 */
	public GroupServiceDefinitionDTO createGroupServiceDefinition(GroupServiceDefinitionDTO groupServiceDefinition);

	/**
	 * gets only service Definitions which are of same roletype
	 * @param roleType
	 * @return List of ServiceDefinitionDTO
	 */
	public List<ServiceDefinitionDTO> getServiceDefinitionsByType(String roleType, String companyLegalUnit);

	/**
	 * gets all service Definitions 
	 * @return List of ServiceDefinitionDTO
	 */
	public List<ServiceDefinitionDTO> getServiceDefinitions();
	
	/**
	 * edits basic details of a Customer Role 
	 * @return MemberGroupDTO
	 */
	public MemberGroupDTO editGroup(MemberGroupDTO serviceDefinitionDTO);
	
	/**
	 * gets all actions and limits for a customer role 
	 * @return List of GroupActionLimitDTO
	 */
	public List<GroupActionLimitDTO> getGroupActionLimit(MemberGroupDTO memberGroupdto);
	
	/**
	 * gets all the details of a customer role from Groups_view by Group_id 
	 * @return GroupsViewDTO
	 */
	public GroupsViewDTO getGroupById(String id);
	
	/**
	 * Removes action assigned to a customer role 
	 * @return true in case of success else false
	 */
	public boolean deleteGroupActionLimit(GroupActionLimitDTO actionLimitDTO);
	
	/**
	 * Removes service definition associated to a customer role 
	 * @return true in case of success else false
	 */
	public boolean deleteGroupServiceDefinition(GroupServiceDefinitionDTO groupservicedto);

	/**
	 * Updates default role for a service definition associated one/more customer roles 
	 * @return true in case of success else false
	 */	
	public boolean editDefaultGroupServiceDefinition(GroupServiceDefinitionDTO groupServiceDefinition);
	
	/**
	 * fetches all the role and service definition associations 
	 * @return List of all GroupServiceDefinitionDTO
	 */	
	public List<GroupServiceDefinitionDTO> fetchGroupServiceDefinitions(String companyLegalUnit);

	/**
	 * fetches all the roles from Groups_view with filter applied 
	 * @return List of GroupsViewDTO
	 */	
	public List<GroupsViewDTO> fetchGroupsViewWithFilter(String filter);
	
	/**
	 * checks if a role is associated to a service definition
	 * @param serviceId - service definition ID
	 * @param groupId - customer role ID
	 * @return true if association exists
	 */
	public boolean checkGroupServicedefinitionAssociation(String serviceId, String groupId);

	/**
	 * fetches the count of customers associated to a particular customer role and Service definition
	 * @return list of CustomerGroupsViewDTO
	 */
	public List<CustomerGroupsViewDTO> getCustomerGroupsView(String companyLegalUnit);

	public List<ServiceDefinitionDTO> getServiceDefinitions(String companyLegalUnit);

	

}
