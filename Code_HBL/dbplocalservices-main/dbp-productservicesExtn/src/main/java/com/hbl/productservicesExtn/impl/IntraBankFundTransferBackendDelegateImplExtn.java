package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hbl.productservicesExtn.dto.IntraBankFundTransferBackendDTOExtn;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.backenddelegate.impl.IntraBankFundTransferBackendDelegateImpl;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferBackendDTO;
import com.temenos.dbx.product.transactionservices.dto.IntraBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class IntraBankFundTransferBackendDelegateImplExtn extends IntraBankFundTransferBackendDelegateImpl {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public IntraBankFundTransferDTO createTransactionWithoutApproval(IntraBankFundTransferBackendDTOExtn intrabankfundtransferbackenddtoExtn, DataControllerRequest request) {
		String serviceName = ServiceId.INTRA_BANK_FUND_TRANSFER_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.INTRA_BANK_FUND_TRANSFER_BACKEND_WITHOUT_AAPROVER;

		String createResponse = null;
		IntraBankFundTransferDTO intrabankfundtransferdto = null;

		Map<String, Object> requestParameters;
		try {
			intrabankfundtransferbackenddtoExtn.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			JSONObject additionalInfo = intrabankfundtransferbackenddtoExtn.getAdditionalInformation();
			alert.prepareError("IntraBankFundTransferBackendDelegateImplExtn:IntraBankFundTransfer:intrabankfundtransferbackenddtoExtn.additionalInfo():"+ additionalInfo).log();
			requestParameters = JSONUtils.parseAsMap(new JSONObject(intrabankfundtransferbackenddtoExtn).toString(), String.class, Object.class);
			alert.prepareError("IntraBankFundTransferBackendDelegateImplExtn:IntraBankFundTransfer:requestParameters:"+ requestParameters).log();
			prepareAdditionalInfo(requestParameters,additionalInfo);
			alert.prepareError("IntraBankFundTransferBackendDelegateImplExtn:IntraBankFundTransfer:requestParameters:"+ requestParameters).log();
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: ", e).log();
			return null;
		}

		try {
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(request.getHeaderMap()).
					withDataControllerRequest(request).
					build().getResponse();
			intrabankfundtransferdto = JSONUtils.parse(createResponse, IntraBankFundTransferDTO.class);
			if(intrabankfundtransferdto.getTransactionId() != null && !"".equals(intrabankfundtransferdto.getTransactionId())) {
				intrabankfundtransferdto.setReferenceId(intrabankfundtransferdto.getTransactionId());
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create intrabank transaction: ", e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create intrabank transaction: ", e).log();
			return null;
		}

		return intrabankfundtransferdto;
		
	}
	public void prepareAdditionalInfo(Map<String, Object> requestParameters, JSONObject additionalInfo) {
		if(additionalInfo!=null) {
		JSONArray additionalInfoAry = new JSONArray();
		Iterator<String> iter = additionalInfo.keys();
		while (iter.hasNext()) {
		    String key = iter.next();
		    String value=additionalInfo.get(key)!=null?additionalInfo.getString(key):"";
		    String keyValue=key+":"+value;
		    JSONObject jsonObj = new JSONObject();
		    jsonObj.put("additionalInformation", keyValue);
		    additionalInfoAry.put(jsonObj);
			}
		alert.prepareError("IntraBankFundTransferBackendDelegateImplExtn:IntraBankFundTransfer:prepareAdditionalInfo:additionalInfoAry:"+ additionalInfoAry).log();
		requestParameters.put("additionalInformations",additionalInfoAry);
		}
		
	}
}
