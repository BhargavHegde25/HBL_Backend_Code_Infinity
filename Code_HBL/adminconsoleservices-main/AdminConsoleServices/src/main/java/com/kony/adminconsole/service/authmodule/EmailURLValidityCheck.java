package com.kony.adminconsole.service.authmodule;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.handler.UserProfileHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to Check Email URL expiry
 * 
 * @author Akash Rana
 * 
 */
public class EmailURLValidityCheck implements JavaService2 {

    private static final int PASSWORD_RESET_LINK_VALIDITY_IN_MINS = 24 * 60;// 24 hours * 60 minutes
    private static final String PASSWORD_RESET_LINK_VALID = "passwordResetLinkValid";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        try {
            Result processedResult = new Result();

            String userId = requestInstance.getParameter("UserID");
            String resetPasswordURL = requestInstance.getParameter("ResetPasswordURL");
            
            String username = null, userRole = null;
            UserProfileHandler userProfileHandlerInstance = new UserProfileHandler(userId, requestInstance);
            UserDetailsBean userDetails = userProfileHandlerInstance.getDetailsBean();

            if (userDetails != null) {
                username = userDetails.getUserName();
                userRole = userDetails.getRoleName();
            }

            if (StringUtils.isBlank(resetPasswordURL) || StringUtils.equalsIgnoreCase(resetPasswordURL, "NULL")) {
                // UnRecognized account OR Incorrect Password Reset Link
                AuditHandler.auditAdminActivity(requestInstance, username, userRole, ModuleNameEnum.LOGIN,
                        EventEnum.UPDATE, ActivityStatusEnum.FAILED, "Email link check failed.");
                diagnostic.prepareDebug("Unrecognised Account OR Incorrect Password Reset Link").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20931);
            }


            // Prepare Input Map
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(ODataQueryConstants.FILTER,
                    "id eq '" + userId + "' and ResetpasswordLink eq '" + resetPasswordURL + "'");
            inputMap.put(ODataQueryConstants.SELECT, "FirstName,LastName,Email,ResetPasswordExpdts");

            // Fetch Password Reset Link Information
            String serviceResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERDETAILS_VIEW_READ, inputMap,
                    null, requestInstance);
            JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);

            if (serviceResponseJSON != null && serviceResponseJSON.has(FabricConstants.OPSTATUS)
                    && serviceResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                // Successful Operation
                diagnostic.prepareDebug("Successful Read").log();

                JSONArray array = serviceResponseJSON.optJSONArray("internaluserdetails_view");
                if (array != null && array.length() == 1) {

                    // Valid Password Change Request. Proceed with further checks.
                    diagnostic.prepareDebug("Valid Password Change Request. Proceeding with further checks.").log();
                    JSONObject userProfileJSON = array.optJSONObject(0);

                    // Verify Password Reset Link Validity
                    long timeDiff = 0L;
                    if (userProfileJSON.has("ResetPasswordExpdts")) {
                        String passwordLinkCreatedTime = userProfileJSON.optString("ResetPasswordExpdts");
                        timeDiff = CommonUtilities.getTimeElapsedFromTimestampToNowInMinutes(passwordLinkCreatedTime,
                                "yyyy-MM-dd HH:mm:ss");
                    }
                    String email= new String();
                    if (userProfileJSON.has("Email")) 
                        email = userProfileJSON.optString("Email");
                    if (timeDiff > (PASSWORD_RESET_LINK_VALIDITY_IN_MINS)) {
                        // Expired Password Reset link
                        diagnostic.prepareDebug("Expired Password Reset Link").log();
                        ErrorCodeEnum.ERR_20939.setErrorCode(processedResult);
                        processedResult.addParam(new Param("passwordResetLinkValidityInMinutes",
                                String.valueOf(PASSWORD_RESET_LINK_VALIDITY_IN_MINS), FabricConstants.INT));
                        processedResult.addParam(new Param("email", email, FabricConstants.STRING));
                        return processedResult;
                    }
                    else {
	                    // Valid Password Reset Request. Update password
	                    diagnostic.prepareDebug("Valid Password Reset Link").log();
	                    processedResult.addParam(new Param(PASSWORD_RESET_LINK_VALID, "0", FabricConstants.INT));
	                    return processedResult;
                    }
                } else {
                    // UnRecognized account OR Incorrect Password Reset Link
                    diagnostic.prepareDebug("Unrecognised Account OR Incorrect Password Reset Link").log();
                    AuditHandler.auditAdminActivity(requestInstance, username, userRole, ModuleNameEnum.LOGIN,
                            EventEnum.UPDATE, ActivityStatusEnum.FAILED, "Email link check failed.");
                    throw new ApplicationException(ErrorCodeEnum.ERR_20931);
                }
            } else {
                // Failed Kony Fabric Operation
                AuditHandler.auditAdminActivity(requestInstance, username, userRole, ModuleNameEnum.LOGIN,
                        EventEnum.UPDATE, ActivityStatusEnum.FAILED, "Email link check failed.");
                alert.prepareError("Failed Operation. Response" + serviceResponse).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20981);
            }
        } catch (ApplicationException e) {
            Result errorResult = new Result();
            alert.prepareError("Application Exception. Checked Involved Operations. Exception Trace:", e).log();
            e.getErrorCodeEnum().setErrorCode(errorResult);
            return errorResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }

    }

}