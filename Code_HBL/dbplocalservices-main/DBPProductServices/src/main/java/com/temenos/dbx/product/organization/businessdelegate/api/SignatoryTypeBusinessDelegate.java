package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.Map;
import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.SignatoryTypeDTO;

public interface SignatoryTypeBusinessDelegate extends BusinessDelegate {

	/**
	 * 
	 * @param inputDTO
	 * @param headersMap
	 * @return
	 * @throws ApplicationException
	 */
	public SignatoryTypeDTO getSignatoryTypes(SignatoryTypeDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException;

}
