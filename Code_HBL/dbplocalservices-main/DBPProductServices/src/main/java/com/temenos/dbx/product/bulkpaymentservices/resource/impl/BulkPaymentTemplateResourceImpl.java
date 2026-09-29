package com.temenos.dbx.product.bulkpaymentservices.resource.impl;

import java.io.IOException;
import java.util.Arrays;
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

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.objectserviceutils.EventsDispatcher;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.constants.EventSubType;
import com.temenos.dbx.constants.EventType;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentFileBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.businessdelegate.api.BulkPaymentTemplateBusinessDelegate;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentTemplateDTO;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentTemplatePODTO;
import com.temenos.dbx.product.bulkpaymentservices.resource.api.BulkPaymentTemplateResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ContractBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.AuditLog;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

	
public class BulkPaymentTemplateResourceImpl implements BulkPaymentTemplateResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	BulkPaymentTemplateBusinessDelegate bulkPaymentTemplateBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentTemplateBusinessDelegate.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	BulkPaymentFileBusinessDelegate bulkPaymentFileBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(BulkPaymentFileBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);
	
	@Override
	public Result createBulkPaymentTemplate(String methodID, Object[] inputArray, DataControllerRequest dcRequest, 
									DataControllerResponse response) {
		
		Result result = new Result();
		BulkPaymentTemplateDTO responseDTO = null;
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
			BulkPaymentTemplateDTO bulkPaymentTemplateDTO = _validateRequestParamsForCreateTemplate(requestMap, customer);

			if (bulkPaymentTemplateDTO == null) {
				alert.prepareError("Unable to store the template info at Backend").log();			
				return ErrorCodeEnum.ERR_28006.setErrorCode(new Result());
			}
			
			if(bulkPaymentTemplateDTO.getDbpErrorCode() != null) {
				return bulkPaymentTemplateDTO.getDbpErrorCode().setErrorCode(new Result());
			}
			
			if (! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (bulkPaymentTemplateDTO.getCreatedby(), bulkPaymentTemplateDTO.getFeatureActionId(), bulkPaymentTemplateDTO.getFromAccount(), CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_28005.setErrorCode(new Result());
			}

			responseDTO = bulkPaymentTemplateBusinessDelegate.createTemplate(bulkPaymentTemplateDTO);
			
			JSONObject responseObj = new JSONObject(responseDTO);
			result = JSONToResult.convert(responseObj.toString());
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while creating BulkPayment template", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		try{
			_logBulkPaymentTemplate(dcRequest, response, result, EventSubType.BULK_PAYMENT_TEMPLATE_CREATE, responseDTO);
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while logging details", e).log();
		}
		return result;
	}

	private BulkPaymentTemplateDTO _validateRequestParamsForCreateTemplate(HashMap<String, Object> requestMap, Map<String, Object> customer) {
		BulkPaymentTemplateDTO bulkPaymentTemplateDTO = null;
		
		try {
			JSONObject requestObj = new JSONObject(requestMap);
			bulkPaymentTemplateDTO = JSONUtils.parse(requestObj.toString(), BulkPaymentTemplateDTO.class);
		} catch (IOException e) {
			alert.prepareError("Unable to parse template").log();
			return bulkPaymentTemplateDTO;
		}
		
		if (StringUtils.isEmpty(bulkPaymentTemplateDTO.getTemplateName())) {
			alert.prepareError("Invalid Template Name").log();
			bulkPaymentTemplateDTO.setDbpErrorCode(ErrorCodeEnum.ERR_28001);
			return bulkPaymentTemplateDTO; 
		}
		
		if (StringUtils.isEmpty(bulkPaymentTemplateDTO.getCurrency())) {
			alert.prepareError("Invalid Currency").log();
			bulkPaymentTemplateDTO.setDbpErrorCode(ErrorCodeEnum.ERR_28002);
			return bulkPaymentTemplateDTO; 
		}
		
		if (StringUtils.isEmpty(bulkPaymentTemplateDTO.getFromAccount())) {
			bulkPaymentTemplateDTO.setDbpErrorCode(ErrorCodeEnum.ERR_28003);
			return bulkPaymentTemplateDTO; 
		}
		
		if (StringUtils.isEmpty(bulkPaymentTemplateDTO.getProcessingMode())) {
			alert.prepareError("Invalid Processing Mode").log();
			bulkPaymentTemplateDTO.setDbpErrorCode(ErrorCodeEnum.ERR_28004);
			return bulkPaymentTemplateDTO; 
		}
		
		if (StringUtils.isEmpty(bulkPaymentTemplateDTO.getPaymentDate())) {
			alert.prepareError("Invalid paymentDate").log();
			bulkPaymentTemplateDTO.setDbpErrorCode(ErrorCodeEnum.ERR_26002);
			return bulkPaymentTemplateDTO; 
		}

		String createdby = CustomerSession.getCustomerId(customer);
		
		String featureActionId = "";
		
		if(bulkPaymentTemplateDTO.getProcessingMode().equals(Constants.SINGLE)) {
			featureActionId = "BULK_PAYMENT_TEMPLATE_SINGLE_CREATE";
		}
		else if(bulkPaymentTemplateDTO.getProcessingMode().equals(Constants.MULTI)) {
			featureActionId = "BULK_PAYMENT_TEMPLATE_MULTIPLE_CREATE";
		}
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, bulkPaymentTemplateDTO.getFromAccount());
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();

		bulkPaymentTemplateDTO.setCompanyId(companyId);
		bulkPaymentTemplateDTO.setCreatedby(createdby);
		bulkPaymentTemplateDTO.setRoleId(customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
		bulkPaymentTemplateDTO.setStatus(TransactionStatusEnum.NEW.getStatus());
		bulkPaymentTemplateDTO.setFeatureActionId(featureActionId);
		
		bulkPaymentTemplateDTO.setPOs(requestMap.get("POs") == null ? null : requestMap.get("POs").toString());
		String id = String.valueOf(HelperMethods.getIdUsingCurrentTimeStamp());
		bulkPaymentTemplateDTO.setTemplateId(id);
		
		return bulkPaymentTemplateDTO;
	}
	
	@Override
	public Result fetchBulkPaymentTemplates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result;
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_TEMPLATE_VIEW);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);
		if(features == null) {
     		return ErrorCodeEnum.ERR_28008.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		FilterDTO filterDTO;
		try {
			filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_28009.setErrorCode(new Result());
		}
		
		ContractBusinessDelegate contractDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ContractBusinessDelegate.class);
		List<String> contracts = contractDelegate.fetchContractCustomers(customerId);
		contracts.add(CustomerSession.getCompanyId(customer));
		
		List<BulkPaymentTemplateDTO> templates = bulkPaymentTemplateBusinessDelegate.fetchTemplates(customerId, contracts);
		
		if(templates == null) {
            alert.prepareError("Error occurred while fetching bulk payment templates").log();
            return ErrorCodeEnum.ERR_28009.setErrorCode(new Result());
        }
		if(templates.size() > 0 && StringUtils.isNotBlank(templates.get(0).getDbpErrMsg())) {
        	alert.prepareError("Error occurred while fetching bulk payment templates").log();
        	return ErrorCodeEnum.ERR_00000.setErrorCode(new Result(), templates.get(0).getDbpErrMsg());
        }
		
		
		boolean isCombinedUser = CustomerSession.IsCombinedUser(customer);
		Set<String> unAuthorizedAccounts = authorizationChecksBusinessDelegate.fetchUnAuthorizedAccounts(
				customerId, FeatureAction.BULK_PAYMENT_TEMPLATE_VIEW, isCombinedUser);
		filterDTO.set_removeByParam("fromAccount");
		filterDTO.set_removeByValue(unAuthorizedAccounts);
		List<BulkPaymentTemplateDTO> filteredTemplates = filterDTO.filter(templates);
		
		try {
            JSONArray uploadedFiles = new JSONArray(filteredTemplates);
            JSONObject resultObject = new JSONObject();
            resultObject.put(Constants.BULKPAYMENTTEMPLATES,uploadedFiles);
            result = JSONToResult.convert(resultObject.toString());
        }
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_28009.setErrorCode(new Result());
        }

		return result;
	}

	public Result deleteBulkPaymentTemplate(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
		String fromAccount = null;
		
		String templateId = requestMap.get("templateId") != null ? requestMap.get("templateId").toString() : null;
		
		if (StringUtils.isEmpty(templateId)) {
			return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
		}
		
		String requiredFeatureActionId = FeatureAction.BULK_PAYMENT_TEMPLATE_DELETE;
		BulkPaymentTemplateDTO filteredTemplate = bulkPaymentTemplateBusinessDelegate.fetchTemplateById(templateId);
		fromAccount = filteredTemplate.getFromAccount();
		
		if (!(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (userId, requiredFeatureActionId, fromAccount, CustomerSession.IsCombinedUser(customer)))) {
			return ErrorCodeEnum.ERR_28012.setErrorCode(new Result());
		}

		boolean isTemplateDeleted = bulkPaymentTemplateBusinessDelegate.deleteTemplate(templateId);
		
		if(isTemplateDeleted) {
			JSONObject resultObject = new JSONObject();
            resultObject.put(Constants.TEMPLATE_ID, templateId);
            resultObject.put(Constants.TEMPLATE_NAME, filteredTemplate.getTemplateName());
            result = JSONToResult.convert(resultObject.toString());
		}
		else {
			return ErrorCodeEnum.ERR_28013.setErrorCode(new Result());
		}

		return result;
	}
	

	/**
	 * Logs BulkPaymentTemplate status in auditactivity
	 * @param request
	 * @param response
	 * @param result
	 * @param eventSubType
	 * @param templateDTO
	 */
	private void _logBulkPaymentTemplate(DataControllerRequest request, DataControllerResponse response, Result result, String eventSubType, BulkPaymentTemplateDTO templateDTO) {
		
		String enableEvents = EnvironmentConfigurationsHandler.getValue(Constants.ENABLE_EVENTS, request);
		if (enableEvents == null || enableEvents.equalsIgnoreCase(Constants.FALSE)) return;
		try {

			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			AuditLog auditLog = new AuditLog();
			String userName = CustomerSession.getCustomerName(customer);
			String eventType = EventType.BULK_PAYMENT_TEMPLATE;
			String producer = "";
			String statusID = "";
			
			double amount = 0.0;
			if(templateDTO.getTotalAmount() != null)
				amount = templateDTO.getTotalAmount();
			
			String templateId = templateDTO.getTemplateId();
			JsonObject customParams = new JsonObject();
			List<Param> params = result.getAllParams();
			
			for(Param param : params) {
				customParams.addProperty(param.getName(), param.getValue());
			}
			
			if(eventSubType == EventSubType.BULK_PAYMENT_TEMPLATE_CREATE) {
				producer = "BulkPaymentObjects/createBulkPaymentTemplate";
			}
			else if(eventSubType == EventSubType.BULK_PAYMENT_TEMPLATE_EDIT) {
				producer = "BulkPaymentObjects/editBulkPaymentTemplate";
			}
			customParams.addProperty("createdby", userName);
			
			if (StringUtils.isEmpty(templateId)) {
				statusID = Constants.SID_EVENT_FAILURE;
			} else {
				statusID = Constants.SID_EVENT_SUCCESS;
				customParams.addProperty(Constants.AMOUNT, amount);
				customParams.addProperty("fromAccountNumber", templateDTO.getFromAccount());
				customParams = auditLog.buildCustomParamsForAlertEngine(templateDTO.getFromAccount(), "", customParams);
				customParams.addProperty(Constants.REFERENCEID, templateId);
			}
			
			EventsDispatcher.dispatch(request, response, eventType, eventSubType, producer, statusID, null, null,
					customParams);
		} catch (Exception e) {
			alert.prepareError("Error while pushing to Audit Engine.", e).log();
		}
	}

	@SuppressWarnings("null")
	@Override
	public Result editBulkPaymentTemplate(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
 		Result result = new Result();
        
        @SuppressWarnings("unchecked")
        HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String userId = CustomerSession.getCustomerId(customer);
        String templateId = requestMap.get("templateId") != null ? requestMap.get("templateId").toString() : null;
        String fromAccount = requestMap.get("fromAccount") != null ? requestMap.get("fromAccount").toString() : null;
        BulkPaymentTemplateDTO updatedTemplateDTO = null;
        
        if (StringUtils.isEmpty(templateId)) {
			return ErrorCodeEnum.ERR_12037.setErrorCode(new Result());
		}
        
        BulkPaymentTemplateDTO bulkPaymentTemplateDTO = _validateRequestParamsForCreateTemplate(requestMap, customer); // Validate the request params before calling backend delegate
        
        if (bulkPaymentTemplateDTO == null) {
            alert.prepareError("Unable to store the template info at Backend").log();            
            return ErrorCodeEnum.ERR_28006.setErrorCode(new Result());
        }
        
        if(bulkPaymentTemplateDTO.getDbpErrorCode() != null) {
            return bulkPaymentTemplateDTO.getDbpErrorCode().setErrorCode(new Result());
        }
        
        String requiredFeatureActionId = FeatureAction.BULK_PAYMENT_TEMPLATE_EDIT;
        
        if (!(authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (userId, requiredFeatureActionId, fromAccount, CustomerSession.IsCombinedUser(customer)))) {
			return ErrorCodeEnum.ERR_28011.setErrorCode(new Result());
		}
        
        String previousAccount = bulkPaymentTemplateBusinessDelegate.fetchTemplateById(templateId).getFromAccount();
        CustomerAccountsDTO previousAccountDTO = accountBusinessDelegate.getAccountDetails(userId, previousAccount);
        CustomerAccountsDTO fromAccountDTO = accountBusinessDelegate.getAccountDetails(userId, fromAccount);
        
        if(previousAccountDTO == null || fromAccountDTO == null || !fromAccountDTO.getOrganizationId().equals(previousAccountDTO.getOrganizationId())) {
        	return ErrorCodeEnum.ERR_28026.setErrorCode(new Result());
        }
        
        Boolean isTemplateDeleted = bulkPaymentTemplateBusinessDelegate.deleteTemplate(templateId);
        if(isTemplateDeleted) {
        	bulkPaymentTemplateDTO.setTemplateId(templateId);
        	updatedTemplateDTO = bulkPaymentTemplateBusinessDelegate.createTemplate(bulkPaymentTemplateDTO);
        	if(updatedTemplateDTO == null) {   		
        		return ErrorCodeEnum.ERR_28006.setErrorCode(new Result()); 		
        	}
			JSONObject resultObject = new JSONObject();
            resultObject.put(Constants.TEMPLATE_ID, updatedTemplateDTO.getTemplateId());
            result = JSONToResult.convert(resultObject.toString());
        }
        
        else {
			return ErrorCodeEnum.ERR_28011.setErrorCode(new Result());
		}
        _logBulkPaymentTemplate(request, response, result, EventSubType.BULK_PAYMENT_TEMPLATE_EDIT, updatedTemplateDTO);
		return result;
	}
	
	@Override
	public Result createBulkRequest(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		try {

			@SuppressWarnings("unchecked")
			Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
			String userId = CustomerSession.getCustomerId(customer);
			String templateId = inputParams.get("templateId") != null ? inputParams.get("templateId").toString() : null;
			String recordsIP = inputParams.get("POs") != null ? inputParams.get("POs").toString() : null;

			if (templateId == null || templateId.isEmpty()) {
				alert.prepareError("templateId is Empty.").log();
				return ErrorCodeEnum.ERR_28016.setErrorCode(new Result());
			}
			
			if (recordsIP == null) {
				return ErrorCodeEnum.ERR_12028.setErrorCode(new Result());
			}
			
			JSONArray executeRecords;

			try {
				executeRecords = new JSONArray(recordsIP.toString());
			} catch (Exception e) {
				alert.prepareError("Error occurred while parsing the input records: ", e).log();
				return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
			}
			
			List<BulkPaymentTemplatePODTO>	poInputList = JSONUtils.parseAsList(executeRecords.toString(), BulkPaymentTemplatePODTO.class);
			BulkPaymentTemplateDTO template = bulkPaymentTemplateBusinessDelegate.fetchTemplateById(templateId);
			
			if (template != null) {
				
				List<BulkPaymentTemplatePODTO> paymentOrders = bulkPaymentTemplateBusinessDelegate.fetchTemplatePOsByTemplateId(templateId);

				if (paymentOrders.size() > 0) {
					
					paymentOrders=(new FilterDTO()).merge(poInputList, paymentOrders, "paymentOrderId=paymentOrderId","");

					if (! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (userId, template.getFeatureActionId(), template.getFromAccount(), CustomerSession.IsCombinedUser(customer))) {
						return ErrorCodeEnum.ERR_28020.setErrorCode(new Result());
					}
					
					BulkPaymentTemplateDTO requestDTO =  bulkPaymentTemplateBusinessDelegate.createTemplateRequest(template, paymentOrders);
					
					if(requestDTO != null) {
						
					String encodedContent = bulkPaymentTemplateBusinessDelegate.generateCSV(requestDTO.getPaymentrequestId());
					
					BulkPaymentFileDTO fileDTO =  bulkPaymentTemplateBusinessDelegate.uploadTemplateRequestAsFile(template, encodedContent, dcRequest);
					
						if (fileDTO == null) {
							alert.prepareError("BulkPayment file dto is null. Error occured while uploading Bulk Payment File").log();
							return ErrorCodeEnum.ERR_21204.setErrorCode(new Result());
						}

						try {
							JSONObject bulkPaymentfileObject = new JSONObject(fileDTO);
							result = JSONToResult.convert(bulkPaymentfileObject.toString());
						} catch (JSONException e) {
							alert.prepareError("Error occured while converting the file to JSON", e).log();
							return ErrorCodeEnum.ERR_21200.setErrorCode(new Result());
						}
					} else {
						return ErrorCodeEnum.ERR_28017.setErrorCode(new Result());
					}
				} else {
					return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
				}
				
			} else {
				return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
			}
			
		}
		catch(Exception e) {
			alert.prepareError("Exception occured while creating Bulk request", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
	

	@Override
	public Result fetchPOsByTemplateId(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_TEMPLATE_VIEW);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (features == null) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String templateId = inputParams.get("templateId") != null ? inputParams.get("templateId").toString() : null;
		
		if (StringUtils.isEmpty(templateId)) {
			alert.prepareError("templateId is Empty.").log();
			return ErrorCodeEnum.ERR_28016.setErrorCode(new Result());
		}
		Result result = new Result();
		
		List<BulkPaymentTemplatePODTO> paymentOrders = bulkPaymentTemplateBusinessDelegate.fetchTemplatePOsByTemplateId(templateId);
		
		if (paymentOrders == null) {
			alert.prepareError("Error occurred while fetching payment orders by templateId").log();
			return ErrorCodeEnum.ERR_28019.setErrorCode(new Result());
		}
		
		if (paymentOrders.size() > 0 && StringUtils.isNotBlank(paymentOrders.get(0).getDbpErrMsg())) {
			alert.prepareError("Error occurred while fetching payment orders by templateId").log();
			return ErrorCodeEnum.ERR_28019.setErrorCode(new Result(), paymentOrders.get(0).getDbpErrMsg());
		}
		
		try {
			JSONArray resArray = new JSONArray(paymentOrders);
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.PAYMENTORDERS, resArray);
			result = JSONToResult.convert(resultObj.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_28019.setErrorCode(new Result());
		}
		return result;
	}
	
	@Override
	public Result fetchTemplateByTemplateId(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		List<String> requiredActionIds = Arrays.asList(FeatureAction.BULK_PAYMENT_TEMPLATE_VIEW);
		String features = CustomerSession.getPermittedActionIds(request, requiredActionIds);

		if (features == null) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		@SuppressWarnings("unchecked")
		Map<String, String> inputParams = (HashMap<String, String>) inputArray[1];
		String templateId = inputParams.get("templateId") != null ? inputParams.get("templateId").toString() : null;
		
		if (StringUtils.isEmpty(templateId)) {
			alert.prepareError("templateId is Empty.").log();
			return ErrorCodeEnum.ERR_28016.setErrorCode(new Result());
		}
		Result result = new Result();
		
		BulkPaymentTemplateDTO template = bulkPaymentTemplateBusinessDelegate.fetchTemplateById(templateId);
		
		if (template == null) {
			alert.prepareError("Error occurred while fetching template details by templateId").log();
			return ErrorCodeEnum.ERR_28018.setErrorCode(new Result());
		}
		
		try {
			JSONObject templateObject = new JSONObject(template);
			result = JSONToResult.convert(templateObject.toString());
		} catch (Exception exp) {
			alert.prepareError("Error occurred while converting list to result", exp).log();
			return ErrorCodeEnum.ERR_28018.setErrorCode(new Result());
		}
		return result;
	}
	
	
}
