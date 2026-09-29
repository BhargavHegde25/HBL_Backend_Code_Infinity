package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class BasicInfoPreProcessor implements DataPreProcessor2 {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            String Customer_id = request.getParameter("Customer_id");
            String partyId = request.getParameter("partyId");
            if (StringUtils.isNotBlank(partyId)) {
                inputMap.put("partyId", partyId);
                return true;
            } else if (StringUtils.isNotBlank(Customer_id)) {
                partyId = getPartyId(Customer_id, request);
                if (StringUtils.isNotBlank(partyId))
                    inputMap.put("partyId", partyId);
                else
                    inputMap.put("partyId", Customer_id);
                return true;
            } else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    private String getPartyId(String Customer_id, DataControllerRequest requestInstance) {
        Map<String, String> postParamsMap = new HashMap<>();
        postParamsMap.put(ODataQueryConstants.FILTER, "Customer_id eq '" + Customer_id + "'");
        postParamsMap.put(ODataQueryConstants.SELECT, "BackendId");

        String readBackendIdentifierResponse = Executor.invokeService(ServiceURLEnum.BACKENDIDENTIFIER_READ,
                postParamsMap, null, requestInstance);
        JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(readBackendIdentifierResponse);

        JSONArray serviceResponseArray = serviceResponseJSON.optJSONArray("backendidentifier");
        if (serviceResponseArray.length() != 0) {
            String id = serviceResponseArray.optJSONObject(0).optString("BackendId");
            return id;
        }

        return null;
    }

}
