package com.temenos.dbx.product.achservices.resource.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHFileBackendDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHFileBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileFormatTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHFileRecordDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHFileResource;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApproversBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.TransactionLimitsBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.commonsutils.LogEvents;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public class ACHFileResourceImpl implements ACHFileResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHFileBusinessDelegate.class);
	TransactionLimitsBusinessDelegate limitsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(TransactionLimitsBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	AccountBusinessDelegate actDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	ACHFileBackendDelegate achFileBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(ACHFileBackendDelegate.class);
	ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
	
	@Override
	public Result uploadACHFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String createdby = CustomerSession.getCustomerId(customer);
		boolean isSMEUser = CustomerSession.IsBusinessUser(customer);
		String featureActionId = FeatureAction.ACH_FILE_UPLOAD;
		
		ACHFileDTO achfileDTO = null;

		achfileDTO = _getACHFileDTOByParsingInput(inputParams);

		if (achfileDTO.getDbpErrorCode() != null) {
			return achfileDTO.getDbpErrorCode().setErrorCode(result);
		}

		achfileDTO.setCreatedby(createdby);
		achfileDTO.setFeatureActionId(featureActionId);

		List<ACHFileRecordDTO> fileRecordsDTO = achfileDTO.getFileRecords();

		Map<String, Double> offsetDetails = _getOffsetAccountDetails(fileRecordsDTO);
		if (offsetDetails == null || offsetDetails.isEmpty()) {
			alert.prepareError("Error occured while getting offset account details").log();
			return ErrorCodeEnum.ERR_12046.setErrorCode(result);
		}
		
		Set<String> accounts = new HashSet<String>();
		for(String key : offsetDetails.keySet()){
			accounts.add(key.split("_")[0]);
		}
		
		AccountBusinessDelegate accountDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
		CustomerAccountsDTO accountDTO = accountDelegate.getCommonContractCustomer(createdby, accounts);
		
		if(accountDTO == null) {
			alert.prepareError("All the offset accounts doesn't belong to same CIF").log();
			return ErrorCodeEnum.ERR_12061.setErrorCode(result);
		}
		
		String contractId = accountDTO.getContractId();
		String coreCustomerId = accountDTO.getCoreCustomerId();
		String companyId = accountDTO.getOrganizationId();
		
		achfileDTO.setCompanyId(companyId);
		achfileDTO.setRoleId(customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		
		String accountNumbers = String.join(",", accounts); 
		achfileDTO.setDebitAccounts(accountNumbers);
		
		if(! authorizationChecksBusinessDelegate.isOneOfMyAccounts(createdby, Arrays.asList(accountNumbers.split(",")))) {
			alert.prepareError("User is not Authorized for the account numbers used").log();
			return ErrorCodeEnum.ERR_12059.setErrorCode(result);
		}

		if (! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(createdby, FeatureAction.ACH_FILE_UPLOAD, accountNumbers, CustomerSession.IsCombinedUser(customer))) {
			alert.prepareError("User is not Authorized").log();
			return ErrorCodeEnum.ERR_12050.setErrorCode(result);
		}

		achfileDTO.setStatus(TransactionStatusEnum.NEW.getStatus());
		String baseCurrency  = application.getBaseCurrencyFromCache();
		String validate = inputParams.get("validate") == null ? null : inputParams.get("validate").toString();
		String backendid = inputParams.get("transactionId") == null || (StringUtils.isEmpty(inputParams.get("transactionId").toString())) ? null : inputParams.get("transactionId").toString();
		String requestid = "";
		
		if("true".equalsIgnoreCase(validate)) {
			ACHFileDTO validateachfileDTO = achFileBackendDelegate.validateTransaction(achfileDTO,request);
			try {
				 result = JSONToResult.convert(new JSONObject(validateachfileDTO).toString());
				 return result;
			} catch (JSONException e) {
				alert.prepareError("Error occured while converting the response from Line of Business service for ACH file transfer: ", e).log();
				return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
			}
		} 
		
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setCustomerId(createdby);
        transactionStatusDTO.setCompanyId(companyId);
        transactionStatusDTO.setStatus(TransactionStatusEnum.NEW);
        transactionStatusDTO.setFeatureActionID(featureActionId);
        transactionStatusDTO.setOffsetDetails(offsetDetails);
        transactionStatusDTO.setConfirmationNumber(backendid);
        
        transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request); 
        if(transactionStatusDTO == null) {			
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
        
		TransactionStatusEnum transactionStatus = transactionStatusDTO.getStatus();
		boolean isSelfApproved = transactionStatusDTO.isSelfApproved();
		achfileDTO.setStatus(transactionStatus.getStatus());
		achfileDTO.setRequestId(transactionStatusDTO.getRequestId());
		String confirmationNumber = (StringUtils.isEmpty(backendid)) ? Constants.REFERENCE_KEY + transactionStatusDTO.getRequestId() : backendid;
		achfileDTO.setConfirmationNumber(confirmationNumber);
		achfileDTO.setTransactionAmount(transactionStatusDTO.getTransactionAmount());
		achfileDTO.setDebitAmount(transactionStatusDTO.getAmount());
		achfileDTO.setServiceCharge(transactionStatusDTO.getServiceCharge());
		achfileDTO.setTransactionCurrency(baseCurrency);

		// Upload ACHFile At DBX
		ACHFileDTO achfiledbxDTO = new ACHFileDTO();
		try {
			achfiledbxDTO = achFileBusinessDelegate.uploadACHFileAtDBX(achfileDTO);
		} catch (Exception e) {
			alert.prepareError("Caught exception at uploadACHFile method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		if (achfiledbxDTO == null) {
			alert.prepareError("ACH file dto is null. Error occured while uploading ACH File").log();
			return ErrorCodeEnum.ERR_29016.setErrorCode(result);
		}

		try {
			JSONObject achfileObject = new JSONObject(achfiledbxDTO);
			result = JSONToResult.convert(achfileObject.toString());
		} catch (JSONException e) {
			return ErrorCodeEnum.ERR_21217.setErrorCode(new Result());
		}
		
		String achFileId = achfiledbxDTO.getAchFile_id();
		achfiledbxDTO.setEffectiveDate(application.getServerTimeStamp());
		
		ACHFileDTO achfiletransactionDTO = new ACHFileDTO();
		if(transactionStatus == TransactionStatusEnum.SENT ) {
			if(StringUtils.isEmpty(backendid)) {
				achfiledbxDTO.setAchFile_id(null);
				achfiletransactionDTO = achFileBackendDelegate.createTransactionWithoutApproval(achfiledbxDTO, request);					
				if(achfiletransactionDTO == null) {	
					achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_12601.setErrorCode(result);
				}
			}
			else {
				achfiletransactionDTO = achFileBusinessDelegate.approveTransaction(backendid, request);
				if(achfiletransactionDTO == null) {	
					achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_29020.setErrorCode(result);
				}
			}
			if(achfiletransactionDTO.getDbpErrorCode() != null || achfiletransactionDTO.getDbpErrMsg() != null) {
				achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
                return ErrorCodeEnum.ERR_00000.setErrorCode(result, achfiletransactionDTO.getDbpErrMsg());
			}
			if(achfiletransactionDTO.getReferenceID() == null || "".equals(achfiletransactionDTO.getReferenceID())) {
				achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
				return ErrorCodeEnum.ERR_12601.setErrorCode(result);
			}
			achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.EXECUTED.getStatus(), achfiletransactionDTO.getReferenceID());
			result.addParam(new Param("achFile_id", achfiletransactionDTO.getReferenceID()));
			result.addParam(new Param("referenceId", achfiletransactionDTO.getReferenceID()));
			result.addParam(new Param("status", transactionStatus.getStatus()));
	        result.addParam(new Param("message", transactionStatus.getMessage()));
		}
		else if(transactionStatus == TransactionStatusEnum.PENDING){
			requestid = transactionStatusDTO.getRequestId();
			String pendingrefId= null;
			if(StringUtils.isEmpty(backendid))
			{
				achfiletransactionDTO = achFileBusinessDelegate.createPendingTransaction(achfiledbxDTO, request);
				if(achfiletransactionDTO == null)
				{
					achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					alert.prepareError("Error occured while creating entry into the backend table: ").log();
					return ErrorCodeEnum.ERR_29017.setErrorCode(new Result());
				}
				if(achfiletransactionDTO.getDbpErrorCode() != null || achfiletransactionDTO.getDbpErrMsg() != null) {
					achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), confirmationNumber);
					return ErrorCodeEnum.ERR_00000.setErrorCode(result, achfiletransactionDTO.getDbpErrMsg());
				}
				backendid = achfiletransactionDTO.getReferenceID();
			}
				pendingrefId= backendid;
				achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, transactionStatus.toString(), backendid);
				transactionStatusDTO = approvalQueueDelegate.updateBackendIdInApprovalQueue(requestid, backendid, isSelfApproved, featureActionId, request);
				if(transactionStatusDTO == null) 
				{							
					achFileBackendDelegate.deleteTransaction(backendid, null, request);
					achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), backendid);
					return ErrorCodeEnum.ERR_29019.setErrorCode(new Result());
				}	
				if(transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
					achFileBackendDelegate.deleteTransaction(backendid, null, request);
					achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, TransactionStatusEnum.FAILED.getStatus(), backendid);
					result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
					result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
					return result;
				}
				transactionStatus = transactionStatusDTO.getStatus();
				backendid = transactionStatusDTO.getConfirmationNumber();
				
			/**
			 * Start: Added as part of Alerts
			 */
			try {
				LogEvents.pushAlertsForApprovalRequests( featureActionId, request, response,null, achfileDTO, 
						backendid, requestid, CustomerSession.getCustomerName(customer),null);
			} catch (Exception e) {
				alert.prepareError("Failed at pushAlertsForApprovalRequests "+e).log();
			}
			/**
			 * End: Added as part of Alerts
			 */
			result.addParam(new Param("requestId", requestid));
			

			if (transactionStatus == TransactionStatusEnum.APPROVED) {
				result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
				result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
				result.addParam(new Param("referenceId", backendid));
				result.addParam(new Param("achFile_id", backendid));
			}
			else {
				result.addParam(new Param("status", transactionStatus.getStatus()));
				result.addParam(new Param("message", transactionStatus.getMessage()));
				result.addParam(new Param("referenceId", pendingrefId));
				result.addParam(new Param("achFile_id", pendingrefId));
				achFileBusinessDelegate.updateStatusUsingTransactionId(achFileId, transactionStatus.toString(), pendingrefId);
			}
		}
		else if(transactionStatus == TransactionStatusEnum.APPROVED){
			result.addParam(new Param("achFile_id", confirmationNumber));
			result.addParam(new Param("referenceId", transactionStatusDTO.getConfirmationNumber()));
	        result.addParam(new Param("status", TransactionStatusEnum.SENT.getStatus()));
	        result.addParam(new Param("message", TransactionStatusEnum.SENT.getMessage()));
		}
		
		if(!application.getIsStateManagementAvailableFromCache()) {
			result.addParam(new Param("achFile_id", achFileId));
		}
		
		try {
			_logACHFile( request,response,result,transactionStatus,CustomerSession.getCustomerName(customer),transactionStatusDTO.getConfirmationNumber(),requestid,isSMEUser);
		} catch(Exception e) {
			alert.prepareError("Error occured while audit logging.",e).log();
		}

		// ADP-7058 update additional meta data
		try{
			approvalQueueDelegate.updateAdditionalMetaForApprovalRequest(transactionStatusDTO.getRequestId(), request);
		} catch(Exception e){
			alert.prepareError(e.toString()).log();
		}
		
		return result;
	}

	@Override
	public Result getACHFileFormats(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = null;
		
		List<ACHFileFormatTypeDTO> fileformatDTOs = achFileBusinessDelegate.getACHFileFormats();
		if (fileformatDTOs == null) {
			alert.prepareError("Exception occured as fileformatDTO is NULL").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		JSONArray fileFormatsJSONArr;
		try {
			fileFormatsJSONArr = new JSONArray(fileformatDTOs);
			JSONObject fileFormatsList = new JSONObject();
			fileFormatsList.put("achfileformats", fileFormatsJSONArr);
			result = JSONToResult.convert(fileFormatsList.toString());
		} 
		catch (JSONException e) {
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

	@Override
	public Result fetchAllACHFiles(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = null;

		try {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);

			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_FILE_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			if (featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);

			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			if(!params.isValidFilter()) {
				alert.prepareError("Input contains special characters").log();
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, new JSONArray());
				return JSONToResult.convert(resultObj.toString());
			}

			List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, "", params, request);
			if (files != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(files, ACHFileDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			} 
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while defining resources for fetch all files", exp).log();
			return null;
		}
		
		return result;
	}

	@Override
	public Result fetchACHFileById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = null;
		ACHFileBusinessDelegate achFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHFileBusinessDelegate.class);

		try{
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			FilterDTO filters = new FilterDTO();
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			String achfile_id = filterParamsMap.get("achfile_id").toString();

			List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, achfile_id, filters, request);
			if(files == null) {
				return ErrorCodeEnum.ERR_10308.setErrorCode(new Result());
			}
			if(files.size()!=0) {
				return ErrorCodeEnum.ERR_10308.setErrorCode(new Result());
			}
		}catch (Exception e){
			alert.prepareError("Error occurred while fetching ach file by id", e).log();
			return null;
		}

		try {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);

			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_FILE_VIEW);
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			if (featureActionId == null) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			String achfile_id = filterParamsMap.get("achfile_id").toString();
			if (achfile_id == null || achfile_id.isEmpty()) {
				alert.prepareError("achfile_id is missing").log();
				return ErrorCodeEnum.ERR_12041.setErrorCode(new Result());
			}

			filterParamsMap.put(Constants._FEATURE_ACTION_LIST, featureActionId);

			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);

			ACHFileDTO achDTO = achFileBusinessDelegate.fetchTransactionById(achfile_id, request);
			if(achDTO == null) {
				alert.prepareError("Record Doesn't Exist").log();
	            return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
			}
			
			achfile_id = achDTO.getAchFile_id();
			
			List<ACHFileDTO> files = achFileBusinessDelegate.fetchACHFiles(userId, achfile_id, params, request);
			/*List<ACHFileDTO> files = new ArrayList<ACHFileDTO>();
			ACHFileDTO file = achFileBusinessDelegate.fetchTransactionById(achfile_id, request);
			files.add(file); */
			if (files != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(files, ACHFileDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			} 
			else
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while fetching ach file by id", exp).log();
			return null;
		}
		
		return result;
	}

	/**
	 * creates a offset account details map containing offset account as key and offset debit amount as value
	 * List<ACHFileRecordDTO> achFileRecordListDTO - List of ACH File Record details
	 * @return Map<String,Double> - map containing offset account and associated debit amount (in case of credit - debit amount will be 0)
	 */
	private Map<String, Double> _getOffsetAccountDetails(List<ACHFileRecordDTO> achFileRecordListDTO) {
		Map<String, Double> offsetDetails = new HashMap<String, Double>();

		for (int i = 0; i < achFileRecordListDTO.size(); i++) {
			ACHFileRecordDTO fileRecord = achFileRecordListDTO.get(i);
			String accountNumber = fileRecord.getOffsetAccountNumber();
			String effectiveDate = fileRecord.getEffectiveDate();
            double debitAmount = ACHConstants.ACH_TRANSACTION_TYPE_PAYMENT.equals(fileRecord.getOffsetTransactionType()) ? fileRecord.getOffsetAmount() : 0.0;
			String key = accountNumber + "_" + effectiveDate;	 
			if (offsetDetails.containsKey(key)) {
				debitAmount = offsetDetails.get(key) + debitAmount;
			}
			offsetDetails.put(key, debitAmount);
		}

		return offsetDetails;
	}

	/**
	 * Checks whether given file name is valid or not
	 * String fileName of uploaded file
	 * @return boolean - returns true if valid otherwise false
	 */
	private boolean _isValidFileName(String fileName) {
		String fileBaseName = null;
		String fileExtension = null;

		if (fileName != null && !fileName.isEmpty() && Pattern.matches("^[\\w,\\s]++(.ach)$", fileName.toLowerCase())) {
			fileBaseName = FilenameUtils.getBaseName(fileName);
			fileExtension = FilenameUtils.getExtension(fileName);

			if (fileBaseName != null && !fileBaseName.isEmpty() && fileExtension != null && !fileExtension.isEmpty()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * sets file format type id to DTO by fetching all file formats supported and matching it with provided file extension
	 * String fileExtension - file extension of uploaded file
	 * @return ACHFileDTO - sets AchFileFormatType_id if success otherwise sets dbpErrorCode to ACH file DTO
	 */
	private ACHFileDTO _setFileTypeId(String fileExtension) {

		ACHFileDTO achFileDTO = new ACHFileDTO();
		Boolean validFileType = false;
		String fileTypeId = null;
		
		try {
			List<ACHFileFormatTypeDTO> fileformatDTOs = achFileBusinessDelegate.getACHFileFormats();
			JSONArray fileformatsJSONArr;
			
			try {
				fileformatsJSONArr = new JSONArray(fileformatDTOs);
			} catch (JSONException e) {
				achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12000);
				return achFileDTO;
			}

			if (fileformatsJSONArr.length() == 0) {
				achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12304);
				return achFileDTO;
			}

			for (int i = 0; i < fileformatsJSONArr.length(); i++) {
				JSONObject obj = fileformatsJSONArr.getJSONObject(i);
				String acceptedFormat = obj.getString(ACHConstants.FILE_EXTENSION);
				if (fileExtension.equals(acceptedFormat)) {
					fileTypeId = obj.get("id") != null ? obj.get("id").toString() : null;
					validFileType = true;
					achFileDTO.setAchFileFormatType_id(fileTypeId);
					return achFileDTO;
				}
			}
			
			if (validFileType != true) {
				achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12305);
				return achFileDTO;
			}
			
        }
        catch(Exception e){
			achFileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12304);
			return achFileDTO;
		}

		return achFileDTO;
	}

	
	/**
	 * Parses input data by validating the file details and returns the ACHFileDTO containing file details 
	 * Map<String, Object> inputParams - request input params
	 * @return ACHFileDTO - if success it sets file details otherwise it sets dbpErrCode to achfileDTO and returns the same
	 */
	private ACHFileDTO _getACHFileDTOByParsingInput(Map<String, Object> inputParams) {

		ACHFileDTO achfileDTO = new ACHFileDTO();
		
		String fileName = inputParams.get("achFileName") != null ? inputParams.get("achFileName").toString() : null;
		if (!_isValidFileName(fileName)) {
			alert.prepareError("Invalid file name").log();
			achfileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12111);
			return achfileDTO;
		}

		achfileDTO = _setFileTypeId(FilenameUtils.getExtension(fileName));
		if (achfileDTO.getDbpErrorCode() != null) {
			alert.prepareError("Invalid file format type").log();
			return achfileDTO;
		}

		String fileContent = inputParams.get("fileContents") != null ? inputParams.get("fileContents").toString() : null;
		if (fileContent == null || fileContent.isEmpty()) {
			alert.prepareError("File is empty.").log();
			achfileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_14005);
			return achfileDTO;
		}

		File uploadedFile = null;
		String fileDir = System.getProperty("java.io.tmpdir");
		String fileBaseName = FilenameUtils.getBaseName(fileName);
		String fileExtension = FilenameUtils.getExtension(fileName);
		String fileTypeId = achfileDTO.getAchFileFormatType_id();
		
		// Decoding File contents of base64 format and writing into file
		try {
			uploadedFile = new File(fileDir, fileBaseName + "." + fileExtension);
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(uploadedFile));
			byte[] achFileContents = Base64.decodeBase64(fileContent);
			fileWriter.write(new String(achFileContents, "UTF-8"));
			fileWriter.close();

			//Parses the file contents to get file transaction details
			achfileDTO = achFileBusinessDelegate.validateACHFile(uploadedFile);
			if (achfileDTO.getDbpErrorCode() != null) {
				alert.prepareError("Error occured while parsing file").log();
				return achfileDTO;
			} 
			
			achfileDTO.setFileSize(String.valueOf(uploadedFile.length()));
			achfileDTO.setAchFileName(fileName);
			achfileDTO.setAchFileFormatType_id(fileTypeId);
			achfileDTO.setNumberOfPrenotes("0");
			achfileDTO.setContents(fileContent);
			
		} 
		catch (Exception e) {
			alert.prepareError("Exception while decoding and writing into file: ", e).log();
			achfileDTO.setDbpErrorCode(ErrorCodeEnum.ERR_12000);
			return achfileDTO;
		}
        finally {
			if (uploadedFile != null) {
				uploadedFile.delete();
			}
		}
		
		return achfileDTO;
	}
	
	/**
	 * Logs ACHfile status in auditactivity
	 * @param request
	 * @param response
	 * @param result
	 * @param transactionStatus
	 * @param userName
	 * @param referenceId
	 * @param requestId
	 */
	private void _logACHFile(DataControllerRequest request,DataControllerResponse response,Result result, TransactionStatusEnum transactionStatus, String userName, String referenceId,String requestId, boolean isSMEUser) {
		
		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE)) return;
		try {

			ApproversBusinessDelegate approversBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApproversBusinessDelegate.class);
			String eventType = Constants.MAKE_TRANSFER;
			String eventSubType = Constants.ACH_FILE_INITIATE;
			String producer = "Transactions/POST(uploadACHFile)";
			String statusID = "";
			int amount;
			amount = Integer.parseInt(result.getParamValueByName("debitAmount"))
					+ Integer.parseInt(result.getParamValueByName("creditAmount"));
			JsonObject customParams = new JsonObject();
			List<Param> params = result.getAllParams();
			for (Param param : params) {
				if (request.containsKeyInRequest(param.getName()) || param.getName().equalsIgnoreCase("contents")) {
					continue;
				} else {
					customParams.addProperty(param.getName(), param.getValue());
				}
			}
			customParams.addProperty("uploadedBy", customParams.get("createdby").getAsString());
			if (transactionStatus.toString().contains("DENIED")) {
				statusID = Constants.SID_EVENT_FAILURE;
				customParams.addProperty(Constants.AMOUNT, amount);
				customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName("achFile_id"));
			} else {
				switch (transactionStatus) {
				case SENT:
					if (referenceId == null || referenceId.isEmpty()) {
						statusID = Constants.SID_EVENT_FAILURE;
					} else {
						statusID = Constants.SID_EVENT_SUCCESS;
						customParams.addProperty(Constants.AMOUNT, amount);
						customParams.addProperty(Constants.REFERENCEID, result.getParamValueByName("achFile_id"));
						if (isSMEUser) {
							customParams.addProperty(Constants.APPROVERS, "Pre-Approved");
							customParams.addProperty("Approved By", "Pre-Approved");
						}
					}
					break;
				case PENDING:
					statusID = Constants.SID_EVENT_SUCCESS;
					customParams.addProperty(Constants.AMOUNT, amount);
					customParams.addProperty(Constants.REFERENCEID, referenceId);
					eventSubType = Constants.PENDING_APPROVAL_ + eventSubType;
					List<String> approvers = approversBusinessDelegate.getRequestApproversList(requestId);
					if (approvers == null) {
						customParams.addProperty(Constants.APPROVERS, "");
					} else {
						customParams.addProperty(Constants.APPROVERS, approvers.toString());
					}
					break;
				default:
					break;
				}
			}
			if (isSMEUser) {
				customParams.addProperty("approvedBy", "N/A");
				customParams.addProperty("rejectedBy", "N/A");
			}
			
			AdminUtil.addAdminUserNameRoleIfAvailable(customParams, request);
			
			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null, userName,
					customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine.").log();
		}
	}
}
