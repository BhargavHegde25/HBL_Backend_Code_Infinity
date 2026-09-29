package com.kony.adminconsole.service.locationsandlocationservices;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.PaginationHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to Fetch Locations
 * 
 * @author Aditya Mankal
 * 
 */
public class LocationGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            Result processedResult = new Result();
            // Empty Input Map - No Inputs from client
            Map<String, String> inputMap = new HashMap<>();
            PaginationHandler.setOffset(requestInstance, inputMap);

            // Fetch Records
            JSONObject readlocationResponseJSON = PaginationHandler.getPaginatedData(ServiceURLEnum.LOCATION_READ,
                    inputMap, null, requestInstance);

            if (readlocationResponseJSON != null && readlocationResponseJSON.has(FabricConstants.OPSTATUS)
                    && readlocationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readlocationResponseJSON.has("location")) {

                diagnostic.prepareDebug("Fetch Location Status:Successful").log();

                // Add Pagination Meta
                PaginationHandler.addPaginationMetadataToResultObject(processedResult, readlocationResponseJSON);
                JSONArray readlocationJSONArray = readlocationResponseJSON.getJSONArray("location");
            
                Dataset locationDataSet = new Dataset();
                locationDataSet.setId("data");

                // Traverse fetched Records to construct Response Dataset
                for (int indexVar = 0; indexVar < readlocationJSONArray.length(); indexVar++) {
                    Record currRecord = new Record();
                    JSONObject currLocationJSONObject = readlocationJSONArray.getJSONObject(indexVar);
                    for (String currKey : currLocationJSONObject.keySet()) {
                        Param currValParam = new Param(currKey, currLocationJSONObject.optString(currKey),
                                FabricConstants.STRING);
                        currRecord.addParam(currValParam);
                    }
                    locationDataSet.addRecord(currRecord);
                }

                // Add constructed Dataset to Result instance
                processedResult.addDataset(locationDataSet);
                return processedResult;

            } else {
                alert.prepareError("Fetch Location Status:Failed. Response:" + readlocationResponseJSON).log();
                ErrorCodeEnum.ERR_20365.setErrorCode(processedResult);
                return processedResult;
            }
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }

    }

}