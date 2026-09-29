package com.bct.custom.businessdeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.PaymentAggregatorDTO;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface PaymentAggregatorBusinessDeligate extends BusinessDelegate{
	public JSONArray getAllPaymentAggregator(Map<String, Object> headerMap) throws ApplicationException;
	public JSONArray getPaymentAggregator(String code, Map<String, Object> headerMap) throws ApplicationException;
	public Result updatePaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createPaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap, Map<String, Object> inputs)
			throws ApplicationException;
}
