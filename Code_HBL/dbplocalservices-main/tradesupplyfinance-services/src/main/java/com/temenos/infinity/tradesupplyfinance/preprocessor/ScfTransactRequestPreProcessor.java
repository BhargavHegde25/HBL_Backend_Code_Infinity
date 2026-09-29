/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.preprocessor;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

import java.util.HashMap;

import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getCoreCustomerId;

/**
 * @author k.meiyazhagan
 */
public class ScfTransactRequestPreProcessor implements DataPreProcessor2 {
    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response, Result result) throws Exception {
        inputMap.put("customerId", getCoreCustomerId(request));
        return true;
    }
}
