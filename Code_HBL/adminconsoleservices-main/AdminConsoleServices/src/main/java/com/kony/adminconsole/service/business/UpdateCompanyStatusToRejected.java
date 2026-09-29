package com.kony.adminconsole.service.business;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class UpdateCompanyStatusToRejected  implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
    
    public static final String STATUS_ID = "SID_ORG_REJECTED";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        long startTime = System.currentTimeMillis();
        long svcEndTime = 0;
        long svcStartTime = 0;
        String organizationId = null;
        try {
        	
        	if (StringUtils.isEmpty(requestInstance.getParameter("organizationId"))) {
                ErrorCodeEnum.ERR_21797.setErrorCode(result);
                return result;
            }
        	
        	if (StringUtils.isEmpty(requestInstance.getParameter("reason"))) {
                ErrorCodeEnum.ERR_21806.setErrorCode(result);
                return result;
            }
        	
        	organizationId = requestInstance.getParameter("organizationId");
        	String reason = requestInstance.getParameter("reason");
        	IdentityHandler identityHandler = requestInstance.getServicesManager().getIdentityHandler();
        	Map<String, Object> userAttributes = identityHandler.getUserAttributes();
        	String rejectedBy = userAttributes.get("username").toString();
            svcStartTime = System.currentTimeMillis();
            JSONObject updateCompanyStatusResponse = DBPServices.updateCompanyStatus( organizationId, rejectedBy, STATUS_ID, reason, requestInstance);
            svcEndTime = System.currentTimeMillis();

            if (updateCompanyStatusResponse == null || !updateCompanyStatusResponse.has(FabricConstants.OPSTATUS)
                    || updateCompanyStatusResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21036.setErrorCode(result);
                result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update compnay status to SID_ORG_REJECTED. organizationId: " + organizationId);
                return result;
            } else if (updateCompanyStatusResponse.has("dbpErrMsg")) {
					result.addParam(new Param("errMsg", updateCompanyStatusResponse.getString("dbpErrMsg"),
							FabricConstants.STRING));
					return result;
			}else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(new Param("opstatus", updateCompanyStatusResponse.get("opstatus").toString(),
                        FabricConstants.STRING));
                result.addParam(new Param("isUpdateSuccess", updateCompanyStatusResponse.get("isUpdateSuccess").toString(),
                        FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully updated compnay status to SID_ORG_REJECTED. organizationId: " + organizationId
                        + ", reaseon: "+reason);
                
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in update company by status to rejected", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update compnay status to SID_ORG_REJECTED. organizationId: " + organizationId);
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        long endTime = System.currentTimeMillis();
        alert.prepareError("MF Time company details send rsp:" + (endTime - startTime) + "service time"
                + (svcEndTime - svcStartTime)).log();
        return result;

    }

}
