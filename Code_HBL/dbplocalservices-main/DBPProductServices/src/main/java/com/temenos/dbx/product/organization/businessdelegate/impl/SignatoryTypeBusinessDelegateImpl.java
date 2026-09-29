package com.temenos.dbx.product.organization.businessdelegate.impl;

import java.util.HashMap;
import java.util.Map;
import com.dbp.core.util.JSONUtils;
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
import com.temenos.dbx.product.dto.SignatoryTypeDTO;
import com.temenos.dbx.product.organization.businessdelegate.api.SignatoryTypeBusinessDelegate;

public class SignatoryTypeBusinessDelegateImpl implements SignatoryTypeBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public SignatoryTypeDTO getSignatoryTypes(SignatoryTypeDTO inputDTO, Map<String, Object> headersMap)
			throws ApplicationException {
		SignatoryTypeDTO responseDTO = new SignatoryTypeDTO();

		try {
			Map<String, Object> inputParams = new HashMap<>();
			inputParams.put(DBPUtilitiesConstants.FILTER, "id" + DBPUtilitiesConstants.EQUAL + inputDTO.getId());
			JsonObject response = ServiceCallHelper.invokeServiceAndGetJson(inputParams, headersMap,
					URLConstants.SIGNATORYTYPE_GET);
			if (JSONUtil.isJsonNotNull(response)
					&& JSONUtil.hasKey(response, DBPDatasetConstants.DATASET_SIGNATORYTYPE)
					&& response.get(DBPDatasetConstants.DATASET_SIGNATORYTYPE).isJsonArray()) {
				responseDTO = JSONUtils.parse(response.get(DBPDatasetConstants.DATASET_SIGNATORYTYPE).getAsJsonArray()
						.get(0).getAsJsonObject().toString(), SignatoryTypeDTO.class);
			} else {
				throw new ApplicationException(ErrorCodeEnum.ERR_10312);
			}
		} catch (Exception e) {
			alert.prepareError("Exception occured while fetching the business signatory information:" + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_10312);
		}

		return responseDTO;
	}

}
