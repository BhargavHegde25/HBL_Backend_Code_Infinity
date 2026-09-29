package com.temenos.infinity.api.visaintegrationapi.resource.api;

import java.util.HashMap;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.visaintegrationapi.dto.VisaDTO;

public interface VisaIntegrationApiResource extends Resource {

	Result linkCard(VisaDTO visaDTO, HashMap<String, Object> headerMap, DataControllerRequest request) throws ApplicationException;

	Result enrollCard(VisaDTO visaDTO, HashMap<String, Object> headerMap, DataControllerRequest request) throws ApplicationException;
}
