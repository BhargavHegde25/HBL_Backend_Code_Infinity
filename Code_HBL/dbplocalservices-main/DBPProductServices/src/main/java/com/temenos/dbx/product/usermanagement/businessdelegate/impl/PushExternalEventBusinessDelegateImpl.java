package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.PushExternalEventBackendDelegate;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.PushExternalEventBusinessDelegate;

public class PushExternalEventBusinessDelegateImpl implements PushExternalEventBusinessDelegate {

    @Override
    public boolean pushExternalEvent(String eventCode, String jsonEventData, Map<String, Object> headersMap)
            throws ApplicationException {

        PushExternalEventBackendDelegate pushEventBD = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(PushExternalEventBackendDelegate.class);

        return pushEventBD.pushExternalEvent(eventCode, jsonEventData, headersMap);
    }

}
