package com.kony.testscripts.getscripts;

import com.google.gson.JsonParser;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCaptchValueOnServiceKey implements JavaService2 {

    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {

        String serviceKey = dcRequest.getParameter("serviceKey");

        String filterQuery = "serviceKey" + DBPUtilitiesConstants.EQUAL + serviceKey;

        Result mfaserviceresult = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.MFA_SERVICE_GET);

        return ConvertJsonToResult.convert(new JsonParser()
                .parse(CryptoText.decrypt(HelperMethods.getFieldValue(mfaserviceresult, "payload"))).getAsJsonObject());
    }
}