package com.kony.dbp.holidayservices.resource.api;
import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;


/**
 * 
 * @author KH2394
 * @version 1.0
 * Interface for HolidaysResource extends {@link Resource}
 *
 */
public interface HolidayServicesResource extends Resource{
	/**
	 *  method to get the Approval rules 
	 *  @param methodID operationName
	 *  @param inputArray input parameters 
	 *  @param request DataControllerRequest
	 *  @param response DataControllerResponse
	 *  @return array of JSON containing all the holidays
	 *  
	 */
	public Result getHolidays(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}
