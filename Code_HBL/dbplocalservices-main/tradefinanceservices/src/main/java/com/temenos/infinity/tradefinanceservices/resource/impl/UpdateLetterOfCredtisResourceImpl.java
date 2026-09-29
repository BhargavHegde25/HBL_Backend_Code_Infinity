package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetLetterOfCreditsByIDBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.UpdateLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.LetterOfCreditsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.UpdateLetterOfCreditsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

public class UpdateLetterOfCredtisResourceImpl implements UpdateLetterOfCreditsResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    UpdateLetterOfCreditsBusinessDelegate updateBusinessDelegate = DBPAPIAbstractFactoryImpl
            .getBusinessDelegate(UpdateLetterOfCreditsBusinessDelegate.class);
    GetLetterOfCreditsByIDBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl
            .getBusinessDelegate(GetLetterOfCreditsByIDBusinessDelegate.class);

    public Result updateLetterOfCredits(LetterOfCreditsDTO letterOfCredit, DataControllerRequest request) {
        LetterOfCreditsDTO letterOfCredits;
        Result result;
        try {
            letterOfCredits = updateBusinessDelegate.updateLetterOfCredits(letterOfCredit, request);
            JSONObject responseObj = new JSONObject();
            responseObj.put("LetterOfCredits", letterOfCredits);
            result = JSONToResult.convert(responseObj.toString());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to Update letter of credits from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result updateImportLCByBank(LetterOfCreditsDTO inputDto, DataControllerRequest request) {
        Result result;
        AlertsEnum alertToPush = null;
        try {
            LetterOfCreditsDTO letterOfCredit = requestBusinessDelegate.getImportLCById(inputDto.getSrmsReqOrderID(), request);
            switch (inputDto.getStatus()) {
                case PARAM_STATUS_CANCELLED:
                case PARAM_STATUS_RETURNED_by_BANK:
                case PARAM_STATUS_PARTIALLY_SETTLED:
                case PARAM_STATUS_CLOSED:
                    break;
                case PARAM_STATUS_PROCESSING_by_BANK:
                    alertToPush = AlertsEnum.IMPORT_LC_SUBMITTED_APPROVAL;
                    break;
                case PARAM_APPROVED:
                    alertToPush = AlertsEnum.IMPORT_LC_APPROVED;
                    break;
                case PARAM_STATUS_REJECTED:
                    alertToPush = AlertsEnum.IMPORT_LC_REJECTED;
                    break;                   
                default:
                    return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
            }

            letterOfCredit.setStatus(inputDto.getStatus());
            letterOfCredit.setFlowType(PARAM_BANK_UPDATE);
            LetterOfCreditsDTO letterOfCredits = updateBusinessDelegate.updateLetterOfCredits(letterOfCredit, request);
            result = JSONToResult.convert((new JSONObject(letterOfCredits)).toString());
            TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, letterOfCredits.getSrmsReqOrderID());
            result.addParam("message", "Record updated Successfully");
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to Update letter of credits by Bank from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }
}
