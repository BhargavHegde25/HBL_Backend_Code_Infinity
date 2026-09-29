package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.OrganizationMembershipDTO;

public interface OrganizationMembershipBusinessDelegate extends BusinessDelegate {

    public OrganizationMembershipDTO createOrganizationMembership(OrganizationMembershipDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException;

}
