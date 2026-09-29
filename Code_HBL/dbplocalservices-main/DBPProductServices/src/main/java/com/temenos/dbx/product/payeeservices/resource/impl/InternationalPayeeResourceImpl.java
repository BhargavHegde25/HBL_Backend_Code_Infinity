package com.temenos.dbx.product.payeeservices.resource.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.dataobject.Param;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.payeeservices.constants.PayeeConstants;
import com.temenos.dbx.product.payeeservices.utils.PayeeUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.InternationalPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InternationalPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeDTO;
import com.temenos.dbx.product.payeeservices.resource.api.InternationalPayeeResource;

public class InternationalPayeeResourceImpl implements InternationalPayeeResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	
	InternationalPayeeBusinessDelegate payeeDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(InternationalPayeeBusinessDelegate.class);
	
	InternationalPayeeBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(InternationalPayeeBackendDelegate.class);

    @Override
   	public Result createPayee(String methodID, Object[] inputArray, DataControllerRequest request, 
   			DataControllerResponse response) {
   		
   		@SuppressWarnings("unchecked")
   		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
   		Result result = new Result();
   		
   		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
   		String userId = CustomerSession.getCustomerId(customer);

   		String legalEntityId = (String) customer.get("legalEntityId");
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT);
   		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get("cif");
		if(StringUtils.isBlank(sharedCifs)) {
			sharedCifMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(featureActions, request.getHeaderMap(), request);
			if(sharedCifMap == null || sharedCifMap.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			PayeeUtils.prepareAndAddAuthorizedCifsInPayload(inputParams, sharedCifMap);
		}
		else {
			sharedCifMap = _getContractCifMap(sharedCifs);
					
			//Authorization for all the cifs present in input for which the payee needs to be shared
			/*
			 * if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(
			 * featureActions, sharedCifMap, request.getHeaderMap(), request)) { alert.
			 * prepareError("The logged in user doesn't have permission to perform this action"
			 * ).log(); return ErrorCodeEnum.ERR_12001.setErrorCode(new Result()); }
			 */
		}
   		
   		InternationalPayeeBackendDTO internationalPayeeBackendDTO = new InternationalPayeeBackendDTO();
   		try {
   			internationalPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalPayeeBackendDTO.class);
   			internationalPayeeBackendDTO.setUserId(userId);
   		} catch (IOException e) {
   			alert.prepareError("Error occured while fetching the input params: " + e).log();
   			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
   		}
   		
   		if(!internationalPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}
   		if(StringUtils.isBlank(internationalPayeeBackendDTO.getBeneficiaryName()) 
				|| (StringUtils.isBlank(internationalPayeeBackendDTO.getIban()) && StringUtils.isBlank(internationalPayeeBackendDTO.getAccountNumber()))
				|| (StringUtils.isBlank(internationalPayeeBackendDTO.getSwiftCode())
						&& (StringUtils.isBlank(internationalPayeeBackendDTO.getClearingCode()) || StringUtils.isBlank(internationalPayeeBackendDTO.getClearingIdentifierCode()))
						&& (StringUtils.isBlank(internationalPayeeBackendDTO.getBankName()) || StringUtils.isBlank(internationalPayeeBackendDTO.getIntermediaryBIC()))
						&& (StringUtils.isBlank(internationalPayeeBackendDTO.getBankName()) || StringUtils.isBlank(internationalPayeeBackendDTO.getBankCountryName()) || StringUtils.isBlank(internationalPayeeBackendDTO.getTownName())))){
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		
   		/*
   		String accNum = StringUtils.isNotBlank(internationalPayeeBackendDTO.getIban()) ? internationalPayeeBackendDTO.getIban() : internationalPayeeBackendDTO.getAccountNumber();
   		if(!payeeBackendDelegate.isValidIbanAndSwiftCode(accNum, internationalPayeeBackendDTO.getSwiftCode(), request)) {
			alert.prepareError("Not a valid iban or swift code").log();
			return ErrorCodeEnum.ERR_12064.setErrorCode(new Result());
		}
		*/
   		
		if(!_isUniquePayee(request, internationalPayeeBackendDTO, sharedCifMap))
		{
			alert.prepareError("Duplicate Payee or Error occured while checking for uniqueness.").log();
			return ErrorCodeEnum.ERR_12062.setErrorCode(new Result());
		}

		String id = HelperMethods.getRandomNumericString(8);
		internationalPayeeBackendDTO.setPayeeId(id);
		TransactionStatusDTO payloadForValidateForApprovals = PayeeUtils.getPayloadForValidateForApprovalsForCreatePayee(internationalPayeeBackendDTO);
		JSONObject transactionResponse;
		try {
			transactionResponse = payeeDelegate.validateForApprovals(request, payloadForValidateForApprovals);
			if(transactionResponse == null) {
				throw new ApplicationException(ErrorCodeEnum.ERR_26024);
			} else if(StringUtils.isNotBlank(transactionResponse.optString("dbpErrCode")) || StringUtils.isNotBlank(transactionResponse.optString("dbpErrMsg"))) {
				result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, transactionResponse.get("dbpErrCode").toString(), MWConstants.INT));
				result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, transactionResponse.getString("dbpErrMsg"), MWConstants.STRING));
				result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "-1", MWConstants.INT));
				result.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "400", MWConstants.INT));
			} else if( TransactionStatusEnum.SENT.getStatus().equals(transactionResponse.optString("status"))) {
				result = createPayeeAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo(),false);
			} else if( TransactionStatusEnum.PENDING.getStatus().equals(transactionResponse.optString("status"))){
				result.addParam("transactionStatus", TransactionStatusEnum.PENDING.getStatus());
				result.addParam("referenceId", transactionResponse.getString("transactionId"));
				boolean selfApprovalFlag = transactionResponse.has("isSelfApproved") && transactionResponse.getString("isSelfApproved").equals("true");
				result.addParam("isSelfApprovalFlag", Boolean.toString(selfApprovalFlag));

				//Create Entry in dbxdb with isApprovedflag 0 to avoid duplicate entry for pending beneficiary
				Result result2 = createPayeeAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo(),true);

			} else if(TransactionStatusEnum.APPROVED.getStatus().equals(transactionResponse.optString("status"))) {
				result = createPayeeAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo(),false);
			}
			transactionResponse.put("className", InternationalPayeeResourceImpl.class.getName());
			transactionResponse.put("associatedCifMap", sharedCifMap);
			PayeeUtils.callPushEvent(result, request, response, "CREATE", transactionResponse);
		} catch ( ApplicationException e ) {
			return e.getErrorCodeEnum().setErrorCode(result);
		}
		return result;
   	}


	@Override
	public Result createPayeeAfterApproval( DataControllerRequest request, JSONObject inputObject,boolean isPending) {
		Map<String, Object> inputParams = inputObject.toMap();
		String legalEntityId;
		if(inputParams.containsKey("legalEntityId") && inputParams.get("legalEntityId") != null) {
			legalEntityId = inputParams.get("legalEntityId").toString();
		} else {
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			legalEntityId = (String) customer.get("legalEntityId");
		}
		String userId;
		if(inputParams.containsKey("userId") && inputParams.get("userId") != null) {
			userId = inputParams.get("userId").toString();
		} else {
			userId = CustomerSession.getCustomerId(CustomerSession.getCustomerMap(request));
		}
		InternationalPayeeBackendDTO internationalPayeeBackendDTO = new InternationalPayeeBackendDTO();
		InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get("cif");
		sharedCifMap = _getContractCifMap(sharedCifs);

		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT);
		//Authorization for all the cifs present in input for which the payee needs to be shared
		/*
		 * if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(
		 * featureActions, sharedCifMap, request.getHeaderMap(), request)) { alert.
		 * prepareError("The logged in user doesn't have permission to perform this action"
		 * ).log(); return ErrorCodeEnum.ERR_12001.setErrorCode(new Result()); }
		 */
		try {
			internationalPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalPayeeBackendDTO.class);
		} catch ( IOException e ) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		if(!internationalPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		if(StringUtils.isBlank(internationalPayeeBackendDTO.getBeneficiaryName()) 
				|| (StringUtils.isBlank(internationalPayeeBackendDTO.getIban()) && StringUtils.isBlank(internationalPayeeBackendDTO.getAccountNumber()))
				|| (StringUtils.isBlank(internationalPayeeBackendDTO.getSwiftCode())
						&& (StringUtils.isBlank(internationalPayeeBackendDTO.getClearingCode()) || StringUtils.isBlank(internationalPayeeBackendDTO.getClearingIdentifierCode()))
						&& (StringUtils.isBlank(internationalPayeeBackendDTO.getBankName()) || StringUtils.isBlank(internationalPayeeBackendDTO.getIntermediaryBIC()))
						&& (StringUtils.isBlank(internationalPayeeBackendDTO.getBankName()) || StringUtils.isBlank(internationalPayeeBackendDTO.getBankCountryName()) || StringUtils.isBlank(internationalPayeeBackendDTO.getTownName())))){
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		if(isPending) {
			internationalPayeeBackendDTO.setIsApproved("0");
		}

		//Create one payee at the backend
		internationalPayeeBackendDTO = payeeBackendDelegate.createPayee(internationalPayeeBackendDTO, request.getHeaderMap(), request);
		if(internationalPayeeBackendDTO == null) {
			alert.prepareError("Error occured while creating payee at backend").log();
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}
		Result result = new Result();
		if(internationalPayeeBackendDTO.getDbpErrMsg() != null && !internationalPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalPayeeBackendDTO.getDbpErrMsg());
		}

		//Getting payeeId of created payee
		String payeeId = internationalPayeeBackendDTO.getId();
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("PayeeId is empty or null").log();
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}


		internationalPayeeDTO.setPayeeId(payeeId);
		internationalPayeeDTO.setCreatedBy(userId);

		if(StringUtils.isNotBlank(legalEntityId))
			internationalPayeeDTO.setLegalEntityId(legalEntityId);

		//Creating payee and cif mappings at dbx table
		for ( Map.Entry<String, List<String>> contractCif : sharedCifMap.entrySet() ) {
			internationalPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for ( int j = 0; j < coreCustomerIds.size(); j++ ) {
				internationalPayeeDTO.setCif(coreCustomerIds.get(j));
				payeeDelegate.createPayeeAtDBX(internationalPayeeDTO);
			}
		}

		try {
			JSONObject requestObj = new JSONObject(internationalPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch ( JSONException e ) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}


    /**
     * This method edits details of an existing International payee
     *
     * @param methodId   contains the operation id
     * @param inputArray contains the input parameter to edit a new International payee
     * @param request    contains request handler
     * @param response   contains the response handler
     * @return Result object contains edited payeeId
     * @author KH2544
     * @version 1.0
     */

    @Override
	public Result editPayee(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String legalEntityId = (String) customer.get("legalEntityId");
		//String userId = CustomerSession.getCustomerId(customer);
		
		String payeeId =  (String) inputParams.get("payeeId");
		if(StringUtils.isBlank(payeeId)) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		try {
			boolean payeeStatusInPending = payeeDelegate.checkIfPayeeStatusInPending(request, payeeId);
			if(payeeStatusInPending) {
				return ErrorCodeEnum.ERR_12613.setErrorCode(new Result());
			}
		} catch ( ApplicationException e ) {
			return ErrorCodeEnum.ERR_12612.setErrorCode(new Result());
		}
		Map<String, List<String>> editedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> removedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> addedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> associatedCifsMap = new HashMap<String, List<String>>();
		String editedCifs = (String) inputParams.get("cif");
		if(StringUtils.isBlank(editedCifs)) {
			inputParams.put("cif", "{}");
			editedCifsMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(Arrays.asList(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT), request.getHeaderMap(), request);
			if(editedCifsMap == null || editedCifsMap.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		else {
			editedCifsMap = _getContractCifMap(editedCifs);
			//Authorization check for cifs which are edited
			if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(Arrays.asList(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT), editedCifsMap, request.getHeaderMap(), request)) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		
		//Fetch payees using payeeId from dbx
		InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
		internationalPayeeDTO.setPayeeId(payeeId);
		internationalPayeeDTO.setLegalEntityId(legalEntityId);
		List<InternationalPayeeDTO> internationalPayeeDTOs = payeeDelegate.fetchPayeeByIdAtDBX(internationalPayeeDTO);
		if(internationalPayeeDTOs == null) {
			alert.prepareError("Failed to fetch payee").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		
		if(internationalPayeeDTOs.isEmpty()) {
			alert.prepareError("Records doesn't exist").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}
		
		Set<String> payeeIds = new HashSet<String>();
		payeeIds.add(payeeId);
		//Fetching backend records using payeeId
		List<InternationalPayeeBackendDTO> backendDTOs = payeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
		if(backendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from backend").log();
			return ErrorCodeEnum.ERR_12058.setErrorCode(new Result());
		}
		
		if(backendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
        }
		
		if(backendDTOs.get(0).getDbpErrMsg() != null && !backendDTOs.get(0).getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, backendDTOs.get(0).getDbpErrMsg());
		}
		
		associatedCifsMap = _getAssociatedCifsMap(internationalPayeeDTOs, request);
		if(StringUtils.isNotBlank(editedCifs)) {
			_setAddedAndRemovedCifMaps(editedCifsMap, addedCifsMap, removedCifsMap, associatedCifsMap);
			if(addedCifsMap.isEmpty() && removedCifsMap.isEmpty()) {
				editedCifs = null;
			}
		}
		
		String createdBy = internationalPayeeDTOs.get(0).getCreatedBy();
		InternationalPayeeBackendDTO internationalPayeeBackendDTO = new InternationalPayeeBackendDTO();
		try {
			internationalPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalPayeeBackendDTO.class);
			internationalPayeeBackendDTO.setUserId(createdBy);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(!internationalPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		String editType;
		try {
			editType = PayeeUtils.checkIfOptionalOrLinkageEdit(backendDTOs.get(0), internationalPayeeBackendDTO, addedCifsMap, removedCifsMap);
		} catch ( ApplicationException e ) {
			return e.getErrorCodeEnum().constructResultObject();
		}
		
		if(!_isUniquePayeeForEdit(internationalPayeeBackendDTO, backendDTOs.get(0), addedCifsMap, associatedCifsMap, editedCifsMap, editedCifs, request)) {
			alert.prepareError("Duplicate Payee or Error occured while checking for uniqueness.").log();
			return ErrorCodeEnum.ERR_12062.setErrorCode(new Result());
		}

		TransactionStatusDTO payloadForValidateForApprovals;
		if(PayeeConstants.LINKAGE_EDIT.equals(editType)) {
			payloadForValidateForApprovals = PayeeUtils.prepareEditLinkagePayload(internationalPayeeBackendDTO, addedCifsMap, removedCifsMap, internationalPayeeDTOs);
		} else if (PayeeConstants.OPTIONAL_FIELD_EDIT.equals(editType)) {
			InternationalPayeeBackendDTO oldInternationalPayeeBackendDTO = backendDTOs.get(0);
			payloadForValidateForApprovals = PayeeUtils.prepareEditOptionalPayload(internationalPayeeBackendDTO, oldInternationalPayeeBackendDTO);
		} else {
			return ErrorCodeEnum.ERR_12615.setErrorCode(new Result());
		}
		JSONObject transactionResponse;
		try {
			transactionResponse = payeeDelegate.validateForApprovals(request, payloadForValidateForApprovals);
			if(transactionResponse == null) {
				throw new ApplicationException(ErrorCodeEnum.ERR_26024);
			} else if(StringUtils.isNotBlank(transactionResponse.optString("dbpErrCode")) || StringUtils.isNotBlank(transactionResponse.optString("dbpErrMsg"))) {
				result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, transactionResponse.get("dbpErrCode").toString(), MWConstants.INT));
				result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, transactionResponse.getString("dbpErrMsg"), MWConstants.STRING));
				result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "-1", MWConstants.INT));
				result.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "400", MWConstants.INT));
			} else if( TransactionStatusEnum.SENT.getStatus().equals(transactionResponse.optString("status"))) {
				result = invokeEditPayeeAfterApproval(request, result, editType, payloadForValidateForApprovals);
			} else if( TransactionStatusEnum.PENDING.getStatus().equals(transactionResponse.optString("status"))){
				result.addParam("transactionStatus", TransactionStatusEnum.PENDING.getStatus());
				result.addParam("referenceId", transactionResponse.getString("transactionId"));
				boolean selfApprovalFlag = transactionResponse.has("isSelfApproved") && transactionResponse.getString("isSelfApproved").equals("true");
				result.addParam("isSelfApprovalFlag", Boolean.toString(selfApprovalFlag));
			} else if(TransactionStatusEnum.APPROVED.getStatus().equals(transactionResponse.optString("status"))) {
				result = invokeEditPayeeAfterApproval(request, result, editType, payloadForValidateForApprovals);
			}
			String actionType = "";
			if(PayeeConstants.LINKAGE_EDIT.equals(editType)) {
				actionType = "EDIT_LINKAGE";
				if (!addedCifsMap.isEmpty() || !removedCifsMap.isEmpty()) {
					PayeeUtils.updateLinkedUnlinkedContractCifs(transactionResponse, addedCifsMap, removedCifsMap);
				}
			} else if(PayeeConstants.OPTIONAL_FIELD_EDIT.equals(editType)) {
				actionType = "EDIT_OPTIONAL";
			}
			transactionResponse.put("className", InternationalPayeeResourceImpl.class.getName());
			transactionResponse.put("associatedCifMap", editedCifsMap);
			PayeeUtils.callPushEvent(result, request, response, actionType, transactionResponse);
		} catch ( ApplicationException e ) {
			return e.getErrorCodeEnum().setErrorCode(result);
		}
		return result;
	}

	private Result invokeEditPayeeAfterApproval( DataControllerRequest request, Result result, String editType, TransactionStatusDTO payloadForValidateForApprovals ) {
		if(PayeeConstants.LINKAGE_EDIT.equals(editType)) {
			result = editPayeeLinkageAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo());
		} else if(PayeeConstants.OPTIONAL_FIELD_EDIT.equals(editType)) {
			result = editPayeeOptionalFieldsAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo());
		}
		return result;
	}

	@Override
	public Result editPayeeOptionalFieldsAfterApproval( DataControllerRequest request, JSONObject inputObject ) {
		InternationalPayeeBackendDTO internationalPayeeBackendDTO = null;
		try {
			 try{
				 Object cifMapping = inputObject.get("cif");
				 String cifMappingstr = cifMapping.toString();
				 inputObject.remove("cif");
				 inputObject.put("cif", cifMappingstr);
				 }catch (Exception e) {
				 alert.prepareError("Invalid meta data stored in the db for requestId: ").log();
				 }
			internationalPayeeBackendDTO = JSONUtils.parse(inputObject.toString(), InternationalPayeeBackendDTO.class);
		} catch ( IOException e ) {
			// TODO Added proper error message
			throw new RuntimeException(e);
		}
		internationalPayeeBackendDTO = payeeBackendDelegate.editPayee(internationalPayeeBackendDTO, request.getHeaderMap(), request);
		if(internationalPayeeBackendDTO == null) {
			alert.prepareError("Error occured while updating payee at backend").log();
			return ErrorCodeEnum.ERR_12055.setErrorCode(new Result());
		}

		Result result = new Result();
		if(internationalPayeeBackendDTO.getDbpErrMsg() != null && !internationalPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalPayeeBackendDTO.getDbpErrMsg());
		}
		try {
			JSONObject requestObj = new JSONObject(internationalPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch ( JSONException e ) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
	@Override
	public Result editPayeeLinkageAfterApproval( DataControllerRequest request, JSONObject inputObject ) {
		//Edit backend payee details
		//deleting mappings from dbx table which are present in removedCifsMap
		Map<String, Map<String, List<String>>> addedAndRemovedContractCifMaps = PayeeUtils.getAddedAndRemovedContractCifMaps(inputObject);
		Map<String, List<String>> removedCifsMap = addedAndRemovedContractCifMaps.get(PayeeConstants.REMOVED_CONTRACT_CIF_MAP_KEY);
		InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
		String legalEntityId;
			Map<String, Object> customer = CustomerSession.getCustomerMap(request);
			legalEntityId = (String) customer.get("legalEntityId");
			internationalPayeeDTO.setPayeeId(inputObject.getString(PayeeConstants.ID_KEY));
			if(StringUtils.isNotBlank(legalEntityId))
			internationalPayeeDTO.setLegalEntityId(legalEntityId);
			for ( Map.Entry<String, List<String>> contractCif : removedCifsMap.entrySet() ) {
			internationalPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for ( int j = 0; j < coreCustomerIds.size(); j++ ) {
				internationalPayeeDTO.setCif(coreCustomerIds.get(j));
				payeeDelegate.deletePayeeAtDBX(internationalPayeeDTO);
			}
		}

		//adding mappings at dbx table which are present in addedCifsMap
		String createdBy = inputObject.getString("userId");
		internationalPayeeDTO.setCreatedBy(createdBy);
		Map<String, List<String>> addedCifsMap = addedAndRemovedContractCifMaps.get(PayeeConstants.ADDED_CONTRACT_CIF_MAP_KEY);
		for ( Map.Entry<String, List<String>> contractCif : addedCifsMap.entrySet() ) {
			internationalPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for ( int j = 0; j < coreCustomerIds.size(); j++ ) {
				internationalPayeeDTO.setCif(coreCustomerIds.get(j));
				payeeDelegate.createPayeeAtDBX(internationalPayeeDTO);
			}
		}

		Result result = new Result();
		try {
			JSONObject requestObj = inputObject;
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
    
    /**
     * This method deletes an International payee
     *
     * @param methodId   contains the operation id
     * @param inputArray contains the input parameter to delete an existing Inter bank payee
     * @param request    contains request handler
     * @param response   contains the response handler
     * @return Result object contains acknowledgement for deleted payee
     * @author KH2544
     * @version 1.0
     */
    @Override
	public Result deletePayee(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String legalEntityId = (String) customer.get("legalEntityId");
		//String userId = CustomerSession.getCustomerId(customer);
		
		String payeeId = null;
		if(inputParams.get("payeeId") != null) {
			payeeId = inputParams.get("payeeId").toString();
		}
		
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		try {
			boolean payeeStatusInPending = payeeDelegate.checkIfPayeeStatusInPending(request, payeeId);
			if(payeeStatusInPending) {
				return ErrorCodeEnum.ERR_12613.setErrorCode(new Result());
			}
		} catch ( ApplicationException e ) {
			return ErrorCodeEnum.ERR_12612.setErrorCode(new Result());
		}
		// Authorization check for cifs for which the payee is deleted
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT), request.getHeaderMap(), request);
		if(authorizedCifs == null || authorizedCifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		//Checking if payee exists with that payeeId or not
		InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
		internationalPayeeDTO.setPayeeId(payeeId);
		internationalPayeeDTO.setLegalEntityId(legalEntityId);
		List<InternationalPayeeDTO> internationalPayeeDTOs = payeeDelegate.fetchPayeeByIdAtDBX(internationalPayeeDTO);
		if(internationalPayeeDTOs == null) {
			alert.prepareError("Failed to fetch payee").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		
		if(internationalPayeeDTOs.isEmpty()) {
			alert.prepareError("Records doesn't exist").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}
		Set<String> payeeIds = new HashSet<String>();
		payeeIds.add(payeeId);
		//Fetching backend records using payeeId
		List<InternationalPayeeBackendDTO> backendDTOs = payeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
		if(backendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from backend").log();
			return ErrorCodeEnum.ERR_12058.setErrorCode(new Result());
		}

		if(backendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}

		if(backendDTOs.get(0).getDbpErrMsg() != null && !backendDTOs.get(0).getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, backendDTOs.get(0).getDbpErrMsg());
		}
		//Need to uncomment if we have to check permission for all the cifs associated with that payeeId
		/*
		Map<String, List<String>> removedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> addedCifsMap = new HashMap<String, List<String>>();
		_setAddedAndRemovedCifMaps(intraBankPayeeDTOs, deletedCifsMap, addedCifsMap, removedCifsMap);
		if(!addedCifsMap.isEmpty() || removedCifsMap.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		*/
		
		Set<String> cifs = internationalPayeeDTOs.stream().map(InternationalPayeeDTO::getCif).collect(Collectors.toSet());
		cifs.retainAll(authorizedCifs);
		if(cifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		InternationalPayeeBackendDTO oldInternationalPayeeBackendDTO = backendDTOs.get(0);
		//Deleting payee at the backend
		InternationalPayeeBackendDTO newInternationalPayeeBackendDTO = new InternationalPayeeBackendDTO();
		try {
			newInternationalPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), InternationalPayeeBackendDTO.class);
			newInternationalPayeeBackendDTO.setUserId(internationalPayeeDTOs.get(0).getCreatedBy());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		TransactionStatusDTO payloadForValidateForApprovals = PayeeUtils.getPayloadForValidateForApprovalsForDeletePayee(newInternationalPayeeBackendDTO, oldInternationalPayeeBackendDTO, internationalPayeeDTOs);
		JSONObject transactionResponse;
		try {
			transactionResponse = payeeDelegate.validateForApprovals(request, payloadForValidateForApprovals);
			if(transactionResponse == null) {
				throw new ApplicationException(ErrorCodeEnum.ERR_26024);
			} else if(StringUtils.isNotBlank(transactionResponse.optString("dbpErrCode")) || StringUtils.isNotBlank(transactionResponse.optString("dbpErrMsg"))) {
				result.addParam(new Param(DBPConstants.DBP_ERROR_CODE_KEY, transactionResponse.get("dbpErrCode").toString(), MWConstants.INT));
				result.addParam(new Param(DBPConstants.DBP_ERROR_MESSAGE_KEY, transactionResponse.getString("dbpErrMsg"), MWConstants.STRING));
				result.addParam(new Param(DBPConstants.FABRIC_OPSTATUS_KEY, "-1", MWConstants.INT));
				result.addParam(new Param(DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY, "400", MWConstants.INT));
			} else if( TransactionStatusEnum.SENT.getStatus().equals(transactionResponse.optString("status"))) {
				result = deletePayeeAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo());
			} else if( TransactionStatusEnum.PENDING.getStatus().equals(transactionResponse.optString("status"))){
				result.addParam("transactionStatus", TransactionStatusEnum.PENDING.getStatus());
				result.addParam("referenceId", transactionResponse.getString("transactionId"));
				boolean selfApprovalFlag = transactionResponse.has("isSelfApproved") && transactionResponse.getString("isSelfApproved").equals("true");
				result.addParam("isSelfApprovalFlag", Boolean.toString(selfApprovalFlag));
			} else if(TransactionStatusEnum.APPROVED.getStatus().equals(transactionResponse.optString("status"))) {
				result = deletePayeeAfterApproval(request, payloadForValidateForApprovals.getAdditionalMetaInfo());
			}
			transactionResponse.put("className", InternationalPayeeResourceImpl.class.getName());
			transactionResponse.put("associatedCifMap", cifs);
			PayeeUtils.callPushEvent(result, request, response, "DELETE", transactionResponse);
		} catch ( ApplicationException e ) {
			return e.getErrorCodeEnum().setErrorCode(result);
		}

		return result;
	}

	@Override
	public Result deletePayeeAfterApproval( DataControllerRequest request, JSONObject deletePayeeObject ) {
		@SuppressWarnings("unchecked")
		Result result = new Result();

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String legalEntityId = (String) customer.get("legalEntityId");
		//String userId = CustomerSession.getCustomerId(customer);

		String payeeId = null;
		if(deletePayeeObject.get("payeeId") != null) {
			payeeId = deletePayeeObject.get("payeeId").toString();
		}

		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		InternationalPayeeBackendDTO internationalPayeeBackendDTO;
		try {
			internationalPayeeBackendDTO = JSONUtils.parse(deletePayeeObject.toString(), InternationalPayeeBackendDTO.class);
			internationalPayeeBackendDTO.setUserId(internationalPayeeBackendDTO.getUserId());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		internationalPayeeBackendDTO = payeeBackendDelegate.deletePayee(internationalPayeeBackendDTO, request.getHeaderMap(), request);
		if(internationalPayeeBackendDTO == null) {
			alert.prepareError("Error occured while deleting payee at backend").log();
			return ErrorCodeEnum.ERR_12056.setErrorCode(new Result());
		}
		
		if(internationalPayeeBackendDTO.getDbpErrMsg() != null && !internationalPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalPayeeBackendDTO.getDbpErrMsg());
		}
		
		internationalPayeeBackendDTO.setId(payeeId);

		InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
		internationalPayeeDTO.setPayeeId(payeeId);
		internationalPayeeDTO.setLegalEntityId(legalEntityId);

		//Deleting payee at dbx
		payeeDelegate.deletePayeeAtDBX(internationalPayeeDTO);
		
		//need to uncomment if we want to delete partially based on authorization
		/*
		for (Map.Entry<String,List<String>> contractCif : removedCifsMap.entrySet()) {
			internationalPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
				internationalPayeeDTO.setCif(coreCustomerIds.get(j));
				payeeDelegate.deletePayeeAtDBX(internationalPayeeDTO);
			}
    	}
    	*/ 
		
		try {
			JSONObject requestObj = new JSONObject(internationalPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
    

    /**
     * This method fetches all International payees for a user or organization
     *
     * @param methodId   contains the operation id
     * @param inputArray contains the input parameter to fetch all Inter bank payee
     * @param request    contains request handler
     * @param response   contains the response handler
     * @return Result object contains a list of all the payees
     * @author KH2544
     * @version 1.0
     */
    @Override
	public Result fetchAllMyPayees(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String legalEntityId = (String) customer.get("legalEntityId");
		//TODO - Fetch cif of logged user 
		//String userId = CustomerSession.getCustomerId(customer);

		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT);
		//To get cifs which are authorized for the user
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(featureActions, request.getHeaderMap(), request);
		if(authorizedCifs == null || authorizedCifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		//To fetch payees for authorized cifs
		List<InternationalPayeeDTO> internationalPayeeDTOs = payeeDelegate.fetchPayeesFromDBX(authorizedCifs,legalEntityId);
		if(internationalPayeeDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from dbx ").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		if(internationalPayeeDTOs.isEmpty()) {
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.EXTERNALACCOUNT, new JSONArray()).toString());
		}
		
		//Getting unique payees with comma seperated cif list
		internationalPayeeDTOs = _getUniquePayees(internationalPayeeDTOs);
		
		//Getting a list of unique payeeIds
		Set<String> payeeIds = internationalPayeeDTOs.stream().map(InternationalPayeeDTO::getPayeeId).collect(Collectors.toSet());
				
				
		//Fetching backend records using payeeId
		List<InternationalPayeeBackendDTO> internationalPayeeBackendDTOs = payeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
		if(internationalPayeeBackendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from backend").log();
			return ErrorCodeEnum.ERR_12058.setErrorCode(new Result());
		}
		
		if(internationalPayeeBackendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.EXTERNALACCOUNT, new JSONArray()).toString());
        }			

		if(internationalPayeeBackendDTOs.get(0).getDbpErrMsg() != null && !(internationalPayeeBackendDTOs.get(0).getDbpErrMsg()).isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalPayeeBackendDTOs.get(0).getDbpErrMsg());
		}
		
		//Merge bankend payees with dbx payees using Id and payeeId fields and populating cif information from dbx payee
		internationalPayeeBackendDTOs = (new FilterDTO()).merge(internationalPayeeBackendDTOs, internationalPayeeDTOs, "id=payeeId", "cif");
		if(internationalPayeeBackendDTOs == null) {
			alert.prepareError("Error occurred while merging payee details").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		//Applying filters like offset,limit,sort etc
		FilterDTO filterDTO = new FilterDTO();
		try {
			filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
		}
		internationalPayeeBackendDTOs = filterDTO.filter(internationalPayeeBackendDTOs);
				
		try {
			JSONArray resArray = new JSONArray(internationalPayeeBackendDTOs);
            JSONObject resultObj = new JSONObject();
            resultObj.put(Constants.EXTERNALACCOUNT,resArray);
            result = JSONToResult.convert(resultObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while defining resources for fetch all templates", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
    }
    
    /*
	 * Method to set added and removed cifs into map with contract and cif information by comparing with already existing cifs which are fetched from dbx table
	 */
	private void _setAddedAndRemovedCifMaps(Map<String, List<String>> inputCifsMap, Map<String, List<String>> addedCifsMap, 
			Map<String, List<String>> removedCifsMap, Map<String, List<String>> associatedCifsMap) {
		
		for (Map.Entry<String,List<String>> contractCif : inputCifsMap.entrySet())  {
			if(associatedCifsMap.containsKey(contractCif.getKey()))
			{
				List<String> associatedCifs = associatedCifsMap.get(contractCif.getKey());
				List<String> inputCifs = contractCif.getValue();
				
				List<String> addedCifs = new ArrayList<>(inputCifs);
				addedCifs.removeAll(associatedCifs);
				if(addedCifs.size()> 0) {
					addedCifsMap.put(contractCif.getKey(), addedCifs);
				}
				
				List<String> removedCifs = new ArrayList<>(associatedCifs);
				removedCifs.removeAll(inputCifs);
				if(removedCifs.size()> 0) {
					removedCifsMap.put(contractCif.getKey(), removedCifs);
				}
				
			}
			else {
				addedCifsMap.put(contractCif.getKey(), contractCif.getValue());
			}
    	}
		
		for (Map.Entry<String,List<String>> associatedcontractCif : associatedCifsMap.entrySet()) {
            if(!(inputCifsMap.containsKey(associatedcontractCif.getKey()))) {
                removedCifsMap.put(associatedcontractCif.getKey(),associatedcontractCif.getValue());
            }           
        }
		
	}
	
	/*
	 * Method to set added and removed cifs into map with contract and cif information by comparing with already existing cifs which are fetched from dbx table
	 */
	private Map<String, List<String>> _getAssociatedCifsMap(List<InternationalPayeeDTO> payeeDTOs, DataControllerRequest request) {
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT), request.getHeaderMap(), request);
		Map<String, List<String>> associatedCifsMap = new HashMap<String, List<String>>();
		for(int i = 0; i < payeeDTOs.size(); i++) {
			if(!authorizedCifs.contains(payeeDTOs.get(i).getCif())) {
				continue;
			}
			if(associatedCifsMap.containsKey(payeeDTOs.get(i).getContractId())) {
				List<String> cifs = new ArrayList<>(associatedCifsMap.get(payeeDTOs.get(i).getContractId()));
				cifs.add(payeeDTOs.get(i).getCif());
				associatedCifsMap.put(payeeDTOs.get(i).getContractId(), cifs);
			}
			else {
				associatedCifsMap.put(payeeDTOs.get(i).getContractId(), Arrays.asList(payeeDTOs.get(i).getCif().split(",")));
			}
		}
		
		return associatedCifsMap;
	}
   	
   	/*
   	 * Method to get contract cif list map from input cifs
   	 */
   	private Map<String, List<String>> _getContractCifMap(String inputCifs) {
   		Map<String, List<String>> contractCifMap = new HashMap<String, List<String>>();
           /*
            "sharedCifs": [
           	{
               "contractId": "1324",
               "coreCustomerId":  "1,2"
           	},
           	{
               "contractId": "1234",
               "coreCustomerId":  "3,4"
           	}
   			]
            */
           JsonParser jsonParser = new JsonParser();
           JsonArray contractCifArray = (JsonArray) jsonParser.parse(inputCifs);
           Type type = new TypeToken<Map<String, String>>() {}.getType();
           Gson gson = new Gson();
           if (contractCifArray.isJsonArray()) {
               for (int i = 0; i < contractCifArray.size(); i++) {
                   @SuppressWarnings("unchecked")
                   Map<String, String> contractObject = (Map<String, String>) gson.fromJson(contractCifArray.get(i), type);
                   if (StringUtils.isNotBlank(contractObject.get("coreCustomerId"))) {
	             	  List<String> coreCustomerIds = Arrays.asList(contractObject.get("coreCustomerId").split(","));
	             	  contractCifMap.put(contractObject.get("contractId"), coreCustomerIds);
                   }
               }
           }
           return contractCifMap;
           /*result will be
            {
            "1324": ["1","2"],
            "1234": ["3","4"]
            }
            */
   	}
   	
    /*
	 * Method to get unique payees based on payeeId and setting comma seperated cif for shared payees
	 */
	private List<InternationalPayeeDTO> _getUniquePayees(List<InternationalPayeeDTO> internationalPayeeDTOs) {
		Map<String, HashMap<String,String>> payeeMap = new HashMap<>();
		
		for(InternationalPayeeDTO payee: internationalPayeeDTOs){
			if(payeeMap.containsKey(payee.getPayeeId())) {
				HashMap<String,String> cifMap = payeeMap.get(payee.getPayeeId());
				String cifIds = payee.getCif();
				if(cifMap.containsKey(payee.getContractId())) {
					cifIds = cifMap.get(payee.getContractId()) + "," + payee.getCif();
				}
				cifMap.put(payee.getContractId(), cifIds);
				payeeMap.put(payee.getPayeeId(), cifMap);
			}
			else {
				HashMap<String,String> cifMap = new HashMap<String,String>();
				cifMap.put(payee.getContractId(), payee.getCif());
				payeeMap.put(payee.getPayeeId(), cifMap);
			}
		}
		
		for(int i = 0; i < internationalPayeeDTOs.size(); i++){
			InternationalPayeeDTO payee = internationalPayeeDTOs.get(i);
			if(payeeMap.containsKey(payee.getPayeeId())) {
				HashMap<String,String> cifMap = payeeMap.get(payee.getPayeeId());
				JSONArray cifArray = new JSONArray();
				int noOfCustomersLinked = 0;
				for (Map.Entry<String,String> entry : cifMap.entrySet()) {
					JSONObject cifObj = new JSONObject();
					cifObj.put("contractId", entry.getKey());
					cifObj.put("coreCustomerId", entry.getValue());
					noOfCustomersLinked += entry.getValue().split(",").length;
					cifArray.put(cifObj);
				}
				payee.setCif(cifArray.toString());
				payee.setNoOfCustomersLinked(String.valueOf(noOfCustomersLinked));
				payeeMap.remove(payee.getPayeeId());
			}
			else {
				internationalPayeeDTOs.remove(i);
				i--;
			}
		}
		
		return internationalPayeeDTOs;
	}
	
	/*
	 * Method to check if payee details are unique for given cifs
	 */
	private boolean _isUniquePayee(DataControllerRequest request, InternationalPayeeBackendDTO inputDTO, Map<String, List<String>> cifMap) {
		Set<String> inputCifs = new HashSet<>();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String legalEntityId = (String) customer.get("legalEntityId");
		for(Map.Entry<String, List<String>> map : cifMap.entrySet()){
			inputCifs.addAll(map.getValue());
		}
		
		List<InternationalPayeeDTO> payeeDTOs = payeeDelegate.fetchPayeesFromDBX(inputCifs,legalEntityId);
		if(payeeDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from dbx ").log();
			return false;
		}
		if(payeeDTOs.isEmpty()) {
			alert.prepareError("No Payees Found").log();
	        return true;
		}
		
		Set<String> payeeIds = payeeDTOs.stream().map(InternationalPayeeDTO::getPayeeId).distinct().collect(Collectors.toSet());
		
		List<InternationalPayeeBackendDTO> backendDTOs = payeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
		if(backendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from backend").log();
			return false;
		}
		
		if(backendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
	        return true;
        }
		
		if(backendDTOs.get(0).getDbpErrMsg() != null && !backendDTOs.get(0).getDbpErrMsg().isEmpty()) {
			return false;
		}
		
		String inputIBAN = inputDTO.getIban();
		String inputAccountNumber = inputDTO.getAccountNumber();
		String inputSwiftCode = inputDTO.getSwiftCode();
		for(int i = 0; i < backendDTOs.size(); i++) {
			InternationalPayeeBackendDTO backendDTO = backendDTOs.get(i);
			String IBAN = backendDTO.getIban();
			String accountNumber = backendDTO.getAccountNumber();
			String swiftCode = backendDTO.getSwiftCode();
			if(backendDTO.getIsApproved().equalsIgnoreCase("1")) {
				if ((StringUtils.isBlank(inputIBAN) || inputIBAN.equals(IBAN))
						&& inputSwiftCode.equals(swiftCode) && (StringUtils.isBlank(inputAccountNumber) || inputAccountNumber.equals(accountNumber))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean _isUniquePayeeForEdit(InternationalPayeeBackendDTO inputDTO, InternationalPayeeBackendDTO backendDTO, 
			Map<String, List<String>> addedCifsMap, Map<String, List<String>> associatedCifsMap, 
			Map<String, List<String>> editedCifsMap, String editedCifs, DataControllerRequest request) {
		boolean validationFieldsChanged = false;
		
		String backendIban = backendDTO.getIban();
		if(StringUtils.isBlank(inputDTO.getIban()) || inputDTO.getIban().equals(backendIban)) {
			inputDTO.setIban(backendIban);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendAccountNumber = backendDTO.getAccountNumber();
		if(StringUtils.isBlank(inputDTO.getAccountNumber()) || inputDTO.getAccountNumber().equals(backendAccountNumber)) {
			inputDTO.setAccountNumber(backendAccountNumber);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendSwiftCode = backendDTO.getSwiftCode();
		if(StringUtils.isBlank(inputDTO.getSwiftCode()) || inputDTO.getSwiftCode().equals(backendSwiftCode)) {
			inputDTO.setSwiftCode(backendSwiftCode);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		if(validationFieldsChanged) {
			if(StringUtils.isBlank(editedCifs)) {
				if(!associatedCifsMap.isEmpty() && !_isUniquePayee(request, inputDTO, associatedCifsMap))
				{
					return false;
				}
			}
			else {
				if(!editedCifsMap.isEmpty() && !_isUniquePayee(request, inputDTO, editedCifsMap))
				{
					return false;
				}
			}
			
		}
		else {
			if(StringUtils.isNotBlank(editedCifs)) {
				if(!addedCifsMap.isEmpty()  && !_isUniquePayee(request, inputDTO, addedCifsMap))
				{
					return false;
				}
			}
		}
		
		return true;
	}
}

