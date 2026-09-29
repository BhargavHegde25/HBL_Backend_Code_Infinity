package com.temenos.infinity.api.visaintegrationapi.businessdelegate.api;

import java.util.HashMap;
import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface VisaIntegrationApiBusinessDelegate extends BusinessDelegate {

	VisaDTO linkCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException;

	VisaDTO enrollCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException;

}
