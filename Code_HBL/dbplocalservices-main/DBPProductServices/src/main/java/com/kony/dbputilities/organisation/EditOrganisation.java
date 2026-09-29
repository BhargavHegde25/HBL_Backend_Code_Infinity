package com.kony.dbputilities.organisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.approvalmatrixservices.businessdelegate.api.ApprovalMatrixBusinessDelegate;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.dto.BusinessTypeDTO;

public class EditOrganisation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		final String ROLETYPEID = "TYPE_ID_BUSINESS";
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		StringBuilder businessTypeId = new StringBuilder();

		if (preProcess(inputParams, dcRequest, result, businessTypeId)) {
			if (StringUtils.isNotBlank(dcRequest.getParameter("id"))) {

				String organisationId = dcRequest.getParameter("id");
				List<HashMap<String, String>> addedList = new ArrayList<>();
				List<HashMap<String, String>> removedList = new ArrayList<>();
				List<HashMap<String, String>> notUpdatedList = new ArrayList<>();

				result = editOrganisation(inputParams, businessTypeId, dcRequest);
				Set<String> hashSet = null;
				if (!HelperMethods.hasError(result) && StringUtils.isNotBlank(dcRequest.getParameter("AccountsList"))) {
					seperateAccountsListGiven(dcRequest.getParameter("AccountsList"), addedList, removedList,
							notUpdatedList, dcRequest.getParameter("id"), dcRequest);
					if (!checkIfGivenAccountsListIsValid(addedList, dcRequest)) {
						Result errorResult = new Result();
						ErrorCodeEnum.ERR_12609.setErrorCode(errorResult);
						return errorResult;
					}
				}
				if (!HelperMethods.hasError(result)
						&& StringUtils.isNotBlank(dcRequest.getParameter("Communication"))) {
					result = EditOrganisationCommunication.invoke(inputParams, dcRequest);
				}
				if (!HelperMethods.hasError(result) && StringUtils.isNotBlank(dcRequest.getParameter("Address"))) {
					result = EditOrganisationAddress.invoke(inputParams, dcRequest);
				}
				if (!HelperMethods.hasError(result)) {
					List<HashMap<String, String>> newleyAddedMembershipList = getNewlyAddedMembershiList(addedList,
							notUpdatedList);
					result = CreateOrganisationMembership.invoke(organisationId, newleyAddedMembershipList, dcRequest);
				}
				if (!HelperMethods.hasError(result)
						&& StringUtils.isNotBlank(dcRequest.getParameter("addedFeatures"))) {
					dcRequest.addRequestParam_(DBPUtilitiesConstants.ORG_FEATURES,
							dcRequest.getParameter("addedFeatures"));
					result = CreateOrganisationFeaturesAndActions.invoke(inputParams, dcRequest, ROLETYPEID);
					Set<String> newlyAddedActionSetWhileEdit = getActionsListCreated(result);
					if (!newlyAddedActionSetWhileEdit.isEmpty())
						addFeaturesToApprovalMatrix(organisationId, notUpdatedList, newlyAddedActionSetWhileEdit,
								dcRequest);

				}
				if (!HelperMethods.hasError(result)
						&& StringUtils.isNotBlank(dcRequest.getParameter("removedFeatures"))) {
					result = RemoveOrganisationFeatures.invoke(inputParams, dcRequest, ROLETYPEID);
					Set<String> removedActionSetWhileEdit = getActionsListCreated(result);
					removeFeaturesFromApprocalMatrix(organisationId, removedActionSetWhileEdit, dcRequest);
				}

				hashSet = getOrganisationActions(organisationId, dcRequest);

				if (!HelperMethods.hasError(result) && StringUtils.isNotBlank(dcRequest.getParameter("AccountsList"))) {
					EditOrganisationAccounts.invoke(inputParams, removedList, dcRequest);
					result = CreateOrganisationAccounts.invoke(inputParams, addedList, dcRequest, hashSet);
				}
				if (!HelperMethods.hasError(result)
						&& StringUtils.isNotBlank(dcRequest.getParameter("updatedActionlimits"))) {
					dcRequest.addRequestParam_(DBPUtilitiesConstants.ORG_ACTIONLIMITS,
							dcRequest.getParameter("updatedActionlimits"));
					result = CreateOrganisationActionLimits.invoke(inputParams, dcRequest, ROLETYPEID, hashSet);
				}

			}
			return postProcess(result);
		}

		return result;
	}

	private List<HashMap<String, String>> getNewlyAddedMembershiList(List<HashMap<String, String>> addedList,
			List<HashMap<String, String>> notUpdatedList) {
		List<HashMap<String, String>> resultList = new ArrayList<>();
		for (HashMap<String, String> addedAccountMap : addedList) {
			boolean status = false;
			for (HashMap<String, String> notUpdatedAccountMap : notUpdatedList) {
				if (StringUtils.isNotBlank(addedAccountMap.get("Membership_id"))
						&& addedAccountMap.get("Membership_id").equals(notUpdatedAccountMap.get("Membership_id"))) {
					status = true;
				}
			}
			if (!status) {
				resultList.add(addedAccountMap);
			}

		}
		return resultList;
	}

	private boolean checkIfGivenAccountsListIsValid(List<HashMap<String, String>> accountList,
			DataControllerRequest dcRequest) {
		StringBuilder givenAccountsListString = new StringBuilder();
		Result result = null;
		for (HashMap<String, String> map : accountList) {
			String accountId = map.get("Account_id");
			if (StringUtils.isNotBlank(accountId)) {
				givenAccountsListString.append(accountId).append(",");
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

	private boolean compareGivenLIstToValidList(String givenList, String validList) {
		String[] givenarrayList = StringUtils.split(givenList, ",");
		String[] validArrayList = StringUtils.split(validList, ",");

		return (givenarrayList != null && validArrayList != null && givenarrayList.length == validArrayList.length);

	}

	private void removeFeaturesFromApprocalMatrix(String organisationId, Set<String> removedActionSetWhileEdit,
			DataControllerRequest dcRequest) {
		try {
			ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);
			String[] actionIds = checkForMonetaryActionsAndconvertHashsetToStringArray(removedActionSetWhileEdit,
					dcRequest);
			approvalmatrixDelegate.deleteApprovalMatrixEntry(organisationId, "", removedActionSetWhileEdit, "actionId");
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
			alert.prepareError("Error occured while deleting the default approval matrix").log();
		}

	}

    private void addFeaturesToApprovalMatrix(String organisationId, List<HashMap<String, String>> notUpdatedList,
            Set<String> newlyAddedActionSetWhileEdit, DataControllerRequest dcRequest) {
        String[] actionIds =
                checkForMonetaryActionsAndconvertHashsetToStringArray(newlyAddedActionSetWhileEdit, dcRequest);

		for (int i = 0; i < notUpdatedList.size(); i++) {
			HashMap<String, String> account = notUpdatedList.get(i);
			String accountId = account.get("Account_id");
			try {
				ApprovalMatrixBusinessDelegate approvalmatrixDelegate = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(ApprovalMatrixBusinessDelegate.class);

				approvalmatrixDelegate.createDefaultApprovalMatrixEntry(organisationId, accountId, actionIds,"", null);
			} catch (Exception e) {
				alert.prepareError(e.getMessage()).log();
				alert.prepareError("Error occured while creating default approval matrix").log();

			}
		}
	}

	private static String[] checkForMonetaryActionsAndconvertHashsetToStringArray(Set<String> hashSet,
			DataControllerRequest dcRequest) {
		String[] actionsList = new String[0];
		Map<String, String> input = new HashMap<>();
		Result result = new Result();
		StringBuilder actionsString = new StringBuilder();
		for (String action : hashSet) {
			actionsString.append(action);
			actionsString.append(",");
		}
		if (actionsString.length() > 0)
			actionsString.replace(actionsString.length() - 1, actionsString.length(), "");

		input.put("_featureActions", actionsString.toString());

		try {
			result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.GET_MONETARY_ACTIONS);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}

		String monetaryActionsString = HelperMethods.getFieldValue(result, "monetaryActions");
		if (StringUtils.isNotBlank(monetaryActionsString)) {
			actionsList = monetaryActionsString.split(",");
		}

		return actionsList;

	}

	private Set<String> getOrganisationActions(String organisationId, DataControllerRequest dcRequest) {
		Result result = new Result();
		Map<String, String> input = new HashMap<>();
		input.put("_organisationId", organisationId);

		try {
			result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATION_ACTIONS_GET_PROC);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}

		return getActionsListCreated(result);
	}

	private void seperateAccountsListGiven(String accountsList, List<HashMap<String, String>> addedList,
			List<HashMap<String, String>> removedList, List<HashMap<String, String>> notUpdatedList, String orgId,
			DataControllerRequest dcRequest) {
		List<String> organisationAccounts = getOrganisationAccounts(orgId, dcRequest);
		List<HashMap<String, String>> accounts = HelperMethods.getAllRecordsMap(accountsList);
		for (int i = 0; i < accounts.size(); i++) {
			HashMap<String, String> account = accounts.get(i);
			String accountId = account.get("Account_id");
			if (organisationAccounts.contains(accountId)) {
				organisationAccounts.remove(accountId);
				HashMap<String, String> map = new HashMap<>();
				map.put("Account_id", accountId);
				notUpdatedList.add(map);
			} else {
				HashMap<String, String> map = new HashMap<>();
				map.put("Account_id", accountId);
				for (Entry<String, String> entry : account.entrySet()) {
					map.put(entry.getKey(), entry.getValue());
				}
				addedList.add(map);
			}
		}

		for (String account : organisationAccounts) {
			HashMap<String, String> map = new HashMap<>();
			map.put("Account_id", account);
			removedList.add(map);
		}

	}

	private List<String> getOrganisationAccounts(String orgId, DataControllerRequest dcRequest) {
		Result result = null;
		String filter = "";
		List<String> organisationAccounts = new ArrayList<>();

		if (StringUtils.isNotBlank(orgId)) {
			filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
		}

		if (!filter.isEmpty()) {
			try {
				result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
						URLConstants.ACCOUNT_GET);
			} catch (HttpCallException e) {
			}
		}
		if (HelperMethods.hasRecords(result)) {
			List<Record> records = result.getAllDatasets().get(0).getAllRecords();
			String accountId = null;
			for (Record record : records) {
				accountId = HelperMethods.getFieldValue(record, "Account_id");
				organisationAccounts.add(accountId);
			}

		}
		return organisationAccounts;
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
			ErrorCodeEnum.ERR_11001.setErrorCode(result);
			return false;
		}
	}

	private boolean checkForMandatoryDetails(Map<String, String> inputParams, DataControllerRequest dcRequest,
			StringBuilder businessTypeId, Result result) {
		boolean status = false;
		if (StringUtils.isNotBlank(dcRequest.getParameter("Name"))
				&& StringUtils.isNotBlank(dcRequest.getParameter("Communication"))
				&& !HelperMethods.getAllRecordsMap(dcRequest.getParameter("Communication")).isEmpty()
				&& StringUtils.isNotBlank(dcRequest.getParameter("id"))) {
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
			businessTypeDTOList = businessTypeBusinessDelegate.getBusinessType(dcRequest.getHeaderMap());
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

	private Result editOrganisation(Map<String, String> inputParams, StringBuilder businessTypeId,
			DataControllerRequest dcRequest) throws HttpCallException {
		Result result = new Result();
		Map<String, String> input = new HashMap<>();
		if (StringUtils.isNotBlank(dcRequest.getParameter("Name"))) {
			input.put("id", dcRequest.getParameter("id"));
			input.put("Name", dcRequest.getParameter("Name"));
			input.put("Description", dcRequest.getParameter("Description"));
			input.put("FaxId", dcRequest.getParameter("FaxId"));
			boolean isCustomersExists = checkIfCustomerExistsForOrganization(dcRequest.getParameter("id"), dcRequest);
			if (!isCustomersExists) {
				input.put("BusinessType_id", businessTypeId.toString());
			}
			result = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATION_UPDATE);
		}
		return result;
	}

	private boolean checkIfCustomerExistsForOrganization(String orgId, DataControllerRequest dcRequest) {
		String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
		Result orgEmployees = null;
		try {
			orgEmployees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATIONEMPLOYEE_GET);
		} catch (HttpCallException e) {
			alert.prepareError("Error while getting the organisation employee records").log();
		}
		return HelperMethods.hasRecords(orgEmployees);
	}

	private Result postProcess(Result result) {

		Result retResult = new Result();

		if (HelperMethods.hasRecords(result)) {
			HelperMethods.setSuccessMsg("Organization Update Successful", retResult);
		} else if (HelperMethods.hasError(result)) {

			ErrorCodeEnum.ERR_11002.setErrorCode(retResult, HelperMethods.getError(result));
		} else {

			ErrorCodeEnum.ERR_11003.setErrorCode(retResult);
		}

		return retResult;
	}
}
