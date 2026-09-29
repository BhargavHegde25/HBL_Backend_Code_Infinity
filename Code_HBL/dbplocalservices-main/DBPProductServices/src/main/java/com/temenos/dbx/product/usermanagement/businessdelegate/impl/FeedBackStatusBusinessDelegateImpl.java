package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.Map;

import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeedBackStatusDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.FeedBackStatusBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class FeedBackStatusBusinessDelegateImpl implements FeedBackStatusBusinessDelegate {

	@Override
	public DBXResult update(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap) {
		DBXResult dbxResult = new DBXResult();
		feedBackStatusDTO.setChanged(true);
		dbxResult.setResponse(feedBackStatusDTO.persist(DTOUtils.getParameterMap(feedBackStatusDTO, true), headerMap));
		return dbxResult;
	}

	@Override
	public DBXResult create(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap) {
		DBXResult dbxResult = new DBXResult();
		feedBackStatusDTO.setNew(true);
		dbxResult.setResponse(feedBackStatusDTO.persist(DTOUtils.getParameterMap(feedBackStatusDTO, true), headerMap));
		return dbxResult;
	}
	
	@Override
	public DBXResult get(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap) {
		DBXResult dbxResult = new DBXResult();
		dbxResult.setResponse(feedBackStatusDTO.loadDTO());
		return dbxResult;
	}

	@Override
	public DBXResult delete(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap)
			throws ApplicationException {

		return null;
	}
}
