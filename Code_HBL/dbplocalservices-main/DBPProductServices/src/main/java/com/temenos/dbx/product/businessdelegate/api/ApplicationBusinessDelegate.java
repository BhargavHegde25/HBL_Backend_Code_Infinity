package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ApplicationDTO;

public interface ApplicationBusinessDelegate extends BusinessDelegate {

	public ApplicationDTO getApplicationProperties(Map<String, Object> headersMap) throws ApplicationException;

}
