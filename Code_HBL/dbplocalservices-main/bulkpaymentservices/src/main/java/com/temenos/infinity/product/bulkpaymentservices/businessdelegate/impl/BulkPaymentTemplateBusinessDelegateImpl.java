package com.temenos.infinity.product.bulkpaymentservices.businessdelegate.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.CustomerDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api.BulkPaymentTemplateBusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentTemplateDTO;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentTemplatePODTO;

public class BulkPaymentTemplateBusinessDelegateImpl implements BulkPaymentTemplateBusinessDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	CustomerBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);

	@Override
	public BulkPaymentTemplateDTO createTemplate(BulkPaymentTemplateDTO templateDTO, String legalEntityId) {
		BulkPaymentTemplateDTO resultDTO = new BulkPaymentTemplateDTO();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTTEMPLATE_CREATE;

		try {
			List<BulkPaymentTemplatePODTO> bulkpaymentTemplatePODTOs = _getPODTOsForCreation(templateDTO.getPOs(), templateDTO);
			
			JSONObject requestObj = new JSONObject(templateDTO);
			Map<String, Object> requestParams = JSONUtils.parseAsMap(requestObj.toString(), String.class, Object.class);
			requestParams.put("legalEntityId", legalEntityId);
			
			String createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
				responseObj = responseArray.getJSONObject(0);
				resultDTO = JSONUtils.parse(responseObj.toString(), BulkPaymentTemplateDTO.class);
				
				if(bulkpaymentTemplatePODTOs != null) {
					boolean isSuccess = _createTemplatePOs(bulkpaymentTemplatePODTOs,legalEntityId);
					if(!isSuccess) {
						deleteTemplate(resultDTO.getTemplateId());
						return null;
					}
				}
			}

		}
		catch(JSONException e) {
			alert.prepareError("Exception occured while creating a Bulkpayment template", e).log();
			return null;
		}catch (Exception e) {
			alert.prepareError("Exception occured while creating a Bulkpayment template", e).log();
			return null;
		}
		
		return resultDTO;
	}


	private boolean _createTemplatePOs(List<BulkPaymentTemplatePODTO> pos, String legalEntityId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.BULKPAYMENT_TEMPLATE_PO_CREATE_PROC;

		Map<String, Object> requestParams = _getMapForTemplatePOs(pos, legalEntityId);

		try {
			String createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				return true;
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while creating an Bulk payment template", e).log();
			return false;
		}
		return false;
	}

	private Map<String, Object> _getMapForTemplatePOs(List<BulkPaymentTemplatePODTO> pos, String legalEntityId) {

		Map<String,Object> requestParam = new HashMap<String, Object>();

		String poValues = _getPoValue(pos, legalEntityId);
		requestParam.put("_povalues", poValues);

		return requestParam;
	}
	
	private String _getPoValue(List<BulkPaymentTemplatePODTO> pos, String legalEntityId) {

		StringBuilder poValues = new StringBuilder("");
		int poSize = pos.size();

		for(int i = 0; i < poSize; i++) {
			
			BulkPaymentTemplatePODTO po = pos.get(i);
			String swift = po.getSwift() == null ? "" : po.getSwift() ;

			poValues.append("\"" + po.getPaymentOrderId() + "\",");
			poValues.append("\"" + po.getTemplateId() + "\",");
			poValues.append("\"" + po.getConfirmationNumber() + "\",");
			poValues.append("\"" + po.getRecipientName() + "\",");
			poValues.append("\"" + po.getAccountNumber() + "\",");
			poValues.append("\"" + po.getFeatureActionId() + "\",");
			poValues.append("\"" + po.getCompanyId() + "\",");
			poValues.append("\"" + po.getRoleId() + "\",");
			poValues.append("\"" + po.getStatus() + "\",");
			poValues.append("\"" + po.getCreatedby() + "\",");
			poValues.append("\"" + po.getBeneficiaryName() + "\",");
			poValues.append("\"" + po.getPaymentMethod() +"\",");
			poValues.append("\"" + po.getCurrency() +"\",");
			poValues.append("\"" + po.getAmount() + "\",");
			poValues.append("\"" + po.getFeesPaidBy() + "\",");
			poValues.append("\""+po.getPaymentReference() + "\",");
			poValues.append("\"" + swift + "\",");
			poValues.append("\"" + po.getBeneficiaryNickName() + "\",");
			poValues.append("\"" + po.getBeneficiaryAddress() + "\",");
			poValues.append("\"" + po.getAccType() + "\",");
			poValues.append("\"" + po.getBeneficiaryType() + "\",");
			poValues.append("\"" + po.getAddToExistingFlag() + "\",");
			poValues.append("\"" + po.getBeneficiaryIBAN() + "\",");
			poValues.append("\"" + po.getBankName() + "\",");
			poValues.append("\"" + legalEntityId + "\"");

			if( i < poSize -1) {
				poValues.append("|");
			}
		}
		return poValues.toString();
	}


	private List<BulkPaymentTemplatePODTO> _getPODTOsForCreation(String posData, BulkPaymentTemplateDTO templateDTO ) {
		
		List<BulkPaymentTemplatePODTO> poDTOList = null;
		double totalAmount = 0;
		int numberOfPOs = 0;

		try {
			JSONArray pos = new JSONArray(posData);
			numberOfPOs = pos.length();

			for(int i=0; i < numberOfPOs; i++) {
				JSONObject po = pos.getJSONObject(i);
				po.put("templateId", templateDTO.getTemplateId());	
				po.put("roleId", templateDTO.getRoleId());
				po.put("featureActionId", templateDTO.getFeatureActionId());
				po.put("companyId", templateDTO.getCompanyId());
				po.put("status", templateDTO.getStatus());
				po.put("createdby", templateDTO.getCreatedby());
				po.put("confirmationNumber", templateDTO.getTemplateId());
				po.put("paymentOrderId", String.valueOf(HelperMethods.getNumericId()));
				
			}
			poDTOList = JSONUtils.parseAsList(pos.toString(), BulkPaymentTemplatePODTO.class);
			
			for(BulkPaymentTemplatePODTO po : poDTOList) {
				if(!StringUtils.isEmpty(po.getAmount())) {
					totalAmount += Double.parseDouble(po.getAmount());
				}
			}

		} catch (IOException e) {
			alert.prepareError("Exception occured while updating records",e).log();
		}
		templateDTO.setTotalAmount(totalAmount);
		templateDTO.setTotalBeneficiaries(String.valueOf(numberOfPOs));
		return poDTOList;
	}
	
	@Override
	public boolean deleteTemplate(String templateId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTTEMPLATE_DELETE;

		try {
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("templateId", templateId);

			String deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestMap).
					build().getResponse();

			JSONObject responseObj = new JSONObject(deleteResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				return true;
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while deleting an Bulk payment template", e).log();
			return false;
		}
		return false;
	}
	
	@Override
	public List<BulkPaymentTemplateDTO> fetchTemplates(String customerId, List<String> companyIds, String legalEntityId) {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTTEMPLATE_GET;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = null;
		
		if (CollectionUtils.isNotEmpty(companyIds))
			filter = DBPUtilitiesConstants.OPEN_BRACE + 
			"companyId" + DBPUtilitiesConstants.EQUAL + 
			String.join(DBPUtilitiesConstants.OR + "companyId" + DBPUtilitiesConstants.EQUAL, companyIds) 
			+ DBPUtilitiesConstants.CLOSE_BRACE;
		
		if(StringUtils.isNotEmpty(customerId)) {
			if(!filter.isEmpty()) {
				filter = filter + DBPUtilitiesConstants.OR;
			} 
			filter = filter + "createdby" + DBPUtilitiesConstants.EQUAL + customerId;
		}
		
		if (StringUtils.isNotBlank(legalEntityId))
			filter = DBPUtilitiesConstants.OPEN_BRACE + filter + DBPUtilitiesConstants.CLOSE_BRACE
					+ DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		List<BulkPaymentTemplateDTO> bulkPaymentTemplateList = null;
		List<BulkPaymentTemplateDTO> filteredDTOs = null;
		String fetchResponse = null;
		
		try {
			fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
            JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
            bulkPaymentTemplateList = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentTemplateDTO.class);
            
            Set<String> uploadedByList = new HashSet<String>();
            for(BulkPaymentTemplateDTO dto : bulkPaymentTemplateList) {
            	uploadedByList.add(dto.getCreatedby());
            }
            
            List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(uploadedByList);
            filteredDTOs = (new FilterDTO()).merge(bulkPaymentTemplateList, customerDTOs, "createdby=id","");
			
		} catch (JSONException e) {
			alert.prepareError("Unable to fetch Bulk Payment Templates", e).log();		
			return null;			
		} catch (Exception e) {
			alert.prepareError("Unable to fetch the Templates at Infinity", e).log();			
			return null;
		}
		
		return filteredDTOs;
	}
		
	@Override
	public BulkPaymentTemplateDTO fetchTemplateById(String templateId, String legalEntityId) {
		
		List<BulkPaymentTemplateDTO> resultDTO = null;
		List<BulkPaymentTemplateDTO> filteredDTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTTEMPLATE_GET;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		BulkPaymentTemplateDTO templateRecord = null;
		String filter = null;
		
		if(StringUtils.isNotEmpty(templateId)) {
			filter = "templateId" + DBPUtilitiesConstants.EQUAL + templateId;
		}
		
		if(StringUtils.isNotEmpty(legalEntityId)) {
			filter += DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		}
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
	
		String fetchResponse = null;
		
		try {
			fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					build().getResponse();
			
			JSONObject responseObj = new JSONObject(fetchResponse);
			JSONArray records = CommonUtils.getFirstOccuringArray(responseObj);
			resultDTO = JSONUtils.parseAsList(records.toString(), BulkPaymentTemplateDTO.class);
			
			if (CollectionUtils.isNotEmpty(resultDTO)) {
				Set<String> uploadedByList = new HashSet<String>();
				for (BulkPaymentTemplateDTO dto : resultDTO) {
					uploadedByList.add(dto.getCreatedby());
				}

				List<CustomerDTO> customerDTOs = customerBusinessDelegate.getCustomerInfo(uploadedByList);
				filteredDTOs = (new FilterDTO()).merge(resultDTO, customerDTOs, "createdby=id", "");

				templateRecord = filteredDTOs.get(0);
			}
		}
		catch (JSONException e) {
			alert.prepareError("Unable to fetch Bulk Payment Templates", e).log();		
			return null;			
		} catch (Exception e) {
			alert.prepareError("Unable to fetch the Templates at Infinity", e).log();			
			return null;
		}
		return templateRecord;
	}

	@Override
	public String generateCSV(String requestId, String legalEntityId) {
		BulkPaymentTemplateDTO bulkPaymentRequestDTO = new BulkPaymentTemplateDTO();
		List<BulkPaymentTemplatePODTO> bulkPaymentRequestPODTOs = null;
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENT_REQUEST_GET;
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "paymentrequestId" + DBPUtilitiesConstants.EQUAL + requestId;
		
		if(StringUtils.isNotEmpty(legalEntityId)) {
			filter += DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		}
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		String encodedFile = "";
		String response; 
		try {
			response = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			
			JSONObject responseObj = new JSONObject(response);
            JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
			responseObj = responseArray.getJSONObject(0);
            
            bulkPaymentRequestDTO = JSONUtils.parse(responseObj.toString(), BulkPaymentTemplateDTO.class);
            if(bulkPaymentRequestDTO == null)
            	return encodedFile;
            
            bulkPaymentRequestPODTOs = fetchRequestPOById(requestId, legalEntityId);
            if(bulkPaymentRequestPODTOs == null)
            	return encodedFile;
		}
		catch (Exception e) {
			alert.prepareError("Unable to find templates", e).log();			
			return null;
		}
		
		File csvFile = createCSV(bulkPaymentRequestDTO, bulkPaymentRequestPODTOs);
		encodedFile = _encodeFile(csvFile);
		
		alert.prepareError("Logging generated file: +encodedFile:START: "+ encodedFile+" :END:").log();
		if(StringUtils.isEmpty(encodedFile)) {
			alert.prepareError("Unable to encode File").log();			
			return null;
		}
		return encodedFile;
            
	}

	private String _encodeFile(File sample) {
		byte[] fileContent = null;
		try {
			fileContent = Files.readAllBytes(sample.toPath());
		} catch (IOException e) {
			alert.prepareError("Unable to encode the file", e).log();			
			return null;
		}
		String encodedString = Base64.getEncoder().encodeToString(fileContent);
        return encodedString;
	}

	private File createCSV(BulkPaymentTemplateDTO bulkPaymentRequestDTO, List<BulkPaymentTemplatePODTO> poDTOs) {

		String fileDir = System.getProperty("java.io.tmpdir");
		String fileName = FilenameUtils.getBaseName(bulkPaymentRequestDTO.getTemplateName());
		ICsvListWriter csvWriter = null;
		File uploadedFile = new File(fileDir + fileName + ".csv");
		
		String FILE_DATE_FORMAT = Constants.SHORT_DATE_FORMAT;
		SimpleDateFormat dateFormat = new SimpleDateFormat(FILE_DATE_FORMAT); 
				
		try {
			String processingMode = bulkPaymentRequestDTO.getProcessingMode();
			String paymentRef = bulkPaymentRequestDTO.getDescription() != null ? bulkPaymentRequestDTO.getDescription() : "";
			String currency = bulkPaymentRequestDTO.getCurrency() != null ? bulkPaymentRequestDTO.getCurrency() : "";
			CustomerAccountsDTO accountDTO = accountBusinessDelegate.getAccountDetails(bulkPaymentRequestDTO.getCreatedby(), bulkPaymentRequestDTO.getFromAccount());
			String customerId = "";
			if(accountDTO != null) {
				customerId = accountDTO.getCoreCustomerId();
			}
			
			String[] header = new String[127];
			Arrays.fill(header, "");
			header[0] = paymentRef;
			header[1] = processingMode + "-PO";
			header[2] = "CREDIT";
			header[3] = processingMode;
			header[4] = customerId;
			header[5] = bulkPaymentRequestDTO.getFromAccount() != null ? bulkPaymentRequestDTO.getFromAccount() : "";
			header[6] = currency;
			header[7] = processingMode.equals("MULTI") ? "" : bulkPaymentRequestDTO.getFromAccount();
			header[8] = bulkPaymentRequestDTO.getFromAccount() != null ? bulkPaymentRequestDTO.getFromAccount() : "";
			header[10] = bulkPaymentRequestDTO.getPaymentDate() != null ? dateFormat.format(dateFormat.parse(bulkPaymentRequestDTO.getPaymentDate())).replace("-", "") : "";
			header[11] = bulkPaymentRequestDTO.getPaymentDate() != null ? dateFormat.format(dateFormat.parse(bulkPaymentRequestDTO.getPaymentDate())).replace("-", "") : "";
			header[12] = bulkPaymentRequestDTO.getPaymentDate() != null ? dateFormat.format(dateFormat.parse(bulkPaymentRequestDTO.getPaymentDate())).replace("-", "") : "";
			header[13] = paymentRef;
			header[14] = paymentRef;
			header[15] = paymentRef;
			header[106] = String.valueOf(poDTOs.toArray(new BulkPaymentTemplatePODTO[0]).length);
			csvWriter = new CsvListWriter(new FileWriter(uploadedFile), CsvPreference.STANDARD_PREFERENCE);
			csvWriter.write(header);
			String paymentMethod = "";
			
			for (BulkPaymentTemplatePODTO poDTO : poDTOs) {
				String[] fieldValues = new String[127];
				Arrays.fill(fieldValues, "");
				String account = poDTO.getAccountNumber().equalsIgnoreCase("null") ? poDTO.getBeneficiaryIBAN() : String.valueOf(poDTO.getAccountNumber());
				int accountField = poDTO.getAccountNumber().equalsIgnoreCase("null") ? 16 : 15;
				
				if (poDTO.getAccType().equalsIgnoreCase("International")) {
					paymentMethod = "INATIONAL";
				}
				else { //Same bank
					paymentMethod = "DOMESTIC";
				}
				
				if("SINGLE".equalsIgnoreCase(processingMode)) {
					paymentMethod = "FTBMBLK";
				}
				
				fieldValues[0] = paymentMethod;
				fieldValues[1] = customerId;
				fieldValues[7] = "SINGLE".equalsIgnoreCase(processingMode) ? currency : poDTO.getCurrency();
				
				fieldValues[accountField] = account; 
                fieldValues[19] = poDTO.getBeneficiaryName() == null ? "" : poDTO.getBeneficiaryName();
                fieldValues[31] = (poDTO.getSwift() == null || poDTO.getAccType().equalsIgnoreCase("Internal"))? "" : poDTO.getSwift();
                fieldValues[40] = "SINGLE".equalsIgnoreCase(processingMode) ? currency : poDTO.getCurrency();
                fieldValues[41] = String.valueOf(poDTO.getAmount());
                fieldValues[48] = poDTO.getPaymentReference();
                
				String feesPaidBy = poDTO.getFeesPaidBy();
				
				if("Me".equalsIgnoreCase(feesPaidBy)) {
					fieldValues[50] = "OUR";
				}
				else if("Beneficiary".equalsIgnoreCase(feesPaidBy)) {
					fieldValues[50] = "BEN";
				}
				else if("Shared".equalsIgnoreCase(feesPaidBy)) {
					fieldValues[50] = "SHA";
				}
				
				fieldValues[126] = processingMode;
				
				csvWriter.write(fieldValues);
					
			}

		} catch (IOException e) {
			alert.prepareError("Error in writing content to csv", e).log();
			return null;
		} catch (ParseException e) {
			alert.prepareError("Error in parsing content", e).log();
			return null;
		}
		finally {
			try {
				csvWriter.flush();
				csvWriter.close();
			} catch (IOException e) {
				alert.prepareError("Error in writing content to csv", e).log();
				return null;
			}
			
		}
		return uploadedFile;
	}
	
	private List<BulkPaymentTemplatePODTO> fetchRequestPOById(String requestId, String legalEntityId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTREQUESTPOS_GET; 
		
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "paymentrequestId" + DBPUtilitiesConstants.EQUAL + requestId;
		
		if (StringUtils.isNotBlank(legalEntityId))
			filter += DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);
		
		String poDTOs = null;
		List<BulkPaymentTemplatePODTO> response = null;
		try {
			poDTOs = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(poDTOs);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			response = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentTemplatePODTO.class);
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch bulk payment requests by : paymentrequestId", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchRequestPOById: ", e).log();
			return null;
		}

		return response;
	}
	
	@Override
	public List<BulkPaymentTemplatePODTO> fetchTemplatePOsByTemplateId(String templateId, String legalEntityId) {

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTTEMPLATEPOS_GET;

		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String filter = "templateId" + DBPUtilitiesConstants.EQUAL + templateId;
		
		if (StringUtils.isNotBlank(legalEntityId))
			filter += DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + legalEntityId;
		
		requestParameters.put(DBPUtilitiesConstants.FILTER, filter);

		List<BulkPaymentTemplatePODTO> paymentOrders = null;
		String poResponse = null;
		try {
			poResponse = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(requestParameters)
					.build().getResponse();
			JSONObject responseObj = new JSONObject(poResponse);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			paymentOrders = JSONUtils.parseAsList(jsonArray.toString(), BulkPaymentTemplatePODTO.class);
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch payment orders by templateId: ", je).log();
			return null;
		} catch (Exception e) {
			alert.prepareError("Caught exception at fetchTemplatePOsByTemplateId: ", e).log();
			return null;
		}

		return paymentOrders;
	}
	
	@Override
	public BulkPaymentTemplateDTO createTemplateRequest(BulkPaymentTemplateDTO templateDTO, List<BulkPaymentTemplatePODTO> paymentOrders, String legalEntityId) {
		BulkPaymentTemplateDTO resultDTO = new BulkPaymentTemplateDTO();

		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTREQUEST_CREATE;

		try {
			
			JSONObject requestObj = new JSONObject(templateDTO);
			
			Map<String, Object> requestParams = JSONUtils.parseAsMap(requestObj.toString(), String.class, Object.class);
			requestParams.put ("paymentrequestId", String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp()));
			requestParams.put("legalEntityId",legalEntityId);
			String createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				JSONArray responseArray = CommonUtils.getFirstOccuringArray(responseObj);
				responseObj = responseArray.getJSONObject(0);
				resultDTO = JSONUtils.parse(responseObj.toString(), BulkPaymentTemplateDTO.class);
				
				if(paymentOrders != null) {
					boolean isSuccess = _createTemplateRequestPOs(paymentOrders, responseObj.getString("paymentrequestId"), legalEntityId);
					if(!isSuccess) {
						deleteTemplateRequest(responseObj.getString("paymentrequestId"));
						return null;
					}
				}
			}

		}
		catch(JSONException e) {
			alert.prepareError("Exception occured while creating a Bulkpayment template request", e).log();
			return null;
		}catch (Exception e) {
			alert.prepareError("Exception occured while creating a Bulkpayment template request", e).log();
			return null;
		}
		
		return resultDTO;
	}
	
	private Map<String, Object> _getMapForTemplateRequestPOs(List<BulkPaymentTemplatePODTO> paymentOrders, String paymentrequestId, String legalEntityId) {

		Map<String,Object> requestParam = new HashMap<String, Object>();

		String poValues = _getPoRequestValue(paymentOrders, paymentrequestId, legalEntityId);
		requestParam.put("_povalues", poValues);

		return requestParam;
	}
	
	private String _getPoRequestValue(List<BulkPaymentTemplatePODTO> paymentOrders, String paymentrequestId, String legalEntityId) {

		StringBuilder poValues = new StringBuilder("");
		int poSize = paymentOrders.size();

		for(int i = 0; i < poSize; i++) {
			
			BulkPaymentTemplatePODTO po = paymentOrders.get(i);

			poValues.append("\"" + String.valueOf(HelperMethods.getNumericId())  + "\",");
			poValues.append("\"" + paymentrequestId + "\",");
			poValues.append("\"" + po.getPaymentOrderId() + "\",");
			poValues.append("\"" + po.getTemplateId() + "\",");
			poValues.append("\"" + po.getConfirmationNumber() + "\",");
			poValues.append("\"" + po.getRecipientName() + "\",");
			poValues.append("\"" + po.getAccountNumber() + "\",");
			poValues.append("\"" + po.getBankName() + "\",");
			poValues.append("\"" + po.getSwift() + "\",");
			poValues.append("\"" + po.getFeatureActionId() + "\",");
			poValues.append("\"" + po.getCompanyId() + "\",");
			poValues.append("\"" + po.getRoleId() + "\",");
			poValues.append("\"" + po.getStatus() + "\",");
			poValues.append("\"" + po.getCurrency() +"\",");
			poValues.append(po.getAmount() + ",");
			poValues.append("\"" + po.getFeesPaidBy() + "\",");
			poValues.append("\"" + po.getPaymentReference() + "\",");
			poValues.append("\"" + po.getDebitAccountIBAN() + "\",");
			poValues.append("\"" + po.getBeneficiaryIBAN() + "\",");
			poValues.append("\"" + po.getBeneficiaryName() + "\",");
			poValues.append("\"" + po.getBeneficiaryNickName() + "\",");
			poValues.append("\"" + po.getBeneficiaryAddress() + "\",");
			poValues.append("\"" + po.getAccountWithBankBIC() + "\",");
			poValues.append("\"" + po.getCustomer() + "\",");
			poValues.append("\"" + po.getPaymentMethod() +"\",");
			poValues.append("\"" + po.getAccType() + "\",");
			poValues.append("\"" + po.getCreatedby() + "\",");
			poValues.append("\"" + legalEntityId + "\"");

			if( i < poSize -1) {
				poValues.append("|");
			}
		}
		return poValues.toString();
	}
	
	private boolean _createTemplateRequestPOs(List<BulkPaymentTemplatePODTO> paymentOrders, String paymentrequestId, String legalEntityId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.BULKPAYMENT_REQUEST_PO_CREATE_PROC;

		Map<String, Object> requestParams = _getMapForTemplateRequestPOs(paymentOrders, paymentrequestId, legalEntityId);

		try {
			String createResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject responseObj = new JSONObject(createResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				return true;
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while creating an Bulk payment template request", e).log();
			return false;
		}
		return false;
	}
	
	@Override
	public boolean deleteTemplateRequest(String paymentrequestId) {
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_BULKPAYMENTREQUEST_DELETE;

		try {
			Map<String, Object> requestMap = new HashMap<String, Object>();
			requestMap.put("paymentrequestId", paymentrequestId);

			String deleteResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestMap).
					build().getResponse();

			JSONObject responseObj = new JSONObject(deleteResponse);
			if(responseObj.getInt(Constants.OPSTATUS) == 0 && responseObj.getInt(Constants.HTTP_STATUS) == 0) {
				return true;
			}
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while deleting an Bulk payment template request", e).log();
			return false;
		}
		return false;
	}
	
	@Override
	public BulkPaymentFileDTO uploadTemplateRequestAsFile(BulkPaymentTemplateDTO template, String content, DataControllerRequest dcr) {
		BulkPaymentFileDTO bulkPaymentDTO = new BulkPaymentFileDTO();
				
		String serviceName = ServiceId.DBPBULKPAYMENTSERVICES;
		String operationName = OperationName.UPLOADBULKPAYMENTFILE ;
		String codeFlow = "templateFlow";
		Map<String, Object> requestParameters = new HashMap<>();
		String fileName = "T"+template.getFromAccount()+String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp())+".csv";
		requestParameters.put("content",content);
		requestParameters.put("fileName",fileName);
		requestParameters.put("description",template.getDescription());
		requestParameters.put("batchMode",template.getProcessingMode());
		requestParameters.put("uploadType",codeFlow);
	
		String uploadResponse = null;
		try {
		
			uploadResponse =  DBPServiceExecutorBuilder.builder().
				withServiceId(serviceName).
				withObjectId(null).
				withOperationId(operationName).
				withRequestParameters(requestParameters).
				withRequestHeaders(dcr.getHeaderMap()).
				withDataControllerRequest(dcr).
				build().getResponse();
			
			JSONObject jsonRsponse = new JSONObject(uploadResponse);
			bulkPaymentDTO = JSONUtils.parse(jsonRsponse.toString(), BulkPaymentFileDTO.class);
			
		} catch (JSONException e) {
			alert.prepareError("Unable to Upload Bulk Payment File", e).log();		
			return null;			
		} catch (Exception e) {
			alert.prepareError("Unable to store the file at Infinity", e).log();			
			return null;
		}
		
		return bulkPaymentDTO;
	}

}