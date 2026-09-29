package com.bct.custom.resource.api;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;

import com.bct.custom.dto.MerchantFieldsDTO;
import com.bct.custom.dto.MerchantPaymentCharges;
import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface MerchantChargesResource extends Resource{
	public Result merchantChargesCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	public ArrayList<MerchantPaymentCharges> validateCreateMerchantCharges(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException;
	public ArrayList<MerchantPaymentCharges> validateEditMerchantCharges(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException;
	public ArrayList<MerchantPaymentCharges> validateDeleteMerchantCharges(JSONArray array, DataControllerRequest dcRequest)
			throws ApplicationException, IOException;

}
