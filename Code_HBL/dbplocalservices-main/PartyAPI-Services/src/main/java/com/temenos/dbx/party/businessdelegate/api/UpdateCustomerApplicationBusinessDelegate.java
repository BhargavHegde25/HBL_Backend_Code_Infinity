package com.temenos.dbx.party.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.dto.CustomerApplicationDTO;

public interface UpdateCustomerApplicationBusinessDelegate extends BusinessDelegate {

    JsonObject updateCustomerApplication(CustomerApplicationDTO dto, String urlToUpdateCustomerApplication,
            String urlToGetCustomerApplication, Map<String, ? extends Object> headerMap);

}
