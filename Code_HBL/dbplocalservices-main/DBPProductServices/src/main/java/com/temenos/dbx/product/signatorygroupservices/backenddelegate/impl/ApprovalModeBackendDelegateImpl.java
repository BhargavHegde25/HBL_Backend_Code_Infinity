package com.temenos.dbx.product.signatorygroupservices.backenddelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.ApprovalModeBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.backenddelegate.api.SignatoryGroupBackendDelegate;
import com.temenos.dbx.product.signatorygroupservices.dto.ApprovalModeDTO;
import com.temenos.dbx.product.signatorygroupservices.dto.SignatoryGroupDTO;
import com.temenos.dbx.product.utils.DTOUtils;
import com.temenos.dbx.product.utils.InfinityConstants;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class ApprovalModeBackendDelegateImpl implements ApprovalModeBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public ApprovalModeDTO fetchApprovalMode(String coreCustomerId, String contractId, Map<String, Object> headersMap,
			DataControllerRequest request) {

		List<ApprovalModeDTO> approvalModeDTO = null;
		ApprovalModeDTO aDTO = null;
		try {
			String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
					+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
					+ contractId;

			Map<String, Object> input = new HashMap<String, Object>();

			input.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject dcresponse = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap,
					URLConstants.APPROVAL_MODE_GET);
			JsonArray actions = new JsonArray();
			JSONArray apporvalModeRecords = new JSONArray();
			if (dcresponse!=null && dcresponse.has(DBPDatasetConstants.APPROVAL_MODE)) {
				JsonElement jsonElement = dcresponse.get(DBPDatasetConstants.APPROVAL_MODE);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					actions = jsonElement.getAsJsonArray();
					for (int i = 0; i < actions.size(); i++) {
						JSONObject userDetail = new JSONObject();
						JsonObject jsonObject2 = actions.get(i).getAsJsonObject();
						String coreCustomerId1 = jsonObject2.get("coreCustomerId").getAsString();
						String Id = jsonObject2.get("id").getAsString();
						Boolean isgrouplevel = jsonObject2.get("isGroupLevel").getAsBoolean();
						String contractId1 = jsonObject2.get("contractId").getAsString();
						userDetail.put("id", Id);
						userDetail.put("coreCustomerId", coreCustomerId1);
						userDetail.put("contractId", contractId1);
						userDetail.put("isGroupLevel", isgrouplevel);
						apporvalModeRecords.put(userDetail);
					}
				}
				else {
					JSONObject userDetail = new JSONObject();
					userDetail.put("coreCustomerId", coreCustomerId);
					userDetail.put("contractId", contractId);
					userDetail.put("isGroupLevel", false);
					apporvalModeRecords.put(userDetail);				
				}
			}
			else {
				JSONObject userDetail = new JSONObject();
				userDetail.put("coreCustomerId", coreCustomerId);
				userDetail.put("contractId", contractId);
				userDetail.put("isGroupLevel", false);
				apporvalModeRecords.put(userDetail);				
			}
			// transactionDTO = JSONUtils.parseAsList(trJsonArray.toString(),
			// ACHFileDTO.class);
			if (apporvalModeRecords.length() != 0)
				approvalModeDTO = JSONUtils.parseAsList(apporvalModeRecords.toString(), ApprovalModeDTO.class);
			if (approvalModeDTO != null && approvalModeDTO.size() != 0) {
				aDTO = approvalModeDTO.get(0);
				// achfile.setAchFile_id(achfile.getConfirmationNumber());
				return aDTO;

			}

		}

		catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}

		return aDTO;
	}

	@Override
	public ApprovalModeDTO updateApprovalMode(ApprovalModeDTO approvalModeDTO, Map<String, Object> headersMap,
			DataControllerRequest request) {

		ApprovalModeDTO resultDTO = null;

		try {
			Map<String, Object> inputParams = DTOUtils.getParameterMap(approvalModeDTO, true);
			JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.APPROVAL_MODE_UPDATE);
			resultDTO = (ApprovalModeDTO) DTOUtils.loadJsonObjectIntoObject(accountJson, ApprovalModeDTO.class, true);

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return resultDTO;
	}

	@Override
	public ApprovalModeDTO deleteApprovalMode(ApprovalModeDTO approvalModeDTO, Map<String, Object> headersMap,
			DataControllerRequest request) {
		ApprovalModeDTO resultDTO = null;
		JsonObject accountJson = null;
		Result result = null;
		try {
			Map<String, Object> inputParams = DTOUtils.getParameterMap(approvalModeDTO, true);
			accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.APPROVAL_MODE_DELETE);
			resultDTO = (ApprovalModeDTO) DTOUtils.loadJsonObjectIntoObject(accountJson, ApprovalModeDTO.class, true);
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return resultDTO;
	}

	@Override
	public ApprovalModeDTO createApprovalMode(ApprovalModeDTO approvalModeDTO, Map<String, Object> headersMap,
			DataControllerRequest request) {
		ApprovalModeDTO resultDTO = null;

		try {
			Map<String, Object> inputParams = DTOUtils.getParameterMap(approvalModeDTO, true);
			JsonObject accountJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.APPROVAL_MODE_CREATE);
			resultDTO = (ApprovalModeDTO) DTOUtils.loadJsonObjectIntoObject(accountJson, ApprovalModeDTO.class, true);

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return resultDTO;
	}

	@Override
	public boolean checkForApprovalMatrixPermission(String coreCustomerId, String contractId, String userId,
			Map<String, Object> headersMap, DataControllerRequest request) {

		try {
			Set<String> actionSet = new HashSet<String>();
			Set<String> users = new HashSet<String>();
			actionSet.add("BILL_PAY_APPROVE");
			actionSet.add("DOMESTIC_WIRE_TRANSFER_APPROVE");
			actionSet.add("BILL_PAY_SELF_APPROVAL");
			actionSet.add("ACH_COLLECTION_APPROVE");
			actionSet.add("ACH_FILE_APPROVE");
			actionSet.add("ACH_FILE_SELF_APPROVAL");
			actionSet.add("ACH_PAYMENT_APPROVE");
			actionSet.add("ACH_PAYMENT_SELF_APPROVAL");
			actionSet.add("BULK_PAYMENT_REQUEST_APPROVE");
			actionSet.add("DOMESTIC_WIRE_TRANSFER_SELF_APPROVAL");
			actionSet.add("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE");
			actionSet.add("INTERNATIONAL_WIRE_TRANSFER_APPROVE");
			actionSet.add("INTERNATIONAL_WIRE_TRANSFER_SELF_APPROVAL");
			actionSet.add("INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE");
			actionSet.add("INTRA_BANK_FUND_TRANSFER_APPROVE");
			actionSet.add("INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE");
			actionSet.add("INTRA_BANK_FUND_TRANSFER_SELF_APPROVAL");
			actionSet.add("P2P_SELF_APPROVAL");
			actionSet.add("TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE");
			actionSet.add("TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE");
			actionSet.add("TRANSFER_BETWEEN_OWN_ACCOUNT_SELF_APPROVAL");
			actionSet.add("ACH_COLLECTION_SELF_APPROVAL");
		    actionSet.add("CHEQUE_BOOK_REQUEST_APPROVE");
		    actionSet.add("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE");
		    actionSet.add("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL");
		    actionSet.add("INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE");
		    actionSet.add("INTER_BANK_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL");
		    actionSet.add("P2P_APPROVE");
		    actionSet.add("APPROVAL_MATRIX_MANAGE");
		    actionSet.add("APPROVAL_MATRIX_VIEW");
		    

			String filter = InfinityConstants.coreCustomerId + DBPUtilitiesConstants.EQUAL + coreCustomerId
					+ DBPUtilitiesConstants.AND + InfinityConstants.contractId + DBPUtilitiesConstants.EQUAL
					+ contractId + DBPUtilitiesConstants.AND + InfinityConstants.Customer_id
					+ DBPUtilitiesConstants.EQUAL + userId;

			Map<String, Object> input = new HashMap<String, Object>();

			input.put(DBPUtilitiesConstants.FILTER, filter);
			JsonObject dcresponse = ServiceCallHelper.invokeServiceAndGetJson(input, headersMap,
					URLConstants.CUSTOMER_ACTION_LIMITS_GET);
			JsonArray resJsonArray = new JsonArray();
			if (dcresponse.has(DBPDatasetConstants.DATASET_CUSTOMERACTION)) {
				JsonElement jsonElement = dcresponse.get(DBPDatasetConstants.DATASET_CUSTOMERACTION);
				if (jsonElement.isJsonArray() && jsonElement.getAsJsonArray().size() > 0) {
					resJsonArray = jsonElement.getAsJsonArray();
					for (int i = 0; i < resJsonArray.size(); i++) {
						JsonObject jsonObject2 = resJsonArray.get(i).getAsJsonObject();

						String actionId = jsonObject2.has(InfinityConstants.Action_id)
								&& !jsonObject2.get(InfinityConstants.Action_id).isJsonNull()
										? jsonObject2.get(InfinityConstants.Action_id).getAsString().trim()
										: null;
						String resUserId = jsonObject2.has(InfinityConstants.Customer_id)
								&& !jsonObject2.get(InfinityConstants.Customer_id).isJsonNull()
										? jsonObject2.get(InfinityConstants.Customer_id).getAsString().trim()
										: null;
						if (actionSet.contains(actionId)) {
							users.add(resUserId);
							break;
						}
					}
				}
			}
			if (users.size() == 0) {

				return false;

			}
		}

		catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}

		return true;
	}
}