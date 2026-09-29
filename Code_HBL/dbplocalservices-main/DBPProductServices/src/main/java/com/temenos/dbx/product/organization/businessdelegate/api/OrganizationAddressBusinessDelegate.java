package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.OrganizationAddressDTO;

public interface OrganizationAddressBusinessDelegate extends BusinessDelegate {

    public OrganizationAddressDTO createOrganizationAddress(OrganizationAddressDTO dto, Map<String, Object> headersMap)
            throws ApplicationException;

}
