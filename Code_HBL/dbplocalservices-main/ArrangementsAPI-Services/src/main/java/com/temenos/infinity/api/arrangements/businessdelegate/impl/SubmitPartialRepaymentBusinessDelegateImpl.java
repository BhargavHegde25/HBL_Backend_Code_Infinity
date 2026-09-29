package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.HashMap;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.api.SubmitPartialRepaymentBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.SubmitPartialRepaymentBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.PartialRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class SubmitPartialRepaymentBusinessDelegateImpl  implements SubmitPartialRepaymentBusinessDelegate {

	public PartialRepaymentDTO SubmitPartialRepayment(PartialRepaymentDTO partialRepayment,
			HashMap<String, Object> headerMap) throws ApplicationException {
		SubmitPartialRepaymentBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(SubmitPartialRepaymentBackendDelegate.class);
		PartialRepaymentDTO partialRepamentDay = orderBackendDelegate.SubmitPartialRepayment(partialRepayment,
				headerMap);
		return partialRepamentDay;
	}


}
