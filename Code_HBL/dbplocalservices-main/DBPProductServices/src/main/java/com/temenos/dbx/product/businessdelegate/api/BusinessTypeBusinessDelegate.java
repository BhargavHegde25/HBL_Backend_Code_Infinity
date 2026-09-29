package com.temenos.dbx.product.businessdelegate.api;

import java.util.List;
import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.BusinessTypeDTO;
import com.temenos.dbx.product.dto.BusinessTypeRoleDTO;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 *
 */
public interface BusinessTypeBusinessDelegate extends BusinessDelegate {

	/**
	 * 
	 * @param headersMap
	 * @return list of business types
	 * @throws ApplicationException 
	 */
	public List<BusinessTypeDTO> getBusinessType(Map<String, Object> headersMap) throws ApplicationException;

	/**
	 * 
	 * @param inputDTO
	 * @param headersMap
	 * @return list of business type roles
	 * @throws ApplicationException 
	 */
	public List<BusinessTypeRoleDTO> getBusinessTypeRoles(BusinessTypeRoleDTO inputDTO, Map<String, Object> headersMap) throws ApplicationException;

}
