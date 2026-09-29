package com.bct.custom.backenddeligate.api;

import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.PaymentAggregatorDTO;
import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface PaymentAggregatorBackendDeligate extends BackendDelegate{
	public JSONArray getAllPaymentAggregator(Map<String, Object> headerMap) throws ApplicationException;
	public JSONArray getPaymentAggregator(String id, Map<String, Object> headerMap) throws ApplicationException;
	public Result updatePaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createPaymentAggregator(PaymentAggregatorDTO dto, Map<String, Object> headerMap, Map<String, Object> inputs)
					throws ApplicationException;

}
