package com.temenos.infinity.api.arrangements.businessdelegate.api;

import java.util.List;

import org.json.JSONObject;
import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface MortgageFacilityDrawingBusinessDelegate extends BusinessDelegate {

	Result getMortageFacilityDrawing(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception;

}
