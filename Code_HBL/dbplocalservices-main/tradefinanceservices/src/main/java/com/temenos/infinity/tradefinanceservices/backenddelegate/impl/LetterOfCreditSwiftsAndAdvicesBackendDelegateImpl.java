/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.DBPDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditSwiftsAndAdvicesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.impl.LetterOfCreditDrawingsBusinessDelegateImpl;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceProperties;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;

public class LetterOfCreditSwiftsAndAdvicesBackendDelegateImpl implements LetterOfCreditSwiftsAndAdvicesBackendDelegate, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public SwiftsAndAdvisesDTO createSwiftsAndAdvises(SwiftsAndAdvisesDTO swiftsAndAdvisesDTO, DBPDTO drawings, DataControllerRequest request) throws com.temenos.infinity.api.commons.exception.ApplicationException {

        LetterOfCreditDrawingsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);

        SwiftsAndAdvisesDTO swiftandadvicesResponse = new SwiftsAndAdvisesDTO();
        JSONObject requestBody = new JSONObject();
        requestBody.put("beneficiaryName", swiftsAndAdvisesDTO.getBeneficiaryName());
        requestBody.put("message", swiftsAndAdvisesDTO.getSwiftMessage());
        requestBody.put("messageType", swiftsAndAdvisesDTO.getSwiftMessageType());
        requestBody.put("messageDate", swiftsAndAdvisesDTO.getSwiftDate());
        requestBody.put("messageCategory", swiftsAndAdvisesDTO.getSwiftCategory());
        requestBody.put("drawingsSrmsReqOrderID", swiftsAndAdvisesDTO.getDrawingsSrmsRequestOrderID());
        String requestbody = requestBody.toString().replaceAll("\"", "'");

        Map<String, Object> inputMap = new HashMap<>();
        Properties props = TradeFinanceProperties.loadProps(PARAM_PROPERTY);
        if (swiftsAndAdvisesDTO.getModule().equalsIgnoreCase("EXLC")) {
            inputMap.put("type", props.getProperty("LetterOfCreditExportSwiftsAndAdvicesType"));
            inputMap.put("subtype", props.getProperty("LetterOfCreditExportSwiftsAndAdvicesSubType"));
        } else {
            inputMap.put("type", props.getProperty("LetterOfCreditSwiftsAndAdvicesType"));
            inputMap.put("subtype", props.getProperty("LetterOfCreditSwiftsAndAdvicesSubType"));
        }

        inputMap.put("serviceReqStatus", "Success");
        inputMap.put("requestBody", requestbody);

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        // Making a call to order request API
        String SwiftAdvicesResponse = null;
        JSONObject Response = new JSONObject();
        try {
            SwiftAdvicesResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getServiceName())
                    .withOperationId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_CREATEORDER.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            request.addRequestParam_("isSrmsFailed", "true");
            alert.prepareError("Unable to create Swift & Advices request order " + e).log();
        }

        if (StringUtils.isNotBlank(SwiftAdvicesResponse)) {
            Response = new JSONObject(SwiftAdvicesResponse);
            diagnostic.prepareInfo("OMS Response : " + Response).log();
        }

        if (Response.has(TradeFinanceConstants.PARAM_ORDER_ID) && StringUtils.isNotBlank(Response.getString(TradeFinanceConstants.PARAM_ORDER_ID))) {
            swiftandadvicesResponse.setSwiftsAndAdvicesSrmsRequestOrderID(Response.getString(TradeFinanceConstants.PARAM_ORDER_ID));
            swiftandadvicesResponse.setStatus("Success");
            request.addRequestParam_("isSrmsFailed", "false");
        } else {
            request.addRequestParam_("isSrmsFailed", "true");
        }

        if (Response.has("dbpErrMsg") && StringUtils.isNotBlank(Response.getString("dbpErrMsg"))) {
            swiftandadvicesResponse.setErrorMessage(Response.getString("dbpErrMsg"));
        }

        if (Response.has("dbpErrCode") && StringUtils.isNotBlank(Response.getString("dbpErrCode"))) {
            swiftandadvicesResponse.setErrorCode(String.valueOf(Response.getInt("dbpErrCode")));
        }

        return swiftandadvicesResponse;
    }

    @Override
    public List<SwiftsAndAdvisesDTO> getSwiftsAndAdvises(DataControllerRequest request) throws ApplicationException {
        List<SwiftsAndAdvisesDTO> swiftAdvices = new ArrayList<>();
        Map<String, Object> inputMap = new HashMap<>();
        Properties props = TradeFinanceProperties.loadProps(PARAM_PROPERTY);

        if (request.containsKeyInRequest("module") && request.getParameter("module").equalsIgnoreCase("EXLC")) {
            inputMap.put("type", props.getProperty("LetterOfCreditExportSwiftsAndAdvicesType"));
            inputMap.put("subtype", props.getProperty("LetterOfCreditExportSwiftsAndAdvicesSubType"));
        } else {
            inputMap.put("type", props.getProperty("LetterOfCreditSwiftsAndAdvicesType"));
            inputMap.put("subtype", props.getProperty("LetterOfCreditSwiftsAndAdvicesSubType"));
        }

        // Set Header Map
        HashMap<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("X-Kony-Authorization", request.getHeader("X-Kony-Authorization"));
        headerMap.put("X-Kony-ReportingParams", request.getHeader("X-Kony-ReportingParams"));

        String swiftAdvicesResponse = null;
        JSONObject Response = new JSONObject();
        try {
            swiftAdvicesResponse = DBPServiceExecutorBuilder.builder()
                    .withServiceId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getServiceName())
                    .withOperationId(TradeFinanceAPIServices.SERVICEREQUESTJAVA_GETORDERDETAILS.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();

        } catch (Exception e) {
            alert.prepareError("Unable to get letter of credits requests " + e).log();
        }
        if (StringUtils.isNotBlank(swiftAdvicesResponse)) {
            Response = new JSONObject(swiftAdvicesResponse);
            diagnostic.prepareInfo("OMS Response " + swiftAdvicesResponse).log();
        }

        JSONArray Orders = Response.getJSONArray(TradeFinanceConstants.PARAM_SERVICE_REQUESTS);
        for (int i = 0; i < Orders.length(); i++) {
            // do logic to insert data here
            SwiftsAndAdvisesDTO order = new SwiftsAndAdvisesDTO();
            JSONObject singleOrder = Orders.getJSONObject(i);
            if (singleOrder.has(PARAM_INPUT_PAYLOAD)) {
                JSONObject payload = singleOrder.getJSONObject(PARAM_INPUT_PAYLOAD);
                String beneficiaryName = payload.has("beneficiaryName") ? payload.getString("beneficiaryName") : "";
                String message = payload.has("message") ? payload.getString("message") : "";
                String messageType = payload.has("messageType") ? payload.getString("messageType") : "";
                String messageDate = payload.has("messageDate") ? payload.getString("messageDate") : "";
                String messageCategory = payload.has("messageCategory") ? payload.getString("messageCategory") : "";
                String drawingsSrmsReqOrderID = payload.has("drawingsSrmsReqOrderID") ? payload.getString("drawingsSrmsReqOrderID") : "";

                if (StringUtils.isNotBlank(beneficiaryName)) {
                    order.setBeneficiaryName(beneficiaryName);
                }
                if (StringUtils.isNotBlank(message)) {
                    order.setSwiftMessage(message);
                }
                if (StringUtils.isNotBlank(messageType)) {
                    order.setSwiftMessageType(messageType);
                }
                if (StringUtils.isNotBlank(messageDate)) {
                    order.setSwiftDate(messageDate);
                }
                if (StringUtils.isNotBlank(messageCategory)) {
                    order.setSwiftCategory(messageCategory);
                }
                if (StringUtils.isNotBlank(drawingsSrmsReqOrderID)) {
                    order.setDrawingsSrmsRequestOrderID(drawingsSrmsReqOrderID);
                }
                if (payload.has(PARAM_SERVICE_REQ_ID)
                        && StringUtils.isNotBlank(payload.getString(PARAM_SERVICE_REQ_ID)))
                    order.setSwiftsAndAdvicesSrmsRequestOrderID(payload.getString(PARAM_SERVICE_REQ_ID));
            }
            swiftAdvices.add(order);
        }

        return swiftAdvices;
    }

}
