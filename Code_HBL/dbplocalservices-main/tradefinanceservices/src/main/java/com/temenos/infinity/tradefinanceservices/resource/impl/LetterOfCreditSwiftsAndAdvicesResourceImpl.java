/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GetExportLetterOfCreditsByIdBackendDelegate;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.LetterOfCreditDrawingsBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteeLCAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.LetterOfCreditSwiftsAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditSwiftsAndAdvicesResource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_STATUS_APPROVED;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;

public class LetterOfCreditSwiftsAndAdvicesResourceImpl implements LetterOfCreditSwiftsAndAdvicesResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    LetterOfCreditSwiftsAndAdvicesBusinessDelegate locSwiftMessagesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(LetterOfCreditSwiftsAndAdvicesBusinessDelegate.class);
    GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate guaranteeSwiftsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);
    GetExportLetterOfCreditsByIdBackendDelegate exportLcBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(GetExportLetterOfCreditsByIdBackendDelegate.class);
    LetterOfCreditDrawingsBackendDelegate importDrawingBackendDelegate = DBPAPIAbstractFactoryImpl.getBackendDelegate(LetterOfCreditDrawingsBackendDelegate.class);
    GuaranteeLCAmendmentsBusinessDelegate gAmendmentsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);

    @Override
    public Result createLetterOfCreditSwiftAndAdvices(SwiftsAndAdvisesDTO swiftAndAdvices, DataControllerRequest request) throws com.temenos.infinity.api.commons.exception.ApplicationException {
        SwiftsAndAdvisesDTO swiftAndAdvicesResponse = new SwiftsAndAdvisesDTO();
        Result result = new Result();

        if (StringUtils.isBlank(swiftAndAdvices.getModule())) {
            diagnostic.prepareDebug("Mandatory fields are missing").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        //validate Input
        if (!_validateSwiftAdvices(swiftAndAdvices, request)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result(), "Input Validation Failed");
        }
        if (swiftAndAdvices.getModule().equalsIgnoreCase("EXLC")) {
            ExportLOCDTO exportLc;
            if (StringUtils.isBlank(swiftAndAdvices.getSwiftCategory())
                    || StringUtils.isBlank(swiftAndAdvices.getBeneficiaryName())
                    || StringUtils.isBlank(swiftAndAdvices.getSwiftMessage())
                    || StringUtils.isBlank(swiftAndAdvices.getSwiftMessageType())
                    || StringUtils.isBlank(swiftAndAdvices.getSwiftDate())
                    || StringUtils.isBlank(swiftAndAdvices.getDrawingsSrmsRequestOrderID())) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }

            exportLc = exportLcBackendDelegate.getExportLetterOfCreditById(swiftAndAdvices.getDrawingsSrmsRequestOrderID(), request);
            if (!exportLc.getStatus().equalsIgnoreCase("Completed"))
                return ErrorCodeEnum.ERRTF_29068.setErrorCode(new Result(),
                        "Requested Drawing status is in " + exportLc.getStatus());

            try {
                swiftAndAdvicesResponse = locSwiftMessagesBusinessDelegate.createSwiftsAndAdvises(swiftAndAdvices, exportLc, request);
            } catch (ApplicationException e) {
                alert.prepareError("Create Order Failed in resources" + e).log();
                return ErrorCodeEnum.ERRTF_29064.setErrorCode(new Result(), "Failed to Create Swift advices");
            }

        } else if (swiftAndAdvices.getModule().equalsIgnoreCase("IMDR")) {
            DrawingsDTO drawings = new DrawingsDTO();
            if (StringUtils.isBlank(swiftAndAdvices.getSwiftCategory())
                    || StringUtils.isBlank(swiftAndAdvices.getBeneficiaryName())
                    || StringUtils.isBlank(swiftAndAdvices.getSwiftMessage())
                    || StringUtils.isBlank(swiftAndAdvices.getSwiftMessageType())
                    || StringUtils.isBlank(swiftAndAdvices.getSwiftDate())
                    || StringUtils.isBlank(swiftAndAdvices.getDrawingsSrmsRequestOrderID())) {
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
            }

            try {
                drawings = importDrawingBackendDelegate.getImportDrawingDetailsById(request, swiftAndAdvices.getDrawingsSrmsRequestOrderID());
            } catch (ApplicationException e) {
                alert.prepareError("Error occurred while fetching drawing details. " + e).log();
            }

            if (StringUtils.isBlank(drawings.getPaymentStatus()))
                drawings.setPaymentStatus(drawings.getStatus());

            if (!((drawings.getStatus().equalsIgnoreCase("Completed"))
                    && (drawings.getPaymentStatus().equalsIgnoreCase("Settled"))))
                return ErrorCodeEnum.ERRTF_29068.setErrorCode(new Result(),
                        "Requested Drawing status is in " + drawings.getPaymentStatus());

            try {
                swiftAndAdvicesResponse = locSwiftMessagesBusinessDelegate.createSwiftsAndAdvises(swiftAndAdvices, drawings, request);
            } catch (ApplicationException e) {
                alert.prepareError("Create Order Failed in resources" + e).log();
                return ErrorCodeEnum.ERRTF_29064.setErrorCode(new Result(), "Failed to Create Swift advices");

            }
        } else if (swiftAndAdvices.getModule().equalsIgnoreCase("GUAM")) {
            if (StringUtils.isBlank(swiftAndAdvices.getGuaranteesAmendId())
                    || StringUtils.isBlank(swiftAndAdvices.getReceiver())
                    || StringUtils.isBlank(swiftAndAdvices.getSender())
                    || StringUtils.isBlank(swiftAndAdvices.getMessageType())
                    || StringUtils.isBlank(swiftAndAdvices.getMessage())
                    || StringUtils.isBlank(swiftAndAdvices.getMessageDate())
                    || StringUtils.isBlank(swiftAndAdvices.getAdviceName())) {
                diagnostic.prepareDebug("Mandatory fields are missing").log();
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
            }

            try {
                GuaranteeLCAmendmentsDTO guaranteeDto = gAmendmentsBusinessDelegate.getGuaranteeLCAmendmentById(swiftAndAdvices.getGuaranteesAmendId(), request);
                if (!guaranteeDto.getAmendStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)) {
                    return ErrorCodeEnum.ERRTF_29068.setErrorCode(new Result(),
                            "Requested Amendment status is in " + guaranteeDto.getAmendStatus());
                }
                swiftAndAdvicesResponse = guaranteeSwiftsBusinessDelegate.createSwiftsAndAdvises(swiftAndAdvices, request);
            } catch (ApplicationException e) {
                alert.prepareError("Create Order Failed in resources" + e).log();
                return ErrorCodeEnum.ERRTF_29064.setErrorCode(new Result(), "Failed to Create Swift advices");
            }
        } else if (swiftAndAdvices.getModule().equalsIgnoreCase("RGUA")
                || swiftAndAdvices.getModule().equalsIgnoreCase("RGAM")
                || swiftAndAdvices.getModule().equalsIgnoreCase("RGCL")
                || swiftAndAdvices.getModule().equalsIgnoreCase("IGCL")
                || swiftAndAdvices.getModule().equalsIgnoreCase("INCL")
                || swiftAndAdvices.getModule().equalsIgnoreCase("INCA")) {

            if (StringUtils.isBlank(swiftAndAdvices.getOrderId())
                    || StringUtils.isBlank(swiftAndAdvices.getbCode())
                    || StringUtils.isBlank(swiftAndAdvices.getBic())
                    || StringUtils.isBlank(swiftAndAdvices.getType())
                    || StringUtils.isBlank(swiftAndAdvices.getTransferDateOrTime())) {
                diagnostic.prepareDebug("Mandatory fields are missing").log();
                return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
            }

            try {
                swiftAndAdvices.setCreatedDate(getCurrentDateTimeUTF());
                swiftAndAdvicesResponse = guaranteeSwiftsBusinessDelegate.createSwiftsAndAdvises(swiftAndAdvices, request);
            } catch (Exception e) {
                alert.prepareError("Create Order Failed in resources" + e).log();
            }
        } else {
            diagnostic.prepareDebug("Invalid Input").log();
            return ErrorCodeEnum.ERR_12002.setErrorCode(new Result(), "Invalid module found in request");
        }

        JSONObject responseObj = new JSONObject(swiftAndAdvicesResponse);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    private boolean _validateSwiftAdvices(SwiftsAndAdvisesDTO swiftAndAdvices, DataControllerRequest request) {
        String alphaNumericMax35CharsWithSpace = "^[a-zA-Z0-9  <>?:\\/\\\'-+=,;()#]{0,35}$";
        Matcher matcher;
        boolean isValid = false;
        Pattern alphaNumericMax35CharsWithSpacePattern = Pattern.compile(alphaNumericMax35CharsWithSpace);
        if (swiftAndAdvices.getBeneficiaryName() != null && swiftAndAdvices.getBeneficiaryName().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpacePattern.matcher(swiftAndAdvices.getBeneficiaryName());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (swiftAndAdvices.getSwiftCategory() != null && swiftAndAdvices.getSwiftCategory().trim().length() != 0) {
            if (!(swiftAndAdvices.getSwiftCategory().equalsIgnoreCase("Inward") || swiftAndAdvices.getSwiftCategory().equalsIgnoreCase("Outward")))
                return false;
        }
        if (swiftAndAdvices.getSwiftMessage() != null && swiftAndAdvices.getSwiftMessage().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpacePattern.matcher(swiftAndAdvices.getSwiftMessage());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (swiftAndAdvices.getSwiftMessageType() != null && swiftAndAdvices.getSwiftMessageType().trim().length() != 0) {
            matcher = alphaNumericMax35CharsWithSpacePattern.matcher(swiftAndAdvices.getSwiftMessageType());
            isValid = matcher.matches();
            if (isValid == false)
                return false;
        }
        if (StringUtils.isNotBlank(swiftAndAdvices.getSwiftDate())) {
            String date = request.getParameter("swiftDate");
            if ((date.matches("([0-9]{2}) ([A-Z]{3}) ([0-9]{4})"))) {
                isValid = _isDateValidate(swiftAndAdvices.getSwiftDate());
                if (isValid == false)
                    return false;
            }
        }
        return true;
    }

    public boolean _isDateValidate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Date Date;
        Date CurrentDate = new Date();
        String CurrDate = formatter.format(CurrentDate);
        try {
            CurrentDate = formatter.parse(CurrDate);
            CurrentDate = DateUtils.addDays(CurrentDate, -1);
            Date = formatter.parse(date);
        } catch (ParseException e) {
            alert.prepareError("Error occurred while parsing date. " + e).log();
            return false;
        }
        if (!Date.after(CurrentDate))
            return false;
        return true;
    }

    @Override
    public Result getLetterOfCreditSwiftAndAdvices(DataControllerRequest request) {
        List<SwiftsAndAdvisesDTO> swiftAdvices;
        Result result = new Result();

        if (StringUtils.isBlank(request.getParameter("module"))) {
            diagnostic.prepareDebug("Mandatory fields are missing").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        try {
            String module = request.getParameter("module");
            if (module.equalsIgnoreCase("IMDR")
                    || module.equalsIgnoreCase("EXLC")) {
                swiftAdvices = locSwiftMessagesBusinessDelegate.getSwiftsAndAdvises(request);
            } else if (module.equalsIgnoreCase("GUAM")
                    || module.equalsIgnoreCase("RGUA")
                    || module.equalsIgnoreCase("RGAM")
                    || module.equalsIgnoreCase("RGCL")
                    || module.equalsIgnoreCase("INCL")
                    || module.equalsIgnoreCase("INCA")
                    || module.equalsIgnoreCase("IGCL")) {
                swiftAdvices = guaranteeSwiftsBusinessDelegate.getGuaranteeSwiftAdvices(request);
            } else {
                diagnostic.prepareDebug("Invalid Input").log();
                return ErrorCodeEnum.ERR_12002.setErrorCode(new Result(), "Invalid module found in request");
            }
        } catch (ApplicationException e) {
            alert.prepareError("Get Order Failed in resources" + e).log();
            return ErrorCodeEnum.ERRTF_29063.setErrorCode(new Result());
        }

        JSONArray SwiftAdvices = new JSONArray(swiftAdvices);
        JSONObject responseObj = new JSONObject();
        responseObj.put("SwiftMessages", SwiftAdvices);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

}
