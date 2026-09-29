package com.temenos.dbx.product.commons.resource.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.backenddelegate.api.CancellationReasonBackendDelegate;
import com.temenos.dbx.product.commons.dto.CancellationReasonDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.resource.api.CancellationReasonsResource;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class CancellationReasonsResourceImpl implements CancellationReasonsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	
	
	@Override
	/*
	 * public Result fetchCancellationReasons(String methodID, Object[] inputArray,
	 * DataControllerRequest request, DataControllerResponse response) { // TODO
	 * Auto-generated method stub
	 * 
	 * return null; }
	 */
	public Result fetchCancellationReasons(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		CancellationReasonBackendDelegate cancellationReasonBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CancellationReasonBackendDelegate.class);
		
		Result result;
		
		
		List<CancellationReasonDTO> reasons = cancellationReasonBackendDelegate.fetchCancellationReasons(request);
		
		if(reasons == null) {
            alert.prepareError("Error occurred while fetching cancellation reasons from backend").log();
            return ErrorCodeEnum.ERR_21218.setErrorCode(new Result());
        }
     
	    try {
            JSONArray records = new JSONArray(reasons);
            JSONObject resultObject = new JSONObject();
            resultObject.put(Constants.CANCELLATIONREASONS,records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_21218.setErrorCode(new Result());
        }

		return result;
	}
	
	
}
