package com.temenos.dbx.product.usermanagement.resource.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomRoleActionLimitDTO;
import com.temenos.dbx.product.dto.CustomRoleDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomRoleBusinessDelegate;
import com.temenos.dbx.product.usermanagement.resource.api.CustomRoleResource;

public class CustomRoleResourceImpl implements CustomRoleResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static String ACCOUNT_ID_KEY = "Account_id";
    private static String ACCOUNT_NAME_KEY = "AccountName";
    private static String FEATURE_ID_KEY = "featureId";
    private static String ACTIONS_KEY = "actions";

    @Override
    public Result getAllCustomRoles(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Result result = null;
        try {
            // permission check for reading list of custom roles
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_VIEW");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String companyId = CustomerSession.getCompanyId(customer);

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);
            List<CustomRoleDTO> customRoles = customRoleDelegate.getAllCustomRoles(companyId, null,
                    request.getHeaderMap());
            JSONObject resultJSON = new JSONObject();
            resultJSON.put(DBPDatasetConstants.DATASET_CUSTOMROLES, new JSONArray(customRoles));
            result = JSONToResult.convert(resultJSON.toString());
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of getAllCustomRoles", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result createCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Result result = null;
        try {
            // code to check create custom role permission
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_CREATE");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String companyId = CustomerSession.getCompanyId(customer);
            String customerId = CustomerSession.getCustomerId(customer);

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            String customRoleName = String.valueOf(inputParams.get("customRoleName"));
            String parentRole_id = String.valueOf(inputParams.get("parentRoleId"));
            String description = String.valueOf(inputParams.get("description"));

            if (customRoleName == null) {
                return ErrorCodeEnum.ERR_21104.setErrorCode(new Result());
            }
            if (!Pattern.matches("^[a-zA-Z0-9 ]+$", description)
                    || !Pattern.matches("^[a-zA-Z0-9 ]+$", customRoleName)) {
                return ErrorCodeEnum.ERR_21107.setErrorCode(new Result());
            }
            if (parentRole_id == null || parentRole_id.equals("")) {
                return ErrorCodeEnum.ERR_21105.setErrorCode(new Result());
            }

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);

            CustomRoleDTO inputDTO = new CustomRoleDTO();
            inputDTO.setName(customRoleName);
            inputDTO.setOrganization_id(companyId);
            inputDTO.setParent_id(parentRole_id);
            inputDTO.setCreatedby(customerId);
            inputDTO.setDescription(description);

            JSONArray accounts = new JSONArray(inputParams.get("accounts").toString());
            JSONArray features = new JSONArray(inputParams.get("features").toString());

            String customRoleId = customRoleDelegate.createCustomRole(inputDTO, accounts, features,
                    request.getHeaderMap());

            result = new Result();
            List<Param> params = new ArrayList<>();
            if (customRoleId != null) {
                if (customRoleId.equals(ErrorConstants.EMPTY_ACCOUNT_LIST)) {
                    return ErrorCodeEnum.ERR_21113.setErrorCode(new Result());
                }
                if (customRoleId.equals(ErrorConstants.INVALID_TRANSACTION_LIMITS)) {
                    return ErrorCodeEnum.ERR_21108.setErrorCode(new Result());
                }
                if (customRoleId.equals(ErrorConstants.UNIQUE_CUSTOM_ROLE_NAME)) {
                    return ErrorCodeEnum.ERR_21109.setErrorCode(new Result());
                }
                if (customRoleId.equals(ErrorConstants.INVALID_ORG_ACCOUNTS)) {
                    return ErrorCodeEnum.ERR_21110.setErrorCode(new Result());
                }
                String userName = HelperMethods.getCustomerFromIdentityService(request).get("username");
                Timestamp ts = new Timestamp(new Date().getTime());
                params.add(new Param("Success", "Successful"));
                params.add(new Param("customRoleId", customRoleId));
                params.add(new Param("createdby", userName));
                params.add(new Param("createdts", String.valueOf(ts)));
            } else {
                params.add(new Param("Success", "Failed"));
            }
            result.addAllParams(params);
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of getAllCustomRoles", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result getCustomRoleById(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Result result = null;
        try {
            // permission check for reading details of a custom role
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_VIEW");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String companyId = CustomerSession.getCompanyId(customer);

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            String customRoleId = String.valueOf(inputParams.get("customRoleId"));
            if (customRoleId == null || customRoleId.equals("")) {
                return ErrorCodeEnum.ERR_21106.setErrorCode(new Result());
            }

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);
            CustomRoleDTO customRole = customRoleDelegate
                    .getAllCustomRoles(companyId, customRoleId, request.getHeaderMap()).get(0);
            List<CustomRoleActionLimitDTO> actionLimits = customRoleDelegate.getCustomRoleActionLimits(customRoleId,
                    request.getHeaderMap());

            ACCOUNT_ID_KEY = "accountId";
            ACCOUNT_NAME_KEY = "AccountName";
            ACTIONS_KEY = "actions";
            FEATURE_ID_KEY = "featureId";

            result = processFinalResult(customRole, actionLimits);
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of custom role details by id", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result deleteCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Result result = null;
        try {
            // permission check before deleting a custom role
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_DELETE");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            String customRoleId = String.valueOf(inputParams.get("customRoleId"));
            if (customRoleId == null || customRoleId.equals("")) {
                return ErrorCodeEnum.ERR_21106.setErrorCode(new Result());
            }

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);
            boolean isSuccess = customRoleDelegate.deleteCustomRole(customRoleId, request.getHeaderMap());
            result = new Result();
            Param success;
            if (isSuccess) {
                success = new Param("Success", "Delete Success");
            } else {
                success = new Param("Success", "Delete Failed");
            }
            result.addParam(success);
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of delete custom role", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result updateCustomRole(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {

        Result result = null;
        try {
            // permission check before updating a custom role
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_CREATE");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String companyId = CustomerSession.getCompanyId(customer);
            String customerId = CustomerSession.getCustomerId(customer);
            String userName = CustomerSession.getCustomerName(customer);

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            String customRoleId = String.valueOf(inputParams.get("customRoleId"));
            if (customRoleId == null || customRoleId.equals("")) {
                return ErrorCodeEnum.ERR_21106.setErrorCode(new Result());
            }

            JSONArray accounts = new JSONArray(String.valueOf(inputParams.get("accounts")));
            JSONArray features = new JSONArray(String.valueOf(inputParams.get("features")));
            String customRoleName = String.valueOf(inputParams.get("customRoleName"));
            String parentRole_id = String.valueOf(inputParams.get("parentRoleId"));
            String description = String.valueOf(inputParams.get("description"));

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);

            CustomRoleDTO customRole = customRoleDelegate
                    .getAllCustomRoles(companyId, customRoleId, request.getHeaderMap()).get(0);
            if (customRoleName != null && !customRoleName.equals("")) {
                customRole.setName(customRoleName);
            }
            if (parentRole_id != null && !parentRole_id.equals("")) {
                customRole.setParent_id(parentRole_id);
            }
            if (description != null && !description.equals("")) {
                customRole.setDescription(description);
            }
            customRole.setModifiedby(customerId);
            customRole.setUserName(userName);

            String respose = customRoleDelegate.updateCustomRole(customRole, accounts, features,
                    request.getHeaderMap());
            result = new Result();
            List<Param> params = new ArrayList<>();
            if (respose != null) {
                if (customRoleId.equals(ErrorConstants.INVALID_TRANSACTION_LIMITS)) {
                    return ErrorCodeEnum.ERR_21108.setErrorCode(new Result());
                }
                if (customRoleId.equals(ErrorConstants.UNIQUE_CUSTOM_ROLE_NAME)) {
                    return ErrorCodeEnum.ERR_21109.setErrorCode(new Result());
                }
                if (customRoleId.equals(ErrorConstants.INVALID_ORG_ACCOUNTS)) {
                    return ErrorCodeEnum.ERR_21110.setErrorCode(new Result());
                }
                params.add(new Param("Success", "Update Successful"));
                params.add(new Param("customRoleId", respose));
            } else {
                params.add(new Param("Success", "Update Failed"));
                params.add(new Param("customRoleId", ""));
            }
            result.addAllParams(params);
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of delete custom role", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result applyCustomRoleForUsers(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        Result result = null;
        try {
            // permission check before applying a custom role to list of selected users
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_APPLY");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String companyId = CustomerSession.getCompanyId(customer);

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            String customRoleId = String.valueOf(inputParams.get("customRoleId"));
            if (customRoleId == null || customRoleId.equals("")) {
                return ErrorCodeEnum.ERR_21106.setErrorCode(new Result());
            }

            JSONArray users = new JSONArray(String.valueOf(inputParams.get("users")));
            Map<String, Object> map = updateUsersPaylodWithCustomRoleDetails(users, customRoleId, companyId,
                    request.getHeaderMap(), request.getHeader("X-Kony-Authorization"));

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);
            boolean isSuccess = customRoleDelegate.applyCustomRoleForUsers(map, request.getHeaderMap(),
                    request.getHeader("X-Kony-Authorization"));
            result = new Result();
            Param success;
            if (isSuccess) {
                success = new Param("Success", "Successful");
            } else {
                success = new Param("Success", "Failed");
            }
            result.addParam(success);
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of apply custom role", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

    private Map<String, Object> updateUsersPaylodWithCustomRoleDetails(JSONArray users, String customRoleId,
            String companyId, Map<String, Object> headerMap, String konyAuthToken) {
        CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CustomRoleBusinessDelegate.class);

        CustomRoleDTO customRole = customRoleDelegate
                .getAllCustomRoles(companyId, customRoleId, headerMap).get(0);
        List<CustomRoleActionLimitDTO> actionLimits = customRoleDelegate.getCustomRoleActionLimits(customRoleId,
                headerMap);

        ACCOUNT_ID_KEY = "accountId";
        ACCOUNT_NAME_KEY = "accountName";
        ACTIONS_KEY = "Actions";
        FEATURE_ID_KEY = "featureID";

        int usersLength = users.length();
        StringBuilder Username = new StringBuilder("");
        Map<String, Object> userNameInput = new HashMap<>();
        for (int i = 0; i < usersLength; i++) {
            Username.append(users.getJSONObject(i).get("UserName"));
            if (i < usersLength - 1) {
                Username.append(",");
            }
        }
        userNameInput.put("Username", Username);
        userNameInput.put("loop_count", usersLength);

        JsonArray userdetails = customRoleDelegate.getListOfUserDetails(userNameInput, headerMap, konyAuthToken);
        Map<String, JSONObject> features = getFeaturesFromActionLimits(actionLimits);
        JSONArray accounts = getAccountsFromActionLimits(actionLimits, "accountId");
        Map<String, JSONArray> featureLevelAccounts = getAccountsAssociatedWithFeatures(actionLimits);
        Map<String, JSONArray> featureAccountLimits = getAccountLimitsAssociatedWithFeatures(actionLimits);

        JSONArray finalFeatures = getFinalFeaturesJSONObject(features, featureLevelAccounts, featureAccountLimits);

        Map<String, Object> map = new HashMap<>();
        StringBuilder userid = new StringBuilder("");
        StringBuilder userName = new StringBuilder("");
        StringBuilder roleId = new StringBuilder("");
        StringBuilder accountsInput = new StringBuilder("");
        StringBuilder featuresInput = new StringBuilder("");
        StringBuilder firstname = new StringBuilder("");
        StringBuilder lastname = new StringBuilder("");
        StringBuilder ssn = new StringBuilder("");
        StringBuilder dateofbirth = new StringBuilder("");
        StringBuilder email = new StringBuilder("");
        StringBuilder drivinglicense = new StringBuilder("");
        StringBuilder phone = new StringBuilder("");

        for (int i = 0; i < usersLength; i++) {
            userid.append(users.getJSONObject(i).get("UserId"));
            userName.append(users.getJSONObject(i).get("UserName"));
            roleId.append(customRole.getParent_id());
            accountsInput.append(accounts.toString());
            featuresInput.append(finalFeatures.toString());
            JsonObject json = userdetails.get(i).getAsJsonObject();
            firstname.append(JSONUtil.getString(json, "FirstName"));
            lastname.append(JSONUtil.getString(json, "LastName"));
            ssn.append(JSONUtil.getString(json, "Ssn"));
            dateofbirth.append(JSONUtil.getString(json, "DateOfBirth"));
            email.append(JSONUtil.getString(json, "Email"));
            drivinglicense.append(JSONUtil.getString(json, "DrivingLicenseNumber"));
            phone.append(JSONUtil.getString(json, "Phone"));
            if (i < usersLength - 1) {
                userid.append("%");
                userName.append("%");
                roleId.append("%");
                accountsInput.append("%");
                featuresInput.append("%");
                firstname.append("%");
                lastname.append("%");
                ssn.append("%");
                dateofbirth.append("%");
                email.append("%");
                drivinglicense.append("%");
                phone.append("%");
            }
        }

        map.put("UserName", userName.toString());
        map.put("id", userid.toString());
        map.put("Role_id", roleId);
        map.put("Ssn", ssn.toString());
        map.put("Phone", phone.toString());
        map.put("DrivingLicenseNumber", drivinglicense.toString());
        map.put("Email", email.toString());
        map.put("LastName", lastname.toString());
        map.put("FirstName", firstname.toString());
        map.put("DateOfBirth", dateofbirth);
        map.put("accounts", accountsInput.toString());
        map.put("features", featuresInput.toString());
        map.put("loop_count", usersLength);

        return map;
    }

    private Result processFinalResult(CustomRoleDTO customRole, List<CustomRoleActionLimitDTO> actionLimits) {

        Map<String, JSONObject> features = getFeaturesFromActionLimits(actionLimits);

        JSONArray accounts = getAccountsFromActionLimits(actionLimits, "Account_id");
        Map<String, JSONArray> featureLevelAccounts = getAccountsAssociatedWithFeatures(actionLimits);
        Map<String, JSONArray> featureAccountLimits = getAccountLimitsAssociatedWithFeatures(actionLimits);
        ACCOUNT_ID_KEY = "Account_id";
        JSONObject resultObject = new JSONObject();
        resultObject.put("id", customRole.getId());
        resultObject.put("CustomRoleName", customRole.getName());
        resultObject.put("Status", customRole.getStatusValue());
        resultObject.put("ParentRole", customRole.getParentRoleName());
        resultObject.put("ParentRoleId", customRole.getParent_id());
        resultObject.put("Description", customRole.getDescription());
        resultObject.put("accounts", accounts);
        resultObject.put("customerActionLimits",
                getFeaturesJSONObject(features, featureLevelAccounts, featureAccountLimits));

        return JSONToResult.convert(resultObject.toString());
    }

    private JSONArray getFeaturesJSONObject(Map<String, JSONObject> features,
            Map<String, JSONArray> featureLevelAccounts, Map<String, JSONArray> featureAccountLimits) {

        JSONArray featuresArray = new JSONArray();

        for (String featureKey : features.keySet()) {
            featuresArray.put(features.get(featureKey));
        }

        for (String actionId : featureLevelAccounts.keySet()) {
            int featureSize = featuresArray.length();
            for (int i = 0; i < featureSize; i++) {
                JSONArray actions = featuresArray.getJSONObject(i).getJSONArray(ACTIONS_KEY);
                int actionsSize = actions.length();
                for (int j = 0; j < actionsSize; j++) {
                    JSONObject action = actions.getJSONObject(j);
                    String featureActionId = action.getString("actionId");
                    if (featureActionId.equals(actionId)) {
                        JSONArray accountsJSON = featureLevelAccounts.get(actionId);
                        if (accountsJSON.length() == 1 && accountsJSON.getJSONObject(0).has("isEnabledValue")) {
                            action.put("isEnabled", accountsJSON.getJSONObject(0).get("isEnabledValue"));
                            if (action.get("isAccountLevel").equals("true")) {
                                action.put("accounts", new JSONArray());
                            }
                        } else {
                            action.put("accounts", accountsJSON);
                        }
                    }
                }
            }
        }

        for (String actionAccountId : featureAccountLimits.keySet()) {
            int featureSize = featuresArray.length();
            for (int i = 0; i < featureSize; i++) {
                JSONArray actions = featuresArray.getJSONObject(i).getJSONArray(ACTIONS_KEY);
                int actionsSize = actions.length();
                for (int act = 0; act < actionsSize; act++) {
                    JSONObject actionObject = actions.getJSONObject(act);
                    if (actionObject.has("accounts")) {
                        JSONArray accountsArray = actionObject.getJSONArray("accounts");
                        int accountsSize = accountsArray.length();
                        for (int j = 0; j < accountsSize; j++) {
                            JSONObject accountObject = accountsArray.getJSONObject(j);
                            String[] keys = actionAccountId.split("\\|", 2);
                            String actionId = keys[0];
                            String accountId = keys[1];
                            if (actionId.equals(actionObject.get("actionId"))
                                    && accountId.equals(accountObject.get("accountId"))) {
                                if (featureAccountLimits.get(actionAccountId) != null) {
                                    accountObject.put("limits", featureAccountLimits.get(actionAccountId));
                                }
                            }
                        }
                    }
                }
            }
        }

        return featuresArray;
    }

    private JSONArray getFinalFeaturesJSONObject(Map<String, JSONObject> features,
            Map<String, JSONArray> featureLevelAccounts, Map<String, JSONArray> featureAccountLimits) {

        JSONArray featuresArray = new JSONArray();

        for (String featureKey : features.keySet()) {
            featuresArray.put(features.get(featureKey));
        }

        for (String actionId : featureLevelAccounts.keySet()) {
            int featureSize = featuresArray.length();
            for (int i = 0; i < featureSize; i++) {
                JSONArray actions = featuresArray.getJSONObject(i).getJSONArray(ACTIONS_KEY);
                int actionsSize = actions.length();
                for (int j = 0; j < actionsSize; j++) {
                    JSONObject action = actions.getJSONObject(j);
                    String featureActionId = action.getString("actionId");
                    if (featureActionId.equals(actionId)) {
                        JSONArray accountsJSON = featureLevelAccounts.get(actionId);
                        if (accountsJSON.length() == 1 && accountsJSON.getJSONObject(0).has("isEnabledValue")) {
                            action.put("isEnabled", accountsJSON.getJSONObject(0).get("isEnabledValue"));
                            if (action.get("isAccountLevel").equals("true")) {
                                action.put("Accounts", new JSONArray());
                            }
                        } else {
                            JSONArray formatedAccountsJSON = new JSONArray();
                            for (int index = 0; index < accountsJSON.length(); index++) {
                                JSONObject accountObj = new JSONObject();
                                accountObj.put("id", accountsJSON.getJSONObject(index).getString("accountId"));
                                accountObj.put("isEnabled", accountsJSON.getJSONObject(index).getString("isEnabled"));
                                formatedAccountsJSON.put(accountObj);
                            }
                            action.put("Accounts", formatedAccountsJSON);
                        }
                    }
                }
            }
        }

        for (String actionAccountId : featureAccountLimits.keySet()) {
            int featureSize = featuresArray.length();
            for (int i = 0; i < featureSize; i++) {
                JSONArray actions = featuresArray.getJSONObject(i).getJSONArray(ACTIONS_KEY);
                int actionsSize = actions.length();
                for (int act = 0; act < actionsSize; act++) {
                    JSONObject actionObject = actions.getJSONObject(act);
                    if (actionObject.has("Accounts")) {
                        JSONArray accountsArray = actionObject.getJSONArray("Accounts");
                        int accountsSize = accountsArray.length();
                        for (int j = 0; j < accountsSize; j++) {
                            JSONObject accountObject = accountsArray.getJSONObject(j);
                            String[] keys = actionAccountId.split("\\|", 2);
                            String actionId = keys[0];
                            String accountId = keys[1];
                            if (actionId.equals(actionObject.get("actionId"))
                                    && accountId.equals(accountObject.get("id"))) {
                                if (featureAccountLimits.get(actionAccountId) != null) {
                                    JSONArray limitsArray = featureAccountLimits.get(actionAccountId);
                                    JSONObject limits = new JSONObject();
                                    for (int l = 0; l < limitsArray.length(); l++) {
                                        limits.put(limitsArray.getJSONObject(l).getString("id"),
                                                limitsArray.getJSONObject(l).getString("value"));
                                    }
                                    accountObject.put("limits", limits);
                                }
                            }
                        }
                    }
                }
            }
        }

        return featuresArray;
    }

    private Map<String, JSONArray> getAccountLimitsAssociatedWithFeatures(List<CustomRoleActionLimitDTO> actionLimits) {

        Map<String, JSONArray> limits = new HashMap<>();
        int actionLimitsSize = actionLimits.size();

        for (int i = 0; i < actionLimitsSize; i++) {
            String accountId = actionLimits.get(i).getAccount_id();
            String actionId = actionLimits.get(i).getAction_id();
            String key = actionId + "|" + accountId;

            JSONArray limitArray = null;
            if (limits.containsKey(key)) {
                limitArray = limits.get(key);
            } else {
                limitArray = new JSONArray();
            }

            JSONObject record = new JSONObject();
            record.put("id", actionLimits.get(i).getLimitType_id());
            record.put("value", actionLimits.get(i).getValue());
            if (!record.toString().equals("{}")) {
                limitArray.put(record);
                limits.put(key, limitArray);
            } else {
                limits.put(key, null);
            }
        }

        return limits;
    }

    private Map<String, JSONArray> getAccountsAssociatedWithFeatures(List<CustomRoleActionLimitDTO> actionLimits) {

        Map<String, JSONArray> accounts = new HashMap<>();
        int actionLimitsSize = actionLimits.size();
        Map<String, HashSet<String>> accountMapSet = new HashMap<>();

        for (int i = 0; i < actionLimitsSize; i++) {
            String actionId = actionLimits.get(i).getAction_id();
            JSONObject accountRecord = new JSONObject();
            String accountId = actionLimits.get(i).getAccount_id();
            JSONArray accountList = null;
            if (!accountMapSet.containsKey(actionId) || !accountMapSet.get(actionId).contains(accountId)) {
                if (accountId != null) {
                    accountRecord.put(ACCOUNT_ID_KEY, accountId);
                    accountRecord.put("isEnabled", actionLimits.get(i).getIsAllowed());
                    if (accounts.containsKey(actionId)) {
                        accountList = accounts.get(actionId);
                    } else {
                        accountList = new JSONArray();
                    }
                    accountList.put(accountRecord);
                    HashSet<String> accountSet = null;
                    if (accountMapSet.containsKey(actionId)) {
                        accountSet = accountMapSet.get(actionId);
                    } else {
                        accountSet = new HashSet<>();
                    }
                    accountSet.add(accountId);
                    accountMapSet.put(actionId, accountSet);
                } else {
                    accountList = new JSONArray();
                    JSONObject otherFeatureJSON = new JSONObject();
                    otherFeatureJSON.put("isEnabledValue", actionLimits.get(i).getIsAllowed());
                    accountList.put(otherFeatureJSON);
                }
                accounts.put(actionId, accountList);
            }
        }
        return accounts;
    }

    private Map<String, JSONObject> getFeaturesFromActionLimits(List<CustomRoleActionLimitDTO> actionLimits) {

        Map<String, JSONObject> features = new HashMap<>();
        Map<String, JSONObject> actions = new HashMap<>();
        int actionLimitsSize = actionLimits.size();

        for (int i = 0; i < actionLimitsSize; i++) {
            String actionId = actionLimits.get(i).getAction_id();
            actions.put(actionId, getFeatureRecord(actionLimits.get(i)));
        }

        for (String actionId : actions.keySet()) {
            JSONObject featureActionObject = actions.get(actionId);
            String featureId = featureActionObject.getString(FEATURE_ID_KEY);
            JSONObject action = null;
            JSONObject actionObject = new JSONObject();
            actionObject.put("actionId", featureActionObject.getString("actionId"));
            actionObject.put("actionType", featureActionObject.getString("actionType"));
            actionObject.put("actionName", featureActionObject.getString("actionName"));
            actionObject.put("actionDescription", featureActionObject.getString("actionDescription"));
            actionObject.put("isAccountLevel", featureActionObject.getString("isAccountLevel"));
            if (features.containsKey(featureId)) {
                action = features.get(featureId);
                JSONArray actionsArray = action.getJSONArray(ACTIONS_KEY);
                actionsArray.put(actionObject);
            } else {
                action = new JSONObject();
                action.put("featureName", featureActionObject.getString("featureName"));
                action.put("featureDescription", featureActionObject.get("featureDescription"));
                action.put(FEATURE_ID_KEY, featureActionObject.getString(FEATURE_ID_KEY));
                JSONArray actionArray = new JSONArray();
                actionArray.put(actionObject);
                action.put(ACTIONS_KEY, actionArray);
            }
            features.put(featureId, action);
        }
        return features;
    }

    private JSONObject getFeatureRecord(CustomRoleActionLimitDTO featureDTO) {

        JSONObject feature = new JSONObject();

        feature.put("actionId", featureDTO.getAction_id());
        feature.put("actionType", featureDTO.getActionType());
        feature.put("actionName", featureDTO.getActionName());
        feature.put("actionDescription", featureDTO.getActionDescription());
        feature.put("featureName", featureDTO.getFeatureName());
        feature.put("featureDescription", featureDTO.getFeatureDescription());
        feature.put(FEATURE_ID_KEY, featureDTO.getFeatureId());
        feature.put("isAccountLevel", featureDTO.getIsAccountLevel());
        return feature;
    }

    private JSONArray getAccountsFromActionLimits(List<CustomRoleActionLimitDTO> actionLimits, String accountIdKey) {

        Map<String, JSONObject> accounts = new HashMap<>();
        int actionLimitsSize = actionLimits.size();

        for (int i = 0; i < actionLimitsSize; i++) {
            String accountId = actionLimits.get(i).getAccount_id();
            if (accountId != null && !accounts.containsKey(accountId)) {
                JSONObject account = new JSONObject();
                String accountName = actionLimits.get(i).getAccountName();
                account.put(accountIdKey, accountId);
                account.put(ACCOUNT_NAME_KEY, accountName);
                accounts.put(accountId, account);
            }
        }

        JSONArray accountsArray = new JSONArray();
        for (String account : accounts.keySet()) {
            JSONObject accountObject = accounts.get(account);
            accountsArray.put(accountObject);
        }

        return accountsArray;
    }

    @Override
    public Result verifyCustomRoleName(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        Result result = null;
        try {
            // permission check for reading details of a custom role
            List<String> featureActionIdList = new ArrayList<>();
            featureActionIdList.add("CUSTOM_ROLES_VIEW");
            String featureActionId = CustomerSession.getPermittedActionIds(request, featureActionIdList);
            if (featureActionId == null) {
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String companyId = CustomerSession.getCompanyId(customer);

            Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
            String customRoleName = String.valueOf(inputParams.get("customRoleName"));
            if (customRoleName == null || customRoleName.equals("")) {
                return ErrorCodeEnum.ERR_21106.setErrorCode(new Result());
            }

            CustomRoleBusinessDelegate customRoleDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(CustomRoleBusinessDelegate.class);
            CustomRoleDTO customRole = customRoleDelegate.getCustomRole(customRoleName, companyId,
                    request.getHeaderMap());

            result = new Result();
            List<Param> params = new ArrayList<>();

            if (customRole != null) {
                params.add(new Param("isDuplicate", "true"));

            } else {
                params.add(new Param("isDuplicate", "false"));
            }
            result.addAllParams(params);
        } catch (Exception exp) {
            alert.prepareError("Exception occured while invoking resources of custom role details by id", exp).log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
        return result;
    }

}