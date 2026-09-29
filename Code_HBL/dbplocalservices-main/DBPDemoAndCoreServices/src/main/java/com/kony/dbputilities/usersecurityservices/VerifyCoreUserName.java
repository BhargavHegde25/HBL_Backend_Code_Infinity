package com.kony.dbputilities.usersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class VerifyCoreUserName implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            Map<String, String> headersMap = HelperMethods.getHeaders(dcRequest);
            headersMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            result = HelperMethods.callApi(dcRequest, null, headersMap, URLConstants.DBX_REQUESTOTP);
            result = postProcess(result);
        }

        return result;
    }

    private Result postProcess(Result result) {

        Result retResult = new Result();

        Record usrAttr = new Record();
        usrAttr.setId(DBPUtilitiesConstants.CORE_ATTR);
        if (!HelperMethods.hasError(result)) {
            usrAttr.addParam(new Param(DBPUtilitiesConstants.IS_USERNAME_EXISTS, "true", "String"));
            for (Param param : result.getAllParams()) {
                usrAttr.addParam(param);
            }
        } else {
            ErrorCodeEnum.ERR_10007.setErrorCode(usrAttr);
        }

        retResult.addRecord(usrAttr);
        return retResult;
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String userName = inputParams.get("userName");
        String phone = inputParams.get("phone");
        String email = inputParams.get("email");
        String filterKey = "";
        String filtervalue = "";
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(userName)) {
            filterKey = "userName";
            filtervalue = userName;
        } else if (StringUtils.isNotBlank(phone)) {
            filterKey = "phone";
            filtervalue = phone;
        } else if (StringUtils.isNotBlank(email)) {
            filterKey = "email";
            filtervalue = email;
        } else {
            Record record = new Record();
            record.setId(DBPUtilitiesConstants.CORE_ATTR);
            ErrorCodeEnum.ERR_10005.setErrorCode(record);
            result.addRecord(record);
            status = false;
        }

        if (status) {
            sb.append(filterKey).append(DBPUtilitiesConstants.EQUAL).append(filtervalue);
            Result rs = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
                    URLConstants.USER_GET);
            if (!HelperMethods.hasRecords(rs)) {
                Record usrAttr = new Record();
                usrAttr.setId(DBPUtilitiesConstants.CORE_ATTR);
                usrAttr.addParam(new Param(DBPUtilitiesConstants.IS_USERNAME_EXISTS, "false", "String"));
                status = false;
                result.addRecord(usrAttr);
            }
        }
        return status;
    }
}
