package com.temenos.dbx.product.achservices.businessdelegate.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTemplateBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateSubRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateTypeDTO;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;

public class ACHTemplateBusinessDelegateImpl implements ACHTemplateBusinessDelegate{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

	@Override
	public BBTemplateDTO createTemplate(BBTemplateDTO templateDTO) {

		BBTemplateDTO resultDTO = new BBTemplateDTO();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TEMPLATE_CREATE;
		String createResponse = null;
		
		Map<String, Object> requestParams = null;

		try {

			JSONObject requestObj = new JSONObject(templateDTO);
			requestParams = JSONUtils.parseAsMap(requestObj.toString(), String.class, Object.class);
			if(templateDTO.getTemplateId() == 0) {
				requestParams.remove("templateId");
			}
			requestParams.put("createdts", new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));

			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt("opstatus") == 0 && responseObj.getInt("httpStatusCode") == 0) {
				JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
				responseObj = responseArray.getJSONObject(0);
				resultDTO = JSONUtils.parse(responseObj.toString(), BBTemplateDTO.class);
				
				List<BBTemplateRecordDTO> queryData = _getRecordDTOForCreation(templateDTO.getRecords(), resultDTO.getTemplateId(), resultDTO.getTemplateRequestType_id());
				if(queryData != null) {
					boolean isSuccess = _createTemplateRecordAndSubRecords(queryData);
					if(!isSuccess) {
						deleteTemplate(resultDTO.getTemplateId());
						return null;
					}
				}
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while creating an ACH template",exp).log();
			return null;
		}
		return resultDTO;
	}

	private boolean _createTemplateRecordAndSubRecords(List<BBTemplateRecordDTO> records) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TEMPLATE_RECORD_SUBRECORD_CREATE;

		String createResponse = null;

		Map<String, Object> requestParams = _getMapForTemplateRecordsAndSubRecords(records);

		try {

			createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt("opstatus") == 0 && responseObj.getInt("httpStatusCode") == 0) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while creating an ACH template",exp).log();
			return false;
		}
		return false;
	}

	/*
	 *@author KH2626
	 *@version 1.0
	 *@param listof record to create input string map for create procedure
	 ***/
	private Map<String, Object> _getMapForTemplateRecordsAndSubRecords(List<BBTemplateRecordDTO> records) {

		Map<String,Object> requestParam = new HashMap<String, Object>();

		String recordValues = _getRecordsValue(records);
		String subRecordValues = _getSubRecordsValue(records);

		requestParam.put("_recordvalues", recordValues);
		requestParam.put("_subRecordvalues", subRecordValues);

		return requestParam;
	}

	/*
	 *@author KH2626
	 *@version 1.0
	 *@param listof record to create input string for create procedure
	 ***/
	private String _getRecordsValue(List<BBTemplateRecordDTO> records) {

		StringBuilder recordValues = new StringBuilder("");
		int recordSize = records.size();

		for(int i=0; i<recordSize; i++) {

			BBTemplateRecordDTO record = records.get(i);		

			String recordName = record.getRecord_Name();
			recordName = (recordName == null) ? recordName : "\""+recordName+"\"";

			String additionalInfo = record.getAdditionalInfo();
			additionalInfo = (additionalInfo == null) ? additionalInfo : "\""+additionalInfo+"\"";

			Long taxTypeId = record.getTaxType_id();
			taxTypeId = (taxTypeId == 0)?null:taxTypeId;

			recordValues.append(recordName+",");
			recordValues.append(record.getToAccountNumber()+",");
			recordValues.append(record.getAbatrcNumber()+",");
			recordValues.append(record.getDetail_id()+",");
			recordValues.append(record.getAmount()+",");
			recordValues.append(additionalInfo+",");
			recordValues.append(record.getEin()+",");
			recordValues.append(record.getIsZeroTaxDue()+",");
			recordValues.append(record.getTemplate_id()+",");
			recordValues.append(taxTypeId+",");
			recordValues.append(record.getTemplateRequestType_id()+",");
			recordValues.append(record.getToAccountType());

			if( i < recordSize-1) {
				recordValues.append("|");
			}
		}
		return recordValues.toString();
	}

	/*
	 *@author KH2626
	 *@version 1.0
	 *@param listof record to create input string for create procedure
	 ***/
	private String _getSubRecordsValue(List<BBTemplateRecordDTO> records) {

		StringBuilder subRecordValues = new StringBuilder("");
		int recordSize = records.size();

		for(int i=0; i<recordSize; i++) {

			List<BBTemplateSubRecordDTO> subRecords = records.get(i).getSubRecords();
			if(subRecords == null) {
				subRecordValues.append(";");
			}
			else {
				int subRecordSize = subRecords.size();
			
				for(int j=0; j<subRecordSize; j++) {
				
					BBTemplateSubRecordDTO subRecord = subRecords.get(j);
				
					subRecordValues.append(subRecord.getAmount()+",");
					subRecordValues.append(subRecord.getTaxSubCategory_id());
				
					if( j < subRecordSize-1) {
						subRecordValues.append(";");
					}
				}
			}
			if( i < recordSize-1) {
				subRecordValues.append("|");
			}
		}
		return subRecordValues.toString();
	}

	@Override
	public List<BBTemplateDTO> fetchTemplate(BBTemplateDTO templateDTO) {
		List<BBTemplateDTO> resultDTO = null;
		try {
			HashMap<String, Object> fetchTemplateMap = new HashMap<>();
			String template_id = Long.toString(templateDTO.getTemplateId());

			fetchTemplateMap.put(DBPUtilitiesConstants.FILTER,
					"templateId" + DBPUtilitiesConstants.EQUAL + template_id);
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			String operationName = OperationName.DB_TEMPLATE_GET;
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(fetchTemplateMap).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray templateJsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			resultDTO = JSONUtils.parseAsList(templateJsonArray.toString(), BBTemplateDTO.class);
		}
		catch (Exception e){
			alert.prepareError("Exception occured while creating an ACH template",e).log();
			return null;
		}
		if(resultDTO != null && resultDTO.size() != 0)
			return resultDTO;
		return null;
	}

	@Override
	public List<BBTemplateRecordDTO> fetchTemplateRecords(BBTemplateDTO templateDTO) {
		List<BBTemplateRecordDTO> resultDTO = null;
		try {
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			String operationName = OperationName.DB_GETALLTEMPLATERECORDS;
			HashMap<String, Object> fetchTemplateMap = new HashMap<>();
			String template_id = Long.toString(templateDTO.getTemplateId());
			fetchTemplateMap.put("_templateId",template_id);
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(fetchTemplateMap).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray templateJsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			resultDTO = JSONUtils.parseAsList(templateJsonArray.toString(), BBTemplateRecordDTO.class);
		}
		catch(Exception e){
			alert.prepareError("Exception occurred while fetching ACH Template records",e).log();
			return null;
		}
		if(resultDTO != null && resultDTO.size() != 0)
			return resultDTO;
		return null;
	}


	@Override
	public void updateTemplate(BBTemplateDTO bbTemplateDTO) {
		try{
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			String operationName = OperationName.DB_BBTEMPLATE_UPDATE;
			Map<String, Object> requestParameters;

			requestParameters = JSONUtils.parseAsMap(new JSONObject(bbTemplateDTO).toString(), String.class, Object.class);
			requestParameters.remove("createdts");
			requestParameters.remove("updatedts");
			
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			new JSONObject(fetchResponse);
		}
		catch(Exception e){
			alert.prepareError("Exception occurred while updating an ACH Template",e).log();
		}
	}

	@Override
	public List<BBTemplateSubRecordDTO> fetchTemplateSubRecord(BBTemplateRecordDTO bbTemplateRecordDTODTO) {
		List<BBTemplateSubRecordDTO> resultDTO = null;
		try {
			String templateRecord_id = Long.toString(bbTemplateRecordDTODTO.getTemplateRecord_id());
			HashMap<String, Object> paramsForFetchSubRecords = new HashMap<>();
			paramsForFetchSubRecords.put("_templateRecordId",templateRecord_id);
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			String operationName = OperationName.DB_GETALLTEMPLATESUBRECORDS;
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(paramsForFetchSubRecords).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject jsonResponse = new JSONObject(fetchResponse);
			JSONArray templateJsonArray = CommonUtils.getFirstOccuringArray(jsonResponse);
			resultDTO = JSONUtils.parseAsList(templateJsonArray.toString(), BBTemplateSubRecordDTO.class);
		}
		catch (Exception e){
			alert.prepareError("Exception occurred while fetching ACH Template sub-records",e).log();
			return null;
		}

		if(resultDTO != null && resultDTO.size() != 0)
			return resultDTO;
		return null;
	}

	@Override
	public List<BBTemplateTypeDTO> getACHTemplateType(){
		List<BBTemplateTypeDTO> resultDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBTEMPLATETYPE_GET;

		HashMap<String, Object> requestParameters = new HashMap<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();

		String achTemplateResponse = null;
		JSONArray records = null;
		try {
			achTemplateResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(requestHeaders).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject achTemplateTypeJSon = new JSONObject(achTemplateResponse);
			records = CommonUtils.getFirstOccuringArray(achTemplateTypeJSon);
		} catch (JSONException e) {
			alert.prepareError("Unable to fetch bb template types: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getACHTemplateType method: " + e).log();
			return null;
		}

		try {
			try {
				resultDTO = JSONUtils.parseAsList(records.toString(), BBTemplateTypeDTO.class);
			} catch (java.io.IOException e) {
				
				alert.prepareError("Unable to fetch records: " + e).log();
			}
		} 
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " + e).log();
			return null;
		}

		return resultDTO;
	}

	@Override
	public List<BBTemplateDTO> fetchAllACHTemplates(String customerId, FilterDTO filters , String templateID) {
		
		List<BBTemplateDTO> resultDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACH_TEMPLATES_GET;
		
		if(templateID == null){
			templateID = "";
		}
		
		Map<String, Object> requestMap;
		String fetchResponse = null;
		
		try {
			requestMap = JSONUtils.parseAsMap(JSONUtils.stringify(filters), String.class, Object.class);
			requestMap.put("_templateId", templateID);
			requestMap.put("_customerId", customerId);
			
			fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestMap).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray records = CommonUtils.getFirstOccuringArray(responseObj);
			resultDTO = JSONUtils.parseAsList(records.toString(), BBTemplateDTO.class);
		}
		catch(Exception exp) {
			alert.prepareError("Error Occurred while fetching templates",exp).log();
			return null;
		}
		return resultDTO;
	}
	
	@Override
    public BBTemplateDTO deleteTemplate(Long templateId) {
        
        BBTemplateDTO resultDTO = null;

        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_TEMPLATE_DELETE;
        
        Map<String, Object> requestMap;
        String deleteResponse = null;
        
        try {
            requestMap = new HashMap<String, Object>();
            requestMap.put("templateId", templateId);
            
            deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestMap).
					build().getResponse();
            
            JSONObject responseObj = new JSONObject(deleteResponse);
            resultDTO = JSONUtils.parse(responseObj.toString(), BBTemplateDTO.class);
        }
        catch(Exception exp) {
            alert.prepareError("Error Occurred while deleting a template",exp).log();
            return null;
        }
        return resultDTO;
    }
	
	@Override
	public boolean softDeleteTemplate(Long templateId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBTEMPLATE_UPDATE;
		
		Map<String, Object> requestMap;
		String deleteResponse = null;
		
		try {
			requestMap = new HashMap<String, Object>();
			requestMap.put("templateId", templateId);
			requestMap.put("softDelete", 1);
			
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestMap).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(deleteResponse);
			if(responseObj.getInt("opstatus") == 0 && responseObj.getInt("httpStatusCode") == 0 && responseObj.getInt("updatedRecords") == 1) {
				return true;
			}
		}
		catch(Exception exp) {
			alert.prepareError("Error Occurred while deleting a template",exp).log();
			return false;
		}
		return false;
	}
	
	/**
	 * @author KH2626
	 * @version 1.0
	 * @param recordData string form array which contains records
	 * @param templateId which contains the id of template created above
	 * @param templateRequestTypeId contains type of the template record 
	 * @return String containing queries for creating records and sub records
	 * **/
	private List<BBTemplateRecordDTO> _getRecordDTOForCreation(String recordsData, long templateId, long templateRequestType_id) {

		List<BBTemplateRecordDTO> recordsDTOList = null;

		try {
			JSONArray records = new JSONArray(recordsData);
			int numberOfRecords = records.length();

			for(int i=0; i < numberOfRecords; i++) {
				JSONObject record = records.getJSONObject(i);
				record.put("template_id", templateId);
				record.put("templateRequestType_id", templateRequestType_id);
			}
			recordsDTOList = JSONUtils.parseAsList(records.toString(), BBTemplateRecordDTO.class);

		} catch (IOException e) {
			alert.prepareError("Exception occured while updating records",e).log();
		}

		return recordsDTOList;
	}

}
