package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDetailsAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class MortgageFacilityDetailsAPIBackendDelegateImpl
        implements MortgageFacilityDetailsAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getMortageFacilityDetailBackendDelegate(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {
        	HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String serviceName = "mockMortagageMS";
			String operationName = "getMortgageFacilites";
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			alert.prepareError("result getMortgageDetails >> "+ResultToJSON.convert(result)).log();
			if(request.getParameter("arrangementId").toString() != null && request.getParameter("arrangementId").equalsIgnoreCase("122877")) {
				result.addParam(new Param("totalOutstandingBalance", "32010.00", "string"));	
			}else if(request.getParameter("arrangementId").toString() != null && request.getParameter("arrangementId").equalsIgnoreCase("122878")) {
				result.addParam(new Param("totalOutstandingBalance", "33010.00", "string"));
			}
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        return result;
	}

}
