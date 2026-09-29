package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.resource.api.DashboardResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;

/**
 * @author naveen.yerra
 */
public class UpdateTradeFinanceConfiguration implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            DashboardResource resource = DBPAPIAbstractFactoryImpl.getResource(
                    DashboardResource.class);
            Map<String, Object> input = (Map<String, Object>) inputArray[1];
            return resource.updateTradeFinanceConfiguration(input, request);
        }
        catch (Exception e) {
            return ErrorCodeEnum.ERR_26021.setErrorCode(new Result());
        }
    }

}
