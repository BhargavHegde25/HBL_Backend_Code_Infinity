package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.MemberGroupDTO;

public interface CustomerGroupBusinessDelegate extends BusinessDelegate {

	public CustomerGroupDTO createCustomerGroup(CustomerGroupDTO dto, Map<String, Object> headersMap)
			throws ApplicationException;

	/**
	 * 
	 * @param dto
	 * @param headersMap
	 * @return
	 * @throws ApplicationException
	 */
	public List<CustomerGroupDTO> getCustomerGroup(CustomerGroupDTO dto, Map<String, Object> headersMap)
			throws ApplicationException;

	/**
	 * 
	 * @param inputDTO
	 * @param headersMap
	 * @return
	 * @throws ApplicationException
	 */
	public MemberGroupDTO getMemberType(MemberGroupDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException;
}
