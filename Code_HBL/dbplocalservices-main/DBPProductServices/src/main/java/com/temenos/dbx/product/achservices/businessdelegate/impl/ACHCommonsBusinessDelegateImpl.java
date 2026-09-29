package com.temenos.dbx.product.achservices.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTaxSubTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHTaxTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionTypesDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRequestTypeDTO;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;

public class ACHCommonsBusinessDelegateImpl implements ACHCommonsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<BBTemplateRequestTypeDTO> fetchTemplateRequestTypes(String transactionType_id) {
		List<BBTemplateRequestTypeDTO> bbtemplateRequestTypeDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.BB_TEMPLATEREQUESTTYPE_GET;

		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = ACHConstants.Transaction_Type_ID + DBPUtilitiesConstants.EQUAL + Long.parseLong(transactionType_id);
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);

		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray requestTypeJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			bbtemplateRequestTypeDTO = JSONUtils.parseAsList(requestTypeJsonArray.toString(), BBTemplateRequestTypeDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONException occured while fetching the template request type", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Exception occured while fetching the template request type", exp).log();
			return null;
		}
		
		return bbtemplateRequestTypeDTO;
	}
	
	@Override
	public List<BBTemplateRequestTypeDTO> fetchTemplateRequestTypes() {
		List<BBTemplateRequestTypeDTO> bbtemplateRequestTypeDTO = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.BB_TEMPLATEREQUESTTYPE_GET;

		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(null).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray requestTypeJsonArray = CommonUtils.getFirstOccuringArray(jsonRsponse);
			bbtemplateRequestTypeDTO = JSONUtils.parseAsList(requestTypeJsonArray.toString(), BBTemplateRequestTypeDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONException occured while fetching the template request type", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Exception occured while fetching the template request type", exp).log();
			return null;
		}
		
		return bbtemplateRequestTypeDTO;
	}
	
	@Override 
	public List<ACHTransactionTypesDTO> fetchBBTransactionTypes(){
			
			List<ACHTransactionTypesDTO> transactionTypes = new ArrayList<>();
			
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			String operationName = OperationName.DB_BBTRANSACTIONTYPES_GET;
			
			JSONArray bbTransactionTypesRecords = new JSONArray();
			
			try {
				String serviceResponse = DBPServiceExecutorBuilder.builder().
						withServiceId(serviceName).
						withObjectId(null).
						withOperationId(operationName).
						withRequestParameters(null).
						withRequestHeaders(null).
						withDataControllerRequest(null).
						build().getResponse();
				JSONObject responseJson = new JSONObject(serviceResponse);
				bbTransactionTypesRecords = CommonUtils.getFirstOccuringArray(responseJson);
				transactionTypes = JSONUtils.parseAsList(bbTransactionTypesRecords.toString(), ACHTransactionTypesDTO.class);
			}	
			catch (JSONException e) {
				alert.prepareError("JSON Exception occurred while fetching transactionTypes " , e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Exception occurred while fetching transactionTypes " , e).log();
				return null;
			}
			
			return transactionTypes;
			
		}
	
	@Override
	public List<ACHTaxTypeDTO> fetchTaxType() {
		
		List<ACHTaxTypeDTO> taxTypes = new ArrayList<>();
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBTAXTYPE_GET;
		
		JSONArray bbTaxTypeRecords = new JSONArray();
		
		try {
			String serviceResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(null).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject responseJson = new JSONObject(serviceResponse);
			bbTaxTypeRecords = CommonUtils.getFirstOccuringArray(responseJson);
			taxTypes = JSONUtils.parseAsList(bbTaxTypeRecords.toString(), ACHTaxTypeDTO.class);
		}	
		catch (JSONException e) {
			alert.prepareError("JSON Exception occurred while fetching taxTypes " , e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Exception occurred while fetching taxTypes " , e).log();
			return null;
		}
		
		return taxTypes;
	}

	@Override
	public List<ACHTaxSubTypeDTO> fetchACHTaxSubType(String taxType) {
		
		List<ACHTaxSubTypeDTO> taxSubTypes = new ArrayList<>();
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BBTAXSUBTYPE_GET;
		
		JSONArray bbTaxSubTypes = new JSONArray();
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = ACHConstants.Tax_Type + DBPUtilitiesConstants.EQUAL + Integer.parseInt(taxType);
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			String serviceResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					withRequestHeaders(null).
					withDataControllerRequest(null).
					build().getResponse();
			JSONObject responseJson = new JSONObject(serviceResponse);
			bbTaxSubTypes = CommonUtils.getFirstOccuringArray(responseJson);
			taxSubTypes = JSONUtils.parseAsList(bbTaxSubTypes.toString(), ACHTaxSubTypeDTO.class);
		}catch(JSONException e) {
			alert.prepareError("JSON Exception occurred while fetching taxSubTypes ", e).log();
			return null;
		}catch(Exception e) {
			alert.prepareError("Exception occurred while fetching taxSubTypes ", e).log();
			return null;
		}
		return taxSubTypes;
		
	}
	
	@Override	
	public String getTransactionTypeById(int transactionTypeID) {
		String transactionType = null;
		try {
	        List<ACHTransactionTypesDTO> bbTransactionTypesDTO = fetchBBTransactionTypes();	
	        
	        if ( bbTransactionTypesDTO == null ) {
	        	return transactionType;
	        }
	        
	        for( ACHTransactionTypesDTO bbTransactionTypesDtoItem : bbTransactionTypesDTO ) {
	        	if( bbTransactionTypesDtoItem.getTransactionType_id() == transactionTypeID) {
	        	  transactionType = bbTransactionTypesDtoItem.getTransactionTypeName();
	        	  break;
	        	}
	        }
	        
	       return transactionType; 
        } catch (Exception e) {
            alert.prepareError("Exception occured while fetching transactionTypes", e).log();
            return transactionType;
        }		
	}

	@Override
	public Double getTotalAmountFromRecords(JSONArray records, boolean doesSubRecordExists) {
		
		double totalAmount = 0.0;
        for (int i = 0; i < records.length(); i++) {

            double recordAmount = 0.0;
            JSONObject record = records.getJSONObject(i);

            if (doesSubRecordExists) {
                JSONArray subRecords = new JSONArray();
                try {
                  subRecords = record.getJSONArray("SubRecords");
                } 
                catch (Exception exp) {
                   alert.prepareError("Exception occured while calculating total amount", exp).log();
                   return 0.0;
                }

                for (int j = 0; j < subRecords.length(); j++) {
                	JSONObject subrecord = subRecords.getJSONObject(j);
                    Double amount = Double.parseDouble(subrecord.getString("Amount"));
                	if(amount <= 0) {
                		alert.prepareError("Amount cannot be lesser than zero").log();
                		return 0.0;
                	}
                	else {
                		recordAmount = recordAmount + amount;
                	}
                }
            } else {
            	Double amount = Double.parseDouble(record.getString("Amount"));
            	if(amount <= 0) {
            		alert.prepareError("Amount cannot be lesser than zero").log();
            		return 0.0;
            	}
            	else {
            		recordAmount = amount;
            	}
            }

            totalAmount = totalAmount + recordAmount;
        }

        return totalAmount;
	}

	@Override
	public BBTemplateDTO validateTotalAmount(boolean doesSubRecordExists, String records, double maxAmount) {

		BBTemplateDTO template = new BBTemplateDTO();
		JSONArray recordsArray = new JSONArray(records);
		if (recordsArray.length() == 0) {
			return null;
		}

		Double totalAmount = getTotalAmountFromRecords(recordsArray, doesSubRecordExists);
		if(totalAmount != 0.0) {
			if (totalAmount <= 0 || totalAmount > maxAmount) {
				return null;
			}
			template.setTotalAmount(totalAmount);
		}
		return template;
	}
}
