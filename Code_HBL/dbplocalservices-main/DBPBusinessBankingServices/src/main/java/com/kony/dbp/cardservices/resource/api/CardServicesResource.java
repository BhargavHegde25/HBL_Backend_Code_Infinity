package com.kony.dbp.cardservices.resource.api;
import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


/**
 * 
 * @author KH2394
 * @version 1.0
 * Interface for CardServicesResource extends {@link Resource}
 *
 */
public interface CardServicesResource extends Resource{
	/**
	 *  method to activate cards
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  
	 */
	public Result activateCards(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	public Result applyForDebitCard(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	public Result getStatements(String card_id, String month, String userId);
	
	public Result fetchCardTransactions(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);	
    public Result getCardProducts(String methodID, Object[] inputArray, DataControllerRequest request,
					DataControllerResponse response);	
	public Result updateCardTransaction(String methodID, Object[] inputArray, DataControllerRequest request,
							DataControllerResponse response);
	public Result applyForCreditCard(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	
}
