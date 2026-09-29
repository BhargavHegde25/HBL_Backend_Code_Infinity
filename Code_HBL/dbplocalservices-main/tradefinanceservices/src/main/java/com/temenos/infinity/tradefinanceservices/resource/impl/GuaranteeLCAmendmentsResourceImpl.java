/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteeLCAmendmentsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.Constants;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteeLCAmendmentsDTO;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GuaranteeLCAmendmentsResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices.TRANSACT_TF_CREATE_ISSUED_GUARANTEE_AMENDMENT;
import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12007;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.getHeadersMap;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.isTransactEnabled;


public class GuaranteeLCAmendmentsResourceImpl implements GuaranteeLCAmendmentsResource, TradeFinanceConstants {

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getGuaranteeLCAmendments(FilterDTO filterDTO, DataControllerRequest request) {
        Result result = new Result();
        JSONObject responseObj = new JSONObject();
        GuaranteeLCAmendmentsBusinessDelegate amendmentsBusiness = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);

        List<GuaranteeLCAmendmentsDTO> responseListDto = amendmentsBusiness.getGuaranteeLCAmendments(request);

        if (responseListDto == null) {
            result = JSONToResult.convert(responseObj.toString());
            alert.prepareError("Error occurred while fetching guarantees lc amendments from backend").log();
            return result;
        }

