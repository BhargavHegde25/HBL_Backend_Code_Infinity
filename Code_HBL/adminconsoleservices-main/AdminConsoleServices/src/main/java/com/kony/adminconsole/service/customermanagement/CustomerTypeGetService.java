package com.kony.adminconsole.service.customermanagement;

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

public class CustomerTypeGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            Result processedResult = new Result();
            Map<String, String> postParametersMap = new HashMap<String, String>();

            PaginationHandler.setOffset(requestInstance, postParametersMap);
			
            JSONObject readMemberGroupTypeResponseJSON = PaginationHandler
                    .getPaginatedData(ServiceURLEnum.MEMBERGROUPTYPE_READ, postParametersMap, null, requestInstance);

            if (readMemberGroupTypeResponseJSON != null && readMemberGroupTypeResponseJSON.has(FabricConstants.OPSTATUS)
                    && readMemberGroupTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readMemberGroupTypeResponseJSON.has("membergrouptype")) {
                diagnostic.prepareDebug("Fetch Customer Type Status:Successful").log();

                PaginationHandler.addPaginationMetadataToResultObject(processedResult, readMemberGroupTypeResponseJSON);
                JSONArray readMemberGroupTypeJSONArray = readMemberGroupTypeResponseJSON.getJSONArray("membergrouptype");
                Dataset memberGroupTypeDataSet = new Dataset();
                memberGroupTypeDataSet.setId("CustomerTypeRecords");
                JSONObject currMemberGroupTypeJSONObject;

                for (int indexVar = 0; indexVar < readMemberGroupTypeJSONArray.length(); indexVar++) {
                    currMemberGroupTypeJSONObject = readMemberGroupTypeJSONArray.getJSONObject(indexVar);
                    Record currRecord = new Record();
                    for (String currKey : currMemberGroupTypeJSONObject.keySet()) {
                        currRecord.addParam(new Param(currKey, currMemberGroupTypeJSONObject.optString(currKey),
                                FabricConstants.STRING));
                    }
                    memberGroupTypeDataSet.addRecord(currRecord);
                }
                processedResult.addDataset(memberGroupTypeDataSet);
                return processedResult;
            }
            alert.prepareError("Fetch Customer Type Status:Failed").log();
            ErrorCodeEnum.ERR_20404.setErrorCode(processedResult);
            return processedResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

}