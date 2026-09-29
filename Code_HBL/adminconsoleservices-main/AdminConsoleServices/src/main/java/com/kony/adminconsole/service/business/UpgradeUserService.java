package com.kony.adminconsole.service.business;

import com.hbl.adminconsole.getlistcache.GetListCacheInvalidator;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.CustomerHandler;
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

public class UpgradeUserService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            return invokeOperation(methodID, inputArray, requestInstance, responseInstance);
        } finally {
            // Online-banking getList cache: this operation changes data getList returns. Runs even
            // after a part-way failure, because some rows may already be written.
            GetListCacheInvalidator.permissionsChanged();
        }
    }

    private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();
        try {
            // Validate UserName
            if (requestInstance.getParameter("Username") == null) {
                ErrorCodeEnum.ERR_20533.setErrorCode(result);
                return result;
            } else if (requestInstance.getParameter("Name") == null) {
                ErrorCodeEnum.ERR_21005.setErrorCode(result);
                return result;
            } else if (StringUtils.isBlank(requestInstance.getParameter("Communication"))) {
                ErrorCodeEnum.ERR_21006.setErrorCode(result);
                return result;
            } else if (StringUtils.isBlank(requestInstance.getParameter("Address"))) {
                ErrorCodeEnum.ERR_21007.setErrorCode(result);
                return result;
            } else {
                String userName = requestInstance.getParameter("Username");
                String name = requestInstance.getParameter("Name");
                String Communication = requestInstance.getParameter("Communication");
                String Address = requestInstance.getParameter("Address");
                String membership = null;
                if (StringUtils.isNotBlank(requestInstance.getParameter("Membership"))) {
                    membership = requestInstance.getParameter("Membership");
                }

                if (LoggedInUserHandler.getUserDetails(requestInstance).isAPIUser() == false) {
                    // Check the access control for this customer for current logged-in internal
                    // user
                    CustomerHandler.doesCurrentLoggedinUserHasAccessToCustomer(userName, null, requestInstance, result);
                    if (result.getParamByName(ErrorCodeEnum.ERROR_CODE_KEY) != null) {
                        return result;
                    }
                    // End of access check
                }

                JSONObject upgradeUserresponse = DBPServices.upgradeUser(userName, name, Communication, Address,
                        membership, requestInstance);
                if (upgradeUserresponse != null && upgradeUserresponse.has("dbpErrMessage")) {
                    result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                    result.addParam(new Param("errMsg", upgradeUserresponse.getString("dbpErrMessage"),
                            FabricConstants.STRING));
                    return result;
                }

                else {
                    if (upgradeUserresponse == null || !upgradeUserresponse.has(FabricConstants.OPSTATUS)
                            || upgradeUserresponse.getInt(FabricConstants.OPSTATUS) != 0) {
                        ErrorCodeEnum.ERR_21012.setErrorCode(result);
                        result.addParam(new Param("status", "Failure", FabricConstants.STRING));
                        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
                                ActivityStatusEnum.FAILED, "Upgrade failed for the user:" + userName);
                        return result;
                    } else {
                        result.addParam(new Param("status", "Success", FabricConstants.STRING));
                        result.addParam(new Param("opstatus", upgradeUserresponse.get("opstatus").toString(),
                                FabricConstants.STRING));
                        result.addParam(new Param("id", upgradeUserresponse.getString("id"), FabricConstants.STRING));
                        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
                                ActivityStatusEnum.SUCCESSFUL, "Upgrade success for the user:" + userName);
                    }
                }
            }
        } catch (Exception e) {
            alert.prepareError("Unexepected Error in Upgrade User ", e).log();
            result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.CUSTOMERS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED,
                    "Upgrade failed for the user:" + requestInstance.getParameter("Username"));
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
        }
        return result;
    }
}
