/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;

public interface RolloverRequestBusinessDelegate extends BusinessDelegate {

	RolloverRequestDTO createRolloverRequest(RolloverRequestDTO inputDto, DataControllerRequest request);
}
