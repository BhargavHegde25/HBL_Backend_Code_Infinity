package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.CustomerSecurityQuestionsViewDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerSecurityQuestionsBusinessDelegate;

public class CustomerSecurityQuestionsBusinessDelegateImpl implements CustomerSecurityQuestionsBusinessDelegate {

	@Override
	public DBXResult get(String customerId, Map<String, Object> headerMap)
			throws ApplicationException {
		CustomerSecurityQuestionsViewDTO securityQuestionsViewDTO = new CustomerSecurityQuestionsViewDTO();

		securityQuestionsViewDTO.setCustomer_id(customerId);

		List<CustomerSecurityQuestionsViewDTO> list = (List<CustomerSecurityQuestionsViewDTO>) securityQuestionsViewDTO.loadDTO();

		DBXResult dbxResult = new DBXResult();

		dbxResult.setResponse(list);

		return dbxResult;
	}

	@Override
	public DBXResult getAreSecurityQuestionsConfigured(String customerId, Map<String, Object> headerMap) {
		CustomerSecurityQuestionsViewDTO securityQuestionsViewDTO = new CustomerSecurityQuestionsViewDTO();

		securityQuestionsViewDTO.setCustomer_id(customerId);

		List<CustomerSecurityQuestionsViewDTO> list = (List<CustomerSecurityQuestionsViewDTO>) securityQuestionsViewDTO.loadDTO();

		DBXResult dbxResult = new DBXResult();

		if(list != null && list.size() >0) {
			dbxResult.setResponse(true);
		}
		else {
			dbxResult.setResponse(false);
		}

		return dbxResult;
	}

}
