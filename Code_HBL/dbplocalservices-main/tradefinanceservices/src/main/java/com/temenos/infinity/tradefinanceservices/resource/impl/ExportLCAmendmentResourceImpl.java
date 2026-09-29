/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CreateExportLCBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.ExportLCAmendmentBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsByIdBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.Constants;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ExportLCAmendmentResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getCurrentDateTimeUTF;


public class ExportLCAmendmentResourceImpl implements ExportLCAmendmentResource, TradeFinanceConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object getExportAmendments(FilterDTO filterDto, DataControllerRequest request, boolean filter) {

        ExportLCAmendmentBusinessDelegate amendmentsBusiness = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLCAmendmentBusinessDelegate.class);

        List<ExportLCAmendmentsDTO> responseListDto = amendmentsBusiness.getExportAmendments(request);
        Result result = new Result();
        JSONObject responseObj1 = new JSONObject();
        responseObj1.put("ExportLcAmendments", responseListDto);
        if (responseListDto == null) {
            result = JSONToResult.convert(responseObj1.toString());
            alert.prepareError("Error occurred while fetching export letter of credits amendments from backend").log();
            return result;
        }
        if (responseListDto.size() < 1) {
            result = JSONToResult.convert(responseObj1.toString());
            alert.prepareError("No export letter of credits amendments found").log();
            return result;
        }
        JSONObject responseObj = new JSONObject();
        responseObj.put("ExportLcAmendments", responseListDto);

        try {
            List<ExportLCAmendmentsDTO> filteredLOC = filterDto.filter(responseListDto);
            if (filter)
                return filteredLOC;
            responseObj1.put("ExportLcAmendments", filteredLOC);
            result = JSONToResult.convert(responseObj1.toString());

        } catch (Exception e) {
            result.addErrMsgParam("Failed to fetch the records");
            alert.prepareError("Error occurred while fetching letter of credits from backend").log();
            return result;
        }

        return result;
    }

    @Override
    public Result amendExportLetterOfCredits(ExportLCAmendmentsDTO letterOfCredit, DataControllerRequest request) {
        Result result = new Result();
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);

        ExportLCAmendmentBusinessDelegate exportLCAmendmentBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLCAmendmentBusinessDelegate.class);
        GetExportLetterOfCreditsByIdBusinessDelegate getbyidbusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GetExportLetterOfCreditsByIdBusinessDelegate.class);
        CreateExportLCBusinessDelegate exportBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CreateExportLCBusinessDelegate.class);

        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getInstance().getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);

        if (StringUtils.isBlank(letterOfCredit.getExportlcSRMSRequestId())) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }
        if (StringUtils.isBlank(letterOfCredit.getExportlcReferenceNo())) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }
        if (StringUtils.isBlank(letterOfCredit.getChargesDebitAccount())) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }
        if (StringUtils.isBlank(letterOfCredit.getOldLcAmount())) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }
        if (StringUtils.isBlank(letterOfCredit.getLcType())) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }
        if (StringUtils.isNotBlank(request.getParameter("lcIssueDate"))) {
            if (!(_isDateValidate(request.getParameter("lcIssueDate")))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("lcExpiryDate"))) {
            if (!(_isDateValidate(request.getParameter("lcExpiryDate")))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("latestShipmentDate"))) {
            if (!(_isDateValidate((request.getParameter("latestShipmentDate"))))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("amendmentReceivedDate"))) {
            if (!(_isDateValidate(request.getParameter("amendmentReceivedDate")))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("selfAcceptanceDate"))) {
            if (!(_isDateValidate(request.getParameter("selfAcceptanceDate")))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("selfRejectedDate"))) {
            if (!(_isDateValidate(request.getParameter("selfRejectedDate")))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }


        // Check whether user has the permission to the account
        String accountEnding = "";
        String errorMessage = "";
        if (StringUtils.isNotBlank(letterOfCredit.getChargesDebitAccount())) {
            if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, letterOfCredit.getChargesDebitAccount())) {
                if (letterOfCredit.getChargesDebitAccount().length() > 3)
                    accountEnding = letterOfCredit.getChargesDebitAccount().substring(letterOfCredit.getChargesDebitAccount().length() - 3);
                errorMessage = "You do not have permission to the Charges Account ending xxx" + accountEnding + ".";
                return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
            }
        }

        //get Export LC Details
        ExportLOCDTO exporlcdetails = new ExportLOCDTO();
        try {
            exporlcdetails = getbyidbusinessDelegate.getExportLetterOfCreditById(letterOfCredit.getExportlcSRMSRequestId(), request);
        } catch (Exception e) {
            alert.prepareError("Unable to get Export Letter Of Credit Details " + e).log();
            return ErrorCodeEnum.ERRTF_29057.setErrorCode(new Result());
        }

        //Validate values with the exportLC values
        if ((!StringUtils.equals(letterOfCredit.getExportlcReferenceNo(), exporlcdetails.getLcReferenceNo()))
                || (!StringUtils.equals(letterOfCredit.getExportlcSRMSRequestId(), exporlcdetails.getExportLCId()))
                || (!StringUtils.equals(letterOfCredit.getLcType(), exporlcdetails.getLcType()))
                || (!StringUtils.equals(letterOfCredit.getOldLcAmount(), exporlcdetails.getAmount()))) {
            return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
        }
        //Comparing issue Date with issue Date of ExportLC
        try {
            String issuedate1 = HelperMethods.changeDateFormat(letterOfCredit.getLcIssueDate(), Constants.TIMESTAMP_FORMAT);
            String issuedate2 = HelperMethods.changeDateFormat(exporlcdetails.getIssueDate(), Constants.TIMESTAMP_FORMAT);
            if (!(issuedate2.equals(issuedate1))) {
                alert.prepareError("Issue Date doesn't match with the exportLC issue Date").log();
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
            }
        } catch (Exception e) {
            alert.prepareError("Error" + e).log();
        }
        String amendmentNum = exporlcdetails.getAmendmentNo() != null ? exporlcdetails.getAmendmentNo() : "0";
        int amendmentNo = Integer.parseInt(amendmentNum);

        if (amendmentNo <= 0)
            amendmentNo = 1;
        else
            amendmentNo += 1;
        exporlcdetails.setAmendmentNo(String.valueOf(amendmentNo));
        letterOfCredit.setAmendmentNo(String.valueOf(amendmentNo));
        letterOfCredit.setAmendmentReceivedDate(getCurrentDateTimeUTF());
        try {
            exportBusinessDelegate.updateExportLetterOfCredit(exporlcdetails, request);
        } catch (Exception e) {
            alert.prepareError("Failed to update letter of credits in OMS").log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
        }

        //Calling Business Delegate Class
        try {
            letterOfCredit = exportLCAmendmentBusinessDelegate.amendExportLetterOfCredits(letterOfCredit, request);
            JSONObject letterOfOrderDTO = new JSONObject(letterOfCredit);
            result = JSONToResult.convert(letterOfOrderDTO.toString());
        } catch (Exception e) {

            alert.prepareError("Failed to amend letter of credits in OMS").log();
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(new Result());
        }
        return result;
    }

    public boolean _isDateValidate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            formatter.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Result updateExportLCAmendment(DataControllerRequest request) {
        Result result = new Result();
        ExportLCAmendmentBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLCAmendmentBusinessDelegate.class);
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            alert.prepareError("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_26014.setErrorCode(result);
        }

        String amendmentSRMSRequestId = request.getParameter("amendmentSRMSRequestId");
        String selfAcceptance = request.getParameter("selfAcceptance");
        String reasonForSelfRejection = request.getParameter("reasonForSelfRejection");

        if (StringUtils.isBlank(amendmentSRMSRequestId) || StringUtils.isBlank(selfAcceptance)
                || (selfAcceptance.equals(PARAM_REJECTED)) && StringUtils.isBlank(reasonForSelfRejection)) {
            diagnostic.prepareDebug("Mandatory fields are missing.").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        try {
            ExportLCAmendmentsDTO amendmentData = requestBusinessDelegate
                    .getExportLCAmendmentById(amendmentSRMSRequestId, request);

            if (amendmentData == null) {
                diagnostic.prepareDebug("Error occurred while fetching record in SRMS").log();
                return ErrorCodeEnum.ERRTF_29080.setErrorCode(result);
            }

            if (StringUtils.isBlank(amendmentData.getExportlcSRMSRequestId())) {
                diagnostic.prepareDebug("Requested amendment is not available in SRMS").log();
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(result);
            }

            if (!amendmentData.getSelfAcceptance().equals(PARAM_STATUS_PENDING)) {
                alert.prepareError("Requested amendment can't be updated").log();
                return ErrorCodeEnum.ERRTF_29073.setErrorCode(result);
            }

            if (selfAcceptance.equals(PARAM_STATUS_ACCEPTED)) {
                amendmentData.setAmendmentStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
                amendmentData.setSelfAcceptance(PARAM_STATUS_ACCEPTED);
                amendmentData.setSelfAcceptanceDate(getCurrentDateTimeUTF());
            } else if (selfAcceptance.equals(PARAM_REJECTED)) {
                amendmentData.setAmendmentStatus(PARAM_REJECTED);
                amendmentData.setSelfAcceptance(PARAM_REJECTED);
                amendmentData.setSelfRejectedDate(getCurrentDateTimeUTF());
                amendmentData.setReasonForSelfRejection(reasonForSelfRejection);
            } else {
                alert.prepareError("Invalid status found in payload").log();
                return ErrorCodeEnum.ERRTF_29068.setErrorCode(result);
            }

            ExportLCAmendmentsDTO updatedAmendment = requestBusinessDelegate.updateExportLCAmendment(amendmentData, request);

            if (updatedAmendment == null) {
                diagnostic.prepareDebug("Error occurred while updating export amendment").log();
                return ErrorCodeEnum.ERRTF_29081.setErrorCode(result);
            }

            JSONObject responseObj = new JSONObject(updatedAmendment);
            result = JSONToResult.convert(responseObj.toString());
            TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT, updatedAmendment.getAmendmentSRMSRequestId());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Error occurred while updating export amendment" + e).log();
            return ErrorCodeEnum.ERRTF_29081.setErrorCode(result);
        }
    }

    @Override
    public Result getExportLCAmendmentById(DataControllerRequest request) {
        Result result = new Result();
        ExportLCAmendmentBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(ExportLCAmendmentBusinessDelegate.class);
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            alert.prepareError("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_26014.setErrorCode(result);
        }

        String amendmentSRMSRequestId = request.getParameter("amendmentSRMSRequestId");
        if (StringUtils.isBlank(amendmentSRMSRequestId)) {
            diagnostic.prepareDebug("Mandatory fields are missing.").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
        }

        try {
            ExportLCAmendmentsDTO amendmentData = requestBusinessDelegate
                    .getExportLCAmendmentById(amendmentSRMSRequestId, request);

            if (amendmentData == null) {
                diagnostic.prepareDebug("Error occurred while fetching record in SRMS").log();
                return ErrorCodeEnum.ERRTF_29080.setErrorCode(result);
            }
            if (StringUtils.isBlank(amendmentData.getExportlcSRMSRequestId())) {
                diagnostic.prepareDebug("Requested amendment is not available in SRMS").log();
                return ErrorCodeEnum.ERRTF_29057.setErrorCode(result);
            }
            JSONObject responseObj = new JSONObject(amendmentData);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (Exception e) {
            diagnostic.prepareDebug("Error occurred while fetching export amendment" + e).log();
            return ErrorCodeEnum.ERRTF_29080.setErrorCode(result);
        }
    }

    @Override
    public Result updateExportLCAmendmentByBank(ExportLCAmendmentsDTO inputDTO, DataControllerRequest request) {
        AlertsEnum alertToPush;
        Result result;
        try {
            ExportLCAmendmentBusinessDelegate requestBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ExportLCAmendmentBusinessDelegate.class);
            ExportLCAmendmentsDTO amendmentData = requestBusinessDelegate.getExportLCAmendmentById(inputDTO.getAmendmentSRMSRequestId(), request);

            switch (inputDTO.getAmendmentStatus()) {
                case PARAM_STATUS_PROCESSING_by_BANK:
                    alertToPush = AlertsEnum.EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL;
                    break;
                case PARAM_STATUS_APPROVED:
                    alertToPush = AlertsEnum.EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED;
                    break;
                case PARAM_STATUS_REJECTED:
                    alertToPush = AlertsEnum.EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED;
                    break;
                default:
                    return ErrorCodeEnum.ERRTF_29077.setErrorCode(new Result());
            }
            amendmentData.setAmendmentStatus(inputDTO.getAmendmentStatus());
            ExportLCAmendmentsDTO updatedAmendment = requestBusinessDelegate.updateExportLCAmendment(amendmentData, request);
            result = JSONToResult.convert((new JSONObject(updatedAmendment)).toString());
            TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, updatedAmendment.getAmendmentSRMSRequestId());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            diagnostic.prepareDebug("Failed to Update Export letter of credit amendment by Bank from OMS " + e).log();
            return ErrorCodeEnum.ERRTF_29048.setErrorCode(new Result());
        }
        return result;
    }
}
