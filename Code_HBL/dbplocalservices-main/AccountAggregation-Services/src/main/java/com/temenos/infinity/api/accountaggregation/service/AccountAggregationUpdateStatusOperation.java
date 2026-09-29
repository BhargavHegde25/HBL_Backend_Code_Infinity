package com.temenos.infinity.api.accountaggregation.service;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountaggregation.resource.api.AccountAggregationResource;
import com.temenos.infinity.api.accountaggregation.resource.impl.AccountAggregationResourceImpl;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

public class AccountAggregationUpdateStatusOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        AccountAggregationResource providerResource =
                DBPAPIAbstractFactoryImpl.getResource(AccountAggregationResource.class);

        String data = request.getParameter("data");
        JSONObject dataObj = new JSONObject(data);

        String stage = dataObj.getString("stage");
        JSONObject custom_fields = dataObj.getJSONObject("custom_fields");

        String digitalProfileId = custom_fields.getString("digitalprofileid");
        String providerCode = custom_fields.getString("providercode");
        String authToken = "";
        try {
			authToken = TokenUtils.getAccAggMSAuthToken(request);
		} catch (CertificateNotRegisteredException e) {
			alert.prepareError("Certificate Not Registered" + e).log();
		} catch (Exception e) {
			alert.prepareError("Exception occured during generation of authToken " + e).log();
		}
        Result result = providerResource.updateStatus(stage, digitalProfileId, providerCode, authToken);
        return result;
    }
}
