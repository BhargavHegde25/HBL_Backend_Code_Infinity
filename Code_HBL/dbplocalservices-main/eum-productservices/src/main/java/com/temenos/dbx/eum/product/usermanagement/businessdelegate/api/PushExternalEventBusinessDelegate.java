package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;

public interface PushExternalEventBusinessDelegate extends BusinessDelegate {

    public boolean pushExternalEvent(String eventCode, String jsonEventData, Map<String, Object> headersMap)
            throws ApplicationException;

}
