package com.bct.custom.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface GetAllMerchantOperationsResource extends Resource {
	 public Result getAllMerchantCategories(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws ApplicationException;
	 public Result getMerchantSubCategoriesByCategoryName(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws ApplicationException;
	 public Result getAllMerchants(String methodID, Object[] inputArray,
				DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException;
	 public Result getMerchantsByCategory(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	 public Result getMerchantFields(String methodID, Object[] inputArray,
				DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException;
	 public Result getAllPaymentAggregators(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
				DataControllerResponse dcResponse) throws ApplicationException, com.kony.dbp.exception.ApplicationException;
	 public Result getPaymentAggregator(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
				DataControllerResponse dcResponse) throws ApplicationException;
	public Result getMerchantDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	public Result getMerchantSubCategoriesForAdmin(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	 public Result getMerchantCategoriesByMerchantCode(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
	            DataControllerResponse dcResponse) throws ApplicationException;
	
	
}
