package com.temenos.infinity.api.wealthOrder.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.DocumentsBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class DocumentsBackendDelegateImpl implements DocumentsBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override

	public Result getDocuments(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {

		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Object isinCodeobj = inputParams.get(TemenosConstants.ISIN);

		String isinCode = null;
		if (isinCodeobj != null && isinCodeobj.toString().trim().length() > 0) {
			isinCode = inputParams.get(TemenosConstants.ISIN).toString();
			inputMap.put("identifierValue", isinCode);
			inputMap.put(TemenosConstants.ISIN, isinCode);

		} /*else {
			return WealthUtils.validateMandatoryFields(TemenosConstants.ISIN);
		}*/

		String createResponse = null;
		String serviceName = ServiceId.WEALTHMKTORCHESTRATION;
		String operationName = OperationName.GETDOCUMENTS;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headersMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Service - " + WealthAPIServices.WEALTH_GETDOCUMENTS.getOperationName()
					+ "  : " + e).log();
		}

		return null;
	}

}
