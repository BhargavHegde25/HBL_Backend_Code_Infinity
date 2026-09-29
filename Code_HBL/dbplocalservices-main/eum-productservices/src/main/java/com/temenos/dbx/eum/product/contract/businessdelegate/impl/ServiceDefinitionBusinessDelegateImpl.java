package com.temenos.dbx.eum.product.contract.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ServiceDefinitionBackendDelegate;
import com.temenos.dbx.eum.product.contract.businessdelegate.api.ServiceDefinitionBusinessDelegate;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;

public class ServiceDefinitionBusinessDelegateImpl implements ServiceDefinitionBusinessDelegate {

    @Override
    public ServiceDefinitionDTO getServiceDefinitionDetails(ServiceDefinitionDTO serviceDefinitionDTO,
            Map<String, Object> headersMap) throws ApplicationException {
        ServiceDefinitionBackendDelegate backendDelegate =
                DBPAPIAbstractFactoryImpl.getBackendDelegate(ServiceDefinitionBackendDelegate.class);
        return backendDelegate.getServiceDefinitionDetails(serviceDefinitionDTO, headersMap);
    }

}
