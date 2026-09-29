/**
 * 
 */
package com.temenos.infinity.api.wealth.javaservice;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.ErrorCodeEnum;
import com.temenos.infinity.api.wealth.resource.api.WealthDashboardResource;

/**
 * 
 * @author s.subashini
 *
 */
public class GetPortfolioList implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
		WealthDashboardResource wealthDashboardResource = DBPAPIAbstractFactoryImpl.getInstance().
				getFactoryInstance(ResourceFactory.class).getResource(WealthDashboardResource.class);
		result = wealthDashboardResource.getPortfolioList(methodId, inputArray, request, response);
		}
		catch(Exception e) {
			alert.prepareError("Caught exception at invoke of GetPortfolioList: ", e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
		return result;
	}


}
