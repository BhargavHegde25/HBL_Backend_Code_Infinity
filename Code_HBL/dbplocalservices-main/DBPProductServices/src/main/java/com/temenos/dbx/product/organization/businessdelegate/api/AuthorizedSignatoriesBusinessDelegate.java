package com.temenos.dbx.product.organization.businessdelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;

public interface AuthorizedSignatoriesBusinessDelegate extends BusinessDelegate {

    public List<MembershipOwnerDTO> getAuthorizedSignatories(String cif, Map<String, Object> headerMap)
            throws ApplicationException;

    public List<MembershipOwnerDTO> getAuthorizedSignatoriesByTaxId(String taxID, String companyName,
            Map<String, Object> headerMap) throws ApplicationException;

    public MembershipOwnerDTO verifyGivenAuthorizerDetailsAndGetData(String firstName, String lastName, String ssn,
            String dateOfBirth, List<MembershipOwnerDTO> dtoList);

    public void filterAuthorizedSignatoriesListByExistance(String organizationId, List<MembershipOwnerDTO> dtoList,
            Map<String, Object> headerMap) throws ApplicationException;

    public Set<MembershipOwnerDTO> filterAuthorizedSignatories(String name, String ssn, String dateOfBirth,
            String organizationId, List<MembershipOwnerDTO> dtoList, Map<String, Object> headerMap)
            throws ApplicationException;

    public JsonObject processDTOAndGetCompanyDetails(MembershipOwnerDTO dto, Map<String, Object> headerMap)
            throws ApplicationException;

    public void sendMailToAuthorizedSignatoriesInAccountCentricFlow(List<AccountsDTO> inputDTO, String orgStatus,
            Map<String, Object> headersMap) throws ApplicationException;

    public void sendMailToAuthorizedSignatoriesInCustomerCentricFlow(List<ContractCoreCustomersDTO> dtoList,
            String contractStatus, Map<String, Object> headersMap) throws ApplicationException;
}
