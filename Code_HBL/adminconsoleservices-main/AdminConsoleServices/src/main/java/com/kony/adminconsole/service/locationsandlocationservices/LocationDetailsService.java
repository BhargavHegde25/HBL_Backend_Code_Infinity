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
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class LocationDetailsService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {


        String locationID = "";
        Result processedResult = new Result();
        diagnostic.prepareInfo("inside LocationDetailsService:").log();
        try {

            if (requestInstance.getParameter("placeID") != null) {
                locationID = requestInstance.getParameter("placeID");
                diagnostic.prepareInfo("inside LocationDetailsService locationID:" + locationID).log();
                if (StringUtils.isBlank(locationID)) {
                    return ErrorCodeEnum.ERR_20648.setErrorCode(processedResult);
                }
            } else {
                return ErrorCodeEnum.ERR_20648.setErrorCode(processedResult);

            }


            JSONObject readEndpointResponse = getLocationDetails( locationID, requestInstance);
            if (readEndpointResponse == null || !readEndpointResponse.has(FabricConstants.OPSTATUS)
                    || readEndpointResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                return ErrorCodeEnum.ERR_20371.setErrorCode(processedResult);
            }
            JSONArray locationDetailJSONArray = (JSONArray) readEndpointResponse.get("locationdetails_view");

            JSONObject locationJSON = null;

            Dataset locationDetailDataSet = new Dataset();
            locationDetailDataSet.setId("PlaceDetails");
            String key = "";
            String value = "";
            Record locationRecord;
            Param currValParam = null;
            for (int i=0 ; i<locationDetailJSONArray.length();i++) {
            locationJSON = locationDetailJSONArray.getJSONObject(i);
            if(locationID.equalsIgnoreCase(locationJSON.getString("locationId"))) {
            locationRecord = new Record();
            for (String currKey : locationJSON.keySet()) {
                key = currKey;
                value = locationJSON.getString(currKey);
                if (key.equalsIgnoreCase("workinghours")) {
                    value = modifyWorkingHours(locationJSON.getString(currKey),requestInstance);
                }
                currValParam = new Param(key, value, FabricConstants.STRING);
                locationRecord.addParam(currValParam);
            }
            locationDetailDataSet.addRecord(locationRecord);
            }
            }
            processedResult = new Result();
            processedResult.addDataset(locationDetailDataSet);
            return processedResult;

        } catch (Exception e) {
            alert.prepareError(" LocationDetailsService Exception" + e).log();
            return ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
        }

    }

    public static String modifyWorkingHours(String inputString, DataControllerRequest requestInstance) throws AppRegistryException {
        ArrayList<String> daysList = new ArrayList<String>();
        daysList.add("Sunday");
        daysList.add("Monday");
        daysList.add("Tuesday");
        daysList.add("Wednesday");
        daysList.add("Thursday");
        daysList.add("Friday");
        daysList.add("Saturday");
        
        ServicesManager sm = requestInstance.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String FridayStartTime = paramHelper.getServerProperty("FRIDAY_START_TIME");
		String FridayEndTime = paramHelper.getServerProperty("FRIDAY_END_TIME");

        String FridayVal = "Friday:"+FridayStartTime+"-"+FridayEndTime;
        inputString = FridayVal + inputString.substring(18);
        for (int i = 0; i < daysList.size(); i++) {
            String days = daysList.get(i);
            diagnostic.prepareInfo("day" + days).log();
            diagnostic.prepareInfo("day inputString:" + inputString).log();
            boolean isDaysExist = inputString.contains(days.toUpperCase()) || inputString.contains(days)
                    || inputString.contains(days.toLowerCase());

            if (!isDaysExist) {
                inputString = inputString + " || " + days + " : Closed";
            }
        }
        return inputString;
    }

    public JSONObject getLocationDetails(String locationId, DataControllerRequest requestInstance) {
        Map<String, String> postParametersMap = new HashMap<String, String>();
        postParametersMap.put("_locationId", locationId);
        String readEndpointResponse = Executor.invokeService(ServiceURLEnum.LOCATIONDETAILS_VIEW_READ,
                postParametersMap, null, requestInstance);
        return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
    }

}