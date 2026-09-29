package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.LegalEntityUtil;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GetBasicInfoOperation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {

        Result resultPS = new Result();
        String id = request.getParameter("Customer_id");
        Map<String, String> postParametersMap = new HashMap<>();
        postParametersMap.put("Customer_id", id);
        String endPointResponse =
                Executor.invokeService(ServiceURLEnum.CUSTOMER_SEARCH_PARTY_MS, postParametersMap, null, request);
        if (endPointResponse != null) {
            JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(endPointResponse);
            resultPS = CommonUtilities.getResultObjectFromJSONObject(serviceResponseJSON);
            return resultPS;
        }
        Result processedResult = new Result();
        String customerId = request.getParameter("Customer_id");
        String username = request.getParameter("Customer_username");
        String legalEntityId = request.getParameter("legalEntityId");
        
        
        try {
        	
        	if(StringUtils.isBlank(legalEntityId)) {
        		 throw new ApplicationException(ErrorCodeEnum.ERR_22230);
             }
            CustomerHandler.computeCustomerBasicInformation(request, processedResult, customerId, username, legalEntityId);
            return processedResult;
            
        } catch (DBPAuthenticationException dbpException) {

            ErrorCodeEnum.ERR_20933.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", dbpException.getMessage(), FabricConstants.STRING));
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20717.setErrorCode(processedResult);
            processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
        }
        return null;
    }

}
