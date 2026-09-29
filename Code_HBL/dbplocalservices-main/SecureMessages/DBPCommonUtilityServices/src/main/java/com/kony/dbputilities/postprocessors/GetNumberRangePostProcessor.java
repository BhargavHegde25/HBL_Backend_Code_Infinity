package com.kony.dbputilities.postprocessors;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetNumberRangePostProcessor {

    public Object execute(Result result, DataControllerRequest dcReq) {
		Log4j2Configurator.getInstance();

        if (DBPUtilitiesConstants.ZERO.equals(result.getParamByName(DBPConstants.FABRIC_OPSTATUS_KEY).getValue())) {
            List<Record> recordList = result.getAllDatasets().get(0).getAllRecords();
            for (Record record : recordList) {
                if (validateCurrentValue(record)) {
                    incrementCurrentValue(record);
                } else {
                    addErrorMsg(record);
                }
            }
            result.addParam(new Param(DBPUtilitiesConstants.RECORD_COUNT, String.valueOf(recordList.size()), "String"));
        }

        return result;
    }

    private void incrementCurrentValue(Record record) {
        Param param = record.getParam(DBPUtilitiesConstants.CURRENT_VALUE);
        long currValue = 0;
        if (null != param && StringUtils.isNotBlank(param.getValue())) {
            currValue = Long.parseLong(param.getValue());
            currValue = currValue + 1;
            param.setValue(String.valueOf(currValue));
        }
    }

    private boolean validateCurrentValue(Record record) {
        Param curValParam = record.getParam(DBPUtilitiesConstants.CURRENT_VALUE);
        Param endValParam = record.getParam(DBPUtilitiesConstants.END_VALUE);
        if (null != curValParam && StringUtils.isNotBlank(curValParam.getValue())) {
            long currVal = Long.parseLong(curValParam.getValue());
            long endVal = Long.parseLong(endValParam.getValue());
            return currVal < endVal;
        }
        return true;
    }

    private void addErrorMsg(Record record) {
        Param curValParam = record.getParam(DBPUtilitiesConstants.CURRENT_VALUE);
        curValParam.setValue("-1");
        record.addParam(new Param(DBPUtilitiesConstants.VALIDATION_ERROR, "current value exhausted.", "String"));
    }
}