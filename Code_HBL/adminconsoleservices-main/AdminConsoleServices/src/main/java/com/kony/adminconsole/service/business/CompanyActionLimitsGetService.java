package com.kony.adminconsole.service.business;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
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

public class CompanyActionLimitsGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        try {
            if (requestInstance.getParameter("organization_id") == null) {
                ErrorCodeEnum.ERR_21011.setErrorCode(result);
                return result;
            } else {
                String Organization_id = requestInstance.getParameter("organization_id");
                svcStartTime = System.currentTimeMillis();
                JSONObject getCompanyActionLimitsresponse = DBPServices.getCompanyActionLimits(Organization_id,
                        requestInstance);
                svcEndTime = System.currentTimeMillis();
                if (getCompanyActionLimitsresponse == null
                        || !getCompanyActionLimitsresponse.has(FabricConstants.OPSTATUS)
                        || getCompanyActionLimitsresponse.getInt(FabricConstants.OPSTATUS) != 0
                        || !getCompanyActionLimitsresponse.has("FeatureActions")) {
                    ErrorCodeEnum.ERR_21016.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                }else if (getCompanyActionLimitsresponse.has("errmsg")) {
                    result.addParam(new Param("errMsg", getCompanyActionLimitsresponse.getString("errmsg"),
                            FabricConstants.STRING));
                    return result;

                } else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", getCompanyActionLimitsresponse.get("opstatus").toString(),
                            FabricConstants.STRING));

                    // Creating Dataset and adding to result
                    JSONArray readResponseJSONArray = getCompanyActionLimitsresponse.getJSONArray("FeatureActions");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("FeatureActions");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                            currRecord.addParam(new Param("featureName", currJSONObject.getString("featureName"),
                                    FabricConstants.STRING));
                            currRecord.addParam(new Param("featureDescription",
                                    currJSONObject.getString("featureDescription"), FabricConstants.STRING));
                            currRecord.addParam(new Param("featureId", currJSONObject.getString("featureId"),
                                    FabricConstants.STRING));
                            currRecord.addParam(new Param("featureStatus", currJSONObject.getString("featureStatus"),
                                    FabricConstants.STRING));
                            JSONArray actionsArray = currJSONObject.getJSONArray("Actions");
                            Dataset actions = new Dataset("Actions");
                            for (Object actionObject : actionsArray) {
                                Record actionRecord = new Record();
                                JSONObject actionJSON = (JSONObject) actionObject;
                                actionRecord.addParam(new Param("actionType", actionJSON.getString("actionType"),
                                        FabricConstants.STRING));
                                actionRecord.addParam(new Param("actionId", actionJSON.getString("actionId"),
                                        FabricConstants.STRING));
                                actionRecord.addParam(new Param("actionDescription",
                                        actionJSON.getString("actionDescription"), FabricConstants.STRING));
                                actionRecord.addParam(new Param("actionName", actionJSON.getString("actionName"),
                                        FabricConstants.STRING));
                                JSONArray limits = actionJSON.optJSONArray("Limits");
                                if (limits != null && limits.length() > 0) {
                                    Dataset actionLimits = new Dataset("limits");
                                    for (int k = 0; k < limits.length(); k++) {
                                        JSONObject limit = limits.getJSONObject(k);
                                        Record limitRecord = new Record();
                                        if (limit.length() != 0) {
                                            limitRecord.addParam(new Param("id",
                                                    limit.getString("id"), FabricConstants.STRING));
                                            limitRecord.addParam(new Param("value",
                                                    limit.getString("value"), FabricConstants.STRING));
                                        }
                                        actionLimits.addRecord(limitRecord);
                                    }

                                    actionRecord.addDataset(actionLimits);
                                }
                                actions.addRecord(actionRecord);

                            }
                            currRecord.addDataset(actions);
                            dataSet.addRecord(currRecord);
                        }
                    }
                    result.addDataset(dataSet);
                }
            }

        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get Company Action Limits", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        diagnostic.prepareDebug("MF Time get accounts send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();

        return result;
    }
}