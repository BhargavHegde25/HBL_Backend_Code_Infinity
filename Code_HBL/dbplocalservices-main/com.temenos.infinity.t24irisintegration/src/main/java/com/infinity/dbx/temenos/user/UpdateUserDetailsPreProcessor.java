package com.infinity.dbx.temenos.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.dbx.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

public class UpdateUserDetailsPreProcessor extends TemenosBasePreProcessor implements UserConstants, Constants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {

        if (request.containsKeyInRequest("source")) {
            if (request.getParameter("source").toString().contentEquals("UserManagement")) {
                String operation = request.getParameter("operation") != null ? request.getParameter("operation") : "";
                String detailToBeUpdated = request.getParameter("detailToBeUpdated") != null
                        ? request.getParameter("detailToBeUpdated")
                        : "";
//                Iterator<String> iterator = request.getParameterNames();
//                while (iterator.hasNext()) {
//                    String key = iterator.next();
//                        alert.prepareError("request param "+key+" "+ request.getParameter(key)).log();
//                }
                
                JSONArray customerDetailsArr = new JSONArray();
                JSONObject obj = new JSONObject();

                if (operation.contentEquals("Create")) {

                    obj.put("isPrimary", request.getParameter("isPrimary"));
                    obj.put("isAlertsRequired", request.getParameter("isAlertsRequired"));
                    obj.put("Extension", request.getParameter("Extension"));

                    if (detailToBeUpdated.contentEquals("phoneNumbers")) {
                        obj.put("phoneNumber", request.getParameter("phoneNumber"));
                        obj.put("phoneCountryCode", request.getParameter("phoneCountryCode"));

                    } else if (detailToBeUpdated.contentEquals("EmailIds")) {
                        obj.put("value", request.getParameter("value"));
                    }
                }

                else if (operation.contentEquals("Update")) {
                    obj.put("isPrimary", request.getParameter("isPrimary"));
                    obj.put("isAlertsRequired", request.getParameter("isAlertsRequired"));
                    obj.put("id", request.getParameter("communication_ID"));
                    obj.put("isTypeBusiness", request.getParameter("isTypeBusiness"));

                    if (detailToBeUpdated.contentEquals("phoneNumbers")) {
                        obj.put("phoneNumber", request.getParameter("phoneNumber"));
                        obj.put("phoneCountryCode", request.getParameter("phoneCountryCode"));
                        obj.put("Extension", request.getParameter("Extension"));
                    } else if (detailToBeUpdated.contentEquals("EmailIds")) {
                        obj.put("value", request.getParameter("value"));
                        if (request.getParameter("Extension") != null) {
                            obj.put("Extension", request.getParameter("Extension"));
                        }
                    }

                } else if (detailToBeUpdated.contentEquals("Addresses")) {
                    obj.put("Addr_type", request.getParameter("Addr_type"));
                    obj.put("addrLine1", request.getParameter("addrLine1"));
                    obj.put("addrLine2", request.getParameter("addrLine2"));
                    obj.put("City_id", request.getParameter("City_id"));
                    obj.put("ZipCode", request.getParameter("ZipCode"));
                    obj.put("Region_id", request.getParameter("Region_id"));
                    obj.put("countryCode", request.getParameter("countryCode"));
                    obj.put("isPrimary", request.getParameter("isPrimary"));

                    if (operation.contentEquals("UpdateAddress")) {
                        obj.put("Addr_id", request.getParameter("Addr_id"));
                    }

                }

                customerDetailsArr.put(obj);

                alert.prepareError("request from SRMS : "+obj).log();
                if (detailToBeUpdated.contentEquals("phoneNumbers")) {

                    if (params.containsKey("phoneNumbers")) {
                        params.put("phoneNumbers", customerDetailsArr.toString());
                    }

                } else if (detailToBeUpdated.contentEquals("EmailIds")) {
                    if (params.containsKey("emailIds")) {
                        params.put("emailIds", customerDetailsArr.toString());
                    }

                } else if (detailToBeUpdated.contentEquals("Addresses")) {
                    if (params.containsKey("addresses")) {
                        params.put("addresses", customerDetailsArr.toString());
                    }
                }
            }
        }

