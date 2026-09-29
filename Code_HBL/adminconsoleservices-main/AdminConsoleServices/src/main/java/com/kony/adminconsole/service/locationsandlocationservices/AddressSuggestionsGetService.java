package com.kony.adminconsole.service.locationsandlocationservices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class AddressSuggestionsGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result processedResult = new Result();
        String currLatitude = "";
        String currLongitude = "";
        String radius = "";
        Record locationRecord = null;
        Dataset locationDetailDataSet = new Dataset();
        JSONObject locationJSON = null;
        String query = "";

        try {

            if (StringUtils.isEmpty(requestInstance.getParameter("currLatitude"))) {
                return ErrorCodeEnum.ERR_20645.setErrorCode(processedResult);
            }
            if(!validateParam(requestInstance.getParameter("currLatitude"))) {
            	return ErrorCodeEnum.ERR_20662.setErrorCode(processedResult);
            }
            currLatitude = requestInstance.getParameter("currLatitude");

            if (StringUtils.isEmpty(requestInstance.getParameter("currLongitude"))) {
                return ErrorCodeEnum.ERR_20644.setErrorCode(processedResult);
            }
            if(!validateParam(requestInstance.getParameter("currLongitude"))) {
            	return ErrorCodeEnum.ERR_20662.setErrorCode(processedResult);
            }
            currLongitude = requestInstance.getParameter("currLongitude");

            if (StringUtils.isEmpty(requestInstance.getParameter("radius"))) {
                radius = "10000";
            } else {
                radius = requestInstance.getParameter("radius");
            }

            if (requestInstance.getParameter("query") != null) {
                query = requestInstance.getParameter("query");
                if (StringUtils.isBlank(query)) {
                    return ErrorCodeEnum.ERR_20647.setErrorCode(processedResult);
                }
            } else {
                return ErrorCodeEnum.ERR_20647.setErrorCode(processedResult);
            }

            JSONObject readEndpointResponse =
                    getLocationRange(currLatitude, currLongitude, radius, query, requestInstance);
            if (readEndpointResponse == null || !readEndpointResponse.has(FabricConstants.OPSTATUS)
                    || readEndpointResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                return ErrorCodeEnum.ERR_20372.setErrorCode(processedResult);
            }
            JSONArray locationRangeDetailJSONArray = (JSONArray) readEndpointResponse.get("records");
            locationDetailDataSet.setId("Locations");
            for (int i = 0; i < locationRangeDetailJSONArray.length(); i++) {
                locationJSON = locationRangeDetailJSONArray.getJSONObject(i);
                locationRecord = new Record();

                String addrLine1 = locationJSON.getString("addressLine1");
                String formattedAddress = addrLine1;
                if (locationJSON.has("addressLine2")) {
                    String addrLine2 = locationJSON.getString("addressLine2");
                    if (addrLine2 != null && StringUtils.isNotBlank(addrLine2)) {
                        formattedAddress = formattedAddress + ", " + addrLine2;
                    }
                }
                if (locationJSON.has("addressLine3")) {

                    String addrLine3 = locationJSON.getString("addressLine3");
                    if (addrLine3 != null && StringUtils.isNotBlank(addrLine3)) {
                        formattedAddress = formattedAddress + ", " + addrLine3;
                    }
                }

                if (locationJSON.has("city")) {
                    String city = locationJSON.getString("city");
                    if (city != null && StringUtils.isNotBlank(city)) {
                        formattedAddress = formattedAddress + ", " + city;
                    }
                }
                String region = locationJSON.getString("Region");
                if (region != null && StringUtils.isNotBlank(region)) {
                    formattedAddress = formattedAddress + ", " + region;
                }
                String country = locationJSON.getString("country");
                if (country != null && StringUtils.isNotBlank(country)) {
                    formattedAddress = formattedAddress + ", " + country;
                }

                String locationId = locationJSON.getString("locationId");

                locationRecord.addParam(new Param("formattedAddress", formattedAddress, FabricConstants.STRING));
                locationRecord.addParam(new Param("place_id", locationId, FabricConstants.STRING));
                locationDetailDataSet.addRecord(locationRecord);
            }
            processedResult.addDataset(locationDetailDataSet);
            return processedResult;
        } catch (Exception e) {
            alert.prepareError(" LocationRangeService Exception" + e).log();
            processedResult.addParam(new Param("exception", e.getMessage(), FabricConstants.STRING));
            return ErrorCodeEnum.ERR_20372.setErrorCode(processedResult);
        }
    }

    public static String modifyWorkingHours(String inputString) {
        ArrayList<String> daysList = new ArrayList<String>();
        daysList.add("Sunday");
        daysList.add("Monday");
        daysList.add("Tuesday");
        daysList.add("Wednesday");
        daysList.add("Thursday");
        daysList.add("Friday");
        daysList.add("Saturday");
        for (int i = 0; i < daysList.size(); i++) {
            String days = daysList.get(i);
            boolean isDaysExist = inputString.contains(days.toUpperCase()) || inputString.contains(days)
                    || inputString.contains(days.toLowerCase());
            if (!isDaysExist) {
                inputString = inputString + " || " + days + " : Closed";
            }
        }
        return inputString;
    }

    public JSONObject getLocationRange(String currLatitude, String currLongitude, String radius, String query,
            DataControllerRequest requestInstance) {
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put("_currLatitude", currLatitude);
        postParametersMap.put("_currLongitude", currLongitude);
        postParametersMap.put("_radius", radius);
        postParametersMap.put("_var", query);
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.LOCATION_ADDRESS_SUGGESTIONS_PROC_SERVICE,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
    }
    public boolean validateParam(String s) {
    	try {
    	Float f = Float.parseFloat(s);
    	return true;
    	}catch(NumberFormatException e) {
    		return false;
    	}
    }

}
