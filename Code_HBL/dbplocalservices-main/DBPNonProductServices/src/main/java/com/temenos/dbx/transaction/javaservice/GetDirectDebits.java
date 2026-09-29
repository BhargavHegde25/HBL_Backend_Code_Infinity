package com.temenos.dbx.transaction.javaservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonObject;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.resource.api.DirectDebitsResource;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/**
 * @author sribarani.vasthan
 */
public class GetDirectDebits implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();

		//permission check for Customer
		String permission;
		List<String> featureActionIdList = new ArrayList<>();
		featureActionIdList.add("DIRECT_DEBIT_VIEW");
		permission= CustomerSession.getPermittedActionIds(request,featureActionIdList);
		if(permission == null) {
			alert.prepareError("Logged in user is not allowed to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		try {
            String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND");
            if ("MOCK".equalsIgnoreCase(PAYMENT_BACKEND) || "SRMS_MOCK".equals(PAYMENT_BACKEND) || "STUB".equalsIgnoreCase(PAYMENT_BACKEND)) {
                // creating an instance for DirectDebitsResource class For Mock
                // Data
                DirectDebitsResource directDebitsResource = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(ResourceFactory.class).getResource(DirectDebitsResource.class);

                result = directDebitsResource.getDirectDebits(methodID, inputArray, request, response);
				return result;
            }
			//For T24 Service Call
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String serviceName = "T24ISPaymentsView";
			String operationName = "getDirectDebits";
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
		} catch (Exception e) {
			alert.prepareError("Caught exception at invoke : " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;

	}
}
