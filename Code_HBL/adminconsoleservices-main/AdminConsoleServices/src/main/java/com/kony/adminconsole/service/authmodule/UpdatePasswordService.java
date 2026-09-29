package com.kony.adminconsole.service.authmodule;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.crypto.BCrypt;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
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
 * 
 * Service to Update Internal User Password
 * 
 * @author Aditya Mankal
 * 
 */
public class UpdatePasswordService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    public static final Pattern PASSWORD_PATTERN = Pattern
            .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,16}$");
    private static final String UPDATE_PASSWORD_STATUS_CODE_KEY = "updatePasswordStatusCode";

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {

            Result processedResult = new Result();

            // Read Inputs
            String inputUsername = requestInstance.getParameter("userid");
            String previousPassword = requestInstance.getParameter("previousPassword");
            String newPassword = requestInstance.getParameter("newPassword");

            // Verify if the new password meets the validation criteria
            if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
                diagnostic.prepareDebug("New Password does not meet the criteria").log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Update Password Failed");
                throw new ApplicationException(ErrorCodeEnum.ERR_20936);
            }

            // Prepare Input Map
            Map<String, String> inputMap = new HashMap<>();
            inputMap.put(ODataQueryConstants.FILTER, "Username eq '" + inputUsername + "'");
            inputMap.put(ODataQueryConstants.SELECT, "id,Password");

            // Fetch previous password from backend
            String operationResponse = Executor.invokeService(ServiceURLEnum.INTERNALUSERDETAILS_VIEW_READ, inputMap,
                    null, requestInstance);
            JSONObject operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);

            if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                    && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {

                diagnostic.prepareDebug("Successful Operation").log();
                JSONArray array = operationResponseJSON.optJSONArray("internaluserdetails_view");

                if (array != null && array.length() == 1) {

                    // Recognized account
                    diagnostic.prepareDebug("Recognised Account").log();
                    JSONObject userProfileJSONObject = array.getJSONObject(0);

                    String userId = userProfileJSONObject.optString("id");
                    String actual_PreviousPassword = userProfileJSONObject.optString("Password");
                    boolean isCorrectPreviousPassword = BCrypt.checkpw(previousPassword, actual_PreviousPassword);

                    if (isCorrectPreviousPassword) {

                        // Correct previous password. Proceed with further checks
                        diagnostic.prepareDebug("Valid Previous Password").log();

                        // New password and previous password are the Same. Invalid Request
                        if (StringUtils.equalsIgnoreCase(previousPassword, newPassword)) {
                            diagnostic.prepareDebug("New password and Previous Password are the same. Password change denied").log();
                            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                                    ActivityStatusEnum.FAILED, "Update Password Failed");
                            throw new ApplicationException(ErrorCodeEnum.ERR_20935);
                        }

                        // Valid Request. Update User Password
                        String newPassword_Salt = BCrypt.gensalt(UserAuthentication.PASSWORD_HASH_ROUNDS);
                        String newPassword_hashed = BCrypt.hashpw(newPassword, newPassword_Salt);
                        inputMap.clear();
                        inputMap.put("id", userId);
                        inputMap.put("Password", newPassword_hashed);
                        inputMap.put("LastPasswordChangedts", CommonUtilities.getISOFormattedLocalTimestamp());
                        operationResponse = Executor.invokeService(ServiceURLEnum.SYSTEMUSER_UPDATE, inputMap, null,
                                requestInstance);
                        operationResponseJSON = CommonUtilities.getStringAsJSONObject(operationResponse);
                        if (operationResponseJSON != null && operationResponseJSON.has(FabricConstants.OPSTATUS)
                                && operationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                            // Succesful Operation
                            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                                    ActivityStatusEnum.SUCCESSFUL, "Update Password Successful");
                            diagnostic.prepareDebug("Password changed successfully").log();
                            processedResult
                                    .addParam(new Param(UPDATE_PASSWORD_STATUS_CODE_KEY, "0", FabricConstants.INT));
                            return processedResult;
                        }

                        // Failed Operation
                        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                                ActivityStatusEnum.FAILED, "Update Password Failed");
                        alert.prepareError("Failed Operation. Response:" + operationResponse).log();
                        throw new ApplicationException(ErrorCodeEnum.ERR_20981);

                    } else { // Incorrect previous password
                        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                                ActivityStatusEnum.FAILED, "Update Password Failed");
                        diagnostic.prepareDebug(
                                "Incorrect Previous Password.Unauthorised Password Change Request. Password Change Denied").log();
                        throw new ApplicationException(ErrorCodeEnum.ERR_20938);
                    }
                } else { // Unrecognized account
                    AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                            ActivityStatusEnum.FAILED, "Update Password Failed");
                    diagnostic.prepareDebug("Unrecognised Account").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_20937);
                }
            } else { // Failed Kony Fabric Operation
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOGIN, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Update Password Failed");
                diagnostic.prepareDebug(
                        "Could not Fetch Password Salt. Error at Database Adapter Layer.Response:" + operationResponse).log();
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