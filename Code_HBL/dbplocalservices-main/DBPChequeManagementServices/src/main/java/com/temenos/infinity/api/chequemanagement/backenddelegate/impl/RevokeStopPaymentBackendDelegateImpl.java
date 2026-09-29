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
import com.temenos.infinity.api.chequemanagement.backenddelegate.api.RevokeStopPaymentBackendDelegate;
import com.temenos.infinity.api.chequemanagement.config.ChequeManagementAPIServices;
import com.temenos.infinity.api.chequemanagement.constants.ChequeManagementConstants;
import com.temenos.infinity.api.chequemanagement.constants.ErrorCodeEnum;
import com.temenos.infinity.api.chequemanagement.dto.StopPayment;
import com.temenos.infinity.api.chequemanagement.utils.ChequeManagementProperties;
import com.temenos.infinity.api.commons.exception.ApplicationException;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class RevokeStopPaymentBackendDelegateImpl implements RevokeStopPaymentBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public StopPayment revokeStopPaymentOrder(StopPayment stopPayment, DataControllerRequest request)
            throws ApplicationException {

        StopPayment stopPaymentOrder = new StopPayment();

        // Load Check Book Request properties
        Properties props = ChequeManagementProperties.loadProps(ChequeManagementConstants.PARAM_PROPERTY);

        // Validations before create order
        JSONObject requestBody = new JSONObject();
        String fromAccountNumber = "";
        if (StringUtils.isNotBlank(stopPayment.getFromAccountNumber())) {
            requestBody.put("fromAccountNumber", stopPayment.getFromAccountNumber());
            fromAccountNumber = stopPayment.getFromAccountNumber();
        }
        
        if (StringUtils.isNotBlank(stopPayment.getCheckNumber1()))
            requestBody.put("checkNumber1", stopPayment.getCheckNumber1());
        
        if (StringUtils.isNotBlank(stopPayment.getPayeeName()))
            requestBody.put("payeeName", stopPayment.getPayeeName());

        if (StringUtils.isNotBlank(stopPayment.getRevokeDate()))
            requestBody.put("revokeDate", stopPayment.getRevokeDate());
        
        if (StringUtils.isNotBlank(stopPayment.getRevokeChequeTypeId()))
            requestBody.put("revokeChequeTypeId", stopPayment.getRevokeChequeTypeId());
        
        if (StringUtils.isNotBlank(stopPayment.getIsRevoke()))
            requestBody.put("isRevoke", stopPayment.getIsRevoke()); 
        
        if (StringUtils.isNotBlank(stopPayment.getAmount()))
            requestBody.put("amount", stopPayment.getAmount());

        // Set Input Parameters for create Order service
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("templateName", props.getProperty("revokeStopPaymentTemplate"));
        inputMap.put("type", props.getProperty("revokeStopPaymentType"));
        inputMap.put("subtype", props.getProperty("revokeStopPaymentSubType"));
        inputMap.put("accountId", fromAccountNumber);
        inputMap.put("requestBody", requestBody.toString());
        alert.prepareError("OMS Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        // Making a call to order request API
        String StopPaymentResponse = null;
        JSONObject Response = new JSONObject();
        try {
            StopPaymentResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to revoke Stop payment request order " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26018);
        }

        if (StringUtils.isNotBlank(StopPaymentResponse)) {
            Response = new JSONObject(StopPaymentResponse);
            alert.prepareError("OMS Response " + StopPaymentResponse).log();
        }

        if (Response.has(ChequeManagementConstants.PARAM_ORDER_ID)
                && StringUtils.isNotBlank(Response.getString(ChequeManagementConstants.PARAM_ORDER_ID))) {
            stopPaymentOrder.setReferenceId(Response.getString(ChequeManagementConstants.PARAM_ORDER_ID));
            stopPaymentOrder.setStatus(Response.getString(ChequeManagementConstants.PARAM_ORDER_STATUS));
        }
        
        if(Response.has("dbpErrMsg")&&StringUtils.isNotBlank(Response.getString("dbpErrMsg")))
        	stopPaymentOrder.setMessage(Response.getString("dbpErrMsg"));
        return stopPaymentOrder;
    }

    @Override
    public StopPayment validateRevokeStopPaymentOrder(StopPayment stopPayment, DataControllerRequest request)
            throws ApplicationException {

        StopPayment stopPaymentOrder = new StopPayment();

        // Set Input Parameters for create Order service
        Map<String, Object> inputMap = new HashMap<>();
        String fromAccountNumber = "";
        
        if (StringUtils.isNotBlank(stopPayment.getFromAccountNumber())) {
        	inputMap.put("fromAccountNumber", stopPayment.getFromAccountNumber());
        	fromAccountNumber = stopPayment.getFromAccountNumber();
        }
        
        if (StringUtils.isNotBlank(stopPayment.getCheckNumber1()))
        	inputMap.put("checkNumber1", stopPayment.getCheckNumber1());
        
        if (StringUtils.isNotBlank(stopPayment.getPayeeName()))
        	inputMap.put("payeeName", stopPayment.getPayeeName());

        if (StringUtils.isNotBlank(stopPayment.getRevokeDate()))
        	inputMap.put("revokeDate", stopPayment.getRevokeDate());
        
        if (StringUtils.isNotBlank(stopPayment.getRevokeChequeTypeId()))
        	inputMap.put("revokeChequeTypeId", stopPayment.getRevokeChequeTypeId());
        
        if (StringUtils.isNotBlank(stopPayment.getIsRevoke()))
        	inputMap.put("isRevoke", stopPayment.getIsRevoke()); 
        
        if (StringUtils.isNotBlank(stopPayment.getAmount()))
        	inputMap.put("amount", stopPayment.getAmount());
        
        inputMap.put("validate", stopPayment.getValidate());
        alert.prepareError("Transact Validate Request" + inputMap.toString()).log();

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        // Making a call to order request API
        String StopPaymentResponse = null;
        JSONObject Response = new JSONObject();
        try {
            StopPaymentResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ChequeManagementAPIServices.T24TRANSACT_REVOKESTOPPAYMENT.getServiceName())
                    .withOperationId(ChequeManagementAPIServices.T24TRANSACT_REVOKESTOPPAYMENT.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to validate Stop payment request order " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERRCHQ_26017);
        }

        if (StringUtils.isNotBlank(StopPaymentResponse)) {
            Response = new JSONObject(StopPaymentResponse);
            alert.prepareError("OMS Response " + StopPaymentResponse).log();
        }

        if (Response.has("referenceId")
                && StringUtils.isNotBlank(Response.getString("referenceId"))) {
            stopPaymentOrder.setReferenceId(Response.getString("referenceId"));
            stopPaymentOrder.setStatus(Response.getString("status"));
            stopPaymentOrder.setFee(Response.getString("fee")); 
        }
        if(Response.has("errcode") || Response.has("errmsg")){
            stopPaymentOrder.setMessage(Response.getString("errmsg"));
        }
        return stopPaymentOrder; 
    }

}
