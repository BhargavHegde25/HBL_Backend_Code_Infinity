package com.temenos.dbx.product.usermanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.businessdelegate.api.KMSBusinessDelegate;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.UserManagementBackendDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CredentialCheckerBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.PasswordHistoryBusinessDelegate;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.HTTPOperations;

public class UserManagementBackendDelegateImpl implements UserManagementBackendDelegate {

    Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public DBXResult getCustomerInformationOnCIFSearch(String customerIdentificationNumber,
            Map<String, Object> headerMap) {
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        String cifSearchCoreURL =
                "http://tmnsinffo2.southindia.cloudapp.azure.com:9089/infinity-retail-api/api/v1.0.0/party/customers/"
                        + customerIdentificationNumber + "/contactDetails";

        DBXResult dbxResult = new DBXResult();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, cifSearchCoreURL, "",
                headerMap);

        JsonObject jsonObject = new JsonObject();

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if (jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if (jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("body").getAsString());
                    return dbxResult;
                }
            } else if (jsonObject.has("error") && !jsonObject.get("error").isJsonNull()) {
                setErrorResponse(jsonObject, dbxResult);
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
        }
        return dbxResult;
    }

    @Override
    public DBXResult getCustomerInformationOnSSNSearch(String ssn, Map<String, Object> headerMap) {
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

        String cifSearchCoreURL =
                "http://tmnsinffo2.southindia.cloudapp.azure.com:9089/infinity-retail-api/api/v1.0.0/party/customers?socialSecurityId="
                        + ssn;

        DBXResult dbxResult = new DBXResult();

        DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, cifSearchCoreURL, "",
                headerMap);

        JsonObject jsonObject = new JsonObject();

        try {
            jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();

            if (jsonObject.has("header") && !jsonObject.has("error") && !jsonObject.get("header").isJsonNull()) {
                jsonObject = jsonObject.get("header").getAsJsonObject();
                if (jsonObject.has("status") && !jsonObject.get("status").isJsonNull()) {
                    dbxResult.setResponse(jsonObject.get("id").getAsString());
                    return dbxResult;
                }
            } else if (jsonObject.has("error") && !jsonObject.get("error").isJsonNull()) {
                setErrorResponse(jsonObject, dbxResult);
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while creating Core customer: ", e).log();
            dbxResult.setDbpErrMsg(response.getDbpErrMsg());
            return dbxResult;
        }
        return null;
    }

    @Override
    public DBXResult getCustomerInformationOnContactNumberSearch(String contactNumber, Map<String, Object> headerMap) {
        return null;
    }

    public void setErrorResponse(JsonObject jsonObject, DBXResult dbxResult) {

        jsonObject = jsonObject.get("error").getAsJsonObject();
        if (jsonObject.has("errorDetails") && !jsonObject.get("errorDetails").isJsonNull()) {
            jsonObject = jsonObject.get("errorDetails").getAsJsonArray().get(0).getAsJsonObject();
            if (jsonObject.has("code") && !jsonObject.get("code").isJsonNull()) {
                dbxResult.setDbpErrCode(jsonObject.get("code").getAsString());
            }

            if (jsonObject.has("message") && !jsonObject.get("message").isJsonNull()) {
                dbxResult.setDbpErrMsg(jsonObject.get("message").getAsString());
            }
        } else {
            dbxResult.setDbpErrMsg(jsonObject.toString());
        }

    }

    @Override
    public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();
        if (DTOUtils.persistObject(customerDTO, headerMap)) {
            dbxResult.setResponse(customerDTO.getId());
        } else {
            dbxResult.setDbpErrMsg("Customer update Failed");
        }

        return dbxResult;
    }

    @Override
    public DBXResult get(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();
        if (StringUtils.isNotBlank(customerDTO.getId())) {
            dbxResult.setResponse(customerDTO.loadDTO(customerDTO.getId()));
        } else {
            dbxResult.setResponse(customerDTO.loadDTO());
        }
        return dbxResult;
    }

    @Override
    public DBXResult getList(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        // TODO Auto-generated method stub

        DBXResult dbxResult = new DBXResult();
        StringBuilder sb = new StringBuilder();
        sb.append(DBPUtilitiesConstants.CUSTOMER_LAST_NAME).append(DBPUtilitiesConstants.EQUAL)
                .append(customerDTO.getLastName());
        sb.append(DBPUtilitiesConstants.AND);
        sb.append(DBPUtilitiesConstants.C_SSN).append(DBPUtilitiesConstants.EQUAL).append(customerDTO.getSsn());
        sb.append(DBPUtilitiesConstants.AND);
        sb.append(DBPUtilitiesConstants.C_DOB).append(DBPUtilitiesConstants.EQUAL).append(customerDTO.getDateOfBirth());

        dbxResult.setResponse(
                DTOUtils.getDTOListfromDB(headerMap, sb.toString(), URLConstants.CUSTOMER_GET, true, true));

        return dbxResult;
    }

    @Override
    public List<CustomerDTO> getCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        List<CustomerDTO> responseDTO = new ArrayList<>();

        boolean isAndAppendedFlag = false;

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(inputDTO.getUserName())) {
            sb.append("UserName");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append("'" + inputDTO.getUserName() + "'");
            isAndAppendedFlag = true;
        }
        if (StringUtils.isNotBlank(inputDTO.getSsn())) {
            if (isAndAppendedFlag)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("Ssn");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append(inputDTO.getSsn());
            isAndAppendedFlag = true;
        }
        if (StringUtils.isNotBlank(inputDTO.getDateOfBirth())) {
            if (isAndAppendedFlag)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("DateOfBirth");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append(inputDTO.getDateOfBirth());
            isAndAppendedFlag = true;
        }
        if (StringUtils.isNotBlank(inputDTO.getLastName())) {
            if (isAndAppendedFlag)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("LastName");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append(inputDTO.getLastName());
            isAndAppendedFlag = true;
        }
        if (StringUtils.isNotBlank(inputDTO.getOrganization_Id())) {
            if (isAndAppendedFlag)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("Organization_Id");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append(inputDTO.getOrganization_Id());
            isAndAppendedFlag = true;
        }
        if (StringUtils.isNotBlank(inputDTO.getId())) {
            if (isAndAppendedFlag)
                sb.append(DBPUtilitiesConstants.AND);
            sb.append("id");
            sb.append(DBPUtilitiesConstants.EQUAL);
            sb.append(inputDTO.getId());
        }

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put(DBPUtilitiesConstants.FILTER, sb.toString());
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap, URLConstants.CUSTOMER_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching customer details :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10278);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("customer")) {
            alert.prepareError("Exception occured while fetching customer details :" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10278);
        }

        if (response.get("customer").getAsJsonArray().size() == 0) {
            return responseDTO;
        }

        try {
            for (JsonElement accountsJson : response.get("customer").getAsJsonArray()) {
                if (accountsJson != null) {
                    responseDTO.add((CustomerDTO) DTOUtils.loadJsonObjectIntoObject(accountsJson.getAsJsonObject(),
                            CustomerDTO.class, true));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the customer details :" + e.getMessage()
                    + "Response from backend :" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10278);
        }
        return responseDTO;
    }

    @Override
    public boolean updateCustomerDetails(CustomerDTO inputDTO, Map<String, Object> headerMap)
            throws ApplicationException {

        boolean isCustomerUpdateSuccess = false;
        Map<String, Object> inputMap = new HashMap<>();

        if (StringUtils.isNotBlank(inputDTO.getId())) {
            inputMap.put("id", inputDTO.getId());
        }

        if (StringUtils.isNotBlank(inputDTO.getStatus_id()))
            inputMap.put("Status_id", inputDTO.getStatus_id());

        if (StringUtils.isNotBlank(inputDTO.getUserName()))
            inputMap.put("UserName", inputDTO.getUserName());

        if (StringUtils.isNotBlank(String.valueOf(inputDTO.getIsEnrolled()))
                && "true".equalsIgnoreCase(String.valueOf(inputDTO.getIsEnrolled())))
            inputMap.put("isEnrolled", String.valueOf(inputDTO.getIsEnrolled()));

        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap, URLConstants.CUSTOMER_UPDATE);
        } catch (Exception e) {
            alert.prepareError("Exception occured while updating customer details :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10280);
        }
        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("updatedRecords")) {
            alert.prepareError("Exception occured while updating customer details :" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10280);
        }
        updateCustomerLegalEntityStatus(inputDTO, headerMap);

        if (response.get("updatedRecords").getAsInt() == 1) {
            isCustomerUpdateSuccess = true;
        }

        return isCustomerUpdateSuccess;
    }
    public boolean updateCustomerLegalEntityStatus(CustomerDTO customerDTO, Map<String, Object> headerMap) {
        Map<String, Object> inputMap = new HashMap<>();
        String customerId = customerDTO.getId();
        String legalEntityId = customerDTO.getCompanyLegalUnit();
        String status = "SID_CUS_ACTIVE";
        String isOLB = "true";
        inputMap.put("customerId", customerId);
        inputMap.put("statusId", status);
        inputMap.put("isOLB", isOLB);
        inputMap.put("legalEntityList", legalEntityId);
        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputMap, headerMap,
                "update_userstatus_by_legalentity_proc");
        return (response.has("records") && response.get("records").getAsJsonArray().size() > 0);
    }


    /**
     * Business Delegate method to create Entry in credential checker table to track activation code
     */
    @Override
    public void createEntryForCredentailCheckerTable(Map<String, String> configurations, CustomerDTO dto,
            String activationCode,
            Map<String, Object> headerMap) throws ApplicationException {
        try {
            int retryCount = 0;
            String credentialType = HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString();
            CredentialCheckerBusinessDelegate credentialCheckerBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CredentialCheckerBusinessDelegate.class);
            CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();
            credentialCheckerDTO.setUserName(dto.getUserName());
            credentialCheckerDTO.setLinktype(credentialType);
            DBXResult response = credentialCheckerBusinessDelegate.get(credentialCheckerDTO, headerMap);
            credentialCheckerDTO = (CredentialCheckerDTO) response.getResponse();
            if (credentialCheckerDTO != null) {
                retryCount = Integer.valueOf(credentialCheckerDTO.getRetryCount());
                credentialCheckerBusinessDelegate.delete(credentialCheckerDTO, headerMap);
                retryCount++;
            }
            credentialCheckerDTO = new CredentialCheckerDTO();
            credentialCheckerDTO.setUserName(dto.getUserName());
            credentialCheckerDTO.setId(activationCode);
            credentialCheckerDTO.setLinktype(credentialType);
            credentialCheckerDTO.setRetryCount(String.valueOf(retryCount));
            credentialCheckerBusinessDelegate.create(credentialCheckerDTO, headerMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while creating entry in credential checker table" + e.getMessage()).log();
        }
    }

    @Override
    public List<CustomerCommunicationDTO> fetchCustomerCommunicationDetailsForEnrollment(
            CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap) throws ApplicationException {
        List<CustomerCommunicationDTO> responseDTO = new ArrayList<>();
        try {
            CommunicationBackendDelegate communicationBackendDelagate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
            DBXResult response =
                    communicationBackendDelagate.getPrimaryMFACommunicationDetails(customerCommunicationDTO, headerMap);
            JsonArray communicationArray = ((JsonObject) response.getResponse())
                    .get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).getAsJsonArray();
            String email = "";
            String phone = "";
            for (JsonElement jsonelement : communicationArray) {
                JsonObject object = jsonelement.getAsJsonObject();
                if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
                    email = JSONUtil.getString(object, "Value");
                if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
                    phone = JSONUtil.getString(object, "Value");
            }
            CustomerCommunicationDTO commDTO = new CustomerCommunicationDTO();
            commDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
            commDTO.setValue(email);
            responseDTO.add(commDTO);

            commDTO = new CustomerCommunicationDTO();

            commDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_PHONE);
            if (phone.contains("+"))
                phone = phone.trim().replace("+", "");
            if (phone.contains("-"))
                phone = phone.trim().replace("-", "");
            commDTO.setValue(phone);
            responseDTO.add(commDTO);

        } catch (Exception e) {
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
            KMSBusinessDelegate kmsBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(KMSBusinessDelegate.class);
            Map<String, Object> input = new HashMap<>();
            input.put("FirstName", customerInfo.getFirstName());
            input.put("EmailType", DBPUtilitiesConstants.ENROLLMENT_USERNAME_TEMPLATE);
            input.put("LastName", customerInfo.getLastName());
            JSONObject addContext = new JSONObject();
            addContext.put("resetPasswordLink", EnvironmentConfigurationsHandler.getValue("DBP_OLB_BASE_URL"));
            addContext.put("userName", customerInfo.getUserName());
            addContext.put("activationCodeExpiry",
                    String.valueOf((Integer.parseInt(
                            configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_EXPIRYTIME))
                            / 1440)));
            input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
            input.put("Email", commDTO.getValue());
            kmsBusinessDelegate.sendKMSEmail(input, headersMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while sending email " + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10746);
        }
    }

    @Override
    public void sendEnrollActivationCodeToMobile(Boolean isAdmin, Boolean isEnrolled, CustomerCommunicationDTO commDTO,
            String activationCode,
            Map<String, Object> headersMap)
            throws ApplicationException {
        try {
            KMSBusinessDelegate kmsBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(KMSBusinessDelegate.class);
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("Phone", commDTO.getValue());
            inputParams.put("otp", activationCode);
            inputParams.put("MessageType", DBPUtilitiesConstants.ENROLLMENT_ACTIVATIONCODE_TEMPLATE);
            if (isEnrolled && !isAdmin) {
                String scaenabled = EnvironmentConfigurationsHandler.getValue("IS_SCA_ENABLED");
                if (StringUtils.isNotBlank(scaenabled) && Boolean.valueOf(scaenabled))
                    inputParams.put("MessageType", DBPUtilitiesConstants.DEVICE_REGISTRATION_ACTIVATIONCODE_TEMPLATE);
            }
            kmsBusinessDelegate.sendKMSSMS(inputParams, headersMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while sending SMS" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10745);
        }

    }

    @Override
    public void sendResetPasswordActivationCodeToMobile(CustomerCommunicationDTO commDTO, String activationCode,
            Map<String, Object> headersMap)
            throws ApplicationException {
        try {
            KMSBusinessDelegate kmsBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(KMSBusinessDelegate.class);
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("Phone", commDTO.getValue());
            inputParams.put("otp", activationCode);
            inputParams.put("MessageType", DBPUtilitiesConstants.ENROLLMENT_ACTIVATIONCODE_TEMPLATE);
            kmsBusinessDelegate.sendKMSSMS(inputParams, headersMap);
        } catch (Exception e) {
            alert.prepareError("Exception occured while sending SMS" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10745);
        }

    }

    @Override
    public boolean activationCodeValidationForEnrollment(Map<String, String> configurations,
            CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {

        boolean status = false;
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("id", credentialcheckerDTO.getId());
        inputParams.put("UserName", credentialcheckerDTO.getUserName());
        inputParams.put(DBPUtilitiesConstants.FILTER,
                "UserName" + DBPUtilitiesConstants.EQUAL + credentialcheckerDTO.getUserName()
                        + DBPUtilitiesConstants.AND + "linktype" + DBPUtilitiesConstants.EQUAL
                        + HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
        Integer retryCount = 0;
        CredentialCheckerDTO responseDTO = new CredentialCheckerDTO();
        try {
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CREDENTIAL_CHECKER_GET);
            if (JSONUtil.hasKey(response, MWConstants.OPSTATUS)
                    && JSONUtil.getString(response, MWConstants.OPSTATUS).equalsIgnoreCase("0")
                    && JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_CREDENTIALCHECKER)
                    && response.get(DBPDatasetConstants.DATASET_CREDENTIALCHECKER).getAsJsonArray().size() > 0) {
                responseDTO =
                        (CredentialCheckerDTO) DTOUtils
                                .loadJsonObjectIntoObject(response.get(DBPDatasetConstants.DATASET_CREDENTIALCHECKER)
                                        .getAsJsonArray().get(0).getAsJsonObject(), CredentialCheckerDTO.class, true);
                retryCount = Integer.parseInt(responseDTO.getRetryCount());
                Integer allowedCount = Integer.valueOf(
                        configurations.get(BundleConfigurationHandler.ACTIVATIONCODE_VALIDATIONATTEMPTS));
                if (retryCount + 1 == allowedCount) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10747);
                }
                if (responseDTO.getId() != null
                        && responseDTO.getId().equals(credentialcheckerDTO.getId())) {
                    status = HelperMethods
                            .isDateInRange(
                                    responseDTO.getCreatedts(),
                                    Integer.parseInt(configurations
                                            .get(BundleConfigurationHandler.ACTIVATIONCODE_EXPIRYTIME)));
                    if (!status) {
                        alert.prepareError("Activation code is expired").log();
                        throw new ApplicationException(ErrorCodeEnum.ERR_10749);
                    }
                } else {
                    alert.prepareError("Activation code and username combination is not present in backend").log();

                    CredentialCheckerDTO dto = new CredentialCheckerDTO();
                    dto.setId(responseDTO.getId());
                    dto.setRetryCount(String.valueOf(retryCount + 1));
                    CredentialCheckerBusinessDelegate bd =
                            DBPAPIAbstractFactoryImpl.getBusinessDelegate(CredentialCheckerBusinessDelegate.class);
                    bd.update(dto, headersMap);
                }
            } else {
                alert.prepareError("Entry is not there with username in credential checker table").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10741);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while validating the activation code" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10741);
        }
        return status;
    }

    @Override
    public String generateServiceKeyForEnrollment(CredentialCheckerDTO credentialcheckerDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        JsonObject payload = new JsonObject();
        payload.addProperty("activationCode", credentialcheckerDTO.getId());
        payload.addProperty("userId", credentialcheckerDTO.getUserName());
        MFAServiceDTO mfaDTO = new MFAServiceDTO();
        try {
            mfaDTO.setUser_(credentialcheckerDTO.getUserName());
            mfaDTO.setPayload(CryptoText.encrypt(payload.toString()));
            mfaDTO.setServiceName(MFAConstants.SERVICE_ID_PRELOGIN);
            mfaDTO.setIsVerified("true");
            MFAServiceBusinessDelegate mfa =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
            mfaDTO = mfa.create(mfaDTO, headersMap);
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while creating entry in mfa service" + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling MFA Business delegate" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10743);
        }
        return mfaDTO.getServiceKey();
    }

    @Override
    public String validateServiceKey(MFAServiceDTO mfaserviceDTO, Map<String, Object> headerMap) {

        MFAServiceBusinessDelegate serviceBusinessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);

        List<MFAServiceDTO> list = new ArrayList<MFAServiceDTO>();
        try {
            list = serviceBusinessDelegate.getMfaService(mfaserviceDTO, null, headerMap);
        } catch (ApplicationException e) {
            alert.prepareError("Caught exception while Reading MFAService : ", e).log();
            return null;
        }
        if (list.size() > 0) {
            mfaserviceDTO = list.get(0);
        } else
            return null;

        MFAServiceUtil mfaServiceUtil = new MFAServiceUtil(mfaserviceDTO);

        try {
            if (mfaServiceUtil.isValidServiceKey(getServiceKeyExpiretime()) && mfaServiceUtil.isStateVerified()) {
                String userName = mfaserviceDTO.getUser_();
                CredentialCheckerDTO dto = new CredentialCheckerDTO();
                dto.setId(new JsonParser().parse(CryptoText.decrypt(mfaserviceDTO.getPayload())).getAsJsonObject()
                        .get("activationCode").getAsString());
                CredentialCheckerBusinessDelegate bd =
                        DBPAPIAbstractFactoryImpl.getBusinessDelegate(CredentialCheckerBusinessDelegate.class);
                bd.delete(dto, headerMap);
                serviceBusinessDelegate.deleteMfaService(mfaserviceDTO.getServiceKey(), headerMap);
                return userName;
            }
        } catch (Exception e) {
            alert.prepareError("Caught exception while Reading MFAService : " + e.getMessage()).log();
            return null;
        }
        return null;
    }

    private int getServiceKeyExpiretime() {

        try {
            return Integer.parseInt(URLFinder.getServerRuntimeProperty(URLConstants.SERVICEKEY_EXPIRE_TIME));
        } catch (Exception e) {
            alert.prepareError("Caught exception while Getting getServiceKeyExpiretime : ", e).log();
        }

        return 10;
    }

    @Override
    public boolean updatePasswordForActivationFlow(CustomerDTO customerDTO, Map<String, Object> headerMap) {

        if (StringUtils.isNotBlank(customerDTO.getPassword())) {
            String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
            String hashedPassword = BCrypt.hashpw(customerDTO.getPassword(), salt);
            customerDTO.setPassword(hashedPassword);
            PasswordHistoryDTO dto = new PasswordHistoryDTO();

            dto.setId(HelperMethods.getNewId());
            dto.setCustomer_id(customerDTO.getId());
            dto.setPreviousPassword(customerDTO.getPassword());
            dto.setIsNew(true);

            PasswordHistoryBusinessDelegate passwordHistoryBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(PasswordHistoryBusinessDelegate.class);

            if (passwordHistoryBusinessDelegate.update(dto, headerMap).getResponse() == null) {
                return false;
            }

            return update(customerDTO, headerMap).getResponse() != null;
        }

        return false;
    }

    @Override
    public void removeServiceKey(MFAServiceDTO mfaserviceDTO, Map<String, Object> headerMap) {

        MFAServiceBusinessDelegate serviceBusinessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
        try {
            serviceBusinessDelegate.deleteMfaService(mfaserviceDTO.getServiceKey(), headerMap);
        } catch (ApplicationException e) {
            alert.prepareError("Caught exception while deleting serviceKey : ", e).log();
        }
    }

    @Override
    public boolean validateServiceKeyAndFetchID(String serviceKey, String id, Map<String, Object> headerMap)
            throws ApplicationException {
        boolean status = false;
        MFAServiceBusinessDelegate serviceBusinessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
        List<MFAServiceDTO> list = new ArrayList<>();
        MFAServiceDTO mfaserviceDTO = new MFAServiceDTO();
        mfaserviceDTO.setServiceKey(serviceKey);
        try {
            list = serviceBusinessDelegate.getMfaService(mfaserviceDTO, null, headerMap);
            if (list != null && !list.isEmpty()) {
                mfaserviceDTO = list.get(0);
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10753);
            }
            MFAServiceUtil mfaServiceUtil = new MFAServiceUtil(mfaserviceDTO);
            if (mfaServiceUtil.isValidServiceKey(getServiceKeyExpiretime())) {
                int index = 0;
                String payload = CryptoText.decrypt(mfaserviceDTO.getPayload());
                String[] values = payload.split(",");
                ArrayList<String> userNameList = new ArrayList<>(Arrays.asList(values));
                index = userNameList.indexOf(id);
                if (index != -1)
                    status = true;
                if (status) {
                    String userAttributesArray = CryptoText.decrypt(mfaserviceDTO.getSecurityQuestions());
                    JsonArray jsonArray = new JsonParser().parse(userAttributesArray).getAsJsonArray();
                    for (JsonElement jsonElement : jsonArray) {
                        if (index > 0) {
                            index--;
                            continue;
                        }
                        JsonObject json = jsonElement.getAsJsonObject();
                        status = (id.equalsIgnoreCase(json.get("id").getAsString())
                                && DBPUtilitiesConstants.CUSTOMER_STATUS_NEW
                                        .equals(json.get("Status_id").getAsString()));
                        break;
                    }
                }
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching id" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10754);
        }
        return status;
    }

}
