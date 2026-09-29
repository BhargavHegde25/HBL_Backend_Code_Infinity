package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.OrganizationCommunicationDTO;

public interface OrganizationCommunicationBusinessDelegate extends BusinessDelegate {

    public OrganizationCommunicationDTO createOrganizationCommunication(OrganizationCommunicationDTO dto,
            Map<String, Object> headersMap) throws ApplicationException;

}
