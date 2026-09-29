package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerActionsBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerActionDTO;
import com.temenos.dbx.product.dto.CustomerActionsProcDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;

public class CustomerActionsBusinessDelegateImpl implements CustomerActionsBusinessDelegate {
	
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    final static private String CONTRACT_ID_DB = "contractId";

    @Override
    public List<CustomerActionsProcDTO> getCustomerActions(CustomerActionsProcDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {
        List<CustomerActionsProcDTO> dtoList = new ArrayList<>();
        try {
            if (null == dto || StringUtils.isBlank(dto.getCustomerId())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10340);
            }
            Map<String, Object> inputParams = new HashMap<>();
            inputParams.put("_customerId", dto.getCustomerId());
            if (StringUtils.isNotBlank(dto.getActionId())) {
                inputParams.put("_actionId", dto.getActionId());
            } else {
                inputParams.put("_actionId", "");
            }

            JsonObject customerActionsProcJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMER_ACTIONS_PROC);

            if (JSONUtil.isJsonNotNull(customerActionsProcJson)
                    && JSONUtil.hasKey(customerActionsProcJson, DBPDatasetConstants.DATASET_RECORDS)
                    && customerActionsProcJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()) {

                JsonArray actionArray =
                        customerActionsProcJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
                for (JsonElement element : actionArray) {
                    if (element.isJsonObject()) {
                        CustomerActionsProcDTO customerActionsProcDTO =
                                (CustomerActionsProcDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                                        CustomerActionsProcDTO.class, true);

                        dtoList.add(customerActionsProcDTO);

                    }

                }
            }

        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10341);
        }

        return dtoList;
    }

    @Override
    public void createCustomerActions(String userId, String contractId, String coreCustomerId, String legalEntityId, String groupId,
            Set<String> accounts, Map<String, Object> headersMap) throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();
        StringBuilder accountsCSV = new StringBuilder();

        for (String account : accounts) {
            accountsCSV.append(account).append(DBPUtilitiesConstants.COMMA_SEPERATOR);
        }
        if (accountsCSV.length() > 0)
            accountsCSV.replace(accountsCSV.length() - 1, accountsCSV.length(), "");

        try {
            inputParams.put("_userId", userId);
            inputParams.put("_accountsCSV", accountsCSV);
            inputParams.put("_coreCustomerId", coreCustomerId);
            inputParams.put("_contractId", contractId);
            inputParams.put("_groupId", groupId);
            inputParams.put("_legalEntityId", legalEntityId);
            if (groupId.equals("GROUP_AUTHORIZER") || groupId.equals("GROUP_CREATOR")) {
            	ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.USERACTIONS_RELATIVE_CREATE_PROC);
            } else {
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.USERACTIONS_CREATE_PROC);
            }            
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10388);
        }
    }

    @Override
    public Map<String, Set<String>> getSecurityAttributes(String customerId, Map<String, Object> headersMap)
            throws ApplicationException {
        Map<String, Set<String>> securityAttributesMap = new HashMap<>();
        final String DATASET_RECORDS1 = "records1";
        final String ACTIONS = "actions";
        final String FEATURES = "features";

        Map<String, Object> inputParams = new HashMap<>();
        try {
            inputParams.put("_userId", customerId);

            JsonObject securityAttributesJson = new JsonObject();
            
            if((boolean) headersMap.get(InfinityConstants.isProspectFlow)) {
            	securityAttributesJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.PROSPECT_SECURITYATTRIBUTES_GET_PROC);
            }
            else {
            	securityAttributesJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.USER_SECURITYATTRIBUTES_GET_PROC);
            }
            if (null != securityAttributesJson
                    && JSONUtil.hasKey(securityAttributesJson, DBPDatasetConstants.DATASET_RECORDS)
                    && securityAttributesJson.get(DBPDatasetConstants.DATASET_RECORDS).isJsonArray()
                    && JSONUtil.hasKey(securityAttributesJson, DATASET_RECORDS1)
                    && securityAttributesJson.get(DATASET_RECORDS1).isJsonArray()) {
                JsonArray actionsArray =
                        securityAttributesJson.get(DBPDatasetConstants.DATASET_RECORDS).getAsJsonArray();
                JsonArray featuresArray = securityAttributesJson.get(DATASET_RECORDS1).getAsJsonArray();

                JsonElement actionsElement =
                        actionsArray.size() > 0 ? actionsArray.get(0) : new JsonObject();
                JsonElement featuresElement =
                        featuresArray.size() > 0 ? featuresArray.get(0) : new JsonObject();
                if (actionsElement.isJsonObject()) {
                    JsonObject object = actionsElement.getAsJsonObject();
                    if (null != object && JSONUtil.hasKey(object, ACTIONS)) {

                        securityAttributesMap.put(ACTIONS,
                                HelperMethods.splitString(object.get(ACTIONS).getAsString(),
                                        DBPUtilitiesConstants.COMMA_SEPERATOR));
                    }
                }

                if (featuresElement.isJsonObject()) {
                    JsonObject object = featuresElement.getAsJsonObject();
                    if (null != object && JSONUtil.hasKey(object, FEATURES)) {

                        securityAttributesMap.put(FEATURES,
                                HelperMethods.splitString(object.get(FEATURES).getAsString(),
                                        DBPUtilitiesConstants.COMMA_SEPERATOR));
                    }
                }
            }

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10391);
        }
        return securityAttributesMap;
    }

    @Override
    public void createCustomerLimitGroupLimits(String userId, String contractId, String coreCustomerId, String legalEntityId,
            Map<String, Object> headersMap) throws ApplicationException {
        Map<String, Object> inputParams = new HashMap<>();

        try {
            inputParams.put("_userId", userId);
            inputParams.put("_coreCustomerId", coreCustomerId);
            inputParams.put("_contractId", contractId);
            inputParams.put("_legalEntityId", legalEntityId);
            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.USER_LIMITGROUP_LIMITS_CREATE_PROC);

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10402);
        }

    }

    @Override
    public List<CustomerActionDTO> getCustomerActions(CustomerActionDTO dto, Map<String, Object> headersMap)
            throws ApplicationException {

        List<CustomerActionDTO> resultCustomerActions = new ArrayList<>();
        try {
            Map<String, Object> inputParams = new HashMap<>();

            inputParams.put(DBPUtilitiesConstants.FILTER,
                    "Customer_id" + DBPUtilitiesConstants.EQUAL + dto.getCustomerId() +
                    DBPUtilitiesConstants.AND + "companyLegalUnit" + DBPUtilitiesConstants.EQUAL +
                    dto.getCompanyLegalUnit());

            JsonObject customerActionsJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CUSTOMER_ACTION_LIMITS_GET);

            if (JSONUtil.isJsonNotNull(customerActionsJson)
                    && JSONUtil.hasKey(customerActionsJson, DBPDatasetConstants.DATASET_CUSTOMERACTION)
                    && customerActionsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACTION).isJsonArray()) {

                JsonArray actionArray =
                        customerActionsJson.get(DBPDatasetConstants.DATASET_CUSTOMERACTION).getAsJsonArray();
                for (JsonElement element : actionArray) {
                    if (element.isJsonObject()) {
                        CustomerActionDTO customerActionsDTO =
                                (CustomerActionDTO) DTOUtils.loadJsonObjectIntoObject(element.getAsJsonObject(),
                                        CustomerActionDTO.class, true);

                        resultCustomerActions.add(customerActionsDTO);

                    }

                }

            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10341);
        }
        return resultCustomerActions;
    }

    public void createAccountDefaultCustomerActions(String queryInput, String customerId,
            Map<String, Object> headersMap) throws ApplicationException {

        Map<String, Object> inputParams = new HashMap<>();
        try {
            inputParams.put("_queryInput", queryInput);
            inputParams.put("_customerId", customerId);

            ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                    URLConstants.CREATE_DEFAULT_AUTOSYNC_ACCOUNTS);

        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10388);
        }

    }
}
