package com.bct.custom.businessdeligate.api;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.dto.PaymentAggregatorDTO;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MerchantDetailsBusinessDeligate extends BusinessDelegate{
	public Result updateMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantDetails(ArrayList<MerchantDTO> merchants, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public JSONArray getMerchantDetails(String code,
			Map<String, Object> headerMap)throws ApplicationException;

}
