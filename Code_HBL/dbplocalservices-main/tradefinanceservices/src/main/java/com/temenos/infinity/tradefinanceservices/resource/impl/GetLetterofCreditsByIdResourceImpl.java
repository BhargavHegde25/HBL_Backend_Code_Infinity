package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetLetterOfCreditsByIDBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GetLetterofCreditsByIdResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetLetterofCreditsByIdResourceImpl implements GetLetterofCreditsByIdResource {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Result getLetterOfCreditsByID(LetterOfCreditsDTO letterOfCredit, DataControllerRequest request) {
        LetterOfCreditsDTO letterOfCredits;
        Result result;
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        try {

            GetLetterOfCreditsByIDBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetLetterOfCreditsByIDBusinessDelegate.class);

            letterOfCredits = requestBusinessDelegate.getImportLCById(letterOfCredit.getSrmsReqOrderID(), request);

            if (letterOfCredits == null) {
                alert.prepareError("Error occurred while fetching letter of credits from backend").log();
            }
            
            if (letterOfCredits.getErrorCode() != null) {
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result(), letterOfCredits.getErrorMessage());
            }
            JSONObject responseObj = new JSONObject();
            List<LetterOfCreditsDTO> LOC = new ArrayList<>();
            LOC.add(letterOfCredits);
            responseObj.put("LetterOfCredits", LOC);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to fetch letter of credits from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29053.setErrorCode(new Result());
        }
        return result;
    }
}
