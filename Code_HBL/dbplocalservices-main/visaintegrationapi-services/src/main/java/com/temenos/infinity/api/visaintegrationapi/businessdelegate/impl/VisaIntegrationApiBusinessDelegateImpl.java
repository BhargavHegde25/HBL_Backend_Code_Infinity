package com.temenos.infinity.api.visaintegrationapi.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;

import com.temenos.infinity.api.visaintegrationapi.backenddelegate.api.VisaIntegrationApiAPIBackendDelegate;
import com.temenos.infinity.api.visaintegrationapi.businessdelegate.api.VisaIntegrationApiBusinessDelegate;
import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class VisaIntegrationApiBusinessDelegateImpl implements VisaIntegrationApiBusinessDelegate {

	@Override
	public VisaDTO linkCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException {
		VisaIntegrationApiAPIBackendDelegate visaIntegrationApiAPIBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(VisaIntegrationApiAPIBackendDelegate.class);
		VisaDTO linkedCardDTO = visaIntegrationApiAPIBackendDelegate.linkCard(visaDTO, headerMap);
		return linkedCardDTO;
	}

	@Override
	public VisaDTO enrollCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException {
		VisaIntegrationApiAPIBackendDelegate visaIntegrationApiAPIBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(VisaIntegrationApiAPIBackendDelegate.class);
		VisaDTO enrolledCardDTO = visaIntegrationApiAPIBackendDelegate.enrollCard(visaDTO, headerMap);
		return enrolledCardDTO;
	}

}