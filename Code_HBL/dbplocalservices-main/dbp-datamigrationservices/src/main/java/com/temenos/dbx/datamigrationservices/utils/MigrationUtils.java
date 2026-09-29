package com.temenos.dbx.datamigrationservices.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.datamigrationservices.javaservice.CreateP2PPayee;
import com.temenos.dbx.eum.product.usermanagement.backenddelegate.api.InfinityUserManagementBackendDelegate;
import com.temenos.dbx.product.dto.ContractCustomersDTO;
import com.temenos.dbx.product.dto.UserCustomerViewDTO;
import com.temenos.dbx.product.payeeservices.constants.PayeeConstants;
import com.temenos.dbx.product.utils.InfinityConstants;

public class MigrationUtils {
	private static final Logger logger = LogManager.getLogger(CreateP2PPayee.class);
	
	public static boolean containSpecialChars(String name) {
		Pattern regex = Pattern.compile("[+=\\\\|<>^*%]");
		return regex.matcher(name).find();
	}

	private static boolean validateRegex(String regex, String string) {
		string.trim();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	public static boolean validateUserDetails(Map<String, String> map, Result result) {

		String firstName = map.get(InfinityConstants.firstName);
		String lastName = map.get(InfinityConstants.lastName);
		String middleName = map.containsKey(InfinityConstants.middleName) ? map.get(InfinityConstants.middleName) : "";
		String ssn = map.get(InfinityConstants.ssn);
		String email = map.get(InfinityConstants.email);
		String phoneCountryCode = map.get("phoneCountryCode");
		String phoneNumber = map.get("phoneNumber");
		String dob = map.get(InfinityConstants.dob);
		if (StringUtils.isNotBlank(firstName)) {
			if (!validateRegex("[A-Za-z0-9\\s]{1,51}$", firstName)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid first name");
				return false;
			}
		}
		if (StringUtils.isNotBlank(lastName)) {
			if (!validateRegex("[A-Za-z0-9\\s]{1,51}$", lastName)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid last name");
				return false;
			}
		}
		if (StringUtils.isNotBlank(middleName)) {
			if (!validateRegex("[A-Za-z0-9\\s]{1,51}$", middleName)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid middle name");
				return false;
			}
		}
		if (StringUtils.isNotBlank(ssn)) {
			if (!validateRegex("^[^-_][a-zA-Z0-9\\s-]*[a-zA-Z0-9\\s]{1,51}$", ssn)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid ssn");
				return false;
			}
		}
		if (StringUtils.isNotBlank(email)) {
			if (!validateRegex("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]{1,51}+$", email)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid email");
				return false;
			}
		}
		if (StringUtils.isNotBlank(phoneCountryCode)) {
			if (!validateRegex("^([0-9]){1,3}$", phoneCountryCode)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid phone country code");
				return false;
			}
		}
		if (StringUtils.isNotBlank(phoneNumber)) {
			if (!validateRegex("^([0-9]){7,15}$", phoneNumber)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid phone number");
				return false;
			}
		}
		if (StringUtils.isNotBlank(dob)) {
			if (!validateRegex("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", dob)) {
				ErrorCodeEnum.ERR_10050.setErrorCode(result, "Invalid date of birth");
				return false;
			}
		}
		return true;
	}

	public static List<String> getListFromResponseObject(Object permissionsObj) {
		String permissions = permissionsObj.toString();
		permissions = permissions.replaceAll("\"", "");
		permissions = permissions.substring(1, permissions.length() - 1);
		List<String> permissionList = Arrays.asList(permissions.trim().split("\\s*,\\s*"));
		return permissionList;
	}

	@SuppressWarnings("deprecation")
	public static Map<String, List<String>> getContractCifMap(String inputCifs) {
		Map<String, List<String>> contractCifMap = new HashMap<String, List<String>>();
		JsonParser jsonParser = new JsonParser();
		JsonArray contractCifArray = (JsonArray) jsonParser.parse(inputCifs);
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
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
	}
	
	public static String getInfinityUserContractCustomerDetails(String customerId, Map<String, Object> headersMap)
			throws ApplicationException {

		List<UserCustomerViewDTO> dtolist = new ArrayList<>();
		JsonArray cifList = new JsonArray();
		try {

			InfinityUserManagementBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
					.getBackendDelegate(InfinityUserManagementBackendDelegate.class);
			ContractCustomersDTO contractCustomerDTO = new ContractCustomersDTO();
			contractCustomerDTO.setCustomerId(customerId);
			dtolist = backendDelegate.getAssociatedCustomers(contractCustomerDTO, headersMap);
			if (dtolist != null && !dtolist.isEmpty()) {
				for (UserCustomerViewDTO childDTO : dtolist) {
					JsonObject cif = new JsonObject();
					cif.addProperty("contractId", childDTO.getContractId());
					cif.addProperty("coreCustomerId", childDTO.getCoreCustomerId());
					cifList.add(cif);
				}
			} else {
				logger.error("No data found");
				throw new ApplicationException(ErrorCodeEnum.ERR_10776);
			}

		} catch (ApplicationException e) {
			logger.error("Exception occured while fetching user contract and contract customer details" + e.getMessage());
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			logger.error("Exception occured while fetching user contract and contract customer details" + e.getMessage());
			throw new ApplicationException(ErrorCodeEnum.ERR_10776);
		}
		logger.error("****** cifList :"+cifList);
		return cifList.toString();
	}
	
	public static void prepareAndAddAuthorizedCifsInPayload(Map<String, String> inputParams,
			Map<String, List<String>> sharedCifMap) {
		JSONArray contractCifsArray = new JSONArray();
		for (Map.Entry<String, List<String>> contractObject : sharedCifMap.entrySet()) {
			JSONObject contractCifObject = new JSONObject();
			// get the list of cif ids
			List<String> cifsArray = contractObject.getValue();
			// Convert to Comma Separated cif Ids
			String cifs = String.join(",", cifsArray);
			// store in contractCifObject with key as contractId and value as comma
			// separated CIFs
			contractCifObject.put(PayeeConstants.CONTRACT_ID_KEY, contractObject.getKey());
			contractCifObject.put(PayeeConstants.CORE_CUSTOMER_ID, cifs);
			contractCifsArray.put(contractCifObject);
		}
		inputParams.put(PayeeConstants.CIF_KEY, contractCifsArray.toString());
	}
}
