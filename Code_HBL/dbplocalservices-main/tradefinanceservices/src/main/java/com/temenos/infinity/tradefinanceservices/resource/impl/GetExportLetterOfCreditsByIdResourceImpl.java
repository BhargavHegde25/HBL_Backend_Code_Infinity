/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.infinity.tradefinanceservices.dto.ExportLOCDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.GetExportLetterOfCreditsByIdBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.resource.api.GetExportLetterOfCreditsByIdResource;
import org.json.JSONObject;


public class GetExportLetterOfCreditsByIdResourceImpl implements GetExportLetterOfCreditsByIdResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public Result getExportLetterOfCreditById(Object[] inputArray, DataControllerRequest request) {

        String exportLcId = "";
        if (request.getParameter("lcReferenceNo") != null) {
            exportLcId = request.getParameter("lcReferenceNo");
        } else {
            alert.prepareError("Mandatory fields missing").log();
            return ErrorCodeEnum.ERRTF_29049.setErrorCode(new Result());
        }
        GetExportLetterOfCreditsByIdBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(GetExportLetterOfCreditsByIdBusinessDelegate.class);

        ExportLOCDTO exportLcDTO = businessDelegate.getExportLetterOfCreditById(exportLcId, request);
        return JSONToResult.convert(String.valueOf(new JSONObject().put("ExportLC", new JSONObject(exportLcDTO))));
    }

}
