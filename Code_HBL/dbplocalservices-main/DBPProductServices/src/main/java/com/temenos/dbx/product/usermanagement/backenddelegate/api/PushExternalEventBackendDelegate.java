package com.temenos.dbx.product.usermanagement.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;

public interface PushExternalEventBackendDelegate extends BackendDelegate {

    public boolean pushExternalEvent(String eventCode, String jsonEventData, Map<String, Object> headersMap)
            throws ApplicationException;

}
