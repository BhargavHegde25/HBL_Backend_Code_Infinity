package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.BusinessSignatoryDTO;

public interface BusinessSignatoryBusinessDelegate extends BusinessDelegate {

	/**
	 * 
	 * @param inputDTO
	 * @param headersMap
	 * @return business signatory list
	 * @throws ApplicationException
	 */
	public List<BusinessSignatoryDTO> getBusinessSignatories(BusinessSignatoryDTO inputDTO,
			Map<String, Object> headersMap) throws ApplicationException;

}
