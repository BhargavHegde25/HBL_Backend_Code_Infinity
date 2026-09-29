package com.kony.dbputilities.customersecurityservices;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class EditOrgEmployeePostProcessor implements DataPostProcessor2 {

    @Override
    public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
        Result retValue = new Result();
        Record dbxUsrAttr = result.getRecordById(DBPUtilitiesConstants.USR_ATTR);
        Record accountsAttr = result.getRecordById(DBPUtilitiesConstants.CUSTOMER_ACCOUNTS_ATTR);
        Record limitsAttr = result.getRecordById("Limits_attr");

        String dbxErrorCode = HelperMethods.getFieldValue(dbxUsrAttr, ErrorCodeEnum.ERROR_CODE_KEY);
        String accountsErrorCode = HelperMethods.getFieldValue(accountsAttr, ErrorCodeEnum.ERROR_CODE_KEY);
        String limitsErrorCode = HelperMethods.getFieldValue(limitsAttr, ErrorCodeEnum.ERROR_CODE_KEY);

        if(dbxUsrAttr != null) {
            for (Param param : dbxUsrAttr.getAllParams()) {
                retValue.addParam(param);
            }
        }
        
        if (StringUtils.isNotBlank(dbxErrorCode)) {
            Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
            retValue.addParam(p);
            return retValue;
        }

        if(accountsAttr != null) {
            for (Param param : accountsAttr.getAllParams()) {
                retValue.addParam(param);
            }
        }

        if (StringUtils.isNotBlank(accountsErrorCode)) {
            Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
            retValue.addParam(p);
            return retValue;
        }

        if(limitsAttr != null) {
            for (Param param : limitsAttr.getAllParams()) {
                retValue.addParam(param);
            }
        }

        if (StringUtils.isNotBlank(limitsErrorCode)) {
            Param p = new Param(DBPUtilitiesConstants.SUCCESS, null, DBPUtilitiesConstants.STRING_TYPE);
            retValue.addParam(p);
            return retValue;
        }

        return retValue;
    }

}
