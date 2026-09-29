package com.temenos.dbx.product.signatorygroupservices.resource.impl;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.approvalservices.businessdelegate.api.ApprovalQueueBusinessDelegate;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.FeatureAction;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.constants.TransactionStatusEnum;
import com.temenos.dbx.product.signatorygroupservices.businessdelegate.api.SignatoryGroupBusinessDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.CustomerSignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;
import com.temenos.dbx.product.signatorygroupservices.resource.api.SignatoryGroupResource;
import com.temenos.dbx.product.constants.FeatureAction;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.CommonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SignatoryGroupResourceImpl implements SignatoryGroupResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

	@Override
	public Result fetchSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);

		List<SignatoryGroupDTO> signatoryGroups = null;
		String coreCustomerId = inputParams.get(Constants.CORECUSTOMERID) == null ? ""
				: inputParams.get(Constants.CORECUSTOMERID).toString();
		String contractId = inputParams.get(Constants.CONTRACTID) == null ? ""
				: inputParams.get(Constants.CONTRACTID).toString();

		if (StringUtils.isBlank(contractId)) {
			return ErrorCodeEnum.ERR_29001.setErrorCode(new Result());
		}
		if (!CustomerSession.IsAPIUser(customer)) {
			if (StringUtils.isNotBlank(contractId) && StringUtils.isNotBlank(coreCustomerId)) {
				boolean isValid = signatoryGroupBusinessDelegate.checkContractCorecustomer(contractId, coreCustomerId,
						userId, headersMap);
				if (!isValid) {
					return ErrorCodeEnum.ERR_21030.setErrorCode(new Result());
				}
			}
		}
		try {
			signatoryGroups = signatoryGroupBusinessDelegate.fetchSignatoryGroups(coreCustomerId, contractId,
					headersMap);
			if (signatoryGroups == null) {
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
			List<SignatoryGroupDTO> groups = new ArrayList<SignatoryGroupDTO>();
			signatoryGroups.forEach((group) -> {
				if (StringUtils.isNotBlank(group.getSignatoryGroupId()))
					groups.add(group);
			});

			JSONObject resultJSON = formatSignatoryGroupsData(groups);
			result = JSONToResult.convert(resultJSON.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	@Override
	public Result fetchSignatoryGroupsByCoreCustomerIds(String methodID, Object[] inputArray,
			DataControllerRequest request, DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);

		List<SignatoryGroupDTO> signatoryGroups = null;
		String coreCustomerId = inputParams.get(Constants.CORECUSTOMERIDS) == null ? ""
				: inputParams.get(Constants.CORECUSTOMERIDS).toString();
		if (StringUtils.isBlank(coreCustomerId)) {
			return ErrorCodeEnum.ERR_21025.setErrorCode(new Result());
		}
		JSONArray customers = new JSONArray(coreCustomerId);
		if (customers.length() == 0) {
			return ErrorCodeEnum.ERR_21025.setErrorCode(new Result());
		}
		if (!CustomerSession.IsAPIUser(customer)) {
			JSONArray corecustomers = signatoryGroupBusinessDelegate.getCorecustomersForUser(userId, headersMap);
			if (corecustomers == null) {
				return ErrorCodeEnum.ERR_21029.setErrorCode(new Result());
			}

			// check if user has access to all corecustomerids in the input
			for (int i = 0; i < customers.length(); i++) {
				boolean flag = false;
				String inputCustomer = customers.getString(i);
				for (int j = 0; j < corecustomers.length(); j++) {
					String validcustomer = corecustomers.getString(j);
					if (inputCustomer.equalsIgnoreCase(validcustomer)) {
						flag = true;
						break;
					}
				}
				if (!flag) {
					//return ErrorCodeEnum.ERR_21017.setErrorCode(new Result());
				}
			}
		}
		try {
			signatoryGroups = signatoryGroupBusinessDelegate
					.fetchSignatoryGroups(coreCustomerId.substring(1, coreCustomerId.length() - 1), "", headersMap);
			if (signatoryGroups == null) {
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
			JSONObject resultJSON = formatSignatoryGroupsData(signatoryGroups);
			result = JSONToResult.convert(resultJSON.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	@Override
	public Result fetchAllSignatoryGroups(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);

		JSONArray customers = signatoryGroupBusinessDelegate.getCorecustomersForUser(userId, headersMap);
		if (customers == null) {
			return ErrorCodeEnum.ERR_21029.setErrorCode(new Result());
		}
		String coreCustomerId = customers.toString();
		List<SignatoryGroupDTO> signatoryGroups = null;

		try {
			signatoryGroups = signatoryGroupBusinessDelegate
					.fetchSignatoryGroups(coreCustomerId.substring(1, coreCustomerId.length() - 1), "", headersMap);
			if (signatoryGroups == null) {
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
			List<SignatoryGroupDTO> groups = new ArrayList<SignatoryGroupDTO>();
			signatoryGroups.forEach((group) -> {
				if (StringUtils.isNotBlank(group.getSignatoryGroupId()))
					groups.add(group);
			});

			JSONObject resultJSON = formatSignatoryGroupsData(groups);
			result = JSONToResult.convert(resultJSON.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	private JSONObject formatSignatoryGroupsData(List<SignatoryGroupDTO> signatoryGroups) {
		JSONObject result = new JSONObject();
		Map<String, List<SignatoryGroupDTO>> signatoryMap = new HashMap<String, List<SignatoryGroupDTO>>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		signatoryGroups.forEach((group) -> {
			if (signatoryMap.containsKey(group.getCoreCustomerId())) {
				List<SignatoryGroupDTO> dtolist = signatoryMap.get(group.getCoreCustomerId());
				dtolist.add(group);
				signatoryMap.put(group.getCoreCustomerId(), dtolist);
			} else {
				List<SignatoryGroupDTO> dtolist = new ArrayList<SignatoryGroupDTO>();
				dtolist.add(group);
				signatoryMap.put(group.getCoreCustomerId(), dtolist);
			}
		});
		JSONArray sigArray = new JSONArray();
		for (String cif : signatoryMap.keySet()) {
			JSONObject corecustomer = new JSONObject();
			corecustomer.put("coreCustomerId", cif);
			corecustomer.put("coreCustomerName", signatoryMap.get(cif).get(0).getCoreCustomerName());
			corecustomer.put("contractName", signatoryMap.get(cif).get(0).getContractName());
			corecustomer.put("contractId", signatoryMap.get(cif).get(0).getContractId());
			String json = gson.toJson(signatoryMap.get(cif));
			JSONArray array = signatoryMap.get(cif).get(0).getSignatoryGroupId() == null ? new JSONArray()
					: new JSONArray(json);
			corecustomer.put("signatoryGroups", array);
			sigArray.put(corecustomer);
		}
		result.put("coreCustomers", sigArray);
		return result;
	}

	@Override
	public Result fetchSignatoryGroupDetails(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		String signatoryGroupId = inputParams.get(Constants.SIGNATORYGROUPID) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPID).toString();
		if (StringUtils.isBlank(signatoryGroupId)) {
			return ErrorCodeEnum.ERR_21027.setErrorCode(new Result());
		}
		List<SignatoryGroupDTO> signatoryGroupDetail = null;
		try {
			signatoryGroupDetail = signatoryGroupBusinessDelegate.fetchSignatoryDetails(signatoryGroupId, headersMap);
			if (signatoryGroupDetail == null) {
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
			if (signatoryGroupDetail.isEmpty()) {
				return ErrorCodeEnum.ERR_21031.setErrorCode(new Result());
			}
			if (!CustomerSession.IsAPIUser(customer)) {
				boolean valid = hasAccessToCoreCustomer(userId, signatoryGroupDetail.get(0).getCoreCustomerId(),
						headersMap);
				if (!valid) {
					return ErrorCodeEnum.ERR_21032.setErrorCode(new Result());
				}
			}
			JSONObject resultObj = convertDetails(signatoryGroupDetail);
			result = JSONToResult.convert(resultObj.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDetailDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
	}

	private boolean hasAccessToCoreCustomer(String userId, String coreCustomerId, Map<String, Object> headersMap) {
		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
		JSONArray corecustomers = new JSONArray();
		try {
			corecustomers = signatoryGroupBusinessDelegate.getCorecustomersForUser(userId, headersMap);
			if (corecustomers == null) {
				return Boolean.FALSE;
			}
		} catch (Exception e) {
			return Boolean.FALSE;
		}

		boolean valid = Boolean.FALSE;
		for (int i = 0; i < corecustomers.length(); i++) {
			if (corecustomers.getString(i).equalsIgnoreCase(coreCustomerId)) {
				valid = Boolean.TRUE;
			}
		}
		;
		return valid;
	}

	public  JSONObject convertDetails(List<SignatoryGroupDTO> signatories) {
		SignatoryGroupDTO dto = new SignatoryGroupDTO();

		JSONObject signatorydetail = new JSONObject();
		dto = signatories.get(0);
		signatorydetail.put("signatoryGroupId", dto.getSignatoryGroupId());
		signatorydetail.put("signatoryGroupName", dto.getSignatoryGroupName());
		signatorydetail.put("signatoryGroupDescription", dto.getSignatoryGroupDescription());
		signatorydetail.put("coreCustomerId", dto.getCoreCustomerId());
		signatorydetail.put("coreCustomerName", dto.getCoreCustomerName());
		signatorydetail.put("createdBy", StringUtils.isBlank(dto.getCreatedby()) ? "-" : dto.getCreatedby());
		signatorydetail.put("createdOn", dto.getCreatedts());
		signatorydetail.put("lastModified", dto.getLastmodifiedts());
		JSONArray array = new JSONArray();

		for (SignatoryGroupDTO sigDto : signatories) {
			if (sigDto.getCustomerSignatoryGroupId() != null) {
				JSONObject signatory = new JSONObject();
				signatory.put("signatoryId", sigDto.getCustomerSignatoryGroupId());
				signatory.put("customerId", sigDto.getCustomerId());
				signatory.put("customerName", sigDto.getFullName().trim());
				signatory.put("userName", sigDto.getUserName());
				signatory.put("role", sigDto.getCustomerRole());
				signatory.put("addedts", sigDto.getSignatoryaddedts());

				array.put(signatory);
			}

		}
		signatorydetail.put("signatories", array);
		return signatorydetail;
	}

	@Override
	public Result getSignatoryUsers(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<>();

		SignatoryGroupBusinessDelegate signatorygroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
		String coreCustomerId = inputParams.get(Constants.CORECUSTOMERID) == null ? ""
				: inputParams.get(Constants.CORECUSTOMERID).toString();
		String contractId = inputParams.get(Constants.CONTRACTID) == null ? ""
				: inputParams.get(Constants.CONTRACTID).toString();
		if (StringUtils.isBlank(coreCustomerId)) {
			return ErrorCodeEnum.ERR_21025.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(contractId)) {
			return ErrorCodeEnum.ERR_21024.setErrorCode(new Result());
		}
		if (!CustomerSession.IsAPIUser(customer)) {
			boolean isValid = signatorygroupBusinessDelegate.checkContractCorecustomer(contractId, coreCustomerId,
					userId, headersMap);
			if (!isValid) {
				return ErrorCodeEnum.ERR_21030.setErrorCode(new Result());
			}
		}
		try {
			List<SignatoryGroupDTO> signatoryDTO = signatorygroupBusinessDelegate.getSignatoryUsers(coreCustomerId,
					contractId, headersMap, request);
			// JSONObject JSONResponse = new JSONObject(signatoryDTO);
			// result = JSONToResult.convert(JSONResponse.toString());
			JSONArray rulesJSONArr = new JSONArray(signatoryDTO);
			JSONObject responseObj = new JSONObject();
			responseObj.put("SignatoryUsers", rulesJSONArr);
			result = JSONToResult.convert(responseObj.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while fetching SavingsPotCategoriesDTO from SavingsPotBusinessDelegate : " + e)
					.log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	@Override
	public Result createSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();
		JSONObject signatoryGroup = null;

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
		String userfullName = CustomerSession.getCustomerCompleteName(customer);
		String currentdate = HelperMethods.getFormattedTimeStamp(new Date(), "yyyy-MM-dd HH:mm:ss.S");

		String coreCustomerId = inputParams.get(Constants.CORECUSTOMERID) == null ? ""
				: inputParams.get(Constants.CORECUSTOMERID).toString();
		String contractId = inputParams.get(Constants.CONTRACTID) == null ? ""
				: inputParams.get(Constants.CONTRACTID).toString();
		String signatoryGroupName = inputParams.get(Constants.SIGNATORYGROUPNAME) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPNAME).toString();
		String signatoryGroupDescription = inputParams.get(Constants.SIGNATORYGROUPDESCRIPTION) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPDESCRIPTION).toString();
		JSONArray signatories = null;

		if (!CustomerSession.IsAPIUser(customer)) {
			if (StringUtils.isNotBlank(contractId) && StringUtils.isNotBlank(coreCustomerId)) {
				boolean isValid = signatoryGroupBusinessDelegate.checkContractCorecustomer(contractId, coreCustomerId,
						userId, headersMap);
				if (!isValid) {
					return ErrorCodeEnum.ERR_21030.setErrorCode(new Result());
				}
			}
		}

		String sigArray = inputParams.get(Constants.SIGNATORIES) == null ? ""
				: inputParams.get(Constants.SIGNATORIES).toString();
		signatories = new JSONArray(sigArray);

		if (StringUtils.isBlank(coreCustomerId)) {
			return ErrorCodeEnum.ERR_21025.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(contractId)) {
			return ErrorCodeEnum.ERR_21024.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(signatoryGroupName)) {
			return ErrorCodeEnum.ERR_21022.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(sigArray)) {
			return ErrorCodeEnum.ERR_21026.setErrorCode(new Result());
		}

		if (containSpecialChars(signatoryGroupName) || containSpecialChars(signatoryGroupDescription)) {
			return ErrorCodeEnum.ERR_21023.setErrorCode(new Result());
		}

		signatoryGroupName = signatoryGroupName.substring(0, Math.min(signatoryGroupName.length(), 50));
		signatoryGroupDescription = signatoryGroupDescription.substring(0,
				Math.min(signatoryGroupDescription.length(), 200));

		boolean isNameDuplicate = signatoryGroupBusinessDelegate.isGroupNameDuplicate(signatoryGroupName,
				coreCustomerId, contractId, headersMap);
		if (!isNameDuplicate) {
			String customerId;
			JSONObject record;
			boolean isUserThere = false;
			boolean isSignatoryAlreadyPresentInAnotherGroup = false;
			List<String> signatoryCustomerIds = new ArrayList<>();
			for (int i = 0; i < signatories.length(); i++) {
				isUserThere = false;
				customerId = signatories.getJSONObject(i).get(Constants.CUSTOMERID).toString();
				signatoryCustomerIds.add(customerId);
				List<SignatoryGroupDTO> signatoryGroups = signatoryGroupBusinessDelegate
						.getSignatoryUsers(coreCustomerId, contractId, headersMap, request);
				JSONArray rulesJSONArr = new JSONArray(signatoryGroups);
				for (int j = 0; j < rulesJSONArr.length(); j++) {
					record = rulesJSONArr.getJSONObject(j);
					if (record.get("userId").toString().equalsIgnoreCase(customerId)) {
						isUserThere = true;
						break;
					}
				}
				if (!isUserThere) {
					return ErrorCodeEnum.ERR_21018.setErrorCode(new Result());
				}

				isSignatoryAlreadyPresentInAnotherGroup = signatoryGroupBusinessDelegate
						.isSignatoryAlreadyPresentInAnotherGroup(customerId, coreCustomerId, contractId, headersMap);
				if (isSignatoryAlreadyPresentInAnotherGroup) {
					return ErrorCodeEnum.ERR_21021.setErrorCode(new Result());
				}
			}
			UUID uuidValue = UUID.randomUUID();
			String confirmationNumber = uuidValue.toString();
			TransactionStatusEnum transactionStatus = TransactionStatusEnum.SENT;
			boolean isSelfApproved = false;
			if(!CustomerSession.IsAPIUser(customer)) {
				TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
			signatoryGroupDescription = signatoryGroupDescription.isEmpty() ? "-" : signatoryGroupDescription;
			String featureActionId = FeatureAction.SIGNATORY_GROUP_CREATE_EDIT;
			transactionStatusDTO.setFeatureActionID(featureActionId);
			transactionStatusDTO.setCustomerId(userId);
			transactionStatusDTO.setCompanyId(contractId + "_" + coreCustomerId);
			transactionStatusDTO.setContractId(contractId);
			transactionStatusDTO.setSignatoryGroupName(signatoryGroupName);
			transactionStatusDTO.setSignatoryGroupDescription(signatoryGroupDescription);
			transactionStatusDTO.setCoreCustomerId(coreCustomerId);
			try {
				transactionStatusDTO.setNewSigValues(signatoryGroupBusinessDelegate.fetchNewSignatoryGroupUserDetails(String.join(",", signatoryCustomerIds), coreCustomerId).toString());
			}catch (Exception e) {
				alert.prepareError("Error while getting setnewSigValuesWithExternalData from fetchNewSignatoryGroupUserDetails " + e).log();
			}
			transactionStatusDTO.setNewSignatories(signatories.toString());
			transactionStatusDTO.setOldSignatories(new JSONArray().toString());
			transactionStatusDTO.setIsNewFlow(true);
			
			transactionStatusDTO.setConfirmationNumber(confirmationNumber);
			ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
			transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);
			if (transactionStatusDTO == null) {
				return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
			}
			if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
				result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
				result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
				return result;
			}
			transactionStatus = transactionStatusDTO.getStatus();
			isSelfApproved = transactionStatusDTO.isSelfApproved();
			}
			if (transactionStatus == TransactionStatusEnum.SENT) {
				signatoryGroup = createSignatoryGroupAfterApproval(signatoryGroupName, signatoryGroupDescription,
						coreCustomerId, contractId, signatories, userId, true, request);
				if (signatoryGroup == null) {
					return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
				}
				try {
					result = JSONToResult.convert(signatoryGroup.toString());
					result.addParam(Constants.CREATEDBY, userfullName);
					result.addParam(Constants.CREATEDON, currentdate);
					return result;
				} catch (Exception e) {
					alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
					return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
				}
			}
			if (transactionStatus == TransactionStatusEnum.APPROVED) {
				signatoryGroup = createSignatoryGroupAfterApproval(signatoryGroupName, signatoryGroupDescription,
						coreCustomerId, contractId, signatories, userId, true, request);
				if (signatoryGroup == null) {
					return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
				}
				try {
					result = JSONToResult.convert(signatoryGroup.toString());
					result.addParam(Constants.CREATEDBY, userfullName);
					result.addParam(Constants.CREATEDON, currentdate);
					return result;
				} catch (Exception e) {
					alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
					return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
				}
			}
			if (transactionStatus == TransactionStatusEnum.PENDING) {
				result.addParam("transactionStatus", transactionStatus.toString());
				result.addParam("referenceId", confirmationNumber);
				result.addParam("isSelfApprovalFlag", isSelfApproved ? "true" : "false");
				return result;
			}
		} else {
			return ErrorCodeEnum.ERR_21020.setErrorCode(new Result());
		}
		return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
	}

	@Override
	public JSONObject createSignatoryGroupAfterApproval(String signatoryGroupName, String signatoryGroupDescription,
			String coreCustomerId, String contractId, JSONArray signatories, String createdby, boolean isCreateFlow,
			DataControllerRequest dcr) {

		Map<String, Object> headersMap = new HashMap<String, Object>();

		boolean isNameDuplicate = signatoryGroupBusinessDelegate.isGroupNameDuplicate(signatoryGroupName,
				coreCustomerId, contractId, headersMap);
		if (!isNameDuplicate) {
			String customerId;
			JSONObject record;
			boolean isUserThere = false;
			boolean isSignatoryAlreadyPresentInAnotherGroup = false;
			for (int i = 0; i < signatories.length(); i++) {
				isUserThere = false;
				customerId = signatories.getJSONObject(i).get(Constants.CUSTOMERID).toString();
				List<SignatoryGroupDTO> signatoryGroups = signatoryGroupBusinessDelegate
						.getSignatoryUsers(coreCustomerId, contractId, headersMap, dcr);
				JSONArray rulesJSONArr = new JSONArray(signatoryGroups);
				for (int j = 0; j < rulesJSONArr.length(); j++) {
					record = rulesJSONArr.getJSONObject(j);
					if (record.get("userId").toString().equalsIgnoreCase(customerId)) {
						isUserThere = true;
						break;
					}
				}
				if (!isUserThere) {
					return null;
				}

				isSignatoryAlreadyPresentInAnotherGroup = signatoryGroupBusinessDelegate
						.isSignatoryAlreadyPresentInAnotherGroup(customerId, coreCustomerId, contractId, headersMap);
				if (isSignatoryAlreadyPresentInAnotherGroup) {
					return null;
				}
			}
			JSONObject signatoryGroup = null;
			SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
			if (isCreateFlow) {
				try {
					signatoryGroup = signatoryGroupBusinessDelegate.createSignatoryGroup(signatoryGroupName,
							signatoryGroupDescription, coreCustomerId, contractId, signatories, createdby);
					return signatoryGroup;
				} catch (Exception e1) {
					alert.prepareError(
							"Error while creating SignatoryGroupDTO from SignatoryGroupBusinessDelegate : " + e1).log();
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public Result updateSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);

		JSONObject signatoryGroup = null;

		String signatoryGroupId = inputParams.get("signatoryGroupId") == null ? ""
				: inputParams.get("signatoryGroupId").toString();
		if (StringUtils.isBlank(signatoryGroupId)) {
			return ErrorCodeEnum.ERR_21027.setErrorCode(new Result());
		}

		String signatoryGroupDescription = "";
		String signatoryGroupName = "";

		SignatoryGroupDTO signatoryDTO = signatoryGroupBusinessDelegate.fetchSignatoryGroupDetails(signatoryGroupId,
				headersMap);
		if (signatoryDTO == null) {
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

		String contractId = signatoryDTO.getContractId();
		String coreCustomerId = signatoryDTO.getCoreCustomerId();
		if (!CustomerSession.IsAPIUser(customer)) {
			Map<String, Set<String>> contractCifs = getContractsAndCoreCustomerIds(userId);
			Set<String> contracts = contractCifs.get("contracts");
			Set<String> cifs = contractCifs.get("cifs");
			if (!cifs.contains(coreCustomerId) && !contracts.contains(contractId))
				return ErrorCodeEnum.ERR_12007.setErrorCode(new Result());
		}

		signatoryGroupDescription = inputParams.get(Constants.SIGNATORYGROUPDESCRIPTION) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPDESCRIPTION).toString();

		if (inputParams.containsKey(Constants.SIGNATORYGROUPNAME)) {
			signatoryGroupName = inputParams.get(Constants.SIGNATORYGROUPNAME).toString();
		}
		if (StringUtils.isBlank(signatoryGroupName)) {
			return ErrorCodeEnum.ERR_21022.setErrorCode(new Result());
		}

		if (containSpecialChars(signatoryGroupName) || containSpecialChars(signatoryGroupDescription)) {
			return ErrorCodeEnum.ERR_21023.setErrorCode(new Result());
		}

		signatoryGroupName = signatoryGroupName.substring(0, Math.min(signatoryGroupName.length(), 50));
		signatoryGroupDescription = signatoryGroupDescription.substring(0,
				Math.min(signatoryGroupDescription.length(), 200));

		List<SignatoryGroupDTO> signatoryList = new ArrayList<>();
		try {
			signatoryList = signatoryGroupBusinessDelegate.fetchSignatoryGroups(coreCustomerId, contractId, headersMap);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

		if (_isDuplicateGroupName(signatoryList, signatoryGroupId, signatoryGroupName)) {
			return ErrorCodeEnum.ERR_21020.setErrorCode(new Result());
		}

		JSONArray signatories = null;
		String sigArray = inputParams.get(Constants.SIGNATORIES) == null ? "[]"
				: inputParams.get(Constants.SIGNATORIES).toString();
		signatories = new JSONArray(sigArray);
		String customerId;
		JSONObject record;
		boolean isUserThere = false;
		for (int i = 0; i < signatories.length(); i++) {
			isUserThere = false;
			customerId = signatories.getJSONObject(i).get(Constants.CUSTOMERID).toString();
			List<SignatoryGroupDTO> signatoryGroups = null;
			if (signatories.getJSONObject(i).has("isUserRemoved")
					&& signatories.getJSONObject(i).get("isUserRemoved").toString().equalsIgnoreCase("true")) {
				continue;
			}
			signatoryGroups = signatoryGroupBusinessDelegate.getSignatoryUsers(coreCustomerId, contractId, headersMap,
					request);
			JSONArray rulesJSONArr = new JSONArray(signatoryGroups);
			for (int j = 0; j < rulesJSONArr.length(); j++) {
				record = rulesJSONArr.getJSONObject(j);
				if (record.get("userId").toString().equalsIgnoreCase(customerId)) {
					isUserThere = true;
					break;
				}
			}
			if (!isUserThere) {
				return ErrorCodeEnum.ERR_21018.setErrorCode(new Result());
			}
		}

		try {
			signatoryGroup = signatoryGroupBusinessDelegate.updateSignatoryGroup(signatoryGroupId, signatoryGroupName,
					signatoryGroupDescription, coreCustomerId, contractId, signatories, userId, request);
		} catch (Exception e1) {
			alert.prepareError("Error while creating SignatoryGroupDTO from SignatoryGroupBusinessDelegate : " + e1)
					.log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
		try {
			signatoryGroup.put("status", "true");
			signatoryGroup.put("signatoryGroupId", signatoryGroupId);
			result = JSONToResult.convert(signatoryGroup.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	@Override
	public Result deleteSignatoryGroup(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<>();
		Result result = new Result();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
		Map<String, Object> customer = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(customer);
		String signatoryGroupId = inputParams.get(Constants.SIGNATORYGROUPID) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPID).toString();
		SignatoryGroupDTO signatoryDTO = signatoryGroupBusinessDelegate.fetchSignatoryGroupDetails(signatoryGroupId,
				headersMap);
		if (signatoryDTO == null) {
			return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
		}
		String contractId = signatoryDTO.getContractId();
		String coreCustomerId = signatoryDTO.getCoreCustomerId();
		if (!CustomerSession.IsAPIUser(customer)) {
			Map<String, Set<String>> contractCifs = getContractsAndCoreCustomerIds(userId);
			Set<String> contracts = contractCifs.get("contracts");
			Set<String> cifs = contractCifs.get("cifs");
			if (!cifs.contains(coreCustomerId) && !contracts.contains(contractId))
				return ErrorCodeEnum.ERR_12007.setErrorCode(new Result());
		}
		boolean eligibility = signatoryGroupBusinessDelegate.isEligibleForDelete(signatoryGroupId);
		if (!eligibility) {
			return ErrorCodeEnum.ERR_29031.setErrorCode(new Result());
		}
		boolean deleted;
		TransactionStatusEnum transactionStatus = TransactionStatusEnum.SENT;
		boolean isSelfApproved = false;
		if (!CustomerSession.IsAPIUser(customer)) {
		String featureActionId = FeatureAction.SIGNATORY_GROUP_DELETE;
		TransactionStatusDTO transactionStatusDTO = new TransactionStatusDTO();
		transactionStatusDTO.setFeatureActionID(featureActionId);
		transactionStatusDTO.setCustomerId(userId);
		transactionStatusDTO.setCompanyId(contractId + "_" + coreCustomerId);
		transactionStatusDTO.setContractId(contractId);
		transactionStatusDTO.setCoreCustomerId(coreCustomerId);
		transactionStatusDTO.setSignatoryGroupId(signatoryGroupId);
		transactionStatusDTO.setSignatoryGroupDescription((signatoryDTO.getSignatoryGroupDescription()).isEmpty()?"":"-");
		transactionStatusDTO.setConfirmationNumber(signatoryGroupId);
		String oldSignatories = "";
		try {
			List<SignatoryGroupDTO> signatoryGroupDetail = null;
			signatoryGroupDetail = signatoryGroupBusinessDelegate.fetchSignatoryDetails(signatoryGroupId, headersMap);
			if (signatoryGroupDetail == null) {
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
			if (signatoryGroupDetail.isEmpty()) {
				return ErrorCodeEnum.ERR_21031.setErrorCode(new Result());
			}
			
			JSONObject resultObj = convertDetails(signatoryGroupDetail);
			oldSignatories = resultObj.toString();
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDetailDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}
		transactionStatusDTO.setOldSignatories(oldSignatories);
		transactionStatusDTO.setNewSignatories((new JSONArray()).toString());
		
		ApprovalQueueBusinessDelegate approvalQueueDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(ApprovalQueueBusinessDelegate.class);
		transactionStatusDTO = approvalQueueDelegate.validateForApprovals(transactionStatusDTO, request);
		

		if (transactionStatusDTO == null) {
			return ErrorCodeEnum.ERR_29018.setErrorCode(new Result());
		}
		if (transactionStatusDTO.getDbpErrCode() != null || transactionStatusDTO.getDbpErrMsg() != null) {
			result.addParam(new Param("dbpErrCode", transactionStatusDTO.getDbpErrCode()));
			result.addParam(new Param("dbpErrMsg", transactionStatusDTO.getDbpErrMsg()));
			return result;
		}
		 transactionStatus = transactionStatusDTO.getStatus();
		 isSelfApproved = transactionStatusDTO.isSelfApproved();
		}
		if (transactionStatus == TransactionStatusEnum.SENT) {
			try {
				deleted = signatoryGroupBusinessDelegate.deleteSignatoryGroup(signatoryGroupId, headersMap);
			} catch (Exception e1) {
				alert.prepareError("Error while deleting SignatoryGroup from SignatoryGroupBusinessDelegate : " + e1)
						.log();
				return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
			}
			try {
				result = new Result();
				result.addParam("status", deleted?"true":"false");
				return result;
			} catch (Exception e) {
				alert.prepareError("Error while converting response to result " + e).log();
				return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
			}

		}
		if (transactionStatus == TransactionStatusEnum.APPROVED) {
			try {
				deleted = signatoryGroupBusinessDelegate.deleteSignatoryGroupAfterApproval(signatoryGroupId);
			} catch (Exception e1) {
				alert.prepareError("Error while deleting SignatoryGroup from SignatoryGroupBusinessDelegate : " + e1)
						.log();
				return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
			}
			try {
				result = new Result();
				result.addParam("status", deleted?"true":"false");
				return result;
			} catch (Exception e) {
				alert.prepareError("Error while converting response to result " + e).log();
				return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
			}
		}
		if (transactionStatus == TransactionStatusEnum.PENDING) {
			result.addParam("transactionStatus", transactionStatus.toString());
			result.addParam("referenceId", signatoryGroupId);
			result.addParam("isSelfApprovalFlag", isSelfApproved ? "true" : "false");
			return result;
		}
		return result;
		 
	}
	
	

	private boolean _isDuplicateGroupName(List<SignatoryGroupDTO> signatoryList, String signatoryId,
			String signatoryName) {
		for (SignatoryGroupDTO sig : signatoryList) {
			if (sig.getSignatoryGroupId() != null && !sig.getSignatoryGroupId().equals(signatoryId)) {
				if (sig.getSignatoryGroupName().equals(signatoryName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Result isSignatoryGroupNameAvailable(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<>();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		String coreCustomerId = inputParams.get(Constants.CORECUSTOMERID) == null ? ""
				: inputParams.get(Constants.CORECUSTOMERID).toString();
		String signatoryGroupName = inputParams.get(Constants.SIGNATORYGROUPNAME) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPNAME).toString();
		String userId = CustomerSession.getCustomerId(CustomerSession.getCustomerMap(request));
		if (StringUtils.isBlank(coreCustomerId)) {
			return ErrorCodeEnum.ERR_21025.setErrorCode(new Result());
		}

		if (StringUtils.isBlank(signatoryGroupName)) {
			return ErrorCodeEnum.ERR_21022.setErrorCode(new Result());
		}
		if (!checkUserHasCoreIdAccess(userId, coreCustomerId)) {
			return ErrorCodeEnum.ERR_12007.setErrorCode(new Result());
		}
		List<SignatoryGroupDTO> signatoryList = new ArrayList<>();
		try {
			signatoryList = signatoryGroupBusinessDelegate.fetchSignatoryGroups(coreCustomerId, "", headersMap);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
		}

		if (signatoryList == null) {
			return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
		}

		List<String> groupNameList = signatoryList.stream().map(SignatoryGroupDTO::getSignatoryGroupName)
				.filter(val -> val != null).collect(Collectors.toList());
		boolean isDuplicate = groupNameList.stream().anyMatch(signatoryGroupName::equalsIgnoreCase);
		if (isDuplicate) {
			try {
				Result result = new Result();
				result.addParam("status", "false");
				return result;
			} catch (Exception e) {
				alert.prepareError("Error while converting response to result " + e).log();
				return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
			}
		}

		try {
			Result result = new Result();
			result.addParam("status", "true");
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	@Override
	public Result fetchSignatoryGroupDetailsById(String customerId) {

		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		JSONArray customers = signatoryGroupBusinessDelegate.getCorecustomersForUser(customerId, headersMap);
		String coreCustomerId = customers.toString();
		List<SignatoryGroupDTO> signatoryGroups = null;
		List<CustomerSignatoryGroupDTO> customerSignatoryGroups = null;

		try {
			signatoryGroups = signatoryGroupBusinessDelegate
					.fetchSignatoryGroups(coreCustomerId.substring(1, coreCustomerId.length() - 1), "", headersMap);
			if (signatoryGroups == null) {
				alert.prepareError("Error while converting response SignatoryGroupDTO to result ").log();
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
		}

		try {
			customerSignatoryGroups = signatoryGroupBusinessDelegate.fetchCustomerSignatoryGroups(customerId);
			if (customerSignatoryGroups == null) {
				alert.prepareError("Error while converting response SignatoryGroupDTO to result ").log();
				return ErrorCodeEnum.ERR_21033.setErrorCode(new Result());
			}
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_21023.setErrorCode(new Result());
		}
		try {
			List<SignatoryGroupDTO> groups = new ArrayList<SignatoryGroupDTO>();
			signatoryGroups.forEach((group) -> {
				if (StringUtils.isNotBlank(group.getSignatoryGroupId()))
					groups.add(group);
			});
			signatoryGroups = updateAssociation(groups, customerSignatoryGroups);
			JSONObject resultJSON = convertSignatory(signatoryGroups);
			result = JSONToResult.convert(resultJSON.toString());
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	@Override
	public Result updateSignatoryGroupForInfinityUser(JSONObject input) {
		Map<String, Object> headersMap = new HashMap<>();
		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Result result = new Result();
		boolean status = false;
		boolean hasAccess = false;
		JSONArray sigArray = new JSONArray();
		try {
			sigArray = input.getJSONArray(Constants.SIGNATORYGROUPS);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		for (int i = 0; i < sigArray.length(); i++) {
			JSONObject request = sigArray.getJSONObject(i);
			String customerId = request.getString(Constants.CUSTOMERID);
			String cif = request.getString(Constants.CIF);
			String contractId = request.getString(Constants.CONTRACTID);
			String signatoryGroupId = request.getString(Constants.SIGNATORYGROUPID) == null ? ""
					: request.getString(Constants.SIGNATORYGROUPID);
			if (StringUtils.isBlank(customerId) || StringUtils.isBlank(cif) || StringUtils.isBlank(contractId)) {
				ErrorCodeEnum.ERR_10204.setErrorCode(result);
				continue;
			}
			hasAccess = signatoryGroupBusinessDelegate.checkContractCorecustomer(contractId, cif, customerId,
					headersMap);
			if (!hasAccess) {
				ErrorCodeEnum.ERR_21030.setErrorCode(result);
				continue;
			}
			if (StringUtils.isNotBlank(signatoryGroupId)) {
				SignatoryGroupDTO signatoryDTO = signatoryGroupBusinessDelegate
						.fetchSignatoryGroupDetails(signatoryGroupId, headersMap);
				if (signatoryDTO == null) {
					ErrorCodeEnum.ERR_21028.setErrorCode(result);
					continue;
				}
				if (!signatoryDTO.getCoreCustomerId().equalsIgnoreCase(cif)
						|| !signatoryDTO.getContractId().equalsIgnoreCase(contractId)) {
					ErrorCodeEnum.ERR_21032.setErrorCode(result);
					continue;
				}
			}
			status = signatoryGroupBusinessDelegate.updateSignatoryGroupForInfinityUser(cif, contractId, customerId,
					signatoryGroupId);
			if (!status) {
				ErrorCodeEnum.ERR_21034.setErrorCode(result);
			}
		}
		if (result.getParamValueByName(DBPConstants.DBP_ERROR_CODE_KEY) == null
				|| result.getParamValueByName(DBPConstants.DBP_ERROR_MESSAGE_KEY) == null) {
			result.addParam("status", Boolean.toString(Boolean.TRUE));
		}
		return result;
	}

	@Override
	public Result isEligibleforDelete(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		Map<String, Object> headersMap = new HashMap<String, Object>();
		Result result = new Result();
		Map<String, Object> loggedinuser = CustomerSession.getCustomerMap(request);
		String userId = CustomerSession.getCustomerId(loggedinuser);

		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);
		String signatoryGroupId = inputParams.get(Constants.SIGNATORYGROUPID) == null ? ""
				: inputParams.get(Constants.SIGNATORYGROUPID).toString();

		if (StringUtils.isBlank(signatoryGroupId)) {
			return ErrorCodeEnum.ERR_21027.setErrorCode(new Result());
		}
		SignatoryGroupDTO sigDto = null;
		try {
			sigDto = signatoryGroupBusinessDelegate.fetchSignatoryGroupDetails(signatoryGroupId, headersMap);
			if (sigDto == null) {
				return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
			}
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_21028.setErrorCode(new Result());
		}
		if (!CustomerSession.IsAPIUser(loggedinuser)) {
			boolean valid = hasAccessToCoreCustomer(userId, sigDto.getCoreCustomerId(), headersMap);
			if (!valid) {
				return ErrorCodeEnum.ERR_21032.setErrorCode(new Result());
			}
		}

		try {
			boolean status = signatoryGroupBusinessDelegate.isEligibleForDelete(signatoryGroupId);
			result.addParam("status", Boolean.toString(status));
			return result;
		} catch (Exception e) {
			alert.prepareError("Error while converting response SignatoryGroupDTO to result " + e).log();
			return ErrorCodeEnum.ERR_20040.setErrorCode(new Result());
		}

	}

	private List<SignatoryGroupDTO> updateAssociation(List<SignatoryGroupDTO> signatoryList,
			List<CustomerSignatoryGroupDTO> customerSignatoryList) {

		List<String> customersignatoryIdList = customerSignatoryList.stream()
				.map(CustomerSignatoryGroupDTO::getSignatoryGroupId).collect(Collectors.toList());
		try {
			for (SignatoryGroupDTO signatory : signatoryList) {
				if (customersignatoryIdList.contains(signatory.getSignatoryGroupId())) {
					signatory.setAssociated(true);
				} else {
					signatory.setAssociated(false);
				}
			}
		} catch (ConcurrentModificationException e) {
			alert.prepareError("Error while converting list" + e).log();
		}
		return signatoryList;
	}

	private JSONObject convertSignatory(List<SignatoryGroupDTO> signatoryGroups) {
		JSONObject result = new JSONObject();
		Map<String, List<SignatoryGroupDTO>> signatoryMap = new HashMap<String, List<SignatoryGroupDTO>>();
		signatoryGroups.forEach((group) -> {
			if (signatoryMap.containsKey(group.getCoreCustomerId())) {
				List<SignatoryGroupDTO> dtolist = signatoryMap.get(group.getCoreCustomerId());
				dtolist.add(group);
				signatoryMap.put(group.getCoreCustomerId(), dtolist);
			} else {
				List<SignatoryGroupDTO> dtolist = new ArrayList<SignatoryGroupDTO>();
				dtolist.add(group);
				signatoryMap.put(group.getCoreCustomerId(), dtolist);
			}
		});
		JSONArray sigArray = new JSONArray();
		for (String cif : signatoryMap.keySet()) {
			JSONObject corecustomer = new JSONObject();
			corecustomer.put("coreCustomerId", cif);
			corecustomer.put("coreCustomerName", signatoryMap.get(cif).get(0).getCoreCustomerName());
			corecustomer.put("contractName", signatoryMap.get(cif).get(0).getContractName());
			corecustomer.put("contractId", signatoryMap.get(cif).get(0).getContractId());
			JSONArray array = new JSONArray();

			for (SignatoryGroupDTO sigDto : signatoryGroups) {
				if (cif.equals(sigDto.getCoreCustomerId())) {
					JSONObject signatory = new JSONObject();
					signatory.put("signatoryGroupId", sigDto.getSignatoryGroupId());
					signatory.put("signatoryGroupName", sigDto.getSignatoryGroupName());
					signatory.put("signatoryGroupDescription", sigDto.getSignatoryGroupDescription());
					signatory.put("isAssociated", sigDto.isAssociated());

					array.put(signatory);
				}
			}
			corecustomer.put("groups", array);
			sigArray.put(corecustomer);
		}
		result.put("SignatoryGroups", sigArray);
		return result;
	}

	@Override
	public Result updateSignatoryGroupForCustomRole(JSONObject input) {
		Map<String, Object> headersMap = new HashMap<>();
		SignatoryGroupBusinessDelegate signatoryGroupBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SignatoryGroupBusinessDelegate.class);

		Result result = new Result();
		boolean status = false;
		String customers = input.get(Constants.CUSTOMERID) == null ? "" : input.get(Constants.CUSTOMERID).toString();
		if (StringUtils.isBlank(customers)) {
			return ErrorCodeEnum.ERR_10204.setErrorCode(new Result());
		}

		JSONArray sigArray = new JSONArray();
		try {
			sigArray = input.getJSONArray(Constants.SIGNATORYGROUPS);
		} catch (Exception e) {
			return ErrorCodeEnum.ERR_28021.setErrorCode(new Result());
		}
		for (int i = 0; i < sigArray.length(); i++) {
			JSONObject request = sigArray.getJSONObject(i);
			String cif = request.getString(Constants.CIF);
			String contractId = request.getString(Constants.CONTRACTID);
			String signatoryGroupId = request.getString(Constants.SIGNATORYGROUPID) == null ? ""
					: request.getString(Constants.SIGNATORYGROUPID);
			if (StringUtils.isBlank(cif) || StringUtils.isBlank(contractId)) {
				ErrorCodeEnum.ERR_10204.setErrorCode(result);
				continue;
			}

			if (StringUtils.isNotBlank(signatoryGroupId)) {
				SignatoryGroupDTO signatoryDTO = signatoryGroupBusinessDelegate
						.fetchSignatoryGroupDetails(signatoryGroupId, headersMap);
				if (signatoryDTO == null) {
					ErrorCodeEnum.ERR_21028.setErrorCode(result);
					continue;
				}
				if (!signatoryDTO.getCoreCustomerId().equalsIgnoreCase(cif)
						|| !signatoryDTO.getContractId().equalsIgnoreCase(contractId)) {
					ErrorCodeEnum.ERR_21032.setErrorCode(result);
					continue;
				}
			}
			status = signatoryGroupBusinessDelegate.updateSignatoryGroupForInfinityUser(cif, contractId,
					customers.substring(1, customers.length() - 1), signatoryGroupId);
			if (!status) {
				ErrorCodeEnum.ERR_21034.setErrorCode(result);
			}
		}
		if (result.getParamValueByName(DBPConstants.DBP_ERROR_CODE_KEY) == null
				|| result.getParamValueByName(DBPConstants.DBP_ERROR_MESSAGE_KEY) == null) {
			result.addParam("status", Boolean.toString(Boolean.TRUE));
		}
		return result;
	}

	private boolean containSpecialChars(String name) {
		Pattern regex = Pattern.compile("[+=\\\\|<>^*%]");
		if (regex.matcher(name).find()) {
			return true;
		}
		return false;
	}

	private boolean checkUserHasCoreIdAccess(String userId, String coreCustomerId) {
		Map<String, Object> params = new HashMap<>();
		String filter = "customerId" + DBPUtilitiesConstants.EQUAL + userId + DBPUtilitiesConstants.AND
				+ "coreCustomerId" + DBPUtilitiesConstants.EQUAL + coreCustomerId;
		params.put(DBPUtilitiesConstants.FILTER, filter);
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CONTRACTCUSTOMERS_GET;
		try {
			String getResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(params).build().getResponse();
			@SuppressWarnings("deprecation")
			JsonObject response = new JsonParser().parse(getResponse).getAsJsonObject();
			if (response != null && response.has("contractcustomers")) {
				JsonArray responseArr = JSONUtil.getJsonArrary(response, "contractcustomers");
				Set<String> cifs = new HashSet<>();
				for (JsonElement jsonEle : responseArr) {
					JsonObject current = jsonEle.getAsJsonObject();
					cifs.add(current.get("coreCustomerId").getAsString());
				}
				if (cifs.contains(coreCustomerId)) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	private Map<String, Set<String>> getContractsAndCoreCustomerIds(String userId) {
		Map<String, Set<String>> contractCifs = new HashMap<>();
		String filter = "customerId" + DBPUtilitiesConstants.EQUAL + userId;
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = OperationName.DB_CONTRACTCUSTOMERS_GET;
		Map<String, Object> params = new HashMap<>();
		params.put(DBPUtilitiesConstants.FILTER, filter);
		try {
			String getResponse = DBPServiceExecutorBuilder.builder().withServiceId(serviceName).withObjectId(null)
					.withOperationId(operationName).withRequestParameters(params).build().getResponse();
			@SuppressWarnings("deprecation")
			JsonObject response = new JsonParser().parse(getResponse).getAsJsonObject();
			if (response != null && response.has("contractcustomers")) {
				JsonArray responseArr = JSONUtil.getJsonArrary(response, "contractcustomers");
				Set<String> contracts = new HashSet<>();
				Set<String> cifs = new HashSet<>();
				for (JsonElement jsonEle : responseArr) {
					JsonObject current = jsonEle.getAsJsonObject();
					contracts.add(current.get("contractId").getAsString());
					cifs.add(current.get("coreCustomerId").getAsString());
				}
				contractCifs.put("cifs", cifs);
				contractCifs.put("contracts", contracts);
			}
		} catch (Exception e) {
			return null;
		}
		return contractCifs;
	}
}