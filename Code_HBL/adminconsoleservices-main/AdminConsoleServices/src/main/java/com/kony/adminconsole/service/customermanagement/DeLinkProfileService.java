package com.kony.adminconsole.service.customermanagement;

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

public class DeLinkProfileService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result result = new Result();
        String combinedUser = requestInstance.getParameter("combinedUser");
        String newUser = requestInstance.getParameter("newUser");
        try {
        	
            if (StringUtils.isEmpty(combinedUser)) {
                ErrorCodeEnum.ERR_21041.setErrorCode(result);
                return result;
            } 
            if (StringUtils.isEmpty("newUser")) {
                ErrorCodeEnum.ERR_21044.setErrorCode(result);
                return result;
            }
            
            
            IdentityHandler identityHandler = requestInstance.getServicesManager().getIdentityHandler();
        	Map<String, Object> userAttributes = identityHandler.getUserAttributes();
        	String username = userAttributes.get("username").toString();
        	
            JSONObject profileDeLinkresponse = DBPServices.deLinkProfile(combinedUser, newUser ,requestInstance);
            
            if (profileDeLinkresponse == null || !profileDeLinkresponse.has(FabricConstants.OPSTATUS)
                    || profileDeLinkresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_21045.setErrorCode(result);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "profile de-linking falied for combinedUser: " + combinedUser
                        +", newUser: " +newUser );
                alert.prepareError("profile de-linking falied for combinedUser: " + combinedUser
                        +", newUser: " +newUser +", reason: "+ profileDeLinkresponse.getString("dbpErrMsg")).log();
                return result;
            }else if (profileDeLinkresponse.has("dbpErrMsg")) {
                result.addParam(new Param("errMsg", profileDeLinkresponse.getString("dbpErrMsg"),
                        FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "profile de-linking falied for combinedUser: " + combinedUser
                        +", newUser: " +newUser );
                alert.prepareError("profile de-linking falied for combinedUser: " + combinedUser
                        +", newUser: " +newUser +", reason: "+ profileDeLinkresponse.getString("dbpErrMsg")).log();
                return result;
            }else {
                result.addParam(new Param("status", "Success", FabricConstants.STRING));
                result.addParam(new Param("opstatus", profileDeLinkresponse.get("opstatus").toString(),
                        FabricConstants.STRING));
                result.addParam(new Param("success", profileDeLinkresponse.getString("success"), FabricConstants.STRING));
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Profile de-linked successfully for combined profile: " + combinedUser + ", by admin: "+username);
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL,
                        "Profile activated after profile de-linked successfully : " + newUser );
            }

        } catch (Exception e) {
            alert.prepareError("Unexepected Error in create Customer ", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.COMPANY, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "profile de-linking falied for combinedUser: " + combinedUser
                    +", newUser: " +newUser );
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        return result;
    }

}