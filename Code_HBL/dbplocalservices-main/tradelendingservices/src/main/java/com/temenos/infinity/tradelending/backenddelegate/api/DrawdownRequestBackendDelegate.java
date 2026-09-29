/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;

public interface DrawdownRequestBackendDelegate extends BackendDelegate {

	DrawdownRequestDTO createDrawdownRequest(DrawdownRequestDTO inputDto, DataControllerRequest request);
}
