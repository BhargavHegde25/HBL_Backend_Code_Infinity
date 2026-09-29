package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetNumberRangePreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) throws Exception {
		Log4j2Configurator.getInstance();

        boolean status = true;
        String filter = (String) paramMap.get(DBPUtilitiesConstants.FILTER);

        boolean hasMandatoryParam = validateMandatoryParam(paramMap, dcRequest);
        if (hasMandatoryParam) {
            setFilterParam(paramMap, dcRequest);
        }

        if (!hasMandatoryParam && !StringUtils.isNotBlank(filter)) {
            Param validionMsg = new Param(DBPUtilitiesConstants.VALIDATION_ERROR,
                    "Please provide filter parameter to select a unique record.",
                    DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(validionMsg);
            status = false;
        }
        return status;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void setFilterParam(Map paramMap, DataControllerRequest dcRequest) {
        String objectId = dcRequest.getParameter(DBPUtilitiesConstants.OBJECT_ID);
        paramMap.put(DBPUtilitiesConstants.FILTER,
                DBPUtilitiesConstants.OBJECT_ID + DBPUtilitiesConstants.EQUAL + objectId);
    }

    @SuppressWarnings("rawtypes")
    private boolean validateMandatoryParam(Map paramMap, DataControllerRequest dcRequest) {
        String objectId = dcRequest.getParameter(DBPUtilitiesConstants.OBJECT_ID);
        return StringUtils.isNotBlank(objectId);
    }
}