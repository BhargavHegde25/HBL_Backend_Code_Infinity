/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.dto.RolloverRequestDTO;

/**
 * @author mrunalini.adepu
 *
 */
public interface RolloverRequestBackendDelegate extends BackendDelegate {

	RolloverRequestDTO createRolloverRequest(RolloverRequestDTO inputDto, DataControllerRequest request);

}
