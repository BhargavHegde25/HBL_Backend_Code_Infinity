package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.HashMap;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface  SubmitChangeRepaymentBusinessDelegate extends BusinessDelegate {

	MortgageRepaymentDTO SubmitChangeRepaymentDay(MortgageRepaymentDTO changeRepaymentday,
			HashMap<String, Object> headerMap) throws ApplicationException;
	MortgageRepaymentDTO SubmitChangeRepaymentAccount(MortgageRepaymentDTO changeRepaymentAccount,
			HashMap<String, Object> headerMap) throws ApplicationException;

}
