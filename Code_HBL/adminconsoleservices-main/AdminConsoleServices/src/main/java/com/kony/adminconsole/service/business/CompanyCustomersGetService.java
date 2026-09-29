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

public class CompanyCustomersGetService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcStartTime = 0;
        long svcEndTime = 0;
        try {
            // Validate UserName
            if (requestInstance.getParameter("Organization_id") == null) {
                ErrorCodeEnum.ERR_21011.setErrorCode(result);
                return result;
            } else {
                String Organization_id = requestInstance.getParameter("Organization_id");
                svcStartTime = System.currentTimeMillis();
                JSONObject getCompanyCustomersresponse = DBPServices.getCompanyCustomers(Organization_id,
                        requestInstance);
                svcEndTime = System.currentTimeMillis();
                if (getCompanyCustomersresponse == null || !getCompanyCustomersresponse.has(FabricConstants.OPSTATUS)
                        || getCompanyCustomersresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                    ErrorCodeEnum.ERR_21017.setErrorCode(result);
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    return result;
                }else if (getCompanyCustomersresponse.has("dbpErrMsg")) {
    				result.addParam(new Param("errMsg", getCompanyCustomersresponse.getString("dbpErrMsg"),
    						FabricConstants.STRING));
    				return result;
			    }else {
                    result.addParam(new Param("status", "Success", FabricConstants.STRING));
                    result.addParam(new Param("opstatus", getCompanyCustomersresponse.get("opstatus").toString(),
                            FabricConstants.STRING));
                    // Creating Dataset and adding to result
                    JSONArray readResponseJSONArray = getCompanyCustomersresponse.getJSONArray("organizationEmployee");
                    Dataset dataSet = new Dataset();
                    dataSet.setId("organizationEmployee");
                    for (int indexVar = 0; indexVar < readResponseJSONArray.length(); indexVar++) {
                        JSONObject currJSONObject = readResponseJSONArray.getJSONObject(indexVar);
                        Record currRecord = new Record();
                        if (currJSONObject.length() != 0) {
                            for (String currKey : currJSONObject.keySet()) {
                                if (currJSONObject.has(currKey)) {
                                    currRecord.addParam(new Param(currKey, currJSONObject.getString(currKey),
                                            FabricConstants.STRING));
                                }
                            }
                            dataSet.addRecord(currRecord);
                        }
                    }
                    result.addDataset(dataSet);

                    // result.addParam(new
                    // Param("organizationEmployee",getCompanyCustomersresponse.get("organizationEmployee").toString(),
                    // FabricConstants.STRING));
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in get Company Customers", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time get customers send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}