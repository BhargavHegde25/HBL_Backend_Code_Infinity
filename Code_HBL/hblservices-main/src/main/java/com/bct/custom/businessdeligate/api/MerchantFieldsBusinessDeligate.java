package com.bct.custom.businessdeligate.api;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantDTO;
import com.bct.custom.dto.MerchantFieldsDTO;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MerchantFieldsBusinessDeligate extends BusinessDelegate{
	public Result updateMerchantFileds(MerchantFieldsDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantFileds(MerchantFieldsDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantFileds(ArrayList<MerchantFieldsDTO> dtoList, DataControllerRequest dcRequest,
			Map<String, Object> inputParams) throws ApplicationException;
	public JSONArray getMerchantFileds(String code, Map<String, Object> inputMap, Map<String, Object> headerMap)
			throws ApplicationException;
	public Result updateMerchantFileds(ArrayList<MerchantFieldsDTO> dtoList, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	Result deleteMerchantFileds(ArrayList<MerchantFieldsDTO> dtoList, DataControllerRequest request,
			Map<String, Object> inputs) throws ApplicationException;

}
