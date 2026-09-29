package com.temenos.infinity.api.chequemanagement.backenddelegate.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.ApproveCheckBookRequestBackendDelegate;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBookAction;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class ApproveCheckBookRequestBackendDelegateImpl implements ApproveCheckBookRequestBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public ChequeBookAction approveChequeBookRequest(ChequeBookAction chequeBook, DataControllerRequest request)
			throws ApplicationException {

        
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("serviceRequestId", chequeBook.getRequestId());
        inputMap.put("signatoryApproved", "true");
        inputMap.put("comments", chequeBook.getComments());

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        // Making a call to order request API
        String chequeBookResponse = null;
        JSONObject Response = new JSONObject();
        try {
            chequeBookResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_APPROVECHECKBOOKREQUEST.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_APPROVECHECKBOOKREQUEST.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to create cheque book request order " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26011);
        }

        if (StringUtils.isNotBlank(chequeBookResponse)) {
            alert.prepareError("OMS Response " + chequeBookResponse).log();
            Response = new JSONObject(chequeBookResponse);
        }
        ChequeBookAction chequeBookOrder = new ChequeBookAction();
        //@todo output mapping
        if (Response.has(ChequeManagementConstants.PARAM_ORDER_ID)
                && StringUtils.isNotBlank(Response.getString(ChequeManagementConstants.PARAM_ORDER_ID))) {

//            chequeBookOrder.setChequeIssueId(Response.getString(ChequeManagementConstants.PARAM_ORDER_ID));
//            chequeBookOrder.setStatus(Response.getString(ChequeManagementConstants.PARAM_ORDER_STATUS));
        }
        return chequeBookOrder;

	}

}
