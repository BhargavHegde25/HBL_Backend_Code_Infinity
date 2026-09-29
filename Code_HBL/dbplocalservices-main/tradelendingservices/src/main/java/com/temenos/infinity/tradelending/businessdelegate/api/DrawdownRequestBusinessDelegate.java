/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradelending.dto.DrawdownRequestDTO;

public interface DrawdownRequestBusinessDelegate extends BusinessDelegate {

	DrawdownRequestDTO createDrawdownRequest(DrawdownRequestDTO inputDto, DataControllerRequest request);
}
