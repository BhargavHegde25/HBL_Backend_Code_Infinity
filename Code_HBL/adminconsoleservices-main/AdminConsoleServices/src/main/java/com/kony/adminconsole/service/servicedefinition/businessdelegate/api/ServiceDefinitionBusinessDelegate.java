package com.kony.adminconsole.service.servicedefinition.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.kony.adminconsole.service.servicedefinition.dto.ActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.FeatureActionRoleTypeDTO;
import com.kony.adminconsole.service.servicedefinition.dto.MemberGroupDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionActionLimitDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionFeatureActionViewDTO;
import com.kony.adminconsole.service.servicedefinition.dto.ServiceDefinitionGroupDTO;


/**
 * Handles all the operations on ServiceDefinition
 * @author KH2660
 * extends {@link BusinessDelegate}
 */

/**
 * @author kruthi.manojna
 *
 */
public interface ServiceDefinitionBusinessDelegate extends BusinessDelegate{

	/**
     * Deletes service definition from the table for the given service definition id
     * @param ServiceDefinitionDTO serviceDefinitionDTO - contains details for service definition
     * @return boolean
     */
	public boolean deleteServiceDefinition(ServiceDefinitionDTO serviceDefinitionDTO);
	
	/**
	 * Returns true if service definition is associated to any contract else false.
	 * @param serviceDefinitionId
	 * @return boolean
	 */
	public boolean isAssociatedToContract(String serviceDefinitionId);
	
	/**
	 * Returns the list of service definition records
	 * @return List of {@link ServiceDefinitionDTO}
	 */
	public List<ServiceDefinitionDTO> fetchAllServiceDefinition(String typeId,String companyLegalUnit);
	
	/**
     * creates a service definition 
     * @param ServiceDefinitionDTO serviceDefinitionDTO - contains details for service definition
     * @return {@link ServiceDefinitionDTO}
     */
	public ServiceDefinitionDTO createServiceDefinition(ServiceDefinitionDTO serviceDefinitionDTO);
	
	/**
     * creates a service definition actions along with the limits
     * @param ServiceDefinitionActionLimitDTO ServiceDefinitionActionLimitDTO - contains details for service definition
     * @return {@link ServiceDefinitionActionLimitDTO}
     */
	public ServiceDefinitionActionLimitDTO createServiceDefinitionActionLimit(ServiceDefinitionActionLimitDTO serviceDefinitionActionLimitDTO);
	
	
	/**
	 * Returns the list of groups of the given type
	 * @return List of {@link MemberGroupDTO}
	 */
	public List<MemberGroupDTO> fetchAllGroups(String typeId);
	
	
	/**
     * creates a service definition record associated to group in the service definition group table
     * @param ServiceDefinitionGroupDTO ServiceDefinitionGroupDTO - contains details for service definition group
     * @return {@link ServiceDefinitionGroupDTO}
     */
	public ServiceDefinitionGroupDTO createServiceDefinitionGroup(ServiceDefinitionGroupDTO serviceDefinitionGroupDTO);
	
	
	/**
	 * Fetches the action along with the limits 
	 * @return List of {@link ActionLimitDTO}
	 */
	public List<ActionLimitDTO> fetchActionLimits(String legalEntityId);
	
	/**
	 * Fetches the feature actions of the given servicedefinition id
	 * @param serviceDefinitionId
	 * @return
	 */
	public List<ServiceDefinitionFeatureActionViewDTO> getServiceDefinitionFeatureActions(String serviceDefinitionId);
	
	/**
     * edits a service definition details of the given id
     * @param ServiceDefinitionDTO serviceDefinitionDTO - contains details for service definition
     * @return {@link ServiceDefinitionDTO}
     */
	public ServiceDefinitionDTO editServiceDefinition(ServiceDefinitionDTO serviceDefinitionDTO);
	
	/**
	 * Fetches the service definition action limits for the given id.
	 * @param ServiceDefinitionDTO serviceDefinitionDTO - contains details for service definition
	 * @return List of {@link ServiceDefinitionActionLimitDTO}
	 */
	public List<ServiceDefinitionActionLimitDTO> fetchServiceDefinitionActionLimit(ServiceDefinitionDTO serviceDefinitionDTO);
	
	
	/**
     * edits a service definition action limits of the given service definition id
     * @param ServiceDefinitionDTO serviceDefinitionDTO - contains details for service definition
     * @return {@link ServiceDefinitionDTO}
     */
	public ServiceDefinitionActionLimitDTO editServiceDefinitionActionLimit(ServiceDefinitionActionLimitDTO serviceDefinitionDTO);
	
	/**
	 * Fetches the service definition details
	 * @return List of {@link ServiceDefinitionDTO }
	 */
	public List<ServiceDefinitionDTO> fetchServiceDefinition();
	
    /**
     * Fetches featureactions of given role type id
     * @param String roleTypeId
     * @return List of {â€‹â€‹â€‹â€‹â€‹â€‹â€‹@link FeatureActionRoleTypeDTO}â€‹â€‹â€‹â€‹â€‹â€‹â€‹
     */
    public List<FeatureActionRoleTypeDTO> getValidRoleTypeActions(String roleTypeId);

	/**
	 * fetches all roles associations for a service definition
	 * @param sid - service definition id
	 * @return List of ServiceDefinitionGroupDTO
	 */
	public List<ServiceDefinitionGroupDTO> fetchAllRolesForServiceDefinition(String sid);

	/**
	 * fetches a service definition details by id
	 * @param id - servicedefinition id
	 * @return ServiceDefinitionDTO
	 */
	public ServiceDefinitionDTO getServiceDefinitionById(String id);

	/**
	 * fetches default role for a service definition
	 * @param id - servicedefinition id
	 * @return ServiceDefinitionGroupDTO
	 */
	public ServiceDefinitionGroupDTO getDefaultRoleForServiceDefinition(String id);

	/**
	 * updates default role associated to a service definition
	 * @param groupServiceDefinition
	 * @return boolean
	 */
	public boolean editDefaultGroupServiceDefinition(ServiceDefinitionGroupDTO groupServiceDefinition);

	/**
	 * deletes action from servicedefinitionactionlimit table
	 * @param actionLimitDTO
	 * @return boolean
	 */
	public boolean deleteServiceDefinitionActionLimit(ServiceDefinitionActionLimitDTO actionLimitDTO);

	/**
	 * fetches all active service definitions which have at least one active role associated
	 * @return List of {@link ServiceDefinitionDTO }
	 */
	public List<ServiceDefinitionDTO> fetchAllServiceDefinitionsForContract(String legalEntityId);
	
	/**
	 * Fetches the limits and actions of the given servicedefinition id
	 * @param serviceDefinitionId
	 * @return
	 */
	public List<ServiceDefinitionFeatureActionViewDTO> getServiceDefinitionMonetaryActions(String serviceDefinitionId, String legalEntityId);

	/**
	 * fetches min transaction limits of all actions from master data
	 * @return List of {@link ActionLimitDTO } 
	 */
	public List<ActionLimitDTO> getMinTransactionLimits();
	
	/**
	 * fetches servicedefinition based on search text
	 * @return List of servicedefinition 
	 */
	public JSONObject searchServiceDefinition(String searchText, String limit);

	public JSONObject getServiceDefinitionProductIdPermissions(Map<String, Object> postParametersMap,
			String dbpServicesClaimsToken) throws DBPApplicationException;

}