        Iterator<String> iterator = request.getParameterNames();

        if (StringUtils.isNotEmpty(request.getParameter(CORE_CUSTOMER_ID))
                || StringUtils.isNotEmpty(request.getParameter(PARTY_ID))) {
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            return Boolean.FALSE;
        }

        boolean isUpdated = false;
        Map<String, Object> userAttributes = CustomerSessionsUtil.getLoggedInUserAttributesMap(request);
        
        if (userAttributes != null && userAttributes.size() > 0) {
            request.addRequestParam_("coreIdentifier", (String) userAttributes.get("backendIdentifiers"));
            params.put("coreIdentifier", (String) userAttributes.get("backendIdentifiers"));
            request.addRequestParam_("loginUserId", (String) userAttributes.get("user_id"));
            params.put("loginUserId", (String) userAttributes.get("user_id"));
            request.addRequestParam_("companyId", (String) userAttributes.get("companyId"));
        }

        super.execute(params, request, response, result);

        if (Boolean.parseBoolean((String) userAttributes.get("isSuperAdmin"))
                && StringUtils.isNotBlank(request.getParameter("Customer_id"))) {
            iterator = request.getParameterNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if(StringUtils.isNotBlank((String) params.get(key))){
                    params.put(key, request.getParameter(key));
                }
            }
            String schema = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME", request);
            String filter = "id eq " + request.getParameter("Customer_id");
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(Constants.PARAM_DOLLAR_FILTER, filter);
            result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                    UserConstants.DB_SERVICE,
                    schema + UserConstants.OP_GET_USER);
            Dataset customerGetDs = result != null ? result.getDatasetById("customer") : null;
            if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
                filter = "BackendType eq T24 and Customer_id eq " + request.getParameter("Customer_id");
                map = new HashMap<String, Object>();
                map.put(Constants.PARAM_DOLLAR_FILTER, filter);
                result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                        UserConstants.DB_SERVICE,
                        schema + UserConstants.OP_GET_BACKENDIDENTIFIER);
                customerGetDs = result != null ? result.getDatasetById("backendidentifier") : null;
                if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
                    request.addRequestParam_("Customer_id",
                            customerGetDs.getAllRecords().get(0).getParamValueByName("BackendId"));
                    params.put("Customer_id", customerGetDs.getAllRecords().get(0).getParamValueByName("BackendId"));
                    request.getHeaderMap().put("companyId",
                            customerGetDs.getAllRecords().get(0).getParamValueByName("CompanyId"));
                }
            }else {
                filter = "BackendType eq T24 and BackendId eq " + request.getParameter("Customer_id");
                map = new HashMap<String, Object>();
                map.put(Constants.PARAM_DOLLAR_FILTER, filter);
                result = CommonUtils.invokeIntegrationServiceAndGetResult(request, map, request.getHeaderMap(),
                        UserConstants.DB_SERVICE,
                        schema + UserConstants.OP_GET_BACKENDIDENTIFIER);
                customerGetDs = result != null ? result.getDatasetById("backendidentifier") : null;
                if (customerGetDs != null && !customerGetDs.getAllRecords().isEmpty()) {
                    request.getHeaderMap().put("companyId",
                            customerGetDs.getAllRecords().get(0).getParamValueByName("CompanyId"));
                }
            }
            
            request.addRequestParam_("userID", request.getParameter("Customer_id"));
            params.put("userID", request.getParameter("Customer_id"));
        }
        
        String addresses = params.get(ADDRESSES) != null ? (String) params.get(ADDRESSES) : "";
        if (StringUtils.isBlank(addresses)) {
            addresses = params.get("Addresses") != null ? (String) params.get("Addresses") : "";
        }
        
        if (StringUtils.isNotBlank(addresses)) {
            JsonElement element = new JsonParser().parse(addresses);
            if (element.isJsonArray()) {
                params.put(ADDRESSES, element.getAsJsonArray().toString());
                for (JsonElement jsonElement : element.getAsJsonArray()) {
                    JsonObject addressJSON = jsonElement.getAsJsonObject();
                    if ("1".equals(addressJSON.get("isPrimary").getAsString())) {
                        updateAddress(params, request, addressJSON);
                        isUpdated = true;
                    }
                }
            }
        }

        String type = "";
        String emailIds = params.get(EMAILIDS) != null ? (String) params.get(EMAILIDS) : "";
        if (StringUtils.isBlank(emailIds)) {
            emailIds = params.get("EmailIds") != null ? (String) params.get("EmailIds") : "";
        }

        if (StringUtils.isNotBlank(emailIds)) {
            JsonElement element = new JsonParser().parse(emailIds);
            if (element.isJsonArray()) {
                for (JsonElement jsonElement : element.getAsJsonArray()) {
                    JsonObject emailJSON = jsonElement.getAsJsonObject();
                    String email = "";
                    if (emailJSON.has(VALUE) && "1".equals(emailJSON.get("isPrimary").getAsString()) && StringUtils.isNotBlank(emailJSON.get(VALUE).getAsString())) {
                        email = email + "{ \"emailId\": \"" + emailJSON.get(VALUE).getAsString() + "\" }";
                        params.put(KONY_DBX_USER_EMAIL, email);
                        isUpdated = true;
                    }
                }
            }
        }

        String phoneNumbers = params.get(PHONE_NUMBERS) != null ? (String) params.get(PHONE_NUMBERS) : "";
        if (StringUtils.isNotBlank(phoneNumbers)) {
            JsonElement element = new JsonParser().parse(phoneNumbers);
            if (element.isJsonArray()) {
                for (JsonElement jsonElement : element.getAsJsonArray()) {
                    JsonObject phoneJSON = jsonElement.getAsJsonObject();
                    if ("1".equals(phoneJSON.get("isPrimary").getAsString())) {
                        String phone = phoneJSON.has("phoneNumber") ? phoneJSON.get("phoneNumber").getAsString() : "";
                        String phoneCountryCode =
                                phoneJSON.has("phoneCountryCode") ? phoneJSON.get("phoneCountryCode").getAsString() : "";
    
                        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(phoneCountryCode) && !phone.contains("-")) {
                            phone = phoneCountryCode + "-" + phone;
                        }
                        
                        if(StringUtils.isNotBlank(phone)) {
                            phone = "{ \"phoneNumber\": \"" + phone + "\" }";
                            params.put(Constants.PARAM_PHONE, phone);
                            isUpdated = true;
                        }
                    }
                }
            }
        }
       HelperMethods.addMSJWTAuthHeader(request, request.getHeaderMap(), AuthConstants.PRE_LOGIN_FLOW );
        if (!isUpdated) {
            result.addOpstatusParam(0);
            result.addHttpStatusCodeParam(200);
            result.addStringParam("Status", "details are not modified");
            result.addStringParam("success", "failed");
//            alert.prepareError("params for update call not updated: "+params).log();
            return Boolean.FALSE;
        }
        
