package com.kony.adminconsole.service.productmanagement.resource.api;

import com.dbp.core.api.Resource;
import com.dbp.core.error.DBPApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface ProductResource extends Resource {
	
	public Result createProduct(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateProduct(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result getProducts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result createProductFacility(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result updateProductFacility(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result deleteProductFacility(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	public Result loadProductInformation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws DBPApplicationException;

	
	public Result getAllProductGroupsCampaign(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);
	
	
	public Result getProductsByProductGroup(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

}
