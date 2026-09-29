package com.temenos.dbx.product.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.product.businessdelegate.api.UpgradeProspectToRetailCustomerBusinessDelegate;

public class UpgradeProspectToRetailCustomerBusinessDelegateImpl
        implements UpgradeProspectToRetailCustomerBusinessDelegate {

    @Override
    public JsonObject upgrade(String customerId, String urlToUpgrade, Map<String, ? extends Object> headerMap) {

        return upgradeProspectToRetail(customerId, urlToUpgrade, headerMap);

    }

    private JsonObject upgradeProspectToRetail(String customerId, String urlToUpgrade,
            Map<String, ? extends Object> headerMap) {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("id", customerId);
        inputMap.put("CustomerType_id", "TYPE_ID_RETAIL");

        return ServiceCallHelper.invokeServiceAndGetJson(inputMap, (Map<String, Object>) headerMap,
                urlToUpgrade);

    }

}
