package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerBusinessTypeDTO;

public interface CustomerBusinessTypeBusinessDelegate extends BusinessDelegate {

    public CustomerBusinessTypeDTO createCustomerBusinessType(CustomerBusinessTypeDTO dto,
            Map<String, Object> headersMap) throws ApplicationException;

}
