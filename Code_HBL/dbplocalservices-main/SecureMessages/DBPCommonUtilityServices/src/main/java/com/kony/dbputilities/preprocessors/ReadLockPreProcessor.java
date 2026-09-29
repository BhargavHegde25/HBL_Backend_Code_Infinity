package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class ReadLockPreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) {
		Log4j2Configurator.getInstance();

        boolean status = true;

        if (isFilterKeyNull(paramMap, dcRequest)) {
            if (validateMandatoryInputParam(dcRequest)) {
                createAndSetFilterKey(paramMap, dcRequest, result);
            } else {
                HelperMethods.setValidationMsg("ExternalId and/or ObjectId are missing.", dcRequest, result);
                status = false;
            }
        }
        return status;
    }

    @SuppressWarnings("rawtypes")
    private boolean isFilterKeyNull(Map paramMap, DataControllerRequest dcRequest) {
        String filter = (String) paramMap.get(DBPUtilitiesConstants.FILTER);
        return !StringUtils.isNotBlank(filter);
    }

    private boolean validateMandatoryInputParam(DataControllerRequest dcRequest) {

        String externalid = dcRequest.getParameter(DBPUtilitiesConstants.EXTERNAL_ID);
        String objectId = dcRequest.getParameter(DBPUtilitiesConstants.OBJECT_ID);

        return StringUtils.isNotBlank(objectId) && StringUtils.isNotBlank(externalid);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void createAndSetFilterKey(Map paramMap, DataControllerRequest dcRequest, Result result) {

        String externalid = dcRequest.getParameter(DBPUtilitiesConstants.EXTERNAL_ID);
        String objectId = dcRequest.getParameter(DBPUtilitiesConstants.OBJECT_ID);

        String filter = "(" + DBPUtilitiesConstants.EXTERNAL_ID + DBPUtilitiesConstants.EQUAL + externalid + ")"
                + DBPUtilitiesConstants.AND + "(" + DBPUtilitiesConstants.OBJECT_ID + DBPUtilitiesConstants.EQUAL
                + objectId + ")";

        paramMap.put(DBPUtilitiesConstants.FILTER, filter);
    }
}