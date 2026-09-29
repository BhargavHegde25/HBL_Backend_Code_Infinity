package com.hbl.productservicesExtn.javaservice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbl.productservicesExtn.api.ScheduledTransactionsBackendDelegate;
import com.hbl.productservicesExtn.impl.ScheduledTransactionsBackendDelegateImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class GetPendingScheduledTrasactions implements JavaService2 {
	// private static final Logger LOG =
	// LogManager.getLogger(GetPendingScheduledTrasactions.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		ScheduledTransactionsBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(ScheduledTransactionsBackendDelegate.class);
		Result result = new Result();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		List<InterBankFundTransferDTO> scheduledTransArry = backendDelegate.GetScheduledTransactions(inputParams, request);
		alert.prepareError("scheduledTransArry:" + scheduledTransArry).log();
		if (scheduledTransArry != null) {
			JSONArray pendingTransactions = getPendingScheduledTrans(scheduledTransArry);
			alert.prepareError("pendingTransactions:" + pendingTransactions).log();
			JSONObject scheduledTransactions = new JSONObject();
			scheduledTransactions.put("scheduledTransactions", pendingTransactions);
			result.appendJson(scheduledTransactions.toString());
			result.addParam(new Param("opstatus", "0"));
			result.addParam(new Param("httpStatusCode", "200"));
			result.addParam(new Param("success", "true"));
		} else {
			result.addParam(new Param("dbpErrCode", "10002"));
			result.addParam(new Param("httpStatusCode", "500"));
			result.addParam(new Param("dbpErrMsg", "Failed to fetch pending domestic transactions "));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	public JSONArray getPendingScheduledTrans(List<InterBankFundTransferDTO> scheduledTransArry) {
		JSONArray transArray = new JSONArray();
		alert.prepareError("scheduledTransArry.size:" + scheduledTransArry.size()).log();
		for (int i = 0; i < scheduledTransArry.size(); i++) {
			InterBankFundTransferDTO interbankDTO = scheduledTransArry.get(i);
			String scheduledDt = interbankDTO.getScheduledDate();
			Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
			alert.prepareError("scheduledTransArry.scheduledDate:" + scheduledDate).log();
			alert.prepareError("scheduledTransArry.scheduledDate.after(new Date()):" + scheduledDate.after(new Date()))
			.log();
			try {
				ObjectMapper mapper = new ObjectMapper();
				//if (scheduledDate.after(new Date())) {
					String jsonString = mapper.writeValueAsString(interbankDTO);
					JSONObject pendingTrans = new JSONObject(jsonString);
					transArray.put(pendingTrans);
				//}
			} catch (JsonProcessingException e) {
				alert.prepareError("Json Exception:" + e.getMessage()).log();
			}
		}
		return transArray;
	}

}
