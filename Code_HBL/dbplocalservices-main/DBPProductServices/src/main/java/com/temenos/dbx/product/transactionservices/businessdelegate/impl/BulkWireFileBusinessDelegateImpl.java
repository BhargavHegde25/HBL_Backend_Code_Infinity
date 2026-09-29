package com.temenos.dbx.product.transactionservices.businessdelegate.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireFileBusinessDelegate;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireTransactionsBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.AccountTypesDTO;
import com.temenos.dbx.product.transactionservices.dto.AccountsDTO;
import com.temenos.dbx.product.transactionservices.dto.ApplicationDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileFormatTypeDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileLineItemsDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireSampleFileDTO;
import com.temenos.dbx.product.transactionservices.dto.CountryListDTO;
import com.temenos.dbx.product.transactionservices.dto.CurrencyListDTO;
import com.temenos.dbx.product.transactionservices.dto.LineItemDTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;

/**
 * 
 * 
 * @version 1.0
 * implements {@link BulkWireTransactionsBusinessDelegate}
 */
public class BulkWireFileBusinessDelegateImpl implements BulkWireFileBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

	public List<BulkWireFileLineItemsDTO> getBulkWireFileLineItems(String bulkWireFileID,String sortByParam,String sortOrder, String searchString)  {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIREFILELINEITEMS_PROC;

		Map<String, Object> queryParams = new HashMap<String, Object>();
		String allFileLineItemsResponse = null;
		List<BulkWireFileLineItemsDTO> bulkWireFileLineItemsDTO = null;
		
		queryParams.put(Constants.BULKWIREFILE_ID, bulkWireFileID);
        queryParams.put(Constants.SEARCHSTRING, searchString);
        queryParams.put(Constants.SORTBYPARAM, sortByParam);
        queryParams.put(Constants.SORTORDER, sortOrder);
        
        try {
        	allFileLineItemsResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, queryParams, null, "");
		} catch (Exception e) {
			alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
			return null;
		}
		try {
			JSONObject bulkwireFileLineItemsJSON = new JSONObject(allFileLineItemsResponse);
			JSONArray records = bulkwireFileLineItemsJSON.getJSONArray(Constants.RECORDS);
			bulkWireFileLineItemsDTO = JSONUtils.parseAsList(records.toString(), BulkWireFileLineItemsDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error while converting db service response to bulkWireFileLineItemsDTO : " + e).log();
			return null;
		}
			return bulkWireFileLineItemsDTO;
	}

	@Override
	public List<BulkWireFileFormatTypeDTO> getBulkWireFileFormatTypes() {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIREFILEFORMATTYPE_GET;

		String allFileLineItemsResponse = null;
		List<BulkWireFileFormatTypeDTO> bulkWireFileFormatTypeDTO = null;

		try {
			allFileLineItemsResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, null, null, "");
		}
		catch (Exception e){
			alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
			return null;
		}
		try {
			JSONObject bulkwireFileFormatTypeJSON = new JSONObject(allFileLineItemsResponse);
			JSONArray records = bulkwireFileFormatTypeJSON.getJSONArray(Constants.DB_BULKWIREFILEFORMATTYPE);
			bulkWireFileFormatTypeDTO = JSONUtils.parseAsList(records.toString(), BulkWireFileFormatTypeDTO.class);
		}
		catch (Exception e) {
			alert.prepareError("Error while converting db service response to bulkWireFileFormatTypeDTO : " + e).log();
			return null;
		}
		return bulkWireFileFormatTypeDTO;
	}
	public BulkWireSampleFileDTO downloadSampleFileBulkWire(String fileCategory) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIRESAMPLEFILE_GET;

		String bulkWireFileResponse = null;
		List<BulkWireSampleFileDTO> bulkWireSampleFileDTO = null;
		HashMap<String, Object> requestParameters = new HashMap<>();
		String filter = Constants.FILECATEGORY + DBPUtilitiesConstants.EQUAL + fileCategory + DBPUtilitiesConstants.AND +
				Constants.SOFTDELETEFLAG + DBPUtilitiesConstants.EQUAL + 0 ;
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			bulkWireFileResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					requestParameters, null, "");
		} catch (Exception e) {
			alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
			return null;
		}
		try {
			JSONObject jsonResponse = new JSONObject(bulkWireFileResponse);
			JSONArray bulkWireFiles = jsonResponse.getJSONArray(Constants.BULKWIRESAMPLEFILE_TABLE);
			bulkWireSampleFileDTO = JSONUtils.parseAsList(bulkWireFiles.toString(), BulkWireSampleFileDTO.class);

		} catch (IOException e) {
			alert.prepareError("Error while converting db service response to bulkWireFilesDTO : " + e).log();
			return null;
		}

		return bulkWireSampleFileDTO.get(0);
	}
	
	public BulkWireFileDTO downloadFileBulkWire(String fileID) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIREFILES_GET;

		String bulkWireFileResponse = null;
		List<BulkWireFileDTO> bulkWireFilesDTO = null;
		StringBuilder filter = new StringBuilder();
		Map<String, Object> inputs = new HashMap<>();
		if (StringUtils.isNotBlank(fileID)) {
			filter.append(Constants.BULKWIREFILE_ID).append(DBPUtilitiesConstants.EQUAL).append(fileID);
		}

		inputs.put(DBPUtilitiesConstants.FILTER, filter.toString());
		try {
			bulkWireFileResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					inputs, null, "");
		} catch (Exception e) {
			alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
			return null;
		}
		try {
			JSONObject jsonResponse = new JSONObject(bulkWireFileResponse);
			JSONArray bulkWireFiles = jsonResponse.getJSONArray(Constants.BULKWIREFILES_TABLE);
			bulkWireFilesDTO = JSONUtils.parseAsList(bulkWireFiles.toString(), BulkWireFileDTO.class);
		} catch (IOException e) {
			alert.prepareError("Error while converting db service response to bulkWireFilesDTO : " + e).log();
			return null;
		}
		return bulkWireFilesDTO.get(0);
	}

	public List<BulkWireFileDTO> getBulkWireFilesForUser(String createdBy, String sortByParam, String sortOrder, Object pageOffset, Object pageSize, String searchString) {
        String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
        String operationName = OperationName.DB_BULKWIREFILES_FETCH;
        Map<String,Object> queryParams = new HashMap <String,Object>();
        String response = null;

        queryParams.put(Constants.CREATEDBY, createdBy);
        queryParams.put(Constants.SEARCHSTRING, searchString);
        queryParams.put(Constants.SORTBYPARAM, sortByParam);
        queryParams.put(Constants.SORTORDER, sortOrder);
        queryParams.put(Constants.PAGEOFFSET, pageOffset);
        queryParams.put(Constants.PAGESIZE, pageSize);
       
		
        List<BulkWireFileDTO> bulkWireFilesDTO = null;
        try {
            response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
                operationName, queryParams, null, "");
        } catch (Exception e) {
            alert.prepareError("Error while invoking DB service - " + operationName + "  : " + e).log();
            return null;
        }
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray bulkWireFiles = jsonResponse.getJSONArray(Constants.RECORDS);
            bulkWireFilesDTO = JSONUtils.parseAsList(bulkWireFiles.toString(), BulkWireFileDTO.class);
        } catch (IOException e) {
            alert.prepareError("Error while converting db service response to bulkWireFilesDTO : " + e).log();
            return null;
        }
        return bulkWireFilesDTO;
    }
	
	@Override
	public List<BulkWireFileDTO> uploadBWFile(BulkWireFileDTO bwfileDTO) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIREFILES_CREATE;
        
		String createResponse = null;
		List<BulkWireFileDTO> bulkWireFilesDTO = new ArrayList<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();
		Map<String,Object> requestParameters = new HashMap<>();
		requestParameters.put("bulkWireFileName", bwfileDTO.getBulkWireFileName());
		requestParameters.put("bulkWireFileContents", bwfileDTO.getBulkWireFileContents());
		requestParameters.put("fileFormatID", bwfileDTO.getFileFormatId());
		requestParameters.put("fileFormatCode", bwfileDTO.getFileFormatId());
		requestParameters.put("noOfTransactions", bwfileDTO.getNoOfTransactions());
		requestParameters.put("noOfDomesticTransactions", bwfileDTO.getNoOfDomesticTransactions());
		requestParameters.put("noOfInternationalTransactions", bwfileDTO.getNoOfInternationalTransactions());
		requestParameters.put("createdBy", bwfileDTO.getCreatedBy());
		requestParameters.put("modifiedBy", bwfileDTO.getCreatedBy());
		try {
			Date date = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp());
			requestParameters.put("createdts", date);
			requestParameters.put("lastmodifiedts", date);
		} catch (ParseException e1) {
			alert.prepareError("Failed to fetch server timestamp").log();
		}
		requestParameters.put("bulkWireFileID", bwfileDTO.getBulkWireFileID());
		if(bwfileDTO.getCompanyId() != null)
			requestParameters.put("company_id", bwfileDTO.getCompanyId());
		try{
			createResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,operationName, requestParameters, requestHeaders, "");
			JSONObject jsonResponse = new JSONObject(createResponse);
			JSONArray bulkWireFiles = jsonResponse.getJSONArray(Constants.BULKWIREFILES_TABLE);
			bulkWireFilesDTO = JSONUtils.parseAsList(bulkWireFiles.toString(), BulkWireFileDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to upload Bulk Wire file: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at uploading Bulk Wires file: " + e).log();
			return null;
		}
		return bulkWireFilesDTO;
	}
	
	@Override
	public List<BulkWireFileLineItemsDTO> createLineItems(LineItemDTO lineItemsDTO) {
		String serviceName = ServiceId.DBPTRANSACTIONORCH;
		String operationName = OperationName.DB_BULKWIREFILELINEITEMS_CREATE;

		String createResponse = null;
		List<BulkWireFileLineItemsDTO> bulkWireLinesDTO = new ArrayList<>();
		HashMap<String, Object> requestHeaders = new HashMap<>();
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("bulkWireFileID",lineItemsDTO.getBulkWireFileID());
		requestParameters.put("bulkWireTransferType",lineItemsDTO.getBulkWireTransferType());
		requestParameters.put("recipientCountryName",lineItemsDTO.getRecipientCountryName());
		requestParameters.put("recipientName",lineItemsDTO.getRecipientName());
		requestParameters.put("transactionType",lineItemsDTO.getTransactionType());
		requestParameters.put("recipientAddressLine1",lineItemsDTO.getRecipientAddressLine1());
		requestParameters.put("recipientAddressLine2",lineItemsDTO.getRecipientAddressLine2());
		requestParameters.put("recipientCity",lineItemsDTO.getRecipientCity());
		requestParameters.put("recipientState",lineItemsDTO.getRecipientState());
		requestParameters.put("recipientZipCode",lineItemsDTO.getRecipientZipCode());
		requestParameters.put("swiftCode",lineItemsDTO.getSwiftCode());
		requestParameters.put("recipientAccountNumber",lineItemsDTO.getRecipientAccountNumber());
		requestParameters.put("routingNumber",lineItemsDTO.getRoutingNumber());
		requestParameters.put("internationalRoutingNumber",lineItemsDTO.getInternationalRoutingNumber());
		requestParameters.put("accountNickname",lineItemsDTO.getAccountNickname());
		requestParameters.put("recipientBankName",lineItemsDTO.getRecipientBankName());
		requestParameters.put("recipientBankAddress1",lineItemsDTO.getRecipientBankAddress1());
		requestParameters.put("recipientBankAddress2",lineItemsDTO.getRecipientBankAddress2());
		requestParameters.put("recipientBankcity",lineItemsDTO.getRecipientBankcity());
		requestParameters.put("recipientBankstate",lineItemsDTO.getRecipientBankstate());
		requestParameters.put("recipientBankZipCode",lineItemsDTO.getRecipientBankZipCode());
		requestParameters.put("fromAccountNumber",lineItemsDTO.getFromAccountNumber());
		requestParameters.put("currency",lineItemsDTO.getCurrency());
		requestParameters.put("amount",lineItemsDTO.getAmount());
		requestParameters.put("note",lineItemsDTO.getNote());
		try {
			Date date = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp());
			requestParameters.put("createdts", date);
			requestParameters.put("lastmodifiedts", date);
		} catch (ParseException e1) {
			alert.prepareError("Failed to fetch server timestamp").log();
		}
		requestParameters.put("loop_count",lineItemsDTO.getLoop_count());
		try{
			createResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,operationName, requestParameters, requestHeaders, "");
			JSONObject jsonResponse = new JSONObject(createResponse);
			JSONArray bulkWireFileLineItems = jsonResponse.getJSONArray(Constants.LOOP_DATA_SET);
			int lineItems = bulkWireFileLineItems.length();
			for(int i = 0; i < lineItems; i++) {
				BulkWireFileLineItemsDTO lineItemDTO = JSONUtils.parseAsList(bulkWireFileLineItems.getJSONObject(0).getJSONArray(Constants.BULKWIREFILELINEITEMS).toString(),BulkWireFileLineItemsDTO.class).get(0);
				bulkWireLinesDTO.add(lineItemDTO);
			}
		}
		catch (JSONException e) {
			alert.prepareError("Failed to upload Bulk Wire file: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at uploading Bulk Wires file: " + e).log();
			return null;
		}
		return bulkWireLinesDTO;
	}
	
	@Override
	public List<CountryListDTO> getCountryList() {

		List<CountryListDTO> fetchCountryList = new ArrayList<>();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_COUNTRY_GET;


		String listOfCountryResponse = null;
		JSONArray records = null;

		try {
			listOfCountryResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, null, null, "");
			JSONObject listOfCountryJSON = new JSONObject(listOfCountryResponse);
			records = listOfCountryJSON.getJSONArray(Constants.COUNTRY);

		}
		catch (JSONException e) {
			alert.prepareError("Unable to fetch getCountryList: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getCountryList method: " + e).log();
			return null;
		}


		try {
			fetchCountryList = JSONUtils.parseAsList(records.toString(), CountryListDTO.class);
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " + e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " + e).log();
			return null;
		}

		return fetchCountryList;

	}
	
	@Override
	public List<CurrencyListDTO> getCurrencyList() {

		List<CurrencyListDTO> fetchCurrencyList = new ArrayList<>();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CURRENY_GET;


		String listOfCurrencyResponse = null;
		JSONArray records = null;

		try {
			listOfCurrencyResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, null, null, "");
			JSONObject listOfCurrencyJSON = new JSONObject(listOfCurrencyResponse);
			records = listOfCurrencyJSON.getJSONArray(Constants.CURRENCY);

		}
		catch (JSONException e) {
			alert.prepareError("Unable to fetch getCurrencyList: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getCurrencyList method: " + e).log();
			return null;
		}


		try {
			fetchCurrencyList = JSONUtils.parseAsList(records.toString(), CurrencyListDTO.class);
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " + e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " + e).log();
			return null;
		}

		return fetchCurrencyList;

	}
	
	@Override
	public List<AccountsDTO> getAccounts() {

		List<AccountsDTO> fetchAccountsList = new ArrayList<>();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_USERACCOUNTS_GET;


		String listOfAccountsResponse = null;
		JSONArray records = null;

		try {
			listOfAccountsResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, null, null, "");
			JSONObject listOfAccountsJSON = new JSONObject(listOfAccountsResponse);
			records = listOfAccountsJSON.getJSONArray(Constants.ACCOUNTS);

		}
		catch (JSONException e) {
			alert.prepareError("Unable to fetch getCurrencyList: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getCurrencyList method: " + e).log();
			return null;
		}
		try {
			fetchAccountsList = JSONUtils.parseAsList(records.toString(), AccountsDTO.class);
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " + e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " + e).log();
			return null;
		}
		return fetchAccountsList;
	}
		
		@Override
		public List<AccountTypesDTO> getAccountTypes() {

			List<AccountTypesDTO> fetchCurrencyList = new ArrayList<>();

			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
			String operationName = OperationName.DB_ACCOUNTSTYPE_GET;


			String listOfAccountTypes = null;
			JSONArray records = null;

			try {
				listOfAccountTypes = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
						operationName, null, null, "");
				JSONObject listOfAccountTypesJSON = new JSONObject(listOfAccountTypes);
				records = listOfAccountTypesJSON.getJSONArray(Constants.ACCOUNT_TYPE);

			}
			catch (JSONException e) {
				alert.prepareError("Unable to fetch getCurrencyList: " + e).log();
				return null;
			}
			catch (Exception e) {
				alert.prepareError("Caught exception at getCurrencyList method: " + e).log();
				return null;
			}
			try {
				fetchCurrencyList = JSONUtils.parseAsList(records.toString(), AccountTypesDTO.class);
			} 
			catch (IOException e) {
				alert.prepareError("Caught exception while parsing list: " + e).log();
				return null;
			}
			catch(NullPointerException e) {
				alert.prepareError("NullPointer Exception for records: " + e).log();
				return null;
			}
			return fetchCurrencyList;
		}
	
	@Override
	public List<BulkWireFileDTO> deleteBWFile(String fileId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKWIREFILES_UPDATE;

		String createResponse = null;
		List<BulkWireFileDTO> bulkWireFilesDTO = null;
		HashMap<String, Object> requestHeaders = new HashMap<>();
		HashMap<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("bulkWireFileID", fileId);
		requestParameters.put("softdeleteflag", "1");
		
		try{
			createResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,operationName, requestParameters, requestHeaders, "");
			JSONObject jsonResponse = new JSONObject(createResponse);
			JSONArray bulkWireFiles = jsonResponse.getJSONArray(Constants.BULKWIREFILES_TABLE);
			bulkWireFilesDTO = JSONUtils.parseAsList(bulkWireFiles.toString(), BulkWireFileDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to upload Bulk Wire file: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at uploading Bulk Wires file: " + e).log();
			return null;
		}
		return bulkWireFilesDTO;
	}
	
	 public Boolean isFileAccessibleByUser(String fileId, Map<String,Object> customerMap) {
		 String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
	     String operationName = OperationName.DB_BULKWIREFILES_GET;
	     Map<String, Object> requestParams = new HashMap<String, Object>();
	     StringBuilder filter = new StringBuilder();
	     String response = null;	     
	     String user_id = CustomerSession.getCustomerId(customerMap);
	     
	     ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
	     List<String> contracts = contractDelegate.fetchContractCustomers(user_id);
	     contracts.add(CustomerSession.getCompanyId(customerMap));
	     
	     String companyIdfilter = DBPUtilitiesConstants.OPEN_BRACE + 
	    		 Constants.COMPANY_ID + DBPUtilitiesConstants.EQUAL + 
	    		 String.join(DBPUtilitiesConstants.OR + Constants.COMPANY_ID + DBPUtilitiesConstants.EQUAL, contracts) 
	    		 + DBPUtilitiesConstants.CLOSE_BRACE;
	     
	     filter.append(Constants.CREATEDBY).append(DBPUtilitiesConstants.EQUAL).append(user_id);
	     if (CollectionUtils.isNotEmpty(contracts)) {
	     filter.append(DBPUtilitiesConstants.OR).append(companyIdfilter);
	     }
	     filter.append(DBPUtilitiesConstants.AND).append(Constants.BULKWIREFILE_ID)
	     .append(DBPUtilitiesConstants.EQUAL).append(fileId);
	     
	     requestParams.put(DBPUtilitiesConstants.FILTER,  filter.toString());
	     try {
	    	 response = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null, operationName,
					 requestParams, null, "");
	    	 if(response == null) {
					return false;
				}
				JSONObject responseJson = new JSONObject(response);
				if(!responseJson.has("opstatus") || responseJson.getInt("opstatus") != 0 || !responseJson.has(Constants.BULKWIREFILES_TABLE)) {
					return false;
				}
				JSONArray files = responseJson.getJSONArray(Constants.BULKWIREFILES_TABLE);
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
	public List<ApplicationDTO> getApplicationProp() {
		List<ApplicationDTO> fetchAppProp = new ArrayList<>();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_APPLICATION_GET;


		String createResponse = null;
		JSONArray records = null;

		try {
			createResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(serviceName, null,
					operationName, null, null, "");
			JSONObject listOfAccountTypesJSON = new JSONObject(createResponse);
			records = listOfAccountTypesJSON.getJSONArray(Constants.APPLICATION);

		}
		catch (JSONException e) {
			alert.prepareError("Unable to fetch application properties: " + e).log();
			return null;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at getApplicationProp method: " + e).log();
			return null;
		}
		try {
			fetchAppProp = JSONUtils.parseAsList(records.toString(), ApplicationDTO.class);
		} 
		catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " + e).log();
			return null;
		}
		catch(NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " + e).log();
			return null;
		}
		return fetchAppProp;
	}

	@Override
	public JSONObject getBWFileDomesticInternationalCount(String bulkWirefileID) {
			String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
	        String operationName = OperationName.DB_FETCH_DOMESTICINTERNATIONALCOUNTFILE_PROC;
	        Map<String,Object> queryParams = new HashMap <String,Object>();
	        String response = null;

	        queryParams.put("_bulkwirefileID", bulkWirefileID);    
	        
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
}
