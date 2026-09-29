/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionAmendmentsDTO;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

/**
 * @author k.meiyazhagan
 */
public class OutwardAmendmentsValidation implements ObjectProcessorTask {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        Pattern alphaMax3 = Pattern.compile("^[a-zA-Z]{0,3}$");
        Pattern alphaMax50 = Pattern.compile("^[a-zA-Z ]{0,50}$");
        Pattern numericMax3 = Pattern.compile("^[0-9]{0,3}$");
        Pattern amountMax15 = Pattern.compile("^[0-9]+([.][0-9]+?){0,50}$");
        Pattern alphaNumericMax35 = Pattern.compile("^[a-zA-Z0-9]{0,35}$");
        Pattern alphaNumericSpaceMax35 = Pattern.compile("^[a-zA-Z0-9 ]{0,35}$");
        Pattern fileNameMax200 = Pattern.compile("^[a-zA-Z0-9 ._/-]{0,200}$");
        Pattern alphaNumericWithSpaceMax500 = Pattern.compile("^[a-zA-Z0-9 .,+<>!?:-]{0,500}$");

        try {
            OutwardCollectionAmendmentsDTO inputDto = JSONUtils.parse(fabricRequestManager.getPayloadHandler().getPayloadAsJson().toString(), OutwardCollectionAmendmentsDTO.class);
            if ((StringUtils.isNotBlank(inputDto.getDocumentNo()) && !alphaNumericMax35.matcher(inputDto.getDocumentNo()).matches())
                    || (StringUtils.isNotBlank(inputDto.getCancellationStatus()) && !StringUtils.equals(inputDto.getCancellationStatus(), PARAM_STATUS_REQUESTED))
                    || ((StringUtils.isNotBlank(inputDto.getAmount()) || StringUtils.isNotBlank(inputDto.getCurrency())) && !amountMax15.matcher(inputDto.getAmount()).matches() && !alphaMax3.matcher(inputDto.getCurrency()).matches())
                    || (StringUtils.isNotBlank(inputDto.getOtherCollectionDetails()) && !alphaNumericWithSpaceMax500.matcher(inputDto.getOtherCollectionDetails()).matches())
                    || (StringUtils.isNotBlank(inputDto.getMessageToBank()) && !alphaNumericWithSpaceMax500.matcher(inputDto.getMessageToBank()).matches())
                    || ((StringUtils.isNotBlank(inputDto.getAmendTenorType()) && (!StringUtils.equals(inputDto.getAmendTenorType(), PARAM_USANCE) || !Arrays.asList(PARAM_YES, PARAM_NO).contains(inputDto.getAllowUsanceAcceptance())
                    && !numericMax3.matcher(inputDto.getUsanceDays()).matches() || !alphaMax50.matcher(inputDto.getUsanceDetails()).matches())))) {
                return updateErrorResponse(fabricResponseManager);
            }

            if (StringUtils.isNotBlank(inputDto.getPhysicalDocuments())) {
                JSONArray documents = new JSONArray(inputDto.getPhysicalDocuments());
                for (int i = 0; i < documents.length(); i++) {
                    JSONObject document = documents.getJSONObject(i);
                    if (!alphaNumericSpaceMax35.matcher(document.getString("copiesCount")).matches()
                            || !alphaNumericSpaceMax35.matcher(document.getString("originalsCount")).matches()
                            || !fileNameMax200.matcher(document.getString("documentTitle")).matches()) {
                        return updateErrorResponse(fabricResponseManager);
                    }
                }
            }
        } catch (Exception e) {
            alert.prepareError("Failed in validating outward collection amendment fields").log();
            return updateErrorResponse(fabricResponseManager);
        }

        return true;
    }

    private boolean updateErrorResponse(FabricResponseManager fabricResponseManager) {
        JsonObject resPayload = new JsonObject();
        if (!HelperMethods.isJsonEleNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
            resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        }
        resPayload = ErrorCodeEnum.ERR_10197.setErrorCode(resPayload);
        fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
        return false;
    }
}
