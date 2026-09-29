package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.ProfileManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl.UserManagementBusinessDelegateImpl;

public class UserManagementBusinessDelegateImpl implements UserManagementBusinessDelegate {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            dbxResult = userManagementBackendDelegate.update(customerDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            dbxResult.setDbpErrCode("");
            dbxResult.setDbpErrMsg("");
        }
        return dbxResult;
    }

    @Override
    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            dbxResult = userManagementBackendDelegate.get(customerDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            dbxResult.setDbpErrCode("");
            dbxResult.setDbpErrMsg("");
        }
        return dbxResult;
    }

    @Override
    public DBXResult getList(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        // TODO Auto-generated method stub

        DBXResult dbxResult = new DBXResult();
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            dbxResult = userManagementBackendDelegate.getList(customerDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            dbxResult.setDbpErrCode("");
            dbxResult.setDbpErrMsg("");
        }
        return dbxResult;
    }

    @Override
    public DBXResult getCustomerInformationOnCIFSearch(String customerIdentificationNumber,
            Map<String, Object> headerMap) {
        DBXResult informationDTO = new DBXResult();

        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            informationDTO = userManagementBackendDelegate
                    .getCustomerInformationOnCIFSearch(customerIdentificationNumber, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            informationDTO.setDbpErrCode("");
            informationDTO.setDbpErrMsg("");
        }
        return informationDTO;
    }

    @Override
    public DBXResult getCustomerInformationOnSSNSearch(String ssn, Map<String, Object> headerMap) {
        DBXResult informationDTO = new DBXResult();

        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            informationDTO = userManagementBackendDelegate.getCustomerInformationOnSSNSearch(ssn, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            informationDTO.setDbpErrCode("");
            informationDTO.setDbpErrMsg("");
        }
        return informationDTO;
    }

    @Override
    public DBXResult getCustomerInformationOnContactNumberSearch(String contactNumber, Map<String, Object> headerMap) {
        DBXResult informationDTO = new DBXResult();

        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            informationDTO = userManagementBackendDelegate.getCustomerInformationOnContactNumberSearch(contactNumber,
                    headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            informationDTO.setDbpErrCode("");
            informationDTO.setDbpErrMsg("");
        }
        return informationDTO;
    }

    @Override
    public List<CustomerDTO> getCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        DBXResult dbxResult = new DBXResult();
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            return userManagementBackendDelegate.getCustomerDetails(inputDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            dbxResult.setDbpErrCode("");
            dbxResult.setDbpErrMsg("");
        }
        return null;
    }

    @Override
    public boolean updateCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException {

        DBXResult dbxResult = new DBXResult();
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            return userManagementBackendDelegate.updateCustomerDetails(inputDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
            dbxResult.setDbpErrCode("");
            dbxResult.setDbpErrMsg("");
        }
        return true;
    }

    @Override
    public DBXResult verifyCustomer(CustomerDTO customerDTO, Map<String, Object> headerMap, String deploymentPlatform)
            throws ApplicationException, JSONException, DBPApplicationException, MiddlewareException, UnsupportedEncodingException {
        ProfileManagementBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
        return backendDelegate.verifyCustomer(customerDTO, headerMap, deploymentPlatform);
    }

    /**
     * Business delegate method to fetch the customerId for enrolling a customer
     */
    @Override
    public CustomerDTO fetchCustomerIdForEnrollment(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        CustomerDTO responseDTO = new CustomerDTO();
        try {
            ProfileManagementBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(ProfileManagementBackendDelegate.class);
            DBXResult response = backendDelegate.fetchCustomerIdForEnrollment(customerDTO, headerMap);
            responseDTO = (CustomerDTO) response.getResponse();
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10738);
        }
        return responseDTO;
    }

    /**
     * Business Delegate method to create Entry in credential checker table to track activation code
     */
    @Override
    public void createEntryForCredentailCheckerTable(Map<String, String> configurations, CustomerDTO dto,
            String activationCode,
            Map<String, Object> headerMap) throws ApplicationException {
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            userManagementBackendDelegate.createEntryForCredentailCheckerTable(configurations, dto, activationCode,
                    headerMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
        }
    }

    @Override
    public List<CustomerCommunicationDTO> fetchCustomerCommunicationDetailsForEnrollment(
            CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) throws ApplicationException {
        List<CustomerCommunicationDTO> responseDTO = new ArrayList<>();
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            responseDTO = userManagementBackendDelegate
                    .fetchCustomerCommunicationDetailsForEnrollment(customerCommunicationDTO, headerMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10740);
        }
        return responseDTO;
    }

    @Override
    public void sendEnrollUserIdToEmail(Map<String, String> configurations, CustomerDTO customerInfo,
            CustomerCommunicationDTO commDTO,
            String activationCode, Map<String, Object> headersMap)
            throws ApplicationException {
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            userManagementBackendDelegate.sendEnrollUserIdToEmail(configurations, customerInfo, commDTO, activationCode,
                    headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10746);
        }
    }

    @Override
    public void sendEnrollActivationCodeToMobile(Boolean isAdmin, Boolean isEnrolled, CustomerCommunicationDTO commDTO,
            String activationCode,
            Map<String, Object> headersMap)
            throws ApplicationException {
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            userManagementBackendDelegate.sendEnrollActivationCodeToMobile(isAdmin,isEnrolled, commDTO, activationCode,
                    headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10745);
        }
    }

    @Override
    public void sendResetPasswordActivationCodeToMobile(CustomerCommunicationDTO commDTO, String activationCode,
            Map<String, Object> headersMap) throws ApplicationException {
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            userManagementBackendDelegate.sendResetPasswordActivationCodeToMobile(commDTO, activationCode, headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while sendResetPasswordActivationCodeToMobile" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10745);
        }
    }

    @Override
    public boolean activationCodeValidationForEnrollment(Map<String, String> configurations,
            CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        boolean status = false;
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            status = userManagementBackendDelegate.activationCodeValidationForEnrollment(configurations,
                    credentialcheckerDTO,
                    headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10741);
        }
        return status;
    }

    @Override
    public String generateServiceKeyForEnrollment(CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        String serviceKey = null;
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            serviceKey =
                    userManagementBackendDelegate.generateServiceKeyForEnrollment(credentialcheckerDTO, headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10743);
        }
        return serviceKey;
    }

    @Override
    public String validateServiceKey(MFAServiceDTO mfaserviceDTO, Map<String, Object> headerMap) {

        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            return userManagementBackendDelegate.validateServiceKey(mfaserviceDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
        }

        return "";
    }

    @Override
    public boolean updatePasswordForActivationFlow(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            return userManagementBackendDelegate.updatePasswordForActivationFlow(customerDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
        }

        return false;
    }

    @Override
    public void removeServiceKey(MFAServiceDTO mfaserviceDTO, Map<String, Object> headerMap) {
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            userManagementBackendDelegate.removeServiceKey(mfaserviceDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            // TODO: assign the error code and error message
        }

    }

    @Override
    public boolean validateServiceKeyAndFetchID(String serviceKey, String id, Map<String, Object> headerMap)
            throws ApplicationException {
        boolean status = false;
        try {
            UserManagementBackendDelegate userManagementBackendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(UserManagementBackendDelegate.class);
            status =
                    userManagementBackendDelegate.validateServiceKeyAndFetchID(serviceKey, id, headerMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling usermanagement backend delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10754);
        }
        return status;
    }
}
