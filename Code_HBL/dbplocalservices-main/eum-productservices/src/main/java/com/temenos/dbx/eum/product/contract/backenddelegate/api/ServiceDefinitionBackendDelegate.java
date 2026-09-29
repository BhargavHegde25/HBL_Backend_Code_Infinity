package com.temenos.dbx.eum.product.contract.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.ServiceDefinitionDTO;

public interface ServiceDefinitionBackendDelegate extends BackendDelegate {

    /**
     * 
     * @param serviceDefinitionDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public ServiceDefinitionDTO getServiceDefinitionDetails(ServiceDefinitionDTO serviceDefinitionDTO,
            Map<String, Object> headersMap) throws ApplicationException;

    /**
     * 
     * @param serviceDefinitionDTO
     * @param headersMap
     * @return
     * @throws ApplicationException
     */
    public String fetchDefaultRoleId(ServiceDefinitionDTO serviceDefinitionDTO, Map<String, Object> headersMap)
            throws ApplicationException;
}
