package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.dbx.product.dto.BusinessSignatoryDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.BusinessSignatoryBusinessDelegate;
import com.temenos.dbx.product.utils.DTOUtils;

public class BusinessSignatoryBusinessDelegateImpl implements BusinessSignatoryBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public List<BusinessSignatoryDTO> getBusinessSignatories(BusinessSignatoryDTO inputDTO,
			Map<String, Object> headersMap) throws ApplicationException {

		List<BusinessSignatoryDTO> responseDTO = new ArrayList<>();

		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER,
					"BusinessType_id" + DBPUtilitiesConstants.EQUAL + inputDTO.getBusinessTypeId());
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.BUSINESSSIGNATORY_GET);
			if (JSONUtil.isJsonNotNull(response)
					&& JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_BUSINESSSIGNATORY)
					&& response.get(DBPDatasetConstants.DATASET_BUSINESSSIGNATORY).isJsonArray()) {
				for (JsonElement json : response.get(DBPDatasetConstants.DATASET_BUSINESSSIGNATORY)
						.getAsJsonArray()) {
					if (json != null) {
						responseDTO.add((BusinessSignatoryDTO) DTOUtils.loadJsonObjectIntoObject(json.getAsJsonObject(),
								BusinessSignatoryDTO.class, true));
					}
				}
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10311);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the business signatory information:" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10311);
		}

		return responseDTO;
	}

}
