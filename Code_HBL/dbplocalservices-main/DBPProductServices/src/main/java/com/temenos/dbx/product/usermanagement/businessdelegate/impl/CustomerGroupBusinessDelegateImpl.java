package com.temenos.dbx.product.usermanagement.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.MemberGroupDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerGroupBusinessDelegateImpl implements CustomerGroupBusinessDelegate {

	@Override
	public CustomerGroupDTO createCustomerGroup(CustomerGroupDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		CustomerGroupDTO resultDTO = null;
		if (null == inputDTO || StringUtils.isBlank(inputDTO.getCustomerId())) {
			return resultDTO;
		}
		try {
			Map<String, Object> inputParams = DTOUtils.getParameterMap(inputDTO, true);
			HelperMethods.removeNullValues(inputParams);
			JsonObject customerGroupJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.CUSTOMER_GROUP_CREATE);
			if (JSONUtil.isJsonNotNull(customerGroupJson)
					&& JSONUtil.hasKey(customerGroupJson, DBPDatasetConstants.DATASET_CUSTOMERGROUP)
					&& customerGroupJson.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP).isJsonArray()) {
				JsonArray customerGroupArray = customerGroupJson.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP)
						.getAsJsonArray();
				JsonObject object = customerGroupArray.size() > 0 ? customerGroupArray.get(0).getAsJsonObject()
						: new JsonObject();
				resultDTO = (CustomerGroupDTO) DTOUtils.loadJsonObjectIntoObject(object, CustomerGroupDTO.class, true);

			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10321);
			}

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10321);
		}

		return resultDTO;
	}

	@Override
	public List<CustomerGroupDTO> getCustomerGroup(CustomerGroupDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		List<CustomerGroupDTO> resultDTO = new ArrayList<>();
		if (null == inputDTO || StringUtils.isBlank(inputDTO.getCustomerId())) {
			return resultDTO;
		}
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER,
					"Customer_id" + DBPUtilitiesConstants.EQUAL + inputDTO.getCustomerId());
			JsonObject customerGroupJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.CUSTOMER_GROUP_GET);
			if (JSONUtil.isJsonNotNull(customerGroupJson)
					&& JSONUtil.hasKey(customerGroupJson, DBPDatasetConstants.DATASET_CUSTOMERGROUP)
					&& customerGroupJson.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP).isJsonArray()) {
				JsonArray customerGroupArray = customerGroupJson.get(DBPDatasetConstants.DATASET_CUSTOMERGROUP)
						.getAsJsonArray();
				for (JsonElement object : customerGroupArray) {
					resultDTO.add((CustomerGroupDTO) DTOUtils.loadJsonObjectIntoObject(object.getAsJsonObject(),
							CustomerGroupDTO.class, true));
				}
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10734);
			}

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10734);
		}

		return resultDTO;
	}

	public MemberGroupDTO getMemberType(MemberGroupDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		MemberGroupDTO responseDTO = new MemberGroupDTO();
		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + inputDTO.getId());
			JsonObject customerGroupJson = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.MEMBERGROUP_GET);
			if (JSONUtil.isJsonNotNull(customerGroupJson)
					&& JSONUtil.hasKey(customerGroupJson, DBPDatasetConstants.DATASET_MEMBERGROUP)
					&& customerGroupJson.get(DBPDatasetConstants.DATASET_MEMBERGROUP).isJsonArray()) {
				JsonArray memberGroupArray = customerGroupJson.get(DBPDatasetConstants.DATASET_MEMBERGROUP)
						.getAsJsonArray();
				JsonObject object = memberGroupArray.size() > 0 ? memberGroupArray.get(0).getAsJsonObject()
						: new JsonObject();
				responseDTO = (MemberGroupDTO) DTOUtils.loadJsonObjectIntoObject(object, MemberGroupDTO.class, true);

			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10735);
			}

		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10735);
		}
		return responseDTO;

	}

}
