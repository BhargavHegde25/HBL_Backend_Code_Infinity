package com.temenos.dbx.eum.product.usermanagement.resource.api;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;

import com.dbp.core.api.Resource;
import com.dbp.core.error.DBPApplicationException;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public interface UserManagementResource extends Resource {

    public Result resetPassword(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result deletePIN(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result verifyPIN(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result verifyExistingPassword(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result verifyUserName(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result resetUserPassword(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result lockUnlockUser(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result getCustomerInformationOnSearch(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse);

    public Result updateDBXUserStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    public Result sendMailToCustomerOnSearch(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;;

    public Result checkSecurityQuestions(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    public Result getUserName(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result getPasswordLockoutSettings(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response);

    public Result verifyCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws JSONException, UnsupportedEncodingException, DBPApplicationException, MiddlewareException;

    /**
     * Activation Code Generation and User Name generation for Enrollment
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result sendActivationCodeAndUsername(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * Activation code for enrollment is validated
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result validateEnrollmentActivation(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * Updates Password for Activation Flow
     * 
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return
     */
    public Result updatePasswordForActivationFlow(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException;

    /**
     * Activation Code Generation and User Name generation for Enrollment based on service key
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws ApplicationException
     */
    public Result sendActivationCodeAndUsernameBasedOnServiceKey(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;

    /**
     * 
     * @param methodID
     * @param inputArray
     * @param dcRequest
     * @param dcResponse
     * @return updating the customer status
     * @throws ApplicationException
     */
    public Result updateCustomerStatusToActive(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException;
    
    public Result searchEnrolledCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
    		DataControllerResponse dcResponse) throws Exception;
}
