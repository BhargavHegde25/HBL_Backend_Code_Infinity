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
import com.temenos.infinity.api.wealthOrder.backenddelegate.api.InstrumentMinimalBackendDelegate;
import com.temenos.infinity.api.wealthOrder.config.WealthAPIServices;
import com.temenos.infinity.api.wealthOrder.constants.OperationName;
import com.temenos.infinity.api.wealthOrder.constants.ServiceId;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;

public class InstrumentMinimalBackendDelegateImpl implements InstrumentMinimalBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Result getInstrumentMinimal(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		Map<String, Object> headerMap = new HashMap<>();
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String instrumentId = null;
		if (inputParams.get(TemenosConstants.INSTRUMENTID) != null
				&& inputParams.get(TemenosConstants.INSTRUMENTID).toString().trim().length() > 0) {
			instrumentId = inputParams.get(TemenosConstants.INSTRUMENTID).toString();
			inputMap.put("instrumentsId", instrumentId);
			inputMap.put(TemenosConstants.INSTRUMENTID, instrumentId);
		} else {
			return PortfolioWealthUtils.validateMandatoryFields(TemenosConstants.INSTRUMENTID);
		}

		if (inputParams.get(TemenosConstants.APPLICATION) != null
				&& inputParams.get(TemenosConstants.APPLICATION).toString().trim().length() > 0) {
			inputMap.put(TemenosConstants.APPLICATION, inputParams.get(TemenosConstants.APPLICATION).toString());
		}

		String createResponse = null;
		String serviceName = ServiceId.WEALTHORCHESTRATION;
		String operationName = OperationName.GETINSTRUMENTMINIMAL;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputMap).withRequestHeaders(headerMap)
					.withDataControllerRequest(request).build().getResponse();
			JSONObject resultJSON = new JSONObject(createResponse);
			return Utilities.constructResultFromJSONObject(resultJSON);
		} catch (Exception e) {
			alert.prepareError("Error while invoking Service - "
					+ WealthAPIServices.WEALTH_GETINSTRUMENTREFEDETAILS.getOperationName() + "  : " + e).log();
		}

		return null;

	}

}
