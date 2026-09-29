package com.temenos.dbx.product.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.temenos.dbx.product.businessdelegate.api.UpdateProspectBusinessDelegate;
import com.temenos.dbx.product.dto.UpdateProspectDTO;

public class UpdateProspectBusinessDelegateImpl implements UpdateProspectBusinessDelegate {

    @Override
    public JsonObject updateProspect(UpdateProspectDTO updateProspectDTO, String customerId,
            String urlToUpdatePersonalInfo,
            String urlToUpdateCommInfo, String urlTogetCommInfo, Map<String, ? extends Object> headerMap) {
        HashMap<String, Object> personalInfoMap = new HashMap<>();
        HashMap<String, Object> contactInfoMap = new HashMap<>();
        HelperMethods.removeNullValues(headerMap);
        updatePersonalInfoMap(updateProspectDTO, personalInfoMap);
        updateContactInfoMap(updateProspectDTO, contactInfoMap);
        JsonObject personalInfoResult =
                updatePersonalInformation(customerId, personalInfoMap, urlToUpdatePersonalInfo, headerMap);
        JsonObject commInfoResult = updateCommunicationInformation(customerId, contactInfoMap, urlToUpdateCommInfo,
                urlTogetCommInfo, headerMap);
        if (JSONUtil.hasKey(personalInfoResult, DBPUtilitiesConstants.VALIDATION_ERROR)) {
            return personalInfoResult;
        }

        return commInfoResult;
    }

    private void updateContactInfoMap(UpdateProspectDTO updateProspectDTO, HashMap<String, Object> contactInfoMap) {
        contactInfoMap.put("Phone", updateProspectDTO.getPhone());
        contactInfoMap.put("Email", updateProspectDTO.getEmail());
    }

    private void updatePersonalInfoMap(UpdateProspectDTO updateProspectDTO, HashMap<String, Object> personalInfoMap) {
        personalInfoMap.put("FirstName", updateProspectDTO.getFirstName());
        personalInfoMap.put("LastName", updateProspectDTO.getLastName());
        personalInfoMap.put("DateOfBirth", updateProspectDTO.getDateOfBirth());
    }

    private JsonObject updateCommunicationInformation(String customerId, HashMap<String, Object> contactInfoMap,
            String urlToUpdateCommInfo, String urlTogetCommInfo, Map<String, ? extends Object> headerMap) {
        String filterQuery = "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
        HashMap<String, Object> input = new HashMap<>();
        input.put(DBPUtilitiesConstants.FILTER, filterQuery);
        JsonObject response = new JsonObject();
        JsonObject commRecords =
                ServiceCallHelper.invokeServiceAndGetJson(input, (Map<String, Object>) headerMap, urlTogetCommInfo);
        if (JSONUtil.hasKey(commRecords, "customercommunication")
                && commRecords.get("customercommunication").isJsonArray()) {
            for (JsonElement element : commRecords.get("customercommunication").getAsJsonArray()) {
                if (JSONUtil.isJsonNotNull(element) && element.isJsonObject()
                        && JSONUtil.hasKey(element.getAsJsonObject(), "Type_id")) {
                    JsonObject record = element.getAsJsonObject();

                    if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equals(record.get("Type_id").getAsString())) {
                        String id = record.get("id").getAsString();
                        HashMap<String, Object> updateParams = new HashMap<>();
                        updateParams.put("Value", contactInfoMap.get("Phone"));
                        updateParams.put("id", id);
                        response =
                                ServiceCallHelper.invokeServiceAndGetJson(updateParams, (Map<String, Object>) headerMap,
                                        urlToUpdateCommInfo);
                        if (JSONUtil.hasKey(response, DBPUtilitiesConstants.VALIDATION_ERROR)) {
                            break;
                        }
                    }

                    if (DBPUtilitiesConstants.COMM_TYPE_EMAIL.equals(record.get("Type_id").getAsString())) {
                        String id = record.get("id").getAsString();
                        HashMap<String, Object> updateParams = new HashMap<>();
                        updateParams.put("Value", contactInfoMap.get("Email"));
                        updateParams.put("id", id);
                        response =
                                ServiceCallHelper.invokeServiceAndGetJson(updateParams, (Map<String, Object>) headerMap,
                                        urlToUpdateCommInfo);
                        if (JSONUtil.hasKey(response, DBPUtilitiesConstants.VALIDATION_ERROR)) {
                            break;
                        }
                    }

                }

            }

        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private JsonObject updatePersonalInformation(String customerId, Map<String, Object> personalInfoMap,
            String urlToUpdatePersonalInfo, Map<String, ? extends Object> headerMap) {
        personalInfoMap.put("id", customerId);
        return ServiceCallHelper.invokeServiceAndGetJson(personalInfoMap, (Map<String, Object>) headerMap,
                urlToUpdatePersonalInfo);
    }
}
