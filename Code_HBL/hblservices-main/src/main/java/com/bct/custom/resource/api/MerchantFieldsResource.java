package com.bct.custom.resource.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantFieldsDTO;
import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MerchantFieldsResource extends Resource{
	public Result merchantFieldsCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	public ArrayList<MerchantFieldsDTO> validateCreateMerchantFieldsRequest(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException;
	public ArrayList<MerchantFieldsDTO> validateEditMerchantFields(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException;
	ArrayList<MerchantFieldsDTO> validateDeleteMerchantFields(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException;

}