        try {
            List<GuaranteeLCAmendmentsDTO> filteredRecords = filterDTO.filter(responseListDto);
            responseObj.put("GuaranteeLCAmendments", filteredRecords);
            result = JSONToResult.convert(responseObj.toString());
        } catch (Exception e) {
            result.addErrMsgParam("Failed to fetch the records");
            alert.prepareError("Error occurred while fetching guarantees lc amendments from backend").log();
        }
        return result;
    }

    @Override
    public Result createGuaranteeLCAmendment(GuaranteeLCAmendmentsDTO amendmentPayloadDTO,
                                             DataControllerRequest request) {
        Result result = new Result();
        GuaranteeLCAmendmentsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);
        GuaranteesBusinessDelegate guaranteesBusiness = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteesBusinessDelegate.class);

        if (StringUtils.isBlank(amendmentPayloadDTO.getGuaranteesSRMSId())
                || StringUtils.isBlank(amendmentPayloadDTO.getCurrency())
                || StringUtils.isBlank(amendmentPayloadDTO.getAmount())
                || StringUtils.isBlank(amendmentPayloadDTO.getProductType())) {
            return ErrorCodeEnum.ERRTF_29065.setErrorCode(new Result());
        }

        if (StringUtils.isNotBlank(request.getParameter("issueDate"))) {
            if (!(_isDateValidate(request.getParameter("issueDate"), "yyyy-MM-dd"))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("expiryDate"))) {
            if (!(_isDateValidate(request.getParameter("expiryDate"), "MM/dd/yyyy") || _isDateValidate(request.getParameter("expiryDate"), "yyyy-MM-dd"))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("amendExpiryDate"))) {
            if (!(_isDateValidate((request.getParameter("amendExpiryDate")), "yyyy-MM-dd"))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }
        if (StringUtils.isNotBlank(request.getParameter("amendmentEffectiveDate"))) {
            if (!(_isDateValidate(request.getParameter("amendmentEffectiveDate"), "yyyy-MM-dd"))) {
                return ErrorCodeEnum.ERRTF_29060.setErrorCode(new Result());
            }
        }

//		TODO: check other mandatory fields
        String guaranteeSRMSId = amendmentPayloadDTO.getGuaranteesSRMSId();
        if (StringUtils.isBlank(guaranteeSRMSId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }

        GuaranteesDTO guaranteeDto = guaranteesBusiness.getGuaranteesById(guaranteeSRMSId, request);
        if (StringUtils.isBlank(guaranteeDto.getGuaranteesSRMSId())) {
            return ErrorCodeEnum.ERRTF_29053.setErrorCode(result);
        }

        if (!guaranteeDto.getStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)) {
            diagnostic.prepareDebug("Guarantee is not eligible to create amendment " + guaranteeDto.getStatus()).log();
            return ErrorCodeEnum.ERR_12001.setErrorCode(result);
        }

        JSONObject lastAmendment = StringUtils.isNotBlank(guaranteeDto.getAmendmentNo()) ? new JSONObject(guaranteeDto.getAmendmentNo()) : new JSONObject();
        String lastAmendmentStatus = lastAmendment.has("amendmentStatus") ? lastAmendment.getString("amendmentStatus") : PARAM_STATUS_APPROVED;
        if (!lastAmendmentStatus.equals(PARAM_STATUS_APPROVED)) {
            diagnostic.prepareDebug("Guarantee is not eligible to create amendment " + lastAmendmentStatus).log();
            return ErrorCodeEnum.ERR_12001.setErrorCode(result, "Guarantee is not eligible to create amendment");
        }
        int amendmentNo = lastAmendment.has("amendmentNo") ? Integer.parseInt(lastAmendment.getString("amendmentNo")) : 0;
        amendmentNo = amendmentNo <= 0 ? 1 : amendmentNo + 1;
        lastAmendment.put("amendmentNo", String.valueOf(amendmentNo));
        lastAmendment.put("amendmentStatus", PARAM_STATUS_SUBMITTED_TO_BANK);
        guaranteeDto.setAmendmentNo(String.valueOf(lastAmendment));
        amendmentPayloadDTO.setAmendmentNo(String.valueOf(amendmentNo));

        //Validate values with the Guarantees values
        if ((!StringUtils.equals(guaranteeDto.getGuaranteesSRMSId(), amendmentPayloadDTO.getGuaranteesSRMSId()))
                || (!StringUtils.equals(guaranteeDto.getProductType(), amendmentPayloadDTO.getProductType()))
                || (!StringUtils.equals(guaranteeDto.getCurrency(), amendmentPayloadDTO.getCurrency()))) {
            return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
        }

        //Comparing issue Date with issue Date of Guarantees
        try {
            String issuedate1 = HelperMethods.changeDateFormat(guaranteeDto.getIssueDate(), Constants.TIMESTAMP_FORMAT);
            String issuedate2 = HelperMethods.changeDateFormat(amendmentPayloadDTO.getIssueDate(), Constants.TIMESTAMP_FORMAT);
            if (!(issuedate2.equals(issuedate1))) {
                alert.prepareError("Issue Date does not match with the guarantees issue Date").log();
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(new Result());
            }
        } catch (Exception e) {
            alert.prepareError("Error" + e).log();
        }

        if (StringUtils.isNotBlank(amendmentPayloadDTO.getCancellationStatus()) &&
                amendmentPayloadDTO.getCancellationStatus().equalsIgnoreCase(PARAM_STATUS_REQUESTED)) {
            amendmentPayloadDTO.setCancellationStatus(PARAM_STATUS_REQUESTED);
        }
        amendmentPayloadDTO.setAmendStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        amendmentPayloadDTO.setAmendRequestedDate(getCurrentDateTimeUTF());

        if (isTransactEnabled()) {
            if (StringUtils.isBlank(amendmentPayloadDTO.getRelatedTransactionReference())) {
                return ErrorCodeEnum.ERRTF_29045.setErrorCode(result);
            }
            GuaranteeLCAmendmentsDTO responseDto = createAmendmentInTransact(request, amendmentPayloadDTO);
            if (StringUtils.isBlank(responseDto.getAmendmentReference()))
                return JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
        }
        GuaranteeLCAmendmentsDTO guaranteeCreateResDTO = businessDelegate.createGuaranteeLCAmendment(amendmentPayloadDTO, request);
        if (StringUtils.isBlank(guaranteeCreateResDTO.getAmendmentSRMSRequestId())) {
            return ErrorCodeEnum.ERRTF_29045.setErrorCode(result);
        }

        // update amendmentNo, amendmentStatus  in Guarantees record
        GuaranteesDTO updateGuarantees = guaranteesBusiness.updateGuarantees(guaranteeDto, request);
        if (StringUtils.isNotBlank(updateGuarantees.getErrorMsg())) {
            return ErrorCodeEnum.ERR_12000.setErrorCode(result);
        }

        result = JSONToResult.convert(new JSONObject(guaranteeCreateResDTO).toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, AlertsEnum.GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL, guaranteeCreateResDTO.getAmendmentSRMSRequestId());
        return result;
    }

    @Override
    public Result getGuaranteeLCAmendmentById(DataControllerRequest request) {
        Result result = new Result();
        GuaranteeLCAmendmentsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);
        GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate requestSwiftBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesAmendmentsSwiftAndAdvicesBusinessDelegate.class);

        String amendmentSRMSId = request.getParameter("amendmentSRMSRequestId");
        if (StringUtils.isBlank(amendmentSRMSId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(result);
        }
        GuaranteeLCAmendmentsDTO responseDto = new GuaranteeLCAmendmentsDTO();
        List<SwiftsAndAdvisesDTO> amendSwiftAdvicesList = new ArrayList<SwiftsAndAdvisesDTO>();
        try {
            responseDto = businessDelegate.getGuaranteeLCAmendmentById(amendmentSRMSId, request);
            List<SwiftsAndAdvisesDTO> swiftAdvicesList = requestSwiftBusinessDelegate.getGuaranteeSwiftAdvices(request);
            for (SwiftsAndAdvisesDTO swiftAdvice : swiftAdvicesList) {
                if (StringUtils.isNotBlank(swiftAdvice.getGuaranteesAmendId()) && swiftAdvice.getGuaranteesAmendId().equalsIgnoreCase(amendmentSRMSId)) {
                    amendSwiftAdvicesList.add(swiftAdvice);
                }
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching amendment details. Error: " + e).log();
        }

        JSONObject responseObj = new JSONObject(responseDto);
        responseObj.put("AmendmentHistory", _getAmendmentHistory(responseDto));
        responseObj.put("SwiftsAndAdvices", new JSONArray(amendSwiftAdvicesList));
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    public Result updateGuaranteeAmendment(HashMap inoutParams, DataControllerRequest request) throws IOException {
        Result result = new Result();
        GuaranteeLCAmendmentsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);
        String amendmentSRMSId = request.getParameter("amendmentSRMSRequestId");
        if (StringUtils.isBlank(amendmentSRMSId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(result);
        }
        GuaranteeLCAmendmentsDTO inputDto = JSONUtils.parse(new JSONObject(inoutParams).toString(), GuaranteeLCAmendmentsDTO.class);
        GuaranteeLCAmendmentsDTO guaranteeLCAmendmentsDTODto = businessDelegate.getGuaranteeLCAmendmentById(amendmentSRMSId, request);
        inputDto.setAmendStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        guaranteeLCAmendmentsDTODto.setAmendStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
        guaranteeLCAmendmentsDTODto = guaranteeLCAmendmentsDTODto.swap(inputDto);
        JSONObject inputObj = new JSONObject(inputDto);

        GuaranteeLCAmendmentsDTO responseDto = businessDelegate.updateGuaranteeAmendment(guaranteeLCAmendmentsDTODto, inputObj, request);
        JSONObject responseObj = new JSONObject(responseDto);
        responseObj.put("AmendmentHistory", _getAmendmentHistory(responseDto));
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    @Override
    public Result generatePdfGuaranteeLcAmendment(DataControllerRequest request) {
        Result result = new Result();
        GuaranteeLCAmendmentsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);

        result = getGuaranteeLCAmendmentById(request);
        JSONObject amendmentData = new JSONObject(ResultToJSON.convert(result));
        byte[] pdfBytes = businessDelegate.generatePdfGuaranteeLcAmendment(amendmentData, request);
        if (ArrayUtils.isEmpty(pdfBytes)) {
            alert.prepareError("Failed in trade finance pdf generation").log();
            return ErrorCodeEnum.ERRTF_29056.setErrorCode(result);
        }
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(PREFIX_GUARANTEE_AMENDMENT);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(pdfBytes), 120);
        result = new Result();
        result.addParam("fileId", fileId);
        return result;
    }

    public Result updateGuaranteeAmendmentByBank(HashMap inputParams, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        Result result = new Result();
        GuaranteeLCAmendmentsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteeLCAmendmentsBusinessDelegate.class);
        GuaranteesBusinessDelegate guaranteesBusiness = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GuaranteesBusinessDelegate.class);

        String amendmentSRMSId = (String) inputParams.get("amendmentSRMSRequestId");
        String status = (String) inputParams.get("status");
        String reasonForReturn = (String) inputParams.get("reasonForReturn");

        if (StringUtils.isBlank(status) || StringUtils.isBlank(amendmentSRMSId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(result);
        }
        GuaranteeLCAmendmentsDTO responseDto = businessDelegate.getGuaranteeLCAmendmentById(amendmentSRMSId, request);
        if (status.equals(responseDto.getAmendStatus())
                || responseDto.getAmendStatus().equalsIgnoreCase(PARAM_STATUS_APPROVED)
                || responseDto.getAmendStatus().equalsIgnoreCase(PARAM_STATUS_REJECTED_BY_BANK)) {
            return ErrorCodeEnum.ERR_12003.setErrorCode(result);
        }
        switch (status) {
            case PARAM_STATUS_RETURNED_BY_BANK:
                if (StringUtils.isBlank(reasonForReturn)) {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(result);
                }
                responseDto.setReasonForReturned(reasonForReturn);
                int historyCount = responseDto.getHistoryCount() != null ? Integer.parseInt(responseDto.getHistoryCount()) + 1 : 1;
                responseDto.setHistoryCount(String.valueOf(historyCount));
                responseDto.setReasonForReturned(reasonForReturn);
                responseDto.setRejectedDate(getCurrentDateTimeUTF());
                break;
            case PARAM_STATUS_REJECTED_BY_BANK:
                if (StringUtils.isBlank(reasonForReturn)) {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(result);
                }
                responseDto.setRejectedReason(reasonForReturn);
                responseDto.setRejectedDate(getCurrentDateTimeUTF());
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_AMENDMENT_REJECTED;
                break;
            case PARAM_STATUS_APPROVED:
                responseDto.setAmendStatus(PARAM_STATUS_APPROVED);
                responseDto.setApprovedDate(getCurrentDateTimeUTF());

                GuaranteesDTO guaranteeDetails = guaranteesBusiness.getGuaranteesById(responseDto.getGuaranteesSRMSId(), request);

                // Guarantee - field: amendmentNo - holds last amendment status and amendment number
                JSONObject lastAmendment = new JSONObject(guaranteeDetails.getAmendmentNo());
                lastAmendment.put("amendmentStatus", PARAM_STATUS_APPROVED);
                guaranteeDetails.setAmendmentNo(String.valueOf(lastAmendment));

                if (StringUtils.isNotBlank(responseDto.getCancellationStatus()) && responseDto.getCancellationStatus().equals(PARAM_STATUS_REQUESTED)) {
                    guaranteeDetails.setStatus(PARAM_STATUS_CANCELLED);
                    responseDto.setCancellationStatus(PARAM_STATUS_CANCELLED);
                }

                // Calling Business Delegate to update Guarantee details
                GuaranteesDTO updateResponseDto = guaranteesBusiness.updateGuarantees(guaranteeDetails, request);
                if (StringUtils.isNotBlank(updateResponseDto.getErrorMsg())) {
                    return ErrorCodeEnum.ERR_12000.setErrorCode(result);
                }
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_AMENDMENT_APPROVED;
                break;
            case PARAM_STATUS_PROCESSING_WITH_BANK:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL;
                break;
            default:
                diagnostic.prepareDebug("Invalid Status found in payload").log();
                return ErrorCodeEnum.ERRTF_29077.setErrorCode(result);
        }

        responseDto.setAmendStatus(status);
        GuaranteeLCAmendmentsDTO updateResponse = businessDelegate.updateGuaranteeAmendmentByBank(responseDto, request);

        JSONObject responseObj = new JSONObject(updateResponse);
        responseObj.put("AmendmentHistory", _getAmendmentHistory(responseDto));
        result = JSONToResult.convert(responseObj.toString());
        TradeFinanceCommonUtils.setAlertDataInResult(result, alertToPush, responseDto.getAmendmentSRMSRequestId());
        return result;
    }

    public String getCurrentDateTimeUTF() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
        return dateFormat.format(new Date());
    }

    private JSONObject _getAmendmentHistory(GuaranteeLCAmendmentsDTO responseDto) {
        JSONObject historyObj = new JSONObject();
        if (StringUtils.isNotBlank(responseDto.getAmendmentHistory1()))
            historyObj.put("amendmentHistory1", responseDto.getAmendmentHistory1());
        if (StringUtils.isNotBlank(responseDto.getAmendmentHistory2()))
            historyObj.put("amendmentHistory2", responseDto.getAmendmentHistory2());
        if (StringUtils.isNotBlank(responseDto.getAmendmentHistory3()))
            historyObj.put("amendmentHistory3", responseDto.getAmendmentHistory3());
        if (StringUtils.isNotBlank(responseDto.getAmendmentHistory4()))
            historyObj.put("amendmentHistory4", responseDto.getAmendmentHistory4());
        if (StringUtils.isNotBlank(responseDto.getAmendmentHistory5()))
            historyObj.put("amendmentHistory5", responseDto.getAmendmentHistory5());
        return historyObj;
    }

    public boolean _isDateValidate(String date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            formatter.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private GuaranteeLCAmendmentsDTO createAmendmentInTransact(DataControllerRequest request, GuaranteeLCAmendmentsDTO inputDto) {
        Map<String, Object> inputMap;
        Result result;
        GuaranteeLCAmendmentsDTO responseDTO = new GuaranteeLCAmendmentsDTO();
        try {
            inputDto.setAmendmentEffectiveDate(inputDto.getAmendmentEffectiveDate().replace("-", ""));
            inputMap = new ObjectMapper().convertValue(inputDto, Map.class);
            inputMap.put("changeAmount", inputDto.getAmendAmount());
            result = CommonUtils.callIntegrationService(request, inputMap, getHeadersMap(request), TRANSACT_TF_CREATE_ISSUED_GUARANTEE_AMENDMENT.getServiceName(), TRANSACT_TF_CREATE_ISSUED_GUARANTEE_AMENDMENT.getOperationName(), false);
            Record headerData = result.getRecordById("header");
            if (StringUtils.equals(headerData.getParamValueByName("status"), "success"))
                responseDTO.setAmendmentReference(headerData.getParamValueByName("id"));
            else if (StringUtils.equals(headerData.getParamValueByName("status"), "failed")) {
                responseDTO.setDbpErrCode(ERR_12007.getErrorCodeAsString());
                responseDTO.setDbpErrMsg(ERR_12007.getErrorMessage());
                responseDTO.setErrorDetails(result.getRecordById("error").getDatasetById("errorDetails").getAllRecords().toString());
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating invoice in transact", e).log();
            responseDTO.setDbpErrCode(ERR_12007.getErrorCodeAsString());
            responseDTO.setDbpErrMsg(ERR_12007.getErrorMessage());
            responseDTO.setErrorDetails("Backend Failed");
        }

        return responseDTO;
    }
}
