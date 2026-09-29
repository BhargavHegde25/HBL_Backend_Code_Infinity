package com.temenos.dbx.product.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.OrganizationGroupActionLimitBusinessDelegate;
import com.temenos.dbx.product.dto.CustomerGroupDTO;
import com.temenos.dbx.product.dto.GroupActionsDTO;
import com.temenos.dbx.product.dto.MemberGroupDTO;
import com.temenos.dbx.product.resource.api.CombinedUserResource;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.CustomerGroupBusinessDelegate;

public class CombinesUserResourceImpl implements CombinedUserResource {

	@Override
	public Result getCombinedUserPermissions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {

		Result result = new Result();
		Map<String, String> userDetails = HelperMethods.getCustomerFromIdentityService(dcRequest);
		String customerId = userDetails.get("customer_id");
		String orgId = userDetails.get("Organization_Id");
		CustomerGroupBusinessDelegate customerGroupBD;
		List<CustomerGroupDTO> response = new ArrayList<>();
		try {
			CustomerGroupDTO inputDTO = new CustomerGroupDTO();
			inputDTO.setCustomerId(customerId);
			customerGroupBD = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CustomerGroupBusinessDelegate.class);
			response = customerGroupBD.getCustomerGroup(inputDTO, dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getErrorCodeEnum());
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10734);
		}

		if (response == null || response.isEmpty()) {
			return result;
		}

		String retailGroupId = null;
		try {
			for (CustomerGroupDTO childDTO : response) {
				MemberGroupDTO inputDTO = new MemberGroupDTO();
				inputDTO.setId(childDTO.getGroupId());
				MemberGroupDTO responseDTO = customerGroupBD.getMemberType(inputDTO, dcRequest.getHeaderMap());
				if (!HelperMethods.isBusinessUserType(responseDTO.getTypeId())) {
					retailGroupId = responseDTO.getId();
					break;
				}
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10735);
		}
		JsonObject retailPermissions = new JsonObject();
		JsonObject businessPermissions = new JsonObject();
		try {
			OrganizationGroupActionLimitBusinessDelegate organizationGroupActionLimitsBD = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(OrganizationGroupActionLimitBusinessDelegate.class);
			if (StringUtils.isNotBlank(retailGroupId)) {
				GroupActionsDTO inputDTO = new GroupActionsDTO();
				inputDTO.setGroupId(retailGroupId);
				retailPermissions = organizationGroupActionLimitsBD.getGroupPermissions(inputDTO,
						dcRequest.getHeaderMap());
			}
			if (StringUtils.isNotBlank(orgId)) {
				Map<String, Object> params = new HashMap<>();
				params.put("customerId", customerId);
				params.put("organizationId", orgId);
				businessPermissions = organizationGroupActionLimitsBD.getCustomerGroupOrganizationPermissions(params,
						dcRequest.getHeaderMap());
			}
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10735);
		}

		result = getFormattedPermissions(retailPermissions, businessPermissions);
		return result;
	}

	private Result getFormattedPermissions(JsonObject retailPermissions, JsonObject businessPermissions) {
		Result result = new Result();
		if (retailPermissions.has("records"))
			result.addStringParam("retailPermissions", getPermssions(retailPermissions));
		if (businessPermissions.has("records"))
			result.addStringParam("businessPermissions", getPermssions(businessPermissions));
		return result;
	}

	private String getJSONString(Set<String> set) {
		return (new JSONArray(set.toString())).toString();
	}

	private String getPermssions(JsonObject permissions) {

		Set<String> permissionsSet = new HashSet<>();
		if (permissions.has("records")) {
			JsonArray jsonarray = permissions.get("records").getAsJsonArray();
			for (JsonElement json : jsonarray) {
				permissionsSet.add(json.getAsJsonObject().get("actionId").getAsString());
			}
		}
		return getJSONString(permissionsSet);
	}
}
