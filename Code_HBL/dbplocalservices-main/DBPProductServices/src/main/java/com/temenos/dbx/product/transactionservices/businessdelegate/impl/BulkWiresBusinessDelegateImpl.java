package com.temenos.dbx.product.transactionservices.businessdelegate.impl;

import java.io.IOException;
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
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWiresBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BulkWireDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireTemplateLineItemDTO;
import com.temenos.dbx.product.transactionservices.dto.PayeeDTO;
import com.temenos.dbx.product.transactionservices.dto.WireTransactionDTO;

/**
 * @version 1.0
 * implements {@link BulkWiresBusinessDelegate}
 */
public class BulkWiresBusinessDelegateImpl implements BulkWiresBusinessDelegate{
    
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	public List<BulkWireDTO> getBulkWiresForUser(String createdBy, String sortByParam, String sortOrder, Object pageOffset, Object pageSize, String searchString,String bulkWireCategoryFilter, Boolean isDomesticFilePermitted,Boolean isInternationalFilePermitted,Boolean isDomesticTemplatePermitted,Boolean isInternationalTemplatePermitted){
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_BULKWIRES_FETCH;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put(Constants.CREATEDBY, createdBy);
        queryParams.put(Constants.SEARCHSTRING, searchString);
        queryParams.put(Constants.SORTBYPARAM, sortByParam);
        queryParams.put(Constants.SORTORDER, sortOrder);
        queryParams.put(Constants.PAGEOFFSET, pageOffset);
        queryParams.put(Constants.PAGESIZE, pageSize);
        queryParams.put(Constants.BULKWIRECATEGORYFILTER, bulkWireCategoryFilter);
        queryParams.put(Constants.FILEDOMESTICVIEW, isDomesticFilePermitted);
        queryParams.put(Constants.FILEINTERNATIONALVIEW, isInternationalFilePermitted);
        queryParams.put(Constants.TEMPLATEDOMESTICVIEW, isDomesticTemplatePermitted);
        queryParams.put(Constants.TEMPLATEINTERNATIONALVIEW, isInternationalTemplatePermitted);
        
		
        List<BulkWireDTO> bulkWireDTO = null;
        try {
            response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
	        } catch (Exception e) {
            alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
            return null;
        }
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray bulkWires = jsonResponse.getJSONArray(Constants.RECORDS);
            bulkWireDTO = JSONUtils.parseAsList(bulkWires.toString(), BulkWireDTO.class);
        } catch (IOException e) {
            alert.prepareError("Error while converting db service response to bulkWireDTO : " + e).log();
            return null;
        }
        return bulkWireDTO;
	}




	@Override
	public Object createBulkWireTemplate(String bulkWireTemplateString, String bulkWireTemplateLineItemsString) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_BULKWIRESTEMPLATE_CREATE;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_bulkwiretemplateValues", bulkWireTemplateString);
        queryParams.put("_bulkwiretemplatelineitemValues", bulkWireTemplateLineItemsString);
        
		
        BulkWireDTO bulkWireDTO = null;
        try {
        	response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
            JSONObject jsonResponse = new JSONObject(response).getJSONArray(Constants.RECORDS).getJSONObject(0);
            if(jsonResponse.has(Constants.ERROR_MESSAGE)) {
            	return jsonResponse.getString(Constants.ERROR_MESSAGE);
            }
            else {
                bulkWireDTO = JSONUtils.parse(jsonResponse.toString(), BulkWireDTO.class);
                return bulkWireDTO;
            }
            
        } catch (Exception e) {
            alert.prepareError("Error while converting db service response to bulkWireDTO in createBulkWireTemplate : " + e).log();
            return null;
        }        
	}

	
	@Override
	public Object updateBulkWireTemplate(String bulkWireTemplateString,String bulkWireTemplateLineItemsString_update, String bulkWireTemplateLineItemsString_create) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_BULKWIRESTEMPLATE_UPDATE;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_bulkwiretemplateValues", bulkWireTemplateString);
        queryParams.put("_update_bulkwiretemplatelineitemValues", bulkWireTemplateLineItemsString_update);
        queryParams.put("_insert_bulkwiretemplatelineitemValues", bulkWireTemplateLineItemsString_create);
        
		
        BulkWireDTO bulkWireDTO = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
			JSONObject jsonResponse = new JSONObject(response).getJSONArray(Constants.RECORDS).getJSONObject(0);
            if(jsonResponse.has(Constants.ERROR_MESSAGE)) {
            	return jsonResponse.getString(Constants.ERROR_MESSAGE);
            }
            else {
                bulkWireDTO = JSONUtils.parse(jsonResponse.toString(), BulkWireDTO.class);
                return bulkWireDTO;
            }
		} catch (Exception e) {
            alert.prepareError("Error while converting db service response to bulkWireDTO in updateBulkWireTemplate : " + e).log();
            return null;
        }
	
	}
	   
	public List<BulkWireTemplateLineItemDTO> getBulkWireTemplateLineItems(String bulkWireTemplateID,String sortByParam,String sortOrder, String searchString,String groupBy)  {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIRETEMPLATELINEITEMS_FETCH;

		Map<String, Object> queryParams = new HashMap<String, Object>();
		String allFileLineItemsResponse = null;
		List<BulkWireTemplateLineItemDTO> bulkWireTemplateLineItemsDTO = null;
		
		queryParams.put(Constants.BULKWIRETEMPLATEID, bulkWireTemplateID);
        queryParams.put(Constants.SEARCHSTRING, searchString);
        queryParams.put(Constants.SORTBYPARAM, sortByParam);
        queryParams.put(Constants.SORTORDER, sortOrder);
        queryParams.put(Constants.GROUPBY, groupBy);
        
        try {
        	allFileLineItemsResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
	    	 
		} catch (Exception e) {
			alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
			return null;
		}
		try {
			JSONObject bulkwireFileLineItemsJSON = new JSONObject(allFileLineItemsResponse);
			JSONArray records = bulkwireFileLineItemsJSON.getJSONArray(Constants.RECORDS);
			bulkWireTemplateLineItemsDTO = JSONUtils.parseAsList(records.toString(), BulkWireTemplateLineItemDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error while converting db service response to BulkWireTemplateLineItemDTO : " + e).log();
			return null;
		}
			return bulkWireTemplateLineItemsDTO;
	}

	
	public Boolean isTemplateAccessibleByUser(String templateId, Map<String,Object> customerMap) {
		 String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
	     String operationName = OperationName.DB_BULKWIRETEMPLATE_GET;
	     Map<String, Object> requestParams = new HashMap<String, Object>();
	     StringBuilder filter = new StringBuilder();
	     String response = null;
	     if(CustomerSession.IsRetailUser(customerMap))
	     {
	    	 String user_id = CustomerSession.getCustomerId(customerMap);
	    	 filter.append(Constants.CREATEDBY).append(DBPUtilitiesConstants.EQUAL).append(user_id);
	     }
	     else
	     {
	    	 String userOrg_id = CustomerSession.getCompanyId(customerMap);
	    	 filter.append(Constants.COMPANY_ID).append(DBPUtilitiesConstants.EQUAL).append(userOrg_id);
	     }
	     filter.append(DBPUtilitiesConstants.AND).append(Constants.BULKWIRETEMPLATEID).append(DBPUtilitiesConstants.EQUAL).append(templateId);
	     requestParams.put(DBPUtilitiesConstants.FILTER,  filter.toString());
	     try {
	    	 response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
						.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
	    	 
				JSONObject responseJson = new JSONObject(response);
				if(!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !responseJson.has(Constants.BULKWIRETEMPLATE_TABLE)) {
					return false;
				}
				JSONArray files = responseJson.getJSONArray(Constants.BULKWIRETEMPLATE_TABLE);
				if(files.length() != 0)
					return true;
				else
					return false;
				
		} catch (Exception e) {
			alert.prepareError("Error while fetching data from db service BulkWireFiles_get: " + e).log();
			return null;
		}
	 }
	
	@Override
	public Boolean deleteBulkWireTemplate(String bulkWireTemplateId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_BULKWIRESTEMPLATE_DELETE;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_bulkwiretemplateID", bulkWireTemplateId);
        queryParams.put("_bulkwiretemplatelineitemIDs", "");
        
		
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();

			JSONObject jsonResponse = new JSONObject(response);
			String status = jsonResponse.getJSONArray(Constants.RECORDS).getJSONObject(0).getString("SUCCESS");
			if("SUCCESS".equals(status)) {
				return true;
			}
			else {
				return false;
			}
		} catch (Exception e) {
            alert.prepareError("Error occurred in deleteBulkWireTemplate : " + e).log();
            return false;
        }
	}



	
	@Override
	public BulkWireDTO deleteBulkWireTemplateRecipient(String bulkWireTemplateId, String bulkWireTemplateLineItemIDs) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_BULKWIRESTEMPLATE_DELETE;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_bulkwiretemplateID", bulkWireTemplateId);
        queryParams.put("_bulkwiretemplatelineitemIDs", bulkWireTemplateLineItemIDs);
        
		
        BulkWireDTO bulkWireDTO = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();

			JSONObject jsonResponse = new JSONObject(response);
			JSONObject bulkWireTemplate = jsonResponse.getJSONArray(Constants.RECORDS).getJSONObject(0);
			bulkWireDTO = JSONUtils.parse(bulkWireTemplate.toString(), BulkWireDTO.class);
		} catch (Exception e) {
            alert.prepareError("Error while converting db service response to bulkWireDTO in deleteBulkWireTemplateRecipient : " + e).log();
            return null;
        }
        return bulkWireDTO;
	}

	@Override
	public BulkWireDTO updateBulkWireTemplate(Map<String, Object> BWTemplateDetails) {
		BulkWireDTO bulkwireDTO;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIRETEMPLATE_UPDATE;

		String bulkWiretemplateResponse = null;
		try {
			diagnostic.prepareDebug("In Business Delegate Method").log();
			bulkWiretemplateResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(BWTemplateDetails).build().getResponse();
			JSONObject jsonRsponse = new JSONObject(bulkWiretemplateResponse);
			JSONArray countJsonArray = jsonRsponse.getJSONArray("bulkwirefiletransactdetails");
			bulkwireDTO = JSONUtils.parse(countJsonArray.getJSONObject(0).toString(), BulkWireDTO.class);
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while creating the bulkwiretransfer: ",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while creating the bulkwiretransfer: ",exp).log();
			return null;
		}

		return bulkwireDTO;
	}



	@Override
	public List<PayeeDTO> getUnselectedPayeesForBWTemplate(String bulkWireTemplateId, String sortByParam, String sortOrder,
			String searchString, String user_id, int isDomesticPermitted, int isInternationalPermitted) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_FETCH_UNSELECTEDPAYEES_PROC;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_bulkwiretemplateID", bulkWireTemplateId);
        queryParams.put(Constants.USER_ID, user_id);
        queryParams.put(Constants.SEARCHSTRING, searchString);
        queryParams.put(Constants.SORTBYPARAM, sortByParam);
        queryParams.put(Constants.SORTORDER, sortOrder);
        queryParams.put("isDomesticPermitted", isDomesticPermitted);
        queryParams.put("isInternationalPermitted", isInternationalPermitted);
        
		
        List<PayeeDTO> payeeDTO = null;
        try {
            response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray payees = jsonResponse.getJSONArray(Constants.RECORDS);
            payeeDTO = JSONUtils.parseAsList(payees.toString(), PayeeDTO.class);
        } catch (Exception e) {
            alert.prepareError("Error while converting db service response to PayeeDTO : " + e).log();
            return null;
        }
        return payeeDTO;
	}
	
	@Override
	public JSONObject getBWTemplateDomesticInternationalCount(String bulkWireTemplateId, String bulkwiretemplatelineitemIDs){
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_FETCH_DOMESTICINTERNATIONALCOUNT_PROC;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_bulkwiretemplateID", bulkWireTemplateId);
        queryParams.put("_bulkwiretemplatelineitemIDs", bulkwiretemplatelineitemIDs);    
        
        try {
            response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
            JSONObject jsonResponse = new JSONObject(response).getJSONArray(Constants.RECORDS).getJSONObject(0);
            return jsonResponse;
        } catch (Exception e) {
            alert.prepareError("Error while converting db service response to PayeeDTO : " + e).log();
            return null;
        }
	}

	@Override
	public Boolean UpdateBulkWireTemplateRecipientCount(String userId, String companyId, String payeeId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_UPDATE_BWTEMPLATE_RECIPIENT_COUNT;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put("_userID", userId);
        if(companyId == null){
        	queryParams.put("_orgID", "");
        }
        else {
        	queryParams.put("_orgID", companyId);
        }
        queryParams.put("_payeeID", payeeId);
        
        try {
            response = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(queryParams).build().getResponse();
            JSONObject jsonResponse = new JSONObject(response).getJSONArray(Constants.RECORDS).getJSONObject(0);
            if("SUCCESS".equals(jsonResponse.getString("Response"))) {
            	return true;
            }
            else {
            	return false;
            }
        } catch (Exception e) {
            alert.prepareError("Error while invoking db service Update_BulkWireTemplateRecipient_Count : " + e).log();
            return null;
        }
	}
	
	@Override
	public Object fetchTransactionsByWireExecutionId(String wireFileExecutionId, String sortByParam, String sortOrder, String searchString, String statusFilter, int isDomesticPermitted, int isInternationalPermitted) {
		
		List<WireTransactionDTO> wireTransactionDTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIRETRANSACTIONEXECDETAILS_FETCH;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		//requestParams.put("transactionId", "");
		requestParams.put(Constants.BULKWIREFILEEXECUTION_ID, wireFileExecutionId);
		requestParams.put(Constants.STATUSFILTER, statusFilter);
		requestParams.put(Constants.SEARCHSTRING, searchString);
		requestParams.put(Constants.SORTBYPARAM, sortByParam);
		requestParams.put(Constants.SORTORDER, sortOrder);
		requestParams.put("isDomesticPermitted", isDomesticPermitted);
		requestParams.put("isInternationalPermitted", isInternationalPermitted);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();	
			JSONArray jsonResponse = new JSONObject(fetchResponse).getJSONArray(Constants.RECORDS);
            if(jsonResponse.length() > 0){
	        	if(jsonResponse.getJSONObject(0).has(Constants.ERROR_MESSAGE)) {
	            	return jsonResponse.getJSONObject(0).getString(Constants.ERROR_MESSAGE);
	            }
	        }
	        wireTransactionDTOs = JSONUtils.parseAsList(jsonResponse.toString(), WireTransactionDTO.class);
	        return wireTransactionDTOs;
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured while fetching the wiretransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching the wiretransaction",exp).log();
			return null;
		}
		
	}
	
	@Override
	public Object fetchTransactionsByWireTemplateExecutionId(String wireTemplateExecutionId, String sortByParam, String sortOrder, String searchString, String statusFilter, int isDomesticPermitted, int isInternationalPermitted) {
		
		List<WireTransactionDTO> wireTransactionDTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIRETEMPLATETRANSACTIONEXECDETAILS_FETCH;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		//requestParams.put("transactionId", "");
		requestParams.put(Constants.BULKWIRETEMPLATEEXECUTION_ID, wireTemplateExecutionId);
		requestParams.put(Constants.STATUSFILTER, statusFilter);
		requestParams.put(Constants.SEARCHSTRING, searchString);
		requestParams.put(Constants.SORTBYPARAM, sortByParam);
		requestParams.put(Constants.SORTORDER, sortOrder);
		requestParams.put("isDomesticPermitted", isDomesticPermitted);
		requestParams.put("isInternationalPermitted", isInternationalPermitted);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParams).build().getResponse();
			JSONArray jsonResponse = new JSONObject(fetchResponse).getJSONArray(Constants.RECORDS);
	        if(jsonResponse.length() > 0){
	        	if(jsonResponse.getJSONObject(0).has(Constants.ERROR_MESSAGE)) {
	            	return jsonResponse.getJSONObject(0).getString(Constants.ERROR_MESSAGE);
	            }
	        }
	        wireTransactionDTOs = JSONUtils.parseAsList(jsonResponse.toString(), WireTransactionDTO.class);
	        return wireTransactionDTOs;
		}
		catch(JSONException jsonExp) {
			alert.prepareError("JSONException occured while fetching the wiretransaction",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while fetching the wiretransaction",exp).log();
			return null;
		}
		
	}
}

