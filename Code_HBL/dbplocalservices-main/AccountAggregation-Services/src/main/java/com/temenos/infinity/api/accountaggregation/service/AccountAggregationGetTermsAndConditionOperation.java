package com.temenos.infinity.api.accountaggregation.service;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountaggregation.resource.api.AccountAggregationResource;
import com.temenos.infinity.api.accountaggregation.utils.AccountAggregationUtils;
import com.kony.dbputilities.util.Log4j2Configurator;

public class AccountAggregationGetTermsAndConditionOperation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        AccountAggregationResource getTermsResource =
                DBPAPIAbstractFactoryImpl.getResource(AccountAggregationResource.class);
        String termsAndConditionsCode=request.getParameter("termsAndConditionsCode");
        String languageCode = request.getParameter("languageCode");
        String operation = request.getParameter("operation");
        String bankCode=request.getParameter("bankCode");
        Result result = new Result();
        if(!isValidInputParams(termsAndConditionsCode,languageCode,operation,bankCode)) {
        	result.addOpstatusParam(8001);
        	result.addErrMsgParam("Invalid Input Params");
        	return result;
        }
        result = getTermsResource.getTermsAndCondition(request.getParameter("termsAndConditionsCode"),
                request.getParameter("languageCode"), request.getParameter("operation"),
                request.getParameter("bankCode"), request.getHeaderMap());

        return result;

    }
    
    private boolean isValidInputParams(String termsAndConditionsCode,String languageCode,String operation,String bankCode) {
    	boolean isValid=true;
    	String validChars="^[a-zA-Z0-9\\s._-]+$";
    	 String bankCodes = AccountAggregationUtils.readFromCache("BankCodes");
         if(!bankCodes.contains(bankCode)  ) {
        	 isValid = false;
         }else if(termsAndConditionsCode==null || !termsAndConditionsCode.matches(validChars)) {
        	 isValid = false;
         }else if(languageCode==null || !languageCode.matches(validChars)) {
        	 isValid = false;
         }else if(operation==null || !operation.matches(validChars)) {
        	 isValid = false;
         }
    	return isValid;
    }

}
