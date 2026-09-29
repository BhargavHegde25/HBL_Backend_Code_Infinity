package com.temenos.dbx.eum.product.contract.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;

public interface CoreCustomerBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param membershipDTO
     * @param headersMap
     * @return DBXResult.response containing JSONArray of records :
     *         [{id,name,firstName,lastName,addressLine1,addressLine2,cityName,country,zipCode,state,phone,email,taxId,industry}]
     * @throws ApplicationException
     */
    public DBXResult searchCoreCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
            throws ApplicationException;
    /**
     * 
     * @param membershipDTOs
     * @param headersMap
     * @return DBXResult.response containing JSONArray of records :
     *         [{id,name,firstName,lastName,addressLine1,addressLine2,cityName,country,zipCode,state,phone,email,taxId,industry}]
     * @throws ApplicationException
     */

    public DBXResult searchCoreCustomers(List<MembershipDTO> membershipDTOs, Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param membershipDTO
     * @param headersMap
     * @return DBXResult.response containing JSONArray of records :
     *         [{id,name,firstName,lastName,addressLine1,addressLine2,cityName,
     *         country,zipCode,state,phone,email,relationshipId,relationshipName,industry}]
     * @throws ApplicationException
     */
    public DBXResult getCoreRelativeCustomers(MembershipDTO membershipDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param membershipDTO
     * @param headersMap
     * @return DBXResult.response containing JSONArray of records :
     * @throws ApplicationException
     */
    public DBXResult getCoreCustomerAccounts(MembershipDTO membershipDTO, Map<String, Object> headersMap)
            throws ApplicationException;

    public MembershipDTO getMembershipDetails(String membershipId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public List<AllAccountsViewDTO> getCoreCustomerAccounts(List<MembershipDTO> membershipList,
            Map<String, Object> headersMap) throws ApplicationException;
}
