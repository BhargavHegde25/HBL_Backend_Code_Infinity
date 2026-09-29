/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.temenos.infinity.tradefinanceservices.dto.*;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.CreateLetterOfCreditBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CreateLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.Constants;

public class CreateLetterOfCreditsBusinessDelegateImpl implements CreateLetterOfCreditsBusinessDelegate {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


    @Override
    public LetterOfCreditsDTO createLetterOfCredits(LetterOfCreditsDTO letterOfCredits, DataControllerRequest request)
            throws ApplicationException {
        CreateLetterOfCreditBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(CreateLetterOfCreditBackendDelegate.class);
        return orderBackendDelegate.createLetterOfCreditsOrder(letterOfCredits, request);
    }

    @Override
    public LetterOfCreditsAmendmentDTO amendLetterOfCredit(LetterOfCreditsAmendmentDTO amendmentDTO, DataControllerRequest request) throws ApplicationException {
        CreateLetterOfCreditBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CreateLetterOfCreditBackendDelegate.class);
        return orderBackendDelegate.amendLetterOfCredit(amendmentDTO, request);
    }

    @Override
    public LetterOfCreditsActionDTO executeLetterOfCredits(LetterOfCreditsActionDTO letterOfCredit, DataControllerRequest request)
            throws ApplicationException {
        LetterOfCreditsBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(LetterOfCreditsBackendDelegate.class);
        LetterOfCreditsActionDTO letterOfCreditDTO = new LetterOfCreditsActionDTO();
        try {
            letterOfCreditDTO = orderBackendDelegate.approveLetterOfCredit(letterOfCredit, request);
        } catch (Exception e) {
            alert.prepareError("Error occurred while updating letter of credit. " + e).log();
        }
        return letterOfCreditDTO;
    }

    @Override
    public LetterOfCreditStatusDTO validateForApprovals(LetterOfCreditStatusDTO letterOfCredit, DataControllerRequest request)
            throws ApplicationException {
        LetterOfCreditStatusDTO statusDTO = null;

        String serviceName = Constants.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = Constants.VALIDATE_FOR_APPROVALS;

        String status = letterOfCredit.getStatus() == null ? null : letterOfCredit.getStatus().toString();
        letterOfCredit.setStatus(null);

        Map<String, Object> requestParameters = null;
        try {
            requestParameters = JSONUtils.parseAsMap(new JSONObject(letterOfCredit).toString(), String.class, Object.class);
            requestParameters.put("status", status);
            //requestParameters.put("offsetDetails", offsetDetails);
        } catch (IOException e) {
            alert.prepareError("Error occurred while fetching the input params: " + e).log();
            return null;
        }

        try {
            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    withRequestHeaders(request.getHeaderMap()).
                    withDataControllerRequest(request).
                    build().getResponse();
            JSONObject jsonRsponse = new JSONObject(response);
            statusDTO = JSONUtils.parse(jsonRsponse.toString(), LetterOfCreditStatusDTO.class);
            if (jsonRsponse.has("isSelfApproved") && jsonRsponse.get("isSelfApproved") != null && StringUtils.isNotBlank(jsonRsponse.get("isSelfApproved").toString())) {
                statusDTO.setSelfApproved(Boolean.parseBoolean(jsonRsponse.get("isSelfApproved").toString()));
            }
        } catch (JSONException jsonExp) {
            alert.prepareError("JSONException occurred while approving the request", jsonExp).log();
            return null;
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while approving the request", exp).log();
            return null;
        }

        return statusDTO;
    }

    @Override
    public BBRequestDTO getAccountId(String requestId) {
        BBRequestDTO statusDTO = null;

        String serviceName = Constants.DBPRBLOCALSERVICEDB;
        String operationName = Constants.DB_BBREQUEST_GET;

        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);

        try {
            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    build().getResponse();
            JSONObject jsonRsponse = new JSONObject(response);
            statusDTO = JSONUtils.parse(jsonRsponse.toString(), BBRequestDTO.class);
        } catch (JSONException jsonExp) {
            alert.prepareError("JSONException occurred while approving the request", jsonExp).log();
            return null;
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while approving the request", exp).log();
            return null;
        }

        return statusDTO;
    }

    @Override
    public LetterOfCreditStatusDTO updateBackendIdInApprovalQueue(String requestId, String transactionId,
                                                                  boolean isSelfApproved, String featureActionId, DataControllerRequest request) {
        LetterOfCreditStatusDTO statusDTO = null;

        String serviceName = Constants.DBP_APPROVAL_REQUEST_SERVICES;
        String operationName = Constants.UPDATE_BACKENDID_IN_APPROVALQUEUE;

        Map<String, Object> requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestId", requestId);
        requestParameters.put("transactionId", transactionId);
        requestParameters.put("isSelfApproved", isSelfApproved);
        requestParameters.put("featureActionId", featureActionId);

        try {
            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParameters).
                    withRequestHeaders(request.getHeaderMap()).
                    withDataControllerRequest(request).
                    build().getResponse();
            JSONObject jsonRsponse = new JSONObject(response);
            statusDTO = JSONUtils.parse(jsonRsponse.toString(), LetterOfCreditStatusDTO.class);
        } catch (JSONException jsonExp) {
            alert.prepareError("JSONException occurred while approving the request", jsonExp).log();
            return null;
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while approving the request", exp).log();
            return null;
        }

        return statusDTO;
    }

    @Override
    public boolean deleteLetterOfCreditFromApprovalQueue(String requestId) {
        String serviceName = Constants.DBPRBLOCALSERVICEDB;
        String operationName = Constants.DB_BBREQUEST_DELETE;

        String deleteResponse = "";
        Map<String, Object> requestParams = new HashMap<String, Object>();
        try {
            requestParams.put("requestId", requestId);

            deleteResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(requestParams).
                    build().getResponse();

            JSONObject jsonRsponse = new JSONObject(deleteResponse);
            if (jsonRsponse.getInt("opstatus") == 0 && jsonRsponse.getInt("httpStatusCode") == 0 && jsonRsponse.getInt("deletedRecords") == 1) {
                return true;
            }
        } catch (Exception exp) {
            alert.prepareError("Exception occurred while fetching actions of a request", exp).log();
            return false;
        }
        return false;
    }


    @Override
    public LetterOfCreditsAmendmentDTO updateAmendLC(LetterOfCreditsAmendmentDTO amendmentDto, DataControllerRequest request) {
        CreateLetterOfCreditBackendDelegate orderBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(CreateLetterOfCreditBackendDelegate.class);
        return orderBackendDelegate.updateAmendLC(amendmentDto, request);
    }

}
