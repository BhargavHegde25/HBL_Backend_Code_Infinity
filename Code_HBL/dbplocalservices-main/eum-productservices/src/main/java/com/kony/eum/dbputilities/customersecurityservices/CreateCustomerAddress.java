package com.kony.eum.dbputilities.customersecurityservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.eum.dbputilities.customersecurityservices.CreateCustomerAddress;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateCustomerAddress {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public static void invoke(Map<String, String> inputParams, DataControllerRequest dcRequest) {

        HashMap<String, String> hashMap = (HashMap<String, String>) inputParams;
        String customerId = hashMap.get("id");
        hashMap.put("id", UUID.randomUUID().toString());
        if (StringUtils.isBlank(hashMap.get("zipCode"))) {
            hashMap.put("zipCode", hashMap.get("zipcode"));
        }
        if (StringUtils.isBlank(hashMap.get("City_id"))) {
            hashMap.put("City_id", getCityID(hashMap.get("city"), dcRequest));
        }
        if(StringUtils.isBlank(hashMap.get("cityName"))) {
			hashMap.put("cityName", getCityID(hashMap.get("CityName"), dcRequest));
		}
        if (StringUtils.isBlank(hashMap.get("Region_id"))) {
            hashMap.put("Region_id", getRegionID(hashMap.get("state"), hashMap.get("City_id"), dcRequest));
        }
        if (StringUtils.isBlank(hashMap.get("country"))) {
            hashMap.put("country",
                    getCountryID(hashMap.get("country"), hashMap.get("City_id"), hashMap.get("Region_id"), dcRequest));
        }
        hashMap.put("isPreferredAddress", "1");
        hashMap.put("type", "home");
        SimpleDateFormat idformatter = new SimpleDateFormat("yyMMddHHmmssSSS");
        hashMap.put("id", idformatter.format(new Date()));
        hashMap.remove("uspsValidationStatus");
        Result result1 = new Result();
        try {
            result1 = HelperMethods.callApi(dcRequest, hashMap, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ADDRESS_CREATE);
        } catch (HttpCallException e) {
            //
            alert.prepareError(e.getMessage()).log();
        }
        Map<String, String> input = new HashMap<>();
        input.put("Customer_id", customerId);
        input.put("Address_id", HelperMethods.getFieldValue(result1, "id"));
        input.put("Type_id", "ADR_TYPE_HOME");
        input.put("isPrimary", "1");
        try {
            HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMER_ADDRESS_CREATE);
        } catch (HttpCallException e) {
            //
            alert.prepareError(e.getMessage()).log();
        }
    }

    private static String getCountryID(String string, String city, String state, DataControllerRequest dcRequest) {

        if (StringUtils.isNotBlank(string)) {

            String filter = "Name eq " + string + DBPUtilitiesConstants.OR + "Code" + DBPUtilitiesConstants.EQUAL
                    + string;

            Result result = new Result();
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.COUNTRY_GET);
            } catch (HttpCallException e) {
                //
                alert.prepareError(e.getMessage()).log();
            }

            if (HelperMethods.hasRecords(result)) {
                return HelperMethods.getFieldValue(result, "id");
            }

            filter = "";
            if (StringUtils.isNotBlank(city)) {
                filter = "id eq " + city;
                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CITY_GET);
                } catch (HttpCallException e) {
                    //
                    alert.prepareError(e.getMessage()).log();
                }
                if (HelperMethods.hasRecords(result)) {
                    return HelperMethods.getFieldValue(result, "Country_id");
                }
            }

            filter = "";
            if (StringUtils.isNotBlank(state)) {
                filter = "id eq " + state;
                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                            URLConstants.STATE_GET);
                } catch (HttpCallException e) {
                    //
                    alert.prepareError(e.getMessage()).log();
                }
                if (HelperMethods.hasRecords(result)) {
                    return HelperMethods.getFieldValue(result, "Country_id");
                }
            }
        }

        return "";
    }

    private static String getRegionID(String string, String cityId, DataControllerRequest dcRequest) {

        if (StringUtils.isNotBlank(string)) {

            String filter = "Name eq " + string + DBPUtilitiesConstants.OR + "Code" + DBPUtilitiesConstants.EQUAL
                    + string;

            Result result = new Result();
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.REGION_GET);
            } catch (HttpCallException e) {
                //
                alert.prepareError(e.getMessage()).log();
            }

            if (HelperMethods.hasRecords(result)) {
                return HelperMethods.getFieldValue(result, "id");
            }
            if (StringUtils.isNotBlank(cityId)) {
                filter = "id eq " + cityId;
                try {
                    result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                            URLConstants.CITY_GET);
                } catch (HttpCallException e) {
                    //
                    alert.prepareError(e.getMessage()).log();
                }

                if (HelperMethods.hasRecords(result)) {
                    return HelperMethods.getFieldValue(result, "Region_id");
                }
            }
        }
        return "";
    }

    private static String getCityID(String string, DataControllerRequest dcRequest) {

        if (StringUtils.isNotBlank(string)) {

            String filter = "Name eq " + string;

            Result result = new Result();
            try {
                result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CITY_GET);
            } catch (HttpCallException e) {
                //
                alert.prepareError(e.getMessage()).log();
            }

            if (HelperMethods.hasRecords(result)) {
                return HelperMethods.getFieldValue(result, "id");
            }
        }
        return "";
    }

}