/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.preprocessor;

import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.CounterPartyInvoiceDTO;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.Arrays;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.preprocessor.validations.CounterPartyInvoiceValidation.isValidInvoice;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getBundleConfigAsJson;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getCoreCustomerId;

/**
 * @author k.meiyazhagan
 */
public class CreateCounterPartyBulkInvoicesPreProcessor implements ObjectServicePreProcessor {
    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager, FabricRequestChain requestChain) throws Exception {
        JsonObject payloadJson = new Gson().fromJson(PAYLOAD_EMPTY_INVOICE, JsonObject.class);
        try {
            JsonObject requestJson = (JsonObject) requestManager.getPayloadHandler().getPayloadAsJson();
            JsonArray invoicesJson = requestJson.getAsJsonArray(PARAM_INVOICES);
            if (invoicesJson.size() > 10)
                throw new ArrayIndexOutOfBoundsException();

            String documentsJson = requestJson.has(PARAM_INVOICE_DOCUMENTS) ? requestJson.getAsJsonArray(PARAM_INVOICE_DOCUMENTS).toString() : null;
            JSONObject uploadConfig = getBundleConfigAsJson(requestManager, PARAM_INVOICE_UPLOAD_KEY);
            JsonArray failedInvoices = new JsonArray();

            String customerId;
            JSONObject newPayload;
            CounterPartyInvoiceDTO singleInvoiceDTO = new CounterPartyInvoiceDTO();
            for (int i = 0; i < invoicesJson.size(); i++) {
                singleInvoiceDTO = JSONUtils.parse(invoicesJson.get(i).getAsJsonObject().toString(), CounterPartyInvoiceDTO.class);
                customerId = getCoreCustomerId(requestManager, Arrays.asList(singleInvoiceDTO.getBuyerId(), singleInvoiceDTO.getSupplierId()));
                singleInvoiceDTO.setRole(StringUtils.equals(singleInvoiceDTO.getBuyerId(), customerId) ? PARAM_ROLE_BUYER : PARAM_ROLE_SUPPLIER);
                if (StringUtils.isNotBlank(documentsJson))
                    singleInvoiceDTO.setInvoiceDocuments(documentsJson);

                // validating invoices
                if (!isValidInvoice(singleInvoiceDTO, uploadConfig, customerId)) {
                    failedInvoices.add(i);
                    continue;
                }

                // constructing orchestration payload
                newPayload = new JSONObject(new ObjectMapper().writeValueAsString(singleInvoiceDTO));
                for (String key : payloadJson.keySet()) {
                    payloadJson.addProperty(key, (i != 0 ? payloadJson.get(key).getAsString() : "") + (newPayload.has(key) ? newPayload.getString(key) : "") + SEPARATOR_INVOICES_ORCHESTRATION);
                }
            }
            payloadJson.addProperty(PARAM_INVOICE_DOCUMENTS, StringUtils.isNotBlank(singleInvoiceDTO.getInvoiceDocuments()) ? StringUtils.repeat(singleInvoiceDTO.getInvoiceDocuments() + SEPARATOR_INVOICES_ORCHESTRATION, invoicesJson.size()) : null);

            if (failedInvoices.isEmpty()) {
                payloadJson.addProperty(PARAM_LOOP_COUNT, invoicesJson.size());
                payloadJson.addProperty(PARAM_LOOP_SEPARATOR, SEPARATOR_INVOICES_ORCHESTRATION);
                requestManager.getPayloadHandler().updatePayloadAsJson(payloadJson);
                requestChain.execute();
            } else {
                // Error response - validation failure
                payloadJson = new JsonObject();
                payloadJson.add(PARAM_FAILED_INVOICE_IDS, failedInvoices);
                responseManager.getPayloadHandler().updatePayloadAsJson(ErrorCodeEnum.ERR_30001.setErrorCode(payloadJson));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            payloadJson = new JsonObject();
            responseManager.getPayloadHandler().updatePayloadAsJson(ErrorCodeEnum.ERR_30026.setErrorCode(payloadJson));
        } catch (Exception e) {
            payloadJson = new JsonObject();
            responseManager.getPayloadHandler().updatePayloadAsJson(ErrorCodeEnum.ERR_30018.setErrorCode(payloadJson));
        }
    }
}