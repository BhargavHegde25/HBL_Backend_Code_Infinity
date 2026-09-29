package com.kony.achservices;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchBBTemplateTypes implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
        try {
            return HelperMethods.callApi(dcRequest, HelperMethods.getInputParamMap(objects),
                    HelperMethods.getHeaders(dcRequest), URLConstants.BB_TEMPLATE_TYPE_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
