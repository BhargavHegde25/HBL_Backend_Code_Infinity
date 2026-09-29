/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.GuaranteesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.GuaranteesResource;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Map;

public class UpdateGuaranteeLcByBankOperation implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
                         DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
        GuaranteesResource requestResource = DBPAPIAbstractFactoryImpl.getResource(GuaranteesResource.class);
        try {
            Map<String, Object> inputParams = (Map<String, Object>) inputArray[1];
            GuaranteesDTO requestGuaranteeDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), GuaranteesDTO.class);
            return requestResource.updateGuaranteeLcByBank(requestGuaranteeDTO, request);
        } catch (Exception e) {
            return ErrorCodeEnum.ERRTF_29047.setErrorCode(new Result());
        }
    }
}
