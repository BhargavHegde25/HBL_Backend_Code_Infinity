package com.temenos.dbx.product.payeeservices.resource.impl;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import com.temenos.dbx.product.payeeservices.backenddelegate.api.P2PPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.P2PPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeDTO;
import com.temenos.dbx.product.payeeservices.resource.api.P2PPayeeResource;

public class P2PPayeeResourceImpl implements P2PPayeeResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

	P2PPayeeBusinessDelegate payeeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PPayeeBusinessDelegate.class);
	P2PPayeeBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(P2PPayeeBackendDelegate.class);


 
    /**
     * This method edits details of an existing P2P payee
     *
     * @param methodId   contains the operation id
     * @param inputArray contains the input parameter to edit a new P2P payee
     * @param request    contains request handler
     * @param response   contains the response handler
     * @return Result object contains edited payeeId
     * @author KH2661
     * @version 1.0
     */
	@Override
    public Result editPayee(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();

		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String customerId = CustomerSession.getCustomerId(customer);
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String legalEntityId = (String) customer.get("legalEntityId");
		String payeeId =  (String) inputParams.get("PayPersonId");
		if(StringUtils.isBlank(payeeId)) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		
		Map<String, List<String>> editedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> removedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> addedCifsMap = new HashMap<String, List<String>>();
		Map<String, List<String>> associatedCifsMap = new HashMap<String, List<String>>();
		String editedCifs = (String) inputParams.get("cif");
		if(StringUtils.isBlank(editedCifs)) {
			inputParams.put("cif", "{}");
			editedCifsMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(Arrays.asList(FeatureAction.P2P_CREATE_RECEPIENT), request.getHeaderMap(), request);
			if(editedCifsMap == null || editedCifsMap.isEmpty()) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		else {
			editedCifsMap = _getContractCifMap(editedCifs);
			//Authorization check for cifs which are edited
			if(!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(Arrays.asList(FeatureAction.P2P_CREATE_RECEPIENT), editedCifsMap, request.getHeaderMap(), request)) {
				alert.prepareError("The logged in user doesn't have permission to perform this action").log();
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		}
		
		//Fetch payees using payeeId from dbx
		P2PPayeeDTO p2pPayeeDTO = new P2PPayeeDTO();
		p2pPayeeDTO.setPayeeId(payeeId);
		p2pPayeeDTO.setLegalEntityId(legalEntityId);
		List<P2PPayeeDTO> p2pPayPayeeDTOs = payeeBusinessDelegate.fetchPayeeByIdAtDBX(p2pPayeeDTO);
		
		if(p2pPayPayeeDTOs == null) {
			alert.prepareError("Failed to fetch payee").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		
		if(p2pPayPayeeDTOs.isEmpty()) {
			alert.prepareError("Records doesn't exist").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}
				
		associatedCifsMap = _getAssociatedCifsMap(p2pPayPayeeDTOs, request);
		if(StringUtils.isNotBlank(editedCifs)) {
			_setAddedAndRemovedCifMaps(editedCifsMap, addedCifsMap, removedCifsMap, associatedCifsMap);
			if(addedCifsMap.isEmpty() && removedCifsMap.isEmpty()) {
				editedCifs = null;
			}
		}
		
		//String Id= p2pPayeeDTO.getId();
		String createdBy = p2pPayPayeeDTOs.get(0).getCreatedBy();
		P2PPayeeBackendDTO p2pPayeeBackendDTO = new P2PPayeeBackendDTO();
		try {
			p2pPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PPayeeBackendDTO.class);
			p2pPayeeBackendDTO.setUserId(createdBy);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(!p2pPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}
		
		inputParams.values().removeAll(Collections.singleton(null));
		if(inputParams.size() > 2) {
			//Edit backend payee details
			p2pPayeeBackendDTO = payeeBackendDelegate.editPayee(p2pPayeeBackendDTO, request.getHeaderMap(), request);
			if(p2pPayeeBackendDTO == null) {
				alert.prepareError("Error occured while updating payee at backend").log();
				return ErrorCodeEnum.ERR_12055.setErrorCode(new Result());
			}
			
			if(p2pPayeeBackendDTO.getDbpErrMsg() != null && !p2pPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
				return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pPayeeBackendDTO.getDbpErrMsg());
			}
		}
	
		//deleting mappings from dbx table which are present in removedCifsMap
		for (Map.Entry<String,List<String>> contractCif : removedCifsMap.entrySet()) {
			p2pPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	p2pPayeeDTO.setCif(coreCustomerIds.get(j));
            	payeeBusinessDelegate.deletePayeeAtDBX(p2pPayeeDTO);
			}
    	} 
		
		//adding mappings at dbx table which are present in addedCifsMap
		p2pPayeeDTO.setCreatedBy(createdBy);
		for (Map.Entry<String,List<String>> contractCif : addedCifsMap.entrySet()) {
			p2pPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	p2pPayeeDTO.setCif(coreCustomerIds.get(j));
            	payeeBusinessDelegate.createPayeeAtDBX(p2pPayeeDTO);
			}
    	} 
		
		/*if(!payeeBusinessDelegate.editPayeeAtDBX(Id)) {
			alert.prepareError("Error occured while updating p2p payee").log();
		}*/
		
		try {
			JSONObject requestObj = new JSONObject(p2pPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;
    }

    /**
     * This method deletes an P2P payee
     *
     * @param methodId   contains the operation id
     * @param inputArray contains the input parameter to delete an existing p2p payee
     * @param request    contains request handler
     * @param response   contains the response handler
     * @return Result object contains acknowledgement for deleted payee
     * @author KH2544
     * @version 1.0
     */
    @Override
    public Result deletePayee(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) {
    	@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		//Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		//String customerId = CustomerSession.getCustomerId(customer);
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String legalEntityId = (String) customer.get("legalEntityId");
		String payeeId = null;
		
		if(inputParams.get("PayPersonId") != null) {
			payeeId = inputParams.get("PayPersonId").toString();
		}
		
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("Missing payeeId").log();
			return ErrorCodeEnum.ERR_12054.setErrorCode(new Result());
		}
		
		// Authorization check for cifs for which the PayPersonId is deleted
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(FeatureAction.P2P_DELETE_RECEPIENT), request.getHeaderMap(), request);
		if(authorizedCifs == null || authorizedCifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
										   
		}
		
		//Checking if PayPerson exists with that payeeId or not
		P2PPayeeDTO p2pPayeeDTO = new P2PPayeeDTO();
		p2pPayeeDTO.setPayeeId(payeeId);
		p2pPayeeDTO.setLegalEntityId(legalEntityId);
		List<P2PPayeeDTO> p2pPayeeDTOs = payeeBusinessDelegate.fetchPayeeByIdAtDBX(p2pPayeeDTO);

		if(p2pPayeeDTOs == null) {
			alert.prepareError("Failed to fetch payee").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
	
		}
		
		if(p2pPayeeDTOs.isEmpty()) {
			alert.prepareError("Records doesn't exist").log();
			return ErrorCodeEnum.ERR_12606.setErrorCode(new Result());
		}
		
		Set<String> cifs = p2pPayeeDTOs.stream().map(P2PPayeeDTO::getCif).collect(Collectors.toSet());
		cifs.retainAll(authorizedCifs);
		if(cifs.isEmpty()) {
			alert.prepareError("The logged in user doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
		
		//Deleting payee at the backend	
		P2PPayeeBackendDTO p2pPayeeBackendDTO = new P2PPayeeBackendDTO();
		try {
			p2pPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PPayeeBackendDTO.class);
			p2pPayeeBackendDTO.setUserId(p2pPayeeDTOs.get(0).getCreatedBy());
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		p2pPayeeBackendDTO = payeeBackendDelegate.deletePayee(p2pPayeeBackendDTO, request.getHeaderMap(), request);
		if(p2pPayeeBackendDTO == null) {
			alert.prepareError("Error occured while deleting payee at backend").log();
			return ErrorCodeEnum.ERR_12056.setErrorCode(new Result());
		}
		
		if(p2pPayeeBackendDTO.getDbpErrMsg() != null && !p2pPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pPayeeBackendDTO.getDbpErrMsg());
		}
		
		if(!payeeBusinessDelegate.deletePayeeAtDBX(p2pPayeeDTO)) {
			alert.prepareError("Error occured while deleting p2p payee").log();
		}
		
		try {
			JSONObject requestObj = new JSONObject(p2pPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
			
		return result;
    }


     

    /**
     * This method fetches all P2P payees for a user or organization
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
			//TODO - Fetch cif of logged user 
		//String customerId = CustomerSession.getCustomerId(customer);
		String legalEntityId = (String) customer.get("legalEntityId");
		
		//AuthorizationCheck
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.P2P_VIEW_RECEPIENT);
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(featureActions, request.getHeaderMap(), request);
		if(authorizedCifs == null || authorizedCifs.isEmpty()) {
			alert.prepareError("The logged in user is doesn't have permission to perform this action").log();
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
			
		//To fetch payees for authorized cifs
		List<P2PPayeeDTO> p2pPayeeDTOs = payeeBusinessDelegate.fetchPayeesFromDBX(authorizedCifs, legalEntityId);
		if(p2pPayeeDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from dbx ").log();
			return ErrorCodeEnum.ERR_12057.setErrorCode(new Result());
		}
		if(p2pPayeeDTOs.isEmpty()) {
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.PAYPERSON_RECORDS, new JSONArray()).toString());
		}
		
		//Getting unique payees with comma seperated cif list
		p2pPayeeDTOs = _getUniquePayees(p2pPayeeDTOs);
		
		//Getting a list of unique payeeIds
		Set<String> payeeIds = p2pPayeeDTOs.stream().map(P2PPayeeDTO::getPayeeId).collect(Collectors.toSet());
		
		//Fetching backend records using payeeId
		List<P2PPayeeBackendDTO> p2pPayeeBackendDTOs = payeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(), request);
		if(p2pPayeeBackendDTOs == null) {
			alert.prepareError("Error occurred while fetching payees from backend").log();
			return ErrorCodeEnum.ERR_12058.setErrorCode(new Result());
		}
		
		if(p2pPayeeBackendDTOs.size() == 0){
			alert.prepareError("No Payees Found").log();
	        return JSONToResult.convert(new JSONObject().put(Constants.PAYPERSON_RECORDS, new JSONArray()).toString());
        }
		
		if(p2pPayeeBackendDTOs.get(0).getDbpErrMsg() != null && !p2pPayeeBackendDTOs.get(0).getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pPayeeBackendDTOs.get(0).getDbpErrMsg());
		}
		
		//Merge bankend payees with dbx payees using Id and payeeId fields and populatinf cif information from dbx payee
		p2pPayeeBackendDTOs = (new FilterDTO()).merge(p2pPayeeBackendDTOs, p2pPayeeDTOs, "id=payeeId", "cif");
		if(p2pPayeeBackendDTOs == null) {
			alert.prepareError("Error occurred while merging payee details").log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		//Applying filters like offset,limit,sort etc
		FilterDTO filterDTO = new FilterDTO();
		try {
			filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
		} catch (IOException e) {
			alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		p2pPayeeBackendDTOs = filterDTO.filter(p2pPayeeBackendDTOs);

		try {
			JSONArray resArray = new JSONArray(p2pPayeeBackendDTOs);
            JSONObject resultObj = new JSONObject();
            resultObj.put(Constants.PAYPERSON_RECORDS,resArray);
            result = JSONToResult.convert(resultObj.toString());
		}
		catch(Exception exp) {
			alert.prepareError("Error occurred while defining resources for fetch all templates", exp).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
    }
    

	@Override
	public Result createPayee(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
		Result result = new Result();
		
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String customerId = CustomerSession.getCustomerId(customer);
		String legalEntityId = 	(String) customer.get("legalEntityId");
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.P2P_CREATE_RECEPIENT);		
		
		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get("cif");
		
		if((StringUtils.isBlank((String) inputParams.get("email")) && (!inputParams.get("phone").equals(inputParams.get("primaryContactForSending"))))
				|| (StringUtils.isBlank((String) inputParams.get("phone")) && (!inputParams.get("email").equals(inputParams.get("primaryContactForSending"))))) {
			alert.prepareError("primaryContactForSending is different from phone or email").log();
			return ErrorCodeEnum.ERR_10159.setErrorCode(new Result());
		}
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
		
		P2PPayeeBackendDTO p2pPayeeBackendDTO = new P2PPayeeBackendDTO();
		try {
			p2pPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PPayeeBackendDTO.class);
			p2pPayeeBackendDTO.setUserId(customerId);
		} catch (IOException e) {
			alert.prepareError("Error occured while fetching the input params: " + e).log();
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}
		
		if(!p2pPayeeBackendDTO.isValidInput()) {
			alert.prepareError("Not a valid input").log();
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}
		
		if(StringUtils.isBlank(p2pPayeeBackendDTO.getName()) || StringUtils.isBlank(p2pPayeeBackendDTO.getPrimaryContactForSending())) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		
		P2PPayeeBackendDTO p2pPayeeBackendResDTO = payeeBackendDelegate.createPayee(p2pPayeeBackendDTO, request.getHeaderMap(), request);
		if(p2pPayeeBackendResDTO == null) {
			alert.prepareError("Error occured while creating payee at backend").log();
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}
		
		if(p2pPayeeBackendResDTO.getDbpErrMsg() != null && !p2pPayeeBackendResDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pPayeeBackendResDTO.getDbpErrMsg());
		}
		
		String payeeId = p2pPayeeBackendResDTO.getId();
		if(payeeId == null || payeeId.isEmpty()) {
			alert.prepareError("PayeeId is empty or null").log();
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}
		
		P2PPayeeDTO p2pPayeeDTO = new P2PPayeeDTO();
		p2pPayeeDTO.setPayeeId(payeeId);
		p2pPayeeDTO.setCreatedBy(customerId);
		p2pPayeeDTO.setLegalEntityId(legalEntityId);
		
		//Creating payee and cif mappings at dbx table
		for (Map.Entry<String,List<String>> contractCif : sharedCifMap.entrySet())  {
			p2pPayeeDTO.setContractId((String) contractCif.getKey());
            List<String> coreCustomerIds = contractCif.getValue(); 
            for(int j = 0; j < coreCustomerIds.size(); j++) {
            	p2pPayeeDTO.setCif(coreCustomerIds.get(j));
            	payeeBusinessDelegate.createPayeeAtDBX(p2pPayeeDTO);
			}
    	} 
		
		try {
			JSONObject requestObj = new JSONObject(p2pPayeeBackendResDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			alert.prepareError("Error occured while converting the response to Result: ", e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		
		return result;

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
	private Map<String, List<String>> _getAssociatedCifsMap(List<P2PPayeeDTO> payeeDTOs, DataControllerRequest request) {
		Set<String> authorizedCifs = authorizationChecksBusinessDelegate.getUserAuthorizedCifsForFeatureAction(Arrays.asList(FeatureAction.P2P_CREATE_RECEPIENT), request.getHeaderMap(), request);
		
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
	 * Method to get unique payees based on payeeId and setting comma seperated cif for shared payees
	 */
	private List<P2PPayeeDTO> _getUniquePayees(List<P2PPayeeDTO> p2pPayeeDTOs) {
		Map<String, HashMap<String,String>> payeeMap = new HashMap<>();
		
		for(P2PPayeeDTO payee: p2pPayeeDTOs){
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
		
		for(int i = 0; i < p2pPayeeDTOs.size(); i++){
			P2PPayeeDTO payee = p2pPayeeDTOs.get(i);
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
				p2pPayeeDTOs.remove(i);
				i--;
			}
		}
		
		return p2pPayeeDTOs;
	}

}

