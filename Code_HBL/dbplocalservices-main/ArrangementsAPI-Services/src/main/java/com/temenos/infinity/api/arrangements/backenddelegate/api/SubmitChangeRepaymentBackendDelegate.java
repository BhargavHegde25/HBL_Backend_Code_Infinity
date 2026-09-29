package com.temenos.infinity.api.arrangements.backenddelegate.api;


import java.util.HashMap;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface SubmitChangeRepaymentBackendDelegate extends BackendDelegate {

	MortgageRepaymentDTO SubmitChangeRepaymentDay(MortgageRepaymentDTO changeRepaymentday,
			HashMap<String, Object> headerMap) throws ApplicationException;
	MortgageRepaymentDTO SubmitChangeRepaymentAccount(MortgageRepaymentDTO changeRepaymentAccount,
			HashMap<String, Object> headerMap) throws ApplicationException;
}
