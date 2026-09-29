package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.GetAccountsArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDetailsAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.backenddelegate.api.MortgageFacilityDrawingAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.prop.AccountTypeProperties;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

public class MortgageFacilityDrawingAPIBackendDelegateImpl
        implements MortgageFacilityDrawingAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getMortageFacilityDrawingBackendDelegate(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
        Result result = new Result();
        try {
        	HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String serviceName = "mockMortagageMS";
			String operationName = "getMortgageDrawings";
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        return result;
	}

}
