package com.temenos.infinity.api.accountaggregation.service;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountaggregation.constant.ErrorCodeEnum;
import com.temenos.infinity.api.accountaggregation.resource.api.AccountAggregationResource;
import com.temenos.infinity.api.accountaggregation.resource.impl.AccountAggregationResourceImpl;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

public class AccountAggregationCreateTermsAndConditionsOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        try {

            AccountAggregationResource customerResource =
                    DBPAPIAbstractFactoryImpl.getResource(AccountAggregationResource.class);
            String scopes = request.getParameter("scopes").replace("'", "\"");
            String fetch_scopes = request.getParameter("fetch_scopes").replace("'", "\"");
            String operation =request.getParameter("operation");
            String authToken = "";
            try {
    			authToken = TokenUtils.getAccAggMSAuthToken(request);
    		} catch (CertificateNotRegisteredException e) {
    			alert.prepareError("Certificate Not Registered" + e).log();
    		} catch (Exception e) {
    			alert.prepareError("Exception occured during generation of authToken " + e).log();
    		}
           // String CompanyId = AccountAggregationUtils.getUserAttributeFromIdentity(request, MSCertificateConstants.COMPANY_ID);
            String CompanyId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
            Result result = customerResource.createTermsAndConditions(request.getParameter("digitalProfileId"),
                    request.getParameter("javascript_callback_type"), request.getParameter("from_date"), scopes,
                    request.getParameter("providerCode"), request.getParameter("period_days"), fetch_scopes,operation, authToken, CompanyId);
            return result;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
        }
    }

}
