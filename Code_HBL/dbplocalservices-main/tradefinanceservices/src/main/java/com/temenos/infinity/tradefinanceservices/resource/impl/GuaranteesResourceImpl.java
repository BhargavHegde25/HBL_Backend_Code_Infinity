/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.GuaranteesBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GuaranteesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.ClauseDTO;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GuaranteesResource;
import com.temenos.infinity.tradefinanceservices.utils.AlertsEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices.*;
import static com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum.ERR_12007;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

public class GuaranteesResourceImpl implements GuaranteesResource, TradeFinanceConstants {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final GuaranteesBusinessDelegate guaranteesBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GuaranteesBusinessDelegate.class);
    private static final AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);


    public Result createGuarantees(String methodId, Map<String, Object> inputParams, DataControllerRequest request)
            throws IOException {
        AlertsEnum alertToPush = null;
        Result result = new Result();
        inputParams.values().removeAll(Collections.singletonList(null));
        GuaranteesDTO guaranteeInputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), GuaranteesDTO.class);

        // Getting customer id from session
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        if (StringUtils.isNotBlank(guaranteeInputDto.getInstructionCurrencies())) {
            JSONArray instructionCurrencies = new JSONArray(guaranteeInputDto.getInstructionCurrencies());
            for (int i = 0; i < instructionCurrencies.length(); i++) {
                JSONObject currency = instructionCurrencies.getJSONObject(0);
                String checkAccount = (String) currency.get("account");
                if (StringUtils.isNotBlank(checkAccount)) {
                    if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, checkAccount)) {
                        String accountEnding = "";
                        if (checkAccount.length() > 3)
                            accountEnding = checkAccount.substring(checkAccount.length() - 3);
                        String errorMessage = "You do not have permission to the Charges Account ending xxx"
                                + accountEnding + ".";
                        return ErrorCodeEnum.ERR_10118.setErrorCode(result, errorMessage);
                    }

                }
            }
        }

        if (StringUtils.isNotBlank(guaranteeInputDto.getBeneficiaryDetails())) {
            JSONArray beneficiaryDetails = new JSONArray(guaranteeInputDto.getBeneficiaryDetails());
            JSONObject beneficiary = beneficiaryDetails.getJSONObject(0);
            guaranteeInputDto.setTotalBeneficiaries(String.valueOf(beneficiaryDetails.length()));
            guaranteeInputDto.setBeneficiaryName((String) beneficiary.get("beneficiaryName"));
        }

        boolean isDraft = methodId.equalsIgnoreCase(TradeFinanceConstants.SAVEGUARANTEES_METHODID) || methodId.equalsIgnoreCase(TradeFinanceConstants.DELETEGUARANTEE_METHODID);
        inputParams.remove("clauseConditions");
        if (!validateInputDetails(guaranteeInputDto, isDraft, inputParams)) {
            return ErrorCodeEnum.ERR_12002.setErrorCode(result);
        }
        if (StringUtils.isNotBlank(guaranteeInputDto.getApplicantPartyId()) && StringUtils.isNotBlank(guaranteeInputDto.getApplicantPartyName())) {
            if (validateParty(guaranteeInputDto.getApplicantPartyId(), guaranteeInputDto.getApplicantPartyName(), request))
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(result);
            guaranteeInputDto.setApplicantParty(
                    guaranteeInputDto.getApplicantPartyName() + " - " + guaranteeInputDto.getApplicantPartyId());
        }
        if (StringUtils.isNotBlank(guaranteeInputDto.getInstructingPartyId()) && StringUtils.isNotBlank(guaranteeInputDto.getInstructingPartyName())) {
            if (validateParty(guaranteeInputDto.getInstructingPartyId(), guaranteeInputDto.getInstructingPartyName(), request))
                return ErrorCodeEnum.ERRTF_29070.setErrorCode(result);
            guaranteeInputDto.setInstructingParty(
                    guaranteeInputDto.getInstructingPartyName() + " - " + guaranteeInputDto.getInstructingPartyId());
        }

        GuaranteesDTO responseDto = new GuaranteesDTO();
        switch (methodId) {
            case CREATEGUARANTEES_METHODID:
                guaranteeInputDto.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
                guaranteeInputDto.setCreatedOn(getCurrentDateTimeUTF());
                if (isTransactEnabled()) {
                    responseDto = createGuaranteeInTransact(request, guaranteeInputDto);
                    if (StringUtils.isBlank(responseDto.getGuaranteesReferenceNo()))
                        return JSONToResult.convert(String.valueOf(new JSONObject(responseDto)));
                    guaranteeInputDto.setRelatedTransactionReference(responseDto.getGuaranteesReferenceNo());
                }
                if (guaranteeInputDto.getGuaranteesSRMSId() == null) {
                    responseDto = guaranteesBusiness.createGuarantees(guaranteeInputDto, request);
                } else {
                    responseDto = guaranteesBusiness.updateGuarantees(guaranteeInputDto, request);
                }
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_SUBMITTED;
                break;
            case SAVEGUARANTEES_METHODID:
                guaranteeInputDto.setCreatedOn(getCurrentDateTimeUTF());
                if (StringUtils.isBlank(guaranteeInputDto.getGuaranteesSRMSId())) {
                    guaranteeInputDto.setStatus(PARAM_STATUS_DRAFT);
                    responseDto = guaranteesBusiness.createGuarantees(guaranteeInputDto, request);
                } else {
                    responseDto = guaranteesBusiness.updateGuarantees(guaranteeInputDto, request);
                }
                break;
            case DELETEGUARANTEE_METHODID:
                if (StringUtils.isNotBlank(guaranteeInputDto.getGuaranteesSRMSId())) {
                    guaranteeInputDto.setStatus(PARAM_STATUS_DELETED);
                    responseDto = guaranteesBusiness.updateGuarantees(guaranteeInputDto, request);
                } else {
                    return ErrorCodeEnum.ERRTF_29049.setErrorCode(result);
                }
                break;
            case UPDATEGUARANTEE_METHODID:
                if (StringUtils.isBlank(guaranteeInputDto.getGuaranteesSRMSId())) {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
                }
                GuaranteesDTO guaranteeDetails = guaranteesBusiness.getGuaranteesById(guaranteeInputDto.getGuaranteesSRMSId(), request);
                if (!guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_RETURNED_BY_BANK)) {
                    diagnostic.prepareDebug("Requested record is in the status - " + guaranteeDetails.getStatus()).log();
                    return ErrorCodeEnum.ERR_12003.setErrorCode(result);
                }
                guaranteeDetails.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
                JSONArray returnHistory = new JSONArray(guaranteeDetails.getReturnHistory());
                JSONObject latestHistory = returnHistory.getJSONObject(returnHistory.length() - 1);
                latestHistory.put("returnMessageToBank", StringUtils.isNotBlank(guaranteeInputDto.getMessageToBank()) ? guaranteeInputDto.getMessageToBank() : "");
                latestHistory.put("corporateUserName", guaranteeInputDto.getCorporateUserName());
                latestHistory.put("responded", "true");
                returnHistory.put(returnHistory.length() - 1, latestHistory);
                guaranteeInputDto.setReturnHistory(returnHistory.toString());
                guaranteeInputDto.setStatus(PARAM_STATUS_SUBMITTED_TO_BANK);
                responseDto = guaranteesBusiness.updateGuarantees(guaranteeInputDto, request);
                break;
            default:
                responseDto.setDbpErrMsg("Invalid modifications");
                break;
        }

        result = JSONToResult.convert(new JSONObject(responseDto).toString());
        setAlertDataInResult(result, alertToPush, responseDto.getGuaranteesSRMSId());
        return result;
    }

    public boolean validateInputDetails(GuaranteesDTO inputDto, boolean isDraft, Map<String, Object> inputParams) {
        String alphaNumericMax35CharsWithSpace = "^[a-zA-Z\\d ]{0,35}$";
        String specialCharUnacceptable = "[<>]";
        String numericMax35Chars = "^\\d{0,35}\\.\\d{0,15}";
        String alphaMax35CharsWithSpace = "^[a-zA-Z]{0,35}$";

        Pattern alphaNumericMax35CharsPattern = Pattern.compile(alphaNumericMax35CharsWithSpace);
        Pattern specialCharUnacceptablePattern = Pattern.compile(specialCharUnacceptable);
        Pattern numericMax35CharsPattern = Pattern.compile(numericMax35Chars);
        Pattern alphaMax35CharsPattern = Pattern.compile(alphaMax35CharsWithSpace);
        Matcher matcher;

        if (!isDraft && (inputDto.getBeneficiaryName() == null)) {
            return false;
        }

        if (inputDto.getProductType() != null) {
            if (!Arrays.asList("Guarantee", "Standby LC").contains(inputDto.getProductType()))
                return false;
        } else if (!isDraft) {
            return false;
        }

        if (inputDto.getGuaranteeAndSBLCType() != null) {
            if (!Arrays.asList("Performance", "BID", "Advance", "Shipping").contains(inputDto.getGuaranteeAndSBLCType())) {
                return false;
            }
        } else if (!isDraft) {
            return false;
        }

        if (inputDto.getModeOfTransaction() != null) {
            if (!Arrays.asList("Swift", "Non Swift").contains(inputDto.getModeOfTransaction())) {
                return false;
            }
        } else if (!isDraft) {
            return false;
        }

        if (inputDto.getExpiryType() != null) {
            if (!Arrays.asList("Open Ended", "Date", "Conditions").contains(inputDto.getExpiryType())) {
                return false;
            }
        } else if (!isDraft) {
            return false;
        }

        if (inputDto.getDemandAcceptance() != null) {
            if (!Arrays.asList("Partial", "Full").contains(inputDto.getDemandAcceptance())) {
                return false;
            }
        }

        if (inputDto.getApplicableRules() != null) {
            if (!Arrays.asList("ISPR", "OTHR", "UCPR", "URDG").contains(inputDto.getApplicableRules())) {
                return false;
            }
        }

        if (inputDto.getSwiftCode() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getSwiftCode());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getIban() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getIban());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getCity() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getCity());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getBankState() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getBankState());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getLocalCode() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getLocalCode());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getCountry() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getCountry());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getBankZipCode() != null) {
            matcher = alphaNumericMax35CharsPattern.matcher(inputDto.getBankZipCode());
            if (!matcher.matches())
                return false;
        }
        if (inputDto.getAmount() != null) {
            matcher = numericMax35CharsPattern.matcher(inputDto.getAmount());
            if (!matcher.matches())
                return false;
        } else if (!isDraft) {
            return false;
        }

        if (inputDto.getCurrency() != null) {
            matcher = alphaMax35CharsPattern.matcher(inputDto.getCurrency());
            if (!matcher.matches())
                return false;
        } else if (!isDraft) {
            return false;
        }

        for (Object value : inputParams.values()) {
            matcher = specialCharUnacceptablePattern.matcher((String) value);
            if (matcher.find())
                return false;
        }
        return true;
    }

    public Result getGuaranteesById(HashMap inputParams, DataControllerRequest request) {
        String srmsId = (String) inputParams.get("guaranteesSRMSId");
        Result result;
        if (StringUtils.isBlank(srmsId)) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(new Result());
        }
        GuaranteesDTO responseDto = guaranteesBusiness.getGuaranteesById(srmsId, request);
        JSONObject responseObj = new JSONObject();
        List<GuaranteesDTO> guarantee = new ArrayList<>();
        guarantee.add(responseDto);
        responseObj.put("Guarantees", guarantee);
        result = JSONToResult.convert(responseObj.toString());
        return result;
    }

    @Override
    public Result getGurantees(Object[] inputArray, GuaranteesDTO guaranteesDTO, DataControllerRequest request) {
        List<GuaranteesDTO> responseListDto = null;
        Result result = new Result();
        JSONObject responseObj1 = new JSONObject();

        try {
            if (Boolean.parseBoolean(request.getParameter("isAmendmentFlow")) && isTransactEnabled()) {
                responseListDto = getAllGuaranteesFromTransact(request);
                inputArray[1] = new HashMap<>();
            } else {
                responseListDto = guaranteesBusiness.getGuranteesLC(guaranteesDTO, request);
            }

        } catch (Exception e) {
            alert.prepareError("Error occurred while calling business delegate class").log();
        }
        responseObj1.put("GuaranteesLC", responseListDto);
        if (responseListDto == null) {
            result = JSONToResult.convert(responseObj1.toString());
            alert.prepareError("Error occurred while fetching guarantees letter of credits from backend").log();
            return result;
        }
        if (responseListDto.isEmpty()) {
            result = JSONToResult.convert(responseObj1.toString());
            alert.prepareError("No guarantees letter of credits  found").log();
            return result;
        }
        JSONObject responseObj = new JSONObject();
        responseObj.put("GuaranteesLC", responseListDto);

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
            FilterDTO filterDto = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);

            List<GuaranteesDTO> filteredLOC = responseListDto;
            if (StringUtils.isNotBlank(filterDto.get_filterByParam()) && StringUtils.isNotBlank(filterDto.get_filterByValue())) {
                filteredLOC = filterBy(responseListDto, filterDto.get_filterByParam(), filterDto.get_filterByValue());
                filterDto.set_filterByValue("");
                filterDto.set_filterByParam("");
            }
            filteredLOC = filterDto.filter(filteredLOC);

            responseObj1.put("GuaranteesLC", filteredLOC);
            result = JSONToResult.convert(responseObj1.toString());

        } catch (Exception e) {
            result.addErrMsgParam("Failed to fetch the records");
            alert.prepareError("Error occurred while fetching letter of credits from backend").log();
            return result;
        }
        return result;
    }

    public Result createClause(HashMap inputParams, DataControllerRequest request) {
        String clauseString = (String) inputParams.get("ClauseString");
        if (!StringUtils.isNotBlank(clauseString))
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        List<JSONObject> inputs;
        try {
            inputs = _getFormattedInput(clauseString);
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_12002.setErrorCode(new Result());
        }
        Map<String, Object> parsedMap = new HashMap<>();
        _parseInput(inputs, parsedMap);
        parsedMap.put("loop_count", inputs.size());

        GuaranteesBackendDelegate guaranteesBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GuaranteesBackendDelegate.class);

        List<ClauseDTO> clauseDTOS = guaranteesBackend.createClauses(parsedMap, request);
        Result result = new Result();
        if (clauseDTOS == null)
            result.addParam("dbpErrMsg", "Something went wrong while creating clause. Please try later");
        else {
            result = JSONToResult.convert((new JSONObject()).put("Clauses", clauseDTOS).toString());
        }
        return result;
    }

    @Override
    public Result updateGuaranteeLcByBank(GuaranteesDTO requestGuaranteeDTO, DataControllerRequest request) {
        AlertsEnum alertToPush = null;
        Result result = new Result();
        GuaranteesDTO updateResponseDto;
        if (StringUtils.isBlank(requestGuaranteeDTO.getGuaranteesSRMSId()) || StringUtils.isBlank(requestGuaranteeDTO.getStatus())) {
            return ErrorCodeEnum.ERR_10118.setErrorCode(result);
        }
        GuaranteesDTO guaranteeDetails = guaranteesBusiness.getGuaranteesById(requestGuaranteeDTO.getGuaranteesSRMSId(), request);

        if (guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_DRAFT)
                || guaranteeDetails.getStatus().equalsIgnoreCase(PARAM_STATUS_CANCELLED)
                || guaranteeDetails.getStatus().equals(PARAM_STATUS_APPROVED)) {
            diagnostic.prepareDebug("Requested record is in the status - " + guaranteeDetails.getStatus()).log();
            return ErrorCodeEnum.ERR_12003.setErrorCode(result);
        } else if (requestGuaranteeDTO.getStatus().equalsIgnoreCase(guaranteeDetails.getStatus())) {
            diagnostic.prepareDebug("Requested record is already in the requested status").log();
            return ErrorCodeEnum.ERR_12003.setErrorCode(result, "Requested record is already in the requested status");
        }
        switch (requestGuaranteeDTO.getStatus()) {
            case PARAM_STATUS_RETURNED_BY_BANK:
                if (StringUtils.isBlank(requestGuaranteeDTO.getReasonForReturn())) {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(result);
                }
                guaranteeDetails.setStatus(PARAM_STATUS_RETURNED_BY_BANK);
                JSONArray returnHistory = new JSONArray();
                if (StringUtils.isNotBlank(guaranteeDetails.getReturnHistory())) {
                    returnHistory = new JSONArray(guaranteeDetails.getReturnHistory());
                }
                JSONObject returnData = new JSONObject();
                returnData.put("returnedTime", getCurrentDateTimeUTF());
                returnData.put("returnCount", returnHistory.length() + 1);
                returnData.put("reasonForReturn", requestGuaranteeDTO.getReasonForReturn());
                returnHistory.put(returnData);
                guaranteeDetails.setReturnHistory(returnHistory.toString());
                updateResponseDto = guaranteesBusiness.updateGuarantees(guaranteeDetails, request);
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_RETURNED_BY_BANK;
                break;
            case PARAM_STATUS_REJECTED_BY_BANK:
                if (StringUtils.isBlank(requestGuaranteeDTO.getReasonForReturn())) {
                    return ErrorCodeEnum.ERR_10118.setErrorCode(result);
                }
                guaranteeDetails.setStatus(requestGuaranteeDTO.getStatus());
                guaranteeDetails.setReasonForReturn(requestGuaranteeDTO.getReasonForReturn());
                updateResponseDto = guaranteesBusiness.updateGuarantees(guaranteeDetails, request);
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_REJECTED;
                break;
            case PARAM_STATUS_APPROVED:
                alertToPush = AlertsEnum.GUARANTEES_ISSUED_APPROVED;
                guaranteeDetails.setStatus(requestGuaranteeDTO.getStatus());
                updateResponseDto = guaranteesBusiness.updateGuarantees(guaranteeDetails, request);
                break;
            case PARAM_STATUS_PROCESSING_WITH_BANK:
            case PARAM_STATUS_PENDING_WITH_BANK:
                guaranteeDetails.setStatus(requestGuaranteeDTO.getStatus());
                updateResponseDto = guaranteesBusiness.updateGuarantees(guaranteeDetails, request);
                break;
            default:
                diagnostic.prepareDebug("Invalid Status found in payload").log();
                return ErrorCodeEnum.ERRTF_29077.setErrorCode(result);
        }

        result = JSONToResult.convert(new JSONObject(updateResponseDto).toString());
        setAlertDataInResult(result, alertToPush, updateResponseDto.getGuaranteesSRMSId());
        return result;
    }

    private List<JSONObject> _getFormattedInput(String clauseString) {
        List<JSONObject> clauseMap = new ArrayList<>();
        JSONArray clauseArray = new JSONArray(clauseString);
        for (Object clause : clauseArray) {
            JSONObject map = new JSONObject(clause.toString());
            clauseMap.add(map);
        }
        return clauseMap;
    }

    private void _parseInput(List<JSONObject> inputs, Map<String, Object> parsedMap) {
        StringBuilder clauseType = new StringBuilder();
        StringBuilder clauseDescription = new StringBuilder();
        StringBuilder clauseTitle = new StringBuilder();
        for (JSONObject input : inputs) {
            clauseType.append(input.get("clauseType")).append(",");
            clauseDescription.append(input.get("clauseDescription")).append(",");
            clauseTitle.append(input.get("clauseTitle")).append(",");
        }
        parsedMap.put("clauseType", clauseType.toString());
        parsedMap.put("clauseDescription", clauseDescription.toString());
        parsedMap.put("clauseTitle", clauseTitle.toString());
    }

    private static JSONArray getInfinityUserContractCustomersPermissions(Map<String, Object> headerParams, DataControllerRequest dcRequest) {
        String serviceName = ServiceId.EUM_PRODUCT_SERVICES;
        String operationName = OperationName.GETINFINITYUSERCONTRACTCUSTOMERS;

        try {
            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(serviceName).
                    withObjectId(null).
                    withOperationId(operationName).
                    withRequestParameters(new HashMap<>()).
                    withRequestHeaders(headerParams).
                    withDataControllerRequest(dcRequest).
                    build().getResponse();
            return CommonUtils.getFirstOccuringArray(new JSONObject(response));
        } catch (Exception e) {
            alert.prepareError("Caught exception while getting feature actions: " + e).log();
            return null;
        }
    }

    private boolean validateParty(String partyId, String partyName, DataControllerRequest dcRequest) {
        JSONArray jsonArray = getInfinityUserContractCustomersPermissions(dcRequest.getHeaderMap(), dcRequest);
        if (jsonArray == null || jsonArray.length() == 0) {
            return true;
        }

        JSONObject contractObj, cifObj;
        String coreCustomerId, coreCustomerName;
        JSONArray contractCustomers;
        for (int i = 0; i < jsonArray.length(); i++) {
            contractObj = jsonArray.getJSONObject(i);
            contractCustomers = contractObj.getJSONArray("contractCustomers");
            for (int j = 0; j < contractCustomers.length(); j++) {
                cifObj = contractCustomers.getJSONObject(j);
                coreCustomerId = cifObj.getString("coreCustomerId");
                coreCustomerName = cifObj.getString("coreCustomerName");
                if (coreCustomerId.equals(partyId) && coreCustomerName.equals(partyName)) {
                    return false;
                }
            }
        }
        return true;
    }

    private GuaranteesDTO createGuaranteeInTransact(DataControllerRequest request, GuaranteesDTO inputDto) {
        Map<String, Object> inputMap;
        Result result;
        GuaranteesDTO responseDTO = new GuaranteesDTO();
        try {
            inputDto.setProductType(StringUtils.equalsIgnoreCase(inputDto.getProductType(), "Guarantee") ? "GTISS" : "STDBY");
            inputDto.setExpiryType(getTransactExpiryType(inputDto.getExpiryType()));
            inputDto.setIssueDate(_formatDate(inputDto.getIssueDate()));
            inputDto.setClauseConditions(getClauseConditions(inputDto.getClauseConditions()));
            if (StringUtils.isNotBlank(inputDto.getExpiryDate()))
                inputDto.setExpiryDate(_formatDate(inputDto.getExpiryDate()));
            inputMap = new ObjectMapper().convertValue(inputDto, Map.class);
            inputMap.put("advisingBank", "");
            inputMap.put("applicantParty", getCoreCustomerId(request));
            result = com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils.callIntegrationService(request, inputMap, getHeadersMap(request), TRANSACT_TF_CREATE_ISSUED_GUARANTEE.getServiceName(), TRANSACT_TF_CREATE_ISSUED_GUARANTEE.getOperationName(), false);
            Record headerData = result.getRecordById("header");
            if (StringUtils.equals(headerData.getParamValueByName("status"), "success"))
                responseDTO.setGuaranteesSRMSId(headerData.getParamValueByName("id"));
            else if (StringUtils.equals(headerData.getParamValueByName("status"), "failed")) {
                responseDTO.setErrorCode(ERR_12007.getErrorCodeAsString());
                responseDTO.setDbpErrMsg(ERR_12007.getErrorMessage());
                responseDTO.setErrorDetails(result.getRecordById("error").getDatasetById("errorDetails").getAllRecords().toString());
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating invoice in transact", e).log();
            responseDTO.setErrorCode(ERR_12007.getErrorCodeAsString());
            responseDTO.setErrorMsg(ERR_12007.getErrorMessage());
            responseDTO.setErrorDetails("Backend Failed");
        }

        return responseDTO;
    }

    private List<GuaranteesDTO> getAllGuaranteesFromTransact(DataControllerRequest request) {
        Map<String, Object> inputMap = new HashMap<>();
        Result result;
        List<GuaranteesDTO> guaranteesList = new ArrayList<>();
        GuaranteesDTO guarantee;
        try {
            inputMap.put("beneficiary", getCoreCustomerId(request));
            result = com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils.callIntegrationService(request, inputMap, getHeadersMap(request), TRANSACT_TF_GET_ISSUED_GUARANTEES.getServiceName(), TRANSACT_TF_GET_ISSUED_GUARANTEES.getOperationName(), false);
            Record headerData = result.getRecordById("header");
            if (StringUtils.equals(headerData.getParamValueByName("status"), "success")) {
                List<Record> records = result.getDatasetById("body").getAllRecords();
                for (Record record : records) {
                    guarantee = new GuaranteesDTO();
                    guarantee.setGuaranteesSRMSId(record.getParamValueByName("guaranteeId"));
                    guarantee.setExpiryType(record.getParamValueByName("expiryType"));
                    guarantee.setProductType(record.getParamValueByName("guaranteeType"));
                    guarantee.setCreatedOn(formatTransactDate(record.getParamValueByName("dealDate")));
                    guarantee.setIssueDate(formatTransactDate(record.getParamValueByName("dealDate")));
                    guarantee.setStatus(record.getParamValueByName("status"));
                    guarantee.setCurrency(record.getParamValueByName("currency"));
                    guarantee.setAmount(record.getParamValueByName("principalAmount"));
                    guarantee.setInstructingParty(record.getParamValueByName("instructingParty"));
                    guarantee.setApplicantParty(record.getParamValueByName("applicantCustomerId"));
                    guarantee.setExpectedIssueDate(formatTransactDate(record.getParamValueByName("valueDate")));
                    guarantee.setGoverningLaw(record.getParamValueByName("governingLaw"));
                    guarantee.setClauseConditions(record.getParamValueByName("senderToReceiverInfo"));
                    guarantee.setLimitInstructions(record.getParamValueByName("limitReference"));
                    guarantee.setBeneficiaryName(record.getParamValueByName("beneficiary"));
                    guaranteesList.add(guarantee);
                }
            } else if (StringUtils.equals(headerData.getParamValueByName("status"), "failed")) {
                guarantee = new GuaranteesDTO();
                guarantee.setErrorCode(ERR_12007.getErrorCodeAsString());
                guarantee.setDbpErrMsg(ERR_12007.getErrorMessage());
                guarantee.setErrorDetails(result.getRecordById("error").getDatasetById("errorDetails").getAllRecords().toString());
                guaranteesList.add(guarantee);
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating invoice in transact", e).log();
            guarantee = new GuaranteesDTO();
            guarantee.setErrorCode(ERR_12007.getErrorCodeAsString());
            guarantee.setErrorMsg(ERR_12007.getErrorMessage());
            guarantee.setErrorDetails("Backend Failed");
            guaranteesList.add(guarantee);
        }

        return guaranteesList;
    }

    private String getTransactExpiryType(String inputExpiryType) {
        String expiryType = "";
        switch (inputExpiryType) {
            case "Open Ended":
                expiryType = "OPEN";
                break;
            case "Date":
                expiryType = "FIXD";
                break;
            case "Conditions":
                expiryType = "COND";
                break;
        }
        return expiryType;
    }

    private String getClauseConditions(String clauseConditions) {
        String result = "";
        try {
            JSONArray clauses = new JSONArray(clauseConditions);
            result = clauses.getJSONObject(0).getString("clauseTitle");
        } catch (Exception e) {
            alert.prepareError("Error occurred while parsing clauses");
        }
        return result;
    }
}