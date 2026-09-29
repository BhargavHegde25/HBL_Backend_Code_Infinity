/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GetExportLetterOfCreditsResource;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetExportLetterOfCreditsResourceImpl implements GetExportLetterOfCreditsResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Result getExportLetterOfCredits(Object[] inputArray, DataControllerRequest request) {
        Result result = new Result();

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId))
            return ErrorCodeEnum.ERR_26014.setErrorCode(new Result());

        GetExportLetterOfCreditsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(GetExportLetterOfCreditsBusinessDelegate.class);
        List<ExportLOCDTO> exportLcList = businessDelegate.getExportLetterOfCredits(request);

        FilterDTO filterDTO = null;
        try {
            Map<String, Object> inputParamsMap = (HashMap<String, Object>) inputArray[1];
            filterDTO = JSONUtils.parse(new JSONObject(inputParamsMap).toString(), FilterDTO.class);
        } catch (IOException e) {
            alert.prepareError("Exception occurred while fetching params: ", e).log();
        }

        try {
            if (StringUtils.isNotBlank(filterDTO.get_filterByParam()) && StringUtils.isNotBlank(filterDTO.get_filterByValue())) {
                exportLcList = TradeFinanceCommonUtils.filterBy(exportLcList, filterDTO.get_filterByParam(), filterDTO.get_filterByValue());
                filterDTO.set_filterByValue("");
                filterDTO.set_filterByParam("");
            }

            exportLcList = filterDTO.filter(exportLcList);
            JSONObject responseObj = new JSONObject();
            responseObj.put("LetterOfExports", exportLcList);
            result = JSONToResult.convert(responseObj.toString());

        } catch (Exception e) {
            result.addErrMsgParam("Failed to fetch the records");
            alert.prepareError("Error occurred while fetching letter of credits from backend").log();
            return result;
        }

        return result;
    }
}
