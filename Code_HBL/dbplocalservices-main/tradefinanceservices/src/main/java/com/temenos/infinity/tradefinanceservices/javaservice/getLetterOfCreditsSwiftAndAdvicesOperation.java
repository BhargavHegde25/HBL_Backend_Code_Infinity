/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.SwiftsAndAdvisesDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditSwiftsAndAdvicesResource;
import com.kony.dbputilities.util.Log4j2Configurator;

public class getLetterOfCreditsSwiftAndAdvicesOperation implements JavaService2{

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		LetterOfCreditSwiftsAndAdvicesResource swiftandadvicesCreateOrder = DBPAPIAbstractFactoryImpl
                .getResource(LetterOfCreditSwiftsAndAdvicesResource.class);
        Result result =null;
        try {
        	result = swiftandadvicesCreateOrder.getLetterOfCreditSwiftAndAdvices(request);
        }catch(Exception e) {
        	return ErrorCodeEnum.ERRTF_29069.setErrorCode(new Result());
        }
        return result;
	}

}
