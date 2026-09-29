package com.temenos.infinity.api.chequemanagement.mapper;

import java.util.HashMap;
import java.util.Map;

import com.dbp.core.api.DBPAPIMapper;
import com.dbp.core.api.Resource;
import com.temenos.infinity.api.chequemanagement.resource.api.ChequeManagementResource;
import com.temenos.infinity.api.chequemanagement.resource.api.CreateChequeBookResource;
import com.temenos.infinity.api.chequemanagement.resource.api.CreateStopPaymentResource;
import com.temenos.infinity.api.chequemanagement.resource.api.GetCheuqeBookResource;
import com.temenos.infinity.api.chequemanagement.resource.api.GetStopPaymentResource;
import com.temenos.infinity.api.chequemanagement.resource.api.RevokeStopPaymentResource;
import com.temenos.infinity.api.chequemanagement.resource.impl.ChequeManagementResourceImpl;
import com.temenos.infinity.api.chequemanagement.resource.impl.CreateChequeBookResourceImpl;
import com.temenos.infinity.api.chequemanagement.resource.impl.CreateStopPaymentResourceImpl;
import com.temenos.infinity.api.chequemanagement.resource.impl.GetChequeBookRequestsResourceImpl;
import com.temenos.infinity.api.chequemanagement.resource.impl.GetStopPaymentsResourceImpl;
import com.temenos.infinity.api.chequemanagement.resource.impl.RevokeStopPaymentResourceImpl;

/**
 * 
 * @author smugesh version 1.0 implements {@link DBPAPIMapper}
 */

public class ChequeManagementResourceMapper implements DBPAPIMapper<Resource> {

    @Override
    public Map<Class<? extends Resource>, Class<? extends Resource>> getAPIMappings() {

        Map<Class<? extends Resource>, Class<? extends Resource>> map = new HashMap<>();

        // Add Mapping of Resource interface and their implementation
        map.put(CreateChequeBookResource.class, CreateChequeBookResourceImpl.class);
        map.put(GetCheuqeBookResource.class, GetChequeBookRequestsResourceImpl.class);
        map.put(CreateStopPaymentResource.class, CreateStopPaymentResourceImpl.class);
        map.put(GetStopPaymentResource.class, GetStopPaymentsResourceImpl.class);
        map.put(RevokeStopPaymentResource.class, RevokeStopPaymentResourceImpl.class);
        map.put(ChequeManagementResource.class, ChequeManagementResourceImpl.class);
        return map; 
    }
}
