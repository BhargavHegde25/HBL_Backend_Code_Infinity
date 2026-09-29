package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.impl.InterBankFundTransferBusinessDelegateImpl;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class InterBankFundTransferBusinessDelegateImplExtn extends InterBankFundTransferBusinessDelegateImpl{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	
	@Override
	public  InterBankFundTransferDTO createTransactionAtDBX(InterBankFundTransferDTO interbankfundtransferdto) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_CREATE;
		String createResponse = null;
		
		Map<String, Object> requestParameters;
		Map<String, Object> internalServicePayload;
		Map<String, Object> externalServicePayload;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(interbankfundtransferdto).toString(), String.class, Object.class);
			internalServicePayload = interbankfundtransferdto.getInternalApiPayload();
			externalServicePayload = interbankfundtransferdto.getExternalApiPayload();
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		try {
			requestParameters.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
			requestParameters.put("transactionId", HelperMethods.getUniqueNumericString(13));
			JSONObject jsonPayload;
			if(internalServicePayload!=null) {
			jsonPayload = new JSONObject(internalServicePayload);
			requestParameters.put("internalServicePayload", jsonPayload.toString());
			}
			if(externalServicePayload!=null) {
			jsonPayload = new JSONObject(externalServicePayload);
			requestParameters.put("externalServicePayload", jsonPayload.toString());
			}
			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject response = new JSONObject(createResponse);
			JSONArray resposneArray = CommonUtils.getFirstOccuringArray(response);
			
			interbankfundtransferdto = JSONUtils.parse(resposneArray.getJSONObject(0).toString(), InterBankFundTransferDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to create interbank transaction entry into interbanktransfers table: "+ e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at create interbank transaction entry: "+ e).log();
			return null;
		}
		
		return interbankfundtransferdto;
	}
	public InterBankFundTransferDTO fetchExecutedTranscationEntryInDBX(String confirmationNumber, List<String> companyIds, String createdby , String legalEntityId) {
		
		List<InterBankFundTransferDTO> transactionDTO = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_GET;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "transactionId" + DBPUtilitiesConstants.EQUAL + confirmationNumber;
		
		String innerFilter = "";

		if (CollectionUtils.isNotEmpty(companyIds))
			innerFilter = innerFilter + DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;

		if (StringUtils.isNotBlank(innerFilter) && StringUtils.isNotBlank(createdby))
			innerFilter = innerFilter + DBPUtilitiesConstants.OR;

		if (StringUtils.isNotBlank(createdby))
			innerFilter = innerFilter + "createdby" + DBPUtilitiesConstants.EQUAL + createdby;
		
		if (StringUtils.isNotBlank(legalEntityId))
			innerFilter = DBPUtilitiesConstants.OPEN_BRACE + innerFilter + DBPUtilitiesConstants.CLOSE_BRACE
					+ DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;

		if (StringUtils.isNotBlank(innerFilter))
			filter = filter + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.OPEN_BRACE + innerFilter
					+ DBPUtilitiesConstants.CLOSE_BRACE;
		
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray trJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), InterBankFundTransferDTO.class);
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the interbankfundtransfers",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the interbankfundtransfers",exp).log();
			return null;
		}
		
		if(transactionDTO != null && transactionDTO.size() != 0)
			return transactionDTO.get(0);
		
		return null;
	}
	

}
