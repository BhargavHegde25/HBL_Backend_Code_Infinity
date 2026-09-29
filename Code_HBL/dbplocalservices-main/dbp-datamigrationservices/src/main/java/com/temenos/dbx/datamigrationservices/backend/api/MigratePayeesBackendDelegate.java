package com.temenos.dbx.datamigrationservices.backend.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeBackendDTO;

public interface MigratePayeesBackendDelegate extends BackendDelegate {

	P2PPayeeBackendDTO createPayee(P2PPayeeBackendDTO p2pPayeeBackendDTO, Map<String, Object> headerParams,
			DataControllerRequest dcRequest);

}
