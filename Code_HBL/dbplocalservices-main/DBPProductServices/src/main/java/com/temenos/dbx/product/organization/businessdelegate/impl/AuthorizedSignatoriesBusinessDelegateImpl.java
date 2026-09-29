package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountHolderBackendDelegate;
import com.temenos.dbx.product.businessdelegate.api.KMSBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.contract.backenddelegate.api.CoreCustomerBackendDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.dto.OrganisationEmployeesViewDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.AuthorizedSignatoriesBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.MembershipBusinessDelegate;

public class AuthorizedSignatoriesBusinessDelegateImpl implements AuthorizedSignatoriesBusinessDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<MembershipOwnerDTO> getAuthorizedSignatories(String cif, Map<String, Object> headerMap)
            throws ApplicationException {

        CoreCustomerBackendDelegate coreCsutomerBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CoreCustomerBackendDelegate.class);

        List<MembershipOwnerDTO> resultList = new ArrayList<>();
        MembershipDTO dto = new MembershipDTO();
        dto.setId(cif);
        DBXResult result = coreCsutomerBD.getCoreRelativeCustomers(dto, headerMap);
        if (null != result && result.getResponse() != null) {
            JsonArray relativeCustomers = (JsonArray) result.getResponse();
            for (JsonElement element : relativeCustomers) {
                JsonObject coreCustomerJson = element.isJsonObject() ? element.getAsJsonObject() : new JsonObject();
                String id = coreCustomerJson.has("id") ? coreCustomerJson.get("id").getAsString() : "";
                String firstName =
                        coreCustomerJson.has("firstName") ? coreCustomerJson.get("firstName").getAsString() : "";
                String lastName =
                        coreCustomerJson.has("lastName") ? coreCustomerJson.get("lastName").getAsString() : "";
                String dateOfBirth =
                        coreCustomerJson.has("dateOfBirth") ? coreCustomerJson.get("dateOfBirth").getAsString() : "";
                String taxId =
                        coreCustomerJson.has("taxId") ? coreCustomerJson.get("taxId").getAsString() : "";
                String phone =
                        coreCustomerJson.has("phone") ? coreCustomerJson.get("phone").getAsString() : "";
                String email =
                        coreCustomerJson.has("email") ? coreCustomerJson.get("email").getAsString() : "";
                if (StringUtils.isNotBlank(id)) {
                    MembershipOwnerDTO ownerDTO = new MembershipOwnerDTO();
                    ownerDTO.setId(id);
                    ownerDTO.setMembershipId(cif);
                    ownerDTO.setFirstName(firstName);
                    ownerDTO.setLastName(lastName);
                    ownerDTO.setDateOfBirth(dateOfBirth);
                    ownerDTO.setTaxId(taxId);
                    ownerDTO.setPhone(phone);
                    ownerDTO.setEmail(email);
                    resultList.add(ownerDTO);
                }
            }
        }

        return resultList;
    }

    @Override
    public List<MembershipOwnerDTO> getAuthorizedSignatoriesByTaxId(String taxID, String companyName,
            Map<String, Object> headerMap) throws ApplicationException {
        List<MembershipOwnerDTO> dtoList = new ArrayList<>();
        MembershipDTO membershipDTO = null;
        try {
            if (StringUtils.isBlank(taxID) || StringUtils.isBlank(companyName)) {
                return dtoList;
            }
            String cif = null;
            MembershipBusinessDelegate membershipBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(MembershipBusinessDelegate.class);
            AuthorizedSignatoriesBusinessDelegate authorizedSignatoryBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);

            membershipDTO = membershipBD.getMembershipDetailsByTaxid(taxID, companyName, headerMap);
            if (null != membershipDTO && StringUtils.isNotBlank(membershipDTO.getId())) {
                cif = membershipDTO.getId();
            } else {
                return dtoList;
            }
            dtoList = authorizedSignatoryBD.getAuthorizedSignatories(cif, headerMap);

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10230);
        }
        return dtoList;
    }

    @Override
    public MembershipOwnerDTO verifyGivenAuthorizerDetailsAndGetData(String firstName, String lastName, String ssn,
            String dateOfBirth, List<MembershipOwnerDTO> dtoList) {
        if (null == dtoList) {
            return null;
        }
        for (MembershipOwnerDTO dto : dtoList) {
            String authorizerFirstName = dto.getFirstName();
            String authorizerLastName = dto.getLastName();
            String authorizerDOB = dto.getDateOfBirth();
            String authorizerSsn = dto.getTaxId();

            if (firstName.equalsIgnoreCase(authorizerFirstName) && lastName.equalsIgnoreCase(authorizerLastName)
                    && ssn.equalsIgnoreCase(authorizerSsn) && dateOfBirth.equalsIgnoreCase(authorizerDOB)) {
                return dto;

            }
        }
        return null;
    }

    @Override
    public void filterAuthorizedSignatoriesListByExistance(String organizationId, List<MembershipOwnerDTO> dtoList,
            Map<String, Object> headerMap) throws ApplicationException {
        OrganizationEmployeesBusinessDelegate orgEmployeeBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(OrganizationEmployeesBusinessDelegate.class);

        OrganisationEmployeesViewDTO ogEmployeeDTO = new OrganisationEmployeesViewDTO();
        ogEmployeeDTO.setOrgemp_orgid(organizationId);

        List<OrganisationEmployeesViewDTO> empDtoList = orgEmployeeBD.getOrganizationEmployees(ogEmployeeDTO,
                headerMap);

        for (OrganisationEmployeesViewDTO dto : empDtoList) {
            String ssn = dto.getSsn();
            String dateOfBirth = dto.getDateOfBirth();

            for (MembershipOwnerDTO ownerDto : new ArrayList<>(dtoList)) {
                if (ownerDto.getSsn().equals(ssn) && ownerDto.getDateOfBirth().equals(dateOfBirth)) {
                    dtoList.remove(ownerDto);
                }
            }
        }

    }

    @Override
    public Set<MembershipOwnerDTO> filterAuthorizedSignatories(String name, String ssn, String dateOfBirth,
            String organizationId, List<MembershipOwnerDTO> dtoList, Map<String, Object> headerMap)
            throws ApplicationException {
        Set<MembershipOwnerDTO> filteredList = new HashSet<>();

        AuthorizedSignatoriesBusinessDelegate authorizedSignatoryBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);

        authorizedSignatoryBD.filterAuthorizedSignatoriesListByExistance(organizationId, dtoList, headerMap);

        if (StringUtils.isBlank(name) && StringUtils.isBlank(ssn) && StringUtils.isBlank(dateOfBirth)) {
            return new HashSet<>(dtoList);
        }
        for (MembershipOwnerDTO dto : dtoList) {
            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(dto.getFirstName())
                    && StringUtils.contains(dto.getFirstName(), name)) {
                filteredList.add(dto);
            }
            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(dto.getLastName())
                    && StringUtils.contains(dto.getLastName(), name)) {
                filteredList.add(dto);
            }
            if (StringUtils.isNotBlank(ssn) && StringUtils.isNotBlank(dto.getSsn())
                    && StringUtils.contains(dto.getSsn(), ssn)) {
                filteredList.add(dto);
            }
            if (StringUtils.isNotBlank(dateOfBirth) && StringUtils.isNotBlank(dto.getDateOfBirth())
                    && dto.getDateOfBirth().equals(dateOfBirth)) {
                filteredList.add(dto);
            }
        }
        return filteredList;
    }

    @Override
    public JsonObject processDTOAndGetCompanyDetails(MembershipOwnerDTO dto, Map<String, Object> headerMap)
            throws ApplicationException {
        JsonObject companyDetailsJson = new JsonObject();
        if (null != dto && null != dto.getMembershipDTO()) {
            companyDetailsJson.addProperty("taxId", dto.getMembershipDTO().getTaxId());
            companyDetailsJson.addProperty("membershipId", dto.getMembershipId());
            companyDetailsJson.addProperty("companyName", dto.getMembershipDTO().getName());
        }
        return companyDetailsJson;
    }

    @Override
    public void sendMailToAuthorizedSignatoriesInAccountCentricFlow(List<AccountsDTO> orgAccounts, String orgStatus,
            Map<String, Object> headersMap) throws ApplicationException {
        String emailTemplate = getEmailTemplate(orgStatus);
        try {
            for (AccountsDTO childDTO : orgAccounts) {
                AccountHolderBackendDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                        .getBackendDelegate(AccountHolderBackendDelegate.class);
                List<MembershipOwnerDTO> accountHolderDTO = businessDelegate
                        .getOrganizationAccountHolderDetails(childDTO, headersMap);
                for (MembershipOwnerDTO dto : accountHolderDTO) {
                    Map<String, Object> input = new HashMap<>();
                    input.put("Subscribe", "true");
                    input.put("FirstName", dto.getFirstName());
                    input.put("EmailType", emailTemplate);
                    input.put("LastName", dto.getLastName());
                    input.put("Email", dto.getEmail());
                    JSONObject addContext = new JSONObject();
                    input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
                    KMSBusinessDelegate kmsBD = DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(KMSBusinessDelegate.class);
                    kmsBD.sendKMSEmail(input, headersMap);
                }
            }
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while sending mail to authorized signatories").log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while sending mail to authorized signatories").log();
        }
    }

    private String getEmailTemplate(String contractStatus) {
        if (DBPUtilitiesConstants.CONTRACT_STATUS_ACTIVE.equalsIgnoreCase(contractStatus))
            return DBPUtilitiesConstants.ORGANIZATION_STATUS_ACTIVE_EMAIL_TEMPLATE;
        else if (DBPUtilitiesConstants.CONTRACT_STATUS_PENDING.equalsIgnoreCase(contractStatus))
            return DBPUtilitiesConstants.ORGANIZATION_STATUS_PENDING_EMAIL_TEMPLATE;
        else
            return DBPUtilitiesConstants.ORAGANIZATION_STATUS_REJECTED_EMAIL_TEMPLATE;
    }

    @Override
    public void sendMailToAuthorizedSignatoriesInCustomerCentricFlow(List<ContractCoreCustomersDTO> dtoList,
            String contractStatus, Map<String, Object> headersMap) throws ApplicationException {
        String emailTemplate = getEmailTemplate(contractStatus);
        try {
            for (ContractCoreCustomersDTO coreCustomerDTO : dtoList) {
                CoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(CoreCustomerBusinessDelegate.class);

                List<MembershipDTO> relativeCustomers =
                        coreCustomerBD.getCoreRelativeCustomers(coreCustomerDTO.getCoreCustomerId(), headersMap);

                for (MembershipDTO dto : relativeCustomers) {
                    Map<String, Object> input = new HashMap<>();
                    input.put("Subscribe", "true");
                    input.put("FirstName", dto.getFirstName());
                    input.put("EmailType", emailTemplate);
                    input.put("LastName", dto.getLastName());
                    input.put("Email", dto.getEmail());
                    JSONObject addContext = new JSONObject();
                    addContext.put("cif", dto.getId());
                    input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
                    KMSBusinessDelegate kmsBD = DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(KMSBusinessDelegate.class);
                    kmsBD.sendKMSEmail(input, headersMap);
                }
            }
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while sending mail to authorized signatories").log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while sending mail to authorized signatories").log();
        }
    }
}
