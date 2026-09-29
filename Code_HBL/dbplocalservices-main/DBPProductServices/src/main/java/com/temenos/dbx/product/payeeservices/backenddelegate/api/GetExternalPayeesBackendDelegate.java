package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeListDTO;

import java.util.List;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public interface GetExternalPayeesBackendDelegate extends BackendDelegate {
    List<ExternalPayeeListDTO> fetchPayees(Map<String, Object> requestParameters, DataControllerRequest dcRequest);
}
