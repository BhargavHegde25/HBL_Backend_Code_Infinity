package com.temenos.dbx.product.resource.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.businessdelegate.api.OrganizationEmployeesBusinessDelegate;
import com.temenos.dbx.product.resource.api.OrganisationEmployeesResource;

public class OrganisationEmployeesResourceImpl implements OrganisationEmployeesResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    private static final String FILTER_COLUMN_NAME = "Ssn";
    private static final String IS_USER_EXISTS_PARAM = "isUserExists";

    @Override
    public Result checkIfOrgUserExists(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        String ssn = inputParams.get("Ssn");
        String orgId = null;
        try {
            orgId = HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        orgId = StringUtils.isBlank(orgId) ? dcRequest.getParameter("organisationId") : orgId;
        ssn = StringUtils.isBlank(ssn) ? dcRequest.getParameter("Ssn") : ssn;
        if (StringUtils.isBlank(orgId) || StringUtils.isBlank(ssn)) {
            ErrorCodeEnum.ERR_10219.setErrorCode(result);
            return result;
        }
        OrganizationEmployeesBusinessDelegate upgradeProspect = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(OrganizationEmployeesBusinessDelegate.class);

        JsonObject employeesList = upgradeProspect.getOrgEmployeeList(orgId, FILTER_COLUMN_NAME, ssn,
                URLConstants.GET_ORGANISATION_EMPLOYEES_LIST,
                HelperMethods.getHeaders(dcRequest));

        if (JSONUtil.isJsonNotNull(employeesList) && JSONUtil.hasKey(employeesList, "records")
                && employeesList.get("records").isJsonArray()) {
            JsonArray array = employeesList.get("records").getAsJsonArray();
            if (array.size() > 0 && array.get(0).isJsonObject()) {
                JsonObject object = array.get(0).getAsJsonObject();
                if (JSONUtil.hasKey(object, "employeesList")
                        && StringUtils.isNotBlank(object.get("employeesList").getAsString())) {
                    result.addParam(new Param(IS_USER_EXISTS_PARAM, "true"));
                } else {
                    result.addParam(new Param(IS_USER_EXISTS_PARAM, "false"));
                }

            } else {
                result.addParam(new Param(IS_USER_EXISTS_PARAM, "false"));
            }

        } else {
            ErrorCodeEnum.ERR_10220.setErrorCode(result);

        }

        return result;
    }

}
