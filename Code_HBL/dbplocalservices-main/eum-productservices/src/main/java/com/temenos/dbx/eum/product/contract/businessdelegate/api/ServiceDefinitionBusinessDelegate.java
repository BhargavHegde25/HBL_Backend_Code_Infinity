package com.temenos.dbx.eum.product.contract.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;

public interface ServiceDefinitionBusinessDelegate extends BusinessDelegate {

    public ServiceDefinitionDTO getServiceDefinitionDetails(ServiceDefinitionDTO serviceDefinitionDTO,
            Map<String, Object> headersMap) throws ApplicationException;

}
