package com.temenos.infinity.api.srmsservices.javaservices;

import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.transactionservices.dto.P2PTransactionDTO;
import com.temenos.infinity.api.srmsservices.constants.SRMSConstants;
import com.temenos.infinity.api.srmstransactions.config.TransformSRMSRequest;

public class SRMSCreateP2PTransaction implements SRMSConstants, JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		@SuppressWarnings("unchecked")
		Map<String, Object> requestparameters = (Map<String, Object>) inputArray[1];

		P2PTransactionDTO p2pTransactionDTO = null;

		// Convert DBPDto to SRMS DTO to filter out the unwanted fields to be
		// sent to SRMS
		TransformSRMSRequest transformObject = TransformSRMSRequest.getInstance();
		Map<String, Object> srmsInputParams = transformObject.P2PTransfer(requestparameters, request);
		Result result = new Result();
		try {
			diagnostic.prepareDebug("Inter bank Account Create Order Request :" + srmsInputParams.toString()).log();
			String srmsresponse = Util.createOrder(srmsInputParams, request);
			diagnostic.prepareDebug("Inter bank Account Create Order Response :" + srmsresponse).log();
			p2pTransactionDTO = JSONUtils.parse(srmsresponse, P2PTransactionDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to create p2p transaction: ", e).log();
			return new Result();
		} catch (Exception e) {
			alert.prepareError("Caught exception at create p2p transaction: ", e).log();
			return new Result();
		}

		try {
			JSONObject resObj = new JSONObject(p2pTransactionDTO);
			String res = resObj.toString();
			result = JSONToResult.convert(res);

		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			result.addParam(new Param("dbpErrCode", "{\"errormsg\":\"" + e.getMessage() + "\"}"));
			return result;
		}
		return result;
	}
}
