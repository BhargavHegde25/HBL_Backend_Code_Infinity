/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.backenddelegate.api.RolloverRequestBackendDelegate;
import com.temenos.infinity.tradelending.businessdelegate.api.RolloverRequestBusinessDelegate;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;

/**
 * @author mrunalini.adepu
 *
 */
public class RolloverRequestBusinessDelegateImpl implements RolloverRequestBusinessDelegate {

	@Override
	public RolloverRequestDTO createRolloverRequest(RolloverRequestDTO inputDto, DataControllerRequest request) {
		RolloverRequestBackendDelegate requestBackend = DBPAPIAbstractFactoryImpl.getBackendDelegate(RolloverRequestBackendDelegate.class);
        return requestBackend.createRolloverRequest(inputDto, request);
	}

}
