/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.AnchorFundingRequestDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class AnchorFundingRequestValidation implements ObjectProcessorTask {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        Pattern alphaMax3 = Pattern.compile("^[a-zA-Z]{0,3}$");
        Pattern amountMax15 = Pattern.compile("^[0-9]+([.][0-9]+?){0,50}$");
        Pattern alphaNumericMax35 = Pattern.compile("^[a-zA-Z0-9]{0,35}$");
        Pattern alphaNumericWithSpaceMax200 = Pattern.compile("^[a-zA-Z0-9 .,-]{0,200}$");

        try {
            AnchorFundingRequestDTO inputDto = JSONUtils.parse(fabricRequestManager.getPayloadHandler().getPayloadAsJson().toString(), AnchorFundingRequestDTO.class);
            if ((StringUtils.isNotBlank(inputDto.getCurrency()) && !alphaMax3.matcher(inputDto.getCurrency()).matches())
                    || (StringUtils.isNotBlank(inputDto.getFacilityCurrency()) && !alphaMax3.matcher(inputDto.getFacilityCurrency()).matches())
                    || (StringUtils.isNotBlank(inputDto.getFundingDocuments()) && !_validateDocuments(inputDto.getFundingDocuments(), fabricRequestManager))
                    || (StringUtils.isNotBlank(inputDto.getFundingRequestAmount()) && !amountMax15.matcher(inputDto.getFundingRequestAmount()).matches())
                    || (StringUtils.isNotBlank(inputDto.getFacilityAvailableLimit()) && !amountMax15.matcher(inputDto.getFacilityAvailableLimit()).matches())
                    || (StringUtils.isNotBlank(inputDto.getFacilityUtilisedLimit()) && !amountMax15.matcher(inputDto.getFacilityUtilisedLimit()).matches())
                    || (/* TODO: add check if currencies are different */
                    StringUtils.equals(inputDto.getCurrency(), inputDto.getFacilityCurrency()) && Double.parseDouble(inputDto.getFundingRequestAmount()) > Double.parseDouble(inputDto.getFacilityAvailableLimit()))
                    || (StringUtils.isNotBlank(inputDto.getFacilityId()) && !alphaNumericMax35.matcher(inputDto.getFacilityId()).matches())
                    || (StringUtils.isNotBlank(inputDto.getProgramName()) && !alphaNumericWithSpaceMax200.matcher(inputDto.getProgramName()).matches())
                    || (StringUtils.isNotBlank(inputDto.getProductId()) && !alphaNumericWithSpaceMax200.matcher(inputDto.getProductId()).matches())
                    || (StringUtils.isNotBlank(inputDto.getProductName()) && !alphaNumericWithSpaceMax200.matcher(inputDto.getProductName()).matches())) {
                return updateErrorResponse(fabricResponseManager, ErrorCodeEnum.ERR_30001);
            }
        } catch (Exception e) {
            alert.prepareError("Error occurred while validating bills input", e).log();
            return updateErrorResponse(fabricResponseManager, ErrorCodeEnum.ERR_30018);
        }

        return true;
    }

    private static boolean _validateDocuments(String documents, FabricRequestManager fabricRequestManager) {
        boolean result = false;
        try {
            JSONObject jsonObject = new JSONObject(documents);
            JSONObject bundleConfig = getBundleConfigAsJson(fabricRequestManager, PARAM_DOCUMENT_CATEGORY_KEY);
            List<String> allowedKeys = new ArrayList<>();
            for (Object docCategory : bundleConfig.getJSONArray(PARAM_ALLOWED_DOC_CATEGORY)) {
                allowedKeys.add(((JSONObject) docCategory).getString(PARAM_KEY));
            }
            result = jsonObject.keySet().stream().allMatch(key -> allowedKeys.contains(key) &&
                    areValidDocuments(jsonObject.get(key).toString(), bundleConfig.getJSONArray(PARAM_ALLOWED_DOC_TYPES)));
        } catch (Exception e) {
            alert.prepareError("Error occurred while validating documents", e).log();
        }
        return result;
    }

    private static boolean _validateInvoices(String invoiceReferences) {
        return false;
    }
}