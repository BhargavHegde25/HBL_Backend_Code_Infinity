/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.javaservices;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.constants.ErrorCodeEnum;
import com.temenos.infinity.tradelending.documentutils.TransactionReport;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;
import com.temenos.infinity.tradelending.resource.api.PaymentRequestResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Generate transaction report - pdf
 *
 * @author k.meiyazhagan
 */
public class GenerateTransactionReport implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();
        PaymentRequestResource resource = DBPAPIAbstractFactoryImpl.getResource(PaymentRequestResource.class);
        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
        PaymentRequestDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), PaymentRequestDTO.class);
        inputDto = resource.getPaymentRequestById(inputDto, request);

        try {
            byte[] bytes = TransactionReport.generatePdf(inputDto);
            response.getHeaders().putAll(TransactionReport.getAllResponseHeaders(inputDto.getPaymentRequestId()));
            response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(new ByteArrayEntity(bytes)));
            response.setStatusCode(HttpStatus.SC_OK);
            return new Result();
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating pdf ", e.getMessage()).log();
            return ErrorCodeEnum.ERR_30009.setErrorCode(new Result(), e.getMessage());
        }
    }
}