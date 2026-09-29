/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.DrawdownRequestBackendDelegate;
import com.temenos.infinity.tradelending.businessdelegate.api.DrawdownRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;

public class DrawdownRequestBusinessDelegateImpl implements DrawdownRequestBusinessDelegate {
	
	@Override
	public DrawdownRequestDTO createDrawdownRequest(DrawdownRequestDTO inputDto, DataControllerRequest request) {
		DrawdownRequestBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(DrawdownRequestBackendDelegate.class);
        return requestBackend.createDrawdownRequest(inputDto, request);
	}

	

}
