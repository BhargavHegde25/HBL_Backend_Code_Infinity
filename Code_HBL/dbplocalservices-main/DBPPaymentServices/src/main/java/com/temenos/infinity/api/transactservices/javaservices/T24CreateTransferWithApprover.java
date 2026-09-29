package com.temenos.infinity.api.transactservices.javaservices;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.temenos.infinity.api.transactservices.constants.Constants;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;

public class T24CreateTransferWithApprover implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(ApplicationBusinessDelegate.class);

	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>) inputArray[1];
		if (!application.getIsStateManagementAvailableFromCache()) {
			requestparameters.put("referenceId",
					Constants.REFERENCE_KEY + requestparameters.get(Constants.PARAM_TRANSACTION_ID));
			return requestparameters;
		}
		String frequency = (requestparameters.get(Constants.PARAM_FREQUENCY_TYPE) != null)
				? requestparameters.get(Constants.PARAM_FREQUENCY_TYPE).toString()
				: null;
		if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
			return createOneTimePendingTransaction(requestparameters, request);
		} else {
			return createRecurringPendingTransaction(requestparameters, request);
		}
	}

	private static Result createOneTimePendingTransaction(Map<String, Object> requestParameters,
			DataControllerRequest request) {
		Result result = new Result();
		try {

			alert.prepareError("INPUT to BACKEND: " + new JSONObject(requestParameters).toString()).log();
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(TemenosConstants.SERVICE_T24IS_PAYMENTORDERS).withObjectId(null)
					.withOperationId(TemenosConstants.OP_CREATE_PAYMENT_WITH_APPROVER)
					.withRequestParameters(requestParameters).withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request).build().getResponse();
			result = JSONToResult.convert(response);
			return result;
		} catch (Exception e) {
			alert.prepareError("Caught exception at create pending transaction: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"" + e.getMessage() + "\"}"));
			return result;
		}
	}

	private static Result createRecurringPendingTransaction(Map<String, Object> requestParameters,
			DataControllerRequest request) {
		Result result = new Result();
		try {
			alert.prepareError("INPUT to BACKEND: " + new JSONObject(requestParameters).toString()).log();
			String response = DBPServiceExecutorBuilder.builder()
					.withServiceId(TemenosConstants.SERVICE_T24IS_STANDINGORDERS).withObjectId(null)
					.withOperationId(TemenosConstants.OP_CREATE_STANDINGORDER_WITH_APPROVER)
					.withRequestParameters(requestParameters).withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request).build().getResponse();
			result = JSONToResult.convert(response);
			return result;

		} catch (Exception e) {
			alert.prepareError("Caught exception at create pending transaction: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"" + e.getMessage() + "\"}"));
			return result;
		}
	}

}
