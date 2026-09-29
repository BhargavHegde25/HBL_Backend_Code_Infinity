package com.temenos.dbx.product.transactionservices.resource.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWireFileBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileFormatTypeDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileLineItemsDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireSampleFileDTO;
import com.temenos.dbx.product.transactionservices.dto.LineItemDTO;
import com.temenos.dbx.product.transactionservices.resource.api.BWFileValidationResource;
import com.temenos.dbx.product.transactionservices.resource.api.BulkWireFileResource;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;


public class BulkWireFileResourceImpl implements BulkWireFileResource{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	public Result getBulkWireFileLineItems(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];

		Result result = new Result();
		Map < String, Object > customer = CustomerSession.getCustomerMap(request);

		String bulkWirefileID = "";

		if (inputParams.get(Constants.BULKWIREFILE_ID) != null && inputParams.get(Constants.BULKWIREFILE_ID) != "") {
		    bulkWirefileID = inputParams.get(Constants.BULKWIREFILE_ID).toString();
		} else {
		    alert.prepareError("BuklWireFileId  is missing in the payload which is mandatory to fetch the file details").log();
		    return ErrorCodeEnum.ERR_14001.setErrorCode(result);
		}
		
		
		    String user_id = CustomerSession.getCustomerId(customer);
	        String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_FILES;
	        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_FILES;
	        
	        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

