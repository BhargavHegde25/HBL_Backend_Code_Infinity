package com.temenos.infinity.api.chequemanagement.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.chequemanagement.dto.ChequeBook;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public interface GetCheuqeBookResource extends Resource {

    Result getChequeBookRequests(ChequeBook chequeBook, DataControllerRequest request);

} 