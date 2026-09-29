package com.temenos.dbx.eum.product.contract.businessdelegate.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ContractAccountsDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;

public interface CoreCustomerBusinessDelegate extends BusinessDelegate {

    /**
     * @author sowmya.vandanapu
     * @version 1.0
     * @since 2021.01
     * @param inputParams
     * @param headersMap
     * @return DBXResult.response of searched customers
     * @throws ApplicationException
     */
    public DBXResult searchCoreCustomers(Map<String, String> configurations, MembershipDTO membershipDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * @author sowmya.vandanapu
     * @version 1.0
     * @since 2021.01
     * @param inputParams
     * @param headersMap
     * @return DBXResult.response of core customer details and relative customer details
     * @throws ApplicationException
     */
    public DBXResult getCoreRelativeCustomers(Map<String, String> configurations, MembershipDTO membershipDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * @author sowmya.vandanapu
     * @version 1.0
     * @since 2021.01
     * @param inputParams
     * @param headersMap
     * @return DBXResult.response of customer accounts
     * @throws ApplicationException
     */
    public DBXResult getCoreCustomerAccounts(String coreCustomerIdList, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    public boolean checkIfTheCoreCustomerIsEnrolled(String coreCustomerId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public List<MembershipDTO> getCoreRelativeCustomers(String membershipId, String legalEntityId, Map<String, Object> headersMap)
            throws ApplicationException;

    public MembershipDTO getMembershipDetails(String membershipId, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public MembershipDTO getMembershipDetailsByTaxid(String taxID, String companyName, String legalEntityId, Map<String, Object> headerMap)
            throws ApplicationException;

    public MembershipDTO verifyGivenAuthorizerDetailsAndGetData(String firstName, String lastName, String ssn,
            String dateOfBirth, List<MembershipDTO> dtoList);

    public JSONObject getCompanyAccounts(JsonObject serviceKeyPayload, String legalEntityId, JsonObject masterServiceKeyPayload,
            String serviceKey, String masterServiceKey, Map<String, Object> headersMap)
            throws ApplicationException, IOException;

    public Map<String, List<ContractAccountsDTO>> getAccountsWithImplicitAccountAccess(Set<String> corecustomers,
            String customerId, String legalEntityId, Map<String, Object> headersMap) throws ApplicationException;

    public List<MembershipDTO> getMembershipDetails(String dateofbirth, String legalEntityId, String email, String phone,
            Map<String, Object> headerMap) throws ApplicationException;

}
