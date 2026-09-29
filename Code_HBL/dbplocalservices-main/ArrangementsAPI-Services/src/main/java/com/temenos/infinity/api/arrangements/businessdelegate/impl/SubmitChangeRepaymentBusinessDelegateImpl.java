package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.HashMap;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.api.SubmitChangeRepaymentBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.SubmitChangeRepaymentBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.MortgageRepaymentDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class SubmitChangeRepaymentBusinessDelegateImpl  implements SubmitChangeRepaymentBusinessDelegate {

	public MortgageRepaymentDTO SubmitChangeRepaymentDay(MortgageRepaymentDTO changeRepaymentday,
			HashMap<String, Object> headerMap) throws ApplicationException {
		SubmitChangeRepaymentBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(SubmitChangeRepaymentBackendDelegate.class);
		MortgageRepaymentDTO repamentDayOrder = orderBackendDelegate.SubmitChangeRepaymentDay(changeRepaymentday,
				headerMap);
		return repamentDayOrder;
	}

	public MortgageRepaymentDTO SubmitChangeRepaymentAccount(MortgageRepaymentDTO changeRepaymentAccount,
			HashMap<String, Object> headerMap) throws ApplicationException {
		SubmitChangeRepaymentBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(SubmitChangeRepaymentBackendDelegate.class);
		MortgageRepaymentDTO repamentAccountOrder = orderBackendDelegate.SubmitChangeRepaymentAccount(changeRepaymentAccount,
				headerMap);
		return repamentAccountOrder;
	}

}
