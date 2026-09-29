package com.temenos.dbx.product.accounts.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.IntegrationTemplateURLFinder;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.OperationName;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.ServiceId;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.accounts.backenddelegate.api.AccountHolderBackendDelegate;
import com.temenos.dbx.product.dto.AccountsDTO;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.MembershipOwnerDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class AccountHolderBackendDelegateImpl implements AccountHolderBackendDelegate {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<AllAccountsViewDTO> getAccountInformation(AllAccountsViewDTO accountDTO, Map<String, Object> headersMap)
            throws ApplicationException {
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                return getAccountInformationFromCore(accountDTO, headersMap);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the account and account holder information from the core"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10736);
        }

        List<AllAccountsViewDTO> allAccountsViewDTO = new ArrayList<>();

        Map<String, Object> inputParams = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isNotBlank(accountDTO.getAccountId())) {
            sb.append("Account_id" + DBPUtilitiesConstants.EQUAL + accountDTO.getAccountId());
        }
        inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.ALLACCOUNTSVIEW_GET);
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the accounts information from backend delegate :"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0 || !response.has("allaccountsview")) {
            alert.prepareError("Exception occured while fetching the account information").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }

        if (response.get("allaccountsview").getAsJsonArray().size() == 0) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10236);
        }

        try {
            for (JsonElement accountsJson : response.get("allaccountsview").getAsJsonArray()) {
                if (accountsJson != null) {
                    allAccountsViewDTO.add((AllAccountsViewDTO) DTOUtils
                            .loadJsonObjectIntoObject(accountsJson.getAsJsonObject(), AllAccountsViewDTO.class, true));
                }
            }
            for (AllAccountsViewDTO dto : allAccountsViewDTO) {
                List<MembershipOwnerDTO> membershipownerdto = getMembershipOwnerDetails(dto.getMembershipId(),
                        headersMap);
                dto.setMembershipownerDTO(membershipownerdto);
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while parsing the account information" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10245);
        }
        return allAccountsViewDTO;
    }

    private List<AllAccountsViewDTO> getAccountInformationFromCore(AllAccountsViewDTO accountDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        List<AllAccountsViewDTO> responseDTO = new ArrayList<>();
        try {

            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put(DBPUtilitiesConstants.ACCOUNT_ID, accountDTO.getAccountId());
            headersMap.put("companyId",
                    EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISACCOUNTS_INTEGRATION_SERVICE,
                    null, OperationName.GET_ACCOUNT_INFORMATION,
                    inputParams, headersMap);

            AllAccountsViewDTO childDTO = new AllAccountsViewDTO();

            if (JSONUtil.hasKey(response, DBPUtilitiesConstants.ACCOUNTS)
                    && JSONUtil.hasKey(response, DBPUtilitiesConstants.ACCOUNT_HOLDER_DATASET)) {
                if ((JSONUtil.hasKey(response, DBPConstants.DBP_ERROR_CODE_KEY)
                        && StringUtils.isNotBlank(JSONUtil.getString(response, DBPConstants.DBP_ERROR_CODE_KEY))) ||
                        JSONUtil.hasKey(response, DBPConstants.DBP_ERROR_MESSAGE_KEY)
                                && StringUtils
                                        .isNotBlank(JSONUtil.getString(response, DBPConstants.DBP_ERROR_MESSAGE_KEY))) {
                    alert.prepareError("Exception occured while calling the core" + response).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10736);
                }

                JsonArray accountHolders = response.get(DBPUtilitiesConstants.ACCOUNT_HOLDER_DATASET).getAsJsonArray();
                List<MembershipOwnerDTO> accountHoldersDTOList = new ArrayList<>();
                MembershipOwnerDTO primaryAccountHolderDTO = null;
                String customerName = "";
                for (JsonElement json : accountHolders) {
                    JsonObject jsonObject = json.getAsJsonObject();
                    if (DBPUtilitiesConstants.MEMBERTYPE_PRIMARY
                            .equalsIgnoreCase(JSONUtil.getString(jsonObject, "customerType"))) {
                        primaryAccountHolderDTO = new MembershipOwnerDTO();
                        primaryAccountHolderDTO.setId(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_ID));
                        primaryAccountHolderDTO.setSsn(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_SSN));
                        primaryAccountHolderDTO
                                .setLastName(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_LASTNAME));
                        primaryAccountHolderDTO
                                .setFirstName(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_FIRSTNAME));
                        primaryAccountHolderDTO
                                .setPhone((JSONUtil.getString(jsonObject, DBPUtilitiesConstants.PHONE_NUMBER))
                                        .replaceAll("[()\\s-]", ""));
                        primaryAccountHolderDTO.setEmail(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.EMAIL));
                        primaryAccountHolderDTO
                                .setDateOfBirth(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.DOB));
                        primaryAccountHolderDTO
                                .setMiddleName(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_MIDDLENAME));
                        primaryAccountHolderDTO.setMemberType(JSONUtil.getString(jsonObject, "customerType"));
                        primaryAccountHolderDTO.setMemberTypeId(JSONUtil.getString(jsonObject, "customerRole"));
                        primaryAccountHolderDTO.setMemberTypeName(JSONUtil.getString(jsonObject, "roleDisplayName"));
                        customerName =
                                primaryAccountHolderDTO.getFirstName() + " " + primaryAccountHolderDTO.getLastName();
                    }
                    if (primaryAccountHolderDTO != null)
                        break;
                }
                accountHoldersDTOList.add(primaryAccountHolderDTO);

                JsonObject account =
                        response.get(DBPUtilitiesConstants.ACCOUNTS).getAsJsonArray().get(0).getAsJsonObject();
                Map<String, String> accountTypes = HelperMethods.getAccountsTypes();
                childDTO.setAccountId(JSONUtil.getString(account, DBPUtilitiesConstants.ACCOUNT_ID));
                childDTO.setAccountName(
                        getAccountTypeName(JSONUtil.getString(account, DBPUtilitiesConstants.PRODUCT_ID)));
                childDTO.setAccountType(childDTO.getAccountName());
                childDTO.setTypeId(accountTypes.get(childDTO.getAccountName()));
                childDTO.setArrangementId(JSONUtil.getString(account, DBPUtilitiesConstants.ARRANGEMENT_ID));
                JsonObject accountholder = new JsonObject();
                accountholder.addProperty("username", customerName);
                accountholder.addProperty("fullname", customerName);
                childDTO.setAccountHolderName(accountholder.toString());
                childDTO.setMembershipownerDTO(accountHoldersDTOList);
                responseDTO.add(childDTO);
            }
            alert.prepareError("Response from CORE Integration from konyDBXTempltes jar" + response).log();
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the response from core" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10736);
        }
        return responseDTO;
    }

    private String getAccountTypeName(String productId) {

        String accountTypePropertyFilename =
                IntegrationTemplateURLFinder.getBackendURL(DBPUtilitiesConstants.ACCOUNTTYPE_PROPERTIES_FILE_NAME);
        return URLFinder.getPropertyValue(productId,
                accountTypePropertyFilename);
    }

    public List<MembershipOwnerDTO> getMembershipOwnerDetails(String membershipId, Map<String, Object> headersMap) {

        List<MembershipOwnerDTO> membershipownerdto = new ArrayList<>();

        Map<String, Object> inputParams = new HashMap<>();

        inputParams.put(DBPUtilitiesConstants.FILTER, "membershipId" + DBPUtilitiesConstants.EQUAL + membershipId);

        JsonObject response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.MEMBERSHIPOWNER_GET);
            membershipownerdto = JSONUtils.parseAsList(response.get("membershipowner").getAsJsonArray().toString(),
                    MembershipOwnerDTO.class);
        } catch (IOException e) {
            alert.prepareError("Exception occured while fetching accountid owner details").log();
        }
        return membershipownerdto;
    }

    @Override
    public List<MembershipOwnerDTO> getOrganizationAccountHolderDetails(AccountsDTO inputDTO,
            Map<String, Object> headersMap)
            throws ApplicationException {
        final String IS_Integrated = Boolean.toString(IntegrationTemplateURLFinder.isIntegrated);
        try {
            if (StringUtils.isNotBlank(IS_Integrated) && IS_Integrated.equalsIgnoreCase("true")) {
                return getOrganizationAccountHolderInformationFromCore(inputDTO, headersMap);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the account and account holder information from the core"
                    + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10737);
        }
        List<MembershipOwnerDTO> membershipownerdto = new ArrayList<>();

        Map<String, Object> inputParams = new HashMap<>();

        inputParams.put(DBPUtilitiesConstants.FILTER,
                "accountId" + DBPUtilitiesConstants.EQUAL + inputDTO.getAccountId());

        JsonObject response = new JsonObject();

        response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                URLConstants.MEMBERSHIP_ACCOUNTS_GET);

        if (!response.has("opstatus") || response.get("opstatus").getAsInt() != 0
                || !response.has("membershipaccounts")) {
            alert.prepareError("Exception occured while fetching the accounts from  in membershipaccounts").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10307);
        }

        inputParams.clear();

        inputParams.put(DBPUtilitiesConstants.FILTER, "membershipId" + DBPUtilitiesConstants.EQUAL + response
                .get("membershipaccounts").getAsJsonArray().get(0).getAsJsonObject().get("membershipId").getAsString());

        response = new JsonObject();

        try {
            response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.MEMBERSHIPOWNER_GET);
            membershipownerdto = JSONUtils.parseAsList(response.get("membershipowner").getAsJsonArray().toString(),
                    MembershipOwnerDTO.class);
        } catch (IOException e) {
            alert.prepareError("Exception occured while fetching accountid owner details" + response).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10307);
        }
        return membershipownerdto;
    }

    private List<MembershipOwnerDTO> getOrganizationAccountHolderInformationFromCore(AccountsDTO accountDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        List<MembershipOwnerDTO> responseDTO = new ArrayList<>();

        try {
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put(DBPUtilitiesConstants.ACCOUNT_ID, accountDTO.getAccountId());
            headersMap.put("companyId",
                    EnvironmentConfigurationsHandler.getValue(DBPUtilitiesConstants.BRANCH_ID_REFERENCE));
            JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(ServiceId.T24ISACCOUNTS_INTEGRATION_SERVICE,
                    null, OperationName.GET_ACCOUNT_INFORMATION,
                    inputParams, headersMap);

            if (JSONUtil.hasKey(response, DBPUtilitiesConstants.ACCOUNT_HOLDER_DATASET)) {
                if ((JSONUtil.hasKey(response, DBPConstants.DBP_ERROR_CODE_KEY)
                        && StringUtils.isNotBlank(JSONUtil.getString(response, DBPConstants.DBP_ERROR_CODE_KEY))) ||
                        JSONUtil.hasKey(response, DBPConstants.DBP_ERROR_MESSAGE_KEY)
                                && StringUtils
                                        .isNotBlank(JSONUtil.getString(response, DBPConstants.DBP_ERROR_MESSAGE_KEY))) {
                    alert.prepareError("Exception occured while calling the core" + response).log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_10737);
                }
                JsonArray accountholders =
                        response.get(DBPUtilitiesConstants.ACCOUNT_HOLDER_DATASET).getAsJsonArray();
                for (JsonElement json : accountholders) {
                    MembershipOwnerDTO dto = new MembershipOwnerDTO();
                    JsonObject jsonObject = json.getAsJsonObject();
                    dto.setId(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_ID));
                    dto.setSsn(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_SSN));
                    dto.setLastName(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_LASTNAME));
                    dto.setFirstName(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_FIRSTNAME));
                    dto.setPhone((JSONUtil.getString(jsonObject, DBPUtilitiesConstants.PHONE_NUMBER))
                            .replaceAll("[()\\s-]", ""));
                    dto.setEmail(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.EMAIL));
                    dto.setDateOfBirth(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.DOB));
                    dto.setMiddleName(JSONUtil.getString(jsonObject, DBPUtilitiesConstants.C_MIDDLENAME));
                    dto.setMemberType(JSONUtil.getString(jsonObject, "customerType"));
                    dto.setMemberTypeId(JSONUtil.getString(jsonObject, "customerRole"));
                    dto.setMemberTypeName(JSONUtil.getString(jsonObject, "roleDisplayName"));
                    responseDTO.add(dto);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching the account holder information" + e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10737);
        }
        return responseDTO;
    }
}
