package com.temenos.dbx.product.achservices.resource.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHCommonsBusinessDelegate;
import com.temenos.dbx.product.achservices.businessdelegate.api.ACHTemplateBusinessDelegate;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateSubRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateTypeDTO;
import com.temenos.dbx.product.achservices.resource.api.ACHTemplateResource;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.CustomerBusinessDelegate;
import com.temenos.dbx.product.commons.dto.CustomerAccountsDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.ACHConstants;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;

public class ACHTemplateResourceImpl implements ACHTemplateResource{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	ACHTemplateBusinessDelegate achDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHTemplateBusinessDelegate.class);
	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	ACHCommonsBusinessDelegate achCommonsDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ACHCommonsBusinessDelegate.class);
	CustomerBusinessDelegate customerDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerBusinessDelegate.class);
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
	AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);

	private static boolean validateRegex(String regex, String string) {
		string.trim();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}
	
	@Override
	public Result createACHTemplate(String emthodID, Object[] inputArray, DataControllerRequest dcRequest, 
									DataControllerResponse response) {
		
		Result result = new Result();

		try {

			@SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];

			Result operationResult = _validateRequestParamsForCreateTemplate(requestMap, dcRequest);

			if(operationResult == null || operationResult.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null
					|| operationResult.getParamByName(ErrorCodeEnum.ERROR_MESSAGE_KEY) != null) {
				return operationResult;
			}

			JSONObject requestObj = new JSONObject(requestMap);
			BBTemplateDTO requestDTO = JSONUtils.parse(requestObj.toString(), BBTemplateDTO.class);

			BBTemplateDTO responseDTO = achDelegate.createTemplate(requestDTO);
			if(responseDTO == null) {
				return ErrorCodeEnum.ERR_12101.setErrorCode(new Result());
			}

			JSONObject responseObj = new JSONObject(responseDTO);
			result = JSONToResult.convert(responseObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while creating an ACH template",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}

	@Override
	public Result editACHTemplate(String emthodID, Object[] inputArray, DataControllerRequest dcRequest, 
			DataControllerResponse response) {
		
		Result result = new Result();

		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
			String userId = CustomerSession.getCustomerId(customer);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> requestMap = (HashMap<String, Object>) inputArray[1];
			String template_id = requestMap.get("TemplateId") != null ? requestMap.get("TemplateId").toString() : null;

			if (template_id == null || template_id.isEmpty()) {
				return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
			}

			FilterDTO filters = new FilterDTO();
			filters.set_featureactionlist(String.join(",", FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW));
			
			List<BBTemplateDTO> templates = achDelegate.fetchAllACHTemplates(userId, filters, template_id);
			if(templates == null || templates.size() == 0) {
				return ErrorCodeEnum.ERR_13502.setErrorCode(new Result()); 
			}
			
			BBTemplateDTO templateDTO = templates.get(0);
			String transactionType_id = String.valueOf(templateDTO.getTransactionType_id());
			if (transactionType_id == null || transactionType_id.isEmpty()) {
				return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
			}

			List<String> requiredActionIds = null;
			if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_COLLECTION)) {
				requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_EDIT_TEMPLATE);
			} 
			else if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_PAYMENT)) {
				requiredActionIds = Arrays.asList(FeatureAction.ACH_PAYMENT_EDIT_TEMPLATE);
			}
			
			String featureActionId = CustomerSession.getPermittedActionIds(dcRequest, requiredActionIds);
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}

			if (! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (userId, featureActionId, templateDTO.getFromAccount(), CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			if (requestMap.get("DebitAccount") != null) {
				templateDTO.setFromAccount(requestMap.get("DebitAccount").toString());
			}
			if (requestMap.get("MaxAmount") != null) {
				templateDTO.setMaxAmount(Double.parseDouble(requestMap.get("MaxAmount").toString()));
			}
			if (requestMap.get("Records") != null) {
				templateDTO.setRecords(requestMap.get("Records").toString());
			}

			BBTemplateDTO deletedDTO = achDelegate.deleteTemplate(Long.parseLong(template_id));
			if (deletedDTO != null) {
				String records = String.valueOf(requestMap.remove("Records"));
				boolean doesSubRecordExists = String.valueOf(templateDTO.getTemplateRequestType_id())
						.equals(ACHConstants.TEMPLATE_REQUEST_TYPE_FEDERAL_TAX);
				BBTemplateDTO amountDTO = achCommonsDelegate.validateTotalAmount(doesSubRecordExists, records,
						templateDTO.getMaxAmount());

				if (amountDTO == null) {
					return ErrorCodeEnum.ERR_21014.setErrorCode(new Result());
				}
				templateDTO.setTotalAmount(amountDTO.getTotalAmount());
				templateDTO.setUpdatedBy(userId);

				BBTemplateDTO responseDTO = achDelegate.createTemplate(templateDTO);
				if (responseDTO == null) {
					return ErrorCodeEnum.ERR_21014.setErrorCode(new Result());
				}
				
				result.addStringParam("Success", "Successful");
				result.addStringParam("Template_id", responseDTO.getTemplateId()+"");
				return result;
			}
			return ErrorCodeEnum.ERR_21014.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Exception occured while updating an ACH template",exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
	}
	
	/**
	 *@author KH2626
	 *@version 1.0
	 *@param requestMap contains the map of request input parameters
	 *@param dcRequest request object which contains loggedIn user info
	 * **/
	private Result _validateRequestParamsForCreateTemplate(HashMap<String, Object> requestMap, DataControllerRequest dcRequest) {

		Result result = new Result();

		Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
		String createdby = CustomerSession.getCustomerId(customer);

		String templateType_id = (String) requestMap.get("TemplateType_id");
		String templateRequestTypeId = (String) requestMap.get("TemplateRequestType_id");
		String maxAmount = (String) requestMap.get("MaxAmount");
		String transactionType_id = (String) requestMap.get("TransactionType_id");
		String debitAccount = (String) requestMap.get("DebitAccount");
		String templateName = (String) requestMap.get("TemplateName");
		String templateDescription = (String) requestMap.get("TemplateDescription");

		String records = (String) requestMap.get("Records");

		if (templateType_id == null || templateType_id.equals("")) {
			requestMap.put("templateType_id", ACHConstants.TEMPLATE_TYPE_ACH);
		}
		if (StringUtils.isBlank(templateName) || !validateRegex("[A-Za-z0-9\\s]{1,51}$", templateName)) {
			return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
		}
		if (StringUtils.isBlank(templateDescription) || !validateRegex("[A-Za-z0-9\\s]{1,51}$", templateDescription)) {
			return ErrorCodeEnum.ERR_29036.setErrorCode(new Result());
		}
		if (templateRequestTypeId == null || templateRequestTypeId.equals("")) {
			return ErrorCodeEnum.ERR_12027.setErrorCode(new Result());
		}
		if (transactionType_id == null || transactionType_id.equals("")) {
			return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
		}
		if (debitAccount == null || debitAccount.equals("")) {
			return ErrorCodeEnum.ERR_12022.setErrorCode(new Result());
		}
		if (maxAmount == null || maxAmount.equals("")) {
			return ErrorCodeEnum.ERR_12029.setErrorCode(new Result());
		}
		if (records == null || records.equals("")) {
			return ErrorCodeEnum.ERR_12028.setErrorCode(new Result());
		}
		if (records != null && !templateRequestTypeId.equals(ACHConstants.TEMPLATE_REQUEST_TYPE_FEDERAL_TAX)){
			JSONArray array = new JSONArray(records);
			for(Object obj : array){
				JSONObject recordObj = (JSONObject) obj;
				String recordName = (String) recordObj.get("Record_Name");
				if(StringUtils.isBlank(recordName) || !validateRegex("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z 0-9]*)*$", recordName)){
					return ErrorCodeEnum.ERR_29038.setErrorCode(new Result());
				}
			}
		}
		
		CustomerAccountsDTO account = accountBusinessDelegate.getAccountDetails(createdby, debitAccount.toString());
		String contractId = account.getContractId();
		String coreCustomerId = account.getCoreCustomerId();
		String companyId = account.getOrganizationId();

		String featureActionId = "";

		if (templateType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH)) {
			if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_COLLECTION)) {
				featureActionId = FeatureAction.ACH_COLLECTION_CREATE_TEMPLATE;
			}
			else if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_PAYMENT)) {
				featureActionId = FeatureAction.ACH_PAYMENT_CREATE_TEMPLATE;
			}
		}

		//removing account level authorization check. since, there is an issue to be fixed from c360.
		
		if (! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (createdby, featureActionId, debitAccount, CustomerSession.IsCombinedUser(customer))) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}

		boolean doesSubRecordExists = templateRequestTypeId.equals(ACHConstants.TEMPLATE_REQUEST_TYPE_FEDERAL_TAX);
		BBTemplateDTO amountResult = achCommonsDelegate.validateTotalAmount(doesSubRecordExists, records, Double.parseDouble(maxAmount));
		
		if(amountResult != null) {
			requestMap.put("companyId", companyId);
			requestMap.put("roleId", customerDelegate.getUserContractCustomerRole(contractId, coreCustomerId, createdby));
			requestMap.put("createdby", createdby);
			requestMap.put("status", TransactionStatusEnum.NEW.getStatus());
			requestMap.put("featureActionId", featureActionId);
			requestMap.put("totalAmount", amountResult.getTotalAmount());
			result.addParam(new Param("isValid","true"));
			return result;
		}
		return ErrorCodeEnum.ERR_21013.setErrorCode(new Result());
	}

	@Override
	public Result executeTemplate(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response){

		try {

			@SuppressWarnings("unchecked")
			HashMap<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

			BBTemplateDTO bbTemplateDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), BBTemplateDTO.class);
			String template_id = inputParams.get("TemplateId").toString();
			String effectiveDate = inputParams.get("EffectiveDate").toString();
			String recordsIP = inputParams.get("Records").toString();

			if (template_id == null) {
				return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
			}
			if (effectiveDate == null) {
				return ErrorCodeEnum.ERR_12021.setErrorCode(new Result());
			}
			if (recordsIP == null) {
				return ErrorCodeEnum.ERR_12028.setErrorCode(new Result());
			}

			JSONArray executeRecords = new JSONArray(recordsIP.toString());

			ACHTemplateBusinessDelegate AchServicesBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ACHTemplateBusinessDelegate.class);

			List<BBTemplateDTO> bbTemplateDTOList = AchServicesBusinessDelegate.fetchTemplate(bbTemplateDTO);
			JSONObject templateParams = new JSONObject();
			if(bbTemplateDTOList.size()>0) {

				bbTemplateDTO = bbTemplateDTOList.get(0);
				boolean doesSubRecordExists = ACHConstants.TEMPLATE_REQUEST_TYPE_FEDERAL_TAX.equals(bbTemplateDTO.getTemplateRequestType_id()+"");

				List<BBTemplateRecordDTO> bbTemplateRecordDTO = AchServicesBusinessDelegate.fetchTemplateRecords(bbTemplateDTO);

				if(bbTemplateRecordDTO.size()>0) {
					Iterator<BBTemplateRecordDTO> it = bbTemplateRecordDTO.iterator();

					HashMap<String, BBTemplateRecordDTO> recordMap = new HashMap<>();

					while (it.hasNext()) {
						BBTemplateRecordDTO p = it.next();
						recordMap.put(String.valueOf(p.getTemplateRecord_id()), p);
					}
					JSONArray recordsJsonArray = new JSONArray();

					for (int i = 0; i < executeRecords.length(); i++) {

						JSONObject executeRecord = executeRecords.getJSONObject(i);
						BBTemplateRecordDTO record = recordMap.get(executeRecord.get("TemplateRecord_id"));

						if (!doesSubRecordExists) {
							record.setAmount(Double.parseDouble(executeRecord.getString("Amount")));
							executeRecord = new JSONObject(record);
							executeRecord.put("Amount", String.valueOf(record.getAmount()));
							executeRecord.put("ABATRCNumber", executeRecord.get("abatrcNumber"));
						} else {
							JSONArray executeSubRecords = new JSONArray();
							executeRecord = new JSONObject(record);
							executeRecord.put("SubRecords", executeRecords.getJSONObject(i).getJSONArray("SubRecords"));
							executeRecord.put("EIN", executeRecord.get("ein"));
							executeRecord.put("ABATRCNumber", executeRecord.get("abatrcNumber"));
							try {
								executeSubRecords = executeRecord.getJSONArray("SubRecords");
							} catch (NullPointerException exp) {
								// No subRecords found;
							} catch (JSONException exp1) {
								// No subRecords found;
							}
							List<BBTemplateSubRecordDTO> templateSubRecords = AchServicesBusinessDelegate.fetchTemplateSubRecord(record);
							if (templateSubRecords.size() > 0) {
								HashMap<String, BBTemplateSubRecordDTO> subRecordMap = new HashMap<>();
								JSONArray subRecords = new JSONArray();
								Iterator<BBTemplateSubRecordDTO> sr = templateSubRecords.iterator();
								while (sr.hasNext()) {
									BBTemplateSubRecordDTO p = sr.next();
									subRecordMap.put(String.valueOf(p.getTemplateSubRecord_id()), p);
								}
								for (int j = 0; j < executeSubRecords.length(); j++) {
									JSONObject obj = executeSubRecords.getJSONObject(j);
									subRecordMap.get(obj.get("TemplateSubRecord_id")).setAmount(Double.parseDouble(obj.getString("Amount")));
									JSONObject executeSubRecord = new JSONObject(obj);
									executeSubRecord.put("Amount", String.valueOf(subRecordMap.get(obj.get("TemplateSubRecord_id")).getAmount()));
									executeSubRecord.put("TaxSubCategory_id", subRecordMap.get(obj.get("TemplateSubRecord_id")).getTaxSubCategory_id());
									executeSubRecord.put("TaxSubType", subRecordMap.get(obj.get("TemplateSubRecord_id")).getTaxSubType());
									subRecords.put(executeSubRecord);
								}
								executeRecord.put("SubRecords", subRecords);
							} else {
								return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
							}
						}
						recordsJsonArray.put(executeRecord);
					}

					templateParams.put("Records", recordsJsonArray);
				}
				else {
					return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
				}
			}
			else {
				return ErrorCodeEnum.ERR_12003.setErrorCode(new Result());
			}
			JSONArray recordsArray = templateParams.getJSONArray("Records");
			Result result = null;
            bbTemplateDTO.setEffectiveDate(inputParams.get("EffectiveDate").toString());
			result = invokeCreateAchTransaction(bbTemplateDTO, recordsArray, request);

			bbTemplateDTO.setEffectiveDate(application.getServerTimeStamp());
			AchServicesBusinessDelegate.updateTemplate(bbTemplateDTO);
            return result;
		}
		catch(Exception e) {
			alert.prepareError("Error occurred while defining resources for executing templates", e).log();
		}
		return null;
	}

	/**
	 * This method invokes createTransaction JavaService.
	 * @author KH2624
	 * @param bbTemplateDTO contains templateDetails
	 * @param templateParams has template records
	 * @param request Data controller Request
	 * @return
	 */
	public Result invokeCreateAchTransaction(BBTemplateDTO bbTemplateDTO, JSONArray templateParams, DataControllerRequest request) {
		try{
			Result resultDTO = null;
			String serviceName = ServiceId.DBP_ACH_SERVICES;
			String operationName = OperationName.BB_CREATE_TRANSACTION;
			Map<String, Object> requestParameters = new HashMap<String, Object>();
			requestParameters.put("Records", templateParams.toString());
			requestParameters.put("TemplateRequestType_id",bbTemplateDTO.getTemplateRequestType_id());
			requestParameters.put("TransactionType_id",bbTemplateDTO.getTransactionType_id());
			requestParameters.put("DebitAccount", bbTemplateDTO.getFromAccount());
			requestParameters.put("MaxAmount", bbTemplateDTO.getMaxAmount().toString());
			requestParameters.put("EffectiveDate",bbTemplateDTO.getEffectiveDate());
			requestParameters.put("Template_id", Long.toString(bbTemplateDTO.getTemplateId()));
			requestParameters.put("TemplateType_id", Long.toString(bbTemplateDTO.getTemplateType_id()));
			requestParameters.put("TemplateName", bbTemplateDTO.getTemplateName());
		    Map<String,Object> headers = request.getHeaderMap();
			String clientIp = request.getHeader(DBPUtilitiesConstants.CLIENT_IP);
			if (StringUtils.isBlank(clientIp)) {
				try {
					clientIp = HelperMethods.getDecyptedClientIpAddressFromCache(request);
				} catch (Exception e) {
				}
			} else {
				clientIp = HelperMethods.decryptClientIp(clientIp);
			}
			if (!headers.containsKey("x-forwarded-for")) {
				headers.put("x-forwarded-for", clientIp);
			}

			resultDTO = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParameters).
					withRequestHeaders(headers).
					withDataControllerRequest(request).
					build().getResult();
			
			return resultDTO;
		}
		catch (Exception e){
			alert.prepareError("Error occurred while defining resources for creating ach tranaction", e).log();
		}
		return null;
	}

	@Override
	public Result getACHTemplateType(Object[] inputArray, DataControllerRequest dcRequest) {
		Result recordResult = new Result();

		List<BBTemplateTypeDTO> templateTypes = achDelegate.getACHTemplateType();
		String templateTypesJSONString=null;
		try {
			
			templateTypesJSONString = JSONUtils.stringifyCollectionWithTypeInfo(templateTypes, BBTemplateTypeDTO.class);
		
		} catch (IOException e) {
		
			alert.prepareError(e.toString()).log();
		}
		JSONArray tempalteTypeJsonArr = new JSONArray(templateTypesJSONString);
		JSONObject responseObj = new JSONObject();
		responseObj.put(Constants.BB_TEMPLATE_TYPE, tempalteTypeJsonArr);
		recordResult = JSONToResult.convert(responseObj.toString());

		return recordResult;
	}
	
	@Override
	public Result fetchAllACHTemplates(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		try {
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			List<String> requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW_TEMPLATE, FeatureAction.ACH_PAYMENT_VIEW_TEMPLATE);
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
	
			@SuppressWarnings("unchecked")
			Map<String, Object> filterParamsMap = (HashMap<String, Object>) inputArray[1];
			filterParamsMap.put("_featureactionlist", featureActionId);
			
			JSONObject requestObj = new JSONObject(filterParamsMap);
			FilterDTO params = JSONUtils.parse(requestObj.toString(), FilterDTO.class);
			if(!params.isValidFilter()) {
				alert.prepareError("Input contains special characters").log();
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, new JSONArray());
				return JSONToResult.convert(resultObj.toString());
			}
			
			List<BBTemplateDTO> templates = achDelegate.fetchAllACHTemplates(userId, params, "");
				
			if(templates != null) {
				String listResponse = JSONUtils.stringifyCollectionWithTypeInfo(templates, BBTemplateDTO.class);
				JSONArray resArray = new JSONArray(listResponse);
				JSONObject resultObj = new JSONObject();
				resultObj.put(Constants.RECORDS, resArray);
				result = JSONToResult.convert(resultObj.toString());
			}
			else
				return ErrorCodeEnum.ERR_13502.setErrorCode(new Result());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while defining resources for fetch all templates", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
	
	@Override
	public Result deleteACHTemplate(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			String template_id = inputParams.get("TemplateId") != null ? inputParams.get("TemplateId").toString() : null;
			
			if (template_id == null || template_id.isEmpty()) {
				return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
			}

			FilterDTO filters = new FilterDTO();
			filters.set_featureactionlist(String.join(",", FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW));
			
			List<BBTemplateDTO> templates = achDelegate.fetchAllACHTemplates(userId, filters, template_id);
			
			if(templates == null || templates.size() == 0) {
				return ErrorCodeEnum.ERR_13502.setErrorCode(new Result()); 
			}
			
			String transactionType_id = templates.get(0).getTransactionType_id()+"";
			
			if (transactionType_id == null || transactionType_id.isEmpty()) {
				return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
			}

			List<String> requiredActionIds = null;
			if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_COLLECTION)) {
					requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_DELETE_TEMPLATE);
			}
			else if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_PAYMENT)) {
					requiredActionIds = Arrays.asList(FeatureAction.ACH_PAYMENT_DELETE_TEMPLATE);
			}
				
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			
			if (! authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction (userId, featureActionId, templates.get(0).getFromAccount(), CustomerSession.IsCombinedUser(customer))) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			boolean deleteSuccess = achDelegate.softDeleteTemplate(Long.parseLong(template_id));
			if(deleteSuccess) {
				result.addStringParam("Success", "Successful");
				result.addStringParam("Template_id", templates.get(0).getTemplateId()+"");
			}
			else {
				return ErrorCodeEnum.ERR_21013.setErrorCode(new Result());
			}
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while deleting the ACH template ", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
	
	@Override
	public Result fetchACHTemplateById(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		
		try {
			@SuppressWarnings("unchecked")
			HashMap<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			String userId = CustomerSession.getCustomerId(customer);
			
			String template_id = inputParams.get("TemplateId") != null ? inputParams.get("TemplateId").toString() : null;
			
			if (template_id == null || template_id.equals("")) {
				return ErrorCodeEnum.ERR_12025.setErrorCode(new Result());
			}
			
			FilterDTO filters = new FilterDTO();
			filters.set_featureactionlist(String.join(",", FeatureAction.ACH_COLLECTION_VIEW, FeatureAction.ACH_PAYMENT_VIEW));
			
			List<BBTemplateDTO> templates = achDelegate.fetchAllACHTemplates(userId, filters, template_id);
			
			if(templates == null || templates.size() == 0) {
				return ErrorCodeEnum.ERR_13502.setErrorCode(new Result());
			}
			
			String transactionType_id = templates.get(0).getTransactionType_id()+"";
			
			if (transactionType_id == null || transactionType_id.equals("")) {
				return ErrorCodeEnum.ERR_12026.setErrorCode(new Result());
			}
			
			List<String> requiredActionIds = null;
			if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_COLLECTION)) {
					requiredActionIds = Arrays.asList(FeatureAction.ACH_COLLECTION_VIEW_TEMPLATE);
			}
			else if (transactionType_id.equals(ACHConstants.TEMPLATE_TYPE_ACH_PAYMENT)) {
					requiredActionIds = Arrays.asList(FeatureAction.ACH_PAYMENT_VIEW_TEMPLATE);
			}
			
			String featureActionId = CustomerSession.getPermittedActionIds(request, requiredActionIds);
			if(featureActionId == null) {
	     		return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			JSONArray resArray = new JSONArray(templates);
			JSONObject resultObj = new JSONObject();
			resultObj.put(Constants.RECORDS, resArray);
			result = JSONToResult.convert(resultObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while defining resources for fetch template by Id ", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
}