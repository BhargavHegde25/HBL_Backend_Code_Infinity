/**
 * 
 */
package com.temenos.infinity.tradesupplyfinance.preprocessor;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_INVOICE_REFERENCE;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_INVOICE_REFERENCES;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.PARAM_LOOP_COUNT;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.SEPARATOR_BILLS_ORCHESTRATION;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.updateErrorResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.api.processor.FabricRequestChain;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePreProcessor;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;

/**
 * @author mrunalini.adepu
 *
 */
public class ApproveRejectAnchorInvoicesPreprocessor implements ObjectServicePreProcessor {

	@Override
	public void execute(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager,
			FabricRequestChain fabricRequestChain) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            JsonObject requestJson = (JsonObject) fabricRequestManager.getPayloadHandler().getPayloadAsJson();
            JsonObject payloadJson = new JsonObject();
            JsonArray invoiceReferenceJson = requestJson.getAsJsonArray(PARAM_INVOICE_REFERENCES);
            StringBuilder invoiceReferencePayload = new StringBuilder();
            for (JsonElement element : invoiceReferenceJson)
                invoiceReferencePayload.append(element.getAsString()).append(SEPARATOR_BILLS_ORCHESTRATION);
            payloadJson.addProperty(PARAM_INVOICE_REFERENCE, invoiceReferencePayload.toString());
            payloadJson.addProperty(PARAM_LOOP_COUNT, invoiceReferenceJson.size());
            fabricRequestManager.getPayloadHandler().updatePayloadAsJson(payloadJson);
            fabricRequestChain.execute();
        } catch (Exception e) {
            updateErrorResponse(fabricResponseManager, ErrorCodeEnum.ERR_30018);
        }

	}

}
