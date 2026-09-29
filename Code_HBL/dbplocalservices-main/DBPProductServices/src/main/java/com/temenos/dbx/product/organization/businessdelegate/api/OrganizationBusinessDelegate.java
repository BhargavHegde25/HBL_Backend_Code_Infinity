package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.OrganisationDTO;
import com.temenos.dbx.product.dto.OrganizationMembershipDTO;

/**
 * 
 * @author Infinity DBX
 *
 */
public interface OrganizationBusinessDelegate extends BusinessDelegate {

    /**
     * 
     * @param statusId
     * @param headerMap
     * @return List<OrganisationDTO>
     * @throws ApplicationException
     */
    public List<OrganisationDTO> getListOfOrganisationsByStatus(String statusId, Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param inputDTO
     * @param headerMap
     * @return update status
     * @throws ApplicationException
     */
    public boolean updateOrganizationStatus(OrganisationDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param inputDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public List<OrganizationMembershipDTO> getOrganizationMembershipDetails(OrganizationMembershipDTO inputDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param inputDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public OrganisationDTO getOrganization(OrganisationDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public OrganisationDTO createOrganization(OrganisationDTO inputDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public OrganisationDTO getOrganizationByName(String organizationName, Map<String, Object> headersMap)
            throws ApplicationException;

}
