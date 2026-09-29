package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPErrorCodeSetter;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.resource.api.GetAccountsBySearchResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

public class GetAccountsBySearchOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private static final String ACCOUNTID = "Account_id";
    private static final String MEMBERSHIPID = "Membership_id";
    private static final String TAXID = "Taxid";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        @SuppressWarnings("unchecked")
        Map<String, String> inputParams = (Map<String, String>) inputArray[1];
        try {
        	String authToken="";
            GetAccountsBySearchResource accountsResource =
                    DBPAPIAbstractFactoryImpl.getResource(GetAccountsBySearchResource.class);
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
            result = accountsResource.getAccounts(inputParams.get(ACCOUNTID), inputParams.get(MEMBERSHIPID),
                    inputParams.get(TAXID),authToken, request);
        } catch (ApplicationException e) {
            alert.prepareError(e.getMessage()).log();
            DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), result);
        }
        return result;
    }

}
