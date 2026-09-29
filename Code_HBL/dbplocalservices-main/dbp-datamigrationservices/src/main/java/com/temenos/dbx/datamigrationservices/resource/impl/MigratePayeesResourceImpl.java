package com.temenos.dbx.datamigrationservices.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.backend.api.MigratePayeesBackendDelegate;
import com.temenos.dbx.datamigrationservices.javaservice.CreateBillPayPayee;
import com.temenos.dbx.datamigrationservices.resource.api.MigratePayeesResource;
import com.temenos.dbx.datamigrationservices.utils.DataFetchUtils;
import com.temenos.dbx.datamigrationservices.utils.MigrationUtils;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.BillPayPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.BillPayPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.P2PPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeDTO;

public class MigratePayeesResourceImpl implements MigratePayeesResource {
	
	private static final Logger logger = LogManager.getLogger(CreateBillPayPayee.class);
	
	BillPayPayeeBusinessDelegate billPayPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(BillPayPayeeBusinessDelegate.class);

	BillPayPayeeBackendDelegate billPayPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(BillPayPayeeBackendDelegate.class);

	AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BusinessDelegateFactory.class)
			.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	
	P2PPayeeBusinessDelegate payeeBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(P2PPayeeBusinessDelegate.class);
	MigratePayeesBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(MigratePayeesBackendDelegate.class);
	
	@Override
	@SuppressWarnings("unchecked")
	public Result createBillPayPayee(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws ApplicationException {
		Result result = new Result();

		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String legalEntityId = (String) inputParams.get("legalEntityId");
		String customerId = (String) inputParams.get("userId");
		String accountNumber = (String) inputParams.get("accountId");
		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		inputParams.put("accountNumber", accountNumber);
		
		if (StringUtils.isBlank(legalEntityId) || StringUtils.isBlank(customerId)) {
			logger.error("user data is missing");
			return ErrorCodeEnum.ERR_10159.setErrorCode(new Result());
		}
		
		String sharedCifs = MigrationUtils.getInfinityUserContractCustomerDetails(customerId, request.getHeaderMap());
		
		if (StringUtils.isBlank(sharedCifs)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}else {
			sharedCifMap = MigrationUtils.getContractCifMap(sharedCifs);
		}
		logger.error("*********** inputParams :"+inputParams);
		
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.BILL_PAY_CREATE_PAYEES);
		
		boolean isAuthorized = DataFetchUtils.isUserAuthorizedForFeatureAction(featureActions, sharedCifMap, request.getHeaderMap(),
				request);
		if (!isAuthorized) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result, "The given User is not authorized to perform this action");
		}

		BillPayPayeeBackendDTO billPayPayeeBackendDTO = new BillPayPayeeBackendDTO();
		try {
			billPayPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), BillPayPayeeBackendDTO.class);
			billPayPayeeBackendDTO.setUserId(customerId);
		} catch (IOException e) {
			logger.error("Error occured while fetching the input params: " + e);
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		if (!billPayPayeeBackendDTO.isValidInput()) {
			logger.error("Not a valid input");
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(billPayPayeeBackendDTO.getName())) {
			billPayPayeeBackendDTO.setName(billPayPayeeBackendDTO.getCompanyName());
		}

		if (StringUtils.isBlank(billPayPayeeBackendDTO.getName())
				|| billPayPayeeBackendDTO.getAccountNumber() == null) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}

		if (!_isUniquePayee(request, billPayPayeeBackendDTO, sharedCifMap, legalEntityId)) {
			logger.error("Duplicate Payee or Error occured while checking for uniqueness.");
			return ErrorCodeEnum.ERR_12062.setErrorCode(new Result());
		}

		// Create one payee at the backend
		billPayPayeeBackendDTO = billPayPayeeBackendDelegate.createPayee(billPayPayeeBackendDTO, request.getHeaderMap(),
				request);
		if (billPayPayeeBackendDTO == null) {
			logger.error("Error occured while creating payee at backend");
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}

		if (billPayPayeeBackendDTO.getDbpErrMsg() != null && !billPayPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, billPayPayeeBackendDTO.getDbpErrMsg());
		}

		// Getting payeeId of created payee
		String payeeId = billPayPayeeBackendDTO.getId();
		if (payeeId == null || payeeId.isEmpty()) {
			logger.error("PayeeId is empty or null");
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}

		BillPayPayeeDTO billPayPayeeDTO = new BillPayPayeeDTO();
		billPayPayeeDTO.setPayeeId(payeeId);
		billPayPayeeDTO.setCreatedBy(customerId);
		billPayPayeeDTO.setLegalEntityId(legalEntityId);
		// Creating payee and cif mappings at dbx table
		for (Map.Entry<String, List<String>> contractCif : sharedCifMap.entrySet()) {
			billPayPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for (int j = 0; j < coreCustomerIds.size(); j++) {
				billPayPayeeDTO.setCif(coreCustomerIds.get(j));
				billPayPayeeDelegate.createPayeeAtDBX(billPayPayeeDTO);
			}
		}

		try {
			JSONObject requestObj = new JSONObject(billPayPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			logger.error("Error occured while converting the response to Result: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	/*
	 * Method to check if payee details are unique for given cifs
	 */
	private boolean _isUniquePayee(DataControllerRequest request, BillPayPayeeBackendDTO inputDTO,
			Map<String, List<String>> cifMap, String legalEntityId) {

		if (StringUtils.isBlank(inputDTO.getAccountNumber())) {
			return true;
		}

		Set<String> inputCifs = new HashSet<>();
		for (Map.Entry<String, List<String>> map : cifMap.entrySet()) {
			inputCifs.addAll(map.getValue());
		}

		List<BillPayPayeeDTO> payeeDTOs = billPayPayeeDelegate.fetchPayeesFromDBX(inputCifs, legalEntityId);
		if (payeeDTOs == null) {
			logger.error("Error occurred while fetching payees from dbx ");
			return false;
		}
		if (payeeDTOs.isEmpty()) {
			logger.error("No Payees Found");
			return true;
		}

		Set<String> payeeIds = payeeDTOs.stream().map(BillPayPayeeDTO::getPayeeId).distinct()
				.collect(Collectors.toSet());

		List<BillPayPayeeBackendDTO> backendDTOs = billPayPayeeBackendDelegate.fetchPayees(payeeIds,
				request.getHeaderMap(), request);
		if (backendDTOs == null) {
			logger.error("Error occurred while fetching payees from backend");
			return false;
		}

		if (backendDTOs.size() == 0) {
			logger.error("No Payees Found");
			return true;
		}

		if (backendDTOs.get(0).getDbpErrMsg() != null && !backendDTOs.get(0).getDbpErrMsg().isEmpty()) {
			return false;
		}

		String inputAccountNumber = inputDTO.getAccountNumber();
		for (int i = 0; i < backendDTOs.size(); i++) {
			if (inputAccountNumber.equals(backendDTOs.get(i).getAccountNumber())) {
				return false;
			}
		}

		return true;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Result createP2PPayee(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Result result = new Result();

		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

		String legalEntityId = (String) inputParams.get("legalEntityId");
		String customerId = (String) inputParams.get("userId");
		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();

		if ((StringUtils.isBlank((String) inputParams.get("email")) && (!inputParams.get("phone").equals(inputParams.get("primaryContactForSending"))))
				|| (StringUtils.isBlank((String) inputParams.get("phone")) && (!inputParams.get("email").equals(inputParams.get("primaryContactForSending"))))) {
			logger.error("primaryContactForSending is different from phone or email");
			return ErrorCodeEnum.ERR_10159.setErrorCode(new Result());
		}
		
		if (StringUtils.isBlank(legalEntityId) || StringUtils.isBlank(customerId)) {
			logger.error("user data is missing");
			return ErrorCodeEnum.ERR_10159.setErrorCode(new Result());
		}
		
		String sharedCifs = MigrationUtils.getInfinityUserContractCustomerDetails(customerId, request.getHeaderMap());
		
		if (StringUtils.isBlank(sharedCifs)) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}else {
			sharedCifMap = MigrationUtils.getContractCifMap(sharedCifs);
		} 
		
		logger.error("*********** inputParams :"+inputParams);
		
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.P2P_CREATE);
		
		boolean isAuthorized = DataFetchUtils.isUserAuthorizedForFeatureAction(featureActions, sharedCifMap, request.getHeaderMap(),
				request);
		if (!isAuthorized) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(result, "The given User is not authorized to perform this action");
		}

		P2PPayeeBackendDTO p2pPayeeBackendDTO = new P2PPayeeBackendDTO();
		try {
			p2pPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), P2PPayeeBackendDTO.class);
			p2pPayeeBackendDTO.setUserId(customerId);
		} catch (IOException e) {
			logger.error("Error occured while fetching the input params: " + e);
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		if (!p2pPayeeBackendDTO.isValidInput()) {
			logger.error("Not a valid input");
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(p2pPayeeBackendDTO.getName())
				|| StringUtils.isBlank(p2pPayeeBackendDTO.getPrimaryContactForSending())) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}

		
		logger.error("*********** p2pPayeeBackendDTO :"+new JSONObject(p2pPayeeBackendDTO));
		
		P2PPayeeBackendDTO p2pPayeeBackendResDTO = payeeBackendDelegate.createPayee(p2pPayeeBackendDTO, request.getHeaderMap(), request);
		if (p2pPayeeBackendResDTO == null) {
			logger.error("Error occured while creating payee at backend");
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}

		if (p2pPayeeBackendResDTO.getDbpErrMsg() != null && !p2pPayeeBackendResDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, p2pPayeeBackendResDTO.getDbpErrMsg());
		}

		String payeeId = p2pPayeeBackendResDTO.getId();
		if (payeeId == null || payeeId.isEmpty()) {
			logger.error("PayeeId is empty or null");
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}

		P2PPayeeDTO p2pPayeeDTO = new P2PPayeeDTO();
		p2pPayeeDTO.setPayeeId(payeeId);
		p2pPayeeDTO.setCreatedBy(customerId);
		p2pPayeeDTO.setLegalEntityId(legalEntityId);

		// Creating payee and cif mappings at dbx table
		for (Map.Entry<String, List<String>> contractCif : sharedCifMap.entrySet()) {
			p2pPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for (int j = 0; j < coreCustomerIds.size(); j++) {
				p2pPayeeDTO.setCif(coreCustomerIds.get(j));
				payeeBusinessDelegate.createPayeeAtDBX(p2pPayeeDTO);
			}
		}

		try {
			JSONObject requestObj = new JSONObject(p2pPayeeBackendResDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			logger.error("Error occured while converting the response to Result: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}
		return result;
	}
	
}
