package com.temenos.dbx.product.achservices.backenddelegate.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.achservices.backenddelegate.api.ACHFileBackendDelegate;
import com.temenos.dbx.product.achservices.dto.ACHFileDTO;
import com.temenos.dbx.product.approvalservices.dto.ApprovalRequestDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class ACHFileBackendDelegateImpl implements ACHFileBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public ACHFileDTO createTransactionWithoutApproval(ACHFileDTO achfileDTO, DataControllerRequest dcr) {

		ACHFileDTO fileResponseDTO;

		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.ACH_FILE_VENDOR_SERVICE;

		Map<String, Object> requestParameters = new HashMap<>();

		try {
			achfileDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(achfileDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}

		String fileResponse = null;
		try {
			fileResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
		} catch (JSONException e) {
			alert.prepareError("Unable to upload ACH File at backend: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createTransactionWithoutApproval method: " , e).log();
			return null;
		}

		try {
			fileResponseDTO = JSONUtils.parse(fileResponse, ACHFileDTO.class);
			if (StringUtils.isNotEmpty(fileResponseDTO.getAchFile_id())) {
				fileResponseDTO.setReferenceID(fileResponseDTO.getAchFile_id());
			}
		} catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;
		} catch (NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " , e).log();
			return null;
		}

		return fileResponseDTO;

	}

	@Override
	public List<ACHFileDTO> fetchFileStatus(List<String> confirmationNumbers, DataControllerRequest dcr) {
		String serviceName = ServiceId.ACH_ORCH_LOB;
		String operationName = OperationName.FETCH_ACH_ORCH_FILE_STATUS;

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("confirmationNumber", String.join(",", confirmationNumbers));
		inputParams.put("loop_count", String.valueOf(confirmationNumbers.size()));
		List<ACHFileDTO> output = new ArrayList<ACHFileDTO>();

		try {
			String createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			JSONArray responseArray = CommonUtils.getFirstOccuringArray(response);
			output = JSONUtils.parseAsList(responseArray.toString(), ACHFileDTO.class);

		} catch (Exception e) {
			alert.prepareError("Caught exception at " + OperationName.FETCH_ACH_ORCH_FILE_STATUS + " entry: " , e).log();
		}
		return output;
	}
	
	@Override
	public ACHFileDTO createPendingTransaction(ACHFileDTO fileResponseDTO, DataControllerRequest dcr) {

		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.ACH_FILE_VENDOR_SERVICE;

		Map<String, Object> requestParameters = new HashMap<>();

		try {
			fileResponseDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
			requestParameters = JSONUtils.parseAsMap(new JSONObject(fileResponseDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " , e).log();
			return null;
		}

		String fileResponse = null;
		try {
			fileResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(requestParameters)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
		} catch (JSONException e) {
			alert.prepareError("Unable to upload ACH File at backend: " , e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at createPendingTransaction method: " , e).log();
			return null;
		}

		try {
			fileResponseDTO = JSONUtils.parse(fileResponse, ACHFileDTO.class);
			if (StringUtils.isNotEmpty(fileResponseDTO.getReferenceID())) {
				fileResponseDTO.setAchFile_id(fileResponseDTO.getReferenceID());
			}
		} catch (IOException e) {
			alert.prepareError("Caught exception while parsing list: " , e).log();
			return null;
		} catch (NullPointerException e) {
			alert.prepareError("NullPointer Exception for records: " , e).log();
			return null;
		}

		return fileResponseDTO;

	}
	
	@Override
	public ACHFileDTO approveTransaction(String referenceId, DataControllerRequest request) {

		ACHFileDTO achFileDTO = new ACHFileDTO();

		achFileDTO.setAchFile_id(referenceId);
		achFileDTO.setStatus(DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);

		achFileDTO = moveTheTransactionFromPendingToLive(achFileDTO, request);

		return achFileDTO;
	}

	@Override
	public ACHFileDTO rejectTransaction(String referenceId, String transactionType, DataControllerRequest request) {
		return deleteTransaction(referenceId, transactionType, request);
	}

	@Override
	public ACHFileDTO withdrawTransaction(String referenceId, String transactionType, DataControllerRequest dataControllerRequest) {
		return deleteTransaction(referenceId, transactionType, dataControllerRequest);
	}
	
	@Override
	public ACHFileDTO fetchTransactionById(String referenceId, DataControllerRequest request) {
		
		List<ACHFileDTO> transactionDTO = null;
		ACHFileDTO achfile = null;

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_GET;
		
		Map<String, Object> requestParams = new HashMap<String, Object>();
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + referenceId;
		requestParams.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();
			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray trJsonArray = jsonRsponse.getJSONArray(Constants.ACH_FILE);
			transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(), ACHFileDTO.class);
			if(transactionDTO != null && transactionDTO.size() != 0) {
				achfile = transactionDTO.get(0);
				achfile.setAchFile_id(achfile.getConfirmationNumber());
				return achfile;
			}
		}
		
		catch(JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching the ach file",jsonExp).log();
			return null;
		}
		catch(Exception exp) {
			alert.prepareError("Excpetion occured while fetching the ach file",exp).log();
			return null;
		}
		
		return null;
	}
	
	private ACHFileDTO moveTheTransactionFromPendingToLive(ACHFileDTO input, DataControllerRequest request) {
		/*
		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.ACH_FILE_BACKEND_UPDATE_STATUS;

		String updateStatus = null;
		ACHFileDTO achFileDTO = null;

		Map<String, Object> requestParameters;
		try {
			requestParameters = JSONUtils.parseAsMap(new JSONObject(input).toString(),
					String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return null;
		}
		try {
			updateStatus = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.withRequestHeaders(request.getHeaderMap())
					.withDataControllerRequest(request)
					.build().getResponse();
			JSONObject editResponse = new JSONObject(updateStatus);

			if (editResponse != null)
				achFileDTO = JSONUtils.parse(editResponse.toString(), ACHFileDTO.class);
		} catch (JSONException e) {
			alert.prepareError("Failed to update status of ach file: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at update status of ach file: " + e).log();
			return null;
		}
		
		return achFileDTO;
		*/
		input.setReferenceID(input.getAchFile_id());
		return input;
	}
	
	@Override
	public ACHFileDTO validateTransaction(ACHFileDTO input, DataControllerRequest request) {
		
		return input;
	}
	
	@Override
	public ACHFileDTO deleteTransaction(String fileId, String transactionType,
			DataControllerRequest dataControllerRequest) {

		/*
		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = OperationName.ACH_FILE_BACKEND_DELETE;
		
		String deleteResponse = null;
		ACHFileDTO achFileDTO = new ACHFileDTO();
		achFileDTO.setDbpErrMsg(ErrorConstants.TRANSACTION_DELETE_FAILED_AT_BACKEND);
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("transactionId", transactionId);
		requestParameters.put("transactionType", transactionType);
		
		try {
			
			deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(dataControllerRequest.getHeaderMap()).
					withDataControllerRequest(dataControllerRequest).
					build().getResponse();
			
			JSONObject deleteResponseObj = new JSONObject(deleteResponse);
			if(deleteResponseObj.has(Constants.OPSTATUS) && deleteResponseObj.getInt(Constants.OPSTATUS) ==0 && (deleteResponseObj.has("fileId") || deleteResponseObj.has("referenceId"))) {
				if(deleteResponseObj.has("fileId") && !"".equals(deleteResponseObj.getString("fileId"))) {
					deleteResponseObj.put("referenceId", deleteResponseObj.getString("fileId"));
				}
			}
			if(deleteResponseObj != null) 
				achFileDTO = JSONUtils.parse(deleteResponseObj.toString(), ACHTransactionDTO.class);
		}
		catch (JSONException e) {
			alert.prepareError("Failed to delete ach file: " + e).log();
			return achTransactionDTO;
		}
		catch (Exception e) {
			alert.prepareError("Caught exception at delete ach file: " + e).log();
			return achFileDTO;
		}
		
		return achFileDTO;
		*/
		return new ACHFileDTO();
	}
	
	@Override
	public ACHFileDTO editTransaction(ACHFileDTO fileDTO, DataControllerRequest dcr) {
		/*
		String serviceName = ServiceId.ACH_LINE_OF_BUSINESS_SERVICE;
		String operationName = null;

		Map<String, Object> inputParams = null;

		try {
			inputParams = JSONUtils.parseAsMap(new JSONObject(fileDTO).toString(), String.class, Object.class);
		} catch (IOException e) {
			alert.prepareError("Failed to extract inputParams from transactionDTO", e).log();
			return null;
		}

		String createResponse = null;

		try {
			createResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(inputParams)
					.withRequestHeaders(dcr.getHeaderMap()).withDataControllerRequest(dcr).build().getResponse();
			JSONObject response = new JSONObject(createResponse);
			fileDTO = JSONUtils.parse(response.toString(), ACHFileDTO.class);
			return fileDTO;
		} catch (JSONException e) {
			alert.prepareError("Failed to create entry into vendor service: " + e).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at getAchVendorServiceResponse entry: " + e).log();
			return null;
		}
		*/
		return fileDTO;
	}
	
	@Override
	public List<ApprovalRequestDTO> fetchBackendTransactionsForApproval(Set<String> achFileIds, DataControllerRequest dcr) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_ACHFILE_GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		List<ApprovalRequestDTO> achFiles = new ArrayList<ApprovalRequestDTO>();
		
		String filter = "confirmationNumber" + DBPUtilitiesConstants.EQUAL + 
				String.join(DBPUtilitiesConstants.OR + "confirmationNumber" + DBPUtilitiesConstants.EQUAL, achFileIds);
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		try {
			String achFileResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			
			JSONObject responseObj = new JSONObject(achFileResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			achFiles = JSONUtils.parseAsList(jsonArray.toString(), ApprovalRequestDTO.class);
			
			achFiles.forEach((achfile) ->{
				achfile.setTransactionId(achfile.getConfirmationNumber());
			});
			
		} 
		catch (JSONException je) {
			alert.prepareError("Failed to fetch ACH files : ", je).log();
		} 
		catch (Exception e) {
			alert.prepareError("Caught exception while fetching ACH files: ", e).log();
		}

		return achFiles;
	}
	

}
