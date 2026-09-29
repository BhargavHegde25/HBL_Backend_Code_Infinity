package com.temenos.dbx.product.usermanagement.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

/**
 * 
 * @author DBX Team
 *
 */
public interface UserManagementBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param customerIdentificationNumber
     * @param headerMap
     * @return DBXResult DTO
     */
    public DBXResult getCustomerInformationOnCIFSearch(String customerIdentificationNumber,
            Map<String, Object> headerMap);

    /**
     * 
     * @param ssn
     * @param headerMap
     * @return DBXResult DTO
     */
    public DBXResult getCustomerInformationOnSSNSearch(String ssn, Map<String, Object> headerMap);

    /**
     * 
     * @param contactNumber
     * @param headerMap
     * @return DBXResult DTO
     */
    public DBXResult getCustomerInformationOnContactNumberSearch(String contactNumber, Map<String, Object> headerMap);

    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getList(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public List<CustomerDTO> getCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public boolean updateCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    /***
     * 
     * @param C360
     *            Bundle configurations
     * @param userId
     * @param activationCode
     * @param headerMap
     * @throws ApplicationException
     */
    public void createEntryForCredentailCheckerTable(Map<String, String> configurations, CustomerDTO dto,
            String activationCode,
            Map<String, Object> headerMap) throws ApplicationException;

    /**
     * 
     * @param customerDTO
     *            contains id
     * @param headerMap
     * @return CustomerCommunicationDTO
     * @throws ApplicationException
     */
    public List<CustomerCommunicationDTO> fetchCustomerCommunicationDetailsForEnrollment(
            CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param customerDTO
     * @param commDTO
     * @param activationCode
     * @param headersMap
     * @throws ApplicationException
     */
    public void sendEnrollUserIdToEmail(Map<String, String> configurations, CustomerDTO customerDTO,
            CustomerCommunicationDTO commDTO,
            String activationCode, Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * Activation code is delivered to enrolling customer through SMS (Mobile)
     * 
     * @param activationCode
     * @param headersMap
     * @throws ApplicationException
     */
    public void sendEnrollActivationCodeToMobile(Boolean isAdmin, Boolean isEnrolled, CustomerCommunicationDTO commDTO,
            String activationCode,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * Activation code is delivered to customer through SMS (Mobile) to reset password
     * 
     * @param commDTO
     * @param activationCode
     * @param headersMap
     * @throws ApplicationException
     */
    public void sendResetPasswordActivationCodeToMobile(CustomerCommunicationDTO commDTO, String activationCode,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * Activation code validation through enrollment
     * 
     * @param credentialcheckerDTO
     * @return
     * @throws ApplicationException
     */
    public boolean activationCodeValidationForEnrollment(Map<String, String> configurations,
            CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * 
     * @param C360
     *            Bundle credentialcheckerDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public String generateServiceKeyForEnrollment(CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param mfaserviceDTO
     * @param headerMap
     * @return
     */
    public String validateServiceKey(MFAServiceDTO mfaserviceDTO, Map<String, Object> headerMap);

    /**
     * 
     * @param customerDTO
     * @param headerMap
     * @return
     */
    public boolean updatePasswordForActivationFlow(CustomerDTO customerDTO, Map<String, Object> headerMap);

    /**
     * 
     * @param mfaserviceDTO
     * @param headerMap
     */
    public void removeServiceKey(MFAServiceDTO mfaserviceDTO, Map<String, Object> headerMap);

    /**
     * 
     * @param serviceKey
     * @param id
     * @param headerMap
     * @return
     * @throws ApplicationException
     */
    public boolean validateServiceKeyAndFetchID(String serviceKey, String id, Map<String, Object> headerMap)
            throws ApplicationException;

}
