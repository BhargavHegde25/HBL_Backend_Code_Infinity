package com.temenos.dbx.datamigrationservices.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.eum.product.contract.backenddelegate.api.ContractBackendDelegate;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.utils.InfinityConstants;

public class DataFetchUtils {
	
	private static LoggerUtil logger = new LoggerUtil(DataFetchUtils.class);

	public static JSONArray getInfinityUserContractCustomersPermissions(Map<String, Object> headerParams,
			DataControllerRequest dcRequest) {
		String customerId = dcRequest.getParameter(InfinityConstants.userId);
		UserCustomerViewDTO dto = new UserCustomerViewDTO();
		dto.setCustomerId(customerId);
		List<UserCustomerViewDTO> dtolist = new ArrayList<>();
		try {
			InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			ContractBackendDelegate contractBackendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(ContractBackendDelegate.class);
			ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
			contractCustomerDTO.setCustomerId(dto.getCustomerId());
			dtolist = backendDelegate.getAssociatedCustomers(contractCustomerDTO, headerParams);
			Map<String, Set<String>> contractCustomers = new HashMap<>();
			Map<String, JsonObject> contractInfo = new HashMap<>();
			Map<String, JsonObject> contractCustomerInfo = new HashMap<>();
			if (dtolist != null && !dtolist.isEmpty()) {
				for (UserCustomerViewDTO childDTO : dtolist) {
					if (!contractInfo.containsKey(childDTO.getContractId())) {
						JsonObject json = new JsonObject();
						json.addProperty("contractId", childDTO.getContractId());
						json.addProperty("contractName", childDTO.getContractName());
						contractInfo.put(childDTO.getContractId(), json);
					}
					if (!contractCustomerInfo.containsKey(childDTO.getCoreCustomerId())) {
						JsonObject json = new JsonObject();
						json.addProperty("coreCustomerId", childDTO.getCoreCustomerId());
						json.addProperty("coreCustomerName", childDTO.getCoreCustomerName());
						json.addProperty("userRole", childDTO.getUserRole());
						json.addProperty("isPrimary", childDTO.getIsPrimary());
						json.addProperty("isBusiness", childDTO.getIsBusiness());
						Set<String> actions = new HashSet<>();
						actions = contractBackendDelegate.fetchUserCoreCustomerActions(childDTO.getCustomerId(),
								childDTO.getCoreCustomerId(), headerParams);
						logger.error(actions.toString());
						json.addProperty("actions", actions.toString());
						contractCustomerInfo.put(childDTO.getCoreCustomerId(), json);
					}
					Set<String> customers = new HashSet<>();
					if (contractCustomers.containsKey(childDTO.getContractId())) {
						customers = contractCustomers.get(childDTO.getContractId());
					}
					customers.add(childDTO.getCoreCustomerId());
					contractCustomers.put(childDTO.getContractId(), customers);
				}
			}
			JsonArray contractCustomersJsonArray = new JsonArray();
			for (Entry<String, Set<String>> contractCustomerEntry : contractCustomers.entrySet()) {
				JsonObject json = new JsonObject();
				for (Map.Entry<String, JsonElement> entry : contractInfo.get(contractCustomerEntry.getKey())
						.entrySet()) {
					json.add(entry.getKey(), entry.getValue());
				}
				JsonArray coreCustomers = new JsonArray();
				for (String coreCustomer : contractCustomerEntry.getValue()) {
					JsonObject coreCustomerJson = new JsonObject();
					for (Map.Entry<String, JsonElement> entry : contractCustomerInfo.get(coreCustomer).entrySet()) {
						coreCustomerJson.add(entry.getKey(), entry.getValue());
					}
					coreCustomers.add(coreCustomerJson);
				}
				json.add("contractCustomers", coreCustomers);
				contractCustomersJsonArray.add(json);
			}
			JsonObject res = new JsonObject();
			res.add("contracts", contractCustomersJsonArray);
			JSONObject obj = new JSONObject(res.toString());
			JSONArray response = CommonUtils.getFirstOccuringArray(obj);
			return response; // contractCustomersJsonArray
		} catch (ApplicationException e) {
			logger.error(
					"Exception occured while fetching user contract and contract customer details" + e.getMessage());
			return null;
		} catch (Exception e) {
			logger.error(
					"Exception occured while fetching user contract and contract customer details" + e.getMessage());
			return null;
		}
	}
	
	public static boolean isUserAuthorizedForFeatureAction(List<String> featureActions,
			Map<String, List<String>> contractCifMap, Map<String, Object> headerParams,
			DataControllerRequest dcRequest) {
		if (contractCifMap == null || contractCifMap.isEmpty()) {
			return false;
		}

		JSONArray jsonArray = getInfinityUserContractCustomersPermissions(headerParams, dcRequest);
		if (jsonArray == null || jsonArray.length() == 0) {
			return false;
		}

		Map<String, List<String>> authContractObject = new HashMap<String, List<String>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject contractObj = jsonArray.getJSONObject(i);
			String contractId = contractObj.getString("contractId");
			JSONArray contractCustomers = contractObj.getJSONArray("contractCustomers");
			for (int j = 0; j < contractCustomers.length(); j++) {
				JSONObject cifObj = contractCustomers.getJSONObject(j);
				String coreCustomerId = cifObj.getString("coreCustomerId");
				authContractObject.put(contractId + "_" + coreCustomerId,
						MigrationUtils.getListFromResponseObject(cifObj.get("actions")));
			}
		}

		for (Map.Entry<String, List<String>> contractCif : contractCifMap.entrySet()) {
			String contractId = contractCif.getKey();
			List<String> coreCustomerIds = contractCif.getValue();
			for (int j = 0; j < coreCustomerIds.size(); j++) {
				List<String> cifPermissions = authContractObject.get(contractId + "_" + coreCustomerIds.get(j));
				if (cifPermissions == null || !cifPermissions.containsAll(featureActions))
					return false;
			}
		}
		return true;
	}
	
	public static Map<String, List<String>> getAuthorizedCifs(List<String> featureActions, Map<String, Object> headerParams,
			DataControllerRequest dcRequest) {
		JSONArray jsonArray = getInfinityUserContractCustomersPermissions(headerParams, dcRequest);
		if (jsonArray == null || jsonArray.length() == 0) {
			return null;
		}

		Map<String, List<String>> authContractObject = new HashMap<String, List<String>>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject contractObj = jsonArray.getJSONObject(i);
			String contractId = contractObj.getString("contractId");
			JSONArray contractCustomers = contractObj.getJSONArray("contractCustomers");
			List<String> customers = new ArrayList<String>();
			for (int j = 0; j < contractCustomers.length(); j++) {
				JSONObject cifObj = contractCustomers.getJSONObject(j);
				String coreCustomerId = cifObj.getString("coreCustomerId");
				List<String> customerActions = MigrationUtils.getListFromResponseObject(cifObj.get("actions"));
				if (customerActions.containsAll(featureActions))
					customers.add(coreCustomerId);

			}
			if (!customers.isEmpty())
				authContractObject.put(contractId, customers);
		}

		return authContractObject;
	}
}
