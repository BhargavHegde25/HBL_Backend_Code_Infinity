package com.temenos.dbx.product.bulkpaymentservices.resource.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.constants.EventSubType;
import com.temenos.dbx.constants.EventType;
import com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api.BulkPaymentFileBackendDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentFileBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentRecordDTO;
import com.temenos.dbx.product.bulkpaymentservices.resource.api.BulkPaymentFileResource;

public class BulkPaymentFileResourceImpl implements BulkPaymentFileResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	BulkPaymentFileBusinessDelegate bulkPaymentFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentFileBusinessDelegate.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	BulkPaymentFileBackendDelegate bulkPaymentFileBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(BulkPaymentFileBackendDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	
	private final String HTTP_HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	private final String ATTACHMENT_FILE_NAME = "attachment; filename=\"Bulk Payment Acknowledgment.pdf\"";
	private final String FILE_DATE_FORMAT = "yyyyMMdd";
	private final String BACKEND_DATE_FORMAT = "yyyy-MM-dd";
	private final String UPLOAD_TYPE = "uploadFlow";
	
	@Override
	public Result uploadBulkPaymentFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		String fileId = String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp());      
		String codeFlow = inputParams.get("uploadType")!= null ? inputParams.get("uploadType").toString() : UPLOAD_TYPE;
        
		BulkPaymentFileDTO bulkPaymentfileDTO = _getBulkPaymentFileDTOByParsingInput(inputParams);

		if(bulkPaymentfileDTO.getDbpErrorCode() != null) {
			return bulkPaymentfileDTO.getDbpErrorCode().setErrorCode(new Result());
		}
		
		String featureActionId = "";
		String extension = FilenameUtils.getExtension(bulkPaymentfileDTO.getFileName());
		if("Single".equalsIgnoreCase(bulkPaymentfileDTO.getBatchMode())) {
			if("csv".equalsIgnoreCase(extension)) {
				featureActionId = FeatureAction.BULK_PAYMENT_FILES_SINGLE_UPLOAD_CSV;
			}else if("xml".equalsIgnoreCase(extension)) {
				featureActionId = FeatureAction.BULK_PAYMENT_FILES_SINGLE_UPLOAD_XML;
			}
		}else {
			if("csv".equalsIgnoreCase(extension)) {
				featureActionId = FeatureAction.BULK_PAYMENT_FILES_MULTI_UPLOAD_CSV;
			}else if("xml".equalsIgnoreCase(extension)) {
				featureActionId = FeatureAction.BULK_PAYMENT_FILES_MULTI_UPLOAD_XML;
			}
		}
		
		String fromAccount = bulkPaymentfileDTO.getFromAccount();
		
		if(StringUtils.isEmpty(fromAccount)) {
			return ErrorCodeEnum.ERR_21262.setErrorCode(new Result());						
		}
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, fromAccount);
		String contractId = "";
		String coreCustomerId = "";
		String companyId = "";
		
		if (account != null) {
		 contractId = account.getContractId();
		 coreCustomerId = account.getCoreCustomerId();
		 companyId = account.getOrganizationId();
		}
		
		String fromAccountEnding = "";
		if (fromAccount.length() > 3) 
			fromAccountEnding = fromAccount.substring(fromAccount.length() - 3);
		
		String errorMessage = "You do not have permission to the Debit Account ending xxx"+fromAccountEnding+". Kindly correct the file and re-upload";		

		AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
		
		if(codeFlow.equals(UPLOAD_TYPE)) {
		if (StringUtils.isNumeric(fromAccount)) {
			if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(createdby, fromAccount)) {
				return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
			}									
		}	
		else {
			if (!isOneOfMyAccountsIBAN(createdby,fromAccount)) {
				return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
			}								
		}
		
		if("csv".equalsIgnoreCase(extension)) {
			List<String> featureActionIdList = new ArrayList<>();
	        featureActionIdList.add(FeatureAction.BULK_PAYMENT_FILES_SINGLE_UPLOAD_CSV);
	        featureActionIdList.add( FeatureAction.BULK_PAYMENT_FILES_MULTI_UPLOAD_CSV);
	        String featureActionlistForCSV = CustomerSession.getPermittedActionIds(request, featureActionIdList);
	        if (StringUtils.isEmpty(featureActionlistForCSV)) {
	        	errorMessage ="You do not have permission to upload .csv file. Please re-upload the file with correct file format";
				return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
			}
		}
		else if("xml".equalsIgnoreCase(extension)) {
			List<String> featureActionIdList = new ArrayList<>();
	        featureActionIdList.add(FeatureAction.BULK_PAYMENT_FILES_SINGLE_UPLOAD_XML);
	        featureActionIdList.add( FeatureAction.BULK_PAYMENT_FILES_MULTI_UPLOAD_XML);
	        String featureActionlistForXML = CustomerSession.getPermittedActionIds(request, featureActionIdList);
	        if (StringUtils.isEmpty(featureActionlistForXML)) {
	        	errorMessage ="You do not have permission to upload .xml file. Please re-upload the file with correct file format";
				return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
			}			
		}
		
		if("Single".equalsIgnoreCase(bulkPaymentfileDTO.getBatchMode())) {
			List<String> featureActionIdList = new ArrayList<>();
	        featureActionIdList.add(FeatureAction.BULK_PAYMENT_FILES_SINGLE_UPLOAD_CSV);
	        featureActionIdList.add( FeatureAction.BULK_PAYMENT_FILES_SINGLE_UPLOAD_XML);
	        String featureActionlistForSingleMode = CustomerSession.getPermittedActionIds(request, featureActionIdList);
	        if (StringUtils.isEmpty(featureActionlistForSingleMode)) {
	        	errorMessage ="You do not have permission to perform Bulk Payments with processing mode as SINGLE";
				return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
			}					
		}
		
		else {
			List<String> featureActionIdList = new ArrayList<>();
	        featureActionIdList.add(FeatureAction.BULK_PAYMENT_FILES_MULTI_UPLOAD_CSV);
	        featureActionIdList.add( FeatureAction.BULK_PAYMENT_FILES_MULTI_UPLOAD_XML);
	        String featureActionlistForMultiMode = CustomerSession.getPermittedActionIds(request, featureActionIdList);
	        if (StringUtils.isEmpty(featureActionlistForMultiMode)) {
	        	errorMessage ="You do not have permission to perform Bulk Payments with processing mode as MULTI";
				return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
			}					
		}
		}
				
		SimpleDateFormat fileFormat = new SimpleDateFormat(FILE_DATE_FORMAT);
		SimpleDateFormat backEndFormat = new SimpleDateFormat(BACKEND_DATE_FORMAT);
		Date currentDate = new Date();
		try {
			currentDate = backEndFormat.parse(bulkPaymentFileBusinessDelegate.getCurrentDateForUpload(request));
		} catch (ParseException e2) {			
			return ErrorCodeEnum.ERR_21228.setErrorCode(result, "Error occured while parsing date");
		}
		Date backEndDate = new Date();
		try {
			backEndDate = backEndFormat.parse(bulkPaymentfileDTO.getPaymentDate());
		} catch (ParseException e1) {		
			return ErrorCodeEnum.ERR_21228.setErrorCode(result, "Error occured while parsing date");
		}	
		if (currentDate.after(backEndDate)) {
			errorMessage = "The execution date defined in the file is in Past, please correct the date in the file and re-upload";
			return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
		}
						
		String authCheckAccountId = getAccountIdFromIBAN(createdby,fromAccount);
		if (codeFlow.equals(UPLOAD_TYPE) && !authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, featureActionId,
				authCheckAccountId, CustomerSession.IsCombinedUser(customer))) {
			errorMessage = "You do not have permission to Bulk Payment Request for the Debit Account ending xxx"+fromAccountEnding+". Kindly correct the file and re-upload";
			return ErrorCodeEnum.ERR_21228.setErrorCode(result, errorMessage);
		}	
			
		BulkPaymentFileDTO bulkPaymentDTO = bulkPaymentFileBackendDelegate.uploadBulkPaymentFile(bulkPaymentfileDTO, request);
		
		if (bulkPaymentDTO == null) {
			alert.prepareError("Unable to store the file info at Backend").log();			
			return ErrorCodeEnum.ERR_21224.setErrorCode(new Result());
		}
		
		if(StringUtils.isNotBlank(bulkPaymentDTO.getDbpErrMsg())) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, bulkPaymentDTO.getDbpErrMsg());
		}
		
		String confirmationNumber = bulkPaymentDTO.getConfirmationNumber();
		
		if(confirmationNumber == null || "".equals(confirmationNumber)) {
			return ErrorCodeEnum.ERR_12601.setErrorCode(result);
		}
		
		bulkPaymentfileDTO.setCompanyId(companyId);
		bulkPaymentfileDTO.setRoleId(customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		bulkPaymentfileDTO.setCreatedby(createdby);
		bulkPaymentfileDTO.setUploadedBy(createdby);
		bulkPaymentfileDTO.setFeatureActionId(featureActionId);
		bulkPaymentfileDTO.setCreatedts(application.getServerTimeStamp());
		bulkPaymentfileDTO.setConfirmationNumber(confirmationNumber);
		bulkPaymentfileDTO.setFileId(fileId);
		bulkPaymentfileDTO = bulkPaymentFileBusinessDelegate.uploadBulkPaymentFileAtDBX(bulkPaymentfileDTO);
		
		if (bulkPaymentfileDTO == null) {
			alert.prepareError("BulkPayment file dto is null. Error occured while uploading Bulk Payment File").log();
			return ErrorCodeEnum.ERR_21204.setErrorCode(new Result());			
		}

		try {
			JSONObject bulkPaymentfileObject = new JSONObject(bulkPaymentDTO);
			result = JSONToResult.convert(bulkPaymentfileObject.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the file to JSON", e).log();
			return ErrorCodeEnum.ERR_21200.setErrorCode(new Result());			
		}
		
	    try {
	    	_logBulkPaymentFile(request, response, result, bulkPaymentfileDTO);
	    }
	    catch(Exception e){
	        alert.prepareError("Error occured while logging the file details", (Throwable)e).log();  	
	    }
		return result;
	}

	/**
	 * Parses input data by validating the file details and returns the BulkPaymentFileDTO containing file details 
	 * Map<String, Object> inputParams - request input params
	 * @return BulkPaymentFileDTO - if success it sets file details otherwise it sets dbpErrCode to bulkPaymentFileDTO and returns the same
	 */
	private BulkPaymentFileDTO _getBulkPaymentFileDTOByParsingInput(Map<String, Object> inputParams) {
		BulkPaymentFileDTO bulkPaymentFileDTO = new BulkPaymentFileDTO();
		
		String fileName = inputParams.get("fileName") != null ? inputParams.get("fileName").toString().trim() : null;
		if(fileName != null) {
			fileName = FilenameUtils.getName(fileName);
		}
		if (!_isValidFileName(fileName) || fileName == null || fileName.isEmpty()) {
			alert.prepareError("You have added an Invalid file. Please upload file with correct filename and extension .csv or .xml").log();			
			bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21219);
			return bulkPaymentFileDTO; 
		}		
		bulkPaymentFileDTO.setFileName(fileName);
		
		String content = inputParams.get("content") != null ? inputParams.get("content").toString() : null;
		if (content == null || content.isEmpty()) {
			alert.prepareError("Content is Empty. Error occured while Parsing Bulk Payment File").log();			
			bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21204);
			return bulkPaymentFileDTO;
		}
		bulkPaymentFileDTO.setContent(content);
		
		String description = inputParams.get("description") != null ? inputParams.get("description").toString() : null;
		if (description == null || description.isEmpty()) {
			alert.prepareError("Description is Empty. Error occured while Parsing Bulk Payment File").log();			
			bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21206);
			return bulkPaymentFileDTO;
		}
		bulkPaymentFileDTO.setDescription(description);
		
		String batchMode = inputParams.get("batchMode") != null ? inputParams.get("batchMode").toString() : null;
		if (StringUtils.isEmpty(batchMode)) {
			alert.prepareError("Processing Mode is not selected during file upload. Error occured while Parsing Bulk Payment File").log();			
			bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21256);
			return bulkPaymentFileDTO;
		}
		bulkPaymentFileDTO.setBatchMode(batchMode);
		
		File uploadedFile = null;
		String fileDir = System.getProperty("java.io.tmpdir");
		String fileBaseName = FilenameUtils.getBaseName(fileName);
		String fileExtension = FilenameUtils.getExtension(fileName);

		// Decoding File contents of base64 format and writing into file
		try {
			uploadedFile = new File(fileDir, fileBaseName + "." + fileExtension);
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(uploadedFile));
			byte[] bulkPaymentFileContent = Base64.decodeBase64(content);
			fileWriter.write(new String(bulkPaymentFileContent, "UTF-8"));
			fileWriter.close();
			if(uploadedFile.length()/(1024*1024)>20) {
				alert.prepareError("The file size exceeds the maximum limit of 20 mb. Please retry with a lesser file size").log();			
				bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_26003);
				return bulkPaymentFileDTO; 
			}
			
			BulkPaymentRecordDTO bulkPaymentRecordDTO = this.bulkPaymentFileBusinessDelegate.fetchBulkPaymentRecord(uploadedFile);
			if (bulkPaymentRecordDTO == null) {
				alert.prepareError("Error occured while fetching Bulk Payment File").log();				
				bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21200);
				return bulkPaymentFileDTO;
			} 
			if(bulkPaymentRecordDTO.getDbpErrorCode() != null) {
				bulkPaymentFileDTO.setDbpErrorCode(bulkPaymentRecordDTO.getDbpErrorCode());
				bulkPaymentFileDTO.setDbpErrMsg(bulkPaymentRecordDTO.getDbpErrMsg());
				return bulkPaymentFileDTO;
			}
			if(!bulkPaymentFileDTO.getBatchMode().equalsIgnoreCase(bulkPaymentRecordDTO.getBatchMode())) {
				alert.prepareError("The Processing mode specified in the file upload is not matching with the batch process selected. Kindly correct the file and reupload or select the right batch processing mode").log();			
				bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21257);
				return bulkPaymentFileDTO;
			}
			bulkPaymentFileDTO.setBatchMode(bulkPaymentRecordDTO.getBatchMode());
			bulkPaymentFileDTO.setFileSize(String.valueOf(uploadedFile.getTotalSpace()));
			bulkPaymentFileDTO.setFileName(uploadedFile.getName());
			bulkPaymentFileDTO.setFromAccount(bulkPaymentRecordDTO.getFromAccount());
			bulkPaymentFileDTO.setTotalAmount(bulkPaymentRecordDTO.getTotalAmount());
			bulkPaymentFileDTO.setTotalTransactions(bulkPaymentRecordDTO.getTotalTransactions());
			bulkPaymentFileDTO.setPaymentDate(bulkPaymentRecordDTO.getPaymentDate());
			
		} 
		catch (Exception e) {
			alert.prepareError("Error occured while Parsing Bulk Payment File", e).log();			
			bulkPaymentFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_21200);
			return bulkPaymentFileDTO;
		}
		finally {
			if (uploadedFile != null) {
				uploadedFile.delete();
			}
		}
		return bulkPaymentFileDTO;
	}
	
	/**
	 * Checks whether given file name is valid or not
	 * String fileName of uploaded file
	 * @return boolean - returns true if valid otherwise false
	 */
	private boolean _isValidFileName(String fileName) {
		String fileBaseName = null;
		String fileExtension = null;

		if (fileName != null && !fileName.isEmpty() && Pattern.matches("^[\\w+]++(.csv|.xml)$", fileName.toLowerCase())) {
			fileBaseName = FilenameUtils.getBaseName(fileName);
			fileExtension = FilenameUtils.getExtension(fileName);

			if (fileBaseName != null && !fileBaseName.isEmpty() && fileExtension != null && !fileExtension.isEmpty()) {
				return true;
			}
		}

		return false;
	}
	
	private boolean isOneOfMyAccountsIBAN(String createdBy, String fromAccountIBAN) {
	
		JSONObject myAccountsjson = authorizationChecksBusinessDelegate.fetchMyAccounts(createdBy);		
		
		if(StringUtils.isNotEmpty(myAccountsjson.toString()) && myAccountsjson.toString().contains(fromAccountIBAN))
			return true;
				
		return false;
	}
	
	private String getAccountIdFromIBAN(String createdBy, String fromAccountIBAN) {
		
		if (StringUtils.isNumeric(fromAccountIBAN)) {
			return fromAccountIBAN;
		}
		else {			
		JSONObject myAccountsjson = authorizationChecksBusinessDelegate.fetchMyAccounts(createdBy);				
		if (StringUtils.isNotEmpty(myAccountsjson.toString())
				&& StringUtils.isNotEmpty(myAccountsjson.keySet().toString())
				&& myAccountsjson.toString().contains(fromAccountIBAN)) {
			for (String accKey : myAccountsjson.keySet()) {
				if (myAccountsjson.get(accKey).toString().contains(fromAccountIBAN))
					return accKey;
			}
		}
	}
		return fromAccountIBAN;
	}
	
	@Override
	public Result fetchBulkPaymentSampleFiles(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		Result result;
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_FILES_VIEW);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
		if(features == null) {
     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		List<BulkPaymentFileDTO> files = bulkPaymentFileBackendDelegate.fetchBulkPaymentSampleFiles(request);
		
		if(files == null) {
            alert.prepareError("Error occurred while fetching bulk payment sample files from backend").log();
            return ErrorCodeEnum.ERR_21218.setErrorCode(new Result());
        }
        if(files.size() > 0 && StringUtils.isNotBlank(files.get(0).getDbpErrMsg())) {
        	alert.prepareError("Error occurred while fetching bulk payment sample files from backend").log();
        	return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), files.get(0).getDbpErrMsg());
        }
        
	    try {
            JSONArray records = new JSONArray(files);
            JSONObject resultObject = new JSONObject();
            resultObject.put(Constants.SAMPLEFILES,records);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_21218.setErrorCode(new Result());
        }

		return result;
	}
	
	@Override
	public Result fetchBulkPaymentUploadedFiles(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result;
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_FILES_VIEW);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
		if(features == null) {
     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
		String timeValue = inputParamsMap.get("timeValue") != null ? inputParamsMap.get("timeValue").toString() : null;
		String timeParam = inputParamsMap.get("timeParam") != null ? inputParamsMap.get("timeParam").toString() : null;
		Result executionDates = fetchExecutionDates(timeValue, request, response);
		String fromDate = executionDates.getParamValueByName("fromDate");
		String toDate = executionDates.getParamValueByName("toDate");
		FilterDTO filterDTO;
		try {
			filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
		}
		
		List<BulkPaymentFileDTO> files = bulkPaymentFileBusinessDelegate.fetchBulkPaymentUploadedFilesfromBackend(fromDate, toDate, request);
		if(bulkPaymentFileBackendDelegate.getClass().getName().toString().contains("BulkPaymentFileBackendDelegateImplExtn"))
		{
			filterDTO.set_timeParam("");
		}
		if(files == null) {
            alert.prepareError("Error occurred while fetching bulk payment uploaded files from backend").log();
            return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
        }
		if(files.size() > 0 && StringUtils.isNotBlank(files.get(0).getDbpErrMsg())) {
        	alert.prepareError("Error occurred while fetching bulk payment uploaded files from backend").log();
        	return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), files.get(0).getDbpErrMsg());
        }
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		boolean isCombinedUser = CustomerSession.IsCombinedUser(customer);
		Set<String> unAuthorizedAccounts = authorizationChecksBusinessDelegate.fetchUnAuthorizedAccounts(
				customerId, FeatureAction.BULK_PAYMENT_FILES_VIEW, isCombinedUser);
		filterDTO.set_removeByParam("fromAccount");
		filterDTO.set_removeByValue(unAuthorizedAccounts);
		List<BulkPaymentFileDTO> filteredFiles = filterDTO.filter(files);
		
		try {
            JSONArray uploadedFiles = new JSONArray(filteredFiles);
            JSONObject resultObject = new JSONObject();
            resultObject.put(Constants.UPLOADEDFILES,uploadedFiles);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
        }

		return result;
	}

	@Override
	public Result initiateBulkPaymentAckFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		byte[] bytes = new byte[0];
		Result result = new Result();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];	
		
		String recordId = inputParams.get("recordId") != null ? inputParams.get("recordId").toString() : null;
		String requestId = inputParams.get("requestId") != null ? inputParams.get("requestId").toString() : null;
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);

		if(StringUtils.isBlank(recordId)) {
			alert.prepareError("recordId is missing").log();
			return ErrorCodeEnum.ERR_13525.setErrorCode(new Result(), ErrorConstants.PROVIDE_MANDATORY_FIELDS);
		}               

		try {											
			bytes = bulkPaymentFileBusinessDelegate.getRecordPDFAsBytes(recordId, requestId, customerId, request);                                
			String fileId = _getbulkPaymentFileID();
			if(fileId == null) {
				alert.prepareError("Error while generating fileID").log(); 
				return ErrorCodeEnum.ERR_21255.setErrorCode(result);
			}
			MemoryManager.saveIntoCache(fileId, Base64.encodeBase64String(bytes), 120);
			result.addParam("fileId", fileId);
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while generating the bulk payments file",e).log();
		}
		return ErrorCodeEnum.ERR_21255.setErrorCode(new Result());
	}

	@Override
	public Result downloadBulkPaymentAckFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();

		@SuppressWarnings("unchecked")
		Map < String, Object > inputParams = (HashMap < String, Object > ) inputArray[1];

		String fileId = inputParams.get("fileId") != null ? inputParams.get("fileId").toString() : null;

		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
		customHeaders.put(HTTP_HEADER_CONTENT_DISPOSITION, ATTACHMENT_FILE_NAME); 

		if (StringUtils.isBlank(fileId)) {			
			alert.prepareError("FileId  is missing in the payload which is mandatory to fetch the file details").log();
			return ErrorCodeEnum.ERR_14017.setErrorCode(result);
		}

		String FileDetails = (String)MemoryManager.getFromCache(fileId);
		byte[] bytes = Base64.decodeBase64(FileDetails);

		try {
			response.getHeaders().putAll(customHeaders);
			response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
					new BufferedHttpEntity(new ByteArrayEntity(bytes)));
			response.setStatusCode(HttpStatus.SC_OK);
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while downloading the bulk payments pdf",e).log();
		}
		return ErrorCodeEnum.ERR_21252.setErrorCode(new Result());							
	}

	private String _getbulkPaymentFileID() {
		String bulkFileId = null;          
		int n = 32;
		String id = CommonUtils.generateUniqueID(n);
		if(id != null) {
			bulkFileId = id.substring(0,8) + "-" + id.substring(8,16) + "-" + id.substring(16,24) + "-"+ id.substring(24,32);
			return bulkFileId;
		}
		return id;
	}
	
	/**
	 * Logs BulkPaymentFile status in auditactivity
	 * @param request
	 * @param response
	 * @param result
	 * @param userName
	 * @param bulkPaymentfileDTO
	 */
	private void _logBulkPaymentFile(DataControllerRequest request, DataControllerResponse response, Result result, BulkPaymentFileDTO bulkPaymentfileDTO) {
		
		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE)) return;
		try {

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userName = CustomerSession.getCustomerName(customer);
			String eventType = EventType.BULK_PAYMENT_FILE;
			String eventSubType = EventSubType.BULK_PAYMENT_FILE_UPLOAD;
			String producer = "BulkPaymentObjects/UploadFile";
			String statusID = "";
			String fileId = bulkPaymentfileDTO.getFileId();
			int amount;
			amount = (int)bulkPaymentfileDTO.getTotalAmount();
			JsonObject customParams = new JsonObject();
			List<Param> params = result.getAllParams();
			for (Param param : params) {
				if (request.containsKeyInRequest(param.getName()) || param.getName().equalsIgnoreCase("content")) {
					continue;
				} else {
					customParams.addProperty(param.getName(), param.getValue());
				}
			}
			customParams.addProperty("uploadedBy", userName);
			AuditLog auditLog = new AuditLog();
			customParams = auditLog.buildCustomParamsForAlertEngine(bulkPaymentfileDTO.getFromAccount(), "", customParams);
			customParams.addProperty("fromAccountNumber", bulkPaymentfileDTO.getFromAccount());
			customParams.addProperty("totalAmount", bulkPaymentfileDTO.getTotalAmount());
			customParams.addProperty("totalTransactions", bulkPaymentfileDTO.getTotalTransactions());
			if (StringUtils.isEmpty(fileId)) {
				statusID = Constants.SID_EVENT_FAILURE;
			} else {
				statusID = Constants.SID_EVENT_SUCCESS;
				customParams.addProperty(Constants.AMOUNT, amount);
				customParams.addProperty(Constants.REFERENCEID, fileId);
			}
			
			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null, null,
					customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine.", e).log();
		}
	}
	
	public Result fetchExecutionDates(String timeValue, DataControllerRequest request, 
			DataControllerResponse response) {

		Result result = new Result();
		
		SimpleDateFormat backEndFormat = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
		Date currentDate = new Date();
		Date requiredDate = new Date();
		try {
			 currentDate = backEndFormat.parse(application.getServerTimeStamp());
			//currentDate = backEndFormat.parse(bulkPaymentFileBusinessDelegate.getCurrentDateForUpload(request));
		} catch (ParseException e2) {			
			return ErrorCodeEnum.ERR_21228.setErrorCode(new Result(), "Error occured while parsing date");
		}
		if(timeValue.isEmpty() == true)
		{
			timeValue = "6, MONTH";
		}
		String[] timeValues = timeValue.split("\\,");
		if(!(timeValues.length<2 || !StringUtils.isNumeric(timeValues[0]) || StringUtils.isEmpty(timeValues[1]))) {
		
		int period = Integer.parseInt(timeValues[0]) * (-1);
		if(timeValues[1].toUpperCase().contains(Constants.DAY)) {
			requiredDate = DateUtils.addDays(currentDate, period);
		} else if(timeValues[1].toUpperCase().contains(Constants.WEEK)) {
			requiredDate = DateUtils.addWeeks(currentDate, period);
		}else if(timeValues[1].toUpperCase().contains(Constants.MONTH)) {
			requiredDate = DateUtils.addMonths(currentDate, period);
		}else if(timeValues[1].toUpperCase().contains(Constants.YEAR)) {
			requiredDate = DateUtils.addYears(currentDate, period);
		}
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		result.addStringParam("fromDate", formatter.format(requiredDate));
		result.addStringParam("toDate", formatter.format(currentDate));
		}
		else
		{
			result.addStringParam("fromDate", "");
			result.addStringParam("toDate", "");
		}
		
		return result;
	}
}
