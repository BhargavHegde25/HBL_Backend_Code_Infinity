package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.HashMap;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.PartialRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface  SubmitPartialRepaymentBusinessDelegate extends BusinessDelegate {

	PartialRepaymentDTO SubmitPartialRepayment(PartialRepaymentDTO PartialRepayment,
			HashMap<String, Object> headerMap) throws ApplicationException;
	

}