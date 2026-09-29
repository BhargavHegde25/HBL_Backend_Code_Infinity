package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.Map;

import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.PasswordHistoryDTO;
import com.temenos.dbx.product.dto.PasswordLockoutSettingsDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.PasswordHistoryBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class PasswordHistoryBusinessDelegateImpl implements PasswordHistoryBusinessDelegate {

	@Override
	public DBXResult update(PasswordHistoryDTO passwordHistoryDTO, Map<String, Object> headerMap) {

		DBXResult dbxResult = new DBXResult();

		if(passwordHistoryDTO.persist(DTOUtils.getParameterMap(passwordHistoryDTO, true), headerMap)) {
			dbxResult .setResponse(passwordHistoryDTO.getId());
		}
		else {
			dbxResult.setDbpErrMsg("Customer update Failed");
		}

		return dbxResult;
	}

	@Override
	public DBXResult get(PasswordHistoryDTO passwordHistoryDTO, Map<String, Object> headerMap) {
		DBXResult dbxResult = new DBXResult();

		dbxResult.setResponse(passwordHistoryDTO.loadDTO(passwordHistoryDTO.getCustomer_id()));

		return dbxResult;
	}

	@Override
	public DBXResult getPasswordLockoutSetting(Map<String, Object> headerMap) {
		
		DBXResult dbxResult = new DBXResult();

		dbxResult.setResponse(new PasswordLockoutSettingsDTO().loadDTO());

		return dbxResult;
		
	}
}
