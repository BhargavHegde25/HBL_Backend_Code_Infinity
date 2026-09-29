/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.javaservices;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.infinity.tradesupplyfinance.dto.PaymentAllocationDTO;
import com.temenos.infinity.tradesupplyfinance.resource.api.PaymentAllocationResource;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * @author k.meiyazhagan
 */
public class CreatePaymentAllocationOperation implements JavaService2 {
    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();
        PaymentAllocationResource requestResource = DBPAPIAbstractFactoryImpl.getResource(PaymentAllocationResource.class);

        PaymentAllocationDTO inputDto;
        List<PaymentAllocationDTO> results = new LinkedList<>();
        JSONArray inputRecords = new JSONArray(request.getParameter("records"));
        for (Object obj : inputRecords) {
            try {
                inputDto = JSONUtils.parse(new JSONObject(obj.toString()).toString(), PaymentAllocationDTO.class);
                results.add(requestResource.createPaymentAllocation(inputDto, request));
            } catch (Exception e) {
                inputDto = new PaymentAllocationDTO();
                inputDto.setDbpErrCode(ErrorCodeEnum.ERR_30016.getErrorCodeAsString());
                inputDto.setDbpErrMsg(ErrorCodeEnum.ERR_30016.getErrorMessage());
                results.add(inputDto);
            }
        }
        return JSONToResult.convert(new JSONObject().put("records", results).toString());
    }
}