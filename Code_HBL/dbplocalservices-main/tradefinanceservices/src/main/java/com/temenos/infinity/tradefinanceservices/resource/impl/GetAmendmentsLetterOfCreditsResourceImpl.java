/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetAmendmentsLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsAmendmentDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GetAmendmentsLetterOfCreditsResource;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetAmendmentsLetterOfCreditsResourceImpl implements GetAmendmentsLetterOfCreditsResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("null")
    @Override
    public Result getAmendLetterOfCredits(Object[] inputArray, DataControllerRequest request) {
        List<LetterOfCreditsAmendmentDTO> amendmentsList;
        Result result;

        try {
            FilterDTO filterDTO = null;
            @SuppressWarnings("unchecked")
            Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];

            try {
                filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
            } catch (IOException e) {
                alert.prepareError("Exception occurred while fetching params: ", e).log();
            }

            GetAmendmentsLetterOfCreditsBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(GetAmendmentsLetterOfCreditsBusinessDelegate.class);

            amendmentsList = orderBusinessDelegate.getAmendLetterOfCredits(request);

            if (amendmentsList != null && !amendmentsList.isEmpty() && StringUtils.isNotBlank(amendmentsList.get(0).getErrorMessage())) {
                alert.prepareError("Error occurred while fetching letter of credits from backend").log();
            }

            List<LetterOfCreditsAmendmentDTO> filter_paymentTerms = new ArrayList<>();
            List<LetterOfCreditsAmendmentDTO> resultFilter = new ArrayList<>();
            String fromDateFilter = inputParamsMap.get("fromDateFilter") != null ? inputParamsMap.get("fromDateFilter").toString() : null;
            String toDateFilter = inputParamsMap.get("toDateFilter") != null ? inputParamsMap.get("toDateFilter").toString() : null;
            String filterbyparam[] = request.getParameter("filterByParam") != null ? request.getParameter("filterByParam").split(",") : null;
            String filterbyvalue[] = request.getParameter("filterByValue") != null ? request.getParameter("filterByValue").split(",") : null;

            if (StringUtils.isNotEmpty(request.getParameter("filterByParam")) && request.getParameter("filterByParam").contains("paymentTerms") && StringUtils.isNotEmpty(request.getParameter("filterByValue")) && filterbyparam != null && filterbyvalue != null && filterbyparam.length > 0 && filterbyvalue.length > 0 && filterbyparam.length == filterbyvalue.length) {
                for (LetterOfCreditsAmendmentDTO ob : amendmentsList) {
                    int add_param = 0;
                    Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class, Object.class);
                    for (int k = 0; k < filterbyparam.length; k++) {
                        if (objMap.containsKey("paymentTerms") && objMap.get("paymentTerms") != null
                                && StringUtils.isNotEmpty(objMap.get("paymentTerms").toString())
                                && objMap.get("paymentTerms").toString().equalsIgnoreCase(filterbyvalue[k])
                                && filterbyparam[k].equalsIgnoreCase("paymentTerms")) {
                            add_param++;
                        }
                    }
                    if (add_param > 0) {
                        filter_paymentTerms.add(ob);
                    }
                }
                amendmentsList = filter_paymentTerms;
            }

            List<LetterOfCreditsAmendmentDTO> filteredLOC = filterDTO.filter(amendmentsList);

            if (StringUtils.isNotBlank(fromDateFilter) && StringUtils.isNotBlank(toDateFilter)) {
                try {
                    String fromValue = fromDateFilter;
                    String toValue = toDateFilter;
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = formatter.parse(fromValue);
                    Date endDate = formatter.parse(toValue);
                    for (LetterOfCreditsAmendmentDTO ob : filteredLOC) {
                        Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class,
                                Object.class);
                        if (objMap.containsKey("amendmentDate") && objMap.get("amendmentDate") != null
                                && StringUtils.isNotEmpty(objMap.get("amendmentDate").toString())) {
                            String LOCIssueDateValue = objMap.get("amendmentDate").toString();
                            Date LOCIssueDate = formatter.parse(LOCIssueDateValue);
                            Boolean isIssueDateAvailable = LOCIssueDate.getTime() >= startDate.getTime()
                                    && LOCIssueDate.getTime() <= endDate.getTime();
                            if (isIssueDateAvailable) {
                                resultFilter.add(ob);
                            }
                        }
                    }
                    JSONArray LOCFiles = new JSONArray(resultFilter);
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("LetterOfCredits", LOCFiles);
                    result = JSONToResult.convert(responseObj.toString());
                    return result;
                } catch (Exception e) {
                    alert.prepareError(e.toString()).log();
                    diagnostic.prepareDebug("No letter of credits available on the range" + e).log();
                    return ErrorCodeEnum.ERRTF_29050.setErrorCode(new Result());
                }
            }
            JSONArray LOCFiles = new JSONArray(filteredLOC);
            JSONObject responseObj = new JSONObject();
            responseObj.put("Amendments", LOCFiles);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch letter of credits from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29053.setErrorCode(new Result());
        }
        return result;
    }

    public Result getAmendmentsById(Object[] inputArray, DataControllerRequest request) {
        Result result = new Result();
        String amendmentReference;

        if (request.getParameter("amendmentReference").equals("") || request.getParameter("amendmentReference") == null) {
            result.addErrMsgParam("Reference Number not found");
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        } else {
            amendmentReference = request.getParameter("amendmentReference");
        }

        try {
            GetAmendmentsLetterOfCreditsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetAmendmentsLetterOfCreditsBusinessDelegate.class);
            LetterOfCreditsAmendmentDTO amendmentBusiness = businessDelegate.getAmendmentsById(amendmentReference, request);
            if (amendmentBusiness == null || StringUtils.isNotBlank(amendmentBusiness.getMsg())) {
                alert.prepareError("Error occurred while fetching letter of credits from backend").log();
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
            }
            String responseObj = new JSONObject(amendmentBusiness).toString();
            result = JSONToResult.convert(responseObj);
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch letter of credits from OMS " + e).log();
            result = ErrorCodeEnum.ERRTF_29053.setErrorCode(new Result());
        }

        return result;
    }
}