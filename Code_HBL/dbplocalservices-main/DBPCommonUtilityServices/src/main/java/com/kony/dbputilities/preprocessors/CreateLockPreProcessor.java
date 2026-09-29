package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class CreateLockPreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) throws Exception {
		Log4j2Configurator.getInstance();
        boolean status = true;
        if (isLockObjectFound(dcRequest)) {
            String objectId = (String) paramMap.get(DBPUtilitiesConstants.OBJECT_ID);
            String externalId = (String) paramMap.get(DBPUtilitiesConstants.EXTERNAL_ID);
            HelperMethods.setValidationMsg(DBPUtilitiesConstants.EXTERNAL_ID + " " + externalId + " and "
                    + DBPUtilitiesConstants.OBJECT_ID + " " + objectId + " are already locked.", dcRequest, result);
            status = false;
        }
        if (status && !validateMandatoryInputParam(dcRequest)) {
            HelperMethods.setValidationMsg("Mandatory parameters are missing.", dcRequest, result);
            status = false;
        }
        return status;
    }

    private boolean isLockObjectFound(DataControllerRequest dcRequest) {
        String count = dcRequest.getParameter(DBPUtilitiesConstants.RECORD_COUNT);
        return (null != count) && !DBPUtilitiesConstants.ZERO.equals(count);
    }

    private boolean validateMandatoryInputParam(DataControllerRequest dcRequest) {
        String objectId = dcRequest.getParameter(DBPUtilitiesConstants.OBJECT_ID);
        String user = dcRequest.getParameter(DBPUtilitiesConstants.USER);
        String externalId = dcRequest.getParameter(DBPUtilitiesConstants.EXTERNAL_ID);
        String mode = dcRequest.getParameter(DBPUtilitiesConstants.MODE);
        return StringUtils.isNotBlank(objectId) && StringUtils.isNotBlank(externalId) && StringUtils.isNotBlank(user)
                && StringUtils.isNotBlank(mode);
    }
}