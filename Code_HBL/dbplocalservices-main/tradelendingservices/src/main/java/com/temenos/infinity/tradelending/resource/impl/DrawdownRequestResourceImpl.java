/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.resource.impl;

import static com.temenos.infinity.tradelending.utils.TradeLendingCommonUtils.getCurrentDateTimeUTF;

import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.businessdelegate.api.DrawdownRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;
import com.temenos.infinity.tradelending.resource.api.DrawdownRequestResource;

public class DrawdownRequestResourceImpl implements DrawdownRequestResource {

	@Override
	public Result submitDrawdownRequest(DrawdownRequestDTO inputDto, DataControllerRequest request) {
		DrawdownRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DrawdownRequestBusinessDelegate.class);
        

		DrawdownRequestDTO responseDTO;
       
        inputDto.setCreatedDate(getCurrentDateTimeUTF());
        responseDTO = requestBusiness.createDrawdownRequest(inputDto, request);
      
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
	}

	
}
