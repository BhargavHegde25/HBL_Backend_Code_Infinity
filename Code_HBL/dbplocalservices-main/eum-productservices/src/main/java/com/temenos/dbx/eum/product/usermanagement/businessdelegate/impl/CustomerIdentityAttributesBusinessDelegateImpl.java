package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerIdentityAttributesBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

public class CustomerIdentityAttributesBusinessDelegateImpl implements CustomerIdentityAttributesBusinessDelegate {

    @Override
    public JsonObject getUserAttributes(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {

        if (null == customerDTO || StringUtils.isBlank(customerDTO.getUserName())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10338);
        }

        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        if (!DBPUtilitiesConstants.CUSTOMER_STATUS_ACTIVE.equalsIgnoreCase(customerDTO.getStatus_id())
                && !DBPUtilitiesConstants.CUSTOMER_STATUS_NEW.equalsIgnoreCase(customerDTO.getStatus_id())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10395);
        }
        JsonObject userAttributes = prepareUserAttributes(customerDTO, headerMap);
        return userAttributes;
    }

    private JsonObject prepareUserAttributes(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        JsonObject userAttributes = new JsonObject();
        try {
            userAttributes.addProperty("customer_id", customerDTO.getId());
            userAttributes.addProperty("user_id", customerDTO.getId());
            userAttributes.addProperty("UserName", customerDTO.getUserName());
            userAttributes.addProperty("userFirstName", customerDTO.getFirstName());
            userAttributes.addProperty("first_name", customerDTO.getFirstName());
            userAttributes.addProperty("userLastName", customerDTO.getLastName());
            userAttributes.addProperty("LastName", customerDTO.getLastName());
            userAttributes.addProperty("gender", customerDTO.getGender());
            userAttributes.addProperty("isPinSet", String.valueOf(customerDTO.getIsPinSet()));
            userAttributes.addProperty("noofdependents", String.valueOf(customerDTO.getNoOfDependents()));
            userAttributes.addProperty("spousefirstname", customerDTO.getSpouseName());
            userAttributes.addProperty("ssn", customerDTO.getSsn());
            userAttributes.addProperty("maritalstatus", customerDTO.getMaritalStatus_id());
            userAttributes.addProperty("lastlogintime", HelperMethods.convertDateFormat(
                    customerDTO.getCurrentLoginTime(), "yyyy-MM-dd'T'HH:mm:ss"));
            userAttributes.addProperty("Lastlogintime", HelperMethods.convertDateFormat(
                    customerDTO.getCurrentLoginTime(), "yyyy-MM-dd'T'HH:mm:ss"));
            userAttributes.addProperty("CurrentLoginTime", HelperMethods.getCurrentDate());
            userAttributes.addProperty("areUserAlertsTurnedOn", customerDTO.getAreUserAlertsTurnedOn());
            userAttributes.addProperty("IDType_id", customerDTO.getiDType_id());
            userAttributes.addProperty("IDCountry", customerDTO.getiDCountry());
            userAttributes.addProperty("IDState", customerDTO.getiDState());
            userAttributes.addProperty("IDIssueDate", customerDTO.getiDIssueDate());
            userAttributes.addProperty("IDExpiryDate", customerDTO.getiDExpiryDate());
            userAttributes.addProperty("DateOfBirth", customerDTO.getDateOfBirth());
            userAttributes.addProperty("isCombinedUser", Boolean.toString(customerDTO.isCombinedUser()));
            userAttributes.addProperty("organizationType", customerDTO.getOrganizationType());
            userAttributes.addProperty("CustomerType_id", customerDTO.getCustomerType_id());
            userAttributes.addProperty("customerTypeId", customerDTO.getCustomerType_id());
            userAttributes.addProperty("isEnrolled", Boolean.toString(customerDTO.getIsEnrolled()));
            userAttributes.addProperty("OlbEnrolmentStatus_id", "");
            userAttributes.addProperty("IsCoreIdentityScope", "");
            userAttributes.addProperty("isCSRAssistMode", DBPUtilitiesConstants.BOOLEAN_STRING_FALSE);
            userAttributes.addProperty("Organization_Id", customerDTO.getOrganization_Id());
            userAttributes.addProperty("isSuperAdmin", BooleanUtils.toStringTrueFalse(
                    customerDTO.getIsSuperAdmin()));
            userAttributes.addProperty("CreditUnionMemberSince", customerDTO.getCreditUnionMemberSince());
            userAttributes.addProperty("OlbEnrolmentStatus_id", customerDTO.getOlbEnrolmentStatus_id());
            userAttributes.addProperty("IsStaffMember", BooleanUtils.toStringTrueFalse(customerDTO.getIsStaffMember()));
            userAttributes.addProperty("CountryCode", customerDTO.getCountryCode());
            userAttributes.addProperty("Status_id", customerDTO.getStatus_id());
            userAttributes.addProperty("Bank_id", customerDTO.getBank_id());
            userAttributes.addProperty("areDepositTermsAccepted", customerDTO.getAreDepositTermsAccepted());
            userAttributes.addProperty("FullName", customerDTO.getFullName());
            userAttributes.addProperty("MemberEligibilityData", customerDTO.getMemberEligibilityData());
            userAttributes.addProperty("isP2PSupported",
                    Boolean.TRUE.equals(customerDTO.getIsP2PSupported()) ? "1" : "0");
            userAttributes.addProperty("isP2PActivated",
                    Boolean.TRUE.equals(customerDTO.getIsP2PActivated()) ? "1" : "0");
            userAttributes.addProperty("MiddleName", customerDTO.getMiddleName());
            userAttributes.addProperty("Salutation", customerDTO.getSalutation());
            userAttributes.addProperty("AtionProfile_id", customerDTO.getAtionProfile_id());
            userAttributes.addProperty("IsOlbAllowed", "false");
            userAttributes.addProperty("Is_MemberEligibile", customerDTO.getIs_MemberEligibile());
            userAttributes.addProperty("IsPhoneEnabled",
                    BooleanUtils.toStringTrueFalse(customerDTO.getIsPhoneEnabled()));
            userAttributes.addProperty("IsAssistConsented",
                    BooleanUtils.toStringTrueFalse(customerDTO.getIsAssistConsented()));
            userAttributes.addProperty("areAccountStatementTermsAccepted",
                    customerDTO.getAreAccountStatementTermsAccepted());
            userAttributes.addProperty("isBillPaySupported",
                    Boolean.TRUE.equals(customerDTO.getIsBillPaySupported()) ? "1" : "0");
            userAttributes.addProperty("IsEmailEnabled", BooleanUtils.toStringTrueFalse(
                    customerDTO.getIsEmailEnabled()));
            userAttributes.addProperty("isBillPayActivated",
                    Boolean.TRUE.equals(customerDTO.getIsBillPayActivated()) ? "1" : "0");
            userAttributes.addProperty("isWireTransferEligible",
                    Boolean.TRUE.equals(customerDTO.getIsWireTransferEligible()) ? "1" : "0");
            userAttributes.addProperty("taxId", customerDTO.getTaxID());
            JsonObject preferencesAttributes = getCustomerPreferencesIdentityAttributes(customerDTO, headerMap);

            for (Entry<String, JsonElement> entry : preferencesAttributes.entrySet()) {
                userAttributes.addProperty(entry.getKey(), entry.getValue().getAsString());
            }

            StringBuilder companyId = new StringBuilder();
            String backendIndetifier = getBackendIdentifierString(customerDTO, companyId, headerMap);
            if (StringUtils.isNotBlank(backendIndetifier)) {
                userAttributes.addProperty("backendIdentifiers", backendIndetifier);
            }
            userAttributes.addProperty("companyId", companyId.toString());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10339);
        }

        return userAttributes;
    }

    private JsonObject getCustomerPreferencesIdentityAttributes(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        CustomerPreferenceBusinessDelegate preferencesBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerPreferenceBusinessDelegate.class);
        JsonObject preferencesAttributes = new JsonObject();
        DBXResult result = preferencesBD.getPreferencesIdentityAttributes(customerDTO, headerMap);
        if (result != null && result.getResponse() != null) {
            preferencesAttributes = (JsonObject) result.getResponse();
        }

        return preferencesAttributes;
    }

    @SuppressWarnings("unchecked")
    private String getBackendIdentifierString(CustomerDTO customerDTO, StringBuilder companyId,
            Map<String, Object> headerMap)
            throws ApplicationException {
        JSONObject json = new JSONObject();
        try {
            BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
            backendIdentifierDTO.setCustomer_id(customerDTO.getId());

            BackendIdentifierBusinessDelegate backendidentifierBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
            List<BackendIdentifierDTO> identifierDTOs = null;

            DBXResult result = backendidentifierBD.getList(backendIdentifierDTO, headerMap);
            if (null != result && null != result.getResponse() && StringUtils.isBlank(result.getDbpErrMsg())
                    && StringUtils.isBlank(result.getDbpErrCode())) {
                identifierDTOs = (List<BackendIdentifierDTO>) result.getResponse();
            }

            for (BackendIdentifierDTO dto : identifierDTOs) {
                String backendType = dto.getBackendType();
                Map<String, String> map = new HashMap<>();
                map.put("sequence_number", dto.getSequenceNumber());
                map.put("BackendId", dto.getBackendId());
                map.put("identifier_name", dto.getIdentifier_name());
                map.put("CompanyId", dto.getCompanyId());
                if (StringUtils.isBlank(companyId)) {
                    companyId.append(dto.getCompanyId());
                }
                if (json.has(backendType)) {
                    JSONArray value = json.getJSONArray(backendType);
                    value.put(map);
                } else {
                    JSONArray value = new JSONArray();
                    value.put(map);
                    json.put(backendType, value);
                }
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10339);
        }

        return String.valueOf(json);
    }

    @Override
    public JsonObject getSecurityAttributes(CustomerDTO customerDTO, Map<String, Object> headerMap)
            throws ApplicationException {
        JsonObject securityAttributesResponse = new JsonObject();
        if (null == customerDTO || StringUtils.isBlank(customerDTO.getUserName())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10338);
        }

        customerDTO = (CustomerDTO) customerDTO.loadDTO();

        Set<String> features;
        Set<String> actions;

        CustomerActionsBusinessDelegate businessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerActionsBusinessDelegate.class);
        Map<String, Set<String>> securityAttributes =
                businessDelegate.getSecurityAttributes(customerDTO.getId(), headerMap);
        actions = securityAttributes.get("actions");
        features = securityAttributes.get("features");

        if (null == actions || null == features) {
            actions = new HashSet<>();
            features = new HashSet<>();
        }

        securityAttributesResponse.addProperty("permissions", getJSONString(actions));
        securityAttributesResponse.addProperty("features", getJSONString(features));

        return securityAttributesResponse;
    }

    private String getJSONString(Set<String> set) {
        return (new JSONArray(set.toString())).toString();
    }

}
