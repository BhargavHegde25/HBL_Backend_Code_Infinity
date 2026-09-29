package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to fetch Filtered Companies Data based on user's Search Query. 
 * 
 * @author Shivaansh Agarwal (KH9448)
 *
 */
public class CustomerGetFilteredCompanies implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
			String searchText = StringUtils.trim(request.getParameter("searchText"));
			return getFilteredCompanies(request, searchText);
		} catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
		}
		//return CommonUtilities.getStringAsJSONObject(readEndpointResponse);
	}
	
	private Result getFilteredCompanies(DataControllerRequest request, String searchText) 
			throws ApplicationException {
		
		Result processedResult = new Result();
		
		Map<String, String> searchStringMap = new HashMap<>();
		searchStringMap.put("_searchText", searchText);
		if(StringUtils.isNotBlank(searchText)) {
			String serviceResponse = Executor.invokeService(ServiceURLEnum.GET_FILTERED_COMPANIES_PROC, 
					searchStringMap, null, request);
			JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
			// Construct Companies Dataset
			JSONArray records = serviceResponseJSON.optJSONArray("records");
			Dataset companyDataset = CommonUtilities.constructDatasetFromJSONArray(records);
			companyDataset.setId("companies");
			processedResult.addDataset(companyDataset);
		}
		
		// Return result
		return processedResult;
		
	}

}
