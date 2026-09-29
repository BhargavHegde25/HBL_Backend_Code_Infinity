package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.util.ServiceCallHelper;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CustomerPreferenceBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.CustomerPreferenceDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerPreferenceBusinessDelegateImpl implements CustomerPreferenceBusinessDelegate {

    @Override
    public DBXResult update(CustomerPreferenceDTO customerPreferenceDTO, Map<String, Object> headerMap) {

        DBXResult dbxResult = new DBXResult();

        if (customerPreferenceDTO.persist(DTOUtils.getParameterMap(customerPreferenceDTO, true), headerMap)) {
            dbxResult.setResponse(customerPreferenceDTO.getId());
        } else {
            dbxResult.setDbpErrMsg("Customer update Failed");
        }

        return dbxResult;
    }

    @Override
    public DBXResult get(CustomerPreferenceDTO customerPreferenceDTO, Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        dbxResult.setResponse(customerPreferenceDTO.loadDTO(customerPreferenceDTO.getCustomer_id()));

        return dbxResult;
    }

    @Override
    public DBXResult getPreferencesForLogin(CustomerPreferenceDTO customerPreferenceDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerPreferenceDTO.getCustomer_id();

        Map<String, Object> inputParams = new HashMap<>();

        inputParams.put(DBPUtilitiesConstants.FILTER, filter);

        JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMERPREFERENCE_GET);

        JsonObject responseJson = new JsonObject();
        if (response != null) {
            if (response.has(DBPUtilitiesConstants.DS_CUSTOMERPREFERENCE)
                    && !response.get(DBPUtilitiesConstants.DS_CUSTOMERPREFERENCE).isJsonNull()) {
                JsonObject jsonObject =
                        response.get(DBPUtilitiesConstants.DS_CUSTOMERPREFERENCE).getAsJsonArray().size() > 0
                                ? response.get(DBPUtilitiesConstants.DS_CUSTOMERPREFERENCE).getAsJsonArray().get(0)
                                        .getAsJsonObject()
                                : new JsonObject();
                for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {

                    if (mappings.get(entry.getKey()) != null) {
                        responseJson.add(mappings.get(entry.getKey()), entry.getValue());
                    }
                }
            }
        }

        response = new JsonObject();
        response.add("customerpreferences", responseJson);

        dbxResult.setResponse(response);

        return dbxResult;
    }

    @Override
    public DBXResult getPreferencesForUserResponse(CustomerPreferenceDTO customerPreferenceDTO,
            Map<String, Object> headerMap) {
        DBXResult dbxResult = new DBXResult();

        customerPreferenceDTO =
                (CustomerPreferenceDTO) customerPreferenceDTO.loadDTO(customerPreferenceDTO.getCustomer_id());

        if (customerPreferenceDTO != null) {

            String[] strings = { "DefaultAccountDeposit", "DefaultAccountBillPay", "DefaultAccountPayments",
                    "DefaultAccountCardless", "DefaultAccountTransfers", "DefaultModule_id",
                    "DefaultAccountWire", "DefaultFromAccountP2P", "DefaultToAccountP2P", "ShowBillPayFromAccPopup",
                    "DefaultFromAccountQR","DefaultAccountLoanPayment","DefaultAccountCardPayment","DefaultAccountCheckManagement",
                    "DefaultAccountDashboard", "DefaultAccountQRPayment", "DefaultAccountCheckDeposit", "DefaultAccountCashWithdrawl", "DefaultAccounteSewa"};

            Set<String> set = new HashSet<String>();

            set.addAll(Arrays.asList(strings));
            Map<String, String> map = DTOUtils.getResponseMap(customerPreferenceDTO, set, true);
            Map<String, String> responseMap = new HashMap<String, String>();
            for (Entry<String, String> entry : map.entrySet()) {
                responseMap.put(mappings.get(entry.getKey()), entry.getValue());
            }

            dbxResult.setResponse(responseMap);
            return dbxResult;
        }

        dbxResult.setDbpErrMsg("Unable to read Customer Preferences");

        return dbxResult;
    }

    static Map<String, String> mappings = null;

    static {
        if (mappings == null) {
            mappings = new HashMap<String, String>();
            mappings.put("DefaultAccountDeposit", "default_account_deposit");
            mappings.put("DefaultAccountBillPay", "default_account_billPay");
            mappings.put("DefaultAccountPayments", "default_account_payments");
            mappings.put("DefaultAccountCardless", "default_account_cardless");
            mappings.put("DefaultAccountTransfers", "default_account_transfers");
            mappings.put("DefaultModule_id", "DefaultModule_id");
            mappings.put("DefaultAccountWire", "default_account_wire");
            mappings.put("DefaultFromAccountP2P", "default_from_account_p2p");
            mappings.put("DefaultToAccountP2P", "default_to_account_p2p");
            mappings.put("ShowBillPayFromAccPopup", "showBillPayFromAccPopup");
            mappings.put("DefaultFromAccountQR", "default_from_account_qr");
            mappings.put("DefaultAccountLoanPayment", "default_account_loanpayment");
            mappings.put("DefaultAccountCardPayment", "default_account_cardpayment");
            mappings.put("DefaultAccountCheckManagement", "default_account_checkmanagement");
            mappings.put("DefaultAccountDashboard", "default_account_dashboard");
            mappings.put("DefaultAccountQRPayment", "default_account_qrpayment");
            mappings.put("DefaultAccountCheckDeposit", "default_account_checkdeposit");
            mappings.put("DefaultAccountCashWithdrawl", "default_account_cashwithdrawl");
            mappings.put("DefaultAccounteSewa", "default_account_esewa");
        }
    }

    @Override
    public DBXResult getPreferencesIdentityAttributes(CustomerDTO customerDTO,
            Map<String, Object> headerMap) throws ApplicationException {
        DBXResult result = new DBXResult();
        if (null == customerDTO || StringUtils.isBlank(customerDTO.getId())) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10338);

        }
        JSONObject addressRecord = new JSONObject();
        JSONObject customerPreferencesRecord = new JSONObject();
        JSONObject communicationRecord = new JSONObject();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("id", customerDTO.getId());

        JsonObject preferencesJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headerMap,
                URLConstants.CUSTOMER_PREFERENCES_CONCURRENT_ORCHESTRATION);

        if (JSONUtil.isJsonNotNull(preferencesJson) && JSONUtil.hasKey(preferencesJson, "address")
                && preferencesJson.get("address").isJsonArray()) {
            JsonArray addressArray = preferencesJson.get("address").getAsJsonArray();
            if (addressArray.size() > 0) {
                addressRecord = new JSONObject(addressArray.get(0).getAsJsonObject().toString());
            }
        }
        if (JSONUtil.isJsonNotNull(preferencesJson) && JSONUtil.hasKey(preferencesJson, "customerpreferences")
                && preferencesJson.get("customerpreferences").isJsonObject()) {
            customerPreferencesRecord =
                    new JSONObject(preferencesJson.get("customerpreferences").getAsJsonObject().toString());

        }
        if (JSONUtil.isJsonNotNull(preferencesJson) && JSONUtil.hasKey(preferencesJson, "customercommunication")
                && preferencesJson.get("customercommunication").isJsonArray()) {
            communicationRecord =
                    new JSONObject(preferencesJson.get("customercommunication").getAsJsonObject().toString());

        }
        JSONObject mergedJson = HelperMethods.mergeJSONObjects(addressRecord, customerPreferencesRecord);
        mergedJson = HelperMethods.mergeJSONObjects(mergedJson, communicationRecord);

        JsonParser parser = new JsonParser();
        result.setResponse(parser.parse(mergedJson.toString()).getAsJsonObject());

        return result;
    }

}
