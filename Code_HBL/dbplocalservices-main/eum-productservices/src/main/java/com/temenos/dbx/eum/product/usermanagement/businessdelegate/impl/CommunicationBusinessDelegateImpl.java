package com.temenos.dbx.eum.product.usermanagement.businessdelegate.impl;

import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.CommunicationBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

public class CommunicationBusinessDelegateImpl implements CommunicationBusinessDelegate {

	@Override
	public DBXResult get(CustomerCommunicationDTO dto, Map<String, Object> headerMap){
		CommunicationBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
		return backendDelegate.get(dto, headerMap);
	}

	@Override
	public DBXResult create(CustomerCommunicationDTO inputDTO,
			Map<String, Object> headersMap) {

		CommunicationBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
		return backendDelegate.create(inputDTO, headersMap);
	}

	@Override
	public DBXResult getPrimaryCommunicationForLogin(CustomerCommunicationDTO customerCommunicationDTO,
			Map<String, Object> headerMap) {
		CommunicationBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
		return backendDelegate.getPrimaryCommunicationForLogin(customerCommunicationDTO, headerMap);
	}

	@Override
	public DBXResult getPrimaryCommunication(CustomerCommunicationDTO customerCommunicationDTO,
			Map<String, Object> headerMap) {
		CommunicationBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CommunicationBackendDelegate.class);
		return backendDelegate.getPrimaryCommunication(customerCommunicationDTO, headerMap);
	}

}
