package com.temenos.dbx.product.usermanagement.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface CommunicationBackendDelegate extends BackendDelegate {

    public DBXResult get(CustomerCommunicationDTO dto, Map<String, Object> headerMap);

    public DBXResult create(CustomerCommunicationDTO dto,
            Map<String, Object> headerMap);

    public DBXResult getPrimaryCommunication(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap);

    public DBXResult getPrimaryCommunicationForLogin(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap);

    public DBXResult getCommunication(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap);

    /**
     * 
     * @param customerCommunicationDTO
     * @param headerMap
     * @return Primary Email , Phone details
     */
    public DBXResult getPrimaryCommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap);

    public DBXResult getCommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap);

    public DBXResult getPrimaryMFACommunicationDetails(CustomerCommunicationDTO customerCommunicationDTO,
            Map<String, Object> headerMap);
}
