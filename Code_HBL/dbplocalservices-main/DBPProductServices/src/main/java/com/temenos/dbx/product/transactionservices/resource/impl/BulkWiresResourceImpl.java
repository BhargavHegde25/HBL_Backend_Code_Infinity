package com.temenos.dbx.product.transactionservices.resource.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWiresBusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BulkWireDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireTemplateLineItemDTO;
import com.temenos.dbx.product.transactionservices.dto.PayeeDTO;
import com.temenos.dbx.product.transactionservices.resource.api.BWFileValidationResource;
import com.temenos.dbx.product.transactionservices.resource.api.BulkWiresResource;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;

public class BulkWiresResourceImpl implements BulkWiresResource{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

	public Result getBulkWiresForUser(String methodID, Object[] inputArray, DataControllerRequest request,
	        DataControllerResponse response) {

	        @SuppressWarnings("unchecked")
	        Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];
	        Result result = new Result();

	        Map<String,Object> customer = CustomerSession.getCustomerMap(request);

	        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);


	        String bulkWireCategoryFilter = "";
	        if (inputParams.get(Constants.BULKWIRECATEGORYFILTER) != null)
	        	bulkWireCategoryFilter = inputParams.get(Constants.BULKWIRECATEGORYFILTER).toString();

	        String user_id = CustomerSession.getCustomerId(customer);
	        String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_FILES;
	        String featureAction2 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
	        String featureAction3 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_FILES;
	        String featureAction4 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
	        
	        Boolean isDomesticFilePermitted =false;
			Boolean isDomesticTemplatePermitted =false;
			Boolean isInternationalFilePermitted =false;
			Boolean isInternationalTemplatePermitted =false;
			
			String featureActionlistDomesticFile = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction1));		
			if (!StringUtils.isEmpty(featureActionlistDomesticFile)) {
				isDomesticFilePermitted = true;	
			}
			
			String featureActionlistDomesticTemplate = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction2));		
			if (!StringUtils.isEmpty(featureActionlistDomesticTemplate)) {
				isDomesticTemplatePermitted = true;	
			}
			
			String featureActionlistInternationalFile = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction3));		
			if (!StringUtils.isEmpty(featureActionlistInternationalFile)) {
				isInternationalFilePermitted = true;	
			}
			
			String featureActionlistInternationalTemplate = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction4));		
			if (!StringUtils.isEmpty(featureActionlistInternationalTemplate)) {
				isInternationalTemplatePermitted = true;	
			}
	        	       
	        
	        if(bulkWireCategoryFilter.equalsIgnoreCase(Constants.FILES)) {
	           if(!isDomesticFilePermitted && !isInternationalFilePermitted) 
		        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);

	        }else if(bulkWireCategoryFilter.equalsIgnoreCase(Constants.TEMPLATES)) {
	        	if(!isDomesticTemplatePermitted && !isInternationalTemplatePermitted) 
		        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	        }else{
	        	if(!isDomesticFilePermitted && !isDomesticTemplatePermitted && !isInternationalFilePermitted && !isInternationalTemplatePermitted) 
	        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	        	
	        	if(!isDomesticFilePermitted && !isInternationalFilePermitted) 
	        		bulkWireCategoryFilter = Constants.TEMPLATES;
	        	else if(!isDomesticTemplatePermitted && !isInternationalTemplatePermitted)
	        		bulkWireCategoryFilter = Constants.FILES;
	        }

	        List <BulkWireDTO> bulkWiresDTO = null;

	        BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);

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
		            bulkWiresDTO = bulkWiresDelegate.getBulkWiresForUser(createdBy, sortBy, sortOrder, pageOffset, pageSize,searchString, bulkWireCategoryFilter,isDomesticFilePermitted,isInternationalFilePermitted,isDomesticTemplatePermitted,isInternationalTemplatePermitted);
	        } catch (Exception e) {
	            return ErrorCodeEnum.ERR_14008.setErrorCode(result);
	        }
	        try {
	            JSONArray respJsonArray = new JSONArray(bulkWiresDTO);
	            JSONObject respJson = new JSONObject();
	            respJson.put(Constants.BULKWIRES, respJsonArray);
	            result = JSONToResult.convert(respJson.toString());
	        } catch (Exception e) {
	            alert.prepareError("Error while converting response bulkWireDTO to result : " + e).log();
	            return null;
	        }

	        return result;
	    }
	
	@Override
    public Result uploadBWTemplateFile(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
                 DataControllerResponse responseInstance) {
		 @SuppressWarnings("unchecked")
		 Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
		
    	 Result processedResult = new Result();
    	 AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
 	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
    	 Map<String,Object> customer = CustomerSession.getCustomerMap(requestInstance);
	     String user_id = CustomerSession.getCustomerId(customer);
	     
    	 String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES;
	     String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES;
  
	     Boolean isDomesticCreateTemplatePermitted = false;
	     Boolean isInternationalCreateTemplatePermitted = false;

			String featureActionlistDomestic = CustomerSession.getPermittedActionIds(requestInstance, Arrays.asList(featureAction1));		
			if (!StringUtils.isEmpty(featureActionlistDomestic)) {
				isDomesticCreateTemplatePermitted = true;	
			}
			
			String featureActionlistInternational = CustomerSession.getPermittedActionIds(requestInstance, Arrays.asList(featureAction2));		
			if (!StringUtils.isEmpty(featureActionlistInternational)) {
				isInternationalCreateTemplatePermitted = true;	
			}		
	        
	     if(!isDomesticCreateTemplatePermitted && !isInternationalCreateTemplatePermitted) {
		      return ErrorCodeEnum.ERR_12001.setErrorCode(processedResult);
	     }
         File uploadedFile = null;
         String uploadedFileName = null;
         String fileExtension = null;
         Object fileTypeId = null;
         Boolean ValidFileType = false;
         BWFileValidationResource validationDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                 .getFactoryInstance(ResourceFactory.class).getResource(BWFileValidationResource.class);
         
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
    	  	if(!validationDelegate.isValidFileName(uploadedFileName,fileExtension)) {
    	  		return ErrorCodeEnum.ERR_14004.setErrorCode(processedResult);
    	  	}
    	  	try {
    	  		fileTypeId = validationDelegate.getFileFormat(fileExtension);
    	  		if(fileTypeId.equals("-1"))
    	  			ValidFileType = false;
    	  		else
    	  			ValidFileType = true;
    	  	} catch (Exception e) {
               alert.prepareError("Error while getting supported file format types : " + e).log();
    	  	}

    	  	if (ValidFileType == false) {
    	  		return ErrorCodeEnum.ERR_14004.setErrorCode(processedResult);
    	  	}
    	  	
    	  	String fileContents = inputParams.get("bulkWireFileContents") != null ? inputParams.get("bulkWireFileContents").toString() : null;
            if (StringUtils.isNotEmpty(fileContents)) {
          	   uploadedFile = CommonUtils.constructFileObjectFromBase64String(fileContents, uploadedFileBaseName, fileExtension);
  		  	 } else {
  			   alert.prepareError("File contents are empty").log();
  			   return ErrorCodeEnum.ERR_14005.setErrorCode(processedResult);
  		  	 }
            
    	  	JSONObject validateFile = new JSONObject();
            try {
            	validateFile = validationDelegate.validateTemplateFile(uploadedFile,requestInstance);
            } catch (Exception e) {
                  alert.prepareError("Error while validating file : " + e).log();
                  return ErrorCodeEnum.ERR_14005.setErrorCode(processedResult);
            }
            
            if(validateFile.has("error")) {
            	HashMap error =  (HashMap) ((JSONArray) validateFile.get("error")).get(0);
            	ErrorCodeEnum errorCode = (ErrorCodeEnum)error.get("errorCode");
                return errorCode.setErrorCode(processedResult);
            }
            JSONArray recipients = (JSONArray) ((JSONArray) validateFile.get("recipients")).get(0);
            int domesticTransactionCount = 0;
            int internationalTransactionCount = 0;
            JSONObject record;
            String transferType;
            for(int i=0; i<recipients.length();i++){
            	record = (JSONObject) recipients.get(i);
            	transferType = record.get(Constants.BULKWIRETRANSFERTYPE).toString();
            	if(transferType.equalsIgnoreCase(Constants.DOMESTIC_RECORDS)) 
            		domesticTransactionCount++;
            	else
            		internationalTransactionCount++;
            }
            if(domesticTransactionCount > 0) {
            	if(!isDomesticCreateTemplatePermitted)
            		 return ErrorCodeEnum.ERR_12005.setErrorCode(processedResult);
            }
            if(internationalTransactionCount > 0) {
            	if(!isInternationalCreateTemplatePermitted)
           		 return ErrorCodeEnum.ERR_12006.setErrorCode(processedResult);
            }
            JSONObject result = new JSONObject();
            result.put("recipients",recipients);
            processedResult = JSONToResult.convert(result.toString());
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
	

	@Override
	public Result createBulkWireTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String user_id = CustomerSession.getCustomerId(customer);
		String companyId = CustomerSession.getCompanyId(customer);
		String fromAccount = request.getParameter("defaultFromAccount");
		
		String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES;
        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES;

        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
               .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
               .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
        
        if (!(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, fromAccount, CustomerSession.IsCombinedUser(customer))
                || authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2,
                		fromAccount, CustomerSession.IsCombinedUser(customer)))) {
          return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
        
        
		String bulkWireTemplateId = _generateBulkWireTemplateID();
		if(bulkWireTemplateId == null) {
	       	alert.prepareError("Error while generating templateID for new Template").log(); 
	       	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	    }
		
		DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
		String currentTimestamp = null;
		try {
			currentTimestamp = df.format(new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
		} catch (ParseException e) {
			alert.prepareError("Error while getting Server time").log();
		}
		
		JSONObject populateResponse = this._populateBulkWireTemplateDTO(request, user_id, companyId, bulkWireTemplateId, "CREATE", currentTimestamp);
		if(populateResponse == null) {
			return ErrorCodeEnum.ERR_21101.setErrorCode(result);
		}
		
		String bulkWireTemplateString = populateResponse.getString("bulkWireTemplateString");
		
		if(populateResponse.getInt("NoOfDomesticTransactions") > 0 && !(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, fromAccount, CustomerSession.IsCombinedUser(customer)))) {
       	 return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
        if(populateResponse.getInt("NoOfInternationalTransactions") > 0 && !(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, fromAccount, CustomerSession.IsCombinedUser(customer)))) {
       	 return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
		
		Object populateLineitemResponse = this._populateBulkWireTemplateLineItemsDTO(request, user_id, bulkWireTemplateId, currentTimestamp);
		JSONObject bulkWireTemplateLineItemsJson = null;
		if(populateLineitemResponse == null) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		else if(populateLineitemResponse instanceof String) {
			String errorMessage = (String)populateLineitemResponse;
			if("21100".equals(errorMessage)) {
				return ErrorCodeEnum.ERR_21100.setErrorCode(result);
			}
			else if("21111".equals(errorMessage)) {
				return ErrorCodeEnum.ERR_21111.setErrorCode(result);
			}
			else {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		}
		else if(populateLineitemResponse instanceof JSONObject) {
			bulkWireTemplateLineItemsJson = (JSONObject)populateLineitemResponse;
		}
		JSONArray payloadBatches_for_create = (JSONArray)(bulkWireTemplateLineItemsJson.get("lineItem_for_create"));
		
		 BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);
		 
		 Object delegateResponse = bulkWiresDelegate.createBulkWireTemplate(bulkWireTemplateString, payloadBatches_for_create.get(0).toString());
		 
		if (delegateResponse instanceof BulkWireDTO) {
			if (payloadBatches_for_create.length() > 1) {
				populateResponse = this._populateBulkWireTemplateDTO(request, user_id, null, bulkWireTemplateId, "UPDATE", currentTimestamp);
				bulkWireTemplateString = populateResponse.getString("bulkWireTemplateString");
				for (int i = 1; i < payloadBatches_for_create.length(); i++) {
					delegateResponse = bulkWiresDelegate.updateBulkWireTemplate(bulkWireTemplateString, "",
							payloadBatches_for_create.get(i).toString());
					if (!(delegateResponse instanceof BulkWireDTO)) {
						break;
					}
				}
				if (delegateResponse instanceof BulkWireDTO) {
					BulkWireDTO bulkWireDTO = (BulkWireDTO) delegateResponse;
					JSONObject respJson = new JSONObject(bulkWireDTO);
					result = JSONToResult.convert(respJson.toString());
					return result;
				} else {
					bulkWiresDelegate.deleteBulkWireTemplate(bulkWireTemplateId);
					 if (delegateResponse instanceof String) {
							String errorMessage = (String) delegateResponse;
							if (errorMessage.contains(Constants.TEMPLATE_USER_CONSTRAINT)
									|| errorMessage.contains(Constants.TEMPLATE_COMPANY_CONSTRAINT)) {
								return ErrorCodeEnum.ERR_21102.setErrorCode(result);
							} else {
								return ErrorCodeEnum.ERR_12000.setErrorCode(result);
							}
						} else {
							return ErrorCodeEnum.ERR_12000.setErrorCode(result);
						}
				}
			}
			else {
				BulkWireDTO bulkWireDTO = (BulkWireDTO) delegateResponse;
				JSONObject respJson = new JSONObject(bulkWireDTO);
				result = JSONToResult.convert(respJson.toString());
				return result;
			}
		}
		 else {
			 bulkWiresDelegate.deleteBulkWireTemplate(bulkWireTemplateId);
			 if(delegateResponse instanceof String) {
				 String errorMessage = (String)delegateResponse;
				 if(errorMessage.contains(Constants.TEMPLATE_USER_CONSTRAINT) || errorMessage.contains(Constants.TEMPLATE_COMPANY_CONSTRAINT)) {
					 return ErrorCodeEnum.ERR_21102.setErrorCode(result);
				 }
				else {
					return ErrorCodeEnum.ERR_12000.setErrorCode(result);
				}
			 }
			else {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		 }
	}

	private JSONObject _populateBulkWireTemplateDTO(DataControllerRequest request, String user_id, String companyId,
			String bulkWireTemplateId, String method, String currentTimestamp) {
		JSONArray recipients = new JSONArray(request.getParameter("Recipients"));

		int noOfDomestic = 0, noOfInternational = 0;

		for (int i = 0; i < recipients.length(); i++) {
			JSONObject recipient = recipients.getJSONObject(i);
			if (recipient.getString("bulkWireTransferType").equals("Domestic"))
				noOfDomestic++;
			else
				noOfInternational++;
		}

		StringBuilder bulkWireTemplate = new StringBuilder();

		if(bulkWireTemplateId == null) {
			bulkWireTemplateId = request.getParameter("bulkWireTemplateID");
		}
		try {
			if ("CREATE".equals(method)) {
				bulkWireTemplate.append("\"").append(bulkWireTemplateId).append("\"").append(",");
				bulkWireTemplate.append("\"").append(request.getParameter("bulkWireTemplateName")).append("\"").append(",");
				bulkWireTemplate.append("\"").append(recipients.length()).append("\"").append(",");
				bulkWireTemplate.append("\"").append(noOfDomestic).append("\"").append(",");
				bulkWireTemplate.append("\"").append(noOfInternational).append("\"").append(",");
				bulkWireTemplate.append("\"").append(user_id).append("\"").append(",");
				bulkWireTemplate.append("\"").append(user_id).append("\"").append(",");

				String nullString = null;

				if (companyId != null) {
					bulkWireTemplate.append("\"").append(companyId).append("\"").append(",");
				} else {
					bulkWireTemplate.append(nullString).append(",");
				}
				bulkWireTemplate.append("\"").append(currentTimestamp).append("\"").append(","); //createdts
				bulkWireTemplate.append("\"").append(currentTimestamp).append("\"").append(","); //lastmodifiedts
			} else {
				bulkWireTemplate.append("\"").append(bulkWireTemplateId).append("\"").append(",");
				bulkWireTemplate.append("\"").append(request.getParameter("bulkWireTemplateName")).append("\"").append(",");
				bulkWireTemplate.append("\"").append(user_id).append("\"").append(",");
				bulkWireTemplate.append("\"").append(user_id).append("\"").append(",");
				bulkWireTemplate.append("\"").append(currentTimestamp).append("\"").append(","); //lastmodifiedts
			}

			bulkWireTemplate.append("\"").append(request.getParameter("defaultFromAccount")).append("\"").append(",");
			bulkWireTemplate.append("\"").append(request.getParameter("defaultCurrency")).append("\"");
		} catch (Exception e) {
			alert.prepareError("bulkWireTemplateName,defaultFromAccount and defaultCurrency are mandatory fields " + e).log();
			return null;
		}

		JSONObject response = new JSONObject();
		response.put("bulkWireTemplateString", bulkWireTemplate.toString());
		response.put("NoOfDomesticTransactions", noOfDomestic);
		response.put("NoOfInternationalTransactions", noOfInternational);

		return response;
	}    


	private String _generateBulkWireTemplateID() {
    	String bulkTemplateId = null;          
        int n = 32;
        String id = CommonUtils.generateUniqueID(n);
        if(id != null) {
        	bulkTemplateId = id.substring(0,8) + "-" + id.substring(8,16) + "-" + id.substring(16,24) + "-"+ id.substring(24,32);
        	return bulkTemplateId;
        }
        return id;
	}

	
	@Override
	public Result updateBulkWireTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String user_id = CustomerSession.getCustomerId(customer);
		String fromAccount = request.getParameter("defaultFromAccount");
		
		String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES;
        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES;

        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
               .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
               .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
        
        if (!(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, fromAccount, CustomerSession.IsCombinedUser(customer))
                || authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2,
                		fromAccount, CustomerSession.IsCombinedUser(customer)))) {
          return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }        
        
		String bulkWireTemplateId = request.getParameter("bulkWireTemplateID");
		
		BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);
		
		if (!bulkWiresDelegate.isTemplateAccessibleByUser(bulkWireTemplateId, customer)) {
            diagnostic.prepareInfo("User does not have enough permissions to access the details of the template with given id" + bulkWireTemplateId).log();
            return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
		
		DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
		String currentTimestamp = null;
		try {
			currentTimestamp = df.format(new SimpleDateFormat(Constants.TIMESTAMP_FORMAT).parse(application.getServerTimeStamp()));
		} catch (ParseException e) {
			alert.prepareError("Error while getting Server time").log();
		}
		
		JSONObject populateResponse = this._populateBulkWireTemplateDTO(request, user_id, null, null, "UPDATE", currentTimestamp);
		if(populateResponse == null) {
			return ErrorCodeEnum.ERR_21101.setErrorCode(result);
		}
		
		String bulkWireTemplateString = populateResponse.getString("bulkWireTemplateString");
		
		if(populateResponse.getInt("NoOfDomesticTransactions") > 0 && !(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction1, fromAccount, CustomerSession.IsCombinedUser(customer)))) {
       	 return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
        if(populateResponse.getInt("NoOfInternationalTransactions") > 0 && !(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(user_id, featureAction2, fromAccount, CustomerSession.IsCombinedUser(customer)))) {
       	 return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }
		
		Object populateLineitemResponse = this._populateBulkWireTemplateLineItemsDTO(request, user_id, bulkWireTemplateId, currentTimestamp);
		JSONObject lineItemPayloads = null;
		if(populateLineitemResponse == null) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		else if(populateLineitemResponse instanceof String) {
			String errorMessage = (String)populateLineitemResponse;
			if("21100".equals(errorMessage)) {
				return ErrorCodeEnum.ERR_21100.setErrorCode(result);
			}
			else if("21111".equals(errorMessage)) {
				return ErrorCodeEnum.ERR_21111.setErrorCode(result);
			}
		}
		else if(populateLineitemResponse instanceof JSONObject) {
			lineItemPayloads = (JSONObject)populateLineitemResponse;
		}
		JSONArray payloadBatches_for_create = (JSONArray)(lineItemPayloads.get("lineItem_for_create"));
		JSONArray payloadBatches_for_update = (JSONArray)(lineItemPayloads.get("lineItem_for_update"));
		 
		 Object delegateResponse = bulkWiresDelegate.updateBulkWireTemplate(bulkWireTemplateString, payloadBatches_for_update.getString(0).toString(), payloadBatches_for_create.getString(0).toString());
		 
		 if(delegateResponse instanceof BulkWireDTO) {
			 if (payloadBatches_for_create.length() > 1) {
					for (int i = 1; i < payloadBatches_for_create.length(); i++) {
						delegateResponse = bulkWiresDelegate.updateBulkWireTemplate(bulkWireTemplateString, "",
								payloadBatches_for_create.get(i).toString());
						if (!(delegateResponse instanceof BulkWireDTO)) {
							break;
						}
					}
					if (delegateResponse instanceof BulkWireDTO) {
						BulkWireDTO bulkWireDTO = (BulkWireDTO) delegateResponse;
						JSONObject respJson = new JSONObject(bulkWireDTO);
						result = JSONToResult.convert(respJson.toString());
						return result;
					} else if (delegateResponse instanceof String) {
						String errorMessage = (String) delegateResponse;
						if (errorMessage.contains(Constants.TEMPLATE_USER_CONSTRAINT)
								|| errorMessage.contains(Constants.TEMPLATE_COMPANY_CONSTRAINT)) {
							return ErrorCodeEnum.ERR_21102.setErrorCode(result);
						} else {
							return ErrorCodeEnum.ERR_12000.setErrorCode(result);
						}
					} else {
						return ErrorCodeEnum.ERR_12000.setErrorCode(result);
					}
				}
			 else {
				 BulkWireDTO bulkWireDTO = (BulkWireDTO)delegateResponse;
				 JSONObject respJson = new JSONObject(bulkWireDTO);
				 result = JSONToResult.convert(respJson.toString());
				 return result;
			 }
		 }
		 else if(delegateResponse instanceof String) {
			 String errorMessage = (String)delegateResponse;
			 if(errorMessage.contains(Constants.TEMPLATE_USER_CONSTRAINT) || errorMessage.contains(Constants.TEMPLATE_COMPANY_CONSTRAINT)) {
				 return ErrorCodeEnum.ERR_21102.setErrorCode(result);
			 }
			else {
				return ErrorCodeEnum.ERR_12000.setErrorCode(result);
			}
		 }
		else {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
	}

	private Object _populateBulkWireTemplateLineItemsDTO(DataControllerRequest request, String user_id,
			String bulkWireTemplateId, String currentTimestamp) {

		StringBuilder lineItem_for_create = new StringBuilder();
		StringBuilder lineItem_for_update = new StringBuilder();
		ArrayList<StringBuilder> payloadBatches_for_create = new ArrayList<>();
		ArrayList<StringBuilder> payloadBatches_for_update = new ArrayList<>();

		JSONArray recipients = new JSONArray(request.getParameter("Recipients"));

		for (int i = 0; i < recipients.length(); i++) {
			JSONObject recipient = recipients.getJSONObject(i);

			String templateRecipientCategory = null;
			if (recipient.has("templateRecipientCategory")) {
				templateRecipientCategory = recipient.getString("templateRecipientCategory");
				if (!(templateRecipientCategory.equals(Constants.EXISTING_RECIPIENT)
						|| templateRecipientCategory.equals(Constants.MANUALLY_ADDED)
						|| templateRecipientCategory.equals(Constants.EXTRACTED_FROM_FILE))) {
					return "21100";
				}
				if (templateRecipientCategory.equals(Constants.EXISTING_RECIPIENT)) {
					if (!(recipient.has("payeeId"))) {
						return "21111";
					}
				}
			} else {
				return "21100";
			}
			
			StringBuilder newLineitem = getLineItemPayload(recipient, user_id, templateRecipientCategory);
			if (recipient.has("bulkWireTemplateLineItemID")) {
				if(lineItem_for_update.length() + newLineitem.length() > 60000) {
					lineItem_for_update.deleteCharAt(lineItem_for_update.length() - 1);
					payloadBatches_for_update.add(lineItem_for_update);
					lineItem_for_update = new StringBuilder();
				}
				lineItem_for_update.append("(");
				lineItem_for_update.append("\"").append(recipient.get("bulkWireTemplateLineItemID")).append("\"").append(",");
				lineItem_for_update.append("\"").append(bulkWireTemplateId).append("\"").append(",");
				lineItem_for_update.append("\"").append(currentTimestamp).append("\"").append(","); //lastmodifiedts
				lineItem_for_update.append(newLineitem);
				lineItem_for_update.append("),");
			} else {
				if(lineItem_for_create.length() + newLineitem.length() > 60000) {
					lineItem_for_create.deleteCharAt(lineItem_for_create.length() - 1);
					payloadBatches_for_create.add(lineItem_for_create);
					lineItem_for_create = new StringBuilder();
				}
				lineItem_for_create.append("(");
				lineItem_for_create.append("\"").append(bulkWireTemplateId).append("\"").append(",");
				lineItem_for_create.append("\"").append(currentTimestamp).append("\"").append(","); //createdts
				lineItem_for_create.append("\"").append(currentTimestamp).append("\"").append(","); //lastmodifiedts
				lineItem_for_create.append(newLineitem);
				lineItem_for_create.append("),");
			}
		}
		if(lineItem_for_update.length() > 0) {
			lineItem_for_update.deleteCharAt(lineItem_for_update.length() - 1);
		}
		if(lineItem_for_create.length() > 0) {
			lineItem_for_create.deleteCharAt(lineItem_for_create.length() - 1);
		}		
		payloadBatches_for_update.add(lineItem_for_update);
		payloadBatches_for_create.add(lineItem_for_create);

		JSONObject lineItemPayloads = new JSONObject();
		lineItemPayloads.put("lineItem_for_update", payloadBatches_for_update);
		lineItemPayloads.put("lineItem_for_create", payloadBatches_for_create);

		return lineItemPayloads;

	}

	private StringBuilder getLineItemPayload(JSONObject recipient, String user_id, String templateRecipientCategory) {

		String nullString = null;
		StringBuilder temp_builder = new StringBuilder();
		String[] keys = { "swiftCode", "bulkWireTransferType", "transactionType", "internationalRoutingNumber",
				"recipientName", "recipientAddressLine1", "recipientAddressLine2", "recipientCity", "recipientState",
				"recipientCountryName", "recipientZipCode", "recipientBankName", "recipientBankAddress1",
				"recipientBankAddress2", "recipientBankZipCode", "recipientBankcity", "recipientBankstate",
				"accountNickname", "recipientAccountNumber", "routingNumber" };

		for (String key : keys) {
			if (recipient.has(key) && !(recipient.isNull(key))) {
				temp_builder.append("\"").append(recipient.getString(key)).append("\"").append(",");
			} else {
				temp_builder.append(nullString).append(",");
			}
		}
		
		temp_builder.append("\"").append(user_id).append("\"").append(",");
		temp_builder.append("\"").append(user_id).append("\"").append(",");

		if (templateRecipientCategory.equals(Constants.EXISTING_RECIPIENT)) {
			temp_builder.append("\"").append(recipient.getString("payeeId")).append("\"").append(",");
		} else {
			temp_builder.append(nullString).append(",");
		}

		temp_builder.append("\"").append(templateRecipientCategory).append("\"");

		return temp_builder;
	}
	
	
	
	public Result getBulkWireTemplateLineItems(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];

		Result result = new Result();
		Map < String, Object > customer = CustomerSession.getCustomerMap(request);

		String bulkWireTemplateID = "";

		if (inputParams.get(Constants.BULKWIRETEMPLATEID) != null && inputParams.get(Constants.BULKWIRETEMPLATEID) != "") {
			bulkWireTemplateID = inputParams.get(Constants.BULKWIRETEMPLATEID).toString();
		} else {
		    alert.prepareError("BuklWireTemplateId  is missing in the payload which is mandatory to fetch the template details").log();
		    return ErrorCodeEnum.ERR_14009.setErrorCode(result);
		}
		 String user_id = CustomerSession.getCustomerId(customer);
	        String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
	        String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_TEMPLATES;
	        
	        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
	                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	        
	        Boolean isDomesticTemplatePermitted =false;
			Boolean isInternationalTemplatePermitted =false;
			
			String featureActionlistDomestic = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction1));		
			if (!StringUtils.isEmpty(featureActionlistDomestic)) {
				isDomesticTemplatePermitted = true;	
			}
			
			String featureActionlistInternational = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction2));		
			if (!StringUtils.isEmpty(featureActionlistInternational)) {
				isInternationalTemplatePermitted = true;	
			}
	       
	        BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);
	        
	        if(!isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
	        	return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	        }else {
	        	JSONObject delegateResponse = bulkWiresDelegate.getBWTemplateDomesticInternationalCount(bulkWireTemplateID,"");
	        	if(isDomesticTemplatePermitted && !isInternationalTemplatePermitted) {
	        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMDOMESTICTRANSACTIONS)) == 0)
	        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);	
	        	}else if(!isDomesticTemplatePermitted && isInternationalTemplatePermitted) {
	        		if(Integer.parseInt(delegateResponse.getString(Constants.NUMINTERNATIONALTRANSACTIONS)) == 0)
	        			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
	        	}
	        }
	      
		if (!bulkWiresDelegate.isTemplateAccessibleByUser(bulkWireTemplateID, customer)) {
		    alert.prepareError("User does not have enough permissions to access the details of the template with given id" + bulkWireTemplateID).log();
		    return ErrorCodeEnum.ERR_14010.setErrorCode(result);
		}
		List <BulkWireTemplateLineItemDTO> bulkWireTemplatelineitemDTOs = null;
		String sortBy = "";
		String sortOrder = "";
		String searchString = "";
		String groupBy = "";
		if (inputParams.get(Constants.SORTBYPARAM) != null) {
		    sortBy = inputParams.get(Constants.SORTBYPARAM).toString();
		}
		if (inputParams.get(Constants.SORTORDER) != null) {
		    sortOrder = inputParams.get(Constants.SORTORDER).toString();
		}
		if (inputParams.get(Constants.SEARCHSTRING) != null) {
		    searchString = inputParams.get(Constants.SEARCHSTRING).toString();
		}
		if (inputParams.get(Constants.GROUPBY) != null) {
			groupBy = inputParams.get(Constants.GROUPBY).toString();
		}
		try {
		    bulkWireTemplatelineitemDTOs = bulkWiresDelegate.getBulkWireTemplateLineItems(bulkWireTemplateID, sortBy, sortOrder, searchString,groupBy);
		} catch (Exception e) {
		    alert.prepareError("Error while fetching bulkWireTemplateLineItemDTO from BulkWireTemplateBusinessDelegate : " + e).log();
		    return ErrorCodeEnum.ERR_14008.setErrorCode(result);
		}
		try {
		    //for JSON Object use directly JSONToResult.convert(JSONObject) to get result
		    JSONArray rulesJSONArr = new JSONArray(bulkWireTemplatelineitemDTOs);
		    JSONObject responseObj = new JSONObject();
		    JSONObject finalRespObj = new JSONObject();
		  
		    if(groupBy.equalsIgnoreCase(Constants.BULKWIRETRANSFERTYPE)) {
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
		    responseObj.put(Constants.DOMESTIC_RECORDS, lineItemsDomesticJSONArr);
		    responseObj.put(Constants.INTERNATIONAL_RECORDS, lineItemsInternationalJSONArr);
		    finalRespObj.put(Constants.BULKWIRETEMPLATELINEITEMS, responseObj);
		    }
//		    else if (groupBy.equalsIgnoreCase(Constants.TEMPLATERECIPIENTCATEGORY)) {
//		    	JSONArray lineItemsExistingRecJSONArr = new JSONArray();
//			    JSONArray lineItemsManuallyAddedRecJSONArr = new JSONArray();
//			    JSONArray lineItemsExtractedFromFileRecJSONArr = new JSONArray();
//			    
//			    int ExistingRec, ManuallyAddedRec, ExtractedFromFileRec = 0;
//
//			    for (ExistingRec = 0; ExistingRec < rulesJSONArr.length(); ExistingRec++) {
//			        if (rulesJSONArr.getJSONObject(ExistingRec).getString(Constants.TEMPLATERECIPIENTCATEGORY).equalsIgnoreCase(Constants.EXISTING_RECIPIENT)) {
//			        	lineItemsExistingRecJSONArr.put(rulesJSONArr.getJSONObject(ExistingRec));
//			        } else {
//			        	break;
//			        }
//			    }
//			    for (ManuallyAddedRec = ExistingRec; ManuallyAddedRec < rulesJSONArr.length(); ManuallyAddedRec++) {
//			        if (rulesJSONArr.getJSONObject(ManuallyAddedRec).getString(Constants.TEMPLATERECIPIENTCATEGORY).equalsIgnoreCase(Constants.MANUALLY_ADDED)) {
//			        	lineItemsManuallyAddedRecJSONArr.put(rulesJSONArr.getJSONObject(ManuallyAddedRec));
//			        } else {
//			        	ExtractedFromFileRec = ManuallyAddedRec;
//			            break;
//			        }
//			    }
//			    if ((rulesJSONArr.length() > 0) && (rulesJSONArr.getJSONObject(ExtractedFromFileRec).getString(Constants.TEMPLATERECIPIENTCATEGORY).equalsIgnoreCase(Constants.EXTRACTED_FROM_FILE))) {
//			        while (ExtractedFromFileRec < rulesJSONArr.length()) {
//			        	lineItemsExtractedFromFileRecJSONArr.put(rulesJSONArr.getJSONObject(ExtractedFromFileRec));
//			            ExtractedFromFileRec++;
//			        }
//			    }
//			    responseObj.put(Constants.EXISTINGRECIPIENT_RECORD, lineItemsExistingRecJSONArr);
//			    responseObj.put(Constants.MANUALLYADDED_RECORD, lineItemsManuallyAddedRecJSONArr);
//			    responseObj.put(Constants.EXTRACTEDFROMFILE_RECORD, lineItemsExtractedFromFileRecJSONArr); 
//			    finalRespObj.put(Constants.BULKWIRETEMPLATELINEITEMS, responseObj);
//		    }
		    else
		    {
		    	finalRespObj.put(Constants.BULKWIRETEMPLATELINEITEMS, rulesJSONArr);
		    }
		    result = JSONToResult.convert(finalRespObj.toString());
		} catch (Exception e) {
		    alert.prepareError("Error while converting response bulkWirefilelineitemsDTOs to result " + e).log();
		    return null;
		}
        
		return result;
	}
	
	
	@Override
	public Result deleteBulkWireTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String user_id = CustomerSession.getCustomerId(customer);

		String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_DELETE_BULK_TEMPLATES;
		String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_DELETE_BULK_TEMPLATES;

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

		String bulkWireTemplateId = request.getParameter("bulkWireTemplateID");

		BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);
		
		if (!bulkWiresDelegate.isTemplateAccessibleByUser(bulkWireTemplateId, customer)) {
            diagnostic.prepareInfo("User does not have enough permissions to access the details of the template with given id" + bulkWireTemplateId).log();
            return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }

		JSONObject delegateResponse = bulkWiresDelegate.getBWTemplateDomesticInternationalCount(bulkWireTemplateId, "");
		if (Integer.parseInt(delegateResponse.getString("noOfDomesticTransactions")) > 0 && !(authorizationChecksBusinessDelegate
				.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer)))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		if (Integer.parseInt(delegateResponse.getString("noOfInternationalTransactions")) > 0 && !(authorizationChecksBusinessDelegate
				.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer)))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}

		Boolean status = bulkWiresDelegate.deleteBulkWireTemplate(bulkWireTemplateId);

		if (status == false) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		} else {
			result.addParam("Success", "BulkWireTemplate has been successfully deleted");
			return result;
		}
	}

	
	
	@Override
	public Result deleteBulkWireTemplateRecipient(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String user_id = CustomerSession.getCustomerId(customer);
		
		String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_DELETE_BULK_TEMPLATES;
		String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_DELETE_BULK_TEMPLATES;

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

		String bulkWireTemplateId = request.getParameter("bulkWireTemplateID");
		JSONArray recipients = new JSONArray(request.getParameter("Recipients"));
		StringBuilder bulkWireTemplateLineItemIDs = new StringBuilder();
		
		for(int i = 0; i< recipients.length(); i++){
			JSONObject recipient = recipients.getJSONObject(i);
			bulkWireTemplateLineItemIDs.append(recipient.get("bulkWireTemplateLineItemID")).append(",");
		}
		bulkWireTemplateLineItemIDs.deleteCharAt(bulkWireTemplateLineItemIDs.length() - 1);

		BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);

		JSONObject delegateResponse = bulkWiresDelegate.getBWTemplateDomesticInternationalCount(bulkWireTemplateId, bulkWireTemplateLineItemIDs.toString());
		if (Integer.parseInt(delegateResponse.getString("noOfDomesticTransactions")) > 0 && !(authorizationChecksBusinessDelegate
				.isUserAuthorizedForFeatureAction(user_id, featureAction1, null, CustomerSession.IsCombinedUser(customer)))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}
		if (Integer.parseInt(delegateResponse.getString("noOfInternationalTransactions")) > 0 && !(authorizationChecksBusinessDelegate
				.isUserAuthorizedForFeatureAction(user_id, featureAction2, null, CustomerSession.IsCombinedUser(customer)))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}		
		
		BulkWireDTO bulkWireDTO = bulkWiresDelegate.deleteBulkWireTemplateRecipient(bulkWireTemplateId, bulkWireTemplateLineItemIDs.toString());
		 
		 if(bulkWireDTO == null) {
			 return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		 }
		 else {
			 JSONObject respJson = new JSONObject(bulkWireDTO);
			 result = JSONToResult.convert(respJson.toString());
			 return result;
		 }
	}

	@Override
	public Result getUnselectedPayeesForBWTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String user_id = CustomerSession.getCustomerId(customer);

		String featureAction1 = FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_RECEPIENT;
		String featureAction2 = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_RECEPIENT;

		int isDomesticPermitted = 0, isInternationalPermitted = 0;
		
		String featureActionlistDomesticTemplate = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction1));		
		if (!StringUtils.isEmpty(featureActionlistDomesticTemplate)) {
			isDomesticPermitted = 1;	
		}
		
		String featureActionlistInternationalTemplate = CustomerSession.getPermittedActionIds(request, Arrays.asList(featureAction2));		
		if (!StringUtils.isEmpty(featureActionlistInternationalTemplate)) {
			isInternationalPermitted = 1;	
		}
		
		if (isDomesticPermitted == 0 && isInternationalPermitted == 0) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result);
		}

		String bulkWireTemplateId = request.getParameter("bulkWireTemplateID");
		String searchString = "";
		String sortByParam = "";
		String sortOrder = "";
		if (request.getParameter(Constants.SORTBYPARAM) != null)
			sortByParam = request.getParameter(Constants.SORTBYPARAM).toString();

		if (request.getParameter(Constants.SORTORDER) != null)
			sortOrder = request.getParameter(Constants.SORTORDER).toString();

		if (request.getParameter(Constants.SEARCHSTRING) != null)
			searchString = request.getParameter(Constants.SEARCHSTRING).toString();

		BulkWiresBusinessDelegate bulkWiresDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);

		List<PayeeDTO> payees = bulkWiresDelegate.getUnselectedPayeesForBWTemplate(bulkWireTemplateId, sortByParam,
				sortOrder, searchString, user_id, isDomesticPermitted, isInternationalPermitted);

		if (payees == null) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		} else {			
			JSONArray respJsonArray = new JSONArray(payees);
            JSONObject respJson = new JSONObject();
            respJson.put(Constants.UnSelectedPayees, respJsonArray);
            result = JSONToResult.convert(respJson.toString());
            return result;
		}

	}
}