package com.temenos.dbx.party.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface DueDiligenceResource extends Resource {

	public Result updateCitizenship(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result createTaxDetail(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result updateTaxDetail(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result createEmploymentDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result updateEmploymentDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result getEmploymentDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result getTaxDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result getDueDiligenceDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result createDueDiligenceOperation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse, boolean isUpdate);

	Result updateAssetLiablities(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result updateFundsSource(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);
	
	Result getAssetLiablities(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

	Result getFundsSource(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse);

}
