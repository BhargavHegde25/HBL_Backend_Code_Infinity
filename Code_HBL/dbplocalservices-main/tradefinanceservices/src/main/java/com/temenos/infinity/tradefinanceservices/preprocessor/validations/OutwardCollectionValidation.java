package com.temenos.infinity.tradefinanceservices.preprocessor.validations;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_NO;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_SIGHT;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_USANCE;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.PARAM_YES;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;

public class OutwardCollectionValidation implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        Pattern alphaMax50 = Pattern.compile("^(?i)[a-z\\d ]{1,50}$");
        Pattern numericMax3 = Pattern.compile("^[0-9]{0,3}$");
        Pattern numericWithSpacesMax9 = Pattern.compile("^[\\d -]{0,9}$");
        Pattern numericMax15With2Decimal = Pattern.compile("^\\d{1,15}(\\.\\d{1,2})?$");
        Pattern alphaNumericMax35 = Pattern.compile("^(?i)[a-z\\d]{1,35}$");
        Pattern alphaNumericMax35WithSpace = Pattern.compile("^(?i)[ a-z\\d]{1,200}$");
        Pattern alphaNumericMax200WithSpace = Pattern.compile("^(?i)[a-z /().,\\d]{1,200}$");
        Pattern alphaNumericMax35WithSpaceEmpty = Pattern.compile("^(?i)[ a-z\\d]{0,50}$");
        Pattern alphaNumericMax50WithSpace = Pattern.compile("^(?i)[ a-z\\d]{0,50}$");
        Pattern alphaNumericMax300WithSpaceAndSpecialCharacters = Pattern.compile("^(?i)[a-z\\d/,?:'_+-= ]{0,300}$");
        Pattern alphaNumericMax500WithSpaceAndSpecialCharacters = Pattern.compile("^(?i)[a-z\\d<>,?:'_+-= ]{0,500}$");
        Pattern alphaNumericMax11 = Pattern.compile("^(?i)[a-z\\d]{1,11}$");
        Pattern fileNameMax200 = Pattern.compile("^[a-zA-Z0-9 /()._-]{0,200}$");
        Pattern upperAlpha3 = Pattern.compile("^[A-Z]{3}$");
        final List<String> availableInstructions = Arrays.asList("ACCEPTANCE/ PAYMENT may await arrival of carrier", "CABLE/ AIRMAIL advice for non payment / non acceptance", "PROTEST for non payment / non acceptance", "If unpaid/ unaccepted, store & insure goods", "Collect overseas charges from Drawee", "Deduct charges from proceeds");

        try {
            OutwardCollectionsDTO inputDto = JSONUtils.parse(fabricRequestManager.getPayloadHandler().getPayloadAsJson().toString(), OutwardCollectionsDTO.class);
            if ((StringUtils.isNotBlank(inputDto.getDocumentNo()) && !alphaNumericMax35.matcher(inputDto.getDocumentNo()).matches())
                    || ((StringUtils.isNotBlank(inputDto.getAmount()) && !numericMax15With2Decimal.matcher(inputDto.getAmount()).matches())
                    || (StringUtils.isNotBlank(inputDto.getCurrency()) && !upperAlpha3.matcher(inputDto.getCurrency()).matches()))
                    || ((StringUtils.isNotBlank(inputDto.getTenorType()) && !Arrays.asList(PARAM_SIGHT, PARAM_USANCE).contains(inputDto.getTenorType()))
                    || (StringUtils.isNotBlank(inputDto.getUsanceDays()) && !numericMax3.matcher(inputDto.getUsanceDays()).matches())
                    || (StringUtils.isNotBlank(inputDto.getUsanceDetails()) && !alphaMax50.matcher(inputDto.getUsanceDetails()).matches())
                    || (StringUtils.isNotBlank(inputDto.getAllowUsanceAcceptance()) && !Arrays.asList(PARAM_YES, PARAM_NO).contains(inputDto.getAllowUsanceAcceptance())))
                    || (StringUtils.isNotBlank(inputDto.getDraweeName()) && !alphaNumericMax200WithSpace.matcher(inputDto.getDraweeName()).matches())
                    || (StringUtils.isNotBlank(inputDto.getCollectingBank()) && !alphaNumericMax200WithSpace.matcher(inputDto.getCollectingBank()).matches())
                    || (StringUtils.isNotBlank(inputDto.getSwiftOrBicCode()) && !alphaNumericMax11.matcher(inputDto.getSwiftOrBicCode()).matches())
                    || (StringUtils.isNotBlank(inputDto.getIncoTerms()) && !Arrays.asList("CIF", "CFR", "FOB", "FCA", "FAS", "CPT", "CIP", "DAP", "DDP", "DDU", "DES", "DEQ", "EXW").contains(inputDto.getIncoTerms()))
                    || (StringUtils.isNotBlank(inputDto.getOtherCollectionDetails()) && !alphaNumericMax500WithSpaceAndSpecialCharacters.matcher(inputDto.getOtherCollectionDetails()).matches())
                    || (StringUtils.isNotBlank(inputDto.getMessageToBank()) && !alphaNumericMax500WithSpaceAndSpecialCharacters.matcher(inputDto.getMessageToBank()).matches())
                    || (StringUtils.isNotBlank(inputDto.getDeliveryInstructions()) && !alphaNumericMax500WithSpaceAndSpecialCharacters.matcher(inputDto.getDeliveryInstructions()).matches())) {
                return updateErrorResult(fabricResponseManager);
            }

            if (StringUtils.isNotBlank(inputDto.getPhysicalDocuments())) {
                JSONArray documents = new JSONArray(inputDto.getPhysicalDocuments());
                for (int i = 0; i < documents.length(); i++) {
                    JSONObject document = documents.getJSONObject(i);
                    if ((StringUtils.isNotBlank(document.getString("copiesCount")) && !alphaNumericMax35WithSpace.matcher(document.getString("copiesCount")).matches())
                            || (StringUtils.isNotBlank(document.getString("originalsCount")) && !alphaNumericMax35WithSpace.matcher(document.getString("originalsCount")).matches())
                            || (StringUtils.isNotBlank(document.getString("documentTitle")) && !fileNameMax200.matcher(document.getString("documentTitle")).matches())) {
                        return updateErrorResult(fabricResponseManager);
                    }
                }
            }
            if (StringUtils.isNotBlank(inputDto.getUploadDocuments())) {
                JSONArray documents = new JSONArray(inputDto.getUploadDocuments());
                for (int i = 0; i < documents.length(); i++) {
                    JSONObject document = documents.getJSONObject(i);
                    String[] temp = StringUtils.split(document.getString("documentName"), ".");
                    if ((StringUtils.isNotBlank(temp[0]) && !fileNameMax200.matcher(temp[0]).matches()) && StringUtils.isNotBlank(document.getString("documentReference"))) {
                        return updateErrorResult(fabricResponseManager);
                    }
                }
            }
            if (StringUtils.isNotBlank(inputDto.getCollectingBankAddress())) {
                JSONObject jsonObj = new JSONObject(inputDto.getCollectingBankAddress());
                if ((jsonObj.has("address1") && !alphaNumericMax300WithSpaceAndSpecialCharacters.matcher(jsonObj.getString("address1")).matches())
                        || (jsonObj.has("address2") && !alphaNumericMax300WithSpaceAndSpecialCharacters.matcher(jsonObj.getString("address2")).matches())
                        || (jsonObj.has("city") && !alphaNumericMax35WithSpaceEmpty.matcher(jsonObj.getString("city")).matches())
                        || (jsonObj.has("country") && !alphaNumericMax35WithSpaceEmpty.matcher(jsonObj.getString("country")).matches())
                        || (jsonObj.has("zipcode") && !numericWithSpacesMax9.matcher(jsonObj.getString("zipcode")).matches())
                        || (jsonObj.has("state") && !alphaNumericMax35WithSpaceEmpty.matcher(jsonObj.getString("state")).matches())) {
                    return updateErrorResult(fabricResponseManager);
                }
            }

            if (StringUtils.isNotBlank(inputDto.getDraweeAddress())) {
                JSONObject jsonObj = new JSONObject(inputDto.getDraweeAddress());
                if ((jsonObj.has("address1") && !alphaNumericMax300WithSpaceAndSpecialCharacters.matcher(jsonObj.getString("address1")).matches())
                        || (jsonObj.has("address2") && !alphaNumericMax300WithSpaceAndSpecialCharacters.matcher(jsonObj.getString("address2")).matches())
                        || (jsonObj.has("city") && !alphaNumericMax35WithSpaceEmpty.matcher(jsonObj.getString("city")).matches())
                        || (jsonObj.has("country") && !alphaNumericMax35WithSpaceEmpty.matcher(jsonObj.getString("country")).matches())
                        || (jsonObj.has("zipcode") && !numericWithSpacesMax9.matcher(jsonObj.getString("zipcode")).matches())
                        || (jsonObj.has("state") && !alphaNumericMax50WithSpace.matcher(jsonObj.getString("state")).matches())) {
                    return updateErrorResult(fabricResponseManager);
                }
            }

            if (StringUtils.isNotBlank(inputDto.getInstructionsForBills())) {
                for (Object instruction : new JSONArray(inputDto.getInstructionsForBills())) {
                    if (!availableInstructions.contains(instruction.toString())) {
                        return updateErrorResult(fabricResponseManager);
                    }
                }
            }

        } catch (Exception e) {
            alert.prepareError("Failed in validating Outward Collection fields").log();
            return updateErrorResult(fabricResponseManager);
        }
        return true;
    }

    private boolean updateErrorResult(FabricResponseManager fabricResponseManager) {
        JsonObject resPayload = null;
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_10197.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }
}
