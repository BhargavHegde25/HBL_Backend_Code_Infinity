package com.temenos.dbx.product.organization.resource.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.CreateCustomerTermsandConditionsPreLogin;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.AddressBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessConfigurationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.BusinessSignatoryDTO;
import com.temenos.dbx.product.dto.BusinessTypeDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.dto.CustomerBusinessTypeDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.OrganisationDTO;
import com.temenos.dbx.product.dto.OrganizationAddressDTO;
import com.temenos.dbx.product.dto.OrganizationCommunicationDTO;
import com.temenos.dbx.product.dto.OrganizationEmployeesDTO;
import com.temenos.dbx.product.dto.OrganizationMembershipDTO;
import com.temenos.dbx.product.dto.OrganizationsFeatureActionsDTO;
import com.temenos.dbx.product.dto.SignatoryTypeDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.BusinessSignatoryBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationAddressBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationCommunicationBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationFeaturesActionsBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationMembershipBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.SignatoryTypeBusinessDelegate;
import com.temenos.dbx.product.organization.resource.api.OrganizationResource;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CommunicationBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerBusinessTypeBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.product.utils.DTOMappings;
import com.temenos.dbx.product.utils.DTOUtils;

/**
 * 
 * @author Infinity DBX
 *
 */
public class OrganizationResourceImpl implements OrganizationResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    SimpleDateFormat idFormatter = new SimpleDateFormat("yyMMddHHmmssSSS");

    @Override
    public Result getListOfOrganisationsByStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_STATUSID = "statusId";
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String statusId = StringUtils.isNotBlank(inputParams.get(INPUT_STATUSID)) ? inputParams.get(INPUT_STATUSID)
                    : dcRequest.getParameter(INPUT_STATUSID);

            OrganizationBusinessDelegate orgBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(OrganizationBusinessDelegate.class);

            List<OrganisationDTO> dtoList = orgBD.getListOfOrganisationsByStatus(statusId, dcRequest.getHeaderMap());
            if (null != dtoList) {
                String orgListString = JSONUtils.stringifyCollectionWithTypeInfo(dtoList, OrganisationDTO.class);
                JSONArray orgListArray = new JSONArray(orgListString);
                JSONObject orgListJson = new JSONObject();
                orgListJson.put(DBPDatasetConstants.DATASET_ORGANISATION, orgListArray);
                result = ConvertJsonToResult.convert(orgListJson.toString());
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10225);

        }
        return result;
    }

    @Override
    public Result updateOrganizationStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        HelperMethods.removeNullValues(inputParams);
        List<String> allowedStatus = new ArrayList<>();
        allowedStatus.add(DBPUtilitiesConstants.ORG_STATUS_ACTIVE);
        allowedStatus.add(DBPUtilitiesConstants.ORG_STATUS_REJECTED);

        String statusId = StringUtils.isNotBlank(inputParams.get("statusId")) ? inputParams.get("statusId")
                : dcRequest.getParameter("statusId");
        String orgId = StringUtils.isNotBlank(inputParams.get("organizationId")) ? inputParams.get("organizationId")
                : dcRequest.getParameter("organizationId");

        if (StringUtils.isBlank(statusId) || StringUtils.isBlank(orgId) || !allowedStatus.contains(statusId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10265);
        }

        OrganisationDTO inputDTO = new OrganisationDTO();
        OrganisationDTO outputDTO = new OrganisationDTO();
        inputDTO.setId(orgId);
        try {
            OrganizationBusinessDelegate organizationBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(OrganizationBusinessDelegate.class);

            outputDTO = organizationBusinessDelegate.getOrganization(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while updating organisation status").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling the OrganizationBusiness delegate call :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10264);
        }

        if (StringUtils.isNotBlank(outputDTO.getStatusId())
                && DBPUtilitiesConstants.ORG_STATUS_ACTIVE.equalsIgnoreCase(outputDTO.getStatusId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10332);
        }
        if (DBPUtilitiesConstants.ORG_STATUS_REJECTED.equalsIgnoreCase(statusId)) {
            inputDTO.setRejectedReason(dcRequest.getParameter("rejectedReason"));
            inputDTO.setRejectedby(dcRequest.getParameter("rejectedBy"));
            inputDTO.setRejectedts(HelperMethods.getCurrentTimeStamp());
        }
        boolean isUpdateSuccess = false;

        inputDTO.setStatusId(statusId);
        try {
            OrganizationBusinessDelegate organizationBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(OrganizationBusinessDelegate.class);

            isUpdateSuccess = organizationBusinessDelegate.updateOrganizationStatus(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            alert.prepareError("Exception occured while updating organisation status").log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while calling the OrganizationBusiness delegate call :" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10264);
        }
        result.addStringParam(DBPUtilitiesConstants.IS_UPDATE_SUCEES, String.valueOf(isUpdateSuccess));
        dcRequest.addRequestParam_(DBPUtilitiesConstants.IS_UPDATE_SUCEES, String.valueOf(isUpdateSuccess));
        return result;
    }

    @Override
    public Result createOrganization(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_ORGTYPE = "Type";
        final String INPUT_ORGNAME = "Name";
        final String INPUT_COMMUNICATION = "Communication";
        final String INPUT_ADDRESS = "Address";
        final String INPUT_MEMBERSHIP = "Membership";
        final String INPUT_ACCOUNTSLIST = "AccountsList";
        final String INPUT_FEATURES = "features";
        final String INPUT_AUTHORIZEDSIGNATORY = "AuthorizedSignatory";
        final String INPUT_SERVICEKEY = "serviceKey";
        final String INPUT_GROUPID = "AuthorizedSignatoryGroupId";

        final String ROLETYPE = "TYPE_ID_BUSINESS";
        String orgTypeID = null;
        String organizationId = null;
        String customerId = null;
        String autoApprovalStatus = null;
        Set<String> orgActionsSet = null;

        Result result = new Result();

        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String orgType = StringUtils.isNotBlank(inputParams.get(INPUT_ORGTYPE)) ? inputParams.get(INPUT_ORGTYPE)
                    : dcRequest.getParameter(INPUT_ORGTYPE);
            String orgName = StringUtils.isNotBlank(inputParams.get(INPUT_ORGNAME)) ? inputParams.get(INPUT_ORGNAME)
                    : dcRequest.getParameter(INPUT_ORGNAME);
            String orgCommunication = StringUtils.isNotBlank(inputParams.get(INPUT_COMMUNICATION))
                    ? inputParams.get(INPUT_COMMUNICATION)
                    : dcRequest.getParameter(INPUT_COMMUNICATION);
            String orgAddress = StringUtils.isNotBlank(inputParams.get(INPUT_ADDRESS)) ? inputParams.get(INPUT_ADDRESS)
                    : dcRequest.getParameter(INPUT_ADDRESS);
            String orgMembershipList = StringUtils.isNotBlank(inputParams.get(INPUT_MEMBERSHIP))
                    ? inputParams.get(INPUT_MEMBERSHIP)
                    : dcRequest.getParameter(INPUT_MEMBERSHIP);
            String orgAccountsLIst = StringUtils.isNotBlank(inputParams.get(INPUT_ACCOUNTSLIST))
                    ? inputParams.get(INPUT_ACCOUNTSLIST)
                    : dcRequest.getParameter(INPUT_ACCOUNTSLIST);
            String orgFeatures = StringUtils.isNotBlank(inputParams.get(INPUT_FEATURES))
                    ? inputParams.get(INPUT_FEATURES)
                    : dcRequest.getParameter(INPUT_FEATURES);
            String authorizedSignatory = StringUtils.isNotBlank(inputParams.get(INPUT_AUTHORIZEDSIGNATORY))
                    ? inputParams.get(INPUT_AUTHORIZEDSIGNATORY)
                    : dcRequest.getParameter(INPUT_AUTHORIZEDSIGNATORY);
            String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUT_SERVICEKEY))
                    ? inputParams.get(INPUT_SERVICEKEY)
                    : dcRequest.getParameter(INPUT_SERVICEKEY);
            String groupId = StringUtils.isNotBlank(inputParams.get(INPUT_GROUPID)) ? inputParams.get(INPUT_GROUPID)
                    : dcRequest.getParameter(INPUT_GROUPID);

            if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(orgType) || StringUtils.isBlank(orgName)
                    || StringUtils.isBlank(orgAccountsLIst) || StringUtils.isBlank(orgFeatures)
                    || StringUtils.isBlank(authorizedSignatory)) {
                ErrorCodeEnum.ERR_10290.setErrorCode(result);
                return result;
            }
            Map<String, String> businessTypes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            List<BusinessTypeDTO> businessTypeDTOList = new ArrayList<>();
            try {
                BusinessTypeBusinessDelegate businessTypeBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(BusinessTypeBusinessDelegate.class);
                businessTypeDTOList =
                        businessTypeBusinessDelegate.getBusinessType(dcRequest.getHeaderMap());
            } catch (Exception e) {
                alert.prepareError("Exception occured while fetching the business types").log();
            }
            for (BusinessTypeDTO dto : businessTypeDTOList) {
                businessTypes.put(dto.getName(), dto.getId());
            }

            orgTypeID = businessTypes.get(orgType);
            if (StringUtils.isBlank(orgTypeID)) {
                ErrorCodeEnum.ERR_10291.setErrorCode(result);
                return result;
            }

            JsonObject payloadJson = getPayload(serviceKey, dcRequest);

            List<AccountsDTO> accountsList = getDTOList(orgAccountsLIst, AccountsDTO.class);

            List<OrganizationMembershipDTO> membershipList = getDTOList(orgMembershipList,
                    OrganizationMembershipDTO.class);

            List<CustomerDTO> authorizedSignatoryList = getDTOList(authorizedSignatory, CustomerDTO.class);

            List<AddressDTO> addressList = getDTOList(orgAddress, AddressDTO.class);

            String authorizedSignatoryName = authorizedSignatoryList.get(0).getFirstName();

            if (!validateAccountsListAndUpdateMembershipList(accountsList, membershipList, dcRequest)) {
                ErrorCodeEnum.ERR_10292.setErrorCode(result);
                return result;
            }

            if (!validatePayload(payloadJson, accountsList, authorizedSignatoryList, dcRequest)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10328);
            }
            ApplicationBusinessDelegate applicationBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(ApplicationBusinessDelegate.class);

            ApplicationDTO applicationDTO = applicationBD.getApplicationProperties(dcRequest.getHeaderMap());
            if ("0".equals(applicationDTO.getIsAccountCentricCore())
                    || DBPUtilitiesConstants.BOOLEAN_STRING_FALSE.equals(applicationDTO.getIsAccountCentricCore())) {
                orgCommunication = getCompanyCommunicationInCustomerCentricFlow(payloadJson);

            }
            autoApprovalStatus = getAutoApprovalStatus(dcRequest);

            OrganisationDTO orgDTO =
                    createOrganization(orgName, orgTypeID, ROLETYPE, authorizedSignatoryName, autoApprovalStatus,
                            dcRequest);

            if (null != orgDTO && StringUtils.isNotBlank(orgDTO.getId())) {
                organizationId = orgDTO.getId();

                createOrgnizationMembership(membershipList, organizationId, dcRequest);

                orgActionsSet = createOrganizationFeaturesAndActions(orgFeatures, organizationId, ROLETYPE, dcRequest);

                createOrganizationAccounts(accountsList, organizationId, orgActionsSet, dcRequest);

                createOrganizationAddress(addressList, organizationId, authorizedSignatoryName, dcRequest);

                createOrganizationCommunication(orgCommunication, organizationId, authorizedSignatoryName, dcRequest);

                customerId = createAuthorizedSignatory(authorizedSignatoryList, groupId, accountsList, orgActionsSet,
                        organizationId, orgTypeID, payloadJson, orgType, dcRequest);

                CreateCustomerTermsandConditionsPreLogin.invoke(customerId, DBPUtilitiesConstants.TNC_BUSINESSENROLL,
                        null, dcRequest);

            } else {
                ErrorCodeEnum.ERR_10281.setErrorCode(result);
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10281);
        }
        if (!HelperMethods.hasError(result)) {
            result.addParam(new Param("success", "Successful", DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("id", organizationId, DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("customerId", customerId, DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("isAutoApproved",
                    DBPUtilitiesConstants.ORGANISATION_STATUS_ACTIVE.equalsIgnoreCase(autoApprovalStatus) ? "true"
                            : "false",
                    DBPUtilitiesConstants.STRING_TYPE));
            dcRequest.addRequestParam_("organizationId", organizationId);
        }
        return result;
    }

    private String getCompanyCommunicationInCustomerCentricFlow(JsonObject payloadJson) {
        String communicationString = new String();
        JsonObject requestPayload =
                JSONUtil.hasKey(payloadJson, "requestPayload") ? payloadJson.get("requestPayload").getAsJsonObject()
                        : null;
        if (JSONUtil.isJsonNotNull(requestPayload)) {
            String phone =
                    JSONUtil.hasKey(requestPayload, "companyPhone") ? requestPayload.get("companyPhone").getAsString()
                            : "";
            String email =
                    JSONUtil.hasKey(requestPayload, "companyEmail") ? requestPayload.get("companyEmail").getAsString()
                            : "";
            if (StringUtils.isNotBlank(phone) || StringUtils.isNotBlank(email)) {
                JsonArray commArray = new JsonArray();
                JsonObject commRecord = new JsonObject();
                commRecord.addProperty("Phone", phone);
                commRecord.addProperty("Email", email);
                commArray.add(commRecord);
                communicationString = commArray.toString();
            }
        }
        return communicationString;
    }

    private boolean validatePayload(JsonObject payloadJson, List<AccountsDTO> accountsList,
            List<CustomerDTO> authorizedSignatoryList, DataControllerRequest dcRequest) throws ApplicationException {
        ApplicationBusinessDelegate applicationBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(ApplicationBusinessDelegate.class);

        ApplicationDTO applicationDTO = applicationBD.getApplicationProperties(dcRequest.getHeaderMap());
        if ("0".equals(applicationDTO.getIsAccountCentricCore())
                || "false".equals(applicationDTO.getIsAccountCentricCore())) {
            return validateCustomerCentricPayload(payloadJson, accountsList, authorizedSignatoryList);

        } else if ("1".equals(applicationDTO.getIsAccountCentricCore())
                || "true".equals(applicationDTO.getIsAccountCentricCore())) {
            return validateAccountCentricPayload(payloadJson, accountsList, authorizedSignatoryList);

        }
        return false;

    }

    private boolean validateAccountCentricPayload(JsonObject payloadJson, List<AccountsDTO> accountsList,
            List<CustomerDTO> authorizedSignatoryList) {
        final String PAYLOAD_ACCOUNTSARRAY_KEY = "accounts";
        final String PAYLOAD_ACCOUNT_CUSTOMERINFO_KEY = "customerInformation";
        boolean status = false;
        if (JSONUtil.isJsonNotNull(payloadJson) && JSONUtil.hasKey(payloadJson, PAYLOAD_ACCOUNTSARRAY_KEY)
                && payloadJson.get(PAYLOAD_ACCOUNTSARRAY_KEY).isJsonArray()
                && payloadJson.get(PAYLOAD_ACCOUNTSARRAY_KEY).getAsJsonArray().size() > 0) {
            JsonArray accountsArray = payloadJson.get(PAYLOAD_ACCOUNTSARRAY_KEY).getAsJsonArray();
            Set<String> accountsSet = new HashSet<>();
            for (JsonElement element : accountsArray) {
                if (element.isJsonObject()) {
                    JsonObject account = element.getAsJsonObject();
                    accountsSet.add(account.get("accountId").getAsString());
                    if (JSONUtil.hasKey(account, PAYLOAD_ACCOUNT_CUSTOMERINFO_KEY)
                            && account.get(PAYLOAD_ACCOUNT_CUSTOMERINFO_KEY).isJsonObject()) {
                        JsonObject customerInfo = account.get(PAYLOAD_ACCOUNT_CUSTOMERINFO_KEY).getAsJsonObject();
                        String payloadSsn = customerInfo.has("ssn") ? customerInfo.get("ssn").getAsString() : "";
                        String payloadDOB = customerInfo.has("dateOfBirth")
                                ? customerInfo.get("dateOfBirth").getAsString()
                                : "";
                        String payloadLastName = customerInfo.has("lastName")
                                ? customerInfo.get("lastName").getAsString()
                                : "";

                        CustomerDTO dto = authorizedSignatoryList.get(0);
                        status = payloadSsn.equalsIgnoreCase(dto.getSsn())
                                && payloadDOB.equalsIgnoreCase(dto.getDateOfBirth())
                                && payloadLastName.equalsIgnoreCase(dto.getLastName());
                    }
                    if (!status) {
                        break;
                    }

                }
            }
            if (status) {
                for (AccountsDTO accountDTO : accountsList) {
                    if (!accountsSet.contains(accountDTO.getAccountId())) {
                        status = false;
                        break;
                    }

                }
            }

        }
        return status;
    }

    private boolean validateCustomerCentricPayload(JsonObject payloadJson, List<AccountsDTO> accountsList,
            List<CustomerDTO> authorizedSignatoryList) {
        JsonObject object = payloadJson.get("requestPayload").getAsJsonObject();
        String payloadSsn = object.has("Ssn") ? object.get("Ssn").getAsString() : "";
        String payloadDOB = object.has("DateOfBirth") ? object.get("DateOfBirth").getAsString() : "";
        String payloadLastName = object.has("LastName") ? object.get("LastName").getAsString() : "";

        CustomerDTO dto = authorizedSignatoryList.get(0);
        boolean status = payloadSsn.equalsIgnoreCase(dto.getSsn()) && payloadDOB.equalsIgnoreCase(dto.getDateOfBirth())
                && payloadLastName.equalsIgnoreCase(dto.getLastName());

        if (status) {
            JsonArray payloadAccountsArray = payloadJson.get("Accounts").getAsJsonArray();
            Set<String> accountsSet = new HashSet<>();
            for (JsonElement element : payloadAccountsArray) {
                if (element.isJsonObject()) {
                    JsonObject account = element.getAsJsonObject();
                    accountsSet.add(account.get("accountId").getAsString());
                }

            }
            for (AccountsDTO accountDTO : accountsList) {
                if (!accountsSet.contains(accountDTO.getAccountId())) {
                    status = false;
                    break;
                }

            }

        }
        return status;
    }

    private JsonObject getPayload(String serviceKey, DataControllerRequest dcRequest) throws ApplicationException {

        MFAServiceDTO dto = new MFAServiceDTO();
        dto.setServiceKey(serviceKey);
        MFAServiceBusinessDelegate bd = DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
        List<MFAServiceDTO> list = bd.getMfaService(dto, new HashMap<>(), dcRequest.getHeaderMap());
        if (list.size() > 0) {
            dto = list.get(0);
        }
        MFAServiceUtil util = new MFAServiceUtil(dto);
        if (!util.isStateVerified()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10324);
        }
        int otpValidTimeInMinutes = util.getServiceKeyExpiretime(dcRequest);
        if (!util.isValidServiceKey(otpValidTimeInMinutes)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10325);
        }
        return util.getRequestPayload();
    }

    private String createAuthorizedSignatory(List<CustomerDTO> authorizedSignatoryList, String groupId,
            List<AccountsDTO> accountsList, Set<String> orgActionsSet, String organizationId, String orgTypeID,
            JsonObject payloadJson, String orgType, DataControllerRequest dcRequest) throws ApplicationException {

        UserManagementBusinessDelegate customerBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(UserManagementBusinessDelegate.class);

        StringBuilder email = new StringBuilder();
        StringBuilder phone = new StringBuilder();
        CustomerDTO customerDTO = authorizedSignatoryList.get(0);
        String id = idFormatter.format(new Date());
        customerDTO.setId(id);
        customerDTO.setUserName(id);
        customerDTO.setIsNew(true);
        customerDTO.setOrganization_Id(organizationId);
        customerDTO.setStatus_id("SID_CUS_NEW");
        customerDTO.setOrganizationType(orgTypeID);
        customerDTO.setCustomerType_id(DBPUtilitiesConstants.TYPE_ID_BUSINESS);

        DBXResult customerResult = customerBD.update(customerDTO, dcRequest.getHeaderMap());
        String customerId = (String) customerResult.getResponse();
        if (StringUtils.isBlank(customerId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10314);
        }

        updateCommunicationDetails(phone, email, payloadJson);
        createCustomerCommunication(email, phone, customerId, dcRequest);
        createOrganizationEmployee(organizationId, customerId, dcRequest);
        createCustomerAccounts(accountsList, customerId, organizationId, dcRequest);
        createCustomerActions(orgActionsSet, accountsList, customerId, orgTypeID, groupId, dcRequest);
        createCustomerGroup(customerId, groupId, dcRequest);
        createCustomerBusinessType(customerId, orgTypeID, dcRequest);
        return customerId;

    }

    private void createCustomerBusinessType(String customerId, String orgTypeID, DataControllerRequest dcRequest)
            throws ApplicationException {
        BusinessSignatoryBusinessDelegate businessSignatoryBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(BusinessSignatoryBusinessDelegate.class);

        BusinessSignatoryDTO dto = new BusinessSignatoryDTO();
        dto.setBusinessTypeId(orgTypeID);
        String signatoryTypeId = null;
        List<BusinessSignatoryDTO> sigDTOLIst = businessSignatoryBD.getBusinessSignatories(dto,
                dcRequest.getHeaderMap());

        if (null != sigDTOLIst && !sigDTOLIst.isEmpty()) {
            signatoryTypeId = sigDTOLIst.get(0).getSignatoryId();
        }

        CustomerBusinessTypeBusinessDelegate customerBusinessTypeBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerBusinessTypeBusinessDelegate.class);

        CustomerBusinessTypeDTO customerBusinessTypeDTO = new CustomerBusinessTypeDTO();
        customerBusinessTypeDTO.setCustomerId(customerId);
        customerBusinessTypeDTO.setBusinessTypeId(orgTypeID);
        customerBusinessTypeDTO.setSignatoryTypeId(signatoryTypeId);

        CustomerBusinessTypeDTO resultDTO = customerBusinessTypeBD.createCustomerBusinessType(customerBusinessTypeDTO,
                dcRequest.getHeaderMap());
        if (null == resultDTO || StringUtils.isBlank(resultDTO.getCustomerId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10330);
        }

    }

    private void createCustomerGroup(String customerId, String groupId, DataControllerRequest dcRequest)
            throws ApplicationException {

        CustomerGroupBusinessDelegate customerGroupBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerGroupBusinessDelegate.class);

        CustomerGroupDTO dto = new CustomerGroupDTO();
        dto.setCustomerId(customerId);
        dto.setGroupId(groupId);
        customerGroupBD.createCustomerGroup(dto, dcRequest.getHeaderMap());

    }

    private void createCustomerActions(Set<String> orgActionsSet, List<AccountsDTO> accountsList, String customerId,
            String orgTypeID, String groupId, DataControllerRequest dcRequest) throws ApplicationException {
        if (null == orgActionsSet || orgActionsSet.isEmpty() || null == accountsList || accountsList.isEmpty()) {
            return;
        }
        StringBuilder actionsCSV = new StringBuilder();

        for (String action : orgActionsSet) {
            actionsCSV.append(action).append(DBPUtilitiesConstants.ACTIONS_SEPERATOR);
        }
        if (actionsCSV.length() > 0)
            actionsCSV.replace(actionsCSV.length() - 1, actionsCSV.length(), "");

        OrganizationFeaturesActionsBusinessDelegate customerActionsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationFeaturesActionsBusinessDelegate.class);

        for (AccountsDTO dto : accountsList) {
            customerActionsBD.createCustomerActions(actionsCSV.toString(), dto.getAccountId(), customerId,
                    orgTypeID, groupId, dcRequest.getHeaderMap());
        }

    }

    private void createCustomerAccounts(List<AccountsDTO> accountsList, String customerId, String organizationId,
            DataControllerRequest dcRequest) throws ApplicationException {
        if (null == accountsList) {
            return;
        }
        CustomerAccountsBusinessDelegate customerAccountsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CustomerAccountsBusinessDelegate.class);

        for (AccountsDTO dto : accountsList) {
            CustomerAccountsDTO customerAccountsDTO = new CustomerAccountsDTO();
            customerAccountsDTO.setId(idFormatter.format(new Date()));
            customerAccountsDTO.setCustomerId(customerId);
            customerAccountsDTO.setAccountId(dto.getAccountId());
            customerAccountsDTO.setOrganizationId(organizationId);
            customerAccountsDTO.setIsOrganizationAccount("1");
            customerAccountsDTO.setAccountName(dto.getAccountName());

            CustomerAccountsDTO resultCustomerAccountsDTO = customerAccountsBD
                    .createCustomerAccounts(customerAccountsDTO, dcRequest.getHeaderMap());

            if (null == resultCustomerAccountsDTO || StringUtils.isBlank(resultCustomerAccountsDTO.getAccountId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10320);
            }
        }

    }

    private void createOrganizationEmployee(String organizationId, String customerId, DataControllerRequest dcRequest)
            throws ApplicationException {
        OrganizationEmployeesBusinessDelegate orgEmployeeBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationEmployeesBusinessDelegate.class);

        OrganizationEmployeesDTO dto = new OrganizationEmployeesDTO();
        dto.setCustomerId(customerId);
        dto.setId(idFormatter.format(new Date()));
        dto.setOrganizationId(organizationId);
        dto.setIsAuthSignatory("1");
        dto.setIsAdmin("1");
        dto.setIsOwner("1");

        OrganizationEmployeesDTO resultDTO = orgEmployeeBD.createOrganizationEmployee(dto, dcRequest.getHeaderMap());
        if (null == resultDTO || StringUtils.isBlank(resultDTO.getId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10318);
        }

    }

    private void createCustomerCommunication(StringBuilder email, StringBuilder phone, String customerId,
            DataControllerRequest dcRequest) throws ApplicationException {
        CommunicationBusinessDelegate communicationBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(CommunicationBusinessDelegate.class);

        CustomerCommunicationDTO communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setId(idFormatter.format(new Date()));
        communicationDTO.setValue(phone.toString());
        communicationDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_PHONE);
        communicationDTO.setCustomer_id(customerId);
        communicationDTO.setIsPrimary(true);
        communicationDTO.setExtension(DBPUtilitiesConstants.CUSTOMER_COMMUNICATION_PHONE_DEFAULT_EXTENSION);

        DBXResult dbxResult = communicationBD.create(communicationDTO, dcRequest.getHeaderMap());

        if (null == dbxResult.getResponse()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10316);
        }

        communicationDTO = new CustomerCommunicationDTO();
        communicationDTO.setId(idFormatter.format(new Date()));
        communicationDTO.setValue(email.toString());
        communicationDTO.setType_id(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
        communicationDTO.setCustomer_id(customerId);
        communicationDTO.setIsPrimary(true);

        dbxResult = communicationBD.create(communicationDTO, dcRequest.getHeaderMap());

        if (null == dbxResult.getResponse()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10316);
        }
    }

    private void updateCommunicationDetails(StringBuilder phone, StringBuilder email, JsonObject payloadJson) {
        final String COMMUNICATION_KEY = "communication";
        if (JSONUtil.isJsonNotNull(payloadJson) && JSONUtil.hasKey(payloadJson, COMMUNICATION_KEY)
                && payloadJson.get(COMMUNICATION_KEY).isJsonObject()) {

            JsonObject communication = payloadJson.get(COMMUNICATION_KEY).getAsJsonObject();
            if (communication.has("phone")) {
                JsonArray phoneArray = communication.get("phone").getAsJsonArray();
                phone.append(phoneArray.get(0).getAsJsonObject().get("unmasked").getAsString());
            }
            if (communication.has("email")) {
                JsonArray emailArray = communication.get("email").getAsJsonArray();
                email.append(emailArray.get(0).getAsJsonObject().get("unmasked").getAsString());
            }

        }

    }

    private void createOrganizationCommunication(String orgCommunication, String organizationId,
            String authorizedSignatory, DataControllerRequest dcRequest) throws ApplicationException {
        if (StringUtils.isBlank(orgCommunication)) {
            return;
        }

        OrganizationCommunicationBusinessDelegate orgCommunicationBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationCommunicationBusinessDelegate.class);

        JsonParser parser = new JsonParser();
        JsonArray communicationArray = parser.parse(orgCommunication).getAsJsonArray();
        for (JsonElement element : communicationArray) {
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    String value = entry.getValue().getAsString();
                    String typeId = HelperMethods.getCommunicationTypes().get(entry.getKey());
                    OrganizationCommunicationDTO orgCommunicationDTO = new OrganizationCommunicationDTO();
                    if (StringUtils.isNotBlank(value)) {
                        orgCommunicationDTO.setId(idFormatter.format(new Date()));
                        orgCommunicationDTO.setValue(value);
                        orgCommunicationDTO.setTypeId(typeId);
                        orgCommunicationDTO.setOrganizationId(organizationId);
                        orgCommunicationDTO.setCreatedBy(authorizedSignatory);
                        OrganizationCommunicationDTO resultDTO = orgCommunicationBD
                                .createOrganizationCommunication(orgCommunicationDTO, dcRequest.getHeaderMap());
                        if (null == resultDTO || StringUtils.isBlank(resultDTO.getId())) {
                            throw new ApplicationException(ErrorCodeEnum.ERR_10304);
                        }
                    }

                }
            }

        }

    }

    private void createOrganizationAddress(List<AddressDTO> addressList, String organizationId,
            String authorizedSignatoryName, DataControllerRequest dcRequest) throws ApplicationException {
        AddressBusinessDelegate addressBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AddressBusinessDelegate.class);

        OrganizationAddressBusinessDelegate orgAddressBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationAddressBusinessDelegate.class);

        for (AddressDTO inputDTO : addressList) {
            inputDTO.setId(idFormatter.format(new Date()));
            AddressDTO resultDTO = addressBD.createAddress(inputDTO, dcRequest.getHeaderMap());
            if (null == resultDTO || StringUtils.isBlank(resultDTO.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10300);
            }

            OrganizationAddressDTO orgAddressDTO = new OrganizationAddressDTO();
            orgAddressDTO.setId(idFormatter.format(new Date()));
            orgAddressDTO.setOrganizationId(organizationId);
            orgAddressDTO.setAddressId(resultDTO.getId());
            orgAddressDTO.setCreatedBy(authorizedSignatoryName);
            OrganizationAddressDTO resultOrgAddressDTO = orgAddressBD.createOrganizationAddress(orgAddressDTO,
                    dcRequest.getHeaderMap());
            if (null == resultOrgAddressDTO || StringUtils.isBlank(resultOrgAddressDTO.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10302);
            }

        }

    }

    private void createOrganizationAccounts(List<AccountsDTO> accountsList, String organizationId,
            Set<String> orgActionsSet, DataControllerRequest dcRequest) throws ApplicationException {

        AccountsBusinessDelegate accountsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountsBusinessDelegate.class);

        for (AccountsDTO inputDTO : accountsList) {
            String accountId = inputDTO.getAccountId();
            if (StringUtils.isBlank(accountId)) {
                continue;
            }
            AccountsDTO accountGetDTO = accountsBD.getAccountDetailsByAccountID(accountId, dcRequest.getHeaderMap());
            if (null == accountGetDTO || StringUtils.isBlank(accountGetDTO.getAccountId())) {
                inputDTO.setOrganizationId(organizationId);
                inputDTO.setIsBusinessAccount("1");
                inputDTO.setStatusDescription("Active");
                AccountsDTO accountCreatedDTO = accountsBD.createAccount(inputDTO, dcRequest.getHeaderMap());
                if (null == accountCreatedDTO || StringUtils.isBlank(accountCreatedDTO.getAccountId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10297);
                }

            } else if (StringUtils.isBlank(accountGetDTO.getOrganizationId())
                    && StringUtils.isNotBlank(accountGetDTO.getAccountId())) {
                inputDTO.setOrganizationId(organizationId);
                inputDTO.setIsBusinessAccount("1");
                inputDTO.setStatusDescription("Active");
                AccountsDTO accountCreatedDTO = accountsBD.updateAccount(inputDTO, dcRequest.getHeaderMap());
                if (null == accountCreatedDTO || StringUtils.isBlank(accountCreatedDTO.getAccountId())) {
                    throw new ApplicationException(ErrorCodeEnum.ERR_10296);
                }

            }

            String monetaryActionsCSV = getMonetaryActionsCSV(orgActionsSet, dcRequest);
            String[] monetaryActionIds = StringUtils.isNotBlank(monetaryActionsCSV) ? monetaryActionsCSV.split(",")
                    : new String[0];
            ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

            approvalmatrixDelegate.createDefaultApprovalMatrixEntry(organizationId, accountId, monetaryActionIds,"", null);
        }

    }

    private String getMonetaryActionsCSV(Set<String> orgActionsSet, DataControllerRequest dcRequest)
            throws ApplicationException {
        StringBuilder actionsString = new StringBuilder();
        for (String action : orgActionsSet) {
            actionsString.append(action);
            actionsString.append(",");
        }
        if (actionsString.length() > 0)
            actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

        OrganizationFeaturesActionsBusinessDelegate orgFeatureActionsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationFeaturesActionsBusinessDelegate.class);

        return orgFeatureActionsBD.getMonetaryActions(actionsString.toString(), dcRequest.getHeaderMap());

    }

    private Set<String> createOrganizationFeaturesAndActions(String orgFeatures, String organizationId, String roleType,
            DataControllerRequest dcRequest) throws ApplicationException {
        JsonParser parser = new JsonParser();
        StringBuilder featuresString = new StringBuilder();
        Set<String> actionsSet = new HashSet<>();
        String actionsCSV = null;
        OrganizationFeaturesActionsBusinessDelegate orgFeatureActionsBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationFeaturesActionsBusinessDelegate.class);

        JsonArray featuresArray = parser.parse(orgFeatures).getAsJsonArray();
        for (JsonElement feature : featuresArray) {
            if (JSONUtil.isJsonNotNull(feature) && feature.isJsonPrimitive()) {
                featuresString.append(feature.getAsString());
                featuresString.append(",");
            }
        }

        if (featuresString.length() > 0) {
            featuresString.replace(featuresString.length() - 1, featuresString.length(), "");
        } else {
            return actionsSet;
        }

        OrganizationsFeatureActionsDTO dto = new OrganizationsFeatureActionsDTO();
        dto.setFeatures(featuresString.toString());
        dto.setOrganisationId(organizationId);
        dto.setOrganisationType(roleType);

        boolean status = orgFeatureActionsBD.createOrganizationFeatures(dto, dcRequest.getHeaderMap());
        if (status) {
            actionsCSV = orgFeatureActionsBD.createOrganizationActionLimits(dto, dcRequest.getHeaderMap());
        }
        if (StringUtils.isNotBlank(actionsCSV)) {
            actionsSet = HelperMethods.splitString(actionsCSV, DBPUtilitiesConstants.ACTIONS_SEPERATOR);
        }

        return actionsSet;
    }

    private void createOrgnizationMembership(List<OrganizationMembershipDTO> membershipList, String organizationId,
            DataControllerRequest dcRequest) throws ApplicationException {
        OrganizationMembershipBusinessDelegate orgMembershipBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationMembershipBusinessDelegate.class);

        OrganizationMembershipDTO statusDto = null;
        for (OrganizationMembershipDTO dto : membershipList) {
            dto.setId(idFormatter.format(new Date()));
            dto.setOrganisationId(organizationId);
            statusDto = orgMembershipBD.createOrganizationMembership(dto, dcRequest.getHeaderMap());
            if (null == statusDto || StringUtils.isBlank(statusDto.getId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10287);
            }
        }
    }

    private OrganisationDTO createOrganization(String orgName, String businessTypeId, String roleType,
            String authorizedSignatoryName, String autoApprovalStatus, DataControllerRequest dcRequest)
            throws ApplicationException {
        OrganisationDTO dto = new OrganisationDTO();
        OrganisationDTO existingOrgDTO = null;
        dto.setId(HelperMethods.generateUniqueOrganisationId(dcRequest));
        dto.setName(orgName);
        dto.setTypeId(roleType);
        dto.setBusinessTypeId(businessTypeId);
        dto.setCreatedby(authorizedSignatoryName);
        dto.setStatusId(autoApprovalStatus);

        OrganizationBusinessDelegate orgBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationBusinessDelegate.class);
        existingOrgDTO = orgBD.getOrganizationByName(orgName, dcRequest.getHeaderMap());
        if (null != existingOrgDTO && !StringUtils.isBlank(existingOrgDTO.getId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10337);
        }

        dto = orgBD.createOrganization(dto, dcRequest.getHeaderMap());
        return dto;
    }

    private String getAutoApprovalStatus(DataControllerRequest dcRequest) throws ApplicationException {
        BusinessConfigurationBusinessDelegate configurationBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(BusinessConfigurationBusinessDelegate.class);

        return configurationBD.getAutoApprovalStatus(dcRequest.getHeaderMap());
    }

    private boolean validateAccountsListAndUpdateMembershipList(List<AccountsDTO> accountsDTOList,
            List<OrganizationMembershipDTO> membershipDTOList, DataControllerRequest dcRequest)
            throws ApplicationException {
        Set<String> membershipIdList = getMembershipIdList(membershipDTOList);
        StringBuilder givenAccountsListString = new StringBuilder();
        String[] validAccountsList;
        for (AccountsDTO accountDTO : accountsDTOList) {
            String accountId = accountDTO.getAccountId();
            String membershipId = accountDTO.getMembershipId();
            String taxId = accountDTO.getTaxId();
            if (StringUtils.isNotBlank(accountId)) {
                givenAccountsListString.append(accountId).append(",");
            }
            if (StringUtils.isNotBlank(taxId) && !membershipIdList.contains(taxId)) {
                OrganizationMembershipDTO membershipDTO = new OrganizationMembershipDTO();
                membershipDTO.setMembershipId(membershipId);
                membershipDTO.setTaxId(taxId);
                membershipIdList.add(taxId);
                membershipDTOList.add(membershipDTO);
            }

        }
        if (givenAccountsListString.length() > 0) {
            givenAccountsListString.replace(givenAccountsListString.length() - 1, givenAccountsListString.length(), "");
        }
        if (StringUtils.isBlank(givenAccountsListString.toString())) {
            return true;
        } else {
            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            validAccountsList = accountsBusinessDelegate.getUnUsedAccountsList(givenAccountsListString.toString(),
                    dcRequest.getHeaderMap());
        }

        return StringUtils.split(givenAccountsListString.toString(), ",").length == validAccountsList.length;
    }

    private Set<String> getMembershipIdList(List<OrganizationMembershipDTO> membershipDTOList) {
        Set<String> membershipIdSet = new HashSet<>();
        for (OrganizationMembershipDTO membershipDTO : membershipDTOList) {
            if (StringUtils.isNotBlank(membershipDTO.getTaxId())) {
                membershipIdSet.add(membershipDTO.getTaxId());
            }
        }

        return membershipIdSet;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getDTOList(String jsonArrayString, Class<T> classType) {
        List<T> dtoList = new ArrayList<>();
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(jsonArrayString).getAsJsonArray();
        for (JsonElement element : array) {
            if (element.isJsonObject()) {
                T dto = null;
                if (null != DTOMappings.getDTOObjectPropertyMappings(classType)) {
                    dto = (T) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), classType, true);
                } else {
                    dto = (T) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(), classType, false);
                }
                if (null != dto) {
                    dtoList.add(dto);
                }

            }

        }
        return dtoList;
    }

    @Override
    public Result getOrganizationBusinessSignatoryTypes(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> userDetails = HelperMethods.getUserFromIdentityService(dcRequest);
        if (HelperMethods.isAuthenticationCheckRequiredForService(userDetails)) {
            ErrorCodeEnum.ERR_10313.setErrorCode(result);
            return result;
        }

        Dataset ds = new Dataset();
        ds.setId("Signatories");
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        String orgId = StringUtils.isBlank(inputParams.get("organizationId")) ? inputParams.get("organizationId")
                : dcRequest.getParameter("organizationId");

        if (StringUtils.isBlank(orgId)) {
            ErrorCodeEnum.ERR_10309.setErrorCode(result);
            return result;
        }

        OrganisationDTO inputDTO = new OrganisationDTO();
        inputDTO.setId(orgId);
        OrganisationDTO outputDTO = new OrganisationDTO();
        try {
            OrganizationBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(OrganizationBusinessDelegate.class);
            outputDTO = businessDelegate.getOrganization(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10310);
        }

        BusinessSignatoryDTO businessSignatoryDTO = new BusinessSignatoryDTO();
        businessSignatoryDTO.setBusinessTypeId(outputDTO.getBusinessTypeId());

        List<SignatoryTypeDTO> sigTypes = new ArrayList<>();

        try {
            BusinessSignatoryBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(BusinessSignatoryBusinessDelegate.class);
            List<BusinessSignatoryDTO> businessSignatories = businessDelegate
                    .getBusinessSignatories(businessSignatoryDTO, dcRequest.getHeaderMap());
            SignatoryTypeBusinessDelegate sigTypebusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(SignatoryTypeBusinessDelegate.class);
            for (BusinessSignatoryDTO dto : businessSignatories) {
                SignatoryTypeDTO signatoryDTO = new SignatoryTypeDTO();
                signatoryDTO.setId(dto.getSignatoryId());
                SignatoryTypeDTO childDTO = sigTypebusinessDelegate.getSignatoryTypes(signatoryDTO,
                        dcRequest.getHeaderMap());
                sigTypes.add(childDTO);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10311);
        }
        try {
            String signatoryType = JSONUtils.stringifyCollectionWithTypeInfo(sigTypes, SignatoryTypeDTO.class);
            JsonObject signatoriesJson = new JsonObject();
            signatoriesJson.add("SignatoryList", new JsonParser().parse(signatoryType).getAsJsonArray());
            result = ConvertJsonToResult.convert(signatoriesJson);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the signatories").log();
        }
        return result;
    }
}
