package com.temenos.dbx.product.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ConvertJsonToResult;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.BusinessTypeBusinessDelegate;
import com.temenos.dbx.product.dto.BusinessTypeDTO;
import com.temenos.dbx.product.dto.BusinessTypeRoleDTO;
import com.temenos.dbx.product.resource.api.BusinessTypeResource;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0 Business Resource Implementation
 * 
 */

public class BusinessTypeResourceImpl implements BusinessTypeResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getBusinessTypes(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		List<BusinessTypeDTO> businessTypeDTOList = new ArrayList<>();
		try {
			BusinessTypeBusinessDelegate businessTypeBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(BusinessTypeBusinessDelegate.class);
			businessTypeDTOList = businessTypeBusinessDelegate.getBusinessType(dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			alert.prepareError("Exception occured while fetching the business types :" + e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		}

		try {
			String businessTypes = JSONUtils.stringifyCollectionWithTypeInfo(businessTypeDTOList,
					BusinessTypeDTO.class);
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("BusinessTypes", new JsonParser().parse(businessTypes).getAsJsonArray());
			result = ConvertJsonToResult.convert(jsonobject);
		} catch (IOException e) {
			alert.prepareError("Exception occured while parsing the DTO :" + e.getMessage()).log();
			ErrorCodeEnum.ERR_10228.setErrorCode(result);
		}
		return result;
	}

	@Override
	public Result getBusinessTypeRoles(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		HelperMethods.removeNullValues(inputParams);

		if (inputParams.isEmpty() && inputParams.containsKey("businessTypeId")
				&& StringUtils.isBlank(inputParams.get("businessTypeId"))) {
			ErrorCodeEnum.ERR_10250.setErrorCode(result);
			return result;
		}

		List<BusinessTypeRoleDTO> businessTypeRoles = new ArrayList<>();
		BusinessTypeRoleDTO inputDTO = new BusinessTypeRoleDTO();
		inputDTO.setBusinessTypeId(inputParams.get("businessTypeId"));
		try {
			BusinessTypeBusinessDelegate businessTypeBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(BusinessTypeBusinessDelegate.class);
			businessTypeRoles = businessTypeBusinessDelegate.getBusinessTypeRoles(inputDTO, dcRequest.getHeaderMap());
		} catch (ApplicationException e) {
			alert.prepareError("Exception occured while fetching the business role types :" + e.getMessage()).log();
			throw new ApplicationException(e.getErrorCodeEnum());
		}
		try {
			String businessTypeRolesString = JSONUtils.stringifyCollectionWithTypeInfo(businessTypeRoles,
					BusinessTypeRoleDTO.class);
			JsonObject jsonobject = new JsonObject();
			jsonobject.add("BusinessTypes", new JsonParser().parse(businessTypeRolesString).getAsJsonArray());
			result = ConvertJsonToResult.convert(jsonobject);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10251);
		}

		return result;
	}
}
