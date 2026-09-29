package com.kony.dbputilities.organisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.accounts.businessdelegate.api.AccountsBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.dto.AllAccountsViewDTO;
import com.temenos.dbx.product.dto.ApplicationDTO;
import com.temenos.dbx.product.dto.BusinessTypeDTO;

public class CreateOrganisation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        final String ROLETYPEID = "TYPE_ID_BUSINESS";
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        StringBuilder businessTypeId = new StringBuilder();

        if (preProcess(inputParams, dcRequest, result, businessTypeId)) {
            List<HashMap<String, String>> accountList =
                    HelperMethods.getAllRecordsMap(dcRequest.getParameter("AccountsList"));
            String string = dcRequest.getParameter("Membership");
            List<HashMap<String, String>> membershipList = HelperMethods.getAllRecordsMap(string);

            if (!checkIfGivenAccountsListIsValidAndUpdateMembershipList(accountList, membershipList, dcRequest)) {
                ErrorCodeEnum.ERR_12609.setErrorCode(result);
                return result;
            }
            try {
                if (!checkIfGivenAccountListIsValidInCore(accountList, membershipList, dcRequest)) {
                    ErrorCodeEnum.ERR_12610.setErrorCode(result);
                    return result;
                }
            } catch (ApplicationException e) {
                e.getErrorCodeEnum().setErrorCode(result);
                return result;
            }

            result = createOrganization(businessTypeId.toString(), dcRequest);

            if (HelperMethods.hasRecords(result)) {
                String id = HelperMethods.getFieldValue(result, "id");
                Set<String> hashSet = null;
                inputParams.put("id", id);
                if (StringUtils.isNotBlank(dcRequest.getParameter("Communication"))) {
                    result = CreateOrganisationCommunication.invoke(inputParams, dcRequest);
                }
                if (!HelperMethods.hasError(result) && StringUtils.isNotBlank(dcRequest.getParameter("Address"))) {
                    result = CreateOrganisationAddress.invoke(inputParams, dcRequest);
                }
                if (!HelperMethods.hasError(result)
                        && StringUtils.isNotBlank(dcRequest.getParameter("Membership"))) {
                    result = CreateOrganisationMembership.invoke(id, membershipList, dcRequest);
                }
                if (!HelperMethods.hasError(result)
                        && StringUtils.isNotBlank(dcRequest.getParameter(DBPUtilitiesConstants.ORG_FEATURES))) {
                    result = CreateOrganisationFeaturesAndActions.invoke(inputParams, dcRequest, ROLETYPEID);
                    hashSet = getActionsListCreated(result);
                }
                if (!HelperMethods.hasError(result)
                        && StringUtils.isNotBlank(dcRequest.getParameter("AccountsList"))) {

                    result = CreateOrganisationAccounts.invoke(inputParams, accountList, dcRequest, hashSet);
                }
                if (!HelperMethods.hasError(result)
                        && StringUtils.isNotBlank(dcRequest.getParameter(DBPUtilitiesConstants.ORG_ACTIONLIMITS))) {
                    result = CreateOrganisationActionLimits.invoke(inputParams, dcRequest, ROLETYPEID, hashSet);
                }
            } else if (HelperMethods.hasError(result)) {

                if (HelperMethods.getError(result).contains("Name_UNIQUE")) {
                    ErrorCodeEnum.ERR_11008.setErrorCode(result);
                } else {
                    ErrorCodeEnum.ERR_11009.setErrorCode(result);
                }
                return result;
            }

            result = postProcess(inputParams, result, dcRequest);

            if (result.getNameOfAllParams().contains("id")) {
                new SendEmailToEnrollOrganization().invoke(inputParams, dcRequest);
            }

        }

        return result;
    }

    private boolean checkIfGivenAccountListIsValidInCore(List<HashMap<String, String>> accountList,
            List<HashMap<String, String>> membershipList, DataControllerRequest dcRequest) throws ApplicationException {
        ApplicationBusinessDelegate applicationBD = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ApplicationBusinessDelegate.class);

        AccountsBusinessDelegate accountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(AccountsBusinessDelegate.class);

        List<AllAccountsViewDTO> allAccountsDTOList = new ArrayList<>();
        boolean status = false;

        ApplicationDTO applicationDTO = applicationBD.getApplicationProperties(dcRequest.getHeaderMap());
        if ("0".equals(applicationDTO.getIsAccountCentricCore())
                || "false".equals(applicationDTO.getIsAccountCentricCore())) {
            for (HashMap<String, String> membershipMap : membershipList) {
                String cif = membershipMap.get("Membership_id");
                String taxId = membershipMap.get("Taxid");
                if (StringUtils.isBlank(cif) || StringUtils.isBlank(taxId)) {
                    return false;
                }
                List<AllAccountsViewDTO> accountsDTOList = accountsBusinessDelegate.getUnUsedAccountsFromMembership(cif,
                        dcRequest.getHeaderMap());
                if (null == accountsDTOList || accountsDTOList.isEmpty()) {
                    return false;
                }
                allAccountsDTOList.addAll(accountsDTOList);
            }
            status = verifyGivenAccountsListIsValidAgainstCore(accountList, allAccountsDTOList);

        } else {

            for (HashMap<String, String> accountsMap : accountList) {
                String accountId = accountsMap.get("Account_id");
                if (StringUtils.isBlank(accountId)) {
                    return false;
                }
                AllAccountsViewDTO dto = new AllAccountsViewDTO();
                dto.setAccountId(accountId);
                List<AllAccountsViewDTO> dtoList = accountsBusinessDelegate.getAllAccountsInformation(dto,
                        dcRequest.getHeaderMap());
                if (null == dtoList || dtoList.isEmpty()) {
                    return false;
                }

            }
            status = true;

        }
        return status;

    }

    private boolean verifyGivenAccountsListIsValidAgainstCore(List<HashMap<String, String>> accountList,
            List<AllAccountsViewDTO> allAccountsDTOList) {
        boolean status = true;
        for (HashMap<String, String> accountsMap : accountList) {
            String accountId = accountsMap.get("Account_id");
            String membershipId = accountsMap.get("Membership_id");
            String taxId = accountsMap.get("TaxId");
            if (!status) {
                return status;
            }
            for (AllAccountsViewDTO accountDTO : allAccountsDTOList) {
                if (accountId.equalsIgnoreCase(accountDTO.getAccountId())) {
                    status = membershipId.equalsIgnoreCase(accountDTO.getMembershipId())
                            && taxId.equalsIgnoreCase(accountDTO.getTaxId());
                    break;
                }
                status = false;
            }

        }
        return status;
    }

    private boolean checkIfGivenAccountsListIsValidAndUpdateMembershipList(List<HashMap<String, String>> accountList,
            List<HashMap<String, String>> membershipList, DataControllerRequest dcRequest) {
        Set<String> membershipIdList = getMembershipIdList(membershipList);
        StringBuilder givenAccountsListString = new StringBuilder();
        Result result = null;
        for (HashMap<String, String> map : accountList) {
            String accountId = map.get("Account_id");
            String membershipId = map.get("Membership_id");
            String taxId = map.get("TaxId");
            if (StringUtils.isNotBlank(accountId)) {
                givenAccountsListString.append(accountId).append(",");
            }
            if (StringUtils.isNotBlank(taxId) && !membershipIdList.contains(taxId)) {
                HashMap<String, String> membershipMap = new HashMap<>();
                membershipMap.put("Membership_id", membershipId);
                membershipMap.put("Taxid", taxId);
                membershipList.add(membershipMap);
                membershipIdList.add(taxId);

            }

        }
        if (givenAccountsListString.length() > 0) {
            givenAccountsListString.replace(givenAccountsListString.length() - 1, givenAccountsListString.length(), "");
        }
        if (StringUtils.isBlank(givenAccountsListString.toString())) {
            return true;
        } else {
            Map<String, String> inputParams = new HashMap<>();
            inputParams.put("_accountsList", givenAccountsListString.toString());
            try {
                result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                        URLConstants.GET_VALID_ORG_ACCOUNTS_LIST);
            } catch (HttpCallException e) {
                alert.prepareError(e.getMessage()).log();
            }
        }
        if (HelperMethods.hasRecords(result)) {
            String validList = HelperMethods.getFieldValue(result, "accountsList");
            return compareGivenLIstToValidList(givenAccountsListString.toString(), validList);

        }
        return false;
    }

    private Set<String> getMembershipIdList(List<HashMap<String, String>> membershipList) {
        Set<String> membershipIdSet = new HashSet<>();
        for (Map<String, String> membershipMap : membershipList) {
            if (StringUtils.isNotBlank(membershipMap.get("Taxid"))) {
                membershipIdSet.add(membershipMap.get("Taxid"));
            }
        }

        return membershipIdSet;
    }

    private boolean compareGivenLIstToValidList(String givenList, String validList) {
        String[] givenarrayList = StringUtils.split(givenList, ",");
        String[] validArrayList = StringUtils.split(validList, ",");

        return (givenarrayList != null && validArrayList != null && givenarrayList.length == validArrayList.length);

    }

    private static Set<String> getActionsListCreated(Result result) {
        String actionList = null;
        Set<String> hashSet = null;
        if (HelperMethods.hasRecords(result)) {
            actionList = result.getAllDatasets().get(0).getRecord(0).getParamValueByName("actionslist");
        }
        if (StringUtils.isNotBlank(actionList)) {
            hashSet = HelperMethods.splitString(actionList, DBPUtilitiesConstants.ACTIONS_SEPERATOR);
        } else {
            hashSet = new HashSet<>();
        }
        return hashSet;

    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result,
            StringBuilder businessTypeId) {

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);
        if (!HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)
                && checkAndUpdateBusinessTypeId(inputParams, dcRequest, businessTypeId, result)
                && checkForMandatoryDetails(inputParams, dcRequest, businessTypeId, result)) {
            return true;
        } else if (HelperMethods.hasError(result)) {
            return false;
        } else {
            ErrorCodeEnum.ERR_11007.setErrorCode(result);
            return false;
        }
    }

    private boolean checkForMandatoryDetails(Map<String, String> inputParams, DataControllerRequest dcRequest,
            StringBuilder businessTypeId, Result result) {
        boolean status = false;
        if (StringUtils.isNotBlank(dcRequest.getParameter("Name"))
                && StringUtils.isNotBlank(dcRequest.getParameter("Communication"))
                && !HelperMethods.getAllRecordsMap(dcRequest.getParameter("Communication")).isEmpty()) {
            status = true;
        } else {
            ErrorCodeEnum.ERR_10204.setErrorCode(result);
            status = false;
        }
        return status;
    }

    private boolean checkAndUpdateBusinessTypeId(Map<String, String> inputParams, DataControllerRequest dcRequest,
            StringBuilder businessTypeId, Result result) {
        String type = inputParams.get("Type");
        if (StringUtils.isBlank(type)) {
            type = dcRequest.getParameter("Type");
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

        type = businessTypes.get(type);
        if (StringUtils.isBlank(type)) {
            ErrorCodeEnum.ERR_10203.setErrorCode(result);
            return false;
        }
        businessTypeId.append(type);
        return true;
    }

    private Result createOrganization(String businessTypeId, DataControllerRequest dcRequest)
            throws HttpCallException {

        Result result = new Result();
        final String INPUT_DESCRIPTION = "Description";

        Map<String, String> input = new HashMap<>();
        if (StringUtils.isNotBlank(dcRequest.getParameter("Name"))) {
            input.put("Name", dcRequest.getParameter("Name"));
            input.put("Type_Id", "TYPE_ID_BUSINESS");
            if (StringUtils.isNotBlank(dcRequest.getParameter(INPUT_DESCRIPTION))) {
                input.put("Description", dcRequest.getParameter(INPUT_DESCRIPTION));
            }
            input.put("FaxId", dcRequest.getParameter("FaxId"));
            input.put("BusinessType_id", businessTypeId);
            input.put("createdby", "admin");
            String approvalStatus = DBPUtilitiesConstants.ORGANISATION_STATUS_ACTIVE;
            input.put("StatusId", approvalStatus);
            input.put("id", HelperMethods.generateUniqueOrganisationId(dcRequest));
            HelperMethods.removeNullValues(input);
            result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATION_CREATE);
        }

        return result;
    }

    private Result postProcess(Map<String, String> inputParams, Result result, DataControllerRequest dcRequest) {
        Result retResult = new Result();
        if (HelperMethods.hasRecords(result)) {
            HelperMethods.setSuccessMsgwithCode(DBPUtilitiesConstants.SUCCESS_MSG, ErrorCodes.RECORD_CREATED,
                    retResult);
            retResult.addParam(new Param("id", inputParams.get("id"), "String"));

            String communication = dcRequest.getParameter("Owner");
            List<HashMap<String, String>> list = HelperMethods.getAllRecordsMap(communication);
            if (!list.isEmpty()) {
                Map<String, String> params = HelperMethods.getAllRecordsMap(communication).get(0);
                inputParams.put("Email", params.get("EmailId"));
                inputParams.put("FirstName", params.get("FirstName"));
                inputParams.put("LastName", params.get("LastName"));
            }

        } else {
            ErrorCodeEnum.ERR_11009.setErrorCode(retResult);
        }
        return retResult;
    }

}
