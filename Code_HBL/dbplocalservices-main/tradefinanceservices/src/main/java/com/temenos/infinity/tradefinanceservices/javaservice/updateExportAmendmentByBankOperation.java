/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.ExportLCAmendmentBackendDelegate;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCAmendmentsDTO;
import org.apache.commons.lang3.StringUtils;
import com.kony.dbputilities.util.Log4j2Configurator;

public class updateExportAmendmentByBankOperation implements JavaService2 {
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest request, DataControllerResponse dataControllerResponse) throws Exception {
		Log4j2Configurator.getInstance();
        String amendmentId = request.getParameter("amendmentSRMSRequestId");
        Result result = new Result();
        if (StringUtils.isBlank(amendmentId)) {
            result.addErrMsgParam("Srms id not found");
        }
        ExportLCAmendmentBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(ExportLCAmendmentBackendDelegate.class);
        ExportLCAmendmentsDTO amendmentsDTO = requestBackend.getExportLCAmendmentById(amendmentId, request);
        try {
            if (amendmentsDTO.getSelfAcceptance().equalsIgnoreCase("Accepted")) {
                amendmentsDTO.setAmendmentStatus("Amended");
                requestBackend.updateExportLCAmendment(amendmentsDTO, request);
                result.addParam("Message", "Successfully amended");
                return result;
            } else {
                result.addErrMsgParam("Requested amendment is not available to update");
            }
        } catch (Exception e) {
            result.addErrMsgParam("Requested amendment cannot be updated");
        }
        return result;
    }
}
