package com.bct.custom.resource.api;

import java.util.Map;

import org.json.JSONArray;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.dataobject.Result;

public interface PaymentAggregatorResource extends Resource{
	public Result paymentAggregatorCRUDOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;

}
