/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;

/**
 * @author mrunalini.adepu
 *
 */
public interface RolloverRequestResource extends Resource{

	Result submitRolloverRequest(RolloverRequestDTO inputDto, DataControllerRequest request);
	
}
