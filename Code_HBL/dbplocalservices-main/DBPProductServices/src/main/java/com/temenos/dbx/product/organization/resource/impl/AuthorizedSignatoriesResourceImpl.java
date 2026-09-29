package com.temenos.dbx.product.organization.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.customersecurityservices.PasswordHistoryManagement;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.kms.KMSUtil;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.ContractCoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.ContractCoreCustomersDTO;
import com.temenos.dbx.product.dto.CredentialCheckerDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.dto.OrganisationDTO;
import com.temenos.dbx.product.dto.OrganisationEmployeesViewDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.AuthorizedSignatoriesBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.MembershipBusinessDelegate;
import com.temenos.dbx.product.organization.businessdelegate.api.OrganizationBusinessDelegate;
import com.temenos.dbx.product.organization.resource.api.AuthorizedSignatoriesResource;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CredentialCheckerBusinessDelegate;

public class AuthorizedSignatoriesResourceImpl implements AuthorizedSignatoriesResource {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getAuthorizedSignatoryCommunication(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_FIRSTNAME = "FirstName";
        final String INPUT_LASTNAME = "LastName";
        final String INPUT_DATEOFBIRTH = "DateOfBirth";
        final String INPUT_SSN = "Ssn";
        final String INPUT_CIF = "Cif";
        final String INPUT_COMPANYNAME = "companyName";
        final String INPUT_TAXID = "Taxid";

        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            List<MembershipOwnerDTO> dtoList = null;
            MembershipDTO dto = null;
            String firstName = StringUtils.isNotBlank(inputParams.get(INPUT_FIRSTNAME))
                    ? inputParams.get(INPUT_FIRSTNAME)
                    : dcRequest.getParameter(INPUT_FIRSTNAME);
            String lastName = StringUtils.isNotBlank(inputParams.get(INPUT_LASTNAME)) ? inputParams.get(INPUT_LASTNAME)
                    : dcRequest.getParameter(INPUT_LASTNAME);
            String dateOfBirth = StringUtils.isNotBlank(inputParams.get(INPUT_DATEOFBIRTH))
                    ? inputParams.get(INPUT_DATEOFBIRTH)
                    : dcRequest.getParameter(INPUT_DATEOFBIRTH);
            String ssn = StringUtils.isNotBlank(inputParams.get(INPUT_SSN)) ? inputParams.get(INPUT_SSN)
                    : dcRequest.getParameter(INPUT_SSN);
            String cif = StringUtils.isNotBlank(inputParams.get(INPUT_CIF)) ? inputParams.get(INPUT_CIF)
                    : dcRequest.getParameter(INPUT_CIF);
            String companyName = StringUtils.isNotBlank(inputParams.get(INPUT_COMPANYNAME))
                    ? inputParams.get(INPUT_COMPANYNAME)
                    : dcRequest.getParameter(INPUT_COMPANYNAME);
            String taxId = StringUtils.isNotBlank(inputParams.get(INPUT_TAXID)) ? inputParams.get(INPUT_TAXID)
                    : dcRequest.getParameter(INPUT_TAXID);

            if ((StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)
                    && StringUtils.isNotBlank(dateOfBirth) && StringUtils.isNotBlank(ssn))) {
                if (StringUtils.isNotBlank(cif)) {
                    AuthorizedSignatoriesBusinessDelegate authorizedSigantoryBD = DBPAPIAbstractFactoryImpl
                            .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                            .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);
                    dtoList = authorizedSigantoryBD.getAuthorizedSignatories(cif, dcRequest.getHeaderMap());
                    if (null != dtoList && !dtoList.isEmpty()
                            && StringUtils.isNotBlank(dtoList.get(0).getMembershipId())) {
                        MembershipBusinessDelegate membershipBD = DBPAPIAbstractFactoryImpl
                                .getBusinessDelegate(MembershipBusinessDelegate.class);
                        dto = membershipBD.getMembershipDetails(cif,
                                dcRequest.getHeaderMap());
                    }
                    verifyDTOListAndUpdateResult(dtoList, dto, result, firstName, lastName, ssn, dateOfBirth,
                            dcRequest);

                } else if (StringUtils.isNotBlank(taxId) && StringUtils.isNotBlank(companyName)) {
                    AuthorizedSignatoriesBusinessDelegate authorizedSigantoryBD = DBPAPIAbstractFactoryImpl
                            .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                            .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);
                    dtoList = authorizedSigantoryBD.getAuthorizedSignatoriesByTaxId(taxId, companyName,
                            dcRequest.getHeaderMap());
                    if (null != dtoList && !dtoList.isEmpty()
                            && StringUtils.isNotBlank(dtoList.get(0).getMembershipId())) {
                        MembershipBusinessDelegate membershipBD = DBPAPIAbstractFactoryImpl
                                .getBusinessDelegate(MembershipBusinessDelegate.class);
                        dto = membershipBD.getMembershipDetails(dtoList.get(0).getMembershipId(),
                                dcRequest.getHeaderMap());
                    }
                    verifyDTOListAndUpdateResult(dtoList, dto, result, firstName, lastName, ssn, dateOfBirth,
                            dcRequest);

                } else {
                    ErrorCodeEnum.ERR_10232.setErrorCode(result);
                }

            } else {
                ErrorCodeEnum.ERR_10232.setErrorCode(result);
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10230);
        }
        return result;
    }

    private void verifyDTOListAndUpdateResult(List<MembershipOwnerDTO> dtoList, MembershipDTO membershipDTO,
            Result result, String firstName, String lastName, String ssn, String dateOfBirth,
            DataControllerRequest dcRequest) throws ApplicationException {

        AuthorizedSignatoriesBusinessDelegate authorizedSigantoryBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);
        CoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CoreCustomerBusinessDelegate.class);

        final String RESULT_PARAM_ISCOMPANYEXISTS = "isCompanyExist";

        if (null == dtoList || dtoList.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10231);
        }

        MembershipOwnerDTO dto = authorizedSigantoryBD.verifyGivenAuthorizerDetailsAndGetData(firstName, lastName, ssn,
                dateOfBirth, dtoList);
        if (null == dto) {
            ErrorCodeEnum.ERR_10231.setErrorCode(result);
        } else if (StringUtils.isNotBlank(dto.getPhone()) && !HelperMethods.hasError(result)
                && StringUtils.isNotBlank(dto.getEmail())) {
            dcRequest.addRequestParam_("phone", dto.getPhone().replaceAll("[()\\s-]", ""));
            dcRequest.addRequestParam_("email", dto.getEmail());
            dcRequest.addRequestParam_("Cif", dto.getMembershipId());
            dcRequest.addRequestParam_("companyPhone", membershipDTO.getPhone());
            dcRequest.addRequestParam_("companyEmail", membershipDTO.getEmail());
            dcRequest.addRequestParam_(DBPUtilitiesConstants.BACKENDID, dto.getId());
            JsonObject companyDetails = authorizedSigantoryBD.processDTOAndGetCompanyDetails(dto,
                    dcRequest.getHeaderMap());
            for (Entry<String, JsonElement> entry : companyDetails.entrySet()) {
                result.addParam(
                        new Param(entry.getKey(), entry.getValue().getAsString(), DBPUtilitiesConstants.STRING_TYPE));
            }

            boolean status =
                    coreCustomerBD.checkIfTheCoreCustomerIsEnrolled(dto.getMembershipId(), dcRequest.getHeaderMap());

            // boolean status = accountsBD.checkIfUnUsedAccountsExistsByMembershipId(dto.getMembershipId(),
            // dcRequest.getHeaderMap());

            if (status) {
                result.addParam(new Param("isEnrolled", DBPUtilitiesConstants.BOOLEAN_STRING_TRUE,
                        DBPUtilitiesConstants.STRING_TYPE));
            } else {
                result.addParam(new Param("isEnrolled", DBPUtilitiesConstants.BOOLEAN_STRING_FALSE,
                        DBPUtilitiesConstants.STRING_TYPE));
            }
            result.addParam(new Param(RESULT_PARAM_ISCOMPANYEXISTS, DBPUtilitiesConstants.BOOLEAN_STRING_TRUE,
                    DBPUtilitiesConstants.STRING_TYPE));
        } else {
            ErrorCodeEnum.ERR_10233.setErrorCode(result);
        }

    }

    @Override
    public Result serachAuthorizedSignatories(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_DATEOFBIRTH = "DateOfBirth";
        final String INPUT_SSN = "Ssn";
        final String INPUT_CIF = "Cif";
        final String INPUT_NAME = "UserName";
        final String INPUT_ORGANIZATIONID = "Organization_id";
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            List<MembershipOwnerDTO> dtoList = null;
            String dateOfBirth = StringUtils.isNotBlank(inputParams.get(INPUT_DATEOFBIRTH))
                    ? inputParams.get(INPUT_DATEOFBIRTH)
                    : dcRequest.getParameter(INPUT_DATEOFBIRTH);
            String ssn = StringUtils.isNotBlank(inputParams.get(INPUT_SSN)) ? inputParams.get(INPUT_SSN)
                    : dcRequest.getParameter(INPUT_SSN);
            String cif = StringUtils.isNotBlank(inputParams.get(INPUT_CIF)) ? inputParams.get(INPUT_CIF)
                    : dcRequest.getParameter(INPUT_CIF);
            String name = StringUtils.isNotBlank(inputParams.get(INPUT_NAME)) ? inputParams.get(INPUT_NAME)
                    : dcRequest.getParameter(INPUT_NAME);
            String organizationId = StringUtils.isNotBlank(inputParams.get(INPUT_ORGANIZATIONID))
                    ? inputParams.get(INPUT_ORGANIZATIONID)
                    : dcRequest.getParameter(INPUT_ORGANIZATIONID);
            if (StringUtils.isNotBlank(cif)) {
                AuthorizedSignatoriesBusinessDelegate authorizedSigantoryBD = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);
                dtoList = authorizedSigantoryBD.getAuthorizedSignatories(cif, dcRequest.getHeaderMap());
                Set<MembershipOwnerDTO> filteredList = authorizedSigantoryBD.filterAuthorizedSignatories(name, ssn,
                        dateOfBirth, organizationId, dtoList, dcRequest.getHeaderMap());
                String filteredListString = JSONUtils.stringifyCollectionWithTypeInfo(filteredList,
                        MembershipOwnerDTO.class);
                JSONArray array = new JSONArray(filteredListString);
                JSONObject object = new JSONObject();
                object.put("Customers", array);
                result = ConvertJsonToResult.convert(object.toString());

            } else {
                ErrorCodeEnum.ERR_10232.setErrorCode(result);
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10230);
        }
        return result;
    }

    @Override
    public Result sendMailToAuthorizedSignatories(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        final String CONTRACTID = "contractId";
        final String CONTRACTSTATUS = "contractStatus";
        Map<String, String> inputParams = new HashMap<>();
        String contractId =
                StringUtils.isNotBlank(dcRequest.getParameter(CONTRACTID)) ? dcRequest.getParameter(CONTRACTID)
                        : inputParams.get(CONTRACTID);
        String contractStatus =
                StringUtils.isNotBlank(dcRequest.getParameter(CONTRACTSTATUS)) ? dcRequest.getParameter(CONTRACTSTATUS)
                        : inputParams.get(CONTRACTSTATUS);
        if (StringUtils.isBlank(contractId) || StringUtils.isBlank(contractStatus)) {
            return result;
        }

        ContractCoreCustomerBusinessDelegate coreCustomerBusinessDelegate =
                DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractCoreCustomerBusinessDelegate.class);

        List<ContractCoreCustomersDTO> dtoList = coreCustomerBusinessDelegate.getContractCoreCustomers(contractId,
                false, false, dcRequest.getHeaderMap());

        boolean isAccountCentricCore = getApplicationPropertyConfiguration(dcRequest);

        try {
            AuthorizedSignatoriesBusinessDelegate authorizedSignatoriesBD = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AuthorizedSignatoriesBusinessDelegate.class);
            if (!isAccountCentricCore) {
                authorizedSignatoriesBD.sendMailToAuthorizedSignatoriesInCustomerCentricFlow(dtoList, contractStatus,
                        dcRequest.getHeaderMap());
                result.addStringParam("sucess", "Email Sent Successfully");
            }

        } catch (Exception e) {
            alert.prepareError("Exception occured while sending mail to authorized signatories").log();
        }

        return result;
    }

    private boolean getApplicationPropertyConfiguration(DataControllerRequest dcRequest) {
        try {
            ApplicationBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(ApplicationBusinessDelegate.class);
            ApplicationDTO responseDTO = businessDelegate.getApplicationProperties(dcRequest.getHeaderMap());
            return Boolean.valueOf(responseDTO.getIsAccountCentricCore());
        } catch (Exception e) {
            return false;
        }
    }

    private String getOrganizationStatus(String orgId, DataControllerRequest dcRequest) {
        OrganisationDTO inputDTO = new OrganisationDTO();
        inputDTO.setId(orgId);

        try {
            OrganizationBusinessDelegate orgBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(OrganizationBusinessDelegate.class);
            OrganisationDTO outputDTO = orgBusinessDelegate.getOrganization(inputDTO, dcRequest.getHeaderMap());
            return outputDTO.getStatusId();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Result sendActivationMailToAuthorizedSignatory(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        if ((StringUtils.isNotBlank(dcRequest.getParameter(DBPUtilitiesConstants.IS_UPDATE_SUCEES))
                && "false".equalsIgnoreCase(dcRequest.getParameter(DBPUtilitiesConstants.IS_UPDATE_SUCEES)))) {
            return result;
        }
        String orgId = dcRequest.getParameter("organizationId");
        if (StringUtils.isBlank(orgId))
            return result;

        if (StringUtils.isNotBlank(dcRequest.getParameter("orgStatus"))
                && !DBPUtilitiesConstants.ORGANISATION_STATUS_ACTIVE
                        .equalsIgnoreCase(dcRequest.getParameter("orgStatus"))) {
            return result;
        }

        OrganisationEmployeesViewDTO inputDTO = new OrganisationEmployeesViewDTO();
        inputDTO.setOrgemp_orgid(orgId);
        inputDTO.setCustcomm_typeid(DBPUtilitiesConstants.COMM_TYPE_EMAIL);
        List<OrganisationEmployeesViewDTO> outputDTO = new ArrayList<>();

        try {
            OrganizationEmployeesBusinessDelegate bd = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(OrganizationEmployeesBusinessDelegate.class);
            outputDTO = bd.getOrganizationEmployees(inputDTO, dcRequest.getHeaderMap());
        } catch (Exception e) {
            return result;
        }
        if (outputDTO.isEmpty())
            return result;

        OrganisationEmployeesViewDTO customerInfo = outputDTO.get(0);
        if (!"SID_CUS_NEW".equalsIgnoreCase(customerInfo.getStatusId()))
            return result;

        CredentialCheckerBusinessDelegate credentialCheckerDB = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CredentialCheckerBusinessDelegate.class);
        CredentialCheckerDTO credentialCheckerDTO = new CredentialCheckerDTO();

        credentialCheckerDTO.setUserName(customerInfo.getCustomer_id());
        DBXResult dbxResultResponseDTO = credentialCheckerDB.get(credentialCheckerDTO, dcRequest.getHeaderMap());
        credentialCheckerDTO = ((CredentialCheckerDTO) dbxResultResponseDTO.getResponse());
        if (credentialCheckerDTO != null) {
            credentialCheckerDB.delete(credentialCheckerDTO, dcRequest.getHeaderMap());
        }
        credentialCheckerDTO = new CredentialCheckerDTO();
        String activationToken = UUID.randomUUID().toString();
        credentialCheckerDTO.setId(activationToken);
        credentialCheckerDTO.setUserName(customerInfo.getCustomer_id());
        credentialCheckerDTO.setLinktype(HelperMethods.CREDENTIAL_TYPE.ACTIVATION.toString());
        credentialCheckerDTO.setCreatedts(HelperMethods.getCurrentTimeStamp());

        credentialCheckerDB.create(credentialCheckerDTO, dcRequest.getHeaderMap());

        String link = URLFinder.getPathUrl(URLConstants.DBX_SBB_ACTIVATION_LINK, dcRequest) + "?qp="
                + encodeToBase64(activationToken);

        PasswordHistoryManagement pm = new PasswordHistoryManagement(dcRequest, true);
        Map<String, String> input = new HashMap<>();
        input.put("Subscribe", "true");
        input.put("FirstName", customerInfo.getFirstName());
        input.put("EmailType", DBPUtilitiesConstants.BUSINESS_ENROLLMENT_ACCOUNT_ACTIVATION);
        input.put("LastName", customerInfo.getLastName());
        JSONObject addContext = new JSONObject();
        addContext.put("resetPasswordLink", link);
        addContext.put("userName", customerInfo.getFirstName() + customerInfo.getLastName());
        addContext.put("linkExpiry", String.valueOf(Math.floorDiv(pm.getRecoveryEmailLinkValidity(), 60)));
        input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
        input.put("Email", customerInfo.getCustcomm_value());
        Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
        headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        Result response = new Result();
        try {
            response = HelperMethods.callApi(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
        } catch (HttpCallException e) {
            alert.prepareError("Exception occured while sending email for setting up username and password").log();
        }
        KMSpostProcess(response);

        return result;
    }

    private Result KMSpostProcess(Result response) {
        response = new Result();
        if ("true".equals(HelperMethods.getParamValue(response.getParamByName("KMSemailStatus")))) {
            response.addParam(new Param("status", "Email sent successfully.", MWConstants.STRING));
        } else {
            String errorMsg = HelperMethods.getParamValue(response.getParamByName("KMSuserMsg"))
                    + HelperMethods.getParamValue(response.getParamByName("KMSemailMsg"));
            response.addParam(new Param("status", "Failed to send Email.", MWConstants.STRING));
            alert.prepareError("Exception occured while calling KMS" + errorMsg).log();
        }
        return response;
    }

    public static String encodeToBase64(String sourceString) {
        if (sourceString == null) {
            return null;
        }
        return new String(java.util.Base64.getEncoder().encode(sourceString.getBytes()));
    }
}
