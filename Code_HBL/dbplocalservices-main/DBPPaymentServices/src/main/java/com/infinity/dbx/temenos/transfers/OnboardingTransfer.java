package com.infinity.dbx.temenos.transfers;

import java.util.HashMap;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBaseService;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class OnboardingTransfer extends TemenosBaseService{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings("unchecked")
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		try {
			Result localResult = (Result) super.invoke(methodId, inputArray, request, response);
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			if (params == null) {
				CommonUtils.setErrMsg(localResult, "No input parameters provided");
				CommonUtils.setOpStatusError(localResult);
				return localResult;
			}
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			serviceHeaders.put("companyId", CommonUtils.getParamValue(params, "companyId"));
			String serviceName = TransferConstants.T24_SERVICE_NAME_ONBOARDING_TRANSFERS; //OnboardingTransfer
			String operationName = TransferConstants.ONBOARDING_TRANSFER_OPERATION; //createOneTimeTransfer
			String productId = TransferConstants.INTERNAL_TRANSFER_PRODUCT_ID; //ACTRF
			request.addRequestParam_(TransferConstants.PAYMENT_PRODUCT_ID, productId);
			params.put(TransferConstants.PAYMENT_PRODUCT_ID, productId); // Setting the paymentOrderProductId as ACTRF 
			result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
					true);
		}catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception Occured while Onboarding Transfer:" + e).log();
			CommonUtils.setOpStatusError(result);
			CommonUtils.setErrMsg(errorResult, e.getMessage());
			return errorResult;
		}
		return result;
	}
}