	        Boolean isDomesticTemplatePermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer));
	        Boolean isInternationalTemplatePermitted = authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer));
	        
	        BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);
	        
	        if(!isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
	        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	        }else {
	        	JSONObject delegateResponse = bulkWireFileDelegate.getBWFileDomesticInternationalCount(bulkWirefileID);
	        	if(isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
	        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMDOMESTICTRANSACTIONS)) == 0)
	        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);	
	        	}else if(!isDomesticTemplatePermitted && isInternationalTemplatePermitted) {
	        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMINTERNATIONALTRANSACTIONS)) == 0)
	        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	        	}
	        }

		if (!bulkWireFileDelegate.isFileAccessibleByUser(bulkWirefileID, customer)) {
		    alert.prepareError("User does not have enough permissions to access the details of the file with given id" + bulkWirefileID).log();
		    return ErrorCodeEnum.ERR_14002.setErrorCode(result);
		}
		List < BulkWireFileLineItemsDTO > bulkWirefilelineitemsDTOs = null;
		String sortBy = "";
		String sortOrder = "";
		String searchString = "";
		if (inputParams.get(Constants.SORTBYPARAM) != null) {
		    sortBy = inputParams.get(Constants.SORTBYPARAM).toString();
		}
		if (inputParams.get(Constants.SORTORDER) != null) {
		    sortOrder = inputParams.get(Constants.SORTORDER).toString();
		}
		if (inputParams.get(Constants.SEARCHSTRING) != null) {
		    searchString = inputParams.get(Constants.SEARCHSTRING).toString();
		}
		try {
		    bulkWirefilelineitemsDTOs = bulkWireFileDelegate.getBulkWireFileLineItems(bulkWirefileID, sortBy, sortOrder, searchString);
		} catch (Exception e) {
		    alert.prepareError("Error while fetching bulkWireFileLineItemsDTO from BulkWireFileBusinessDelegate : " + e).log();
		    return null;
		}
		try {
		    if (bulkWirefilelineitemsDTOs == null) {
		        return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		    }

		    //for JSON Object use directly JSONToResult.convert(JSONObject) to get result
		    JSONArray rulesJSONArr = new JSONArray(bulkWirefilelineitemsDTOs);
		    JSONArray lineItemsDomesticJSONArr = new JSONArray();
		    JSONArray lineItemsInternationalJSONArr = new JSONArray();
		    int domesticJSONArr, internationalJSONarr = 0;

		    for (domesticJSONArr = 0; domesticJSONArr < rulesJSONArr.length(); domesticJSONArr++) {
		        if (rulesJSONArr.getJSONObject(domesticJSONArr).getString(Constants.BULKWIRETRANSFERTYPE).equalsIgnoreCase(Constants.DOMESTIC_RECORDS)) {
		            lineItemsDomesticJSONArr.put(rulesJSONArr.getJSONObject(domesticJSONArr));
		        } else {
		            internationalJSONarr = domesticJSONArr;
		            break;
		        }
		    }
		    if ((rulesJSONArr.length() > 0) && (rulesJSONArr.getJSONObject(internationalJSONarr).getString(Constants.BULKWIRETRANSFERTYPE).equalsIgnoreCase(Constants.INTERNATIONAL_RECORDS))) {
		        while (internationalJSONarr < rulesJSONArr.length()) {
		            lineItemsInternationalJSONArr.put(rulesJSONArr.getJSONObject(internationalJSONarr));
		            internationalJSONarr++;
		        }
		    }
		    JSONObject responseObj = new JSONObject();
		    responseObj.put(Constants.DOMESTIC_RECORDS, lineItemsDomesticJSONArr);
		    responseObj.put(Constants.INTERNATIONAL_RECORDS, lineItemsInternationalJSONArr);
		    JSONObject finalRespObj = new JSONObject();
		    finalRespObj.put(Constants.BULKWIREFILELINEITEMS_TABLE, responseObj);
		    result = JSONToResult.convert(finalRespObj.toString());
		} catch (Exception e) {
		    alert.prepareError("Error while converting response bulkWirefilelineitemsDTOs to result " + e).log();
		    return null;
		}

		return result;
	}
	
	
	@Override
	public Result getBulkWireFileFormatTypes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response)  {
		Result result = new Result();

		// Initialization of business Delegate Class
		BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);

		try {
			List<BulkWireFileFormatTypeDTO> wirefileformatTypeDTOs =  bulkWireFileDelegate.getBulkWireFileFormatTypes();
			if (wirefileformatTypeDTOs == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}

			//for JSON Object use directly JSONToResult.convert(JSONObject) to get result
			JSONArray rulesJSONArr = new JSONArray(wirefileformatTypeDTOs);
			JSONObject responseObj = new JSONObject();
			responseObj.put(Constants.BULKWIREFILEFORMATTYPE_TABLE, rulesJSONArr);
			result = JSONToResult.convert(responseObj.toString());

		} catch (Exception e) {
			alert.prepareError("Caught exception at getWireFileFormatType method: " + e).log();
			return null;
		}
      return result;
	}

	@Override
	public Result initiateDownloadBulkWireSampleFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		BulkWireSampleFileDTO bulkWireSampleFileDTO = null;
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		String fileCategory = "";
		String fileExtension = "";
		//if FileCategory is not mentioned, by default sample for file will be sent, to handle backward compatibility
		if(inputParams.get(Constants.FILECATEGORY) == null || inputParams.get(Constants.FILECATEGORY) == "") {
		   fileCategory = Constants.BULKWIRE_FILE;
		}
		else
		{
		   fileCategory = inputParams.get(Constants.FILECATEGORY).toString();
		}

        //Initialization of business Delegate Class
		BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);

		try {
			bulkWireSampleFileDTO = bulkWireFileDelegate.downloadSampleFileBulkWire(fileCategory);
		} catch (Exception e) {
			alert.prepareError("Error while fetching bulkWireFilesDTO from BulkWireTransactionsBusinessDelegate : " + e).log();
			return null;
		}
		List<BulkWireFileFormatTypeDTO> fileTypes = null;
		try {
			fileTypes = bulkWireFileDelegate.getBulkWireFileFormatTypes();
			fileExtension = (String) fileTypes.get(Integer.parseInt(bulkWireSampleFileDTO.getBulkWireSampleFileFormatCode())-1).getBulkWiresFileFormatTypeName();
		}catch (Exception e) {
			alert.prepareError("Error while fetching BulkWireFileFormatTypeDTO from BulkWireTransactionsBusinessDelegate : " + e).log();
			return null;
		}

		JSONObject wireJSON = new JSONObject(bulkWireSampleFileDTO);
	    String fileId = getbulkWireFileID();
	    if(fileId == null) {
        	alert.prepareError("Error while generating fileID").log(); 
        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
         }
	    MemoryManager.saveIntoCache(fileId, wireJSON.toString(), 120);
	    MemoryManager.saveIntoCache("fileExtension", fileExtension, 120);
	    result.addParam("fileId", fileId);
		return result;

	}

	public Result downloadFileBulkWire(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Result result = new Result();
		String user_id;

//		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
//				.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
//				.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
//		String featureAction1 = "INTERNATIONAL_WIRE_TRANSFER_BULKWIRES";
//		if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer))) {
//			String featureAction2 = "DOMESTIC_WIRE_TRANSFER_BULKWIRES";
//			if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer))) {
//				return ErrorCodeEnum.ERR_12001.setErrorCode(result);
//			}
//		}
		Map<String, Object> customerDetails = CustomerSession.getCustomerMap(request);
		user_id = CustomerSession.getCustomerId(customerDetails);
		BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);
		String file_id = inputParams.get(Constants.BULKWIREFILE_ID).toString();
		
		CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CustomerBusinessDelegate.class);
		
		JSONObject customerJson = customerBusinessDelegate.getUserDetails(user_id);
		
		Map<String, Object> customer = new HashMap<String, Object>() ;
		customer.put(Constants.CUSTOMER_ID,user_id);
		customer.put(Constants.CUSTOMERTYPE_ID, customerJson.get(Constants.CUSTOMERTYPE_ID));
		if(customerJson.has(Constants.ORGANIZATION_ID))
			customer.put(Constants.ORGANIZATION_ID, customerJson.get(Constants.ORGANIZATION_ID));
		
		if (!bulkWireFileDelegate.isFileAccessibleByUser(file_id, customer)) {
		    alert.prepareError("User does not have enough permissions to access the details of the file with given id" + file_id).log();
		    return ErrorCodeEnum.ERR_14002.setErrorCode(result);
		}
		
		BulkWireFileDTO bulkWireFilesDTO = null;
		try {
			bulkWireFilesDTO = bulkWireFileDelegate.downloadFileBulkWire(file_id);
		} catch (Exception e) {
			alert.prepareError("Error while fetching bulkWireFilesDTO from BulkWireTransactionsBusinessDelegate : " + e).log();
			return null;
		}
	    JSONObject wireJSON = new JSONObject(bulkWireFilesDTO);
	    String fileId = getbulkWireFileID();
	    if(fileId == null) {
        	alert.prepareError("Error while generating fileID").log(); 
        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
         }
	    MemoryManager.saveIntoCache(fileId, wireJSON.toString(), 120);
	    result.addParam("fileId", fileId);
		return result;
	}


	public Result getBulkWireFilesForUser(String methodID, Object[] inputArray, DataControllerRequest request,
	        DataControllerResponse response) {

	        @SuppressWarnings("unchecked")
	        Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];
	        Result result = new Result();

	        Map<String,Object> customer = CustomerSession.getCustomerMap(request);

	        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);


	        String user_id = CustomerSession.getCustomerId(customer);
	        String featureAction1 = "INTERNATIONAL_WIRE_TRANSFER_BULKWIRES";

