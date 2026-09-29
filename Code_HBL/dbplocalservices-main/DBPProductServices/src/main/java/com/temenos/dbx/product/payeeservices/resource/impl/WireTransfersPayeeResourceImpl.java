package com.temenos.dbx.product.payeeservices.resource.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
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
import com.temenos.dbx.product.payeeservices.backenddelegate.api.WireTransfersPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.WireTransfersPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.WireTransfersPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.WireTransfersPayeeDTO;
import com.temenos.dbx.product.payeeservices.resource.api.WireTransfersPayeeResource;
import com.temenos.dbx.product.transactionservices.businessdelegate.api.BulkWiresBusinessDelegate;

public class WireTransfersPayeeResourceImpl implements WireTransfersPayeeResource {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	WireTransfersPayeeBusinessDelegate wireTransfersPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(WireTransfersPayeeBusinessDelegate.class);
	
	WireTransfersPayeeBackendDelegate wireTransfersPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(WireTransfersPayeeBackendDelegate.class);

	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	
	@Override
	public Result fetchAllMyPayees(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();

		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//TODO - Fetch cif of logged user 
		//String userId = CustomerSession.getCustomerId(customer);

		//To get cifs which are authorized for the user
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.DOMESTIC_WIRE_TRANSFER_VIEW_RECEPIENT);
		Set<String> domesticAuthorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(featureActions, request.getHeaderMap(), request);
		featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTERNATIONAL_WIRE_TRANSFER_VIEW_RECEPIENT);
		Set<String> internationalAuthorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(featureActions, request.getHeaderMap(), request);
		
		if((domesticAuthorizedCifs == null || domesticAuthorizedCifs.isEmpty()) && 
				(internationalAuthorizedCifs == null || internationalAuthorizedCifs.isEmpty())) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		//To fetch domestic payees for authorized cifs
		List<WireTransfersPayeeDTO> domesticWirePayeeDTOs = new ArrayList<WireTransfersPayeeDTO>();
		if(!domesticAuthorizedCifs.isEmpty()) {
			domesticWirePayeeDTOs = wireTransfersPayeeDelegate.fetchPayeesFromDBX(domesticAuthorizedCifs, Constants.TYPE_ID_DOMESTIC);
			if(domesticWirePayeeDTOs == null) {
				alert.prepareError("Error occurred while fetching payees from dbx ").log();
				return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
			}
		}
		
		//To fetch international payees for authorized cifs
		List<WireTransfersPayeeDTO> internationalWirePayeeDTOs = new ArrayList<WireTransfersPayeeDTO>();
		if(!internationalAuthorizedCifs.isEmpty()) {
			internationalWirePayeeDTOs = wireTransfersPayeeDelegate.fetchPayeesFromDBX(internationalAuthorizedCifs, Constants.TYPE_ID_INTERNATIONAL);
			if(internationalWirePayeeDTOs == null) {
				alert.prepareError("Error occurred while fetching payees from dbx ").log();
				return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
			}
		}
		
		List<WireTransfersPayeeDTO> wireTransfersPayeeDTOs = new ArrayList<WireTransfersPayeeDTO>();
		wireTransfersPayeeDTOs.addAll(domesticWirePayeeDTOs);
		wireTransfersPayeeDTOs.addAll(internationalWirePayeeDTOs);
		
		if(wireTransfersPayeeDTOs.isEmpty()) {
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.PAYEE, new JSONArray()).toString());
		}
		
		//Getting unique payees with comma seperated cif list
		wireTransfersPayeeDTOs = _getUniquePayees(wireTransfersPayeeDTOs);
		
		//Getting a list of unique payeeIds
		Set<String> payeeIds = wireTransfersPayeeDTOs.stream().map(WireTransfersPayeeDTO::getPayeeId).collect(Collectors.toSet());
		
		//Fetching backend records using payeeId
		List<WireTransfersPayeeBackendDTO> wireTransfersPayeeBackendDTOs = wireTransfersPayeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
		if(wireTransfersPayeeBackendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from backend").log();
			return ErrorCodeEnum.ERR_12058.setErrorCode(new Result());
		}
		
		if(wireTransfersPayeeBackendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.PAYEE, new JSONArray()).toString());
        }
		
		if(wireTransfersPayeeBackendDTOs.get(0).getDbpErrMsg() != null && !wireTransfersPayeeBackendDTOs.get(0).getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, wireTransfersPayeeBackendDTOs.get(0).getDbpErrMsg());
		}
		
		//Merge bankend payees with dbx payees using Id and payeeId fields and populating cif information from dbx payee
		wireTransfersPayeeBackendDTOs = (new FilterDTO()).merge(wireTransfersPayeeBackendDTOs, wireTransfersPayeeDTOs, "id=payeeId", "cif");
		if(wireTransfersPayeeBackendDTOs == null) {
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
		wireTransfersPayeeBackendDTOs = filterDTO.filter(wireTransfersPayeeBackendDTOs);
		
		try {
	        JSONArray records = new JSONArray(wireTransfersPayeeBackendDTOs);
	        JSONObject resultObject = new JSONObject();
	        resultObject.put(Constants.PAYEE,records);
	        result = JSONToResult.convert(resultObject.toString());
		}
        catch(Exception exp) {
            alert.prepareError("Exception occurred while converting DTO to result: ",exp).log();
            return ErrorCodeEnum.ERR_12048.setErrorCode(new Result());
        }
		
		return result;
	}
	
	@Override
	public Result createPayee(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
		
		String typeId = null;
		String wireAccountType = null;
		if(inputParams.get("wireAccountType") != null) {
			wireAccountType = inputParams.get("wireAccountType").toString();
		}
		
		List<String> featureActions = new ArrayList<String>();
		if(Constants.INTERNATIONAL.equals(wireAccountType)) {
			featureActions.add(FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE_RECEPIENT);
			typeId = Constants.TYPE_ID_INTERNATIONAL;
		}
		else if(Constants.DOMESTIC.equals(wireAccountType)) {
			featureActions.add(FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE_RECEPIENT);
			typeId = Constants.TYPE_ID_DOMESTIC;
		}
		else{
			alert.prepareError("The logged in user is doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get("cif");
		if(StringUtils.isBlank(sharedCifs)) {
			sharedCifMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(featureActions, request.getHeaderMap(), request);
			if(sharedCifMap == null || sharedCifMap.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		else {
			sharedCifMap = _getContractCifMap(sharedCifs);
					
			//Authorization for all the cifs present in input for which the payee needs to be shared
			if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(featureActions, sharedCifMap, request.getHeaderMap(), request)) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		
		WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO = new WireTransfersPayeeBackendDTO();
		try {
			wireTransfersPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), WireTransfersPayeeBackendDTO.class);
			wireTransfersPayeeBackendDTO.setUserId(userId);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(!wireTransfersPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}
		
		if(StringUtils.isBlank(wireTransfersPayeeBackendDTO.getName()) || StringUtils.isBlank(wireTransfersPayeeBackendDTO.getTypeId())
				|| wireTransfersPayeeBackendDTO.getAccountNumber() == null) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		if(Constants.INTERNATIONAL.equals(wireAccountType)) {
			if(StringUtils.isBlank(wireTransfersPayeeBackendDTO.getSwiftCode()) || (StringUtils.isBlank(wireTransfersPayeeBackendDTO.getInternationalRoutingCode()) && StringUtils.isBlank(wireTransfersPayeeBackendDTO.getIban()))) {
				return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
			}
		}
		else if(Constants.DOMESTIC.equals(wireAccountType)) {
			if(StringUtils.isBlank(wireTransfersPayeeBackendDTO.getRoutingCode())) {
				return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
			}
		}
		
		if(!_isUniquePayee(request, wireTransfersPayeeBackendDTO, sharedCifMap, typeId))
		{
			alert.prepareError("Duplicate Payee or Error occured while checking for uniqueness.").log();
			return ErrorCodeEnum.ERR_12062.setErrorCode(new Result());
		}
		
		//Create one payee at the backend
		wireTransfersPayeeBackendDTO = wireTransfersPayeeBackendDelegate.createPayee(wireTransfersPayeeBackendDTO, request.getHeaderMap(), request);
		if(wireTransfersPayeeBackendDTO == null) {
			alert.prepareError("Error occured while creating payee at backend").log();
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}
		
		if(wireTransfersPayeeBackendDTO.getDbpErrMsg() != null && !wireTransfersPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, wireTransfersPayeeBackendDTO.getDbpErrMsg());
		}
		
		//Getting payeeId of created payee
		String payeeId = wireTransfersPayeeBackendDTO.getId();
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("PayeeId is empty or null").log();
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}
		
		WireTransfersPayeeDTO wireTransfersPayeeDTO = new WireTransfersPayeeDTO();
		wireTransfersPayeeDTO.setPayeeId(payeeId);
		wireTransfersPayeeDTO.setCreatedBy(userId);
		wireTransfersPayeeDTO.setTypeId(typeId);
		
		//Creating payee and cif mappings at dbx table
		for (Map.Entry<String,List<String>> contractCif : sharedCifMap.entrySet())  {
			wireTransfersPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	wireTransfersPayeeDTO.setCif(coreCustomerIds.get(j));
				wireTransfersPayeeDelegate.createPayeeAtDBX(wireTransfersPayeeDTO);
			}
    	} 
	
		try {
			JSONObject requestObj = new JSONObject(wireTransfersPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
	
	@Override
	public Result editPayee(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String userId = CustomerSession.getCustomerId(customer);
		
		String payeeId = null;
		if(inputParams.get("payeeId") != null) {
			payeeId = inputParams.get("payeeId").toString();
		}
		
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		
		//Fetch payees using payeeId from dbx
		WireTransfersPayeeDTO wireTransfersPayeeDTO = new WireTransfersPayeeDTO();
		wireTransfersPayeeDTO.setPayeeId(payeeId);
		List<WireTransfersPayeeDTO> wireTransfersPayeeDTOs = wireTransfersPayeeDelegate.fetchPayeeByIdAtDBX(wireTransfersPayeeDTO);
		if(wireTransfersPayeeDTOs == null) {
			alert.prepareError("Failed to fetch payee").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		
		if(wireTransfersPayeeDTOs.isEmpty()) {
			alert.prepareError("Records doesn't exist").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}
		
		Set<String> payeeIds = new HashSet<String>();
		payeeIds.add(payeeId);
		//Fetching backend records using payeeId
		List<WireTransfersPayeeBackendDTO> backendDTOs = wireTransfersPayeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
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
		
		String typeId = wireTransfersPayeeDTOs.get(0).getTypeId();
		String featureAction = null;
		if(Constants.TYPE_ID_INTERNATIONAL.equals(typeId)) {
			featureAction = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE_RECEPIENT; 
		}
		else if(Constants.TYPE_ID_DOMESTIC.equals(typeId)) {
			featureAction = FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE_RECEPIENT; 
		}
		else {
			alert.prepareError("The logged in user is doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		Map<String, List<String>> editedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> removedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> addedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> associatedCifsMap = new HashMap<String, List<String>>();
		String editedCifs = (String) inputParams.get("cif");
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(featureAction), request.getHeaderMap(), request);
		if(StringUtils.isBlank(editedCifs)) {
			inputParams.put("cif", "{}");
			editedCifsMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(Arrays.asList(featureAction), request.getHeaderMap(), request);
			if(editedCifsMap == null || editedCifsMap.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		else {
			editedCifsMap = _getContractCifMap(editedCifs);
			// Authorization check for cifs for which the payee is deleted
			if(authorizedCifs == null || authorizedCifs.isEmpty() || editedCifsMap.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			Set<String> cifs = new HashSet<String>();
			for(Map.Entry<String, List<String>> map : editedCifsMap.entrySet()){
				cifs.addAll(map.getValue());
			}
			cifs.retainAll(authorizedCifs);
			if(cifs.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		
		associatedCifsMap = _getAssociatedCifsMap(wireTransfersPayeeDTOs, request);
		if(StringUtils.isNotBlank(editedCifs)) {
			_setAddedAndRemovedCifMaps(editedCifsMap, addedCifsMap, removedCifsMap, associatedCifsMap);
			if(addedCifsMap.isEmpty() && removedCifsMap.isEmpty()) {
				editedCifs = null;
			}
		}
		
		String createdBy = wireTransfersPayeeDTOs.get(0).getCreatedBy();
		WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO = new WireTransfersPayeeBackendDTO();
		try {
			wireTransfersPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), WireTransfersPayeeBackendDTO.class);
			wireTransfersPayeeBackendDTO.setUserId(createdBy);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(!wireTransfersPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}
		
		if(!_isUniquePayeeForEdit(wireTransfersPayeeBackendDTO, backendDTOs.get(0), addedCifsMap, associatedCifsMap, editedCifsMap, editedCifs, typeId, request)) {
			alert.prepareError("Duplicate Payee or Error occured while checking for uniqueness.").log();
			return ErrorCodeEnum.ERR_12062.setErrorCode(new Result());
		}
		
		inputParams.values().removeAll(Collections.singleton(null));
		if(inputParams.size() > 2) {
			//Edit backend payee details
			wireTransfersPayeeBackendDTO = wireTransfersPayeeBackendDelegate.editPayee(wireTransfersPayeeBackendDTO, request.getHeaderMap(), request);
			if(wireTransfersPayeeBackendDTO == null) {
				alert.prepareError("Error occured while updating payee at backend").log();
				return ErrorCodeEnum.ERR_12055.setErrorCode(new Result());
			}
			
			if(wireTransfersPayeeBackendDTO.getDbpErrMsg() != null && !wireTransfersPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, wireTransfersPayeeBackendDTO.getDbpErrMsg());
			}
		}
		
		//deleting mappings from dbx table which are present in removedCifsMap
		for (Map.Entry<String,List<String>> contractCif : removedCifsMap.entrySet()) {
			wireTransfersPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	wireTransfersPayeeDTO.setCif(coreCustomerIds.get(j));
				wireTransfersPayeeDelegate.deletePayeeAtDBX(wireTransfersPayeeDTO);
			}
    	} 
		
		//adding mappings at dbx table which are present in addedCifsMap
		wireTransfersPayeeDTO.setCreatedBy(createdBy);
		for (Map.Entry<String,List<String>> contractCif : addedCifsMap.entrySet()) {
			wireTransfersPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	wireTransfersPayeeDTO.setCif(coreCustomerIds.get(j));
				wireTransfersPayeeDelegate.createPayeeAtDBX(wireTransfersPayeeDTO);
			}
    	}
		
		try {
			JSONObject requestObj = new JSONObject(wireTransfersPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
	}
	
	@Override
	public Result deletePayee(String methodID, Object[] inputArray, DataControllerRequest request, 
			DataControllerResponse response) {
		
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String userId = CustomerSession.getCustomerId(customer);
		
		String payeeId = null;
		if(inputParams.get("payeeId") != null) {
			payeeId = inputParams.get("payeeId").toString();
		}
		
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		
		//Checking if payee exists with that payeeId or not
		WireTransfersPayeeDTO wireTransfersPayeeDTO = new WireTransfersPayeeDTO();
		wireTransfersPayeeDTO.setPayeeId(payeeId);
		List<WireTransfersPayeeDTO> wireTransfersPayeeDTOs = wireTransfersPayeeDelegate.fetchPayeeByIdAtDBX(wireTransfersPayeeDTO);
		if(wireTransfersPayeeDTOs == null) {
			alert.prepareError("Failed to fetch payee").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		
		if(wireTransfersPayeeDTOs.isEmpty()) {
			alert.prepareError("Records doesn't exist").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}

		String typeId = wireTransfersPayeeDTOs.get(0).getTypeId();
		String featureAction = null;
		if(Constants.TYPE_ID_INTERNATIONAL.equals(typeId)) {
			featureAction = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_DELETE_RECEPIENT; 
		}
		else if(Constants.TYPE_ID_DOMESTIC.equals(typeId)) {
			featureAction = FeatureAction.DOMESTIC_WIRE_TRANSFER_DELETE_RECEPIENT; 
		}
		else {
			alert.prepareError("The logged in user is doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		// Authorization check for cifs for which the payee is deleted
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(featureAction), request.getHeaderMap(), request);
		if(authorizedCifs == null || authorizedCifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		Set<String> cifs = wireTransfersPayeeDTOs.stream().map(WireTransfersPayeeDTO::getCif).collect(Collectors.toSet());
		cifs.retainAll(authorizedCifs);
		if(cifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		//Deleting payee at the backend
		WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO = new WireTransfersPayeeBackendDTO();
		try {
			wireTransfersPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), WireTransfersPayeeBackendDTO.class);
			wireTransfersPayeeBackendDTO.setUserId(wireTransfersPayeeDTOs.get(0).getCreatedBy());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		wireTransfersPayeeBackendDTO = wireTransfersPayeeBackendDelegate.deletePayee(wireTransfersPayeeBackendDTO, request.getHeaderMap(), request);
		if(wireTransfersPayeeBackendDTO == null) {
			alert.prepareError("Error occured while deleting payee at backend").log();
			return ErrorCodeEnum.ERR_12056.setErrorCode(new Result());
		}
		
		if(wireTransfersPayeeBackendDTO.getDbpErrMsg() != null && !wireTransfersPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, wireTransfersPayeeBackendDTO.getDbpErrMsg());
		}
		
		//Deleting payee at dbx
		wireTransfersPayeeDelegate.deletePayeeAtDBX(wireTransfersPayeeDTO);
		
		try {
			JSONObject requestObj = new JSONObject(wireTransfersPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		String userId = wireTransfersPayeeDTOs.get(0).getCreatedBy();
		BulkWiresBusinessDelegate bulkWiresBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BulkWiresBusinessDelegate.class);
		//Need to recheck
		bulkWiresBusinessDelegate.UpdateBulkWireTemplateRecipientCount(userId, null, payeeId);

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
	private Map<String, List<String>> _getAssociatedCifsMap(List<WireTransfersPayeeDTO> payeeDTOs, DataControllerRequest request) {
		String typeId = payeeDTOs.get(0).getTypeId();
		String featureAction;
		if(Constants.TYPE_ID_INTERNATIONAL.equals(typeId)) {
			featureAction = FeatureAction.INTERNATIONAL_WIRE_TRANSFER_CREATE_RECEPIENT; 
		}
		else {
			featureAction = FeatureAction.DOMESTIC_WIRE_TRANSFER_CREATE_RECEPIENT; 
		}
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(featureAction), request.getHeaderMap(), request);
		
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
	private List<WireTransfersPayeeDTO> _getUniquePayees(List<WireTransfersPayeeDTO> wireTransfersPayeeDTOs) {
		Map<String, HashMap<String,String>> payeeMap = new HashMap<>();
		
		for(WireTransfersPayeeDTO payee: wireTransfersPayeeDTOs){
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
		
		for(int i = 0; i < wireTransfersPayeeDTOs.size(); i++){
			WireTransfersPayeeDTO payee = wireTransfersPayeeDTOs.get(i);
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
				wireTransfersPayeeDTOs.remove(i);
				i--;
			}
		}
		
		return wireTransfersPayeeDTOs;
	}
	
	/*
	 * Method to check if payee details are unique for given cifs
	 */
	private boolean _isUniquePayee(DataControllerRequest request, WireTransfersPayeeBackendDTO inputDTO, Map<String, List<String>> cifMap, String wireAccountType) {
		Set<String> inputCifs = new HashSet<>();
		for(Map.Entry<String, List<String>> map : cifMap.entrySet()){
			inputCifs.addAll(map.getValue());
		}
		
		List<WireTransfersPayeeDTO> payeeDTOs = wireTransfersPayeeDelegate.fetchPayeesFromDBX(inputCifs, wireAccountType);
		if(payeeDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from dbx ").log();
			return false;
		}
		if(payeeDTOs.isEmpty()) {
			alert.prepareError("No Payees Found").log();
	        return true;
		}
		
		Set<String> payeeIds = payeeDTOs.stream().map(WireTransfersPayeeDTO::getPayeeId).distinct().collect(Collectors.toSet());
		
		List<WireTransfersPayeeBackendDTO> backendDTOs = wireTransfersPayeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
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
		
		String inputAccountNumber = inputDTO.getAccountNumber();
		String inputType = inputDTO.getTypeId();
		String inputRoutingCode = inputDTO.getRoutingCode();
		String inputSwiftCode = inputDTO.getSwiftCode();
		String inputInternationalRoutingCode = inputDTO.getInternationalRoutingCode();
		String inputIban = inputDTO.getIban();
		
		for(int i = 0; i < backendDTOs.size(); i++) {
			WireTransfersPayeeBackendDTO backendDTO = backendDTOs.get(i);
			String accountNumber = backendDTO.getAccountNumber();
			String type = backendDTO.getTypeId();
			String routingCode = backendDTO.getRoutingCode();
			String swiftCode = backendDTO.getSwiftCode();
			String internationalRoutingCode = backendDTO.getInternationalRoutingCode();
			String iban = backendDTO.getIban();
			if(inputAccountNumber.equals(accountNumber) && inputType.equals(type)) {
				if(wireAccountType.equals(Constants.TYPE_ID_INTERNATIONAL) && inputSwiftCode.equals(swiftCode) && 
						(StringUtils.isNotBlank(inputInternationalRoutingCode) && inputInternationalRoutingCode.equals(internationalRoutingCode)) && (StringUtils.isNotBlank(inputIban) && inputIban.equals(iban))) {
					return false;
				}
				else if(wireAccountType.equals(Constants.TYPE_ID_DOMESTIC) && inputRoutingCode.equals(routingCode)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	private boolean _isUniquePayeeForEdit(WireTransfersPayeeBackendDTO inputDTO, WireTransfersPayeeBackendDTO backendDTO, 
			Map<String, List<String>> addedCifsMap, Map<String, List<String>> associatedCifsMap, 
			Map<String, List<String>> editedCifsMap, String editedCifs, String typeId, DataControllerRequest request) {
		boolean validationFieldsChanged = false;
		
		String backendAccountNumber = backendDTO.getAccountNumber();
		if(StringUtils.isBlank(inputDTO.getAccountNumber()) || inputDTO.getAccountNumber().equals(backendAccountNumber)) {
			inputDTO.setAccountNumber(backendAccountNumber);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendSwiftCode = backendDTO.getSwiftCode();
		if(typeId.equals(Constants.TYPE_ID_DOMESTIC) || StringUtils.isBlank(inputDTO.getSwiftCode()) || inputDTO.getSwiftCode().equals(backendSwiftCode)) {
			inputDTO.setSwiftCode(backendSwiftCode);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendRoutingCode = backendDTO.getRoutingCode();
		if(typeId.equals(Constants.TYPE_ID_INTERNATIONAL) || StringUtils.isBlank(inputDTO.getRoutingCode()) || inputDTO.getRoutingCode().equals(backendRoutingCode)) {
			inputDTO.setRoutingCode(backendRoutingCode);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendInternationalRoutingCode = backendDTO.getInternationalRoutingCode();
		if(typeId.equals(Constants.TYPE_ID_DOMESTIC) || StringUtils.isBlank(inputDTO.getInternationalRoutingCode()) || inputDTO.getInternationalRoutingCode().equals(backendInternationalRoutingCode)) {
			inputDTO.setInternationalRoutingCode(backendInternationalRoutingCode);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendIban = backendDTO.getIban();
		if(typeId.equals(Constants.TYPE_ID_DOMESTIC) || StringUtils.isBlank(inputDTO.getIban()) || inputDTO.getIban().equals(backendIban)) {
			inputDTO.setIban(backendIban);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		String backendType = backendDTO.getTypeId();
		if(StringUtils.isBlank(inputDTO.getTypeId()) || inputDTO.getTypeId().equals(backendType)) {
			inputDTO.setTypeId(backendType);
			validationFieldsChanged = validationFieldsChanged || false;
		}
		else {
			validationFieldsChanged = validationFieldsChanged || true;
		}
		
		if(validationFieldsChanged) {
			if(StringUtils.isBlank(editedCifs)) {
				if(!associatedCifsMap.isEmpty() && !_isUniquePayee(request, inputDTO, associatedCifsMap, typeId))
				{
					return false;
				}
			}
			else {
				if(!editedCifsMap.isEmpty() && !_isUniquePayee(request, inputDTO, editedCifsMap, typeId))
				{
					return false;
				}
			}
			
		}
		else {
			if(StringUtils.isNotBlank(editedCifs)) {
				if(!addedCifsMap.isEmpty()  && !_isUniquePayee(request, inputDTO, addedCifsMap, typeId))
				{
					return false;
				}
			}
		}
		
		return true;
	}

}
