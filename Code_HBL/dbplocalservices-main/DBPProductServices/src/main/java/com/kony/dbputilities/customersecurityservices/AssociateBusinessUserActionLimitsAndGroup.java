package com.kony.dbputilities.customersecurityservices;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbp.handler.LimitsHandler;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class AssociateBusinessUserActionLimitsAndGroup implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String customerId = dcRequest.getParameter("Customer_id") != null ? dcRequest.getParameter("Customer_id")
                    : dcRequest.getParameter("id");
            String group_id = dcRequest.getParameter("Role_id");
            String accounts = dcRequest.getParameter("accounts");
            String features = dcRequest.getParameter("features");
            String organizationId =
                    dcRequest.getParameter("Organization_id") != null ? dcRequest.getParameter("Organization_id")
                            : dcRequest.getParameter("Organization_Id");

            if (StringUtils.isEmpty(organizationId)) {
                organizationId = inputParams.get("Organization_Id");

                if (StringUtils.isBlank(organizationId)) {
                    organizationId = HelperMethods.getOrganizationIDForUser(customerId, dcRequest);
                }
            }
            if (StringUtils.isBlank(organizationId)) {
                ErrorCodeEnum.ERR_12406.setErrorCode(result);
                return result;
            }

            if (StringUtils.isBlank(customerId)) {
                ErrorCodeEnum.ERR_13504.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(group_id)) {
                ErrorCodeEnum.ERR_13514.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(accounts)) {
                ErrorCodeEnum.ERR_13506.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(features)) {
                ErrorCodeEnum.ERR_13523.setErrorCode(result);
                return result;
            }
            if (StringUtils.isBlank(organizationId)) {
                ErrorCodeEnum.ERR_13517.setErrorCode(result);
                return result;
            }

            JSONArray actionsJSON = new JSONArray();
            JSONArray accountsJSON = new JSONArray(accounts);
            JSONArray featuresJSON = new JSONArray(features);

            LimitsHandler.modifyCustomerActionLimits(null, null, organizationId, group_id, actionsJSON, accountsJSON,
                    featuresJSON, customerId, dcRequest, result);

            result.addParam(new Param("Status", "success", MWConstants.STRING));
        } catch (ApplicationException e) {
            e.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError(
                    "Exception occured in AssociateBusinessUserActionLimitsAndGroup JAVA service. ApplicationException: ",
                    e).log();
        } catch (Exception e) {
            ErrorCodeEnum.ERR_13513.setErrorCode(result);
            result.addParam(new Param("FailureReason", e.getMessage(), MWConstants.STRING));
            alert.prepareError("Exception occured in AssociateBusinessUserActionLimitsAndGroup JAVA service. Exception: ", e).log();
        }
        return result;

    }

}
