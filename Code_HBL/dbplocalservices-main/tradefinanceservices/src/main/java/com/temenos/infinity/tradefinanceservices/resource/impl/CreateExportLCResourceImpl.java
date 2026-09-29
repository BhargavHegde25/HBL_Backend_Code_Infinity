/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

import java.util.Arrays;
import java.util.Map;

import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CreateExportLCBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsByIdBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.CreateExportLCResource;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

public class CreateExportLCResourceImpl implements CreateExportLCResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final CreateExportLCBusinessDelegate createBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CreateExportLCBusinessDelegate.class);
    private static final GetExportLetterOfCreditsByIdBusinessDelegate getBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetExportLetterOfCreditsByIdBusinessDelegate.class);

    @Override
    public Result createExportLetterOfCredit(ExportLOCDTO createPayloadDTO, DataControllerRequest request) {
        Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        ExportLOCDTO exportDTO;
        try {
            createPayloadDTO.setStatus(PARAM_STATUS_NEW); 
            createPayloadDTO.setLcCreatedOn(getCurrentDateTimeUTF());
            createPayloadDTO.setLcUpdatedOn(createPayloadDTO.getLcCreatedOn());
            exportDTO = createBusinessDelegate.createExportLetterOfCredit(createPayloadDTO, request);
        } catch (Exception e) {
            alert.prepareError("Failed to create export letter of credit request in OMS", e).log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
        }
        if (StringUtils.isNotBlank(exportDTO.getErrorMsg())) {
            result.addParam("ErrorMsg", exportDTO.getErrorMsg());
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result(), exportDTO.getErrorMsg());
        }
        JSONObject responseObj = new JSONObject(exportDTO);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    @Override
    public Result updateExportLCByBank(ExportLOCDTO inputDTO, DataControllerRequest request) {
        Result result;
        try {
            switch (inputDTO.getStatus()) {
                case PARAM_STATUS_CLOSED:
                case PARAM_STATUS_PARTIALLY_SETTLED:
                case PARAM_STATUS_CANCELLED:
                case PARAM_APPROVED:
                case PARAM_STATUS_REJECTED:
                case PARAM_STATUS_PROCESSING_by_BANK:
                case PARAM_STATUS_RETURNED_by_BANK:
                    break;
                default:
                    return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
            }
            ExportLOCDTO exportLcDTO = getBusinessDelegate.getExportLetterOfCreditById(inputDTO.getExportLCId(), request);
            exportLcDTO.setLcUpdatedOn(getCurrentDateTimeUTF());
            exportLcDTO.setStatus(inputDTO.getStatus());
            ExportLOCDTO updatedLcDTO = createBusinessDelegate.updateExportLetterOfCredit(exportLcDTO, request);
            result = JSONToResult.convert((new JSONObject(updatedLcDTO)).toString());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to Update letter of credit drawings by Bank from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result beneficiaryConsent(ExportLOCDTO inputDTO, DataControllerRequest request) {
        Result result = new Result();
        try {
            if (StringUtils.isBlank(inputDTO.getExportLCId())
                    || StringUtils.isBlank(inputDTO.getBeneficiaryConsent())
                    || (StringUtils.equals(inputDTO.getBeneficiaryConsent(), PARAM_STATUS_REJECTED) && StringUtils.isBlank(inputDTO.getReasonForRejection()))) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }
            if (!Arrays.asList(PARAM_STATUS_ACCEPTED, PARAM_STATUS_REJECTED).contains(inputDTO.getBeneficiaryConsent())) {
                return ErrorCodeEnum.ERR_12002.setErrorCode(new Result());
            }

            ExportLOCDTO exportLcDTO = getBusinessDelegate.getExportLetterOfCreditById(inputDTO.getExportLCId(), request);
            if (!Arrays.asList(PARAM_STATUS_NEW, PARAM_STATUS_RETURNED_by_BANK).contains(exportLcDTO.getStatus())) {
                return ErrorCodeEnum.ERRTF_29047.setErrorCode(new Result());
            }
            if (StringUtils.equals(inputDTO.getBeneficiaryConsent(), PARAM_STATUS_REJECTED)) {
                exportLcDTO.setReasonForRejection(inputDTO.getReasonForRejection());
            }
            exportLcDTO.setMessageToBank(inputDTO.getMessageToBank());
            exportLcDTO.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
            exportLcDTO.setLcUpdatedOn(getCurrentDateTimeUTF());
            exportLcDTO.setBeneficiaryConsent(inputDTO.getBeneficiaryConsent());
            ExportLOCDTO letterOfCredits = createBusinessDelegate.updateExportLetterOfCredit(exportLcDTO, request);
            result = JSONToResult.convert((new JSONObject(letterOfCredits)).toString());
            TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED, letterOfCredits.getExportLCId());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to Update export letter of credit beneficiary consent from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }
}
