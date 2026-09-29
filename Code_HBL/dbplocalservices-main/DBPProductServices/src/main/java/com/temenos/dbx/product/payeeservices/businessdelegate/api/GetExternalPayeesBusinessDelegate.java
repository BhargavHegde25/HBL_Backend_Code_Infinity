package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeListDTO;

import java.util.List;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public interface GetExternalPayeesBusinessDelegate extends BusinessDelegate {
    List<ExternalPayeeListDTO> fetchPayees(Map<String, Object> requestParameters, DataControllerRequest dcRequest);
}
