package com.temenos.dbx.product.businessdelegate.impl;

import java.util.Map;

import com.temenos.dbx.product.businessdelegate.api.BankBusinessDelegate;
import com.temenos.dbx.product.dto.BankDTO;
import com.temenos.dbx.product.dto.DBXResult;


public class BankBusinessDelegateImpl implements BankBusinessDelegate{

	@Override
	public DBXResult getBankName(String bankID, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		DBXResult dbxResult = new DBXResult();
		BankDTO bankDTO = new BankDTO();
		bankDTO.setId(bankID);
		bankDTO = (BankDTO) bankDTO.loadDTO();
		if(bankDTO != null) {
			dbxResult.setResponse(bankDTO.getDescription());
		}
		return dbxResult;
	}
}
