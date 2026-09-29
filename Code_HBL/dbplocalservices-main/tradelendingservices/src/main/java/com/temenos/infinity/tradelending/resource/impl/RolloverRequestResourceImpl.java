/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.resource.impl;

import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.businessdelegate.api.RolloverRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;
import com.temenos.infinity.tradelending.resource.api.RolloverRequestResource;
import static com.temenos.infinity.tradelending.utils.TradeLendingCommonUtils.getCurrentDateTimeUTF;

/**
 * @author mrunalini.adepu
 *
 */
public class RolloverRequestResourceImpl implements RolloverRequestResource {

	@Override
	public Result submitRolloverRequest(RolloverRequestDTO inputDto, DataControllerRequest request) {
		RolloverRequestBusinessDelegate requestBusiness = DBPAPIAbstractFactoryImpl.getBusinessDelegate(RolloverRequestBusinessDelegate.class);
        

        RolloverRequestDTO responseDTO;
       
        inputDto.setCreatedDate(getCurrentDateTimeUTF());
        responseDTO = requestBusiness.createRolloverRequest(inputDto, request);
      
        return JSONToResult.convert(String.valueOf(new JSONObject(responseDTO)));
	}

}
