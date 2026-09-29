package com.temenos.dbx.eum.product.usermanagement.resource.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kony.dbputilities.util.LegalEntityUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.error.DBPApplicationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CredentialCheckerBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerSecurityQuestionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PasswordHistoryBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.ProfileManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PushExternalEventBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.UserManagementResource;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerSecurityQuestionsViewDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MemberSearchBean;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;
import com.temenos.dbx.product.dto.PasswordLockoutSettingsDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class UserManagementResourceImpl implements UserManagementResource {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static LoggerUtil logger = new LoggerUtil(UserManagementResourceImpl.class);

    @SuppressWarnings("unused")
    public Result resetPassword(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        String userName = (String) inputParams.get(DBPUtilitiesConstants.USER_NAME);
        String password = (String) inputParams.get(DBPUtilitiesConstants.PWD_FIELD);
        inputParams.put("passWord", password);
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            HelperMethods.setValidationMsg("Please provide Valid Input.", dcRequest, result);
            return result;
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUserName(userName);
        customerDTO = (CustomerDTO) customerDTO.loadDTO();
        customerDTO.setIsChanged(true);

        if (customerDTO == null) {
            HelperMethods.setValidationMsg("incorrect userName.", dcRequest, result);
            return result;
        }

        String previousPassword = customerDTO.getPassword();

        UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        DBXResult response = managementBusinessDelegate.update(customerDTO, dcRequest.getHeaderMap());

        if (StringUtils.isBlank((String) response.getResponse())) {
            HelperMethods.setValidationMsg("Password Update failed.", dcRequest, result);
        }

        PasswordHistoryDTO dto = new PasswordHistoryDTO();

        dto.setId(HelperMethods.getNewId());
        dto.setCustomer_id((String) response.getResponse());
        dto.setPreviousPassword(password);
        dto.setIsNew(true);

        PasswordHistoryBusinessDelegate passwordHistoryBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(PasswordHistoryBusinessDelegate.class);

        response = passwordHistoryBusinessDelegate.update(dto, dcRequest.getHeaderMap());

        if (response.getResponse() != null) {
            Result retVal = new Result();
            Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "updated", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            retVal.addParam(p);
            return retVal;
        }

        customerDTO.setPassword(previousPassword);
        managementBusinessDelegate.update(customerDTO, dcRequest.getHeaderMap());
        HelperMethods.setValidationMsg("Password History create Failed.", dcRequest, result);
        return result;
    }

    @Override
    public Result deletePIN(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();

        String id = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO = (CustomerDTO) customerDTO.loadDTO(id);
        customerDTO.setPin("");
        customerDTO.setIsPinSet(false);
        customerDTO.setIsChanged(true);

        UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        DBXResult response = managementBusinessDelegate.update(customerDTO, dcRequest.getHeaderMap());
        if (response.getResponse() != null) {
            Result retVal = new Result();
            Param p = new Param("success", "Successful", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            retVal.addParam(p);
            return retVal;
        }

        HelperMethods.setValidationMsg("Pin delete operation failed", dcRequest, result);
        return result;

    }

    @Override
    public Result verifyPIN(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        boolean isValidated = false;
        String pin = (String) inputParams.get("pin");
        if (StringUtils.isNotBlank(pin)) {
            String id = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(id);

            UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);

            DBXResult response = managementBusinessDelegate.get(customerDTO, dcRequest.getHeaderMap());

            if (response.getResponse() != null) {
                customerDTO = (CustomerDTO) response.getResponse();

                if (new Date().before(HelperMethods.getFormattedTimeStamp(customerDTO.getValidDate()))
                        && pin.equals(customerDTO.getPin())) {

                    result.addParam(
                            new Param("result", String.valueOf(isValidated), DBPUtilitiesConstants.STRING_TYPE));
                    return result;
                }
            }

        }
        result.addParam(new Param("result", String.valueOf(isValidated), DBPUtilitiesConstants.STRING_TYPE));

        return result;

    }

    @Override
    public Result verifyExistingPassword(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String password = (String) inputParams.get("password");
        if (StringUtils.isNotBlank(password)) {
            String id = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(id);

            UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);

            DBXResult response = managementBusinessDelegate.get(customerDTO, dcRequest.getHeaderMap());

            if (response.getResponse() != null) {
                customerDTO = (CustomerDTO) response.getResponse();
                Boolean status = false;
                status = BCrypt.checkpw(inputParams.get(DBPUtilitiesConstants.PWD_FIELD), customerDTO.getPassword());

                if (status) {
                    Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "The user is verified",
                            DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                    result.addParam(p);
                }
            }
        }

        Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "Invalid Credentials",
                DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        result.addParam(p);
        return result;

    }

    @Override
    public Result verifyUserName(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String password = (String) inputParams.get("password");
        if (StringUtils.isNotBlank(password)) {
            String id = HelperMethods.getCustomerIdFromSession(dcRequest);
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(id);

            UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);

            DBXResult response = managementBusinessDelegate.get(customerDTO, dcRequest.getHeaderMap());

            if (response.getResponse() != null) {
                customerDTO = (CustomerDTO) response.getResponse();
                Boolean status = false;
                status = BCrypt.checkpw(inputParams.get(DBPUtilitiesConstants.PWD_FIELD), customerDTO.getPassword());

                if (status) {
                    Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "The user is verified",
                            DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                    result.addParam(p);
                }
            }
        }

        Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "Invalid Credentials",
                DBPConstants.FABRIC_STRING_CONSTANT_KEY);
        result.addParam(p);
        return result;
    }

    @Override
    public Result resetUserPassword(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        DBXResult response;

        String securityKey = "";
        String userName = (String) inputParams.get("UserName");
        String password = (String) inputParams.get("Password");
        inputParams.put("inputPassword", password);

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            ErrorCodeEnum.ERR_10009.setErrorCode(result);
            return result;
        }

        PasswordHistoryBusinessDelegate passwordHistoryBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(PasswordHistoryBusinessDelegate.class);

        response = passwordHistoryBusinessDelegate.getPasswordLockoutSetting(dcRequest.getHeaderMap());
        PasswordLockoutSettingsDTO lockoutSettingsDTO = (PasswordLockoutSettingsDTO) response.getResponse();

        securityKey = (String) inputParams.get(MFAConstants.SECURITY_KEY);

        CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();
        if (StringUtils.isNotBlank(securityKey)) {
            credentialCheckerDTO.setId(securityKey);
            CredentialCheckerBusinessDelegate credentialCheckerBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CredentialCheckerBusinessDelegate.class);

            response = credentialCheckerBusinessDelegate.get(credentialCheckerDTO, dcRequest.getHeaderMap());

            if (response.getResponse() != null) {
                credentialCheckerDTO = (CredentialCheckerDTO) response.getResponse();
                String userName2 = credentialCheckerDTO.getUserName();
                String type = credentialCheckerDTO.getLinktype();
                String createdDate = credentialCheckerDTO.getCreatedts();
                if (!(StringUtils.isNotBlank(userName2) && userName.equals(userName2)
                        && type.equals(HelperMethods.CREDENTIAL_TYPE.RESETPASSWORD.toString())
                        && !lockoutSettingsDTO.isEmailRecoveryLinkExpired(dcRequest, createdDate))) {
                    ErrorCodeEnum.ERR_10010.setErrorCode(result);
                    return result;
                }
            } else {
                ErrorCodeEnum.ERR_10015.setErrorCode(result);
                return result;
            }
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUserName(userName);

        UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        response = managementBusinessDelegate.get(customerDTO, dcRequest.getHeaderMap());

        if (response.getResponse() == null) {
            ErrorCodeEnum.ERR_10011.setErrorCode(result);
            return result;
        }

        customerDTO = (CustomerDTO) response.getResponse();

        inputParams.put("id", customerDTO.getId());

        PasswordHistoryDTO passwordHistoryDTO = new PasswordHistoryDTO();
        passwordHistoryDTO.setCustomer_id(customerDTO.getId());

        if (HelperMethods.getCustomerStatus().get("ACTIVE").equals(customerDTO.getStatus_id())
                && StringUtils.isNotBlank(customerDTO.getPassword())) {
            if (passwordHistoryDTO.checkForPasswordEntry(lockoutSettingsDTO.getPasswordHistoryCount(), password)) {
                String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
                String hashedPassword = BCrypt.hashpw(password, salt);
                inputParams.put("Password", hashedPassword);
            } else {
                ErrorCodeEnum.ERR_10134.setErrorCode(result, "Password is already present in the previous "
                        + lockoutSettingsDTO.getPasswordHistoryCount() + " passwords.");
                return result;
            }
        } else {
            ErrorCodeEnum.ERR_10013.setErrorCode(result, "Password cannot be reset");
            return result;
        }

        customerDTO.setUserName(userName);
        customerDTO.setPassword(password);
        customerDTO.setLockCount(0);

        response = managementBusinessDelegate.update(customerDTO, dcRequest.getHeaderMap());
        if (response.getResponse() != null) {
            PasswordHistoryDTO dto = new PasswordHistoryDTO();

            dto.setId(HelperMethods.getNewId());
            dto.setCustomer_id((String) response.getResponse());
            dto.setPreviousPassword(password);
            dto.setIsNew(true);

            response = passwordHistoryBusinessDelegate.update(dto, dcRequest.getHeaderMap());

            if (response.getResponse() != null) {
                credentialCheckerDTO.setDeleted(true);
                CredentialCheckerBusinessDelegate credentialCheckerBusinessDelegate = DBPAPIAbstractFactoryImpl
                        .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(CredentialCheckerBusinessDelegate.class);

                response = credentialCheckerBusinessDelegate.update(credentialCheckerDTO, dcRequest.getHeaderMap());

                Result retVal = new Result();
                boolean eSignAgreementRequired = isUserEsignAgreementReq(result.getAllDatasets().get(0).getRecord(0),
                        dcRequest);
                boolean isEagreementSigned = customerDTO.getIsEagreementSigned();
                Param p = new Param(DBPUtilitiesConstants.RESULT_MSG, "updated",
                        DBPConstants.FABRIC_STRING_CONSTANT_KEY);
                retVal.addParam(new Param("isEAgreementRequired", "" + eSignAgreementRequired, "String"));
                retVal.addParam(new Param("isEagreementSigned", "" + isEagreementSigned, "String"));
                HelperMethods.setSuccessMsg("updated", retVal);
                retVal.addParam(p);
                retVal.addParam(new Param(DBPUtilitiesConstants.CUSTOMTER_ID, customerDTO.getId(), "String"));
                return retVal;
            } else {
                ErrorCodeEnum.ERR_10016.setErrorCode(result);
            }

        } else {
            ErrorCodeEnum.ERR_10016.setErrorCode(result);
        }
        return result;

    }

    private boolean isUserEsignAgreementReq(Record user, DataControllerRequest dcRequest) {
        Map<String, String> map = new HashMap<>();
        map.put("Username", HelperMethods.getFieldValue(user, "UserName"));
        Result result = new Result();
        try {
            result = AdminUtil.invokeAPI(map, URLConstants.E_AGREEMENT_AVAILABLE, dcRequest);
        } catch (HttpCallException e) {
        	alert.prepareError("Exception", e).log();
        }
        return "true".equalsIgnoreCase(HelperMethods.getParamValue(result.getParamByName("isEAgreementAvailable")));
    }

    @Override
    public Result lockUnlockUser(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        CustomerDTO customerDTO = new CustomerDTO();

        Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
        if (userPermissions.contains("USER_MANAGEMENT")) {
            customerDTO.setUserName(inputParams.get("UserName"));
        } else {
            customerDTO.setId(HelperMethods.getCustomerIdFromSession(dcRequest));
        }

        UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        DBXResult response = managementBusinessDelegate.get(customerDTO, dcRequest.getHeaderMap());

        String res = "Customer not found";

        if (response.getResponse() != null) {

            int lockCount = customerDTO.getLockCount();

            PasswordHistoryBusinessDelegate passwordHistoryBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PasswordHistoryBusinessDelegate.class);

            response = passwordHistoryBusinessDelegate.getPasswordLockoutSetting(dcRequest.getHeaderMap());
            PasswordLockoutSettingsDTO lockoutSettingsDTO = (PasswordLockoutSettingsDTO) response.getResponse();

            if (lockoutSettingsDTO != null) {
                lockoutSettingsDTO = new PasswordLockoutSettingsDTO();
                lockoutSettingsDTO.setAccountLockoutThreshold(6);
            }

            if (lockCount >= lockoutSettingsDTO.getAccountLockoutThreshold()) {
                res = "Customer Unlocked";
                customerDTO.setLockCount(0);
            } else {
                res = "Customer Locked";
                customerDTO.setLockCount(lockoutSettingsDTO.getAccountLockoutThreshold() + 1);
            }

            customerDTO.setIsChanged(true);
            managementBusinessDelegate.update(customerDTO, dcRequest.getHeaderMap());
        }

        result.addParam(new Param("result", res, "String"));

        return result;

    }

    @Override
    public Result getCustomerInformationOnSearch(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        DBXResult informationDTO = new DBXResult();
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        Map<String, Object> headersMap = HelperMethods.addJWTAuthHeader(dcRequest, dcRequest.getHeaderMap(),
                AuthConstants.PRE_LOGIN_FLOW);
        if (inputParams.containsKey("ssn")) {
            try {
                UserManagementBusinessDelegate userManagementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(UserManagementBusinessDelegate.class);

                informationDTO = userManagementBusinessDelegate
                        .getCustomerInformationOnSSNSearch(inputParams.get("ssn"), headersMap);
            } catch (Exception e) {
                informationDTO.setDbpErrCode("");
                informationDTO.setDbpErrMsg("");
            }
        } else if (inputParams.containsKey("customerIdentificationNumber")) {
            try {
                UserManagementBusinessDelegate userManagementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(UserManagementBusinessDelegate.class);

                informationDTO = userManagementBusinessDelegate
                        .getCustomerInformationOnCIFSearch(inputParams.get("customerIdentificationNumber"), headersMap);
            } catch (Exception e) {
                informationDTO.setDbpErrCode("");
                informationDTO.setDbpErrMsg("");
            }

        } else if (inputParams.containsKey("contactNumber")) {
            try {
                UserManagementBusinessDelegate userManagementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(UserManagementBusinessDelegate.class);

                informationDTO = userManagementBusinessDelegate
                        .getCustomerInformationOnContactNumberSearch(inputParams.get("contactNumber"), headersMap);
            } catch (Exception e) {
                informationDTO.setDbpErrCode("");
                informationDTO.setDbpErrMsg("");
            }
        } else {
            // TODO : Change the error code : Invalid input parameters
            ErrorCodeEnum.ERR_10016.setErrorCode(result);
            return result;
        }

        if (StringUtils.isNotBlank(informationDTO.getDbpErrCode())
                || StringUtils.isNotBlank(informationDTO.getDbpErrMsg())) {
            ErrorCodeEnum.ERR_10000.setErrorCode(result, informationDTO.getDbpErrCode(), informationDTO.getDbpErrMsg());
        } else {
            processCustomerInformation((JsonObject) informationDTO.getResponse(), result);
        }
        return result;
    }

    private void processCustomerInformation(JsonObject response, Result result) {

        Dataset ds = new Dataset();
        ds.setId("customerInformation");
        Record record = new Record();
        if (response.has("lastName")) {
            record.addStringParam("lastName", response.get("lastName").getAsString());
            ds.addRecord(record);
        }
        if (response.has("dateOfBirth")) {
            record = new Record();
            record.addStringParam("dateOfBirth", response.get("dateOfBirth").getAsString());
            ds.addRecord(record);
        }

    }

    @Override
    public Result updateDBXUserStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {

        String eventCode = null;
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        List<CustomerDTO> customerList = new ArrayList<>();
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
        List<String> allowedStatus = Arrays.asList("ACTIVE", "SUSPENDED");
        CustomerDTO inputDTO = new CustomerDTO();

        String userName = StringUtils.isNotBlank(inputParams.get("UserName")) ? inputParams.get("UserName")
                : dcRequest.getParameter("UserName");
        String statusId = StringUtils.isNotBlank(inputParams.get("Status")) ? inputParams.get("Status")
                : dcRequest.getParameter("Status");

        if (StringUtils.isBlank(userName) || !allowedStatus.contains(statusId)) {
            ErrorCodeEnum.ERR_10017.setErrorCode(result);
            return result;
        }

        inputDTO.setUserName(userName);
        try {
            if (StringUtils.isNotBlank(userName)) {
                UserManagementBusinessDelegate usermanagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(UserManagementBusinessDelegate.class);
                customerList = usermanagementBusinessDelegate.getCustomerDetails(inputDTO, dcRequest.getHeaderMap());
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling the UserManagement Business delegate :" + e.getMessage()).log();
        }
        if (customerList.isEmpty()) {
            ErrorCodeEnum.ERR_10017.setErrorCode(result);
            return result;
        }
        CustomerDTO userUnderUpdateDTO = customerList.get(0);
        String scaenabled = EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED");
        if (!Boolean.valueOf(scaenabled) && StringUtils.isBlank(userUnderUpdateDTO.getPassword())) {
            statusId = "NEW";
        }

        inputParams.put("Status", statusId);

        JsonArray jsonArray = getUserContracts(userUnderUpdateDTO.getId(), dcRequest.getHeaderMap());
        if (jsonArray.size() <= 0) {
            ErrorCodeEnum.ERR_10051.setErrorCode(result);
            return result;
        }

        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
            Map<String, String> customerDetails = HelperMethods.getCustomerFromIdentityService(dcRequest);
            String customerId = customerDetails.get("customer_id");
            if (!customerId.equals(userUnderUpdateDTO.getId())) {
                if (!userPermissions.contains("USER_MANAGEMENT")) {
                    ErrorCodeEnum.ERR_10051.setErrorCode(result);
                    return result;
                }
            }
        }

        inputDTO = new CustomerDTO();
        inputDTO.setId(customerList.get(0).getId());
        inputDTO.setStatus_id(HelperMethods.getCustomerStatus().get(statusId));

        boolean isUpdateSuccess = false;
        try {
            UserManagementBusinessDelegate usermanagementBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
            isUpdateSuccess = usermanagementBusinessDelegate.updateCustomerDetails(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling the UserManagement Business delegate :" + e.getMessage()).log();
        }
        if (isUpdateSuccess) {
            result.addStringParam("success", "User Status updated.");
            result.addStringParam("Status", inputParams.get("Status"));
            if (DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE
                    .equalsIgnoreCase(HelperMethods.getCustomerStatus().get(statusId))) {
                eventCode = "SCA_USERSTATUS_ACTIVE";
            } else if (DBPUtilitiesConstants.CUSTOMER_STATUS_SUSPENDED
                    .equalsIgnoreCase(HelperMethods.getCustomerStatus().get(statusId))) {
                eventCode = "SCA_USERSTATUS_SUSPENDED";
            }
            if (StringUtils.isNotBlank(eventCode)) {
                PushExternalEventBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(PushExternalEventBusinessDelegate.class);
                JsonObject json = new JsonObject();
                json.addProperty("userId", userName);

                businessDelegate.pushExternalEvent(eventCode, json.toString(), dcRequest.getHeaderMap());
            }
        } else {
            ErrorCodeEnum.ERR_10018.setErrorCode(result);
        }
        return result;
    }

    private JsonArray getUserContracts(String id, Map<String, Object> headerMap) {
        String filter = InfinityConstants.customerId + DBPUtilitiesConstants.EQUAL + id;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DBPUtilitiesConstants.FILTER, filter);
        JsonObject jsonObject = ServiceCallHelper.invokeServiceAndGetJson(map, headerMap,
                URLConstants.CONTRACT_CUSTOMERS_GET);

        if (jsonObject.has(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS)) {
            JsonElement jsonElement = jsonObject.get(DBPDatasetConstants.DATASET_CONTRACT_CUSTOMERS);
            if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                return jsonArray;
            }
        }
        return new JsonArray();
    }

    @Override
    public Result sendMailToCustomerOnSearch(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Result result = new Result();
        Map<String, String> input = new HashMap<>();
        input.put("Subscribe", "true");
        input.put("FirstName", inputParams.get("FirstName"));
        input.put("EmailType", DBPUtilitiesConstants.CANTSIGNINFLOW_EMAILTEMPLATE);
        input.put("LastName", inputParams.get("LastName"));
        input.put("Email", dcRequest.getParameter("Email"));
        JSONObject addContext = new JSONObject();
        addContext.put("UserName", dcRequest.getParameter("UserName"));
        input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
        Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
        headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        Result response = new Result();
        try {
            response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
        } catch (HttpCallException e) {
            alert.prepareError("Exception occured while calling the KMS Orchestration").log();
        }

        if ("true".equals(HelperMethods.getParamValue(response.getParamByName("KMSemailStatus")))) {
            result.addParam(new Param("status", "Email sent successfully.", MWConstants.STRING));
        } else {
            String errorMsg = HelperMethods.getParamValue(response.getParamByName("KMSuserMsg"))
                    + HelperMethods.getParamValue(response.getParamByName("KMSemailMsg"));
            result.addParam(new Param("status", "Failed to send Email.", MWConstants.STRING));
            HelperMethods.setSuccessMsgwithCode(errorMsg, "10057", result);
        }
        return result;
    }

    public Result checkSecurityQuestions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        String msg = "Questions does not exist";
        String id = HelperMethods.getCustomerIdFromKnownUserToken(dcRequest);
        if (StringUtils.isNotBlank(id)) {

            CustomerSecurityQuestionsBusinessDelegate customerSecurityQuestionsBusinessDelegate =
                    DBPAPIAbstractFactoryImpl
                            .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                            .getBusinessDelegate(CustomerSecurityQuestionsBusinessDelegate.class);

            DBXResult response = (DBXResult) customerSecurityQuestionsBusinessDelegate.get(id,
                    dcRequest.getHeaderMap());

            if (response.getResponse() != null) {
                List<CustomerSecurityQuestionsViewDTO> list = (List<CustomerSecurityQuestionsViewDTO>) response
                        .getResponse();

                if (list.size() > 0) {
                    msg = "Questions Exist";
                }

            }
        }

        result.addParam(new Param("result", msg, DBPUtilitiesConstants.STRING_TYPE));
        return result;
    }

    @Override
    public Result getUserName(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Result result = new Result();
        String ssn = (String) inputParams.get(DBPUtilitiesConstants.SSN);
        String dob = (String) inputParams.get(DBPUtilitiesConstants.DOB);
        String lastname = (String) inputParams.get(DBPUtilitiesConstants.LAST_NAME);

        if (!StringUtils.isNotBlank(ssn) && !StringUtils.isNotBlank(dob) && !StringUtils.isNotBlank(lastname)) {
            HelperMethods.setValidationMsg("Please provide last name and/or ssn and/or date of birth.", request,
                    result);
            return result;
        }

        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setDateOfBirth(dob);
        customerDTO.setSsn(ssn);
        customerDTO.setLastName(lastname);

        UserManagementBusinessDelegate managementBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        DBXResult dbxResult = managementBusinessDelegate.getList(customerDTO, request.getHeaderMap());

        if (response.getResponse() != null) {
            List<DBPDTO> dbpdtos = (List<DBPDTO>) dbxResult.getResponse();
            if (dbpdtos.size() > 0) {
                customerDTO = (CustomerDTO) dbpdtos.get(0);
                Record user = new Record();
                user.addParam(new Param("userName", customerDTO.getUserName(), "String"));
                Dataset data = new Dataset("user");
                data.addRecord(user);
                result.addDataset(data);
                /*
                 * Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR,
                 * "user not found.",DBPConstants.FABRIC_STRING_CONSTANT_KEY); retVal.addParam(p);
                 */
                return result;
            }
        }

        Record user = new Record();
        user.addParam(new Param("userName", "konybankingdev", "String"));
        Dataset data = new Dataset("user");
        data.addRecord(user);
        result.addDataset(data);
        /*
         * Param p = new Param(DBPUtilitiesConstants.VALIDATION_ERROR,
         * "user not found.",DBPConstants.FABRIC_STRING_CONSTANT_KEY); retVal.addParam(p);
         */
        return result;

    }

    @Override
    public Result getPasswordLockoutSettings(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        Result result = new Result();
        Dataset ds = new Dataset();
        ds.setId("passwordlockoutsettings");
        PasswordHistoryBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(PasswordHistoryBusinessDelegate.class);
        DBXResult dbxResult = businessDelegate.getPasswordLockoutSetting(request.getHeaderMap());
        if (dbxResult.getResponse() != null) {
            PasswordLockoutSettingsDTO lockoutSettingsDTO = (PasswordLockoutSettingsDTO) dbxResult.getResponse();
            Record record = DTOUtils.getRecordFromDTO(lockoutSettingsDTO, true);
            ds.addRecord(record);
        }
        result.addDataset(ds);
        return result;
    }

    @Override
    public Result verifyCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws JSONException, UnsupportedEncodingException, DBPApplicationException, MiddlewareException {

        Result result = new Result();
        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        CustomerDTO customerDTO = new CustomerDTO();

        String ssn = StringUtils.isNotBlank(map.get("Ssn")) ? map.get("Ssn") : dcRequest.getParameter("Ssn");
        String lastName = StringUtils.isNotBlank(map.get("LastName")) ? map.get("LastName")
                : dcRequest.getParameter("LastName");
        String phone = StringUtils.isNotBlank(map.get("Phone")) ? map.get("Phone") : dcRequest.getParameter("Phone");
        String email = StringUtils.isNotBlank(map.get("Email")) ? map.get("Email") : dcRequest.getParameter("Email");
        String dateOfBirth = StringUtils.isNotBlank(map.get("DateOfBirth")) ? map.get("DateOfBirth")
                : dcRequest.getParameter("DateOfBirth");
        String serviceKey = StringUtils.isNotBlank(map.get("serviceKey")) ? map.get("serviceKey")
                : dcRequest.getParameter("serviceKey");
        String legalEntityId = EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
        if (StringUtils.isBlank(serviceKey)) {
            return result;
        }

        try {
            MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
            MFAServiceDTO mfaServiceDTO = new MFAServiceDTO();
            mfaServiceDTO.setServiceKey(serviceKey);
            List<MFAServiceDTO> dtoList = null;
            dtoList = mfaserviceBD.getMfaService(mfaServiceDTO, null, dcRequest.getHeaderMap());
            if (null == dtoList || dtoList.isEmpty() || StringUtils.isBlank(dtoList.get(0).getServiceKey())) {
                return result;
            }

            if (!"true".equalsIgnoreCase(dtoList.get(0).getIsVerified())) {
                return result;
            }
            mfaserviceBD.deleteMfaService(serviceKey, dcRequest.getHeaderMap());

            customerDTO.setLastName(lastName);
            customerDTO.setDateOfBirth(dateOfBirth);
            customerDTO.setSsn(ssn);
            CustomerCommunicationDTO commDTO = new CustomerCommunicationDTO();
            commDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
            commDTO.setValue(email);
            customerDTO.setCustomerCommuncation(commDTO);
            commDTO = new CustomerCommunicationDTO();
            commDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_PHONE);
            commDTO.setValue(phone);
            customerDTO.setLegalEntityId(legalEntityId);
            customerDTO.setCustomerCommuncation(commDTO);

            
            UserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
            String deploymentPlatform = EnvironmentConfigurationsHandler.getValue("ODMS_DEPLOYMENT_PLATFORM",dcRequest);
            DBXResult dbxResult = businessDelegate.verifyCustomer(customerDTO, dcRequest.getHeaderMap(), deploymentPlatform);
            if (dbxResult.getResponse() != null) {
                JsonObject jsonObject = (JsonObject) dbxResult.getResponse();
                result = JSONToResult.convert(jsonObject.toString());
            } else {
                HelperMethods.addError(result, dbxResult);
            }

        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError(e.getMessage()).log();
        }
        return result;
    }

    @Override
    public Result sendActivationCodeAndUsername(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
        String id = StringUtils.isNotBlank(map.get("id")) ? map.get("id") : dcRequest.getParameter("id");
        String sendEmail = StringUtils.isNotBlank(map.get("sendEmail")) ? map.get("sendEmail") : "true"; 

        if (StringUtils.isBlank(id)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10739);
        }
        Boolean isAdmin = true;
        Map<String, String> info = HelperMethods.getCustomerFromIdentityService(dcRequest);
        if (HelperMethods.isAuthenticationCheckRequiredForService(info)) {
            isAdmin = false;
            if (!id.equalsIgnoreCase(info.get("user_id"))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10739);
            }
        }
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        try {
            CustomerDTO inputCustomerDTO = new CustomerDTO();
            inputCustomerDTO.setId(id);
            
           
            /**
             * Fetches the userId for enrolling a customer
             */
            UserManagementBusinessDelegate userManagementbusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
            CustomerDTO responseCustomerDTO = userManagementbusinessDelegate
                    .fetchCustomerIdForEnrollment(inputCustomerDTO, dcRequest.getHeaderMap());

            String activationCodeLength = configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_LENGTH);
            if (StringUtils.isBlank(activationCodeLength))
                activationCodeLength = "10";
            String activationCode = generateAlphaNumericString(Integer.parseInt(activationCodeLength));

            /**
             * Credential checker entry for tracking activation code
             */
            userManagementbusinessDelegate.createEntryForCredentailCheckerTable(configurations, responseCustomerDTO,
                    activationCode, dcRequest.getHeaderMap());
            
            
            boolean isSCAEnabled = false;
            boolean isSCACommunicationEnabled = false;
            isSCAEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("IS_SCA_ENABLED"));
			isSCACommunicationEnabled = Boolean.parseBoolean(EnvironmentConfigurationsHandler.getServerProperty("SCA_COMMUNICATION"));
			if(!isSCAEnabled || isSCACommunicationEnabled) {
	            /**
	             * Fetch enrolling customer communication information
	             */
	            CustomerCommunicationDTO inputCustomerCommunicationDTO = new CustomerCommunicationDTO();
	            inputCustomerCommunicationDTO.setCustomer_id(responseCustomerDTO.getId());
	            inputCustomerCommunicationDTO.setCompanyLegalUnit(responseCustomerDTO.getHomeLegalEntity());
	            List<CustomerCommunicationDTO> customerCommunicationResponse = userManagementbusinessDelegate
	                    .fetchCustomerCommunicationDetailsForEnrollment(inputCustomerCommunicationDTO,
	                            dcRequest.getHeaderMap());
	            for (CustomerCommunicationDTO customerCommunicationDTO : customerCommunicationResponse) {
	                if (Boolean.parseBoolean(sendEmail) && DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(customerCommunicationDTO.getType_id())) {
	                    if (isAdmin)
	                        userManagementbusinessDelegate.sendEnrollUserIdToEmail(configurations, responseCustomerDTO,
	                                customerCommunicationDTO, activationCode, dcRequest.getHeaderMap());
	                }
	                if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(customerCommunicationDTO.getType_id())) {
	                    userManagementbusinessDelegate.sendEnrollActivationCodeToMobile(isAdmin,
	                            responseCustomerDTO.getIsEnrolled(),
	                            customerCommunicationDTO,
	                            activationCode, dcRequest.getHeaderMap());
	                }
	            }
			}
			
           
            result.addParam(new Param("status", "success"));
            dcRequest.addRequestParam_("userId", responseCustomerDTO.getUserName());
            dcRequest.addRequestParam_("activationCode", activationCode);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while fetching enrolling userId" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching enrolling userId" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10738);
            
        }
      
        return result;
    }

    public String generateAlphaNumericString(int size) {
        StringBuilder sb = new StringBuilder(size);
        String alphaNumbericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        for (int i = 0; i < size; i++) {
            int index = (int) (alphaNumbericString.length() * Math.random());
            sb.append(alphaNumbericString.charAt(index));
        }
        return sb.toString();
    }

    /**
     * Activation code validation
     */
    @Override
    public Result validateEnrollmentActivation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
        String userName = StringUtils.isNotBlank(map.get("UserName")) ? map.get("UserName")
                : dcRequest.getParameter("UserName");
        String activationCode = StringUtils.isNotBlank(map.get("activationCode")) ? map.get("activationCode")
                : dcRequest.getParameter("activationCode");
        if (StringUtils.isBlank(activationCode) || StringUtils.isBlank(userName)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10742);
        }

        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        try {
            UserManagementBusinessDelegate userManagementbusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
            CredentialCheckerDTO credentialcheckerDTO = new CredentialCheckerDTO();
            credentialcheckerDTO.setId(activationCode);
            credentialcheckerDTO.setUserName(userName);
            boolean status = userManagementbusinessDelegate.activationCodeValidationForEnrollment(configurations,
                    credentialcheckerDTO, dcRequest.getHeaderMap());
            if (!status) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10741);
            }
            result.addStringParam("isActivationCodeValid", String.valueOf(status));
            if (status) {
                String serviceKey = userManagementbusinessDelegate.generateServiceKeyForEnrollment(credentialcheckerDTO,
                        dcRequest.getHeaderMap());
                result.addStringParam("serviceKey", serviceKey);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while validating the activation code" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10741);
        }
        return result;
    }

    @Override
    public Result updatePasswordForActivationFlow(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException {
        Result result = new Result();

        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);

        String serviceKey = map.get("serviceKey");
        if (StringUtils.isBlank(serviceKey)) {
            serviceKey = request.getParameter("serviceKey");
        }

        if (StringUtils.isBlank(serviceKey)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10742);
        }

        String password = map.get("password");
        if (StringUtils.isBlank(password)) {
            password = request.getParameter("password");
        }

        if(!validatePasswordRegex(password, request)){
            throw new ApplicationException(ErrorCodeEnum.ERR_10012);
        }

        MFAServiceDTO mfaserviceDTO = new MFAServiceDTO();
        mfaserviceDTO.setServiceKey(serviceKey);

        UserManagementBusinessDelegate userManagementbusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        String userName = userManagementbusinessDelegate.validateServiceKey(mfaserviceDTO, request.getHeaderMap());

        if (StringUtils.isBlank(userName)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10742);
        }

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setUserName(userName);

        customerDTO = (CustomerDTO) userManagementbusinessDelegate.get(customerDTO, request.getHeaderMap())
                .getResponse();

        customerDTO.setIsChanged(true);
        customerDTO.setStatus_id(DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE);
        customerDTO.setIsEnrolled(true);
        customerDTO.setPassword(password);
        if (userManagementbusinessDelegate.updatePasswordForActivationFlow(customerDTO, request.getHeaderMap())) {
            result.addStringParam("status", "success");
            userManagementbusinessDelegate.removeServiceKey(mfaserviceDTO, request.getHeaderMap());
        } else {
            throw new ApplicationException(ErrorCodeEnum.ERR_10744);
        }

        return result;
    }

    @Override
    public Result sendActivationCodeAndUsernameBasedOnServiceKey(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        LegalEntityUtil.addCompanyIDToHeaders(dcRequest);
        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
        String id = StringUtils.isNotBlank(map.get("id")) ? map.get("id") : dcRequest.getParameter("id");
        String serviceKey = StringUtils.isNotBlank(map.get("serviceKey")) ? map.get("serviceKey")
                : dcRequest.getParameter("serviceKey");
        if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(id)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10752);
        }
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        UserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(UserManagementBackendDelegate.class);
        UserManagementBusinessDelegate userManagementbusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(UserManagementBusinessDelegate.class);
        try {
            CustomerDTO inputCustomerDTO = new CustomerDTO();
            inputCustomerDTO.setId(id);
            String userId = "";
            CustomerDTO responseCustomerDTO = (CustomerDTO) backendDelegate
                    .get(inputCustomerDTO, dcRequest.getHeaderMap()).getResponse();
            userId = responseCustomerDTO.getId();

            String activationCodeLength = configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_LENGTH);
            if (StringUtils.isBlank(activationCodeLength))
                activationCodeLength = "10";
            String activationCode = generateAlphaNumericString(Integer.parseInt(activationCodeLength));

            /**
             * Credential checker entry for tracking activation code
             */
            userManagementbusinessDelegate.createEntryForCredentailCheckerTable(configurations, responseCustomerDTO,
                    activationCode, dcRequest.getHeaderMap());
            /**
             * Fetch enrolling customer communication information
             */
            CustomerCommunicationDTO inputCustomerCommunicationDTO = new CustomerCommunicationDTO();
            inputCustomerCommunicationDTO.setCustomer_id(responseCustomerDTO.getId());
            List<CustomerCommunicationDTO> customerCommunicationResponse = userManagementbusinessDelegate
                    .fetchCustomerCommunicationDetailsForEnrollment(inputCustomerCommunicationDTO,
                            dcRequest.getHeaderMap());
            for (CustomerCommunicationDTO customerCommunicationDTO : customerCommunicationResponse) {
                if (DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(customerCommunicationDTO.getType_id())) {
                    if (!responseCustomerDTO.getIsEnrolled())
                        userManagementbusinessDelegate.sendEnrollUserIdToEmail(configurations, responseCustomerDTO,
                                customerCommunicationDTO, activationCode, dcRequest.getHeaderMap());
                }
                if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(customerCommunicationDTO.getType_id())) {
                    if (!responseCustomerDTO.getIsEnrolled())
                        userManagementbusinessDelegate.sendEnrollActivationCodeToMobile(true,
                                responseCustomerDTO.getIsEnrolled(), customerCommunicationDTO,
                                activationCode, dcRequest.getHeaderMap());
                    else
                        userManagementbusinessDelegate.sendResetPasswordActivationCodeToMobile(customerCommunicationDTO,
                                activationCode, dcRequest.getHeaderMap());
                }
            }
            result.addStringParam("status", "success");
            dcRequest.addRequestParam_("userId", userId);
            dcRequest.addRequestParam_("activationCode", activationCode);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while fetching enrolling userId" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching enrolling userId" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10738);
        }
        return result;
    }

    @Override
    public Result updateCustomerStatusToActive(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        CustomerDTO customerDTO = new CustomerDTO();
        Map<String, String> inputParams = new HashMap<>();
        String userName = StringUtils.isNotBlank(inputParams.get("userName")) ? inputParams.get("userName")
                : dcRequest.getParameter("userName");
        customerDTO.setUserName(userName);
        try {
            UserManagementBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(UserManagementBusinessDelegate.class);
            DBXResult response = businessDelegate.get(customerDTO, dcRequest.getHeaderMap());
            CustomerDTO responseDTO = (CustomerDTO) response.getResponse();
            responseDTO.setStatus_id(DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE);
            responseDTO.setIsEnrolled(true);
            responseDTO.setIsNew(false);
            responseDTO.setIsChanged(true);
            businessDelegate.updateCustomerDetails(responseDTO, dcRequest.getHeaderMap());
            result.addStringParam("status", "success");
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating the customer status" + e.getLocalizedMessage()).log();
        }
        return result;
    }

    private boolean validatePasswordRegex(String password, DataControllerRequest dcRequest) {

        Map<String, String> passwordRulesAndPolicyInputMap = new HashMap<>();
        passwordRulesAndPolicyInputMap.put("ruleForCustomer", "true");
        passwordRulesAndPolicyInputMap.put("policyForCustomer", "true");

        Result result = null;
        Result passwordRulesAndPolicyResult = new Result();
        try {
            passwordRulesAndPolicyResult = AdminUtil.invokeAPI(passwordRulesAndPolicyInputMap,
                    URLConstants.GET_PASSWORD_RULES_AND_POLICIES, dcRequest);
            JSONObject passwordRulesAndPolicyResponse = new JSONObject(
                    ResultToJSON.convert(passwordRulesAndPolicyResult));
            if (passwordRulesAndPolicyResponse != null
                    && passwordRulesAndPolicyResponse.has("passwordrules")
                    && passwordRulesAndPolicyResponse.getJSONObject("passwordrules") != null) {

                JSONObject passwordRules = passwordRulesAndPolicyResponse
                        .getJSONObject("passwordrules");

                if (passwordRules.has("charRepeatCount")) {
                    String repeatedCharRegex = "(.)\\1{" + passwordRules.getString("charRepeatCount") + ",}";
                    Pattern repeatedCharPattern = Pattern.compile(repeatedCharRegex);
                    Matcher repeatedCharMatcher = repeatedCharPattern.matcher(password);
                    if (repeatedCharMatcher.find()) {
                        return false;
                    }
                }

                String passwordRegex = "";

                if (passwordRules.has("atleastOneSymbol") && passwordRules.getString("atleastOneSymbol").equals("true")) {
                    if (passwordRules.has("atleastOneLowerCase")
                            && passwordRules.getString("atleastOneLowerCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[a-z])";
                    }
                    if (passwordRules.has("atleastOneUpperCase")
                            && passwordRules.getString("atleastOneUpperCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[A-Z])";
                    }
                    if (passwordRules.has("atleastOneNumber")
                            && passwordRules.getString("atleastOneNumber").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*\\d)";
                    }
                    if (passwordRules.has("supportedSymbols") && passwordRules.getString("supportedSymbols").length() != 0) {
                        String supportedSymbols = "";
                        if(passwordRules.getString("supportedSymbols").indexOf("-") > -1) {
                            supportedSymbols = passwordRules.getString("supportedSymbols").replace("-", "\\-");
                            passwordRules.put("supportedSymbols", supportedSymbols);
                        }
                        passwordRegex = passwordRegex + "(?=(.*[" + passwordRules.getString("supportedSymbols") + "]))";
                        if(passwordRules.getString("supportedSymbols").indexOf(",") > -1) {
                            supportedSymbols = passwordRules.getString("supportedSymbols").replaceAll(",", "");
                            passwordRules.put("supportedSymbols", supportedSymbols);
                        }
                    }
                    passwordRegex = passwordRegex + "[A-Za-z0-9" + passwordRules.getString("supportedSymbols")
                            + "]{" + passwordRules.getString("minLength") + ","
                            + passwordRules.getString("maxLength") + "}$";
                } else {
                    passwordRegex = "^";
                    if (passwordRules.has("atleastOneLowerCase")
                            && passwordRules.getString("atleastOneLowerCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[a-z])";
                    }
                    if (passwordRules.has("atleastOneUpperCase")
                            && passwordRules.getString("atleastOneUpperCase").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*[A-Z])";
                    }
                    if (passwordRules.has("atleastOneNumber")
                            && passwordRules.getString("atleastOneNumber").equals("true")) {
                        passwordRegex = passwordRegex + "(?=.*\\d)";
                    }
                    passwordRegex = passwordRegex + "[^\\\\W]{" + passwordRules.getString("minLength") + ","
                            + passwordRules.getString("maxLength") + "}$";
                }

                Pattern passwordRegexPattern = Pattern.compile(passwordRegex);
                Matcher passwordRegexMatcher = passwordRegexPattern.matcher(password);

                if (!passwordRegexMatcher.matches()) {
                    return false;
                }
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception : ", e).log();
        }

        return true;
    }
    
    @Override
    public Result searchEnrolledCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
    		DataControllerResponse dcResponse) throws Exception{
    	Result processedResult = new Result();
    	
    	try {

    	CustomerDTO customerDTO = new CustomerDTO();
    	customerDTO.setId(dcRequest.getParameter("_id"));
    	customerDTO.setUserName(dcRequest.getParameter("_username"));
    	customerDTO.setFirstName(dcRequest.getParameter("_name"));
    	
    	String id = dcRequest.getParameter("_id");
    	String name = dcRequest.getParameter("_name");
    	String username = dcRequest.getParameter("_username");

    	if(StringUtils.isBlank(id) && StringUtils.isBlank(name) && StringUtils.isBlank(username)){
    		ErrorCodeEnum.ERR_29041.setErrorCode(processedResult);
    		return processedResult;	
    	}

    	UserManagementBusinessDelegate businessDelegate =
    			DBPAPIAbstractFactoryImpl.getBusinessDelegate(UserManagementBusinessDelegate.class);

    	DBXResult records = businessDelegate.get(customerDTO, dcRequest.getHeaderMap());
    	if (records.getResponse() != null) {
    		if(StringUtils.isBlank(customerDTO.getId()) && StringUtils.isBlank(customerDTO.getUserName())) {
    			List<CustomerDTO> customerResponse = (List<CustomerDTO>) records.getResponse();
        		JSONArray customers = new JSONArray();
        		for(int i=0;i<customerResponse.size();i++)
        		{
        			CustomerDTO customerObj = (CustomerDTO)customerResponse.get(i);
    	    		JSONObject customer = new JSONObject();
    	    		customer.put("id", customerObj.getId());
    	    		customer.put("FirstName", customerObj.getFirstName());
    	    		customer.put("LastName", customerObj.getLastName());
    	    		customer.put("UserName", customerObj.getUserName());
    	    		customers.put(customer);
        		}
        		processedResult.addParam(new Param("customers",customers.toString()));
    		}
    		else {
    		CustomerDTO customerResponse = (CustomerDTO) records.getResponse();
    		JSONObject customer = new JSONObject();
    		customer.put("id", customerResponse.getId());
    		customer.put("FirstName", customerResponse.getFirstName());
    		customer.put("LastName", customerResponse.getLastName());
    		customer.put("UserName", customerResponse.getUserName());
    		JSONArray customers = new JSONArray();
    		customers.put(customer);
    		processedResult.addParam(new Param("customers",customers.toString())) ;
    		}
    	} else {
    		ErrorCodeEnum.ERR_29041.setErrorCode(processedResult);
    		return processedResult;	
    	}
		return processedResult;
    	
    	}
    	catch(Exception e)
    	{

			ErrorCodeEnum.ERR_10020.setErrorCode(processedResult);
			return processedResult;
    	}
    }
}
