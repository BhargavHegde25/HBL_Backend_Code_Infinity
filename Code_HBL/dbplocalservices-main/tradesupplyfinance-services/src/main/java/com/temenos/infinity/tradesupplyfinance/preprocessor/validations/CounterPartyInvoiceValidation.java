/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.preprocessor.validations;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.*;

/**
 * @author k.meiyazhagan
 */
public class CounterPartyInvoiceValidation implements ObjectProcessorTask {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Pattern amountMax15 = Pattern.compile("^[0-9]+([.][0-9]+?){0,50}$");
    private static final Pattern alphaNumericMax35 = Pattern.compile("^[a-zA-Z0-9]{0,35}$");
    private static final Pattern alphaNumericWithSpaceMax200 = Pattern.compile("^[a-zA-Z0-9 .,-]{0,200}$");
    private static final JSONArray allowedDocTypes = new JSONArray(Arrays.toString(new String[]{"pdf", "jpeg", "png", "tiff", "doc", "xls", "csv"}));

    @Override
    public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) throws Exception {
        try {
            JsonObject inputPayloadJson = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            CounterPartyInvoiceDTO inputDto = JSONUtils.parse(inputPayloadJson.toString(), CounterPartyInvoiceDTO.class);
            String customerId = getCoreCustomerId(fabricRequestManager, Arrays.asList(inputDto.getBuyerId(), inputDto.getSupplierId()));
            JSONObject uploadConfig = getBundleConfigAsJson(fabricRequestManager, PARAM_INVOICE_UPLOAD_KEY);

            if (!isValidInvoice(inputDto, uploadConfig, customerId)) {
                return updateErrorResponse(fabricResponseManager, ErrorCodeEnum.ERR_30001);
            }

            inputPayloadJson.addProperty(PARAM_ROLE, StringUtils.equals(inputDto.getBuyerId(), customerId) ? PARAM_ROLE_BUYER : PARAM_ROLE_SUPPLIER);
            fabricRequestManager.getPayloadHandler().updatePayloadAsJson(inputPayloadJson);
        } catch (Exception e) {
            alert.prepareError("Error occurred while validating input", e).log();
            return updateErrorResponse(fabricResponseManager, ErrorCodeEnum.ERR_30018);
        }

        return true;
    }

    public static boolean isValidInvoice(CounterPartyInvoiceDTO inputDto, JSONObject uploadConfig, String customerId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(UTC_DATE_FORMAT);
        return (uploadConfig.getJSONArray(PARAM_CURRENCY).toList().contains(inputDto.getInvoiceCurrency())
                && (!StringUtils.isNotBlank(inputDto.getInvoiceDocuments()) || areValidDocuments(inputDto.getInvoiceDocuments(), allowedDocTypes))
                && amountMax15.matcher(inputDto.getInvoiceAmount()).matches()
                && alphaNumericMax35.matcher(inputDto.getBillReference()).matches()
                && alphaNumericMax35.matcher(inputDto.getBuyerId()).matches()
                && (!StringUtils.isNotBlank(inputDto.getBuyerName()) || alphaNumericWithSpaceMax200.matcher(inputDto.getBuyerName()).matches())
                && alphaNumericMax35.matcher(inputDto.getSupplierId()).matches()
                && (!StringUtils.isNotBlank(inputDto.getSupplierName()) || alphaNumericWithSpaceMax200.matcher(inputDto.getSupplierName()).matches())
                && uploadConfig.getJSONArray(PARAM_BILL_TYPE).toList().contains(inputDto.getBillType())
                && (/*Buyer and supplier should not be the same*/(!StringUtils.equals(inputDto.getSupplierId(), inputDto.getBuyerId())
                && /*Logged in user should be the buyer or supplier*/Arrays.asList(inputDto.getBuyerId(), inputDto.getSupplierId()).contains(customerId))
                && isValidDate(dateFormat, inputDto.getIssueDate()))
                && isValidDate(dateFormat, inputDto.getMaturityDate())
                && isDateGreater(dateFormat, inputDto.getIssueDate(), inputDto.getMaturityDate()));
    }
}