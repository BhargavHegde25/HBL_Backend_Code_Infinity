package com.temenos.infinity.api.visaintegrationapi.backenddelegate.api;

import java.util.HashMap;
import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface VisaIntegrationApiAPIBackendDelegate extends BackendDelegate {

	VisaDTO linkCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException;

	VisaDTO enrollCard(VisaDTO visaDTO, HashMap<String, Object> headerMap) throws ApplicationException;

}
