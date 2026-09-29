package com.temenos.dbx.product.contract.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.contract.businessdelegate.api.CoreCustomerBusinessDelegate;
import com.temenos.dbx.product.contract.resource.api.CoreCustomerResource;
import com.temenos.dbx.product.dto.AddressDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.MembershipDTO;

public class CoreCustomerResourceImpl implements CoreCustomerResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    private static final String CORE_CUSTOMER_ID = "coreCustomerId";
    private static final String CUSTOMER_NAME = "customerName";
    private static final String EMAIL = "email";
    private static final String PHONE_COUNTRY_CODE = "phoneCountryCode";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String CUSTOMER_STATUS = "customerStatus";
    private static final String COUNTRY = "country";
    private static final String CITY = "city";
    private static final String ZIP_CODE = "zipCode";
    private static final String CORE_CUSTOMER_IDLIST = "coreCustomerIdList";

    @Override
    public Result searchCoreCustomers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerId =
                inputParams.get(CORE_CUSTOMER_ID);
        String customerName =
                inputParams.get(CUSTOMER_NAME);
        String email =
                inputParams.get(EMAIL);
        String phoneCountryCode =
                inputParams.get(PHONE_COUNTRY_CODE);
        String phoneNumber =
                inputParams.get(PHONE_NUMBER);
        String dateOfBirth =
                inputParams.get(DATE_OF_BIRTH);
        String customerStatus =
                inputParams.get(CUSTOMER_STATUS);
        String country =
                inputParams.get(COUNTRY);
        String city =
                inputParams.get(CITY);
        String zipCode =
                inputParams.get(ZIP_CODE);

        if ((StringUtils.isBlank(coreCustomerId) && StringUtils.isBlank(customerName)
                && StringUtils.isBlank(email) && StringUtils.isBlank(phoneCountryCode)
                && StringUtils.isBlank(phoneNumber) && StringUtils.isBlank(dateOfBirth)
                && StringUtils.isBlank(customerStatus) && StringUtils.isBlank(country)
                && StringUtils.isBlank(city) && StringUtils.isBlank(zipCode))
                || (StringUtils.isNotBlank(phoneNumber) && StringUtils.isBlank(phoneCountryCode))) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10755);
        }

        MembershipDTO membershipDTO = new MembershipDTO();
        AddressDTO addressDTO = new AddressDTO();
        boolean isAddressPresent = false;

        if (StringUtils.isNotBlank(coreCustomerId))
            membershipDTO.setId(coreCustomerId);
        if (StringUtils.isNotBlank(customerName))
            membershipDTO.setName(customerName);
        if (StringUtils.isNotBlank(email))
            membershipDTO.setEmail(email);
        if (StringUtils.isNotBlank(phoneNumber)) {
            StringBuilder phone = new StringBuilder();
            phoneCountryCode = phoneCountryCode.trim();
            phone.append(phoneCountryCode).append("-").append(phoneNumber);
            membershipDTO.setPhone(phone.toString());
        }
        if (StringUtils.isNotBlank(customerStatus))
            membershipDTO.setStatus(customerStatus);

        if (StringUtils.isNotBlank(dateOfBirth))
            membershipDTO.setDateOfBirth(dateOfBirth);

        if (StringUtils.isNotBlank(country)) {
            isAddressPresent = true;
            addressDTO.setCountry(country);
        }
        if (StringUtils.isNotBlank(city)) {
            isAddressPresent = true;
            addressDTO.setCityName(city);
        }
        if (StringUtils.isNotBlank(zipCode)) {
            isAddressPresent = true;
            addressDTO.setZipCode(zipCode);
        }
        if (isAddressPresent)
            membershipDTO.setAddress(addressDTO);

        DBXResult response = new DBXResult();
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        try {
            CoreCustomerBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
            response = businessDelegate.searchCoreCustomers(configurations, membershipDTO, dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                result = ConvertJsonToResult.convert((JsonObject) response.getResponse());
            }
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerResourceImpl class : Exception occured while searching core customers"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerResourceImpl class : Exception occured while searching core customers"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10757);
        }
        return result;
    }

    @Override
    public Result getCoreRelativeCustomers(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);       
        String coreCustomerId =
                inputParams.get(CORE_CUSTOMER_ID);
        if (coreCustomerId == null) {
        	String coreCustomersList =
                    inputParams.get("coreCustomersList");
        	 JsonArray coreCustomerorPartyListArray = null;
        	 JsonParser parser = new JsonParser();
        	 coreCustomerorPartyListArray = parser.parse(coreCustomersList).getAsJsonArray();
        	 for (JsonElement element : coreCustomerorPartyListArray) {
        		 String coreCustomerOrPartyDetails = element.getAsString();
 				String[] corecustomerOrPartyDetailsArray = StringUtils.split(coreCustomerOrPartyDetails, ":");
 				String coreCustomerIdorPartyId = corecustomerOrPartyDetailsArray[0];
 				coreCustomerId = coreCustomerIdorPartyId;
        	 }
        }
        if (StringUtils.isBlank(coreCustomerId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10758);
        }
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setId(coreCustomerId);
        DBXResult response = new DBXResult();
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        try {
            CoreCustomerBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
            response =
                    businessDelegate.getCoreRelativeCustomers(configurations, membershipDTO, dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                result = ConvertJsonToResult.convert((JsonObject) response.getResponse());
            }
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerResourceImpl class : Exception occured while fetching the core customer details"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerResourceImpl class : Exception occured while fetching the core customer details"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10760);
        }
        return result;
    }

    @Override
    public Result getCoreCustomerAccounts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String coreCustomerIdList = inputParams.get(CORE_CUSTOMER_IDLIST);
        if (StringUtils.isBlank(coreCustomerIdList)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10761);
        }
        DBXResult response = new DBXResult();
        try {
            CoreCustomerBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);
            response = businessDelegate.getCoreCustomerAccounts(coreCustomerIdList, dcRequest.getHeaderMap());
            if (response != null && response.getResponse() != null) {
                result = ConvertJsonToResult.convert((JsonObject) response.getResponse());
            }
        } catch (ApplicationException e) {
            alert.prepareError("CoreCustomerResourceImpl class : Exception occured while fetching the core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("CoreCustomerResourceImpl class : Exception occured while fetching the core customer accounts"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10785);
        }
        return result;
    }

    @Override
    public Result checkCoreRelativeCustomerForCompany(String methodID, Object[] inputArray,
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
            List<MembershipDTO> relativeCustomersList = null;
            MembershipDTO companyDetailsDTO = null;
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
            CoreCustomerBusinessDelegate coreCustomerBusinessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CoreCustomerBusinessDelegate.class);

            if ((StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)
                    && StringUtils.isNotBlank(dateOfBirth) && StringUtils.isNotBlank(ssn))) {

                if (StringUtils.isNotBlank(taxId) && StringUtils.isNotBlank(companyName)) {
                    MembershipDTO coreCustomerDTO =
                            coreCustomerBusinessDelegate.getMembershipDetailsByTaxid(taxId, companyName,
                                    dcRequest.getHeaderMap());
                    cif = coreCustomerDTO.getId();
                }
                if (StringUtils.isNotBlank(cif)) {
                    relativeCustomersList =
                            coreCustomerBusinessDelegate.getCoreRelativeCustomers(cif, dcRequest.getHeaderMap());
                    if (null != relativeCustomersList && !relativeCustomersList.isEmpty()) {
                        companyDetailsDTO = coreCustomerBusinessDelegate.getMembershipDetails(cif,
                                dcRequest.getHeaderMap());
                    }
                    verifyDTOListAndUpdateResult(relativeCustomersList, companyDetailsDTO, result, firstName, lastName,
                            ssn,
                            dateOfBirth, dcRequest);

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

    private void verifyDTOListAndUpdateResult(List<MembershipDTO> dtoList, MembershipDTO companyDetailsDTO,
            Result result, String firstName, String lastName, String ssn, String dateOfBirth,
            DataControllerRequest dcRequest) throws ApplicationException {

        CoreCustomerBusinessDelegate coreCustomerBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CoreCustomerBusinessDelegate.class);

        final String RESULT_PARAM_ISCOMPANYEXISTS = "isCompanyExist";

        if (null == dtoList || dtoList.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10231);
        }

        MembershipDTO dto = coreCustomerBD.verifyGivenAuthorizerDetailsAndGetData(firstName, lastName, ssn,
                dateOfBirth, dtoList);
        if (null == dto) {
            ErrorCodeEnum.ERR_10231.setErrorCode(result);
        } else if (StringUtils.isNotBlank(dto.getPhone()) && !HelperMethods.hasError(result)
                && StringUtils.isNotBlank(dto.getEmail())) {
            dcRequest.addRequestParam_("phone", processPhone(dto.getPhone()));
            dcRequest.addRequestParam_("email", dto.getEmail());
            dcRequest.addRequestParam_("Cif", companyDetailsDTO.getId());
            dcRequest.addRequestParam_("companyPhone", processPhone(companyDetailsDTO.getPhone()));
            dcRequest.addRequestParam_("companyEmail", companyDetailsDTO.getEmail());
            dcRequest.addRequestParam_(DBPUtilitiesConstants.BACKENDID, dto.getId());

            boolean status =
                    coreCustomerBD.checkIfTheCoreCustomerIsEnrolled(companyDetailsDTO.getId(),
                            dcRequest.getHeaderMap());

            if (status) {
                result.addParam(new Param("isEnrolled", DBPUtilitiesConstants.BOOLEAN_STRING_TRUE,
                        DBPUtilitiesConstants.STRING_TYPE));
            } else {
                result.addParam(new Param("isEnrolled", DBPUtilitiesConstants.BOOLEAN_STRING_FALSE,
                        DBPUtilitiesConstants.STRING_TYPE));
                result.addParam(new Param("taxId", companyDetailsDTO.getTaxId(),
                        DBPUtilitiesConstants.STRING_TYPE));
                result.addParam(new Param("membershipId", companyDetailsDTO.getId(),
                        DBPUtilitiesConstants.STRING_TYPE));
                result.addParam(new Param("companyName", companyDetailsDTO.getName(),
                        DBPUtilitiesConstants.STRING_TYPE));
                result.addParam(new Param("isBusiness", companyDetailsDTO.getIsBusiness(),
                        DBPUtilitiesConstants.STRING_TYPE));
            }
            result.addParam(new Param(RESULT_PARAM_ISCOMPANYEXISTS, DBPUtilitiesConstants.BOOLEAN_STRING_TRUE,
                    DBPUtilitiesConstants.STRING_TYPE));
        } else {
            ErrorCodeEnum.ERR_10233.setErrorCode(result);
        }

    }

    private String processPhone(String phone) {
        if (StringUtils.isNotBlank(phone)) {
            phone = phone.replaceAll("[()\\s]", "");
            if (phone.contains("-") && !phone.contains("+")) {
                phone = "+".concat(phone);
            }
        }
        return phone;

    }

    @Override
    public Result getCompanyAccounts(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        final String INPUT_SERVICEKEY = "serviceKey";
        final String INPUT_MASTER_SERVICE_KEY = "masterServiceKey";
        JsonObject serviceKeyPayload = null;
        JsonObject masterServiceKeyPayload = null;

        try {
            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);

            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUT_SERVICEKEY))
                    ? inputParams.get(INPUT_SERVICEKEY)
                    : dcRequest.getAttribute(INPUT_SERVICEKEY);
            String masterServiceKey = StringUtils.isNotBlank(inputParams.get(INPUT_MASTER_SERVICE_KEY))
                    ? inputParams.get(INPUT_MASTER_SERVICE_KEY)
                    : dcRequest.getAttribute(INPUT_MASTER_SERVICE_KEY);
            if (StringUtils.isBlank(serviceKey)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10257);
            }

            serviceKeyPayload = getPayload(serviceKey, dcRequest);
            if (StringUtils.isNotBlank(masterServiceKey)) {
                masterServiceKeyPayload = getPayload(masterServiceKey, dcRequest);
            }
            JSONObject resultObject = accountsBusinessDelegate.getCustomerCentricAccounts(serviceKeyPayload,
                    masterServiceKeyPayload, serviceKey, masterServiceKey, dcRequest.getHeaderMap());
            result = JSONToResult.convert(resultObject.toString());

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10260);
        }
        return result;
    }

    private JsonObject getPayload(String masterServiceKey, DataControllerRequest dcRequest)
            throws ApplicationException {
        MFAServiceDTO dto = new MFAServiceDTO();
        dto.setIsVerified("true");
        dto.setServiceKey(masterServiceKey);
        MFAServiceBusinessDelegate bd = DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
        List<MFAServiceDTO> list = bd.getMfaService(dto, new HashMap<>(), dcRequest.getHeaderMap());
        if (!list.isEmpty()) {
            dto = list.get(0);
        }
        MFAServiceUtil util = new MFAServiceUtil(dto);
        validateServiceKey(util, dcRequest);
        return util.getRequestPayload();
    }

    private void validateServiceKey(MFAServiceUtil util, DataControllerRequest dcRequest) throws ApplicationException {
        if (!util.isStateVerified()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10326);
        }
        int otpValidTimeInMinutes = util.getServiceKeyExpiretime(dcRequest);
        if (!util.isValidServiceKey(otpValidTimeInMinutes)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10327);
        }
    }
}
