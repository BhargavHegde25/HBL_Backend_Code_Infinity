package com.temenos.dbx.product.usermanagement.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeedBackStatusDTO;

public interface FeedBackStatusBusinessDelegate extends BusinessDelegate {

	public DBXResult update(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap);

	public DBXResult get(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap);

	public DBXResult delete(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap)
			throws ApplicationException;

	public DBXResult create(FeedBackStatusDTO feedBackStatusDTO, Map<String, Object> headerMap);
}
