package com.kony.achservices;

import java.util.Map;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * Class FetchBBTemplateRecord
 */
public class FetchBBTemplateRecords implements JavaService2 {

    /**
     * @param s
     * @param objects
     * @param dataControllerRequest
     * @param dataControllerResponse
     * @return
     * @throws Exception
     * @description Base invoke method
     */
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dataControllerRequest,
            DataControllerResponse dataControllerResponse) throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);
        return fetchBBTemplateRecordHelper(inputParams, dataControllerRequest);
    }

    /**
     * @param inputParams
     * @param dcr
     * @return
     * @throws Exception
     * @description Helper function for fetching template Record of a BB User
     */
    public Result fetchBBTemplateRecordHelper(Map<String, String> inputParams, DataControllerRequest dcr)
            throws Exception {
        try {
            Map<String, String> dataMap = CommonUtils.getTemplateIdFilter(inputParams);
            return HelperMethods.callApi(dcr, dataMap, HelperMethods.getHeaders(dcr),
                    URLConstants.BB_TEMPLATE_RECORD_DETAILS_VIEW_GET);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
        }
    }
}
