package com.kony.dbputilities.customersecurityservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class createOrgEmployeeAccounts implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		Result result = new Result();
		Record record = new Record();
		record.setId(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);

		String id = inputParams.get("id");
		if (StringUtils.isBlank(id) && StringUtils.isNotBlank(dcRequest.getParameter("UserName"))) {
			Result userRec = HelperMethods.getUserRecordByName(dcRequest.getParameter("UserName"), dcRequest);
			id = HelperMethods.getFieldValue(userRec, "id");
		}

		String loggedInUserId =  HelperMethods.getCustomerIdFromSession(dcRequest);
        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromAPIDBPIdentityService(dcRequest);
        if(StringUtils.isBlank(loggedInUserId)) {
            loggedInUserId = loggedInUserInfo.get("customer_id");
        }
        
        
        if (StringUtils.isBlank(id)  || loggedInUserId.equals(id)) {
            ErrorCodeEnum.ERR_12405.setErrorCode(record);
            result.addRecord(record);
            return result;
        }
		String orgIdOfUserUnderUpdate = inputParams.get("Organization_Id"); 
				
		if(StringUtils.isBlank(orgIdOfUserUnderUpdate)) {
				orgIdOfUserUnderUpdate = HelperMethods.getOrganizationIDForUser(id, dcRequest);
		}
		if (StringUtils.isBlank(orgIdOfUserUnderUpdate)) {
			ErrorCodeEnum.ERR_12406.setErrorCode(record);
			result.addRecord(record);
			return result;
		}

		String userName = loggedInUserInfo.get("UserName");
		String loggedInUserOrgId = HelperMethods.getOrganizationIDForUser(loggedInUserId, dcRequest);

		if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
			Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
			if (userPermissions.contains("USER_MANAGEMENT")) {
				if (!loggedInUserOrgId.equals(orgIdOfUserUnderUpdate)) {
					ErrorCodeEnum.ERR_12408.setErrorCode(record);
					result.addRecord(record);
					return result;
				}
			} else {
				ErrorCodeEnum.ERR_12407.setErrorCode(record);
				result.addRecord(record);
				return result;
			}
		}

		processAccountsForEmployee(id, inputParams, dcRequest, orgIdOfUserUnderUpdate, result, userName);

		return result;

	}

	private void processAccountsForEmployee(String id, Map<String, String> inputParams, DataControllerRequest dcRequest,
			String orgIdOfUserUnderUpdate, Result result, String userName) {

		Record record = new Record();
		record.setId(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);

		if (StringUtils.isBlank(id)) {
			ErrorCodeEnum.ERR_12409.setErrorCode(record);
			result.addRecord(record);
			return;
		}

		String customer_id = id;

		result.addParam(new Param("Customer_id", customer_id, "String"));
		result.addParam(new Param("id", id, "String"));

		JSONObject existingAccounts = new JSONObject();
		existingAccounts = getExistingAccounts(customer_id, dcRequest);
		
		validateAccounts(dcRequest, orgIdOfUserUnderUpdate);
		createEmployeeAccounts(customer_id, existingAccounts, dcRequest, orgIdOfUserUnderUpdate,
				result, userName);
	}

	private static JSONArray validateAccounts(DataControllerRequest dcRequest, String organizationId) {
		// TODO Auto-generated method stub


		String accounts = dcRequest.getParameter("accounts");

		JSONArray accountsJSON = new JSONArray(accounts);

		JSONArray array = new  JSONArray();

		Set<String> set = getOrganizationAccounts(dcRequest, organizationId);
		
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();

		for (int i = 0; i < accountsJSON.length(); i++) {
			JSONObject account = accountsJSON.getJSONObject(i);
			map.put(account.getString("accountId"), account);
		}

		for(String key : map.keySet()) {
			JSONObject account = map.get(key);
			if(set.contains(key)){
				array.put(account);
			}
		}

		dcRequest.addRequestParam_("accounts", array.toString());

		return array;
	}
	
	public static Set<String> getOrganizationAccounts(DataControllerRequest dcRequest, String organizationId) {
		// TODO Auto-generated method stub
		String filter  = "Organization_id" + DBPUtilitiesConstants.EQUAL + organizationId;

		Set<String> set = new HashSet<String>();

		try {
			Result result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNT_GET);
			if(HelperMethods.hasRecords(result)) {
				for(Record record : result.getAllDatasets().get(0).getAllRecords()) {
					set.add(record.getParamValueByName("Account_id"));
				}
			}
		} catch (HttpCallException e) {
			
			alert.prepareError(e.toString()).log();
		}

		return set;
	}

	private void createEmployeeAccounts(String customer_id, JSONObject existingAccounts,
			DataControllerRequest dcRequest, String organizationId, Result result, String userName) {

		String customer_accounts = dcRequest.getParameter("accounts");
		JSONArray accountsJSON = new JSONArray(customer_accounts);
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();

		for (int i = 0; i < accountsJSON.length(); i++) {
			JSONObject account = accountsJSON.getJSONObject(i);
			map.put(account.getString("accountId"), account);
		}
		
		Record rec = new Record();
		rec.setId(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);

		Result createAccount = new Result();
		List<String> createdAccounts = new ArrayList<>();

		for(String key : existingAccounts.keySet()) {
			if(map.containsKey(key)) {
				map.remove(key);
			}
			else {

				Map<String, String> input = new HashMap<>();
				input.put("id", existingAccounts.getString(key));
				
				try {
					Result deleteAdditionalAccounts = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
							URLConstants.CUSTOMERACCOUNTS_DELETE);
					if (!HelperMethods.hasError(deleteAdditionalAccounts)) {
						HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, rec);
						result.addRecord(rec);
					} else {
						ErrorCodeEnum.ERR_12412.setErrorCode(rec);
						result.addRecord(rec);
					}
				} catch (HttpCallException e) {
					alert.prepareError(e.toString()).log();
				}
			}
		}

		for(String key : map.keySet()) {
			Map<String, String> inputMaps = new HashMap<>();
			JSONObject account = map.get(key);
			UUID uuid = UUID.randomUUID();
			inputMaps.put("id", uuid.toString());
			inputMaps.put("Customer_id", customer_id);
			inputMaps.put("Account_id", account.getString("accountId"));
			inputMaps.put("AccountName",account.has("accountName") ? account.getString("accountName") : "");
			inputMaps.put("Organization_id", organizationId);
			inputMaps.put("IsViewAllowed", "1");
			inputMaps.put("IsDepositAllowed", "1");
			inputMaps.put("IsWithdrawAllowed", "1");
			inputMaps.put("IsOrganizationAccount", "1");
			inputMaps.put("createdby", userName);
			inputMaps.put("createdts", HelperMethods.getCurrentTimeStamp());
			try {
				createAccount = HelperMethods.callApi(dcRequest, inputMaps, HelperMethods.getHeaders(dcRequest),
						URLConstants.CUSTOMERACCOUNTS_CREATE);
				if (!HelperMethods.hasError(createAccount)) {
					HelperMethods.setSuccessMsg(DBPUtilitiesConstants.SUCCESS_MSG, rec);
					result.addRecord(rec);
					createdAccounts.add(inputMaps.get("Account_id"));
				} else {
					ErrorCodeEnum.ERR_12411.setErrorCode(rec);
					result.addRecord(rec);
				}
			} catch (HttpCallException e) {
				alert.prepareError(e.getMessage()).log();
			}
			
		}
	}

	private JSONObject getExistingAccounts(String customer_id, DataControllerRequest dcRequest) {

		JSONObject existingAccounts = new JSONObject();

		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customer_id + DBPUtilitiesConstants.AND + "IsOrganizationAccount"+ DBPUtilitiesConstants.EQUAL + "1";
		Result getExisting = getExistingAccounts(filter, customer_id, dcRequest);

		if (HelperMethods.hasRecords(getExisting)) {
			List<Record> accounts = getExisting.getAllDatasets().get(0).getAllRecords();
			for (Record accountRecord : accounts) {
				existingAccounts.put(HelperMethods.getFieldValue(accountRecord, "Account_id"),
						HelperMethods.getFieldValue(accountRecord, "id"));
			}
		}
		return existingAccounts;
	}

	public Result getExistingAccounts(String filter, String customer_id, DataControllerRequest dcRequest) {

		Result getExisting = new Result();

		try {
			getExisting = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERACCOUNTS_GET);
		} catch (HttpCallException e) {
			alert.prepareError(e.getMessage()).log();
		}

		return getExisting;
	}

}
