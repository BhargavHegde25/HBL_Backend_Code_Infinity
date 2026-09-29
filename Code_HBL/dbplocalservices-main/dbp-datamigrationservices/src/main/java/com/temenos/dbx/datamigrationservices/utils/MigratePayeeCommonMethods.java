package com.temenos.dbx.datamigrationservices.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.InterBankPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.InternationalPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.backenddelegate.api.IntraBankPayeeBackendDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InterBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.InternationalPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.IntraBankPayeeBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

public class MigratePayeeCommonMethods {

	private static LoggerUtil logger = new LoggerUtil(MigratePayeeCommonMethods.class);

	public static boolean isUniquePayee(DataControllerRequest request, IntraBankPayeeBackendDTO inputDTO,
			Map<String, List<String>> cifMap) {
		Set<String> inputCifs = new HashSet<>();
		String legalEntityId = request.getParameter(InfinityConstants.legalEntityId);
		for (Map.Entry<String, List<String>> map : cifMap.entrySet()) {
			inputCifs.addAll(map.getValue());
		}
		IntraBankPayeeBusinessDelegate payeeDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(IntraBankPayeeBusinessDelegate.class);
		IntraBankPayeeBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(IntraBankPayeeBackendDelegate.class);
		List<IntraBankPayeeDTO> payeeDTOs = payeeDelegate.fetchPayeesFromDBX(inputCifs, legalEntityId);
		if (payeeDTOs == null) {
			logger.error("Error occurred while fetching payees from dbx ");
			return false;
		}
		if (payeeDTOs.isEmpty()) {
			logger.error("No Payees Found");
			return true;
		}

		Set<String> payeeIds = payeeDTOs.stream().map(IntraBankPayeeDTO::getPayeeId).distinct()
				.collect(Collectors.toSet());

		List<IntraBankPayeeBackendDTO> backendDTOs = payeeBackendDelegate.fetchPayees(payeeIds, request.getHeaderMap(),
				request);
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
			if (backendDTOs.get(i).getIsApproved().equalsIgnoreCase("1")) {
				if (inputAccountNumber.equals(backendDTOs.get(i).getAccountNumber())) {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isUniquePayee(DataControllerRequest request, InterBankPayeeBackendDTO inputDTO,
			Map<String, List<String>> cifMap) {

		Set<String> inputCifs = new HashSet<>();
		String legalEntityId = request.getParameter(InfinityConstants.legalEntityId);
		InterBankPayeeBusinessDelegate interBankPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(InterBankPayeeBusinessDelegate.class);
		for (Map.Entry<String, List<String>> map : cifMap.entrySet()) {
			inputCifs.addAll(map.getValue());
		}

		List<InterBankPayeeDTO> payeeDTOs = interBankPayeeDelegate.fetchPayeesFromDBX(inputCifs, legalEntityId);
		if (payeeDTOs == null) {
			logger.error("Error occurred while fetching payees from dbx ");
			return false;
		}
		if (payeeDTOs.isEmpty()) {
			logger.error("No Payees Found");
			return true;
		}
		Set<String> payeeIds = payeeDTOs.stream().map(InterBankPayeeDTO::getPayeeId).distinct()
				.collect(Collectors.toSet());
		InterBankPayeeBackendDelegate interBankPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(InterBankPayeeBackendDelegate.class);
		List<InterBankPayeeBackendDTO> backendDTOs = interBankPayeeBackendDelegate.fetchPayees(payeeIds,
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
		String inputSwiftCode = inputDTO.getSwiftCode();
		String inputRoutingNumber = inputDTO.getRoutingNumber();
		for (int i = 0; i < backendDTOs.size(); i++) {
			InterBankPayeeBackendDTO backendDTO = backendDTOs.get(i);
			String accountNumber = backendDTO.getAccountNumber();
			String swiftCode = backendDTO.getSwiftCode();
			String routingNumber = backendDTO.getRoutingNumber();
			if (backendDTO.getIsApproved().equalsIgnoreCase("1")) {
				if (inputAccountNumber.equals(accountNumber)
						&& (StringUtils.isBlank(inputSwiftCode) || inputSwiftCode.equals(swiftCode))
						&& (StringUtils.isBlank(inputRoutingNumber) || inputRoutingNumber.equals(routingNumber))) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isUniquePayee(DataControllerRequest request, InternationalPayeeBackendDTO inputDTO,
			Map<String, List<String>> cifMap) {
		Set<String> inputCifs = new HashSet<>();
		InternationalPayeeBusinessDelegate payeeDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InternationalPayeeBusinessDelegate.class);
		InternationalPayeeBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InternationalPayeeBackendDelegate.class);

		String legalEntityId = request.getParameter(InfinityConstants.legalEntityId);
		for (Map.Entry<String, List<String>> map : cifMap.entrySet()) {
			inputCifs.addAll(map.getValue());
		}

		List<InternationalPayeeDTO> payeeDTOs = payeeDelegate.fetchPayeesFromDBX(inputCifs, legalEntityId);
		if (payeeDTOs == null) {
			logger.error("Error occurred while fetching payees from dbx ");
			return false;
		}
		if (payeeDTOs.isEmpty()) {
			logger.error("No Payees Found");
			return true;
		}

		Set<String> payeeIds = payeeDTOs.stream().map(InternationalPayeeDTO::getPayeeId).distinct()
				.collect(Collectors.toSet());

		List<InternationalPayeeBackendDTO> backendDTOs = payeeBackendDelegate.fetchPayees(payeeIds,
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

		String inputIBAN = inputDTO.getIban();
		String inputAccountNumber = inputDTO.getAccountNumber();
		String inputSwiftCode = inputDTO.getSwiftCode();
		for (int i = 0; i < backendDTOs.size(); i++) {
			InternationalPayeeBackendDTO backendDTO = backendDTOs.get(i);
			String IBAN = backendDTO.getIban();
			String accountNumber = backendDTO.getAccountNumber();
			String swiftCode = backendDTO.getSwiftCode();
			if (backendDTO.getIsApproved().equalsIgnoreCase("1")) {
				if ((StringUtils.isBlank(inputIBAN) || inputIBAN.equals(IBAN)) && inputSwiftCode.equals(swiftCode)
						&& (StringUtils.isBlank(inputAccountNumber) || inputAccountNumber.equals(accountNumber))) {
					return false;
				}
			}
		}

		return true;
	}

	public static Result createPayeeAfterApprovalInterBankPayee(DataControllerRequest request, JSONObject inputObject,
			boolean isPending) {

		Map<String, Object> inputParams = inputObject.toMap();
		String legalEntityId = "", userId = "";
		InterBankPayeeBusinessDelegate interBankPayeeDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(InterBankPayeeBusinessDelegate.class);
		InterBankPayeeBackendDelegate interBankPayeeBackendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BackendDelegateFactory.class)
				.getBackendDelegate(InterBankPayeeBackendDelegate.class);

		if (inputParams.containsKey(InfinityConstants.legalEntityId)
				&& inputParams.get(InfinityConstants.legalEntityId) != null) {
			legalEntityId = inputParams.get(InfinityConstants.legalEntityId).toString();
		} else {
			legalEntityId = request.getParameter(InfinityConstants.legalEntityId);
		}

		if (inputParams.containsKey(InfinityConstants.userId) && inputParams.get(InfinityConstants.userId) != null) {
			userId = inputParams.get(InfinityConstants.userId).toString();
		} else {
			userId = request.getParameter(InfinityConstants.userId);
		}

		InterBankPayeeBackendDTO interBankPayeeBackendDTO = new InterBankPayeeBackendDTO();

		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get(InfinityConstants.cif);
		sharedCifMap = MigrationUtils.getContractCifMap(sharedCifs);

		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT);

		try {
			interBankPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(),
					InterBankPayeeBackendDTO.class);
		} catch (IOException e) {
			logger.error("Error occured while fetching the input params: " + e);
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		if (!interBankPayeeBackendDTO.isValidInput()) {
			logger.error("Not a valid input");
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(interBankPayeeBackendDTO.getBeneficiaryName())
				|| StringUtils.isBlank(interBankPayeeBackendDTO.getAccountNumber())
				|| (StringUtils.isBlank(interBankPayeeBackendDTO.getSwiftCode())
						&& StringUtils.isBlank(interBankPayeeBackendDTO.getRoutingNumber()))) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		if (isPending)
			interBankPayeeBackendDTO.setIsApproved("0");
		// Create one payee at the backend
		interBankPayeeBackendDTO = interBankPayeeBackendDelegate.createPayee(interBankPayeeBackendDTO,
				request.getHeaderMap(), request);
		if (interBankPayeeBackendDTO == null) {
			logger.error("Error occured while creating payee at backend");
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}
		Result result = new Result();
		if (interBankPayeeBackendDTO.getDbpErrMsg() != null && !interBankPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, interBankPayeeBackendDTO.getDbpErrMsg());
		}

		// Getting payeeId of created payee
		String payeeId = interBankPayeeBackendDTO.getId();
		if (payeeId == null || payeeId.isEmpty()) {
			logger.error("PayeeId is empty or null");
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}

		InterBankPayeeDTO interBankPayeeDTO = new InterBankPayeeDTO();
		interBankPayeeDTO.setPayeeId(payeeId);
		interBankPayeeDTO.setCreatedBy(userId);

		if (StringUtils.isNotBlank(legalEntityId))
			interBankPayeeDTO.setLegalEntityId(legalEntityId);

		// Creating payee and cif mappings at dbx table
		for (Map.Entry<String, List<String>> contractCif : sharedCifMap.entrySet()) {
			interBankPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for (int j = 0; j < coreCustomerIds.size(); j++) {
				interBankPayeeDTO.setCif(coreCustomerIds.get(j));
				interBankPayeeDelegate.createPayeeAtDBX(interBankPayeeDTO);
			}
		}

		try {
			JSONObject requestObj = new JSONObject(interBankPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			logger.error("Error occured while converting the response to Result: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	public static Result createPayeeAfterApprovalInternationalPayee(DataControllerRequest request,
			JSONObject inputObject, boolean isPending) {
		Map<String, Object> inputParams = inputObject.toMap();
		String legalEntityId = "", userId = "";

		if (inputParams.containsKey(InfinityConstants.legalEntityId)
				&& inputParams.get(InfinityConstants.legalEntityId) != null) {
			legalEntityId = inputParams.get(InfinityConstants.legalEntityId).toString();
		} else {
			legalEntityId = request.getParameter(InfinityConstants.legalEntityId);
		}

		if (inputParams.containsKey(InfinityConstants.userId) && inputParams.get(InfinityConstants.userId) != null) {
			userId = inputParams.get(InfinityConstants.userId).toString();
		} else {
			userId = request.getParameter(InfinityConstants.userId);
		}

		InternationalPayeeBusinessDelegate payeeDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(InternationalPayeeBusinessDelegate.class);
		InternationalPayeeBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(InternationalPayeeBackendDelegate.class);

		InternationalPayeeBackendDTO internationalPayeeBackendDTO = new InternationalPayeeBackendDTO();
		InternationalPayeeDTO internationalPayeeDTO = new InternationalPayeeDTO();
		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get(InfinityConstants.cif);
		sharedCifMap = MigrationUtils.getContractCifMap(sharedCifs);

		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT);

		try {
			internationalPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(),
					InternationalPayeeBackendDTO.class);
		} catch (IOException e) {
			logger.error("Error occured while fetching the input params: " + e);
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		if (!internationalPayeeBackendDTO.isValidInput()) {
			logger.error("Not a valid input");
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(internationalPayeeBackendDTO.getBeneficiaryName())
				|| StringUtils.isBlank(internationalPayeeBackendDTO.getAccountNumber())
				|| (StringUtils.isBlank(internationalPayeeBackendDTO.getSwiftCode())
						&& StringUtils.isBlank(internationalPayeeBackendDTO.getRoutingNumber()))) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		if (isPending) {
			internationalPayeeBackendDTO.setIsApproved("0");
		}
		// Create one payee at the backend
		internationalPayeeBackendDTO = payeeBackendDelegate.createPayee(internationalPayeeBackendDTO,
				request.getHeaderMap(), request);
		if (internationalPayeeBackendDTO == null) {
			logger.error("Error occured while creating payee at backend");
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}
		Result result = new Result();
		if (internationalPayeeBackendDTO.getDbpErrMsg() != null
				&& !internationalPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, internationalPayeeBackendDTO.getDbpErrMsg());
		}

		// Getting payeeId of created payee
		String payeeId = internationalPayeeBackendDTO.getId();
		if (payeeId == null || payeeId.isEmpty()) {
			logger.error("PayeeId is empty or null");
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}
		internationalPayeeDTO.setPayeeId(payeeId);
		internationalPayeeDTO.setCreatedBy(userId);
		if (StringUtils.isNotBlank(legalEntityId))
			internationalPayeeDTO.setLegalEntityId(legalEntityId);

		// Creating payee and cif mappings at dbx table
		for (Map.Entry<String, List<String>> contractCif : sharedCifMap.entrySet()) {
			internationalPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for (int j = 0; j < coreCustomerIds.size(); j++) {
				internationalPayeeDTO.setCif(coreCustomerIds.get(j));
				payeeDelegate.createPayeeAtDBX(internationalPayeeDTO);
			}
		}

		try {
			JSONObject requestObj = new JSONObject(internationalPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			logger.error("Error occured while converting the response to Result: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

	public static Result createPayeeAfterApprovalIntraBankPayee(DataControllerRequest request, JSONObject inputObject,
			boolean isPending) {
		Map<String, Object> inputParams = inputObject.toMap();
		String legalEntityId = "", userId = "";
		if (inputParams.containsKey(InfinityConstants.legalEntityId)
				&& inputParams.get(InfinityConstants.legalEntityId) != null) {
			legalEntityId = inputParams.get(InfinityConstants.legalEntityId).toString();
		} else {
			legalEntityId = request.getParameter(InfinityConstants.legalEntityId);
		}

		if (inputParams.containsKey(InfinityConstants.userId) && inputParams.get(InfinityConstants.userId) != null) {
			userId = inputParams.get(InfinityConstants.userId).toString();
		} else {
			userId = request.getParameter(InfinityConstants.userId);
		}

		Map<String, List<String>> sharedCifMap = new HashMap<String, List<String>>();
		String sharedCifs = (String) inputParams.get("cif");
		List<String> featureActions = new ArrayList<String>();
		featureActions.add(FeatureAction.INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT);
		if (StringUtils.isBlank(sharedCifs)) {
			sharedCifMap = DataFetchUtils.getAuthorizedCifs(featureActions, request.getHeaderMap(), request);
			if (sharedCifMap == null || sharedCifMap.isEmpty()) {
				logger.error("The logged in user doesn't have permission to perform this action");
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
		} else {
			sharedCifMap = MigrationUtils.getContractCifMap(sharedCifs);
		}

		IntraBankPayeeBusinessDelegate payeeDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(IntraBankPayeeBusinessDelegate.class);
		IntraBankPayeeBackendDelegate payeeBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(IntraBankPayeeBackendDelegate.class);

		IntraBankPayeeBackendDTO intraBankPayeeBackendDTO = new IntraBankPayeeBackendDTO();
		try {
			intraBankPayeeBackendDTO = JSONUtils.parse(new JSONObject(inputParams).toString(),
					IntraBankPayeeBackendDTO.class);
			intraBankPayeeBackendDTO.setUserId(userId);
		} catch (IOException e) {
			logger.error("Error occured while fetching the input params: " + e);
			return ErrorCodeEnum.ERR_10549.setErrorCode(new Result());
		}

		if (!intraBankPayeeBackendDTO.isValidInput()) {
			logger.error("Not a valid input");
			return ErrorCodeEnum.ERR_10710.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(intraBankPayeeBackendDTO.getBeneficiaryName())
				|| StringUtils.isBlank(intraBankPayeeBackendDTO.getAccountNumber())) {
			return ErrorCodeEnum.ERR_10348.setErrorCode(new Result());
		}
		if (isPending)
			intraBankPayeeBackendDTO.setIsApproved("0");
		// Create one payee at the backend
		intraBankPayeeBackendDTO = payeeBackendDelegate.createPayee(intraBankPayeeBackendDTO, request.getHeaderMap(),
				request);
		if (intraBankPayeeBackendDTO == null) {
			logger.error("Error occured while creating payee at backend");
			return ErrorCodeEnum.ERR_12053.setErrorCode(new Result());
		}

		Result result = new Result();
		if (intraBankPayeeBackendDTO.getDbpErrMsg() != null && !intraBankPayeeBackendDTO.getDbpErrMsg().isEmpty()) {
			return ErrorCodeEnum.ERR_00000.setErrorCode(result, intraBankPayeeBackendDTO.getDbpErrMsg());
		}

		// Getting payeeId of created payee
		String payeeId = intraBankPayeeBackendDTO.getId();
		if (payeeId == null || payeeId.isEmpty()) {
			logger.error("PayeeId is empty or null");
			return ErrorCodeEnum.ERR_12053.setErrorCode(result);
		}

		IntraBankPayeeDTO intraBankPayeeDTO = new IntraBankPayeeDTO();
		intraBankPayeeDTO.setPayeeId(payeeId);
		intraBankPayeeDTO.setCreatedBy(userId);
		if (StringUtils.isNotBlank(legalEntityId))
			intraBankPayeeDTO.setLegalEntityId(legalEntityId);
		// Creating payee and cif mappings at dbx table
		for (Map.Entry<String, List<String>> contractCif : sharedCifMap.entrySet()) {
			intraBankPayeeDTO.setContractId((String) contractCif.getKey());
			List<String> coreCustomerIds = contractCif.getValue();
			for (String coreCustomerId : coreCustomerIds) {
				intraBankPayeeDTO.setCif(coreCustomerId);
				payeeDelegate.createPayeeAtDBX(intraBankPayeeDTO);
			}
		}

		try {
			JSONObject requestObj = new JSONObject(intraBankPayeeBackendDTO);
			result = JSONToResult.convert(requestObj.toString());
		} catch (JSONException e) {
			logger.error("Error occured while converting the response to Result: ", e);
			return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
		}

		return result;
	}

}
