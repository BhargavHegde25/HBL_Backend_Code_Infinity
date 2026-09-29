package com.temenos.infinity.api.cardless;

import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetQrCode implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        process(inputParams, dcRequest, result);
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void process(Map inputParams, DataControllerRequest dcRequest, Result result) {
        Dataset ds = new Dataset("transaction");
        Record rec = new Record();
        Param id = new Param("id", (String) inputParams.get("id"), DBPUtilitiesConstants.STRING_TYPE);
        rec.addParam(id);
        ds.addRecord(rec);
        result.addDataset(ds);
    }
}
