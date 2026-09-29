package com.temenos.infinity.api.loanspayoff.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.loanspayoff.constants.ErrorCodeEnum;
import com.temenos.infinity.api.loanspayoff.resource.api.LoansPayoffResource;
import com.temenos.infinity.api.loanspayoff.resource.impl.LoansPayoffResourceImpl;

/**
 * 
 * @author suryaacharans
 * @version Java Service end point to create Loan Simulation for a given arrangement
 */
public class CreateSimulationOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            LoansPayoffResource simulateResource = DBPAPIAbstractFactoryImpl.getResource(LoansPayoffResource.class);
            String arrangementId = request.getParameter("arrangementId");
            String activityId = request.getParameter("activityId");
            String productId = request.getParameter("productId");
            String effectiveDate = new String();
            request.addRequestParam_("flowType", "PostLogin");
            String backendToken = TokenUtils.getT24AuthToken(request);
            if (request.getParameter("effectiveDate") != null && request.getParameter("effectiveDate").length() > 0)
                effectiveDate = request.getParameter("effectiveDate");
            Result result = simulateResource.createSimulation(arrangementId, activityId, productId, effectiveDate,
                    backendToken);
            return result;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());

        }

    }

}
