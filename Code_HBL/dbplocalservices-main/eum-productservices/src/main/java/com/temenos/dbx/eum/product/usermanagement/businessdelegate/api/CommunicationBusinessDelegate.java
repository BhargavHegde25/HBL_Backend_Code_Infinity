package com.temenos.dbx.eum.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;

public interface CommunicationBusinessDelegate extends BusinessDelegate {

    public DBXResult get(CustomerCommunicationDTO dto, Map<String, Object> headerMap);

    public DBXResult create(CustomerCommunicationDTO dto,
            Map<String, Object> headerMap);

	public DBXResult getPrimaryCommunication(CustomerCommunicationDTO customerCommunicationDTO,
			Map<String, Object> headerMap);

	public DBXResult getPrimaryCommunicationForLogin(CustomerCommunicationDTO customerCommunicationDTO,
			Map<String, Object> headerMap);

}
