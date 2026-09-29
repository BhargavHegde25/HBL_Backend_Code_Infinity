package com.temenos.dbx.transaction.javaservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.transaction.resource.api.DirectDebitsResource;

import org.apache.commons.lang3.StringUtils;
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
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;

/**
 * @author sribarani.vasthan
 */
public class CancelDirectDebit implements JavaService2 {
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
		featureActionIdList.add("DIRECT_DEBIT_CANCEL");
		permission= CustomerSession.getPermittedActionIds(request,featureActionIdList);
		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getServerAppProperty("PAYMENT_BACKEND");
		if (permission == null) {
			alert.prepareError("Logged in user is not allowed to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		try {
			// For T24 Service Call
			if ("SRMS".equalsIgnoreCase(PAYMENT_BACKEND)) {
				HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
				HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
				String serviceName = "ServiceRequestJavaService";
				String operationName = "createOrder";
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			} else {
				// Making a call to Stub API
				DirectDebitsResource directDebitsResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(DirectDebitsResource.class);

				result = directDebitsResource.cancelDirectDebit(methodID, inputArray, request, response);
				return result;

			}
		} catch (Exception e) {
			alert.prepareError("Caught exception in invoke method : " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
		return postprocess(result);
	}
	private Result postprocess(Result result) {
        Result retResult =result.getCopy();
        List<Param> list2 = result.getAllParams();
        for(Param i: list2) {
        	if(!StringUtils.isNotBlank(i.getValue())) {
        		retResult.removeParamByName(i.getName());
        	}
        }
        return retResult;
    }
}