//        alert.prepareError("params for update call : "+params).log();
         return Boolean.TRUE;
    }

    private void updateAddress(HashMap params, DataControllerRequest request, JsonObject address) throws Exception {

        // Get state name from db
        String regionId = address.has(REGION_ID) ? address.get(REGION_ID).getAsString() : "";
        if (StringUtils.isNotBlank(regionId)) {
            try {
                HashMap<String, Object> inputParams = new HashMap<String, Object>();
                inputParams.put(PARAM_DOLLAR_FILTER, "id eq " + regionId);
                Result stateGet = CommonUtils.callIntegrationService(request, inputParams, null,
                        TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_STATES_GET, false);
                String countryId = stateGet.getDatasetById(DS_REGION).getRecord(0).getParamValueByName(COUNTRY_ID);
                // Get country name from db
                inputParams.put(PARAM_DOLLAR_FILTER, "id eq " + countryId);
                Result countryGet = CommonUtils.callIntegrationService(request, inputParams, null,
                        TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OP_COUNTRY_GET, false);
                String countryName = countryGet.getDatasetById(DS_COUNTRY).getRecord(0).getParamValueByName(NAME);
                params.put(COUNTRY, countryName);
            } catch (Exception e) {
               alert.prepareError(e.toString()).log();
            }
        } else if (address.has("countryCode")) {
            params.put(COUNTRY, address.get("countryCode").getAsString());
        }
        // Get city name from db
        
        String cityId = address.has(CITY_ID)? address.get(CITY_ID).getAsString() : null;
        
        if(StringUtils.isBlank(cityId)) {
            cityId = address.has("CityName")? address.get("CityName").getAsString() : null;
        }

        params.put(CITY, cityId);
        params.put(ADDRESS_LINE1, address.has(ADDRLINE1)? address.get(ADDRLINE1).getAsString() : null);
        params.put(ZIPCODE, address.has(DB_ZIPCODE)? address.get(DB_ZIPCODE).getAsString() : null);
        String addressType = address.has(ADDRESS_TYPE)? address.get(ADDRESS_TYPE).getAsString() : null;
        if (ADDRESS_TYPE_WORK.equalsIgnoreCase(addressType)) {
            params.put(ADDRESS, "Work");
        } else {
            params.put(ADDRESS, "Home");
        }

    }

    public String getT24UserIdFromBackendIdentifier(DataControllerRequest request, String customerId,
            String backendType,
            String identifier_name, String sequenceNumber) {

        String dbxId = "";
        try {

            HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
            HashMap<String, Object> svcParams = new HashMap<String, Object>();
            String filter = CommonUtils.buildOdataCondition(Constants.CUSTOMER_ID, Constants.EQUAL, customerId);
            filter = StringUtils.isNotBlank(sequenceNumber)
                    ? CommonUtils
                            .buildSearchGroupQuery(filter, Constants.AND,
                                    CommonUtils.buildOdataCondition(Constants.PARAM_SEQUENCENUMBER,
                                            Constants.EQUAL, sequenceNumber),
                                    false)
                    : filter;
            filter = StringUtils.isNotBlank(backendType)
                    ? CommonUtils.buildSearchGroupQuery(filter, Constants.AND,
                            CommonUtils.buildOdataCondition(Constants.BACKEND_TYPE, Constants.EQUAL, backendType),
                            false)
                    : filter;
            svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
            Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
                    Constants.DBX_DB_SERVICE_NAME, Constants.DBX_DB_BACKEND_IDENTIFIER_GET, false);
            Dataset backendIdentifiersDataset = result.getDatasetById(Constants.DS_BACKEND_IDENTIFIER);
            if (null != backendIdentifiersDataset) {

                List<Record> identifiers = result.getAllDatasets().get(0).getAllRecords();

                JSONObject json = new JSONObject();

                for (Record record : identifiers) {

                    backendType = record.getParamValueByName("BackendType");

                    Map<String, String> map = new HashMap<>();
                    map.put("sequence_number", String.valueOf(record.getParamValueByName("sequenceNumber")));
                    map.put("BackendId", record.getParamValueByName("BackendId"));
                    map.put("identifier_name", record.getParamValueByName("identifier_name"));

                    if (json.has(backendType)) {

                        JSONArray value = json.getJSONArray(backendType);
                        value.put(map);
                    } else {

                        JSONArray value = new JSONArray();
                        value.put(map);
                        json.put(backendType, value);
                    }
                }
                return String.valueOf(json);

            }

        } catch (Exception e) {
            alert.prepareError("Error while retrieving dbxId form backendIdentifier  " + e).log();
        }

        return dbxId;
    }
}