//	        if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer))) {
//	            String featureAction2 = "DOMESTIC_WIRE_TRANSFER_BULKWIRES";
//	            if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer))) {
//	                return ErrorCodeEnum.ERR_12001.setErrorCode(result);
//	            }
//	        }

	        List < BulkWireFileDTO > bulkWireFilesDTO = null;

	        BulkWireFileBusinessDelegate bulkWireFileDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWireFileBusinessDelegate.class);

	        String createdBy = CustomerSession.getCustomerId(customer);
	        String searchString = "";
	        String sortBy = "";
	        String sortOrder = "";
	        Object pageOffset = null;
	        Object pageSize = null;
	        if (inputParams.get(Constants.SORTBYPARAM) != null)
	            sortBy = inputParams.get(Constants.SORTBYPARAM).toString();

	        if (inputParams.get(Constants.SORTORDER) != null)
	            sortOrder = inputParams.get(Constants.SORTORDER).toString();

	        if (inputParams.get(Constants.SEARCHSTRING) != null)
	        	searchString = inputParams.get(Constants.SEARCHSTRING).toString();

	        if (inputParams.get(Constants.PAGEOFFSET) != null)
	        	pageOffset = inputParams.get(Constants.PAGEOFFSET);

	        if (inputParams.get(Constants.PAGESIZE) != null)
	            pageSize = inputParams.get(Constants.PAGESIZE);

	        try {
		            bulkWireFilesDTO = bulkWireFileDelegate.getBulkWireFilesForUser(createdBy, sortBy, sortOrder, pageOffset, pageSize,searchString);
	        } catch (Exception e) {
	            alert.prepareError("Error while fetching bulkWireFilesDTO from BulkWireTransactionsBusinessDelegate : " + e).log();
	            return null;
	        }
	        try {
	        	if (bulkWireFilesDTO == null) {
					return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
				}
	            JSONArray respJsonArray = new JSONArray(bulkWireFilesDTO);
	            JSONObject respJson = new JSONObject();
	            respJson.put(Constants.BULKWIREFILE_TABLE, respJsonArray);
	            result = JSONToResult.convert(respJson.toString());
	        } catch (Exception e) {
	            alert.prepareError("Error while converting response bulkWireFilesDTO to result : " + e).log();
	            return null;
	        }

	        return result;
	    }
	
	/* 
	 * Helper Method to configure response headers for sample file
	*/ 
	private Map<String, String> _getResponseHeadersForSampleFile() {
		Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Sample.xls\"");
        return customHeaders;
	}
	

	/* 
	 * Helper Method to configure response headers 
	*/ 
	private Map<String, String> _getResponseHeaders(String fileName) {
		Map<String, String> customHeaders = new HashMap<>();
	    customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/vnd.ms-excel");
        customHeaders.put("Content-Disposition", "attachment; filename="+fileName);
        return customHeaders;
	}
	
	@Override
    public Result uploadBWFile(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
                 DataControllerResponse responseInstance) {

          @SuppressWarnings("unchecked")
		  Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
          
          Result processedResult = new Result();
          File uploadedFile = null;
          String uploadedFileName = null;
          String fileExtension = null;
          Object fileTypeId = null;
          Boolean ValidFileType = false;
          List<BulkWireFileDTO> bulkWireTransactionsDTO = null;
          BulkWireFileBusinessDelegate bwFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(BulkWireFileBusinessDelegate.class);
          BulkWireFileDTO bwfileDTO =  new BulkWireFileDTO();

          String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_UPLOAD_BULK_FILES;
          String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_UPLOAD_BULK_FILES;

          AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
                 .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                 .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
          BWFileValidationResource validationDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                 .getFactoryInstance(ResourceFactory.class).getResource(BWFileValidationResource.class);
          Map<String, Object> customer = CustomerSession.getCustomerMap(requestInstance);
          String createdBy = CustomerSession.getCustomerId(customer);
          if (!(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdBy, featureAction1, null, CustomerSession.IsCombinedUser(customer))
                       || authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdBy, featureAction2,
                                    null, CustomerSession.IsCombinedUser(customer)))) {
                 return ErrorCodeEnum.ERR_12001.setErrorCode(processedResult);
          }

          uploadedFileName = inputParams.get("bulkWireFileName") != null ? inputParams.get("bulkWireFileName").toString() : null;
          String uploadedFileBaseName = null;
          if (StringUtils.isNotEmpty(uploadedFileName)) {
      		uploadedFileBaseName = FilenameUtils.getBaseName(uploadedFileName);
      		fileExtension = FilenameUtils.getExtension(uploadedFileName);
      		if (StringUtils.isEmpty(uploadedFileBaseName) || StringUtils.isEmpty(fileExtension)) {
        	  alert.prepareError("Invalid file basename or extension").log();
        	  return ErrorCodeEnum.ERR_12111.setErrorCode(processedResult);
      		}
          }
          else {
        	  alert.prepareError("Invalid filename").log();
        	  return ErrorCodeEnum.ERR_12111.setErrorCode(processedResult);
          }
          
          try {
                 String regExp = "^[a-zA-Z0-9]+$";
                 if(!uploadedFileName.substring(0,uploadedFileName.length()-fileExtension.length()-1).matches(regExp)) {
                	 return ErrorCodeEnum.ERR_14004.setErrorCode(processedResult);
                 }
                 try {
                       List<BulkWireFileFormatTypeDTO> fileTypes = bwFileBusinessDelegate.getBulkWireFileFormatTypes();
                       int fileTypesCount = fileTypes.size();
                       for (int i = 0; i < fileTypesCount; i++) {
                              if (fileExtension.equals(fileTypes.get(i).getBulkWiresFileFormatTypeName())) {
                                    ValidFileType = true;
                                    fileTypeId = fileTypes.get(i).getBulkWiresFileFormatTypeCode();
                                    break;
                              }
                       }
                 } catch (Exception e) {
                       alert.prepareError("Error while getting supported file format types : " + e).log();
                 }

                 if (ValidFileType != true) {
                       return ErrorCodeEnum.ERR_14004.setErrorCode(processedResult);
                 }
                 
                 String fileContents = inputParams.get("bulkWireFileContents") != null ? inputParams.get("bulkWireFileContents").toString() : null;
                 if (StringUtils.isNotEmpty(fileContents)) {
               	   uploadedFile = CommonUtils.constructFileObjectFromBase64String(fileContents, uploadedFileBaseName, fileExtension);
       		  	 } else {
       			   alert.prepareError("File contents are empty").log();
       			   return ErrorCodeEnum.ERR_14005.setErrorCode(processedResult);
       		  	 }
                 
                 Map<String, Object> validateFile = new HashMap<String, Object>();
                 try {
                       validateFile = validationDelegate.validateFile(uploadedFile,requestInstance);
                 } catch (Exception e) {
                       alert.prepareError("Error while validating file : " + e).log();
                       return ErrorCodeEnum.ERR_14005.setErrorCode(processedResult);
                 }

                 if (validateFile.get("status").equals("Denied")) {
                	 ErrorCodeEnum errorCode = (ErrorCodeEnum)validateFile.get("errorCode");
                       return errorCode.setErrorCode(processedResult);
                 }

                 Map<String, Object> BWfileMapContents = validateFile;
                 Map<String, Object> BWfileMap = new HashMap<>();
                 
                 byte[] bytesArray = new byte[(int) uploadedFile.length()]; 
                  FileInputStream fis = new FileInputStream(uploadedFile);
                 fis.read(bytesArray); //read file into bytes[]
                 fis.close();                
					
					Set<String> accounts = new HashSet<>(
							Arrays.asList(BWfileMapContents.get("fromAccountNumber").toString().split(",")));
					AccountBusinessDelegate accountDelegate = DBPAPIAbstractFactoryImpl
							.getBusinessDelegate(AccountBusinessDelegate.class);
					CustomerAccountsDTO accountDTO = accountDelegate.getCommonContractCustomer(createdBy,accounts);

					if (accountDTO == null) {
						alert.prepareError("All the offset accounts doesn't belong to same CIF").log();
						return ErrorCodeEnum.ERR_12061.setErrorCode(processedResult);
					}
										
				 String companyId = accountDTO.getOrganizationId();
                 bwfileDTO.setBulkWireFileName((String)uploadedFileName.substring(0,uploadedFileName.length()-fileExtension.length()-1) + "_" + HelperMethods.getCurrentTimeStamp()+"."+fileExtension);
                 bwfileDTO.setBulkWireFileContents((String)java.util.Base64.getMimeEncoder().encodeToString(bytesArray));
                 bwfileDTO.setFileFormatId(Integer.parseInt((String)fileTypeId));
                 bwfileDTO.setNoOfTransactions(Integer.parseInt((String)BWfileMapContents.get("noOfLineItems")));
                 bwfileDTO.setNoOfDomesticTransactions(Integer.parseInt((String)BWfileMapContents.get("domesticRecords")));
                 bwfileDTO.setNoOfInternationalTransactions(Integer.parseInt((String)BWfileMapContents.get("internationalRecords")));
                 bwfileDTO.setCreatedBy((String)createdBy);
                 bwfileDTO.setCompanyId((String)companyId);
                 String fileID = getbulkWireFileID();
                 if(fileID == null) {
                	alert.prepareError("Error while generating fileID for new File").log(); 
                	return ErrorCodeEnum.ERR_12001.setErrorCode(processedResult);
                 }
                 bwfileDTO.setBulkWireFileID(fileID);
                 
                 if(bwfileDTO.getNoOfDomesticTransactions() > 0 && bwfileDTO.getNoOfInternationalTransactions() <= 0) {
                	if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdBy, featureAction1, null, CustomerSession.IsCombinedUser(customer)))
                			return ErrorCodeEnum.ERR_12001.setErrorCode(processedResult);
                 }
                 if(bwfileDTO.getNoOfInternationalTransactions() > 0 && bwfileDTO.getNoOfDomesticTransactions() <= 0) {
                	 if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdBy, featureAction2, null, CustomerSession.IsCombinedUser(customer)))
             			return ErrorCodeEnum.ERR_12001.setErrorCode(processedResult);
                 }


              
                 bulkWireTransactionsDTO = bwFileBusinessDelegate.uploadBWFile(bwfileDTO);
                 String fileId = null;
                 try {
                       fileId = bulkWireTransactionsDTO.get(0).getBulkWireFileID();
                       BWfileMapContents = validationDelegate.appendFileId(BWfileMapContents, fileId);
                 } catch (Exception e) {
                       alert.prepareError("Error while appending file Id to line Items : " + e).log();
                       return ErrorCodeEnum.ERR_12000.setErrorCode(processedResult);
                 }
                 if (fileId == null)
                       return ErrorCodeEnum.ERR_12000.setErrorCode(processedResult);
                 BWfileMapContents.put("loop_count", BWfileMapContents.get("noOfLineItems"));
                 List<BulkWireFileLineItemsDTO> bulkwireLineItemsDTO = bwFileBusinessDelegate.createLineItems(createLineItemsDTO(BWfileMapContents));
                 if (isLineItemsCreateSuccessful(bulkwireLineItemsDTO)) {
                       JSONObject responseObj = new JSONObject();
                       responseObj.put("bulkWireFileID", fileId);
                       responseObj.put("noOfTransactions", BWfileMapContents.get("noOfLineItems"));
                       responseObj.put("bulkWireFileName", bulkWireTransactionsDTO.get(0).getBulkWireFileName());
                       processedResult = JSONToResult.convert(responseObj.toString());
                 } else {
                       try {
                              bulkWireTransactionsDTO = bwFileBusinessDelegate.deleteBWFile(fileId);
                              return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
                       } catch (Exception e) {
                              alert.prepareError("Error while updating soft delete flag for uploaded file : " + e).log();
                       }
                 }
          }

          catch (Exception e) {
                 return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
          }
          
          finally {
  			if (uploadedFile != null) {
  				uploadedFile.delete();
  			}
  		  }
  		
          return processedResult;

    }

    private String getbulkWireFileID() {
    	String bulkFileId = null;          
        int n = 32;
        String id = CommonUtils.generateUniqueID(n);
        if(id != null) {
        bulkFileId = id.substring(0,8) + "-" + id.substring(8,16) + "-" + id.substring(16,24) + "-"+ id.substring(24,32);
        return bulkFileId;
        }
        return id;
	}


	private LineItemDTO createLineItemsDTO(Map<String, Object> bWfileMapContents) {
          LineItemDTO lineItemsDTO = new LineItemDTO();
    lineItemsDTO.setBulkWireFileID((String)bWfileMapContents.get("bulkWireFileID"));
    lineItemsDTO.setBulkWireTransferType((String)bWfileMapContents.get("bulkWireTransferType"));
    lineItemsDTO.setRecipientCountryName((String)bWfileMapContents.get("recipientCountryName"));
    lineItemsDTO.setRecipientName((String)bWfileMapContents.get("recipientName"));
    lineItemsDTO.setTransactionType((String)bWfileMapContents.get("transactionType"));
    lineItemsDTO.setRecipientAddressLine1((String)bWfileMapContents.get("recipientAddressLine1"));
    lineItemsDTO.setRecipientAddressLine2((String)bWfileMapContents.get("recipientAddressLine2"));
    lineItemsDTO.setRecipientCity((String)bWfileMapContents.get("recipientCity"));
    lineItemsDTO.setRecipientState((String)bWfileMapContents.get("recipientState"));
    lineItemsDTO.setRecipientZipCode((String)bWfileMapContents.get("recipientZipCode"));
    lineItemsDTO.setSwiftCode((String)bWfileMapContents.get("swiftCode"));
    lineItemsDTO.setRecipientAccountNumber((String)bWfileMapContents.get("recipientAccountNumber"));
    lineItemsDTO.setRoutingNumber((String)bWfileMapContents.get("routingNumber"));
    lineItemsDTO.setInternationalRoutingNumber((String)bWfileMapContents.get("internationalRoutingNumber"));
    lineItemsDTO.setAccountNickname((String)bWfileMapContents.get("accountNickname"));
    lineItemsDTO.setRecipientBankName((String)bWfileMapContents.get("recipientBankName"));
    lineItemsDTO.setRecipientBankAddress1((String)bWfileMapContents.get("recipientBankAddress1"));
    lineItemsDTO.setRecipientBankAddress2((String)bWfileMapContents.get("recipientBankAddress2"));
    lineItemsDTO.setRecipientBankcity((String)bWfileMapContents.get("recipientBankcity"));
    lineItemsDTO.setRecipientBankstate((String)bWfileMapContents.get("recipientBankstate"));
    lineItemsDTO.setRecipientBankZipCode((String)bWfileMapContents.get("recipientBankZipCode"));
    lineItemsDTO.setFromAccountNumber((String)bWfileMapContents.get("fromAccountNumber"));
    lineItemsDTO.setCurrency((String)bWfileMapContents.get("currency"));
    lineItemsDTO.setAmount((String)bWfileMapContents.get("amount"));
        lineItemsDTO.setNote((String)bWfileMapContents.get("note"));
    lineItemsDTO.setLoop_count((String)bWfileMapContents.get("loop_count"));
          return lineItemsDTO;
    }
    
  
    private boolean isLineItemsCreateSuccessful(List<BulkWireFileLineItemsDTO> bulkwireLineItemsDTO) {
    		if (bulkwireLineItemsDTO == null)
    			return false;
    		int lineItems = bulkwireLineItemsDTO.size();
    		for (int i = 0; i < lineItems; i++)
    			if (bulkwireLineItemsDTO.get(i).getBulkWireFileLineItemID() == null)
    				return false;
    		return true;
    	}


	@Override
	public Result downloadFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response){
		
		String fileID = null;
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];
		
		if (inputParams.get(Constants.BULKWIREFILE_ID) != null && inputParams.get(Constants.BULKWIREFILE_ID) != "") {
			fileID = inputParams.get(Constants.BULKWIREFILE_ID).toString();
		} else {
		    alert.prepareError("FileId  is missing in the payload which is mandatory to fetch the file details").log();
		    return ErrorCodeEnum.ERR_14017.setErrorCode(result);
		}

		String FileDetails = (String)MemoryManager.getFromCache(fileID);
		BulkWireFileDTO bulkWireFilesDTO = null;
		try {
			bulkWireFilesDTO = JSONUtils.parse(FileDetails, BulkWireFileDTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception while parsing file Contents").log();
			return null;
		}
		
		String fileContents = bulkWireFilesDTO.getBulkWireFileContents();
		byte[] decodedBytes = Base64.getMimeDecoder().decode(fileContents);
		response.getHeaders().putAll(_getResponseHeaders(bulkWireFilesDTO.getBulkWireFileName()));
		
		try {
			response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
			new BufferedHttpEntity(new ByteArrayEntity(decodedBytes)));
		} catch (Exception e) {
			alert.prepareError("Error while converting response bulkWireFilesDTO to result : " + e).log();
			return null;
		}
		response.setStatusCode(HttpStatus.SC_OK);
		return result;
	}
	@Override
	public Result downloadSampleFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response){
		
        String fileID = null;
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];
		
		if (inputParams.get(Constants.BULKWIREFILE_ID) != null && inputParams.get(Constants.BULKWIREFILE_ID) != "") {
			fileID = inputParams.get(Constants.BULKWIREFILE_ID).toString();
		} else {
		    alert.prepareError("FileId  is missing in the payload which is mandatory to fetch the file details").log();
		    return ErrorCodeEnum.ERR_14017.setErrorCode(result);
		}

		String FileDetails = (String)MemoryManager.getFromCache(fileID);
		String fileExtension = (String)MemoryManager.getFromCache("fileExtension");
		BulkWireSampleFileDTO bulkWireSampleFileDTO = null;
		try {
			bulkWireSampleFileDTO = JSONUtils.parse(FileDetails, BulkWireSampleFileDTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception while parsing file Contents").log();
			return null;
		}
		try {
			result = JSONToResult.convert(FileDetails);
			byte[] bytes = bulkWireSampleFileDTO.getSampleFileContents().getBytes();
			byte[] decodedBytes = Base64.getMimeDecoder().decode(bytes);
			response.getHeaders().putAll(_getResponseHeaders(bulkWireSampleFileDTO.getBulkWireSampleFileName()+"."+fileExtension));
			response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
			new BufferedHttpEntity(new ByteArrayEntity(decodedBytes)));
		} catch (Exception e) {
			alert.prepareError("Error while converting response BulkWireFileFormatTypeDTO to result : " + e).log();
			return null;
		}
		response.setStatusCode(HttpStatus.SC_OK);
		return result;
	}  
}
