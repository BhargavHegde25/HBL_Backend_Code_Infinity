package com.kony.dbputilities.preprocessors;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class DeleteLockPreProcessor {

    @SuppressWarnings("rawtypes")
    public boolean execute(Map paramMap, DataControllerRequest dcRequest, Result result) {
		Log4j2Configurator.getInstance();
        boolean status = true;
        if (!validateMandatoryInputParam(paramMap)) {
            HelperMethods.setValidationMsg("Mandatory fields are missing.", dcRequest, result);
            status = false;
        }
        return status;
    }

    @SuppressWarnings("rawtypes")
    private boolean validateMandatoryInputParam(Map paramMap) {
        String objectId = (String) paramMap.get(DBPUtilitiesConstants.OBJECT_ID);
        String user = (String) paramMap.get(DBPUtilitiesConstants.USER);
        String externalId = (String) paramMap.get(DBPUtilitiesConstants.EXTERNAL_ID);
        return StringUtils.isNotBlank(objectId) && StringUtils.isNotBlank(externalId) && StringUtils.isNotBlank(user);
    }
}