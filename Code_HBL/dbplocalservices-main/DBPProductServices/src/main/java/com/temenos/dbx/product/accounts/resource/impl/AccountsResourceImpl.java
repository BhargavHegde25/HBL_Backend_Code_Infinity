package com.temenos.dbx.product.accounts.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.utils.MFAServiceUtil;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.accounts.resource.api.AccountsResource;
import com.temenos.dbx.product.dto.AccountTypeDTO;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerAccountsBusinessDelegate;

/**
 * 
 * @author sowmya.vandanapu
 *
 */
public class AccountsResourceImpl implements AccountsResource {

    final String RESULT_DATASET_NAME = "Accounts";
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    public Result getAccountInformation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {

        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        HelperMethods.removeNullValues(inputParams);
        if (StringUtils.isNotBlank(dcRequest.getParameter("canProceed"))
                && "false".equalsIgnoreCase(dcRequest.getParameter("canProceed"))) {
            return result;
        }

        /**
         * Input Validation varies according to the implementation
         */
        AllAccountsViewDTO inputDTO = null;
        AccountsBusinessDelegate accountsBusinessDelegate = null;
        try {
            accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            inputDTO = accountsBusinessDelegate.validateGetAccountInformationInput(inputParams);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10237);
        }

        /**
         * Fetch the account information
         */

        List<AllAccountsViewDTO> allAccountsViewDTO = new ArrayList<>();

