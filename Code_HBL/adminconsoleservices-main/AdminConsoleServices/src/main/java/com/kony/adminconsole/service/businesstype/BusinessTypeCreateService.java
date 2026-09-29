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

public class BusinessTypeCreateService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		try {

			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			String businessType_id;
			String name = requestInstance.getParameter("name");
			String minAuthSignatories = requestInstance.getParameter("minAuthSignatories");
			String maxAuthSignatories = requestInstance.getParameter("maxAuthSignatories");
			int minAuthSigCount = 0;
			int maxAuthSigCount = 0;

			String signatoryTypes = requestInstance.getParameter("signatoryTypes");
			if (StringUtils.isBlank(name)) {
				Param result_param = new Param("Invalid", "Name cannot be null", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(minAuthSignatories)) {
				Param result_param = new Param("Invalid", "Min Authorised signatory cannot be null",
						FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else if (StringUtils.isBlank(signatoryTypes)) {
				Param result_param = new Param("Invalid", "Signatories cannot be null", FabricConstants.STRING);
				result.addParam(result_param);
				return result;
			} else {
				minAuthSigCount = Integer.parseInt(minAuthSignatories);
				if (StringUtils.isBlank(maxAuthSignatories) && minAuthSigCount == 0) {
					Param result_param = new Param("Invalid", "Invalid min and max authorised signatories",
							FabricConstants.STRING);
					result.addParam(result_param);
					return result;

				}
				if (StringUtils.isNotBlank(maxAuthSignatories)) {
					maxAuthSigCount = Integer.parseInt(maxAuthSignatories);
					if (maxAuthSigCount < minAuthSigCount) {
						Param result_param = new Param("Invalid", "Invalid min and max authorised signatories",
								FabricConstants.STRING);
						result.addParam(result_param);
						return result;
					}
				}
				JSONArray signatoryTypesArray = new JSONArray(signatoryTypes);
				if (signatoryTypesArray.length() <= 0) {
					Param result_param = new Param("Invalid", "Signatories cannot be null", FabricConstants.STRING);
					result.addParam(result_param);
					return result;
				}
				Result createBusinessTypeResult = BusinessTypeHandler.createBusinessType(name, signatoryTypesArray,
						minAuthSignatories, maxAuthSignatories, authToken, requestInstance);
				businessType_id = createBusinessTypeResult.getAllParams().get(0).getValue();
			}
			Param result_param = new Param("Status", "Success", FabricConstants.STRING);
			result.addParam(new Param("businessType_id", businessType_id, FabricConstants.STRING));
			result.addParam(result_param);
			return result;
		} catch (Exception e) {
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
		}
		return result;
	}

	
}