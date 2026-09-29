/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import java.io.IOException;
import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.dto.ExportLCDrawingsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.ExportLetterOfCreditsDrawingsResource;

public class CreateExportLCDrawingOperation implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        ExportLetterOfCreditsDrawingsResource drawingsResource = DBPAPIAbstractFactoryImpl
                .getResource(ExportLetterOfCreditsDrawingsResource.class);
        ExportLCDrawingsDTO drawingsDto = constructDTO(inputArray, request);
        Result result = new Result();
        if (StringUtils.isEmpty(drawingsDto.getErrorMessage()))
            result = drawingsResource.createExportDrawing(drawingsDto, request);
        else
            result.addErrMsgParam(drawingsDto.getErrorMessage());

        return result;
    }

    public static ExportLCDrawingsDTO constructDTO(Object[] inputArray, DataControllerRequest request) {
        ExportLCDrawingsDTO exportLCDrawing = new ExportLCDrawingsDTO();

        @SuppressWarnings("unchecked")
        HashMap<String, Object> requestParameters = (HashMap<String, Object>) inputArray[1];

        try {
            ExportLCDrawingsDTO parseInput = JSONUtils.parse(new JSONObject(requestParameters).toString(), ExportLCDrawingsDTO.class);
            if (StringUtils.isNotEmpty(parseInput.getDrawingReferenceNo()))
                parseInput.setDrawingSRMSRequestId(parseInput.getDrawingReferenceNo());
            return parseInput;
        } catch (IOException e) {
            exportLCDrawing.setErrorMessage("Error occurred while parsing the input.");
        }

        return exportLCDrawing;
    }

}