        try {
            allAccountsViewDTO = accountsBusinessDelegate.getAccountInformation(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10247);
        }
        if (allAccountsViewDTO == null || allAccountsViewDTO.isEmpty()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10247);
        }

        /**
         * Validate account information
         */

        AllAccountsViewDTO primaryAccountHolder = new AllAccountsViewDTO();
        try {
            primaryAccountHolder =
                    accountsBusinessDelegate.validateGetAccountInformationOutput(allAccountsViewDTO, inputParams);
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10247);
        }
        if (primaryAccountHolder == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10272);
        }

        JsonObject mndatoryParams = new JsonObject();
        try {
            mndatoryParams = accountsBusinessDelegate.getAccountInformationServiceKeyParameters(primaryAccountHolder);
        } catch (Exception e) {
            dcRequest.addRequestParam_("canProceed", "false");
            alert.prepareError("Exception occured while mapping the mandatory parameters").log();
        }
        setRequestParams(mndatoryParams, dcRequest);
        result.addStringParam("isAccountExists", "true");
        return result;
    }

    /**
     * 
     * @param accountsInformation
     * @param dcRequest
     */

    private void setRequestParams(JsonObject mandatoryParams, DataControllerRequest dcRequest) {
        dcRequest.addRequestParam_("phone",
                processPhone(new JsonParser().parse(dcRequest.getParameter("customerInformation")).getAsJsonArray()
                        .get(0).getAsJsonObject().get("phone").getAsString()));
        dcRequest.addRequestParam_("email", new JsonParser().parse(dcRequest.getParameter("customerInformation"))
                .getAsJsonArray().get(0).getAsJsonObject().get("email").getAsString());

        dcRequest.addRequestParam_("companyInformation",
                new JsonParser().parse(dcRequest.getParameter("companyInformation")).getAsJsonArray().get(0)
                        .getAsJsonObject().toString());
        dcRequest.addRequestParam_("customerInformation",
                new JsonParser().parse(dcRequest.getParameter("customerInformation")).getAsJsonArray().get(0)
                        .getAsJsonObject().toString());

        Set<Entry<String, JsonElement>> entrySet = mandatoryParams.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            if (mandatoryParams.get(entry.getKey()).isJsonPrimitive())
                dcRequest.addRequestParam_(entry.getKey(), mandatoryParams.get(entry.getKey()).getAsString());
            else
                dcRequest.addRequestParam_(entry.getKey(), mandatoryParams.get(entry.getKey()).toString());
        }

    }

    private String processPhone(String phone) {
        if (!StringUtils.contains(phone, "-") && !StringUtils.contains(phone, "+")) {
            phone = "+91-" + phone;
        }
        return phone;
    }

    @Override
    public Result checkAccountIfAvailable(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();

        if (StringUtils.isNotBlank("canProceed") && "false".equalsIgnoreCase(dcRequest.getParameter("canProceed"))) {
            return result;
        }

        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        HelperMethods.removeNullValues(inputParams);

        if (!inputParams.containsKey("accountId")) {
            ErrorCodeEnum.ERR_10241.setErrorCode(result);
            result.addStringParam("isAccountAvailable", "false");
            dcRequest.addRequestParam_("canProceed", "false");
            return result;
        }
        AccountsDTO inputDTO = new AccountsDTO();
        inputDTO.setAccountId(inputParams.get("accountId"));
        inputDTO.setAccountName(inputParams.get("accountName"));

        boolean isAccountAvailable = Boolean.FALSE;
        try {
            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            isAccountAvailable = accountsBusinessDelegate.checkAccountIfAvailable(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            dcRequest.addRequestParam_("canProceed", "false");
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            dcRequest.addRequestParam_("canProceed", "false");
            throw new ApplicationException(ErrorCodeEnum.ERR_10240);
        }

        if (Boolean.FALSE.equals(isAccountAvailable)) {
            dcRequest.addRequestParam_("canProceed", "false");
        }

        result.addStringParam("isAccountAvailable", String.valueOf(isAccountAvailable));

        return result;
    }

    @Override
    public Result getAccountInformationBasedOnServicekey(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();

        Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
        HelperMethods.removeNullValues(inputParams);

        String masterServiceKey = inputParams.containsKey("masterServiceKey")
                ? inputParams.get("masterServiceKey").toString()
                : dcRequest.getParameter("masterServiceKey");

        String serviceKey = inputParams.containsKey("masterServiceKey") ? inputParams.get("serviceKey").toString()
                : dcRequest.getParameter("serviceKey");

        if (StringUtils.isBlank(masterServiceKey) && StringUtils.isBlank(serviceKey)) {
            return result;
        }

        dcRequest.addRequestParam_("canProceed", "false");
        Map<String, String> serviceKeyPayloadMap = null;
        try {
            serviceKeyPayloadMap = validateServiceKeyInformationAndGetPayload(inputParams, masterServiceKey, serviceKey,
                    dcRequest);
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            return result;
        }
        if (serviceKeyPayloadMap == null || serviceKeyPayloadMap.isEmpty()
                || !serviceKeyPayloadMap.containsKey("serviceKey")) {
            dcRequest.addRequestParam_("canProceed", "false");
            ErrorCodeEnum.ERR_10241.setErrorCode(result);
            return result;
        }
        if (StringUtils.isNotBlank(masterServiceKey) && !serviceKeyPayloadMap.containsKey("serviceKey")) {
            dcRequest.addRequestParam_("canProceed", "false");
            ErrorCodeEnum.ERR_10241.setErrorCode(result);
            return result;
        }

        result = getAccountInformationFromServiceKeyAndUpdateMasterKey(serviceKey, masterServiceKey,
                serviceKeyPayloadMap, dcRequest);
        return result;
    }

    private Result getAccountInformationFromServiceKeyAndUpdateMasterKey(String serviceKey, String masterServiceKey,
            Map<String, String> payloadMap, DataControllerRequest dcRequest) {

        Result result = new Result();
        try {
            String serviceKeyPayload = null;
            serviceKeyPayload = CryptoText.decrypt(payloadMap.get("serviceKey"));
            JsonObject accountInfoJsonArray = new JsonParser()
                    .parse(new JsonParser().parse(serviceKeyPayload).getAsJsonObject().get("requestPayload")
                            .getAsJsonObject().get(DBPUtilitiesConstants.ACCOUNTS).getAsString())
                    .getAsJsonObject();
            result = ConvertJsonToResult.convert(accountInfoJsonArray.toString());

            JsonObject masterServiceKeyJson = new JsonObject();
            JsonArray accountListJsonArray = new JsonArray();
            JsonObject accountsJson = new JsonObject();
            accountsJson.addProperty("accountId", accountInfoJsonArray.get(DBPUtilitiesConstants.ACCOUNTS)
                    .getAsJsonArray().get(0).getAsJsonObject().get("accountId").getAsString());
            accountsJson.add("companyInformation",
                    new JsonParser().parse(new JsonParser().parse(serviceKeyPayload).getAsJsonObject()
                            .get("requestPayload").getAsJsonObject().get("companyInformation").getAsString())
                            .getAsJsonObject());
            accountsJson.add("customerInformation",
                    new JsonParser().parse(new JsonParser().parse(serviceKeyPayload).getAsJsonObject()
                            .get("requestPayload").getAsJsonObject().get("customerInformation").getAsString())
                            .getAsJsonObject());
            if (StringUtils.isNotBlank(payloadMap.get("masterServiceKey"))) {
                String masterServiceKeyPayload = payloadMap.get("masterServiceKey");
                masterServiceKeyPayload = CryptoText.decrypt(masterServiceKeyPayload);
                masterServiceKeyJson = new JsonParser().parse(masterServiceKeyPayload).getAsJsonObject();
                accountListJsonArray = masterServiceKeyJson.get("accounts").getAsJsonArray();
                serviceKey = masterServiceKey;
            }
            accountListJsonArray.add(accountsJson);
            masterServiceKeyJson.add("accounts", accountListJsonArray);
            masterServiceKeyJson.add("communication",
                    new JsonParser().parse(serviceKeyPayload).getAsJsonObject().get("communication").getAsJsonObject());
            masterServiceKeyJson.add(DBPUtilitiesConstants.BACKENDID,
                    new JsonParser().parse(serviceKeyPayload).getAsJsonObject()
                            .get("requestPayload").getAsJsonObject().get(DBPUtilitiesConstants.BACKENDID));
            String payload = null;
            payload = CryptoText.encrypt(masterServiceKeyJson.toString());
            MFAServiceDTO inputDTO = new MFAServiceDTO();
            inputDTO.setServiceKey(serviceKey);
            inputDTO.setPayload(payload);
            MFAServiceBusinessDelegate mfaservicebusinessdelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
            mfaservicebusinessdelegate.updateMfaService(inputDTO, dcRequest.getHeaderMap());
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing the accounts information").log();
        }
        return result;
    }

    public Map<String, String> validateServiceKeyInformationAndGetPayload(Map<String, Object> inputParams,
            String masterServiceKey, String serviceKey, DataControllerRequest dcRequest) throws ApplicationException {

        MFAServiceDTO inputDTO = new MFAServiceDTO();
        inputDTO.setServiceKey(serviceKey);
        inputDTO.setServiceName(MFAConstants.SERVICE_ID_PRELOGIN);

        List<MFAServiceDTO> mfaserviceDTO = new ArrayList<>();
        try {
            MFAServiceBusinessDelegate mfaservicebusinessdelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
            mfaserviceDTO = mfaservicebusinessdelegate.getMfaService(inputDTO, inputParams, dcRequest.getHeaderMap());

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10242);
        }
        if (mfaserviceDTO == null) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10243);
        }
        MFAServiceUtil mfaserviceutil = new MFAServiceUtil();
        Integer serviceKeyExpriryTime = mfaserviceutil.getServiceKeyExpiretime(dcRequest);

        Map<String, String> payloadMap = new HashMap<>();
        for (MFAServiceDTO dto : mfaserviceDTO) {
            mfaserviceutil = new MFAServiceUtil(dto);
            if (mfaserviceutil.isStateVerified() && mfaserviceutil.isValidServiceKey(serviceKeyExpriryTime)) {
                if (dto.getServiceKey().equals(serviceKey))
                    payloadMap.put("serviceKey", dto.getPayload());
                if (StringUtils.isNotBlank("masterServiceKey") && dto.getServiceKey().equals(masterServiceKey))
                    payloadMap.put("masterServiceKey", dto.getPayload());
            }
        }
        return payloadMap;
    }

    @Override
    public Result getAccountTypes(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        List<AccountTypeDTO> accountTypesDTOList = new ArrayList<>();

        try {
            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            accountTypesDTOList = accountsBusinessDelegate.getAccountTypes(dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        }
        try {
            String accountTypes = JSONUtils.stringifyCollectionWithTypeInfo(accountTypesDTOList, AccountTypeDTO.class);
            JsonObject jsonobject = new JsonObject();
            jsonobject.add("records", new JsonParser().parse(accountTypes).getAsJsonArray());
            result = ConvertJsonToResult.convert(jsonobject);
        } catch (Exception e) {
            alert.prepareError("Failed to parse the account types information" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10254);
        }
        return result;
    }

    @Override
    public Result getAllAccounts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_MEMBERSHIPID = "Membership_id";
        final String INPUT_ACCOUNTID = "Account_id";
        final String INPUT_TAXID = "Taxid";
        Result result = new Result();
        AllAccountsViewDTO dto = new AllAccountsViewDTO();

        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String membershipId = StringUtils.isNotBlank(inputParams.get(INPUT_MEMBERSHIPID))
                    ? inputParams.get(INPUT_MEMBERSHIPID)
                    : dcRequest.getParameter(INPUT_MEMBERSHIPID);
            String accountId = StringUtils.isNotBlank(inputParams.get(INPUT_ACCOUNTID))
                    ? inputParams.get(INPUT_ACCOUNTID)
                    : dcRequest.getParameter(INPUT_ACCOUNTID);
            String taxId = StringUtils.isNotBlank(inputParams.get(INPUT_TAXID)) ? inputParams.get(INPUT_TAXID)
                    : dcRequest.getParameter(INPUT_TAXID);
            if (StringUtils.isBlank(membershipId) && StringUtils.isBlank(accountId) && StringUtils.isBlank(taxId)) {
                ErrorCodeEnum.ERR_11024.setErrorCode(result);
            }
            if (StringUtils.isNotBlank(membershipId)) {
                dto.setMembershipId(membershipId);
            } else if (StringUtils.isNotBlank(accountId)) {
                dto.setAccountId(accountId);
            } else {
                dto.setTaxId(taxId);
            }

            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            List<AllAccountsViewDTO> dtoList = accountsBusinessDelegate.getAllAccountsInformation(dto,
                    dcRequest.getHeaderMap());
            if (null != dtoList) {
                dtoList = accountsBusinessDelegate.getUnUsedAccountsFromArray(dtoList, dcRequest.getHeaderMap());
                String membershipAccountsString = JSONUtils.stringifyCollectionWithTypeInfo(dtoList,
                        AllAccountsViewDTO.class);
                JSONArray accountsArray = new JSONArray(membershipAccountsString);
                JSONObject accountsJson = new JSONObject();
                accountsJson.put(RESULT_DATASET_NAME, accountsArray);
                result = ConvertJsonToResult.convert(accountsJson.toString());
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10236);
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }
        return result;
    }

    @Override
    public Result getCustomerCentricAccountsInformationByServiceKey(String methodID, Object[] inputArray,
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

    private void validateServiceKey(MFAServiceUtil util, DataControllerRequest dcRequest) throws ApplicationException {
        if (!util.isStateVerified()) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10326);
        }
        int otpValidTimeInMinutes = util.getServiceKeyExpiretime(dcRequest);
        if (!util.isValidServiceKey(otpValidTimeInMinutes)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10327);
        }
    }

    private JsonObject getPayload(String masterServiceKey, DataControllerRequest dcRequest)
            throws ApplicationException {
        MFAServiceDTO dto = new MFAServiceDTO();
        dto.setIsVerified("true");
        dto.setServiceKey(masterServiceKey);
        MFAServiceBusinessDelegate bd = DBPAPIAbstractFactoryImpl.getBusinessDelegate(MFAServiceBusinessDelegate.class);
        List<MFAServiceDTO> list = bd.getMfaService(dto, new HashMap<>(), dcRequest.getHeaderMap());
        if (list.size() > 0) {
            dto = list.get(0);
        }
        MFAServiceUtil util = new MFAServiceUtil(dto);
        validateServiceKey(util, dcRequest);
        return util.getRequestPayload();
    }

    @Override
    public Result getAccountInformationForAdmin(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = new HashMap<>();
        HelperMethods.removeNullValues(inputParams);

        String accountId = inputParams.containsKey("accountId") ? inputParams.get("accountId")
                : dcRequest.getParameter("accountId");

        if (StringUtils.isBlank(accountId)) {
            ErrorCodeEnum.ERR_10266.setErrorCode(result);
            return result;
        }

        AllAccountsViewDTO inputDTO = new AllAccountsViewDTO();
        inputDTO.setAccountId(accountId);
        List<AllAccountsViewDTO> allAccountsViewDTO = new ArrayList<>();
        try {
            AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            allAccountsViewDTO = accountsBusinessDelegate.getAccountInformation(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        }
        try {
            String accountsInformation = JSONUtils.stringifyCollectionWithTypeInfo(allAccountsViewDTO,
                    AllAccountsViewDTO.class);
            JsonObject jsonobject = new JsonObject();
            jsonobject.add(DBPUtilitiesConstants.ACCOUNTS,
                    new JsonParser().parse(accountsInformation).getAsJsonArray());
            result = ConvertJsonToResult.convert(jsonobject);
            result.addStringParam("isAccountExists", "true");
            JsonArray accountInformationJsonArray = new JsonParser().parse(accountsInformation).getAsJsonArray();
            if (accountInformationJsonArray.get(0).getAsJsonObject().get("accountId") != null) {
                dcRequest.addRequestParam_("accountId",
                        accountInformationJsonArray.get(0).getAsJsonObject().get("accountId").getAsString());
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10267);
        }
        return result;
    }

    @Override
    public Result getOrganizationAccountsSignatoriesInformation(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        final String ACCOUNT_SIGNATORIES = "AccountSignatories";
        Result result = new Result();
        Map<String, String> userDetails = HelperMethods.getUserFromIdentityService(dcRequest);
        if (HelperMethods.isAuthenticationCheckRequiredForService(userDetails)) {
            ErrorCodeEnum.ERR_10308.setErrorCode(result);
            return result;
        }

        Dataset ds = new Dataset();
        ds.setId(ACCOUNT_SIGNATORIES);
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        String orgId = StringUtils.isBlank(inputParams.get("organizationId")) ? inputParams.get("organizationId")
                : dcRequest.getParameter("organizationId");

        if (StringUtils.isBlank(orgId)) {
            ErrorCodeEnum.ERR_10306.setErrorCode(result);
            return result;
        }
        AccountsDTO inputDTO = new AccountsDTO();
        inputDTO.setOrganizationId(orgId);
        List<AccountsDTO> orgAccounts = new ArrayList<>();

        try {
            AccountsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            orgAccounts = businessDelegate.getOrganizationAccounts(inputDTO, dcRequest.getHeaderMap());
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10305);
        }
        if (orgAccounts.isEmpty()) {
            result.addDataset(ds);
            return result;
        }
        String accountHoldersInfo = new String();
        try {
            AccountsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountsBusinessDelegate.class);
            orgAccounts = businessDelegate.getOrganizationAccountHolderDetails(orgAccounts, dcRequest.getHeaderMap());
            accountHoldersInfo = JSONUtils.stringifyCollectionWithTypeInfo(orgAccounts, AccountsDTO.class);
        } catch (ApplicationException e) {
            result.addDataset(ds);
            return result;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10307);
        }

        JsonObject jsonResult = new JsonObject();
        jsonResult.add(ACCOUNT_SIGNATORIES, new JsonParser().parse(accountHoldersInfo));
        result = ConvertJsonToResult.convert(jsonResult);
        return result;
    }

    @Override
    public Result getCustomerAssociatedToAccounts(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String accountId = StringUtils.isNotBlank(inputParams.get("accountId")) ? inputParams.get("accountId")
                : dcRequest.getParameter("accountId");
        String legalEntityId = StringUtils.isNotBlank(inputParams.get("legalEntityId")) ?
        		inputParams.get("legalEntityId") : dcRequest.getParameter("legalEntityId");
        if (StringUtils.isBlank(accountId)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10771);
        }
        if (StringUtils.isBlank(legalEntityId)) {
        	legalEntityId = LegalEntityUtil
        			.getCurrentLegalEntityIdFromCache(dcRequest);
        }
        CustomerAccountsDTO dto = new CustomerAccountsDTO();
        dto.setAccountId(accountId);
        if(StringUtils.isNotBlank(legalEntityId)) {
        	dto.setCompanyLegalUnit(legalEntityId);
        }
        try {
            CustomerAccountsBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerAccountsBusinessDelegate.class);
            List<CustomerAccountsDTO> dtoList = businessDelegate.getCustomerAccounts(dto, dcRequest.getHeaderMap());
            if (dtoList != null && dtoList.size() > 0) {
                String json = JSONUtils.stringifyCollectionWithTypeInfo(dtoList, CustomerAccountsDTO.class);
                JsonObject response = new JsonObject();
                response.add(DBPDatasetConstants.DATASET_CUSTOMERS, new JsonParser().parse(json).getAsJsonArray());
                result = ConvertJsonToResult.convert(response);
            }
        } catch (ApplicationException e) {
            alert.prepareError("AccountsResourceImpl : Exception occured while fetching customers associated to contract"
                    + e.getMessage()).log();
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("AccountsResourceImpl : Exception occured while fetching customers associated to contract"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10771);
        }
        return result;
    }

	@Override
    public Result checkIfAccountPermissionEnabled(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        
        Result result = new Result();
        String accNum = StringUtils.EMPTY;
        String userId = StringUtils.EMPTY;

 

        try {
            
            userId = dcRequest.getServicesManager().getIdentityHandler().getUserId();
            
            if(StringUtils.isBlank(userId)) {
                ErrorCodeEnum.ERR_10152.setErrorCode(result);
                return result;
            }
            
            accNum = dcRequest.getParameter("accountNumber");
            if(StringUtils.isBlank(accNum)) {
                ErrorCodeEnum.ERR_29010.setErrorCode(result);
                return result;
            }
            
            String permissions = StringUtils.EMPTY;
            Set<String> permissionSet = null;
            try {
                
                permissions = dcRequest.getParameter("permissions");
                if(StringUtils.isBlank( permissions )) {
                    
                    alert.prepareError("Missing input param permissions : "+permissions).log();
                    ErrorCodeEnum.ERR_29011.setErrorCode(result);
                    return result;
                }

 

                permissionSet = new HashSet<String>();
                JsonArray permissionsJsonArray = new JsonParser().parse(permissions).getAsJsonArray();
                if(null== permissionsJsonArray || permissionsJsonArray.size() == 0) {
                    alert.prepareError("Empty permissions array : "+permissions).log();
                    ErrorCodeEnum.ERR_29011.setErrorCode(result);
                    return result;
                }
                
                
                for(JsonElement element : permissionsJsonArray ) {
                    permissionSet.add(element.getAsString());
                }
                
                
            } catch (Exception exp) {
                alert.prepareError("Exception occured while reading input param permissions array: "+permissions).log();
                ErrorCodeEnum.ERR_29011.setErrorCode(result);
                return result;
            }
            
            CustomerAccountsBusinessDelegate businessDelegate =
                    DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerAccountsBusinessDelegate.class);

 

            boolean isPermissionEnabled = businessDelegate.checkIfAccountPermissionEnabled(userId, accNum, permissionSet, dcRequest.getHeaderMap());
            
            result.addStringParam("isPermissionEnabled", String.valueOf(isPermissionEnabled));
            
        }catch(Exception exp) {
            alert.prepareError("Exception occured while checking permission details "+exp).log();
            ErrorCodeEnum.ERR_29009.setErrorCode(result);
        }
        
        return result;
    }
}