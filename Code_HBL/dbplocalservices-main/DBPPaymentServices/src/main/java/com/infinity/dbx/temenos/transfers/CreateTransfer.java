package com.infinity.dbx.temenos.transfers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBaseService;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CreateTransfer extends TemenosBaseService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		try {

			Result localResult = (Result) super.invoke(methodId, inputArray, request, response);
			HashMap<String, Object> params = (HashMap<String, Object>) inputArray[1];
			String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
			if (StringUtils.isNotBlank(transactionType)
					&& !Constants.TRANSCTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)
					&& !Constants.TRANSCTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
				CommonUtils.setOpStatusOk(result);
				result.addParam(new Param(Constants.PARAM_HTTP_STATUS_CODE, Constants.PARAM_HTTP_STATUS_OK,
						Constants.PARAM_DATATYPE_STRING));
				return result;
			}
			if (params == null) {
				CommonUtils.setErrMsg(localResult, "No input parameters provided");
				CommonUtils.setOpStatusError(localResult);
				return localResult;
			}
			String frequency = CommonUtils.getParamValue(params, Constants.PARAM_FREQUENCY_TYPE);
			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			String serviceName = TransferConstants.T24_SERVICE_NAME_TRANSFERS;
			String operationName = null;
			TransferUtils.setPaymentDetails(params, request);
			if (Constants.FREQUENCY_ONCE.equalsIgnoreCase(frequency)) {
				operationName = TransferConstants.ONE_TIME_TRANSFER_OPERATION_NAME;
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			} else {
				operationName = TransferConstants.STANDING_ORDER_OPERATION_NAME;
				result = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName, operationName,
						true);
			}
			addPaymentStatus(request, params, result);
		} catch (Exception e) {
			Result errorResult = new Result();
			alert.prepareError("Exception Occured while creating Transfer:" + e).log();
			CommonUtils.setOpStatusError(result);
			CommonUtils.setErrMsg(errorResult, e.getMessage());
			return errorResult;
		}
		return result;
	}
	
	public void addPaymentStatus(DataControllerRequest request,HashMap<String, Object> params, Result result)
	{
		boolean isCompleted = false;
		alert.prepareError("transfer service result "+ResultToJSON.convert(result)).log();
		if (result != null) {
			String transactionType = CommonUtils.getParamValue(params, Constants.PARAM_TRANSACTION_TYPE);
			String serviceName = CommonUtils.getParamValue(params, TransferConstants.SERVICE_NAME);
			String IBAN = CommonUtils.getParamValue(params, TemenosConstants.PARAM_IBAN);
			alert.prepareError("fields -"+transactionType+" service name "+serviceName+" IBAN "+IBAN).log();
			if (TransferConstants.TRANSCTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType) && (StringUtils.isNotBlank(serviceName)
					&& TransferConstants.DOMESTIC_TRANSFER_SERVICE_ID.equalsIgnoreCase(serviceName))
					|| StringUtils.isNotBlank(IBAN))
			{
				String intgrationServiceName = TransferConstants.T24_SERVICE_NAME_TRANSACTION;
				String operationName = TransferConstants.OP_GET_TRANSFER_STATUS;
				try {
					String id = result.getParamValueByName(TransferConstants.PARAM_REFERENCE_ID);
					String transactionStatus = result.getParamValueByName(TransferConstants.PARAM_STATUS);
					alert.prepareError("transaction id "+ id).log();
					if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(transactionStatus) && TransferConstants.STATUS_SUCCESS.equalsIgnoreCase(transactionStatus)) {
						request.addRequestParam_(TransferConstants.PARAM_TRANSACTION_ID, result.getParamValueByName(TransferConstants.PARAM_REFERENCE_ID));
						Thread.currentThread().sleep(5000);
						Result integrationServiceResult = CommonUtils.callIntegrationService(request, null, null, intgrationServiceName, operationName, true);
						alert.prepareError("status service result "+ResultToJSON.convert(integrationServiceResult)).log();
						if (integrationServiceResult != null) {
							Dataset dataset = integrationServiceResult.getDatasetById(TransferConstants.BODY);
							if (dataset != null) {
								List<Record> allRecords = dataset.getAllRecords();
								if (allRecords != null && allRecords.size()  > 0) {
									Record record = allRecords.get(0);
									String currentStatus = record.getParamValueByName(TransferConstants.PARAM_CURRENT_STATUS);
									if (StringUtils.isNotBlank(currentStatus) && TransferConstants.STATUS_COMPLETE.equalsIgnoreCase(currentStatus)) {
										isCompleted = true;
									}
								}
							}
						}
						result.addParam(TransferConstants.IS_COMPLETED, String.valueOf(isCompleted));
					}
				} catch (Exception e) {
					alert.prepareError("error occured while adding transaction status ",e).log();
				}
			}
			try {
				String id = result.getParamValueByName(TransferConstants.PARAM_REFERENCE_ID);
				if(params.containsKey("uploadedattachments") && params.containsKey("validate")) {
					String isValidate = params.get("validate").toString();
					if ((StringUtils.isBlank(isValidate)
							|| (StringUtils.isNotBlank(isValidate) && isValidate.equalsIgnoreCase("false")))
							&& StringUtils.isNotBlank(id)) {
						String userId = params.get("userId").toString();
						String uploadedAttachments = CommonUtils.getParamValue(params,
								TransferConstants.PARAM_UPLOADED_ATTACHMENTS);
						if (StringUtils.isNotBlank(uploadedAttachments)) {
							String uploadedAttachmentsArray[] = uploadedAttachments.split(",");
							if (uploadedAttachmentsArray.length > 0)
								parseUploadingAttachments(request, result, id, userId, uploadedAttachmentsArray);
						}
					}
				}
			} catch (Exception e) {
				alert.prepareError("Error occured while uploading attachments: ", e).log();
			}
		}
	}
	private void parseUploadingAttachments(DataControllerRequest request, Result transactionResult, String referenceId,
			String userId, String[] uploadedAttachmentsArray) {
		String uploadedFileName = null;
		String fileExtension = null;
		String fileContents = null;
		ArrayList<String> failedUploads = new ArrayList<>();
		ArrayList<String> successfulUploads = new ArrayList<>();
		try {
			for (int i = 0; i < uploadedAttachmentsArray.length; i++) {
				String attachmentDetails[] = uploadedAttachmentsArray[i].split("-");
				if (attachmentDetails.length == 3) {
					uploadedFileName = attachmentDetails[TransferConstants.FILENAME_INDEX];
					fileExtension = attachmentDetails[TransferConstants.FILETYPE_INDEX];
					fileContents = attachmentDetails[TransferConstants.FILECONTENTS_INDEX];
					boolean status = uploadAttachments(request, referenceId, userId, uploadedFileName, fileExtension,
							fileContents);
					if(status) {
						successfulUploads.add(uploadedFileName);
					} else {
						failedUploads.add(uploadedFileName);
					}
				} else {
					alert.prepareError("File input is incorrect for performing upload operation").log();
					failedUploads.add(attachmentDetails[TransferConstants.FILENAME_INDEX]);
				}
			}
		} catch (Exception e) {
			alert.prepareError("Error occured while uploading attachments ", e).log();
		} finally {
			transactionResult.addParam("failedUploads", StringUtils.join(failedUploads, ","));
			transactionResult.addParam("successfulUploads", StringUtils.join(successfulUploads, ","));
		}
	}

	private boolean uploadAttachments(DataControllerRequest request, String referenceId,
			String userId, String uploadedFileName, String fileExtension, String fileContents) {
		Map<String, String> dataMap = new HashMap<>();
		Result result = new Result();
		try {
			if (CommonUtils.isDMSIntegrationEnabled()) {
				request.addRequestParam_("documentName", uploadedFileName);
				request.addRequestParam_("category", "payment");
				request.addRequestParam_("content", fileContents);
				request.addRequestParam_("version", "1.0");
				request.addRequestParam_("referenceId", referenceId);
				request.addRequestParam_("userId", userId);
				result = CommonUtils.callObjectService(request, null, null, TransferConstants.DOCUMENT_INTEGRATION_OBJECT_ID, TransferConstants.DOCUMENT_INTEGRATION_SERVICE_ID , TransferConstants.DOCUMENT_INTEGRATION_UPLOAD_OPER_ID, true);
			} else {
				String id = generateUniqueID(TransferConstants.UNIQUE_ID_LENGTH);
				request.addRequestParam_("paymentFileID", id);
				request.addRequestParam_("paymentFileName", uploadedFileName);
				request.addRequestParam_("paymentFileType", fileExtension);
				request.addRequestParam_("paymentFileContents", fileContents);
				request.addRequestParam_("transactionId", referenceId);
				request.addRequestParam_("userId", userId);
				result = CommonUtils.callIntegrationService(request, null, null, TransferConstants.RBLOCALSERVICESDB_SERVICE_ID, TransferConstants.PAYMENTFILESCREATE_OPER_ID, true);
			}
			if(result.getParamValueByName("opstatus").equals("0")) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("Error occured while uploading attachments ", e).log();
			return false;
		}
	}
	
	public static String generateUniqueID(int length) {
		try {
			String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	        String NUMBER = "0123456789";
	        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	       
	        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");	        		
	        
	        if (length < 1) throw new IllegalArgumentException();
	        StringBuilder sb = new StringBuilder(length);
	        
	        for (int i = 0; i < length; i++) {
	            // 0-62 (exclusive), random returns 0-61
	            int rndCharAt = secureRandomGenerator.nextInt(DATA_FOR_RANDOM_STRING.length());
	            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

	            sb.append(rndChar);
	        }
	        return sb.toString();	
		} catch (Exception e) {
			return null;
		}
	}
}
