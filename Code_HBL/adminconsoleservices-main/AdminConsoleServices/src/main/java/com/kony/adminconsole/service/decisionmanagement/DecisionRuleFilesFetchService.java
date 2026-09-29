package com.kony.adminconsole.service.decisionmanagement;

/*
* 
* @author Sai Krishna Aitha
*
*/
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DecisionRuleFilesFetchService implements JavaService2 {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        try {
            // Read Input
            String decisionId = requestInstance.getParameter("decisionId");
            String decisionName = requestInstance.getParameter("decisionName");
            // Validate Inputs
            if (StringUtils.isBlank(decisionName) && StringUtils.isBlank(decisionId)) {
                diagnostic.prepareDebug("Name and Id shouldn't be empty").log();
                ErrorCodeEnum.ERR_21581.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            }

            if (StringUtils.isBlank(decisionId)) {
                decisionId = StringUtils.EMPTY;
            }
            // Fetching Files of Decision
            JSONObject getAllFilesofDecisionRuleResponse = DBPServices.getAllFilesforDecisionRule(decisionId,
                    decisionName, requestInstance);

            if (getAllFilesofDecisionRuleResponse == null
                    || !getAllFilesofDecisionRuleResponse.has(FabricConstants.OPSTATUS)
                    || getAllFilesofDecisionRuleResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                diagnostic.prepareDebug("Failed to fetch Files of a decision:" + decisionName).log();
                ErrorCodeEnum.ERR_21577.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                return result;
            }

            JSONArray readResponseJSONArray = getAllFilesofDecisionRuleResponse.optJSONArray("rulesFileList");
            if (readResponseJSONArray == null) {
                diagnostic.prepareDebug("There are no files").log();
                ErrorCodeEnum.ERR_21582.setErrorCode(result);
                return result;
            }

            Dataset dataSet = new Dataset();
            dataSet.setId("rulesFileList");
            result.addDataset(dataSet);
            for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                Record currRecord = new Record();
                if (currJSONObject.length() != 0) {
                    for (String currKey : currJSONObject.keySet()) {
                        if (currJSONObject.has(currKey)) {
                            currRecord.addParam(
                                    new Param(currKey, currJSONObject.getString(currKey), FabricConstants.STRING));
                        }
                    }
                    dataSet.addRecord(currRecord);
                }
            }
            return result;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

}
