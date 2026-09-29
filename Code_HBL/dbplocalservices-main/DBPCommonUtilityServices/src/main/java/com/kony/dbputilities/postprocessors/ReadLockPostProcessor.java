package com.kony.dbputilities.postprocessors;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ReadLockPostProcessor {

    public Object execute(Result result, DataControllerRequest dcRequest) throws Exception {
		Log4j2Configurator.getInstance();
        if (DBPUtilitiesConstants.ZERO.equals(result.getParamByName(DBPConstants.FABRIC_OPSTATUS_KEY).getValue())) {
            List<Record> recordList = result.getAllDatasets().get(0).getAllRecords();
            if (null != recordList) {
                dcRequest.addRequestParam_(DBPUtilitiesConstants.RECORD_COUNT, String.valueOf(recordList.size()));
            }
        }
        return result;
    }
}