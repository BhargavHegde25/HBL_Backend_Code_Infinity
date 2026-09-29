package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GetLetterOfCreditsResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetLetterOfCreditsResourceImpl implements GetLetterOfCreditsResource {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("null")
    @Override
    public Result getLetterOfCredits(Object[] inputArray, LetterOfCreditsDTO letterOfCreditsDTO, DataControllerRequest request) {
        List<LetterOfCreditsDTO> letterOfCredits;
        Result result;

        try {
            FilterDTO filterDTO = null;
            Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
            try {
                filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
            } catch (IOException e) {
                alert.prepareError("Exception occurred while fetching params: ", e).log();
            }

            GetLetterOfCreditsBusinessDelegate orderBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetLetterOfCreditsBusinessDelegate.class);
            letterOfCredits = orderBusinessDelegate.getLetterOfCredits(letterOfCreditsDTO, request);

            List<LetterOfCreditsDTO> filter_paymentTerms = new ArrayList<>();
            List<LetterOfCreditsDTO> resultFilter = new ArrayList<>();
            String fromDateFilter = inputParamsMap.get("fromDateFilter") != null ? inputParamsMap.get("fromDateFilter").toString() : null;
            String toDateFilter = inputParamsMap.get("toDateFilter") != null ? inputParamsMap.get("toDateFilter").toString() : null;
            String filterbyparam[] = request.getParameter("filterByParam") != null ? request.getParameter("filterByParam").split(",") : null;
            String filterbyvalue[] = request.getParameter("filterByValue") != null ? request.getParameter("filterByValue").split(",") : null;

            if (StringUtils.isNotEmpty(request.getParameter("filterByParam")) && request.getParameter("filterByParam").contains("paymentTerms") && StringUtils.isNotEmpty(request.getParameter("filterByValue")) && filterbyparam != null && filterbyvalue != null && filterbyparam.length > 0 && filterbyvalue.length > 0 && filterbyparam.length == filterbyvalue.length) {
                for (LetterOfCreditsDTO ob : letterOfCredits) {
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
                letterOfCredits = filter_paymentTerms;
            }

            List<LetterOfCreditsDTO> filteredLOC = filterDTO.filter(letterOfCredits);
            if (StringUtils.isNotBlank(fromDateFilter) && StringUtils.isNotBlank(toDateFilter)) {
                try {
                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date startDate = formatter.parse(fromDateFilter);
                    Date endDate = formatter.parse(toDateFilter);
                    for (LetterOfCreditsDTO ob : filteredLOC) {
                        Map<String, Object> objMap = JSONUtils.parseAsMap(new JSONObject(ob).toString(), String.class,
                                Object.class);
                        if (objMap.containsKey("lcCreatedOn") && objMap.get("lcCreatedOn") != null
                                && StringUtils.isNotEmpty(objMap.get("lcCreatedOn").toString())) {
                            String LOCIssueDateValue = objMap.get("lcCreatedOn").toString();
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

            result = JSONToResult.convert(new JSONObject().put("LetterOfCredits", new JSONArray(filteredLOC)).toString());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch letter of credits from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29053.setErrorCode(new Result());
        }
        return result;
    }

}