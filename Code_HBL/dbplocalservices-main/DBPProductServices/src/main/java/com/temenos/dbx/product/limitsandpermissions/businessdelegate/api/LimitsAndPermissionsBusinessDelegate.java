package com.temenos.dbx.product.limitsandpermissions.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.limitsandpermissions.dto.ActionLimitsDTO;
import com.temenos.dbx.product.limitsandpermissions.dto.ContractDTO;
import com.temenos.dbx.product.limitsandpermissions.dto.CustomerGroupDTO;

public interface LimitsAndPermissionsBusinessDelegate extends BusinessDelegate{
	
	/**
     * fetches the contracts ids related to the service definition id
     * @param String  service definition ID 
     * @return List {@link ContractDTO}
     */
	public List<ContractDTO> fetchContracts(String serviceDefinitionId);
	
	/**
	 * Edits the existing serviceDefinition action limits
	 * @param actionsDTO
	 * @return true if edit is successful
	 */
	public boolean editServiceDefinitionLimits(ActionLimitsDTO actionsDTO);
	
	/**
	 * Removes the actions from contracts and customers related to the service definition
	 * @param actionId denotes the action to be removed
	 * @param contractId denotes the contracts related to service definition
	 * @return true if remove is successful
	 */
	public boolean deleteServiceDefinitionActions(String actionId,String contractId);
	
	/**
     * fetches the customer ids related to the customer role
     * @param String  customer role ID 
     * @return List {@link CustomerGroupDTO}
     */
	public List<CustomerGroupDTO> fetchCustomerIds(String customerRoleId);
	
	/**
	 * Edits the existing customer action limits related to customer role
	 * @param actionsDTO
	 * @return true if edit is successful
	 */
	public boolean editCustomerRoleLimits(ActionLimitsDTO actionsDTO);
	
	/**
	 * Removes the actions from contracts and customers related to the service definition
	 * @param actionId denotes the action to be removed
	 * @param CustomerId denotes the customers related to customer role
	 * @return true if remove is successful
	 */
	public boolean deleteCustomerRoleActions(String actionId,String customerId);

}
