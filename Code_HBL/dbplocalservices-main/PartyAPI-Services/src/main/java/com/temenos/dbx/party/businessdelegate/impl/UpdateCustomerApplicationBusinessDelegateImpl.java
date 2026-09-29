package com.temenos.dbx.party.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.party.businessdelegate.api.UpdateCustomerApplicationBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerApplicationDTO;

public class UpdateCustomerApplicationBusinessDelegateImpl implements UpdateCustomerApplicationBusinessDelegate {

    @Override
    public JsonObject updateCustomerApplication(CustomerApplicationDTO dto, String urlToUpdateCustomerApplication,
            String urlToGetCustomerApplication, Map<String, ? extends Object> headerMap) {
        if (dto == null || StringUtils.isBlank(dto.getPartyId())) {
            JsonObject object = new JsonObject();
            object.addProperty(DBPUtilitiesConstants.VALIDATION_ERROR, "invalid inputParameters");
            return object;
        } else {
            String partyId = dto.getPartyId();
            HashMap<String, Object> customerApplicationMap = new HashMap<>();
            updateCustomerApplicationMap(dto, customerApplicationMap);
            return updateCustomerApplicationRecords(partyId, customerApplicationMap, urlToGetCustomerApplication,
                    urlToUpdateCustomerApplication, headerMap);

        }

    }

    private JsonObject updateCustomerApplicationRecords(String partyId, HashMap<String, Object> customerApplicationMap,
            String urlToGetCustomerApplication, String urlToUpdateCustomerApplication,
            Map<String, ? extends Object> headerMap) {
        HashMap<String, Object> input = new HashMap<>();
        String filterQuery = "Party_id" + DBPUtilitiesConstants.EQUAL + partyId;
        input.put(DBPUtilitiesConstants.FILTER, filterQuery);
        JsonObject response = new JsonObject();
        JsonObject customerApplicationRecords = ServiceCallHelper.invokeServiceAndGetJson(input,
                (Map<String, Object>) headerMap, urlToGetCustomerApplication);
        if (JSONUtil.hasKey(customerApplicationRecords, "customerapplication")
                && customerApplicationRecords.get("customerapplication").isJsonArray()) {
            for (JsonElement element : customerApplicationRecords.get("customerapplication").getAsJsonArray()) {
                if (JSONUtil.isJsonNotNull(element) && element.isJsonObject()
                        && JSONUtil.hasKey(element.getAsJsonObject(), "ApplicationId")) {
                    input = new HashMap<String, Object>();
                    input.put("ApplicationId", element.getAsJsonObject().get("ApplicationId").getAsString());
                    input.put("CoreCustomer_id", customerApplicationMap.get("CoreCustomer_id"));
                    input.put("Customer_id", customerApplicationMap.get("Customer_id"));
                    response = ServiceCallHelper.invokeServiceAndGetJson(input,
                            (Map<String, Object>) headerMap, urlToUpdateCustomerApplication);
                    if (JSONUtil.hasKey(response, DBPUtilitiesConstants.VALIDATION_ERROR)) {
                        break;
                    }
                }
            }
        }
        return response;

    }

    private void updateCustomerApplicationMap(CustomerApplicationDTO dto,
            HashMap<String, Object> customerApplicationMap) {
        customerApplicationMap.put("Customer_id", dto.getCustomerId());
        customerApplicationMap.put("CoreCustomer_id", dto.getCoreCustomerId());
        HelperMethods.removeNullValues(customerApplicationMap);

    }

}
