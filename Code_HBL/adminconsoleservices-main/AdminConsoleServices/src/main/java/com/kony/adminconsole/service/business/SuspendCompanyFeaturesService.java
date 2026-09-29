package com.kony.adminconsole.service.business;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class SuspendCompanyFeaturesService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result result = new Result();
		try {
			if (requestInstance.getParameter("id") == null) {
				ErrorCodeEnum.ERR_21011.setErrorCode(result);
				return result;
			} else if (requestInstance.getParameter("Type") == null) {
				ErrorCodeEnum.ERR_21004.setErrorCode(result);
				return result;
			} else if (requestInstance.getParameter("suspendedFeatures") == null) {
				ErrorCodeEnum.ERR_21005.setErrorCode(result);
				return result;
			} else {
				String Id = requestInstance.getParameter("id");
				String Type = requestInstance.getParameter("Type");
				String suspendedFeatures = null;
				if (requestInstance.getParameter("suspendedFeatures") != null) {
					suspendedFeatures = requestInstance.getParameter("suspendedFeatures");
				}
				JSONObject suspendCompanyFeatureresponse = DBPServices.suspendCompanyFeatures(Id, Type, suspendedFeatures,requestInstance);
				if (suspendCompanyFeatureresponse.has("dbpErrMsg")) {
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					result.addParam(
							new Param("errMsg", suspendCompanyFeatureresponse.getString("dbpErrMsg"), FabricConstants.STRING));
					AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
							ActivityStatusEnum.FAILED, "Suspend Company Feature Failed");
					return result;
				} else {
					if (suspendCompanyFeatureresponse == null || !suspendCompanyFeatureresponse.has(FabricConstants.OPSTATUS)
							|| suspendCompanyFeatureresponse.getInt(FabricConstants.OPSTATUS) != 0) {
						ErrorCodeEnum.ERR_21013.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
								ActivityStatusEnum.FAILED, "Suspend Company Feature Failed");
						return result;
					} else {
						result.addParam(new Param("status", "Success", FabricConstants.STRING));
						result.addParam(new Param("opstatus", suspendCompanyFeatureresponse.get("opstatus").toString(),
								FabricConstants.STRING));
						result.addParam(new Param("message", suspendCompanyFeatureresponse.get("success").toString(),
								FabricConstants.STRING));
						AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
								ActivityStatusEnum.SUCCESSFUL, "Company Features suspended successfully");
					}
				}
			}
		} catch (Exception e) {
			alert.prepareError("Unexepected Error in suspend company feature ", e).log();
			result.addParam(new Param("status", "Failure", FabricConstants.STRING));
			ErrorCodeEnum.ERR_20001.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
					ActivityStatusEnum.FAILED, "Suspend company feature failed");
		}
		return result;
	}
}
