package com.kony.adminconsole.service.businesstype;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.BusinessTypeHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class BusinessTypeEditService extends BusinessTypeCreateService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result result = new Result();
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String businessType_id = requestInstance.getParameter("id");
			String name = requestInstance.getParameter("name");
			String signatoryTypes = requestInstance.getParameter("signatoryTypes");

			String minAuthSignatories = requestInstance.getParameter("minAuthSignatories");
			String maxAuthSignatories = requestInstance.getParameter("maxAuthSignatories");
			String defaultRole = requestInstance.getParameter("defaultRole");
			if (StringUtils.isBlank(businessType_id)) {
				Param result_param = new Param("Invalid", "id cannot be null", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(name)) {
				Param result_param = new Param("Invalid", "Name cannot be null", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(minAuthSignatories)) {
				Param result_param = new Param("Invalid", "Min Authorised signatory cannot be null",
						FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(maxAuthSignatories)) {
				Param result_param = new Param("Invalid", "Max Authorised signatory cannot be null",
						FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(signatoryTypes)) {
				Param result_param = new Param("Invalid", "Signatories cannot be null", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else {
				JSONArray signatoryTypesArray = new JSONArray(signatoryTypes);
				if (signatoryTypesArray.length() <= 0) {
					Param result_param = new Param("Invalid", "Signatories cannot be null", FabricConstants.STRING);
					result.addParam(result_param);
					return result;
				}

				BusinessTypeHandler.removeSignatoryTypes(businessType_id, requestInstance);
				BusinessTypeHandler.updateBusinessType(businessType_id, name, signatoryTypesArray, minAuthSignatories,
						maxAuthSignatories, defaultRole, authToken, requestInstance);
			}
			Param result_param = new Param("Status", "Success", FabricConstants.STRING);
			result.addParam(result_param);
			return result;
		} catch (Exception e) {
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		return result;

	}
}
