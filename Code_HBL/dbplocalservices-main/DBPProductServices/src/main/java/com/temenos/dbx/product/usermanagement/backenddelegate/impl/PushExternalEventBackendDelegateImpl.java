package com.temenos.dbx.product.usermanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.PushExternalEventBackendDelegate;

public class PushExternalEventBackendDelegateImpl implements PushExternalEventBackendDelegate {

    @Override
    public boolean pushExternalEvent(String eventCode, String jsonEventData, Map<String, Object> headersMap)
            throws ApplicationException {
        JsonParser parse = new JsonParser();
        try {
            if (StringUtils.isBlank(jsonEventData)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10346);
            }
            JsonObject json =
                    parse.parse(jsonEventData).isJsonObject() ? parse.parse(jsonEventData).getAsJsonObject() : null;

            if (JSONUtil.isJsonNotNull(json)) {
                Map<String, Object> inputParams = new HashMap<>();
                inputParams.put("eventCode", eventCode);
                inputParams.put("eventData", json);

                JsonObject eventResponse = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
                        URLConstants.PUSHEXTERNALEVENT_OBJECTSERVICE);
                if (JSONUtil.isJsonNotNull(eventResponse) && JSONUtil.hasKey(eventResponse, "success")) {
                    return "true".equalsIgnoreCase(eventResponse.get("success").getAsString());
                }
            } else {
                throw new ApplicationException(ErrorCodeEnum.ERR_10346);
            }
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10346);
        }
        return false;
    }

}
