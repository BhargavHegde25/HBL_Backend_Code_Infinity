package com.hbl.productservicesExtn.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.dbp.transactionslimitengine.utils.TransactionsLimitConstants;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;
import com.hbl.productservicesExtn.api.ScheduledTransactionsBackendDelegate;
import com.hbl.productservicesExtn.exception.CustomException;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.dto.InterBankFundTransferDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class ScheduledTransactionsBackendDelegateImpl implements ScheduledTransactionsBackendDelegate{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@Override
	public List<InterBankFundTransferDTO> GetScheduledTransactions(Map<String, Object> inputMap, DataControllerRequest request)
			throws ApplicationException {
		List<InterBankFundTransferDTO> interbankfundtransferdto = null;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_INTERBANKFUNDTRANSFERS_GET;
		Map<String, Object> requestParams =generateFilter(inputMap);
		alert.prepareError("inputmap:" + requestParams).log();
		try {
			String updateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(updateResponse);
			String dbpErrCode= jsonRsponse.has("dbpErrCode")?jsonRsponse.getString("dbpErrCode"):"";
			String dbpErrMsg= jsonRsponse.has("dbpErrMsg")?jsonRsponse.getString("dbpErrMsg"):"";
			if(StringUtils.isNotBlank(jsonRsponse.optString("dbpErrMsg"))) {
				throw new CustomException(dbpErrCode, dbpErrMsg);
			}else {
			JSONArray interbankJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);//jsonRsponse.optJSONArray("interbankfundtransfers");
			interbankfundtransferdto= new ArrayList<InterBankFundTransferDTO>();
			alert.prepareError("Scheduled Interbank Json Response:" + interbankJsonArray).log();
			//interbankfundtransferdto = JSONUtils.parseAsList(interbankJsonArray.toString(), InterBankFundTransferDTO.class);
			for(int i=0;i<interbankJsonArray.length();i++) {
			JSONObject obj=interbankJsonArray.getJSONObject(i);
			String excternalServiceResponse=obj.has("externalServiceResponse")?obj.getString("externalServiceResponse"):"";
			String externalServicePayload=obj.has("externalServicePayload")?obj.getString("externalServicePayload"):"";
			String internalServicePayload=obj.has("internalServicePayload")?obj.getString("internalServicePayload"):"";
			String internalServiceResponse=obj.has("internalServiceResponse")?obj.getString("internalServiceResponse"):"";
			InterBankFundTransferDTO dto=JSONUtils.parse(obj.toString(), InterBankFundTransferDTO.class);
			if(StringUtils.isNotBlank(excternalServiceResponse)){
			JSONObject jsonExcternalServiceResponse = new JSONObject(excternalServiceResponse);
			dto.setExternalServiceResponse(jsonExcternalServiceResponse);
			}
			if(StringUtils.isNotBlank(externalServicePayload)){
			JSONObject jsonExternalServicePayload = new JSONObject(externalServicePayload);
			dto.setExternalApiPayload((HashMap<String, Object>) jsonExternalServicePayload.toMap());
			}
			if(StringUtils.isNotBlank(internalServicePayload)){
			JSONObject jsonInternalServicePayload = new JSONObject(internalServicePayload);
			dto.setInternalApiPayload((HashMap<String, Object>) jsonInternalServicePayload.toMap());
			}
			if(StringUtils.isNotBlank(internalServiceResponse)){
			JSONObject jsonInternalServiceResponse = new JSONObject(internalServiceResponse);
			dto.setInternalServiceResponse(jsonInternalServiceResponse);
			}
			interbankfundtransferdto.add(dto);
			}
			}
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while getting the interbankfundtransfer",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while getting the interbankfundtransfer",exp).log();
			return null;
		}
		
		if(interbankfundtransferdto != null) {
			return interbankfundtransferdto;
		}
		
		return null;
	}
	private Map<String, Object> generateFilter(Map<String, Object> inputMap) {
	Map<String, Object> requestParams = new HashMap<String, Object>();
	String filterQuery = "";
	String status =inputMap.get("status")!=null?inputMap.get("status").toString():"Scheduled";
	String customerId=inputMap.get("userId")!=null?inputMap.get("userId").toString():null;
	String isScheduled=inputMap.get("isScheduled")!=null?inputMap.get("isScheduled").toString():"1";
	String filter= inputMap.get("filter")!=null?inputMap.get("filter").toString():"UPCOMING_SCHEDULED";
	if (StringUtils.isNotBlank(customerId)) {
		filterQuery = filterQuery + "createdby eq '" + customerId + "'";
		if (StringUtils.isNotBlank(isScheduled)) {
			filterQuery = filterQuery + "and isScheduled eq '" + isScheduled + "'";
		}
		if(StringUtils.isNotBlank(filter) && filter.equalsIgnoreCase("UPCOMING_SCHEDULED")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String dateFrom = formatter.format(LocalDate.now());
			filterQuery = filterQuery + "and scheduledDate ge '" + dateFrom + "' and status eq '"+status+"'";
			requestParams.put("$top", "5");
		}
	}else {
		filterQuery = filterQuery + "status eq '"+status+"' and isScheduled eq '" + isScheduled + "'";
	}
	if(StringUtils.isNotBlank(filterQuery)) {
	requestParams.put(TransactionsLimitConstants.FILTER, filterQuery);
	requestParams.put("$orderby", "scheduledDate desc");
	}
	return requestParams;
	}
			
	

}
