package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;

public interface UpgradeProspectToRetailCustomerBusinessDelegate extends BusinessDelegate {

    JsonObject upgrade(String customerId, String urlToUpgrade, Map<String, ? extends Object> headerMap);

}
