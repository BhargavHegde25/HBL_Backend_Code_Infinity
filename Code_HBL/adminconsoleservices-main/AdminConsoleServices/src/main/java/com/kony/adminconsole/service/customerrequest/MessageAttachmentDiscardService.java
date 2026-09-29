package com.kony.adminconsole.service.customerrequest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.handler.CustomerRequestAndMessagesHandler;
import com.kony.adminconsole.service.authmodule.APICustomIdentityService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * <p>
 * Service to discard message attachments
 * </p>
 * 
 * @author Aditya Mankal
 *
 */
public class MessageAttachmentDiscardService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) {

        try {
            Result resutlt = new Result();

            // Read input
            String mediaIds = StringUtils.trim(requestInstance.getParameter("mediaIds"));
            List<String> mediaIdsList = CommonUtilities.getStringifiedArrayAsList(mediaIds);

            UserDetailsBean userDetails = LoggedInUserHandler.getUserDetails(requestInstance);
            String loggedInUserId = userDetails.getId();

            // Read Input Headers
            if (StringUtils.equalsIgnoreCase(userDetails.getId(), APICustomIdentityService.API_USER_ID)) {
                // Request is from OLB/MB. Perform authorization check
                String customerUsername = requestInstance.getHeader("username");
                String customerUserId = CustomerHandler.getCustomerId(customerUsername, requestInstance);
                loggedInUserId = customerUserId;
                // Auth check
                boolean isAuthz =
                        CustomerRequestAndMessagesHandler.hasAccessToMedia(mediaIdsList, loggedInUserId, requestInstance);
                if (isAuthz == false) {
                    diagnostic.prepareDebug("Unathorized access to delete media").log();
                    throw new ApplicationException(ErrorCodeEnum.ERR_21027);
                }
            }

            // Delete media
            CustomerRequestAndMessagesHandler.deleteMedia(mediaIdsList, loggedInUserId, requestInstance);

            // Return success message
            resutlt.addParam(new Param("discardedFiles", Integer.toString(mediaIdsList.size()), FabricConstants.INT));
            return resutlt;

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
