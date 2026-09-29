package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.resource.api.ArrangementsResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

/**
 * 
 * @author KH2281
 * @version 1.0 Java Service end point to fetch all the transactions of a particular account
 */

public class GetAccountPreviewOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        try {
            // Initializing of Arrangements through Abstract factory method
            ArrangementsResource AccountsResource = DBPAPIAbstractFactoryImpl.getResource(ArrangementsResource.class);
            String authToken="";
            Map<String, String> inputParams = new HashMap<String, String>();
            String userName = request.getParameter("userName");
            String did = request.getParameter("deviceID");
            String userId = inputParams.get("userId");

            // If product line is null then set default to ACCOUNT
            String productLineId = request.getParameter("productLineId");
            if (StringUtils.isBlank(productLineId)) {
                productLineId = "ACCOUNTS";
            }

            if (StringUtils.isBlank(userId) && StringUtils.isNotBlank(userName)) {
                result = AccountsResource.getUserDetailsFromDBX(userName, request);
                Record record = result.getDatasetById("records").getRecord(0);
                String backendId = record.getParamValueByName("backendUserId");
                String customerType = record.getParamValueByName("customerType");
                String customerID = record.getParamValueByName("customerID");

                inputParams.put("userId", backendId);
                inputParams.put("customerType", customerType);
                inputParams.put("customerID", customerID);
            }
            
			try {
				Map<String, String> inputMap = new HashMap<>();
				inputMap.put("customerId",ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"));
				authToken = TokenUtils.getAMSAuthToken(inputMap);
    			CommonUtils.setCompanyIdToRequest(request);
    		} catch (CertificateNotRegisteredException e) {
    			alert.prepareError("Certificate Not Registered" + e).log();
    		} catch (Exception e) {
    			alert.prepareError("Exception occured during generation of authToken " + e).log();
    		}

            try {
                result = AccountsResource.getArrangementPreviewAccounts(did, userName, inputParams.get("userId"),
                        inputParams.get("customerType"), productLineId, inputParams.get("customerID"), request, authToken);
            } catch (Exception e) {
                return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
            }
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
        }

        return result;
    }
}
