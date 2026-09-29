package com.kony.integrationpreprocessors;

import java.util.ArrayList;
import java.util.List;

import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CrudLayerPostProcessor implements DataPostProcessor2 {

    @Override
    public Object execute(Result result, DataControllerRequest arg1, DataControllerResponse arg2) throws Exception {

        // String[] strings = {"UserName", "Ssn", "DrivingLicenseNumber"};
        String[] strings = { "Ssn", "DrivingLicenseNumber" };

        Dataset dataset = new Dataset();
        List<Record> records = new ArrayList<>();
        if (HelperMethods.hasRecords(result)) {
            dataset = result.getAllDatasets().get(0);
            records = dataset.getAllRecords();
            for (Record record : records) {
                for (String key : strings) {
                    Param p = record.getParamByName(key);
                    if (p != null) {
                        String value = CryptoText.decrypt(p.getValue());
                        record.removeParamByName(key);
                        p.setValue(value);
                        record.addParam(p);
                    }
                }
            }
        }

        return result;
    }
}
