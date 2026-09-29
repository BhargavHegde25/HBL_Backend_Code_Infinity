package com.temenos.dbx.product.payeeservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author naveen.yerra
 */
public interface GetExternalPayeesResource extends Resource {
    Result fetchPayees(Object[] inputArray, DataControllerRequest dcRequest);
}
