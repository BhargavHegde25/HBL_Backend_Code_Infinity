package com.bct.custom.backenddeligate.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.MerchantDTO;
import com.dbp.core.api.BackendDelegate;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MerchantDetailsBackendDeligate extends BackendDelegate{
	public Result updateMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public Result createMerchantDetails(ArrayList<MerchantDTO> merchants, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	Result createMerchantDetails(MerchantDTO dto, DataControllerRequest request, Map<String, Object> inputs)
			throws ApplicationException;
	public JSONArray getMerchantDetails(String code,
			Map<String, Object> headerMap)throws ApplicationException;
	public JSONArray getAllMerchants(Map<String, Object> headerMap)throws ApplicationException; 
		

}
