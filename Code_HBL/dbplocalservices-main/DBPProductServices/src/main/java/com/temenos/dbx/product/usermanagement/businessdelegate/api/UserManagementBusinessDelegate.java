package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface UserManagementBusinessDelegate extends BusinessDelegate {

    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getCustomerInformationOnCIFSearch(String customerIdentificationNumber,
            Map<String, Object> headerMap);

    public DBXResult getCustomerInformationOnSSNSearch(String ssn, Map<String, Object> headerMap);

    public DBXResult getCustomerInformationOnContactNumberSearch(String contactNumber, Map<String, Object> headerMap);

    public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public DBXResult getList(CustomerDTO customerDTO, Map<String, Object> headerMap);

    public List<CustomerDTO> getCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public boolean updateCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    public DBXResult verifyCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap, String deploymentPlatform) throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException, UnsupportedEncodingException;

    /**
     * 
     * @param customerDTO
     *            contains id
     * @param headerMap
     * @return CustomerDTO contains id
     * @throws ApplicationException
     */
    public CustomerDTO fetchCustomerIdForEnrollment(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException;

    /**
     * 
     * @param C360
     *            Bundle configurations
     * @param userId
     * @param activationCode
     * @param headerMap
     * @throws ApplicationException
     */
    public void createEntryForCredentailCheckerTable(Map<String, String> configurations, CustomerDTO customerDTO,
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
     * Activation code is delivered to customer through SMS (Mobile) to reset his password
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
     * 
     * @param C360
     *            configurations
     * @param credentialcheckerDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public boolean activationCodeValidationForEnrollment(Map<String, String> configurations,
            CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap)
            throws ApplicationException;

    /**
     * Service key generation for the enrollment
     * 
     * @param credentialcheckerDTO
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
     * @return
     * @throws ApplicationException
     */
    public boolean validateServiceKeyAndFetchID(String serviceKey, String id, Map<String, Object> headerMap)
            throws ApplicationException;

}
