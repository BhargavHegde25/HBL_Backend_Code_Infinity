package com.temenos.infinity.api.arrangements.backenddelegate.api;


import java.util.HashMap;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.arrangements.dto.PartialRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface SubmitPartialRepaymentBackendDelegate extends BackendDelegate {

	PartialRepaymentDTO SubmitPartialRepayment(PartialRepaymentDTO partialRepayment,
			HashMap<String, Object> headerMap) throws ApplicationException;
}
